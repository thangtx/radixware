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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.KernelIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.xml.sax.Attributes;


final class SqmlEnumDefImpl extends SqmlDefinitionImpl implements ISqmlEnumDef{
    
    private final List<ISqmlEnumItem> items = new ArrayList<>();    
    private final Id titleId;
    private final EValType itemType;

    public SqmlEnumDefImpl(final SqmlModule module, final Attributes attributes, final List<SqmlEnumItemImpl> items){
        super(module,attributes);        
        for (SqmlEnumItemImpl item: items){
            this.items.add(item);
        }
        titleId = Id.Factory.loadFrom(attributes.getValue("TitleId"));        
        itemType = SqmlDefinitionImpl.parseValType(attributes.getValue("ValType"));
    }

    @Override
    public ClientIcon getIcon() {
        switch (itemType) {
            case INT:
                return  KernelIcon.getInstance(RadixObjectIcon.ENUM_INT);
            case CHAR:
                return KernelIcon.getInstance(RadixObjectIcon.ENUM_CHAR);
            case STR:
                return KernelIcon.getInstance(RadixObjectIcon.ENUM_STR);
            default:
                return KernelIcon.getInstance(RadixObjectIcon.UNKNOWN);
        }
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public ISqmlEnumItem get(int idx) {
        return items.get(idx);
    }

    @Override
    public ISqmlEnumItem findItemById(Id itemId) {
        for (ISqmlEnumItem item: items){
            if (item.getId().equals(itemId)){
                return item;
            }
        }
        return null;
    }

    @Override
    public ISqmlEnumItem findItemByValue(final Object value) {
        final String valAsStr = ValAsStr.Factory.newInstance(value, itemType).toString();
        for (ISqmlEnumItem item: items){
            if (item.getValueAsString().equals(valAsStr)){
                return item;
            }
        }
        return null;
    }

    @Override
    public EValType getItemType() {
        return itemType;
    }

    @Override
    public Iterator<ISqmlEnumItem> iterator() {
        return items.iterator();
    }        

    @Override
    public String getTitle() {
        return getTitle(titleId);
    }        
}
