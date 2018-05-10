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

package org.radixware.kernel.explorer.models;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPalette;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDomainDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEnumItem;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlFunctionParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlOutgoingReference;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReferences;
import org.radixware.kernel.common.client.meta.sqml.ISqmlPackageDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableColumns;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndexDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableIndices;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import java.util.Objects;
import org.radixware.kernel.common.client.meta.sqml.ISqmlSelectorPresentationDef;
import org.radixware.kernel.common.enums.EValType;

/**
 * Qt-модель древовидного представления {@link ISqmlDefinition sqml-дефиниций}
 */
public class SqmlTreeModel extends QAbstractItemModel {
    
    private final static QColor DEFAULT_TEXT_COLOR = QApplication.palette().color(QPalette.ColorRole.WindowText);

    private static class Node {

        private final static QFont DEFAULT_FONT = QApplication.font();
        private final static QFont BOLD_FONT = QApplication.font().clone();
        private final static QFont STRICKE_OUT_FONT = QApplication.font().clone();
        
        static {
            BOLD_FONT.setBold(true);
            STRICKE_OUT_FONT.setStrikeOut(true);
        }
        private final String referencedItemTitle = Application.translate("ClassDefTreeModel", "Referenced objects");
        private final String parametersItemTitle = Application.translate("ClassDefTreeModel", "Parameters");
        private final Object data;
        private final Node parent;
        private final int internalId;
        private long indexInTree;
        private List<Node> childNodes;

        public Node(final Object nodeData, final Node parentNode) {
            data = nodeData;
            parent = parentNode;
            if (data instanceof ISqmlTableDef) {
                final ISqmlTableDef table = (ISqmlTableDef) data;
                internalId = 7
                        + 67 * (table.hasAlias() ? table.getAlias().hashCode() : 0)
                        + 67 * table.getId().toString().hashCode()
                        + 67 * (parent == null ? 0 : parent.hashCode());
            } else if (data instanceof ISqmlDefinition) {
                internalId = 7
                        + 67 * ((ISqmlDefinition) data).getId().toString().hashCode()
                        + 67 * (parent == null ? 0 : parent.hashCode());
            } else if (data instanceof Id) {
                internalId = 7
                        + 67 * ((Id) data).toString().hashCode()
                        + 67 * (parent == null ? 0 : parent.hashCode());
            } else if (data instanceof String) {
                internalId = 7
                        + 67 * data.hashCode()
                        + 67 * (parent == null ? 0 : parent.hashCode());
            } else if (data != null) {
                internalId = 7
                        + 67 * data.getClass().getName().hashCode()
                        + 67 * (parent == null ? 0 : parent.hashCode());
            } else {
                internalId = 7
                        + 67 * (parent == null ? 0 : parent.hashCode());
            }
        }
        
        private boolean isDeprecated(){
             if (data instanceof ISqmlDefinition) {
                 return ((ISqmlDefinition)data).isDeprecated();
             }
             return false;
        }

        private void collectChildrenInTable(final ISqmlTableDef table, final EnumSet<ItemType> itemTypes) {
            if (itemTypes.contains(ItemType.PROPERTY)) {
                final ISqmlTableColumns columns = table.getColumns();
                for (ISqmlColumnDef column : columns) {
                    if (itemTypes.contains(ItemType.PROPERTY_OBJECT) || column.getType()!= EValType.OBJECT){
                        childNodes.add(new Node(column, this));
                    }
                }
            }
            if (itemTypes.contains(ItemType.REFERENCE) && table.getReferences().size() > 0) {
                final ISqmlTableReferences references = table.getReferences();
                if (itemTypes.contains(ItemType.REFERENCE_GROUP)) {
                    childNodes.add(new Node(references, this));
                } else {
                    for (ISqmlTableReference reference : references) {
                        childNodes.add(new Node(reference, this));
                    }
                }
            }
            if (itemTypes.contains(ItemType.INDEX) /*&& table.getIndices().size() > 0*/) {
                final ISqmlTableIndices indices = table.getIndices();
                childNodes.add(new Node(indices.getPrimaryIndex(),this));
                for (ISqmlTableIndexDef index : indices) {
                    childNodes.add(new Node(index, this));
                }
            }
            if (itemTypes.contains(ItemType.SELECTOR)) {
                for (ISqmlSelectorPresentationDef selectorPresentation: table.getSelectorPresentations()) {
                    childNodes.add(new Node(selectorPresentation, this));
                }
            }
        }

