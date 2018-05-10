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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPalette;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.env.ReleaseRepository;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitions;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;


final class ChooseSelectorPresentationModel extends QAbstractItemModel {
    
    final public static int FILTER_ROLE = Qt.ItemDataRole.UserRole + 2;

    private abstract static class Node<D extends ISqmlDefinition> {

        private final Node parent;
        private final D definition;        
        private final int internalId;
        private long indexInTree;

        protected Node(final D definition, final Node parentNode) {
            this.definition = definition;
            parent = parentNode;
            internalId = 7 + 67 * definition.getId().hashCode()
                    + 67 * (parent == null ? 0 : parent.hashCode());
        }

        public Node parent() {
            return parent;
        }

        @Override
        public int hashCode() {
            return internalId;
        }
        
        public final D definition(){
            return definition;
        }
        
        public final String title(){
            return definition.getShortName();
        }
        
        public abstract String description();

        public final QIcon icon(){
            return ExplorerIcon.getQIcon(definition.getIcon());
        }
                
        public void setInternalIndex(final long index) {
            indexInTree = index;
        }

        public long internalIndex() {
            return indexInTree;
        }
    }

    private final static class SelectorPresentationNode extends Node<ISqmlSelectorPresentationDef> {
                
        private final String description;

        public SelectorPresentationNode(final ISqmlSelectorPresentationDef selectorPresentation, final ClassDefNode parent) {
            super(selectorPresentation, parent);
            description = parent.definition().getFullName();                   
        }

        @Override
        public String description() {
            return description;
        }
    }

    private final static class ClassDefNode extends Node<ISqmlTableDef> {

        private List<SelectorPresentationNode> childNodes = null;        

        public ClassDefNode(final ISqmlTableDef sqmlTable) {
            super(sqmlTable, null);
        }
        
        public List<SelectorPresentationNode> childNodes() {
            if (childNodes == null) {
                childNodes = new LinkedList<>();
                for (ISqmlSelectorPresentationDef presentation: definition().getSelectorPresentations()){
                    childNodes.add(new SelectorPresentationNode(presentation, this));
                }
            }
            return childNodes;
        }

        @Override
        public String description() {
            return definition().getModuleName();
        }
    }
    
    private final List<ClassDefNode> classes = new ArrayList<>();
    private final List<Id> classIds = new ArrayList<>();
    private final Map<Long, Node> data = new HashMap<>(64);
    private final Map<Long, QModelIndex> indexes = new HashMap<>(64);    

    public ChooseSelectorPresentationModel(final IClientEnvironment environment) {
        super();
        collectClasses(environment);
    }
    
    private void collectClasses(final IClientEnvironment environment) {        
        final ISqmlDefinitions sqmlDefs = environment.getSqmlDefinitions();
        final ITaskWaiter taskWaiter = environment.getApplication().newTaskWaiter();
        final List<ISqmlTableDef> sqmlClasses;
        try {
            final ISqmlTableDef[] classes = taskWaiter.runAndWait(new Callable<ISqmlTableDef[]>() {
                @Override
                public ISqmlTableDef[] call() throws Exception {
                    final Collection<ReleaseRepository.DefinitionInfo> defs = 
                            environment.getDefManager().getRepository().getDefinitions(EDefType.CLASS);
                    return defInfosToSqmlDef(sqmlDefs,defs);
                }
            });
            sqmlClasses = Arrays.asList(classes);
        }catch (ExecutionException ex) {
            environment.processException(environment.getMessageProvider().translate("SqmlEditor", "Failed to build classes list"), ex.getCause());
            return;
        }catch (InterruptedException ex){
            return;
            //Do nothing
        }finally {
            taskWaiter.close();
        }                            
        for (ISqmlTableDef classDef: sqmlClasses){
            if (classDef.hasEntityClass() && classDef.getSelectorPresentations().size()>0){
                classes.add(new ClassDefNode(classDef));
            }
        }
    }
    
