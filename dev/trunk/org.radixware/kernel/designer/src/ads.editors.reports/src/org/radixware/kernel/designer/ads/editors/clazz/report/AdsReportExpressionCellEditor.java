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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.BorderLayout;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportExpressionCell;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsReportExpressionCellEditor extends javax.swing.JPanel {

    private final AdsReportExpressionCell cell;

    /** Creates new form AdsReportExpressionCellEditor */
    public AdsReportExpressionCellEditor(final AdsReportExpressionCell cell) {
        super();
        this.cell = cell;
        initComponents();
    }

    public boolean open(final OpenInfo openInfo) {
        Jml jmlExpression = cell.getExpression();
        if (openInfo.getTarget() == jmlExpression){
            jmlEditor.open(jmlExpression, openInfo);
            return true;
        } else {
            jmlEditor.open(jmlExpression);
            return false;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jmlEditor = new org.radixware.kernel.designer.common.editors.jml.JmlEditor();

        setLayout(new java.awt.BorderLayout());
        add(jmlEditor, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.radixware.kernel.designer.common.editors.jml.JmlEditor jmlEditor;
    // End of variables declaration//GEN-END:variables

}
