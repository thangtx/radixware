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
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class ParagraphExpItemRadixdoc extends AdsDefinitionRadixdoc<AdsParagraphExplorerItemDef> {

    public ParagraphExpItemRadixdoc(AdsParagraphExplorerItemDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeDefSpecificInfo(ContentContainer overview) {
        Table generalAttrsTable = getWriter().addGeneralAttrTable(overview);
        if (source.isTopLevelDefinition()) {
            getWriter().addStr2BoolRow(generalAttrsTable, "Root", source.isRoot());
        }
        getWriter().addAllStrRow(generalAttrsTable, "Environment", source.getClientEnvironment().getName());

        if (source.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
            AdsAbstractUIDef expView = source.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.EXPLORER);
            getWriter().addStr2RefRow(generalAttrsTable, "Desktop View", expView, expView.getOwnerDef());
        }
        if (source.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB)) {
            AdsAbstractUIDef webView = source.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.WEB);
            getWriter().addStr2RefRow(generalAttrsTable, "Web View", webView, webView.getOwnerDef());
        }
        getWriter().addStr2RefRow(generalAttrsTable, "Model Class", source.getModel(), source.getModel().getOwnerDef());

        List<AdsCommandDef> usedCommands = new ArrayList<>();
        for (ContextlessCommandUsage.CommandInfo comm : source.getUsedContextlessCommands().getCommandInfos()) {
            usedCommands.add(comm.findCommand());
        }
        getWriter().writeElementsList(overview, usedCommands, "Contextless Commands");
        getWriter().writeExplorerChildrensList(overview, source.getExplorerItems().getChildren().getAll(ExtendableDefinitions.EScope.ALL), "Children");
    }
}
