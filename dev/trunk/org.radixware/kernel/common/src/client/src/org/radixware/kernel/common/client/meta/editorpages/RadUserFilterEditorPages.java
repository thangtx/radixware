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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterParameters;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;


public final class RadUserFilterEditorPages extends RadEditorPages {

    private final static String PREDEFINED_PARAMS_PAGE_NAME = "__PREDEFINED_PARAMS_PAGE__";
    private final static Id PREDEFINED_PARAMS_PAGE_ID =
            Id.Factory.loadFrom(EDefinitionIdPrefix.EDITOR_PAGE + PREDEFINED_PARAMS_PAGE_NAME);
    private final RadStandardEditorPageDef predefinedParamsPage;
    private final RadFilterDef baseFilter;
    private final RadFilterParameters parameters;

    public RadUserFilterEditorPages(final RadFilterDef baseFilter, final RadFilterParameters parameters) {
        super(null, baseFilter);
        predefinedParamsPage =
                new RadUserFilterParamsPageDef(PREDEFINED_PARAMS_PAGE_ID, PREDEFINED_PARAMS_PAGE_NAME, parameters, true);
        this.baseFilter = baseFilter;
        this.parameters = parameters;
    }
    
    private boolean needForPredefinedParamsPage() {
        return baseFilter.getEditorPages().getTopLevelPages().isEmpty() && //унаследованных страниц нет,
               !baseFilter.getParameters().isEmpty() &&//есть предопределенные и
                parameters.customParametersCount() > 0;//пользовательские параметры
    }

    @Override
    public Collection<Id> getAllPagesIds() {
        final Collection<Id> result = new ArrayList<Id>();
        result.addAll(baseFilter.getEditorPages().getAllPagesIds());
        if (needForPredefinedParamsPage()) {
            result.add(PREDEFINED_PARAMS_PAGE_ID);
        }
        return Collections.unmodifiableCollection(result);
    }

    @Override
    public RadEditorPageDef getPageById(final Id pageId) {
        if (PREDEFINED_PARAMS_PAGE_ID.equals(pageId) && needForPredefinedParamsPage()) {
            return predefinedParamsPage;
        } else {
            return baseFilter.getEditorPages().getPageById(pageId);
        }
    }

    @Override
    public List<RadEditorPageDef> getTopLevelPages() {
        final List<RadEditorPageDef> pages = new ArrayList<RadEditorPageDef>();
        pages.addAll(baseFilter.getEditorPages().getTopLevelPages());
        if (needForPredefinedParamsPage()) {
            pages.add(predefinedParamsPage);
        }
        return Collections.unmodifiableList(pages);
    }

    @Override
    public boolean isPropertyDefined(final Id propertyId) {
        return baseFilter.getEditorPages().isPropertyDefined(propertyId)
                || (needForPredefinedParamsPage() && predefinedParamsPage.isPropertyDefined(propertyId));
    }
}
