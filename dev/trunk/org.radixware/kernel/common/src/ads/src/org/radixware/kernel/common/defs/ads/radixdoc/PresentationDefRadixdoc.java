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
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.radixdoc.DefaultMeta;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class PresentationDefRadixdoc extends AdsDefinitionRadixdoc<AdsPresentationDef> {

    public PresentationDefRadixdoc(AdsPresentationDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    protected void writeContextlessCommandsInfo(ContentContainer innerContent) {
        if (!source.getUsedContextlessCommands().isEmpty()) {
            List<AdsContextlessCommandDef> contextlessCommands = new ArrayList<>(source.getUsedContextlessCommands().getCommandInfos().size());
            for (ContextlessCommandUsage.CommandInfo comm : source.getUsedContextlessCommands().getCommandInfos()) {
                contextlessCommands.add(comm.findCommand());
            }
            getWriter().writeElementsList(innerContent, contextlessCommands, "Contextless Commands");
        }
    }

    protected void writeRestrictionsInfo(ContentContainer innerContent, final long restrs) {
        Block restrBlock = innerContent.addNewBlock();
        Table restrTable = getWriter().setBlockCollapsibleAndAddTable(restrBlock, "Restrictions");
        Table.Row restrRow = restrTable.addNewRow();
        if (source.isRestrictionsInherited()) {
            restrRow.addNewCell().addNewText().setStringValue("Restrictions");
            Table.Row.Cell restrValCell = restrRow.addNewCell();
            restrValCell.addNewText().setStringValue("Inherited from: ");
            if (source.getRestrictionsOwner() != null) {
                getWriter().addRef(restrValCell, source.getRestrictionsOwner(), source.getRestrictionsOwner().getModule());
            }
        } else {
            getWriter().addAllStrRow(restrTable, "Prohibited actions", getWriter().getRestrictionsAsStr(restrs & source.getRestrictions().toBitField()));

            if ((source.getRestrictions().toBitField() & ERestriction.ANY_COMMAND.getValue()) == 0) {
                getWriter().addAllStrRow(restrTable, "Allowed Commands", "Any commands");
            } else {
                List<AdsCommandDef> enabledCommands = new ArrayList<>();
                ExtendableDefinitions<AdsScopeCommandDef> commandsLookup = source.getCommandsLookup();
                for (Id commId : source.getRestrictions().getEnabledCommandIds()) {
                    AdsCommandDef comm = null;
                    if (commandsLookup != null) {
                        comm = commandsLookup.findById(commId, ExtendableDefinitions.EScope.ALL).get();
                    }
                    if (comm == null) {
                        for (ContextlessCommandUsage.CommandInfo commInfo : source.getUsedContextlessCommands().getCommandInfos()) {
                            AdsCommandDef contextlessComm = commInfo.findCommand();
                            if (contextlessComm != null && contextlessComm.getId().equals(commId)) {
                                comm = contextlessComm;
                                break;
                            }
                        }
                    }
                    if (comm != null) {
                        enabledCommands.add(comm);
                    }
                }
                if (!enabledCommands.isEmpty()) {
                    Block enabledCommBlock = restrBlock.addNewBlock();
                    enabledCommBlock.setMeta(DefaultMeta.Block.CONTENT);
                    getWriter().writeElementsList(enabledCommBlock, enabledCommands, "Allowed Commands");
                }
            }
        }
    }
}
