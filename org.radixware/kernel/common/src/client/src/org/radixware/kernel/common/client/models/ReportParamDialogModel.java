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

package org.radixware.kernel.common.client.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadReportPresentationDef;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.types.Icon;

import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IReportParamDialogView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;

import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EFormButtonType;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;

/**
 * Класс модели отчета. Предоставляет доступ к параметрам, страницам и командам отчета.
 * Содержит обработчики сигналов кастомного представления отчета.
 * <p>Потомки (Tafm<GUID>) генерируются дизайнером.
 * Для каждого отчета существует сгенерированный класс ее модели.
 */
public abstract class ReportParamDialogModel extends ModelWithPages {
    
    private DialogResult saveResult = DialogResult.REJECTED;
    private Model reportContext;

    protected ReportParamDialogModel(IClientEnvironment environment,final RadReportPresentationDef def) {
        super(environment,def);
    }

    public final RadReportPresentationDef getReportPresentationDef() {
        return (RadReportPresentationDef) getDefinition();
    }

    public final IReportParamDialogView getReportView() {
        return getView() != null ? (IReportParamDialogView) getView() : null;
    }

    @Override
    protected final Property activateProperty(final Id id) {
        final Property property = super.activateProperty(id);
        if (property != null) {
            final RadPropertyDef propertyDef = property.getDefinition();
            property.setServerValue(new PropertyValue(propertyDef, ValAsStr.fromStr(propertyDef.getInitialVal(), propertyDef.getType())));
        }
        return property;
    }

    @Override
    public void setView(IView view_) {
        if (view_ == null && getReportView() != null) {
            saveResult = getReportView().getDialogResult();
        }
        super.setView(view_);
    }

    public IContext.Report getReportParamDialogContext() {
        return (IContext.Report) getContext();
    }

    public void setReportContextModel(final Model reportContext) {
        this.reportContext=reportContext;
    }
    
    public Model getReportContextModel() {
        return reportContext;
    }

    @Override
    public void setTitle(String newTitle) {
        super.setTitle(newTitle);
        if (getReportView() != null) {
            getReportView().setWindowTitle(getWindowTitle());
        }
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        if (getReportView() != null) {
            getReportView().setWindowIcon(icon);
        }
    }

    //context - RepoerPub; parentEntityModel - command button owner
    public static ReportParamDialogModel create(final Id reportId, final Model reportPubModel,final Model  parentModel) {
        final RadReportPresentationDef def = parentModel.getEnvironment().getDefManager().getReportPresentationDef(reportId);
        return def.createModel(new IContext.Report(parentModel, reportPubModel));        
    }

    public final void readProperties(org.radixware.schemas.eas.PropertyList propertyList) {
        if (getReportView() != null) {
            getReportView().setUpdatesEnabled(false);
        }
        try {
            setPropertiesImpl(propertyList);
            afterRead();
        } finally {
            if (getReportView() != null) {
                getReportView().setUpdatesEnabled(true);
            }
        }
    }

    public final org.radixware.schemas.eas.PropertyList writeProperties() {
        final org.radixware.schemas.eas.PropertyList result = org.radixware.schemas.eas.PropertyList.Factory.newInstance();
        if (properties != null && !properties.isEmpty()) {
            //Copy properties collection to avoid concurrent modification in overrided getters
            final Collection<Property> props = new ArrayList<>();
            props.addAll(properties.values());
            for (Property property : props) {
                if (!property.isLocal() && property.getDefinition().getNature()!= EPropNature.PARENT_PROP){
                    property.writeValue2Xml(result.addNewItem());
                }
            }
        }
        return result;
    }

    private void setPropertiesImpl(org.radixware.schemas.eas.PropertyList propertyList) {
        if (propertyList != null && propertyList.getItemList() != null) {
            Property property;
            for (org.radixware.schemas.eas.PropertyList.Item item : propertyList.getItemList()) {
                property = getProperty(item.getId());
                try {
                    final Object value;
                    if (property.getDefinition().getType() == EValType.XML) {
                        final PropertyXml xmlProp = (PropertyXml) property;
                        value = 
                            xmlProp.castValue(ValueConverter.easPropXmlVal2ObjVal(item, EValType.XML, null));
                    } else {
                        final Id valTableId;
                        if (property.getDefinition() instanceof RadParentRefPropertyDef) {
                            valTableId = ((RadParentRefPropertyDef) property.getDefinition()).getReferencedTableId();
                        } else {
                            valTableId = null;
                        }
                        value = 
                            ValueConverter.easPropXmlVal2ObjVal(item, property.getDefinition().getType(), valTableId, getEnvironment().getDefManager());
                    }
                    property.setServerValue(new PropertyValue(property.getDefinition(), value));
                } catch (RuntimeException ex) {
                    showException(ex);
                }
            }
        }
    }

    public DialogResult execDialog() {
        setView(null);
        IReportParamDialogView view = (IReportParamDialogView) createView();
        view.open(this);
        if (getView() != null) {
            //при открытии формы не был вызван метод close
            view.execDialog();
        }
        if (getReportView() != null) {
            setView(null);
        }
        if (saveResult == DialogResult.REJECTED) {
            afterCancel();
        }
        return saveResult;
    }

    public final boolean acceptParameters() throws PropertyIsMandatoryException, InvalidPropertyValueException {
        if (!beforeExecReport()) {
            return false;
        }        
        checkPropertyValues();
        traceParameters();
        return true;
    }

