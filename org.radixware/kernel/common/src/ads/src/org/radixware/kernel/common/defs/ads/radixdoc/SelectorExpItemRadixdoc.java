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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsNodeExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class SelectorExpItemRadixdoc extends NodeExpItemRadixdoc {

    public SelectorExpItemRadixdoc(AdsNodeExplorerItemDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeOverrideInfo(ContentContainer overview, Table genericAttrsTable) {
        AdsSelectorExplorerItemDef selectorItem = (AdsSelectorExplorerItemDef) source;

        getWriter().addStr2BoolRow(genericAttrsTable, "Inherit class catalog from presentation", selectorItem.isClassCatalogInherited());
        AdsClassCatalogDef classCatalog = selectorItem.findCreationClassCatalog().get();
        if (classCatalog != null) {
            getWriter().addStr2RefRow(genericAttrsTable, "Class catalog", classCatalog, classCatalog.getOwnerDef());
        } else {
            getWriter().addAllStrRow(genericAttrsTable, "Class catalog", "<Not Defined>");
        }

        AdsSelectorPresentationDef selector = selectorItem.findReferencedSelectorPresentation().get();
        if (selector != null) {
            getWriter().addStr2RefRow(genericAttrsTable, "Selector presentation", selector, selector.getOwnerDef());

            if (selector.isRestrictionsInherited()) {
                getWriter().addStr2BoolRow(genericAttrsTable, "Inherit restrictions from presentation", selector.isRestrictionsInherited());
            } else {
                final long restrs = ERestriction.INSERT_INTO_TREE.getValue()
                        | ERestriction.INSERT_ALL_INTO_TREE.getValue()
                        | ERestriction.RUN_EDITOR.getValue()
                        | ERestriction.EDITOR.getValue()
                        | ERestriction.CREATE.getValue()
                        | ERestriction.DELETE.getValue()
                        | ERestriction.DELETE_ALL.getValue()
                        | ERestriction.UPDATE.getValue()
                        | ERestriction.MULTIPLE_COPY.getValue();
                getWriter().addAllStrRow(genericAttrsTable, "Prohibited actions", getWriter().getRestrictionsAsStr(restrs & selectorItem.getRestrictions().toBitField()));

                List<AdsDefinition> allowedCommands = new LinkedList();
                ExtendableDefinitions<AdsScopeCommandDef> lookup = selector.getCommandsLookup();
                if (lookup != null) {
                    for (Id commId : selectorItem.getRestrictions().getEnabledCommandIds()) {
                        AdsScopeCommandDef comm = lookup.findById(commId, ExtendableDefinitions.EScope.ALL).get();
                        if (comm != null) {
                            allowedCommands.add(comm);
                        }
                    }
                }
                getWriter().writeElementsList(overview, allowedCommands, "Allowed Commands");
            }
        }
    }
}
