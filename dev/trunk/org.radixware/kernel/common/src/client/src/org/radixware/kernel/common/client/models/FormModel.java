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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ModelException;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadFormDef;
import org.radixware.kernel.common.client.meta.RadParentRefPropertyDef;
import org.radixware.kernel.common.client.meta.RadPresentationCommandDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyRef;
import org.radixware.kernel.common.client.models.items.properties.PropertyValue;
import org.radixware.kernel.common.client.models.items.properties.PropertyXml;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.views.IFormView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;

import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EFormButtonType;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;

/**
 * Класс модели формы. Предоставляет доступ к свойствам, страницам и командам формы.
 * Содержит обработчики сигналов кастомного представления формы.
 * <p>Метод submit отсылает значения свойств формы на сервер и закрывает представление формы.
 * Перед посылкой значений свойств на сервер вызывается обработчик beforeSubmit.
 * <p>Потомки (Tafm<GUID>) генерируются дизайнером.
 * Для каждой формы существует сгенерированный класс ее модели.
 *
 */
public abstract class FormModel extends ModelWithPages {
    
    private final static Id STANDARD_BUTTONS_ENUM_ID = Id.Factory.loadFrom("acsS7F4BA7TJXNRDCHCABIFNQAABA");
    
    private List<Id> commandIds = null;
    private boolean reopen;
    private boolean closed;    

    public enum FormResult {

        NEXT(1, DialogResult.ACCEPTED),
        PREVIOUS(2, DialogResult.APPLY),
        CANCEL(0,DialogResult.REJECTED);
        
        public final int value;
        private final DialogResult dialogResult;

        private FormResult(final int value, final DialogResult result) {
            this.value = value;
            dialogResult = result;
        }
        
        public DialogResult getDialogResult(){
            return dialogResult;
        }
    }
    
    private FormResult saveResult = FormResult.PREVIOUS;

    protected FormModel(IClientEnvironment environment, RadFormDef def) {
        super(environment, def);
    }

    public RadFormDef getFormDef() {
        return (RadFormDef) getDefinition();
    }

    public IFormView getFormView() {
        return getView() != null ? (IFormView) getView() : null;
    }

    @Override
    public void setView(IView view_) {
        if (view_ == null && getFormView() != null) {
            saveResult = getFormView().hasUI() ? FormResult.CANCEL : getFormView().formResult();
        }
        super.setView(view_);
    }

    public IContext.Form getFormContext() {
        return (IContext.Form) getContext();
    }

    @Override
    public void setTitle(String newTitle) {
        super.setTitle(newTitle);
        if (getFormView() != null) {
            getFormView().setWindowTitle(getWindowTitle());
        }
    }

    @Override
    public void setIcon(Icon icon) {
        super.setIcon(icon);
        if (getFormView() != null) {
            getFormView().setWindowIcon(icon);
        }
    }

    @Override
    protected Property activateProperty(Id id) {
        final Property p = super.activateProperty(id);
        if (p != null) {
            final RadPropertyDef propertyDef = p.getDefinition();
            p.setServerValue(new PropertyValue(propertyDef, ValAsStr.fromStr(propertyDef.getInitialVal(), propertyDef.getType())));
        }
        return p;
    }

    private class DialogEventListener implements IDialog.DialogResultListener {

        @Override
        public void dialogClosed(IDialog dialog, DialogResult result) {
            if (result == DialogResult.REJECTED) {
                afterReject();
            }
        }
    }
    private final DialogEventListener rejectListener = new DialogEventListener();

    @Override
    public void clean() {
        if (getFormView() != null) {
            getFormView().getEventSupport().removeResultListener(DialogResult.REJECTED, rejectListener);
        }
        closed = true;
        super.clean();
    }

    @Override
    public IView createView() {
        final IView result = super.createView();
        if (result != null) {

            ((IFormView) result).getEventSupport().addResultListener(DialogResult.REJECTED, rejectListener);
        }
        return result;
    }

