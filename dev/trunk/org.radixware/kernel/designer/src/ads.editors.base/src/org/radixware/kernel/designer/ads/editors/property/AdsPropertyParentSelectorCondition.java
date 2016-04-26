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
 * AdsPropertyParentSelectorCondition.java
 *
 * Created on 03.09.2009, 17:47:19
 */

package org.radixware.kernel.designer.ads.editors.property;

import java.awt.Dimension;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.designer.common.editors.sqml.SqmlEditorPanel;


public class AdsPropertyParentSelectorCondition extends javax.swing.JPanel {


    private SqmlEditorPanel jenConditionFrom = null;
    private SqmlEditorPanel jenConditionWhere = null;

    public AdsPropertyParentSelectorCondition() {
        initComponents();
        jenConditionFrom = new SqmlEditorPanel();//ScmlEditor.SQML_MIME_TYPE, null);
        jenConditionWhere = new SqmlEditorPanel();//ScmlEditor.SQML_MIME_TYPE, null);

        Dimension dim = new Dimension(50, 50);

        jenConditionFrom.setMinimumSize(dim);
        jenConditionWhere.setMinimumSize(dim);

        jScrollPane3.setViewportView(jenConditionFrom);
        jScrollPane4.setViewportView(jenConditionWhere);
    }
    AdsCondition condition = null;
    public void open(AdsCondition condition)
    {
      this.condition = condition;
      update();
    }

    public void update()
    {
      if (condition!=null)
      {
         jenConditionFrom.open(condition.getFrom());
         jenConditionWhere.open(condition.getWhere());
      }
    }
    boolean isReadOnly = false;
    void setReadOnly(boolean isReadOnly)
    {
      this.isReadOnly = isReadOnly;
      jenConditionFrom.setEditable(!isReadOnly);
      jenConditionWhere.setEditable(!isReadOnly);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();

        jSplitPane1.setDividerLocation(110);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyParentSelectorCondition.class, "AdsPropertyParentSelectorCondition.jPanel15.border.title"))); // NOI18N

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
        );

        jSplitPane1.setTopComponent(jPanel15);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdsPropertyParentSelectorCondition.class, "AdsPropertyParentSelectorCondition.jPanel16.border.title"))); // NOI18N

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
        );

        jSplitPane1.setBottomComponent(jPanel16);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 179, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 231, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables

}
