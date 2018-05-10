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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.QSignalEmitter;
import com.trolltech.qt.core.QDir;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOption;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEventCodeDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.explorer.dialogs.SetTableAliasDialog;
import org.radixware.kernel.explorer.editors.sqmleditor.sqmltags.SqmlTag;
import org.radixware.kernel.explorer.editors.sqmleditor.sqmltags.SqmlTag_IfParam;
import org.radixware.kernel.explorer.editors.sqmleditor.sqmltags.SqmlTag_PropSqlName;
import org.radixware.kernel.explorer.editors.sqmleditor.sqmltags.SqmlTag_TableSqlName;
import org.radixware.kernel.explorer.editors.sqmleditor.sqmltags.SqmlTag_ThisTableRef;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.DbFuncCall_Dialog;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.ParameterCondition_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.AbstractXscmlEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ImageManager;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.schemas.sqmlexpression.SqmlExpression;
import org.radixware.schemas.sqmlexpression.SqmlExpressionDocument;
import org.radixware.schemas.xscml.Sqml;


final class ToolBar {   
    
    private final static class Separator extends QWidget{
        
        private final QSize sizeHint = new QSize();
        private final QStyleOption option = new QStyleOption();
         
        public Separator(final QWidget parent){
            super(parent);
            setSizePolicy(Policy.Fixed, Policy.Maximum);
        }
        
        private QStyleOption initStyleOption(){
            option.initFrom(this);
            option.setState(QStyle.StateFlag.State_Horizontal);
            return option;
        }

        @Override
        public QSize sizeHint() {
            final int extent = 
                style().pixelMetric(QStyle.PixelMetric.PM_ToolBarSeparatorExtent, initStyleOption(), parentWidget());
            sizeHint.setHeight(parentWidget().height());
            sizeHint.setWidth(extent+2);
            return sizeHint;
        }

        @Override
        protected void paintEvent(final QPaintEvent paintEvent) {
            final QPainter painter = new QPainter();
            painter.begin(this);
            try{
                final QStyleOption option = initStyleOption();
                final QRect rect = option.rect();                
                rect.setWidth(rect.width()-1);
                rect.setX(rect.x()+1);
                option.setRect(rect);
                style().drawPrimitive(QStyle.PrimitiveElement.PE_IndicatorToolBarSeparator, option, painter, parentWidget());
            }finally{
                painter.end();
            }          
        }        
    }
    
    private final SqmlEditor editor;
    private final IClientEnvironment environment;
    private QFrame toolBarWidget;
    private QToolButton btnShowTitle;
    private QToolButton btnInsertTable;
    private QToolButton btnInsertColumn;
    private QToolButton btnInsertConstant;
    private QToolButton btnInsertDbFuncCall;
    private QToolButton btnInsertThisTableRef;
    private QToolButton btnInsertEntityRefValue;
    private QToolButton btnInsertId;
    private QToolButton btnInsertEventCode;
    private QToolButton btnInsertCondition;
    private QMenu paramsMenu;
    private QToolButton btnCreateAlias;
    private QToolButton btnTranslateSqml;
    private QToolButton btnExport;
    private QToolButton btnImport;
    private Separator impExpSeparator;
    private final AbstractXscmlEditor editText;
    
    private QAction actShowTitle;
    private QAction actShowModulName;
    private QAction actShowName;
    private boolean isTranslateButtonEnabled = true;

    @SuppressWarnings("LeakingThisInConstructor")
    public ToolBar(final SqmlEditor editor) {
        this.editor = editor;
        this.environment = editor.getEnvironment();
        editText = editor.getTextEditor();
        createToolBar();
        if (editor.getEnvironment().getApplication().isSqmlAccessible()){
            editText.textChanged.connect(this, "onSqmlTextChange()");
        }else{
            toolBarWidget.setVisible(false);
        }        
    }