    protected String getButtonText(final String buttonType) {//DBP-1671
        if (getButtonType(buttonType) == EFormButtonType.OK) {
            return getEnvironment().getMessageProvider().translate("ExplorerDialog", "&OK");
        } else if (getButtonType(buttonType) == EFormButtonType.CANCEL) {
            return getEnvironment().getMessageProvider().translate("ExplorerDialog", "&Cancel");
        } else {
            RadEnumPresentationDef.Item item = getConstSetItemByValue(buttonType);
            return item != null ? item.getTitle() : buttonType;
        }
    }

    protected Icon getButtonIcon(final String buttonType) {//DBP-1671
        if (getButtonType(buttonType) == EFormButtonType.OK) {
            return getApplication().getImageManager().loadIcon(ClientIcon.Dialog.BUTTON_OK.fileName);////getEnvironment().getEnvironment().getIconSupport().getIcon(IconKind.BUTTON_OK);
        } else if (getButtonType(buttonType) == EFormButtonType.CANCEL) {
            return getApplication().getImageManager().loadIcon(ClientIcon.Dialog.BUTTON_CANCEL.fileName);////getEnvironment().getEnvironment().getIconSupport().getIcon(IconKind.BUTTON_CANCEL);
        } else {
            RadEnumPresentationDef.Item item = getConstSetItemByValue(buttonType);
            return item != null ? item.getIcon() : null;
        }
    }

    protected boolean isDefaultButton(final String buttonType) {
        return getButtonType(buttonType) == EFormButtonType.OK
                || getButtonType(buttonType) == EFormButtonType.EXECUTE;
    }

    protected void onButtonClicked(final String buttonType) {//DBP-1671
        if (getReportView() != null) {
            if (getButtonType(buttonType) == EFormButtonType.OK) {
                getReportView().acceptDialog();
            } else if (getButtonType(buttonType) == EFormButtonType.CANCEL) {
                getReportView().rejectDialog();
            }
        }
    }

    public ArrStr getButtons() {//DBP-1671
        return new ArrStr(EFormButtonType.OK.getValue(), EFormButtonType.CANCEL.getValue());
    }

    public IPushButton createButton(final String buttonType) {//DBP-1671
        final IPushButton button = getEnvironment().getApplication().getWidgetFactory().newPushButton();
        button.setObjectName(buttonType);
        button.setTitle(getButtonText(buttonType));
        button.setIcon(getButtonIcon(buttonType));
        button.addClickHandler(new IButton.ClickHandler() {

            @Override
            public void onClick(IButton source) {
                processButtonClick(source);
            }
        });
        if (isDefaultButton(buttonType)) {
            button.setDefault(true);
        }
        return button;
    }

    @SuppressWarnings("unused")
    private void processButtonClick(IButton source) {//DBP-1671
        onButtonClicked(source.getObjectName());
    }

    protected Id getReportButtonsConstSetId() {
        return null;
    }

    private RadEnumPresentationDef.Item getConstSetItemByValue(final String value) {
        if (value == null || getReportButtonsConstSetId() == null) {
            return null;
        }
        try {
            final RadEnumPresentationDef enumDef = getEnvironment().getDefManager().getEnumPresentationDef(getReportButtonsConstSetId());
            return enumDef.getItems().findItemByValue(ValAsStr.Factory.loadFrom(value));
        } catch (DefinitionError err) {
            final String mess = "Cannot get attibutes for dialog button %s in form %s";
            getEnvironment().getTracer().error(String.format(mess, value, getDefinition().toString()));
        }
        return null;
    }

    private EFormButtonType getButtonType(final String buttonType) {
        if (getReportButtonsConstSetId() == null) {
            try {
                return EFormButtonType.getForValue(buttonType);
            } catch (NoConstItemWithSuchValueError err) {
                return null;
            }
        }
        return null;
    }
    
    protected final void traceParameters(){
        final StringBuilder traceMessageBuilder = new StringBuilder();        
        final String headerMessage = getEnvironment().getMessageProvider().translate("TraceMessage", "Parameters of '%1$s' (#%2$s) report:");
        traceMessageBuilder.append(String.format(headerMessage, getTitle(), getDefinition().getId().toString()));
        final List<Property> props = getActivePropertiesByOrder();        
        boolean registeredInEditorPage;        
        final IEditorPagesHolder editorPagesHolder = getDefinition();                
        for (Property property : props) {
            registeredInEditorPage =
                    editorPagesHolder == null ? false : editorPagesHolder.getEditorPages().isPropertyDefined(property.getId());
            if (property.hasSubscriber() || (editorPagesHolder != null && registeredInEditorPage)) {                
                if (property.isActivated() && property.isVisible()){                
                    traceMessageBuilder.append('\n');
                    traceMessageBuilder.append(String.format("\'%1$s\' (#%2$s) = %3$s", 
                                                             property.getTitle(), 
                                                             property.getDefinition().getId().toString(), 
                                                             property.getValueAsString()));
                }
            }
        }
        getEnvironment().getTracer().debug(traceMessageBuilder.toString());
    }
    
    public org.radixware.schemas.eas.Report toXml(){
        final org.radixware.schemas.eas.Report result = 
            org.radixware.schemas.eas.Report.Factory.newInstance();        
        result.setId(getDefinition().getId());
        result.setProperties(writeProperties());
        return result;        
    }

    public void close() {
        clean();
    }

    //событие вызывается после инициализации свойств в методе readProperties
    protected void afterRead() {
    }

    //выполняется перед закрытием диалога со статусом Accepted
    protected boolean beforeExecReport() {
        return true;
    }

    //выполняется после закрытия диалога со статусом Rejected
    protected void afterCancel() {
    }
    
    public boolean beforeCloseDialog(final IDialog.DialogResult result){
        return true;
    }    
    
    public void afterSetParent(PropertyRef property) {
    }    
}