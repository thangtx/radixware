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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.types.Id;

public class RadSortingDef extends TitledDefinition implements IGroupSetting{
    
    
    public final static class SortingItem {
        //Public fields
        
        public enum SortOrder {
            ASC,
            DESC
        }
        
        public final Id propId;
        public final boolean sortDesc;

        //Constructor
        public SortingItem(
                final Id propId_,
                final boolean sortDesc_) {
            if (propId_==null)
                throw new NullPointerException("propId cannot be null");
            propId = propId_;
            sortDesc = sortDesc_;
        }

        public SortingItem(final Id propId, final SortOrder sortOrder) {
            this(propId, sortOrder == SortOrder.DESC);
        }
        
        /**
         * @return inverted sorting
         */
        public SortingItem getInvertedSorting() {
            return new SortingItem(propId, !this.sortDesc);
        }
                
        @Override
        public boolean equals(Object o) {
            if (o==this)
                return true;
            if (o instanceof SortingItem){
                SortingItem item = (SortingItem)o;
                return item.propId.equals(propId) && item.sortDesc==sortDesc;
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 37 * hash + (this.propId != null ? this.propId.hashCode() : 0);
            hash = 37 * hash + (this.sortDesc ? 1 : 0);
            return hash;
        }               
    }
    //Public fields
    protected final List<SortingItem> orderBy = new LinkedList<SortingItem>();
    private final Id classId;

    //Constructor
    public RadSortingDef(
            final Id id,
            final String name,
            final Id classId,
            final Id titleId,
            final SortingItem[] columns) {
        super(id, name, classId, titleId);
        if (columns != null) {
            Collections.addAll(orderBy, columns);
        }
        this.classId = classId;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "sorting %s");
        return String.format(desc, super.getDescription());
    }
    
    public List<SortingItem> getSortingColumns(){
        return Collections.unmodifiableList(orderBy);
    }
    
    
    final public Id getOwnerClassId(){
        return classId;
    }
    
    @Override
    public boolean isUserDefined() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean hasAncestor() {
        return false;
    }

    @Override
    public boolean wasModified() {
        return false;
    }

    @Override
    public void setName(final String name) {//empty implementation
    }

    @Override
    public Icon getIcon() {
        return getApplication().getImageManager().getIcon(ClientIcon.Definitions.SORTING);
    }

    @Override
    public DialogResult openEditor(final IClientEnvironment environment, final IWidget parent, final Collection<String> restrictedNames, final boolean showApplyButton) {
        return getApplication().getStandardViewsFactory().newSortingEditorDialog(environment, this, restrictedNames, showApplyButton, parent).execDialog();
    }
    
}
