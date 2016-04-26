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

package org.radixware.kernel.common.client.meta.editorpages;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.meta.filters.RadFilterParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;


final class RadUserFilterParamsPageDef extends RadStandardEditorPageDef {

    private static class FilterParametersGroup extends RadStandardEditorPageDef.PropertiesGroup {

        private final boolean predefined;
        private final RadFilterParameters parameters;

        public FilterParametersGroup(final Id groupId, final RadFilterParameters parameters, final boolean forPredefined) {
            super(groupId, null, null, null);
            predefined = forPredefined;
            this.parameters = parameters;
        }

        @Override
        public List<PageItem> getPageItems() {
            final List<PageItem> items = new ArrayList<PageItem>();
            int column = 0, row = 0;
            boolean isPredefined;
            for (ISqmlParameter parameter : parameters.getAll()) {
                isPredefined = !(parameter instanceof ISqmlModifiableParameter);
                if (isPredefined == predefined) {
                    items.add(new PageItem(parameter.getId(), column, row));
                    if (column == 2) {
                        column = 0;
                        row++;
                    } else {
                        column++;
                    }
                }
            }
            return items;
        }

        @Override
        public boolean isPropertyDefined(Id propertyId) {
            final ISqmlParameter parameter = parameters.getParameterById(propertyId);
            return parameter != null && !(parameter instanceof ISqmlModifiableParameter) == predefined;
        }
    }
    private final Id rootGroupId = Id.Factory.newInstance(EDefinitionIdPrefix.ADS_PROPERTY_GROUP);
    private final boolean predefined;
    private final PropertiesGroup group;

    public RadUserFilterParamsPageDef(final Id pageId, final String name, final RadFilterParameters parameters, final boolean forPredefined) {
        super(pageId, name, null, null, null, null);
        group = new FilterParametersGroup(rootGroupId, parameters, forPredefined);
        predefined = forPredefined;
    }

    @Override
    public PropertiesGroup getPropertiesGroup(Id groupId) {
        return rootGroupId.equals(groupId) ? group : super.getPropertiesGroup(groupId);
    }

    @Override
    public PropertiesGroup getRootPropertiesGroup() {
        return group;
    }

    @Override
    public boolean isEmpty() {
        return group.getPageItems().isEmpty();
    }

    @Override
    public boolean isPropertyDefined(Id propertyId) {
        return group.isPropertyDefined(propertyId);
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    RadStandardEditorPageDef createCopyWithSubPages(List<RadEditorPageDef> childPages) {
        throw new UnsupportedOperationException("This method is not supported in RadUserFilterParamsPageDef");
    }
    
    
}
