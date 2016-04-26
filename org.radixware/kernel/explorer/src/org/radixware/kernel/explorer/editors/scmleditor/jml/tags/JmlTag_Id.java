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

package org.radixware.kernel.explorer.editors.scmleditor.jml.tags;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.kind.translator.JmlTagIdTranslator;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.IScmlTranslator;
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.kind.translator.JmlTagTranslator;


public class JmlTag_Id extends TagInfo{
    
    private static final String path = "org.radixware.explorer/S_E/SYNTAX_JML/JML_TAG_ID";
    private JmlTagId tag;
    
    public JmlTag_Id(final IClientEnvironment environment, JmlTagId tag){
        super(environment);
        translator=new  JmlTagIdTranslator(tag);
        this.tag=tag;
    }
    
    @Override
    public IScmlTranslator getTranslator() {
        return translator;
    }

    @Override
    public IScmlItem getCopy() {
        //JmlTag_Id res = new JmlTag_Id(environment, this);
        //res.tag = this.tag;
        //return res;
         return new JmlTag_Id(  environment,  tag);  
    }
    
    @Override
    protected String getSettingsPath() {
        return path;
    }
    
     @Override
    public XmlObject saveToXml() {
        return ((JmlTagTranslator)translator).saveToXml();
    }
   /* @Override
    public XmlObject asXml() {
        //XmlObject xmlObject=XmlObject.Factory.newInstance();
        //tag.appendTo(xmlObject);
    }*/
    
    
}
