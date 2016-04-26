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
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 *
 * @author npopov
 */
public class CommandRadixdoc extends AdsDefinitionRadixdoc<AdsCommandDef> {

    public CommandRadixdoc(AdsCommandDef source, Page page, DocumentOptions options) {
        super(source, page, options);
    }

    @Override
    protected void writeDefSpecificInfo(ContentContainer overview) {
        Table commandProps = getWriter().addGeneralAttrTable(overview);
        List<String> modifiers = new ArrayList<>();
        if (getSource().isFinal()) {
            modifiers.add("Final");
        }
        if (getSource().isDeprecated()) {
            modifiers.add("Deprecated");
        }
        if (getSource().getPresentation().getIsConfirmationRequired()) {
            modifiers.add("Confirmation required");
        }
        if (getSource().getData().isReadOnlyCommand()) {
            modifiers.add("No modifications");
        }
        final boolean noRequestToServer = getSource().getData().isLocal();
        if (noRequestToServer) {
            modifiers.add("Do not send request to server");
        }
        if (getSource().getPresentation().getIsVisible()) {
            modifiers.add("Show command button");
        }
        if (!modifiers.isEmpty()) {
            getWriter().addAllStrRow(commandProps, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
        }

        if (getSource().getPresentation().getIsVisible()) {
            if (getSource().getTitleId() != null) {
                getWriter().addStr2MslIdRow(commandProps, "Title", getSource().getLocalizingBundleId(), getSource().getTitleId());
            } else {
                getWriter().addAllStrRow(commandProps, "Title", "");
            }
        }

        writeCommandDefInfo(commandProps);

        if (!noRequestToServer) {
            AdsTypeDeclaration inputType = getSource().getData().getInType();
            writeCommandParameterInfo(commandProps, inputType, getSource(), true);
            AdsTypeDeclaration outputType = getSource().getData().getOutType();
            writeCommandParameterInfo(commandProps, outputType, getSource(), false);
        }
    }

    protected void writeCommandDefInfo(Table geneticAttrTable) {
        //Overrided
    }

    private void writeCommandParameterInfo(Table commandTable, AdsTypeDeclaration type, AdsCommandDef source, boolean isInput) {
        String paramDirection = isInput ? "Input" : "Output";
        if (type.isVoid()) {
            getWriter().addAllStrRow(commandTable, paramDirection, "No value");
        } else {
            TypeDocument typeDoc = new TypeDocument();
            typeDoc.addType(type, source);
            Table.Row paramRow = commandTable.addNewRow();
            if (type.isBasedOn(EValType.XML)) {
                getWriter().addText(paramRow.addNewCell(), paramDirection + " (Xml)");
            } else {
                getWriter().addText(paramRow.addNewCell(), paramDirection + " (Form)");
            }
            getWriter().documentType(paramRow.addNewCell(), typeDoc, source);
        }
    }
}
