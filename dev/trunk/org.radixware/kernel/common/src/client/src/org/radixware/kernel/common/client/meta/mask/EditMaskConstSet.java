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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditMaskEnumCorrection;
import org.radixware.kernel.common.enums.EEditMaskEnumOrder;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Arr;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;

public final class EditMaskConstSet extends org.radixware.kernel.common.client.meta.mask.EditMask {        

    private final Id constSetId;
    private final boolean dummyInstance;
    private EEditMaskEnumOrder order;
    private EEditMaskEnumCorrection correction;
    private final List<Id> correctionItems;
    private RadEnumPresentationDef.Items items;
    private RadEnumPresentationDef.Items includedItems;
    private RadEnumPresentationDef.Items excludedItems;
    private DisplayMode mode = DisplayMode.SHOW_TITLE;
    private static final EnumSet<EValType> SUPPORTED_VALTYPES =
            EnumSet.of(EValType.INT, EValType.STR, EValType.CHAR,
            EValType.ARR_INT, EValType.ARR_STR, EValType.ARR_CHAR);

    @Override
    public EEditMaskType getType() {
        return EEditMaskType.ENUM;
    }

    @Override
    public EnumSet<EValType> getSupportedValueTypes() {
        return SUPPORTED_VALTYPES;
    }

    public static enum DisplayMode {

        SHOW_NAME,
        SHOW_TITLE
    }
    
    private EditMaskConstSet(){
        dummyInstance = true;
        constSetId = RadEnumPresentationDef.EMPTY_ENUM_DEF.getId();
        order = EEditMaskEnumOrder.BY_ORDER;
        correction = EEditMaskEnumCorrection.NONE;
        correctionItems = new ArrayList<>();
        items = RadEnumPresentationDef.EMPTY_ENUM_DEF.getItems();
    }

    public EditMaskConstSet(final Id constSetId, final EEditMaskEnumOrder order, final EEditMaskEnumCorrection correction, final Id[] correctionItems) {
        dummyInstance = false;
        this.constSetId = constSetId;
        this.order = order != null ? order : EEditMaskEnumOrder.BY_ORDER;
        this.correction = correction != null ? correction : EEditMaskEnumCorrection.NONE;
        if (correctionItems==null){
            this.correctionItems = new ArrayList<>();
        }
        else{
            this.correctionItems = new ArrayList<>(Arrays.asList(correctionItems));//modifable array
        }
    }

    public EditMaskConstSet(final Id constSetId) {
        this(constSetId, null, null, null);
    }

    public EditMaskConstSet(final EditMaskConstSet source) {
        this.dummyInstance = source.dummyInstance;
        this.constSetId = source.constSetId;
        this.order = source.order;
        this.correction = source.correction;
        this.correctionItems = source.correctionItems == null ? new ArrayList<Id>() : new ArrayList<>(source.correctionItems);
        this.items = source.items != null ? source.items.copy() : null;
        this.excludedItems = source.excludedItems != null ? source.excludedItems.copy() : null;
        this.includedItems = source.includedItems != null ? source.includedItems.copy() : null;
        this.mode = source.mode;
    }

    protected EditMaskConstSet(final Id enumId, final org.radixware.schemas.editmask.EditMaskEnum editMask) {
        super();
        dummyInstance = false;
        constSetId = enumId;
        order = editMask.isSetOrderBy() ? editMask.getOrderBy() : EEditMaskEnumOrder.BY_ORDER;
        correction = editMask.isSetCorrection() ? editMask.getCorrection() : EEditMaskEnumCorrection.NONE;
        correctionItems = editMask.getCorrectionItems() == null ? new ArrayList<Id>() : new ArrayList<>(editMask.getCorrectionItems());
    }

    @Override
    public void writeToXml(final org.radixware.schemas.editmask.EditMask editMask) {
        final org.radixware.schemas.editmask.EditMaskEnum editMaskEnum = editMask.addNewEnum();
        editMaskEnum.setOrderBy(order);
        if (includedItems!=null){
            final RadEnumPresentationDef.Items finalItems = includedItems.copy();
            if (excludedItems!=null && !excludedItems.isEmpty()){
                finalItems.removeItems(excludedItems);
            }
            final List<Id> itemIds = new ArrayList<>(finalItems.size());
            for(RadEnumPresentationDef.Item item: finalItems){
                itemIds.add(item.getId());
            }
            editMaskEnum.setCorrectionItems(itemIds);
            editMaskEnum.setCorrection(EEditMaskEnumCorrection.INCLUDE);

        }
        else if (excludedItems!=null){
            final List<Id> itemIds = new ArrayList<>(excludedItems.size());
            for(RadEnumPresentationDef.Item item: excludedItems){
                itemIds.add(item.getId());
            }
            editMaskEnum.setCorrectionItems(itemIds);
            editMaskEnum.setCorrection(EEditMaskEnumCorrection.EXCLUDE);
        }
        else if (correction!=EEditMaskEnumCorrection.NONE){
            editMaskEnum.setCorrectionItems(correctionItems);
            editMaskEnum.setCorrection(correction);            
        }
    }

