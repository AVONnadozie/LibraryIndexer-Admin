
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Victor Anuebunwa
 */
public class Admin {

    private static Admin thisClass;

    private boolean loggedIn;

    private String password;

    private Admin() {
        try {
            password = getPasswordFromFile();
        } catch (IOException ex) {
            password = "";
        }
    }

    public static Admin getInstance() {
        if (thisClass == null) {
            thisClass = new Admin();
        }
        return thisClass;
    }

    public boolean changePassword(String oldPassword, String newPassword) throws NoSuchAlgorithmException {
        if (login(oldPassword)) {
            try {
                register(newPassword);
                return true;
            } catch (IOException | IllegalArgumentException ex) {
                Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public boolean isSet() {
        return password != null && !password.isEmpty();
    }

    public void setAdmin(String password, String... answers) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    private String getPasswordFromFile() throws IOException {
        String p;
        File passwordFile = new File(System.getProperty("user.dir"), "pwordlogger.dat");
        try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
            p = br.readLine();
        }
        return p;
    }

    private void savePasswordToFile(String password) throws IOException {
        File passwordFile = new File(System.getProperty("user.dir"), "pwordlogger.dat");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(passwordFile, false))) {
            bw.write(password);
        }
    }

    public boolean login(String password) throws NoSuchAlgorithmException, IllegalArgumentException {
        if (password != null && !password.isEmpty()) {
            password = Utility.crypt(password);
            boolean ok = password.equalsIgnoreCase(getPassword());
            setLoggedIn(ok);
            return ok;
        } else {
            throw new IllegalArgumentException("Invalid Password");
        }
    }

    public void register(String password) throws IOException, NoSuchAlgorithmException, IllegalArgumentException {
        if (password != null && !password.isEmpty()) {
            savePasswordToFile(Utility.crypt(password));
            this.password = password;
            setLoggedIn(true);
        } else {
            throw new IllegalArgumentException("Invalid Password");
        }
    }

}
