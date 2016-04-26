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

import org.radixware.kernel.common.client.types.FilterRules;
import org.radixware.kernel.common.html.Html;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.icons.images.WsIcons;
import org.radixware.wps.rwt.IGrid.ICell;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.IGrid.IEditingOptions;
import org.radixware.wps.rwt.tree.Tree.Column;
import org.radixware.wps.rwt.tree.Tree.EditMaskRenderer;
import org.radixware.wps.rwt.tree.Tree.EditingOptions;
import org.radixware.wps.rwt.tree.Tree.ICellEditorProvider;
import org.radixware.wps.rwt.tree.Tree.ICellRendererProvider;
import org.radixware.wps.text.WpsTextOptions;

public class Node extends UIObject {    

    public static abstract class Children {

        protected List<Node> nodes;
        private Node owner;
        public static final Children LEAF = new Children() {
            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            protected List<Node> createNodes() {
                return Collections.emptyList();
            }
        };

        public void reset() {
            synchronized (this) {
                if (nodes != null) {
                    for (Node node : new ArrayList<>(nodes)) {
                        node.getChildNodes().reset();
                        remove(node);
                    }
                    nodes = null;
                }
            }
        }

        public final List<Node> getCreatedNodes() {
            synchronized (this) {
                if (nodes != null) {
                    return new ArrayList<>(nodes);
                } else {
                    return Collections.emptyList();
                }
            }
        }

        public final List<Node> getNodes() {
            synchronized (this) {
                if (nodes == null) {
                    nodes = createNodes();
                    for (Node node : nodes) {
                        node.setParent(owner);
                    }
                }
                return Collections.unmodifiableList(nodes);
            }
        }

        public boolean isEmpty() {
            synchronized (this) {
                if (nodes != null) {
                    return nodes.isEmpty();
                } else {
                    return getNodes().isEmpty();
                }
            }
        }

        public void add(Node node) {
            add(-1, node);
        }

        public void add(int index, Node node) {
            if (nodes == null) {
                nodes = createNodes();
            }
            if (index < 0 || index >= nodes.size()) {
                nodes.add(node);
            } else {
                nodes.add(index, node);
            }
            node.setParent(owner);
            update();
        }

        private void remove(Node node) {
            if (nodes == null) {
                nodes = createNodes();
            }
            if (owner!=null && owner.getTree()!=null && owner.getTree().getSelectedNode()==node) {
                owner.getTree().setSelectedNode(null);
            }
            node.setCurrentCell(null);
            nodes.remove(node);
            node.setParent(null);
            update();
        }

        protected abstract List<Node> createNodes();

        private void update() {
            if (owner != null) {
                owner.refreshChildNodes();
            }
        }

        protected final Node getOwnerNode() {
            return owner;
        }
    }

    static class DefaultChildren extends Children {

        public DefaultChildren() {
        }

        @Override
        protected List<Node> createNodes() {
            return new LinkedList<>();
        }

        void sortChildren() {
            Collections.sort(nodes, new Comparator<Node>() {

                @Override
                public int compare(Node o1, Node o2) {
                    String t1 = o1.getDisplayName();
                    String t2 = o2.getDisplayName();
                    if (t1 == null || t2 == null) {
                        return 0;
                    }
                    return t1.compareTo(t2);
                }
            });
            for (Node node : nodes) {
                if (node instanceof DefaultNode) {
                    ((DefaultNode) node).sortChildren();
                }
            }
        }
    }

    public static class DefaultNode extends Node {

        public DefaultNode(String displayName) {
            super(displayName);
        }

        public DefaultNode() {
            super();
        }

        public DefaultNode(String displayName, WpsIcon icon) {
            super(displayName, icon);
        }

        public DefaultNode(String displayName, Children children) {
            super(displayName, children);
        }

        public DefaultNode(Children children) {
            super(children);
        }

        public DefaultNode(String displayName, WpsIcon icon, Children children) {
            super(displayName, icon, children);
        }

        public void add(int index, Node node) {
            Node.Children children = getChildNodes();
            if (children == Node.Children.LEAF) {
                children = new DefaultChildren();
                setChildNodes(children);
            }

            children.add(index, node);
        }

        public void add(Node node) {
            add(-1, node);
        }

        public void remove(Node node) {
            getChildNodes().remove(node);
        }

        public void sortChildren() {
            if (getChildNodes() instanceof DefaultChildren) {
                ((DefaultChildren) getChildNodes()).sortChildren();
            }
        }
    }

    public interface Key {
    }

    public static abstract class KeyChildren<T extends Key> {

        public void updateKeys(List<T> newKeys) {
        }

        public abstract Node createNodeForKey(T key);
    }
    private Children children;
    private boolean isExpanded;
    private boolean isAutoExpanded;
    private Object userData;    
    private static final Tree.DoubleClickListener NODE_DBL_CLICK_HANDLER = new Tree.DoubleClickListener() {
        @Override
        public void nodeDoubleClick(Node node) {

            Tree tree = node.getTree();
            if (tree != null) {
                tree.dblClickNode(node);
            }
        }
    };
    private static final ButtonBase.ClickHandler BUTTON_CLICK_HANDLER = new ButtonBase.ClickHandler() {
        @Override
        public void onClick(IButton source) {
            ((Button) source).toggleNode();
        }
    };

    private class Button extends ButtonBase {
        
        boolean isExpanded;

        public Button() {
            super(new Html("img"));
            html.setCss("width", "7px");
            html.setCss("height", "7px");
            html.setCss("cursor", "pointer");
            html.setCss("position", "relative");
            html.setCss("left", "0px");
            html.setCss("top", "-2px");
            setParent(Node.this);
            html.addClass("rwt-tree-node-indicator");
            addClickHandler(BUTTON_CLICK_HANDLER);
        }

        public void expand() {
            this.html.setAttr("src", WsIcons.EXPANDED.getURI(this));
            if (!isExpanded && !Node.this.isHiddenRoot){
                final Tree tree = Node.this.getTree();
                if (tree!=null){
                    tree.notifyBranchListeners(Node.this, true);
                }
                isExpanded = true;
            }
        }

