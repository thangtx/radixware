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

package org.radixware.kernel.explorer.editors.xml;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlCursor.TokenType;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.views.IProgressHandle;
import org.radixware.kernel.common.exceptions.AppError;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.explorer.env.Application;
import org.w3c.dom.Node;



final class TreeBuilder extends QObject {

    private static class NamespaceMaps {

        private final Map<SchemaTypeSystem, Map<String, String>> namespacesByTypeSystems =
                new HashMap<>(16);

        private Map<String, String> getMapForTypeSystem(final SchemaTypeSystem typeSystem) {
            Map<String, String> namespaceMap = namespacesByTypeSystems.get(typeSystem);
            if (namespaceMap == null) {
                namespaceMap = new HashMap<>(32);
                namespacesByTypeSystems.put(typeSystem, namespaceMap);
            }
            return namespaceMap;
        }

        public void addPrefixForNamespace(final SchemaTypeSystem typeSystem, final String prefix, final String namespaceUri) {
            getMapForTypeSystem(typeSystem).put(prefix, namespaceUri);
        }

        public String findNamespaceForPrefix(final SchemaTypeSystem typeSystem, final String prefix) {
            return getMapForTypeSystem(typeSystem).get(prefix);
        }
    }

    private static abstract class AbstractTokenEvent extends QEvent {
        
        public static class Factory{
            
            private Factory(){            
            }

            public static AbstractTokenEvent newInstance(final XTreeTag parent, final XmlCursor cursor){
                if (parent.isAnyType() && cursor.isStart()){
                    return new ProcessAnyTypeTokenEvent(parent, cursor);
                }else{
                    return new ProcessTokenEvent(parent,cursor);
                }
            }
        }

        protected final XmlCursor cursor;
        protected final XTreeTag parent;

        public AbstractTokenEvent(final XTreeTag parentItem, final XmlCursor xmlCursor) {
            super(QEvent.Type.User);
            cursor = xmlCursor;
            parent = parentItem;
        }

        public final AbstractTokenEvent nextEvent() {
            if (cursor.isStart()) {
                cursor.toEndToken();
            }
            cursor.toNextToken();
            if (cursor.isEnd() || cursor.isEnddoc()){
                return null;
            }else{
                return Factory.newInstance(parent, cursor);
            }
        }
        
        protected abstract void process(final TreeBuilder treeBuilder) throws Exception;
    }

    private static class ProcessTokenEvent extends AbstractTokenEvent {
        
        public ProcessTokenEvent(final XTreeTag parentItem, final XmlCursor xmlCursor) {
            super(parentItem, xmlCursor);
        }

        @Override
        protected void process(TreeBuilder treeBuilder) throws Exception {
            treeBuilder.processCurrentToken(parent, -1, cursor);
        }
    }

    private static class ProcessAnyTypeTokenEvent extends ProcessTokenEvent {

        public ProcessAnyTypeTokenEvent(final XTreeTag parentItem, final XmlCursor xmlCursor) {
            super(parentItem,xmlCursor);
        }

        @Override
        public void process(final TreeBuilder treeBuilder) throws ClassNotFoundException {
            treeBuilder.processAnyTypeToken(parent, cursor);
        }   
    }
    
    private final QTreeWidget tree;
    private final TreeWindow treeWindow;
    private final NamespaceMaps namespaceMaps = new NamespaceMaps();
    private final static int CHILDS_COUNT_TO_START_WAITER = 64;
    private final IProgressHandle progress;

    public TreeBuilder(final IClientEnvironment environment, final QTreeWidget tree) {
        super(tree);
        progress = environment.getProgressHandleManager().newProgressHandle();
        this.tree = tree;
        treeWindow = (TreeWindow) tree.parentWidget();
    }

    private boolean isReadonly() {
        return treeWindow.getEditor().isReadOnlyMode();
    }

    public void addRootItems(final XmlObject xmlObject) {
        tree.clear();
        addItemsImpl(null, xmlObject.newCursor());
        if (tree.topLevelItemCount() > 0) {
            final QTreeWidgetItem firstItem = tree.topLevelItem(0);
            if (!firstItem.isExpanded()) {
                firstItem.setExpanded(true);
                tree.setCurrentItem(firstItem);
            }
        }
    }