        public List<Node> childNodes(final EnumSet<ItemType> itemTypes) {
            if (childNodes == null) {
                childNodes = new ArrayList<>();
                if (data instanceof ISqmlTableDef) {
                    collectChildrenInTable((ISqmlTableDef) data, itemTypes);
                } else if (data instanceof ISqmlColumnDef && itemTypes.contains(ItemType.ENUIM_ITEM)) {
                    final Collection<ISqmlEnumDef> enums = ((ISqmlColumnDef) data).getEnums();
                    if (enums.size()==1){
                        final ISqmlEnumDef enumDef = enums.iterator().next();
                        if (itemTypes.contains(ItemType.ENUIM_ITEM)) {
                            for (ISqmlEnumItem enumItem : enumDef) {
                                childNodes.add(new Node(enumItem, this));
                            }
                        }
                    }else if (enums.size()>1){
                        for (ISqmlEnumDef enumDef: enums){
                            childNodes.add(new Node(enumDef, this));
                        }
                    }
                } else if(data instanceof ISqmlEnumDef && itemTypes.contains(ItemType.ENUIM_ITEM)){
                    final ISqmlEnumDef enumDef = (ISqmlEnumDef)data;
                    for (ISqmlEnumItem enumItem : enumDef) {
                        childNodes.add(new Node(enumItem, this));
                    }
                } else if (data instanceof ISqmlTableReference) {
                    final ISqmlTableReference reference = (ISqmlTableReference) data;
                    final ISqmlTableDef table = reference.findReferencedTable();
                    if (table == null) {
                        childNodes.add(new Node(reference.getReferencedTableId(), this));
                    } else {
                        collectChildrenInTable(table, itemTypes);
                    }
                } else if (data instanceof ISqmlTableReferences) {
                    final ISqmlTableReferences references = (ISqmlTableReferences) data;
                    for (ISqmlTableReference reference : references) {
                        if (reference.findReferencedTable() == null) {
                            childNodes.add(new Node(reference.getReferencedTableId(), this));
                        } else {
                            childNodes.add(new Node(reference, this));
                        }
                    }
                } else if ((data instanceof ISqmlEnumDef) && itemTypes.contains(ItemType.ENUIM_ITEM)) {
                    final ISqmlEnumDef enumDef = (ISqmlEnumDef) data;
                    for (ISqmlEnumItem enumItem : enumDef) {
                        childNodes.add(new Node(enumItem, this));
                    }
                } else if (data instanceof ISqmlParameters) {
                    final ISqmlParameters parameters = (ISqmlParameters) data;
                    for (ISqmlParameter parameter : parameters.getAll()) {
                        childNodes.add(new Node(parameter, this));
                    }
                } else if ((data instanceof ISqmlPackageDef) && itemTypes.contains(ItemType.FUNCTION)) {
                    final List<ISqmlFunctionDef> functions = ((ISqmlPackageDef) data).getAllFunctions();
                    for (ISqmlFunctionDef function : functions) {
                        childNodes.add(new Node(function, this));
                    }
                } else if ((parent instanceof ISqmlFunctionDef) && itemTypes.contains(ItemType.FUNCTION_PARAMETER)) {
                    final List<ISqmlFunctionParameter> params = ((ISqmlFunctionDef) data).getAllParameters();
                    for (ISqmlFunctionParameter param : params) {
                        childNodes.add(new Node(param, this));
                    }
                } else if (data instanceof ISqmlDomainDef){
                    final List<ISqmlDomainDef> subdomains = ((ISqmlDomainDef)data).getChildDomains();
                    for (ISqmlDomainDef domainDef: subdomains){
                        childNodes.add(new Node(domainDef,this));
                    }
                }
            }
            return childNodes;
        }

