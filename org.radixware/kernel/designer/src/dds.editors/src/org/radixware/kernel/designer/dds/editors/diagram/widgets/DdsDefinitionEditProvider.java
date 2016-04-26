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
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.dds.DdsDefinition;

class DdsDefinitionEditProvider implements EditProvider {

    private DdsDefinitionEditProvider() {
        super();
    }

    @Override
    public void edit(Widget widget) {
        final DdsModelDiagram diagram = (DdsModelDiagram) widget.getScene();
        final Object obj = diagram.findObject(widget);

        if (obj instanceof DdsDefinition) {
            DdsDefinition definition = (DdsDefinition) obj;
            if (DdsEditorsManager.open(definition)) {
                diagram.update();
            }
        }
    }

    public static final class Factory {

        private Factory() {
        }
        private static final DdsDefinitionEditProvider instance = new DdsDefinitionEditProvider();

        public static DdsDefinitionEditProvider getDefault() {
            return instance;
        }
    }
}
