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

import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class FormPresentationsRadixdoc extends ClassPresentationsRadixdoc {

    public FormPresentationsRadixdoc(ContentContainer container, ClassRadixdoc classDoc, ClassPresentations presentations) {
        super(container, classDoc, presentations);
    }

    @Override
    protected void writeOverrideInfo(Block presChapter) {
        AbstractFormPresentations formPres = (AbstractFormPresentations) presentations;

        Table commonInfoTable = clWriter.addGeneralAttrTable(presChapter);
        clWriter.addStr2RefRow(commonInfoTable, "Model Class", formPres.getModel(), formPres.getOwnerClass());
        AdsAbstractUIDef explorerView = formPres.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.EXPLORER);
        if (explorerView != null) {
            clWriter.addStr2RefRow(commonInfoTable, "Desktop View", explorerView, formPres.getOwnerClass());
        }
        AdsAbstractUIDef webView = formPres.getCustomViewSupport().getCustomView(ERuntimeEnvironmentType.WEB);
        if (webView != null) {
            clWriter.addStr2RefRow(commonInfoTable, "Web View", webView, formPres.getOwnerClass());
        }

        List<AdsEditorPageDef> editorPages = formPres.getEditorPages().getAll(ExtendableDefinitions.EScope.ALL);
        if (!editorPages.isEmpty()) {
            clWriter.writeElementsList(presChapter, editorPages, "Editor pages");
        }
    }
}
