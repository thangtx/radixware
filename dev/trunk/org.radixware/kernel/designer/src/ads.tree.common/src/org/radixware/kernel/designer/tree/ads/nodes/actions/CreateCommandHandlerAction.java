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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphModelClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.exceptions.RadixPublishedException;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMemberCfgs;
import org.radixware.kernel.designer.ads.common.dialogs.ChooseDefinitionMembers;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;

public class CreateCommandHandlerAction extends AdsDefinitionAction {

    public static class Cookie implements Node.Cookie {

        private AdsClassDef clazz;
        private AdsMethodGroup group;

        public Cookie(AdsMethodGroup group) {
            this.clazz = group.getOwnerClass();
            this.group = group;
        }

        private void process() {
            AdsClassDef realContextClazz = clazz;
            final ContextlessCommandUsage usage;
            final List<AdsContextlessCommandDef> usedClcs = new ArrayList<>();
            if (clazz instanceof AdsModelClassDef) {
                if (clazz instanceof AdsEntityModelClassDef) {
                    usage = ((AdsEntityModelClassDef) clazz).getOwnerEditorPresentation().getUsedContextlessCommands();
                    realContextClazz = ((AdsEntityModelClassDef) clazz).findServerSideClasDef();
                } else if (clazz instanceof AdsFormModelClassDef) {
                    usage = null;
                    realContextClazz = ((AdsFormModelClassDef) clazz).findServerSideClasDef();
                } else if (clazz instanceof AdsReportModelClassDef) {
                    usage = null;
                    realContextClazz = ((AdsReportModelClassDef) clazz).findServerSideClasDef();
                } else if (clazz instanceof AdsGroupModelClassDef) {
                    usage = ((AdsGroupModelClassDef) clazz).getOwnerSelectorPresentation().getUsedContextlessCommands();
                    AdsEntityObjectClassDef owner = ((AdsGroupModelClassDef) clazz).findServerSideClasDef();
                    if (owner != null) {
                        realContextClazz = ((AdsEntityBasedClassDef) owner).findRootBasis().findEntityGroup();
                    }
                } else if (clazz instanceof AdsParagraphModelClassDef) {
                    usage = ((AdsParagraphModelClassDef) clazz).getOwnerParagraph().getUsedContextlessCommands();
                    realContextClazz = clazz;
                } else {
                    usage = null;
                }

                final Collection<Definition> clcs = DefinitionsUtils.collectTopAround(clazz, new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {

                        if (radixObject instanceof AdsContextlessCommandDef) {
                            final AdsContextlessCommandDef clc = (AdsContextlessCommandDef) radixObject;
                            if (clazz.getMethods().findById(clc.getHandlerId(), EScope.LOCAL).get() == null) {
                                if (clazz.getUsageEnvironment().isClientEnv()) {
                                    if (clc.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                                        return true;
                                    } else {
                                        return clc.getClientEnvironment() == clazz.getClientEnvironment();
                                    }
                                } else {
                                    return true;
                                }
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                });
                for (final Definition def : clcs) {
                    usedClcs.add((AdsContextlessCommandDef) def);
                }
            } else {
                usage = null;
            }
            final List<AdsDefinition> commands = ChooseDefinitionMembers.choose(new ChooseDefinitionMemberCfgs.ChooseClassMembersCfg(realContextClazz, true, usedClcs) {
                @Override
                public List<AdsScopeCommandDef> listMembers(final AdsDefinition clazz, boolean forOverwrite) {

                    if (clazz instanceof IAdsPresentableClass) {
                        final ClassPresentations presentations = ((IAdsPresentableClass) clazz).getPresentations();
                        return presentations.getCommands().get(EScope.LOCAL, new IFilter<AdsScopeCommandDef>() {
                            @Override
                            public boolean isTarget(AdsScopeCommandDef object) {
                                if (object.getScope() == ECommandScope.RPC) {
                                    return false;
                                }
                                return Cookie.this.clazz.getMethods().findById(object.getHandlerId(), EScope.LOCAL).get() == null;
                            }
                        });
                    } else {
                        return Collections.emptyList();
                    }
                }

                @Override
                public String getTitle() {
                    return "Create Command Handler";
                }
            });
            try {
                for (AdsDefinition def : commands) {
                    if (def instanceof AdsCommandDef) {
                        final AdsCommandHandlerMethodDef handler = AdsCommandHandlerMethodDef.Factory.newInstance((AdsCommandDef) def);
                        clazz.getMethods().getLocal().add(handler);
                        handler.updateProfile();
                        handler.setUpAutoCode();
                        if (def instanceof AdsContextlessCommandDef) {
                            if (usage != null) {
                                usage.addUsedCommand((AdsContextlessCommandDef) def);
                            }
                        }
                        if (group != clazz.getMethodGroup()) {
                            group.addMember(handler);
                        }
                        AdsModule module = clazz.getModule();
                        if (module != null) {
                            module.getDependences().actualize();
                        }
                    }
                }
            } catch (RadixPublishedException e) {
                DialogUtils.messageError(new RadixObjectError("Problem while creating command handlers", e));
            } catch (RadixObjectError e) {
                DialogUtils.messageError(new RadixObjectError("Problem while creating command handlers", e));
            }

        }
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        if (nodes.length == 1) {
            Cookie c = nodes[0].getCookie(Cookie.class);
            if (c != null) {
                return !c.clazz.isReadOnly() && c.clazz.isCodeEditable();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        Cookie c = activatedNodes[0].getCookie(Cookie.class);

        if (c != null) {
            c.process();
        }
    }

    @Override
    public String getName() {
        return "Create Command Handlers...";
    }
}