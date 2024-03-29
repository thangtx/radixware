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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.schemas.xscml.Sqml;


public class SqmlTag_ElseIf  extends SqmlTag {
    
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PREPROCESSOR";
    
    private SqmlTag_ElseIf(final IClientEnvironment environment, final SqmlTag_ElseIf source){
        super(environment,source);
    }
    
    public SqmlTag_ElseIf(final IClientEnvironment environment, final long pos){
        super(environment,pos);
        setDisplayedInfo(null,"#ELSE");
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        ((Sqml.Item) itemTag).addNewElseIf();        
    }    

    @Override
    protected String getSettingsPath() {
        return PATH;
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            setDisplayedInfo(null,"#ELSE");
            return true;
        }else{
            return false;
        }
    }

    @Override
    public SqmlTag_ElseIf copy() {
        return new SqmlTag_ElseIf(environment, this);
    }

    
    
}
