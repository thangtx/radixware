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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.explorer.env.Application;


public class EntityRefValueTranslator extends AbstractReferenceTranslator{
    private final String refObjectTitle;
    
    public EntityRefValueTranslator(final boolean isValid, final String refObjectTitle, final IClientEnvironment environment,
    final ISqmlTableDef referenceTable,final ISqmlTableIndexDef tableIndex,final EPidTranslationMode translationMode){
        super(environment,isValid,referenceTable,tableIndex,translationMode);
        this.refObjectTitle=refObjectTitle;
    }
    
    
    @Override
    public String getDisplayString() {
        //if(isValid){
           // String displayValue = entityRefValue.getDisplayValue();
            //String prefix = (displayValue == null || displayValue.isEmpty()) ? refObjectTitle : displayValue;
            //return  getReferenceDisplayedInfo(prefix);
        //}
        return "";
    }

    @Override
    public String getToolTip() {
        if(isValid){
            final StringBuilder strBuilder = new StringBuilder();
            final String lbTableObl = Application.translate("SqmlEditor", "Table Object");
            strBuilder.append("<b>");
            strBuilder.append(lbTableObl);
            strBuilder.append(":</b><br>&nbsp;&nbsp;&nbsp;&nbsp;");
            strBuilder.append(refObjectTitle);
            strBuilder.append("</br>");
            strBuilder.append(getReferenceToolTip());
            return strBuilder.toString();
        }
        return "";
    }    
}
