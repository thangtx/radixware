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

import java.util.List;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.designer.common.dialogs.choosedomain.AdsDefinitionDomainsEditor;
import org.radixware.kernel.designer.common.dialogs.choosedomain.ChooseDomain;
import org.radixware.kernel.designer.tree.ads.nodes.actions.AdsDefinitionAction;


public class ConfigureDomainsAction extends AdsDefinitionAction {

    public static final class ConfigureDomainsCookie implements Node.Cookie {

        private AdsDefinition def;

        public ConfigureDomainsCookie(AdsDefinition def) {
            this.def = def;
        }

        private boolean isEnabled() {
            return true;
        }

        private static class DefDomainCfg extends AdsDefinitionDomainsEditor {

            private AdsDefinition def;

            private DefDomainCfg(AdsDefinition def) {
                this.def = def;
                setReadOnly(def.isReadOnly());
            }

            @Override
            protected String getTitle() {
                return "Assign definition to domains";
            }
        }

        private void configure() {
            AdsDefinitionDomainsEditor editor = new DefDomainCfg(def);
            editor.open(def, null);
            editor.showModal();
//            List<AdsDomainDef> domains = ChooseDomain.chooseDomains(def);
//            if (domains != null) {
//                def.getDomains().clearDomains();
//                for (AdsDomainDef domain : domains) {
//                    def.getDomains().addDomain(domain);
//                }
//            }
        }
    }

    @Override
    protected boolean calcEnabled(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            ConfigureDomainsCookie c = n.getCookie(ConfigureDomainsCookie.class);
            if (c == null || !c.isEnabled()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{ConfigureDomainsCookie.class};
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            ConfigureDomainsCookie c = n.getCookie(ConfigureDomainsCookie.class);
            if (c != null && c.isEnabled()) {
                c.configure();
                return;
            }
        }
    }

    @Override
    public String getName() {
        return "Configure Domains...";
    }


}
