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

package org.radixware.wps.dialogs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import org.radixware.kernel.common.client.dialogs.ISelectInstantiatableClassDialog;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.types.InstantiatableClass;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.Dialog;
import org.radixware.kernel.common.client.types.FilterRules;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.ICssStyledItem;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.CheckBox;
import org.radixware.wps.rwt.IGrid;
import org.radixware.wps.rwt.IGrid.ICell;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.UIObject;
import org.radixware.wps.rwt.VerticalBoxContainer;
import org.radixware.wps.rwt.events.ClickHtmlEvent;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.events.KeyDownEventFilter;
import org.radixware.wps.rwt.events.KeyDownHtmlEvent;
import org.radixware.wps.rwt.events.MouseClickEventFilter;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.INodeCellRenderer;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.rwt.tree.Tree.ICellRendererProvider;
import org.radixware.wps.views.editors.valeditors.ValStrEditorController;


class SelectInstantiatableClassDialog extends Dialog implements ISelectInstantiatableClassDialog {

    private final static String DLG_SETTINGS_GROUP_NAME=SettingNames.SYSTEM+"/SEL_INST_CLASS_DLG";
    private final static String EXPANDED_ITEMS_GROUP_NAME="expandedItems";
    private final static Color DEFAULT_SELECTION_COLOR = new Color(0xFF, 0xFF, 0x66);
    
    private enum ECheckState{
        CHECKED,UNCHECKED,PARTIALLY_CHECKED;
    }
    
    private static interface ICheckStateListener{
        void checkStateChanged(final ECheckState prevState, final ECheckState newState, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }
        
    private static interface INodeCheckStateListener{
        void checkStateChanged(final Node node, final ECheckState newState, final EnumSet<EKeyboardModifier> keyboardModifiers);
    }        
    
    private final static class SelectionCheckBox extends UIObject{
        
        private final Html label = new Html("label");        
        private final List<ICheckStateListener> listeners = new LinkedList<>();
        private ECheckState checkState = ECheckState.UNCHECKED;
        
        public SelectionCheckBox(){
            super(new Div());
            final Html html = getHtml();
            html .add(label);            
            label.setCss("left", "3px");//NOPMD
            html.setCss("margin-top", "1px");
            html.setCss("margin-left", "3px");
            html.setCss("width", "10px");
            html.setCss("height", "11px");
            html.setCss("float", "left");
            html.setCss("padding", null);
            html.setCss("padding-top", "0px");
            html.setCss("padding-bottom", "0px");
            html.setCss("padding-right", "0px");
            html.setCss("padding-left", "2px");
            html.setCss("vertical-align", "middle");
            html.setCss("border", "solid 1px #BBB");
            html.setCss("background-color", "#FFFFFF");
            subscribeToEvent(new MouseClickEventFilter(EKeyEvent.VK_LBUTTON, EKeyboardModifier.ANY));
        }
        
        public ECheckState getCheckState(){
            return checkState;
        }
        
        public void setCheckState(final ECheckState newState){
            checkState = newState;
            switch(checkState){
                case CHECKED:
                    label.setInnerText("\u2714");
                    label.setCss("color", null);
                    break;
                case PARTIALLY_CHECKED:
                    label.setInnerText("\u2714");
                    label.setCss("color", "gray");
                    break;
                case UNCHECKED:
                    label.setInnerText(" ");
                    label.setCss("color", null);
                    break;                    
            }
        }

        @Override
        protected void processHtmlEvent(final HtmlEvent event) {
            if (event instanceof ClickHtmlEvent){
                final ECheckState state = getCheckState();
                if (state==ECheckState.UNCHECKED){
                    setCheckState(ECheckState.CHECKED);
                }else{
                    setCheckState(ECheckState.UNCHECKED);
                }
                notifyListeners(state, getCheckState(), ((ClickHtmlEvent)event).getKeyboardModifiers());
            }
            super.processHtmlEvent(event);
        }
        
        public void addListener(final ICheckStateListener listener){
            listeners.add(listener);
        }
        
        public void removeListener(final ICheckStateListener listener){
            listeners.remove(listener);
        }
        
        private void notifyListeners(final ECheckState prevState, final ECheckState newState, final EnumSet<EKeyboardModifier> keyboardModifiers){
            final List<ICheckStateListener> copy = new LinkedList<>(listeners);
            for (ICheckStateListener listener: copy){
                listener.checkStateChanged(prevState, newState, keyboardModifiers);
            }
        }

