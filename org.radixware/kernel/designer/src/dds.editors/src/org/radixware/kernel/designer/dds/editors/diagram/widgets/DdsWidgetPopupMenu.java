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

package org.radixware.kernel.designer.dds.editors.diagram.widgets;

import org.radixware.kernel.designer.dds.editors.diagram.*;
import java.awt.Point;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

/**
 * Common part of popup menu of DdsModel and its widgets.
 * Realises common features: clipboard, find usages...
 */
class DdsWidgetPopupMenu implements PopupMenuProvider {

    private final DdsModelDiagram diagram;
    private final DdsDefinition definition;

    public DdsWidgetPopupMenu(DdsModelDiagram diagram, DdsDefinition definition) {
        this.diagram = diagram;
        this.definition = definition;
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        final JPopupMenu popupMenu = DialogUtils.createPopupMenu(diagram.getView());
        return popupMenu;
    }
}
