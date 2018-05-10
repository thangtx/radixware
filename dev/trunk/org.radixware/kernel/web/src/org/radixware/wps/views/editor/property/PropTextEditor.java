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

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import org.radixware.kernel.common.client.editors.property.PropEditorController;
import org.radixware.kernel.common.client.editors.property.PropEditorOptions;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyClob;
import org.radixware.kernel.common.client.models.items.properties.PropertyStr;
import org.radixware.kernel.common.client.models.items.properties.SimpleProperty;
import org.radixware.kernel.common.client.text.ITextOptionsProvider;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.AbstractTextField;
import org.radixware.wps.rwt.ButtonBase;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.TableLayout;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBox;
import org.radixware.wps.rwt.uploading.IUploadedDataReader;
import org.radixware.wps.rwt.uploading.LoadFileAction;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.RwtAction;

public class PropTextEditor extends AbstractPropEditor {

    @Override
    public void setLabelVisible(boolean visible) {
    }

    @Override
    public boolean getLabelVisible() {
        return true;
    }

    class MultilineEditorButton extends ToolButton {

        protected ToolButton button;

        public MultilineEditorButton(ToolButton b) {
            super(b.getHtml());
            this.button = b;

            b.getHtml().setCss("border", "1px solid #BBB");
            b.getHtml().setCss("border-radius", "4px");

            b.setIconHeight(12);
            b.setIconWidth(12);

            b.setHeight(19);
            b.setWidth(17);

            this.getHtml().addClass("rwt-tool-button");
            this.getHtml().addClass("rwt-tool-button-div");
            this.getHtml().setAttr("tabIndex", "-1");
            this.getHtml().layout("$RWT.toolButton.layout");
            this.getHtml().setAttr("rwt_f_minsize", "$RWT.toolButton.size");
            this.getHtml().markAsChoosable();
        }
    }

    private class MultilineTextEditor extends AbstractTextField {

        @Override
        public void setReadOnly(boolean isReadOnly) {
            if (isReadOnly) {
                textarea.getHtml().setAttr("readonly", "true");
            } else {
                textarea.getHtml().setAttr("readonly", null);
            }
        }

        @Override
        public boolean isReadOnly() {
            return Utils.equals(textarea.getHtml().getAttr("readonly"), "true");
        }

        private class MultilineTextElement extends UIObject {

            public MultilineTextElement() {
                super(new Html("textarea"));
                getHtml().addClass("rwt-ui-element");
                getHtml().addClass("rwt-ui-element-text");
                //getHtml().addClass("rwt-text-field");

                getHtml().setCss("padding", "0px 3px 0px 5px");
                getHtml().setCss("width", "100%");//98%
                getHtml().setCss("resize", "none");
                getHtml().setCss("height", "100%");
                getHtml().setCss("overflow", "auto");
                getHtml().setCss("padding", "0px");
                getHtml().setCss("margin", "0px");
                getHtml().setCss("border", "solid 1px transparent");

                getHtml().setAttr("spellcheck", "false");
                getHtml().setAttr("onfocus", "$RWT.textField.focusIn");
                getHtml().setAttr("onblur", "$RWT.textField.focusOut");
                getHtml().setAttr("ondrop", "$RWT.textField.drop");
                getHtml().setAttr("ondragover", "$RWT.textField.dragOver");
                getHtml().setAttr("ondragenter", "$RWT.textField.dragEnter");
                getHtml().setAttr("ondragleave", "$RWT.textField.dragLeave");
                getHtml().setAttr("rwt_f_focusIn", "$RWT.inputBox.textFieldFocusIn");//got focus event
                getHtml().setAttr("rwt_f_focusOut", "$RWT.inputBox.textFieldFocusOut");//blur event
            }
        }
        protected MultilineTextElement textarea = new MultilineTextElement();

        public MultilineTextEditor() {
            super(new Div());
            Div editor = new Div();

            editor.setCss("height", "100%");

            editor.add(textarea.getHtml());

            this.getHtml().add(editor);
            this.getHtml().addClass("rwt-text-area");
            this.getHtml().setCss("padding", "3px");

            addTextListener(new TextChangeListener() {
                @Override
                public void textChanged(String oldText, String newText) {
                    if (getProperty().getEditMask().validate(getEnvironment(), newText) == ValidationResult.ACCEPTABLE) {//NOPMD
                        setOnlyValue(newText);
                    } else {
                        setTextOnly((String) value);
                    }
                }
            });
        }