        @Override
        public UIObject findObjectByHtmlId(final String id) {            
            return super.findObjectByHtmlId(id);
        }
    }
    
    private static class NodeRenderer extends Node.DefaultTreeColumnCellRenderer{
        @Override
        public void update(final Node node, final int c, final Object value) {            
            final InstantiatableClass cl = (InstantiatableClass)node.getUserData();
            if (cl!=null){
                if (cl.isAbstractClass()){
                    label.setCss("font-style", "italic");                
                }
                label.setInnerText(cl.getTitle());
            }
        }
        
        @Override
        public UIObject getUI() {
            if (getParent()!=null){
                getParent().getHtml().addClass("editor-cell");
            }
            return this;
        }
    }      
    
    private final static class CheckableNodeRenderer extends NodeRenderer{
        
        private final SelectionCheckBox checkBox = new SelectionCheckBox();
        private final List<INodeCheckStateListener> checkStateListeners = new LinkedList<>();
        private final Node node;
        private final Color selectionColor;
        
        public CheckableNodeRenderer(final Node node, final Color selectionColor){
            html.remove(label);
            label.renew();            
            getHtml().add(checkBox.getHtml());            
            getHtml().add(label);
            label.setCss("top", null);
            this.selectionColor = selectionColor;
            this.node = node;
            checkBox.addListener(new ICheckStateListener() {
                @Override
                public void checkStateChanged(final ECheckState prevState, final ECheckState newState, final EnumSet<EKeyboardModifier> keyboardModifiers) {
                    node.setCellValue(0, newState);
                    notifyCheckStateChanged(newState, keyboardModifiers);
                }
            });            
        }
        
        
        public void addCheckStateListener(final INodeCheckStateListener listener){
            checkStateListeners.add(listener);
        }
        
        public void removeCheckStateListener(final INodeCheckStateListener listener){
            checkStateListeners.remove(listener);
        }
        
        private void notifyCheckStateChanged(final ECheckState checkState, final EnumSet<EKeyboardModifier> keyboardModifiers){
            final List<INodeCheckStateListener> listeners = new LinkedList<>(checkStateListeners);
            for (INodeCheckStateListener listener: listeners){
                listener.checkStateChanged(node, checkState, keyboardModifiers);
            }
        }

        @Override
        public UIObject findObjectByHtmlId(final String id) {
            final UIObject obj = checkBox.findObjectByHtmlId(id);
            if (obj!=null){
                return obj;
            }
            return super.findObjectByHtmlId(id);
        }

        @Override
        public void visit(final Visitor visitor) {
            checkBox.visit(visitor);
            super.visit(visitor);            
        }        

        @Override
        public void update(final Node node, final int c, final Object value) {
            super.update(node, c, value);
            final InstantiatableClass cl = (InstantiatableClass)node.getUserData();
            if (cl!=null){
                if (value instanceof ECheckState){
                    checkBox.setCheckState((ECheckState)value);
                    if (!cl.isAbstractClass() && value==ECheckState.CHECKED){
                        node.setBackground(selectionColor);
                    }else{
                        node.setBackground(null);
                    }
                }else{
                    node.setBackground(null);
                }
                node.getHtml().removeChoosableMarker();
            }            
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
            super.rowSelectionChanged(isRowSelected);
            node.getHtml().removeChoosableMarker();
        }

        @Override
        public void selectionChanged(Node node, int c, Object value, IGrid.ICell cell, boolean isSelected) {
            super.selectionChanged(node, c, value, cell, isSelected);
            node.getHtml().removeChoosableMarker();
        }
        
        @Override
        protected ICssStyledItem getBackgroundHolder() {
            return this.getHtml();
        }        
    }      
    
    private final class CheckableNodeRendererProvider implements ICellRendererProvider{
        
        private final Color selectionColor;
        
        public CheckableNodeRendererProvider(final Color selectionColor){
            this.selectionColor = selectionColor;
        }

        @Override
        public INodeCellRenderer newCellRenderer(final Node node, final int columnIndex) {
            final CheckableNodeRenderer renderer = new CheckableNodeRenderer(node, selectionColor);
            renderer.addCheckStateListener(new INodeCheckStateListener() {
                @Override
                public void checkStateChanged(final Node node, final ECheckState newState, final EnumSet<EKeyboardModifier> keyboardModifiers) {
                    onNodeChecked(node, newState, keyboardModifiers);
                }
            });
            return renderer;
        }
        
    }
    
