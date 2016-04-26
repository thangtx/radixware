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
import org.radixware.kernel.common.client.enums.EMouseButton;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Table;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.Events;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.LabeledEditGrid;
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

    public static class EditingOptions implements IEditingOptions {

        private ECellEditingMode mode;
        private final Cell cell;
        private final Column column;
        private EditMask mask = null;

        EditingOptions(Cell cell) {
            mode = null;
            this.cell = cell;
            this.column = null;
        }

        EditingOptions(Column col) {
            mode = ECellEditingMode.NULL_VALUE_ACCEPTED;
            this.cell = null;
            this.column = col;
        }

        @Override
        public EditMask getEditMask() {
            if (mask == null) {
                return null;
            }
            return EditMask.newCopy(mask);
        }

        @Override
        public ECellEditingMode getEditingMode() {
            return mode;
        }

        @Override
        public void setEditMask(EditMask editMask) {
            if (mask == null && editMask == null) {
                return;
            }
            mask = EditMask.newCopy(editMask);
            if (cell != null) {
                cell.updateCurrentState();
            }
            if (column != null) {
                column.updateCellsAtColumn();
            }
        }

        @Override
        public void setEditingMode(ECellEditingMode editMode) {
            if (editMode == null) {
                mode = ECellEditingMode.NULL_VALUE_ACCEPTED;
            }
            if (editMode == mode) {
                return;
            }
            this.mode = editMode;

            if (cell != null) {
                cell.updateCurrentState();
            }
            if (column != null) {
                column.updateCellsAtColumn();
            }
        }
    }

    private class Header extends UIObject {

        private Table table = new Table();
        private Table.Row row = table.addRow();
        private Map<Html, Column> columns = new HashMap<>();

        public Header() {
            super(new Div());
            html.setCss("overflow", "hidden");
            html.add(table);
            this.table.setCss("cellspacing", "0px");
            this.table.setCss("cellpadding", "0px");
            this.table.setCss("border-collapse", "collapse");
            this.table.setCss("border", "none");

            html.setCss("width", "100%");
            html.setCss("position", "relative");
            html.setAttr("role", "header");
            html.setCss("height", "20px");
            html.addClass("rwt-grid-header-panel");
            this.table.setCss("position", "relative");
        }

        public void addColumn(int index, Column column) {
            Table.DataCell cell = row.addCell(index);
            cell.addClass("rwt-grid-row-cell");
            cell.setCss("border-top", "none");
            cell.setCss("border-bottom", "none");
            cell.setCss("padding", "0");
            cell.add(column.getHtml());

            column.setParent(this);
            columns.put(cell, column);
            headerAlign(getHeaderAlignment());
            cell.setAttr(IGrid.IColumn.SIZE_POLISY_ATTR_NAME, column.getSizePolicy().getHtmlAttrValue());            
        }

        public void removeColumn(int index) {
            if (header.getColumnCount() > index) {
                Column c = getColumn(index);
                c.setParent(null);
                row.remove(row.getCell(index));
                columns.remove(c.getHtml());
            }
        }

        public void headerAlign(Alignment a) {
            if (a != null) {
                String align;
                switch (a) {
                    case RIGHT:
                        align = "right";
                        break;
                    case CENTER:
                        align = "center";
                        break;
                    case LEFT:
                        align = "left";
                        break;
                    default:
                        align = "center";
                        break;
                }

                if (columns != null && !columns.isEmpty()) {
                    for (Column c : columns.values()) {
                        c.getHtml().setCss("text-align", align);
                    }
                }
            }
        }

        public int getColumnCount() {
            return row.cellCount();
        }

        public Column getColumn(int index) {
            return columns.get(row.getCell(index));
        }

        public Column getTreeColumn() {
            return getColumn(0);
        }

        @Override
        public void visit(Visitor visitor) {
            super.visit(visitor);
            if (columns != null) {
                for (Column c : columns.values()) {
                    c.visit(visitor);
                }
            }
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            }
            if (columns != null) {
                for (Column c : columns.values()) {
                    result = c.findObjectByHtmlId(id);
                    if (result != null) {
                        return result;
                    }
                }
            }
            if (Objects.equals(id, table.getId())){
                return this;//process resizeAction
            }
            return null;
        }

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (IGrid.RESIZE_ACTION_NAME.equals(actionName) && actionParam != null) {
                headerSettings = actionParam;
            }else{
                super.processAction(actionName, actionParam);
            }
        }
        
        Html getCell(final int columnIndex){
            return row.getCell(columnIndex);
        }
    }        

    protected List<ColumnDescriptor> getAllColumnDescriptors() {
        return null;
    }

    protected List<ColumnDescriptor> getVisibleColumnDescriptors(List<ColumnDescriptor> all) {
        return null;
    }

    private class SetupColumnVisibilityDialog extends Dialog {

        private class VCheckBox extends CheckBox {

            ColumnDescriptor desc;

            VCheckBox(ColumnDescriptor desc) {
                setText(desc.getTitle());
                this.desc = desc;
            }
        }

        public SetupColumnVisibilityDialog(final IClientEnvironment env) {
            super(env, null);
            this.setWindowTitle(getEnvironment().getMessageProvider().translate("Tree", "Columns Visibility"));
            List<ColumnDescriptor> all = getAllColumnDescriptors();
            if (all == null) {
                return;
            }
            List<ColumnDescriptor> visible = getVisibleColumnDescriptors(all);
            if (visible == null) {
                return;
            }
            this.getHtml().setAttr("dlgId", "visibleColumns");
            this.setWidth(300);
            this.setHeight(300);
            LabeledEditGrid grid = createLabeledEditGrid();
            this.add(grid);
            List<SetupColumnVisibilityDialog.VCheckBox> cbss = fillColumnDescription(all, visible, grid);

            grid.setTop(3);
            grid.setLeft(3);
            grid.getAnchors().setRight(new Anchors.Anchor(1, -3));
            grid.getAnchors().setBottom(new Anchors.Anchor(1, -3));
            this.addCloseAction(EDialogButtonType.OK);
            this.addCloseAction(EDialogButtonType.CANCEL);

            if (this.execDialog() == IDialog.DialogResult.ACCEPTED) {
                List<ColumnDescriptor> descriptor = new LinkedList<>();
                for (SetupColumnVisibilityDialog.VCheckBox cb : cbss) {
                    if (cb.isSelected()) {
                        descriptor.add(cb.desc);
                    }
                }
                updateColumnsVisibility(descriptor);
                Tree.this.updateColumnsResizingMode();
            }
        }

        private LabeledEditGrid createLabeledEditGrid() {
            return new LabeledEditGrid(new LabeledEditGrid.AbstractEditor2LabelMatcher() {
                @Override
                protected UIObject createLabelComonent(UIObject editorComponent) {
                    return new Label();
                }
            });
        }

        private List<SetupColumnVisibilityDialog.VCheckBox> fillColumnDescription(List<ColumnDescriptor> all, final List<ColumnDescriptor> visible, LabeledEditGrid grid) {
            final List<SetupColumnVisibilityDialog.VCheckBox> cbss = new LinkedList<>();
            for (ColumnDescriptor desc : all) {
                SetupColumnVisibilityDialog.VCheckBox cb = new SetupColumnVisibilityDialog.VCheckBox(desc);
                grid.addEditor(cb, 0, -1);
                cb.setSelected(visible.contains(desc));
                cbss.add(cb);
                if (visible.size() == 1) {
                    cb.setEnabled(!visible.contains(desc));
                }
                cb.addSelectionStateListener(new CheckBox.SelectionStateListener() {
                    @Override
                    public void onSelectionChange(CheckBox cb) {
                        int j = 0;
                        for (int i = 0; i < cbss.size(); i++) {
                            if (!cbss.get(i).isSelected()) {
                                j++;
                            }
                            int index = findUncheckedItemIndex(cbss);
                            cbss.get(i).setEnabled(i != index && i != 0);
                            cbss.get(index).setEnabled(!(j == cbss.size() - 1) && index != 0);
                        }
                    }
                });
            }
            cbss.get(0).setEnabled(false);//root node always visble
            return cbss;
        }

        private int findUncheckedItemIndex(List<SetupColumnVisibilityDialog.VCheckBox> cbss) {
            for (SetupColumnVisibilityDialog.VCheckBox cb : cbss) {
                if (cb.isSelected()) {
                    return cbss.indexOf(cb);
                }
            }
            return cbss.size() - 1;
        }
    }

    private void setupColumnsVisiblity() {
        Dialog dlg = new SetupColumnVisibilityDialog(getEnvironment());
        dlg.execDialog(null);
    }

    protected void updateColumnsVisibility(List<ColumnDescriptor> visibleColumns) {
        if (visibleColumns == null) {//default behaviour
            boolean visible[] = new boolean[header.columns.size()];
            int i = 0;
            for (Column c : header.columns.values()) {
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

    public class Column extends UIObject implements IColumn {

        private Html label = new Html("label");
        private Html srtDirectionIndicator, srtSequenceIndicator;
        private ICellRendererProvider rendererProvider;
        private ICellEditorProvider editorProvider;
        private Object userData;
        private final IEditingOptions editOpts = new EditingOptions(this);
        private boolean isEditable = true;
        private EColumnSizePolicy policy;
        private Html handle = new Html("span");
        private WpsIcon icon;
        private Div imageContainer;

        @Override
        public IEditingOptions getEditingOptions() {
            return editOpts;
        }

        private Column(String title) {
            super(new Div());
            html.setCss("overflow", "hidden");
            html.add(label);
            label.setInnerText(title);
            html.setCss("padding-top", "3px");
            html.setCss("height", "17px");
            label.setCss("white-space", "nowrap");
            html.addClass("header-cell");
            html.setCss("vertical-align", "middle");
            handle.setCss("width", "1px");
            handle.setCss("height", "100%");
            handle.setCss("float", "right");
            handle.setCss("padding-left", "0");
            handle.setCss("padding-top", "0");
            handle.addClass("header-handle");
            html.add(handle);
            html.setAttr("onclick", "$RWT.tree.onHeaderClick");
            html.setAttr("oncontextmenu", "$RWT.tree.onContextMenu");
            setSizePolicyImpl(EColumnSizePolicy.INTERACTIVE);            
        }

        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                if (actionParam == null || actionParam.isEmpty()) {
                    processClickEvent(0);
                } else {
                    final int keyModifiers;
                    try {
                        keyModifiers = Integer.parseInt(actionParam);
                    } catch (NumberFormatException exception) {
                        processClickEvent(0);
                        return;
                    }
                    processClickEvent(keyModifiers);
                }
            } else {
                super.processAction(actionName, actionParam);
            }
        }

        @Override
        public void setTitle(String title) {
            label.setInnerText(title);
        }

        @Override
        public String getTitle() {
            return label.getInnerText();
        }

        protected void updateCellsAtColumn() {
            if (getRootNode() != null) {
                final Stack<Node> nodes = new Stack<>();
                nodes.push(getRootNode());
                Node node;
                while (!nodes.isEmpty()) {
                    node = nodes.pop();
                    if (!node.getCells().isEmpty() && getColumnCount() > getIndex()) {
                        node.getCells().get(getIndex()).updateCurrentState();
                    }
                    for (Node childNode : node.getChildNodes().getNodes()) {
                        nodes.push(childNode);
                    }
                }
            }
        }

        public boolean isEditable() {
            return isEditable;
        }

        public void setEditable(boolean editable) {
            this.isEditable = editable;
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

        public void setCellRendererProvider(ICellRendererProvider renderer) {
            rendererProvider = renderer;
        }

        public void setCellEditorProvider(ICellEditorProvider editor) {
            editorProvider = editor;
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
        public int getFixedWidth() {
            String attr = getHeaderCell().getAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME);
            if (attr != null) {
                try {
                    return Integer.parseInt(attr);
                } catch (NumberFormatException e) {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        @Override
        public boolean isSetFixedWidth() {
            return getHeaderCell().getAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME) != null;
        }

        @Override
        public void setFixedWidth(final int fw) {
            getHeaderCell().setAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME, fw);
            setSizePolicy(IGrid.EColumnSizePolicy.FIXED);
        }
        
        

        @Override
        public void setIcon(final WpsIcon icon) {
            if (!Objects.equals(icon, this.icon)) {
                this.icon = icon;
            }
            if (icon == null) {
                if (imageContainer != null) {
                    imageContainer.remove();
                    imageContainer = null;
                }
            } else {
                final Html image;
                if (imageContainer == null) {
                    imageContainer = new Div();
                    image = new Html("img");
                    imageContainer.add(image);
                    image.setCss("width", "12px");
                    image.setCss("height", "12px");

                    imageContainer.setCss("vertical-align", "middle");
                    imageContainer.setCss("padding-top", "3px");
                    imageContainer.setCss("display", "inline");
                    imageContainer.setCss("margin-right", "3px");

                    html.add(0, imageContainer);
                } else {
                    image = imageContainer.getChildAt(0);
                }
                final String imageUri = getIcon().getURI(this);
                if (!Objects.equals(image.getAttr("src"), imageUri)) {
                    image.setAttr("src", imageUri);
                }
            }
        }

        @Override
        public WpsIcon getIcon() {
            return icon;
        }

        @Override
        public void unsetFixedWidth() {
            getHeaderCell().setAttr(IGrid.IColumn.FIXED_WIDTH_ATTR_NAME, null);
            setSizePolicy(IGrid.EColumnSizePolicy.INTERACTIVE);
        }

        @Override
        public int getInitialWidth() {
            String attr = getHeaderCell().getAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME);
            if (attr != null) {
                try {
                    return Integer.parseInt(attr);
                } catch (NumberFormatException e) {
                    return -1;
                }
            } else {
                return -1;
            }
        }

        @Override
        public boolean isSetInitialWidth() {
            return getHeaderCell().getAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME) != null;
        }

        @Override
        public void setInitialWidth(int iw) {
            getHeaderCell().setAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME, iw);
        }

        @Override
        public void unsetInitialWidth() {
            getHeaderCell().setAttr(IGrid.IColumn.INITIAL_WIDTH_ATTR_NAME, null);
        }

        @Override
        public int getWidth() {
            return super.getWidth();
        }

        @Override
        public final void setWidth(int w) {
            this.html.setCss("width", String.valueOf(w) + "px");
            this.html.setAttr("width", w);
        }

        @Override
        public Object getUserData() {
            return userData;
        }

        @Override
        public void setUserData(Object object) {
            this.userData = object;
        }

        private void processClickEvent(final int keyModifiers) {
            final EnumSet<EMouseButton> mouseButtons = EMouseButton.fromAwtBitMask(keyModifiers);
            if (mouseButtons.contains(EMouseButton.LEFT)) {
                Tree.this.notifyHeaderClick(this, EKeyboardModifier.fromAwtBitMask(keyModifiers));
            } else if (mouseButtons.contains(EMouseButton.RIGHT)) {
                Tree.this.setupColumnsVisiblity();
            }
        }

        @Override
        public void showSortingIndicator(final RadSortingDef.SortingItem.SortOrder direction,
                final int sequenceNumber) {
            if (sequenceNumber > 0) {
                if (srtSequenceIndicator == null) {
                    initSrtSequenceIndicator();
                }
                srtSequenceIndicator.setCss("visibility", null);
                srtSequenceIndicator.setInnerText(String.valueOf(sequenceNumber));
            } else if (srtSequenceIndicator != null) {
                srtSequenceIndicator.setCss("visibility", "hidden");
            }
            if (srtDirectionIndicator == null) {
                initSrtDirectionIndicator();
            }
            if (direction == RadSortingDef.SortingItem.SortOrder.ASC) {
                srtDirectionIndicator.setInnerText(SelectorSortUtils.ASC_ARROW);
            } else {
                srtDirectionIndicator.setInnerText(SelectorSortUtils.DESC_ARROW);
            }
            srtDirectionIndicator.setCss("visibility", null);
        }

        @Override
        public void hideSortingIndicator() {
            if (srtDirectionIndicator != null) {
                srtDirectionIndicator.setCss("visibility", "hidden");
            }
            if (srtSequenceIndicator != null) {
                srtSequenceIndicator.setCss("visibility", "hidden");
            }
        }

        @Override
        public boolean isSortingIndicatorVisible() {
            return srtDirectionIndicator != null && !"hidden".equals(srtDirectionIndicator.getCss("visibility"));
        }

        private void initSrtSequenceIndicator() {
            srtSequenceIndicator = createSrtIndicatorLabel();
            srtSequenceIndicator.setCss("font-size", "10px");
        }

        private void initSrtDirectionIndicator() {
            srtDirectionIndicator = createSrtIndicatorLabel();
            srtDirectionIndicator.setCss("font-size", "14px");
        }

        private Html createSrtIndicatorLabel() {
            final Html srtIndicator = new Html("label");
            html.add(srtIndicator);
            srtIndicator.addClass("rwt-ui-element");
            srtIndicator.setCss("white-space", "nowrap");
            srtIndicator.setCss("float", "right");
            srtIndicator.setCss("color", "dimgray");
            srtIndicator.setCss("font-weight", "bold");
            srtIndicator.setCss("height", "100%");
            return srtIndicator;
        }
        
        void setSizePolicyImpl(final EColumnSizePolicy newSizePolicy){
            if (getParent() instanceof Header){
                getHeaderCell().setAttr(IGrid.IColumn.SIZE_POLISY_ATTR_NAME, newSizePolicy.getHtmlAttrValue());
            }            
            policy = newSizePolicy;            
        }

        @Override
        public void setSizePolicy(final EColumnSizePolicy newSizePolicy) {
            if (policy!=newSizePolicy){
                if (newSizePolicy==EColumnSizePolicy.FIXED && !isSetFixedWidth()){
                    return;
                }
                setSizePolicyImpl(newSizePolicy);
                Tree.this.updateColumnsResizingMode();
            }
        }

        @Override
        public EColumnSizePolicy getSizePolicy() {
            return policy;
        }

        @Override
        public boolean isVisible() {
            return super.isVisible();
        }

        @Override
        public void setVisible(boolean isVisible) {
            super.setVisible(isVisible);
            updateColumnsVisibility(null);
            Tree.this.updateColumnsResizingMode();
        }
        
        private Html getHeaderCell(){
            return header.getCell(getIndex());
        }

        @Override
        public String getPersistenceKey() {
            return getHeaderCell().getAttr("ccid");
        }

        @Override
        public void setPersistenceKey(final String key) {
            getHeaderCell().setAttr("ccid", key);
        }                
        
        private void setCanResize(final boolean isResizable){
            handle.setAttr(IGrid.IColumn.RESIZABLE_ATTR_NAME, isResizable);
        }
    }
    

    public interface BranchListener {

        void afterExpand(final Node node);        
        
        void afterCollapse(final Node node);
    }
    
    public interface FilterListener{        
        void afterApplyFilter(final String filter);                
    }
    
    private void updateColumnsResizingMode(){
        final IGrid.EColumnSizePolicy[] actualSizePolicy = columnsResizeController.calcFinalSizePolicy();                    
        for (int i=0,count=getColumnCount(); i<count; i++){
            getColumn(i).setSizePolicyImpl(actualSizePolicy[i]);
        }
        for (int i=0,count=getColumnCount(); i<count; i++){
            final int resizingColumnIdx = columnsResizeController.findSectionToResize(i);
            final Column column = getColumn(i);
            column.getHtml().setAttr(IGrid.IColumn.RESIZE_COLUMN_INDEX_ATTR_NAME, resizingColumnIdx);
            column.setCanResize(resizingColumnIdx>=0);
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
    public void removeColumn(int index) {
        final Node selected;
        if (getSelectedNode() != null
                && !getSelectedNode().isSpanned()) {
            selected = getSelectedNode();
        } else {
            selected = null;
        }
        header.removeColumn(index);        

        if (getRootNode() != null) {
            final Stack<Node> nodes = new Stack<>();
            nodes.push(getRootNode());
            Node node;
            while (!nodes.isEmpty()) {
                node = nodes.pop();
                node.removeCell(index);
                for (Node childNode : node.getChildNodes().getCreatedNodes()) {
                    nodes.push(childNode);
                }
            }
        }
        updateColumnsResizingMode();
        //update borders
    }

    @Override
    public Column addColumn(final int index, final String title) {
        Column c = new Column(title);
        header.addColumn(index, c);
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
    private Node root;
    private Node selectedNode = null;
    private boolean isRootVisible = true;
    private Html content = new Html("tbody");
    private Html table = new Html("table");
    private final Column treeColumn;
    private Header header = new Header();
    private IGrid.ColumnsResizeController columnsResizeController = new IGrid.ColumnsResizeController(this);
    private String headerSettings = "";
    private Div data = new Div();
    private boolean borderShown = false;
    private boolean browserFocusFrameEnabled = true;
    private Color currentNodeFrameColor = Color.decode("#3399ff");
    private Color currentCellFrameColor = Color.decode("#404040");
    private ValStrEditorController filterEditor;
    private final ValueEditor.ValueChangeListener<String> filterChangeListener = 
            new ValueEditor.ValueChangeListener<String>(){
                    @Override
                    public void onValueChanged(String oldValue, String newValue) {
                        applyFilter(newValue);
                    }        
            };
    private EnumSet<IGrid.ESelectionStyle> selectionStyle = EnumSet.of(IGrid.ESelectionStyle.BACKGROUND_COLOR);
    private boolean blockBranchListeners;
    private String currentFilter;
    private List<BranchListener> branchListeners;
    private List<FilterListener> filterListeners;
    private final List<DoubleClickListener> listeners = new LinkedList<>();
    private final List<NodeListener> selectionListeners = new LinkedList<>();
    private final IGrid.RowFrame currentNodeFrame;
    private final IGrid.CellFrame currentCellFrame;

    public Tree() {
        super(new Div());
        this.html.add(data);
        this.html.setCss("overflow", "hidden");
        this.data.setAttr("role", "data");
        this.data.setCss("position", "absolute");
        this.data.setCss("overflow", "auto");
        this.data.addClass("rwt-tree");
        this.data.add(this.table);

        this.data.setCss("width", "100%");
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
                this.getHtml().add(0, header.getHtml());
                header.setVisible(true);
            }
        } else {
            header.setVisible(false);
        }
    }
    private Alignment headerAlignment;

    public void setHeaderAlignment(Alignment a) {
        this.headerAlignment = a;
        header.headerAlign(a);
    }

    public Alignment getHeaderAlignment() {
        return headerAlignment;
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
        return headerSettings;
    }

    @Override
    public void setHeaderSettings(String settings) {
        if (settings != null && !settings.isEmpty() && !settings.equals(headerSettings)) {
            this.headerSettings = settings;
            String[] hs = settings.split(";");
            if (hs != null && hs.length > 0) {
                for (int i = 0; i < hs.length; i++) {
                    String id = hs[i].split(":")[0];
                    int width = Integer.parseInt(hs[i].split(":")[1].split(",")[0]);
                    for (int j = 0; j < getColumnCount(); j++) {
                        Column column = getColumn(j);
                        if (column.isVisible()
                            && column.getPersistenceKey().equals(id) 
                            && column.getSizePolicy()==IGrid.EColumnSizePolicy.INTERACTIVE) {
                            column.setInitialWidth(width);
                        }
                    }
                }
            }
        }
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

    void attachNode(Node node) {
        if (node.getTree() != this) {
            return;
        }
        if (node.getHtml().getParent() != content) {
            if (node.getHtml().getParent() != null) {
                node.getHtml().remove();
            }
            if (node == root) {
                content.add(node.getHtml());
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

                content.add(realIndex, node.getHtml());
            }
        }
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
        if (getRootNode() != null) {
            final Stack<Node> nodes = new Stack<>();
            nodes.push(getRootNode());
            Node node;
            while (!nodes.isEmpty()) {
                node = nodes.pop();
                node.expand();
                for (Node childNode : node.getChildNodes().getNodes()) {
                    nodes.push(childNode);
                }
            }
        }
    }

    public void collapseAllNodes() {
        if (getRootNode() != null) {
            final Stack<Node> nodes = new Stack<>();
            nodes.push(getRootNode());
            Node node;
            while (!nodes.isEmpty()) {
                node = nodes.pop();
                node.collapse();
                for (Node childNode : node.getChildNodes().getCreatedNodes()) {
                    nodes.push(childNode);
                }
            }
        }
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
        return header.getColumnCount();
    }

    void updateMaxLevel() {
        int level[] = new int[]{0};
        if (root != null) {
            if (isRootVisible()){
                compiteMaxLevel(root, level);
            }else{
                for (Node node : root.getChildNodes().getCreatedNodes()) {
                    compiteMaxLevel(node, level);
                }             
            }
        }
        data.setAttr("max-level", level[0] * 20);
    }

    void compiteMaxLevel(Node root, int[] level) {
        if (root.isNodeVisible()) {
            int l = root.lastComputedLevel;
            if (l < 0) {
                root.updateLevel();
            }
            l = root.lastComputedLevel;
            if (level[0] < l) {
                level[0] = l;
            }
            for (Node node : root.getChildNodes().getCreatedNodes()) {
                compiteMaxLevel(node, level);
            }
        }
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
                currentCellFrame.setCell(selectedNode, index);
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
    
    public final void setFilterEditor(final ValStrEditorController editor){
        if (filterEditor!=editor){
            if (filterEditor!=null){
                filterEditor.removeValueChangeListener(filterChangeListener);
            }
            filterEditor = editor;
            final UIObject editorUI = editor==null ? null : (UIObject)editor.getValEditor();
            getHtml().setAttr("filterEditor", editorUI==null ? null : editorUI.getHtmlId());
            if (filterEditor!=null){
                filterEditor.addValueChangeListener(filterChangeListener);
            }
        }
    }
    
    public final ValStrEditorController getFilterEditor(){
        return filterEditor; 
    }    
    
    @Override
    public void processAction(final String actionName, final String actionParam) {    
        if ("filter".equals(actionName)){
            applyFilter(actionParam);
        }else{
            super.processAction(actionName, actionParam);
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
        if (listener!=null){
            if (filterListeners==null){
                filterListeners = new LinkedList<>();
            }
            if (!filterListeners.contains(listener)){
                filterListeners.add(listener);
            }
        }
    }
    
    public final void removeFilterListener(final FilterListener listener){
        if (listener!=null && filterListeners!=null){
            filterListeners.remove(listener);
            if (filterListeners.isEmpty()){
                filterListeners = null;
            }
        }
    }
    
    private void notifyFilterListeners(final String filter){
        if (filterListeners!=null){
            final List<FilterListener> copy = new LinkedList<>(filterListeners);
            for (FilterListener listener: copy){
                listener.afterApplyFilter(filter);
            }
        }
    }
    
    private void applyFilter(final String filter){
        if (!Objects.equals(currentFilter, filter)){
            blockBranchListeners = true;
            try{
                if (isRootVisible()){
                    applyFilter(getRootNode(), filter);
                }else{
                    for (Node node: getRootNode().getChildNodes().getNodes()){
                        applyFilter(node, filter);
                    }
                }
            }finally{
                blockBranchListeners = false;
            }
            currentFilter = filter;
            notifyFilterListeners(filter);
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
}