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

import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsLabelDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.designer.dds.editors.diagram.DdsModelDiagram;

/**
 * Factory to create DDS definitions nodes.
 * Allows to incapsulate widget type.
 */
public final class DdsDefinitionWidgetFactory {

    private DdsDefinitionWidgetFactory() {
    }

    public static Widget newInstance(DdsModelDiagram diagram, DdsDefinition definition) {
        if (definition instanceof DdsTableDef) {
            return new DdsTableWidget(diagram, (DdsTableDef) definition);
        } else if (definition instanceof DdsExtTableDef) {
            return new DdsExtTableWidget(diagram, (DdsExtTableDef) definition);
        } else if (definition instanceof DdsSequenceDef) {
            return new DdsSequenceWidget(diagram, (DdsSequenceDef) definition);
        } else if (definition instanceof DdsLabelDef) {
            return new DdsLabelWidget(diagram, (DdsLabelDef) definition);
        } else if (definition instanceof DdsReferenceDef) {
            return new DdsReferenceWidget(diagram, (DdsReferenceDef) definition);
        } else {
            throw new IllegalStateException("Attemp to display unsupported object in DDS diagram: " + String.valueOf(definition));
        }
    }
}
