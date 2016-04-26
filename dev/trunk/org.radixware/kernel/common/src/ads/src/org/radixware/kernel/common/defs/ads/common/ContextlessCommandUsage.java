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

package org.radixware.kernel.common.defs.ads.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public class ContextlessCommandUsage extends RadixObject {

    public interface IContextlessCommandsUser {

        public ContextlessCommandUsage getUsedContextlessCommands();

        public ERuntimeEnvironmentType getClientEnvironment();
    }

    public static final class Factory {

        private Factory() {
            super();
        }

        public static final ContextlessCommandUsage newInstance(IContextlessCommandsUser owner) {
            return new ContextlessCommandUsage(owner, null);
        }

        public static final ContextlessCommandUsage loadFrom(IContextlessCommandsUser owner, List<Id> ids) {
            return new ContextlessCommandUsage(owner, ids);
        }
    }
    private ArrayList<Id> usedContextlessCommands = null;

    private ContextlessCommandUsage(IContextlessCommandsUser owner, List<Id> ids) {
        super();
        setContainer((RadixObject) owner);
        if (ids != null && !ids.isEmpty()) {
            this.usedContextlessCommands = new ArrayList<>(ids);
        }
    }

    public IContextlessCommandsUser getUser() {
        return (IContextlessCommandsUser) getContainer();
    }

    public boolean isEmpty() {
        return usedContextlessCommands == null || usedContextlessCommands.isEmpty();
    }

    @Override
    public String getName() {
        return "Used Contexless Commands";
    }

    public List<Id> getUsedContextlessCommandIds() {
        if (usedContextlessCommands == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(usedContextlessCommands);
        }
    }

    public void addUsedCommand(AdsContextlessCommandDef clc) {
        synchronized (this) {
            if (usedContextlessCommands == null || clc == null) {
                usedContextlessCommands = new ArrayList<>();
            }
            if (!usedContextlessCommands.contains(clc.getId())) {
                usedContextlessCommands.add(clc.getId());
            }
            setEditState(EEditState.MODIFIED);
        }
    }

    public void removeUsedCommand(AdsContextlessCommandDef clc) {
        synchronized (this) {
            if (clc != null) {
                removeUsedCommandId(clc.getId());
            }
        }
    }

    public void removeUsedCommandId(final Id id) {
        synchronized (this) {
            if (usedContextlessCommands == null || id == null) {
                return;
            } else {
                usedContextlessCommands.remove(id);
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public List<Id> getCommandIds() {
        if (usedContextlessCommands == null) {
            return Collections.emptyList();
        } else {
            return new ArrayList<>(usedContextlessCommands);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (usedContextlessCommands != null) {
            DefinitionSearcher<AdsContextlessCommandDef> searcher = AdsSearcher.Factory.newAdsContextlessCommandSearcher((AdsModule) getOwnerDefinition().getModule());
            for (Id id : usedContextlessCommands) {
                AdsContextlessCommandDef clc = searcher.findById(id).get();
                if (clc != null) {
                    list.add(clc);
                }
            }
        }
    }

    public class CommandInfo {

        public final Id commmandId;

        public final AdsContextlessCommandDef findCommand() {
            return AdsSearcher.Factory.newAdsContextlessCommandSearcher((AdsModule) getOwnerDefinition().getModule()).findById(commmandId).get();
        }

        private CommandInfo(Id id) {
            this.commmandId = id;
        }
    }

    public List<CommandInfo> getCommandInfos() {
        ArrayList<CommandInfo> infos = new ArrayList<>();
        if (usedContextlessCommands != null) {
            for (Id id : usedContextlessCommands) {
                infos.add(new CommandInfo(id));
            }
        }
        return infos;
    }     
}
