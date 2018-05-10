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

package org.radixware.kernel.common.client.widgets;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.types.FilterRules;
import org.radixware.kernel.common.client.types.Icon;


public class ListWidgetItem {
    
    public static enum EAttribute{VALUE,
                                                USER_DATA,
                                                TEXT,
                                                ICON,
                                                TOOL_TIP,
                                                EXT_INFO,
                                                TEXT_OPTIONS,
                                                FILTER_RULES,
                                                IS_SELECTABLE,
                                                VALIDATION_RESULT,
                                                NAME};
    
    public interface IChangeAttrributeListener{
        void attributeChanged(ListWidgetItem item, EAttribute attribute);
    }
    
    private Object value;
    private Object userData;
    private String name;
    private String text;
    private Icon icon;
    private String toolTip;
    private String extTitle;
    private TextOptions textOptions;
    private FilterRules filterRules;
    private boolean isSelectable = true;
    private List<IChangeAttrributeListener> listeners;
    private ValidationResult validationResult;
    
    public ListWidgetItem(){        
    }
    
    public ListWidgetItem(final String text){
        this.text = text;
    }
    
    public ListWidgetItem(final String text, final Object value){
        this.text = text;
        this.value = value;
    }
    
    public ListWidgetItem(final String text, final Object value, final Icon icon){
        this.text = text;
        this.value = value;
        this.icon = icon;
    }
    
    public ListWidgetItem(final IClientEnvironment env, final EditMaskList.Item maskItem){
        text = maskItem.getTitle(env);
        value = maskItem.getValue();
        icon = maskItem.getIcon(env);
        toolTip = maskItem.getToolTip(env);
        extTitle = maskItem.getExtendedTitle(env);
    }
    
    public ListWidgetItem(final ListWidgetItem copy){
        text = copy.text;
        value = copy.getValue();
        icon = copy.getIcon();
        toolTip = copy.getToolTip();
        extTitle = copy.getExtendedTitle();
        isSelectable = copy.isSelectable;
    }

    public Object getValue() {
        return value;
    }

    public final void setValue(final Object value) {
        if (!Objects.equals(value, this.value)){
            this.value = value;
            notifyListeners(EAttribute.VALUE);
        }
    }

    public Object getUserData() {
        return userData;
    }

    public final void setUserData(final Object userData) {
        if (!Objects.equals(userData, this.userData)){
            this.userData = userData;
            notifyListeners(EAttribute.USER_DATA);
        }
    }
    
    public String getName(){
        return name;
    }
    
    public final void setName(final String name){
        if (!Objects.equals(name, this.name)){
            this.name = name;
            notifyListeners(EAttribute.NAME);
        }
    }

    public String getText() {
        return text;
    }

    public final void setText(final String text) {
        if (!Objects.equals(text, this.text)){
            this.text = text;
            notifyListeners(EAttribute.TEXT);
        }
    }

    public Icon getIcon() {
        return icon;
    }

    public final void setIcon(final Icon icon) {
        if (!Objects.equals(this.icon, icon)){
            this.icon = icon;
            notifyListeners(EAttribute.ICON);
        }
    }
    
    public String getToolTip(){
        return toolTip;
    }
    
    public final void setToolTip(final String toolTip){
        if (!Objects.equals(toolTip, this.toolTip)){
            this.toolTip = toolTip;
            notifyListeners(EAttribute.TOOL_TIP);
        }
    }

    public String getExtendedTitle() {
        return extTitle;
    }

    public final void setExtendedTitle(final String extTitle) {
        if (!Objects.equals(extTitle, this.extTitle)){
            this.extTitle = extTitle;
            notifyListeners(EAttribute.EXT_INFO);
        }
    }

    public TextOptions getTextOptions() {
        return textOptions;
    }

    public final void setTextOptions(final TextOptions textOptions) {
        if (!Objects.equals(textOptions, this.textOptions)){
            this.textOptions = textOptions;
            notifyListeners(EAttribute.TEXT_OPTIONS);
        }
    }

    public FilterRules getFilterRules() {
        if (filterRules==null){
            final FilterRules defaultFilterRules = new FilterRules();
            defaultFilterRules.addRule(getText());
            final String extTitle = getExtendedTitle();
            if (extTitle!=null){
                defaultFilterRules.addRule(extTitle);
            }
            return defaultFilterRules;
        }else{
            return filterRules;
        }
    }

    public final void setFilterRules(final FilterRules filterRules) {
        this.filterRules = filterRules;
        notifyListeners(EAttribute.FILTER_RULES);
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public final void setSelectable(final boolean isSelectable) {
        if (this.isSelectable!=isSelectable){
            this.isSelectable = isSelectable;
            notifyListeners(EAttribute.IS_SELECTABLE);
        }
    }            
    
    public final void addListener(final IChangeAttrributeListener listener){
        if (listener!=null){
            if (listeners==null){
                listeners = new LinkedList<>();                
            }
            if (!listeners.contains(listener)){
                listeners.add(listener);
            }
        }
    }
    
    public final void removeListener(final IChangeAttrributeListener listener){
        if (listener!=null && listeners!=null){
            listeners.remove(listener);
            if (listeners.isEmpty()){
                listeners = null;
            }
        }
    }
    
    protected final void notifyListeners(final EAttribute attribute){
        if (listeners!=null){
            final List<IChangeAttrributeListener> copy = new LinkedList<>(listeners);
            for (IChangeAttrributeListener listener: copy){
                listener.attributeChanged(this, attribute);
            }
        }
    }
    
    public boolean isMatchToFilter(final String filterText){
        if (filterText==null || filterText.isEmpty()){
            return true;
        }else{
            return getFilterRules().isMatchToSomeFilter(filterText);
        }
    }
    
    public static List<ListWidgetItem> createForEditMaskList(final IClientEnvironment env, final EditMaskList mask){
        final List<ListWidgetItem> items = new LinkedList<>();
        for (EditMaskList.Item maskItem: mask.getItems()){
            items.add(new ListWidgetItem(env, maskItem));
        }
        return items;
    }
    
    public static List<ListWidgetItem> createForEditMaskConstSet(final IClientEnvironment env, final EditMaskConstSet mask){
        final List<ListWidgetItem> items = new LinkedList<>();
        for (RadEnumPresentationDef.Item enumItem: mask.getItems(env.getApplication())){
            final String enumItemTitle = enumItem.getTitle();
            final String listItemTitle = enumItemTitle==null || enumItemTitle.isEmpty() ? enumItem.getName() : enumItemTitle;
            final ListWidgetItem listItem = new ListWidgetItem(listItemTitle, enumItem.getValue(), enumItem.getIcon());
            listItem.setUserData(enumItem);
            listItem.setName("rx_enum_item_"+enumItem.getId().toString());
            items.add(listItem);
        }
        return items;
    }
    
    public void setValidationResult(ValidationResult validationResult) {
        this.validationResult = validationResult;
        notifyListeners(EAttribute.VALIDATION_RESULT);
    }

    public ValidationResult getValidationResult() {
        if (validationResult == null) {
            return ValidationResult.ACCEPTABLE;
        }
        return validationResult;
    }
}
