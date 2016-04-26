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

package org.radixware.wps.views.editor.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.editors.property.PropertyProxy;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.CommonEditingHistory;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import static org.radixware.kernel.common.enums.EPropertyValueStorePossibility.FILE_LOAD;
import static org.radixware.kernel.common.enums.EPropertyValueStorePossibility.FILE_SAVE;
import static org.radixware.kernel.common.enums.EPropertyValueStorePossibility.FILE_SAVE_AND_LOAD;
import static org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.LoadFileAction;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.InputBoxController;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;


public  class ProxyPropEditor extends AbstractPropEditor {

    private final Property property;
    private ValEditorController proxyValEditor;
    private EditMask editMask;
    private EValType evalType;
    private PropertyProxy proxyProp;
    private final WpsEnvironment env;
    private final ISettingsChangeListener settingsChangeListener = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            updateSettings();
        }
    };
    private final ValueEditor.ValueChangeListener valueChangeListener = new ValueEditor.ValueChangeListener() {
        @Override
        public void onValueChanged(Object oldValue, Object newValue) {
            if (newValue != null) {
                String valAsString = ValAsStr.toStr(newValue, evalType);
                getProperty().setValueObject(valAsString);
            } else {
                getProperty().setValueObject(null);
            }
        }
    };

    private WpsTextOptions getTextOptions(final EnumSet<ETextOptionsMarker> valEditorMarkers, final ESelectorRowStyle style) {
        EnumSet<ETextOptionsMarker> markers = getTextOptionsMarkers(valEditorMarkers);
        Property.PropertyTextOptions propertyValueTextOptions = getProperty().getValueTextOptions();
        WpsTextOptions editorOptions = (WpsTextOptions) propertyValueTextOptions.getOptions(markers);

        return editorOptions;
    }

    protected EnumSet<ETextOptionsMarker> getTextOptionsMarkers(final EnumSet<ETextOptionsMarker> valEditorMarkers) {
        final EnumSet<ETextOptionsMarker> propertyMarkers = getProperty().getTextOptionsMarkers();
        if (!valEditorMarkers.contains(ETextOptionsMarker.UNDEFINED_VALUE)) {
            propertyMarkers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        if (!valEditorMarkers.contains(ETextOptionsMarker.INVALID_VALUE)) {
            propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
        }
        return propertyMarkers;
    }

    private class PropertyValueTextOptionsProvider implements ITextOptionsProvider {

        @Override
        public WpsTextOptions getOptions(EnumSet<ETextOptionsMarker> markers, ESelectorRowStyle style) {
            return getTextOptions(markers, style);
        }
    }

    private class InternalValEditorFactory extends ValEditorFactory {

        @Override
        public IValEditor createValEditor(EValType valType, EditMask editMask, IClientEnvironment env) {
            return getDefault().createValEditor(valType, editMask, env);
        }
    }

    private class InternalPropertyProxy extends PropertyProxy {

        private final EditMask proxyEditMask;
        private final Property prop;
        private final EValType proxyType;

        public InternalPropertyProxy(final Property prop, EditMask proxyEditMask, EValType type) {
            super(prop);
            this.proxyEditMask = proxyEditMask;
            this.prop = prop;
            this.proxyType = type;
        }

        @Override
        public EditMask getPropertyEditMask() {
            return proxyEditMask;
        }

        @Override
        public Object getPropertyInitialValue() {
            final Object value = super.getPropertyInitialValue();
            if (value != null) {
                return ValAsStr.Factory.newInstance(prop.getInitialValue(), proxyType);
            } else {
                return null;
            }
        }

        @Override
        public Object getPropertyValue() {
            final Object value = super.getPropertyValue();
            if (value != null) {
                return ValAsStr.fromStr(prop.getValueAsString(), proxyType);
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask) {
        super(prop);
        if (prop == null) {
            throw new IllegalArgumentException("Property cannot be null.");
        }
        if (!prop.getType().equals(EValType.BLOB) && !prop.getType().equals(EValType.STR)) {
            throw new IllegalArgumentException("Cannot create proxy editor for type: " + prop.getType() + ". Property type must be one of EValType.STR or EValType.BLOB.");
        }
        this.property = prop;
        this.evalType = type;
        this.editMask = mask;

        this.proxyProp = new InternalPropertyProxy(property, editMask, evalType);
        IValEditor editor = new InternalValEditorFactory().createValEditor(evalType, editMask, getEnvironment());
        this.proxyValEditor = editor.getController();

        setEditorWidget((UIObject) editor);
        ((UIObject) editor).setHeight(getPreferredHeight());
        setClientHandler("requestFocus", "$RWT.propEditor.requestFocus");
        proxyValEditor.setTextOptionsProvider(new PropertyValueTextOptionsProvider());
        proxyValEditor.addValueChangeListener(valueChangeListener);
        addStandardButtons();
        if (getProperty().getDefinition().storeHistory()) {
            final StringBuilder settingPath = new StringBuilder(getProperty().getOwner().getDefinition().getId().toString());
            settingPath.append("/");
            settingPath.append(SettingNames.SYSTEM);
            settingPath.append("/");
            settingPath.append(getProperty().getDefinition().getId().toString());
            enableEditingHistory(settingPath.toString());
        }
        if (getProperty() != null && getProperty() instanceof SimpleProperty) {
            EPropertyValueStorePossibility ps = getProperty().getPropertyValueStorePossibility();
            if (ps != null) {
                switch (ps) {
                    case FILE_LOAD:
                        addLoadFromFileButton();
                        break;
                    case FILE_SAVE:
                        addSaveToFileButton();
                        break;
                    case FILE_SAVE_AND_LOAD:
                        addLoadFromFileButton();
                        addSaveToFileButton();
                        break;
                    case NONE:
                        break;
                }
            }
        }
        this.env = (WpsEnvironment) getEnvironment();
        this.env.addSettingsChangeListener(settingsChangeListener);
    }

    protected ValEditorController getProxyValEditor() {
        return proxyValEditor;
    }

    @Override
    protected void closeEditor() {
        ValEditorController editor = getProxyValEditor();
        if (editor != null) {
            editor.close();
        }
    }

    protected PropertyProxy getPropertyProxy() {
        return proxyProp;
    }

    protected void enableEditingHistory(final String settingPath) {
        setEditingHistory(new CommonEditingHistory(getEnvironment(), settingPath));
    }

    public void setEditingHistory(final IEditingHistory history) {
        ValEditorController c = getProxyValEditor();
        if (c instanceof InputBoxController && !(getProperty() instanceof PropertyArr)) {
            InputBoxController ib = (InputBoxController) c;
            EValType type = ValueConverter.serverValType2ClientValType(getProperty().getDefinition().getType());
            ib.setEditHistory(history, type);
        }
    }

    @SuppressWarnings("unchecked")
    public void setEditMask(final EditMask mask) {
        if (mask != null) {
            this.editMask = mask;
            this.proxyProp = new InternalPropertyProxy(property, editMask, evalType);
            IValEditor editor = new InternalValEditorFactory().createValEditor(evalType, editMask, getEnvironment());
            this.html.remove(((UIObject) proxyValEditor.getValEditor()).getHtml());
            this.proxyValEditor = editor.getController();
            proxyValEditor.addValueChangeListener(valueChangeListener);
            
            setEditorWidget((UIObject) editor);
            addStandardButtons();
            proxyValEditor.refresh();
             
        }
    }

    public final EditMask getEditMask() {
        return editMask;
    }

    @SuppressWarnings("unchecked")
    public void setType(final EValType type) {
        if (type != null) {
            this.evalType = type;
            this.proxyProp = new InternalPropertyProxy(property, editMask, evalType);
            IValEditor editor = new InternalValEditorFactory().createValEditor(evalType, editMask, getEnvironment());
            this.html.remove(((UIObject) proxyValEditor.getValEditor()).getHtml());
            this.proxyValEditor = editor.getController();
            proxyValEditor.addValueChangeListener(valueChangeListener);
            setEditorWidget((UIObject) editor);
            addStandardButtons();
            proxyValEditor.refresh();
        }
    }

    public final EValType getType() {
        return evalType;
    }

    @Override
    protected void updateSettings() {
        final Property finalProperty = getPropertyProxy().getProperty();
        final ValidationResult state = finalProperty.getOwner().getPropertyValueState(finalProperty.getId());
        getProxyValEditor().setValidationResult(state);
        getProxyValEditor().refreshTextOptions();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void updateEditor(Object currentValue, Object initialValue, PropEditorOptions options) {
        final ValEditorController finalValEditor = getProxyValEditor();
        finalValEditor.setToolTip(options.getTooltip());
        if (currentValue != null) {

            finalValEditor.setValue(ValAsStr.fromStr((String) currentValue, evalType));
        } else {
            finalValEditor.setValue(null);
        }
        finalValEditor.setMandatory(options.isMandatory());
        finalValEditor.setReadOnly(options.isReadOnly());

        if (initialValue != null) {
            finalValEditor.setInitialValue(ValAsStr.fromStr((String) initialValue, evalType));
        } else {

            finalValEditor.setInitialValue(null);
        }
        finalValEditor.refresh();
    }

    @Override
    protected Object getCurrentValueInEditor() {
        if (getProperty().getValueObject() != null) {
            return getProperty().getValueAsString();
        }
        return null;
    }

    @Override
    public void addButton(IButton button) {
        if (getProxyValEditor() != null) {
            getProxyValEditor().addButton(button);
        }
    }

    @SuppressWarnings("unchecked")
    protected void addLoadFromFileButton() {//load
        IButton loadBtn = controller.getLoadFromFileButton();
        if (loadBtn != null) {
            addButton(loadBtn);
            final Property prop = getProperty();
            IUploadedDataReader reader = new IUploadedDataReader() {
                @Override
                public void readData(InputStream stream, String fileName, long fileSize) {
                    try {
                        SimpleProperty property = (SimpleProperty) prop;
                        Object val = property.loadFromStream(stream);
                        //prop.setValueObject(val);
                        getProxyValEditor().setValue(editMask.toStr(env, val));//load as object of evalType
                    } catch (IOException ex) {
                        String mess = String.format("Failed to load value of property %s from file\n%s", getProperty(), fileName);
                        getEnvironment().processException(mess, ex);
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (IOException ex) {
                                getEnvironment().getTracer().error(ex);
                            }
                        }
                    }
                }
            };
            LoadFileAction action = new LoadFileAction(getEnvironment(), reader);
            action.addActionPresenter((RwtAction.IActionPresenter) loadBtn);
            loadBtn.addAction(action);
        }
    }

    protected void addSaveToFileButton() {
        IButton saveBtn = controller.getSaveToFileButton();
        if (saveBtn != null) {
            addButton(saveBtn);
        }
    }

    @Override
    public void setLabelVisible(boolean visible) {
        ValEditorController c = getProxyValEditor();
        if (c instanceof InputBoxController) {
            InputBoxController ib = (InputBoxController) c;
            ib.setLabelVisible(visible);
        }
    }

    @Override
    public boolean getLabelVisible() {
        ValEditorController c = getProxyValEditor();
        if (c instanceof InputBoxController) {
            InputBoxController ib = (InputBoxController) c;
            return ib.isLabelVisible();
        }
        return false;
    }
}