    public static FormModel create(final Id formId, final IContext.Form ctx) {
        final RadFormDef def = ctx.getEnvironment().getDefManager().getFormDef(formId);
        final FormModel form = def.createModel(ctx);
        form.afterInit();
        form.afterRead();
        return !form.closed ? form : null;
    }

    public static FormModel create(org.radixware.schemas.eas.Form form, final IContext.Form ctx) {
        final RadFormDef def = ctx.getEnvironment().getDefManager().getFormDef(form.getId());
        final FormModel model = def.createModel(ctx);
        model.setPropertiesImpl(form.getProperties());
        model.afterInit();
        model.afterRead();
        return !model.closed ? model : null;
    }

    public void setProperties(org.radixware.schemas.eas.PropertyList propertyList) {
        if (getFormView() != null) {
            getFormView().setUpdatesEnabled(false);
        }
        try {
            setPropertiesImpl(propertyList);
            afterRead();
        } finally {
            if (getFormView() != null) {
                getFormView().setUpdatesEnabled(true);
            }
        }
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
                        value = xmlProp.castValue(ValueConverter.easPropXmlVal2ObjVal(item, EValType.XML, null));
                    } else {
                        final Id valTableId;
                        if (property.getDefinition() instanceof RadParentRefPropertyDef) {
                            valTableId = ((RadParentRefPropertyDef) property.getDefinition()).getReferencedTableId();
                        } else {
                            valTableId = null;
                        }                        
                        value = ValueConverter.easPropXmlVal2ObjVal(item, property.getDefinition().getType(),
                                valTableId);                        
                    }
                    property.setValueObject(value);
                    property.setServerValue(new PropertyValue(property.getDefinition(),value));
                } catch (RuntimeException ex) {
                    showException(ex);
                }
            }
        }
    }

    public boolean submit() throws ModelException, ServiceClientException, InterruptedException {
        if (!beforeSubmit()) {
            return false;
        }
        checkPropertyValues();
        IContext.Form ctx = (IContext.Form) getContext();
        final Id commandId = ctx.startCommand.getDefinition().getId();
        final XmlObject commandResponse;

        if (ctx.startCommand.getDefinition() instanceof RadPresentationCommandDef) {
            commandResponse = getEnvironment().getEasSession().executeCommand(ctx.startCommand.getOwner(), this, commandId, ctx.commandPropertyId, null);
        } else {
            commandResponse = getEnvironment().getEasSession().executeCommand(this, commandId, null);
        }

        final FormModel nextForm = ctx.startCommand.processResponse(commandResponse, this, ctx.commandPropertyId);

        if (nextForm == this) {
            if (getFormView() != null) {
                getFormView().show();
            }
            return true;
        }

        if (nextForm != null) {
            try {
                final FormResult result = nextForm.exec(getFormView());
                reopen = (result == FormResult.PREVIOUS) && (getFormView() != null);
                if (reopen) {
                    return false;
                } else {
                    if (getFormView() != null) {
                        getFormView().done(result);
                    }
                    clean();
                }
            } finally {
                nextForm.clean();
            }
        } else {
            clean();
        }
        return true;
    }

    public FormResult exec(IFormView previous) {
        do {
            cleanPages();
            setView(null);
            IFormView view = (IFormView) createView();
            reopen = false;
            view.open(this);
            if (getView() != null) {
                //при открытии формы не был вызван метод close
                if (previous != null) {
                    previous.getEventSupport().removeResultListeners(DialogResult.REJECTED);
                    previous.hide();
                }
                view.execDialog();

                saveResult = view.formResult();
            }
        } while (reopen);

        if (getFormView() != null) {
            setView(null);
        }
        return saveResult;
    }

    public boolean returnToPreviousForm() {
        if (((IContext.Form) getContext()).previousForm == null) {
            throw new IllegalUsageError("There are no previous form");
        }
        if (beforeReturnToPreviousForm()) {
            if (getView() != null) {
                getFormView().getEventSupport().removeResultListeners(DialogResult.REJECTED);
                getFormView().done(FormResult.PREVIOUS);
                if (getFormView()!=null){
                    getFormView().close();
                    cleanPages();
                    setView(null);
                }
            }
            //close();
            return true;
        }
        return false;
    }

    public boolean beforeSubmit() throws ModelException, ServiceClientException, InterruptedException {
        return true;
    }

    public boolean beforeReturnToPreviousForm() {
        return true;
    }

    public void afterInit() {
    }

    protected void afterRead() {
    }

    public void afterReject() {
    }

    public void afterSetParent(PropertyRef property) {
    }    
    
    public org.radixware.schemas.eas.Form toXml() {
        final org.radixware.schemas.eas.Form result = org.radixware.schemas.eas.Form.Factory.newInstance();
        result.setId(getDefinition().getId());
        if (properties != null && !properties.isEmpty()) {
            final org.radixware.schemas.eas.PropertyList propList = result.addNewProperties();
            //Copy properties collection to avoid concurrent modification in overrided getters
            final Collection<Property> props = new ArrayList<>();
            props.addAll(properties.values());
            for (Property property : props) {
                if (!property.isLocal() && property.getDefinition().getNature()!= EPropNature.PARENT_PROP){
                    property.writeValue2Xml(propList.addNewItem());
                }
            }
        }
        IContext.Form ctx = (IContext.Form) getContext();

        if (ctx != null && ctx.previousForm != null) {
            result.setPrevForm(ctx.previousForm.toXml());
        }
        return result;
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
            return getApplication().getImageManager().loadIcon(ClientIcon.Dialog.BUTTON_OK.fileName);//getEnvironment().getEnvironment().getIconSupport().getIcon(IconKind.BUTTON_OK);
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
        if (getFormView() != null) {
            if (getButtonType(buttonType) == EFormButtonType.OK) {
                getFormView().submit();
            } else if (getButtonType(buttonType) == EFormButtonType.CANCEL) {
                getFormView().rejectDialog();
            }
        }
    }

    public ArrStr getButtons() {//DBP-1671
        return new ArrStr(EFormButtonType.OK.getValue(), EFormButtonType.CANCEL.getValue());
    }

    public void setupButton(final IPushButton button, final String buttonType) {//DBP-1671
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
    }

    @SuppressWarnings("unused")
    private void processButtonClick(IButton button) {//DBP-1671
        onButtonClicked(button.getObjectName());
    }

    protected Id getFormButtonsConstSetId() {
        return null;
    }

    private Id getStdButtonsConstSetId() {
        return STANDARD_BUTTONS_ENUM_ID;
    }

    private RadEnumPresentationDef.Item getConstSetItemByValue(final String value) {
        if (value == null) {
            return null;
        }
        if (getFormButtonsConstSetId() == null) {
            return getConstSetItemByValue(getStdButtonsConstSetId(), value);
        } else {
            return getConstSetItemByValue(getFormButtonsConstSetId(), value);
        }
    }

    private RadEnumPresentationDef.Item getConstSetItemByValue(final Id enumId, final String value) {
        if (enumId != null) {
            try {
                final RadEnumPresentationDef enumDef = getEnvironment().getDefManager().getEnumPresentationDef(enumId);
                return enumDef.getItems().findItemByValue(ValAsStr.Factory.loadFrom(value));
            } catch (DefinitionError err) {
                final String mess = "Cannot get attibutes for dialog button %s in form %s";
                getEnvironment().getTracer().error(String.format(mess, value, getDefinition().toString()));
            }
        }
        return null;
    }

    private EFormButtonType getButtonType(final String buttonType) {
        if (getFormButtonsConstSetId() == null) {
            try {
                return EFormButtonType.getForValue(buttonType);
            } catch (NoConstItemWithSuchValueError err) {
                return null;
            }
        }
        return null;
    }

    public void close() {
        clean();
    }
    
    public boolean beforeCloseDialog(final IDialog.DialogResult result){
        return true;
    }    
}