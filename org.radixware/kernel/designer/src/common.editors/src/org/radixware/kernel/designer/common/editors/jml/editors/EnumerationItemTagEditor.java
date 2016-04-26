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

package org.radixware.kernel.designer.common.editors.jml.editors;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.editors.jml.JmlEditor;


public class EnumerationItemTagEditor extends TagEditor<JmlTagInvocation> {

    @Override
    protected boolean tagDefined() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "";
    }

    @Override
    protected void applyChanges() {
    }

    @Override
    protected void afterOpen() {
    }

    @Override
    protected void setReadOnly(boolean readOnly) {
    }

    @Override
    public boolean showModal() {
        if (getOpenInfo().isReadOnly()) {
            return false;
        }
        Definition enumDef = getObject().resolve(getOpenInfo().getLookup().lookup(JmlEditor.class).getContext());
        if (enumDef instanceof AdsEnumItemDef) {
            Definition selectedDef = ChooseDefinition.chooseDefinition(ChooseDefinitionCfg.Factory.newInstance(((AdsEnumItemDef) enumDef).getOwnerEnum().getItems().list(EScope.ALL)));
            if (selectedDef != null) {
                getObject().setPath(new AdsPath(selectedDef));
                return true;
            }
            return false;
        } else {
            throw new IllegalArgumentException("Tag doesn't point to enum item");
        }
    }
}
