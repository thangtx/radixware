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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;


public class InsertTagCodeGeneratorFactory implements CodeGenerator.Factory {

    public static final String RADIX_OBJECT_FOR_TAG_CREATION = "radix-object-for-tag-creation";

    @Override
    public List<? extends CodeGenerator> create(Lookup context) {
        final ScmlEditorPane pane = context.lookup(ScmlEditorPane.class);
        if (pane == null) {
            return Collections.EMPTY_LIST;
        }
        RadixObject radixObject = (RadixObject) pane.getClientProperty(RADIX_OBJECT_FOR_TAG_CREATION);
        if (radixObject == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<? extends TagGeneratorFactory> factories = MimeLookup.getLookup(pane.getContentType()).lookupAll(TagGeneratorFactory.class);
        List<CodeGenerator> generators = new ArrayList<CodeGenerator>();
        if (factories != null) {
            for (TagGeneratorFactory factory : factories) {
                Collection<TagGenerator> creators = factory.getGenerators(pane, radixObject);
                if (creators != null) {
                    for (TagGenerator creator : creators) {
                        generators.add(new TagItemCodeGenerator(creator.createTagItem(), pane));
                    }
                }
            }
        }
        return generators;
    }

    private static class TagItemCodeGenerator implements CodeGenerator {

        private final TagItem tagItem;
        private final ScmlEditorPane pane;
        private final int offset;

        public TagItemCodeGenerator(TagItem tagItem, ScmlEditorPane pane) {
            this.tagItem = tagItem;
            this.pane = pane;
            this.offset = pane.getCaretPosition();
        }

        @Override
        public String getDisplayName() {
            return tagItem.getDisplayName();
        }

        @Override
        public void invoke() {
            pane.setCaretPosition(offset);
            pane.insertTag(tagItem.getTag());
            pane.requestFocusInWindow();
        }
    }
}