        public void clearChilds() {
            childNodes = null;
        }

        public Node parent() {
            return parent;
        }

        public Object data() {
            return data;
        }

        @Override
        public int hashCode() {
            return internalId;
        }

        public String text(final EDefinitionDisplayMode displayMode) {
            if (data instanceof ISqmlTableReferences) {
                return referencedItemTitle;
            } else if (data instanceof ISqmlParameters) {
                return parametersItemTitle;
            } else if (data instanceof ISqmlOutgoingReference) {
                final ISqmlOutgoingReference ref = (ISqmlOutgoingReference) data;
                if (ref.findReferencedTable() != null) {
                    return getDisplayedString(ref.findReferencedTable(), displayMode) + getRefColumnNames(ref.getChildColumnNames());
                } else {
                    return "??? <" + ref.getReferencedTableId() + "> ???";
                }
            } else if (data instanceof ISqmlFunctionDef) {
                if (displayMode == EDefinitionDisplayMode.SHOW_SHORT_NAMES) {
                    return ((ISqmlDefinition) data).getTitle();
                } else {
                    return ((ISqmlDefinition) data).getDisplayableText(displayMode);
                }
            } else if ((data instanceof ISqmlTableDef) && parent == null) {
                final ISqmlTableDef table = (ISqmlTableDef) data;
                return table.hasAlias() ? table.getAlias() : getDisplayedString(table, displayMode);
            } else if (data instanceof ISqmlDefinition) {
                return getDisplayedString((ISqmlDefinition) data, displayMode);
            } else if (data instanceof Id) {
                return "??? <" + ((Id) data).toString() + "> ???";
            }
            else if (data==null){
                return "null";
            }
            return "??? <" + data.getClass().getSimpleName() + "> ???";
        }

        private static String getDisplayedString(final ISqmlDefinition definition, final EDefinitionDisplayMode displayMode) {
            switch (displayMode) {
                case SHOW_FULL_NAMES:
                    return (definition instanceof ISqmlTableDef) || (definition instanceof ISqmlEnumDef) ? definition.getFullName() : definition.getShortName();
                case SHOW_TITLES:
                    return definition.getTitle();
                default:
                    return definition.getShortName();
            }
        }

        private static String getRefColumnNames(final List<String> columns) {
            if (columns.isEmpty()) {
                return "";
            }
            String res = " (";
            for (int i = 0; i < columns.size(); i++) {
                res = res.concat(columns.get(i));
                if (i < (columns.size() - 1)) {
                    res = res.concat(", ");
                }
            }
            return res.concat(")");
        }

        public QIcon icon() {
            if (data instanceof ISqmlTableReference) {
                final ISqmlTableDef table = ((ISqmlTableReference) data).findReferencedTable();
                if (table != null) {
                    return ExplorerIcon.getQIcon(table.getIcon());
                }
            }
            return data instanceof ISqmlDefinition ? ExplorerIcon.getQIcon(((ISqmlDefinition) data).getIcon()) : null;
        }

        public ISqmlDefinition definition() {
            return data instanceof ISqmlDefinition ? (ISqmlDefinition) data : null;
        }

        public QFont font(final boolean markDeprecatedItems) {
            if (data instanceof Iterable){
                return BOLD_FONT;
            }else if (markDeprecatedItems && isDeprecated()){
                return STRICKE_OUT_FONT;
            }else{
                return DEFAULT_FONT;
            }
        }

        public void setInternalIndex(final long index) {
            indexInTree = index;
        }

        public long internalIndex() {
            return indexInTree;
        }
    }
    
    final public static int FILTER_ROLE = Qt.ItemDataRole.UserRole + 2;    
    
    private final Map<Long, Node> data = new HashMap<>(64);
    private final Map<Long, QModelIndex> indexes = new HashMap<>(64);
    /**
     * Режимы отображения элементов дерева:
     * SHOW_NAMES - показывать имена
     * SHOW_TITLES - показывать заголовки
     */
    private EDefinitionDisplayMode displayMode = EDefinitionDisplayMode.SHOW_SHORT_NAMES;

