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
import java.util.HashSet;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;


public class GoToCommandHandlerCommonCookie implements Node.Cookie {

    protected AdsCommandDef command;

    public GoToCommandHandlerCommonCookie(AdsCommandDef command) {
        initProvider();//NOPMD
        this.command = command;
    }
    private Collection<AdsDefinition> chosenHandlers;

    private class HandlersVisitor implements IVisitor {

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof AdsCommandHandlerMethodDef) {
                chosenHandlers.add((AdsDefinition) radixObject);
            }
        }
    }

    protected void initProvider() {
        provider = new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return true;
            }

            @Override
            public boolean isContainer(RadixObject radixObject) {
                return true;
            }
        };
    }
    protected VisitorProvider provider;

    public void goToHandler() {
        chosenHandlers = new HashSet<>();
        command.getModule().getSegment().getLayer().getBranch().visit(new HandlersVisitor(), provider);

        final Definition def;
        if (chosenHandlers.size() == 1) {
            def = chosenHandlers.iterator().next();
        } else {
            ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(chosenHandlers);
            def = ChooseDefinition.chooseDefinition(cfg);
        }

        if (def != null) {
            NodesManager.selectInProjects(def);
            EditorsManager.getDefault().open(def);
        }
    }
}
