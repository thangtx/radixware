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
package org.radixware.wps.rwt.tree;

import java.awt.Color;
import java.util.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.ValueEditor;
import org.radixware.wps.rwt.tree.Node.Cell;
import org.radixware.wps.rwt.tree.Node.INodeCellEditor;
import org.radixware.wps.rwt.tree.Node.INodeCellRenderer;

import org.radixware.wps.rwt.tree.Node.NodeRow;
import org.radixware.wps.views.editors.valeditors.IValEditor;
import org.radixware.wps.views.editors.valeditors.ValEditorController;
import org.radixware.wps.views.editors.valeditors.ValEditorFactory;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;

public class Tree extends UIObject implements IGrid {

    public interface ICellRendererProvider {

        INodeCellRenderer newCellRenderer(Node node, int columnIndex);
    }

    public interface ICellEditorProvider {

        INodeCellEditor newCellEditor(Node node, int columnIndex);
    }
    
    public static interface INodeVisitor{
        void visit(Node node, boolean isRootNode);
        boolean interrupt(Node node, boolean isRootNode);
        boolean ignoreChildren(Node node, boolean isRootNode);
    }
    
    public static abstract class AbstractNodeVisitor implements INodeVisitor{

        @Override
        public boolean interrupt(final Node node, final boolean isRootNode) {
            return false;
        }

        @Override
        public boolean ignoreChildren(final Node node, final boolean isRootNode) {
            return false;
        }        
    }
    
    private static class MaxLevelCalculator implements INodeVisitor{
        
        private int maxLevel = 0;
        private final boolean processRootNode;
        
        public MaxLevelCalculator(final boolean processRoot){
            processRootNode = processRoot;
        }

        @Override
        public void visit(final Node node, final boolean isRootNode) {
            if ((!isRootNode || processRootNode) && node.isNodeVisible()){                
                if (node.lastComputedLevel < 0) {
                    node.updateLevel();
                }
                maxLevel = Math.max(maxLevel, node.lastComputedLevel);
            }            
        }

        @Override
        public boolean interrupt(final Node node, final boolean isRootNode) {
            return false;
        }

        @Override
        public boolean ignoreChildren(final Node node, final boolean isRootNode) {
            return !node.isNodeVisible();
        }       
        
        public int getMaxLevel(){
            return maxLevel;
        }
    }

    public static class EditMaskRenderer implements INodeCellRenderer {

        private EditMask editMask;
        private IValEditor editor;
        private final IClientEnvironment e;
        private ECellEditingMode editMode;
        private final Cell editingCell;

        private void setModeOpts(ECellEditingMode mode, ValEditorController controller) {
            switch (mode) {
                case NULL_VALUE_ACCEPTED:
                    controller.setReadOnly(false);
                    controller.setMandatory(false);
                    break;
                case NULL_VALUE_RESTRICTED:
                    controller.setReadOnly(false);
                    controller.setMandatory(true);
                    break;
                case READ_ONLY:
                    controller.setReadOnly(true);
                    break;
            }
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
        }

        @Override
        public void selectionChanged(Node node, int c, Object value, ICell cell, boolean isSelected) {
        }

        @Override
        @SuppressWarnings("unchecked")
        public UIObject getUI() {
            if (editor == null) {
                this.editor = ValEditorFactory.getDefault().createValEditor(null, editMask, e);

                UIObject uio = (UIObject) editor;
                uio.getHtml().addClass("renderer");
                uio.setHeight(21);
            } else {
                final UIObject parent = ((UIObject) editor).getParent();
                if (parent != null) {
                    parent.getHtml().addClass("editor-cell");
                }
            }

            editor.addValueChangeListener(new ValueEditor.ValueChangeListener() {
                @Override
                public void onValueChanged(Object oldValue, Object newValue) {
                    if (editingCell != null) {
                        editingCell.setUserData(newValue);
                    }
                }
            });

            return (UIObject) editor;
        }

        public EEditMaskType getEditMaskType() {
            return editMask.getType();
        }

        public EditMaskRenderer(EditMask editMask, IClientEnvironment env, Cell cell) {
            e = env;
            this.editMask = editMask;
            this.editingCell = cell;
        }

        public EditMaskRenderer(EditMask editMask, IClientEnvironment env) {
            e = env;
            this.editMask = editMask;
            editingCell = null;
        }

        public Object getEditorValue() {
            if (editor != null) {
                return editor.getValue();
            }
            return null;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void update(final Node node, final int c, final Object value) {
            if (editor == null) {
                return;
            }
            Cell cell = node.getCells().get(c);
            Column col = node.getTree().getColumn(c);
            IEditingOptions cellOpts = cell.getEditingOptions();
            IEditingOptions colOpts = col.getEditingOptions();

            EditMask cellMask = cellOpts.getEditMask();
            EditMask colMask = colOpts.getEditMask();
            ECellEditingMode cellMode = cellOpts.getEditingMode();
            ECellEditingMode colMode = colOpts.getEditingMode();
            if (cellMask != null) {
                editMask = cellMask;
            } else {
                editMask = colMask;
            }
            final ValEditorController controller = editor.getController();
            controller.setEditMask(editMask);
            editMode = cellMode != null ? cellMode : colMode;
            setModeOpts(editMode, controller);
            editor.setValue(value);
        }
    }

