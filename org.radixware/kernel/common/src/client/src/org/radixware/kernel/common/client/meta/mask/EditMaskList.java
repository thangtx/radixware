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

package org.radixware.kernel.common.client.meta.mask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.ArrChar;
import org.radixware.kernel.common.types.ArrInt;
import org.radixware.kernel.common.types.ArrNum;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public final class EditMaskList extends org.radixware.kernel.common.client.meta.mask.EditMask {

    final public static class Item {

        private String title;
        private Id titleId;
        final private Id titleOwnerId;
        final private Object value;

        public Item(String title, Object value) {
            checkValue(value);
            this.title = title;
            this.titleId = null;
            this.titleOwnerId = null;
            this.value = value;
        }

        public Item(Id titleOwnerId, Id titleId, Object value) {
            checkValue(value);
            this.title = null;
            this.titleId = titleId;
            this.titleOwnerId = titleOwnerId;
            this.value = value;
        }

        private void checkValue(final Object value) {
            if (value != null
                    && !(value instanceof String)
                    && !(value instanceof Long)
                    && !(value instanceof BigDecimal)
                    && !(value instanceof Character)) {
                throw new IllegalArgumentException("value of simple type Str, Int, Num or Char expected, but value of \"" + value.getClass().getName() + "\" found");
            }
        }

        public Object getValue() {
            return value;
        }

        public String getTitle(IClientEnvironment environment) {
            if (title == null && titleId != null && titleOwnerId != null) {
                try {
                    title = environment.getApplication().getDefManager().getMlStringValue(titleOwnerId, titleId);
                } catch (DefinitionError err) {
                    final String mess = environment.getMessageProvider().translate("TraceMessage", "Cannot get item title #%s for edit mask");
                    environment.getTracer().error(String.format(mess, titleId.toString()), err);
                    title = environment.getMessageProvider().translate("ExplorerItem", "<No Title>");
                }
            }
            return title;
        }

        @Override
        public boolean equals(final Object object) {
            if (object == null) {
                return value == null;
            }
            if (this == object) {
                return true;
            }
            if (object instanceof Item) {
                final Item other = (Item)object;
                if (!Objects.equals(value, other.value)){
                    return false;
                }
                if (title==null){
                    if (!Objects.equals(titleOwnerId, other.titleOwnerId)){
                        return false;
                    }
                    return Objects.equals(titleId, other.titleId);
                }else{
                    return Objects.equals(title, other.title);
                }
            }
            return object.equals(value);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            if (title==null){
                hash = 41 * hash + Objects.hashCode(this.titleId);
                hash = 41 * hash + Objects.hashCode(this.titleOwnerId);                
            }else{
                hash = 41 * hash + Objects.hashCode(this.title);
            }
            hash = 41 * hash + Objects.hashCode(this.value);
            return hash;
        }
        
    }
    private final List<Item> items = new ArrayList<>();
    private Map<Object, Item> itemsByValue = null;
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.INT, EValType.NUM, EValType.STR, EValType.CHAR,
            EValType.ARR_INT, EValType.ARR_NUM, EValType.ARR_STR, EValType.ARR_CHAR);

    public Item createNewItem(String title, Object value) {
        return new Item(title, value);
    }

    public Item createNewItem(Id titleOwnerId, Id titleId, Object value) {
        return new Item(titleOwnerId, titleId, value);
    }

    public EditMaskList(List<Item> items) {
        super();
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public EditMaskList(Item[] items) {
        super();
        if (items != null) {
            this.items.addAll(Arrays.asList(items));

        }
    }

    public EditMaskList() {
        super();
    }

    public EditMaskList(EditMaskList source) {
        super();
        if (source != null) {
            items.addAll(source.items);

        }
    }

    @SuppressWarnings("PMD.MissingBreakInSwitch")
    protected EditMaskList(IClientEnvironment env, org.radixware.schemas.editmask.EditMaskList editMask,
            final EValType type, final Id titleOwnerId) {
        super();

        if (editMask.getItemList() != null) {
            String itemTitle;
            for (org.radixware.schemas.editmask.EditMaskList.Item item : editMask.getItemList()) {
                if (item.getTitleId() != null) {
                    try {
                        itemTitle = env.getDefManager().getMlStringValue(titleOwnerId, item.getTitleId());
                    } catch (DefinitionError err) {
                        final String mess = env.getMessageProvider().translate("TraceMessage", "Cannot get item title #%s for edit mask");
                        env.getTracer().error(String.format(mess, item.getTitleId().toString()), err);
                        itemTitle = env.getMessageProvider().translate("ExplorerItem", "<No Title>");
                    }
                } else {
                    itemTitle = item.getTitle();
                }
                final Item listItem;
                switch (type) {
                    case INT:
                    case ARR_INT:
                        listItem = new Item(itemTitle, Long.valueOf(item.getValue()));
                        break;
                    case CHAR:
                    case ARR_CHAR:
                        listItem = new Item(itemTitle, Character.valueOf(item.getValue().charAt(0)));
                        break;
                    case NUM:
                    case ARR_NUM:
                        listItem = new Item(itemTitle, new BigDecimal(item.getValue()));
                        break;
                    default:
                        listItem = new Item(itemTitle, item.getValue());                        
                }
                listItem.titleId = item.getTitleId();
                items.add(listItem);
            }
        }
    }

    @Override
    public void writeToXml(org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskList editMaskList = editMask.addNewList();
        org.radixware.schemas.editmask.EditMaskList.Item listItem;
        for (Item item : items) {
            listItem = editMaskList.addNewItem();
            if (item.titleId != null) {
                listItem.setTitleId(item.titleId);
            } else if (item.title != null) {
                listItem.setTitle(item.title);
            }
            if (item.value != null) {
                listItem.setValue(String.valueOf(item.value));
            }
        }
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Object getValueByIdx(int idx) {
        return items.get(idx).value;
    }

    public void setItems(List<Item> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        itemsByValue = null;
        afterModify();

    }

    public Item addItem(String title, Object value) {
        final Item item = new Item(title, value);
        items.add(item);
        itemsByValue = null;
        afterModify();
        return item;
    }

    public Item addItem(int idx, String title, Object value) {
        final Item item = new Item(title, value);
        items.add(idx, item);
        itemsByValue = null;
        afterModify();
        return item;
    }

    public void clearItems() {
        items.clear();
        itemsByValue = null;
        afterModify();
    }

    public void removeItem(String title) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).title.equals(title)) {
                items.remove(i);
            }
        }
        itemsByValue = null;
        afterModify();
    }
    
    public void removeItemsWithValue(Object value) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (Utils.equals(value, items.get(i).getValue())) {
                items.remove(i);
            }
        }
        itemsByValue = null;
        afterModify();
    }
    

    @Override
    public String toStr(IClientEnvironment environment, Object o) {
        //if (wasInherited()) { return INHERITED_VALUE; }
        if (o instanceof ArrInt
                || o instanceof ArrNum
                || o instanceof ArrStr
                || o instanceof ArrChar) {
            return arrToStr(environment, (Arr) o);
        }

        if (itemsByValue == null) {
            itemsByValue = new HashMap<>(items.size());
            for (Item item : items) {
                itemsByValue.put(item.value, item);
            }
        }
        final Item item = itemsByValue.get(o);
        if (item==null){
            return o==null ? getNoValueStr(environment.getMessageProvider()) : o.toString();
        }else{
            return item.getTitle(environment);
        }
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        if (value == null) {
            return ValidationResult.ACCEPTABLE;
        }
        if (value instanceof Arr) {
            return validateArray(environment,(Arr)value);
        }
        for (Item item : items) {
            if (item.equals(value)) {
                return ValidationResult.ACCEPTABLE;
            }
        }
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
    }


    @Override
    public EEditMaskType getType() {
        return EEditMaskType.LIST;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.items);
        hash = 31 * hash + super.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this){
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EditMaskList other = (EditMaskList) obj;
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        return super.equals(obj);
    }        
}
