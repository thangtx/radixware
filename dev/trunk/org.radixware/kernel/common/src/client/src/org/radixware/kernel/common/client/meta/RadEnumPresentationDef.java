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

package org.radixware.kernel.common.client.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class RadEnumPresentationDef extends NamedDefinition {
    
    public final static RadEnumPresentationDef EMPTY_ENUM_DEF = new RadEnumPresentationDef();

    public class Item {

        final private org.radixware.kernel.common.meta.RadEnumDef.Item kernelEnumItem;
        final EValType type;
        final Id enumId;

        private Item(final org.radixware.kernel.common.meta.RadEnumDef.Item item,
                final EValType valType,
                final Id ownerId) {
            kernelEnumItem = item;
            type = valType;
            this.enumId = ownerId;
        }        

        public Id getId() {
            return kernelEnumItem.getId();
        }

        public Id getEnumId() {
            return enumId;
        }

        public ValAsStr getValAsStr() {
            return kernelEnumItem.getValue();
        }

        public IKernelEnum getConstant() {
            return kernelEnumItem.getConstant();
        }

        public Comparable getValue() {
            switch (type) {
                case INT:
                    return (Long) kernelEnumItem.getValue().toObject(type);
                case CHAR:
                    return (Character) kernelEnumItem.getValue().toObject(type);
                case STR:
                    return (String) kernelEnumItem.getValue().toObject(type);
                default:
                    throw new WrongFormatError("Unsupported enumeration item type: \'" + type.getName() + "\'");
            }
        }

        public EValType getValType() {
            return type;
        }

        public String getName() {
            return kernelEnumItem.getName();
        }

        public String getTitle() {
            return kernelEnumItem.getTitle(RadEnumPresentationDef.this.getApplication());            
        }
        
        public String getTitle(final EIsoLanguage language){
            return kernelEnumItem.getTitle(RadEnumPresentationDef.this.getApplication(), language);
        }

        public Icon getIcon() {
            if (kernelEnumItem.getIconId() != null) {
                try {
                    return getDefManager().getImage(kernelEnumItem.getIconId());
                } catch (DefinitionError err) {
                    getApplication().getTracer().put(err);
                    return null;
                }
            } else {
                return null;
            }
        }

        public int getViewOrder() {
            return kernelEnumItem.getViewOrder();
        }
        
        public boolean isDeprecated(){
            return kernelEnumItem.isDeprecated() && !RadEnumPresentationDef.this.isDeprecated();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 79 * hash + Objects.hashCode(getId());
            hash = 79 * hash + (this.type != null ? this.type.hashCode() : 0);
            hash = 79 * hash + Objects.hashCode(this.enumId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj==this){
                return true;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Item other = (Item) obj;
            if (!Objects.equals(getId(), other.getId())) {
                return false;
            }
            if (this.type != other.type) {
                return false;
            }
            return Objects.equals(this.enumId, other.enumId);
        }
        
        
    }

    public static enum ItemsOrder {

        BY_NAME, BY_TITLE, BY_VALUE, BY_ORDER;

        public static ItemsOrder fromEditMaskEnumOrder(EEditMaskEnumOrder from) {
            switch (from) {
                case BY_TITLE:
                    return BY_TITLE;
                case BY_VALUE:
                    return BY_VALUE;
                default:
                    return BY_ORDER;
            }
        }
    }

    public class Items implements Iterable<RadEnumPresentationDef.Item> {

        private final List<Item> content = new ArrayList<>();        

        protected Items() {
        }

        protected Items(List<Item> enumItems) {
            for (Item item : enumItems) {
                addItem(item);
            }
        }

        public Items copy() {
            return new Items(this);
        }

        protected Items(Items copy) {
            content.addAll(copy.content);            
        }

        private Items(org.radixware.kernel.common.meta.RadEnumDef kernelEnumDef) {
            IEnumDef.IItems<? extends IEnumDef.IItem> itemsContainer = kernelEnumDef.getItems();
            final List<? extends IEnumDef.IItem> kernelItems = itemsContainer.list(EScope.ALL);
            org.radixware.kernel.common.meta.RadEnumDef.Item kernelItem;
            for (IEnumDef.IItem iitem : kernelItems) {
                kernelItem = (org.radixware.kernel.common.meta.RadEnumDef.Item) iitem;
                content.add(new Item(kernelItem, kernelEnumDef.getItemType(), kernelEnumDef.getId()));
            }            
        }

        @Override
        public Iterator<Item> iterator() {
            return content.iterator();
        }

        public Item findItemByValue(final ValAsStr value) {
            if (value == null) {
                return null;
            }
            for (Item item : content) {
                if (item.getValAsStr().equals(value)) {
                    return item;
                }
            }
            return null;
        }

        public Item findItemByValue(final Comparable value) {
            return findItemByValue(ValAsStr.Factory.newInstance(value, itemType));
        }

        public Item findItemById(final Id id) {
            for (Item item : content) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
            return null;
        }

        public Item findItemForConstant(final IKernelEnum constant) {
            for (Item item : content) {
                if (item.getConstant() == constant) {
                    return item;
                }
            }
            return null;
        }

        public List<Item> asList() {
            return Collections.unmodifiableList(content);
        }

        public void clear() {
            content.clear();
        }

        public boolean isEmpty() {
            return content.isEmpty();
        }

        public int size() {
            return content.size();
        }

        public Item getItem(final int index) {
            return content.get(index);
        }

        public void addItemsById(final List<Id> itemIds) {
            for (Id id : itemIds) {
                addItemWithId(id);
            }
        }

        public void addItems(final Items moreItems) {
            for (Item item : moreItems.content) {
                addItem(item);
            }
        }

        public void addItemWithValue(final Comparable value) {
            final Item item = RadEnumPresentationDef.this.items.findItemByValue(value);
            if (item == null) {
                final String message = getApplication().getMessageProvider().translate("ExplorerError", "Enumeration item with value \'%s\' was not found in enumeration \'%s\'");
                throw new NoConstItemWithSuchValueError(String.format(message, String.valueOf(value), RadEnumPresentationDef.this.getName()), value);
            } else if (!content.contains(item)) {
                content.add(item);
            }
        }

        public void insertItemWithValue(final int index, final Comparable value) {
            final Item item = RadEnumPresentationDef.this.items.findItemByValue(value);
            if (item == null) {
                final String message = getApplication().getMessageProvider().translate("ExplorerError", "Enumeration item with value \'%s\' was not found in enumeration \'%s\'");
                throw new NoConstItemWithSuchValueError(String.format(message, String.valueOf(value), RadEnumPresentationDef.this.getName()), value);
            } else if (!content.contains(item)) {
                content.add(index, item);
            }
        }

        public void addItemWithId(final Id id) {
            final Item item = RadEnumPresentationDef.this.items.findItemById(id);
            if (item == null) {
                throw new NoDefinitionWithSuchIdError(RadEnumPresentationDef.this, NoDefinitionWithSuchIdError.SubDefinitionType.ENUM_ITEM, id);
            } else if (!contains(item)) {
                content.add(item);
            }
        }

        public void insertItemWithId(final int index, final Id id) {
            final Item item = RadEnumPresentationDef.this.items.findItemById(id);
            if (item == null) {
                throw new NoDefinitionWithSuchIdError(RadEnumPresentationDef.this, NoDefinitionWithSuchIdError.SubDefinitionType.ENUM_ITEM, id);
            } else if (!contains(item)) {
                content.add(index, item);
            }
        }

        public void addItemForConstant(final IKernelEnum constant) {
            final Item item = RadEnumPresentationDef.this.items.findItemForConstant(constant);
            if (item == null) {
                throw new IllegalArgumentException(
                        String.format("Enumeration presentation \"%s\" does not contains item for constant \"%s\"",
                        RadEnumPresentationDef.this.getName(), constant.getName()));
            } else if (!contains(item)) {
                content.add(item);
            }
        }

        public void insertItemForConstant(final int index, final IKernelEnum constant) {
            final Item item = RadEnumPresentationDef.this.items.findItemForConstant(constant);
            if (item == null) {
                throw new IllegalArgumentException(
                        String.format("Enumeration presentation \"%s\" does not contains item for constant \"%s\"",
                        RadEnumPresentationDef.this.getName(), constant.getName()));
            } else if (!contains(item)) {
                content.add(index, item);
            }
        }

        public final void addItem(final Item item) {
            if (!item.getEnumId().equals(RadEnumPresentationDef.this.getId())) {
                throw new IllegalArgumentException("Cannot add item of enumeration #" + item.getEnumId() + " to items of enumeration #" + RadEnumPresentationDef.this.getId());
            }
            if (!contains(item)) {
                content.add(item);
            }
        }

        public void insertItem(final int index, final Item item) {
            if (!item.getEnumId().equals(RadEnumPresentationDef.this.getId())) {
                throw new IllegalArgumentException("Cannot add item of enumeration #" + item.getEnumId() + " to items of enumeration #" + RadEnumPresentationDef.this.getId());
            }
            if (!contains(item)) {
                content.add(index, item);
            }
        }

        public void addItem(final IKernelEnum item) {
            addItemWithValue(item.getValue());
        }

        public void insertItem(final int index, final IKernelEnum item) {
            insertItemWithValue(index, item.getName());
        }

        public void removeItemsById(final List<Id> itemIds) {
            for (int i = content.size() - 1; i >= 0; i--) {
                if (itemIds.contains(content.get(i).getId())) {
                    content.remove(i);
                }
            }
        }

        public void removeItemWithId(final Id id) {
            for (int i = content.size() - 1; i >= 0; i--) {
                if (content.get(i).getId().equals(id)) {
                    content.remove(i);
                    return;
                }
            }
        }

        public void removeItemForConstant(final IKernelEnum constant) {
            for (int i = content.size() - 1; i >= 0; i--) {
                if (content.get(i).getConstant() == constant) {
                    content.remove(i);
                    return;
                }
            }
        }

        public void removeItem(final Item item) {
            content.remove(item);
        }

        public void removeItemWithValue(final Comparable value) {
            for (int i = content.size() - 1; i >= 0; i--) {
                if (content.get(i).getValue().equals(value)) {
                    content.remove(i);
                    return;
                }
            }
        }

        public void removeItem(final IKernelEnum item) {
            for (int i = content.size() - 1; i >= 0; i--) {
                if (content.get(i).getConstant() == item) {
                    content.remove(i);
                    return;
                }
            }
        }

        public void removeItems(final Items itemsToRemove) {
            for (Item item : itemsToRemove) {
                content.remove(item);
            }
        }
        
        public void removeDeprecatedItems(){
            for (int i=content.size()-1; i>=0; i--){
                if (content.get(i).isDeprecated()){
                    content.remove(i);
                }
            }
        }                

        public void sort(ItemsOrder orderedBy) {
            Collections.sort(content, new ItemComparator(orderedBy));
        }

        public boolean contains(Item item) {
            if (item != null) {
                for (Item i : content) {
                    if (i.getEnumId().equals(item.getEnumId()) && Utils.equals(i.getValue(), item.getValue())) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        public RadEnumPresentationDef getEnumPresentationDef(){
            return RadEnumPresentationDef.this;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.content);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj==this){
                return true;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Items other = (Items) obj;
            return Objects.equals(this.content, other.content);
        }
    }

    private static class ItemComparator implements Comparator<Item> {

        private final ItemsOrder order;

        public ItemComparator(ItemsOrder itemsOrder) {
            order = itemsOrder;
        }

        @Override
        @SuppressWarnings("unchecked")
        public int compare(Item item1, Item item2) {
            switch (order) {
                case BY_NAME:
                    return item1.getName().compareTo(item2.getName());
                case BY_TITLE:
                    return compareStrings(item1.getTitle(), item2.getTitle());
                case BY_VALUE: {
                    if (item1.type != item2.type) {
                        throw new IllegalArgumentException("Cannot compare item with different types: " + item1.type.name() + " and " + item2.type.name());
                    }
                    switch (item1.type) {
                        case INT:                            
                        case CHAR:
                        case STR:                            
                            return item1.getValue().compareTo(item2.getValue());
                        default:
                            throw new WrongFormatError("Unsupported enumeration item type: \"" + item1.type.getName() + "\"");
                    }

                }
                default:
                    if (item1.getViewOrder() < 0 && item2.getViewOrder() < 0) {
                        return compareStrings(item1.getTitle(), item2.getTitle());
                    } else if (item1.getViewOrder() > item2.getViewOrder()) {
                        return 1;
                    } else if (item2.getViewOrder() > item1.getViewOrder()) {
                        return -1;
                    }
                    return 0;
            }
        }

        private static int compareStrings(final String s1, final String s2) {
            if (s1 == null) {
                return s2 == null ? 0 : -1;
            }
            return s2 == null ? 1 : s1.compareTo(s2);
        }
    }
    private final Items items;
    private final EValType itemType;
    private final boolean isDeprecated;
    
    private RadEnumPresentationDef() {
        super(Id.Factory.loadFrom("acsDUMMY_ENUM"),"dummy");
        itemType = EValType.INT;
        items = new Items();
        isDeprecated = false;        
    }

    public RadEnumPresentationDef(org.radixware.kernel.common.meta.RadEnumDef kernelEnumDef) {
        super(kernelEnumDef.getId(), kernelEnumDef.getName());
        itemType = kernelEnumDef.getItemType();
        items = new Items(kernelEnumDef);
        isDeprecated = kernelEnumDef.isDeprecated();
    }
    
    public boolean isDeprecated(){
        return isDeprecated;
    }

    public Items getItems() {
        return new Items(items);
    }

    public Item getItem(final IKernelEnum enumItem) {
        for (Item item : items) {
            if (item.getConstant() == enumItem) {
                return item;
            }
        }
        final String message = getApplication().getMessageProvider().translate("ExplorerError", "Enumeration item with name \'%s\' and value \'%s\' was not found in enumeration \'%s\'");
        throw new NoConstItemWithSuchValueError(String.format(message, enumItem.getName(), String.valueOf(enumItem.getValue()), RadEnumPresentationDef.this.getName()), enumItem.getValue());
    }

    public Item findItemByValue(final Comparable value) {
        return items.findItemByValue(value);
    }

    public Item findItemForConstant(final IKernelEnum constant) {
        return items.findItemForConstant(constant);
    }

    public Items getEmptyItems() {
        return new Items();
    }

    public Items getItems(final Id id0, final Id id1, final Id id2, final Id id3, final Id id4) {
        final Items result = new Items();
        result.addItemWithId(id0);
        result.addItemWithId(id1);
        result.addItemWithId(id2);
        result.addItemWithId(id3);
        result.addItemWithId(id4);
        return result;
    }

    public Items getItems(final Id id0, final Id id1, final Id id2, final Id id3) {
        final Items result = new Items();
        result.addItemWithId(id0);
        result.addItemWithId(id1);
        result.addItemWithId(id2);
        result.addItemWithId(id3);
        return result;
    }

    public Items getItems(final Id id0, final Id id1, final Id id2) {
        final Items result = new Items();
        result.addItemWithId(id0);
        result.addItemWithId(id1);
        result.addItemWithId(id2);
        return result;
    }

    public Items getItems(final Id id0, final Id id1) {
        final Items result = new Items();
        result.addItemWithId(id0);
        result.addItemWithId(id1);
        return result;
    }

    public Items getItems(final Id id0) {
        final Items result = new Items();
        result.addItemWithId(id0);
        return result;
    }

    public Items getItems(final IKernelEnum const0, final IKernelEnum const1, final IKernelEnum const2, final IKernelEnum const3, final IKernelEnum const4) {
        final Items result = new Items();
        result.addItemForConstant(const0);
        result.addItemForConstant(const1);
        result.addItemForConstant(const2);
        result.addItemForConstant(const3);
        result.addItemForConstant(const4);
        return result;
    }

    public Items getItems(final IKernelEnum const0, final IKernelEnum const1, final IKernelEnum const2, final IKernelEnum const3) {
        final Items result = new Items();
        result.addItemForConstant(const0);
        result.addItemForConstant(const1);
        result.addItemForConstant(const2);
        result.addItemForConstant(const3);
        return result;
    }

    public Items getItems(final IKernelEnum const0, final IKernelEnum const1, final IKernelEnum const2) {
        final Items result = new Items();
        result.addItemForConstant(const0);
        result.addItemForConstant(const1);
        result.addItemForConstant(const2);
        return result;
    }

    public Items getItems(final IKernelEnum const0, final IKernelEnum const1) {
        final Items result = new Items();
        result.addItemForConstant(const0);
        result.addItemForConstant(const1);
        return result;
    }

    public Items getItems(final IKernelEnum const0) {
        final Items result = new Items();
        result.addItemForConstant(const0);
        return result;
    }

    public EValType getItemType() {
        return itemType;
    }
}