    /**
     * Типы, отображаемых в дереве, элементов
     */
    public static enum ItemType {

        PROPERTY, PROPERTY_OBJECT, ENUIM_ITEM, REFERENCE, INDEX, REFERENCE_GROUP, FUNCTION, FUNCTION_PARAMETER, MODULE_INFO, SELECTOR
    };
    private final EnumSet<ItemType> itemTypes = EnumSet.allOf(ItemType.class);
    private final List<Node> rootNodes = new ArrayList<>();
    private boolean markDeprecatedItems = false;

    //private final ISqmlParameters parameters;
    /**
     * Конструктор модели.
     * В качестве первого параметра передается список дефиниций верхнего уровня.
     * Если список пуст, то инстанция модели не будет содержать ни одного элемента.
     * Если список равен null, то инстанция модели будет содержать все таблицы в текущей версии.
     * Второй параметр определяет элементы каких типов будут присутствовать в модели.
     * Если равен null, то в модели будут находится элементы всех поддерживаемых типов.
     * Третий параметр задает набор sqml-параметров (параметров фильтра), которые можно использовать в условии
     * Если равен null, то использовать параметры запрещено
     * @param topLevelDefinitions - список дефиниций верхнего уровня. Может быть null.
     * @param defTypes - Набор типов дефиниций. Может быть null.
     */
    public SqmlTreeModel(final IClientEnvironment environment, final List<ISqmlDefinition> topLevelDefinitions, final EnumSet<ItemType> defTypes, final ISqmlParameters sqmlParameters) {
        this(environment, topLevelDefinitions, sqmlParameters);
        if (defTypes != null) {
            this.itemTypes.clear();
            this.itemTypes.addAll(defTypes);
        }
    }

    public SqmlTreeModel(final IClientEnvironment environment, final List<ISqmlDefinition> topLevelDefinitions, final EnumSet<ItemType> defTypes) {
        this(environment, topLevelDefinitions, defTypes, null);
    }

    public SqmlTreeModel(final IClientEnvironment environment, final List<ISqmlDefinition> topLevelDefinitions, final ISqmlParameters sqmlParameters) {
        super();
        if (topLevelDefinitions == null) {
            final Collection<ISqmlTableDef> tables = environment.getSqmlDefinitions().getTables();
            for (ISqmlTableDef table : tables) {
                rootNodes.add(new Node(table, null));
            }
        } else {
            for (ISqmlDefinition definition : topLevelDefinitions) {
                rootNodes.add(new Node(definition, null));
            }
        }
        if (sqmlParameters != null) {
            rootNodes.add(new Node(sqmlParameters, null));
        }
    }

    public SqmlTreeModel(final IClientEnvironment environment, final List<ISqmlDefinition> topLevelDefinitions) {
        this(environment, topLevelDefinitions, (ISqmlParameters) null);
    }

    private Node findNodeByIndex(final QModelIndex index) {
        return index != null ? data.get(index.internalId()) : null;
    }

    private QModelIndex findIndexByNode(final Node node) {
        return node != null ? indexes.get(node.internalIndex()) : null;
    }

    @SuppressWarnings("empty-statement")
    private static Node rootNode(final Node node) {
        Node parentNode;
        for (parentNode = node; parentNode.parent() != null; parentNode = parentNode.parent());
        return parentNode;
    }

    @SuppressWarnings("empty-statement")
    private static QModelIndex rootIndex(final QModelIndex index) {
        QModelIndex rootIndex;
        for (rootIndex = index; rootIndex.parent() != null; rootIndex = rootIndex.parent());
        return rootIndex;
    }

