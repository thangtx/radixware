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

package org.radixware.kernel.designer.ads.editors.members_group;

import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;


public class AdsPropertyGroupEditor extends AbstractMembersGroupEditor<AdsPropertyDef, AdsPropertyGroup>{

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsPropertyGroup> {

        @Override
        public IRadixObjectEditor<AdsPropertyGroup> newInstance(AdsPropertyGroup group) {
            return new AdsPropertyGroupEditor(group);
        }
    }

    protected AdsPropertyGroupEditor(AdsPropertyGroup group) {
        super(group);
    }

    @Override
    protected ExtendableMembers<AdsPropertyDef> getExtendableMembers() {
        return getGroup().getOwnerClass().getProperties();
    }

    @Override
    protected String getLeftComponentName() {
        return "Used properties";
    }

    @Override
    protected String getRightComponentName() {
        return "Available properties";
    }
}
