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


public class SqmlTag_TargetDbPreprocessorTag  extends SqmlTag {
    
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PREPROCESSOR";
    
    private final PreprocessorDbParameters parameters;
    
    private SqmlTag_TargetDbPreprocessorTag(final IClientEnvironment environment, final SqmlTag_TargetDbPreprocessorTag source){
        super(environment,source);
        parameters = source.parameters.copy();
    }
    
    public SqmlTag_TargetDbPreprocessorTag(final IClientEnvironment environment, final int pos, final Sqml.Item.TargetDbPreprocessor xml){
        super(environment,pos);
        parameters = PreprocessorDbParameters.Factory.parse(xml);
        setDisplayedInfo(null,"#IF "+parameters.toString());
        
    }
    
    public SqmlTag_TargetDbPreprocessorTag(final IClientEnvironment environment, final int pos, final PreprocessorDbParameters parameters){
        super(environment,pos);
        this.parameters = parameters.copy();
        setDisplayedInfo(null,"#IF "+parameters.toString());
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item tag = (Sqml.Item) itemTag;
        parameters.writeToXml(tag.addNewTargetDbPreprocessor());
    }    

    @Override
    protected String getSettingsPath() {
        return PATH;
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if (isValid()){
            setDisplayedInfo(null,"#IF "+parameters.toString());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public SqmlTag_TargetDbPreprocessorTag copy() {
        return new SqmlTag_TargetDbPreprocessorTag(environment, this);
    }       
}
