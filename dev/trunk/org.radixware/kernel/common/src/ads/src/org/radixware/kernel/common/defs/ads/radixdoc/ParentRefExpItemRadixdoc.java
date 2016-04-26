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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsNodeExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class ParentRefExpItemRadixdoc extends NodeExpItemRadixdoc {

    public ParentRefExpItemRadixdoc(AdsNodeExplorerItemDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeOverrideInfo(ContentContainer overview, Table genericAttrsTable) {
        AdsParentRefExplorerItemDef parentRef = (AdsParentRefExplorerItemDef) source;

        getWriter().addStr2RefRow(genericAttrsTable, "DDS Reference", parentRef.findParentReference(), parentRef.findParentReference().getOwnerDefinition());

        long restrs = ERestriction.UPDATE.getValue() | ERestriction.ANY_COMMAND.getValue();
        getWriter().addAllStrRow(genericAttrsTable, "Restrictions", getWriter().getRestrictionsAsStr(restrs & parentRef.getRestrictions().toBitField()));

        List<AdsEditorPresentationDef> editors = new ArrayList<>();
        for (AdsParentRefExplorerItemDef.EditorPresentationInfo editor : parentRef.getEditorPresentationInfos()) {
            editors.add(editor.findEditorPresentation());
        }
        getWriter().writeElementsList(overview, editors, "Editor Presentations");
    }
}
