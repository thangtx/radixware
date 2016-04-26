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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;


public abstract class InsertTagByDefinitionAction extends ScmlInsertTagAction {

    public InsertTagByDefinitionAction(String name, ScmlEditor editor) {
        super(name, editor);
    }

    @Override
    public List<Tag> createTags() {
        List<Tag> tags = new LinkedList<Tag>();
        List<Definition> definitions = chooseDefinitions();
        if (definitions != null) {
            for (Definition definition : definitions) {
                Scml.Tag tag = createTag(definition);
                if (tag != null) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    protected abstract List<Definition> chooseDefinitions();

    protected abstract Scml.Tag createTag(Definition definition);
}