    public void addChildItems(final XTreeTag parent) {//parent не может быть null
        final XmlCursor cursor = parent.getNode().newCursor();
        try {
            if (parent.isAnyType()) {
                final int itemsCount = XElementTools.getItemsCount(cursor);
                cursor.toNextToken();
                if (!cursor.isEnd() && !cursor.isEnddoc()){
                    progress.setMaximumValue(itemsCount);
                    progress.startProgress(Application.translate("Wait Dialog", "Reading Document..."), false);
                    QApplication.postEvent(this, AbstractTokenEvent.Factory.newInstance(parent, cursor));
                }else{
                    cursor.dispose();
                }
            } else {
                cursor.toNextToken();
                addItemsImpl(parent, cursor);
            }
        } catch (Exception e) {
            treeWindow.getEnvironment().processException(e);
            cursor.dispose();
        }
    }

    private void addItemsImpl(final XTreeTag parent, final XmlCursor cursor) {//parent может быть null
        final int itemsCount = XElementTools.getItemsCount(cursor);
        if (itemsCount < CHILDS_COUNT_TO_START_WAITER) {
            int nodesCounter = 0;
            while (!cursor.isEnddoc() && nodesCounter >= 0) {
                if (cursor.isStart()) {
                    nodesCounter++;
                }
                processCurrentToken(parent, -1, cursor);
                if (cursor.isEnd()) {
                    nodesCounter--;
                }
                cursor.toNextToken();
            }
            cursor.dispose();
        } else {
            if (!cursor.isEnddoc()) {
                progress.setMaximumValue(itemsCount);
                progress.startProgress(Application.translate("Wait Dialog", "Reading Document..."), false);
                QApplication.postEvent(this, AbstractTokenEvent.Factory.newInstance(parent, cursor));
            } else {
                cursor.dispose();
            }
        }
    }

