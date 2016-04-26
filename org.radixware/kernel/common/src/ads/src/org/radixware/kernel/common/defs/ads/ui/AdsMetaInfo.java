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

package org.radixware.kernel.common.defs.ads.ui;

import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.ui.enums.EArrayClassName;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.enums.EStandardButton;
import org.radixware.kernel.common.defs.ads.ui.rwt.*;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.ui.Connection;
import org.radixware.schemas.ui.Property;
import org.radixware.schemas.ui.QtTypesDocument;
import org.radixware.schemas.ui.QtTypesDocument.QtTypes;


public class AdsMetaInfo {

    public static final String DIALOG_CLASS = "QDialog";
    public static final String WIDGET_CLASS = "QWidget";
    public static final String HORIZONTAL_LAYOUT_CLASS = "QHBoxLayout";
    public static final String VERTICAL_LAYOUT_CLASS = "QVBoxLayout";
    public static final String GRID_LAYOUT_CLASS = "QGridLayout";
    public static final String SPACER_CLASS = "QSpacerItem";
    public static final String PUSH_BUTTON_CLASS = "QPushButton";
    public static final String TOOL_BUTTON_CLASS = "QToolButton";
    public static final String RADIO_BUTTON_CLASS = "QRadioButton";
    public static final String CHECK_BOX_CLASS = "QCheckBox";
    public static final String DIALOG_BUTTON_BOX_CLASS = "QDialogButtonBox";
    public static final String GROUP_BOX_CLASS = "QGroupBox";
    public static final String TAB_WIDGET_CLASS = "QTabWidget";
    public static final String TOOL_BAR_CLASS = "QToolBar";
    public static final String ACTION_CLASS = "QAction";
    public static final String SPLITTER_CLASS = "QSplitter";
    public static final String LABEL_CLASS = "QLabel";
    public static final String PROGRESS_BAR_CLASS = "QProgressBar";
    public static final String COMBO_BOX_CLASS = "QComboBox";
    public static final String LINE_EDIT_CLASS = "QLineEdit";
    public static final String TEXT_EDIT_CLASS = "QTextEdit";
    public static final String SPIN_BOX_CLASS = "QSpinBox";
    public static final String DOUBLE_SPIN_BOX_CLASS = "QDoubleSpinBox";
    public static final String TIME_EDIT_CLASS = "QTimeEdit";
    public static final String DATE_EDIT_CLASS = "QDateEdit";
    public static final String DATE_TIME_EDIT_CLASS = "QDateTimeEdit";
    public static final String LIST_VIEW_CLASS = "QListView";
    public static final String TREE_VIEW_CLASS = "QTreeView";
    public static final String TABLE_VIEW_CLASS = "QTableView";
    public static final String LIST_WIDGET_CLASS = "QListWidget";
    public static final String TREE_WIDGET_CLASS = "QTreeWidget";
    public static final String TABLE_WIDGET_CLASS = "QTableWidget";
    public static final String LIST_WIDGET_ITEM_CLASS = "QListWidgetItem";
    public static final String TREE_WIDGET_ITEM_CLASS = "QTreeWidgetItem";
    public static final String TABLE_WIDGET_ITEM_CLASS = "QTableWidgetItem";
    public static final String WIDGET_ITEM_CLASS = "QWidgetItem";
    public static final String PROP_LABEL_CLASS = "org.radixware.kernel.explorer.widgets.PropLabel";
    public static final String PROP_EDITOR_CLASS = "org.radixware.kernel.explorer.widgets.propeditors.PropEditor";
    public static final String COMMAND_PUSH_BUTTON_CLASS = "org.radixware.kernel.explorer.widgets.commands.CommandPushButton";
    public static final String EMBEDDED_EDITOR_CLASS = "org.radixware.kernel.explorer.widgets.EmbeddedEditor";
    public static final String EMBEDDED_SELECTOR_CLASS = "org.radixware.kernel.explorer.widgets.EmbeddedSelector";
    public static final String EDITOR_PAGE_CLASS = "org.radixware.kernel.explorer.widgets.EditorPage";
    public static final String CUSTOM_PROP_EDITOR = "org.radixware.kernel.explorer.views.PropEditorDialog";
    public static final String CUSTOM_DIALOG = "org.radixware.kernel.explorer.views.Dialog";
    public static final String CUSTOM_FORM_DIALOG = "org.radixware.kernel.explorer.views.Form";
    public static final String CUSTOM_FILTER_DIALOG = "org.radixware.kernel.explorer.views.FilterParameters";
    public static final String CUSTOM_REPORT_DIALOG = "org.radixware.kernel.explorer.views.ReportParamDialogView";
    public static final String CUSTOM_PARAG_EDITOR = "org.radixware.kernel.explorer.views.ParagraphEditor";
    public static final String CUSTOM_EDITOR_PAGE = "org.radixware.kernel.explorer.views.EditorPageView";
    public static final String CUSTOM_EDITOR = "org.radixware.kernel.explorer.views.Editor";
    public static final String CUSTOM_SELECTOR = "org.radixware.kernel.explorer.views.selector.Selector";
    public static final String CUSTOM_WIDGET = "org.radixware.kernel.explorer.views.CustomWidget";
    public static final String ADVANCED_SPLITTER_CLASS = "org.radixware.kernel.explorer.views.Splitter";
    public static final String FRAME_CLASS = "QFrame";
    public static final String STACKED_WIDGET_CLASS = "QStackedWidget";
    public static final String SCROLL_AREA_CLASS = "QScrollArea";
    public static final String COMMAND_TOOL_BUTTON_CLASS = "org.radixware.kernel.explorer.widgets.commands.CommandToolButton";
    public static final String VAL_BIN_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValBinEditor";
    public static final String VAL_BOOL_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor";
    public static final String VAL_CHAR_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValCharEditor";
    public static final String VAL_CONST_SET_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValConstSetEditor";
    public static final String VAL_DATE_TIME_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValDateTimeEditor";
    public static final String VAL_INT_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValIntEditor";
    public static final String VAL_LIST_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValListEditor";
    public static final String VAL_NUM_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValNumEditor";
    public static final String VAL_STR_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValStrEditor";
    public static final String VAL_TIMEINTERVAL_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValTimeIntervalEditor";
    public static final String VAL_REF_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValRefEditor";
    public static final String VAL_FILE_PATH_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors.ValFilePathEditor";
    public static final String VAL_EDITOR_CLASS = "org.radixware.kernel.explorer.editors.valeditors";
    //
    //
    //                           RWT class names
    //
    //
    public static final String RWT_UI_OBJECT = "org.radixware.wps.rwt.UIObject";
    public static final String RWT_UI_DIALOG = "org.radixware.wps.rwt.Dialog";
    public static final String RWT_UI_BUTTON_BOX = "org.radixware.wps.rwt.ButtonBox";
    public static final String RWT_UI_BUTTON_BASE = "org.radixware.wps.rwt.ButtonBase";
    public static final String RWT_UI_PUSH_BUTTON = "org.radixware.wps.rwt.PushButton";
    public static final String RWT_UI_TEXT_FIELD = "org.radixware.wps.rwt.TextField";
    public static final String RWT_UI_FILE_INPUT = "org.radixware.wps.rwt.FileInput";
    public static final String RWT_UI_TEXT_AREA = "org.radixware.wps.rwt.TextArea";
    public static final String RWT_UI_INPUT_BOX = "org.radixware.wps.rwt.InputBox";
    public static final String RWT_UI_LABEL = "org.radixware.wps.rwt.Label";
    public static final String RWT_UI_CONTAINER = "org.radixware.wps.rwt.Container";
    public static final String RWT_UI_ABSTRACT_CONTAINER = "org.radixware.wps.rwt.AbstractContainer";
    public static final String RWT_UI_PANEL = "org.radixware.wps.rwt.Panel";
    public static final String RWT_UI_GROUP_BOX = "org.radixware.wps.rwt.GroupBox";
    public static final String RWT_UI_GRID_BOX_CONTAINER = "org.radixware.wps.rwt.GridBoxContainer";
    public static final String RWT_EDITOR_PAGE = "org.radixware.wps.views.editor.EditorPage";
    public static final String RWT_UI_TREE = "org.radixware.wps.rwt.tree.Tree";
    public static final String RWT_UI_LIST = "org.radixware.wps.rwt.ListBox";
    public static final String RWT_UI_CHECK_BOX = "org.radixware.wps.rwt.CheckBox";
    public static final String RWT_UI_RADIO_BUTTON = "org.radixware.wps.rwt.RadioButton";
    public static final String RWT_UI_GRID = "org.radixware.wps.rwt.Grid";
    public static final String RWT_TOOL_BAR = "org.radixware.wps.rwt.ToolBar";
    public static final String RWT_TAB_SET = "org.radixware.wps.rwt.TabLayout";
    public static final String RWT_SPLITTER = "org.radixware.wps.rwt.Splitter";
    public static final String RWT_TAB_SET_TAB = "org.radixware.wps.rwt.TabLayout.Tab";
    public static final String RWT_LABELED_EDIT_GRID = "org.radixware.wps.rwt.LabeledEditGrid";
    //                          RADIX WIDGETS
    public static final String RWT_PROP_EDITOR_DIALOG = "org.radixware.wps.views.dialog.RwtPropEditorDialog";
    public static final String RWT_CUSTOM_DIALOG = "org.radixware.wps.views.dialog.RwtDialog";
    public static final String RWT_CUSTOM_WIDGET = "org.radixware.wps.views.dialog.RwtCustomWidget";
    public static final String RWT_FORM_DIALOG = AdsRwtCustomFormDialogDef.PLATFORM_CLASS_NAME_STR;
    public static final String RWT_FILTER_DIALOG = AdsRwtCustomFilterDialogDef.PLATFORM_CLASS_NAME_STR;
    public static final String RWT_CUSTOM_EDITOR_PAGE = AdsRwtCustomPageEditorDef.PLATFORM_CLASS_NAME_STR;
    public static final String RWT_PARAG_EDITOR = AdsRwtCustomParagEditorDef.PLATFORM_CLASS_NAME_STR;
    public static final String RWT_CUSTOM_FILTER_DIALOG = AdsRwtCustomFilterDialogDef.PLATFORM_CLASS_NAME_STR;
    public static final String RWT_CUSTOM_REPORT_DIALOG = AdsRwtCustomReportDialogDef.PLATFORM_CLASS_NAME_STR;
    public static final String RWT_CUSTOM_EDITOR = "org.radixware.wps.views.editor.CustomEditor";
    public static final String RWT_CUSTOM_SELECTOR = "org.radixware.wps.views.editor.RwtSelector";
    public static final String RWT_EMBEDDED_EDITOR = "org.radixware.wps.views.editor.EmbeddedEditor";
    public static final String RWT_EMBEDDED_SELECTOR = "org.radixware.wps.views.selector.EmbeddedSelector";
    public static final String RWT_LABELED_EDIT_GRID_ELEMENT = "LabeledEditGridItem";
    public static final String RWT_GRID_ELEMENT = "GridItem";
    public static final String RWT_GRID_BOX_ELEMENT = "GridBoxItem";
    public static final String RWT_EDIT_GRID_ELEMENT = "EditGridItem";
    public static final String RWT_BOX_ELEMENT = "RwtBoxElement";
    public static final String RWT_ANCHOR_CONTAINER_ELEMENT = "AnchorContainerItem";
    public static final String RWT_PROPERTIES_GRID = "org.radixware.wps.views.editor.PropertiesGrid";
    public static final String RWT_PROP_EDITOR = " org.radixware.wps.views.editor.property.PropEditor";
    public static final String RWT_PROP_LABEL = " org.radixware.wps.views.editor.property.PropLabel";
    public static final String RWT_VERTICAL_BOX_CONTAINER = "org.radixware.wps.rwt.VerticalBoxContainer";
    public static final String RWT_HORIZONTAL_BOX_CONTAINER = "org.radixware.wps.rwt.HorizontalBoxContainer";
    public static final String RWT_COMMAND_PUSH_BUTTON = "org.radixware.wps.views.CommandPushButton";
    //                         Value editors
    public static final String RWT_VAL_EDITOR = "AbstractValEditor";
    public static final String RWT_VAL_STR_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<String,org.radixware.kernel.common.client.meta.mask.EditMaskStr>";
    public static final String RWT_VAL_INT_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<Long,org.radixware.kernel.common.client.meta.mask.EditMaskInt>";
    public static final String RWT_VAL_NUM_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<java.math.BigDecimal,org.radixware.kernel.common.client.meta.mask.EditMaskNum>";
    public static final String RWT_VAL_TIME_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<Long,org.radixware.kernel.common.client.meta.mask.EditMaskDateTime>";
    public static final String RWT_VAL_BIN_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<org.radixware.kernel.common.types.Bin,org.radixware.kernel.common.client.meta.mask.EditMaskNone>";
    public static final String RWT_VAL_ENUM_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<T,org.radixware.kernel.common.client.meta.mask.EditMaskConstSet>";
    public static final String RWT_VAL_LIST_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<T,org.radixware.kernel.common.client.meta.mask.EditMaskList>";
    public static final String RWT_VAL_REF_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<org.radixware.kernel.common.client.types.Reference,org.radixware.kernel.common.client.meta.mask.EditMaskRef>";
    public static final String RWT_VAL_TIME_INTERVAL_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<Long,org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval>";
    public static final String RWT_VAL_ARR_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<T,org.radixware.kernel.common.client.meta.mask.EditMaskNone>";
    public static final String RWT_VAL_DATE_TIME_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<java.sql.Timestamp,org.radixware.kernel.common.client.meta.mask.EditMaskDateTime>";
    public static final String RWT_VAL_FILE_PATH_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<String,org.radixware.kernel.common.client.meta.mask.EditMaskFilePath>";
    public static final String RWT_VAL_BOOL_EDITOR = "org.radixware.wps.views.editors.valeditors.IValEditor<Boolean, org.radixware.kernel.common.client.meta.mask.EditMaskNone>";
    //actions
    public static final String RWT_ACTION = "org.radixware.wps.views.RwtAction";
    private static final HashMap<String, String> clazzInh = new HashMap<>();
    private static final HashMap<String, List<AdsUIProperty>> clazzProps = new HashMap<>();
    private static final HashMap<String, List<AdsUIConnection>> clazzConns = new HashMap<>();

