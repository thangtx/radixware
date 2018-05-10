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

import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
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
    
    public static String translate(final IClientEnvironment environment, 
                                              final Sqml where, 
                                              final Sqml from,
                                              final Id contextTableId, 
                                              final Id contextEntityId,
                                              final ISqmlParameters parameters,
                                              final Map<Id, Object> paramValues,
                                              final EDefinitionDisplayMode displayMode){
        try {
            final SqmlTranslationRqDocument requestDoc = SqmlTranslationRqDocument.Factory.newInstance();
            final SqmlTranRq request = requestDoc.addNewSqmlTranslationRq();
            if (contextTableId!=null){
                request.setTableId(contextTableId);
            }
            if (contextEntityId!=null){
                request.setEntityClassId(contextEntityId);                
            }
            final SqmlPreprocessor preprocessor = new SqmlPreprocessor();
            final Sqml preprocessedWhere 
                = preprocessor.preprocess(where, parameters, paramValues, environment, displayMode);
            request.setSqml(preprocessedWhere);
            if (from!=null){
                final Sqml preprocessedFrom
                    = preprocessor.preprocess(from, parameters, paramValues, environment, displayMode);
                request.setAdditionalFrom(preprocessedFrom);
            }
            request.setTableAlias("");
            final SqmlTranslationRsDocument resp =  
                (SqmlTranslationRsDocument) environment.getEasSession().executeContextlessCommand(TRANSLATE_SQML_COMMAND_ID, requestDoc, SqmlTranslationRsDocument.class);
            return preprocessor.postprocess( resp.getSqmlTranslationRs().getResult() );
        } catch (ServiceClientException ex) {
            final String title = environment.getMessageProvider().translate("SqmlEditor", "SQML Translation Error");
            final String message = environment.getMessageProvider().translate("SqmlEditor", "Exception occured during SQML translation");
            environment.messageException(title, message, ex);
            return null;
        } catch(InterruptedException ex){
            return null;
        }        
    }    
    
    public static String translate(final IClientEnvironment environment, 
                                              final Sqml sqml,
                                              final Id contextTableId, 
                                              final Id contextEntityId,
                                              final ISqmlParameters parameters,
                                              final Map<Id, Object> paramValues,
                                              final EDefinitionDisplayMode displayMode){
        return translate(environment, sqml, null, contextTableId, contextEntityId, parameters, paramValues, displayMode);
    }
    
    public static String translate(final IClientEnvironment environment, 
                                              final Sqml sqml, 
                                              final Id contextTableId, 
                                              final Id contextEntityId){
        return translate(environment, sqml, null, contextTableId, contextEntityId, null, null, EDefinitionDisplayMode.SHOW_SHORT_NAMES);
    }    
    
    public static String translate(final IClientEnvironment environment, 
                                              final Sqml sqml, 
                                              final ISqmlTableDef context){
        return translate(environment, sqml, context, null, null, EDefinitionDisplayMode.SHOW_SHORT_NAMES);
    }  
    
    public static String translate(final IClientEnvironment environment, 
                                              final Sqml where, 
                                              final Sqml from, 
                                              final ISqmlTableDef context,
                                              final ISqmlParameters parameters,
                                              final Map<Id, Object> paramValues,
                                              final EDefinitionDisplayMode displayMode){
        if (context==null){
            return translate(environment, where, from, null, null, parameters, paramValues, displayMode);
        }else{
            return translate(environment, where, from, context.getTableId(), context.getId(), parameters, paramValues, displayMode);
        }
    }    
            
    public static String translate(final IClientEnvironment environment, 
                                              final Sqml sqml, 
                                              final ISqmlTableDef context,
                                              final ISqmlParameters parameters,
                                              final Map<Id, Object> paramValues,
                                              final EDefinitionDisplayMode displayMode){
        if (context==null){
            return translate(environment, sqml, (Sqml)null, null, null, parameters, paramValues, displayMode);
        }else{
            return translate(environment, sqml, null, context.getTableId(), context.getId(), parameters, paramValues, displayMode);
        }
    }
    
    public static boolean isAccessible(final IClientEnvironment environment){
        return environment.isContextlessCommandAccessible(TRANSLATE_SQML_COMMAND_ID);
    }
    
}
