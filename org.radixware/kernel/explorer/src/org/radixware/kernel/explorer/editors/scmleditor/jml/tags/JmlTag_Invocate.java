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
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.kind.translator.JmlTagTranslator;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.IScmlTranslator;


public class JmlTag_Invocate extends TagInfo{
     
    private static final String path = "org.radixware.explorer/S_E/SYNTAX_JML/JML_TAG_INVOCATE";
    private  JmlTagInvocation tag;
     
    public JmlTag_Invocate(final IClientEnvironment environment, JmlTagInvocation tag){
        super(environment);
        translator=new  JmlTagTranslator(tag);
        this.tag=tag;
    }
    
    @Override
    public IScmlTranslator getTranslator() {
        return translator;
    }

    @Override
    public IScmlItem getCopy() {
        return new JmlTag_Invocate(  environment,  tag);   
    }
    
    @Override
    protected String getSettingsPath() {
        return path;
    }
    
     @Override
    public XmlObject saveToXml() {
        return ((JmlTagTranslator)translator).saveToXml();
    }
}
