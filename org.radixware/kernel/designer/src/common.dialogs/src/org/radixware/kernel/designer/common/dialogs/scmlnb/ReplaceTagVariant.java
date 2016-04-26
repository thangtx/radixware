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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ReplaceTagGenerator;


public interface ReplaceTagVariant {

    public List<ReplaceTagGenerator> getGenerators(ScmlEditorPane pane, Scml.Tag tag);

    public static abstract class TagItemCodeGenerator implements ReplaceTagGenerator {
//

        private final Scml.Tag tag;
        protected final ScmlEditorPane pane;

        public TagItemCodeGenerator(Scml.Tag tagItem, ScmlEditorPane pane) {
            this.tag = tagItem;
            this.pane = pane;
        }

        protected abstract Scml.Item[] createNewTag();

        @Override
        public void invoke() {
            Scml jml = tag.getOwnerScml();


            int[] offsetAndLen = pane.getScmlDocument().itemOffsetAndLength(jml.getItems().indexOf(tag));
            try {
                Scml.Item[] inv = createNewTag();
                if (inv != null && inv.length > 0) {
                    for (int i = inv.length - 1; i >= 0; i--) {
                        pane.getScmlDocument().insertItem(inv[i], offsetAndLen[0]);
                    }

                    int oldTagOffset = pane.getScmlDocument().getItemStartOffset(jml.getItems().indexOf(tag));
                    pane.getScmlDocument().remove(oldTagOffset, offsetAndLen[1]);
                    afterInsertTags(inv);
                    pane.update();
                }
            } catch (BadLocationException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }

        protected void afterInsertTags(Scml.Item[] inv) {
        }
    }
}
