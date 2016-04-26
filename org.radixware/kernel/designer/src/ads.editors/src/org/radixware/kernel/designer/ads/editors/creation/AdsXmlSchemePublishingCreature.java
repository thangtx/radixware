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

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class AdsXmlSchemePublishingCreature extends Creature<AdsXmlSchemeDef> {

    private AdsModule module;
    private String publishingUrl = "";
    private ERuntimeEnvironmentType environment = ERuntimeEnvironmentType.COMMON;

    public AdsXmlSchemePublishingCreature(AdsModule module){
        super(module.getDefinitions());
        this.module = module;
    }

    public AdsModule getModule(){
        return this.module;
    }

    public void setPublishingUrl(String url){
        this.publishingUrl = url;
    }

    public void setTargetEnvironment(ERuntimeEnvironmentType environment){
        this.environment = environment;
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new WizardInfo() {

            @Override
            public CreatureSetupStep createFirstStep() {
                return new AdsXmlSchemePublishingWizardStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public void afterAppend(AdsXmlSchemeDef object) {
    }

    @Override
    public boolean afterCreate(AdsXmlSchemeDef object) {
        object.setTargetEnvironment(environment);
        return true;
    }

    @Override
    public AdsXmlSchemeDef createInstance() {
        if (publishingUrl != null && !publishingUrl.isEmpty()){
            return AdsXmlSchemeDef.Factory.newInstance(publishingUrl);
        }
        return null;
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(AdsXmlSchemePublishingCreature.class, "XmlPublisingCreature-Description");
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.XML_SCHEME;
    }

    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(AdsXmlSchemePublishingCreature.class, "XmlPublishingCreature-DisplayName");
    }

}