    protected List<ColumnDescriptor> getAllColumnDescriptors() {
        return null;
    }

    protected List<ColumnDescriptor> getVisibleColumnDescriptors(List<ColumnDescriptor> all) {
        return null;
    }
    
    protected boolean showRestoreDefaultColumnSettingsButton(){
        return false;
    }
    
    protected void restoreDefaultColumnSettings(){
        
    }    

    private void setupColumnsVisiblity() {
        final List<ColumnDescriptor> allColumns = getAllColumnDescriptors();
        if (allColumns==null){
            return;
        }
        final List<ColumnDescriptor> visibleColumns = getVisibleColumnDescriptors(allColumns);
        if (visibleColumns==null){
            return;
        }
        final IGrid.SetupColumnVisibilityDialog dialog = 
            new IGrid.SetupColumnVisibilityDialog(getEnvironment(), allColumns,  visibleColumns, true, showRestoreDefaultColumnSettingsButton());
        if (dialog.execDialog(this)==DialogResult.ACCEPTED){
            updateColumnsVisibility(dialog.getSelectedColumns());
            updateColumnsResizingMode();            
        }else if (dialog.needToRestoreDefaultSettings()){
            restoreDefaultColumnSettings();
            updateColumnsResizingMode();           
        }
    }
    
    protected void updateColumnsVisibility(List<ColumnDescriptor> visibleColumns) {
        if (visibleColumns == null) {//default behaviour
            boolean visible[] = new boolean[header.getColumnsCount()];
            int i = 0;
            for (Column c : header) {
                visible[i] = c.isVisible();
                i++;
            }
            for (Node r : getRootNode().getChildNodes().getCreatedNodes()) {
                for (i = 0; i < visible.length; i++) {
                    if (i < r.getCells().size()) {
                        r.getCells().get(i).setVisible(visible[i]);
                    }
                }
            }
        }
    }

    public class Column extends IGrid.AbstractColumn {

        private ICellRendererProvider rendererProvider;
        private ICellEditorProvider editorProvider;

        private Column(final AbstractColumnHeaderCell cell) {
            super(cell, "$RWT.tree.onHeaderClick", "$RWT.tree.onContextMenu");
        }

        @Override
        public void updateCellsRenderer() {
            visitNodes(new AbstractNodeVisitor() {
                @Override
                public void visit(final Node node, final boolean isRootNode) {
                    if (!node.getCells().isEmpty() && getColumnCount() > getIndex()) {
                        node.getCells().get(getIndex()).updateRenderer();
                    }
                }
            }, true);
        }

        public ICellRendererProvider getCellRendererProvider() {
            if (rendererProvider != null) {
                return rendererProvider;
            }
            return new ICellRendererProvider() {
                @Override
                public INodeCellRenderer newCellRenderer(Node node, int columnIndex) {
                    Cell cell = columnIndex >= 0 && getColumnCount() > columnIndex ? node.getCells().get(columnIndex) : null;
                    Column column = getColumn(columnIndex);
                    EditMask colmask = column == null ? null : column.getEditingOptions().getEditMask();
                    EditMask mask = (colmask != null ? colmask : null);
                    if (mask != null && cell != null) {
                        return new Tree.EditMaskRenderer(mask, getEnvironment(), cell);
                    } else {
                        if (getIndex() == 0) {
                            return new Node.DefaultTreeColumnCellRenderer();
                        } else {
                            return new Node.DefaultTreeCellRenderer();
                        }
                    }
                }
            };
        }

        public ICellEditorProvider getCellEditorProvider() {
            if (editorProvider != null) {
                return editorProvider;
            } else {
                return null;
            }
        }

        public void setCellRendererProvider(final ICellRendererProvider renderer) {
            if (renderer!=rendererProvider){
                rendererProvider = renderer;
                final int index = getIndex();
                visitNodes(new AbstractNodeVisitor() {
                    @Override
                    public void visit(final Node node, final boolean isRootNode) {
                        final Cell cell = node.getCells().get(index);
                        if (!cell.isEditMode()){
                            cell.update(index, false);
                        }
                    }
                }, true);
            }
        }

        public void setCellEditorProvider(final ICellEditorProvider editor) {
            if (editorProvider!=editor){
                editorProvider = editor;                
                final int index = getIndex();
                visitNodes(new AbstractNodeVisitor() {
                    @Override
                    public void visit(final Node node, final boolean isRootNode) {
                        final Cell cell = node.getCells().get(index);
                        if (cell.isEditMode()){
                            cell.update(index, true);
                        }
                    }
                }, true);                
            }            
        }

