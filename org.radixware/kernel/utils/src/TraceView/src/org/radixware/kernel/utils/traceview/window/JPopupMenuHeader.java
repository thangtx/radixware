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
package org.radixware.kernel.utils.traceview.window;

import org.radixware.kernel.utils.traceview.utils.TraceViewUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.radixware.kernel.utils.traceview.TraceViewSettings.EEventColumn;

class JPopupMenuHeader extends JPopupMenu {

    private final JTable table;
    private final JTable fictiveTableForObtainingTableDimensions;
    private final WindowMode window;

    public JPopupMenuHeader(JTable table, JTable fictiveTableForObtainingTableDimensions, TabSettings settings, WindowMode window) {
        this.table = table;
        this.fictiveTableForObtainingTableDimensions = fictiveTableForObtainingTableDimensions;
        this.window = window;
        for (EEventColumn column : EEventColumn.values()) {
            addJCheckBoxMenuItem(column.getName(), column.getIndex(), settings);
        }
    }

    public final void addJCheckBoxMenuItem(String name, int index, TabSettings settings) {
        JCheckBoxMenuItem jcheckBoxMenuItem = new JCheckBoxMenuItem(name);
        jcheckBoxMenuItem.setState(window.getColumnState(index));
        jcheckBoxMenuItem.addActionListener(new MenuItemActionListener(index, settings));
        add(jcheckBoxMenuItem);
    }

    private class MenuItemActionListener implements ActionListener {

        private final int index;
        private final TabSettings settings;

        public MenuItemActionListener(int index, TabSettings settings) {
            this.index = index;
            this.settings = settings;
            recalculation();
        }

        public final void recalculation() {
            if (window.getColumnState(index)) {
                int width = (index == EEventColumn.MESSAGE.getIndex()) ? 1000
                        : TraceViewUtils.getPreferredWidthColumn(index, fictiveTableForObtainingTableDimensions);
                table.getColumnModel().getColumn(index).setMaxWidth(100000);
                table.getColumnModel().getColumn(index).setWidth(width);
            } else {
                table.getColumnModel().getColumn(index).setWidth(0);
                table.getColumnModel().getColumn(index).setMaxWidth(0);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            window.setColumnState(index, ((JCheckBoxMenuItem) e.getSource()).getState());
            recalculation();
        }
    }
}
