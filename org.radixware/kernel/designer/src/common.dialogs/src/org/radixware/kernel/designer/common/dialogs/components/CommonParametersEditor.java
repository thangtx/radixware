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
 * CommonParametersEditor.java
 *
 * Created on 08.05.2009, 11:17:41
 */
package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.dialogs.components.CommonParametersEditorCellLib.ParameterNameChangeEvent;
import org.radixware.kernel.designer.common.dialogs.components.TunedTable.EscapePressedOnTableEvent;

import org.radixware.kernel.common.resources.RadixWareIcons;


public class CommonParametersEditor extends javax.swing.JPanel {

    private AdsDefinition context;
    //private DefaultTableModel pModel;
    private boolean readonly = false;

    private TunedTable.EscapeKeyOnTableListener escapeListener = new TunedTable.EscapeKeyOnTableListener() {

        @Override
        public void onEvent(EscapePressedOnTableEvent e) {
            CommonParametersEditor.this.getEditorStateSupport().fireEvent(new CommonParametersEditorEvent(ESCAPE_BUTTON_PRESSED));
        }
    };

    private CommonParametersEditorCellLib.ParameterNameChangeListener nameChangeListener = new CommonParametersEditorCellLib.ParameterNameChangeListener() {

        @Override
        public void onEvent(ParameterNameChangeEvent e) {
            CommonParametersEditor.this.editorNameChangeSupport.fireChange();
        }
    };

    /** Creates new form CommonParametersEditor */
    public CommonParametersEditor() {
        initComponents();
        table.getEscapeKeySupport().addEventListener(escapeListener);
        table.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                onFocusEvent();
            }

