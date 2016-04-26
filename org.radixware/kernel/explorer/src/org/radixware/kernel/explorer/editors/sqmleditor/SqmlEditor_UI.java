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

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QShowEvent;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.*;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SqmlTreeModelProxy;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.Condition_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.AbstractXscmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.kernel.explorer.views.Splitter;
import org.radixware.kernel.explorer.widgets.QExtTabWidget;


public class SqmlEditor_UI {

    private final QVBoxLayout editorLayout = new QVBoxLayout();
    private Splitter splitter;
    private QPushButton btn_AddTag;
    private QPushButton btn_AddExp;
    private final SqmlEditor editor;
    private QFrame rightPanel;
    private QTreeView propTree;
    private ParameterListWidget paramList;
    private SqmlTreeModelProxy proxyModel;
    private ToolBar toolBar;
    private final AbstractXscmlEditor editText;
    private ParametersToolBar paramToolBar;
    private ISqmlParameters params;
    private QExtTabWidget tabTree;
    private final Map<String, Id> tablesAliases = new HashMap<>();
    private QModelIndex curIndex = null;
    private final static String SPLITTER_KEY_NAME = "splitterSqmlEditor";    
    
    public SqmlEditor_UI(final SqmlEditor editor, final ISqmlParameters param) {
        this.editor = editor;
        this.params = param;
        editText = editor.getTextEditor();
        editorLayout.setMargin(0);
        editorLayout.setSpacing(0);
        createSqmlEditorUi();
        editor.setLayout(editorLayout);
    }

    public void setParameters(final ISqmlParameters parameters) {//add by yremizov
        params = parameters;
        if (parameters != null) {
            fillParamList();            
        }
        updateTabSet();
        updateRightPanelVisiblity();
        if (paramToolBar != null) {
            paramToolBar.setParameters(parameters);
            paramToolBar.refresh();
        }
    }

    private void createTree(QFrame panel) {
        propTree = createPropTree(panel);
        propTree.setIconSize(new QSize(20, 20));
        if (propTree.currentIndex() != null) {
            propTree.expand(propTree.currentIndex());
        }
        sortPropTree();
        createTreeWithParams(panel);
    }

    private QFrame creareParamPanel() {
        final QFrame paramFrame = new QFrame();
        paramFrame.setFrameShape(Shape.NoFrame);
        final QVBoxLayout layout = new QVBoxLayout();
        layout.setSpacing(0);
        layout.setMargin(0);
        paramList = new ParameterListWidget(paramFrame);
        paramList.setObjectName("paramTree");
        paramList.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        fillParamList();
        paramList.doubleClicked.connect(this, "onParameterItemDoubleClick(QModelIndex)");
        paramList.currentItemChanged.connect(this, "onParameterItemClick(QListWidgetItem,QListWidgetItem)");
        paramToolBar = new ParametersToolBar(editor.getEnvironment(), editor, paramList, paramFrame);
        paramToolBar.refresh();
        paramList.setParamToolBar(paramToolBar);
        layout.addWidget(paramToolBar);
        layout.addWidget(paramList);
        paramFrame.setLayout(layout);
        return paramFrame;
    }

    private void createTreeWithParams(final QFrame panel) {
        tabTree = new QExtTabWidget(panel);
        tabTree.currentChanged.connect(this, "curTabChanged(Integer)");
        final QFrame paramFrame = creareParamPanel();
        tabTree.addTab(propTree, editor.getEnvironment().getMessageProvider().translate("SqmlEditor", "Properties"));
        tabTree.addTab(paramFrame, editor.getEnvironment().getMessageProvider().translate("SqmlEditor", "Parameters"));       
       
        if (isRightPanelVisible()){
            updateTabSet();
            panel.setVisible(true);
        }else{
            panel.setVisible(false);
        }
        
        panel.layout().addWidget(tabTree);
    }