        int getIndex() {
            for (int i = 0; i < getColumnCount(); i++) {
                if (getColumn(i) == this) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void setVisible(boolean isVisible) {
            super.setVisible(isVisible);
            updateColumnsVisibility(null);
            Tree.this.updateColumnsResizingMode();
        }
        
        private void setCanResize(final boolean isResizable){
            getHeaderCell().setResizable(isResizable);
            final int nextColumnIndex = getIndex()+1;
            if (nextColumnIndex<getColumnCount()){
                getColumn(nextColumnIndex).getHeaderCell().setPrevCellResizable(isResizable);
            }            
        }
        
        private void setResizingColumnIdx(final int idx){
            getHeaderCell().getHtml().setAttr(IGrid.IColumn.RESIZE_COLUMN_INDEX_ATTR_NAME, idx);            
            getHeaderCell().setResizable(idx>=0);
            final int nextColumnIndex = getIndex()+1;
            if (nextColumnIndex<getColumnCount()){
                getColumn(nextColumnIndex).getHeaderCell().setPrevCellResizable(idx>=0);
            }
        }        

        @Override
        protected void afterChangeSizePolicy() {
            Tree.this.updateColumnsResizingMode();
        }

        @Override
        protected void notifyColumnHeaderClick(final EnumSet<EKeyboardModifier> modifiers) {
            Tree.this.notifyHeaderClick(this, modifiers);
        }

        @Override
        protected void setupColumnsVisiblity() {
            Tree.this.setupColumnsVisiblity();
        }                
    }
    

    public interface BranchListener {

        void afterExpand(final Node node);        
        
        void afterCollapse(final Node node);
    }    
    
    private void updateColumnsResizingMode(){
        final IGrid.EColumnSizePolicy[] actualSizePolicy = columnsResizeController.calcFinalSizePolicy();                    
        for (int i=0,count=getColumnCount(); i<count; i++){
            getColumn(i).applySizePolicy(actualSizePolicy[i]);
        }
        for (int i=0,count=getColumnCount(); i<count; i++){
            final int resizingColumnIdx = columnsResizeController.findSectionToResize(i);
            getColumn(i).setResizingColumnIdx(resizingColumnIdx);
        }
    }

    @Override
    public final Column addColumn(String title) {
        return addColumn(-1, title);
    }

    @Override
    public void setPersistenceKey(String key) {
        super.setPersistenceKey(key);
    }

    @Override
    public void removeColumn(final int index) {
        final Node selected;
        if (getSelectedNode() != null
                && !getSelectedNode().isSpanned()) {
            selected = getSelectedNode();
        } else {
            selected = null;
        }
        header.removeColumn(index);        

        visitNodes(new AbstractNodeVisitor() {
            @Override
            public void visit(final Node node, final boolean isRootNode) {
                node.removeCell(index);
            }
        }, true);
        updateColumnsResizingMode();
    }

    @Override
    public Column addColumn(final int index, final String title) {
        return addColumn(index, title, null);
    }

    @Override
    public Column addColumn(final String title, final AbstractColumnHeaderCell columnHeaderCell) {
        return addColumn(-1, title, columnHeaderCell);
    }

    @Override
    public Column addColumn(final int index, final String title, final AbstractColumnHeaderCell columnHeaderCell) {
        final Column c = header.addColumn(index, title, columnHeaderCell);
        if (root != null) {
            root.ensureColumnCount(index, getColumnCount());
        }
        updateColumnsResizingMode();
        return c;
    }        

    public interface DoubleClickListener {

        public void nodeDoubleClick(Node item);
    }

    public interface HeaderClickListener {

        void onClick(final Column column, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }        
    
    public interface RowHeaderClickListener{
        void onClick(final Node node, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }
    
    public interface RowHeaderDoubleClickListener{
        void onDoubleClick(final Node node, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }    
        
    private static final class RowHeader extends IGrid.AbstractRowHeader{
        
        private List<RowHeaderClickListener> clickListeners;
        private List<RowHeaderDoubleClickListener> dblClickListeners;        
        
        private final Map<AbstractRowHeaderCell, VerticalHeaderCell> rowCell2HeaderCell = new HashMap<>();
        private final Map<VerticalHeaderCell, Node> headerCell2Node = new HashMap<>();
        private final List<Node> nodesInPlainOrder = new ArrayList<>();
        
        public RowHeader(final UIObject owner){
            super(owner); 
        }
        
        public void addClickListener(final RowHeaderClickListener listener){
            if (listener!=null){
                if (clickListeners==null){
                    clickListeners = new LinkedList<>();
                    getHtml().setAttr("onclick", "$RWT.gridLayout.onVerticalHeaderCellClick");
                }
                if (!clickListeners.contains(listener)){
                    clickListeners.add(listener);
                }
            }
        }
        
        public void removeClickListener(final RowHeaderClickListener listener){
            if (clickListeners!=null && !clickListeners.isEmpty() && listener!=null){
                clickListeners.remove(listener);
                if (clickListeners.isEmpty()){
                    clickListeners = null;
                    getHtml().setAttr("onclick", null);
                }
            }
        }        

        @Override
        protected void notifyClick(final VerticalHeaderCell vHeaderCell, final EnumSet<EKeyboardModifier> modifiers) {
            if (clickListeners!=null){
                final Node node = headerCell2Node.get(vHeaderCell);
                if (node!=null){
                    final List<RowHeaderClickListener> listeners = new LinkedList<>(clickListeners);
                    for (RowHeaderClickListener listener: listeners){
                        listener.onClick(node, modifiers);
                    }
                }
            }
        }
        
        public void addDoubleClickListener(final RowHeaderDoubleClickListener listener){
            if (listener!=null){
                if (dblClickListeners==null){
                    dblClickListeners = new LinkedList<>();
                    getHtml().setAttr("ondblclick", "$RWT.gridLayout.onVerticalHeaderCellDblClick");
                }
                if (!dblClickListeners.contains(listener)){
                    dblClickListeners.add(listener);
                }
            }
        }
        
        public void removeDoubleClickListener(final RowHeaderDoubleClickListener listener){
            if (dblClickListeners!=null && !dblClickListeners.isEmpty() && listener!=null){
                dblClickListeners.remove(listener);
                if (dblClickListeners.isEmpty()){
                    dblClickListeners = null;
                    getHtml().setAttr("ondblclick", null);
                }
            }
        }        

        @Override
        protected void notifyDoubleClick(final VerticalHeaderCell vHeaderCell, final EnumSet<EKeyboardModifier> modifiers) {
            if (dblClickListeners!=null){
                final Node node = headerCell2Node.get(vHeaderCell);
                if (node!=null){
                    final List<RowHeaderDoubleClickListener> listeners = new LinkedList<>(dblClickListeners);
                    for(RowHeaderDoubleClickListener listener: listeners){
                        listener.onDoubleClick(node, modifiers);
                    }
                }
            }
        }
        
        public void addRow(final int index, final Node node){
            final AbstractRowHeaderCell rowHeaderCell = node.getRowHeaderCell();
            final VerticalHeaderCell headerCell = new VerticalHeaderCell(this, rowHeaderCell);            
            addHeaderCell(index, headerCell);
            node.setVerticalHeaderCell(headerCell);
            rowCell2HeaderCell.put(rowHeaderCell, headerCell);
            headerCell2Node.put(headerCell, node);            
            if (index<0 || index>=nodesInPlainOrder.size()){
                nodesInPlainOrder.add(node);
            }else{
                nodesInPlainOrder.add(index,node);
            }            
        }
        
        public void removeAllRows(){
            clearHeaderCells();
            rowCell2HeaderCell.clear();
            headerCell2Node.clear();
            for (Node node: nodesInPlainOrder){
                node.setVerticalHeaderCell(null);
            }            
            nodesInPlainOrder.clear();
        }
        
        public void removeRow(final Node node){
            final AbstractRowHeaderCell rowHeaderCell = node.getRowHeaderCell();
            final VerticalHeaderCell headerCell = rowCell2HeaderCell.get(rowHeaderCell);
            if (headerCell!=null){
                removeHeaderCell(headerCell);
                rowCell2HeaderCell.remove(rowHeaderCell);
                headerCell2Node.remove(headerCell);                
                node.setVerticalHeaderCell(null);
            }
            nodesInPlainOrder.remove(node);
        }
        
        public boolean isEmpty(){
            return nodesInPlainOrder.isEmpty();
        }
        
        @Override
        public UIObject findObjectByHtmlId(final String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            for (VerticalHeaderCell cell: headerCell2Node.keySet()) {
                result = cell.findObjectByHtmlId(id);
                if (result!=null){
                    return result;
                }
            }
            return null;
        }

        @Override
        public void visit(final Visitor visitor) {            
            for (VerticalHeaderCell cell: headerCell2Node.keySet()) {
                cell.visit(visitor);
            }
            super.visit(visitor);
        }           
    }
    
    private Node root;
    private Node selectedNode = null;
    private boolean isRootVisible = true;
    private Html content = new Html("tbody");    
    private Html table = new Html("table");
    private final Column treeColumn;
    private final IGrid.AbstractHorizontalHeader<Tree.Column> header = new IGrid.AbstractHorizontalHeader<Tree.Column>(){
        @Override
        public Column createColumn(final AbstractColumnHeaderCell headerCell) {
            return new Column(headerCell);
        }        
    };
    
    private IGrid.ColumnsResizeController columnsResizeController = new IGrid.ColumnsResizeController(this);
    private final Div data = new Div();
    private boolean borderShown = false;
    private boolean browserFocusFrameEnabled = true;
    private Color currentNodeFrameColor = Color.decode("#3399ff");
    private Color currentCellFrameColor = Color.decode("#404040");
    private final IGrid.FilterEditorController filterController;
    private final ValueEditor.ValueChangeListener<String> filterChangeListener = 
            new ValueEditor.ValueChangeListener<String>(){
                    @Override
                    public void onValueChanged(String oldValue, String newValue) {
                        applyFilter(newValue);
                    }        
            };
    private final RowHeader rowHeader = new RowHeader(this);
    private boolean isVerticalHeaderVisible;
    private EnumSet<IGrid.ESelectionStyle> selectionStyle = EnumSet.of(IGrid.ESelectionStyle.BACKGROUND_COLOR);
    private boolean blockBranchListeners;
    private String currentFilter;
    private List<BranchListener> branchListeners;
    private final List<DoubleClickListener> listeners = new LinkedList<>();
    private final List<NodeListener> selectionListeners = new LinkedList<>();
    private final IGrid.RowFrame currentNodeFrame;
    private final IGrid.CellFrame currentCellFrame;
    private final Html outerContainer = new Div();//contains data and hHeader

    public Tree() {
        super(new Div());
        
        outerContainer.addClass("rwt-grid-outer-container");
        outerContainer.setCss("overflow", "none");
        outerContainer.add(data);
        
        this.html.add(outerContainer);
        this.html.setCss("overflow", "hidden");
        this.data.setAttr("role", "data");
        //this.data.setCss("position", "absolute");
        this.data.setCss("overflow", "auto");
        this.data.addClass("rwt-grid-data-panel");
        this.data.addClass("rwt-tree");
        this.data.add(this.table);

        //this.data.setCss("width", "100%");
        this.table.add(this.content);
        this.table.setCss("border-collapse", "collapse");
        this.table.setCss("border-spacing", "0px 0px");
        this.table.setCss("border", "none");
        this.table.setAttr("cellspacing", "0");
        this.table.setAttr("cellpadding", "0");
        this.data.setAttr("onscroll", "$RWT.tree._syncScroll");
        //   this.table.setCss("width", "100%");
        this.html.layout("$RWT.tree.layout");
        treeColumn = addColumn("");//"Tree"
        setBorderVisible(false);
        showHeader(true);//no header for explorer tree
        currentNodeFrame = new IGrid.RowFrame(data, "rowFrame");
        currentNodeFrame.setColor(currentNodeFrameColor);
        currentCellFrame = new IGrid.CellFrame(data, "cellFrame");
        currentCellFrame.setColor(currentCellFrameColor);
        filterController = new IGrid.FilterEditorController(getHtml(), filterChangeListener);
    }

    public void setBackgroundColor(String hexColor) {
        data.setCss("background-color", hexColor);
    }

    public Color getBackgoundColor() {
        return Color.decode(data.getCss("background-color"));
    }

    public final void showHeader(boolean show) {
        if (show) {
            if (header.getHtml().getParent() == null) {
                outerContainer.add(0, header.getHtml());
                header.setVisible(true);
            }
        } else {
            header.setVisible(false);
        }
    }    

    public void setHeaderAlignment(final Alignment a) {        
        header.setTextAlignment(a);
    }

    public Alignment getHeaderAlignment() {
        return header.getTextAlignment();
    }

    public Node getRootNode() {
        synchronized (this) {
            return this.root;
        }
    }

    public void shadeEvenRow(int opacityPercent) {
        if (table != null) {
            table.setAttr("alpha", opacityPercent);
        }
    }
    private Color shadeColor;

    public void setShadeColor(Color color) {
        if (table != null && color != null) {
            shadeColor = color;
            String c = "rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";//add alpha componenet
            table.setAttr("shadeColor", c);
        }
    }

    public Color getShadeColor() {
        return shadeColor;
    }

    @Override
    public String getHeaderSettings() {
        return header.getSettings();
    }

    @Override
    public void setHeaderSettings(String settings) {
        header.setSettings(settings);
    }

    public void setRootNode(Node root) {
        synchronized (this) {
            if (this.root != null) {
                this.root.setParent(null);
                this.content.clear();
            }
            this.root = root;
            if (this.root != null) {
                this.root.setParent(this);
                attachNode(root);
                root.setHiddenRoot(!isRootVisible);
            }

        }
    }   
    
    @Override
    public void setCurrentCellFrameColor(Color frameColor){
        if (frameColor!=null && !frameColor.equals(currentCellFrameColor)){
            currentCellFrameColor = frameColor;
            currentCellFrame.setColor(frameColor);
        }
    }

    @Override
    public Color getCurrentCellFrameColor(){
        return currentCellFrameColor;
    }
    
    public void setCurrentNodeFrameColor(Color frameColor) {
        if (frameColor!=null && !frameColor.equals(currentNodeFrameColor)){
            currentNodeFrameColor = frameColor;
            currentNodeFrame.setColor(frameColor);
        }
    }
    
    public Color getCurrentNodeFrameColor() {
        return currentNodeFrameColor;
    }
    
    @Override
    public final EnumSet<IGrid.ESelectionStyle> getSelectionStyle(){
        return selectionStyle.clone();
    }
    
    @Override
    public void setSelectionStyle(final EnumSet<IGrid.ESelectionStyle> newStyle){
        if (newStyle!=null && !selectionStyle.equals(newStyle)){
            final boolean enablingBackgroundColor = 
                !selectionStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR) && newStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR);
            final boolean disablingBackgroundColor = 
                selectionStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR) && !newStyle.contains(IGrid.ESelectionStyle.BACKGROUND_COLOR);
            selectionStyle.clear();
            selectionStyle.addAll(newStyle);
            if (selectedNode!=null){
                if (enablingBackgroundColor || disablingBackgroundColor){
                    selectedNode.updateMarkedColors();
                }
                updateFrames();
            }
        }
    }    

    public void setRootVisible(boolean visible) {
        //if (isRootVisible != visible) {
        isRootVisible = visible;
        if (root != null) {
            root.setHiddenRoot(!visible);
        }
        //}
    }

    public boolean isRootVisible() {
        return isRootVisible;
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject res = super.findObjectByHtmlId(id);
        if (res != null) {
            return res;
        }
        if (root != null) {
            res = root.findObjectByHtmlId(id);
        }
        if (res == null) {
            res = header.findObjectByHtmlId(id);
        }
        if (res == null){
            res = rowHeader.findObjectByHtmlId(id);
        }
        return res;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public interface NodeListener {

        public void selectionChanged(Node oldSelection, Node newSelection);
    }    

    public void addSelectionListener(NodeListener listener) {
        synchronized (selectionListeners) {
            selectionListeners.add(listener);
        }
    }

    public void removeSelectionListener(NodeListener listener) {
        synchronized (selectionListeners) {
            selectionListeners.remove(listener);
        }
    }

    private void fireSelectionChange(Node oldNode, Node newNode) {
        synchronized (selectionListeners) {
            for (NodeListener l : selectionListeners) {
                l.selectionChanged(oldNode, newNode);
            }
        }
    }    

    final void dblClickNode(Node node) {
        synchronized (listeners) {
            setSelectedNode(node);
            for (DoubleClickListener l : listeners) {
                l.nodeDoubleClick(node);
            }
        }
    }

    public void addDoubleClickListener(DoubleClickListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeDoubleClickListener(DoubleClickListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }
    private List<HeaderClickListener> headerClickListeners;

    public void addHeaderClickListener(final HeaderClickListener l) {
        if (l != null) {
            if (headerClickListeners == null) {
                headerClickListeners = new LinkedList<>();
            }
            if (!headerClickListeners.contains(l)) {
                headerClickListeners.add(l);
            }
        }
    }

    public void removeHeaderClickListener(final HeaderClickListener l) {
        if (headerClickListeners != null && l != null) {
            headerClickListeners.remove(l);
        }
    }

    private void notifyHeaderClick(final Column column, final EnumSet<EKeyboardModifier> keyboardModifiers) {
        if (headerClickListeners != null) {
            List<HeaderClickListener> list;
            list = new LinkedList<>(headerClickListeners);
            for (HeaderClickListener listener : list) {
                listener.onClick(column, keyboardModifiers.clone());
            }
        }
    }
    
    public void addRowHeaderClickListener(final RowHeaderClickListener l){
        rowHeader.addClickListener(l);
    }
    
    public void removeRowHeaderClickListener(final RowHeaderClickListener l){
        rowHeader.removeClickListener(l);
    }
    
    public void addRowHeaderDoubleClickListener(final RowHeaderDoubleClickListener l){
        rowHeader.addDoubleClickListener(l);
    }
    
    public void removeRowHeaderDoubleClickListener(final RowHeaderDoubleClickListener l){
        rowHeader.removeDoubleClickListener(l);
    }    

    void attachNode(Node node) {
        if (node.getTree() != this) {
            return;
        }
        if (node.getHtml().getParent() != content) {
            for (Cell cell: node.getCells()){
                cell.update(cell.getIndex(), false);
            }
            if (node.getHtml().getParent() != null) {
                node.getHtml().remove();
            }
            if (node == root) {
                addNodeToTable(-1, node);
            } else {
                Node parentNode = node.getParentNode();
                if (parentNode == null) {
                    return;
                } else {
                    attachNode(parentNode);
                }

                int index = content.indexOfChild(parentNode.getHtml());
                if (index < 0) {
                    return;
                }
                int selfIndex = parentNode.indexOfChild(node);
                int realIndex = -1;

                if (selfIndex < 0) {
                    return;
                }
                if (selfIndex == 0) {
                    realIndex = index + 1;
                } else {
                    Node prevNode = parentNode.getChildAt(selfIndex - 1);
                    //look for prev node
                    int prevNodeIndex = -1;
                    for (int i = index + 1, len = content.childCount(); i < len; i++) {
                        NodeRow rowHtml = (NodeRow) content.getChildAt(i);
                        if (rowHtml == prevNode.getHtml()) {
                            prevNodeIndex = i;
                            break;
                        }
                    }
                    if (prevNodeIndex < 0) {
                        return;
                    }
                    for (int i = prevNodeIndex + 1, len = content.childCount(); i < len; i++) {
                        NodeRow rowHtml = (NodeRow) content.getChildAt(i);
                        Node nodeByRow = rowHtml.getNode();
                        if (!nodeByRow.isChildOf(prevNode)) {
                            realIndex = i;
                            break;
                        }
                    }
                }

                addNodeToTable(realIndex, node);                
            }
        }
    }        
    
    void dettachNode(final Node node){
        removeNodeFromTable(node);
        node.setParent(null);
    }
    
    private void addNodeToTable(final int index, final Node node){
        content.add(index, node.getHtml());
        rowHeader.addRow(index, node); 
        updateVerticalHeaderVisibility();
    }
    
    private void removeNodeFromTable(final Node node){
        rowHeader.removeRow(node);
        updateVerticalHeaderVisibility();
        content.remove(node.getHtml());
    }

    public final void scrollToNode(Node node) {
        this.getHtml().setAttr("scrolledToNode", node.getHtmlId());
    }
    
    public final void setSelectedNode(Node node) {
        synchronized (this) {
            final Node oldSelectedNode = selectedNode;
            if (oldSelectedNode != node) {
                if (onChangeSelection(oldSelectedNode, node)) {
                    if (oldSelectedNode != null) {
                        oldSelectedNode.markAsNotSelected();
                    }
                    this.selectedNode = node;
                    if (this.selectedNode != null) {
                        this.selectedNode.markAsSelected();
                    }
                    updateFrames();
                    fireSelectionChange(oldSelectedNode, node);
                }
            }
        }
    }

    public Node findNodeByPath(int[] path) {

        Node parent = getRootNode();
        if (path.length == 0) {
            return parent;
        }
        Node selection = null;
        for (int i = 0; i < path.length; i++) {
            int currentIndex = path[i];
            if (parent.getChildCount() > currentIndex) {
                selection = parent.getChildAt(currentIndex);
                parent = selection;
            } else {
                if (parent.getChildCount() > 0) {
                    selection = parent.getChildAt(parent.getChildCount() - 1);
                    break;
                }
            }
        }
        return selection;
    }

    public void expandNode(Node node) {
        while (node != null) {
            node.expand();
            node = node.getParentNode();
        }
    }        

    public void expandAllNodes() {
        visitNodes(new INodeVisitor() {
            @Override
            public void visit(final Node node, final boolean isRootNode) {
                node.expand();
            }

            @Override
            public boolean interrupt(final Node node, final boolean isRootNode) {
                return false;
            }

            @Override
            public boolean ignoreChildren(final Node node, final boolean isRootNode) {
                return false;
            }
            
        }, false);
    }

    public void collapseAllNodes() {
        visitNodes(new INodeVisitor() {
            @Override
            public void visit(final Node node, final boolean isRootNode) {
                node.collapse();
            }
            
            @Override
            public boolean interrupt(final Node node, boolean isRootNode) {
                return false;
            }

            @Override
            public boolean ignoreChildren(final Node node, boolean isRootNode) {
                return false;
            }            
            
        }, true);        
    }

    public void clearTree() {
        if (root.getChildNodes() != null && root.getChildCount() > 0) {
            for (Node node : root.getChildNodes().getCreatedNodes()) {
                node.remove();
            }
        }
    }

    protected boolean onChangeSelection(final Node oldSelection, final Node newSelection) {
        return true;
    }

    private boolean findNode(Node lookup, Node node, List<String> path) {
        if (lookup == node) {
            return true;
        }
        for (Node child : lookup.getChildNodes().getNodes()) {
            if (findNode(child, node, path)) {
                path.add(0, child.getHtmlId());
                return true;
            }
        }
        return false;
    }

    public String getNodePath(Node node) {
        if (root == null || node == null) {
            return null;
        }
        List<String> pathElements = new LinkedList<>();
        if (findNode(root, node, pathElements)) {
            StringBuilder sb = new StringBuilder();

            for (String id : pathElements) {
                sb.append('/');
                sb.append(id);
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    @Override
    public final void setBorderVisible(boolean showBorder) {
        borderShown = showBorder;
        table.setAttr(IGrid.SHOW_BORDER_ATTR_NAME, showBorder ? "true" : null);
    }

    @Override
    public boolean isBorderVisible() {
        return borderShown;
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/client.js","org/radixware/wps/rwt/tree/tree.js"};
    }

    @Override
    protected String[] clientCssRequired() {
        return new String[]{"org/radixware/wps/rwt/tree/tree.css", "org/radixware/wps/rwt/grid.css"};
    }

    @Override
    public int getColumnCount() {
        return header.getColumnsCount();
    }

    void updateMaxLevel() {
        final MaxLevelCalculator calculator = new MaxLevelCalculator(isRootVisible());
        visitNodes(calculator, true);
        data.setAttr("max-level", calculator.getMaxLevel() * 20);
    }
    
    void updateFrames(){
        currentNodeFrame.hide();
        currentCellFrame.hide();
        if (selectedNode!=null){
            final int level = selectedNode.getPath().size() - 1 - (isRootVisible() ? 0 : 1);
            final int offsetX = level*15 + 7;
            if (getSelectionStyle().contains(IGrid.ESelectionStyle.ROW_FRAME)){
                currentNodeFrame.setRow(selectedNode);
                currentNodeFrame.setOffsetLeft(offsetX);
            }
            if (selectedNode.getCurrentCell()!=null && getSelectionStyle().contains(IGrid.ESelectionStyle.CELL_FRAME)){
                final int index = selectedNode.getCurrentCell().getIndex();
                int visibleCellIndex = index;
                for (int i=0,count=getColumnCount();i<index && i<count;i++){
                    if (!getColumn(i).isVisible()){
                        visibleCellIndex--;
                    }
                }
                currentCellFrame.setCell(selectedNode, visibleCellIndex);
                currentCellFrame.setOffsetLeft(index==0 ? offsetX : 0);
            }
        }
    }

    public Column getTreeColumn() {
        return treeColumn;
    }        

    @Override
    public Column getColumn(int index) {
        return header.getColumn(index);
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        this.header.visit(visitor);
        rowHeader.visit(visitor);
    }        
    
    @Override
    public void setBrowserFocusFrameEnabled(boolean enabled){
        if (browserFocusFrameEnabled!=enabled){
            browserFocusFrameEnabled = enabled;
            if (enabled){
                data.removeClass("rwt-grid-disable-standard-focus-frame");
            }else{
                data.addClass("rwt-grid-disable-standard-focus-frame");
            }
        }
    }

    @Override
    public boolean isBrowserFocusFrameEnabled(){
        return browserFocusFrameEnabled;
    }
    
    @Override
    public final void setFilterEditor(final ValStrEditorController editor){
        filterController.setFilterEditor(editor);
    }
    
    @Override
    public final ValStrEditorController getFilterEditor(){
        return filterController.getFilterEditor();
    }    
    
    @Override
    public void processAction(final String actionName, final String actionParam) {    
        if ("filter".equals(actionName)){
            applyFilter(actionParam);
        }else{
            super.processAction(actionName, actionParam);
        }
    }
    
    public final void setRowHeaderVisible(final boolean isVisible){
        if (isVisible!=isVerticalHeaderVisible){
            isVerticalHeaderVisible = isVisible;
            updateVerticalHeaderVisibility();
        }
    }
    
    public final boolean isRowHeaderVisible(){
        return isVerticalHeaderVisible;
    }   
    
    private void updateVerticalHeaderVisibility(){
        final boolean currentVisibility = getHtml().getChildAt(0)==rowHeader.getHtml();
        final boolean vHeaderActualVisibility = isVerticalHeaderVisible && !rowHeader.isEmpty();
        if (currentVisibility!=vHeaderActualVisibility){
            if (vHeaderActualVisibility){
                rowHeader.getHtml().renew();
                getHtml().add(0, rowHeader.getHtml());
            }else{
                getHtml().remove(rowHeader.getHtml());
            }            
        }
    }    
    
    public final void addBranchListener(final BranchListener listener){
        if (listener!=null){
            if (branchListeners==null){
                branchListeners = new LinkedList<>();
            }
            if (!branchListeners.contains(listener)){
                branchListeners.add(listener);
            }
        }
    }
    
    public final void removeBranchListener(final BranchListener listener){
        if (listener!=null && branchListeners!=null){
            branchListeners.remove(listener);
            if (branchListeners.isEmpty()){
                branchListeners = null;
            }
        }
    }
    
    void notifyBranchListeners(final Node node, final boolean notifyExpanded){
        if (branchListeners!=null && !blockBranchListeners){
            final List<BranchListener> copy = new LinkedList<>(branchListeners);
            for (BranchListener listener: copy){
                if (notifyExpanded){
                    listener.afterExpand(node);
                }else{
                    listener.afterCollapse(node);
                }
            }
        }
    }   
    
    public final void addFilterListener(final FilterListener listener){
        filterController.addFilterListener(listener);
    }
    
    public final void removeFilterListener(final FilterListener listener){
        filterController.removeFilterListener(listener);
    }
    
    private void notifyFilterListeners(final String filter){
        filterController.notifyFilterListeners(filter);
    }
    
    private void applyFilter(final String filter){
        if (!Objects.equals(currentFilter, filter)){
            currentFilter = filter;
            applyCurrentFilter();
        }
    }        
    
    private boolean applyFilter(final Node node, final String filter){
        boolean isVisible = false;
        if (node.isLeaf()){            
            isVisible = node.isMatchToFilter(filter);
        }else{
            for (Node childNode: node.getChildNodes().getNodes()){
                if (applyFilter(childNode, filter)){
                    isVisible = true;
                }
            }            
            if (!isVisible){
                isVisible = node.getFilterRules()!=null && node.isMatchToFilter(filter);
            }else{
                if (filter!=null && !filter.isEmpty()){
                    if (!node.isExpanded()){
                        node.expandOnFilter();
                    }
                }else if (node.isExpandedOnFilter()){
                    node.collapse();
                }
            }
        }
        node.setVisible(isVisible);
        return isVisible;        
    }
    
    @Override
    public void applyCurrentFilter(){
        blockBranchListeners = true;
        try{
            if (isRootVisible()){
                applyFilter(getRootNode(), currentFilter);
            }else{
                for (Node node: getRootNode().getChildNodes().getNodes()){
                    applyFilter(node, currentFilter);
                }
            }
        }finally{
            blockBranchListeners = false;
        }        
        notifyFilterListeners(currentFilter);
    }    
    
    public void visitNodes(final INodeVisitor visitor, final boolean createdNodesOnly){
        final Node rootNode = getRootNode();
        if ( rootNode != null) {
            final Stack<Node> nodes = new Stack<>();
            nodes.push(rootNode);
            Node node;
            boolean isRoot = true;
            while (!nodes.isEmpty()) {
                node = nodes.pop();
                if (visitor.interrupt(node, isRoot)){
                    break;
                }
                visitor.visit(node, isRoot);
                if (!visitor.ignoreChildren(node, isRoot)){
                    final Collection<Node> childNodes;
                    if (createdNodesOnly){
                        childNodes = node.getChildNodes().getCreatedNodes();                    
                    }else{
                        childNodes = node.getChildNodes().getNodes();
                    }
                    for (Node childNode : childNodes) {
                        nodes.push(childNode);
                    }
                }
                if (isRoot){
                    isRoot = false;
                }                
            }
        }      
    }
}