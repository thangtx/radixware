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

package org.radixware.kernel.designer.tree.ads.nodes;

import java.util.*;
import javax.swing.Action;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.BuildCookie;
import org.radixware.kernel.designer.ads.build.actions.BuildAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAction;
import org.radixware.kernel.designer.ads.build.actions.CleanAndBuildAction;
import org.radixware.kernel.designer.ads.editors.creation.AdsClassCreature;
import org.radixware.kernel.designer.ads.editors.creation.AdsModuleDefinitionCreature;
import org.radixware.kernel.designer.ads.editors.creation.AdsTransparentClassCreature;
import org.radixware.kernel.designer.ads.editors.creation.AdsXmlSchemePublishingCreature;
import org.radixware.kernel.designer.ads.editors.enumeration.creation.AdsEnumCreature;
import org.radixware.kernel.designer.ads.editors.msdl.creation.MsdlCreature;
import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;
import org.radixware.kernel.designer.common.tree.actions.RadixdocAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsModuleImageSetEditAction;
import org.radixware.kernel.designer.tree.ads.nodes.actions.OverrideModuleDefinitionAction;

/**
 * Tree node handling ads module object
 *
 */
public class AdsModuleNode extends RadixObjectNode {

    private static class AdsModuleNodeChildren extends RadixObjectsNodeSortedChildren<AdsDefinition> {

        private final AdsModule module;

        public AdsModuleNodeChildren(AdsModule module) {
            super();
            this.module = module;
        }

        @Override
        protected RadixObjects<AdsDefinition> getRadixObjects() {
            return module.getDefinitions();
        }
        // commented so dislike
//        @Override
//        protected List<RadixObject> getOrderedList() {
//            final List<RadixObject> list = super.getOrderedList();
//            list.add(module.getImages());
//            return list;
//        }
    }
    private AdsModule module;
    @SuppressWarnings({"rawtypes"})
    private final IRadixEventListener isTestListener = new IRadixEventListener() {
        @Override
        public void onEvent(RadixEvent e) {
            updateIcon();
        }
    };
    OverrideModuleDefinitionAction.OverrideCookie overrideCookie = null;

    protected AdsModuleNode(final AdsModule module) {
        super(module, new AdsModuleNodeChildren(module));
        Cookie imageSetEditCookie = new AdsModuleImageSetEditAction.Cookie(module);
        addCookie(imageSetEditCookie);
        addCookie(new BuildCookie(module));
        addCookie(new OverrideModuleDefinitionAction.OverrideCookie(module));
        addCookie(new RadixdocAction.RadixdocCookie(module));
        this.module = module;
        module.addIsTestListener(isTestListener);
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return new CreationSupport() {
            @Override
            public ICreatureGroup[] createCreatureGroups(RadixObject object) {
                List<ICreatureGroup> groups = new LinkedList<>();

                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        ArrayList<ICreature> result = new ArrayList<>();
                        result.addAll(
                                AdsClassCreature.Factory.instanceList(module, EnumSet.of(EClassType.DYNAMIC, EClassType.ENUMERATION, EClassType.INTERFACE, EClassType.EXCEPTION, EClassType.FORM_HANDLER)));
                        result.add(null);
                        result.add(new AdsTransparentClassCreature(module));// ygalkina
                        return result;
                    }

                    @Override
                    public String getDisplayName() {
                        return "Non Persistent Classes";
                    }
                });
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        return AdsClassCreature.Factory.instanceList(module, EnumSet.of(EClassType.ALGORITHM));
                    }

                    @Override
                    public String getDisplayName() {
                        return "Workflow Classes";
                    }
                });
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        return AdsClassCreature.Factory.instanceList(module, EnumSet.of(EClassType.ENTITY, EClassType.APPLICATION, EClassType.ENTITY_GROUP, EClassType.PRESENTATION_ENTITY_ADAPTER));
                    }

                    @Override
                    public String getDisplayName() {
                        return "Entity Classes";
                    }
                });
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        return AdsClassCreature.Factory.instanceList(module, EnumSet.of(EClassType.SQL_CURSOR, EClassType.SQL_PROCEDURE, EClassType.SQL_STATEMENT, EClassType.REPORT));
                    }

                    @Override
                    public String getDisplayName() {
                        return "SQL Classes";
                    }
                });
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        ArrayList<ICreature> result = new ArrayList<>();
                        result.add(new AdsEnumCreature(module));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.CONTEXTLESS_COMMAND));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.XML_SCHEME));
                        result.add(new AdsXmlSchemePublishingCreature(module));
                        result.add(new MsdlCreature(module));
//                        result.add(new XsltCreature(module));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.PARAGRAPH));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.ROLE));                        
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.DOMAIN));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.PHRASE_BOOK));
                        return result;
                    }

                    @Override
                    public String getDisplayName() {
                        return "Another ADS Definitions";
                    }
                });
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        ArrayList<ICreature> result = new ArrayList<>();
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.CUSTOM_DIALOG));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.CUSTOM_PROP_EDITOR));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.CUSTOM_WIDGET_DEF));


                        return result;
                    }

                    @Override
                    public String getDisplayName() {
                        return "Explorer Custom Views";
                    }
                });
                groups.add(new ICreatureGroup() {
                    @Override
                    public List<ICreature> getCreatures() {
                        ArrayList<ICreature> result = new ArrayList<>();
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.CUSTOM_DIALOG, ERuntimeEnvironmentType.WEB));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.CUSTOM_PROP_EDITOR, ERuntimeEnvironmentType.WEB));
                        result.add(new AdsModuleDefinitionCreature(module, EDefType.CUSTOM_WIDGET_DEF, ERuntimeEnvironmentType.WEB));
                        return result;
                    }

                    @Override
                    public String getDisplayName() {
                        return "Web Custom Views";
                    }
                });
                if (module.isTest()) {
                    groups.add(new ICreatureGroup() {
                        @Override
                        public List<ICreature> getCreatures() {
                            return Collections.singletonList(AdsClassCreature.Factory.newTestInstance(module));
                        }

                        @Override
                        public String getDisplayName() {
                            return "Tests";
                        }
                    });
                }
                return groups.toArray(new ICreatureGroup[0]);
            }
        };
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        if (module.findOverwritten() != null) {
            actions.add(SystemAction.get(OverrideModuleDefinitionAction.class));
            actions.add(null);
        }
        super.addCustomActions(actions);
        actions.add(SystemAction.get(BuildAction.class));
        actions.add(SystemAction.get(CleanAndBuildAction.class));
        actions.add(SystemAction.get(CleanAction.class));
        actions.add(null);
        actions.add(SystemAction.get(AdsModuleImageSetEditAction.class));
        RadixdocAction action = SystemAction.get(RadixdocAction.class);
        if (action.isAvailable()) {
            actions.add(action);
        }
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<AdsModule> {

        @Override // Registered in layer.xml
        public RadixObjectNode newInstance(AdsModule ddsModule) {
            return new AdsModuleNode(ddsModule);
        }
    }
}