    private void createToolBar() {
        toolBarWidget = new QFrame(editor);
        toolBarWidget.setFrameShape(Shape.NoFrame);
        final QHBoxLayout mainLayout = new QHBoxLayout();
        mainLayout.setSpacing(0);
        mainLayout.setMargin(0);
        final MessageProvider msgProvider = environment.getMessageProvider();
        final QSize size30 = new QSize(30, 30);
       
        btnInsertId = createInsertIdButton();
        
        QIcon icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.TABLE);
        btnInsertTable = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert Table"), "actionInsertTable()", "btnInsertTable", icon, size30);

        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_COLUMN);
        btnInsertColumn = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert Column"), "actionInsertColumn()", "btnInsertColumn", icon, size30);

        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.CONSTSET);
        btnInsertConstant = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert Constant"), "actionInsertConstSet()", "btnInsertConstant", icon, size30);

        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.SQL_FUNCTION);
        btnInsertDbFuncCall = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert SQL Function Call"), "actionInsertDbFuncCall()", "btnInsertDbFuncCall", icon, size30);
        
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_THIS_TABLE_REF);
        btnInsertThisTableRef = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert Context Table Reference"), "actionInsertThisTableRef()", "btnInsertThisTableRef", icon, size30);
        btnInsertThisTableRef.setEnabled(editor.getContextClassDef() != null);

        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_TAG_DB_OBJECT);
        btnInsertEntityRefValue = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert Entity Object Reference"), "actionInsertEntityRefValue()", "btnInsertEntityRefValue", icon, size30);
        //btnInsertEntityRefValue.setVisible(false);
                
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_EVENT_CODE);
        btnInsertEventCode = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert Event Code"), "actionInsertEventCode()", "btnInsertEventCode", icon, size30);
        
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_IF_TAG);
        btnInsertCondition = createToolBtn(null, msgProvider.translate("SqmlEditor", "Insert Preprocessor by Parameter"), null, "btnInsertIf", icon, size30);
        final QMenu conditionMenu = new QMenu(btnInsertCondition);
        conditionMenu.setObjectName("rx_insert_sqml_condition_menu");
        btnInsertCondition.setMenu(conditionMenu);
        btnInsertCondition.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_CUSTOM_PARAMETER);
        paramsMenu = conditionMenu.addMenu(icon, msgProvider.translate("SqmlEditor", "Parameter"));        
        paramsMenu.setObjectName("rx_sqml_params_menu");
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_ELSE_TAG);
        conditionMenu.addAction(icon, msgProvider.translate("SqmlEditor", "Insert Else Tag"), this, "actionInsertElseTag()");
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_END_TAG);
        conditionMenu.addAction(icon, msgProvider.translate("SqmlEditor", "Insert EndIf Tag"), this, "actionInsertEndIfTag()");        
        
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_SQML_TRAN);
        btnTranslateSqml = createToolBtn(null, msgProvider.translate("SqmlEditor", "View Translation Result"), "translateSqml()", "btnTranslateSqml", icon, size30);
        
        final String settingsKey = SettingNames.SYSTEM + "/" + "SQML_EDITOR" + "/" + "showTitle_for_Sqml";
        icon = getImageManager().loadSvgIcon("classpath:images/show_title.svg", QColor.transparent);
        final int showMode = environment.getConfigStore().readInteger(settingsKey, EDefinitionDisplayMode.SHOW_SHORT_NAMES.ordinal());
        btnShowTitle = createToolBtn(null, msgProvider.translate("SqmlEditor", "Display Mode"), null, "btnShowTitle", icon, size30);

        btnShowTitle.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
        createMenuActions(showMode);

        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_ADD_ALIAS);
        btnCreateAlias = createToolBtn(null, msgProvider.translate("SqmlEditor","Attach Table"), "actionCreateAlias()", "btnCreateAlias", icon, size30);
        
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_IMPORT);
        btnImport = createToolBtn(null, msgProvider.translate("SqmlEditor","Import Condition"), "actionImport()", "btnImport", icon, size30);
        
        icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_EXPORT);
        btnExport = createToolBtn(null, msgProvider.translate("SqmlEditor","Export Condition"), "actionExport()", "btnExport", icon, size30);
        
        mainLayout.addWidget(btnInsertId);
        mainLayout.addWidget(btnInsertTable);
        mainLayout.addWidget(btnInsertColumn);
        mainLayout.addWidget(btnInsertConstant);
        mainLayout.addWidget(btnInsertDbFuncCall);
        mainLayout.addWidget(btnInsertThisTableRef);
        mainLayout.addWidget(btnInsertEntityRefValue);
        mainLayout.addWidget(btnInsertEventCode);
        mainLayout.addWidget(btnInsertCondition);
        impExpSeparator = new Separator(toolBarWidget);
        mainLayout.addWidget(impExpSeparator);
        mainLayout.addWidget(btnExport);
        mainLayout.addWidget(btnImport);        
        
        final QHBoxLayout otherButtons = new QHBoxLayout();
        
        otherButtons.addWidget(btnTranslateSqml);
        otherButtons.addWidget(btnShowTitle);
        otherButtons.addWidget(btnCreateAlias);
        mainLayout.addLayout(otherButtons);
        mainLayout.setAlignment(otherButtons, AlignmentFlag.AlignRight);
        toolBarWidget.setLayout(mainLayout);
        onSqmlTextChange();        
        updateInsertConditionTagBtn();
        editor.onParametersChanged.connect(this, "updateInsertConditionTagBtn()");
    }

    public void addToolButton(final QToolButton toolBtn) {
        final int index = toolBarWidget.layout().count() - 1;
        insertNewToolBtn(toolBtn, index);
    }

    public void insertToolButton(final QToolButton toolBtn) {
        insertNewToolBtn(toolBtn, 0);
    }

    private void insertNewToolBtn(final QToolButton toolBtn, final int index) {
        if (toolBtn.parentWidget() == null) {
            toolBtn.setParent(toolBarWidget);
        }
        toolBtn.setFixedSize(new QSize(30, 30));

        final QHBoxLayout layout = (QHBoxLayout) toolBarWidget.layout();
        layout.insertWidget(index, toolBtn);
    }

    private QToolButton createToolBtn(final String text, final String toolTip, final String connect, final String objName, final QIcon icon, final QSize size) {
        final QToolButton btn = new QToolButton(toolBarWidget);
        final QFont font = btn.font();
        font.setBold(true);
        btn.setFont(font);
        btn.setAutoRaise(true);
        btn.setObjectName(objName);
        if (toolTip != null) {
            btn.setToolTip(toolTip);
        }
        if (text != null) {
            btn.setText(text);
        }
        if (connect != null) {
            btn.clicked.connect(this, connect);
        }
        if (size != null) {
            btn.setFixedSize(size);
        } else {//added by yremizov
            btn.setSizePolicy(Policy.Fixed, Policy.Fixed);
        }
        if (icon != null) {
            btn.setIcon(icon);
            btn.setIconSize(new QSize(30, 30));
        }
        return btn;
    }

    private QToolButton createInsertIdButton() {
        final QToolButton button = new QToolButton(toolBarWidget);
        button.setAutoRaise(true);
        button.setPopupMode(QToolButton.ToolButtonPopupMode.InstantPopup);
        final MessageProvider messageProvider = environment.getMessageProvider();
        final String toolTip = messageProvider.translate("SqmlEditor", "Insert ID");
        button.setToolTip(toolTip);
        final QIcon icon = ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_INSERT_ID);
        button.setIcon(icon);
        final QMenu topMenu = new QMenu(button);
        topMenu.addAction(ExplorerIcon.getQIcon(ClientIcon.Definitions.TABLE), messageProvider.translate("SqmlEditor", "Table")).triggered.connect(this, "actionInsertTableId()");
        topMenu.addAction(ExplorerIcon.getQIcon(ClientIcon.Definitions.COLUMN), messageProvider.translate("SqmlEditor", "Property")).triggered.connect(this, "actionInsertPropId()");
        topMenu.addAction(ExplorerIcon.getQIcon(ClientIcon.Definitions.CLASS), messageProvider.translate("SqmlEditor", "Class")).triggered.connect(this, "actionInsertClassId()");
        topMenu.addAction(ExplorerIcon.getQIcon(ClientIcon.Definitions.DOMAIN), messageProvider.translate("SqmlEditor", "Domain")).triggered.connect(this, "actionInsertDomainId()");
        topMenu.addAction(ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.Definitions.CONSTSET), messageProvider.translate("SqmlEditor", "Enumeration")).triggered.connect(this, "actionInsertEnumId()");
        button.setMenu(topMenu);
        
        return button;
    }
    
    public void setBtnThisTableRefEnable(final boolean enable) {
        btnInsertThisTableRef.setEnabled(enable);
    }
    
    public void setBtnTranslateSqmlEnable(final boolean enable) {
        isTranslateButtonEnabled = enable;
        btnTranslateSqml.setEnabled(enable);
    }
    
    public void setReadOnlyMode(final boolean flag) {
        btnInsertTable.setEnabled(!flag);
        btnInsertColumn.setEnabled(!flag);
        btnInsertConstant.setEnabled(!flag);
        btnInsertDbFuncCall.setEnabled(!flag);
        btnInsertThisTableRef.setEnabled(!flag && (editor.getContextClassDef() != null));
        btnInsertEntityRefValue.setEnabled(!flag);
        btnInsertId.setEnabled(!flag);
        btnInsertCondition.setEnabled(!flag);
        btnCreateAlias.setEnabled(!flag);
        btnInsertEventCode.setEnabled(!flag);
        btnImport.setEnabled(!flag);
    }        
    
    private void createMenuActions(final int showMode) {
        final MessageProvider msgProvider = environment.getMessageProvider();
        actShowModulName = new QAction(new QIcon(), msgProvider.translate("SqmlEditor", "Display full object names"), editor);
        actShowModulName.setObjectName("actShowModulName");
        actShowModulName.setCheckable(true);
        actShowModulName.triggered.connect(this, "showModulName()");
        actShowTitle = new QAction(new QIcon(), msgProvider.translate("SqmlEditor", "Display object names"), editor);
        actShowTitle.setObjectName("actShowTitle");
        actShowTitle.setCheckable(true);
        actShowTitle.triggered.connect(this, "showTitle()");
        actShowName = new QAction(new QIcon(), msgProvider.translate("SqmlEditor", "Display short object names"), editor);
        actShowName.setObjectName("actShowName");
        actShowName.setCheckable(true);
        actShowName.triggered.connect(this, "showName()");
        if (showMode == EDefinitionDisplayMode.SHOW_FULL_NAMES.ordinal()) {
            showModulName();
        } else if (showMode == EDefinitionDisplayMode.SHOW_TITLES.ordinal()) {
            showTitle();
        } else if (showMode == EDefinitionDisplayMode.SHOW_SHORT_NAMES.ordinal()) {
            showName();
        }
        btnShowTitle.addAction(actShowName);
        btnShowTitle.addAction(actShowModulName);
        btnShowTitle.addAction(actShowTitle);
    }

    private ImageManager getImageManager() {
        return (ImageManager) environment.getApplication().getImageManager();
    }

    @SuppressWarnings("unused")
    private void showModulName() {
        actShowTitle.setChecked(false);
        actShowName.setChecked(false);
        actShowModulName.setChecked(true);
        btnShowTitle.setIcon(getImageManager().loadSvgIcon("classpath:images/model_name.svg", QColor.transparent));
        actionShowTitle(EDefinitionDisplayMode.SHOW_FULL_NAMES);
    }

    @SuppressWarnings("unused")
    private void showTitle() {
        actShowModulName.setChecked(false);
        actShowName.setChecked(false);
        actShowTitle.setChecked(true);
        btnShowTitle.setIcon(getImageManager().loadSvgIcon("classpath:images/show_title.svg", QColor.transparent));
        actionShowTitle(EDefinitionDisplayMode.SHOW_TITLES);
    }

    @SuppressWarnings("unused")
    private void showName() {
        actShowTitle.setChecked(false);
        actShowModulName.setChecked(false);
        actShowName.setChecked(true);
        btnShowTitle.setIcon(getImageManager().loadSvgIcon("classpath:images/show_title.svg", QColor.transparent));
        actionShowTitle(EDefinitionDisplayMode.SHOW_SHORT_NAMES);
    }
    
    @SuppressWarnings("unused")
    private void actionInsertTableId() {
        addIdPath(editor.getSqmlProcessor().chooseSqmlTable(null, editor.isReadonly(), editor, false),true);
    }
    
    @SuppressWarnings("unused")
    private void actionInsertPropId() {
        addIdPath(editor.getSqmlProcessor().chooseSqmlColumn(null, null, editor.isReadonly(), true, editor),false);
    }
    
    @SuppressWarnings("unused")
    private void actionInsertTable() {
        addTag(editor.getSqmlProcessor().chooseSqmlTable(null, editor.isReadonly(), editor, false));
    }

    @SuppressWarnings("unused")
    private void actionInsertColumn() {
        addTag(editor.getSqmlProcessor().chooseSqmlColumn(null, null, editor.isReadonly(), null));
    }

    @SuppressWarnings("unused")
    private void actionInsertConstSet() {
        addTag(editor.getSqmlProcessor().chooseEnumItem(editor.isReadonly(), null));
    }
    
    @SuppressWarnings("unused")
    private void actionInsertDbFuncCall() {
        final DbFuncCall_Dialog dialog = 
                new DbFuncCall_Dialog(environment, editor.getSqmlProcessor().getShowMode(), false, editor);
        if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
            final long pos = editText.textCursor().position();
            final SqmlTag funcCallTag = 
                editor.getSqmlProcessor().createDbFuncCall(dialog.getSqmlFunction(), dialog.getParameters(), pos);
            editText.insertTag(funcCallTag, "");
        }
    }
    
    @SuppressWarnings("unused")
    private void actionInsertThisTableRef() {
        final ISqmlTableDef table = editor.getContextClassDef();
        final long pos = editText.textCursor().position();
        final SqmlTag_ThisTableRef tag = editor.getSqmlProcessor().createThisTableRef(table, pos);
        editText.insertTag(tag, "");
    }

    @SuppressWarnings("unused")
    private void actionInsertEntityRefValue() {
        addTag(editor.getSqmlProcessor().chooseSqmlTableObject(null, editor.isReadonly(), editor));
    }
    
    public void updateInsertConditionTagBtn(){
        final ISqmlParameters parameters = editor.getParamters();
        final int count = parameters==null ? 0 : parameters.size();
        paramsMenu.clear();
        for (int i=0; i<count; i++){
            final ISqmlParameter parameter = parameters.get(i);
            if (parameter.getId()!=null && parameter.getType()!=null){
                final QIcon icon = ExplorerIcon.getQIcon(parameter.getIcon());                        
                paramsMenu.addAction(icon, parameter.getTitle(), this, "actionInsertIfParamTag()").setData(parameter.getId().toString());
            }
        }
        btnInsertCondition.setEnabled(!editor.isReadonly() && count>0);
    }
    
    @SuppressWarnings("unused")
    private void actionInsertElseTag(){
        editText.insertTag(editor.getSqmlProcessor().createElseIf(editText.textCursor().position()), "");
        editText.insertText("\n");
    }
    
    @SuppressWarnings("unused")
    private void actionInsertEndIfTag(){
        editText.insertTag(editor.getSqmlProcessor().createEndIf(editText.textCursor().position()), "");
        editText.insertText("\n");        
    }
    
    @SuppressWarnings("unused")
    private void actionInsertIfParamTag(){
        if (QObject.signalSender() instanceof QAction && editor.getParamters()!=null){
            final QAction action = (QAction)QObject.signalSender();
            if (action.data() instanceof String){
                final Id paramId = Id.Factory.loadFrom((String)action.data());
                final ISqmlParameter parameter = editor.getParamters().getParameterById(paramId);
                if (parameter!=null){
                    final ParameterCondition_Dialog dialog = 
                            new ParameterCondition_Dialog(editor.getParamters(),
                                                                             parameter, 
                                                                             editor.getSqmlProcessor().getShowMode(), 
                                                                             editText,
                                                                             editor.getEnvironment());
                    if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
                        final long pos = editText.textCursor().position();
                        final SqmlTag_IfParam ifParam = editor.getSqmlProcessor().createIfParam(dialog.getIfParamTag(), pos);
                        editText.insertTag(ifParam, "");
                        editText.insertText("\n");
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void actionShowTitle(final EDefinitionDisplayMode showMode) {
        editText.getTagConverter().setShowMode(showMode);
        editor.sortPropTree();
        editor.updateTagsName();
        final ClientSettings settings = environment.getConfigStore();
        final String settingsKey = SettingNames.SYSTEM + "/" + "SQML_EDITOR" + "/" + "showTitle_for_Sqml";
        settings.writeInteger(settingsKey, editText.getTagConverter().getShowMode().ordinal());
    }

    @SuppressWarnings("unused")
    private void actionCreateAlias() {
        final SqmlTreeModel sqmlModel=new SqmlTreeModel(environment, null, EnumSet.of(SqmlTreeModel.ItemType.MODULE_INFO));
        //sqmlModel.setMarkDeprecatedItems(true);
        final SetTableAliasDialog dlg = new SetTableAliasDialog(
                environment,
                sqmlModel,
                null,
                null);
        if(dlg.execDialog() == IDialog.DialogResult.ACCEPTED) {
            editor.setAliases(Collections.singletonMap(dlg.getAlias(), dlg.getSelectedId()));
        }
    }
    
    @SuppressWarnings("unused")
    private void actionInsertClassId() {
        addIdPath(editor.getSqmlProcessor().chooseClassId(editor.isReadonly(), editor),false);
    }
    
    @SuppressWarnings("unused")
    private void actionInsertDomainId() {
        addIdPath(editor.getSqmlProcessor().chooseDomainId(editor.isReadonly(), editor),false);
    }
    
    @SuppressWarnings("unused")
    private void actionInsertEnumId() {
        addIdPath(editor.getSqmlProcessor().chooseEnumId(editor.isReadonly(), editor),false);
    }    
    
    @SuppressWarnings("unused")
    private void actionInsertEventCode() {
        final ISqmlEventCodeDef eventCodeDef = (ISqmlEventCodeDef) editor.getSqmlProcessor().chooseEventCode(editor.isReadonly(), editor);
        addTag(eventCodeDef);
    }
    
    public void setImportAccessible(final boolean isAccessible){
        btnImport.setVisible(isAccessible);
        impExpSeparator.setVisible(btnImport.isVisible() || btnExport.isVisible());
    }
    
    public boolean isImportAccessible(){
        return btnImport.isVisible();
    }
    
    public void setExportAccessible(final boolean isAccessible){
        btnExport.setVisible(isAccessible);
        impExpSeparator.setVisible(btnImport.isVisible() || btnExport.isVisible());
    }
    
    public boolean isExportAccessible(){
        return btnExport.isVisible();
    }
    
    @SuppressWarnings("unused")
    private void actionImport(){
        final MessageProvider mp = environment.getMessageProvider();
        final String fileFilter = mp.translate("SqmlEditor", "XML Document (%s);;All Files (%s)");
        final String fileName =
                QFileDialog.getOpenFileName(editor, mp.translate("SqmlEditor", "Import Filter"), QDir.homePath(),
                new QFileDialog.Filter(String.format(fileFilter, "*.xml", "*.*")));
        if (fileName != null && !fileName.isEmpty()) {
            final File xmlFile = new File(fileName);
            if (xmlFile.exists()) {                
                try{
                    if (!readSqmlExpression(xmlFile) && !readFilterCondition(xmlFile)){
                        final String message = environment.getMessageProvider().translate("SqmlEditor", "Unable to import from file %1$s.\nWrong file format.");
                        final String title = environment.getMessageProvider().translate("SqmlEditor", "Unable to Import Condition");
                        environment.messageError(title, String.format(message, fileName));
                    }
                }catch(IOException exception){
                    showIOException(exception, mp.translate("SqmlEditor", "Failed to import from file '%s'"), fileName);
                }
            }
        }
    }
    
    private boolean readSqmlExpression(final File xmlFile) throws IOException{
        final SqmlExpressionDocument document;
        try{
            document = SqmlExpressionDocument.Factory.parse(xmlFile);
        }catch(XmlException exception){            
            return false;
        }
        final String filePath = xmlFile.getAbsolutePath();
        final SqmlExpression xml = document.getSqmlExpression();
        if (!checkImportContext(xml.isSetTableId() ? xml.getTableId() : null, filePath)){
            return true;
        }        
        final ISqmlParameters parameters;
        if (xml.isSetParameters()){
            parameters=importParameters(xml.getParameters(), filePath);
            if (parameters==null){
                return true;//one error message was already displayed - do not show second
            }
        }else{
            parameters=editor.getParamters();
        }
        importCondition(xml.getSqml(), parameters, filePath);
        return true;
    }
    
    private boolean checkImportContext(final Id xmlTableId, final String fileName){
        final ISqmlTableDef context = editor.getContextClassDef();        
        final ISqmlTableDef xmlContext = xmlTableId==null ? null : environment.getSqmlDefinitions().findTableById(xmlTableId);
        if (xmlTableId!=null && xmlContext==null){
            final String message = environment.getMessageProvider().translate("SqmlEditor", "Unable to import from file %1$s.\nThis file contains condition for unknown table #%2$s.");
            final String title = environment.getMessageProvider().translate("SqmlEditor", "Unable to Import Condition");
            environment.messageError(title, String.format(message, fileName, xmlTableId.toString()));
            return false;
        }
        if (xmlContext!=null
            && (context==null || !xmlTableId.equals(context.getTableId()))){
            final String message = environment.getMessageProvider().translate("SqmlEditor", "Unable to import from file %1$s.\nThis file contains condition for %2$s.");
            final String title = environment.getMessageProvider().translate("SqmlEditor", "Unable to Import Condition");
            environment.messageError(title, String.format(message, fileName, xmlContext.getTitle()));
            return false;
        }
        return true;
    }
    
    private boolean checkParameters(final Sqml condition, final ISqmlParameters parameters, final String fileName){
        if (condition==null || condition.getItemList()==null || condition.getItemList().isEmpty()){
            return true;
        }
        for (Sqml.Item sqmlItem: condition.getItemList()){
            if (sqmlItem.getParameter()!=null 
                && (parameters==null || parameters.getParameterById(sqmlItem.getParameter().getParamId())==null)){
                showUnknownParamMessage(fileName);
                return false;
            }
            if (sqmlItem.getParamValCount()!=null
                && (parameters==null || parameters.getParameterById(sqmlItem.getParamValCount().getParamId())==null)){
                showUnknownParamMessage(fileName);
                return false;
            }
        }
        return true;
    }
    
    private void showUnknownParamMessage(final String fileName){
        final String message = environment.getMessageProvider().translate("SqmlEditor", "Unable to import from file %1$s.\nThe condition contains unknown parameter.");
        final String title = environment.getMessageProvider().translate("SqmlEditor", "Unable to Import Condition");
        environment.messageError(title, String.format(message, fileName));        
    }
    
    private boolean importCondition(final Sqml condition, final ISqmlParameters parameters, final String fileName){
        if (checkParameters(condition, parameters, fileName)){
            if (parameters==editor.getParamters()){
                editor.setSqml(condition);
            }else{
                editor.setSqml(condition, parameters);
            }
            return true;
        }
        return false;
    }
    
    private ISqmlParameters importParameters(final XmlObject parameters, final String fileName){
        final ISqmlParameters result;
        if (editor.getSqmlProcessor().canImportParametersFromXml()){
            result = editor.getSqmlProcessor().importParametersFromXml(parameters);
            if (result==null){
                final String message = environment.getMessageProvider().translate("SqmlEditor", "Unable to import from file %1$s.\nFailed to import condition parameters.");
                final String title = environment.getMessageProvider().translate("SqmlEditor", "Unable to Import Condition");
                environment.messageError(title, String.format(message, fileName));
            }
        }else{
            final String message = environment.getMessageProvider().translate("SqmlEditor", "Unable to import from file %1$s.\nThis file contains condition with parameters.");
            final String title = environment.getMessageProvider().translate("SqmlEditor", "Unable to Import Condition");
            environment.messageError(title, String.format(message, fileName));
            result = null;
        }
        return result;
    }    
    
    private boolean readFilterCondition(final File xmlFile) throws IOException{        
        org.radixware.schemas.groupsettings.FilterDocument document;
        try{
            document = org.radixware.schemas.groupsettings.FilterDocument.Factory.parse(xmlFile);
        }catch(XmlException exception){
            return false;
        }
        final String filePath = xmlFile.getAbsolutePath();
        final org.radixware.schemas.groupsettings.CustomFilter xmlFilter = document.getFilter();
        if (!checkImportContext(xmlFilter.getTableId(), filePath)){
            return true;
        }
        final ISqmlParameters parameters;
        if (xmlFilter.getParameters()!=null 
            && xmlFilter.getParameters().getParameterList()!=null 
            && !xmlFilter.getParameters().getParameterList().isEmpty()){
            parameters = importParameters(xmlFilter.getParameters(), filePath);
            if (parameters==null){
                return true;
            }
        }else{
            parameters = null;
        }
        return importCondition(xmlFilter.getCondition(), parameters, filePath);
    }
    
    @SuppressWarnings("unused")
    private void actionExport(){
        final MessageProvider mp = environment.getMessageProvider();
        final String fileFilter = mp.translate("SqmlEditor", "XML Document (%s);;All Files (%s)");
        final String fileName =
                QFileDialog.getSaveFileName(editor, mp.translate("SqmlEditor", "Export Condition"), QDir.homePath(),
                                                             new QFileDialog.Filter(String.format(fileFilter, "*.xml", "*.*")));        
        if (fileName != null && !fileName.isEmpty()) {
            File xmlFile = new File(fileName);
            if (xmlFile.getName().indexOf('.')<0){
                xmlFile = new File(fileName+".xml");
            }
            try {
                exportSqmlExpression(xmlFile);
            } catch (IOException exception) {
                showIOException(exception, mp.translate("SqmlEditor", "Failed to export into file '%s'"), fileName);
            }
        }
    }
    
    private void showIOException(final IOException exception, final String messageTemplate, final String fileName){
        final MessageProvider mp = environment.getMessageProvider();
        final String title = mp.translate("SqmlEditor", "Input/Output Exception");
        final String message =  String.format(messageTemplate, fileName);
        if (exception.getLocalizedMessage() != null && !exception.getLocalizedMessage().isEmpty()) {            
            environment.messageError(title, message+"\n"+exception.getLocalizedMessage());
        } else {            
            environment.messageError(title, message);
        }
    }
    
    private void exportSqmlExpression(final File file) throws IOException{
        final SqmlExpressionDocument document = SqmlExpressionDocument.Factory.newInstance();
        final SqmlExpression xml = document.addNewSqmlExpression();
        final ISqmlTableDef context = editor.getContextClassDef();
        if (context!=null){
            if (context.hasEntityClass()){
                xml.setEntityClassId(context.getId());
            }
            xml.setTableId(context.getTableId());
        }
        xml.setSqml(editor.getSqml());
        final XmlObject parameters = editor.getSqmlProcessor().exportParameters();
        if (parameters!=null){
            xml.setParameters(parameters);
        }
        XmlFormatter.save(document, file);
    }
    
    private void addTag(final Object obj) {
        if (obj instanceof ISqmlColumnDef) {
            final ISqmlColumnDef prop = (ISqmlColumnDef) obj;
            final long pos = editText.textCursor().position();
            final SqmlTag_PropSqlName tag;
            tag = editor.getSqmlProcessor().createPropSqlName(prop, pos);
            editText.insertTag(tag, "");
        } else if (obj instanceof ISqmlTableDef) {
            final ISqmlTableDef table = (ISqmlTableDef) obj;
            final long pos = editText.textCursor().position();
            final SqmlTag_TableSqlName tag = editor.getSqmlProcessor().createTableSqlName(table, pos);
            editText.insertTag(tag, "");
        } else if (obj instanceof ISqmlEnumItem) {
            final ISqmlEnumItem enumItem = (ISqmlEnumItem) obj;
            final long pos = editText.textCursor().position();
            final TagInfo tag = editor.getSqmlProcessor().createTag(enumItem, pos, null, null);
            editText.insertTag(tag, "");
        } else if (obj instanceof EntityModel) {
            final EntityModel entityModel = (EntityModel) obj;
            final long pos = editText.textCursor().position();
            final TagInfo tag = editor.getSqmlProcessor().createEntityRefValue(entityModel.getPid(), pos, false, null);
            editText.insertTag(tag, "");
        } else if (obj instanceof ISqmlEventCodeDef) {
            final long pos = editText.textCursor().position();
            final TagInfo tag = editor.getSqmlProcessor().createEventCode((ISqmlEventCodeDef)obj, pos);
            editText.insertTag(tag, "");
        }
    }
   
    private void addIdPath(final ISqmlDefinition definition,final boolean isTable){
        if (definition!=null){
            final long pos = editText.textCursor().position();
            editText.insertTag(editor.getSqmlProcessor().createIdPath(definition, pos, isTable), "");
        }
    }
    
    public QFrame getToolBar() {
        return toolBarWidget;
    }
    
    @SuppressWarnings("unused")
    private void translateSqml() {
        final Map<Id,Object> paramValues;
        final Sqml sqml = editor.getSqml();
        final Sqml additionalFrom = editor.getAdditionalFrom();
        if (SqmlPreprocessor.hasPreprocessorTags(sqml)
            || SqmlPreprocessor.hasPreprocessorTags(additionalFrom)){
            final InputPreprocessorParamValuesDialog dialog = 
                new InputPreprocessorParamValuesDialog(environment, editor);
            if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
                paramValues = dialog.getParamValues();
            }else{
                return;
            }
        }else{
            paramValues = Collections.emptyMap();
        }
        final String sql = 
            SqmlTranslator.translate(environment, 
                                                 sqml,
                                                 additionalFrom,
                                                 editor.getContextClassDef(), 
                                                 editor.getParamters(), 
                                                 paramValues, 
                                                 editText.getTagConverter().getShowMode());
        if (sql!=null){
            final String title = environment.getMessageProvider().translate("SqmlEditor", "SQML Translation Result");
            environment.messageInformation(title, sql);            
        }
    }
    
    private void onSqmlTextChange() {
        if(btnTranslateSqml != null && isTranslateButtonEnabled) {
            if(editText.toPlainText().isEmpty()) {
                btnTranslateSqml.setEnabled(false);
            } else {
                btnTranslateSqml.setEnabled(SqmlTranslator.isAccessible(environment));
            }
        }
    }        
}
