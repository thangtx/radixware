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
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.defs.value.ValAsStr;
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
        private String extTitle;
        private String toolTip;
        private Icon icon;
        private final Id iconId;
        private final Id titleId;
        private final Id extTitleId;
        private final Id toolTipId;
        private final Id titleOwnerId;
        private final Object value;
        
        public Item(final String title, 
                    final Object value,
                    final Icon icon, 
                    final String toolTip,
                    final String extendedTitle) {
            final Object checkedValue = checkValue(value);
            this.title = title;
            this.extTitle = extendedTitle;
            this.toolTip = toolTip;
            this.icon = icon;
            this.titleId = null;
            this.toolTipId = null;
            this.extTitleId = null;
            this.titleOwnerId = null;
            this.value = checkedValue;
            this.iconId = null;
        }
        
        public Item(final String title, 
                    final Object value,
                    final Id iconId, 
                    final String toolTip,
                    final String extendedTitle) {
            final Object checkedValue = checkValue(value);
            this.title = title;
            this.extTitle = extendedTitle;
            this.toolTip = toolTip;
            this.iconId = iconId;
            this.titleId = null;
            this.toolTipId = null;
            this.extTitleId = null;
            this.titleOwnerId = null;
            this.value = checkedValue;
        }        
        
        public Item(final String title, final Object value, final Icon icon, final String toolTip) {
            this(title,value, icon, toolTip, null);
        }

        public Item(final String title, final Object value, final Id iconId, final String toolTip) {
            this(title,value, iconId, toolTip, null);
        }        
        
        public Item(final String title, final Object value, final Icon icon) {
            this(title, value, icon, null, null);
        }
        
        public Item(final String title, final Object value, final Id iconId) {
            this(title, value, iconId, null, null);
        }        

        public Item(final String title, final Object value) {
            this(title, value, (Icon)null, null, null);
        }

        public Item(final Id titleOwnerId, final Id titleId, final Object value) {
            this(titleOwnerId, titleId, value, null, null, null);
        }
        
        public Item(final Id titleOwnerId, 
                    final Id titleId, 
                    final Object value,
                    final Id iconId) {
            this(titleOwnerId, titleId, value, iconId, null, null);
        }        
        
        public Item(final Id titleOwnerId, 
                    final Id titleId, 
                    final Object value,
                    final Id iconId,
                    final Id toolTipId) {
            this(titleOwnerId, titleId, value, iconId, toolTipId, null);
        }        
        
        
        public Item(final Id titleOwnerId, 
                    final Id titleId,
                    final Object value, 
                    final Id iconId,
                    final Id toolTipId,
                    final Id extendedTitleId){
            final Object checkedValue = checkValue(value);
            this.titleId = titleId;
            this.extTitleId = extendedTitleId;
            this.toolTipId = toolTipId;
            this.iconId = iconId;
            this.titleOwnerId = titleOwnerId;
            this.value = checkedValue;
        }
        
        Item(final IClientEnvironment env,             
             final org.radixware.schemas.editmask.EditMaskList.Item xml,
             final EValType type,
             final Id titleOwnerId){
            if (titleOwnerId != null) {
                this.titleOwnerId = titleOwnerId;
            } else {
                this.titleOwnerId = xml.getTitleOwnerId();
            }
            if (xml.getTitleId()!=null){
                titleId = xml.getTitleId();
            }else if (xml.getTitle()!=null){
                title = xml.getTitle();
                titleId = null;
            }else{
                titleId = null;
            }
            if (xml.getToolTipId()!=null){
                toolTipId = xml.getToolTipId();
            }else if (xml.getToolTip()!=null){
                toolTip = xml.getToolTip();
                toolTipId = null;
            }else{
                toolTipId = null;
            }
            if (xml.getExtendedTitleId()!=null){
                extTitleId = xml.getExtendedTitleId();
            }else if (xml.getExtendedTitle()!=null){
                extTitle = xml.getExtendedTitle();
                extTitleId = null;
            }else{
                extTitleId = null;
            }            
            iconId = xml.getIconId();
            if (xml.getValue()==null){
                value = null;
            }else{
                switch (type) {
                    case INT:
                    case ARR_INT:
                        value = Long.valueOf(xml.getValue());
                        break;
                    case CHAR:
                    case ARR_CHAR:
                        value = xml.getValue().charAt(0);
                        break;
                    case NUM:
                    case ARR_NUM:
                        value = new BigDecimal(xml.getValue());
                        break;
                    default:
                        value = xml.getValue();
                }
            }
        }

        private Object checkValue(final Object value) {
            if (value != null
                    && !(value instanceof String)
                    && !(value instanceof Integer)
                    && !(value instanceof Long)
                    && !(value instanceof BigDecimal)
                    && !(value instanceof Character)) {
                throw new IllegalArgumentException("value of simple type Str, Int, Num or Char expected, but value of \"" + value.getClass().getName() + "\" found");
            }
            if (value instanceof Integer) {
                return ((Integer)value).longValue();
            }
            return value;
        }

        public Object getValue() {
            return value;
        }
        
        public String getValAsStr(){
            if (value==null){
                return null;
            }
            if (value instanceof String){
                return ValAsStr.toStr(value, EValType.STR);
            }else if (value instanceof Integer || value instanceof Long){
                return ValAsStr.toStr(value, EValType.INT);
            }else if (value instanceof BigDecimal){
                return ValAsStr.toStr(value, EValType.NUM);
            }else if (value instanceof Character){
                return ValAsStr.toStr(value, EValType.CHAR);
            }else{
                throw new IllegalArgumentException("value of simple type Str, Int, Num or Char expected, but value of \"" + value.getClass().getName() + "\" found");
            }
        }

        public String getTitle(final IClientEnvironment environment) {
            if (title == null && titleId != null && titleOwnerId != null) {
                try {
                    title = environment.getApplication().getDefManager().getMlStringValue(titleOwnerId, titleId);
                } catch (DefinitionError err) {
                    final String mess = environment.getMessageProvider().translate("TraceMessage", "Failed to get title string #%s for edit mask list item");
                    environment.getTracer().error(String.format(mess, titleId.toString()), err);
                    title = environment.getMessageProvider().translate("ExplorerItem", "<No Title>");
                }
            }
            return title==null ? environment.getMessageProvider().translate("ExplorerItem", "<No Title>") : title;
        }
        
        public String getExtendedTitle(final IClientEnvironment environment) {
            if (extTitle == null && extTitleId != null && titleOwnerId != null) {
                try {
                    extTitle = environment.getApplication().getDefManager().getMlStringValue(titleOwnerId, extTitleId);
                } catch (DefinitionError err) {
                    final String mess = environment.getMessageProvider().translate("TraceMessage", "Failed to get extended title string #%s for edit mask list item");
                    environment.getTracer().error(String.format(mess, extTitleId.toString()), err);                    
                }
            }
            return extTitle;
        }        
        
        public String getToolTip(final IClientEnvironment environment) {
            if (toolTip == null && toolTipId != null && titleOwnerId != null) {
                try {
                    toolTip = environment.getApplication().getDefManager().getMlStringValue(titleOwnerId, toolTipId);
                } catch (DefinitionError err) {
                    final String mess = environment.getMessageProvider().translate("TraceMessage", "Failed to get tool tip string #%s for edit mask item");
                    environment.getTracer().error(String.format(mess, toolTipId.toString()), err);                    
                }
            }
            return toolTip;
        }        
        
        public Icon getIcon(final IClientEnvironment environment){
            if (icon==null && iconId!=null){
                try{
                    icon = environment.getDefManager().getImage(iconId);
                }catch (DefinitionError err) {
                    final String mess = environment.getMessageProvider().translate("TraceMessage", "Failed to get item icon #%s for edit mask");
                    environment.getTracer().error(String.format(mess, iconId.toString()), err);                    
                }
            }
            return icon;
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
                if (titleId!=null){
                    if (!Objects.equals(titleOwnerId, other.titleOwnerId)){
                        return false;
                    }
                    if (!Objects.equals(titleId, other.titleId)){
                        return false;
                    }
                }else{
                    if (!Objects.equals(title, other.title)){
                        return false;
                    }
                }
                
                if (toolTipId!=null){
                    if (!Objects.equals(titleOwnerId, other.titleOwnerId)){
                        return false;
                    }
                    if (!Objects.equals(toolTipId, other.toolTipId)){
                        return false;
                    }
                }else{
                    if (!Objects.equals(toolTip, other.toolTip)){
                        return false;
                    }
                }
                
                if (extTitleId!=null){
                    if (!Objects.equals(titleOwnerId, other.titleOwnerId)){
                        return false;
                    }
                    if (!Objects.equals(extTitleId, other.extTitleId)){
                        return false;
                    }
                }else{
                    if (!Objects.equals(extTitle, other.extTitle)){
                        return false;
                    }
                }
                
                if (iconId!=null){
                    if (!Objects.equals(iconId, other.iconId)){
                        return false;
                    }
                } else if (other.iconId != null) {
                    return false;
                }else{
                    if (icon!=other.icon){
                        return false;
                    }
                }
                return true;
            }
            return object.equals(value);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            if (titleId==null){
                hash = 41 * hash + Objects.hashCode(title);
            }else{
                hash = 41 * hash + Objects.hashCode(titleId);
                hash = 41 * hash + Objects.hashCode(titleOwnerId);
            }
            if (iconId==null){
                hash = 41 * hash + Objects.hashCode(icon);
            }else{
                hash = 41 * hash + Objects.hashCode(iconId);
            }
            hash = 41 * hash + Objects.hashCode(toolTipId==null ? toolTip : toolTipId);
            hash = 41 * hash + Objects.hashCode(extTitleId==null ? extTitle : extTitleId);
            hash = 41 * hash + Objects.hashCode(value);
            return hash;
        }
        
        void writeToXml(final org.radixware.schemas.editmask.EditMaskList.Item item){
            if (titleId!=null){
                item.setTitleId(titleId);
                if (titleOwnerId != null) {
                    item.setTitleOwnerId(titleOwnerId);
                }
            }else if (title!=null && !title.isEmpty()){
                item.setTitle(title);
            }
            if (iconId!=null){
                item.setIconId(iconId);
            }
            if (toolTipId!=null){
                item.setToolTipId(toolTipId);
            }else if (toolTip!=null && !toolTip.isEmpty()){
                item.setToolTip(toolTip);
            }
            if (extTitleId!=null){
                item.setExtendedTitleId(extTitleId);
            }else if (extTitle!=null && !extTitle.isEmpty()){
                item.setExtendedTitle(extTitle);
            }
            if (value!=null){
                item.setValue(String.valueOf(value));
            }
        }
        
    }
    
    private final List<Item> items = new ArrayList<>();    
    private Map<Object, Item> itemsByValue = null;
    private int maxItemsInDDList = -1;
    private boolean isAutoSortingEnabled;
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.INT, EValType.NUM, EValType.STR, EValType.CHAR,
            EValType.ARR_INT, EValType.ARR_NUM, EValType.ARR_STR, EValType.ARR_CHAR);

    @Deprecated
    public Item createNewItem(String title, Object value) {
        return new Item(title, value);
    }
    
    @Deprecated
    public Item createNewItem(Id titleOwnerId, Id titleId, Object value) {
        return new Item(titleOwnerId, titleId, value);
    }

    public EditMaskList(final List<Item> items, final boolean autoSortByTitles, final int maxItemsInPopup) {
        super();
        if (items != null) {
            this.items.addAll(items);
        }
        isAutoSortingEnabled = autoSortByTitles;
        maxItemsInDDList = maxItemsInPopup;
    }
    
    public EditMaskList(final List<Item> items, final boolean autoSortByTitles) {
        this(items,autoSortByTitles,-1);
    }    
    
    public EditMaskList(final List<Item> items) {
        this(items,false,-1);
    }
    

    public EditMaskList(final Item[] items, final boolean autoSortByTitles, final int maxItemsInPopup) {
        super();
        if (items != null) {
            this.items.addAll(Arrays.asList(items));

        }
        isAutoSortingEnabled = autoSortByTitles;
        maxItemsInDDList = maxItemsInPopup;        
    }
    
    public EditMaskList(final Item[] items, final boolean autoSortByTitles){
        this(items, autoSortByTitles, -1);
    }
    
    public EditMaskList(final Item[] items){
        this(items, false, -1);
    }

    public EditMaskList(final boolean autoSortByTitles, final int maxItemsInPopup) {
        super();
        isAutoSortingEnabled = autoSortByTitles;
        maxItemsInDDList = maxItemsInPopup;        
    }
    
    public EditMaskList(final boolean autoSortByTitles) {
        this(autoSortByTitles,-1);
    }
    
    public EditMaskList() {
        this(false,-1);
    }

    public EditMaskList(EditMaskList source) {
        super();
        if (source != null) {
            items.addAll(source.items);            
            maxItemsInDDList = source.maxItemsInDDList;
            isAutoSortingEnabled = source.isAutoSortingEnabled;
        }
    }

    @SuppressWarnings("PMD.MissingBreakInSwitch")
    protected EditMaskList(IClientEnvironment env, org.radixware.schemas.editmask.EditMaskList editMask,
            final EValType type, final Id titleOwnerId) {
        super();
        if (editMask.getItemList() != null) {
            for (org.radixware.schemas.editmask.EditMaskList.Item item : editMask.getItemList()) {
                items.add(new Item(env,item,type,titleOwnerId));
            }
        }
        maxItemsInDDList = editMask.getMaxItemsInDDList();
        isAutoSortingEnabled = editMask.isSetAutoSortByTitle();
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskList editMaskList = editMask.addNewList();
        for (Item item : items) {            
            item.writeToXml(editMaskList.addNewItem());
        }
        if (maxItemsInDDList>=0){
            editMaskList.setMaxItemsInDDList(maxItemsInDDList);
        }
        if (isAutoSortingEnabled){
            editMaskList.setAutoSortByTitle(true);
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

    public Item addItem(final String title, final Object value, final Icon icon, final String toolTip, final String extTitle) {
        final Item item = new Item(title, value, icon, toolTip, extTitle);
        items.add(item);
        itemsByValue = null;
        afterModify();
        return item;
    }
    
    public Item addItem(final String title, final Object value, final Id iconId, final String toolTip, final String extTitle) {
        final Item item = new Item(title, value, iconId, toolTip, extTitle);
        items.add(item);
        itemsByValue = null;
        afterModify();
        return item;
    }
    
    public Item addItem(final int idx, final String title, final Object value, final Icon icon, final String toolTip, final String extTitle) {
        final Item item = new Item(title, value, icon, toolTip, extTitle);
        items.add(idx, item);
        itemsByValue = null;
        afterModify();
        return item;
    }
    
    public Item addItem(final int idx, final String title, final Object value, final Id iconId, final String toolTip, final String extTitle) {
        final Item item = new Item(title, value, iconId, toolTip, extTitle);
        items.add(idx, item);
        itemsByValue = null;
        afterModify();
        return item;
    }     
    
    public Item addItem(final String title, final Object value, final Icon icon, final String toolTip) {
        return addItem(title, value, icon, toolTip, null);
    }
    
    public Item addItem(final String title, final Object value, final Id iconId, final String toolTip) {
        return addItem(title, value, iconId, toolTip, null);
    }    
    
    public Item addItem(final String title, final Object value, final Icon icon) {
        return addItem(title, value, icon, null, null);
    }
    
    public Item addItem(final String title, final Object value, final Id iconId) {
        return addItem(title, value, iconId, null, null);
    }    
    
    public Item addItem(final String title, final Object value) {
        return addItem(title, value, (Icon)null, null, null);
    }
    
    public Item addItem(final int idx, final String title, final Object value, final Icon icon, final String toolTip) {
        return addItem(idx, title, value, icon, toolTip, null);
    }
    
    public Item addItem(final int idx, final String title, final Object value, final Id iconId, final String toolTip) {
        return addItem(idx, title, value, iconId, toolTip, null);
    }    
    
    public Item addItem(final int idx, final String title, final Object value, final Icon icon) {
        return addItem(idx, title, value, icon, null, null);
    }
    
    public Item addItem(final int idx, final String title, final Object value, final Id iconId) {
        return addItem(idx, title, value, iconId, null, null);
    }    
    
    public Item addItem(final int idx, final String title, final Object value) {
        return addItem(idx, title, value, (Icon)null, null, null);
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
    
    public int getMaxIntemsNumberInDropDownList(){
        return maxItemsInDDList;
    }
    
    public void setMaxItemsNumberInDropDownList(final int maxNumber){
        maxItemsInDDList = maxNumber;
        afterModify();
    }
    
    public boolean isAutoSortByTitles(){
        return isAutoSortingEnabled;
    }
    
    public void setAutoSortByTitles(final boolean sort){
        isAutoSortingEnabled = sort;
        afterModify();
    }

    @Override
    public String toStr(final IClientEnvironment environment, final Object o) {
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
        return ValidationResult.Factory.newInvalidResult(InvalidValueReason.NO_REASON);
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
        hash = 31 * hash + maxItemsInDDList;
        hash = 31 * hash + (isAutoSortingEnabled ? 1 : 0);
        hash = 31 * hash + super.hashCode();
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
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
        if (maxItemsInDDList!=other.maxItemsInDDList){
            return false;
        }
        if (isAutoSortingEnabled!=other.isAutoSortingEnabled){
            return false;
        }
        if (!Objects.equals(this.items, other.items)) {
            return false;
        }
        return super.equals(obj);
    }        
}
