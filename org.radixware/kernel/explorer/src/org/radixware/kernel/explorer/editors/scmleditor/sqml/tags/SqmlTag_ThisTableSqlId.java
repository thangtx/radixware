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

package org.radixware.kernel.explorer.editors.scmleditor.sqml.tags;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scmleditor.sqml.tags.translator.ThisTableSqlIdTranslator;


public class SqmlTag_ThisTableSqlId extends TagInfo{
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_THIS_TABLE_SQL_NAME";
    
    SqmlTag_ThisTableSqlId(final IClientEnvironment environment){
        super(environment);
    }
    
    SqmlTag_ThisTableSqlId(final IClientEnvironment environment,final ISqmlTableDef presentationClassDef){
        super(environment);
        translator=new ThisTableSqlIdTranslator(presentationClassDef != null,presentationClassDef);      
    }
    
    @Override
    protected String getSettingsPath() {
        return PATH;
    }
    
    @Override
    public XmlObject saveToXml() {
        return XmlObject.Factory.newInstance();
    }
    
    @Override  
    public IScmlItem  getCopy() {   
        final SqmlTag_ThisTableSqlId res= new SqmlTag_ThisTableSqlId(environment);
        res.translator=translator;
        return res;
    }
    
    
}