    private int findSqmlParameters() {
        for (int i = rootNodes.size() - 1; i >= 0; i--) {
            if (rootNodes.get(i).data() instanceof ISqmlParameters) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public QModelIndex index(int row, int column, QModelIndex parentIndex) {
        if (row < 0 || row >= rowCount(parentIndex)) {
            return null;//for internal call on remove last row.
        }
        if (column < 0 || column > 1) {
            return null;
        }
        final Node parentNode = parentIndex == null ? null : findNodeByIndex(parentIndex);
        final Node node = parentNode == null ? rootNodes.get(row) : parentNode.childNodes(itemTypes).get(row);
        final QModelIndex result = createIndex(row, column, node.hashCode());
        if (column == 0 && !indexes.containsKey(node.internalIndex())) {
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
    public int rowCount(QModelIndex parentIndex) {
        if (parentIndex == null) {
            return rootNodes.size();
        }
        final Node parentNode = findNodeByIndex(parentIndex);
        return parentNode.childNodes(itemTypes).size();
    }

    @Override
    public int columnCount(QModelIndex index) {
        return itemTypes.contains(ItemType.MODULE_INFO) ? 2 : 1;
    }

    @Override
    public Object data(final QModelIndex index, final int role) {
        final Node node = findNodeByIndex(index);
        if (node == null) {
            return null;
        }
        switch (role) {
            case ItemDataRole.FontRole:
                return node.font(markDeprecatedItems);
            case ItemDataRole.DisplayRole:
                if (index.column() == 0) {
                    return node.text(displayMode);
                } else {
                    final Node rootNode = rootNode(node);
                    if (rootNode.definition() == null) {
                        return "";
                    }
                    return index.parent() == null ? rootNode.definition().getModuleName() : rootNode.definition().getFullName();
                }
            case ItemDataRole.DecorationRole:
                return index.column() == 0 ? node.icon() : null;
            case FILTER_ROLE:
                return data(rootIndex(index), Qt.ItemDataRole.DisplayRole);
            case ItemDataRole.TextAlignmentRole:
                final Qt.Alignment alignment = new Qt.Alignment(Qt.AlignmentFlag.AlignVCenter);
                alignment.set(index.column() == 0 ? Qt.AlignmentFlag.AlignLeft : Qt.AlignmentFlag.AlignRight);
                return alignment.value();            
            case ItemDataRole.ForegroundRole:
                return index.column() == 0 ? DEFAULT_TEXT_COLOR : QColor.gray;
            //case SqmlDefinitionsTreeItemDelegate.SELECTED_ITEM_FOREGROUND_ROLE:
            //    return markDeprecatedItems && node.isDeprecated() ? QColor.red : null;
            case ItemDataRole.UserRole:
                return node.data();
        }
        return null;
    }
    
    private void reinit(final boolean clearChildNodes) {
        data.clear();
        indexes.clear();
        if (clearChildNodes) {
            for (Node rootNode : rootNodes) {
                rootNode.clearChilds();
            }
        }
        reset();
    }

    /**
     * Включить в модель дефиниции указанного типа
     */
    public final void showDefinitions(final ItemType type) {
        if (type != null) {
            itemTypes.add(type);
            reinit(true);
        }
    }

    /**
     * Включить в модель дефиниции указанных типов
     */
    public final void showDefinitions(final EnumSet<ItemType> types) {
        itemTypes.clear();
        itemTypes.addAll(types == null ? EnumSet.allOf(ItemType.class) : types);
        reinit(true);
    }

    /**
     * Убрать из модели дефиниции указанного типа
     */
    public final void hideDefinitions(final ItemType type) {
        itemTypes.remove(type);
        reinit(true);
    }

    public final EnumSet<ItemType> getVisibleDefinitionTypes() {
        return itemTypes.clone();
    }

    public final void setDisplayMode(final EDefinitionDisplayMode mode) {
        if (mode != displayMode) {
            displayMode = mode;
            //this.reset();
        }
    }

    public final EDefinitionDisplayMode displayMode() {
        return displayMode;
    }

    public void setTopLevelDefinitions(final List<ISqmlDefinition> defList) {
        if (defList != null) {
            rootNodes.clear();
            for (ISqmlDefinition definition : defList) {
                rootNodes.add(new Node(definition, null));
            }
            reinit(false);
        }
    }
    
    public void addTopLevelDefinitions(final List<ISqmlDefinition> defList) {
        if (defList != null && !defList.isEmpty()) {
            for (ISqmlDefinition definition : defList) {
                rootNodes.add(new Node(definition, null));
            }
            reinit(false);
        }
    }
    
    public void removeTopLevelDefinitions(final Collection<ISqmlDefinition> defList){
        if (defList!=null){
            final int keepSize = rootNodes.size();
            for (int i = rootNodes.size()-1; i>0; i--){
                for (ISqmlDefinition definition: defList){
                    if (sameDefinitions(rootNodes.get(i).definition(), definition)){
                        rootNodes.remove(i);
                        break;
                    }
                }   
            }
            if (rootNodes.size()!=keepSize){
                reinit(false);
            }
        }
    }
    
    private static boolean sameDefinitions(ISqmlDefinition def1, ISqmlDefinition def2){
        if (def1==def2){
            return true;
        }
        if (def1==null || def2==null){
            return false;
        }
        
        if (!Objects.equals(def1.getId(), def2.getId())){
            return false;
        }
        
        if (def1 instanceof ISqmlTableDef && def2 instanceof ISqmlTableDef){
            if ( !Objects.equals( ((ISqmlTableDef)def1).getAlias(), ((ISqmlTableDef)def2).getAlias() ) ){
                return false;
            }
        }
        return true;
    }

    public void clear() {
        rootNodes.clear();
        reinit(false);
    }

    public void addTable(final ISqmlTableDef table) {
        rootNodes.add(new Node(table, null));
        reinit(false);
    }

    public QModelIndex addParameter(final ISqmlModifiableParameter parameter) {
        final int row = findSqmlParameters();
        final Node parametersNode = row >= 0 ? rootNodes.get(row) : null;
        final ISqmlParameters parameters = parametersNode == null ? null : (ISqmlParameters) parametersNode.data();
        if (parameter != null && parameters != null) {
            final int parameterIdx = parameters.addParameter(parameter);
            reinit(true);
            parametersNode.childNodes(itemTypes).get(parameterIdx);
            final QModelIndex parametersIndex = index(row, 0, null);
            return index(parameterIdx, 0, parametersIndex);
        }
        return null;
    }

    public boolean removeParameter(final ISqmlParameter parameter) {
        final int row = findSqmlParameters();
        final ISqmlParameters parameters = row >= 0 ? (ISqmlParameters) rootNodes.get(row).data() : null;
        if (parameter != null && parameters != null) {
            for (int i = 0; i < parameters.size(); i++) {
                if (parameters.get(i).getId().equals(parameter.getId())) {
                    parameters.removeParameter(i);
                    reinit(true);
                    return true;
                }
            }
        }
        return false;
    }

    public ISqmlDefinition definition(final QModelIndex index) {
        final Node node = findNodeByIndex(index);
        return node == null ? null : node.definition();
    }

    public QModelIndex findDefinitionIndex(final ISqmlDefinition definition, final QModelIndex parentIndex) {
        if (parentIndex == null) {
            for (int row = rootNodes.size() - 1; row >= 0; row--) {
                if (rootNodes.get(row).definition() != null && rootNodes.get(row).definition().getId().equals(definition.getId())) {
                    return index(row, 0, null);
                }
            }
        } else {
            ISqmlDefinition def;
            for (int row = rowCount(parentIndex) - 1; row >= 0; row--) {
                if (index(row, 0, parentIndex).data(Qt.ItemDataRole.UserRole) instanceof ISqmlDefinition) {
                    def = (ISqmlDefinition) index(row, 0, parentIndex).data(Qt.ItemDataRole.UserRole);
                    if (def.getId().equals(definition.getId())) {
                        return index(row, 0, parentIndex);
                    }
                }
            }
        }
        return null;
    }
    
    public void setMarkDeprecatedItems(boolean markDeprecatedItems){
        this.markDeprecatedItems=markDeprecatedItems;
    }
}