    public void insertChildItems(final XTreeTag parent, final XmlObject node, final int index) {//parent не может быть null
        final XmlCursor cursor = node.newCursor();
        int nodesCounter = 0;
        try {
            do {
                if (cursor.isStart()) {
                    nodesCounter++;
                }
                processCurrentToken(parent, index, cursor);
                if (cursor.isEnd()) {
                    nodesCounter--;
                }
                cursor.toNextToken();
            } while (!cursor.isEnddoc() && nodesCounter > 0);
        } finally {
            cursor.dispose();
        }
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof AbstractTokenEvent) {
            AbstractTokenEvent tokenEvent = (AbstractTokenEvent) event;
            try {
                tokenEvent.process(this);
                tokenEvent = tokenEvent.nextEvent();
                if (tokenEvent == null) {
                    progress.finishProgress();
                    if (tree.topLevelItemCount() > 0) {
                        final QTreeWidgetItem firstItem = tree.topLevelItem(0);
                        if (!firstItem.isExpanded()) {
                            firstItem.setExpanded(true);
                            tree.setCurrentItem(firstItem);
                        }
                    }

                } else {
                    progress.incValue();
                    QApplication.postEvent(this, tokenEvent);
                }
            } catch (Exception exception) {
                treeWindow.getEnvironment().processException(exception);
                progress.finishProgress();
                tokenEvent.cursor.dispose();
            }
        }
        super.customEvent(event);
    }

    private void processAnyTypeToken(final XTreeTag parent, final XmlCursor cursor) {
        final XmlObject untypifiedNode = cursor.getObject();
        final String namespace = findNamespaceForXmlObject(parent, untypifiedNode);
        if (namespace != null) {
            XTreeTag newItemChild;
            try {
                final String className = untypifiedNode.getDomNode().getLocalName();
                final DefManager defManager = treeWindow.getEnvironment().getApplication().getDefManager();
                final Class<XmlObject> tagClass =
                        defManager.getXmlBeansClassByNodeName(namespace, className);
                XmlObject typifiedNode;
                try{
                    typifiedNode =
                        XmlObjectProcessor.castToXmlClass(defManager.getClassLoader(), untypifiedNode, tagClass);
                }
                catch(AppError error){
                    final MessageProvider messageProvider = treeWindow.getEnvironment().getMessageProvider();
                    final String message = messageProvider.translate("XmlEditor", "Can't typify item '%s'");
                    final String formattedMessage = String.format(message, untypifiedNode.getDomNode().getNodeName());
                    treeWindow.getEnvironment().getTracer().error(formattedMessage, error);
                    newItemChild =
                        new XTreeTag(treeWindow, untypifiedNode);
                    addTreeItem(parent, newItemChild);
                    return;
                }
                SchemaType nativeSchemaType = typifiedNode.schemaType();
                final XmlCursor xmlCursor = typifiedNode.newCursor();
                try {
                    if (typifiedNode.schemaType().isDocumentType()) {
                        xmlCursor.toNextToken();
                        if (xmlCursor.isStart()) {
                            typifiedNode = xmlCursor.getObject();
                            nativeSchemaType = typifiedNode.schemaType();
                        }
                    } else if (xmlCursor.isStartdoc()) {
                        xmlCursor.toNextToken();
                        if (xmlCursor.isStart()) {
                            typifiedNode = xmlCursor.getObject();
                        }
                    }
                } finally {
                    xmlCursor.dispose();
                }
                newItemChild =
                        new XTreeTag(treeWindow, typifiedNode, nativeSchemaType, untypifiedNode);
            } catch (ClassNotFoundException exception) {
                treeWindow.getEnvironment().getTracer().warning("Class Not Found: " + exception.getMessage());
                newItemChild =
                        new XTreeTag(treeWindow, untypifiedNode);
            } catch (DefinitionError exception) {
                treeWindow.getEnvironment().getTracer().warning(exception.getMessage());
                newItemChild =
                        new XTreeTag(treeWindow, untypifiedNode);
            }
            addTreeItem(parent, newItemChild);
        }else{
            final XTreeTag newItemChild = new XTreeTag(treeWindow, untypifiedNode);                        
            addTreeItem(parent, newItemChild);
        }
    }
    
    private void addTreeItem(final XTreeTag parent, final XTreeTag newItemChild){
        parent.addChild(newItemChild);
        if (hasChildren(newItemChild)) {
            newItemChild.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
        }                
        newItemChild.init();
        if (newItemChild.getEditor()!=null){
            tree.setItemWidget(newItemChild, 1, newItemChild.getEditor());
        }        
    }

    private void processCurrentToken(final XTreeTag parent, final int index, final XmlCursor cursor) {//parent может быть null
        switch (cursor.currentTokenType().intValue()) {
            case TokenType.INT_NAMESPACE: {
                QName uriName = cursor.getName();
                String prefix = uriName.getLocalPart();
                String namespaceURI = uriName.getNamespaceURI();
                if (prefix != null) {                    
                    SchemaTypeSystem typeSystem = findParentTypeSystem(parent);                    
                    if (typeSystem == null) {
                        cursor.push();
                        try{
                            cursor.toStartDoc();
                            typeSystem = cursor.getObject().schemaType().getTypeSystem();
                        }finally{
                            cursor.pop();
                        }
                    }
                    treeWindow.addToEditorsNamespaceMap(namespaceURI, prefix);
                    namespaceMaps.addPrefixForNamespace(typeSystem, prefix, namespaceURI);
                }
            }
            break;
            case TokenType.INT_STARTDOC: {
                if (cursor.getDomNode().getNodeType()==Node.DOCUMENT_FRAGMENT_NODE){
                    final XFragment rootItem = new XFragment(treeWindow, cursor.getObject());
                    tree.addTopLevelItem(rootItem);
                    if (hasChildren(rootItem)) {
                        rootItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
                    }                    
                }
            }
            break;
            case TokenType.INT_START: {
                addChildNodeItem(parent, index, cursor);
                cursor.toEndToken();
            }
            break;
            case TokenType.INT_ATTR: {
                if (parent!=null){
                    addChildAttributeItem(parent, cursor);
                }
            }
            break;
            case TokenType.INT_PROCINST:
            case TokenType.INT_COMMENT: {
                XValue newItem = new XValue(treeWindow, cursor.getDomNode());
                if (parent != null) {
                    parent.addChild(newItem);
                    parent.treeWidget().setItemWidget(newItem, 1, newItem.getEditor());
                } else {
                    tree.addTopLevelItem(newItem);
                    tree.setItemWidget(newItem, 1, newItem.getEditor());
                }
            }
            break;
        }
    }

    private void addChildNodeItem(final XTreeTag parent, final int index, final XmlCursor cursor) {//parent может быть null
        final XmlObject node = cursor.getObject();
        final XTreeTag newItem;
        
        if (parent != null && parent.isExternalTypeSystem()) {
            final SchemaType parentNodeType = parent.getSchemaType();
            final SchemaType childNodeType = parentNodeType.getElementType(cursor.getName(), null, parentNodeType.getTypeSystem());
            newItem = new XTreeTag(treeWindow, node, childNodeType, null);
        } else {
            newItem = new XTreeTag(treeWindow, node);
        }

        if (parent != null) {
            if (index > -1 && index <= parent.childCount()) {
                parent.insertChild(index, newItem);
            } else {
                parent.addChild(newItem);
            }
            newItem.init();
        } else {
            tree.addTopLevelItem(newItem);
            newItem.init();
        }

        //**********************************************************
        if (!isReadonly()) {
            final SchemaParticle[] choice = newItem.getChoices();
            final SchemaParticle[] seqInChoice = newItem.checkForSequenceInChoice(newItem.getNode(), choice);
            if (seqInChoice != null) {
                if (XElementTools.checkForModelMembers(newItem, seqInChoice)) {
                    newItem.setFirstChoice(false);
                }
            }
            if (choice != null && newItem.isFirstChoice()) {
                final List<String> list = new ArrayList<>();
                final String prefix = newItem.getElementPrefix();
                initlist(list, choice, prefix);
                final XComboBox box = new XComboBox(newItem, list);
                newItem.setData(0, Qt.ItemDataRole.UserRole, box);
                newItem.setText(0, newItem.text(0) + " \u21B4");
            }
        }
        //**********************************************************
        if (newItem.getEditor() != null) {
            tree.setItemWidget(newItem, 1, newItem.getEditor());
        }

        final String ann = newItem.getAnnotationText();
        if (ann != null && !ann.isEmpty()) {
            newItem.setToolTip(0, ann);
        }

        if (hasChildren(newItem)) {
            newItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
        }
    }

    private void addChildAttributeItem(final XTreeTag parent, final XmlCursor cursor) {//parent не может быть null
        final String name = cursor.getName().getLocalPart();
        final SchemaLocalAttribute def = XElementTools.getAttrDef(name, parent.getSchemaType());        
        if (XElementTools.isXsiNilAttr(cursor)){
            return;//ignore xsi:nil attribute
        }
        final XmlObject attribute = cursor.getObject();
        final XTreeAttribute newItem = new XTreeAttribute(treeWindow, attribute, def, isReadonly());
        parent.addChild(newItem);
        if (attribute.getDomNode().getNodeValue() != null) {
            tree.setItemWidget(newItem, 1, newItem.getEditor());
        }
        final String ann = newItem.getAnnotationText();
        if (ann != null && !ann.isEmpty()) {
            newItem.setToolTip(0, ann);
        }
    }

    private static SchemaTypeSystem findParentTypeSystem(final XTreeTag parentItem) {
        for (XTreeTag nodeItem = parentItem; nodeItem != null; nodeItem = (XTreeTag) nodeItem.parent()) {
            if (!nodeItem.isAnyType()) {
                return nodeItem.getSchemaType().getTypeSystem();
            }
        }
        return null;
    }

    private boolean hasChildren(final XTreeTag nodeItem) {
        return XElementTools.hasChildNodes(nodeItem.getNode());
    }

    private String findNamespaceForXmlObject(final XTreeTag parentItem, final XmlObject node) {
        final String prefix = node.getDomNode().getPrefix();
        if (prefix==null || prefix.isEmpty()){
            final String selfNameSpace = readNamespaceURI(node);
            return selfNameSpace==null ? readNamespaceURI(parentItem.getNode()) : selfNameSpace;
        }
        final List<SchemaTypeSystem> lookedTypeSystems = new ArrayList<>(16);
        String namespace = null;
        SchemaTypeSystem typeSystem;

        for (XTreeTag nodeItem = parentItem; nodeItem != null; nodeItem = (XTreeTag) nodeItem.parent()) {
            if (!nodeItem.isAnyType()) {
                typeSystem = nodeItem.getSchemaType().getTypeSystem();
                if (!lookedTypeSystems.contains(typeSystem)) {
                    lookedTypeSystems.add(typeSystem);
                    namespace = namespaceMaps.findNamespaceForPrefix(typeSystem, prefix);
                    if (namespace != null) {
                        break;
                    }
                }
            }
        }
        
        if (namespace==null || namespace.isEmpty()){
            final String selfNameSpace = readNamespaceURI(node);
            return selfNameSpace==null ? readNamespaceURI(parentItem.getNode()) : selfNameSpace;            
        }else{
            return namespace;
        }
    }
    
    private static String readNamespaceURI(final XmlObject node){
        final XmlCursor cursor = node.newCursor();
        try {
            boolean stop = false;
            while ((!cursor.isNamespace() || !cursor.getName().getLocalPart().isEmpty()) && !stop) {
                cursor.toNextToken();
                if (cursor.isStart() || cursor.isEnd() || cursor.isEnddoc()) {
                    stop = true;
                }
            }
            return stop ? null : cursor.getName().getNamespaceURI();
        } finally {
            cursor.dispose();
        }        
    }

    private static void initlist(final List<String> list, final SchemaParticle[] choice, String prefix) {
        if (!prefix.isEmpty()) {
            prefix += ":";
        }
        for (SchemaParticle p : choice) {
            if (p.getParticleType() == SchemaParticle.ELEMENT) {
                list.add(prefix + p.getName().getLocalPart());
            } else {
                String item = XElementTools.getTitleForSequence(p);
                list.add(item);
            }
        }
    }
}
