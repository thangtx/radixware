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

import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;


public class ThisTableSqlNameTranslator extends SqmlTagTranslator{
    private final ISqmlTableDef presentationClassDef;
    
    public ThisTableSqlNameTranslator(final boolean isValid,final ISqmlTableDef presentationClassDef){
        super(isValid);
        this.presentationClassDef=presentationClassDef;
    }
    
    
    @Override
    public String getDisplayString() {
        return isValid? "This":"???ThisTableName???";       
    }

    @Override
    public String getToolTip() {
        if (isValid) {
            final String tableName = presentationClassDef.getFullName();
            final String tableTitle = presentationClassDef.getTitle().replaceAll("<", "&#60;");
            return "Table <b>" + tableName + "</b> with title " + tableTitle;
        } 
        return  "???ThisTableName???";
    }
    
    
}
