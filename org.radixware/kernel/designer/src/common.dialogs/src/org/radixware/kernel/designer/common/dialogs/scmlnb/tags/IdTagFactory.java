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

package org.radixware.kernel.designer.common.dialogs.scmlnb.tags;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import org.netbeans.api.editor.mimelookup.MimePath;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.IdTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;


public class IdTagFactory {

    public static Scml.Tag createIdTag(Definition definition, ScmlEditorPane pane) {
        if (definition == null || pane == null) {
            return null;
        }



        if (pane.getContentType() == null) {
            return null;
        }
        Scml.Tag tag = null;
        if (pane.getContentType().contains("x-jml")) {
            if (definition instanceof AdsDefinition) {
                tag = new JmlTagId((AdsDefinition) definition);
            } else if (definition instanceof Module) {
                tag = new JmlTagId((Module) definition);
            } else if (definition instanceof DdsTableDef) {
                tag = new JmlTagId((DdsTableDef) definition);
            } else if (definition instanceof DdsAccessPartitionFamilyDef) {
                tag = new JmlTagId((DdsAccessPartitionFamilyDef) definition);
            }
        } else if (pane.getContentType().contains("x-sqml")) {
            Id[] path = definition.getIdPath();
            tag = IdTag.Factory.newInstance(path);
        }

        return tag;
    }
}
