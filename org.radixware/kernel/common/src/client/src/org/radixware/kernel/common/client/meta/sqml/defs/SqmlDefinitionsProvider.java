/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.meta.sqml.defs;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import javax.xml.parsers.ParserConfigurationException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsProvider;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.xml.sax.SAXException;


public final class SqmlDefinitionsProvider implements ISqmlDefinitionsProvider{
    
    public SqmlDefinitionsProvider(){        
    }
    
    private final SqmlDefinitions.LoadingHandler loadingHanlder = new SqmlDefinitions.LoadingHandler();

    @Override
    public ISqmlDefinitions load(final IClientEnvironment environment) throws DefinitionError, CancellationException {
        final SqmlDefinitions definitions = new SqmlDefinitions(environment);
        try{
            if (definitions.loadDefinitions(loadingHanlder)){
                return definitions;
            }else{
                throw new CancellationException("SQML definitions loading was cancelled");
            }
        }catch(IOException | ParserConfigurationException | SAXException exception){
            final String message = environment.getMessageProvider().translate("ExplorerError", "Failed to load SQML definitions");
            throw new DefinitionError(message, exception);
        }        
    }

    @Override
    public void cancelLoading() {
        loadingHanlder.cancel();
    }
}
