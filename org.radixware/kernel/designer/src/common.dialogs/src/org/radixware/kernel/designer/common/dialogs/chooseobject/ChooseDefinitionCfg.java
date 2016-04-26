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

package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.util.Collection;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;

/**
 * Options for choose definition panel or dialog.
 */
public class ChooseDefinitionCfg {

    public static final int FIRST_STEP = 1;
    private final RadixObject context;
    private final VisitorProvider provider;
    private volatile boolean forOverwrite = false;
    private String typeTitle = Definition.DEFINITION_TYPE_TITLE;
    private String typesTitle = Definition.DEFINITION_TYPES_TITLE;
    private int stepCount = FIRST_STEP;
    private final Collection<? extends Definition> predefinedAllowedDefinitions;
    private EChooseDefinitionDisplayMode displayMode = EChooseDefinitionDisplayMode.NAME_AND_LOCATION;
    private boolean forAlgo = false;

    protected ChooseDefinitionCfg(final RadixObject context, final VisitorProvider provider) {
        this.context = context;
        this.provider = provider;
        this.predefinedAllowedDefinitions = null;
    }

    protected ChooseDefinitionCfg(final Collection<? extends Definition> predefinedAllowedDefinitions) {
        this.context = null;
        this.provider = null;
        this.predefinedAllowedDefinitions = predefinedAllowedDefinitions;
    }
    
    
    public IChooseDefinitionAdditionTextProvider getAdditionTextProvider(){
        return null;
    }

    /**
     * Get context for Choose Definition dialog
     * @return context.
     */
    public RadixObject getContext() {
        return context;
    }

    /**
     * Get provider for Choose Definition dialog
     * @return context.
     */
    public VisitorProvider getProvider() {
        return provider;
    }

    /**
     * @return true if visibility scope is bounded by overwritten module of the context, false if visibility scope is not bounded.
     */
    public boolean isForOverwrite() {
        return forOverwrite;
    }

    /**
     * Set true if you want to bound visibility scope by overwritten module of the context, false otherwise, default - false.
     */
    public void setForOverwrite(boolean forOverwrite) {
        this.forOverwrite = forOverwrite;
    }

    /**
     * Get title of choosed object type.
     */
    public String getTypeTitle() {
        return typeTitle;
    }

    /**
     * Set title choosed object type. 'Definition' by default.
     */
    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }

    /**
     * Get title of choosed object type in plural form.
     */
    public String getTypesTitle() {
        return typesTitle;
    }

    /**
     * Set title of choosed object type in plural form. 'Definitions' by default.
     */
    public void setTypesTitle(String typesTitle) {
        this.typesTitle = typesTitle;
    }

    /**
     * Get level of grouping of object owners.
     */
    public int getStepCount() {
        return stepCount;
    }

    /**
     * Set level of grouping of object owners. 1 by default.
     */
    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public void setForAlgo(boolean forAlgo) {
        this.forAlgo = forAlgo;
        this.stepCount = 2;
    }
    
    public boolean isForAlgo() {
        return forAlgo;
    }
            
    public EChooseDefinitionDisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(EChooseDefinitionDisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public static final class Factory {

        private Factory() {
        }

        public static ChooseDefinitionCfg newInstance(RadixObject context, VisitorProvider provider) {
            return new ChooseDefinitionCfg(context, provider);
        }

        public static ChooseDefinitionCfg forOverwrite(Definition context, VisitorProvider provider) {
            final ChooseDefinitionCfg cfg = new ChooseDefinitionCfg(context, provider);
            cfg.setForOverwrite(true);
            return cfg;
        }

        public static ChooseDefinitionCfg newInstance(final Collection<? extends Definition> predefinedAllowedDefinitions) {
            return new ChooseDefinitionCfg(predefinedAllowedDefinitions);
        }
    }

    public Collection<? extends Definition> collectAllowedDefinitions() {
        if (predefinedAllowedDefinitions != null) {
            return predefinedAllowedDefinitions;
        }

        final Definition contextAsDefinition = context.getDefinition();
        final Collection<Definition> allowedDefinitions;

        if (contextAsDefinition != null) {
            if (forOverwrite) {
                allowedDefinitions = DefinitionsUtils.collectForOverwrite(contextAsDefinition, provider);
            } else {
                allowedDefinitions = DefinitionsUtils.collectAllAround(contextAsDefinition, provider);
            }
        } else {
            allowedDefinitions = DefinitionsUtils.collectAllAround(context, provider);
        }

        provider.setCancelled(false);
        return allowedDefinitions;
    }
}