        @Override
        protected void updateTextInHtml(String text) {
            text = text.replaceAll("(\r\n|\n)", "<br />");
            textarea.getHtml().setAttr("value", text);
        }

        public final void editText(final String text) {
            this.textarea.getHtml().setAttr("value", text);
        }

        @Override
        protected final String getTextFromHtml() {
            String text = textarea.getHtml().getAttr("value");
            return text;
        }

        public void setLineWrap(final boolean wrapOn) {
            if (wrapOn) {
                textarea.getHtml().setCss("white-space", null);
                textarea.getHtml().setAttr("wrap", null);
            } else {
                textarea.getHtml().setCss("white-space", "pre");
                textarea.getHtml().setAttr("wrap", "off");
            }
        }

        public boolean getLineWrap() {
            String wrap = textarea.getHtml().getAttr("wrap");
            return wrap != null;
        }

        protected Html getInputHtml() {
            return textarea.getHtml();
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            return textarea.getHtml().getId().equals(id) ? this : null;
        }

        public void setClientOnFocusHandler(final String method_name) {
            textarea.getHtml().setAttr("rwt_f_focusIn", method_name);
        }

        public void setClientOnBlurHandler(final String method_name) {
            textarea.getHtml().setAttr("rwt_f_focusOut", method_name);
        }

        @Override
        public void setClientHandler(String customEventName, String code) {
            super.setClientHandler(textarea.getHtml(), customEventName, code);
        }

        @Override
        protected String[] clientScriptsRequired() {
            return Utils.merge(new String[]{"org/radixware/wps/rwt/jquery.hotkeys-0.7.9.min.js", "org/radixware/wps/rwt/inputBox.js"});
        }
    }

    private class ClearButton extends ToolButton {