    private final static class NodeRendererProvider implements ICellRendererProvider{

        @Override
        public INodeCellRenderer newCellRenderer(final Node node, final int columnIndex) {
            return new NodeRenderer();
        }       
        
    }
    
    private final class ClickableNode extends Node.DefaultNode{
        
        private Color background;
        
        public ClickableNode(final boolean isMultiSelectEnabled){
            super();            
            subscribeToEvent(new MouseClickEventFilter(EKeyboardModifier.ANY));
            setCellValue(0, ECheckState.UNCHECKED);
        }

        @Override
        protected void processHtmlEvent(final HtmlEvent event) {
            onNodeClick(this, event);
        }                

        @Override
        public void setBackground(final Color c) {
            if (!Objects.equals(c, background)){
                background = c;
                super.setBackground(c);
            }            
        }
    }
    
    private static interface INodeVisitor{
        boolean visit(Node node, boolean isAbstractClass);
    }
    
    private final static class NodeCheckStateCalculator implements INodeVisitor{
        
        private boolean someNodeChecked;
        private boolean someNodeUnchecked;

        @Override
        public boolean visit(final Node node, final boolean isAbstractClass) {
            if (!isAbstractClass){
                if (node.getCellValue(0)==ECheckState.CHECKED){
                    someNodeChecked = true;
                }
                if (node.getCellValue(0)==ECheckState.UNCHECKED){
                    someNodeUnchecked = true;
                }
            }
            return !someNodeChecked || !someNodeUnchecked;
        }
        
        public ECheckState getCheckState(){
            if (someNodeChecked){
                return someNodeUnchecked ? ECheckState.PARTIALLY_CHECKED : ECheckState.CHECKED;
            }else{
                return ECheckState.UNCHECKED;
            }
        }
    }
    
    private InstantiatableClass currentClass = null;
    private Node currentNode;
    private final Tree tree = new Tree(){
        @Override
        protected void processHtmlEvent(final HtmlEvent event) {
            if (event instanceof KeyDownHtmlEvent){                
                onSelectAll();
            }else{
                super.processHtmlEvent(event);
            }
        }        
    };  
    private final VerticalBoxContainer container = new VerticalBoxContainer();
    private final Label lbNumberOfSelectedItems = new Label();
    private final CheckBox cbMultiSelectEnabled;
    private final ValStrEditorController filter;
    private final boolean sortByTitles;
    private final String settingsGroupName;
    private final List<InstantiatableClass> expandedClasses = new LinkedList<>();
    private final List<ClickableNode> nodesByOrder = new ArrayList<>();
    private final String selectedItemsCountTextTemplate;
    private final String selectedItemTextTemplate;
    private final String noSelectionText;
    private long lastNodeClickTime;
    
    public SelectInstantiatableClassDialog(final WpsEnvironment env, 
                                           final List<InstantiatableClass> classes, 
                                           final String configGroup,
                                           final boolean sortByTitles) {
        this(env,classes,configGroup,sortByTitles,false);
    }
    
    public SelectInstantiatableClassDialog(final WpsEnvironment env, 
                                           final List<InstantiatableClass> classes, 
                                           final String configGroup,
                                           final boolean sortByTitles,
                                           final boolean isMultiSelectEnabled) {
        super(env.getDialogDisplayer(), "");        
        settingsGroupName = DLG_SETTINGS_GROUP_NAME+"/"+configGroup;
        filter = new ValStrEditorController(env);
        this.sortByTitles = sortByTitles;
        final List<InstantiatableClass> compactedClasses;
        if (sortByTitles){
            compactedClasses = InstantiatableClass.sortByTitles(InstantiatableClass.compact(classes));
        }else{
            compactedClasses = InstantiatableClass.compact(classes);
        }        
        final MessageProvider mp = env.getMessageProvider();
        selectedItemsCountTextTemplate = mp.translate("SelectInstantiatableClass", "Number of selected items: %1$s");            
        selectedItemTextTemplate = mp.translate("SelectInstantiatableClass", "Selected item: %1$s");
        noSelectionText = mp.translate("SelectInstantiatableClass", "No items selected");        
        if (isMultiSelectEnabled){
            cbMultiSelectEnabled = new CheckBox();
            cbMultiSelectEnabled.setText(mp.translate("SelectInstantiatableClass", "Create several objects of different classes"));                        
        }else{
            cbMultiSelectEnabled = null;
        }
        
        setupUi(env, compactedClasses, isMultiSelectEnabled);        
    }
    
