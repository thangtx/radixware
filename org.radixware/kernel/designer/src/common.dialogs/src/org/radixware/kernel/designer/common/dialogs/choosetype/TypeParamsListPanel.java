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
 * TypeParamsListPanel.java
 *
 * Created on 04.06.2009, 13:39:52
 */
package org.radixware.kernel.designer.common.dialogs.choosetype;

import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.utils.IterableWalker;


public class TypeParamsListPanel extends javax.swing.JPanel {

    public TypeParamsListPanel() {
        initComponents();

        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                changeSupport.fireChange();
            }
        });
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setModel(new DefaultListModel());
    }

    public void open(final AdsClassDef context) {
        final DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.clear();

        context.getNestedClassWalker().walk(
            new IterableWalker.Acceptor<AdsClassDef, Void>() {

            @Override
            public void accept(AdsClassDef object) {
                for (final TypeArgument typeArgument : object.getTypeArguments().getArgumentList()) {
                    // display class name prefix only for arguments from other classes
                    listModel.addElement(new TypeArgumentItem(typeArgument, object != context ? object : null));
                }
                if (!object.isInner()) {
                    cancel();
                }
            }
        });
    }

    public String getCurrentParameter() {
        if (list.getSelectedValue() != null) {
            return ((TypeArgumentItem) list.getSelectedValue()).argument.getName();
        }
        return null;
    }

    private class TypeArgumentItem {

        final TypeArgument argument;
        final AdsClassDef context;

        public TypeArgumentItem(TypeArgument arg) {
            this(arg, null);
        }

        private TypeArgumentItem(TypeArgument typeArgument, AdsClassDef context) {
            this.argument = typeArgument;
            this.context = context;
        }

        @Override
        public String toString() {
            if (argument != null) {
                return (context != null? context.getName() + ":" : "") + argument.getName();
            }
            return "<Not Defined>";
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        jScrollPane1.setViewportView(list);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables

    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);
}