    @SuppressWarnings("unused")
    private void curTabChanged(final Integer curTab) {
        if (curTab == 0) {
            onTreeItemClick(propTree.currentIndex());
        } else {
            onParameterItemClick(paramList.currentItem(), null);
        }
    }

    public void sortPropTree() {
        if (proxyModel != null) {
            ((SqmlTreeModel) proxyModel.sourceModel()).setDisplayMode(editText.getTagConverter().getShowMode());
            proxyModel.invalidate();
            propTree.resizeColumnToContents(0);
            propTree.update();
        }
    }

    private void createSqmlEditorUi() {
        splitter = new Splitter(editor,(ExplorerSettings)editor.getEnvironment().getConfigStore());
        splitter.setOrientation(Orientation.Horizontal);

        final QFrame leftPanel = createLeftPanel();
        leftPanel.setObjectName("leftPanel");
        splitter.addWidget(leftPanel);

        rightPanel = createRightPanel();
        rightPanel.setObjectName("rightPanel");
        splitter.addWidget(rightPanel);

        editorLayout.addWidget(splitter);
    }

    private QFrame createLeftPanel() {
        final QFrame w = new QFrame(editor);
        w.setFrameShape(Shape.NoFrame);
        final QVBoxLayout leftLayout = new QVBoxLayout();
        leftLayout.setMargin(0);
        leftLayout.setWidgetSpacing(0);

        toolBar = new ToolBar(editor);
        leftLayout.addWidget(toolBar.getToolBar());
        leftLayout.addWidget(editText);
        w.setLayout(leftLayout);
        return w;
    }

    private QFrame createRightPanel() {
        final QHBoxLayout rightLayout = new QHBoxLayout();
        rightLayout.setContentsMargins(5, 0, 0, 0);
        
        final QFrame panel = new QFrame(editor);
        panel.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Preferred);
        panel.setFrameShape(Shape.NoFrame);
        final QVBoxLayout layout = new QVBoxLayout();

