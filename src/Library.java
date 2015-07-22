
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author Victor Anuebunwa
 */
public class Library {

    private static Library thisClass;
    private volatile ArrayList<Material> materials;
    private final ArrayList<ProgressListener> listeners;
    private boolean busy;
    private int totalFiles;

    private Library() {
        materials = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    /**
     * Load materials from library locations<br/>
     * This might be a lengthy operation. therfore It is advisable no to invoke
     * this method on the Event dispatch thread.
     *
     */
    public synchronized void init() {
        if (busy) {
            throw new IllegalStateException("Library is busy");
        }
        busy = true;
        loadLocalLibrary();
        syncLibrary();
        materials.sort((Material o1, Material o2) -> o1.getTitle().compareTo(o2.getTitle()));
        busy = false;
    }

    public ArrayList<Material> searchLibrary(String query) {
        if (busy || materials == null) {
            throw new IllegalStateException("Library is busy");
        }

        if (query.isEmpty()) {
            return materials;
        }

        ArrayList<Material> list = new ArrayList<>();
        String[] names;
        if (query.contains(" ")) {
            names = query.split("\\s");
        } else {
            names = new String[]{query};
        }
        materials.stream().forEach((material) -> {
            boolean found = false;
            for (String keyname : names) {
                //Search attributes for match
                keyname = keyname.toLowerCase();
                if (material.getTitle() != null) {
                    found = found || material.getTitle().toLowerCase().contains(keyname);
                }
                if (material.getAuthor() != null) {
                    found = found || material.getAuthor().toLowerCase().contains(keyname);
                }
                if (material.getISBN() != null) {
                    found = found || material.getISBN().toLowerCase().contains(keyname);
                }
            }
            if (found) {
                list.add(material);
            }
        });

        return list;
    }

    public boolean ready() {
        return !busy;
    }

    public synchronized void addLocation(URI path) throws SQLException, IOException {
        //Save current Changes
        update();
        //Add Location
        DatabaseImpl.addLibraryLocation(path);
        DatabaseImpl.getConnection().commit();
        //Reload
        init();
    }

    public synchronized void removeLocation(URI path) throws SQLException, IOException {
        //Save current Changes
        update();
        //Remove location
        DatabaseImpl.deleteLibraryLocation(path);
        DatabaseImpl.getConnection().commit();
        //Reload
        init();
    }

    public boolean addProgressListener(ProgressListener listener) {
        return listeners.add(listener);
    }

    public boolean removeProgressListener(ProgressListener listener) {
        return listeners.remove(listener);
    }

    private void loadLocalLibrary() {
        try {
            String[] libraryLocations = DatabaseImpl.getLibraryLocations();

            totalFiles = 0;
            for (String libraryLocation : libraryLocations) {
                totalFiles += Utility.countFiles(new File(new URI(libraryLocation)));
            }
            //trigger
            listeners.stream().forEach((listener) -> {
                listener.onStart("Loading " + totalFiles + " files");
            });

            materials.clear();

            for (String libraryLocation : libraryLocations) {
                addFiles(new File(new URI(libraryLocation)));
            }

            //trigger
            listeners.stream().forEach((listener) -> {
                listener.onStop("Done");
            });
        } catch (SQLException | URISyntaxException ex) {
            Utility.writeLog(ex);
        }

    }

    private void addFiles(File dir) {
        if (dir != null) {
            File[] listFiles = dir.listFiles((File pathname) -> {
                return pathname.isDirectory() || pathname.getName()
                        .matches("[\\s\\S]*" + Utility.getAllowedMaterialsExtensions());
            });

            for (File file : listFiles) {
                if (file.isFile()) {
                    try (FileInputStream is = new FileInputStream(file)) {
                        //File
                        Material material = new Material(file.toURI());
                        material.setAuthor("Unknown Author");
                        String name = file.getName();
                        int stop = name.lastIndexOf('.');
                        material.setTitle(name.substring(0, stop)); //Remove extension
                        material.setExcluded(false);
                        material.setKeywords(new String[]{});
                        material.setDateAdded(Date.from(Instant.now()));
                        material.setLastModificationTime(new Date(0L));

                        //Setting type
                        MaterialType type;
                        if (file.getName().matches("[\\s\\S]*" + Utility.getAllowedCDExtentions())) {
                            type = MaterialType.CD;
                        } else {
                            type = MaterialType.EBOOK;
                        }
                        material.setType(type);
                        if (type.equals(MaterialType.EBOOK)) {
                            //Read content using Tika
                            // **** Tika specific-stuff.
                            ContentHandler contenthandler = new BodyContentHandler(1000); //Max Write of 1000
                            Metadata metadata = new Metadata();
                            ParseContext context = new ParseContext();
                            Parser parser = new AutoDetectParser();
                            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
                            // OOXMLParser parser = new OOXMLParser();
                            try {
                                parser.parse(is, contenthandler, metadata, context);
                            } catch (IOException | SAXException | TikaException ex) {
//                                Utility.writeLog(ex);
                            }
                            // **** End Tika-specific
                            material.setPreview(contenthandler.toString());

                        } else {
                            material.setPreview("");
                        }
                        //Add material
                        materials.add(material);

                        //trigger
                        int progress = (int) (((double) materials.size() * 100) / totalFiles);
                        listeners.stream().forEach((listener) -> {
                            listener.update(material.getTitle(), progress);
                        });
                    } catch (IOException ex) {
                        Utility.writeLog(ex);
                    }
                } else {
                    //Directory
                    addFiles(file);
                }
            }
        }
    }

    public static Library getInstance() {
        if (thisClass == null) {
            thisClass = new Library();
        }
        return thisClass;
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }

    public Material getMaterial(URI path) {
        for (Material material : materials) {
            if (material.getPath().equals(path)) {
                return material;
            }
        }
        return null;
    }

    public void addMaterial(Material material) {
        materials.add(material);
    }

    /**
     * Updates database and indexes with materials Existing indexes will be
     * replaced
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public void update() throws SQLException, IOException {
        for (Material material : materials) {
            material.update();
        }
    }

    /**
     * Synchronises local library with database library
     */
    private void syncLibrary() {
        listeners.stream().forEach((listener) -> {
            listener.onStart("Synchronizing files");
        });

        int size = materials.size();
        for (int i = 0; i < size; i++) {
            Material material = materials.get(i);
            try {
                Material dbMaterial = DatabaseImpl.getMaterial(material.getPath());
                if (dbMaterial != null) {
                    if (!material.isSynced()
                            || dbMaterial.getLastModificationTime()
                            .after(material.getLastModificationTime())) {
                        //Download
                        material.setAuthor(dbMaterial.getAuthor());
                        material.setExcluded(dbMaterial.isExcluded());
                        material.setDateAdded(dbMaterial.getDateAdded());
                        material.setKeywords(dbMaterial.getKeywords());
                        material.setPreview(dbMaterial.getPreview());
                        material.setTitle(dbMaterial.getTitle());
                        material.setType(dbMaterial.getType());
                        //Equate modification time of both materials.
                        //This should be the last property to be set
                        material.setLastModificationTime(dbMaterial.getLastModificationTime());
                        material.setSynced(true);
                        material.suppressNotedChanges(); //Ignore this changes to avoid re-indexing
                    } else {
                        //Do nothing
                        //Local version is the same or newer
                    }
                } else {
                    /*
                     No match found
                     Material may not have been added to database yet.
                     To add material call Material#update()
                     */
                }
            } catch (SQLException ex) {
                Utility.writeLog(ex);
            }

            int progress = (int) ((float) i / size) * 100;
            listeners.stream().forEach((listener) -> {
                listener.update("Synchronizing files...", progress);
            });
        }

        listeners.stream().forEach((listener) -> {
            listener.update("Removing obsolete materials from database", 100);
        });

        DatabaseImpl.removeObsoleteMaterials();

        try {
            update();
        } catch (SQLException | IOException ex) {
            Utility.writeLog(ex);
        }

        listeners.stream().forEach((listener) -> {
            listener.onStop("Done.");
        });
    }
}
