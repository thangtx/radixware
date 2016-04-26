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

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;


class DdsReferenceReconnectProvider implements ReconnectProvider {

    @Override
    public void reconnectingStarted(ConnectionWidget connectionWidget, boolean reconnectingSource) {
    }

    @Override
    public void reconnectingFinished(ConnectionWidget connectionWidget, boolean reconnectingSource) {
    }

    @Override
    public boolean isSourceReconnectable(ConnectionWidget connectionWidget) {
        final DdsReferenceWidget refWidget = (DdsReferenceWidget) connectionWidget;
        final DdsReferenceDef ref = refWidget.getReference();

        if (ref.isReadOnly()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isTargetReconnectable(ConnectionWidget connectionWidget) {
        final DdsReferenceWidget refWidget = (DdsReferenceWidget) connectionWidget;
        final DdsReferenceDef ref = refWidget.getReference();

        if (ref.isReadOnly()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasCustomReplacementWidgetResolver(Scene scene) {
        return false;
    }

    private DdsTableDef findTable(Widget widget) {
        final DdsTableDef table;
        if (widget instanceof DdsTableWidget) {
            DdsTableWidget tableWidget = (DdsTableWidget) widget;
            table = tableWidget.getTable();
        } else if (widget instanceof DdsExtTableWidget) {
            DdsExtTableWidget extTableWidget = (DdsExtTableWidget) widget;
            DdsExtTableDef extTable = extTableWidget.getExtTable();
            table = extTable.findTable();
        } else {
            table = null;
        }
        return table;
    }

    @Override
    public ConnectorState isReplacementWidget(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {
        DdsTableDef table = findTable(replacementWidget);

        if (table != null) {
            return ConnectorState.ACCEPT;
        } else {
            return ConnectorState.REJECT;
        }
    }

    @Override
    public Widget resolveReplacementWidget(Scene scene, Point sceneLocation) {
        return null;
    }

    @Override
    public void reconnect(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource) {

        final DdsReferenceWidget refWidget = (DdsReferenceWidget) connectionWidget;
        final DdsReferenceDef ref = refWidget.getReference();

        if (ref.isReadOnly()) {
            return;
        }

        final DdsTableDef table = findTable(replacementWidget);
        if (table == null) {
            return;
        }

        final Id tableId = table.getId();
        if (reconnectingSource) {
            ref.setChildTableId(tableId);
        } else {
            ref.setParentTableId(tableId);
        }

        if (replacementWidget instanceof DdsExtTableWidget) {
            final DdsExtTableWidget extTableWidget = (DdsExtTableWidget) replacementWidget;
            final DdsExtTableDef extTable = extTableWidget.getExtTable();
            final Id extTableId = extTable.getId();
            if (reconnectingSource) {
                ref.setExtChildTableId(extTableId);
            } else {
                ref.setExtParentTableId(extTableId);
            }
        }

        refWidget.update();
    }
}
