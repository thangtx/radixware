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
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.kind.translator.JmlTagDBEntityTranslator;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.jml.JmlTagDbEntity;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.scmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.IScmlTranslator;
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.kind.translator.JmlTagTranslator;


public class JmlTag_DbEntity extends TagInfo{

    @Override
    public XmlObject saveToXml() {
        return ((JmlTagTranslator)translator).saveToXml();
    }
    
    private static final String path = "org.radixware.explorer/S_E/SYNTAX_JML/JML_TAG_DB_ENTITY";
    private final JmlTagDbEntity tag;
    
    public JmlTag_DbEntity(final IClientEnvironment environment, JmlTagDbEntity tag){
        super(environment);
        translator=new  JmlTagDBEntityTranslator(tag, actualize(tag.getEntityId(), tag.getPidAsStr()));
        this.tag=tag;
    }
    
    @Override
    public IScmlTranslator getTranslator() {
        return translator;
    }

    @Override
    public IScmlItem getCopy() {
         return new JmlTag_DbEntity(environment,  tag);
    }
    
    @Override
    protected String getSettingsPath() {
        return path;
    }
    
    private boolean actualize(Id entityId, String pidAsStr){
        try{
           if(entityId!=null && pidAsStr!=null){
                Pid pid = new Pid(entityId, pidAsStr);
                pid.getDefaultEntityTitle(environment.getEasSession());
           }
           return true;
        } catch (InterruptedException ex) {
        } catch (ServiceClientException ex) {
            valid=false;
        } 
        return false;
    }
}   
