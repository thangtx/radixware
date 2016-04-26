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

package org.radixware.kernel.designer.common.editors.sqml;

import org.radixware.kernel.designer.common.dialogs.scmlnb.ReplaceTagGenerator;
import org.radixware.kernel.designer.common.editors.jml.*;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ReplaceTagVariant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.jml.JmlTagDbName;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.IdTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;


@MimeRegistration(mimeType = "text/x-sqml", service = ReplaceTagVariant.class)
public class ReplaceTagVariantImpl implements ReplaceTagVariant {

    @Override
    public List<ReplaceTagGenerator> getGenerators(ScmlEditorPane pane, Scml.Tag tag) {
        if (tag.isReadOnly()) {
            return Collections.emptyList();
        }
        if (tag instanceof IdTag) {
            IdTag id = (IdTag) tag;
            Definition def = id.findTarget();

            List<ReplaceTagGenerator> items = new LinkedList<ReplaceTagGenerator>();
            items.add(new SwapIdSoftness(id, pane));
            return items;

        }
        return Collections.emptyList();
    }

    static class SwapIdSoftness extends TagItemCodeGenerator {

        private boolean setSoft;
        private Id[] path;
        private int mode;
        private boolean isDbName = false;

        public SwapIdSoftness(IdTag tagItem, ScmlEditorPane pane) {
            super(tagItem, pane);

            this.setSoft = !tagItem.isSoftReference();
            this.path = tagItem.getPath();
        }

        @Override
        protected Item[] createNewTag() {
            IdTag id = IdTag.Factory.newInstance(path, setSoft);            
            return new Item[]{id};
        }

        @Override
        public String getDisplayName() {
            if (setSoft) {
                return "Convert to soft ID reference (no module dependences used for resolution)";
            } else {
                return "Convert to hard ID reference (resolved using module dependences)";
            }
        }

        @Override
        protected void afterInsertTags(Scml.Item[] inv) {
            if (inv.length > 0) {
                Module module = inv[0].getModule();
                if (module != null) {
                    module.getDependences().actualize();
                }
            }
        }
    }
}