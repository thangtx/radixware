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
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyArr;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.types.CommonEditingHistory;
import org.radixware.kernel.common.client.types.IEditingHistory;
import org.radixware.kernel.common.client.types.UnacceptableInput;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.client.views.IPropLabel;
import org.radixware.kernel.common.client.views.IPropertyStorePossibility;
import org.radixware.kernel.common.client.views.IPropertyValueStorage;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.ICommandToolButton;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.InputBox;
import org.radixware.wps.rwt.InputBox.DisplayController;
import org.radixware.wps.rwt.Label;
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


public abstract class PropEditor extends AbstractPropEditor implements IPropertyValueStorage {
    
    private final static int COMMAND_BUTTONS_PRIORITY = Integer.MAX_VALUE - 100;

    @Override
    public void setPropertyStorePossibility(IPropertyStorePossibility isp) {
        controller.setPropertyStorePossibility(isp);
    }

    @Override
    public IPropertyStorePossibility getPropertyStorePossibility() {
        return controller.getPropertyStorePossibility();
    }

    protected static class PropertyDisplayController<T> implements InputBox.DisplayController<T> {

        final InputBox.DisplayController<T> defaultDisplayController;
        final Property property;

        public PropertyDisplayController(final InputBox.DisplayController<T> displayController, final Property property) {
            defaultDisplayController = displayController;
            this.property = property;
        }

        @Override
        public String getDisplayValue(T value, boolean isFocused, boolean isReadOnly) {
            final EEditMaskType maskType = property.getEditMask().getType();
            final boolean isEditMaskCompatible = maskType == EEditMaskType.BOOL ||  maskType == EEditMaskType.LIST || maskType == EEditMaskType.ENUM;
            final EValType valType = property.getType();
            final boolean isValTypeCompatible = valType.isArrayType() || valType==EValType.OBJECT || valType==EValType.XML;
            final String defaultDisplayString = defaultDisplayController.getDisplayValue(value, isFocused, isReadOnly);
            if (isEditMaskCompatible || isValTypeCompatible || isReadOnly) {
                return property.getOwner().getDisplayString(
                        property.getId(), value, defaultDisplayString, !property.hasOwnValue());
            } else {
                return defaultDisplayString;
            }
        }
    }

    protected abstract static class AbstractValEditorFactoryImpl<T> extends ValEditorFactory {

        protected final Property property;

        public AbstractValEditorFactoryImpl(final Property property) {
            this.property = property;
        }

        protected DisplayController<T> createDisplayControllerWrapper(final DisplayController<T> defaultController) {
            return new PropertyDisplayController<>(defaultController, property);
        }

        protected final Label createLabel() {
            return LabelComponentFactory.getDefault().createPropLabelComponent(property);
        }
    }

    protected abstract static class LabelComponentFactory {

        private static final DefaultLabelComponentFactory DEFAULT_INSTANCE = new DefaultLabelComponentFactory();

        private static class DefaultLabelComponentFactory extends LabelComponentFactory {

            @Override
            public PropLabel createPropLabelComponent(final Property property) {
                IPropLabel label = property.createPropertyLabel();
                if (label instanceof PropLabel == false) {
                    label = new PropLabel(property);
                }
                label.bind();
                return (PropLabel) label;
            }
        }

        public abstract PropLabel createPropLabelComponent(final Property property);

