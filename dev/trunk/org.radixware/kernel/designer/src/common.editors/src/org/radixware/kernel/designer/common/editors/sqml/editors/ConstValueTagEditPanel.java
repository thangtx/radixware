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

package org.radixware.kernel.designer.common.editors.sqml.editors;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.sqml.tags.ConstValueTag;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ProxyTagEditor;


public class ConstValueTagEditPanel<T extends ConstValueTag> extends ProxyTagEditor<T> {

    public ConstValueTagEditPanel() {
        super(null);
    }

    @Override
    public boolean showModal() {
        if (getOpenInfo().isReadOnly()) {
            return false;
        }
        final ConstValueTag tag = getTag();
        final IEnumDef enumDef = tag.findEnum();
        if (enumDef == null) {
            return false;
        }
        final List<Definition> items = new ArrayList<Definition>();
        for (IEnumDef.IItem item : enumDef.getItems().list(EScope.ALL)) {
            items.add((Definition) item);
        }
        final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(items);
        final Definition def = ChooseDefinition.chooseDefinition(cfg);
        if (def != null) {
            final IEnumDef.IItem item = (IEnumDef.IItem) def;
            tag.setItemId(item.getId());
            return true;
        } else {
            return false;
        }
    }
}
