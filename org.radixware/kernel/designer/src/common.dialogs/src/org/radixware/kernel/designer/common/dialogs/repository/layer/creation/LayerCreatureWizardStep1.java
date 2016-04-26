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

package org.radixware.kernel.designer.common.dialogs.repository.layer.creation;

import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class LayerCreatureWizardStep1 extends CreatureSetupStep<LayerCreature, LayerCreatureWizardStep1Panel> {

    private LayerCreature creature;

    @Override
    public String getDisplayName() {
        return "New Layer Settings";
    }

    @Override
    protected LayerCreatureWizardStep1Panel createVisualPanel() {
        return new LayerCreatureWizardStep1Panel();
    }

    @Override
    public void open(LayerCreature creature) {
        super.open(creature);
        this.creature = creature;
        getVisualPanel().open(this);
    }

    public void setName(String name) {
        if (!name.equals(this.creature.name)) {
            this.creature.setName(name);
            fireChange();
        }
    }

    public void setTitle(String title) {
        if (!title.equals(this.creature.title)) {
            this.creature.setTitle(title);
            fireChange();
        }
    }

    public String getTitle() {
        return creature.title;
    }

    public String getName() {
        return creature.name;
    }

    public void setCopyright(String copyright) {
        if (!copyright.equals(this.creature.copyright)) {
            this.creature.copyright = copyright;
            fireChange();
        }
    }

    public String getCopyright() {
        return this.creature.copyright;
    }

    public Layer getPrevLayer() {
        return this.creature.prevLayer;
    }

    public void setPrevLayer(Layer layer) {
        this.creature.prevLayer = layer;
    }
    
    public void setIsLocalizing(boolean isLocalizing) {
        if(this.creature.isLocalizing!=isLocalizing){
            this.creature.isLocalizing = isLocalizing;
            fireChange();
        }
    }

    public Branch getBranch() {
        return this.creature.branch;
    }

    public void setURI(String URI) {
        if (!URI.equals(this.creature.URI)) {
            this.creature.URI = URI;
            fireChange();
        }
    }

    public String getURI() {
        return this.creature.URI;
    }

    public void setLanguages(List<EIsoLanguage> languages) {
        this.creature.setLanguages(languages);
        fireChange();
    }

    public List<EIsoLanguage> getLanguages() {
        return this.creature.languages;
    }

    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }

    @Override
    public boolean isFinishiable() {
        return true;
    }
}
