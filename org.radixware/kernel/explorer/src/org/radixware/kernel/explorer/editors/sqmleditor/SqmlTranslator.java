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

package org.radixware.kernel.explorer.editors.sqmleditor;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.sqmltran.SqmlTranRq;
import org.radixware.schemas.sqmltran.SqmlTranslationRqDocument;
import org.radixware.schemas.sqmltran.SqmlTranslationRsDocument;
import org.radixware.schemas.xscml.Sqml;


final class SqmlTranslator {
    
    private final static Id TRANSLATE_SQML_COMMAND_ID = Id.Factory.loadFrom("clcTFAJ5XZEJJAF7ESTDWOSTGW3II");
    
    private SqmlTranslator(){
        
    }        
    
    public static String translate(final IClientEnvironment environment, final Sqml sqml, final Id contextTableId, final Id contextEntityId){
        try {
            final SqmlTranslationRqDocument requestDoc = SqmlTranslationRqDocument.Factory.newInstance();
            final SqmlTranRq request = requestDoc.addNewSqmlTranslationRq();
            if (contextTableId!=null){
                request.setTableId(contextTableId);
            }
            if (contextEntityId!=null){
                request.setEntityClassId(contextEntityId);                
            }
            request.setSqml(sqml);
            request.setTableAlias("");
            final SqmlTranslationRsDocument resp =  
                (SqmlTranslationRsDocument) environment.getEasSession().executeContextlessCommand(TRANSLATE_SQML_COMMAND_ID, requestDoc, SqmlTranslationRsDocument.class);
            return resp.getSqmlTranslationRs().getResult();
        } catch (ServiceClientException ex) {
            final String title = environment.getMessageProvider().translate("SqmlEditor", "SQML Translation Error");
            final String message = environment.getMessageProvider().translate("SqmlEditor", "Exception occured during SQML translation");
            environment.messageException(title, message, ex);
            return null;
        } catch(InterruptedException ex){
            return null;
        }        
    }
    
    public static String translate(final IClientEnvironment environment, final Sqml sqml, final ISqmlTableDef context){
        if (context==null){
            return translate(environment, sqml, null, null);
        }else{
            return translate(environment, sqml, context.getTableId(), context.getId());
        }
    }
    
    public static boolean isAccessible(final IClientEnvironment environment){
        return environment.isContextlessCommandAccessible(TRANSLATE_SQML_COMMAND_ID);
    }
    
}
