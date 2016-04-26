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

import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsLabelDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.dds.editors.DdsLabelEditor;
import org.radixware.kernel.designer.dds.editors.DdsSequenceEditor;
import org.radixware.kernel.designer.dds.editors.reference.DdsReferenceEditor;
import org.radixware.kernel.designer.dds.editors.table.DdsTableEditor;

/**
 * Allows to edit DDS diagram definitions
 */
public class DdsEditorsManager {

    public static boolean open(final DdsDefinition definition) {
        final OpenInfo openInfo = new OpenInfo(definition);
        return open(definition, openInfo);
    }

    public static boolean open(DdsDefinition definition, final OpenInfo openInfo) {
        final boolean result;

        if (definition instanceof DdsExtTableDef) {
            // open table editor by ext table
            final DdsExtTableDef extTable = (DdsExtTableDef) definition;
            final DdsTableDef table = extTable.getTable();
            if (table != null) {
                definition = table;
            }
        }

        if (definition instanceof DdsTableDef) {
            result = EditorsManager.getDefault().open((DdsTableDef) definition, openInfo, new DdsTableEditor.Factory());
        } else if (definition instanceof DdsReferenceDef) {
            result = EditorsManager.getDefault().open((DdsReferenceDef) definition, openInfo, new DdsReferenceEditor.Factory());
        } else if (definition instanceof DdsSequenceDef) {
            result = EditorsManager.getDefault().open((DdsSequenceDef) definition, openInfo, new DdsSequenceEditor.Factory());
        } else if (definition instanceof DdsLabelDef) {
            result = EditorsManager.getDefault().open((DdsLabelDef) definition, openInfo, new DdsLabelEditor.Factory());
        } else if (definition instanceof DdsExtTableDef) {
            result = true;
        } else {
            result = EditorsManager.getDefault().open(definition, openInfo);
        }

        if (result) {
            final DdsModelDef model = definition.getOwnerModel();
            if (model != null) {
                EditorsManager.getDefault().updateEditorIfOpened(model);
            }
        }
        return result;
    }
}
