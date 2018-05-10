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

import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;

final class SqmlEnumItemImpl extends SqmlDefinitionImpl implements ISqmlEnumItem{
    
    private final String valAsStr;
    private final Id titleId;
    private final Id ownerEnumId;    
    private final String ownerEnumName;
    private final EValType valType;
    private final boolean ownerEnumIsDeprecated;
    
    public SqmlEnumItemImpl(final SqmlModule module, 
                                            final Attributes attributes,
                                            final String valAsStr,
                                            final Attributes enumAttributes){
        super(module,attributes);
        if (valAsStr==null){
            this.valAsStr = null;                        
        }else{
            this.valAsStr = org.radixware.kernel.common.build.xbeans.XmlEscapeStr.parseSafeXmlString(valAsStr);
        }
        ownerEnumId = Id.Factory.loadFrom(enumAttributes.getValue("Id"));
        ownerEnumName = enumAttributes.getValue("Name");
        titleId = Id.Factory.loadFrom(attributes.getValue("TitleId"));        
        valType = SqmlDefinitionImpl.parseValType(enumAttributes.getValue("ValType"));
        final int isDeprecatedIndex = enumAttributes.getIndex("IsDeprecated");
        ownerEnumIsDeprecated = isDeprecatedIndex>-1 ? "true".equals(enumAttributes.getValue(isDeprecatedIndex)) : false;
    }

    @Override
    public ClientIcon getIcon() {                
        switch (valType) {
            case INT:
                return  KernelIcon.getInstance(RadixObjectIcon.ENUM_ITEM_INT);
            case CHAR:
                return KernelIcon.getInstance(RadixObjectIcon.ENUM_ITEM_CHAR);
            case STR:
                return KernelIcon.getInstance(RadixObjectIcon.ENUM_ITEM_STR);
            default:
                return KernelIcon.getInstance(RadixObjectIcon.UNKNOWN);
        }        
    }

    @Override
    public Id getOwnerEnumId() {
        return ownerEnumId;
    }

    @Override
    public String getValueAsString() {
        return valAsStr;
    }        

    @Override
    public String getFullName() {
        return getModuleName()+"::"+ownerEnumName+":"+getShortName();
    }

    @Override
    public String getTitle() {
        return getTitle(ownerEnumId, titleId);
    }

    @Override
    public Id[] getIdPath() {
        return new Id[]{ownerEnumId, getId()};
    }

    @Override
    public boolean isDeprecated() {
        return super.isDeprecated() || ownerEnumIsDeprecated;
    }        
}
