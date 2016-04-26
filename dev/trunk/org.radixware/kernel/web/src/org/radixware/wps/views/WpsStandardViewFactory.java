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

package org.radixware.wps.views;

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
import org.radixware.kernel.common.client.models.items.properties.*;
import org.radixware.kernel.common.client.views.IArrayEditorDialog;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.client.views.IEditorPageWidget;
import org.radixware.kernel.common.client.views.IEntityEditorDialog;
import org.radixware.kernel.common.client.views.IFilterEditorDialog;
import org.radixware.kernel.common.client.views.IParameterCreationWizard;
import org.radixware.kernel.common.client.views.IParameterEditorDialog;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.kernel.common.client.views.ISelectEntityDialog;
import org.radixware.kernel.common.client.views.ISortingEditorDialog;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.views.StandardViewFactory;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Arr;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.ArrayEditorDialog;
import org.radixware.wps.views.dialog.EntityEditorDialog;
import org.radixware.wps.views.dialog.RwtStandardForm;
import org.radixware.wps.views.dialog.RwtStandardReportParamEditor;
import org.radixware.wps.views.dialog.SelectEntityDialog;
import org.radixware.wps.views.editor.EditorPage;
import org.radixware.wps.views.editor.StandardEditor;
import org.radixware.wps.views.editor.StandardEditorPage;
import org.radixware.wps.views.editor.property.*;
import org.radixware.wps.views.paragraph.ParagraphView;
import org.radixware.wps.views.selector.StandardFilterParameters;
import org.radixware.wps.views.selector.WpsStandardSelector;


public class WpsStandardViewFactory implements StandardViewFactory {

    @Override
    public IView newStandardSelector(IClientEnvironment userSession) {
        return new WpsStandardSelector((WpsEnvironment) userSession);
    }

    @Override
    public IView newStandardReportParametersDialog(IClientEnvironment userSession) {
        return new RwtStandardReportParamEditor(userSession);
    }

    @Override
    public IView newStandardParagraphEditor(IClientEnvironment userSession) {
        return new ParagraphView((WpsEnvironment) userSession);
    }

    @Override
    public IView newStandardForm(IClientEnvironment userSession) {
        return new RwtStandardForm(userSession);
    }

    @Override
    public IView newStandardFilterParameters(IClientEnvironment userSession) {
        return new StandardFilterParameters(userSession);
    }

    @Override
    public IView newStandardEditor(IClientEnvironment userSession) {
        return new StandardEditor((WpsEnvironment) userSession);
    }

    @Override
    public IParameterCreationWizard newParameterCreationWizard(IClientEnvironment userSession, ISqmlTableDef table, ISqmlParameterFactory parameterFactory, List<String> parameterNames, IWidget parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IParameterEditorDialog newParameterEditorDialog(IClientEnvironment userSession, ISqmlParameter parameter, ISqmlTableDef table, List<String> parameterNames, boolean readonly, IWidget parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IPropEditor newPropIntEditor(PropertyInt prop) {
        return new PropIntEditor(prop);
    }

    @Override
    public IPropEditor newPropObjectEditor(PropertyObject prop) {
        return new PropObjectEditor(prop);
    }

    @Override
    public IPropEditor newPropListEditor(Property prop) {
        return new PropListEditor(prop);
    }

    @Override
    public IPropEditor newPropXmlEditor(PropertyXml prop) {
        return new PropXmlEditor(prop);
    }

    @Override
    public IPropEditor newPropStrEditor(PropertyStr prop) {
        return new PropStrEditor(prop);
    }

    @Override
    public IPropEditor newPropBinEditor(PropertyBin prop) {
        return new PropBinEditor(prop);
    }

    @Override
    public IPropEditor newPropBlobEditor(PropertyBlob prop) {
        return new PropBinEditor(prop);
    }

    @Override
    public IPropEditor newPropBoolEditor(PropertyBool prop) {
        return new PropBoolEditor(prop);
    }

    @Override
    public IPropEditor newPropCharEditor(PropertyChar prop) {
        return new PropCharEditor(prop);
    }

    @Override
    public IPropEditor newPropClobEditor(PropertyClob prop) {
        return new PropStrEditor(prop);
    }

    @Override
    public IPropEditor newPropDateTimeEditor(PropertyDateTime prop) {
        return new PropDateTimeEditor(prop);
    }

    @Override
    public IPropEditor newPropNumEditor(PropertyNum prop) {
        return new PropNumEditor(prop);
    }

    @Override
    public IPropEditor newPropObjectlEditor(PropertyObject prop) {
        return new PropObjectEditor(prop);
    }

    @Override
    public IPropEditor newPropRefEditor(PropertyRef prop) {
        return new PropRefEditor(prop);
    }

    @Override
    public IPropEditor newPropTextStrEditor(PropertyStr prop) {
        return new PropTextEditor(prop);
    }

    @Override
    public IPropEditor newPropTextClobEditor(PropertyClob prop) {
        return new PropTextEditor(prop);
    }

    @Override
    public <T extends Arr> IPropEditor newPropArrEditor(PropertyArr<T> prop) {
        return new PropArrEditor<T>(prop);
    }

    @Override
    public IPropLabel newPropLabel(Property prop) {
        return new PropLabel(prop);
    }

    @Override
    public IEntityEditorDialog newEntityEditorDialog(EntityModel entity) {
        final ERuntimeEnvironmentType environmentType = entity.getEditorPresentationDef().getRuntimeEnvironmentType();
        if (environmentType != ERuntimeEnvironmentType.COMMON_CLIENT && environmentType != ERuntimeEnvironmentType.WEB) {
            final String message =
                    "Can't use editor for " + environmentType.getName() + " environment  in " + ERuntimeEnvironmentType.WEB.getName() + " environment";
            throw new IllegalUsageError(message);
        }
        return new EntityEditorDialog(entity);
    }

    @Override
    public ISelectEntityDialog newSelectEntityDialog(GroupModel parentGroupModel, boolean canClear) {
        final ERuntimeEnvironmentType environmentType = parentGroupModel.getSelectorPresentationDef().getRuntimeEnvironmentType();
        if (environmentType != ERuntimeEnvironmentType.COMMON_CLIENT && environmentType != ERuntimeEnvironmentType.EXPLORER) {
            final String message =
                    "Can't use selector for " + environmentType.getName() + " environment  in " + ERuntimeEnvironmentType.EXPLORER.getName() + " environment";
            throw new IllegalUsageError(message);
        }
        return new SelectEntityDialog(((WpsEnvironment) parentGroupModel.getEnvironment()).getDialogDisplayer(), parentGroupModel, canClear);
    }

    @Override
    public IFilterEditorDialog newFilterEditorDialog(FilterModel filter, Collection<String> restrictedNames, boolean showApplyButton, IWidget parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ISortingEditorDialog newSortingEditorDialog(IClientEnvironment environment, RadSortingDef sorting, Collection<String> restrictedNames, boolean showApplyButton, IWidget parent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IEditorPageWidget newEditorPageWidget(EditorPageModelItem item) {
        return new EditorPage(item);
    }

    @Override
    public IEditorPageView newStandardEditorPage(IClientEnvironment userSession, IView parentView, RadEditorPageDef editorPage) {
        return new StandardEditorPage(userSession, parentView, editorPage);
    }

    @Override
    public IArrayEditorDialog newArrayEditorDialog(PropertyArr prop, IWidget parent) {
        final WpsEnvironment wpsEnv = (WpsEnvironment) prop.getEnvironment();
        return new ArrayEditorDialog(prop, wpsEnv.getDialogDisplayer());
    }
}
