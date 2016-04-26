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
 * MyPanel.java
 *
 * Created on May 18, 2012, 4:18:08 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.chart;

import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class ChangeSupportPanel extends javax.swing.JPanel {    
    protected javax.swing.JPanel content = new javax.swing.JPanel();
    protected final StateManager stateManager;
    final ChangeSupport changeSupport = new ChangeSupport(content);    

    /** Creates new form MyPanel */
    public ChangeSupportPanel() { 
        initComponents();
        this.stateManager = new StateManager(content);
    }    

    public final void addChangeListener(final ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(final ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    
    public boolean isComplete() {        
        return !stateManager.isErrorneous();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
