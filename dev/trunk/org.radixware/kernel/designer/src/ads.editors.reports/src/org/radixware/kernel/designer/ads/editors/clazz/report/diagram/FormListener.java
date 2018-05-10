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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Component;
import javax.swing.JPopupMenu;
import org.radixware.kernel.designer.ads.editors.clazz.report.diagram.selection.SelectionEvent;


class FormListener extends WidgetMouseListener {

    private final AdsReportFormDiagram formDiagram;

    public FormListener(final AdsReportFormDiagram formDiagram) {
        super();
        this.formDiagram = formDiagram;
    }

    @Override
    protected void edit() {
        formDiagram.edit();
    }

    @Override
    protected void select(final boolean expand) {
        // NOTHING
    }

    @Override
    protected void popup(final Component component,final int x,final int y) {
        formDiagram.fireSelectionChanged(new SelectionEvent(formDiagram, true));
        final JPopupMenu popupMenu = AdsReportWidgetUtils.getPopupMenu(formDiagram);
        popupMenu.show(component, x, y);
    }
}

