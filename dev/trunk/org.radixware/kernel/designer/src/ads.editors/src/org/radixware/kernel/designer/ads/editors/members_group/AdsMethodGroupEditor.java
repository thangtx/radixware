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

import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;


public class AdsMethodGroupEditor extends AbstractMembersGroupEditor<AdsMethodDef, AdsMethodGroup> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsMethodGroup> {

        @Override
        public IRadixObjectEditor<AdsMethodGroup> newInstance(AdsMethodGroup group) {
            return new AdsMethodGroupEditor(group);
        }
    }

    @Override
    protected ExtendableMembers<AdsMethodDef> getExtendableMembers() {
        if (getGroup() == null) {
            return null;
        }
        if (getGroup().getOwnerClass() == null) {
            return null;
        }
        return getGroup().getOwnerClass().getMethods();
    }

    protected AdsMethodGroupEditor(AdsMethodGroup methodGroup) {
        super(methodGroup);

    }

    @Override
    protected String getLeftComponentName() {
        return "Used methods";
    }

    @Override
    protected String getRightComponentName() {
        return "Available methods";
    }
}
