
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.lucene.analysis.util.CharArraySet;

/**
 *
 * @author Admin
 */
public class DatabaseImpl {

    private static Connection c;

    public static Connection getConnection() throws SQLException {
        if (c == null || c.isClosed()) {
            c = DriverManager.getConnection("jdbc:mysql://localhost/library",
                    "root",
                    "");
            c.setAutoCommit(false);
        }
        return c;
    }

    public static CharArraySet getStopWords() throws SQLException {
        Statement s = getConnection().createStatement();
        ResultSet rs = s.executeQuery("select word from stopwords");
        rs.last();
        int rowSize = rs.getRow();
        CharArraySet array = new CharArraySet(rowSize, true);
        rs.beforeFirst();
        while (rs.next()) {
            array.add(rs.getString(1));
        }
        return array;
    }

    /**
     *
     * @return Library folder location
     * @throws java.sql.SQLException
     */
    public static String[] getLibraryLocations() throws SQLException {
        Statement s = getConnection().createStatement();
        ResultSet rs = s.executeQuery("select * from library_locations limit 3");
        rs.last();
        String[] loc = new String[rs.getRow()];
        rs.beforeFirst();
        int count = 0;
        while (rs.next()) {
            loc[count++] = rs.getString("path");
        }
        return loc;

    }

    /**
     *
     * @param location
     * @return
     * @throws java.sql.SQLException
     */
    public static boolean addLibraryLocation(URI location) throws SQLException {
        Statement s = getConnection().createStatement();
        ResultSet rs = s.executeQuery("select * from library_locations where path = '" + location + "'");
        if (rs.next()) {
            return false; //location already added
        } else {
            s.executeUpdate("insert into library_locations set path = '" + location + "'");
            return true;
        }
    }

    /**
     *
     * @param location
     * @return
     * @throws java.sql.SQLException
     */
    public static boolean deleteLibraryLocation(URI location) throws SQLException {
        Statement s = getConnection().createStatement();
        int rows = s.executeUpdate("delete from library_locations where path = '" + location + "'");
        return rows > 0;
    }

    /**
     * Get a material from library
     *
     * @param path
     * @return material from database
     * @throws java.sql.SQLException
     */
    public static Material getMaterial(URI path) throws SQLException {
        if (path != null) {
            PreparedStatement s = getConnection()
                    .prepareStatement("select * from materials where path = ?");
            s.setString(1, path.toString());
            s.execute();
            ResultSet rs = s.getResultSet();
            if (rs.next()) {
                //Create material
                Material m = new Material(path);
                m.setAuthor(rs.getString("author"));
                m.setDateAdded(rs.getTimestamp("date_added"));
                m.setPreview(rs.getString("preview"));
                m.setPath(path);
                m.setTitle(rs.getString("title"));
                m.setKeywords(rs.getString("keywords").split(","));
                MaterialType type = MaterialType.valueOf(rs.getString("type"));
                m.setType(type);
                m.setExcluded(rs.getBoolean("is_excluded"));
                m.setLastModificationTime(rs.getTimestamp("last_modification_time"));
                return m;
            }
            return null;
        } else {
            throw new IllegalArgumentException("Path is null");
        }
    }

    /**
     *
     * @param material
     * @throws java.sql.SQLException
     */
    public static void updateMaterial(Material material) throws SQLException {
        if (material != null) {
            PreparedStatement s;
            if (material.isSynced()) {
                //Exists in database
                //Updating existing content
                String query = "update materials set "
                        + "author = ?, "
                        + "title = ?, "
                        + "preview = ?, "
                        + "type = ?, "
                        + "is_excluded = ?, "
                        + "date_added = ?, "
                        + "last_modification_time = now(), "
                        + "keywords = ? "
                        + "where path = ?";
                s = getConnection().prepareStatement(query);
                s.setString(1, material.getAuthor());
                s.setString(2, material.getTitle());
                s.setString(3, material.getPreview());
                s.setString(4, material.getType().name());
                s.setString(5, (material.isExcluded() ? "1" : "0"));
                s.setDate(6, new Date(material.getDateAdded().getTime()));
                s.setString(7, String.join(",", material.getKeywords()));
                s.setString(8, material.getPath().toString());
            } else {
                //Does not exist in database
                //Inserting new row
                String query = "insert into materials set "
                        + "path = ?, "
                        + "author = ?, "
                        + "title = ?, "
                        + "preview = ?, "
                        + "type = ?, "
                        + "is_excluded = ?, "
                        + "date_added = ?, "
                        + "last_modification_time = now(), "
                        + "keywords = ?";
                s = getConnection().prepareStatement(query);
                s.setString(1, material.getPath().toString());
                s.setString(2, material.getAuthor());
                s.setString(3, material.getTitle());
                s.setString(4, material.getPreview());
                s.setString(5, material.getType().name());
                s.setString(6, (material.isExcluded() ? "1" : "0"));
                s.setDate(7, new Date(material.getDateAdded().getTime()));
                s.setString(8, String.join(",", material.getKeywords()));
            }
            s.execute();
        }
    }

    /**
     * Delete materials from database that are no longer in any Library location
     */
    
    //Bug: The method does not properly implement the documentation above, fix it
    public static void removeObsoleteMaterials() {
        try {
            Statement s = getConnection().createStatement();
            ResultSet rs = s.executeQuery("select * from materials");

            while (rs.next()) {
                try {
                    File f = new File(new URI(rs.getString("path")));
                    if (!Files.exists(f.toPath())) {
                        deleteMaterial(rs.getString("path"));
                    }
                } catch (URISyntaxException ex) {
                    //Path could not be resolved
                    deleteMaterial(rs.getString("path"));
                }
            }
        } catch (SQLException ex) {
            Utility.writeLog(ex);
        }
    }

    private static boolean deleteMaterial(String path) {
        try {
            Statement s = getConnection().createStatement();
            s.executeUpdate("delete from materials where path = '" + path + "'");
            return true;
        } catch (SQLException ex) {
            Utility.writeLog(ex);
            return false;
        }
    }

    public static void saveIndexFile(File file) throws SQLException {
        //Insert file; on duplicate, update
        if (file != null && file.exists()) {
            try (InputStream is = new FileInputStream(file)) {
                PreparedStatement ps = getConnection()
                        .prepareStatement("insert into index_files set "
                                + "file_name = ?, "
                                + "content = ?, "
                                + "last_modified = now() "
                                + "on duplicate key "
                                + "update content = ?, "
                                + "last_modified = now()");
                ps.setString(1, file.getName());
                ps.setBinaryStream(2, is);
                ps.setBinaryStream(3, is);
                ps.execute();
            } catch (IOException ex) {
                Utility.writeLog(ex);
            }
        } else {
            throw new IllegalArgumentException("File is invalid");
        }

    }

    public static boolean clearIndexFiles() {
        try {
            Statement s = getConnection().createStatement();
            s.executeUpdate("truncate table index_files");
            return true;
        } catch (SQLException ex) {
            Utility.writeLog(ex);
            return false;
        }
    }

}