        public static LabelComponentFactory getDefault() {
            return DEFAULT_INSTANCE;
        }
    }

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
        /*if (valEditorMarkers.contains(ETextOptionsMarker.INVALID_VALUE)) {
            propertyMarkers.add(ETextOptionsMarker.INVALID_VALUE);
        }else{
            propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
        }*/
        return propertyMarkers;
    }

    private class PropertyValueTextOptionsProvaider implements ITextOptionsProvider {

        @Override
        public WpsTextOptions getOptions(EnumSet<ETextOptionsMarker> markers, ESelectorRowStyle style) {
            return getTextOptions(markers, style);
        }
    }
    private ValEditorController valEditor;
    private final WpsEnvironment env;
    private boolean isEnabled = true;
    private boolean wasBinded = false;
    private final ISettingsChangeListener settingsChangeListener = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            updateSettings();
        }
    };

    protected PropEditor(final Property property,
            final ValEditorFactory valEditorFactory) {
        super(property);
        setPreferredHeight(20);
        setupUi(valEditorFactory);
        this.env = (WpsEnvironment) getEnvironment();
        this.env.addSettingsChangeListener(settingsChangeListener);                
    }

    private void setupUi(final ValEditorFactory valEditorFactory) {
        final EValType valType = ValueConverter.serverValType2ClientValType(getProperty().getType());
        final IValEditor editor =
                valEditorFactory.createValEditor(valType, controller.getPropertyEditMask(), getEnvironment());
        valEditor = editor.getController();
        setEditorWidget((UIObject) editor);
        ((UIObject) editor).setHeight(getPreferredHeight());
        ((UIObject) editor).setObjectName("valEditor");
        setClientHandler("requestFocus", "$RWT.propEditor.requestFocus");
        valEditor.setTextOptionsProvider(new PropertyValueTextOptionsProvaider());
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
    }
    
    protected final void changeValEditor(final ValEditorFactory valEditorFactory){
        final boolean isLabelVisible = this.getLabelVisible();
        setupUi(valEditorFactory);
        if (isLabelVisible){
            setLabelVisible(true);
        }
        if (wasBinded){
            setupListeners();
        }
    }

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
                        prop.setValueObject(val);
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

    protected void enableEditingHistory(final String settingPath) {
        setEditingHistory(new CommonEditingHistory(getEnvironment(), settingPath));
    }

    public void setEditingHistory(final IEditingHistory history) {
        ValEditorController c = getValEditor();
        if (c instanceof InputBoxController && !(getProperty() instanceof PropertyArr)) {
            InputBoxController ib = (InputBoxController) c;
            EValType type = ValueConverter.serverValType2ClientValType(getProperty().getDefinition().getType());
            ib.setEditHistory(history, type);
        }
    }

    @Override
    public final void setPreferredHeight(int height) {
        super.setPreferredHeight(height);
        if (valEditor != null) {
            ((UIObject) valEditor.getValEditor()).setHeight(height);
        }
    }

    @Override
    protected void addCommandButtons(final List<ICommandToolButton> commandButtons) {
        final List<ICommandToolButton> buttons = new ArrayList<>(commandButtons);
        Collections.reverse(buttons);
        for (ICommandToolButton commandButton : buttons) {
            valEditor.addButton(commandButton, getCommandButtonPriority(commandButton));
        }
    }
    
    protected int getCommandButtonPriority(final ICommandToolButton commandButton){
        return COMMAND_BUTTONS_PRIORITY;
    }    

    @Override
    public void addButton(IButton button) {
        valEditor.addButton(button);
    }

    @Override
    protected void closeEditor() {
        getValEditor().close();
    }

    protected ValEditorController getValEditor() {
        return valEditor;
    }

    @Deprecated
    protected PropLabel createLabelComponent() {
        PropLabel label = new PropLabel(getProperty());
        label.bind();
        return label;
    }

    @Override
    public void setLabelVisible(boolean visible) {
        ValEditorController c = getValEditor();
        if (c instanceof InputBoxController) {
            InputBoxController ib = (InputBoxController) c;
            ib.setLabelVisible(visible);
        }
    }

    @Override
    public boolean getLabelVisible() {
        ValEditorController c = getValEditor();
        if (c instanceof InputBoxController) {
            InputBoxController ib = (InputBoxController) c;
            return ib.isLabelVisible();
        }
        return false;
    }

    public String getLabel() {
        ValEditorController c = getValEditor();
        if (c instanceof InputBoxController) {
            InputBoxController ib = (InputBoxController) c;
            return ib.getLabel();
        } else {
            return null;
        }
    }

    public void setLabel(String label) {
        ValEditorController c = getValEditor();
        if (c instanceof InputBoxController) {
            InputBoxController ib = (InputBoxController) c;
            ib.setLabel(label);
        }
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        ValEditorController c = getValEditor();
        if (c instanceof InputBoxController) {
            InputBoxController ib = (InputBoxController) c;
            ib.setEnabled(isEnabled);
            this.isEnabled = isEnabled;
        }
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    protected Object getCurrentValueInEditor() {
        return getValEditor().getValue();
    }

    protected final boolean isReadOnly() {
        return controller.isReadonly();
    }

    @Override    
    public void bind() {
        super.bind();
        setupListeners();
        wasBinded = true;
    }
    
    @SuppressWarnings("unchecked")
    private void setupListeners(){
        final ValEditorController finalValEditor = getValEditor();
        finalValEditor.addValueChangeListener(new ValueEditor.ValueChangeListener<Object>() {
            @Override
            public void onValueChanged(final Object oldValue, final Object newValue) {
                controller.finishEdit();
            }
        });
        finalValEditor.addStartChangeValueListener(new ValueEditor.StartChangeValueListener() {
            @Override
            public void onStartChangeValue() {
                controller.onValueModificationStarted();
            }
        });
        finalValEditor.addFinishChangeValueListener(new ValueEditor.FinishChangeValueListener() {
            @Override
            public void onFinishChangeValue(final boolean valueAccepted) {
                if (!valueAccepted) {
                    controller.onValueModificationDiscarded();
                }
            }
        });
        
        finalValEditor.addUnacceptableInputListener(new ValueEditor.UnacceptableInputListener() {
            @Override
            public void onUnacceptableInputChanged(final UnacceptableInput previous, final UnacceptableInput current) {
                PropEditor.this.onUnacceptableInputChanged(current);
            }
        });        
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void updateEditor(final Object currentValue, final Object initialValue, final PropEditorOptions options) {
        final ValEditorController finalValEditor = getValEditor();
        finalValEditor.setToolTip(options.getTooltip());
        final UnacceptableInput unacceptableInput = options.getUnacceptableInput();
        if (unacceptableInput==null){
            finalValEditor.setValue(currentValue);
        }
        finalValEditor.setEditMask(options.getEditMask());
        finalValEditor.setMandatory(options.isMandatory());
        finalValEditor.setReadOnly(options.isReadOnly());
        finalValEditor.setButtonsVisible(options.isEnabled());
        if (finalValEditor instanceof  InputBoxController 
            && RadPropertyDef.isPredefinedValuesSupported(getProperty().getType(), options.getEditMask().getType())){
            ((InputBoxController)finalValEditor).setPredefinedValues(options.getPredefinedValues());
        }
        finalValEditor.setInitialValue(initialValue);        
        if (unacceptableInput!=null){
            finalValEditor.setInputText(options.getUnacceptableInput().getText());
        }
        if (finalValEditor instanceof InputBoxController){
            final InputBoxController inputBox = (InputBoxController)finalValEditor;
            final String dialogTitle = 
                    ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), getProperty().getTitle());
            inputBox.setDialogTitle(dialogTitle);
            inputBox.setMaxPredefinedValuesInDropDownList(getProperty().getMaxPredefinedValuesInDropDownList());        
        }
    }

    @Override
    protected void updateSettings() {
        final Property finalProperty = getProperty();
        final ValidationResult state = finalProperty.getOwner().getPropertyValueState(finalProperty.getId());
        getValEditor().setValidationResult(state);
        getValEditor().refreshTextOptions();
        getValEditor().refresh();
    }
        
    @Override
    public boolean close() {
        if (valEditor != null) {
            valEditor.close();
        }
        if (settingsChangeListener != null) {
            env.removeSettingsChangeListener(settingsChangeListener);
        }
        return super.close();
    }
    
    private void onUnacceptableInputChanged(final UnacceptableInput input){
        if (input==null){
            getProperty().removeUnacceptableInputRegistration();
        }else{
            getProperty().registerUnacceptableInput(input);
        }
    }
    
    @Override
    protected UnacceptableInput getUnacceptableInput() {
        final ValEditorController editor = getValEditor();
        return editor==null ? null : editor.getUnacceptableInput();
    }    
}
