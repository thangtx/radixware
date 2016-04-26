/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

/*
 * TestDialog.java
 *
 * Created on 11.03.2009, 17:26:39
 */

package org.radixware.kernel.common.design.msdleditor.field.panel;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import org.radixware.kernel.common.design.msdleditor.JDialogClosedByEscape;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;


public class TestDialog extends JDialogClosedByEscape {

    private static final String PREFERENCES_KEY = "MsdlEditor";
    private static final String TESTDIALOG_KEY = "TestDialog";
    private static final String POS_KEY = "Pos";
    private static final String POSX_KEY = "X";
    private static final String POSY_KEY = "Y";
    private static final String WIDTH_KEY = "Width";
    private static final String HEIGHT_KEY = "Height";

    /** Creates new form TestDialog */
    public TestDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void restorePos() {
        try {
            if (Preferences.userRoot().nodeExists(PREFERENCES_KEY)) {
                Preferences editorPreferences = Preferences.userRoot().node(PREFERENCES_KEY);
                if (editorPreferences.nodeExists(TESTDIALOG_KEY)) {
                    Preferences dialogPrefences = editorPreferences.node(TESTDIALOG_KEY);
                    if (dialogPrefences.nodeExists(POS_KEY)) {
                        Preferences dimensionKey = dialogPrefences.node(POS_KEY);
                        int width = dimensionKey.getInt(WIDTH_KEY, 400);
                        int height = dimensionKey.getInt(HEIGHT_KEY, 300);
                        setSize(new Dimension(width, height));
                        int x = dimensionKey.getInt(POSX_KEY, 100);
                        int y = dimensionKey.getInt(POSY_KEY, 100);
                        setLocation(x,y);
                    }
                }
            }
        } catch (BackingStoreException ex) {
            JOptionPane.showMessageDialog(null, "Can't restore configuration: " + ex.getMessage());
        }
    }

    public void savePos() {
        Preferences editorPreferences = Preferences.userRoot().node(PREFERENCES_KEY);
        Preferences dialog = editorPreferences.node(TESTDIALOG_KEY);
        Preferences pos = dialog.node(POS_KEY);
        Dimension dim = getSize();
        pos.putInt(WIDTH_KEY, dim.width);
        pos.putInt(HEIGHT_KEY, dim.height);
        Point p = getLocation();
        pos.putInt(POSX_KEY, p.x);
        pos.putInt(POSY_KEY, p.y);
        try {
            Preferences.userRoot().flush();
        }
        catch (BackingStoreException ex) {
            JOptionPane.showMessageDialog(null, "Can't save configuration: " + ex.getMessage());
        }

    }

    public void open(AbstractFieldModel model, String fieldName) {
        testPanel1.open(this, model);
        restorePos();
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                savePos();
                testPanel1.saveCfg();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        setTitle("Test '" + fieldName + "'");
        setVisible(true);

    }

    @Override
    protected boolean tryClose() {
        savePos();
//        int res = JOptionPane.showConfirmDialog(null,"Do you want save string and xml ?");
//        if (res == JOptionPane.YES_OPTION) {
           testPanel1.saveCfg();
           return true;
//        }
//        return false;
    }



    @Override
    public synchronized void addWindowListener(WindowListener l) {
        super.addWindowListener(l);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        testPanel1 = new org.radixware.kernel.common.design.msdleditor.field.panel.TestPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        getContentPane().add(testPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.common.design.msdleditor.field.panel.TestPanel testPanel1;
    // End of variables declaration//GEN-END:variables

}
