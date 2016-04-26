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

package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;


final class PresenterCellRenderer extends DefaultTableCellRenderer {

    private final static Color DARK_GREEN = new Color(0, 128, 0);
    private Color defaultForeground;
    private Color defaultBackground;

    public PresenterCellRenderer() {
        super();

        this.defaultForeground = UIManager.getColor("Label.foreground");
        this.defaultBackground = UIManager.getColor("Label.background");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ClassMemberPresenter) {
            ClassMemberPresenter presentation = (ClassMemberPresenter) value;

            if (!presentation.isCorrect()) {
                setForeground(Color.RED);
            } else if (presentation.isMarkedToBePublished()) {
                setForeground(DARK_GREEN);
            } else {
                setForeground(defaultForeground);
            }

            setBackground(defaultBackground);
            setIcon(presentation.getIcon());
            setText(presentation.toString());
        }
        return this;
    }
}
