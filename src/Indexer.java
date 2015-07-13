
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
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
public class Indexer {

    private static Indexer thisClass;
    private boolean modified;
    private final Library library;
    private final File indexDir;
    private final ArrayList<ProgressListener> listeners;
    private final StandardAnalyzer analysis;
    private IndexWriter idx;
    private FSDirectory dir;

    private Indexer(Library library) throws IOException, SQLException {
        this.library = library;
        listeners = new ArrayList<>();
        indexDir = new File(Utility.getIndexFolderLocation());
        analysis = new StandardAnalyzer(DatabaseImpl.getStopWords());
    }

    public boolean addProgressListener(ProgressListener listener) {
        return listeners.add(listener);
    }

    public boolean removeProgressListener(ProgressListener listener) {
        return listeners.remove(listener);
    }

    /**
     * Returns an instance of this indexer.<br/>
     * Instance of this class is created only if no other instance exists
     *
     * @return Indexer
     * @throws IOException if error occurred while opening index directory
     * @throws SQLException if database error occurred
     */
    public static Indexer getInstance() throws IOException, SQLException {
        if (thisClass == null) {
            thisClass = new Indexer(Library.getInstance());
        }
        return thisClass;
    }

    /**
     * Runs indexer on the material in library mapped to this path
     *
     * @param path
     * @throws IOException
     * @throws SQLException
     */
    public void runOnMaterial(URI path) throws IOException, SQLException {
        //Make sure the material is in the Library
        runOnMaterial(library.getMaterial(path));
    }

    private void runOnMaterial(Material material) throws IOException, SQLException {
        if (material != null) {
            if (idx == null) {
                dir = FSDirectory.open(indexDir); // Store the index on disk
                idx = new IndexWriter(dir, new IndexWriterConfig(Version.LATEST, analysis));
            }

            Document document = getDocument(material);
            if (material.isSynced()) {
                idx.updateDocument(new Term(IndexFields.PATH.name(), material.getPath().toString()),
                        document);
            } else {
                idx.addDocument(document);
            }
            modified = true;
        }
    }

    /**
     * Saves Lucene index files to database, this overwrites existing ones in
     * database
     *
     * @return
     * @throws SQLException
     */
    public boolean saveIndexesToDatabase() throws SQLException {
        if (modified) {
            //Upload indexes to database

            File[] listFiles = indexDir.listFiles((File pathname) -> {
                //Accept all files except .lock files
                return pathname.isFile() && !pathname.getName()
                        .matches("[\\s\\S]*.[Ll][Oo][Cc][Kk]");
            });

            try {
                if (listFiles.length > 0 && DatabaseImpl.clearIndexFiles()) {
                    for (File file : listFiles) {
                        DatabaseImpl.saveIndexFile(file);
                    }
                    DatabaseImpl.getConnection().commit();
                    modified = false;
                    return true;
                } else {
                    DatabaseImpl.getConnection().rollback();
                    return false;
                }
            } catch (SQLException ex) {
                DatabaseImpl.getConnection().rollback();
                Utility.writeLog(ex);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Runs indexer on the available library object<br/>
     * This might be a lengthy operation. therfore It is advisable no to invoke
     * this method on the Event dispatch thread.
     *
     *
     * @throws java.io.IOException
     */
    public void run() throws IOException {
        if (library != null && library.ready()) {
            if (idx == null) {
                dir = FSDirectory.open(indexDir); // Store the index on disk
                idx = new IndexWriter(dir, new IndexWriterConfig(Version.LATEST, analysis));
            }

            ArrayList<Material> materials = library.getMaterials();
            int noOfMaterials = materials.size();

            //Trigger listeners
            listeners.stream().forEach((listener) -> {
                listener.onStart("Indexing " + noOfMaterials + " materials");
            });

            try {
                for (int i = 0; i < noOfMaterials; i++) {
                    Document doc = getDocument(materials.get(i));
                    idx.addDocument(doc);
                    modified = true;
                    updateProgress(i + 1, noOfMaterials);
                }
            } catch (IOException | AssertionError ex) {
                Utility.writeLog(ex);
            }

            //Trigger listeners
            listeners.stream().forEach((listener) -> {
                listener.onStop("Done");
            });
        }
    }

    private void updateProgress(int indexedFiles, int totalSize) {
        if (listeners != null) {
            int progress = (int) ((double) indexedFiles / totalSize * 100);
            String message;
            if (progress >= 95) {
                message = "And...";
            } else if (progress >= 80) {
                message = "Just a little more...";
            } else if (progress >= 50) {
                message = "Half way there...";
            } else if (progress >= 30) {
                message = "Extracting contents...";
            } else if (progress >= 20) {
                message = "Don't worry, you might only need to do this once.";
            } else {
                message = "This might take a while depending on the size of your libraries.";
            }

            //Trigger listeners
            listeners.stream().forEach((listener) -> {
                listener.update(message, progress);
            });
        }
    }

    private Document getDocument(Material material) throws IOException, AssertionError {
        Document doc = new Document();
        try (FileInputStream is = new FileInputStream(new File(material.getPath()))) {
            ContentHandler contenthandler = null;
            if (material.getType().equals(MaterialType.EBOOK)) { //Index contents only in EBOOKS
                // **** Tika specific-stuff.
                contenthandler = new BodyContentHandler(Integer.MAX_VALUE);
                Metadata metadata = new Metadata();
                ParseContext context = new ParseContext();
                // OOXMLParser parser = new OOXMLParser();
                Parser parser = new AutoDetectParser();
                metadata.set(Metadata.RESOURCE_NAME_KEY, material.getTitle());

                try {
                    parser.parse(is, contenthandler, metadata, context);
                } catch (IOException | SAXException | TikaException | AssertionError ex) {
                    Utility.writeLog(ex);
                }
                // **** End Tika-specific
            }
            FieldType type = new FieldType();
            type.setTokenized(true);
            type.setIndexed(true);
            type.setStored(true);
            for (IndexFields value : IndexFields.values()) {
                switch (value) {
                    case CONTENT:
                        doc.add(new TextField(value.name(),
                                new StringReader(contenthandler == null ? "" : contenthandler.toString())));
                        break;
                    case TITLE:
                        doc.add(new Field(value.name(), material.getTitle(), type));
                        break;
                    case PATH:
                        doc.add(new Field(value.name(), material.getPath().toString(), type));
                        break;
                    case AUTHOR:
                        doc.add(new Field(value.name(), material.getAuthor(), type));
                        break;
                    case KEYWORDS:
                        doc.add(new Field(value.name(), String.join(", ", material.getKeywords()), type));
                        break;
                    case CREATION_DATE:
                        doc.add(new LongField(value.name(),
                                material.getDateAdded().getTime(),
                                Field.Store.YES));
                        break;
                    case TYPE:
                        doc.add(new StringField(value.name(), material.getType().name(), Field.Store.YES));
                        break;
                    default:
                        throw new AssertionError(value.name());
                }
            }
        }
        return doc;
    }
}
