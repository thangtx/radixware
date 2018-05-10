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
import java.util.List;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class OverrideModuleDefinitionAction extends AdsDefinitionAction {

    private static class ChooseCfg extends ChooseDefinitionCfg {

        public ChooseCfg(final AdsModule module) {
            super(module, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsDefinition) {
                        AdsDefinition def = (AdsDefinition) radixObject;
                        return def.isTopLevelDefinition() && def.isPublished() && def instanceof IOverwritable && ((IOverwritable) def).allowOverwrite() && module.getTopContainer().findById(def.getId()) == null;
                    }
                    return false;
                }
            });
            setForOverwrite(true);
        }
    }

    public static class OverrideCookie implements Node.Cookie {

        private AdsModule module;

        public OverrideCookie(AdsModule module) {
            this.module = module;
        }

        private void overwrite() {
            AdsModule ovr = module.findOverwritten();
            if (ovr == null) {
                DialogUtils.messageError("Nothing to overwrite (No overwritten module found)");
                return;
            }
            List<Definition> defs = ChooseDefinition.chooseDefinitions(new ChooseCfg(module));
            List<Definition> failures = new ArrayList<>();
            if (defs != null) {
                for (Definition def : defs) {
                    try {
                        module.getDefinitions().overwrite((AdsDefinition) def);
                    } catch (RadixObjectError e) {
                        //do nothinf
                        failures.add(def);
                    }
                }
            }
            if (!failures.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                builder.append("Follwing definitions were failed to overwrite:\n");
                for (Definition def : failures) {
                    builder.append("\t");
                    builder.append(def.getQualifiedName());
                    builder.append('\n');
                }
                DialogUtils.messageError(builder.toString());
            }
        }
    }

    public OverrideModuleDefinitionAction() {
    }

    @Override
    protected boolean calcEnabled(Node[] nodes) {
        if (nodes.length > 0) {
            OverrideCookie c = nodes[0].getCookie(OverrideCookie.class);
            if (c != null) {
                return !c.module.isReadOnly();
            }
        }
        return false;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Class<?>[] cookieClasses() {
        return new Class[]{OverrideCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        OverrideCookie c = activatedNodes[0].getCookie(OverrideCookie.class);
        if (c != null) {
            c.overwrite();
        }
    }

    @Override
    public String getName() {
        return "Overwrite Definition...";
    }
}