        btn_AddTag = new QPushButton(panel);
        btn_AddTag.setObjectName("btn_AddTag");
        btn_AddTag.setIcon(ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_ADD_TAG));
        btn_AddTag.setToolTip(editor.getEnvironment().getMessageProvider().translate("SqmlEditor", "Add Tag"));
        btn_AddTag.clicked.connect(this, "btn_AddTag_clicked()");
        btn_AddTag.setFixedSize(30, 30);
        btn_AddTag.setEnabled(false);

        btn_AddExp = new QPushButton(panel);
        btn_AddExp.setObjectName("btn_AddExp");
        btn_AddExp.setIcon(ExplorerIcon.getQIcon(SqmlEditor.SqmlEditorIcons.IMG_ADD_CONDITION));
        btn_AddExp.setToolTip(editor.getEnvironment().getMessageProvider().translate("SqmlEditor", "Add Condition"));
        btn_AddExp.clicked.connect(this, "btn_AddExp_clicked()");
        btn_AddExp.setFixedSize(30, 30);
        btn_AddExp.setEnabled(false);

        layout.addWidget(btn_AddTag);
        layout.addWidget(btn_AddExp);

        final Alignment al = new Alignment();
        al.set(AlignmentFlag.AlignCenter);
        layout.setAlignment(al);
        rightLayout.addLayout(layout);
        panel.setLayout(rightLayout);
        createTree(panel);

        return panel;
    }

    public void updatePropTree() {
        editText.setTagConverter(editor.getSqmlProcessor());
        fillPropTree((SqmlDefinitionsTree) propTree, editor.getContextClassDef());
        updateTabSet();
        updateRightPanelVisiblity();
    }

    public void contextClassChanged(final ISqmlTableDef contextClass) {
        boolean enable=contextClass!=null;
        toolBar.setBtnThisTableRefEnable(enable);
        toolBar.setBtnTranslateSqmlEnable(enable);
    }

    private boolean fillPropTree(final QTreeView tree, final ISqmlTableDef contextClass) {
        if (contextClass != null) {
            return fillPropTree(tree, Collections.<ISqmlDefinition>singletonList(contextClass));
        }else{
            return false;
        }        
    }
    
    private boolean fillPropTree(final QTreeView tree, final List<ISqmlDefinition> tables){
        boolean res = false;
        final SqmlTreeModel sqmlModel = new SqmlTreeModel(editor.getEnvironment(), tables);
        sqmlModel.hideDefinitions(SqmlTreeModel.ItemType.INDEX);
        sqmlModel.hideDefinitions(SqmlTreeModel.ItemType.MODULE_INFO);
        sqmlModel.setMarkDeprecatedItems(true);
        sqmlModel.setDisplayMode(editText.getTagConverter().getShowMode());
        final QModelIndex rootIndex = sqmlModel.index(0, 0, null);
        proxyModel = new SqmlTreeModelProxy(sqmlModel, null);
        proxyModel.setFilterKeyColumn(0);
        proxyModel.sort(0, Qt.SortOrder.AscendingOrder);
        tree.setModel(proxyModel);
        tree.setHeaderHidden(true);

        int n = sqmlModel.rowCount(rootIndex);
        if (n > 0) {
            res = true;
            tree.setCurrentIndex(proxyModel.index(0, 0));
            onTreeItemClick(tree.currentIndex());
        }
        tree.expandToDepth(0);//add by yremizov
        tree.resizeColumnToContents(0);
        return res;
    }

    private void fillParamList() {
        paramList.clear();
        if(params==null) return;        
        for (ISqmlParameter p : params.getAll()) {
            if (checkParameter(p)) {
                ParameterItem item = new ParameterItem(p);
                if ((p.getIcon() != null) && (ExplorerIcon.getQIcon(p.getIcon()) != null)) {
                    item.setIcon(ExplorerIcon.getQIcon(p.getIcon()));
                }
                paramList.addItem(item);
            } else {
                final String message = editor.getEnvironment().getMessageProvider().translate("SqmlEditor", "Referenced table was not found for parameter %s. Ignoring this parameter");
                editor.getEnvironment().getTracer().put(EEventSeverity.WARNING, String.format(message, p.getTitle()));
            }

        }
        if (paramList.count() > 0) {
            paramList.setCurrentRow(0);
        }
    }

    private boolean checkParameter(final ISqmlParameter p) {
        if (p.getType() == EValType.PARENT_REF || p.getType() == EValType.ARR_REF) {
            try{
              return  p.getReferencedTableId()!=null;
            }
            catch(DefinitionError error){
                final String traceMessage = editor.getEnvironment().getMessageProvider().translate("SqmlEditor","Parameter %s is invalid:\n%s");
                editor.getEnvironment().getTracer().error(String.format(traceMessage, p.getTitle(), error.getMessage()));
                return false;
            }
        }
        return true;
    }

    private QTreeView createPropTree(final QFrame panel) {
        SqmlDefinitionsTree tree = new SqmlDefinitionsTree(panel);
        tree.setObjectName("propTree");
        tree.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        tree.doubleClicked.connect(this, "onItemDoubleClick(QModelIndex)");
        tree.collapsed.connect(this, "onCollapsed(QModelIndex)");
        tree.expanded.connect(this, "onExpanded(QModelIndex)");
        if (editor.getContextClassDef() != null) {
            fillPropTree(tree, editor.getContextClassDef());
        }
        return tree;
    }    

    private ISqmlDefinition setCurItemValue(final QModelIndex modelIndex) {
        if (modelIndex == null) {
            return null;
        }
        final QModelIndex sourceIndex = ((SqmlTreeModelProxy) modelIndex.model()).mapToSource(modelIndex);
        final SqmlTreeModel model = (SqmlTreeModel) ((SqmlTreeModelProxy) modelIndex.model()).sourceModel();
        return model.definition(sourceIndex);
    }

    private ISqmlDefinition getSqmlDefinition(final QModelIndex modelIndex) {
        QModelIndex sourceIndex = ((SqmlTreeModelProxy) modelIndex.model()).mapToSource(modelIndex);
        final SqmlTreeModel model = (SqmlTreeModel) ((SqmlTreeModelProxy) modelIndex.model()).sourceModel();
        ISqmlDefinition def = model.definition(sourceIndex);
        if (!(def instanceof ISqmlParameter)) {
            while ((!(def instanceof ISqmlTableDef)) || (def == null)) {
                sourceIndex = sourceIndex.parent();
                def = model.definition(sourceIndex);
            }
        }
        return def;
    }

    private String getTableAlias() {
        final ISqmlDefinition definition = getSqmlDefinition(curIndex);
        if (definition instanceof ISqmlTableDef) {
            return ((ISqmlTableDef) definition).getAlias();
        }
        return null;
    }

    @SuppressWarnings("unused")
    private void onCollapsed(final QModelIndex modelIndex) {
        propTree.setCurrentIndex(modelIndex);
        onTreeItemClick(modelIndex);
        propTree.resizeColumnToContents(0);
    }

    @SuppressWarnings("unused")
    private void onExpanded(final QModelIndex modelIndex) {
        propTree.resizeColumnToContents(0);
    }

    @SuppressWarnings("unused")
    private void onTreeItemClick(final QModelIndex modelIndex) {
        curIndex = modelIndex;
        final ISqmlDefinition curItemValue = setCurItemValue(modelIndex);
        btn_AddTag.setEnabled(curItemValue != null && btn_AddTag_isEnabled(curItemValue));
        btn_AddExp.setEnabled(curItemValue != null && btn_AddExp_isEnabled(curItemValue));
    }

    @SuppressWarnings("unused")
    private void onParameterItemClick(final QListWidgetItem curItem, final QListWidgetItem item2) {
        btn_AddTag.setEnabled(curItem != null);
        btn_AddExp.setEnabled(false);
        if (paramToolBar != null) {
            paramToolBar.refresh();
        }

    }

    @SuppressWarnings("unused")
    private void onItemDoubleClick(final QModelIndex modelIndex) {
        curIndex = modelIndex;
        if (modelIndex == null) {
            return;
        }
        if (btn_AddTag.isEnabled()) {
            addSqmlTag();
        } else if (btn_AddExp.isEnabled()) {
            btn_AddExp_clicked();
        }
    }

    @SuppressWarnings("unused")
    private void onParameterItemDoubleClick(final QModelIndex modelIndex) {
        curIndex = modelIndex;
        if (modelIndex == null) {
            return;
        }
        if (btn_AddTag.isEnabled()) {
            btn_AddTag_clicked();
        }
    }

    private boolean btn_AddTag_isEnabled(final ISqmlDefinition obj) {
        if (editor.isReadonly()) {
            return false;
        } else if (obj instanceof ISqmlColumnDef) {
            final ISqmlColumnDef prop = (ISqmlColumnDef) obj;
            final ISqmlTableDef parent = prop.getOwnerTable();
            if ((parent != null) && (parent instanceof ISqmlTableDef)) {
                return (prop.getType() == EValType.PARENT_REF) && (prop.getReferenceIndex() != null) || (prop.getType() != EValType.PARENT_REF);//!((prop.getType()==EValType.PARENT_REF)&&(presentClass.equals(editor.getContextClassDef())));
            }
            return false;
        } else if ((obj instanceof List) || (obj instanceof Exception)) {
            return false;
        }
        return true;
    }

    private boolean btn_AddExp_isEnabled(final ISqmlDefinition obj) {
        if (editor.isReadonly()) {
            return false;
        }
        return (obj instanceof ISqmlColumnDef) && canCreatValEditor((ISqmlColumnDef)obj);
    }
    
    private boolean canCreatValEditor(final ISqmlColumnDef obj){
        final EValType type=obj.getType();
        return type.getArrayType()!=null || type.getArrayItemType()!=null  || type==EValType.ANY;
        /*(type == EValType.BOOL)||(type == EValType.CHAR)||(type == EValType.BIN)||
               (type == EValType.BLOB)||(type == EValType.DATE_TIME)||(type == EValType.INT)||
               (type == EValType.CLOB)||(type == EValType.STR)||(type == EValType.NUM)||
               (type ==  EValType.PARENT_REF) ;*/
    }

    @SuppressWarnings("unused")
    private void btn_AddTag_clicked() {
        if (tabTree != null && tabTree.currentIndex() == 1) {
            final ParameterItem curParameter = (ParameterItem) paramList.currentItem();
            editor.getSqmlTagInsertion().insertTag(curParameter.getParameter(), null, null);
        } else {
            curIndex = propTree.currentIndex();
            addSqmlTag();
        }
    }

    @SuppressWarnings("unused")
    private void btn_AddExp_clicked() {
        final ISqmlDefinition curItemValue = setCurItemValue(curIndex);
        addSqmlExpression(curItemValue);
    }

    @SuppressWarnings("unused")
    private void onCheckBoxSorting_Click(final boolean isCheck) {
        proxyModel.sort(isCheck ? 0 : -1, Qt.SortOrder.AscendingOrder);
    }
    
    private boolean isPropTreeVisible(){
        return editor.getContextClassDef()!=null || !tablesAliases.isEmpty();
    }
    
    private boolean isRightPanelVisible(){
        return !editor.isReadonly() && (isPropTreeVisible() || params!=null);
    }

    private void updateRightPanelVisiblity(){
        rightPanel.setVisible(isRightPanelVisible());            
    }
    
    private void updateTabSet(){
        boolean isTabsVisible=isPropTreeVisible() && params!=null;
        int curTabIndax= isPropTreeVisible() ? 0 : 1;
        tabTree.setCurrentIndex(curTabIndax);
        tabTree.setTabBarVisible(isTabsVisible);
    }
    
    public void setReadOnlyMode(final boolean isReadOnly) {
        updateRightPanelVisiblity();
        toolBar.setReadOnlyMode(isReadOnly);
        editText.setReadOnly(isReadOnly);
    }

    private void addSqmlTag() {
        final Object obj = setCurItemValue(curIndex);
        editor.getSqmlTagInsertion().insertTag(obj, getTableAlias(), curIndex);
    }

    private void addSqmlExpression(final ISqmlDefinition obj) {
        if (!(obj instanceof ISqmlColumnDef)) {
            return;
        }
        final ISqmlColumnDef prop = (ISqmlColumnDef) obj;
        final Condition_Dialog d = new Condition_Dialog(editor.getEnvironment(), editor, prop, null, null, editText.getTagConverter().getShowMode());
        if ((d.isValid()) && (d.exec() == 1)) {
            if (prop.getType() == EValType.PARENT_REF) {
                final QTextCursor tc = editText.textCursor();
                editor.getSqmlTagInsertion().insertParentCondition(d.isParentCondition(), d.getValue(), prop, d.getStrOperator(), d.getOperator(), getTableAlias(), tc);
            } else {
                editor.getSqmlTagInsertion().insertCondition(d.isParentCondition(), d.getValue(), prop, d.getStrOperator(), d.getOperator(), getTableAlias(), curIndex);
            }
        }
    }

    @SuppressWarnings("unused")
    private void saveSplitterSettings() {
        final ExplorerSettings settings = (ExplorerSettings)editor.getEnvironment().getConfigStore();
        final String settingsKey = SettingNames.SYSTEM + "/" + "SQML_EDITOR" + "/" + SPLITTER_KEY_NAME;
        if (splitter.isCollapsed(1)) {
            settings.writeDouble(settingsKey, 1.0);
        } else if (splitter.isCollapsed(0)) {
            settings.writeDouble(settingsKey, 0.0);
        } else {
            settings.writeDouble(settingsKey, ((double) splitter.getCurrentPosition()) / editor.width());
        }
    }

    private void loadSplitterSettings() {
        final ExplorerSettings settings = (ExplorerSettings)editor.getEnvironment().getConfigStore();
        final String key = SettingNames.SYSTEM + "/" + "SQML_EDITOR" + "/" + SPLITTER_KEY_NAME;
        if (settings.contains(key)) {
            final double ratio = settings.readDouble(key);
            if (ratio == 0) {
                splitter.collapse(0);
            } else if (ratio == 1.0) {
                splitter.collapse(1);
            } else {
                splitter.moveToPosition((int) (ratio * editor.width()));
            }
        } else {
            splitter.moveToPosition(editor.width() / 2);
        }
    }

    public void showEvent(final QShowEvent event) {
        loadSplitterSettings();
        splitter.splitterMoved.connect(this, "saveSplitterSettings()");
    }

    public void addAliases(final Map<String, Id> aliases) {
        if (aliases.keySet() == null || aliases.isEmpty()) {
            return;
        }        
        ISqmlTableDef table;
        final List<ISqmlDefinition> tables = new ArrayList<>();
        for (Map.Entry<String, Id> entry : aliases.entrySet()) {
            if(!tablesAliases.containsKey(entry.getKey())) {
                tablesAliases.put(entry.getKey(), entry.getValue());
                table = editor.getEnvironment().getSqmlDefinitions().findTableById(entry.getValue());
                if (table != null) {
                    tables.add(table.createCopyWithAlias(entry.getKey()));
                }
            }
        }
        if (!tables.isEmpty()) {
            if (proxyModel==null){
                fillPropTree(propTree, tables);
            }else{
                final SqmlTreeModel sqmlModel = (SqmlTreeModel) proxyModel.sourceModel();
                sqmlModel.addTopLevelDefinitions(tables);
                propTree.expandToDepth(0);
            }
        }
        updateTabSet();
        updateRightPanelVisiblity();
    }

    private class SqmlDefinitionsTree extends QTreeView {

        SqmlDefinitionsTree(final QWidget parent) {
            super(parent);
            setItemDelegate(new QItemDelegate(this));
        }

        @Override
        protected void currentChanged(final QModelIndex current, final QModelIndex previous) {
            super.currentChanged(current, previous);
            onTreeItemClick(current);
        }

        @Override
        public QSize sizeHint() {
            final QSize size = super.sizeHint();
            if (model() != null && model().columnCount() > 0) {
                int width = sizeHintForColumn(0) + frameWidth() * 2;
                if (verticalScrollBar().isVisible()) {
                    width += verticalScrollBar().width();
                }
                size.setWidth(width);
            }
            return size;
        }
    }

    public void addToolButton(final QToolButton toolBtn) {
        toolBar.addToolButton(toolBtn);
    }

    public void insertToolButton(final QToolButton toolBtn) {
        toolBar.insertToolButton(toolBtn);
    }
    
    void clearAliases() {
        if (!tablesAliases.isEmpty()){
            final SqmlTreeModel sqmlModel = proxyModel==null ? null : (SqmlTreeModel) proxyModel.sourceModel();
            if (sqmlModel!=null){
                final List<ISqmlDefinition> tables = new LinkedList<>();
                ISqmlTableDef table;
                for (Map.Entry<String, Id> entry : tablesAliases.entrySet()) {
                    table = editor.getEnvironment().getSqmlDefinitions().findTableById(entry.getValue());
                    tables.add(table.createCopyWithAlias(entry.getKey()));
                }
                sqmlModel.removeTopLevelDefinitions(tables);
                tablesAliases.clear();
            }
            updatePropTree();
            updateRightPanelVisiblity();
        }
    }
}