    private static ISqmlTableDef[] defInfosToSqmlDef(final ISqmlDefinitions sqmlDefs, final Collection<ReleaseRepository.DefinitionInfo> defs) {
        final List<ISqmlTableDef> result = new LinkedList<>();        
        for (ReleaseRepository.DefinitionInfo defInfo : defs) {
            EDefinitionIdPrefix prefix = defInfo.id.getPrefix();
            if (prefix == EDefinitionIdPrefix.ADS_ENTITY_CLASS || prefix == EDefinitionIdPrefix.ADS_APPLICATION_CLASS) {
                final ISqmlTableDef foundDef = sqmlDefs.findTableById(defInfo.id);
                if (foundDef != null) {
                    result.add(foundDef);
                }
            }
        }
        return result.<ISqmlTableDef>toArray(new ISqmlTableDef[0]);
    }    

    private Node findNodeByIndex(final QModelIndex index) {
        return index != null ? data.get(index.internalId()) : null;
    }

    private QModelIndex findIndexByNode(final Node node) {
        return node != null ? indexes.get(node.internalIndex()) : null;
    }

    @Override
    public QModelIndex index(int row, int column, QModelIndex parentIndex) {
        if (row < 0 || row >= rowCount(parentIndex)) {
            return null;//for internal call on remove last row.
        }
        if (column < 0 || column > 1) {
            return null;
        }
        final ClassDefNode classNode = parentIndex == null ? null : classes.get(parentIndex.row());
        final Node node = classNode == null ? classes.get(row) : classNode.childNodes().get(row);
        final QModelIndex result = createIndex(row, column, node.hashCode());
        if (column == 0 && node.internalIndex() != result.internalId()) {
            node.setInternalIndex(result.internalId());
            indexes.put(node.internalIndex(), result);
            data.put(node.internalIndex(), node);
        }
        return result;
    }

    @Override
    public QModelIndex parent(QModelIndex index) {
        final Node childNode = findNodeByIndex(index);
        return childNode != null ? findIndexByNode(childNode.parent()) : null;
    }

    @Override
    public int columnCount(QModelIndex index) {
        return 2;
    }

    @Override
    public int rowCount(QModelIndex index) {
        if (index == null) {
            return classes.size();
        }
        if (index.row() < classes.size()) {
            final ClassDefNode classNode = classes.get(index.row());
            if (index.internalId() == classNode.internalIndex()) {
                return classNode.childNodes().size();
            }
        }
        return 0;
    }

    @Override
    public Object data(QModelIndex index, int role) {
        final Node node = findNodeByIndex(index);
        if (node == null) {
            return null;
        }
        switch (role) {
            case Qt.ItemDataRole.DisplayRole:
                if (index.column() == 0) {
                    return node.title();
                } else {
                    return node.description();
                }
            case Qt.ItemDataRole.DecorationRole:
                return index.column() == 0 ? node.icon() : null;
            case FILTER_ROLE:
                return data(index.parent() == null ? index : index.parent(), Qt.ItemDataRole.DisplayRole);
            case Qt.ItemDataRole.TextAlignmentRole:
                return index.column() == 0 ? Qt.AlignmentFlag.AlignLeft.value() : Qt.AlignmentFlag.AlignRight.value();
            case Qt.ItemDataRole.ForegroundRole:
                return index.column() == 0 ? QApplication.palette().color(QPalette.ColorRole.WindowText) : QColor.gray;
            case Qt.ItemDataRole.UserRole:
                return node.definition();
        }
        return null;
    }

    public ISqmlSelectorPresentationDef presentation(final QModelIndex index) {
        final Node node = findNodeByIndex(index);
        return node instanceof SelectorPresentationNode ? ((SelectorPresentationNode) node).definition() : null;
    }

    public ISqmlDefinition definition(final QModelIndex index) {
        final Node node = findNodeByIndex(index);
        return node == null ? null : node.definition();
    }
}
