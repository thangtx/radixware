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
package org.radixware.kernel.designer.ads.editors.xml;

import java.awt.Dimension;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.TableRowSorter;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.EXmlSchemaLinkMode;
import org.radixware.kernel.common.utils.Pair;

/**
 *
 * @author dlastochkin
 */
public class LinkedSchemasTable extends JTable {
    
    private static final String[] COLUMN_HEADERS = {"Schema Name", "Location", "Link Mode"};
    
    public LinkedSchemasTable(Map<AdsXmlSchemeDef, EXmlSchemaLinkMode> objects) {
        super(new LinkedSchemasTableModel(objects));
        
        for (int i = 0; i < COLUMN_HEADERS.length; i++) {
            this.getColumnModel().getColumn(i).setHeaderValue(COLUMN_HEADERS[i]);
        }        
        this.getTableHeader().setEnabled(false);             
        
        this.setShowGrid(false);
        this.setIntercellSpacing(new Dimension(0, 0));
        this.setFillsViewportHeight(true);
        this.setRowHeight(this.getRowHeight() + 4);
        this.setDefaultRenderer(Object.class, new LinkedSchemasTableCellRenderer());
        this.setRowSorter(new TableRowSorter<>((LinkedSchemasTableModel) this.getModel()));
    }

    public void addRow(AdsXmlSchemeDef schema, EXmlSchemaLinkMode linkMode) {
        ((LinkedSchemasTableModel) this.getModel()).addRow(schema, linkMode);
    }

    public Pair<AdsXmlSchemeDef, EXmlSchemaLinkMode> getSelectedRowValue() {
        LinkedSchemasTableModel model = (LinkedSchemasTableModel) this.getModel();
        
        if (getSelectedRow() != -1) {
            return (Pair) model.getValueAt(getSelectedRow(), 0);
        } else {
            return null;
        }
    }
}