    private void setupUi(final WpsEnvironment env, final List<InstantiatableClass> compactedClasses, final boolean isMultiSelectEnabled){
        setWindowTitle(env.getMessageProvider().translate("SelectInstantiatableClass", "Select Class of New Object"));        
                
        filter.setMandatory(true);
        filter.setLabel(env.getMessageProvider().translate("SelectInstantiatableClass","Filter:"));
        filter.setLabelVisible(true);        
        container.add((UIObject)filter.getValEditor());
        
        container.addSpace();
        container.addSpace();
                
        tree.showHeader(false);
        tree.setRootVisible(false);
        tree.setFilterEditor(filter);
        if (isMultiSelectEnabled){
            tree.subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_A, EKeyboardModifier.CTRL));
        }
        container.add(tree);         
        tree.getTreeColumn().setCellRendererProvider(new NodeRendererProvider());
        
        tree.addBranchListener(new Tree.BranchListener() {

            @Override
            public void afterExpand(final Node node) {
                if (node!=null){
                    expandedClasses.add(SelectInstantiatableClassDialog.getClass(node));
                }
            }

            @Override
            public void afterCollapse(final Node node) {
                if (node!=null){
                    expandedClasses.remove(SelectInstantiatableClassDialog.getClass(node));
                }
            }
        });

        final Node.DefaultNode root = new Node.DefaultNode();
        final List<InstantiatableClass> expandedItems = readExpandedItems();
        for (InstantiatableClass cls : compactedClasses) {
            fillTree(cls, root, expandedItems);
        }
        
