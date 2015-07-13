
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class SettingsPanel extends javax.swing.JPanel {

    private File lastAccessedDir = new File(System.getProperty("user.home"));
    private List<String> paths;
    private final Timer getLocationTimer;

    public SettingsPanel() {
        initComponents();
        indexProgressPanel.setVisible(false);
        libraryLocation1Panel.setVisible(false);
        libraryLocation2Panel.setVisible(false);
        libraryLocation3Panel.setVisible(false);
        addLocationButton.setEnabled(false);
        runIndexerButton.setEnabled(false);

        ProgressListener indexerListener = new ProgressListener() {

            @Override
            public void onStart(String message) {
                indexProgressPanel.setVisible(true);
                jLabel2.setVisible(true);
                jLabel3.setVisible(true);
                jProgressBar1.setIndeterminate(false);

                jLabel2.setText(message);

                enableVolatileActions(false);
            }

            @Override
            public void update(String message, int progress) {
                jProgressBar1.setValue(progress);
                jLabel3.setText(message);
            }

            @Override
            public void onStop(String message) {
                indexProgressPanel.setVisible(false);

                enableVolatileActions(true);
            }
        };

        try {
            Indexer indexer = Indexer.getInstance();
            Library library = Library.getInstance();

            indexer.addProgressListener(indexerListener);
            library.addProgressListener(indexerListener);
        } catch (IOException | SQLException ex) {
            Utility.writeLog(ex);
        }

        //Load info
        getLocationTimer = new Timer(0, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] libraryLocations = DatabaseImpl.getLibraryLocations();
                    paths = new ArrayList<>(Arrays.asList(libraryLocations));

                    refreshDisplay();

                    getLocationTimer.stop();
                } catch (SQLException ex) {
                    Utility.writeLog(ex);
                }
            }
        });
        getLocationTimer.setDelay(1000); //10secs
        getLocationTimer.setRepeats(true);
        getLocationTimer.start();

    }

    private void enableVolatileActions(boolean status) {
        runIndexerButton.setEnabled(status);
        addLocationButton.setEnabled(status);
        deleteLibraryButton1.setEnabled(status);
        deleteLibraryButton2.setEnabled(status);
        deleteLibraryButton3.setEnabled(status);
    }

    private void refreshDisplay() {
        int total = paths.size();

        if (total >= 1) {
            try {
                File file = new File(new URI(paths.get(0)));
                libraryLocation1Panel.setVisible(true);
                libraryLocation1PathLabel.setText(file.toString());
                libraryLocation1ItemsLabel.setText(String.valueOf(Utility.countFiles(file)) + " materials found");
            } catch (URISyntaxException ex) {
                Utility.writeLog(ex);
            }
        } else {
            libraryLocation1Panel.setVisible(false);
        }

        if (total >= 2) {
            try {
                File file = new File(new URI(paths.get(1)));
                libraryLocation2Panel.setVisible(true);
                libraryLocation2PathLabel.setText(file.toString());
                libraryLocation2ItemsLabel.setText(String.valueOf(Utility.countFiles(file)) + " materials found");
            } catch (URISyntaxException ex) {
                Utility.writeLog(ex);
            }
        } else {
            libraryLocation2Panel.setVisible(false);
        }

        if (total >= 3) {
            try {
                File file = new File(new URI(paths.get(2)));
                libraryLocation3Panel.setVisible(true);
                libraryLocation3PathLabel.setText(file.toString());
                libraryLocation3ItemsLabel.setText(String.valueOf(Utility.countFiles(file)) + " materials found");
            } catch (URISyntaxException ex) {
                Utility.writeLog(ex);
            }

            addLocationButton.setVisible(false);
        } else {
            libraryLocation3Panel.setVisible(false);
            addLocationButton.setVisible(true);
        }

    }

    private void runIndexer() {
        Runnable runnable = () -> {
            try {
                Indexer.getInstance().run();
                Utility.saveChanges();
            } catch (IOException | SQLException ex) {
                Utility.writeLog(ex);
            }
        };
        Utility.getExecutorService().execute(runnable);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        libraryLocation3Panel = new javax.swing.JPanel();
        deleteLibraryButton3 = new javax.swing.JButton();
        libraryLocation3PathLabel = new javax.swing.JLabel();
        libraryLocation3ItemsLabel = new javax.swing.JLabel();
        libraryLocation2Panel = new javax.swing.JPanel();
        deleteLibraryButton2 = new javax.swing.JButton();
        libraryLocation2PathLabel = new javax.swing.JLabel();
        libraryLocation2ItemsLabel = new javax.swing.JLabel();
        libraryLocation1Panel = new javax.swing.JPanel();
        deleteLibraryButton1 = new javax.swing.JButton();
        libraryLocation1PathLabel = new javax.swing.JLabel();
        libraryLocation1ItemsLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        addLocationButton = new javax.swing.JButton();
        runIndexerButton = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        indexProgressPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();

        setOpaque(false);

        jPanel1.setOpaque(false);

        deleteLibraryButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/delete_20.png"))); // NOI18N
        deleteLibraryButton3.setActionCommand("2");
        deleteLibraryButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                libraryButtonsActionPerformed(evt);
            }
        });

        libraryLocation3PathLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        libraryLocation3PathLabel.setText("C:/path/to/library");

        libraryLocation3ItemsLabel.setText("28 materials found");

        javax.swing.GroupLayout libraryLocation3PanelLayout = new javax.swing.GroupLayout(libraryLocation3Panel);
        libraryLocation3Panel.setLayout(libraryLocation3PanelLayout);
        libraryLocation3PanelLayout.setHorizontalGroup(
            libraryLocation3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(libraryLocation3PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(libraryLocation3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, libraryLocation3PanelLayout.createSequentialGroup()
                        .addComponent(libraryLocation3PathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteLibraryButton3))
                    .addGroup(libraryLocation3PanelLayout.createSequentialGroup()
                        .addComponent(libraryLocation3ItemsLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        libraryLocation3PanelLayout.setVerticalGroup(
            libraryLocation3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(libraryLocation3PanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(libraryLocation3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteLibraryButton3)
                    .addComponent(libraryLocation3PathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(libraryLocation3ItemsLabel)
                .addContainerGap())
        );

        deleteLibraryButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/delete_20.png"))); // NOI18N
        deleteLibraryButton2.setActionCommand("1");
        deleteLibraryButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                libraryButtonsActionPerformed(evt);
            }
        });

        libraryLocation2PathLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        libraryLocation2PathLabel.setText("C:/path/to/library");

        libraryLocation2ItemsLabel.setText("28 materials found");

        javax.swing.GroupLayout libraryLocation2PanelLayout = new javax.swing.GroupLayout(libraryLocation2Panel);
        libraryLocation2Panel.setLayout(libraryLocation2PanelLayout);
        libraryLocation2PanelLayout.setHorizontalGroup(
            libraryLocation2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(libraryLocation2PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(libraryLocation2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, libraryLocation2PanelLayout.createSequentialGroup()
                        .addComponent(libraryLocation2PathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteLibraryButton2))
                    .addGroup(libraryLocation2PanelLayout.createSequentialGroup()
                        .addComponent(libraryLocation2ItemsLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        libraryLocation2PanelLayout.setVerticalGroup(
            libraryLocation2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(libraryLocation2PanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(libraryLocation2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteLibraryButton2)
                    .addComponent(libraryLocation2PathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(libraryLocation2ItemsLabel)
                .addContainerGap())
        );

        deleteLibraryButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/delete_20.png"))); // NOI18N
        deleteLibraryButton1.setActionCommand("0");
        deleteLibraryButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                libraryButtonsActionPerformed(evt);
            }
        });

        libraryLocation1PathLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        libraryLocation1PathLabel.setText("C:/path/to/library");

        libraryLocation1ItemsLabel.setText("28 materials found");

        javax.swing.GroupLayout libraryLocation1PanelLayout = new javax.swing.GroupLayout(libraryLocation1Panel);
        libraryLocation1Panel.setLayout(libraryLocation1PanelLayout);
        libraryLocation1PanelLayout.setHorizontalGroup(
            libraryLocation1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(libraryLocation1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(libraryLocation1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, libraryLocation1PanelLayout.createSequentialGroup()
                        .addComponent(libraryLocation1PathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteLibraryButton1))
                    .addGroup(libraryLocation1PanelLayout.createSequentialGroup()
                        .addComponent(libraryLocation1ItemsLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        libraryLocation1PanelLayout.setVerticalGroup(
            libraryLocation1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(libraryLocation1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(libraryLocation1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteLibraryButton1)
                    .addComponent(libraryLocation1PathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(libraryLocation1ItemsLabel)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Library locatons");

        addLocationButton.setText("Add Location");
        addLocationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addLocationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(libraryLocation1Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(libraryLocation2Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(libraryLocation3Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addLocationButton))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addLocationButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(libraryLocation1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(libraryLocation2Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(libraryLocation3Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        runIndexerButton.setText("Run Indexer");
        runIndexerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runIndexerButtonActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI Light", 0, 20)); // NOI18N
        jLabel10.setText("Settings");

        indexProgressPanel.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semilight", 0, 18)); // NOI18N
        jLabel2.setText("Indexing contents in libraries.");

        jProgressBar1.setStringPainted(true);

        jLabel3.setText("half way there...");

        javax.swing.GroupLayout indexProgressPanelLayout = new javax.swing.GroupLayout(indexProgressPanel);
        indexProgressPanel.setLayout(indexProgressPanelLayout);
        indexProgressPanelLayout.setHorizontalGroup(
            indexProgressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexProgressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(indexProgressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        indexProgressPanelLayout.setVerticalGroup(
            indexProgressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexProgressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(runIndexerButton))
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(indexProgressPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(indexProgressPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runIndexerButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void runIndexerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runIndexerButtonActionPerformed
        // TODO add your handling code here:
        runIndexer();
    }//GEN-LAST:event_runIndexerButtonActionPerformed

    private void addLocationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addLocationButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser(lastAccessedDir);
        fc.setDialogTitle("Choose Library Location");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.showDialog(this, "Add Location");
        File selectedFile = fc.getSelectedFile();
        if (selectedFile != null) {
            lastAccessedDir = selectedFile.getParentFile();
            Runnable runnable = () -> {
                try {

                    //Check if a parent or child directory to this path has been added already
                    for (String path : paths) {
                        String s = selectedFile.toURI().toString();
                        boolean isChild = s.contains(path);
                        boolean isParent = path.contains(s);
                        if (isParent || isChild) {
                            JOptionPane.showMessageDialog(null, "Selected library is related to an existing library " + path);
                            return;
                        }
                    }

                    //Set display
                    indexProgressPanel.setVisible(true);
                    jLabel2.setText("Updating materials.");
                    jLabel3.setVisible(false);
                    jProgressBar1.setIndeterminate(true);

                    //Add materials in location to library, also index them
                    Library.getInstance().addLocation(selectedFile.toURI());
                    //Show location
                    paths.add(selectedFile.toURI().toString());
                    refreshDisplay();
                } catch (HeadlessException | SQLException | IOException ex) {
                    Utility.writeLog(ex);
                }

            };
            Utility.getExecutorService().execute(runnable);
        }
    }//GEN-LAST:event_addLocationButtonActionPerformed

    private void libraryButtonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_libraryButtonsActionPerformed
        // TODO add your handling code here:
        int ans = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?");
        if (ans != JOptionPane.YES_OPTION) {
            return;
        }

        JButton button = (JButton) evt.getSource();
        String command = button.getActionCommand();
        Runnable runnable = () -> {
            try {
                int pathIndex = Integer.parseInt(command);
                String path = paths.get(pathIndex);
                URI uriPath = new URI(path);
                try {
                    //Set display
                    indexProgressPanel.setVisible(true);
                    jLabel2.setText("Updating materials.");
                    jLabel3.setVisible(false);
                    jProgressBar1.setIndeterminate(true);

                    //Remove materials in this location from library, also remove their index
                    Library.getInstance().removeLocation(uriPath);
                    paths.remove(pathIndex);
                    refreshDisplay();
                } catch (SQLException | IOException e) {
                    Utility.writeLog(e);
                }

            } catch (NumberFormatException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(null, "Oops! Something went wrong\n"
                        + "Please try again");
                Utility.writeLog(ex);
            }
        };
        Utility.getExecutorService().execute(runnable);
    }//GEN-LAST:event_libraryButtonsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLocationButton;
    private javax.swing.JButton deleteLibraryButton1;
    private javax.swing.JButton deleteLibraryButton2;
    private javax.swing.JButton deleteLibraryButton3;
    private javax.swing.JPanel indexProgressPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JLabel libraryLocation1ItemsLabel;
    private javax.swing.JPanel libraryLocation1Panel;
    private javax.swing.JLabel libraryLocation1PathLabel;
    private javax.swing.JLabel libraryLocation2ItemsLabel;
    private javax.swing.JPanel libraryLocation2Panel;
    private javax.swing.JLabel libraryLocation2PathLabel;
    private javax.swing.JLabel libraryLocation3ItemsLabel;
    private javax.swing.JPanel libraryLocation3Panel;
    private javax.swing.JLabel libraryLocation3PathLabel;
    private javax.swing.JButton runIndexerButton;
    // End of variables declaration//GEN-END:variables
}