        public void collapse() {
            this.html.setAttr("src", WsIcons.COLLAPSED.getURI(this));
            if (isExpanded && !Node.this.isHiddenRoot){
                final Tree tree = Node.this.getTree();
                if (tree!=null){                
                    tree.notifyBranchListeners(Node.this, false);
                }
                isExpanded = false;
            }
        }

        public void hide() {
            this.html.setAttr("src", WsIcons.LEAF.getURI(this));
            if (isExpanded && !Node.this.isHiddenRoot){
                final Tree tree = Node.this.getTree();
                if (tree!=null){                
                    tree.notifyBranchListeners(Node.this, false);
                }
                isExpanded = false;
            }            
        }

        private void toggleNode() {
            try {
                getNode().toggle();
                Tree tree = getTree();
                if (tree != null) {
                    Node node = tree.getSelectedNode();
                    if (node != null) {
                        Cell cell = node.getCurrentCell(getTree());
                        if (cell == null) {
                            cell = node.getNodeCell();
                        }
                        if (cell != null) {
                            cell.setFocused(true);
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        private Node getNode() {
            return (Node) getParent().getParent();
        }
    }

    private Cell getCurrentCell(final Tree tree) {
        if (tree == null) {
            return null;
        }
        Node node = tree.getSelectedNode();
        if (node == null) {
            return null;
        }
        return node.currentCell;
    }

    protected final class Cell extends UIObject implements ICell {

        protected INodeCellEditor editor = null;
        protected INodeCellRenderer renderer = null;
        private ICellRendererProvider rendererProvider = null;
        private WpsTextOptions textOptions = null;
        private Html contentElement;
        private Html buttonContainer;
        private Html[] innerHtmls = null;
        private Object userData;
        private Color markedBackground = Color.decode("#6495ED"), markedForeground = null;
        private Color foreground, background;
        private boolean foregroundSaved, backgroundSaved;
        private final IEditingOptions editOpts = new EditingOptions(this);
        private boolean isEditable = true;
        private boolean isSelected;
        private Div container = new Div();

        @Override
        public IEditingOptions getEditingOptions() {
            return editOpts;
        }

        public Cell(int index) {
            super(new Html("td"));

            if (index == 0) {
                innerHtmls = new Html[]{
                    new Html("table"),
                    new Html("tbody"),
                    new Html("tr"),
                    new Html("td"),
                    new Html("td")
                };
                Html table = innerHtmls[0];
                table.setCss("border-collapse", "collapse");
                Html tbody = innerHtmls[1];
                Html tr = innerHtmls[2];
                table.add(tbody);
                tbody.add(tr);
                Html td = innerHtmls[3];
                td.setCss("padding", "0");
                tr.add(td);
                buttonContainer = td;
                td = innerHtmls[4];
                td.setCss("padding", "0");
                //td.setCss("width", "100%");
                tr.add(td);
                html.add(table);
                contentElement = td;

            } else {
                contentElement = html;
            }
            contentElement.add(container);
            html.addClass("rwt-tree-node-cell");
            html.setAttr("tabindex", 1);
            html.setAttr("onclick", "$RWT.tree.onLeafClick");
            html.setAttr("ondblclick", "$RWT.tree.onLeafDblClick");
            html.setAttr("onkeydown", "$RWT.tree.node.keyDown");            
            setParent(Node.this);
            setForeground(Node.this.getForeground());
        }

        @Override
        public Object getUserData() {
            return userData;
        }

        @Override
        public void setUserData(Object userData) {
            this.userData = userData;
        }

        protected boolean isEditMode() {
            return editor != null;
        }

        protected void clearContent() {
            this.container.clear();
            if (renderer != null) {
                renderer.getUI().setParent(null);
            }
//            if (renderer != null) {
//                this.contentElement.remove(renderer.getUI().getHtml());
//            } else if (editor != null) {
//                this.html.remove(editor.getUI().getHtml());
//            }
        }

        protected Node getOwnerNode() {
            return Node.this;
        }

        @Override
        public void processAction(String actionName, String actionParam) {
            switch (actionName) {
                case Events.EVENT_NAME_ONDBLCLICK:
                    NODE_DBL_CLICK_HANDLER.nodeDoubleClick(Node.this);
                    break;
                case Events.EVENT_NAME_ONCLICK:
                    setCurrent(true);
                    break;
                case Events.EVENT_NAME_KEY_DOWN:
                    if ("27".equals(actionParam)) {//esc
                        if (isEditMode()) {
                            leaveEditMode(false);
                        }
                    } else if ("13".equals(actionParam)) {
                        if (isEditMode()) {
                            leaveEditMode(true);
                        } else {
                            if (getCurrentCell() == this) {
                                enterEditMode();
                            }
                        }
                    }
                    break;
            }
        }

        private void setCurrent(boolean clickAgain) {
            Cell curCell = getCurrentCell(getTree());
            if (curCell != this) {
                Node oldNode = null;
                Node curNode = getOwnerNode();

                if (curCell != null) {
                    oldNode = curCell.getOwnerNode();
                    if (curCell.leaveEditMode(true)) {
                        getTree().setSelectedNode(Node.this);
                        setCurrentCell(this);                        
                    }
                } else {
                    getTree().setSelectedNode(Node.this);
                    setCurrentCell(this);                                    
                }
                if (oldNode != curNode) {
                    if (oldNode != null) {
                        for (Cell c : oldNode.dataCells) {
                            if (c.renderer != null) {
                                c.renderer.rowSelectionChanged(false);
                            }
                        }
                    }
                    if (curNode != null) {
                        for (Cell c : curNode.dataCells) {
                            if (c.renderer != null) {
                                c.renderer.rowSelectionChanged(true);
                            }
                        }
                    }
                }
            } else {
                enterEditMode();
            }
        }

        private void enterEditMode() {
            if (!isEditMode() && getIndex() >= 0) {

                update(getIndex(), true);
                if (isEditMode()) {
                    getHtml().setAttr("mode", "edit");
                    getTree().getHtml().setAttr("editor", editor.getUI().getHtmlId());
                }

            } else {
                editor.getUI().setFocused(true);
            }
        }

        private boolean leaveEditMode(boolean applyChanges) {
            if (!isEditMode()) {
                return true;
            } else {
                if (applyChanges && editor != null) {
                    try {
                        editor.applyChanges();
                    } catch (Throwable e) {
                        getEnvironment().messageException("Apply value from editor exception", "User code exception", e);
                    }
                }

                update(getIndex(), false);
                if (!isEditMode()) {
                    getHtml().setAttr("mode", null);
                    getTree().getHtml().setAttr("editor", null);
                }
                return true;
            }
        }

        public void finishEdit(boolean applyChanges) {
            leaveEditMode(applyChanges);
        }

        public void finishEdit() {
            finishEdit(true);
        }

        public void add(UIObject obj) {
            add(-1, obj);
        }

        public void add(int index, UIObject obj) {
            html.add(index, obj.getHtml());
        }

        @Override
        public UIObject findObjectByHtmlId(String id) {
            UIObject result = super.findObjectByHtmlId(id);
            if (result != null) {
                return result;
            } else {
                if (editor != null) {
                    result = editor.getUI().findObjectByHtmlId(id);
                }
                if (result != null) {
                    return result;
                }
                if (renderer != null) {
                    result = renderer.getUI().findObjectByHtmlId(id);
                }
                if (result != null) {
                    if (getHtml().getClasses().indexOf("editor-cell") >= 0) {
                        return result;
                    } else {
                        return this;
                    }
                }
            }
            if (innerHtmls != null) {
                for (Html h : innerHtmls) {
                    if (h.getId().equals(id)) {
                        return this;
                    }
                }
            }
            return null;
        }

        protected int getIndex() {
            return dataCells == null ? -1 : dataCells.indexOf(this);
        }

        protected void update(int index, boolean showEditor) {
            if (showEditor) {
                ICellEditorProvider provider = getCellEditorProvider(index);
                if (provider != null) {
                    editor = provider.newCellEditor(Node.this, index);
                    final Object value = getCellValue(index);
                    editor.setValue(Node.this, index, value);
                    UIObject cell = editor.getUI();
                    if (cell != null) {
                        clearContent();
                        container.add(cell.getHtml());
                        cell.setParent(this);
                        cell.setFocused(true);
                    } else {
                        editor = null;//this cell is not editable
                    }
                }
            } else {
                if (isEditMode()) {
                    clearContent();
                    editor = null;
                }

                if (renderer == null || rendererProvider != getCellRendererProvider(index)) {
                    clearContent();
                    rendererProvider = getCellRendererProvider(index);
                    renderer = rendererProvider.newCellRenderer(Node.this, index);
                    renderer.getUI().setParent(this);
                }

                if (index > 0) {
                    html.addClass("rwt-grid-row-cell");
                }
                renderer.update(Node.this, index, getCellValue(index));
                if (container.childCount() == 0) {
                    final UIObject cell = renderer.getUI();
                    cell.setParent(null);//renew call
                    container.add(cell.getHtml());
                    cell.setParent(this);
                    cell.getHtml().setAttr("role", "view");
                }

                if (columnTextOptions != null) {
                    WpsTextOptions colOpts = getColumnTextOptions(index);
                    if (colOpts != null && !colOpts.isEmpty()) {
                        setBackground(colOpts.getBackgroundColor());
                        setForeground(colOpts.getForegroundColor());
                        if (colOpts.getAlignment() != null) {
                            renderer.getUI().getHtml().setCss("text-align", colOpts.getAlignment().getCssPropertyValue());
                        } else if (alignment != null) {
                            renderer.getUI().getHtml().setCss("text-align", alignment.name());
                        }
                    }
                }
            }
            if (textOptions != null) {
                setTextOptionsImpl(textOptions);
            }
        }

        protected void updateCurrentState() {
            if (editor == null && getTree() != null) {
                Node n = Node.this;
                int c = getIndex();
                Column column = getTree().getColumn(c);

                IEditingOptions colOpts = column.getEditingOptions();
                IEditingOptions cellopts = this.getEditingOptions();
                EditMask cellmask = cellopts.getEditMask();
                EditMask colmask = colOpts.getEditMask();
                EditMask currMask = cellmask != null ? cellmask : colmask;
                boolean isMaskRenderer = this.renderer instanceof EditMaskRenderer;

                if (currMask == null && isMaskRenderer) {
                    this.setEditable(true);
                    changeRenderer(n, c);
                } else if (currMask != null && !isMaskRenderer) {
                    this.setEditable(false);
                    changeRenderer(n, c);
                } else if (currMask != null && isMaskRenderer) {
                    EditMaskRenderer editMaskRenderer = (EditMaskRenderer) renderer;
                    if (!currMask.getType().equals(editMaskRenderer.getEditMaskType())) {
                        changeRenderer(n, c);
                    }
                }
            }
            this.renderer.update(Node.this, getIndex(), getCellValue(getIndex()));
        }

        public void setEditable(boolean isEditable) {
            this.isEditable = isEditable;
        }

        public boolean isEditable() {
            Column column = getTree().getColumn(this.getIndex());
            if (column.getCellEditorProvider() != null) {
                if (!Node.this.isEditable || (!column.isEditable())) {
                    return false;
                } else {
                    return isEditable;
                }
            } else {
                return false;
            }
        }

        private void changeRenderer(Node node, int c) {
            this.renderer.getUI().setParent(null);

            this.container.remove(this.renderer.getUI().getHtml());
            ICellRendererProvider newRendererProvider = getCellRendererProvider(c);
            if (rendererProvider != newRendererProvider) {
                rendererProvider = newRendererProvider;
            }
            this.renderer = rendererProvider.newCellRenderer(Node.this, c);
            this.renderer.getUI().setParent(this);
            this.container.add(this.renderer.getUI().getHtml());
        }

        void markSelected(final boolean isSelected) {
            if (this.isSelected!=isSelected){
                this.isSelected = isSelected;
                updateMarkedBackgound();
                updateMarkedForeground();
                if (this.renderer != null) {
                    this.renderer.selectionChanged(Node.this, getIndex(), getCellValue(getIndex()), this, isSelected);
                }
                if (isSelected && Node.this.currentCell==this){
                    html.removeChoosableMarker();
                    currentCell.getHtml().addClass("rwt-grid-current-cell");
                }else{
                    html.markAsChoosable();
                    if (currentCell!=null){
                        currentCell.getHtml().removeClass("rwt-grid-current-cell");
                    }
                }
            }
        }

        public void setSelectedFontColor(final Color color) {
            if (!Objects.equals(color, markedForeground)) {
                this.markedForeground = color;
                updateMarkedForeground();
            }
        }
        
        private void updateMarkedBackgound(){
            if (isSelected && getTree().getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)){
                background = getBackground();
                backgroundSaved = true;
                if (!getHtml().containsClass("rwt-ui-selected-item")){
                    getHtml().addClass("rwt-ui-selected-item");                        
                }                
                if (renderer != null) {
                    renderer.getUI().setBackground(markedBackground);
                }
                super.setBackground(markedBackground);
            }else if (backgroundSaved) {
                getHtml().removeClass("rwt-ui-selected-item");
                setBackground(background);
                backgroundSaved = false;
            }
        }
        
        private void updateMarkedForeground(){
            if (isSelected && getTree().getSelectionStyle().contains(IGrid.ESelectionStyle.BACKGROUND_COLOR)){
                foreground = getForeground();
                foregroundSaved = true;
                if (renderer != null) {
                    renderer.getUI().setForeground(markedForeground);
                }
                super.setForeground(markedForeground);                
            }else if (foregroundSaved){
                setForeground(foreground);
                foregroundSaved = false;
            }
        }

        @Override
        public void setForeground(Color c) {
            if (html.containsClass("rwt-ui-selected-item")) {
                this.foreground = c;
            }else{
                if (renderer != null) {
                    renderer.getUI().setForeground(c);
                }
                super.setForeground(c);
            }         
        }

        @Override
        public void setBackground(Color c) {
            if (html.containsClass("rwt-ui-selected-item")) {
                this.background = c;
            }else{
                if (renderer != null) {
                    renderer.getUI().setBackground(c);
                }
                super.setBackground(c);
            }
        }

        /*
        @Override
        public Color getForeground() {
            return this.foreground;
        }

        @Override
        protected ICssStyledItem getForegroundHolder() {
            if (renderer != null) {
                return renderer.getUI().getForegroundHolder();
            } else {
                return html;
            }
        }*/

        @Override
        public void setTextOptions(final WpsTextOptions options) {
            textOptions = options;
            super.setTextOptions(options);
            setTextOptionsImpl(textOptions);
        }

        public WpsTextOptions getTextOptions() {
            return textOptions;
        }

        private void setTextOptionsImpl(final WpsTextOptions options) {
            if (isEditMode() && editor != null && editor.getUI() != null) {
                editor.getUI().setTextOptions(options);
            } else if (!isEditMode() && renderer != null && renderer.getUI() != null) {
                renderer.getUI().setTextOptions(options);
            } else {
                super.setTextOptions(options);
            }
        }
        
        boolean isSelected(){
            return isSelected;
        }
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }
    private Cell currentCell = null;
    private final Button button;
    private WpsIcon icon = null;
    private HashMap<Integer, WpsTextOptions> columnTextOptions;
    private Alignment alignment;
    private FilterRules filterRules;

    public Node() {
        this("Leaf");
    }

    public Node(String displayName) {
        this(displayName, null, null);
    }

    public Node(String displayName, WpsIcon icon) {
        this(displayName, icon, Children.LEAF);
    }

    public Node(String displayName, Children children) {
        this(displayName, null, children);
    }

    public Node(Children children) {
        this("Leaf", null, children);
    }

    static class NodeRow extends Html {

        private Node node;

        public NodeRow() {
            super("tr");
        }

        private void setNode(Node node) {
            this.node = node;
        }

        Node getNode() {
            return node;
        }
    }

    public Node(String displayName, WpsIcon icon, Children children) {
        super(new NodeRow());
        ensureColumnCount(-1,1);
        Cell nodeCell = dataCells.get(0);        

        html.addClass("rwt-ui-tree-node");
        this.button = new Button();
        nodeCell.buttonContainer.add(button.getHtml());
        button.setParent(nodeCell);
        //nodeCell.add(0, button);
        button.hide();
        setCellValue(0, displayName);
        setChildNodes(children == null ? Children.LEAF : children);
        ((NodeRow) html).setNode(this);
        html.markAsChoosable();
    }

    public Tree getTree() {
        UIObject parent = getParent();
        while (parent != null) {
            if (parent instanceof Tree) {
                return (Tree) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }

    public Node getParentNode() {
        UIObject parent = getParent();
        while (parent != null) {
            if (parent instanceof Node) {
                return (Node) parent;
            }
            if (parent instanceof Tree) {
                return null;
            }
            parent = parent.getParent();
        }
        return null;
    }

    public void update() {
        //fireChange();
    }

    private Cell getNodeCell() {
        return dataCells == null ? null : dataCells.get(0);
    }

    private void detachChildren() {
        if (children != null) {
            for (Node node : children.getCreatedNodes()) {
                node.detachChildren();
                node.getHtml().remove();
            }
        }
    }

    public void setColumnTextOptions(int index, WpsTextOptions options) {
        if (columnTextOptions == null) {
            columnTextOptions = new HashMap<>();
        }
        if (options != null) {
            this.columnTextOptions.put(index, options);
        }
    }

    public WpsTextOptions getColumnTextOptions(int index) {
        if (columnTextOptions != null && !columnTextOptions.isEmpty() && columnTextOptions.containsKey(index)) {
            return columnTextOptions.get(index);
        } else {
            return WpsTextOptions.EMPTY;
        }
    }

    public void setAlignment(Alignment align) {
        if (align != null) {
            this.alignment = align;
            for (Cell c : dataCells) {
                if (c.renderer != null) {
                    c.renderer.getUI().getHtml().setCss("text-align", alignment.name());
                }
            }
        }
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void remove() {
        detachChildren();
        Node parentNode = getParentNode();
        if (parentNode != null) {
            parentNode.getChildNodes().remove(this);
            if (parentNode.getChildNodes().getNodes().isEmpty()) {
                parentNode.setChildNodes(Children.LEAF);
            }  
        }
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
        checkVisibility(false);
    }

    public final boolean isLeaf() {
        return children == Children.LEAF;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public final String getDisplayName() {
        //return this.nodeTitle.getTitle();
        return String.valueOf(getCellValue(0));
    }

    public final void setDisplayName(String displayName) {
        setCellValue(0, displayName);
        //this.nodeTitle.setTitle(displayName);
    }

    public final Children getChildNodes() {
        return children == null ? Children.LEAF : children;
    }

    private void refreshChildNodes() {
        this.checkExpandedState();
    }

    public final void setChildNodes(Children children) {
        if (this.children != null) {

            if (this.html.getParent() != null) {
                List<Node> cns = this.children.getCreatedNodes();
                for (Node n : cns) {
                    n.getHtml().remove();
                    n.setParent(null);
                }
            }
            this.children.owner = null;
        }
        this.children = children == null ? Children.LEAF : children;
        if (this.children == Children.LEAF) {
            this.button.hide();
        } else {
            this.children.owner = this;

            List<Node> cns = this.children.getCreatedNodes();
            for (Node n : cns) {
                n.setParent(this);
            }
        }
        updateInternalMargins();
        checkExpandedState();
    }

    private void updateInternalMargins() {
    }

    private void checkExpandedState() {
        for (Cell cell : this.dataCells) {
            cell.leaveEditMode(false);
        }

        final Tree tree = getTree();        
        if (isExpanded) {
            if (children != Children.LEAF) {
                if (isExpanded){
                    this.button.expand();
                }
                if (tree != null) {
                    for (Node node : new ArrayList<>(children.getNodes())) {
                        tree.attachNode(node);
                        node.checkVisibility(false);
                        node.checkExpandedState();

                    }
                }
            }
        } else {
            if (children != Children.LEAF) {
                this.button.collapse();                
                if (tree != null) {
                    for (Node node : new ArrayList<>(children.getCreatedNodes())) {
                        node.checkVisibility(false);
                        node.checkExpandedState();
                    }
                }
            }
        }        
        if (tree != null) {
            tree.updateMaxLevel();
        }
    }
    
    
    private boolean isVisible = true;
    boolean isHiddenRoot = false;

    void setHiddenRoot(boolean isHiddenRoot) {
        this.isHiddenRoot = isHiddenRoot;
        checkVisibility(true);
        if (isHiddenRoot) {
            expand();
        } else {
            checkExpandedState();
        }
    }

    private boolean isVisibleInTree() {
        Node parent = getParentNode();
        if (parent != null) {
            return parent.isVisible && parent.isExpanded && parent.isVisibleInTree();
        } else {
            return true;
        }
    }

    private void checkVisibility(boolean checkLevels) {
        if (isHiddenRoot) {
            html.setCss("display", "none");
        }
        if (checkLevels) {
            updateLevel();
        }
        if (isVisible && !isHiddenRoot && isVisibleInTree()) {
            html.setCss("display", null);
        } else {
            html.setCss("display", "none");
        }
        if (children != null) {
            for (Node node : new ArrayList<>(children.getCreatedNodes())) {
                node.checkVisibility(checkLevels);
            }
        }
    }

    boolean isNodeVisible() {
        return !"none".equals(html.getCss("display"));
    }

    public final WpsIcon getIcon() {
        return icon;
    }

    public final void setIcon(WpsIcon icon) {
        if (icon == null) {
            if (this.icon != null) {
                this.icon = null;
                this.getNodeCell().updateCurrentState();
            }
        } else {
            if (this.icon == null || this.icon != icon) {
                this.icon = icon;
                this.getNodeCell().updateCurrentState();
            }
        }
        updateInternalMargins();
    }

    public void toggle() {
        this.isExpanded = !this.isExpanded;
        isAutoExpanded = false;
        checkExpandedState();
    }

    public void expand() {
        this.isExpanded = true;
        isAutoExpanded = false;
        checkExpandedState();
    }    

    public void collapse() {
        this.isExpanded = false;
        isAutoExpanded = false;
        checkExpandedState();
    }

    public Node findChildById(String id) {
        List<Node> childList = getChildNodes().getNodes();
        if (childList == null || childList.isEmpty()) {
            return null;
        }
        for (Node c : childList) {
            if (Utils.equals(c.getHtmlId(), id)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void visit(Visitor visitor) {
        super.visit(visitor);
        //nodeCell.visit(visitor);
        if (this.dataCells != null) {
            for (Cell cell : dataCells) {
                cell.visit(visitor);
            }
        }
        if (this.button != null) {
            this.button.visit(visitor);
        }
        //     this.nodeTitle.visit(visitor);
        if (this.children != null) {
            for (Node node : new ArrayList<Node>(this.children.getCreatedNodes())) {
                node.visit(visitor);
            }
        }
    }

    @Override
    public UIObject findObjectByHtmlId(String id) {
        UIObject res = super.findObjectByHtmlId(id);
        if (res != null) {
            return res;
        }
        if (getHtmlId().equals(id)) {
            return this;
        }
        if (this.button != null) {
            res = this.button.findObjectByHtmlId(id);
            if (res != null) {
                return res;
            }
        }
        if (this.dataCells != null) {
            for (Cell cell : dataCells) {
                res = cell.findObjectByHtmlId(id);
                if (res != null) {
                    return res;
                }
            }
        }

        if (this.children != null) {
            for (Node node : new ArrayList<Node>(this.children.getCreatedNodes())) {
                res = node.findObjectByHtmlId(id);
                if (res != null) {
                    return res;

                }
            }
        }
        return null;
    }

    private int getLevel() {
        Node parentNode = getParentNode();
        if (parentNode == null) {
            return 0;
        } else {
            return parentNode.getLevel() + 1;
        }
    }

    @Override
    public void setParent(UIObject parent) {
        super.setParent(parent);
        if (parent == null) {
            this.html.remove();
            this.html.setAttr("pid", null);
        } else {
            updateLevel();
            updateInternalMargins();
            Node parentNode = getParentNode();
            if (parentNode != null) {
                this.html.setAttr("pid", parentNode.getHtmlId());
            }
            Tree tree = getTree();
            if (tree != null) {
                ensureColumnCount(-1, tree.getColumnCount());
            }
        }
    }
    
    int lastComputedLevel = -1;

    void updateLevel() {
        int level = getLevel();
        Tree tree = getTree();
        if (tree != null && !tree.isRootVisible() && level > 0) {
            level--;
        }
        lastComputedLevel = level;

        this.button.getHtml().setCss("padding-left", String.valueOf(level * 15) + "px");
    }

    void markAsSelected() {
        markSelected(true);        
    }

    void markAsNotSelected() {
        markSelected(false);
    }

    public int getChildCount() {
        return isLeaf() ? 0 : getChildNodes().getNodes().size();
    }

    public int indexOfChild(Node child) {
        List<Node> nodes = getChildNodes().getNodes();
        return nodes.indexOf(child);
    }

    public Node getChildAt(int index) {
        List<Node> nodes = getChildNodes().getNodes();
        if (index >= 0 && index < nodes.size()) {
            return nodes.get(index);
        } else {
            return null;
        }
    }

    public List<Node> getPath() {
        Node node = this;
        List<Node> path = new LinkedList<>();
        while (node != null) {
            path.add(0, node);
            node = node.getParentNode();
        }
        return path;
    }

    public int[] getIndexPath() {
        List<Integer> path = new LinkedList<>();
        Node node = this;
        while (node != null) {
            Node parent = node.getParentNode();
            if (parent == null) {
                break;
            } else {
                path.add(0, parent.indexOfChild(node));
            }
            node = parent;
        }
        int[] result = new int[path.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = path.get(i);
        }
        return result;
    }

    private class ColumnInfo {

        Object value;
        private ICellRendererProvider cellRendererProvider;
        private ICellEditorProvider cellEditorProvider;

        public ICellEditorProvider getCellEditorProvider() {
            if (cellEditorProvider != null) {
                return cellEditorProvider;
            }
            Tree.Column column = getColumn();
            if (column != null) {
                return column.getCellEditorProvider();
            } else {
                return null;
            }
        }

        public ICellRendererProvider getCellRendererProvider() {
            if (cellRendererProvider != null) {
                return cellRendererProvider;
            }
            final Cell cell = getIndex() >= 0 && getIndex() < getCells().size() ? getCells().get(getIndex()) : null;
            final EditMask cellmask = cell == null ? null : cell.getEditingOptions().getEditMask();
            if (cellmask != null) {
                return new ICellRendererProvider() {
                    @Override
                    public INodeCellRenderer newCellRenderer(Node node, int columnIndex) {
                        Cell cell = node.getCells().get(columnIndex);
                        return new EditMaskRenderer(cellmask, getEnvironment(), cell);
                    }
                };
            } else {
                Tree.Column column = getColumn();
                if (column != null) {
                    return column.getCellRendererProvider();
                } else {
                    return null;
                }
            }
        }

        private Tree.Column getColumn() {
            int index = getIndex();
            if (index >= 0) {
                Tree tree = getTree();
                if (tree == null) {
                    return null;
                }
                return tree.getColumn(index);
            }
            return null;
        }

        private int getIndex() {
            for (int i = 0; i < columnInfos.size(); i++) {
                if (columnInfos.get(i) == this) {
                    return i;
                }
            }
            return -1;
        }
    }
    
    private List<ColumnInfo> columnInfos = null;

    private ColumnInfo getColumnInfo(int columnIndex) {
        if (columnInfos == null) {
            columnInfos = new ArrayList<>();
        }
        while (columnIndex >= columnInfos.size()) {
            columnInfos.add(new ColumnInfo());
        }
        return columnInfos.get(columnIndex);
    }
    
    private void insertColumnInfo(int columnIndex){
        if (columnInfos == null) {
            columnInfos = new ArrayList<>();
            for (int i=0; i<=columnIndex; i++){
                columnInfos.add(new ColumnInfo());
            }
        }else{
            if (columnIndex >= columnInfos.size()) {
                for (int i=columnInfos.size(); i<=columnIndex; i++){
                    columnInfos.add(new ColumnInfo());
                }                
            }else{
                columnInfos.add(columnIndex, new ColumnInfo());
            }
        }
    }
    
    private void removeColumnInfo(int columnIndex){
        columnInfos.remove(columnIndex);
    }

    public interface INodeCellRenderer {

        public void update(final Node node, final int c, final Object value);

        public void selectionChanged(Node node, int c, Object value, ICell cell, boolean isSelected);

        public void rowSelectionChanged(boolean isRowSelected);

        public UIObject getUI();
    }

    public interface INodeCellEditor {

        public void setValue(Node node, int c, Object value);

        public Object getValue();

        public void applyChanges();

        public void cancelChanges();

        public UIObject getUI();
    }

    public static class DefaultTreeCellRenderer extends UIObject implements INodeCellRenderer {

        protected final Html label;

        public DefaultTreeCellRenderer() {
            super(new Div());
            setHeight(14);

            html.add(label = new Html("label"));
            this.label.setCss("top", "3px");
            this.label.setCss("left", "3px");
        //    this.label.setCss("padding-right", "6px");
            this.label.setCss("position", "relative");
            //this.label.setCss("cursor", "inherit");
            this.label.addClass("rwt-ui-element-text");            
            html.setCss("overflow", "hidden");
            html.setCss("display", "block");
            html.setCss("white-space", "nowrap");
            setDefaultClassName("rwt-ui-element-text");
        }

        protected String getDisplayText(final Object value) {
            return String.valueOf(value);
        }

        @Override
        protected ICssStyledItem getBackgroundHolder() {
            return label;
        }

        @Override
        protected ICssStyledItem getForegroundHolder() {
            return label;
        }

        @Override
        protected ICssStyledItem getFontOptionsHolder() {
            return label;
        }

        @Override
        public void update(final Node node, final int c, final Object value) {
            label.setInnerText(getDisplayText(value));
        }

        @Override
        public void selectionChanged(Node node, int c, Object value, ICell cell, boolean isSelected) {
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
        }

        @Override
        public UIObject getUI() {
            return DefaultTreeCellRenderer.this;
        }
    }

    public static class DefaultTreeColumnCellRenderer extends DefaultTreeCellRenderer {

        private Html icon;

        public DefaultTreeColumnCellRenderer() {
            this.label.setCss("top", "-2px");
            setDefaultClassName("rwt-ui-element");
        }

        private Node getNode() {
            UIObject parent = getParent();
            if (parent == null) {
                return null;
            }

            return (Node) parent.getParent();
        }

        @Override
        public void update(Node node, int c, Object value) {
            if (node != null && node.icon != null) {
                if (this.icon == null) {
                    this.icon = new Html("img");
                    this.icon.setCss("width", "12px");
                    this.icon.setCss("height", "12px");
                    this.icon.setCss("top", "1px");
                    this.icon.setCss("position", "relative");
                    this.icon.setAttr("viewBox", "0 0 12 12");
                    this.icon.setAttr("preserveAspectRatio", "xMinYMin");
                    html.add(0, this.icon);
                }
                if (!Utils.equals(this.icon.getAttr("src"), node.icon.getURI(this))) {
                    this.icon.setAttr("src", node.icon.getURI(this));
                }
            } else {
                if (this.icon != null) {
                    this.icon.remove();
                    this.icon = null;
                }
            }

            super.update(node, c, value);
        }

        @Override
        public UIObject getUI() {
            return DefaultTreeColumnCellRenderer.this;
        }
    }
    
    private final ICellRendererProvider defaultRendererProvider = new ICellRendererProvider() {
        @Override
        public INodeCellRenderer newCellRenderer(Node node, int columnIndex) {
            if (columnIndex == 0) {
                return new DefaultTreeColumnCellRenderer();
            } else {
                return new DefaultTreeCellRenderer();
            }
        }
    };

    public ICellRendererProvider getCellRendererProvider(final int columnIndex) {
        ICellRendererProvider rendererProvider = getColumnInfo(columnIndex).getCellRendererProvider();
        return rendererProvider == null ? defaultRendererProvider : rendererProvider;
    }

    public IEditingOptions getEditingOptionsForCell(int index) {
        if (getCells().size() > index && index >= 0) {
            return getCells().get(index).getEditingOptions();
        } else {
            return null;
        }
    }

    public Tree.ICellEditorProvider getCellEditorProvider(int columnIndex) {
        return getColumnInfo(columnIndex).getCellEditorProvider();
    }

    public void setCellRendererProvider(int columnIndex, ICellRendererProvider rendererProvider) {
        getColumnInfo(columnIndex).cellRendererProvider = rendererProvider;
    }

    public void setCellEditorProvider(int columnIndex, Tree.ICellEditorProvider editorProvider) {
        getColumnInfo(columnIndex).cellEditorProvider = editorProvider;
    }

    public Object getCellValue(int columnIndex) {
        return getColumnInfo(columnIndex).value;
    }

    public Object getCellValue(Tree.Column c) {
        return getCellValue(c.getIndex());
    }

    public final void setCellValue(int columnIndex, Object value) {
        getColumnInfo(columnIndex).value = value;
        updateCellContent(columnIndex);
    }

    public final void setCellValue(Tree.Column column, Object value) {
        setCellValue(column.getIndex(), value);
    }

    public final int getCellsCount() {
        return columnInfos == null ? 0 : columnInfos.size();
    }

    public final boolean isSpanned() {
        return spanned;
    }

    public void setEditable(final boolean isEditable) {
        this.isEditable = isEditable;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public final void setSpanned(final boolean isSpanned) {
        spanned = isSpanned;
        if (getTree() != null) {
            ensureColumnCount(-1, getTree().getColumnCount());
        }
    }

    public void setTextOptions(final int columnIndex, final TextOptions options) {
        dataCells.get(columnIndex).setTextOptions((WpsTextOptions) options);
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        if (dataCells != null) {
            for (Cell cell : dataCells) {
                cell.setForeground(c);
                cell.updateCurrentState();
            }
        }
    }

    @Override
    public void setBackground(Color c) {
        super.setBackground(c);
        if (dataCells != null) {
            for (Cell cell : dataCells) {
                cell.setBackground(c);
                cell.updateCurrentState();
            }
        }
    }

    void markSelected(boolean selected) {
        if (this.isSelected!=selected){
            isSelected = selected;
            if (selected){
                html.removeChoosableMarker();
                html.addClass("grid-current-row");
            }else{
                 html.markAsChoosable();
                 html.removeClass("grid-current-row");
                 setCurrentCell(null);
            }
            if (dataCells != null && !dataCells.isEmpty()) {
                if (isSelected){
                    setCurrentCell(dataCells.get(0));
                }
                for (Cell cell : dataCells) {
                    cell.markSelected(selected);
                }
            }
        }
    }

    @Deprecated
    public void setSelectedNodeBackground(String color) {
        /*if (dataCells != null && color != null) {
            for (Cell c : dataCells) {
                c.setSelectedNodeColor(color);
            }
        }*/
    }

    public void setSelectedNodeForeground(Color color) {
        if (dataCells != null && color != null) {
            for (Cell c : dataCells) {
                c.setSelectedFontColor(color);
            }
        }
    }
    
    void updateMarkedColors(){
        if (dataCells!=null){
            for (Cell c : dataCells) {
                c.updateMarkedBackgound();
                c.updateMarkedForeground();
            }
        }
    }

    public boolean isChildOf(final Node node) {
        for (Node n = getParentNode(); n != null; n = n.getParentNode()) {
            if (n == node) {
                return true;
            }
        }
        return false;        
    }
    
    public final boolean isParentFor(final Node node){
        for (Node n = node.getParentNode(); n != null; n = n.getParentNode()) {
            if (n == this) {
                return true;
            }
        }
        return false;
    }
    
    private List<Cell> dataCells = null;
    private boolean spanned = false;
    private boolean isEditable = true;
    private boolean isSelected;

    final void ensureColumnCount(final int index, final int addColumns) {
        ensureColumnCountImpl(index, spanned ? 1 : addColumns);
        if (spanned) {
            getNodeCell().getHtml().setAttr("colSpan", addColumns);
        } else {
            getNodeCell().getHtml().setAttr("colSpan", null);
        }
    }
    
    final void removeCell(final int index){
        if (isSpanned()) {
            Cell cell = getCells().get(0);
            cell.getHtml().setAttr("colSpan", getCellsCount()-1);
        } else {
            final int cellsCount = getCellsCount();
            if (index < getCellsCount()) {
                final Cell cell = getCells().remove(index);
                getHtml().remove(cell.getHtml());
                cell.setParent(null);
                removeColumnInfo(index);
                if (cell.isSelected() && isSelected){
                    if (index==cellsCount - 1 && getCellsCount()>0){
                        setCurrentCell(getCells().get(getCellsCount()-1));
                    }else if (index<cellsCount - 1){
                        setCurrentCell(getCells().get(index));
                    }
                }
            }
        } 
    }

    private void ensureColumnCountImpl(final int index, final int addColumns) {
        int currentColumnCount = this.getHtml().children().size();
        int initColCount = currentColumnCount;
        if (currentColumnCount != addColumns) {
            while (currentColumnCount < addColumns) {
                Cell cell = new Cell(currentColumnCount);
                if (cell.renderer != null) {
                    cell.renderer.getUI().setParent(cell);
                    cell.container.add(cell.renderer.getUI().getHtml());
                }
                cell.container.addClass("rwt-ui-element");
                cell.container.setCss("width", "100%");
                cell.container.setCss("height", "100%");
                cell.container.setCss("overflow", "hidden");
                if (dataCells == null) {
                    dataCells = new LinkedList<>();
                }
                if (index<0){
                    dataCells.add(cell);
                    html.add(cell.getHtml());
                }else{
                    insertColumnInfo(index);
                    dataCells.add(index, cell);
                    html.add(index, cell.getHtml());
                }
                if (isSelected){
                    cell.markSelected(true);
                }
                currentColumnCount++;
            }
            while (currentColumnCount > addColumns) {
                this.getHtml().children().get(this.getHtml().children().size() - 1).remove();
                currentColumnCount--;
            }
            if (dataCells != null) {
                int i = 0;
                for (Cell cell : dataCells) {
                    if (cell.editor != null) {
                        cell.editor.cancelChanges();
                    }
                    cell.update(i, false);
                    i++;
                }
            }
        }
        if (this.children != null && this.children != Children.LEAF) {
            for (Node node : children.getCreatedNodes()) {
                node.ensureColumnCount(index, addColumns);
            }
        }
        if (initColCount == 1 && currentColumnCount > 1) {
            getNodeCell().getHtml().addClass("rwt-grid-row-cell");
        } else if (currentColumnCount == 1 && initColCount > 1) {
            getNodeCell().getHtml().removeClass("rwt-grid-row-cell");
        }
    }

    public List<Cell> getCells() {
        return dataCells;
    }

    private void updateCellContent(int index) {
        if (index >= 0 && dataCells != null && index < dataCells.size()) {
            Cell cell = dataCells.get(index);
            cell.update(index, false);
        }
    }

    void setCurrentCell(final Cell cell) {
        if (currentCell!=cell){
            if (currentCell!=null){
                currentCell.getHtml().removeClass("rwt-grid-current-cell");
                currentCell.getHtml().removeClass("grid-current-cell");
                if (Node.this.isSelected){
                    currentCell.getHtml().markAsChoosable();
                }else{
                    currentCell.getHtml().removeChoosableMarker();
                }
            }
            this.currentCell = cell;
            if (this.currentCell != null) {
                if (this.currentCell.editor != null) {
                    this.currentCell.editor.getUI().setFocused(true);
                } else {
                    this.currentCell.setFocused(true);
                }
                if (isSelected){
                    currentCell.getHtml().removeChoosableMarker();
                    currentCell.getHtml().addClass("rwt-grid-current-cell");
                    currentCell.getHtml().addClass("grid-current-cell");
                }
            }
            if (getTree()!=null && getTree().getSelectedNode()==this){
                getTree().updateFrames();
            }        
        }
    }
    
    Cell getCurrentCell(){
        return currentCell;
    }
    
    public void finishEdit() {
        if (currentCell != null) {
            currentCell.finishEdit();
        }
    }    
    
    public final void setFilterRules(final FilterRules newRules){
        filterRules = newRules==null ? null : newRules.copy();        
    }
    
    public final FilterRules getFilterRules(){
        return filterRules==null ? null : filterRules.copy();
    }    
    
    void expandOnFilter(){
        if (!isExpanded){
            expand();
            isAutoExpanded = true;
        }
    }
    
    boolean isExpandedOnFilter(){
        return isAutoExpanded;
    }
    
    boolean isMatchToFilter(final String filter){
        return filterRules==null ? true : filterRules.isMatchToSomeFilter(filter);
    }
}
