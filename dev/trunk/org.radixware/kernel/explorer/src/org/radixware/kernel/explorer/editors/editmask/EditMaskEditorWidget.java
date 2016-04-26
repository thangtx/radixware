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

package org.radixware.kernel.explorer.editors.editmask;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QStackedLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMaskTimeInterval.Scale;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.editors.editmask.consteditor.ConstSetEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.datetimeeditor.DateTimeEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.inteditor.IntEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.listeditor.ListEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.numeditor.NumEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.streditor.StrEditMaskEditorWidget;
import org.radixware.kernel.explorer.editors.editmask.timeintervaleditor.TimeIntervalEditMaskEditorWidget;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public final class EditMaskEditorWidget extends ExplorerWidget implements IEditMaskEditor {

    private static final String MASK_TYPE_SELECTOR = "MaskTypeSelector";
    private final IClientEnvironment environment;
    private boolean isDefaultMaskUsed = false;
    private boolean readOnly = false;
    private final QStackedLayout maskContainer = new QStackedLayout();
    private QComboBox cbMaskTypeSelector;
    private final EditMask defaultMask;

    public EditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent, final EValType type) {
        super(environment, parent);
        this.environment = environment;

        QLayout layout = null;
        switch (type) {
            /*case BOOL:
             layout = buildUiForBool();
             defaultMask = new EditMaskBool();
             break;*/
            case INT:
                layout = buildUiForInteger();
                defaultMask = new EditMaskInt();
                break;
            case NUM:
                layout = buildUiForNumber();
                defaultMask = new EditMaskNum();
                break;
            case CHAR:
                layout = buildUiForString(true);
                defaultMask = new EditMaskStr();
                ((EditMaskStr) defaultMask).setMaxLength(1);
                break;
            case STR:
                layout = buildUiForString(false);
                defaultMask = new EditMaskStr();
                break;
            case DATE_TIME:
                layout = buildUiForDatetime();
                defaultMask = new EditMaskDateTime();
                break;
            default:
                throw new IllegalArgumentException("Wrong value type " + type);
        }
        this.setLayout(layout);
        update();
    }

    public EditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent, final RadEnumPresentationDef enumDef) {
        super(environment, parent);
        this.environment = environment;

        defaultMask = new EditMaskConstSet(enumDef.getId());
        this.setLayout(buildUiForEnum(enumDef));
        update();
    }

    public EditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent, final EEditMaskType maskType) {
        super(environment, parent);
        this.environment = environment;
        //create "use default" check box
        final QVBoxLayout layout = new QVBoxLayout();
        final String chboxText = environment.getMessageProvider().translate("EditMask", "Use default mask");
        final QCheckBox useDefault = new QCheckBox(chboxText, this);
        useDefault.stateChanged.connect((QWidget) this, "onUseDefaultDateTimeMask(Integer)");
        QWidget widget = null;
        switch (maskType) {
            case DATE_TIME:
                widget = new DateTimeEditMaskEditorWidget(environment, this);
                defaultMask = new EditMaskDateTime();
                break;
            case INT:
                widget = new IntEditMaskEditorWidget(environment, this);
                defaultMask = new EditMaskInt();
                break;
            case NUM:
                widget = new NumEditMaskEditorWidget(environment, this);
                defaultMask = new EditMaskNum();
                break;
            case STR:
                widget = new StrEditMaskEditorWidget(environment, this);
                defaultMask = new EditMaskStr();
                break;
            case TIME_INTERVAL:
                widget = new TimeIntervalEditMaskEditorWidget(environment, this);
                defaultMask = new EditMaskTimeInterval(Scale.HOUR);
                break;
            case FILE_PATH:
                widget = null;
                defaultMask = new EditMaskFilePath();
                break;
            case ENUM:
                throw new UnsupportedOperationException("Unsupported for ENUM. Use newEditMaskConstSetEditorDialog(IClientEnvironment, IWidget, RadEnumPresentationDef)");
            case LIST:
                throw new UnsupportedOperationException("Unsupported for LIST. Use newEditMaskEditor(IClientEnvironment, IWidget, EValType)");
            case BOOL:
                throw new UnsupportedOperationException("Unsupported for BOOL. Use newEditMaskEditor(IClientEnvironment, IWidget, EValType)");
            case OBJECT_REFERENCE:
                throw new UnsupportedOperationException("Unsupported for OBJECT_REFERENCE. Use newEditMaskEditor(IClientEnvironment, IWidget, EValType)");
            default:
                throw new IllegalArgumentException("Unknown argument's value");

        }

        maskContainer.addWidget(widget);
        layout.addWidget(useDefault);
        layout.addLayout(maskContainer);
        this.setLayout(layout);
    }

    @Override
    public EditMask getEditMask() {
        if (isDefaultMaskUsed) {
            return defaultMask;
        }
        return ((IEditMaskEditor) getCurrentEditor()).getEditMask();
    }

    @Override
    public void setEditMask(final EditMask editMask) {
        QObject widget = null;
        if (editMask instanceof EditMaskInt) {
            widget = findChild(IntEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskTimeInterval) {
            widget = findChild(TimeIntervalEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskList) {
            widget = findChild(ListEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskNum) {
            widget = findChild(NumEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskStr) {
            widget = findChild(StrEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskDateTime) {
            widget = findChild(DateTimeEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskConstSet) {
            widget = findChild(ConstSetEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskBool) {
            widget = null;//findChild(BoolEditMaskEditorWidget.class);
        } else if (editMask instanceof EditMaskFilePath) {
            widget = null;
        } else if (editMask instanceof EditMaskRef) {
            widget = null;
        }
        setMask(widget, editMask);
    }

    @Override
    public void setHiddenOptions(final EnumSet<EEditMaskOption> options) {
        ((IEditMaskEditor) maskContainer.currentWidget()).setHiddenOptions(options);
    }

    @Override
    public void setVisibleOptions(final EnumSet<EEditMaskOption> options) {
        ((IEditMaskEditor) maskContainer.currentWidget()).setVisibleOptions(options);
    }

    @Override
    public void setEnabledOptions(final EnumSet<EEditMaskOption> options) {
        ((IEditMaskEditor) maskContainer.currentWidget()).setEnabledOptions(options);
    }

    @Override
    public void setDisabledOptions(final EnumSet<EEditMaskOption> options) {
        ((IEditMaskEditor) maskContainer.currentWidget()).setDisabledOptions(options);
    }

    @Override
    public boolean checkOptions() {
        return ((IEditMaskEditor) maskContainer.currentWidget()).checkOptions();
    }

    private QLayout buildUiForInteger() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final QLayout ltComboBox = setUpMaskTypeSelector();


        //build widgets' panel
        QWidget maskEditor = new IntEditMaskEditorWidget(environment, this);
        maskContainer.addWidget(maskEditor);
        maskEditor = new ListEditMaskEditorWidget(environment, this, EValType.INT);
        maskContainer.addWidget(maskEditor);
        maskEditor = new TimeIntervalEditMaskEditorWidget(environment, this);
        maskContainer.addWidget(maskEditor);
        maskContainer.setCurrentIndex(0);

        //set the combobox up
        String itemText = msgProvider.translate("EditMask", "Default");
        cbMaskTypeSelector.addItem(itemText);
        itemText = msgProvider.translate("EditMask", "Integer");
        cbMaskTypeSelector.addItem(itemText);
        itemText = msgProvider.translate("EditMask", "List");
        cbMaskTypeSelector.addItem(itemText);
        itemText = msgProvider.translate("EditMask", "Time interval");
        cbMaskTypeSelector.addItem(itemText);

        final QVBoxLayout resultLayout = new QVBoxLayout();
        setUpLayout(resultLayout);
        resultLayout.addLayout(ltComboBox);
        resultLayout.addLayout(maskContainer);
        cbMaskTypeSelector.currentIndexChanged.emit(0);

        return resultLayout;
    }

    private QLayout buildUiForNumber() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final QLayout ltComboBox = setUpMaskTypeSelector();


        QWidget maskEditor = new NumEditMaskEditorWidget(environment, this);
        maskContainer.addWidget(maskEditor);
        maskEditor = new ListEditMaskEditorWidget(environment, this, EValType.NUM);
        maskContainer.addWidget(maskEditor);

        String itemText = msgProvider.translate("EditMask", "Default");
        cbMaskTypeSelector.addItem(itemText);
        itemText = msgProvider.translate("EditMask", "Real number");
        cbMaskTypeSelector.addItem(itemText);
        itemText = msgProvider.translate("EditMask", "List");
        cbMaskTypeSelector.addItem(itemText);

        final QVBoxLayout resultLayout = new QVBoxLayout();
        setUpLayout(resultLayout);
        resultLayout.addLayout(ltComboBox);
        resultLayout.addLayout(maskContainer);
        cbMaskTypeSelector.currentIndexChanged.emit(0);

        return resultLayout;
    }

    private QLayout buildUiForString(final boolean isChar) {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final QLayout ltComboBox = setUpMaskTypeSelector();

        //set the stacked layout up
        QWidget maskEditor = new StrEditMaskEditorWidget(environment, this, isChar);
        maskContainer.addWidget(maskEditor);
        if (isChar) {
            maskEditor = new ListEditMaskEditorWidget(environment, maskEditor, EValType.CHAR);
        } else {
            maskEditor = new ListEditMaskEditorWidget(environment, this, EValType.STR);
        }
        maskContainer.addWidget(maskEditor);
        maskContainer.setCurrentIndex(0);

        //set the combobox up
        String itemText = msgProvider.translate("EditMask", "Default");
        cbMaskTypeSelector.addItem(itemText);
        itemText = msgProvider.translate("EditMask", "String");
        cbMaskTypeSelector.addItem(itemText);
        itemText = msgProvider.translate("EditMask", "List");
        cbMaskTypeSelector.addItem(itemText);

        final QVBoxLayout resultLayout = new QVBoxLayout();
        setUpLayout(resultLayout);
        resultLayout.addLayout(ltComboBox);
        resultLayout.addLayout(maskContainer);
        cbMaskTypeSelector.currentIndexChanged.emit(0);

        return resultLayout;
    }

    private QLayout buildUiForDatetime() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final QVBoxLayout layout = new QVBoxLayout();

        final String chboxText = msgProvider.translate("EditMask", "Use default mask");
        final QCheckBox useDefault = new QCheckBox(chboxText, this);
        useDefault.stateChanged.connect((QWidget) this, "onUseDefaultDateTimeMask(Integer)");
        final QWidget editor = new DateTimeEditMaskEditorWidget(environment, this);

        layout.addWidget(useDefault);
        maskContainer.addWidget(editor);
        layout.addLayout(maskContainer);

        return layout;
    }

    private QLayout buildUiForEnum(final RadEnumPresentationDef enumPresentation) {
        final MessageProvider msgProvider = environment.getMessageProvider();
        final QVBoxLayout layout = new QVBoxLayout();

        final String chboxText = msgProvider.translate("EditMask", "Use default mask");
        final QCheckBox useDefault = new QCheckBox(chboxText, this);
        useDefault.stateChanged.connect((QWidget) this, "onUseDefaultConstSetMask(Integer)");
        final QWidget editor = new ConstSetEditMaskEditorWidget(environment, this, enumPresentation);


        layout.addWidget(useDefault);
        maskContainer.addWidget(editor);
        layout.addLayout(maskContainer);

        return layout;
    }

    private QLayout buildUiForBool() {
        final QVBoxLayout layout = new QVBoxLayout();
        return layout;
    }

    private QWidget getCurrentEditor() {
        return maskContainer.currentWidget();
    }

    @SuppressWarnings("unused")
    private void onUseDefaultConstSetMask(final Integer stateInt) {
        final Qt.CheckState state = Qt.CheckState.resolve(stateInt);
        if (state == Qt.CheckState.Checked) {
            getCurrentEditor().setDisabled(true);
            isDefaultMaskUsed = true;
        } else {
            getCurrentEditor().setEnabled(true);
            isDefaultMaskUsed = false;
        }
    }

    @SuppressWarnings("unused")
    private void onUseDefaultDateTimeMask(final Integer stateInt) {
        final Qt.CheckState state = Qt.CheckState.resolve(stateInt);
        if (state == Qt.CheckState.Checked) {
            maskContainer.currentWidget().setDisabled(true);
            isDefaultMaskUsed = true;
        } else {
            maskContainer.currentWidget().setEnabled(true);
            isDefaultMaskUsed = false;
        }
    }

    private QLayout setUpMaskTypeSelector() {
        final MessageProvider msgProvider = environment.getMessageProvider();
        //build label+combobox 
        final QHBoxLayout layoutComboBox = new QHBoxLayout();
        setUpLayout(layoutComboBox);
        final String text = msgProvider.translate("EditMask", "Mask type:");
        final QLabel lblComboBoxText = new QLabel(text, this);
        cbMaskTypeSelector = new QComboBox(this);
        cbMaskTypeSelector.setSizeAdjustPolicy(QComboBox.SizeAdjustPolicy.AdjustToMinimumContentsLength);
        cbMaskTypeSelector.setSizePolicy(Policy.Expanding, Policy.Fixed);
        cbMaskTypeSelector.setObjectName(MASK_TYPE_SELECTOR);
        cbMaskTypeSelector.currentIndexChanged.connect((QWidget) this, "onMaskTypeChanged(Integer)");
        layoutComboBox.addWidget(lblComboBoxText);
        layoutComboBox.addWidget(cbMaskTypeSelector);
        return layoutComboBox;
    }

    @SuppressWarnings("unused")
    private void onMaskTypeChanged(final Integer index) {
        if (index == 0) {
            maskContainer.currentWidget().setDisabled(true);
            isDefaultMaskUsed = true;
        } else {
            maskContainer.setCurrentIndex(index - 1);
            maskContainer.currentWidget().setEnabled(true);
            isDefaultMaskUsed = false;
        }
    }

    private void setUpLayout(final QLayout layout) {
        layout.setMargin(0);
        layout.setAlignment(new Alignment(AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft));
    }

    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
        this.setDisabled(readOnly);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    private void setMask(final QObject object, final EditMask mask) {
        if (object != null) {
            ((IEditMaskEditor) object).setEditMask(mask);
            maskContainer.setCurrentWidget((QWidget) object);
            // set proper type in combo box if mask type selector presents
            if (cbMaskTypeSelector != null) {
                cbMaskTypeSelector.setCurrentIndex(maskContainer.currentIndex() + 1);
            }
        }
    }
}
