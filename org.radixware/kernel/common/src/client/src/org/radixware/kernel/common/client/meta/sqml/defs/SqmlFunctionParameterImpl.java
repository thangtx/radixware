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

import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlFunctionParameterImpl extends SqmlDefinitionImpl implements ISqmlFunctionParameter{
    
    private final EValType valType;
    private final EParamDirection direction;
    private final String defaultVal;
    private final String dbName;
    private final String dbType;
    private final Id ownerFunctionId;
    private final Id ownerPackageId;
    
    public SqmlFunctionParameterImpl(final SqmlModule module, 
                                                        final Attributes attributes,
                                                        final String defaultValue,
                                                        final Id ownerFunctionId,
                                                        final Id ownerPackageId){
        super(module,attributes);
        final int valTypeAttrIndex = attributes.getIndex("ValType");
        if (valTypeAttrIndex>-1){
            valType = SqmlDefinitionImpl.parseValType(attributes.getValue("ValType"));
        }else{
            valType = null;
        }
        if (defaultValue==null){
            defaultVal = null;
        }else{
            defaultVal = org.radixware.kernel.common.build.xbeans.XmlEscapeStr.parseSafeXmlString(defaultValue);
        }
        this.ownerFunctionId = ownerFunctionId;
        this.ownerPackageId = ownerPackageId;
        dbName = attributes.getValue("DbName");
        dbType = attributes.getValue("DbType");
        final Long directionValue = Long.parseLong(attributes.getValue("Direction"));
        direction = EParamDirection.getForValue(directionValue);
    }
    
    public String getDbName(){
        return dbName;        
    }
    
    public String getDbType(){
        return dbType;
    }
    
    public EParamDirection getDirection(){
        return direction;
    }

    @Override
    public ClientIcon getIcon() {
        if (valType==null){
            return KernelIcon.getInstance(RadixObjectIcon.UNKNOWN);
        }else{
            return KernelIcon.getInstance(RadixObjectIcon.getForValType(valType));
        }
    }

    @Override
    public String getDefaultVal() {
        return defaultVal;
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{ownerPackageId, ownerPackageId, ownerFunctionId, getId()};//DdsFunctionDef.getIdPath() returns path with double package id
    }

    @Override
    public String getFullName() {
        return getShortName();
    }

    @Override
    public String getTitle() {
        return getShortName();
    }

    @Override
    public String getDisplayableText(final EDefinitionDisplayMode mode) {
        return getShortName();
    }
}