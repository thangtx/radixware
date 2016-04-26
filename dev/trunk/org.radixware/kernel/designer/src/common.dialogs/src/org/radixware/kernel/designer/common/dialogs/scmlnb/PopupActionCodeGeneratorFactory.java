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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.util.Lookup;
import org.radixware.kernel.common.scml.Scml;


public class PopupActionCodeGeneratorFactory implements CodeGenerator.Factory {

    public static final String RADIX_OBJECT_FOR_TAG_REPLACEMENT = "radix-object-for-tag-replacement";

    @Override
    public List<? extends CodeGenerator> create(Lookup context) {
        final ScmlEditorPane pane = context.lookup(ScmlEditorPane.class);
        if (pane == null) {
            return Collections.EMPTY_LIST;
        }
        Scml.Tag radixObject = (Scml.Tag) pane.getClientProperty(RADIX_OBJECT_FOR_TAG_REPLACEMENT);
        if (radixObject == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<? extends ReplaceTagVariant> variants = MimeLookup.getLookup(pane.getContentType()).lookupAll(ReplaceTagVariant.class);
        List<CodeGenerator> generators = new ArrayList<CodeGenerator>();
        if (variants != null) {
            for (ReplaceTagVariant factory : variants) {
                Collection<ReplaceTagGenerator> creators = factory.getGenerators(pane, radixObject);
                if (creators != null) {
                    for (ReplaceTagGenerator creator : creators) {
                        generators.add(creator);
                    }
                }
            }
        }
        return generators;
    }
}