            @Override
            public void focusGained(FocusEvent e) {
                onFocusEvent();
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                CommonParametersEditor.this.onFocusEvent();
            }
        });
        ActionListener addListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int type = CommonParametersEditor.ADD_BUTTON_PRESSED;
                if (CommonParametersEditor.this.editorStateSupport != null) {
                    CommonParametersEditor.this.editorStateSupport.fireEvent(new CommonParametersEditorEvent(type));
                }
            }
        };
        addBtn.addActionListener(addListener);
        ActionListener removeListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int type = CommonParametersEditor.REMOVE_BUTTON_PRESSED;
                if (CommonParametersEditor.this.editorStateSupport != null) {
                    CommonParametersEditor.this.editorStateSupport.fireEvent(new CommonParametersEditorEvent(type));
                }
            }
        };
        removeBtn.addActionListener(removeListener);
        ActionListener upListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int type = CommonParametersEditor.UP_BUTTON_PRESSED;
                if (CommonParametersEditor.this.editorStateSupport != null) {
                    CommonParametersEditor.this.editorStateSupport.fireEvent(new CommonParametersEditorEvent(type));
                }
                CommonParametersEditor.this.table.moveRow(true);
                changeSupport.fireChange();
            }
        };
        upBtn.addActionListener(upListener);
        ActionListener downListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int type = CommonParametersEditor.DOWN_BUTTON_PRESSED;
                if (CommonParametersEditor.this.editorStateSupport != null) {
                    CommonParametersEditor.this.editorStateSupport.fireEvent(new CommonParametersEditorEvent(type));
                }
                CommonParametersEditor.this.table.moveRow(false);
                changeSupport.fireChange();
            }
        };

        table.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                CommonParametersEditor.this.changeSupport.fireChange();
            }

        });
        table.addNameChangeListener(nameChangeListener);
        downBtn.addActionListener(downListener);
        addBtn.setEnabled(false);
        removeBtn.setEnabled(false);
        upBtn.setEnabled(false);
        downBtn.setEnabled(false);
    }

    public void onFocusEvent() {
        if (table.getModel() != null) {
            addBtn.setEnabled(!readonly);
            if (table.getModel().getRowCount() > 0) {
                if (table.getSelectedRow() != -1) {
                    removeBtn.setEnabled(!readonly);
                    upBtn.setEnabled(!readonly && (table.getSelectedRow() > 0));
                    downBtn.setEnabled(!readonly && (table.getSelectedRow() < (table.getModel().getRowCount() - 1)));
                } else {
                    removeBtn.setEnabled(false);
                }
            } else {
                removeBtn.setEnabled(false);
                upBtn.setEnabled(false);
                downBtn.setEnabled(false);
            }
        }
    }

    public void clearTable() {
        table.clearTable();
    }

    public void open(AdsDefinition context, AdsMethodParameters profile, AdsMethodProfileSettings settings) {
        this.context = context;
        this.readonly = settings.layerReadonly || settings.optionalReadonly;
        table.setProfileSettings(settings);
        table.setupTableUI(false, readonly, false, settings.isTransparent, false);
        clearTable();
        for (int i = 0; i <= profile.size() - 1; i++) {
            MethodParameter p = profile.get(i);
            CommonParametersEditorCellLib.StringCellValue nameitem = new CommonParametersEditorCellLib.StringCellValue(p.getName());
            CommonParametersEditorCellLib.TypePresentation typeitem = new CommonParametersEditorCellLib.TypePresentation(p.getType(), context, p);
            String desc = p.getDescription();
            table.addModelRow(new Object[]{nameitem, typeitem, new CommonParametersEditorCellLib.StringCellValue(desc != null ? desc : "")});
        }
        onFocusEvent();
    }

    public void open(AdsDefinition context, AdsTypeDeclaration.TypeArguments arguments, boolean readonly) {
        this.context = context;
        this.readonly = readonly;
        table.setupTableUI(false, readonly, true, false, false);
        clearTable();
        for (int i = 0, size = arguments.getArgumentList().size() - 1; i <= size; i++) {
            AdsTypeDeclaration.TypeArgument a = arguments.getArgumentList().get(i);
            CommonParametersEditorCellLib.StringCellValue nameitem = new CommonParametersEditorCellLib.StringCellValue(a.getName());
            CommonParametersEditorCellLib.TypePresentation typeitem = new CommonParametersEditorCellLib.TypePresentation(a, context);
            table.addModelRow(new Object[]{nameitem, a.getDerivation(), typeitem});
        }
        onFocusEvent();
    }

    public void closeCellEditors() {
        table.closeCellEditors();
    }

    public List<String> getCurrentNames() {
        return table.getCurrentNames();
    }

    public String getCurrentRowName() {
        return table.getCurrentRowName();
    }

    public List<AdsTypeDeclaration> getCurrentTypes() {
        return table.getCurrentTypes();
    }

    public AdsTypeDeclaration getCurrentRowType() {
        return table.getCurrentRowType();
    }

    public AdsTypeDeclaration.TypeArgument.Derivation getCurrentRowDerivation() {
        return table.getCurrentRowDerivation();
    }

    public String getCurrentDescription(){
        return table.getCurrentDescription();
    }

    public List<String> getCurrentDescriptions(){
        return table.getCurrentDescriptions();
    }

    public int getCurrentRow() {
        return table.getSelectedRow();
    }

    public void addRow(MethodParameter p) {
        closeCellEditors();
        CommonParametersEditorCellLib.StringCellValue nameitem = new CommonParametersEditorCellLib.StringCellValue(p.getName());
        CommonParametersEditorCellLib.TypePresentation typeitem = new CommonParametersEditorCellLib.TypePresentation(p.getType(), context, p);
        CommonParametersEditorCellLib.StringCellValue descitem = new CommonParametersEditorCellLib.StringCellValue(p.getDescription() != null ? p.getDescription() : "");
       // table.addModelRow(new Object[]{nameitem, typeitem, descitem});
        table.insertModelRowToSelected(new Object[]{nameitem, typeitem, descitem});
        changeSupport.fireChange();
    }

    public void addRow(AdsTypeDeclaration.TypeArgument arg) {
        closeCellEditors();
        CommonParametersEditorCellLib.StringCellValue nameitem = new CommonParametersEditorCellLib.StringCellValue(arg.getName());
        CommonParametersEditorCellLib.TypePresentation typeitem = new CommonParametersEditorCellLib.TypePresentation(arg, context);
        //table.addModelRow(new Object[]{nameitem, arg.getDerivation(), typeitem});
        table.insertModelRowToSelected(new Object[]{nameitem, arg.getDerivation(), typeitem});
        changeSupport.fireChange();
    }

    public void removeRow(int row) {
        closeCellEditors();
        table.removeModelRow(row); 
        int index = row == 0 ? row : row-1;
        table.getSelectionModel().setSelectionInterval(index, index);
        changeSupport.fireChange();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        downBtn = new javax.swing.JButton();
        upBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new org.radixware.kernel.designer.common.dialogs.components.CommonParametersTable();

        addBtn.setIcon(RadixWareIcons.CREATE.ADD.getIcon(13, 13));
        addBtn.setText(org.openide.util.NbBundle.getMessage(CommonParametersEditor.class, "ParametersEditorBtns-Add")); // NOI18N
        addBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        removeBtn.setIcon(RadixWareIcons.DELETE.DELETE.getIcon(13, 13));
        removeBtn.setText(org.openide.util.NbBundle.getMessage(CommonParametersEditor.class, "ParametersEditorBtns-Remove")); // NOI18N
        removeBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        downBtn.setIcon(RadixWareIcons.ARROW.MOVE_DOWN.getIcon(13, 13));
        downBtn.setText(org.openide.util.NbBundle.getMessage(CommonParametersEditor.class, "ParametersEditorBtnd-Down")); // NOI18N
        downBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        upBtn.setIcon(RadixWareIcons.ARROW.MOVE_UP.getIcon(13, 13));
        upBtn.setText(org.openide.util.NbBundle.getMessage(CommonParametersEditor.class, "ParametersEditorBtns-Up")); // NOI18N
        upBtn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBtn)
                    .addComponent(removeBtn)
                    .addComponent(downBtn)
                    .addComponent(upBtn)))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addBtn, downBtn, removeBtn, upBtn});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(addBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(upBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(downBtn))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton downBtn;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeBtn;
    private org.radixware.kernel.designer.common.dialogs.components.CommonParametersTable table;
    private javax.swing.JButton upBtn;
    // End of variables declaration//GEN-END:variables

    public boolean isComplete() {
        return table.isComplete();
    }

    private ChangeSupport editorNameChangeSupport = new ChangeSupport(this);

    public void addNameChangeListener(ChangeListener l){
        this.editorNameChangeSupport.addChangeListener(l);
    }

    public void removeNameChangeListener(ChangeListener l){
        this.editorNameChangeSupport.removeChangeListener(l); 
    }

    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }
    public static final Integer ADD_BUTTON_PRESSED = 0;
    public static final Integer REMOVE_BUTTON_PRESSED = 1;
    public static final Integer UP_BUTTON_PRESSED = 2;
    public static final Integer DOWN_BUTTON_PRESSED = 3;
    public static final Integer ESCAPE_BUTTON_PRESSED = 4;
    private CommonParametersEditorStateSupport editorStateSupport;

    public static class CommonParametersEditorEvent extends RadixEvent {

        public int type;

        public CommonParametersEditorEvent(int type) {
            this.type = type;
        }
    }

    public interface ICommonParametersEditorEventListener extends IRadixEventListener<CommonParametersEditorEvent> {
    }

    public static class CommonParametersEditorStateSupport extends RadixEventSource<ICommonParametersEditorEventListener, CommonParametersEditorEvent> {

        private EventListenerList listeners = new EventListenerList();

        @Override
        public synchronized void addEventListener(ICommonParametersEditorEventListener listener) {
            listeners.add(ICommonParametersEditorEventListener.class, listener);
        }

        @Override
        public synchronized void removeEventListener(ICommonParametersEditorEventListener listener) {
            listeners.remove(ICommonParametersEditorEventListener.class, listener);
        }

        @Override
        public void fireEvent(CommonParametersEditorEvent event) {
            Object[] l = listeners.getListeners(ICommonParametersEditorEventListener.class);
            for (int i = 0; i <= l.length - 1; i++) {
                if (event != null) {
                    ((ICommonParametersEditorEventListener) l[i]).onEvent(event);
                }
            }
        }
    }

    public synchronized CommonParametersEditorStateSupport getEditorStateSupport() {
        if (editorStateSupport == null) {
            editorStateSupport = new CommonParametersEditorStateSupport();
        }
        return editorStateSupport;
    }
}
