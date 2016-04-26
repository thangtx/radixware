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
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QIcon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.KernelIcon;


final class ChooseSelectorPresentationModel extends QAbstractItemModel {

    private abstract static class Node {

        private final AdsDefinition definition;
        private final Node parent;
        private final int internalId;
        private long indexInTree;

        protected Node(final AdsDefinition adsDef, final Node parentNode) {
            definition = adsDef;
            parent = parentNode;
            internalId = 7 + 67 * definition.getId().toString().hashCode()
                    + 67 * (parent == null ? 0 : parent.hashCode());
        }

        public Node parent() {
            return parent;
        }

        public AdsDefinition definition() {
            return definition;
        }

        @Override
        public int hashCode() {
            return internalId;
        }

        public String text() {
            return definition.getName();
        }

        public QIcon icon() {
            return ExplorerIcon.getQIcon(KernelIcon.getInstance(definition.getIcon()));
        }

        public void setInternalIndex(final long index) {
            indexInTree = index;
        }

        public long internalIndex() {
            return indexInTree;
        }
    }

    private static class SelectorPresentationNode extends Node {

        public SelectorPresentationNode(final AdsSelectorPresentationDef selectorPresentation, final Node parent) {
            super(selectorPresentation, parent);
        }

        public AdsSelectorPresentationDef selectorPresentationDef() {
            return (AdsSelectorPresentationDef) definition();
        }
    }

    private static class ClassDefNode extends Node {

        private List<SelectorPresentationNode> childNodes = null;

        public ClassDefNode(final AdsEntityObjectClassDef classDef) {
            super(classDef, null);
        }

        public AdsEntityObjectClassDef entityClassDef() {
            return (AdsEntityObjectClassDef) definition();
        }

        public List<SelectorPresentationNode> childNodes() {
            if (childNodes == null) {
                childNodes = new LinkedList<SelectorPresentationNode>();
                final AdsEntityObjectClassDef adsClass = entityClassDef();
                final List<AdsSelectorPresentationDef> selectorPresentations =
                        adsClass.getPresentations().getSelectorPresentations().get(EScope.ALL);
                for (AdsSelectorPresentationDef selectorPresentation : selectorPresentations) {
                    if (!selectorPresentation.getRestrictions().isDenied(ERestriction.CONTEXTLESS_USAGE)) {
                        childNodes.add(new SelectorPresentationNode(selectorPresentation, this));
                    }
                }
            }
            return childNodes;
        }
    }
    final public static int FILTER_ROLE = Qt.ItemDataRole.UserRole + 2;
    private final Branch branch;
    private final List<ClassDefNode> classes = new ArrayList<ClassDefNode>();
    private final List<Id> classIds = new ArrayList<Id>();
    private final Map<Long, Node> data = new HashMap<Long, Node>(64);
    private final Map<Long, QModelIndex> indexes = new HashMap<Long, QModelIndex>(64);

    private static class AdsEntityClassesSearcher extends AdsVisitorProvider.AdsTopLevelDefVisitorProvider {

        public AdsEntityClassesSearcher() {
            super();
        }

        @Override
        public boolean isTarget(final RadixObject radixObject) {
            return (radixObject instanceof AdsEntityObjectClassDef);
        }
    }
    private final IVisitor classesCollector = new IVisitor() {

        @Override
        public void accept(final RadixObject radixObject) {
            if (radixObject instanceof AdsEntityObjectClassDef) {
                final AdsEntityObjectClassDef adsClass = (AdsEntityObjectClassDef) radixObject;
                final List<AdsSelectorPresentationDef> selectorPresentations =
                        adsClass.getPresentations().getSelectorPresentations().get(EScope.LOCAL_AND_OVERWRITE);
                if (!selectorPresentations.isEmpty() && !classIds.contains(adsClass.getId())) {
                    for (AdsSelectorPresentationDef selectorPresentation : selectorPresentations) {
                        if (!selectorPresentation.getRestrictions().isDenied(ERestriction.CONTEXTLESS_USAGE)) {
                            classIds.add(adsClass.getId());
                            classes.add(new ClassDefNode(adsClass));
                            break;
                        }
                    }
                }
            }
        }
    };

    private void collectClasses() {
        final AdsEntityClassesSearcher entitySearcher = new AdsEntityClassesSearcher();
        branch.visit(classesCollector, entitySearcher);
    }

    public ChooseSelectorPresentationModel(final Branch radixBranch) {
        super();
        branch = radixBranch;
        collectClasses();
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
                    return node.text();
                } else {
                    final ClassDefNode classNode;
                    if (node instanceof ClassDefNode) {
                        classNode = (ClassDefNode) node;
                    } else {
                        classNode = (ClassDefNode) node.parent();
                    }
                    return index.parent() == null ? classNode.definition().getModule().getQualifiedName() : classNode.definition().getQualifiedName();
                }
            case Qt.ItemDataRole.DecorationRole:
                return index.column() == 0 ? node.icon() : null;
            case FILTER_ROLE:
                return data(index.parent() == null ? index : index.parent(), Qt.ItemDataRole.DisplayRole);
            case Qt.ItemDataRole.TextAlignmentRole:
                return index.column() == 0 ? Qt.AlignmentFlag.AlignLeft.value() : Qt.AlignmentFlag.AlignRight.value();
            case Qt.ItemDataRole.ForegroundRole:
                return index.column() == 0 ? QColor.black : QColor.gray;
            case Qt.ItemDataRole.UserRole:
                return node.definition();
        }
        return null;
    }

    public AdsSelectorPresentationDef presentation(final QModelIndex index) {
        final Node node = findNodeByIndex(index);
        return node instanceof SelectorPresentationNode ? ((SelectorPresentationNode) node).selectorPresentationDef() : null;
    }

    public AdsDefinition definition(final QModelIndex index) {
        final Node node = findNodeByIndex(index);
        return node == null ? null : node.definition();
    }
}
