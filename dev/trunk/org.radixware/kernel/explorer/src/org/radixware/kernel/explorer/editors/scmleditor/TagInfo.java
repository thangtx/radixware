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

package org.radixware.kernel.explorer.editors.scmleditor;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.jml.JmlTagDbEntity;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.JmlTag_DbEntity;
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.JmlTag_Id;
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.JmlTag_Invocate;
import org.radixware.kernel.explorer.editors.scmleditor.jml.tags.JmlTag_Type;


public class TagInfo extends ScmlTag{    

    //private String title;
    //private String displayName;
    //private long startPos;
    //private long endPos;
    //protected String fullName;
    //private QTextCharFormat charFormat = new QTextCharFormat();
    protected boolean valid = true;
    protected final IClientEnvironment environment;

   /* public TagInfo1(final IClientEnvironment environment, long pos) {
        super(environment,pos);
        setCharFormat();
    }*/
    
    public static class Factory {

        private Factory() {
        }

        public static TagInfo newInstance(final IClientEnvironment environment,final Scml.Tag item){
            TagInfo tag=null;
            if (item instanceof JmlTagInvocation) {
                 tag = new JmlTag_Invocate(environment, (JmlTagInvocation) item);
            } else if (item instanceof JmlTagId) {
                 tag = new JmlTag_Id(environment,(JmlTagId) item);
            } else if (item instanceof JmlTagTypeDeclaration) {
                 tag = new JmlTag_Type(environment,(JmlTagTypeDeclaration) item);
            } else if (item instanceof JmlTagDbEntity) {
                 tag = new JmlTag_DbEntity(environment,(JmlTagDbEntity) item);                   
                 
            }
            return tag;
        }
    }
    
    public TagInfo(final IClientEnvironment environment/*,IScmlTranslator translator*/) {
        super();
        this.environment=environment;
        //setCharFormat();
    }   


    public boolean isValid() {
        return valid;
    }

 
    public boolean showEditDialog(final XscmlEditor editText,final EDefinitionDisplayMode showMode) {
        return false;
    }
   

    protected String getSettingsPath() {
        return null;
    }
  
}
