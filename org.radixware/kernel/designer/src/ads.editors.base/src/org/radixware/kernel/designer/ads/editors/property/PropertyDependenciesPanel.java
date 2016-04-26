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

package org.radixware.kernel.designer.ads.editors.property;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


final class PropertyDependenciesPanel extends DependentItemsPanel {

    public void open(AdsPropertyPresentationPropertyDef prop) {
        open(new ItemProvider(prop));
    }

    private static class ItemProvider implements DependentItemsPanel.IDependenciesProvider {

        protected AdsPropertyPresentationPropertyDef property;

        public ItemProvider(AdsPropertyPresentationPropertyDef prop) {
            this.property = prop;
        }

        @Override
        public void remove(Id id) {
            property.getDependents().removeDependent(id);
        }

        @Override
        public void add(Id id) {
            property.getDependents().addDependent(id);
        }

        @Override
        public List<AdsDefinition> getAvaliableItems() {
            final List<AdsDefinition> result = new ArrayList<>();

            result.addAll(getAvaliableProperties());
            result.addAll(getAvaliableCommands());

            return result;
        }

        @Override
        public List<AdsDefinition> getDependentItems(ExtendableDefinitions.EScope scope) {
            final List<AdsDefinition> result = new ArrayList<>();

            result.addAll(getDependentProperties(scope));
            result.addAll(getDependentCommands(scope));

            return result;
        }

        @Override
        public boolean isLocal(Id id) {
            return property.getDependents().getDependents(ExtendableDefinitions.EScope.LOCAL).contains(id);
        }

        public List<AdsCommandDef> getAvaliableCommands() {
            final List<AdsCommandDef> result = new ArrayList<>();
            final List<Id> dependents = property.getDependents().getDependents(ExtendableDefinitions.EScope.ALL, AdsPropertyPresentationPropertyDef.COMMAND_FILTER);
            final AdsPresentationDef presentation = RadixObjectsUtils.findContainer(property, AdsPresentationDef.class);
            if (presentation != null) {
                result.addAll(presentation.getCommandsLookup().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsScopeCommandDef>() {
                    @Override
                    public boolean isTarget(AdsScopeCommandDef radixObject) {
                        return presentation.getRestrictions().isCommandEnabled(radixObject.getId())
                                && !dependents.contains(radixObject.getId());
                    }
                }));
            }
            return result;
        }

        public List<AdsCommandDef> getDependentCommands(ExtendableDefinitions.EScope scope) {
            final List<AdsCommandDef> result = new ArrayList<>();
            final List<Id> dependents = property.getDependents().getDependents(scope, AdsPropertyPresentationPropertyDef.COMMAND_FILTER);

            final AdsPresentationDef presentation = RadixObjectsUtils.findContainer(property, AdsPresentationDef.class);
            if (presentation != null) {
                for (Id id : dependents) {
                    final SearchResult<AdsScopeCommandDef> find = presentation.getCommandsLookup().findById(id, ExtendableDefinitions.EScope.ALL);
                    if (!find.isEmpty()) {
                        result.add(find.get());
                    }
                }
            }
            return result;
        }

        public List<AdsPropertyDef> getAvaliableProperties() {
            final List<AdsPropertyDef> result = new ArrayList<>();
            final AdsClassDef model = property.getOwnerClass();

            if (model instanceof AdsModelClassDef) {
                final AdsClassDef serverSideClass = ((AdsModelClassDef) model).findServerSideClasDef();

                final List<Id> dependentsList = property.getDependents().getDependents(ExtendableDefinitions.EScope.ALL,
                        AdsPropertyPresentationPropertyDef.PROPERTY_FILTER);
                if (serverSideClass != null) {
                    result.addAll(serverSideClass.getProperties().get(ExtendableDefinitions.EScope.ALL, new IFilter<AdsPropertyDef>() {
                        @Override
                        public boolean isTarget(AdsPropertyDef radixObject) {
                            if (!(radixObject instanceof IAdsPresentableProperty)) {
                                return false;
                            }

                            final IAdsPresentableProperty presentableProperty = (IAdsPresentableProperty) radixObject;
                            if (presentableProperty.getPresentationSupport() == null) {
                                return false;
                            }
                            final PropertyPresentation presentation = presentableProperty.getPresentationSupport().getPresentation();
                            if (presentation == null || !presentation.isPresentable()) {
                                return false;
                            }

                            return !dependentsList.contains(radixObject.getId()) && !radixObject.getId().equals(property.getId());
                        }
                    }));
                }
            }
            return result;
        }

        public List<AdsPropertyDef> getDependentProperties(ExtendableDefinitions.EScope scope) {
            final List<AdsPropertyDef> result = new ArrayList<>();
            final List<Id> dependents = property.getDependents().getDependents(scope, AdsPropertyPresentationPropertyDef.PROPERTY_FILTER);

            final AdsClassDef model = property.getOwnerClass();

            if (model instanceof AdsModelClassDef) {
                final AdsClassDef serverSideClass = ((AdsModelClassDef) model).findServerSideClasDef();
                if (serverSideClass != null) {
                    for (Id id : dependents) {
                        final SearchResult<AdsPropertyDef> find = serverSideClass.getProperties().findById(id, ExtendableDefinitions.EScope.ALL);
                        if (!find.isEmpty()) {
                            result.add(find.get());
                        }
                    }
                }
            }
            return result;
        }
    }
}
