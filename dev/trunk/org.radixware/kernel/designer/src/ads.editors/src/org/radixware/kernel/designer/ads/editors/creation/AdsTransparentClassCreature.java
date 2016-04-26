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

package org.radixware.kernel.designer.ads.editors.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentPropertyDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.ClassMember;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.ads.editors.creation.AdsModuleDefinitionCreature;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class AdsTransparentClassCreature extends AdsModuleDefinitionCreature {

    public AdsTransparentClassCreature(AdsModule module) {
        super(module, EDefType.CLASS);
    }

    @Override
    public AdsDefinition createInstance() {
        final PlatformLib lib = ((AdsSegment) getAdsModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(targetEnv);
        final RadixPlatformClass platformClass = lib.findPlatformClass(platformClassName);
        return PublicationUtils.createTransparentClass(platformClass, lib, targetEnv);
    }

    @Override
    public boolean afterCreate(AdsDefinition object) {

        if (object instanceof AdsClassDef) {
            final AdsClassDef classDef = (AdsClassDef) object;
            final PlatformLib lib = ((AdsSegment) module.getSegment()).getBuildPath().getPlatformLibs().getKernelLib(targetEnv);
            final RadixPlatformClass platformClass = lib.findPlatformClass(platformClassName);

            PublicationUtils.setupTransparentClass(classDef, platformClass, className, isExtendable, platformClassName);

            return true;
        }
        return false;
    }

    @Override
    public void afterAppend(AdsDefinition object) {
        if (object instanceof AdsClassDef) {
            AdsClassDef def = (AdsClassDef) object;
            for (final RadixPlatformClass.ClassMember member : getPublishedMembers()) {
                if (member instanceof Method) {
                    Method method = (Method) member;

                    final AdsTransparentMethodDef adsMethod;
                    if (method.isConstructor()) {
                        adsMethod = AdsTransparentMethodDef.Factory.newConstructorInstance();
                    } else {
                        adsMethod = AdsTransparentMethodDef.Factory.newInstance();
                    }
                    PublicationUtils.setupTransparentMethodDef(adsMethod, method, def, false);
                    def.getMethods().getLocal().add(adsMethod);
                } else if (member instanceof RadixPlatformClass.Field) {
                    RadixPlatformClass.Field field = (RadixPlatformClass.Field) member;

                    final AdsTransparentPropertyDef property = AdsTransparentPropertyDef.Factory.newInstance();

                    PublicationUtils.setupTransparentFieldDef(property, field);
                    def.getProperties().getLocal().add(property);
                }
            }
        }
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AdsTransparentClassCreature.class, "Type-Display-Name-TransparentClass");
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(AdsTransparentClassCreature.class, "PublisingCreatureDescription");
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new AdsTransparentClassWizardStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }

        };
    }

    AdsModule getAdsModule() {
        return this.module;
    }

    //SETTINGS

    private String className;
    private boolean isExtendable;
    private String platformClassName;
    private Collection<RadixPlatformClass.ClassMember> publishedMembers;

    void setEnvironment(ERuntimeEnvironmentType type) {
        targetEnv = type;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setIsExtendable(boolean isExtendable) {
        this.isExtendable = isExtendable;
    }

    public void setPlatformClass(String platformClass) {
        this.platformClassName = platformClass;
    }

    public void setPublishedMembers(Collection<RadixPlatformClass.ClassMember> publishedMembers) {
        this.publishedMembers = new ArrayList<>(publishedMembers);
    }

    public String getPlatformClass() {
        return this.platformClassName;
    }

    public Collection<ClassMember> getPublishedMembers() {
        return publishedMembers != null ? publishedMembers : Collections.<RadixPlatformClass.ClassMember>emptyList();
    }

    public ERuntimeEnvironmentType getEnvironment() {
        return this.targetEnv;
    }

}
