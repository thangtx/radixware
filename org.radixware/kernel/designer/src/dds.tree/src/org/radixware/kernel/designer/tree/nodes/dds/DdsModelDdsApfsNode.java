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
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.dds.editors.DdsApfPanel;
import org.radixware.kernel.designer.common.tree.RadixObjectsNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;

/**
 * Node of designer tree for {@linkplain DdsColumnTempalteDef column templates} of {@linkplain DdsModelDef}.
 */
public class DdsModelDdsApfsNode extends RadixObjectsNode {

    protected DdsModelDdsApfsNode(Definitions<DdsAccessPartitionFamilyDef> accessPartitionFamilies) {
        super(accessPartitionFamilies, new RadixObjectsNodeSortedChildren(accessPartitionFamilies));
    }

    private static class DdsApfCreature extends Creature<DdsAccessPartitionFamilyDef> {

        private final DdsAccessPartitionFamilyDef apf;

        public DdsApfCreature(DdsDefinitions<DdsAccessPartitionFamilyDef> container) {
            super(container);
            this.apf = DdsAccessPartitionFamilyDef.Factory.newInstance(container);
        }

        @Override
        public WizardInfo getWizardInfo() {
            return new Creature.WizardInfo() {

                @Override
                public CreatureSetupStep createFirstStep() {
                    return new DdsApfStep();
                }

                @Override
                public boolean hasWizard() {
                    return true;
                }
            };
        }

        @Override
        public DdsAccessPartitionFamilyDef createInstance() {
            return apf.getClipboardSupport().duplicate();
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getDisplayName() {
            return DdsAccessPartitionFamilyDef.APF_TYPE_TITLE;
        }

        @Override
        public void afterAppend(DdsAccessPartitionFamilyDef object) {
        }

        @Override
        public boolean afterCreate(DdsAccessPartitionFamilyDef object) {
            return true;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.ACCESS_PARTITION_FAMILY;
        }

        private static class DdsApfStep extends CreatureSetupStep<DdsApfCreature, DdsApfPanel> {

            @Override
            protected DdsApfPanel createVisualPanel() {
                return new DdsApfPanel();
            }

            @Override
            public void open(final DdsApfCreature creature) {
                getVisualPanel().open(creature.apf);

                getVisualPanel().addChangeListener(new ChangeListener() {

                    @Override
                    public void stateChanged(ChangeEvent e) {
                        // System.out.println("!!!DdsApfStep.this.fireChange();");
                        DdsApfStep.this.fireChange();
                    }
                });


            }

            @Override
            public String getDisplayName() {
                return "Setup header and parent reference";
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

    public DdsDefinitions<DdsAccessPartitionFamilyDef> getApfs() {
        return (DdsDefinitions<DdsAccessPartitionFamilyDef>) getRadixObject();
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        DdsDefinitions<DdsAccessPartitionFamilyDef> apfs = getApfs();
        DdsApfCreature creature = new DdsApfCreature(apfs);
        return new DdsCreationSupport(creature);
    }

    @NodeFactoryRegistration
    public static class Factory implements INodeFactory<DdsDefinitions<DdsAccessPartitionFamilyDef>> {

        @Override // Registered in layer.xml
        public Node newInstance(final DdsDefinitions<DdsAccessPartitionFamilyDef> apfs) {
            return new DdsModelDdsApfsNode(apfs);
        }
    }
}
