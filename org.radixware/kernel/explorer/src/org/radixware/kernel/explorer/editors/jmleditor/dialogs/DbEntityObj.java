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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public class DbEntityObj extends DefInfo {

    private final RadClassPresentationDef classDef;
    private GroupModel groupModel = null;

    public DbEntityObj(final IClientEnvironment environment, final Id classId) {
        super(classId, null, null, false, ERuntimeEnvironmentType.SERVER, null);
        classDef = environment.getApplication().getDefManager().getClassPresentationDef(classId);
        final RadSelectorPresentationDef selPresentDef = classDef.getDefaultSelectorPresentation();
        if (selPresentDef != null) {
            groupModel = GroupModel.openTableContextlessSelectorModel(environment, selPresentDef);
        }
    }

    public GroupModel getGroupModel() {
        return groupModel;
    }

    @Override
    public String getName() {
        if ((classDef.getClassTitle() == null) || (classDef.getClassTitle().equals(""))) {
            return classDef.getName();
        }
        return classDef.getClassTitle();
    }
}
