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

package org.radixware.kernel.common.client.views;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterFactory;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.models.items.properties.PropertyBin;
import org.radixware.kernel.common.client.models.items.properties.PropertyBlob;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.models.items.properties.PropertyChar;
import org.radixware.kernel.common.client.models.items.properties.PropertyClob;
import org.radixware.kernel.common.client.models.items.properties.PropertyDateTime;
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
import org.radixware.kernel.common.client.models.items.properties.PropertyNum;
import org.radixware.kernel.common.client.models.items.properties.PropertyObject;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyStr;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.types.Arr;


public interface StandardViewFactory {

    public IView newStandardSelector(final IClientEnvironment environment);

    public IView newStandardReportParametersDialog(final IClientEnvironment environment);

    public IView newStandardParagraphEditor(final IClientEnvironment environment);

    public IView newStandardForm(final IClientEnvironment environment);

    public IView newStandardFilterParameters(final IClientEnvironment environment);

    public IView newStandardEditor(final IClientEnvironment environment);

    public IParameterCreationWizard newParameterCreationWizard(final IClientEnvironment environment, final ISqmlTableDef table, final ISqmlParameterFactory parameterFactory, final List<String> parameterNames, IWidget parent);

    public IParameterEditorDialog newParameterEditorDialog(final IClientEnvironment environment, final ISqmlParameter parameter, final ISqmlTableDef table, final List<String> parameterNames, final boolean readonly, final IWidget parent);

    public IPropEditor newPropIntEditor(PropertyInt prop);

    public IPropEditor newPropObjectEditor(PropertyObject prop);

    public IPropEditor newPropListEditor(Property prop);

    public IPropEditor newPropXmlEditor(PropertyXml prop);

    public IPropEditor newPropStrEditor(PropertyStr prop);

    public IPropEditor newPropBinEditor(PropertyBin prop);

    public IPropEditor newPropBlobEditor(PropertyBlob prop);

    public IPropEditor newPropBoolEditor(PropertyBool prop);

    public IPropEditor newPropCharEditor(PropertyChar prop);

    public IPropEditor newPropClobEditor(PropertyClob prop);

    public IPropEditor newPropDateTimeEditor(PropertyDateTime prop);

    public IPropEditor newPropNumEditor(PropertyNum prop);

    public IPropEditor newPropObjectlEditor(PropertyObject prop);

    public IPropEditor newPropRefEditor(PropertyRef prop);
    
    public IPropEditor newPropTextStrEditor(PropertyStr prop);
    public IPropEditor newPropTextClobEditor(PropertyClob prop);

    public <T extends Arr> IPropEditor newPropArrEditor(PropertyArr<T> prop);

    public IPropLabel newPropLabel(Property prop);

    public IEntityEditorDialog newEntityEditorDialog(EntityModel entity);

    public ISelectEntityDialog newSelectEntityDialog(GroupModel parentGroupMode, boolean canClear);

    public IFilterEditorDialog newFilterEditorDialog(final FilterModel filter, final Collection<String> restrictedNames, final boolean showApplyButton, final IWidget parent);
    
    public ISortingEditorDialog newSortingEditorDialog(final IClientEnvironment environment, final RadSortingDef sorting, final Collection<String> restrictedNames, final boolean showApplyButton, final IWidget parent);

    public IEditorPageWidget newEditorPageWidget(EditorPageModelItem item);

    public IEditorPageView newStandardEditorPage(IClientEnvironment environment, IView parentView, RadEditorPageDef editorPage);

    public IArrayEditorDialog newArrayEditorDialog(PropertyArr prop, IWidget parent);        
}