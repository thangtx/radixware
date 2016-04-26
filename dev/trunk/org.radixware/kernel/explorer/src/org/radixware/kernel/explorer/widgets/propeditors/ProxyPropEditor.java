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

package org.radixware.kernel.explorer.widgets.propeditors;

import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.editors.property.PropertyProxy;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.CommonEditingHistory;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public  class ProxyPropEditor extends AbstractPropEditor {

    private final Property property;
    private final QHBoxLayout layout;
    private ValEditor proxyValEditor;
    private EditMask editMask;
    private EValType evalType;
    private PropertyProxy proxyProp;    

    private class PropertyValueTextOptionsProvider implements ITextOptionsProvider {

        @Override
        public ExplorerTextOptions getOptions(final EnumSet<ETextOptionsMarker> markers, final ESelectorRowStyle style) {
            return ProxyPropEditor.this.getTextOptions(markers);
        }
    }

    private class InternalValEditorFactory extends ValEditorFactory {

        @Override
        public ValEditor createValEditor(final EValType valType, final EditMask editMask, final IClientEnvironment environment, final QWidget parentWidget) {
            return getDefault().createValEditor(valType, editMask, environment, parentWidget);
        }
    }

    private class InternalPropertyProxy extends PropertyProxy {

        private final EditMask proxyEditMask;
        private final Property prop;
        private final EValType proxyType;

        public InternalPropertyProxy(final Property prop, final EditMask proxyEditMask, final EValType type) {
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

    @SuppressWarnings("LeakingThisInConstructor")
    public ProxyPropEditor(final Property prop, final EValType type, final EditMask mask) {
        super(prop);
        if (prop == null) {
            throw new IllegalArgumentException("Property cannot be null.");
        }
        if (!prop.getType().equals(EValType.BLOB) && !prop.getType().equals(EValType.STR)) {
            throw new IllegalArgumentException("Cannot create proxy editor for type: " + prop.getType() + ". Property type must be one of EValType.STR or EValType.BLOB.");
        }
        layout = WidgetUtils.createHBoxLayout(this);
        property = prop;
        evalType = type;
        editMask = mask;

        proxyProp = new InternalPropertyProxy(property, editMask, evalType);
        proxyValEditor = new InternalValEditorFactory().createValEditor(evalType, editMask, getEnvironment(), this);
        proxyValEditor.setTextOptionsProvider(new PropertyValueTextOptionsProvider());

        if (getProperty().getDefinition().storeHistory() && !(getProperty() instanceof PropertyArr)) {
            final StringBuilder settingPath = new StringBuilder(getProperty().getOwner().getDefinition().getId().toString());
            settingPath.append('/');
            settingPath.append(SettingNames.SYSTEM);
            settingPath.append('/');
            settingPath.append(getProperty().getDefinition().getId().toString());
            enableEditingHistory(settingPath.toString());
        }
        layout.addWidget(proxyValEditor);
        proxyValEditor.valueChanged.connect(this, "onValueChanged(Object)");
        addStandardButtons();
    }

    @SuppressWarnings("unused")
    private void onValueChanged(final Object val) {//typed object
        if (val != null) {
            final String valAsString = ValAsStr.toStr(val, evalType);
            getProperty().setValueObject(valAsString);
        } else {
            getProperty().setValueObject(null);
        }
    }

    protected ValEditor getProxyValEditor() {
        return proxyValEditor;
    }

    @Override
    protected void closeEditor() {
        final ValEditor editor = getProxyValEditor();
        if (editor != null) {
            editor.close();
        }
    }

    protected PropertyProxy getPropertyProxy() {
        return proxyProp;
    }

    protected QHBoxLayout getLayout() {
        return layout;
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

    private ExplorerTextOptions getTextOptions(final EnumSet<ETextOptionsMarker> valEditorMarkers) {
        return (ExplorerTextOptions) getProperty().getValueTextOptions().getOptions(getTextOptionsMarkers(valEditorMarkers));
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
    protected void updateEditor(final Object value, final PropEditorOptions options) {//String value 
        final ValEditor finalValEditor = getProxyValEditor();
        finalValEditor.setToolTip(options.getTooltip());
        if (value != null) {
            final Object val = ValAsStr.fromStr(editMask.toStr(getEnvironment(), value), evalType);
            finalValEditor.setValue(val);
        } else {
            finalValEditor.setValue(null);
        }
        finalValEditor.setMandatory(options.isMandatory());
        finalValEditor.setReadOnly(options.isReadOnly());
        if (RadPropertyDef.isPredefinedValuesSupported(getProperty().getType(), options.getEditMask().getType())) {
            finalValEditor.setPredefinedValues(options.getPredefinedValues());
        }
        finalValEditor.refresh();
    }

    public void setEditMask(final EditMask mask) {
        if (mask != null) {
            editMask = mask;
            proxyProp = new InternalPropertyProxy(property, editMask, evalType);
            layout.removeWidget(proxyValEditor);
            proxyValEditor.valueChanged.disconnect(this, "onValueChanged(Object)");
            proxyValEditor = new InternalValEditorFactory().createValEditor(evalType, editMask, getEnvironment(), this);
            proxyValEditor.valueChanged.connect(this, "onValueChanged(Object)");
            layout.addWidget(proxyValEditor);
            proxyValEditor.refresh();
        }
    }

    public final EditMask getEditMask() {
        return editMask;
    }

    public void setType(final EValType type) {
        if (type != null) {
            this.evalType = type;
            proxyProp = new InternalPropertyProxy(property, editMask, evalType);
            layout.removeWidget(proxyValEditor);
            proxyValEditor.valueChanged.disconnect(this, "onValueChanged(Object)");
            proxyValEditor = new InternalValEditorFactory().createValEditor(evalType, editMask, getEnvironment(), this);
            proxyValEditor.valueChanged.connect(this, "onValueChanged(Object)");
            layout.addWidget(proxyValEditor);
            proxyValEditor.refresh();
        }
    }

    public final EValType getType() {
        return evalType;
    }

    protected void enableEditingHistory(final String settingPath) {
        setEditingHistory(new CommonEditingHistory(getEnvironment(), settingPath));
    }

    public void setEditingHistory(final IEditingHistory history) {
        getProxyValEditor().setEditingHistory(history);
    }

    @Override
    protected Object getCurrentValueInEditor() {
        if (property.getValueObject() == null) {
            return null;
        }
        return property.getValueAsString();
    }

    @Override
    public void addButton(final IButton button) {
        if (getProxyValEditor() != null) {
            getProxyValEditor().addButton((QToolButton) button);
        }
    }

    @Override
    public boolean setFocus(final Property aThis) {
        if (getProxyValEditor() != null) {
            getProxyValEditor().setFocus();
            return true;
        }
        return false;
    }
}
