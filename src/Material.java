
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Victor Anuebunwa
 */
public class Material implements Comparable<Material> {

    private MaterialType type;
    private boolean excluded;
    private String preview;
    private String title;
    private String ISBN;
    private URI path;
    private String author;
    private String[] keywords;
    private Date dateAdded;
    private Date lastModificationTime;
    private boolean modified;
    private boolean synced;

    /**
     * Convenient for creating a material from local library
     *
     * @param path
     */
    public Material(URI path) {
        this.path = path;
        this.ISBN = "";
    }

    /**
     *
     * @return true if material has been synchronised with copy in database,
     * false otherwise
     */
    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public MaterialType getType() {
        return type;
    }

    public void setExcluded(boolean excluded) {
        this.excluded = excluded;
        noteChanges();
    }

    public boolean isExcluded() {
        return excluded;
    }

    public String getPreview() {
        return preview;
    }

    public String getTitle() {
        return title;
    }

    public URI getPath() {
        return path;
    }

    public String getAuthor() {
        return author;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getLastModificationTime() {
        return lastModificationTime;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
        noteChanges();
    }

    public void setLastModificationTime(Date lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
        modified = true;
    }

    public void setType(MaterialType type) {
        this.type = type;
        noteChanges();
    }

    public void setPreview(String preview) {
        this.preview = preview;
        noteChanges();
    }

    public void setTitle(String title) {
        this.title = title;
        noteChanges();
    }

    public void setPath(URI path) {
        this.path = path;
        noteChanges();
    }

    public void setAuthor(String author) {
        this.author = author;
        noteChanges();
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
        noteChanges();
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
        noteChanges();
    }

    private void noteChanges() {
        lastModificationTime = Date.from(Instant.now());
        modified = true;
    }

    /**
     * This causes this material to ignore noting previous changes.<br/>
     * This can be used to prevent this material from being re-indexed
     */
    public void suppressNotedChanges() {
        this.modified = false;
    }

    /**
     * Update database and Index to reflect updates to this material
     *
     * @throws java.sql.SQLException if saveIndexesToDatabase failed
     * @throws java.io.IOException
     */
    public void update() throws SQLException, IOException {
        if (modified && !excluded) {
            try {
                //Update database
                DatabaseImpl.updateMaterial(this);

                //Update index
                Indexer.getInstance().runOnMaterial(path);
                synced = true;
                modified = false;
                DatabaseImpl.getConnection().commit();
            } catch (SQLException | IOException ex) {
                DatabaseImpl.getConnection().rollback();
                throw ex;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Material) {
            Material otherMaterial = (Material) obj;
            return this.path.equals(otherMaterial.getPath());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.path);
        return hash;
    }

    @Override
    public String toString() {
        if (title == null) {
            title = "";
        }
        return title + ((author == null || author.isEmpty()) ? "" : " by " + author);
    }

    @Override
    public int compareTo(Material otherMaterial) {
        if (title == null) {
            title = "";
        }
        return title.compareTo(otherMaterial.title);
    }

}
