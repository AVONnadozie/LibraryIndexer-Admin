
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JFrame;

/**
 *
 * @author Anuebunwa Victor
 */
public final class MainWindow extends javax.swing.JFrame {

    private Image img;
    private static MainWindow thisClass;
    private final Dimension lastWindowSize;
    private final Point point;
    private final MaterialsPanel materials;

    private MainWindow() {
        point = new Point();
        try {
            img = ImageIO.read(getClass().getResource("resources/bg.jpg"));
        } catch (IOException ex) {
            Utility.writeLog(ex);
        }

        initComponents();

        LoginPanel login = new LoginPanel();
        scene.add(login, "login");

        materials = new MaterialsPanel();
        scene.add(materials, "materials");

        SettingsPanel settings = new SettingsPanel();
        scene.add(settings, "settings");

        lastWindowSize = getSize();
        showNavButtons(false);
    }

    public void fitWindowToScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (fitToScreenToggleButton.isSelected()) {
            setSize(screenSize);
            setLocation(0, 0);
            fitToScreenToggleButton.setToolTipText("Restore");
        } else {
            setSize(lastWindowSize);
            Utility.centreOnScreen(this);

            fitToScreenToggleButton.setToolTipText("Fit to screen");
        }
    }

    private void bodyMousePressed(MouseEvent evt) {
        if (!evt.isMetaDown()) {
            point.x = evt.getX();
            point.y = evt.getY();
        }
    }

    private void bodyMouseDragged(MouseEvent evt) {
        if (!evt.isMetaDown()) {
            Point p = getLocation();
            setLocation(p.x + evt.getX() - this.point.x,
                    p.y + evt.getY() - this.point.y);
        }
    }

    public static MainWindow getInstance() {
        if (thisClass == null) {
            thisClass = new MainWindow();
        }
        return thisClass;
    }

    public void showMaterialsPanel() {
        getSceneLayout().show(scene, "materials");
        try {
            materials.defaultSearch();
        } catch (IllegalStateException e) {
        }
    }

    public void showSettingsPanel() {
        getSceneLayout().show(scene, "settings");
    }

    public void showNavButtons(boolean state) {
        materialNavButton.setVisible(state);
        settingsNavButton.setVisible(state);
    }

    private CardLayout getSceneLayout() {
        return (CardLayout) scene.getLayout();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        scene = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        settingsNavButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        materialNavButton = new javax.swing.JButton();
        minimizeButton = new javax.swing.JButton();
        fitToScreenToggleButton = new javax.swing.JToggleButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Library Indexer - Administrator");
        setUndecorated(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 3));

        scene.setBackground(new java.awt.Color(255, 255, 255));
        scene.setMinimumSize(new java.awt.Dimension(647, 300));
        scene.setLayout(new java.awt.CardLayout());

        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                MainWindow.this.mouseDragged(evt);
            }
        });
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                MainWindow.this.mousePressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        jLabel1.setText("Library Indexer");
        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                MainWindow.this.mouseDragged(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                MainWindow.this.mousePressed(evt);
            }
        });

        settingsNavButton.setText("Settings");
        settingsNavButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsNavButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Admin");
        jLabel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                MainWindow.this.mouseDragged(evt);
            }
        });
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                MainWindow.this.mousePressed(evt);
            }
        });

        materialNavButton.setText("Materials");
        materialNavButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                materialNavButtonActionPerformed(evt);
            }
        });

        minimizeButton.setBackground(new java.awt.Color(0, 153, 255));
        minimizeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/minimize_12.png"))); // NOI18N
        minimizeButton.setToolTipText("Minimize");
        minimizeButton.setBorder(null);
        minimizeButton.setBorderPainted(false);
        minimizeButton.setContentAreaFilled(false);
        minimizeButton.setFocusPainted(false);
        minimizeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimizeButtontopButtonsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimizeButtontopButtonsMouseExited(evt);
            }
        });
        minimizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minimizeButtonActionPerformed(evt);
            }
        });

        fitToScreenToggleButton.setBackground(new java.awt.Color(0, 153, 255));
        fitToScreenToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/fitScreen.png"))); // NOI18N
        fitToScreenToggleButton.setToolTipText("Fit to Screen");
        fitToScreenToggleButton.setBorder(null);
        fitToScreenToggleButton.setBorderPainted(false);
        fitToScreenToggleButton.setContentAreaFilled(false);
        fitToScreenToggleButton.setFocusPainted(false);
        fitToScreenToggleButton.setRolloverEnabled(false);
        fitToScreenToggleButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/restore.png"))); // NOI18N
        fitToScreenToggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fitToScreenToggleButtontopButtonsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fitToScreenToggleButtontopButtonsMouseExited(evt);
            }
        });
        fitToScreenToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fitToScreenToggleButtonActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 0, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("X");
        jButton3.setToolTipText("Close");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setFocusPainted(false);
        jButton3.setOpaque(true);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(materialNavButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(settingsNavButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(minimizeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(fitToScreenToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2)
                .addComponent(jButton3)
                .addGap(3, 3, 3))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(minimizeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fitToScreenToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(settingsNavButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(materialNavButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scene, javax.swing.GroupLayout.DEFAULT_SIZE, 806, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scene, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void materialNavButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_materialNavButtonActionPerformed
        // TODO add your handling code here:
        showMaterialsPanel();
    }//GEN-LAST:event_materialNavButtonActionPerformed

    private void settingsNavButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsNavButtonActionPerformed
        // TODO add your handling code here:
        showSettingsPanel();
    }//GEN-LAST:event_settingsNavButtonActionPerformed

    private void minimizeButtontopButtonsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeButtontopButtonsMouseEntered
        // TODO add your handling code here:
        AbstractButton b = (AbstractButton) evt.getSource();
        b.setOpaque(true);
        b.repaint();
    }//GEN-LAST:event_minimizeButtontopButtonsMouseEntered

    private void minimizeButtontopButtonsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizeButtontopButtonsMouseExited
        // TODO add your handling code here:
        AbstractButton b = (AbstractButton) evt.getSource();
        b.setOpaque(false);
        b.repaint();
    }//GEN-LAST:event_minimizeButtontopButtonsMouseExited

    private void minimizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minimizeButtonActionPerformed
        // TODO add your handling code here:
        setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimizeButtonActionPerformed

    private void fitToScreenToggleButtontopButtonsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fitToScreenToggleButtontopButtonsMouseEntered
        // TODO add your handling code here:
        AbstractButton b = (AbstractButton) evt.getSource();
        b.setOpaque(true);
        b.repaint();
    }//GEN-LAST:event_fitToScreenToggleButtontopButtonsMouseEntered

    private void fitToScreenToggleButtontopButtonsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fitToScreenToggleButtontopButtonsMouseExited
        // TODO add your handling code here:
        AbstractButton b = (AbstractButton) evt.getSource();
        b.setOpaque(false);
        b.repaint();
    }//GEN-LAST:event_fitToScreenToggleButtontopButtonsMouseExited

    private void fitToScreenToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fitToScreenToggleButtonActionPerformed
        // TODO add your handling code here:
        fitWindowToScreen();
    }//GEN-LAST:event_fitToScreenToggleButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Main.closeApp();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        // TODO add your handling code here:
        bodyMousePressed(evt);
    }//GEN-LAST:event_mousePressed

    private void mouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseDragged
        // TODO add your handling code here:
        bodyMouseDragged(evt);
    }//GEN-LAST:event_mouseDragged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton fitToScreenToggleButton;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton materialNavButton;
    private javax.swing.JButton minimizeButton;
    private javax.swing.JPanel scene;
    private javax.swing.JButton settingsNavButton;
    // End of variables declaration//GEN-END:variables
}
