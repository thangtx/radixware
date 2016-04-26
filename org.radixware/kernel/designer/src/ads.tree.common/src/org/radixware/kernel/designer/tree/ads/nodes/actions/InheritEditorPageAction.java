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

package org.radixware.kernel.designer.tree.ads.nodes.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.PageOrder;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMemberCfgs.ChooseClassMembersCfg;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMemberCfgs.ChoosePresentationMemberCfg;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class InheritEditorPageAction extends AdsDefinitionAction {

    public static final class InheritEditorPageCookie implements Node.Cookie {

        private final transient PageOrder order;

        public InheritEditorPageCookie(final PageOrder order) {
            this.order = order;
        }

        private boolean isAllowed() {
            return !order.isReadOnly() && order.isNodeOrder();
        }

        private void inherit() {
            final ChooseDefinitionMembers.ChooseDefinitionMembersCfg config;

            if (order != null) {
                final EditorPages pageSet = order.getOwnerEditorPages();

                final Collection<Id> usedIds = pageSet.getOrder().getOrderedPageIds();

                final IFilter<AdsEditorPageDef> filter = new IFilter<AdsEditorPageDef>() {

                    @Override
                    public boolean isTarget(final AdsEditorPageDef radixObject) {
                        return !usedIds.contains(radixObject.getId());
                    }
                };

                if (pageSet.getOwnerDefinition() instanceof AdsEditorPresentationDef) {
                    config = new ChoosePresentationMemberCfg((AdsEditorPresentationDef) pageSet.getOwnerDefinition()) {

                        @Override
                        public List<AdsEditorPageDef> listMembers(final AdsDefinition def, final boolean forOverwrite) {
                            if (def instanceof AdsEditorPresentationDef) {
                                return ((AdsEditorPresentationDef) def).getEditorPages().get(EScope.ALL, filter);
                            } else {
                                return Collections.emptyList();
                            }
                        }

                        @Override
                        public String getTitle() {
                            return "Inherit Editor Page";
                        }
                    };
                } else if (pageSet.getOwnerClass() instanceof AdsFormHandlerClassDef) {
                    config = new ChooseClassMembersCfg(pageSet.getOwnerClass()) {

                        @Override
                        public List<? extends AdsDefinition> listMembers(final AdsDefinition def, final boolean forOverwrite) {
                            if (def instanceof AdsFormHandlerClassDef) {
                                return ((AdsFormHandlerClassDef) def).getPresentations().getEditorPages().get(EScope.LOCAL, filter);
                            } else {
                                return Collections.emptyList();
                            }
                        }

                        @Override
                        public String getTitle() {
                            return "Inherit Editor Page";
                        }
                    };
                } else {
                    config = null;
                }
            } else {
                config = null;
            }
            if (config == null) {
                DialogUtils.messageError("Unable to add inherited page");
                return;
            }
            final List<AdsDefinition> propsAndMethods = ChooseDefinitionMembers.choose(config);
            try {
                if (order != null) {
                    for (AdsDefinition def : propsAndMethods) {
                        if (def instanceof AdsEditorPageDef) {
                            order.addPageToOrder((AdsEditorPageDef) def);
                        }
                    }
                }
            } catch (RadixObjectError e) {
                DialogUtils.messageError(new RadixObjectError("Problem while overriding some of selected class members", e));
            }
        }
    }

    @Override
    protected boolean calcEnabled(final Node[] nodes) {
        if (nodes.length == 0) {
            return false;
        }

        final InheritEditorPageCookie c = nodes[0].getCookie(InheritEditorPageCookie.class);

        if (c == null) {
            return false;
        } else {
            return c.isAllowed();
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{InheritEditorPageCookie.class};
    }

    @Override
    protected void performAction(final Node[] activatedNodes) {
        final InheritEditorPageCookie c = activatedNodes[0].getCookie(InheritEditorPageCookie.class);
        if (c != null) {
            c.inherit();
        }
    }

    @Override
    public String getName() {
        return "Inherit Sub Page";
    }

}
