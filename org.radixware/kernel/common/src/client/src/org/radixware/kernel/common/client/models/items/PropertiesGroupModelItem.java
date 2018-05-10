package org.radixware.kernel.common.client.models.items;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;

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


public class PropertiesGroupModelItem extends ModelItemInEditorPage{
    
    private final RadStandardEditorPageDef.PropertiesGroup def;
    private boolean enabled = true;
    private boolean visible = true;
    private boolean isFrameVisible;
    private String title;
    private List<Property> properties = null;
    private List<PropertiesGroupModelItem> childGroups = null;
    private final List<Property> userProperties = new LinkedList<>();

    public PropertiesGroupModelItem(final Model owner, final RadStandardEditorPageDef.PropertiesGroup group){
        super(owner,group.getId());
        def = group;
        isFrameVisible = group.isFrameVisible();
        title = def.hasTitle() ?  def.getTitle() : null;
    }
    
    @Override
    public ModelWithPages getOwner() {
        return (ModelWithPages)super.getOwner();
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        afterModify();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
        afterModify();
    }
    
    public void setVisible(final boolean visible) {
        if (this.visible == visible) {
            return;
        }
        final boolean currentVisible = isVisible();
        this.visible = visible;
        if (currentVisible != visible) {
            afterModify();
            final List<EditorPageModelItem> dependentPages = findDependentEditorPages();
            for (EditorPageModelItem page: dependentPages){
                page.afterModify();
            }
        }
    }    
    
    public boolean isVisible() {
        return visible && hasVisibleContent();
    }    
    
    private boolean hasVisibleContent(){
        for (Property property: getProperties()){
            if (property.isVisible()){
                return true;
            }
        }
        for (PropertiesGroupModelItem childGroup: getChildGroups()){
            if (childGroup.isVisible()){
                return true;
            }
        }
        return false;
    }
    
    public boolean isFrameVisible(){
        return isFrameVisible;
    }
    
    public void setFrameVisible(final boolean isVisible){
        isFrameVisible = isVisible;
        afterModify();
    }
    
    public List<PropertiesGroupModelItem> getChildGroups() {
        if (childGroups == null) {
            childGroups = new ArrayList<>();
            for (RadStandardEditorPageDef.PageItem item: getDefinition().getPageItems()){
                if (item.getItemId().getPrefix() == EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP &&
                    getOwner().isPropertiesGroupExists(item.getItemId())
                    ){
                    final PropertiesGroupModelItem childGroup = getOwner().getPropertiesGroup(item.getItemId());
                    childGroups.add(childGroup);
                }
            }
        }
        return Collections.unmodifiableList(childGroups);
    }

    public RadStandardEditorPageDef.PropertiesGroup getDefinition() {
        return def;
    }
    
    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<>();
            final Collection<RadStandardEditorPageDef.PageItem> pageItems = def.getPageItems();
            Property property;
            for (RadStandardEditorPageDef.PageItem item : pageItems) {
                if (item.getItemId().getPrefix() != EDefinitionIdPrefix.EDITOR_PAGE_PROP_GROUP && 
                    owner.getDefinition().isPropertyDefExistsById(item.getItemId())
                   ) {
                    property = owner.getProperty(item.getItemId());
                    properties.add(property);
                }
            }
        }
        final List<Property> result = new ArrayList<>();
        result.addAll(properties);
        result.addAll(userProperties);
        return Collections.unmodifiableList(result);
    }
    
    public final boolean isAllPropertiesReadOnly(){
        for (Property property: getProperties()){
            if (!property.isReadonly()){
                return false;
            }
        }
        for (PropertiesGroupModelItem childGroup: getChildGroups()){
            if (!childGroup.isAllPropertiesReadOnly()){
                return false;
            }
        }
        return true;
    }    
    
}
