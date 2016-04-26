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

package org.radixware.kernel.designer.ads.editors.enumeration.creation;

import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.defs.ads.module.AdsModule;

import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsPlatformEnumDef;

import org.radixware.kernel.common.defs.ads.platform.RadixPlatformEnum;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class AdsEnumCreature extends Creature<AdsEnumDef> {

    private String currentName;
    private AdsModule currentAdsModule;
    private Layer currentLayer;
    private AdsEnumDef overridenAdsEnumDefinition;
    private String platformPublishedClassName; //for E_CREATION_TYPE.PLATFORM_PUBLICATED_ENUM_CREATION
    private EValType eValType;
    private boolean isExtendable;
    private boolean isIdEnum = false;

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.ENUM;
    }

    public static enum E_CREATION_TYPE {

        UNDEFINED,
        SIMPLE_ENUM_CREATION,
        PUBLICATED_DDS_ENUM_CREATION,
        OVERRIDEN_ADS_ENUM_CREATION,
        PLATFORM_PUBLICATED_ENUM_CREATION;
    }
    private E_CREATION_TYPE creationType;

    public AdsEnumCreature(AdsModule container) {
        super(container.getDefinitions());

        platformPublishedClassName = "";
        currentName = "Enum"; //default name
        currentAdsModule = container;
        currentLayer = currentAdsModule.getSegment().getLayer();
        overridenAdsEnumDefinition = null;

        creationType = E_CREATION_TYPE.UNDEFINED;
        eValType = EValType.INT;
        isExtendable = false;
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {
            @Override
            public CreatureSetupStep createFirstStep() {
                return new NewAdsEnumDefinitionStep();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return currentLayer != null ? !currentLayer.isReadOnly() : false;
    }

    @Override
    public AdsEnumDef createInstance() {
        if (creationType == E_CREATION_TYPE.OVERRIDEN_ADS_ENUM_CREATION) {
            AdsEnumDef resultEnumDef = (AdsEnumDef) currentAdsModule.getDefinitions().overwrite(overridenAdsEnumDefinition);
            resultEnumDef.setName(currentName);
            return resultEnumDef;
        } else if (creationType == E_CREATION_TYPE.PLATFORM_PUBLICATED_ENUM_CREATION) {
            final RadixPlatformEnum source = AdsPlatformEnumDef.findPlatformEnum(currentAdsModule, platformPublishedClassName, null);
            return AdsEnumDef.Factory.newInstance(source);
        } else {
            return AdsEnumDef.Factory.newInstance();
        }
    }

    @Override
    public String getDisplayName() {
        return "Enumeration";
    }

    //invoking final settings
    @Override
    public boolean afterCreate(AdsEnumDef object) {

        switch (creationType) {
            case SIMPLE_ENUM_CREATION:
                object.setName(currentName);
                object.setItemType(eValType);
                if (object.getItemType() == EValType.STR) {
                    object.setIdEnum(isIdEnum());
                }
                break;
            /*   case OVERRIDEN_ADS_ENUM_CREATION:
             object.setName(currentName);
             object .overwrite(currentAdsModule, overridenAdsEnumDefinition);
             break;
             */
            case PUBLICATED_DDS_ENUM_CREATION:
                object.setName(currentName);
                object.setExtendable(isExtendable);
                break;
            case PLATFORM_PUBLICATED_ENUM_CREATION:
                object.setName(currentName);
                break;
            case UNDEFINED: //incorrect behaviour
            default:
                return false;
        }

        return true;
    }

    @Override
    public void afterAppend(AdsEnumDef object) {
        //do nothing
    }

    public String getName() {
        return currentName;
    }

    public void setName(String name) {
        currentName = name;
    }

    public void setPublishedClassName(String className) { //for E_CREATION_TYPE.PLATFORM_PUBLICATED_ENUM_CREATION
        this.platformPublishedClassName = className;
    }

    public Layer getLayer() {
        return currentLayer;
    }

    public AdsModule getModule() {
        return currentAdsModule;
    }

    public void setOverridenDefinition(AdsEnumDef overridenAdsEnumDefinition) {
        this.overridenAdsEnumDefinition = overridenAdsEnumDefinition;
    }

    public void setCreationType(E_CREATION_TYPE creationType) {
        this.creationType = creationType;
    }

    public E_CREATION_TYPE getCreationType() {
        return creationType;
    }

    public EValType getValType() {
        return eValType;
    }

    public void setValType(EValType eValType) {
        this.eValType = eValType;
    }

    public void setIsExtendable(boolean isExtendable) {
        this.isExtendable = isExtendable;
    }

    public boolean isIdEnum() {
        return isIdEnum;
    }

    public void setIdEnum(boolean isIdEnum) {
        this.isIdEnum = isIdEnum;
    }
}
