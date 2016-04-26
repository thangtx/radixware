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

package org.radixware.kernel.designer.ads.editors.msdl.creation;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class MsdlCreature extends Creature<AdsMsdlSchemeDef> {

    private String name;
    private String namespace;
    private final Layer currentLayer;

    public MsdlCreature(AdsModule container) {
        super(container.getDefinitions());

        currentLayer = container.getLayer();
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.MSDL_SCHEME;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getDisplayName() {
        return "Msdl Schema";
    }

    @Override
    public WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {
            @Override
            public CreatureSetupStep createFirstStep() {
                return new SetupStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public boolean isEnabled() {
        return currentLayer != null ? !currentLayer.isReadOnly() : false;
    }

    public String getDisplayedName() {
        return NbBundle.getMessage(MsdlCreature.class, "Type-Display-Name-MsdlScheme");
    }

    @Override
    public AdsMsdlSchemeDef createInstance() {
        return AdsMsdlSchemeDef.Factory.newInstance();
    }

    @Override
    public boolean afterCreate(AdsMsdlSchemeDef object) {
        object.setName(name);
        object.setNamespace(namespace);
        return true;
    }

    @Override
    public void afterAppend(AdsMsdlSchemeDef object) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