        tree.setRootNode(root);        
        tree.addSelectionListener(new Tree.NodeListener() {
            @Override
            public void selectionChanged(Node oldSelection, Node newSelection) {
                if (oldSelection != newSelection) {
                    currentClass = SelectInstantiatableClassDialog.getClass(newSelection);
                    setActionEnabled(EDialogButtonType.OK, currentClass!=null && !currentClass.isAbstractClass());
                    currentNode = newSelection;
                }
            }
        });        
        tree.addDoubleClickListener(new Tree.DoubleClickListener() {
            @Override
            public void nodeDoubleClick(final Node item) {
                onNodeDoubleClick(item);
            }
        });
        tree.addFilterListener(new Tree.FilterListener() {
            @Override
            public void afterApplyFilter(final String filter) {
                if (currentNode==null || !currentNode.isVisible()){
                    updateCurrentNode();
                }else if (currentNode!=null){                
                    for (Node node=currentNode.getParentNode(); node!=null; node=node.getParentNode()){
                        if (!node.isVisible() || !node.isExpanded()){
                            updateCurrentNode();
                            break;
                        }
                    }
                }
                updateCheckState(tree.getRootNode());
            }
        });
        if (isMultiSelectEnabled){
            container.addSpace();            
            container.add(cbMultiSelectEnabled);
            cbMultiSelectEnabled.addSelectionStateListener(new CheckBox.SelectionStateListener() {
                @Override
                public void onSelectionChange(final CheckBox cb) {
                    setMultiSelectEnabled(cb.isSelected());
                }
            });
        }        
        container.setAutoSize(tree, true);
        add(container);
        container.setVSizePolicy(SizePolicy.EXPAND);        
        addCloseAction(EDialogButtonType.OK);
        addCloseAction(EDialogButtonType.CANCEL);        
        setActionEnabled(EDialogButtonType.OK, false);
        ((UIObject)filter.getValEditor()).setFocused(true);
    }        
    
    private void fillTree(final InstantiatableClass cls, 
                          final Node.DefaultNode parentNode,
                          final List<InstantiatableClass> expandedItems){
        final Node.DefaultNode node = createTreeNodeForClass(cls);        
        parentNode.add(node);
        if (cls.getDerivedClassesCount()>0){
            final List<InstantiatableClass> derivedClasses;
            if (sortByTitles){
                derivedClasses = InstantiatableClass.sortByTitles(cls.getDerivedClasses());
            }else{
                derivedClasses = cls.getDerivedClasses();
            }
            for (InstantiatableClass derivedClass: derivedClasses){
                fillTree(derivedClass, node, expandedItems);
            }
            if (cls.containsIn(expandedItems)){
                node.expand();
                expandedClasses.add(cls);
            }
        }        
    }
    
    private Node.DefaultNode createTreeNodeForClass(final InstantiatableClass cls){
        final ClickableNode node = new ClickableNode(isMultiSelectAllowed());
        node.setUserData(cls);
        if (!cls.isAbstractClass()){
            final FilterRules rules = new FilterRules();
            rules.addRule(cls.getTitle());
            rules.addRule(cls.getItemId().toString(),false,true);
            if (cls.getId()!=null){
                rules.addRule(cls.getId().toString(),false,true);
            }
            node.setFilterRules(rules);
        }
        nodesByOrder.add(node);
        final String nodeId;
        if (cls.getId()!=null){
            nodeId = cls.getId().toString();
        }else if (cls.getItemId()!=null){
            nodeId = cls.getItemId().toString();
        }else{
            nodeId = cls.getTitle();
        }
        node.setObjectName("rx_entity_class_node_"+nodeId);
        return node;
    }
    
    private static InstantiatableClass getClass(final Node node){
        return node==null ? null : (InstantiatableClass) node.getUserData();
    }
    
    private void updateCurrentNode(){
        final Stack<Node> stack = new Stack<>();
        for (int i=tree.getRootNode().getChildCount()-1; i>=0; i--){
            stack.push(tree.getRootNode().getChildAt(i));
        }
        Node curNode;
        while (!stack.isEmpty()){
            curNode = stack.pop();
            if (curNode.isVisible()){
                if (!getClass(curNode).isAbstractClass()){
                    tree.setSelectedNode(curNode);
                    return;
                }
                if (curNode.isExpanded()){
                    for (int i=curNode.getChildCount()-1; i>=0; i--){
                        stack.push(curNode.getChildAt(i));
                    }
                }
            }
        }
        tree.setSelectedNode(null);
        ((UIObject)filter.getValEditor()).setFocused(true);
    }

    @Override
    public InstantiatableClass getCurrentClass() {
        return currentClass;
    }

    @Override
    public List<InstantiatableClass> getSelectedClasses() {        
        if (isMultiSelectEnabled()){
            final List<InstantiatableClass> classes = new LinkedList<>();
            for (Node node: nodesByOrder){
                if (!isAbstractClass(node) && node.getCellValue(0)==ECheckState.CHECKED){
                    classes.add(SelectInstantiatableClassDialog.getClass(node));
                }
            }
            if (!classes.isEmpty()){
                return classes;
            }
        }
        if (currentClass==null || currentClass.isAbstractClass()){
            return Collections.emptyList();
        }else{
            return Collections.singletonList(currentClass);
        }
    }
    
    private boolean isSomeNodeChecked(){
        for (Node node: nodesByOrder){
            if (node.getCellValue(0)!=ECheckState.UNCHECKED){
                return true;
            }
        }
        return false;
    }

    @Override
    protected DialogResult onClose(final String action, final DialogResult actionResult) {
        writeExpandedItems();
        return super.onClose(action, actionResult);
    }
    
    private void onSelectAll(){
        if (isMultiSelectAllowed()){
            enableMultiSelect();
            for (Node node: nodesByOrder){
                node.setCellValue(0, ECheckState.CHECKED);
            }
            onSelectionChanged();
        }
    }
    
    private void onNodeClick(final Node node, final HtmlEvent event){
        final Node currentNode = tree.getSelectedNode();
        if (node==currentNode){            
            if ((System.currentTimeMillis()-lastNodeClickTime)<500){
                lastNodeClickTime = 0;
                onNodeDoubleClick(node);
            }else{
                lastNodeClickTime = System.currentTimeMillis();
            }
        }else{
            lastNodeClickTime = System.currentTimeMillis();
            tree.setSelectedNode(node);
            if (isMultiSelectAllowed()){
                final EnumSet<EKeyboardModifier> modifiers = ((ClickHtmlEvent)event).getKeyboardModifiers();
                if (modifiers.contains(EKeyboardModifier.SHIFT) && modifiers.size()==1){
                    enableMultiSelect();
                    setRangeCheckState(node, currentNode, ECheckState.CHECKED);
                }
                if (modifiers.contains(EKeyboardModifier.CTRL) && modifiers.size()==1 && isMultiSelectEnabled()){
                    setRangeCheckState(node, currentNode, ECheckState.UNCHECKED);
                }
                onSelectionChanged();
            }
        }
        if (currentNode!=null){
            currentNode.getHtml().removeChoosableMarker();
            ((ICell)currentNode.getCells().get(0)).getHtml().removeChoosableMarker();
        }
    }        
    
    private void onSelectionChanged(){
        final List<InstantiatableClass> selectedClasses = getSelectedClasses();
        if (selectedClasses.isEmpty()){
            lbNumberOfSelectedItems.setText(noSelectionText);
        }else if (selectedClasses.size()==1){
            lbNumberOfSelectedItems.setText(String.format(selectedItemTextTemplate, selectedClasses.get(0).getTitle()));
        }else{
            if (lbNumberOfSelectedItems.getParent()==null){
                container.addSpace();
                container.add(lbNumberOfSelectedItems);
            }
            lbNumberOfSelectedItems.setText(String.format(selectedItemsCountTextTemplate, selectedClasses.size()));
        }
        getButton(EDialogButtonType.OK).setEnabled(!selectedClasses.isEmpty());
    }        
    
    private void onNodeChecked(final Node node, final ECheckState checkState, final EnumSet<EKeyboardModifier> modifiers){        
        final Node currentNode = tree.getSelectedNode();
        if (currentNode!=null && currentNode!=node && !modifiers.isEmpty() && isMultiSelectAllowed()){
            if (modifiers.contains(EKeyboardModifier.SHIFT) && modifiers.size()==1){
                enableMultiSelect();
                setRangeCheckState(node, currentNode, ECheckState.CHECKED);
            }
            if (modifiers.contains(EKeyboardModifier.CTRL) && modifiers.size()==1 && isMultiSelectEnabled()){
                setRangeCheckState(node, currentNode, ECheckState.UNCHECKED);
            }        
        }
        if (isAbstractClass(node)){
            setChildrenCheckState(node, checkState);
        }
        updateParentsCheckState(node);
        if (currentNode!=null){
            tree.setSelectedNode(node);
            currentNode.getHtml().removeChoosableMarker();
            ((ICell)currentNode.getCells().get(0)).getHtml().removeChoosableMarker();
        }
        onSelectionChanged();    
    }    
    
    private void onNodeDoubleClick(final Node node){
        tree.setSelectedNode(node);
        if (isAbstractClass(node)){
            if (node.isExpanded()){
                node.collapse();                
            }else{
                node.expand();
            }
        }else{
            if (isMultiSelectEnabled() && isSomeNodeChecked()){
                if (node.getCellValue(0)==ECheckState.CHECKED){
                    node.setCellValue(0, ECheckState.UNCHECKED);
                }else{
                    node.setCellValue(0, ECheckState.CHECKED);
                }
                updateParentsCheckState(node);
                onSelectionChanged();
            }else if (isActionEnabled(EDialogButtonType.OK)){            
                acceptDialog();
            }            
        }
    }
    
    private static void setChildrenCheckState(final Node parent, final ECheckState checkState){
        visitChildren(parent, new INodeVisitor() {
            @Override
            public boolean visit(final Node node, final boolean isAbstractClass) {
                if (node.getCellValue(0)!=checkState){
                    node.setCellValue(0, checkState);
                    updateParentsCheckState(node);
                }
                return true;
            }
        });
    }
    
    private void setRangeCheckState(final Node node1, final Node node2, final ECheckState checkState){
        visitRange(node1, node2, new INodeVisitor() {
            @Override
            public boolean visit(final Node node, final boolean isAbstractClass) {
                if (!isAbstractClass){
                    node.setCellValue(0, checkState);
                }
                return true;
            }
        });
        updateCheckState(tree.getRootNode());
    }    
    
    private static ECheckState updateCheckState(final Node node){
        Node child;
        ECheckState checkState = null, childCheckState;
        for (int i=0,count=node.getChildCount(); i<count; i++){
            child = node.getChildAt(i);
            if (isAbstractClass(child)){
                childCheckState = updateCheckState(child);                
            }else{
                childCheckState = child.getCellValue(0)==ECheckState.CHECKED ? ECheckState.CHECKED : ECheckState.UNCHECKED;
            }
            if (checkState==null){
                checkState = childCheckState;
            } else if (checkState!=childCheckState){
                checkState = ECheckState.PARTIALLY_CHECKED;
            }
        }
        if (getClass(node)!=null){
            node.setCellValue(0, checkState==null ? ECheckState.UNCHECKED : checkState);
        }
        return checkState;
    }
    
    private static void updateParentsCheckState(final Node node){
        for (Node parent=node.getParentNode(); parent!=null && parent.getUserData()!=null; parent=parent.getParentNode()){
            updateParentCheckState(parent);
        }
    }        
    
    private static void updateParentCheckState(final Node node){
        if (getClass(node)==null || isAbstractClass(node)){
            final NodeCheckStateCalculator checkStateCalculator = new NodeCheckStateCalculator();
            visitChildren(node, checkStateCalculator);
            node.setCellValue(0, checkStateCalculator.getCheckState());
        }
    }
    
    private static void visitChildren(final Node parent, final INodeVisitor visitor){
        final Stack<Node> nodes = new Stack<>();
        nodes.push(parent);
        Node node,childNode;
        while(!nodes.isEmpty()){
            node = nodes.pop();
            for (int i=0,count=node.getChildCount(); i<count; i++){
                childNode = node.getChildAt(i);                
                if (childNode.isVisible() && !visitor.visit(childNode, isAbstractClass(childNode) )){
                    return;
                }
                nodes.push(childNode);
            }            
        }
    }
        
    private void visitRange(final Node node1, final Node node2, final INodeVisitor visitor){
        final int firstItemIndex = nodesByOrder.indexOf(node1);
        int secondItemIndex = nodesByOrder.indexOf(node2);
        for (int i=Math.min(firstItemIndex, secondItemIndex),to=Math.max(firstItemIndex, secondItemIndex); i<=to; i++){
            final Node currentNode = nodesByOrder.get(i);
            if (currentNode.isVisible() && !visitor.visit(currentNode, isAbstractClass(currentNode))){
                return;
            }
        }
    }
    
    private static boolean isAbstractClass(final Node node){
        return getClass(node).isAbstractClass();
    }
    
    private boolean isMultiSelectAllowed(){
        return cbMultiSelectEnabled!=null;
    }
    
    private boolean isMultiSelectEnabled(){
        return isMultiSelectAllowed() && cbMultiSelectEnabled.isSelected();
    }
    
    private void enableMultiSelect(){
        if (isMultiSelectAllowed() && !isMultiSelectEnabled()){
            cbMultiSelectEnabled.setSelected(true);
        }
    }
    
    private void setMultiSelectEnabled(final boolean isEnabled){        
        for (ClickableNode node: nodesByOrder){
            node.setCellValue(0, ECheckState.UNCHECKED);            
        }
        onSelectionChanged();
        if (isEnabled){
            tree.getTreeColumn().setCellRendererProvider(new CheckableNodeRendererProvider(getSelectionColor()));
            tree.setSelectionStyle(EnumSet.of(IGrid.ESelectionStyle.ROW_FRAME));
        }else{
            tree.getTreeColumn().setCellRendererProvider(new NodeRendererProvider());
            tree.setSelectionStyle(EnumSet.of(IGrid.ESelectionStyle.BACKGROUND_COLOR));
        }        
    }
    
    private void writeExpandedItems(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.remove(settingsGroupName);
        settings.beginGroup(settingsGroupName);
        try{            
            InstantiatableClass.writeToSettings(settings, EXPANDED_ITEMS_GROUP_NAME, expandedClasses);
        }finally{
            settings.endGroup();
        }
    }
    
    private List<InstantiatableClass> readExpandedItems(){
        final ClientSettings settings = getEnvironment().getConfigStore();
        settings.beginGroup(settingsGroupName);
        try{
            return InstantiatableClass.readFromSettings(settings, EXPANDED_ITEMS_GROUP_NAME, null);
        }finally{
            settings.endGroup();
        }        
    }    
    
    private Color getSelectionColor(){
        final WpsSettings settings = ((WpsEnvironment)getEnvironment()).getConfigStore();
        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
        try{
            return settings.readColor(SettingNames.Selector.Common.SELECTED_ROW_COLOR, DEFAULT_SELECTION_COLOR);
        }finally{
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
    }
    
}
