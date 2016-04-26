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

package org.radixware.kernel.common.client.editors.xmleditor;

import java.io.IOException;
import java.io.InputStream;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;


public class StandardXmlSchemaProvider implements IXmlSchemaProvider{

    private final IClientEnvironment environment;
    
    public StandardXmlSchemaProvider(final IClientEnvironment environment){
        this.environment = environment;
    }

    @Override
    public String getSchemaForNamespaceUri(final String nameSpaceUri) {
        final ReleaseRepository repository = environment.getDefManager().getRepository();
        final Id schemaId = repository.findXmlSchemeIdByTargetNameSpace(nameSpaceUri);
        if (schemaId==null){
            return null;
        }else{
            final InputStream istream;
            try{
                 istream = repository.getInputStreamForXmlScheme(schemaId);
            }catch(DefinitionError error){
                 environment.getTracer().error(error);
                 return null;
            }
            try{
                return FileUtils.readTextStream(istream, FileUtils.XML_ENCODING);
            }catch(IOException exception){
                environment.getTracer().error(exception);
                return null;
            }
        }       
    }    
}
