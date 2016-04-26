/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItems;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

/**
 *
 * @author akrylov
 */
public class InheritExplorerItemAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        public final ExplorerItems context;

        public Cookie(ExplorerItems context) {
            this.context = context;
        }

        public void perform() {
            ChooseDefinitionMembers.ChooseDefinitionMembersCfg config = createConfig();
            if (config == null) {
                DialogUtils.messageError("Nothing to inherit");
                return;
            }
            List<AdsDefinition> definitions = ChooseDefinitionMembers.choose(config);
            for (AdsDefinition definition : definitions) {
                if (definition instanceof AdsExplorerItemDef) {
                    context.inheritChild(definition.getId());
                }
            }
        }

        private ChooseDefinitionMembers.ChooseDefinitionMembersCfg createConfig() {
            ChooseDefinitionMembers.ChooseDefinitionMembersCfg config = new ChooseDefinitionMembers.ChooseDefinitionMembersCfg((AdsDefinition) context.getOwnerDefinition()) {

                @Override
                public List<? extends AdsDefinition> listMembers(AdsDefinition def, boolean forOverwrite) {
                    if (def instanceof AdsEditorPresentationDef) {
                        final AdsEditorPresentationDef epr = (AdsEditorPresentationDef) def;
                        return epr.getExplorerItems().getChildren().get(ExtendableDefinitions.EScope.LOCAL, new IFilter<AdsExplorerItemDef>() {

                            @Override
                            public boolean isTarget(AdsExplorerItemDef radixObject) {
                                return !context.isChildInherited(radixObject.getId());
                            }
                        });
                    } else if (def instanceof AdsParagraphExplorerItemDef) {
                        final AdsParagraphExplorerItemDef paragraph = (AdsParagraphExplorerItemDef) def;
                        return paragraph.getExplorerItems().getChildren().get(ExtendableDefinitions.EScope.LOCAL, new IFilter<AdsExplorerItemDef>() {

                            @Override
                            public boolean isTarget(AdsExplorerItemDef radixObject) {
                                return !context.isChildInherited(radixObject.getId());
                            }
                        });
                    } else {
                        return Collections.emptyList();
                    }
                }

                @Override
                public List<? extends AdsDefinition> listBaseDefinitions(AdsDefinition def,Collection<AdsDefinition> seen) {
                    if (def instanceof AdsEditorPresentationDef) {
                        List<AdsDefinition> result = new ArrayList<>();
                        AdsEditorPresentationDef root = ((AdsEditorPresentationDef) def).findBaseEditorPresentation().get();
                        if (root != null) {
                            result.add(root);
                        }
                        return result;
                    } else if (def instanceof AdsParagraphExplorerItemDef) {

                        List<AdsDefinition> result = new ArrayList<>();
                        def = def.getHierarchy().findOverwritten().get();
                        if (def != null) {
                            result.add(def);
                        }
                        return result;
                    } else {
                        return Collections.emptyList();
                    }
                }

                @Override

                public String getTitle() {
                    return "Inherit Child Explorer Item";
                }
            };
            return config;
        }

        public boolean isEnabled() {
            if (context.isReadOnly()) {
                return false;
            }
            if (context.getOwnerDefinition() instanceof AdsEditorPresentationDef) {
                AdsEditorPresentationDef epr = (AdsEditorPresentationDef) context.getOwnerDefinition();
                if (epr.isExplorerItemsInherited()) {
                    return false;
                }
                return true;
            } else if (context.getOwnerDefinition() instanceof AdsParagraphExplorerItemDef) {
                if (((AdsParagraphExplorerItemDef) context.getOwnerDefinition()).isExplorerItemsInherited()) {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class
        };
    }

    @Override
    protected void performAction(Node[] nodes) {
        if (nodes.length < 1) {
            return;
        }
        final Cookie c = nodes[0].getCookie(Cookie.class
        );
        if (c
                != null) {
            c.perform();
        }
    }

    @Override
    public String getName() {
        return "Inherit Child Explorer Item";
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        if (activatedNodes.length < 1) {
            return false;
        }
        final Cookie c = activatedNodes[0].getCookie(Cookie.class
        );
        if (c
                == null) {
            return false;
        }

        return c.isEnabled();
    }

}
