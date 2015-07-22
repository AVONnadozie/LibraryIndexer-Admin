
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class MaterialsPanel extends javax.swing.JPanel {

    private final Timer searchTimer;
    private Library library;
    private ArrayList<Material> materials;

    /**
     * Creates new form Materials
     */
    public MaterialsPanel() {
        initComponents();
        progressBar.setVisible(false);
        progressLabel.setVisible(false);

        library = Library.getInstance();
        library.addProgressListener(new ProgressListener() {

            @Override
            public void onStart(String message) {
                progressBar.setVisible(true);
                progressLabel.setVisible(true);
                progressBar.setString(message);
            }

            @Override
            public void update(String message, int progress) {
                progressBar.setValue(progress);
                if (message.length() > 100) {
                    message = message.substring(0, 100) + "...";
                }
                progressLabel.setText(message);
            }

            @Override
            public void onStop(String message) {
                progressBar.setVisible(false);
                progressLabel.setVisible(false);
            }
        });

        Runnable runnable = () -> {
            library.init();
            //Populate table
            materials = library.getMaterials();
            populateTable();
        };
        Utility.getExecutorService().execute(runnable);

        //Table item selection
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener((ListSelectionEvent e) -> {

            if (e.getValueIsAdjusting()) {
                return;
            }

            if (e.getSource() instanceof DefaultListSelectionModel) {
                DefaultListSelectionModel l = (DefaultListSelectionModel) e.getSource();
                if (!l.isSelectionEmpty()) {
                    int lastIndex = e.getFirstIndex();
                    if (materials != null && lastIndex < materials.size()) {
                        Material material = materials.get(lastIndex);
                        EditMaterialDialog dialog = new EditMaterialDialog(material);
                        Utility.centreOnParent(MainWindow.getInstance(), dialog);
                        dialog.setVisible(true);
                        defaultSearch();
                    }
                }
            }
        });

        Utility.setPlaceholder(queryField, "Search");

        //Sets search timer to execute in a period of 500 milliseconds
        searchTimer = new Timer(500, (ActionEvent e) -> {
            defaultSearch();
        });
        searchTimer.setRepeats(false);
        searchTimer.setDelay(1000);
    }

    private void populateTable() {
        Object[] columnNames = new Object[]{
            "S/N", "Title", "Author", "Type", "ISBN"
        };
        Object[][] data = new Object[materials.size()][columnNames.length];
        for (int i = 0; i < data.length; i++) {
            data[i][0] = i + 1;
            data[i][1] = materials.get(i).getTitle();
            data[i][2] = materials.get(i).getAuthor();
            data[i][3] = materials.get(i).getType();
            data[i][4] = materials.get(i).getISBN();
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(data, columnNames);
        table.setModel(defaultTableModel);
        table.getTableHeader().setReorderingAllowed(true);
        table.getTableHeader().setResizingAllowed(true);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setWidth(400);
        table.getColumnModel().getColumn(2).setMaxWidth(250);
        table.getColumnModel().getColumn(3).setMaxWidth(100);
        table.getColumnModel().getColumn(4).setMaxWidth(180);
        TableRowSorter<DefaultTableModel> tableRowSorter = new TableRowSorter<>(defaultTableModel);
        tableRowSorter.setComparator(0, (Integer o1, Integer o2) -> o1.compareTo(o2));
        table.setRowSorter(tableRowSorter);
    }

    public void defaultSearch() {
        String query = queryField.getText();
        if (Utility.isPlaceholderShowing(queryField)) {
            query = "";
        }
        materials = library.searchLibrary(query);
        populateTable();
    }

    /**
     * Sets the time for next search
     */
    private void scheduleDefaultSearch() {
        if (searchTimer.isRunning()) {
            searchTimer.restart();
        } else {
            searchTimer.start();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        categoryGroup = new javax.swing.ButtonGroup();
        sortGroup = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        queryField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        progressBar = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();
        progressLabel = new javax.swing.JLabel();

        setOpaque(false);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/search67.png"))); // NOI18N
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.setContentAreaFilled(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        queryField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        queryField.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        queryField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                queryFieldActionPerformed(evt);
            }
        });
        queryField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                queryFieldKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(queryField, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(queryField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 0, 20)); // NOI18N
        jLabel1.setText("Materials");

        table.setAutoCreateRowSorter(true);
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ISBN", "Title", "Author", "Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        progressBar.setString("Loading Materials...");
        progressBar.setStringPainted(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel2.setText("* Click on a material to edit");

        progressLabel.setText("<progress message>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(progressLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressLabel))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        defaultSearch();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void queryFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryFieldActionPerformed
        // TODO add your handling code here:
        defaultSearch();
    }//GEN-LAST:event_queryFieldActionPerformed

    private void queryFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_queryFieldKeyTyped
        // TODO add your handling code here:
        scheduleDefaultSearch();
    }//GEN-LAST:event_queryFieldKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup categoryGroup;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JTextField queryField;
    private javax.swing.ButtonGroup sortGroup;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