    static {
        final InputStream stream = AdsWidgetDef.class.getClassLoader().getResourceAsStream("org/radixware/kernel/common/defs/ads/ui/qttypes.xml");
        if (stream != null) {
            try {
                final QtTypesDocument xml = QtTypesDocument.Factory.parse(stream);
                for (QtTypes.Widget w : xml.getQtTypes().getWidgetList()) {
                    if (w.isSetExtends()) {
                        clazzInh.put(w.getName(), w.getExtends());
                    }
                    final List<AdsUIProperty> props = new ArrayList<>();
                    for (Property p : w.getPropertyList()) {
                        props.add(AdsUIProperty.Factory.loadFrom(p));
                    }
                    clazzProps.put(w.getName(), props);
                    final List<AdsUIConnection> conns = new ArrayList<>();
                    for (Connection c : w.getConnectionList()) {
                        conns.add(new AdsUIConnection(c));
                    }
                    clazzConns.put(w.getName(), conns);

                }
                List<AdsUIProperty> baseProps = clazzProps.get(WIDGET_CLASS);
                if (baseProps != null) {
                    baseProps.add(new AdsUIProperty.AccessProperty(null));
                }
            } catch (Throwable ex) {
                Logger.getLogger(AdsMetaInfo.class.getName()).log(Level.FINE, "UI types load error", ex);
            }
        }

        //preparations for web props
        //UIObject
        clazzInh.put(RWT_ANCHOR_CONTAINER_ELEMENT, null);
        List<AdsUIProperty> props = new ArrayList<>();
        props.add(new AdsUIProperty.AnchorProperty("anchor"));
        clazzProps.put(RWT_ANCHOR_CONTAINER_ELEMENT, props);



        props = new ArrayList<>();
        props.add(new AdsUIProperty.IntProperty("gridColumn", 0));
        props.add(new AdsUIProperty.IntProperty("gridRow", -1));
        props.add(new AdsUIProperty.IntProperty("colSpan", 1));
        clazzInh.put(RWT_EDIT_GRID_ELEMENT, RWT_ANCHOR_CONTAINER_ELEMENT);
        clazzProps.put(RWT_EDIT_GRID_ELEMENT, props);
        clazzInh.put(RWT_LABELED_EDIT_GRID_ELEMENT, RWT_EDIT_GRID_ELEMENT);

        props = new ArrayList<>();
        props.add(new AdsUIProperty.LocalizedStringRefProperty("label"));
        clazzProps.put(RWT_LABELED_EDIT_GRID_ELEMENT, props);
        //  clazzInh.put(RWT_UI_OBJECT, RWT_LABELED_EDIT_GRID_ELEMENT);



        props = new ArrayList<>();
        props.add(new AdsUIProperty.BooleanProperty("expand", false));
        clazzProps.put(RWT_BOX_ELEMENT, props);

        clazzInh.put(RWT_BOX_ELEMENT, RWT_LABELED_EDIT_GRID_ELEMENT);

        //clazzInh.put(RWT_UI_OBJECT, RWT_BOX_ELEMENT);


        props = new ArrayList<>();
        props.add(new AdsUIProperty.StringProperty("objectName", "widget"));
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 100));
//        props.add(new AdsUIProperty.BooleanProperty("useHCoverage", false));
//        props.add(new AdsUIProperty.BooleanProperty("useVCoverage", false));
//        props.add(new AdsUIProperty.FloatProperty("hCoverage", 100));
//        props.add(new AdsUIProperty.FloatProperty("vCoverage", 100));
        props.add(new AdsUIProperty.BooleanProperty("enabled", true));



        List<AdsUIConnection> connections = new LinkedList<>();
        clazzProps.put(RWT_UI_OBJECT, props);


        clazzInh.put(RWT_UI_CHECK_BOX, RWT_UI_BUTTON_BASE);
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("onSelectionChange(org.radixware.wps.rwt.CheckBox checkBox)", "org.radixware.wps.rwt.CheckBox.SelectionStateListener", "addSelectionStateListener", "void"));
        props = new ArrayList<>();
        props.add(new AdsUIProperty.BooleanProperty("selected", false));
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 20));
        clazzConns.put(RWT_UI_CHECK_BOX, connections);
        clazzProps.put(RWT_UI_CHECK_BOX, props);
        clazzInh.put(RWT_UI_RADIO_BUTTON, RWT_UI_CHECK_BOX);


        clazzInh.put(RWT_UI_ABSTRACT_CONTAINER, RWT_UI_OBJECT);
        clazzInh.put(RWT_UI_CONTAINER, RWT_UI_ABSTRACT_CONTAINER);
        clazzInh.put(RWT_VERTICAL_BOX_CONTAINER, RWT_UI_ABSTRACT_CONTAINER);
        clazzInh.put(RWT_HORIZONTAL_BOX_CONTAINER, RWT_UI_ABSTRACT_CONTAINER);


        clazzInh.put(RWT_UI_GROUP_BOX, RWT_UI_CONTAINER);

        props = new ArrayList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 50, 20));
        props.add(new AdsUIProperty.LocalizedStringRefProperty("text"));
        clazzProps.put(RWT_UI_LABEL, props);
        clazzInh.put(RWT_UI_LABEL, RWT_UI_OBJECT);

        //grid box container
        props = new ArrayList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 120, 120));
        clazzInh.put(RWT_UI_GRID_BOX_CONTAINER, RWT_UI_OBJECT);
        clazzProps.put(RWT_UI_GRID_BOX_CONTAINER, props);

        props = new ArrayList<>();
        props.add(new AdsUIProperty.IntProperty("row", -1));
        props.add(new AdsUIProperty.IntProperty("col", -1));
        props.add(new AdsUIProperty.IntProperty("rowSpan", 1));
        props.add(new AdsUIProperty.IntProperty("colSpan", 1));
        clazzInh.put(RWT_GRID_BOX_ELEMENT, RWT_BOX_ELEMENT);
        clazzProps.put(RWT_GRID_BOX_ELEMENT, props);
        clazzInh.put(RWT_GRID_ELEMENT, RWT_GRID_BOX_ELEMENT);
        clazzInh.put(RWT_UI_OBJECT, RWT_GRID_ELEMENT);
        //grid box container

        //page editor
        props = new ArrayList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 200, 200));
        props.add(new AdsUIProperty.StringProperty("objectName", "editorPage"));
        props.add(new AdsUIProperty.EditorPageRefProperty("editorPage", null));
        clazzInh.put(RWT_EDITOR_PAGE, RWT_UI_OBJECT);
        clazzProps.put(RWT_EDITOR_PAGE, props);
        //end of page editor

        props = new ArrayList<>();
        props.add(new AdsUIProperty.LocalizedStringRefProperty("text"));
        props.add(new AdsUIProperty.ImageProperty("icon"));
        props.add(new AdsUIProperty.SizeProperty("iconSize", 13, 13));

        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("onClick(org.radixware.kernel.common.client.widgets.IButton button)", "org.radixware.kernel.common.client.widgets.IButton.ClickHandler", "addClickHandler", "void"));
        clazzConns.put(RWT_UI_BUTTON_BASE, connections);
        clazzProps.put(RWT_UI_BUTTON_BASE, props);
        clazzInh.put(RWT_UI_BUTTON_BASE, RWT_UI_OBJECT);


        props = new ArrayList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 20));
        clazzProps.put(RWT_UI_PUSH_BUTTON, props);
        clazzInh.put(RWT_UI_PUSH_BUTTON, RWT_UI_BUTTON_BASE);

        props = new ArrayList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 20));
        props.add(new AdsUIProperty.LocalizedStringRefProperty("text"));
        props.add(new AdsUIProperty.BooleanProperty("password", false));
        clazzProps.put(RWT_UI_TEXT_FIELD, props);
        clazzInh.put(RWT_UI_TEXT_FIELD, RWT_UI_OBJECT);


        props = new ArrayList<>();
        props.add(new AdsUIProperty.StringProperty("text"));
        clazzProps.put(RWT_UI_TEXT_AREA, props);
        clazzInh.put(RWT_UI_TEXT_AREA, RWT_UI_OBJECT);

        props = new ArrayList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 20));
        props.add(new AdsUIProperty.StringProperty("value", ""));
        clazzProps.put(RWT_UI_INPUT_BOX, props);
        clazzInh.put(RWT_UI_INPUT_BOX, RWT_UI_OBJECT);
        //toolbar
        props = new ArrayList<>();
        props.add(new AdsUIProperty.IdListProperty("actions"));
        props.add(new AdsUIProperty.EnumValueProperty("orientation", EOrientation.Horizontal.getValue()));
        clazzProps.put(RWT_TOOL_BAR, props);
        clazzInh.put(RWT_TOOL_BAR, RWT_UI_OBJECT);
        //toolbar
        //dialog
        props = new ArrayList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 200, 200));
        props.add(new AdsUIProperty.LocalizedStringRefProperty("windowTitle"));
        props.add(new AdsUIProperty.BooleanProperty("buttonBoxVisible", true));
        props.add(new AdsUIProperty.SetProperty("standardButtons", EStandardButton.Ok.getValue() + "|" + EStandardButton.Cancel.getValue()));
        clazzProps.put(RWT_UI_DIALOG, props);
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("onClose(org.radixware.kernel.common.enums.EDialogButtonType closeButtonType,org.radixware.kernel.common.client.views.IDialog.DialogResult closeResult)", "org.radixware.wps.rwt.Dialog.CloseButtonListener", "addCloseButtonListener", "void"));
        connections.add(new AdsUIConnection("beforeClose(org.radixware.kernel.common.enums.EDialogButtonType closeButtonType,org.radixware.kernel.common.client.views.IDialog.DialogResult closeResult)", "org.radixware.wps.rwt.Dialog.BeforeCloseButtonListener", "addBeforeCloseListener", "boolean"));
        connections.add(new AdsUIConnection("onApply(org.radixware.kernel.common.enums.EDialogButtonType applyButtonType)", "org.radixware.wps.rwt.Dialog.ApplyButtonListener", "addApplyButtonListener", "void"));
        connections.add(new AdsUIConnection("onAction(String actionName)", "org.radixware.wps.rwt.Dialog.CustomActionListener", "addCustomActionListener", "void"));
        clazzConns.put(RWT_UI_DIALOG, connections);
        clazzInh.put(RWT_UI_DIALOG, RWT_UI_OBJECT);
        //dialog
        //tab set
        props = new ArrayList<>();
        props.add(new AdsUIProperty.IntProperty("currentIndex", -1));


        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("onCurrentTabChange(org.radixware.wps.rwt.TabLayout.Tab oldCurrentTab, org.radixware.wps.rwt.TabLayout.Tab newCurrentTab)", "org.radixware.wps.rwt.TabLayout.TabListener", "addTabListener", "void"));
        clazzConns.put(RWT_TAB_SET, connections);
        clazzProps.put(RWT_TAB_SET, props);
        clazzInh.put(RWT_TAB_SET, RWT_UI_OBJECT);

        props = new ArrayList<>();
        props.add(new AdsUIProperty.LocalizedStringRefProperty("title"));
        props.add(new AdsUIProperty.ImageProperty("icon"));
        clazzProps.put(RWT_TAB_SET_TAB, props);
        clazzInh.put(RWT_TAB_SET_TAB, RWT_UI_CONTAINER);
        //tab set
        //splitter
        props = new ArrayList<>();
        props.add(new AdsUIProperty.EnumValueProperty("orientation", EOrientation.Horizontal.getValue()));
        clazzProps.put(RWT_SPLITTER, props);
        clazzInh.put(RWT_SPLITTER, RWT_UI_OBJECT);
        //splitter
        //tree
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("selectionChanged(org.radixware.wps.rwt.tree.Node,org.radixware.wps.rwt.tree.Node node)", "org.radixware.wps.rwt.tree.Tree.NodeListener", "addSelectionListener", "void"));
        clazzConns.put(RWT_UI_TREE, connections);
        clazzInh.put(RWT_UI_TREE, RWT_UI_OBJECT);
        //tree
        //list
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("currentItemChanged(org.radixware.wps.rwt.ListBox.ListBoxItem item)", "org.radixware.wps.rwt.ListBox.CurrentItemListener", "addCurrentItemListener", "void"));
        connections.add(new AdsUIConnection("itemDoubleClick(org.radixware.wps.rwt.ListBox.ListBoxItem item)", "org.radixware.wps.rwt.ListBox.DoubleClickListener", "addDoubleClickListener", "void"));
        clazzConns.put(RWT_UI_LIST, connections);
        clazzInh.put(RWT_UI_LIST, RWT_UI_OBJECT);
        //list
        //grid
        connections = new LinkedList<>();
        //connections.add(new AdsUIConnection("rowSelectionChanged(org.radixware.wps.rwt.Grid.Row oldSelectedRow ,org.radixware.wps.rwt.Grid.Row newSelectedRow)", "org.radixware.wps.rwt.Grid.UnconditionalRowSelectionListener", "addSelectionListener", "void"));
        connections.add(new AdsUIConnection("currentRowChanged(org.radixware.wps.rwt.Grid.Row oldSelectedRow ,org.radixware.wps.rwt.Grid.Row newSelectedRow)", "org.radixware.wps.rwt.Grid.UnconditionalRowSelectionListener", "addCurrentRowListener", "void"));
        clazzConns.put(RWT_UI_GRID, connections);
        clazzInh.put(RWT_UI_GRID, RWT_UI_OBJECT);
        //grid

        //labeled edit grid
        clazzInh.put(RWT_LABELED_EDIT_GRID, RWT_UI_OBJECT);
        //labeled edit grid


        clazzInh.put(RWT_PROP_EDITOR_DIALOG, RWT_CUSTOM_DIALOG);
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("opened()", "org.radixware.wps.views.dialog.RwtDialog.RwtDialogListener", "addDialogListener", "void"));
        clazzConns.put(RWT_CUSTOM_DIALOG, connections);
        clazzInh.put(RWT_CUSTOM_DIALOG, RWT_UI_DIALOG);



        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("opened(org.radixware.kernel.common.client.widgets.IWidget content)", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("closed()", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("entityRemoved()", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("entityUpdated()", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("entitiesCreated(java.util.List<org.radixware.kernel.common.client.models.EntityModel> entities)", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("modifiedStateChanged(boolean modified)", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("insertedIntoTree(org.radixware.kernel.common.client.views.IExplorerItemView view)", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("onDeleteAll()", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("afterReread(org.radixware.kernel.common.client.types.Pid pid)", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        connections.add(new AdsUIConnection("onShowException(java.lang.Throwable ex)", "org.radixware.wps.views.selector.RwtSelector.SelectorListenerAdapter", "addSelectorListener", "void"));
        clazzConns.put(RWT_CUSTOM_SELECTOR, connections);
        clazzInh.put(RWT_CUSTOM_SELECTOR, RWT_UI_OBJECT);
        //custom editor
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("afterOpen()", "org.radixware.kernel.common.client.views.IEditor.OpenHandler", "addOpenHandler", "void"));
        connections.add(new AdsUIConnection("onClose()", "org.radixware.kernel.common.client.views.IEditor.CloseHandler", "addCloseHandler", "void"));
        clazzConns.put(RWT_CUSTOM_EDITOR, connections);
        clazzInh.put(RWT_CUSTOM_EDITOR, RWT_UI_OBJECT);
        //custom editor
        //form widget
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("opened(org.radixware.kernel.common.client.views.IFormView form)", "org.radixware.wps.views.dialog.RwtForm.RwtFormListener", "addFormListener", "void"));
        connections.add(new AdsUIConnection("closed(org.radixware.kernel.common.client.views.IFormView form)", "org.radixware.wps.views.dialog.RwtForm.RwtFormListener", "addFormListener", "void"));
        clazzConns.put(RWT_FORM_DIALOG, connections);
        clazzInh.put(RWT_FORM_DIALOG, RWT_UI_DIALOG);
        //form widget

        clazzInh.put(RWT_CUSTOM_REPORT_DIALOG, RWT_UI_DIALOG);


        //paragraph editor
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("opened()", "org.radixware.wps.views.paragraph.CustomParagEditor.RwtParagraphEditorListener", "addParagraphListener", "void"));
        clazzConns.put(RWT_PARAG_EDITOR, connections);
        clazzInh.put(RWT_PARAG_EDITOR, RWT_UI_OBJECT);
        //paragraph editor


        //filter editor
        connections = new LinkedList<>();

        connections.add(new AdsUIConnection("opened(org.radixware.wps.views.selector.FilterParameters filterParameters)", "org.radixware.kernel.common.client.views.IView.IViewListener<org.radixware.wps.views.selector.FilterParameters>", "addViewListener", "void"));
        clazzConns.put(RWT_CUSTOM_FILTER_DIALOG, connections);
        clazzInh.put(RWT_CUSTOM_FILTER_DIALOG, RWT_UI_OBJECT);
        //paragraph editor

        //custom widget
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("opened(" + RWT_CUSTOM_WIDGET + " widget)", "org.radixware.wps.views.dialog.RwtCustomWidget.RwtCustomWidgetListener", "addCustomWidgetListener", "void"));
        clazzConns.put(RWT_CUSTOM_WIDGET, connections);
        clazzInh.put(RWT_CUSTOM_WIDGET, RWT_UI_CONTAINER);
        //custom widget

        //prop editor
        props = new LinkedList<>();
        props.add(new AdsUIProperty.PropertyRefProperty("property", null));
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 20));
        clazzProps.put(RWT_PROP_EDITOR, props);
        clazzInh.put(RWT_PROP_EDITOR, RWT_UI_OBJECT);
        //prop editor

        //prop label
        props = new LinkedList<>();
        props.add(new AdsUIProperty.PropertyRefProperty("property", null));
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 70, 20));
        clazzProps.put(RWT_PROP_LABEL, props);
        clazzInh.put(RWT_PROP_LABEL, RWT_UI_OBJECT);
        //prop label




        //properties grid

        clazzInh.put(RWT_PROPERTIES_GRID, RWT_UI_OBJECT);
        //properties grid

        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("opened(org.radixware.kernel.common.client.views.IEditorPageView view)", "org.radixware.wps.views.editor.EditorPageView.EditorPageOpenHandler", "addEditorPageListener", "void"));
        connections.add(new AdsUIConnection("closed(org.radixware.kernel.common.client.views.IEditorPageView view)", "org.radixware.wps.views.editor.EditorPageView.EditorPageCloseHandler", "addEditorPageListener", "void"));

        clazzConns.put(RWT_CUSTOM_EDITOR_PAGE, connections);
        clazzInh.put(RWT_CUSTOM_EDITOR_PAGE, RWT_UI_CONTAINER);

        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("fileSelected(String fileName)", RWT_UI_FILE_INPUT + ".FileListener", "addFileListener", "void"));
        clazzConns.put(RWT_UI_FILE_INPUT, connections);
        clazzInh.put(RWT_UI_FILE_INPUT, RWT_UI_OBJECT);

        props = new LinkedList<>();

        props.add(new AdsUIProperty.CommandRefProperty("command", null));
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 20));
        props.add(new AdsUIProperty.BooleanProperty("useCommandTitle", true));
        clazzProps.put(RWT_COMMAND_PUSH_BUTTON, props);
        clazzInh.put(RWT_COMMAND_PUSH_BUTTON, RWT_UI_BUTTON_BASE);


        props = new ArrayList<>();
        props.add(new AdsUIProperty.StringProperty("objectName", "action"));
        props.add(new AdsUIProperty.ImageProperty("icon"));
        props.add(new AdsUIProperty.LocalizedStringRefProperty("text"));
        props.add(new AdsUIProperty.LocalizedStringRefProperty("toolTip"));
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 50, 50));
        clazzProps.put(RWT_ACTION, props);
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("triggered(org.radixware.kernel.common.client.widgets.actions.Action action)", "org.radixware.kernel.common.client.widgets.actions.Action.ActionListener", "addActionListener", "void"));
        connections.add(new AdsUIConnection("changed(org.radixware.kernel.common.client.widgets.actions.Action action)", "org.radixware.kernel.common.client.widgets.actions.Action.ActionStateListener", "addActionStateListener", "void"));
        connections.add(new AdsUIConnection("toggled(org.radixware.kernel.common.client.widgets.actions.Action action,boolean toggledin)", "org.radixware.kernel.common.client.widgets.actions.Action.ActionToggleListener", "addActionToggleListener", "void"));
        clazzConns.put(RWT_ACTION, connections);

        clazzInh.put(RWT_ACTION, null);

        props = new LinkedList<>();
        props.add(new AdsUIProperty.EmbeddedEditorOpenParamsProperty("openParams"));
        props.add(new AdsUIProperty.BooleanProperty("synchronizedWithParentView", false));
        props.add(new AdsUIProperty.BooleanProperty("readOnly", false));
        clazzProps.put(RWT_EMBEDDED_EDITOR, props);
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("afterOpen()", "org.radixware.kernel.common.client.views.IEditor.OpenHandler", "addOpenHandler", "void"));
        connections.add(new AdsUIConnection("onClose()", "org.radixware.kernel.common.client.views.IEditor.CloseHandler", "addCloseHandler", "void"));
        clazzConns.put(RWT_EMBEDDED_EDITOR, connections);

        clazzInh.put(RWT_EMBEDDED_EDITOR, RWT_UI_OBJECT);

        props = new LinkedList<>();
        props.add(new AdsUIProperty.EmbeddedSelectorOpenParamsProperty("openParams"));
        props.add(new AdsUIProperty.BooleanProperty("synchronizedWithParentView", false));
        props.add(new AdsUIProperty.BooleanProperty("readOnly", false));
        clazzProps.put(RWT_EMBEDDED_SELECTOR, props);
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("onSetCurrentEntity(org.radixware.kernel.common.client.models.EntityModel entity)", "org.radixware.wps.views.selector.RwtSelector.CurrentEntitySetAdapter", "addCurrentEntityHandler", "void"));
        connections.add(new AdsUIConnection("onLeaveCurrentEntity()", "org.radixware.wps.views.selector.RwtSelector.CurrentEntityLeaveAdapter", "addCurrentEntityHandler", "void"));
        clazzConns.put(RWT_EMBEDDED_SELECTOR, connections);
        clazzInh.put(RWT_EMBEDDED_SELECTOR, RWT_UI_OBJECT);

        // val editors
        clazzInh.put(RWT_VAL_EDITOR, RWT_UI_OBJECT);
        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("onValueChanged(java.lang.Object oldValue,java.lang.Object newValue)", "org.radixware.wps.rwt.ValueEditor.ValueChangeListener", "addValueChangeListener", "void"));
        clazzConns.put(RWT_VAL_EDITOR, connections);


        props = new LinkedList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 100, 20));
        clazzProps.put(RWT_VAL_EDITOR, props);
        clazzInh.put(RWT_VAL_STR_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_INT_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_NUM_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_TIME_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_BIN_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_FILE_PATH_EDITOR, RWT_VAL_EDITOR);
        props = new LinkedList<>();
        props.add(new AdsUIProperty.EnumRefProperty("enumeration", null));
        clazzProps.put(RWT_VAL_ENUM_EDITOR, props);
        clazzInh.put(RWT_VAL_ENUM_EDITOR, RWT_VAL_EDITOR);
        props = new LinkedList<>();
        props.add(new AdsUIProperty.TypeDeclarationProperty("itemType"));
        clazzProps.put(RWT_VAL_LIST_EDITOR, props);
        clazzInh.put(RWT_VAL_LIST_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_REF_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_TIME_INTERVAL_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_DATE_TIME_EDITOR, RWT_VAL_EDITOR);
        clazzInh.put(RWT_VAL_BOOL_EDITOR, RWT_VAL_EDITOR);

        props = new LinkedList<>();
        props.add(new AdsUIProperty.EnumValueProperty("arrayType", EArrayClassName.ARR_INT.getValue()));
        clazzProps.put(RWT_VAL_ARR_EDITOR, props);

        clazzInh.put(RWT_VAL_ARR_EDITOR, RWT_VAL_EDITOR);


        connections = new LinkedList<>();
        connections.add(new AdsUIConnection("onClick(org.radixware.kernel.common.client.widgets.IButton button)", "org.radixware.kernel.common.client.widgets.IButton.ClickHandler", "addClickHandler", "void"));
        clazzConns.put(RWT_UI_BUTTON_BOX, connections);
        props = new LinkedList<>();
        props.add(new AdsUIProperty.RectProperty("geometry", 0, 0, 200, 25));
        props.add(new AdsUIProperty.SetProperty("standardButtons", EStandardButton.Ok.getValue() + "|" + EStandardButton.Cancel.getValue()));
        props.add(new AdsUIProperty.IdListProperty("actions"));
        clazzProps.put(RWT_UI_BUTTON_BOX, props);
        clazzInh.put(RWT_UI_BUTTON_BOX, RWT_UI_OBJECT);

    }

    public static String getExtends(String clazz, RadixObject context) {
        if (AdsUIUtil.isCustomWidget(clazz)) {
            if (context instanceof AdsDefinition) {
                if (((AdsDefinition) context).getUsageEnvironment() == ERuntimeEnvironmentType.WEB) {
                    return RWT_CUSTOM_WIDGET;
                }
            }
            return WIDGET_CLASS;
        }
        return clazzInh.get(clazz);
    }

    public static String getExtendsForProp(String clazz, RadixObject context) {
        if (AdsUIUtil.isCustomWidget(clazz)) {
            if (context instanceof AdsDefinition) {
                if (((AdsDefinition) context).getUsageEnvironment() == ERuntimeEnvironmentType.WEB) {
                    return RWT_CUSTOM_WIDGET;
                }
            }
            return WIDGET_CLASS;
        }
        String superClass = clazzInh.get(clazz);

        if (AdsMetaInfo.RWT_GRID_ELEMENT.equals(superClass)) {
            AdsRwtWidgetDef w = (AdsRwtWidgetDef) context;
            AdsRwtWidgetDef owner = w.getOwnerWidget();
            if (owner == null) {
                superClass = clazzInh.get(AdsMetaInfo.RWT_GRID_BOX_ELEMENT);
            } else {
                switch (owner.getClassName()) {
                    case AdsMetaInfo.RWT_UI_GRID_BOX_CONTAINER:
                        break;
                    default:
                        superClass = clazzInh.get(AdsMetaInfo.RWT_GRID_BOX_ELEMENT);
                        break;
                }
            }
        }

        if (AdsMetaInfo.RWT_BOX_ELEMENT.equals(superClass)) {
            AdsRwtWidgetDef w = (AdsRwtWidgetDef) context;
            AdsRwtWidgetDef owner = w.getOwnerWidget();
            if (owner == null) {
                superClass = clazzInh.get(AdsMetaInfo.RWT_BOX_ELEMENT);
            } else {
                if (!AdsMetaInfo.RWT_VERTICAL_BOX_CONTAINER.equals(owner.getClassName()) && !AdsMetaInfo.RWT_HORIZONTAL_BOX_CONTAINER.equals(owner.getClassName())) {
                    superClass = clazzInh.get(AdsMetaInfo.RWT_BOX_ELEMENT);
                }
            }
        }

        if (AdsMetaInfo.RWT_LABELED_EDIT_GRID_ELEMENT.equals(superClass)) {
            AdsRwtWidgetDef w = (AdsRwtWidgetDef) context;
            AdsRwtWidgetDef owner = w.getOwnerWidget();
            if (owner == null) {
                superClass = clazzInh.get(AdsMetaInfo.RWT_EDIT_GRID_ELEMENT);
            } else {
                switch (owner.getClassName()) {
                    case AdsMetaInfo.RWT_LABELED_EDIT_GRID:
                        break;
                    case AdsMetaInfo.RWT_PROPERTIES_GRID:
                        superClass = clazzInh.get(AdsMetaInfo.RWT_LABELED_EDIT_GRID_ELEMENT);
                        break;
                    default:
                        superClass = clazzInh.get(AdsMetaInfo.RWT_EDIT_GRID_ELEMENT);
                        break;
                }
            }
        }

        if (AdsMetaInfo.RWT_ANCHOR_CONTAINER_ELEMENT.equals(superClass)) {
            AdsRwtWidgetDef w = (AdsRwtWidgetDef) context;
            AdsRwtWidgetDef owner = w.getOwnerWidget();
            String className = owner == null ? null : AdsUIUtil.getQtClassName(owner);
            if (owner == null || !(AdsMetaInfo.RWT_UI_CONTAINER.equals(className)
                    || AdsMetaInfo.RWT_UI_DIALOG.equals(className)
                    || AdsMetaInfo.RWT_CUSTOM_EDITOR.equals(className)
                    || AdsMetaInfo.RWT_CUSTOM_DIALOG.equals(className)
                    || AdsMetaInfo.RWT_CUSTOM_EDITOR_PAGE.equals(className)
                    || AdsMetaInfo.RWT_CUSTOM_SELECTOR.equals(className)
                    || AdsMetaInfo.RWT_FORM_DIALOG.equals(className)
                    || AdsMetaInfo.RWT_FILTER_DIALOG.equals(className)
                    || AdsMetaInfo.RWT_PROP_EDITOR_DIALOG.equals(className)
                    || AdsMetaInfo.RWT_UI_GROUP_BOX.equals(className)
                    || AdsMetaInfo.RWT_TAB_SET_TAB.equals(className)
                    || AdsMetaInfo.RWT_CUSTOM_WIDGET.equals(className))) {
                superClass = clazzInh.get(AdsMetaInfo.RWT_ANCHOR_CONTAINER_ELEMENT);
            }
        }
        return superClass;

    }

    public static String getExtends(String clazz) {
        return clazzInh.get(clazz);
    }

    public static List<AdsUIProperty> getProps(String clazz, RadixObject context) {
        if (AdsUIUtil.isCustomWidget(clazz)) {
            AdsAbstractUIDef customUI = getCustomUI(clazz, context);
            if (customUI == null) {
                return Collections.emptyList();
            }
            if (customUI instanceof AdsCustomWidgetDef) {
                List<AdsUIProperty> list = ((AdsCustomWidgetDef) customUI).getProperties().list();
                if (customUI.getWidget() != null) {
                    list.addAll(customUI.getWidget().getProperties().list());
                }
                return list;
            }
//            if (customUI instanceof AdsRwtCustomWidgetDef) {
//                List<AdsUIProperty> list = ((AdsRwtCustomWidgetDef) customUI).getProperties().list();
//                if (customUI.getWidget() != null) {
//                    list.addAll(customUI.getWidget().getProperties().list());
//                }
//                return list;
//            }
        }
        return clazzProps.get(clazz);
    }

    public static List<AdsUIConnection> getConns(String clazz, RadixObject context) {
        if (AdsUIUtil.isCustomWidget(clazz)) {
            AdsAbstractUIDef customUI = getCustomUI(clazz, context);
            if (customUI == null) {
                return Collections.emptyList();
            }
            List<AdsUIConnection> conns = new ArrayList<>();
            if (customUI instanceof AdsCustomWidgetDef) {
                for (AdsUISignalDef signal : ((AdsCustomWidgetDef) customUI).getSignals()) {
                    conns.add(new AdsUIConnection(signal));
                }
            }
            return conns;
        } else {
            AdsAbstractUIDef def = AdsUIUtil.getUiDef(context);
            if (def instanceof AdsRwtCustomDialogDef) {
                List<AdsUIConnection> conns = new ArrayList<>();
                for (AdsUISignalDef signal : ((AdsRwtCustomDialogDef) def).getCloseSignals()) {
                    conns.add(new AdsUIConnection(signal));
                }
                List<AdsUIConnection> css = clazzConns.get(clazz);
                if (css != null) {
                    conns.addAll(css);
                }
                return conns;
            }
        }
        return clazzConns.get(clazz);
    }

    public static AdsUIConnection getConnByName(String clazz, String name, RadixObject context) {
        List<AdsUIConnection> conns = getConns(clazz, context);
        if (conns != null) {
            for (AdsUIConnection conn : conns) {
                if (Utils.equals(conn.getSignalName(), name)) {
                    return conn;
                }
            }
        }
        return null;
    }

    public static AdsUIProperty getPropByName(String clazz, String name, RadixObject context) {
        String clz = clazz;
        while (clz != null) {
            List<AdsUIProperty> props = getProps(clz, context);
            if (props != null) {
                for (AdsUIProperty p : props) {
                    if (p.getName().equals(name)) {
                        return p.duplicate();
                    }
                }
            }
            clz = getExtends(clz, context);
        }
        return null;
    }

    public static String getDefaultName(String clazz, String name) {
        if (AdsUIUtil.isCustomWidget(clazz)) {
            return getDefaultName(WIDGET_CLASS, name);
        }
        AdsUIProperty prop = getPropByName(clazz, name, null);
        if (prop != null) {
            return ((AdsUIProperty.StringProperty) prop).value;
        }
        return "";
    }

    public static AdsAbstractUIDef getCustomUI(Id id, RadixObject context) {
        AdsAbstractUIDef targetUI = null;

        if (targetUI == null) {
            AdsModule module = AdsUIUtil.getModule(context);
            if (context != null && module != null) {
                targetUI = (AdsAbstractUIDef) AdsSearcher.Factory.newAdsDefinitionSearcher(module).findById(id).get();
            }
        }

        return targetUI;
    }

    public static AdsAbstractUIDef getCustomUI(String clazz, RadixObject context) {
        Id id = Id.Factory.loadFrom(clazz);
        return getCustomUI(id, context);
    }

    public static AdsAbstractUIDef getCustomUI(AdsUIItemDef widget) {
        if (!AdsUIUtil.isCustomWidget(widget)) {
            return null;
        }

        Id id = Id.Factory.loadFrom(widget instanceof AdsWidgetDef ? ((AdsWidgetDef) widget).getClassName() : ((AdsRwtWidgetDef) widget).getClassName());
        return getCustomUI(id, widget);
    }
}
