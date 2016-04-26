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

import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;


public class SimpleTable extends JTable {

    public SimpleTable() {
        super();

        final int rowHeight = calcRowHeight();

        if (rowHeight > -1) {
            setRowHeight(rowHeight);
        }
    }

    protected final void insertRowHeightUpdater(TableModel dataModel) {

        if (dataModel != null) {
            dataModel.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    invokeUpdateRowHeight();
                }
            });

            invokeUpdateRowHeight();
        }
    }

    protected final void invokeUpdateRowHeight() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updateRowHeight();
            }
        });
    }

    /**
     * Update height of each row by its content.
     */
    protected void updateRowHeight() {
        for (int row = 0; row < getRowCount(); ++row) {

            int height = getRowHeight(row);

            for (int col = 0; col < getColumnCount(); col++) {
                final Component renderer = prepareRenderer(getCellRenderer(row, col), row, col);
                height = Math.max(height, renderer.getPreferredSize().height);
            }

            setRowHeight(row, height);
        }
    }

    /**
     * Calculate row height by font.
     *
     * @return row height
     */
    protected final int calcRowHeight() {
        final Font font = getFont();
        if (font != null) {
            final FontMetrics fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(getFont());
            return fontMetrics.getHeight() + 2;
        }

        return -1;
    }
}
