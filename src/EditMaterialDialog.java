
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Victor Anuebunwa
 */
public class EditMaterialDialog extends javax.swing.JDialog {

    private final Material material;
    private boolean fieldEdited;

    /**
     * Creates new form EditMaterialDialog
     *
     * @param material
     */
    public EditMaterialDialog(Material material) {
        super(MainWindow.getInstance(), true);
        this.material = material;
        initComponents();

        keywordNoteLabel.setVisible(false);

        if (material != null) {
            setTitle(material.getTitle());
            typeLabel.setText(material.getType().name());
            excludeCheckBox.setSelected(material.isExcluded());
            titleField.setText(material.getTitle());
            authorField.setText(material.getAuthor());
            pathField.setText(new File(material.getPath()).toString());
//            dateAddedLabel.setText(material.getDateAdded().toString());
            keywordsTextArea.setText(String.join(", ", material.getKeywords()));
            previewTextPane.setText(material.getPreview());
        }

        Utility.setPlaceholder(titleField, "Enter title of Material");
        Utility.setPlaceholder(authorField, "Enter author of Material");
        Utility.setPlaceholder(keywordsTextArea, "Enter keywords for this material seperated by comma");
        Utility.setPlaceholder(previewTextPane, "No preview available");
    }

    private void saveChanges() {
        if (fieldEdited) {
            material.setExcluded(excludeCheckBox.isSelected());
            if (!Utility.isPlaceholderShowing(titleField)) {
                material.setTitle(titleField.getText());
            }
            if (!Utility.isPlaceholderShowing(authorField)) {
                material.setAuthor(authorField.getText());
            }
            if (!Utility.isPlaceholderShowing(keywordsTextArea)) {
                material.setKeywords(keywordsTextArea.getText().split(","));
            }

            Runnable runnable = () -> {
                //Disallow other actions
                editButton.setEnabled(false);
                okButton.setEnabled(false);

                try {
                    //Update
                    material.update();
                    fieldEdited = false;
                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(null, "Oops! Something went wrong\n"
                            + "Changes could not be saved");
                    Utility.writeLog(ex);
                }

                //allow other actions
                editButton.setEnabled(true);
                okButton.setEnabled(true);
            };
            Utility.getExecutorService().execute(runnable);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        excludeCheckBox = new javax.swing.JCheckBox();
        authorField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        previewTextPane = new javax.swing.JTextPane();
        keywordNoteLabel = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        editButton = new javax.swing.JButton();
        keywordsTextArea = new javax.swing.JTextField();
        pathField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Title");

        titleField.setEditable(false);
        titleField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        excludeCheckBox.setText("Exclude this material from indexing");
        excludeCheckBox.setIconTextGap(10);
        excludeCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excludeCheckBoxActionPerformed(evt);
            }
        });

        authorField.setEditable(false);
        authorField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Author");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Keywords");

        jLabel5.setText("Type:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Preview");

        previewTextPane.setEditable(false);
        jScrollPane1.setViewportView(previewTextPane);

        keywordNoteLabel.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        keywordNoteLabel.setText("* seperate keywords with commas");

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Path");

        typeLabel.setText("None");

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        keywordsTextArea.setEditable(false);
        keywordsTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        pathField.setEditable(false);
        pathField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(titleField)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(excludeCheckBox))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(authorField)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(keywordNoteLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(keywordsTextArea)
                    .addComponent(pathField))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(excludeCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keywordsTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keywordNoteLabel)
                .addGap(7, 7, 7)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(editButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        JButton button = (JButton) evt.getSource();
        if (button.getActionCommand().equalsIgnoreCase("Edit")) { //Allow editing of fields
            titleField.setEditable(true);
            authorField.setEditable(true);
            keywordsTextArea.setEditable(true);
            keywordNoteLabel.setVisible(true);
            button.setActionCommand("Update");
            button.setText("Update");
        } else {
            titleField.setEditable(false);
            authorField.setEditable(false);
            keywordsTextArea.setEditable(false);
            keywordNoteLabel.setVisible(false);
            button.setActionCommand("Edit");
            button.setText("Edit");
            saveChanges();
        }

    }//GEN-LAST:event_editButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        // TODO add your handling code here:
        saveChanges();
        dispose();
    }//GEN-LAST:event_okButtonActionPerformed

    private void excludeCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excludeCheckBoxActionPerformed
        // TODO add your handling code here:
        fieldEdited = true;
    }//GEN-LAST:event_excludeCheckBoxActionPerformed

    private void KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KeyTyped
        // TODO add your handling code here:
        fieldEdited = true;
    }//GEN-LAST:event_KeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authorField;
    private javax.swing.JButton editButton;
    private javax.swing.JCheckBox excludeCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel keywordNoteLabel;
    private javax.swing.JTextField keywordsTextArea;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField pathField;
    private javax.swing.JTextPane previewTextPane;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables

}
