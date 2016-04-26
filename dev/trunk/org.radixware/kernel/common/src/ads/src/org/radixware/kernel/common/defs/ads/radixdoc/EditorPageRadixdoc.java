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
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class EditorPageRadixdoc extends AdsDefinitionRadixdoc<AdsEditorPageDef> {

    public EditorPageRadixdoc(AdsEditorPageDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeDefSpecificInfo(ContentContainer overview) {
        writeGenericAttributes(overview);

    }

    private void writeGenericAttributes(ContentContainer content) {
        Table generalAttrsTable = getWriter().addGeneralAttrTable(content);
        getWriter().addStr2BoolRow(generalAttrsTable, "Final", source.isFinal());
        if (source.isOverwrite()) {
            AdsEditorPageDef overwrittenPage = source.getHierarchy().findOverwritten().get();
            getWriter().addStr2RefRow(generalAttrsTable, "Overwriting", overwrittenPage, overwrittenPage.getOwnerClass());
        } else {
            getWriter().addStr2BoolRow(generalAttrsTable, "Overwriting", false);
        }

        AdsEditorPageDef overridenPage = source.getHierarchy().findOverridden().get();
        if (overridenPage != null) {
            getWriter().addStr2RefRow(generalAttrsTable, "Overriding", overridenPage, overridenPage.getOwnerClass());
        }
        getWriter().addAllStrRow(generalAttrsTable, "Environment", source.getClientEnvironment().getName());
        getWriter().addAllStrRow(generalAttrsTable, "Page type", source.getType().getAsStr());

        switch (source.getType()) {
            case CONTAINER:
                getWriter().addStr2RefRow(generalAttrsTable, "Embedded explorer item", source.findEmbeddedExplorerItem(), source.findEmbeddedExplorerItem().getOwnerDef());
                break;
            case CUSTOM:
                AdsAbstractUIDef explorerView = source.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.EXPLORER);
                getWriter().addStr2RefRow(generalAttrsTable, "Desktop View", explorerView, explorerView.getModule());
                AdsAbstractUIDef webView = source.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.WEB);
                getWriter().addStr2RefRow(generalAttrsTable, "Web View", webView, webView.getModule());
                break;
            case STANDARD:
                List<AdsDefinition> usedProps = new ArrayList<>();
                for (PropertyUsageSupport.PropertyRef propRef : source.getUsedProperties().get()) {
                    usedProps.add(propRef.findProperty());
                }
                getWriter().writeElementsList(content, usedProps, "Used Properties");
                break;
            default:
                throw new EnumConstantNotPresentException(EEditorPageType.class, source.getType().getName());
        }

    }
}