    /**
     * Get enum definition for this edit mask.
     * @return Enum definition
     */
    @Override
    public RadEnumPresentationDef getRadEnumPresentationDef(IClientApplication application) {
        return dummyInstance ? RadEnumPresentationDef.EMPTY_ENUM_DEF : application.getDefManager().getEnumPresentationDef(constSetId);
    }

    /**
     * Get current items order.
     * List of items returned by {@link #getItems()} method  will be sorted according to this order.
     * @return items order.
     */
    public EEditMaskEnumOrder getOrder() {
        return order;
    }

    /**
     * Set items order.
     * List of items returned by {@link #getItems()} method  will be sorted according to this order.
     * @param newOrder new items order
     */
    public void setOrder(EEditMaskEnumOrder newOrder) {
        if (newOrder != order) {//NOPMD
            if (dummyInstance){
                throw new UnsupportedOperationException("This operation is not supported for empty edit mask");
            }            
            items = null;
            order = newOrder;
            afterModify();
        }
    }

    private void fillItems(IClientApplication context) {
        final RadEnumPresentationDef enumDef = getRadEnumPresentationDef(context);
        if (!correctionItems.isEmpty() && correction == EEditMaskEnumCorrection.INCLUDE) {
            items = enumDef.getEmptyItems();
            items.addItemsById(correctionItems);
        } else if (!correctionItems.isEmpty() && correction == EEditMaskEnumCorrection.EXCLUDE) {
            items = enumDef.getItems();
            items.removeItemsById(correctionItems);
        } else if (excludedItems != null && !excludedItems.isEmpty()) {
            items = includedItems != null ? includedItems.copy() : enumDef.getItems();
            items.removeItems(excludedItems);
        } else {
            items = includedItems != null ? includedItems.copy() : enumDef.getItems();
        }
        items.removeDeprecatedItems();
        final RadEnumPresentationDef.ItemsOrder itemsOrder = RadEnumPresentationDef.ItemsOrder.fromEditMaskEnumOrder(order);
        items.sort(itemsOrder);
    }

    public void setExcludedItems(final RadEnumPresentationDef.Items itemsToExclude) {
        if (dummyInstance){
            throw new UnsupportedOperationException("This operation is not supported for empty edit mask");
        }        
        correctionItems.clear();
        correction = EEditMaskEnumCorrection.NONE;
        excludedItems = itemsToExclude.copy();
        items = null;
        afterModify();
    }

    public RadEnumPresentationDef.Items getExcludedItems(IClientApplication application) {
        final RadEnumPresentationDef.Items finalExcludedItems =
            excludedItems != null ? excludedItems.copy() : getRadEnumPresentationDef(application).getEmptyItems();
        if (!correctionItems.isEmpty() && correction == EEditMaskEnumCorrection.EXCLUDE){
            finalExcludedItems.addItemsById(correctionItems);
        }
        return finalExcludedItems;
    }

    public void clearExcludedItems() {
        if (dummyInstance){
            throw new UnsupportedOperationException("This operation is not supported for empty edit mask");
        }        
        if (excludedItems != null) {
            items = null;
            excludedItems=null;
            afterModify();
        }
    }

    /**
     * Get current ordered list of enum items (except of excluded items).
     * @return items to show
     * @see #setOrder(org.radixware.kernel.common.enums.EEditMaskEnumOrder)
     */
    public RadEnumPresentationDef.Items getItems(IClientApplication application) {
        if (items == null) {
            fillItems(application);
        }
        return items.copy();
    }

    public void clearItems(IClientApplication application) {
        if (dummyInstance){
            throw new UnsupportedOperationException("This operation is not supported for empty edit mask");
        }        
        includedItems = null;
        items = getRadEnumPresentationDef(application).getEmptyItems();
        afterModify();
    }

