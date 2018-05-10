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

package org.radixware.kernel.common.client.models.items;

import java.util.Objects;
import org.radixware.kernel.common.client.enums.ESelectorColumnHeaderMode;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.*;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.ESelectorColumnSizePolicy;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.Id;

public final class SelectorColumnModelItem extends ModelItem {

    private static final int MAX_STR_LENGTH_FOR_RESIZE_BY_CONTENT = 40;
    final private RadSelectorPresentationDef.SelectorColumn def;
    private String title;
    private String hint;
    private boolean visible;
    private boolean isForbidden;    
    private ESelectorColumnHeaderMode headerMode = ESelectorColumnHeaderMode.ICON_AND_TEXT;
    private ESelectorColumnSizePolicy sizePolicy;
    private ESelectorColumnAlign alignment;
    private Icon headerIcon;

    public SelectorColumnModelItem(final GroupModel owner, final Id id) {
        super(owner, id);
        def = owner.getSelectorPresentationDef().getSelectorColumns().findColumn(id);
        title = def.getTitle();
        hint = getPropertyDef().getHint();
        visible = def.getVisibility() == ESelectorColumnVisibility.INITIAL;
        alignment = def.getAlignment();
        sizePolicy = def.getSizePolicy();
    }

    public RadPropertyDef getPropertyDef() {
        return getOwner().getSelectorPresentationDef().getPropertyDefById(def.getPropertyId());
    }
    
    public RadSelectorPresentationDef.SelectorColumn getColumnDef(){
        return def;
    }

    @Override
    public GroupModel getOwner() {
        return (GroupModel) super.getOwner();
    }

    public ESelectorColumnAlign getAlignment() {
        //if (alignment==null)
        //используется выравнивание по-умолчанию - взять из настроек
        //	return Environment.getConfigStore()..
        if (alignment == null) {
            final ClientSettings settings = getEnvironment().getConfigStore();
            try {
                final EValType valType;
                if (getPropertyDef().getEditMask().getType()==EEditMaskType.ENUM || 
                    getPropertyDef().getEditMask().getType()==EEditMaskType.LIST
                    ){
                    valType = EValType.STR;
                }else{
                    valType = getPropertyDef().getType();
                }
                settings.beginGroup(SettingNames.SYSTEM);
                settings.beginGroup(SettingNames.SELECTOR_GROUP);
                settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
                alignment = ESelectorColumnAlign.getForValue((long) settings.readInteger(valType.getName(), ESelectorColumnAlign.LEFT.ordinal()));
            } catch(NoConstItemWithSuchValueError error){
                getEnvironment().getTracer().error(error);
            }finally {
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();
                settings.endGroup();                
            }
        }
        return alignment;
    }

    public void setAlignment(final ESelectorColumnAlign alignment) {
        if (this.alignment!=alignment){
            this.alignment = alignment;
            afterModify();
        }
    }

    public ESelectorColumnSizePolicy getSizePolicy() {
        return sizePolicy;
    }

    public void setSizePolicy(final ESelectorColumnSizePolicy policy) {
        if (sizePolicy!=policy){
            sizePolicy = policy;
            afterModify();
        }
    }

    public ESelectorColumnSizePolicy getAutoSizePolicy() {
        final EditMask editMask = getPropertyDef().getEditMask();
        final EEditMaskType editMaskType = editMask.getType();
        if (editMaskType == null) {
            return ESelectorColumnSizePolicy.MANUAL_RESIZE;
        }
        switch (editMaskType) {//NOPMD
            case BOOL:
            case DATE_TIME:
            case TIME_INTERVAL:
            case INT:
            case OBJECT_REFERENCE:
                return ESelectorColumnSizePolicy.RESIZE_BY_CONTENT;
            case STR:
                final EditMaskStr editMaskStr = (EditMaskStr) editMask;
                if (editMaskStr.getMaxLength() <= MAX_STR_LENGTH_FOR_RESIZE_BY_CONTENT) {
                    return ESelectorColumnSizePolicy.RESIZE_BY_CONTENT;
                } else if (!editMaskStr.getInputMask().isEmpty()
                        && editMaskStr.getInputMask().getLength() <= MAX_STR_LENGTH_FOR_RESIZE_BY_CONTENT) {
                    return ESelectorColumnSizePolicy.RESIZE_BY_CONTENT;
                } else {
                    return ESelectorColumnSizePolicy.MANUAL_RESIZE;
                }
            case LIST:
                final EditMaskList editMaskList = (EditMaskList) editMask;
                for (EditMaskList.Item item : editMaskList.getItems()) {
                    final String itemTitle = item.getTitle(getEnvironment());
                    if (itemTitle != null
                            && itemTitle.length() > MAX_STR_LENGTH_FOR_RESIZE_BY_CONTENT) {
                        return ESelectorColumnSizePolicy.MANUAL_RESIZE;
                    }
                }
                return ESelectorColumnSizePolicy.RESIZE_BY_CONTENT;
            case ENUM:
                final EditMaskConstSet editMaskConstSet = (EditMaskConstSet) editMask;
                final RadEnumPresentationDef.Items items =
                        editMaskConstSet.getItems(getEnvironment().getApplication());
                for (RadEnumPresentationDef.Item item : items) {
                    final String itemTitle = item.getTitle();
                    if (itemTitle != null
                            && itemTitle.length() > MAX_STR_LENGTH_FOR_RESIZE_BY_CONTENT) {
                        return ESelectorColumnSizePolicy.MANUAL_RESIZE;
                    }
                }
                return ESelectorColumnSizePolicy.RESIZE_BY_CONTENT;
            default:
                return ESelectorColumnSizePolicy.MANUAL_RESIZE;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        if (!Objects.equals(title, this.title)){
            this.title = title;
            afterModify();
        }
    }
    
    public String getHint(){
        return hint;
    }
    
    public void setHint(final String hint){
        if (!Objects.equals(hint, this.hint)){
            this.hint = hint;
            afterModify();
        }
    }

    public boolean isVisible() {
        return visible && !isForbidden() && def.getVisibility() != ESelectorColumnVisibility.NEVER;
    }

    public void setVisible(final boolean visible) {
        if (this.visible!=visible){
            this.visible = visible;
            afterModify();
        }
    }
    
    public boolean isForbidden(){
        return isForbidden;
    }
    
    public void setForbidden(final boolean forbidden){
        if (this.isForbidden!=forbidden){
            this.isForbidden = forbidden;
            afterModify();        
        }
    }

    public ESelectorColumnHeaderMode getHeaderMode() {
        return headerMode;
    }

    public void setHeaderMode(final ESelectorColumnHeaderMode headerMode) {
        if (this.headerMode!=headerMode){
            this.headerMode = headerMode;
            afterModify();
        }
    }

    public Icon getHeaderIcon() {
        return headerIcon;
    }

    public void setHeaderIcon(final Icon headerIcon) {
        if (!Objects.equals(this.headerIcon, headerIcon)){
            this.headerIcon = headerIcon;
            afterModify();
        }
    }
}
