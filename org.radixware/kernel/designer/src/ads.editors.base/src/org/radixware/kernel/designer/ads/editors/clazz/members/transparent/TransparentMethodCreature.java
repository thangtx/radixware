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

package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class TransparentMethodCreature extends TransparentCreature<AdsMethodDef, RadixPlatformClass.Method> {

    private final AdsMethodGroup group;

    public TransparentMethodCreature(AdsClassDef ownerClass, AdsMethodGroup group, RadixObjects<AdsMethodDef> container) {
        super(ownerClass, container);

        this.group = group;
    }

    @Override
    public AdsTransparentMethodDef createInstance() {
        if (getMemberSource() != null && getMemberSource().isConstructor()) {
            return AdsTransparentMethodDef.Factory.newConstructorInstance();
        } else {
            return AdsTransparentMethodDef.Factory.newInstance();
        }
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(TransparentMethodCreature.class, "TransparentMethodCreature-Description");
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TransparentMethodCreature.class, "TransparentMethodCreature-DisplayName");
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.Method.METHOD;
    }

    @Override
    public boolean afterCreate(AdsMethodDef object) {
        PublicationUtils.setupTransparentMethodDef((AdsTransparentMethodDef) object, getMemberSource(), getOwnerClass(), false);
        return true;
    }

    @Override
    public void afterAppend(AdsMethodDef object) {
        group.addMember(object);

//        if (EnvSelectorPanel.isMeaningFullFor(object)) {
//            object.setUsageEnvironment(object.getUsageEnvironment());
//        }
    }

    @Override
    public EClassMemberType getClassMemberType() {
        return EClassMemberType.METHOD;
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new PublishMethodStep();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }
}
