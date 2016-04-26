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

package org.radixware.kernel.designer.tree.nodes.dds;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsCustomTextDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectItemDef;
import org.radixware.kernel.common.defs.dds.DdsPrototypeDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionPanel;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.NamedRadixObjectCreature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectEditCookie;
import org.radixware.kernel.designer.dds.editors.DdsFunctionProfilePanel;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeChildren;


public class DdsPlSqlHeaderItemsNode extends RadixObjectsNode {

    protected DdsPlSqlHeaderItemsNode(DdsDefinitions<DdsPlSqlObjectItemDef> headerItems) {
        super(headerItems, new RadixObjectsNodeChildren(headerItems));
    }

    public static class DdsFunctionCreature extends Creature<DdsFunctionDef> {

        private DdsFunctionDef function;
        private final boolean isPublic;

        public DdsFunctionCreature(DdsDefinitions<DdsPlSqlObjectItemDef> container, boolean isPublic) {
            super(container);
            function = DdsFunctionDef.Factory.newInstance("NewFunction");
            this.isPublic = isPublic;
        }

        @Override
        public WizardInfo getWizardInfo() {
            return new Creature.WizardInfo() {

                @Override
                public CreatureSetupStep createFirstStep() {
                    return new DdsFunctionStep();
                }

                @Override
                public boolean hasWizard() {
                    return true;
                }
            };
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.FUNCTION;
        }

        @Override
        public void afterAppend(DdsFunctionDef function) {
            function.setPublic(isPublic);
        }

        @Override
        public boolean afterCreate(DdsFunctionDef object) {
            return true;
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getDisplayName() {
            return DdsFunctionDef.FUNCTION_TYPE_TITLE;
        }

        @Override
        public DdsFunctionDef createInstance() {
            final DdsFunctionDef result = function;
            function = DdsFunctionDef.Factory.newInstance("NewFunction");
            return result;
        }

        private static class DdsFunctionStep extends CreatureSetupStep<DdsFunctionCreature, DdsFunctionProfilePanel> {

            @Override
            protected DdsFunctionProfilePanel createVisualPanel() {
                return new DdsFunctionProfilePanel();
            }

            @Override
            public void open(DdsFunctionCreature creature) {
                getVisualPanel().open(creature.function);
                getVisualPanel().addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        DdsFunctionStep.this.fireChange();
                    }
                });
            }

            @Override
            public String getDisplayName() {
                return "Profile";
            }

            @Override
            public void apply(DdsFunctionCreature creature) {
                getVisualPanel().apply();
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
    }

    public static class DdsPrototypeCreature extends Creature<DdsPrototypeDef> {

        private Id functionId = null;

        public DdsPrototypeCreature(RadixObjects container) {
            super(container);
        }

        protected DdsPlSqlObjectDef getPlSqlObject() {
            return (DdsPlSqlObjectDef) getContainer().getOwnerDefinition().getOwnerDefinition();
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.PROTOTYPE;
        }

        @Override
        public WizardInfo getWizardInfo() {
            return new Creature.WizardInfo() {

                @Override
                public CreatureSetupStep createFirstStep() {
                    return new DdsPrototypeStep();
                }

                @Override
                public boolean hasWizard() {
                    return true;
                }
            };
        }

        @Override
        public DdsPrototypeDef createInstance() {
            return DdsPrototypeDef.Factory.newInstance();
        }

        @Override
        public void afterAppend(DdsPrototypeDef object) {
        }

        @Override
        public boolean afterCreate(DdsPrototypeDef prototype) {
            prototype.setFunctionId(functionId);
            return functionId != null;
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getDisplayName() {
            return DdsPrototypeDef.PROTOTYPE_TYPE_TITLE;
        }

        private static class DdsPrototypeStep extends CreatureSetupStep<DdsPrototypeCreature, ChooseDefinitionPanel> {

            @Override
            protected ChooseDefinitionPanel createVisualPanel() {
                return new ChooseDefinitionPanel();
            }

            @Override
            public void open(final DdsPrototypeCreature creature) {
                ChooseDefinitionPanel chooseDefinitionPanel = getVisualPanel();
                DdsPlSqlObjectDef plSqlObject = creature.getPlSqlObject();

                VisitorProvider provider = DdsVisitorProviderFactory.newFunctionForPrototypeProvider(plSqlObject);
                ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(plSqlObject, provider);
                chooseDefinitionPanel.open(cfg);

                chooseDefinitionPanel.addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        Definition definition = getVisualPanel().getSelected();
                        creature.functionId = (definition != null ? definition.getId() : null);
                        DdsPrototypeStep.this.fireChange();
                    }
                });
            }

            @Override
            public String getDisplayName() {
                return "Function choise";
            }

            @Override
            public boolean isComplete() {
                return getVisualPanel().getSelected() != null;
            }

            @Override
            public boolean isFinishiable() {
                return true;
            }
        }
    }

    public DdsDefinitions<DdsPlSqlObjectItemDef> getHeaderItems() {
        return (DdsDefinitions<DdsPlSqlObjectItemDef>) getRadixObject();
    }

    public static class DdsCustomTextCreature extends NamedRadixObjectCreature<DdsPlSqlObjectItemDef> {

        public DdsCustomTextCreature(DdsDefinitions<DdsPlSqlObjectItemDef> container) {
            super(container, DdsCustomTextDef.CUSTOM_TEXT_TYPE_TITLE);
        }

        @Override
        public DdsCustomTextDef createInstance(final String name) {
            final DdsCustomTextDef customText = DdsCustomTextDef.Factory.newInstance(name);
            return customText;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.LABEL;
        }
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        final DdsDefinitions<DdsPlSqlObjectItemDef> headerItems = getHeaderItems();
        final DdsPlSqlObjectDef plSqlObject = (DdsPlSqlObjectDef) headerItems.getContainer().getContainer();
        final DdsDefinitions<DdsPlSqlObjectItemDef> bodyItems = plSqlObject.getBody().getItems();
        final boolean isPublic = true;
        final Creature functionCreature = new DdsFunctionCreature(bodyItems, isPublic);
        final Creature customTextCreature = new DdsCustomTextCreature(headerItems);
        return new DdsCreationSupport(functionCreature, customTextCreature);
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsDefinitions<DdsPlSqlObjectItemDef>> {

        @Override // Registered in layer.xml
        public Node newInstance(DdsDefinitions<DdsPlSqlObjectItemDef> headerItems) {
            return new DdsPlSqlHeaderItemsNode(headerItems);
        }
    }

    @Override
    protected RadixObjectEditCookie createEditCookie() {
        return new RadixObjectEditCookie(getHeaderItems().getContainer());
    }
}
