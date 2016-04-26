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


package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JTextArea;


public class SizableTextArea extends JTextArea {
    private int rowCount;

    public SizableTextArea(int rowCount) {
        this.rowCount = rowCount;
        setRows(rowCount);
    }

    public Dimension calcPreferredSize() {
        Dimension d = getPreferredSize();
        d = (d == null) ? new Dimension(400,400) : d;
        
        final Insets insets = getInsets();

        if (getColumns() != 0) {
            d.width = Math.max(d.width, getColumns() * getColumnWidth() + insets.left + insets.right);
        }
        if (rowCount != 0) {
            d.height = rowCount * getRowHeight() + insets.top + insets.bottom;
        }
        return d;
    }
    
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        setRows(rowCount);
    }
}
