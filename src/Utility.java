
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Admin
 */
public class Utility {

    private static ExecutorService executor;
    private final static Color PLACEHOLDER_COLOR = Color.lightGray;

    public static ExecutorService getExecutorService() {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }

    public static String crypt(String message) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("sha-1");
        md.update(message.getBytes());
        byte[] digest = md.digest();
        return new BigInteger(digest).toString(16);
    }

    public static void writeLog(Throwable e) {
        Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
    }

    public static void centreOnParent(Window parent, Window child) {
        int x = parent.getLocationOnScreen().x + (parent.getWidth() - child.getWidth()) / 2;
        int y = parent.getLocationOnScreen().y + (parent.getHeight() - child.getHeight()) / 2;
        child.setLocation(x, y);
        child.setIconImages(parent.getIconImages());
    }

    public static void centreOnScreen(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2.0D);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2.0D);
        frame.setLocation(x, y);
    }

    /**
     * Provide regular expressions that match allowed eBook formats
     *
     * @return
     */
    public static String getAllowedEBookExtentions() {
        return "((.[Dd][Oo][Cc][Xx]?)|(.[Pp][Dd][Ff])|(.[Tt][Xx][Tt])|(.[Pp]{2}[Tt][Xx]?))";
    }

    /**
     * Provide regular expressions that match allowed music and video formats
     *
     * @return
     */
    public static String getAllowedCDExtentions() {
        return "((.[Mm][Pp][34])|(.[Ff][Ll][Vv])|(.[Aa][Vv][Ii]))";
    }

    /**
     * Provide regular expressions that match all allowed formats for library
     * materials
     *
     * @return
     */
    public static String getAllowedMaterialsExtensions() {
        return "(" + getAllowedEBookExtentions() + "|" + getAllowedCDExtentions() + ")";
    }

    /**
     *
     * @return Index folder location
     */
    public static String getIndexFolderLocation() {
        return "indexer_files"; //root folder
    }

    public static int countFiles(File file) {
        int total = 0;
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles((File pathname) -> {
                return pathname.isDirectory() || pathname.getName()
                        .matches("[\\s\\S]*" + Utility.getAllowedMaterialsExtensions());
            });
            for (File listFile : listFiles) {
                if (listFile != null) {
                    total += countFiles(listFile);
                }
            }
        } else if (file.isFile()) {
            total += 1;
        }
        return total;
    }

    public static boolean isPlaceholderShowing(JTextComponent c) {
        return c.getForeground().equals(PLACEHOLDER_COLOR);
    }

    public static void setPlaceholder(JTextComponent c, String placeholder) {
        if (c.getText().trim().isEmpty()) {
            c.setText(placeholder);
            c.setForeground(PLACEHOLDER_COLOR);
        }

        c.addFocusListener(new FocusListener() {

            private boolean isPlaceholderShowing() {
                return c.getText().equalsIgnoreCase(placeholder)
                        && c.getForeground().equals(PLACEHOLDER_COLOR);
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholderShowing()) {
                    c.setText("");
                    c.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (c.getText().trim().isEmpty()) {
                    c.setText(placeholder);
                    c.setForeground(PLACEHOLDER_COLOR);
                }
            }
        });
    }

    public static void saveChanges() {
        try {
            //Upload library to database
            Library.getInstance().update();

            //Upload indexes
            Indexer.getInstance().saveIndexesToDatabase();
        } catch (SQLException | IOException ex) {
            Utility.writeLog(ex);
        }
    }
}
