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
import java.util.Collections;
import java.util.HashSet;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;

/**
 * Configuration for ConfigureDefinitionListPanel.
 */
public class ConfigureDefinitionListCfg {

    private final Collection<? extends Definition> availableDefinitions;
    private final Collection<Id> disabledIds;

    /**
     * Use Factory
     */
    protected ConfigureDefinitionListCfg(Collection<? extends Definition> availableDefinitions, Collection<Id> disabledIds) {
        this.availableDefinitions = availableDefinitions;
        this.disabledIds = disabledIds;
    }

    public Collection<? extends Definition> getAvailableDefinitions() {
        return availableDefinitions;
    }

    public Collection<Id> getDisabledIds() {
        return disabledIds;
    }

    public static class Factory {

        private Factory() {
        }

        /**
         * @param availableDefinitions - list of alailable to choose definitions.
         * @param enabledIds - list of enabled to add or remove identifiers.
         */
        public static ConfigureDefinitionListCfg newInstanceWithEnabledIds(Collection<? extends Definition> availableDefinitions, Collection<Id> enabledIds) {
            Collection<Id> disabledIds = new HashSet<Id>();

            for (Definition definition : availableDefinitions) {
                if (!enabledIds.contains(definition.getId())) {
                    disabledIds.add(definition.getId());
                }
            }

            ConfigureDefinitionListCfg cfg = new ConfigureDefinitionListCfg(availableDefinitions, disabledIds);
            return cfg;
        }

        /**
         * @param availableDefinitions - list of alailable to choose definitions.
         * @param disabledIdos - list of disabled to add or remove identifiers.
         */
        public static ConfigureDefinitionListCfg newInstanceWithDisabledIds(Collection<? extends Definition> availableDefinitions, Collection<Id> disabledIds) {
            ConfigureDefinitionListCfg cfg = new ConfigureDefinitionListCfg(availableDefinitions, disabledIds);
            return cfg;
        }

        /**
         * @param availableDefinitions - list of alailable to choose definitions.
         */
        public static ConfigureDefinitionListCfg newInstance(Collection<? extends Definition> availableDefinitions) {
            Collection<Id> forbiddenIds = Collections.emptyList();
            ConfigureDefinitionListCfg cfg = new ConfigureDefinitionListCfg(availableDefinitions, forbiddenIds);
            return cfg;
        }

        private static Collection<Definition> collect(Definition context, VisitorProvider provider){
            return DefinitionsUtils.collectTopAround(context, provider);
        }

        /**
         * @param context - context for visitor provider.
         * @param provider - visitor provider that allows to collect list of alailable to choose definitions.
         */
        public static ConfigureDefinitionListCfg newInstance(Definition context, VisitorProvider provider) {
            Collection<Definition> availableDefinitions = collect(context, provider);
            return newInstance(availableDefinitions);
        }

        /**
         * @param context - context for visitor provider.
         * @param provider - visitor provider that allows to collect list of alailable to choose definitions.
         * @param enabledIds - list of enabled to add or remove identifiers.
         */
        public static ConfigureDefinitionListCfg newInstanceWithEnabledList(Definition context, VisitorProvider provider, Collection<Id> enabledIds) {
            Collection<Definition> availableDefinitions = collect(context, provider);
            return newInstanceWithEnabledIds(availableDefinitions, enabledIds);
        }

        /**
         * @param context - context for visitor provider.
         * @param provider - visitor provider that allows to collect list of alailable to choose definitions.
         * @param disabledIds - list of disabled to add or remove identifiers.
         */
        public static ConfigureDefinitionListCfg newInstanceWithDisabledList(Definition context, VisitorProvider provider, Collection<Id> disabledIds) {
            Collection<Definition> availableDefinitions = collect(context, provider);
            return newInstanceWithDisabledIds(availableDefinitions, disabledIds);
        }
    }
}
