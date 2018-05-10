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

package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.JTable;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport.PropertyRef;


public class PropertiesArrangmentTransferHandler extends TransferHandler {

    private Object value;
    private DataFlavor objectsFlavor;
    private DataFlavor[] flavors = new DataFlavor[1];
    private int selectedRow, selectedColumn;
    private int targetRowIndex, targetColumnIndex;

    public PropertiesArrangmentTransferHandler() {
        super();
        try {
            final String mimeType = DataFlavor.javaJVMLocalObjectMimeType
                    + ";class=\""
                    + Object[].class.getName()
                    + "\"";
            objectsFlavor = new DataFlavor(mimeType);
            flavors[0] = objectsFlavor;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        
        final JTable table = (JTable) c;
        selectedRow = table.getSelectedRow();
        selectedColumn = table.getSelectedColumn();

        if (selectedRow != -1 && selectedColumn != -1) {
            value = table.getModel().getValueAt(selectedRow, selectedColumn);
            return new ObjectsTransferable(value);
        }
        return null;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        
        final JTable target = (JTable) support.getComponent();
        EditorPagePropTableModel targetModel = (EditorPagePropTableModel) target.getModel();
        
        if (targetModel.isReadOnly()) {
            return false;
        }
        
        if (!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if (!support.isDataFlavorSupported(objectsFlavor)) {
            return false;
        }

        DropLocation location = support.getDropLocation();
        targetRowIndex = target.rowAtPoint(location.getDropPoint());
        targetColumnIndex = target.columnAtPoint(location.getDropPoint());

        if (selectedRow == targetRowIndex && selectedColumn == targetColumnIndex) {
            return false;
        }
        if (target.getValueAt(selectedRow, selectedColumn) == null) {
            return false;
        }

        return true;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if ((action & MOVE) == MOVE) {
            //remove value
            final JTable table = (JTable) source;
            EditorPagePropTableModel model = (EditorPagePropTableModel) table.getModel();
            model.moveItem((EditorPagePropTableModel.PageItem) value, targetRowIndex, targetColumnIndex);
        }
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        /*if (canImport(support)) {
        try {
        final Object obj = support.getTransferable().getTransferData(objectsFlavor);
        importObject((JComponent)support.getComponent(), obj);
        return true;
        } catch (UnsupportedFlavorException ufe) {
        } catch (IOException ioe) {
        }
        }
        return false;*/
        return canImport(support);
    }
    /*
    private void importObject(JComponent c, Object obj) {
    
    //final JTable target = (JTable) c;
    //target.setValueAt(value, targetRowIndex, targetColumnIndex);
    }*/

    private class ObjectsTransferable implements Transferable {

        private Object value;

        public ObjectsTransferable(Object value) {
            this.value = value;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return value;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return objectsFlavor.equals(flavor);
        }
    }
}
