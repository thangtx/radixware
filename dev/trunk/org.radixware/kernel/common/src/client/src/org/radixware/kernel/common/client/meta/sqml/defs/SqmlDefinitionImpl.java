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

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


abstract class SqmlDefinitionImpl implements ISqmlDefinition{
    
    private final Id id;
    private final String name;
    private final String moduleName;
    private final Id[] idPath;
    private final SqmlModule module;
    private final boolean isDeprecated;    
    
    public SqmlDefinitionImpl(final SqmlModule module, final Attributes attributes){
        this.module = module;
        id = Id.Factory.loadFrom(attributes.getValue("Id"));
        name = attributes.getValue("Name");
        final Id[] arrId = parseArrId(attributes.getValue("IdPath"));
        idPath = arrId==null ? new Id[]{id} : arrId;
        isDeprecated = parseOptionalBoolean(attributes, "IsDeprecated", false);
        final int moduleNameIndex = attributes.getIndex("ModuleName");
        moduleName = moduleNameIndex>-1 ? attributes.getValue(moduleNameIndex) : module.getQualifiedName();
    }

    @Override
    public boolean isDeprecated(){
        return isDeprecated;
    }

    @Override
    public Id[] getIdPath(){
        return idPath;
    }

    @Override
    public String getDisplayableText(final EDefinitionDisplayMode mode) {
        switch (mode) {
            case SHOW_TITLES:
                return getTitle();
            case SHOW_FULL_NAMES:
                return getFullName();
            default:
                return getShortName();
        }
    }

    @Override
    public String getTitle(){
        return getShortName();
    }

    @Override
    public String getFullName(){
        return moduleName+"::"+name;
    }

    @Override
    public String getModuleName(){
        return moduleName;
    }

    @Override
    public String getShortName(){
        return name;
    }

    @Override
    public Id getId(){
        return id;
    }    
    
    public final IClientEnvironment getEnvironment(){
        return module.getEnvironment();
    }
    
    protected final String getTitle(final Id definitionId, final Id titleId){
        if (titleId != null) {
            try {
                final String title = getEnvironment().getDefManager().getMlStringValue(definitionId, titleId);
                return title==null || title.isEmpty() ? getShortName() : title;
            } catch (DefinitionError err) {
                final String mess = getEnvironment().getMessageProvider().translate("TraceMessage", "Cannot get Title with id \"%s\" for %s");
                getEnvironment().getTracer().error(String.format(mess, titleId, getFullName()), err);
                return getShortName();
            }
        } else {
            return getShortName();
        }
    }
    
    protected final String getTitle(final Id titleId){
        return getTitle(id, titleId);
    }
    
    public static Id[] parseArrId(final String attributeValue){
        if (attributeValue!=null && !attributeValue.isEmpty()){
            final String[] arrIdAsStr = attributeValue.split(" ");
            final List<Id> idList = new LinkedList<>();
            for (String idStr: arrIdAsStr){
                if (idStr!=null && !idStr.trim().isEmpty()){
                    idList.add(Id.Factory.loadFrom(idStr.trim()));
                }
            }
            if (!idList.isEmpty()){
                final Id[] arrId = new Id[idList.size()];                
                for (int i=0,count=idList.size(); i<count; i++){
                    arrId[i] = idList.get(i);
                }
                return arrId;
            }else{
                return null;
            }
        }else{
            return null;
        }        
    }
    
    public static EValType parseValType(final String attrValue){
        if (attrValue==null || attrValue.isEmpty()){
            return null;
        }
        return EValType.getForValue(Long.parseLong(attrValue));        
    }
    
    public static Id parseOptionalId(final Attributes attributes, final String attrName){
        return parseOptionalId(attributes, attrName, null);
    }
    
    public static Id parseOptionalId(final Attributes attributes, final String attrName, final Id defaultId){
        final int idIndex = attributes.getIndex(attrName);
        return idIndex>-1 ? Id.Factory.loadFrom(attributes.getValue(idIndex)) : defaultId;
    }
    
    public static boolean parseOptionalBoolean(final Attributes attributes, final String attrName, final boolean defaultValue){
        final int valIndex = attributes.getIndex(attrName);
        return valIndex>-1 ? "true".equals(attributes.getValue(valIndex)) : defaultValue;
    }
}
