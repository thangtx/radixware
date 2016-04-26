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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator;

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.enums.EPidTranslationMode;


public class AbstractReferenceTranslator extends SqmlTagTranslator{
    
    private final IClientEnvironment environment;
    private ISqmlTableDef referenceTable;
    private ISqmlTableIndexDef tableIndex;
    private EPidTranslationMode translationMode;
    
    public AbstractReferenceTranslator(final IClientEnvironment environment,boolean isValid, 
    ISqmlTableDef referenceTable, ISqmlTableIndexDef tableIndex, EPidTranslationMode translationMode){
        super(isValid);
        this.environment=environment;
        this.referenceTable=referenceTable;
        this.tableIndex=tableIndex;
        this.translationMode=translationMode;
    }
    
    protected String getReferenceToolTip() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final StringBuilder strBuilder = new StringBuilder(105);

        final String referencedTableStr = msgProvider.translate("SqmlEditor", "Reference to");
        final String translationModeStr = msgProvider.translate("SqmlEditor", "PID translation mode");
        final String keyColumnsStr = msgProvider.translate("SqmlEditor", "Key columns");

        strBuilder.append("<br><b>");
        strBuilder.append(referencedTableStr);
        strBuilder.append(":</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        if (displayMode == EDefinitionDisplayMode.SHOW_TITLES) {
            strBuilder.append(referenceTable.getFullName());
        } else {
            strBuilder.append(referenceTable.getTitle());
        }
        strBuilder.append("</br><br><b>");
        strBuilder.append(translationModeStr);
        strBuilder.append(":</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        switch (translationMode) {
            case PRIMARY_KEY_PROPS:
                strBuilder.append(msgProvider.translate("SqmlEditor", "Primary Key column(s)"));
                break;
            case SECONDARY_KEY_PROPS:
                strBuilder.append(msgProvider.translate("SqmlEditor", "Secondary Key column(s) "));
                strBuilder.append(tableIndex.getFullName());
                break;
            default:
                strBuilder.append(msgProvider.translate("SqmlEditor", "PK string representation"));
        }
        strBuilder.append("</br>");

        if (translationMode != EPidTranslationMode.AS_STR && tableIndex != null) {
            final List<ISqmlColumnDef> columns = tableIndex.getColumns();
            strBuilder.append("<br><b>");
            strBuilder.append(keyColumnsStr);
            strBuilder.append(":</b></br>");
            for (ISqmlColumnDef column : columns) {
                strBuilder.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;");
                if (displayMode == EDefinitionDisplayMode.SHOW_TITLES) {
                    strBuilder.append(column.getShortName());
                } else {
                    strBuilder.append(column.getTitle());
                }
                strBuilder.append("</br>");
            }
        }

        return strBuilder.toString();
    }

    protected String getReferenceDisplayedInfo(final String prefix) {
        final MessageProvider msgProvider = environment.getMessageProvider();
        if (translationMode == EPidTranslationMode.AS_STR) {
            return prefix + "." + msgProvider.translate("SqmlEditor", "PID");
        } else {
            if (tableIndex.getColumns().isEmpty()) {
                if (translationMode == EPidTranslationMode.PRIMARY_KEY_PROPS) {
                    return prefix + "." + msgProvider.translate("SqmlEditor", "PK");
                } else {
                    final String title = msgProvider.translate("SqmlEditor", "SK %s ");
                    return prefix + "." + String.format(title, tableIndex.getShortName());
                }
            } else if (tableIndex.getColumns().size() == 1) {
                return prefix + "." + tableIndex.getColumns().get(0).getDisplayableText(displayMode);
            } else {
                boolean firstColumn = true;
                final StringBuilder columnsBuilder = new StringBuilder();
                final List<ISqmlColumnDef> columns = tableIndex.getColumns();
                columnsBuilder.append('(');
                for (ISqmlColumnDef column : columns) {
                    if (firstColumn) {
                        firstColumn = false;
                    } else {
                        columnsBuilder.append(", ");
                    }
                    columnsBuilder.append(prefix);
                    columnsBuilder.append('.');
                    if (displayMode == EDefinitionDisplayMode.SHOW_FULL_NAMES) {
                        columnsBuilder.append(column.getOwnerTable().getFullName());
                        columnsBuilder.append('.');
                        columnsBuilder.append(column.getShortName());
                    } else {
                        columnsBuilder.append(column.getDisplayableText(displayMode));
                    }
                }
                columnsBuilder.append(')');
                return columnsBuilder.toString();
            }
        }
    }   
}