        public ClearButton() {
            super();
            this.setIcon(getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CLEAR));
            this.getHtml().addClass("rwt-clear-button");
            this.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    setValue(null);
                }
            });
        }
    }
    private MultilineTextEditor editor;
    private TableLayout table = new TableLayout();
    private TableLayout.Row row = table.addRow();
    private Object value;
    private TableLayout.Row.Cell editorCell;
    private TableLayout.Row.Cell buttonsCell;
    private final VerticalBox vbox = new VerticalBox();
    private WpsTextOptions textOptions;
    private ITextOptionsProvider provider;
    private EnumSet<ETextOptionsMarker> textOptionsMarkers = EnumSet.of(ETextOptionsMarker.EDITOR);
    private Container container = new Container();
    private ClearButton clearButton = null;
    private boolean isEnabled = true;
    private Object previousValue;
    private boolean isInModificationState = false;
    private boolean isReadOnly;
    private final ISettingsChangeListener listener = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            updateSettings();
        }
    };

    public PropTextEditor(final PropertyStr property) {
        super(property);
        setupUI();
    }

    public PropTextEditor(final PropertyClob property) {
        super(property);
        setupUI();
    }

    private void setupUI() {
        final EValType valType = controller.getProperty().getDefinition().getType();
        if (valType != EValType.STR && valType != EValType.CLOB) {
            throw new IllegalArgumentException("String or CLOB property expected, but " + valType.getName() + " property got");
        }
        value = getProperty().getValueObject();
        editor = new MultilineTextEditor();
        editor.getHtml().setCss("height", "80px");
        editor.getHtml().setAttr("name", "propTextEditor");//important!
        controller.addFocusListener(new PropEditorController.FocusListener() {
            @Override
            public void focused() {
                onFocusIn();
            }

            @Override
            public void unfocused() {
                onFocusOut();
            }
        });
        editorCell = row.addCell();
        editorCell.getHtml().setCss("border-radius", "4px");
        editorCell.getHtml().setCss("border", "solid 1px #BBB");
        container.getHtml().addClass("rwt-input-box");

        container.getHtml().setAttr("validationMessage", "");
        container.getHtml().setAttr("isvalid", "true");
        container.getHtml().setAttr("is_clearable", "false");

        container.setClientHandler("requestFocus", "$RWT.inputBox.requestFocusTextArea");
        container.add(table);
        setEditorWidget(container);

        buttonsCell = row.addCell();
        buttonsCell.getHtml().setCss("background-color", "#f5f7f9");
        buttonsCell.getHtml().setCss("border", "none");
        buttonsCell.getHtml().setCss("width", "23px");

        provider = getEnvironment().getTextOptionsProvider();

        textOptions = WpsTextOptions.EMPTY;
        textOptionsMarkers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        if (getProperty().isReadonly()) {
            textOptionsMarkers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        if (getProperty().isMandatory()) {
            textOptionsMarkers.add(ETextOptionsMarker.MANDATORY_VALUE);
        }

        editorCell.add(editor);
        buttonsCell.add(vbox);
        buttonsCell.getHtml().setAttr("valign", "top");
        vbox.getHtml().setCss("height", "auto");//ie comaptible
        vbox.getHtml().setCss("display", "table-row");
        //vbox.setWidth(25);
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
        addStandardButtons();
        setClearController(true);
        setEnabled(getProperty().isEnabled());
        //setValue(null);
        editor.textarea.setTextOptions(calculateTextOptions(textOptionsMarkers));
        ((WpsEnvironment) this.getEnvironment()).addSettingsChangeListener(listener);
    }

    protected void addLoadFromFileButton() {
        IButton loadBtn = controller.getLoadFromFileButton();
        if (loadBtn != null) {
            addButton(loadBtn);
            final Property p = getProperty();
            IUploadedDataReader reader = new IUploadedDataReader() {
                @Override
                public void readData(InputStream stream, String fileName, long fileSize) {
                    try {
                        SimpleProperty property = (SimpleProperty) p;
                        Object val = property.loadFromStream(stream);
                        p.setValueObject(val);
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

    public void setClearController(boolean set) {
        if (set) {
            html.setAttr("is_clearable", true);
            addButton(clearButton = new ClearButton());
            updateValuesMarker();
        } else {
            html.setAttr("is_clearable", null);
            updateValuesMarker();
        }
        updateButtons();
    }

    public void setLineWrap(boolean lineWrap) {
        editor.setLineWrap(lineWrap);
    }

    public boolean getLineWrap() {
        return editor.getLineWrap();
    }


    public void onFocusIn() {
        updateValuesMarker();
        if (value == null && !controller.isReadonly()) {
            setTextOnly("");
        }
    }

    public void onFocusOut() {
        if (inModificationState()) {
            setModificationState(false);
        }
        updateValuesMarker();
    }


    @Override
    protected void closeEditor() {
        if (listener != null) {
            ((WpsEnvironment) this.getEnvironment()).removeSettingsChangeListener(listener);
        }
    }

    @Override
    protected void updateEditor(Object currentValue, Object initialValue, PropEditorOptions options) {
        if (!setValue((String) currentValue)) {
            updateTextEdit();
        }
        editor.setToolTip(options.getTooltip());
        editor.setReadOnly(options.isReadOnly());
        buttonsCell.setVisible(options.isEnabled());
        editor.setFocused(false);
    }

    private void updateTextEdit() {
        final String text = getProperty().getEditMask().toStr(getProperty().getEnvironment(), value);
        if (controller.isReadonly()) {
            final String displayText = getProperty().getOwner().getDisplayString(
                    getProperty().getId(), getProperty().getValueObject(), text, !getProperty().hasOwnValue());
            setTextOnly(displayText);
        } else {
            setTextOnly(text);
            setValueAttr(text);
        }
    }

    private void setTextOnly(String text) {
        editor.updateTextInHtml(text);
        updateValuesMarker();
    }

    private boolean setValue(final String newValue) {
        if (setOnlyValue(newValue)) {
            updateTextEdit();
            updateValuesMarker();
            return true;
        }
        updateValuesMarker();
        return false;
    }

    private void setValueAttr(String newValue) {
        if (editor != null) {
            if (value != null) {
                editor.getHtml().setAttr("value_is_null", null);
                setFocusedText(newValue);
                setTextOnly(newValue);
            } else {
                editor.getHtml().setAttr("value_is_null", true);
                setFocusedText("");
                setTextOnly(newValue);
            }
        }
    }

    private boolean setOnlyValue(final String newValue) {
        if (Utils.equals(value, newValue)) {
            return false;
        }
        if (!inModificationState()) {
            setModificationState(true);
        }
        this.value = newValue;
        if (newValue == null) {
            setValueAttr(getProperty().getEditMask().getNoValueStr(getProperty().getEnvironment().getMessageProvider()));
        } else {
            setValueAttr(newValue);
        }
        controller.onValueEdit(getValue());
        return true;
    }

    private boolean applyTextOptionsMarkers(final EnumSet<ETextOptionsMarker> newMarkers) {
        if (!textOptionsMarkers.equals(newMarkers)) {
            textOptionsMarkers = newMarkers.clone();
            refreshTextOptions();
            updateValuesMarker();
            return true;
        }
        updateValuesMarker();
        return false;
    }

    public final boolean setTextOptionsMarkers(final ETextOptionsMarker... markers) {
        final EnumSet<ETextOptionsMarker> newMarkers = EnumSet.noneOf(ETextOptionsMarker.class);
        newMarkers.addAll(Arrays.asList(markers));
        return applyTextOptionsMarkers(newMarkers);
    }

    @Override
    protected void updateSettings() {
        refreshTextOptions();
    }

    public void refreshTextOptions() {
        WpsTextOptions opts = calculateTextOptions(textOptionsMarkers);
        final EnumSet<ETextOptionsMarker> markers = textOptionsMarkers.clone();

        if (getValue() == null) {
            markers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        if (getValue() == null) {
            markers.add(ETextOptionsMarker.MANDATORY_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        }
        {
            markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
            WpsTextOptions editopts = calculateTextOptions(markers);
            Color editFgrn = editopts.getForegroundColor();
            this.editor.getHtml().setAttr("editcolor", color2Str(editFgrn));
        }
        /*if (!textOptionsMarkers.equals(markers)) {
         applyTextOptionsMarkers(markers);
         }*/
        editor.textarea.setTextOptions(opts);
    }

    public final EnumSet<ETextOptionsMarker> getTextOptionsMarkers() {
        return textOptionsMarkers.clone();
    }

    protected WpsTextOptions calculateTextOptions(final EnumSet<ETextOptionsMarker> markers) {
        final WpsTextOptions options = (WpsTextOptions) provider.getOptions(markers, null);
        if (options == null) {
            final StringBuilder traceMessage = new StringBuilder();
            traceMessage.append("Unable to get text options.\n");
            if (markers == null) {
                traceMessage.append("Text options markers are null");
            } else if (markers.isEmpty()) {
                traceMessage.append("Text options markers are empty");
            } else {
                traceMessage.append("Text options markers: [");
                for (ETextOptionsMarker marker : markers) {
                    traceMessage.append(marker.name());
                    traceMessage.append(" ");
                }
            }
            traceMessage.append("]\nText options provider is ");
            traceMessage.append(provider.getClass().getName());
            getEnvironment().getTracer().warning(traceMessage.toString());
        }
        return options;
    }

    @Override
    public void setTextOptions(final WpsTextOptions options) {
        if (options == null) {
            final WpsTextOptions defaultOptions = WpsTextOptions.getDefault((WpsEnvironment) getEnvironment());
            super.setTextOptions(defaultOptions);
            editorCell.setBackground(defaultOptions.getBackgroundColor());
            if (!defaultOptions.getFont().equals(textOptions.getFont())) {
                editor.textarea.setFont(defaultOptions.getFont());
            }
            textOptions = defaultOptions;
        } else if (!textOptions.equals(options) || !textOptions.getPredefinedCssStyles().equals(options.getPredefinedCssStyles())) {
            super.setTextOptions(options);
            if (!options.getFont().equals(textOptions.getFont())) {
                editor.textarea.setFont(options.getFont());
            }
            editorCell.setBackground(options.getBackgroundColor());
            textOptions = options;
        }
    }

    private void updateButtons() {
        if (clearButton != null) {
            clearButton.setVisible(!isReadOnly());
        }
        if (controller.getLoadFromFileButton() != null) {
            controller.getLoadFromFileButton().setEnabled(isEnabled);
            controller.getLoadFromFileButton().setVisible(!isReadOnly);
            if (((ButtonBase)controller.getLoadFromFileButton()).getFileUploader()!=null){
                ((ButtonBase)controller.getLoadFromFileButton()).getFileUploader().setEnabled(isEnabled && !isReadOnly);
            }
        }
        if (controller.getSaveToFileButton() != null) {
            controller.getSaveToFileButton().setVisible(!isReadOnly);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        if (isEnabled) {
            html.setAttr("disabled", null);
            editor.textarea.setEnabled(true);
            html.removeClass("ui-state-disabled");
        } else {
            html.setAttr("disabled", true);
            editor.textarea.setEnabled(false);
            html.addClass("ui-state-disabled");
        }
        updateButtons();
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
        editor.setReadOnly(isReadOnly);
        if (readOnly) {
            html.setAttr("isvalid", true);
        } else {
            html.setAttr("isvalid", null);
        }
        updateValuesMarker();
        updateButtons();
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    private void updateValuesMarker() {
        final EnumSet<ETextOptionsMarker> markers = textOptionsMarkers.clone();
        if (getValue() == null) {
            markers.add(ETextOptionsMarker.UNDEFINED_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.UNDEFINED_VALUE);
        }
        if (getValue() == null && getProperty().isMandatory()) {
            markers.add(ETextOptionsMarker.MANDATORY_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.MANDATORY_VALUE);
        }
        if (getValue() == null && getProperty().isReadonly()) {
            markers.add(ETextOptionsMarker.READONLY_VALUE);
        } else {
            markers.remove(ETextOptionsMarker.READONLY_VALUE);
        }
        if (!textOptionsMarkers.equals(markers)) {
            applyTextOptionsMarkers(markers);
        }
    }

    public Object getValue() {
        return value;
    }

    @Override
    protected Object getCurrentValueInEditor() {
        return value;
    }

    public final void setMaxLength(final int maxLength) {
        editor.textarea.getHtml().setAttr("maxLength", maxLength);
    }

    public int getMaxLength() {
        return Integer.parseInt(editor.textarea.getHtml().getAttr("maxLength"));
    }

    public final void setFocusedText(final String focusedText) {
        container.getHtml().setAttr("focusedtext", focusedText.replaceAll("(\r\n|\n)", "<br />"));
    }

    public final void setInitialText(final String initialText) {
        container.getHtml().setAttr("initialtext", initialText.replaceAll("(\r\n|\n)", "<br />"));
    }

    private void setModificationState(final boolean inModification) {
        if (inModification) {
            previousValue = value;
        }
        isInModificationState = inModification;
    }

    private boolean inModificationState() {
        return isInModificationState;
    }

    @Override
    public void addButton(IButton button) {
        if (button instanceof ToolButton) {
            ToolButton btn = (ToolButton) button;
            btn.getHtml().setCss("border", "1px solid #BBB");
            btn.getHtml().setCss("border-radius", "4px");

            btn.setIconHeight(12);
            btn.setIconWidth(12);

            btn.setHeight(19);
            btn.setWidth(17);

            btn.getHtml().addClass("rwt-tool-button");
            btn.getHtml().addClass("rwt-tool-button-div");
            btn.getHtml().setAttr("tabIndex", "-1");
            btn.getHtml().layout("$RWT.toolButton.layout");
            btn.getHtml().setAttr("rwt_f_minsize", "$RWT.toolButton.size");
            btn.getHtml().markAsChoosable();
            vbox.add(btn);
            vbox.addSpace(2);
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject result = super.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        result = table.findObjectByHtmlId(id);
        if (result != null) {
            return result;
        }
        return result;
    }

    @Override
    protected String[] clientScriptsRequired() {
        return Utils.merge(new String[]{"org/radixware/wps/rwt/jquery.hotkeys-0.7.9.min.js", "org/radixware/wps/rwt/inputBox.js", "org/radixware/wps/rwt/table-layout.js"});
    }
}