    /**
     *
     * @param o
     */
    public void setItems(RadEnumPresentationDef.Items itemsToShow) {
        if (dummyInstance){
            throw new UnsupportedOperationException("This operation is not supported for empty edit mask");
        }        
        correctionItems.clear();
        correction = EEditMaskEnumCorrection.NONE;
        includedItems = itemsToShow.copy();
        items = null;
        afterModify();
    }

    public void showAllItems() {
        if (dummyInstance){
            throw new UnsupportedOperationException("This operation is not supported for empty edit mask");
        }        
        correctionItems.clear();
        correction = EEditMaskEnumCorrection.NONE;
        includedItems = null;
        items = null;
        afterModify();
    }

    public DisplayMode getDisplayMode() {
        return mode;
    }

    public void setDisplayMode(DisplayMode newMode) {
        if (dummyInstance){
            throw new UnsupportedOperationException("This operation is not supported for empty edit mask");
        }
        mode = newMode;
    }

    @Override
    public String toStr(IClientEnvironment environment, Object o) {
        //if (wasInherited()) return INHERITED_VALUE;
        if (o == null) {
            return getNoValueStr(environment.getMessageProvider());
        }
        final RadEnumPresentationDef.Item item = getItemFromObject(environment.getApplication(), o);
        if (item != null) {
            boolean hasTitle = item.getTitle() != null && !item.getTitle().isEmpty();
            return mode == DisplayMode.SHOW_NAME || !hasTitle ? item.getName() : item.getTitle();
        } else if (o instanceof Arr) {
            return arrToStr(environment, (Arr) o);
        } else {
            return o.toString();
        }
    }

    @Override
    public ValidationResult validate(final IClientEnvironment environment, final Object value) {
        if (value == null) return ValidationResult.ACCEPTABLE;
		if (value instanceof Arr) return validateArray(environment, (Arr)value);
		final RadEnumPresentationDef.Item item = getItemFromObject(environment.getApplication(),value);
        if (item==null){
            return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForInvalidValueType(environment));
        }        
        if (item.isDeprecated()){
            return ValidationResult.Factory.newInvalidResult(InvalidValueReason.Factory.createForDeprecatedValue(environment));
        }
        return getItems(environment.getApplication()).contains(item) ? ValidationResult.ACCEPTABLE : ValidationResult.Factory.newInvalidResult("");
    }

    private RadEnumPresentationDef.Item getItemFromObject(IClientApplication application, final Object object) {
        final RadEnumPresentationDef.Item item;
        if (object instanceof RadEnumPresentationDef.Item) {
            item = ((RadEnumPresentationDef.Item) object);
        } else if (object instanceof IKernelEnum) {
            item = getRadEnumPresentationDef(application).getItems().findItemForConstant((IKernelEnum) object);
        } else if (object instanceof String) {
            item = getRadEnumPresentationDef(application).getItems().findItemByValue(ValAsStr.Factory.newInstance(object, EValType.STR));
        } else if (object instanceof Character) {
            item = getRadEnumPresentationDef(application).getItems().findItemByValue(ValAsStr.Factory.newInstance(object, EValType.CHAR));
        } else if (object instanceof Long) {
            item = getRadEnumPresentationDef(application).getItems().findItemByValue(ValAsStr.Factory.newInstance(object, EValType.INT));
        } else {
            item = null;
        }
        return item;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.constSetId);
        hash = 53 * hash + (this.order != null ? this.order.hashCode() : 0);
        hash = 53 * hash + (this.correction != null ? this.correction.hashCode() : 0);
        hash = 53 * hash + Objects.hashCode(this.correctionItems);
        hash = 53 * hash + Objects.hashCode(this.includedItems);
        hash = 53 * hash + Objects.hashCode(this.excludedItems);
        hash = 53 * hash + (this.mode != null ? this.mode.hashCode() : 0);
        hash = 53 * hash + super.hashCode();
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
        final EditMaskConstSet other = (EditMaskConstSet) obj;
        if (!Objects.equals(this.constSetId, other.constSetId)) {
            return false;
        }
        if (this.order != other.order) {
            return false;
        }
        if (this.correction != other.correction) {
            return false;
        }
        if (!Objects.equals(this.correctionItems, other.correctionItems)) {
            return false;
        }
        if (!Objects.equals(this.includedItems, other.includedItems)) {
            return false;
        }
        if (!Objects.equals(this.excludedItems, other.excludedItems)) {
            return false;
        }
        if (this.mode != other.mode) {
            return false;
        }
        return super.equals(obj);
    }        
    
    public static EditMaskConstSet getEmptyMask(){
        return new EditMaskConstSet();
    }
}
