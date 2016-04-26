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
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentPropertyDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class TransparentPropertyCreature extends TransparentCreature<AdsPropertyDef, RadixPlatformClass.Field> {

    private final AdsPropertyGroup group;

    public TransparentPropertyCreature(AdsClassDef ownerClass, AdsPropertyGroup group, RadixObjects<AdsPropertyDef> container) {
        super(ownerClass, container);

        this.group = group;
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(TransparentPropertyCreature.class, "TransparentPropertyCreature-Description");
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(TransparentMethodCreature.class, "TransparentPropertyCreature-DisplayName");
    }

    @Override
    public AdsTransparentPropertyDef createInstance() {
        return AdsTransparentPropertyDef.Factory.newInstance();
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.Property.PROPERTY_DYNAMIC;
    }

    @Override
    public boolean afterCreate(AdsPropertyDef object) {
        PublicationUtils.setupTransparentFieldDef((AdsTransparentPropertyDef) object, getMemberSource());
        return true;
    }

    @Override
    public void afterAppend(AdsPropertyDef object) {
        group.addMember(object);
    }

    @Override
    public EClassMemberType getClassMemberType() {
        return EClassMemberType.FIELD;
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new PublishFieldStep();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }
}
