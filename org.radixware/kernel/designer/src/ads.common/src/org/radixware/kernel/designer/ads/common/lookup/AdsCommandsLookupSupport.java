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

package org.radixware.kernel.designer.ads.common.lookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityBasedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage.CommandInfo;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.enums.EDefType;


public abstract class AdsCommandsLookupSupport {

    /**
     * Return list of available commands
     */
    public abstract Collection<AdsCommandDef> getAvailableCommandList();

    protected Collection<AdsCommandDef> getAvailableCommandList(AdsSelectorExplorerItemDef ei) {
        ArrayList<AdsCommandDef> cmds = new ArrayList<AdsCommandDef>();
        collectAvailableCommands(ei, cmds);
        return cmds;
    }

    protected Collection<AdsCommandDef> getAvailableCommandList(AdsParentRefExplorerItemDef ei) {
        ArrayList<AdsCommandDef> cmds = new ArrayList<AdsCommandDef>();
        collectAvailableCommands(ei, cmds);
        return cmds;
    }

    protected Collection<AdsCommandDef> getAvailableCommandList(AdsEditorPresentationDef epr) {
        ArrayList<AdsCommandDef> cmds = new ArrayList<AdsCommandDef>();
        collectAvailableCommands(epr, cmds);
        return cmds;
    }

    protected Collection<AdsCommandDef> getAvailableCommandList(AdsSelectorPresentationDef spr) {
        ArrayList<AdsCommandDef> cmds = new ArrayList<AdsCommandDef>();
        collectAvailableCommands(spr, cmds);
        return cmds;
    }

    protected void collectAvailableCommands(AdsPresentationDef pr, Collection<AdsCommandDef> c) {
        List<CommandInfo> clcs = pr.getUsedContextlessCommands().getCommandInfos();
        for (CommandInfo info : clcs) {
            AdsContextlessCommandDef clc = info.findCommand();
            if (clc != null) {
                c.add(clc);
            }
        }
        AdsEntityObjectClassDef clazz = pr.getOwnerClass();
        if (clazz == null) {
            return;
        }
        if (pr.getDefinitionType() == EDefType.SELECTOR_PRESENTATION) {
            AdsEntityClassDef aec = clazz.findRootBasis();
            if (aec == null) {
                return;
            }
            AdsEntityGroupClassDef agc = aec.findEntityGroup();
            if (agc == null) {
                return;
            }
            collectAvailableCommands(agc, c);
        } else {
            collectAvailableCommands(clazz, c);

        }
    }

    protected void collectAvailableCommands(AdsSelectorExplorerItemDef ei, final Collection<AdsCommandDef> c) {
        ei.findReferencedSelectorPresentation().iterate(new SearchResult.Acceptor<AdsSelectorPresentationDef>() {

            @Override
            public void accept(AdsSelectorPresentationDef spr) {
                collectAvailableCommands(spr, c);
            }
        });
    }

    protected void collectAvailableCommands(AdsEntityBasedClassDef clazz, Collection<AdsCommandDef> c) {
        if (clazz != null) {
            ClassPresentations presentations = null;
            if (clazz instanceof AdsEntityGroupClassDef) {
                presentations = ((AdsEntityGroupClassDef) clazz).getPresentations();
            } else {
                presentations = ((AdsEntityObjectClassDef) clazz).getPresentations();
            }
            for (AdsCommandDef cmd : presentations.getCommands().get(EScope.ALL)) {
                c.add(cmd);
            }
        }
    }

    protected void collectAvailableCommands(AdsParentRefExplorerItemDef ei, Collection<AdsCommandDef> c) {
        collectAvailableCommands(ei.findReferencedEntityClass(), c);

    }
}
