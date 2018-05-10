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

package org.radixware.kernel.explorer.widgets.selector;

import org.radixware.kernel.common.client.widgets.selector.SelectorSortUtils;
import java.util.ArrayList;
import java.util.List;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.EHierarchicalSelectionMode;
import org.radixware.kernel.common.client.enums.ESelectorColumnHeaderMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.errors.ActivatingPropertyError;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.errors.SettingPropertyValueError;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException;
import org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.filters.RadCommonFilter;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.HierarchicalSelection;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.ProxyGroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
import org.radixware.kernel.common.client.text.CachedTextOptionsProvider;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.ESelectorColumnVisibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.enums.ESelectorRowStyle;
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.explorer.env.ImageManager;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.editors.valeditors.TristateCheckBoxStyle;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class SelectorModel extends QAbstractItemModel {
    
    public final static int SORT_INDICATOR_ITEM_ROLE = Qt.ItemDataRole.UserRole + 1;
    public final static int COLUMN_ITEM_ROLE = Qt.ItemDataRole.UserRole + 2;    
        
    private final static int STRING_MAX_LENGTH = 1000; //максимально допустимая длина строки в ячейке DBP-1658
    private final static EnumSet<ETextOptionsMarker> BROKEN_ENTITY_MARKERS = 
        EnumSet.of(ETextOptionsMarker.SELECTOR_ROW, ETextOptionsMarker.BROKEN_REFERENCE);
    private final static String ROWS_LIMIT_CONFIG_PATH = 
        SettingNames.SYSTEM+"/"+SettingNames.SELECTOR_GROUP+"/"+SettingNames.Selector.COMMON_GROUP+"/"+SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;        
    private final static int ROWS_LOAD_LIMIT = 100;
    
    private final static Qt.ItemFlags GENERAL_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
    private final static Qt.ItemFlags ENABLED_PROPERTY_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsEditable);
    private final static Qt.ItemFlags SELECTION_COLUMN_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsUserCheckable);
    private final static Qt.ItemFlags TRISTATE_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsTristate);
    private final static Qt.ItemFlags CHECKABLE_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled, Qt.ItemFlag.ItemIsUserCheckable);    
    private final static Qt.ItemFlags TRISTATE_AND_CHECKABLE_FLAGS = 
        new Qt.ItemFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled, 
                                   Qt.ItemFlag.ItemIsTristate, Qt.ItemFlag.ItemIsUserCheckable);    
    
            
    private static class SelectorNodeInItemModel extends SelectorNode{
                
        public final long internalIndexId;
        public final SelectorNodeInItemModel parentNode;        
        
        private int row;
        private QModelIndex parentIndex;
        private GroupModel childGroupModel;        
        private int rows = -1 ;//number of rows loaded in view
        private boolean wasErrorOnDataLoading;
        private boolean wasErrorOnCheckChoosable;
        private Set<Id> errorsOnGetPropVal;
        private EntityModel brokenEntityModel;
        private Collection<SelectorNodeInItemModel> children;//unsorted
        
        public SelectorNodeInItemModel(final long internalIndexId,
                                                          final int row,
                                                          final QModelIndex parentIndex, 
                                                          final SelectorNodeInItemModel parentNode){
            super(parentNode, parentNode==null ? 7 : calcChildNodeHashCode(parentNode, row));
            this.parentIndex = parentIndex;
            this.row = row;
            this.internalIndexId = internalIndexId;
            this.parentNode = parentNode;
            if (parentNode!=null){
                if (parentNode.children==null){
                    parentNode.children = new LinkedList<>();
                }
                parentNode.children.add(this);
            }
        }

        public QModelIndex getParentIndex() {
            return parentIndex;
        }

        public void setParentIndex(final QModelIndex parentIndex) {
            this.parentIndex = parentIndex;
        }
        
        public void setRow(final int row){
            this.row = row;
        }
        
        public int getRow(){
            return row;
        }
                
        @Override
        public EntityModel getEntityModel() {
            if (brokenEntityModel==null){
                final EntityModel entityModel = parentNode.getChildEntityModel(row);
                if (entityModel instanceof BrokenEntityModel){
                    brokenEntityModel = (BrokenEntityModel)entityModel;                    
                    setHasChildren(false);
                }
                return entityModel;
            }else{
                return brokenEntityModel;
            }
        }
                
        private EntityModel getChildEntityModel(final int row){
            final GroupModel group = getChildGroupModel();
            try {
                return group == null ? null : group.getEntity(row);
            } catch (ServiceClientException | InterruptedException ex) {
                group.getEnvironment().processException(ex);
                return null;
            } catch (BrokenEntityObjectException ex) {
                return
                    new BrokenEntityModel(group.getEnvironment(), group.getSelectorPresentationDef(), ex);
            }
        }
        
        public static int calcChildNodeHashCode(final SelectorNodeInItemModel node, final int row){
            final EntityModel childEntityModel = node==null ? null : node.getChildEntityModel(row);            
            if (childEntityModel==null || childEntityModel.getPid()==null){
                return row;
            }else{
                return childEntityModel.getPid().toStr().hashCode();
            }
        }
        
        public GroupModel getChildGroupModel(){
            return childGroupModel;
        }
        
        public final void setChildGroupModel(final GroupModel group){
            if (group.getEntitiesCount()>0){
                setHasChildren(true);
            }else if (!group.hasMoreRows()){
                setHasChildren(false);
            }
            rows = -1;
            childGroupModel = group;
        }

        @Override
        public void addChildGroupModel(final GroupModel groupModel) {
            setChildGroupModel(groupModel);
        }

        @Override
        public List<GroupModel> getChildGroupModels() {
            if (childGroupModel==null){
                return Collections.emptyList();
            }else{
                return Collections.singletonList(childGroupModel);
            }
        }        
                
        public final int getNumberOfLoadedInnerNodes(){
            return rows>-1 && hasChildren() ? rows : 0;
        }
        
        public final void setNumberOfLoadedInnerNodes(final int number){
            if (number>=0){
                rows = number;
                setHasChildren(number>0);
            }
        }
        
        public final boolean wasErrorOnDataLoading(){
            return wasErrorOnDataLoading;
        }
        
        public final void registerErrorOnDataLoading(){
            wasErrorOnDataLoading = true;
        }
        
        public final void registerErrorOnGetPropertyValue(final Id columnId){
            if (errorsOnGetPropVal==null){
                errorsOnGetPropVal = new HashSet<>();
            }
            errorsOnGetPropVal.add(columnId);
        }
        
        public final boolean wasErrorOnGetPropVal(final Id columnId){
            return errorsOnGetPropVal==null ? false : errorsOnGetPropVal.contains(columnId);
        }
        
        public final void registerErrorOnCheckChoosable(){
            wasErrorOnCheckChoosable = true;
        }
        
        public final boolean wasErrorOnCheckChoosable(){
            return wasErrorOnCheckChoosable;
        }
        
        @Override
        public final void invalidate(){
            if (brokenEntityModel==null){
                super.invalidate();
                rows = -1 ;
                wasErrorOnDataLoading = false;
                wasErrorOnCheckChoosable = false;                
                errorsOnGetPropVal = null;
                children = null;
            }            
        }
        
        public final boolean canReadMore(){
            final GroupModel group = getChildGroupModel();
            return group!=null
                       && !wasErrorOnDataLoading
                       && (rows<group.getEntitiesCount() || group.hasMoreRows());
        }
        
        public final Collection<SelectorNodeInItemModel> getChildren(final boolean recursive){
            if (children==null || children.isEmpty()){
                return Collections.emptyList();
            }else if (recursive){
                final Stack<SelectorNodeInItemModel> stack = new Stack<>();
                final Collection<SelectorNodeInItemModel> result = new LinkedList<>();
                stack.add(this);
                SelectorNodeInItemModel node;
                while(!stack.isEmpty()){
                    node = stack.pop();
                    if (node.children!=null){
                        stack.addAll(node.children);
                        result.addAll(node.children);
                    }
                }
                return result;
            }else{
                return Collections.unmodifiableCollection(children);
            }
        }
        
        public final void removeChildNode(final SelectorNodeInItemModel node){
            if (children!=null){
                children.remove(node);
                if (children.isEmpty()){
                    children = null;
                }
            }
        }
    }
    
    private final static class RootSelectorNode extends SelectorNodeInItemModel{
        
        public final GroupModel groupModel;
        
        public RootSelectorNode(final GroupModel groupModel){
            super(0,-1,null,null);
            this.groupModel = groupModel;
        }

        @Override
        public EntityModel getEntityModel() {
            return null;
        }                

        @Override
        public GroupModel getChildGroupModel() {
            return groupModel;
        }     
    }
    
    private final static class EntityObjectNode extends SelectorNode{
                
        public final EntityModel entityModel;
        
        public EntityObjectNode(final SelectorNode parent, final EntityModel entityModel){
            super(parent, entityModel.getPid().toStr().hashCode());
            this.entityModel = entityModel;
        }

        @Override
        public EntityModel getEntityModel() {
            return entityModel;
        }
    }
    
    private final class Hierarchy implements HierarchicalSelection.IHierarchyDelegate<SelectorNode>{

        @Override
        public SelectorNode getParent(final SelectorNode child) {
            final SelectorNode parent = child==null ? null : child.getParentNode();
            return parent==SelectorModel.this.rootNode ? null : parent;
        }

        @Override
        public List<GroupModel> getChildGroupModels(final SelectorNode parent) {
            if (parent==null){
                return Collections.singletonList(SelectorModel.this.getRootGroupModel());
            }else if (parent instanceof SelectorNodeInItemModel){
                final boolean childrenInited = parent.isChildrenInited();
                try{
                    final QModelIndex index = SelectorModel.this.getIndex(((SelectorNodeInItemModel)parent));
                    if (index==null){
                        return Collections.singletonList(SelectorModel.this.getRootGroupModel());
                    }
                    
                    final GroupModel groupModel = SelectorModel.this.getChildGroup(index);
                    if (groupModel==null){
                        return Collections.emptyList();
                    }else{
                        return Collections.singletonList(groupModel);
                    }
                }finally{
                    if (!childrenInited){
                        parent.invalidate();                        
                    }
                }
            }else {
                final List<GroupModel> groupModels = parent.getChildGroupModels();
                if (groupModels==null || groupModels.isEmpty()){
                    final GroupModel groupModel = SelectorModel.this.getChildGroup(parent);                    
                    if (groupModel==null){
                        return Collections.emptyList();
                    }else{
                        parent.addChildGroupModel(groupModel);
                        return Collections.singletonList(groupModel);
                    }                  
                }else{
                    return groupModels;
                }
            }
        }

        @Override
        public List<SelectorNode> getVirtualChildNodes(final SelectorNode parent) {
            return Collections.<SelectorNode>emptyList();
        }

        @Override
        public int getVirtualChildNodeIndex(final SelectorNode parent, final SelectorNode node) {
            return -1;
        }

        @Override
        public GroupModel getOwnerGroupModel(final SelectorNode node) {
            final SelectorNode parent = node==null ? null : node.getParentNode();
            if (parent==null){
                return null;
            }else{
                final EntityModel entityModel = node.getEntityModel();
                if (entityModel==null){
                    return SelectorModel.getFirstChildGroup(parent);
                }else{
                    return SelectorModel.this.getOwnerGroupModel(entityModel);
                }
            }
       }

        @Override
        public EntityModel getEntityModel(final SelectorNode node) {
            return SelectorModel.this.getEntity(node);
        }

        @Override
        public SelectorNode getEntityModelNode(final SelectorNode parent, final GroupModel childGroupModel, final EntityModel entityModel, final int rowInGroup) {
            if (parent instanceof EntityObjectNode){
                 return createNode(parent, childGroupModel, entityModel);
            }
            final SelectorNodeInItemModel parentNode;
            if (parent == null){
                parentNode = SelectorModel.this.rootNode;
            }else if (parent instanceof SelectorNodeInItemModel){
                parentNode = (SelectorNodeInItemModel)parent;
            }else{
                parentNode = null;
            }
            if (parentNode==null){
                return null;
            }
            final QModelIndex parentIndex = SelectorModel.this.getIndex(parentNode);
            if (rowInGroup<parentNode.getNumberOfLoadedInnerNodes()){
                final QModelIndex index = SelectorModel.this.index(rowInGroup, 0, parentIndex);
                return index==null ? null : SelectorModel.this.getNode(index);
            }else{
                return createNode(parent, childGroupModel, entityModel);
            }
        }        
        
        private EntityObjectNode createNode(final SelectorNode parent, 
                                                                 final GroupModel groupModel,
                                                                 final EntityModel entityModel){
            final EntityModel resultModel = 
                SelectorModel.this.replaceEntityModel(groupModel, entityModel, parent.getNestingLevel()+1);
            return new EntityObjectNode(parent, resultModel);
        }

        @Override
        public String nodeToString(final SelectorNode node) {
            if (node==null){
                return null;
            }
            final EntityModel entityModel = node.getEntityModel();
            if (entityModel==null){
                if (node instanceof SelectorNodeInItemModel){
                    return String.valueOf( ((SelectorNodeInItemModel)node).getRow() );
                }else{
                    return String.valueOf( node.hashCode() );
                }
            }else{
                return entityModel.getPid().toStr();
            }
        }

        @Override
        public boolean isEquals(final SelectorNode first, final SelectorNode second) {
            return Objects.equals(first, second);
        }         
     }    
    
    private final GroupModel root;    
    private final Map<Long, SelectorNodeInItemModel> nodes = new HashMap<>();
    private final List<SelectorColumnModelItem> columns = new ArrayList<>();    
    private final QStyleOptionViewItem styleOption = new QStyleOptionViewItem();
    private final Map<String,ExplorerTextOptions> textOptionsCache = new HashMap<>(512);
    private final Hierarchy hierarchy = new Hierarchy();
    private final HierarchicalSelection<SelectorNode> selection = new HierarchicalSelection<>(hierarchy);
    private final CachedTextOptionsProvider textOptionsProvider;
    private final RootSelectorNode rootNode;    
    private final Stack<SelectorNodeInItemModel> context = new Stack<>();
    private final Qt.ItemFlags itemFlags = new Qt.ItemFlags(0);
    private SelectorNodeInItemModel contextNode;
    private boolean textOptionsCacheEnabled;
    private int textMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin);
    private int rowsLimit = 0;
    private boolean locked = false;
    private boolean selectionEnabled;
    private final String errorValueStr;
    private EnumSet<EHierarchicalSelectionMode> primarySelectionMode = EnumSet.of(EHierarchicalSelectionMode.SINGLE_OBJECT);
    private List<EHierarchicalSelectionMode> additionalSelectionModes = 
        Arrays.<EHierarchicalSelectionMode>asList(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE, 
                                                                          EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS);
    
        
    public SelectorModel(final GroupModel rootModel) {
        super();
        if (rootModel == null) {
            throw new IllegalArgumentException("root group model must be not null");
        }
        errorValueStr = rootModel.getEnvironment().getMessageProvider().translate("Value", "<!!!Error!!!>");
        rootNode = new RootSelectorNode(rootModel);
        root = rootModel;
        final RadSelectorPresentationDef presentationDef = root.getSelectorPresentationDef();
        final RadSelectorPresentationDef.SelectorColumns selectorColumns = presentationDef.getSelectorColumns();

        for (RadSelectorPresentationDef.SelectorColumn column : selectorColumns) {
            if (column.getVisibility() != ESelectorColumnVisibility.NEVER) {
                columns.add(root.getSelectorColumn(column.getPropertyId()));
            }
        }

        final int count = Math.min(root.getEntitiesCount(), ROWS_LOAD_LIMIT);
        rootNode.setNumberOfLoadedInnerNodes(count);//temporary change row count to make index method work with new rows
        if (count>0){
            final int lastValidRowIndex = verifyRows(null, 0, count - 1);
            rootNode.setNumberOfLoadedInnerNodes(lastValidRowIndex+1);
        }
        
        increaseRowsLimit();
        styleOption.setDecorationPosition(QStyleOptionViewItem.Position.Left);
        styleOption.setDecorationAlignment(Qt.AlignmentFlag.AlignCenter);
        styleOption.setDisplayAlignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
        selection.subscribe(null, rootModel);
        textOptionsProvider = new CachedTextOptionsProvider(getEnvironment().getTextOptionsProvider());
    }
    
    public void setIconSize(final QSize size){
        styleOption.setDecorationSize(size);
    }
    
    public final void setTextMargin(final int margin){
        textMargin = margin;
    }
    
    final void clearTextOptionsCache(){
        textOptionsCache.clear();
    }

    protected final IClientEnvironment getEnvironment() {
        return root.getEnvironment();
    }

    public GroupModel getRootGroupModel() {
        return root;
    }
        
    @Override
    public Qt.ItemFlags flags(final QModelIndex index) {
        if (isBrokenEntity(index)){
            return GENERAL_FLAGS;
        }
        if (isSelectionEnabled() && index.column()==0){
            final SelectorNodeInItemModel node = getNode(index);
            if (node==null || node.wasErrorOnCheckChoosable()){
                return GENERAL_FLAGS;
            }else{
                final EntityModel entity = getEntity(node);
                final GroupModel ownerGroup = entity==null ? null : getOwnerGroupModel(entity);
                if (ownerGroup!=null && ownerGroup.getEntitySelectionController().isEntityChoosable(entity)){
                    return SELECTION_COLUMN_FLAGS;
                }else{
                    return GENERAL_FLAGS;
                }
            }
        }
        if (isBrokenPropertyValue(index)){
            return GENERAL_FLAGS;
        }else{
            final Property property = getProperty(index);
            return property==null ? GENERAL_FLAGS : getItemFlagsForProperty(property);
        }
    }
    
    protected Qt.ItemFlags getItemFlagsForProperty(final Property property){
        // для bool свойств показывать флажок        
        if (canUseStandardCheckBox(property)) {
            final boolean isMandatory = property.isMandatory();
            final boolean isEditable = canEditPropertyValue(property) && property.isEnabled();
            if (isEditable && !isMandatory){
                itemFlags.setValue(TRISTATE_AND_CHECKABLE_FLAGS.value());
            }else if (isEditable){
                itemFlags.setValue(CHECKABLE_FLAGS.value());
            }else if (!isMandatory){
                itemFlags.setValue(TRISTATE_FLAGS.value());
            }
        } else if (property.isEnabled()) {
            itemFlags.setValue(ENABLED_PROPERTY_FLAGS.value());
        }else{
            itemFlags.setValue(GENERAL_FLAGS.value());
        }
        return itemFlags;
    }
    
    @Override
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    public Object data(final QModelIndex index, int role) {
        if (index == null) {
            return null;
        }
        final SelectorNodeInItemModel contextNode = getNode(index);
        if (contextNode==null){
            return null;
        }
        pushContextNode(contextNode);
        try{
            switch (role) {
                case Qt.ItemDataRole.CheckStateRole:
                    return getCheckState(index);
                case Qt.ItemDataRole.DecorationRole:
                    return getDecoration(index);
                case Qt.ItemDataRole.DisplayRole:
                    return getDisplay(index);
                case Qt.ItemDataRole.FontRole:
                    return getFont(index);
                case Qt.ItemDataRole.SizeHintRole:
                    return getSizeHint(index, false);
                case Qt.ItemDataRole.TextAlignmentRole:
                    final int alignmentFlags = getTextAlignment(index);
                    return alignmentFlags<0 ? null : alignmentFlags;
                case Qt.ItemDataRole.UserRole:
                    return getProperty(index);
                case Qt.ItemDataRole.BackgroundRole: {                
                    return getBackground(index);
                }
                case Qt.ItemDataRole.ForegroundRole: {
                    return getForeground(index);
                }
                case Qt.ItemDataRole.ToolTipRole:{
                    return getToolTip(index);
                }
                case WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE:{
                    return getRowName(index);
                }
                case WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE:{
                    return getCellName(index);
                }
                case WidgetUtils.MODEL_ITEM_CELL_VALUE_DATA_ROLE:{
                    return getCellValueAsStr(index);
                }
                case WidgetUtils.MODEL_ITEM_CELL_VALUE_IS_NULL_DATA_ROLE:{
                    return isCellValueNull(index);
                }
                default:
                    return null;
            }
        }finally{
            popContextNode();
        }
    }
    
    public final QSize getSizeHint(final QModelIndex index, final boolean approximately){        
        final int widthMargin = 2*(textMargin+1);
        if (isBrokenPropertyValue(index)){
            final QFontMetrics fontMetrics = getTextOptions(index).getFont().getQFontMetrics();
            final boolean hasIcon = getDecoration(index)!=null;
            int height = hasIcon ? styleOption.decorationSize().height() : 0;
            height = Math.max(height, fontMetrics.height());
            int width = hasIcon ? styleOption.decorationSize().width() + widthMargin : 0;
            final String text = errorValueStr;
            height = Math.max(height, fontMetrics.height());
            width+=WidgetUtils.calcTextWidth(text, fontMetrics, approximately) + 2*widthMargin;
            return new QSize(width, height);
        }else{
            final Property property = getProperty(index);
            if (property==null){
                final QFontMetrics fontMetrics = getTextOptions(index).getFont().getQFontMetrics();
                final String text = getDisplay(index);
                return new QSize(WidgetUtils.calcTextWidth(text, fontMetrics, approximately) + widthMargin, fontMetrics.height());
            }else{
                return getSizeHint(property, approximately, widthMargin, getDecoration(index)!=null);
            }
        }
    }
    
    protected QSize getSizeHint(final Property property, final boolean approximately, final int widthMargin, final boolean hasIcon){
        final boolean hasCheckBox = getCheckState(property)!=null;
        final QFontMetrics fontMetrics = getTextOptions(property).getFont().getQFontMetrics();
        if (hasCheckBox){
            int width = TristateCheckBoxStyle.INDICATOR_SIZE + 2*widthMargin;
            final int height = Math.max(TristateCheckBoxStyle.INDICATOR_SIZE, fontMetrics.height());
            if (property.getValueObject()==null){
                final String text = getTextToDisplay(property);
                if (text!=null && !text.isEmpty()){
                    width+=WidgetUtils.calcTextWidth(text, fontMetrics, approximately) + widthMargin;
                }
            }
            return new QSize(width, height);
        }
        int height = hasIcon ? styleOption.decorationSize().height() : 0;
        height = Math.max(height, fontMetrics.height());
        int width = hasIcon ? styleOption.decorationSize().width() + widthMargin : 0;
        final String text = getTextToDisplay(property);
        if (text!=null && !text.isEmpty()){
            height = Math.max(height, fontMetrics.height());
            width+=WidgetUtils.calcTextWidth(text, fontMetrics, approximately) + 2*widthMargin;
        }
        return new QSize(width, height);        
    }
    
    public final int getHeightHint(final QModelIndex index){
        if (isBrokenPropertyValue(index)){
            return getTextOptions(index).getFont().getQFontMetrics().height();
        }else{
            final Property property = getProperty(index);
            if (property==null){
                return getTextOptions(index).getFont().getQFontMetrics().height();
            }else{
                return getHeightHint(property, getDecoration(index)!=null);
            }
        }
    }
    
    protected int getHeightHint(final Property property, final boolean hasIcon){
        final boolean hasCheckBox = getCheckState(property)!=null;
        final QFontMetrics fontMetrics = getTextOptions(property).getFont().getQFontMetrics();
        if (hasCheckBox){
            return Math.max(TristateCheckBoxStyle.INDICATOR_SIZE, fontMetrics.height());
        }
        if (hasIcon){
            return Math.max(styleOption.decorationSize().height(), fontMetrics.height());
        }else{
            return fontMetrics.height();
        }        
    }
    
    private Object getCheckState(final QModelIndex index) {
        if (isSelectionEnabled() && index.column()==0){
            final EntityModel entity = getEntity(index);
            if (entity == null || (entity instanceof BrokenEntityModel)) {
                return null;
            }
            final SelectorNodeInItemModel node = getNode(index);
            final EnumSet<EHierarchicalSelectionMode> modes = calcAllSelectionModes(node);
            if (modes.isEmpty()){
                return null;
            }
            if (selection.isSelected(node)){
                return Qt.CheckState.Checked;
            }else if (selection.isSomeChildNodeSelected(node, true)){
                return Qt.CheckState.PartiallyChecked;
            }else{
                return Qt.CheckState.Unchecked;
            }
        }
        if (isBrokenPropertyValue(index)){
            return null;
        }else{
            final Property property = getProperty(index);
            return property==null ? null : getCheckState(property);
        }
    }
    
    private Qt.CheckState getCheckState(final Property property){
        if (property != null && canUseStandardCheckBox(property)) {
            Object propertyValue = property.getValueObject();
            // assume null as false
            if (propertyValue == null) {
                return Qt.CheckState.PartiallyChecked;
            } else if ((Boolean) propertyValue == true) {
                return Qt.CheckState.Checked;
            } else {
                return Qt.CheckState.Unchecked;
            }
        }
        return null;        
    }
    
    protected QIcon getDecoration(final QModelIndex index) {
        if (index.column()==(isSelectionEnabled() ? 1 : 0)){
            final EntityModel entityModel = getEntity(index);
            if (entityModel==null || entityModel instanceof BrokenEntityModel){
                return null;
            }
            final QIcon icon = getIconForEntityModel(entityModel);
            if (icon!=null){
                return icon;
            }
        }
        if (isBrokenPropertyValue(index)){
            return null;
        }else{
            final Property property = getProperty(index);
            return property == null ? null : getIconForProperty(property);
        }
    }
    
    protected QIcon getIconForEntityModel(final EntityModel entityModel){
        return null;
    }

    protected QIcon getIconForProperty(final Property property) {
        final RadEnumPresentationDef constSet = property.getDefinition().getConstSet();
        if (constSet == null || property.getDefinition().getType().isArrayType()) {
            return null;
        }
        final Object val = property.getValueObject();
        if (val == null) {
            return null;
        }
        final RadEnumPresentationDef.Item item;
        final EValType valType = property.getDefinition().getType();

        if (val instanceof IKernelEnum) {// val instanceof Long
            item = constSet.findItemForConstant((IKernelEnum) val);
        } else {
            item = constSet.getItems().findItemByValue(ValAsStr.Factory.newInstance(val, valType));
        }
        return item == null ? null : ImageManager.getQIcon(item.getIcon());
    }

    protected final String getDisplay(final QModelIndex index) {
        final EntityModel entity = getEntity(index);
        if (entity instanceof BrokenEntityModel){
            if (index.column()==firstVisibleColumn){
                final BrokenEntityModel brokenEntityModel = (BrokenEntityModel)entity;
                final String pidAsStr = brokenEntityModel.getPid()==null ? "" : brokenEntityModel.getPid().toString();
                if (brokenEntityModel.getExceptionMessage()!=null){                    
                    return pidAsStr+" "+brokenEntityModel.getExceptionClass()+": "+brokenEntityModel.getExceptionMessage();
                }
                else{
                    return pidAsStr+" "+brokenEntityModel.getExceptionClass();
                }
            }
            else{
                return "";
            }
        }else if (isBrokenPropertyValue(index)){
            return errorValueStr;
        }

        final Property property = getProperty(index);
        if (property == null) {
            return "";
        }
        // для bool свойств не выводится значение
        // кроме случая когда value == null
        if (canUseStandardCheckBox(property)) {
            if (property.getValueObject() == null) {
                return getTextToDisplay(property);
            }
            return null;
        }
        try {
            return getTextToDisplay(property);
        } catch (ActivatingPropertyError error) {
            getEnvironment().getTracer().put(error);
            return errorValueStr;
        }
    }

    @Override
    public boolean setData(final QModelIndex index, final Object data, final int role) {
        if (role == Qt.ItemDataRole.CheckStateRole) {
            if (isSelectionEnabled() && index.column()==0){
                final SelectorNodeInItemModel node = getNode(index);
                if (node!=null && !node.wasErrorOnCheckChoosable()){
                    final EntityModel entity = getEntity(node);
                    if (entity != null && entity instanceof BrokenEntityModel==false) {
                        final GroupModel groupModel = getOwnerGroupModel(entity);
                        if (groupModel.getEntitySelectionController().isEntityChoosable(entity)){
                            selection.invertSelection(node);
                            return true;
                        }else{
                            return false;
                        }     
                    }
                }
            }
            if (!isBrokenPropertyValue(index)){
                final Property property = getPropertyForChange(index);
                if (property != null) {
                    Boolean propertyValue = (Boolean) property.getValueObject();
                    try {
                        if (propertyValue == null) {
                            property.setValueObject(true);
                        } else if (propertyValue == false) {
                            if (property.isMandatory()) {
                                property.setValueObject(true);
                            } else {
                                property.setValueObject(null);
                            }
                        } else {
                            property.setValueObject(false);
                        }
                    } catch (Exception ex) {
                        getEnvironment().processException(new SettingPropertyValueError(property, ex));
                    }
                }
            }
        }
        return super.setData(index, data, role);
    }

    //Дополнительные проверки в WrapGroupModel
    protected Property getPropertyForChange(final QModelIndex index) {
        final Property property = getProperty(index);
        return property == null || !canEditPropertyValue(property) ? null : property;
    }
    
    protected String getTextToDisplay(final Property property) {
        return getTextToDisplay(property, STRING_MAX_LENGTH);
    }

    // метод для корректировки отображаемой строки в представлении
    protected final String getTextToDisplay(final Property property, final int maxLength) {
        if (property.isUnacceptableInputRegistered()){
            return property.getUnacceptableInput().getText();
        }else{
            String valueAsStr = property.getValueAsString();
            if (maxLength>0 && valueAsStr != null && valueAsStr.length() > maxLength){//DBP-1658
                valueAsStr = valueAsStr.substring(0, maxLength - 1) + "...";
            }

            final boolean cantEdit = !canEditPropertyValue(property);

            if (cantEdit) {
                valueAsStr = property.getOwner().getDisplayString(property.getId(), property.getValueObject(), valueAsStr, !property.hasOwnValue());
                if (maxLength>0 && valueAsStr != null && valueAsStr.length() > maxLength){//DBP-1658
                    valueAsStr = valueAsStr.substring(0, maxLength - 1) + "...";
                }
            }

            if (valueAsStr != null) {
                return valueAsStr.replace('\n', ' ');
            }

            return valueAsStr;
        }
    } 
    
    private static boolean canEditPropertyValue(final Property property) {
        return !property.isReadonly()
                && (property.hasOwnValue() || !property.isValueDefined())
                && !property.isCustomEditOnly()
                && property.isEnabled()
                && property.getEditPossibility() != EEditPossibility.PROGRAMMATICALLY;
    }    

    private static boolean canUseStandardCheckBox(final Property property) {
        final RadPropertyDef def = property.getDefinition();
        return def.getType() == EValType.BOOL 
               && !def.isInheritable()
               && property.getEnabledCommands().isEmpty();
    }        
    
    public final void setTextOptionsCacheEnabled(final boolean enabled){
        textOptionsCacheEnabled = enabled;
        if (!enabled){
            textOptionsCache.clear();
        }
    }
    
    private static String indexCacheKey(final QModelIndex index){
        return index.row()+"_"+index.column()+"_"+index.internalId();
    }
    
    public final ExplorerTextOptions getTextOptions(final QModelIndex index){
        ExplorerTextOptions textOptions;
        if (textOptionsCacheEnabled){
            textOptions = textOptionsCache.get(indexCacheKey(index));
        }else{
            textOptions = null;
        }
        if (textOptions==null){
            if (isBrokenEntity(index) || isBrokenPropertyValue(index)){
                textOptions = (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(BROKEN_ENTITY_MARKERS, null);
            }else{
                final Property property = getProperty(index);
                if (property==null){
                    final EntityModel entity = getEntity(index);
                    final ESelectorRowStyle rowStyle = entity==null ? ESelectorRowStyle.NORMAL : entity.getSelectorRowStyle();                    
                    return (ExplorerTextOptions)textOptionsProvider.getOptions(EnumSet.of(ETextOptionsMarker.SELECTOR_ROW), rowStyle);
                }else{
                    textOptions = getTextOptions(property);
                }
            }
            if (textOptionsCacheEnabled){
                textOptionsCache.put(indexCacheKey(index), textOptions);
            }
        }
        return textOptions;
    }
    
    protected final ExplorerTextOptions getTextOptions(final Property property){
        final EnumSet<ETextOptionsMarker> propertyMarkers =  property.getTextOptionsMarkers();
        if (propertyMarkers.contains(ETextOptionsMarker.INVALID_VALUE) && !canEditPropertyValue(property)){
            propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
        }
        return (ExplorerTextOptions)property.getValueTextOptions().getOptions(propertyMarkers);
    }
    
    protected final QFont getFont(final QModelIndex index) {
        return getTextOptions(index).getQFont();
    }

    protected final QColor getForeground(QModelIndex index) {
        return getTextOptions(index).getForeground();
    }

    protected final String getToolTip(final QModelIndex index){
        if (isBrokenPropertyValue(index)){
            return null;
        }else{
            final Property property = getProperty(index);
            return property==null ? null : getToolTip(property);
        }
    }
    
    protected String getToolTip(final Property property){
        if (!property.isReadonly()){
            if (property.isUnacceptableInputRegistered()){
                final String messageTemplate = 
                    property.getUnacceptableInput().getMessageText(InvalidValueReason.EMessageType.ThisPropertyValue);
                return String.format(messageTemplate, property.getTitle());
            }else{
                ValidationResult state = property.getEditMask().validate(getEnvironment(),property.getValueObject());
                if (state!=ValidationResult.ACCEPTABLE){
                    return state.getInvalidValueReason().getMessage(getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value);
                }
                state = property.getOwner().getPropertyValueState(property.getId());
                if (state!=ValidationResult.ACCEPTABLE){
                    return state.getInvalidValueReason().getMessage(getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value);
                }                
            }        
        }
        return property.getHint();
    }
    
    private List<Property> subscribedProperties;

    public final void subscribeProperties(final EntityModel entity, final IModelWidget subscriber) {
        unsubscribeProperties(subscriber);
        final List<Property> properties;
        if (entity instanceof BrokenEntityModel){
            properties = Collections.<Property>emptyList();
        }
        else{
            properties = getPropertiesToSubscribe(entity);
        }
        subscribedProperties = new ArrayList<>(columns.size());
        for (Property property : properties) {
            property.subscribe(subscriber);
            subscribedProperties.add(property);
        }
    }

    protected List<Property> getPropertiesToSubscribe(final EntityModel entity) {
        final List<Property> properties = new ArrayList<>();
        final GroupModel groupModel = getOwnerGroupModel(entity);
        Id propertyId;
        Property property;
        SelectorColumnModelItem column;
        for (int i = 0; i < columns.size(); ++i) {
            column = columns.get(i);
            try {
                propertyId = mapSelectorColumn(column, groupModel);
                if (propertyId != null) {
                    property = entity.getProperty(propertyId);
                    properties.add(property);
                }
            } catch (Throwable ex) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't get property corresponding to column #%s with title \'%s\'");
                getEnvironment().getTracer().error(String.format(title, column.getId(), column.getTitle()), ex);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(title, column.getId(), column.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
            }
        }
        return properties;
    }

    public final void unsubscribeProperties(IModelWidget subscriber) {
        if (subscribedProperties != null) {
            for (Property property : subscribedProperties) {
                property.unsubscribe(subscriber);
            }
            subscribedProperties = null;
        }
    }

    public final boolean isSubscribedToProperty(final Property property) {
        if (subscribedProperties != null && property != null) {
            for (int i = 0; i < subscribedProperties.size(); i++) {
                if (subscribedProperties.get(i).getId().equals(property.getId())) {
                    return true;
                }
            }
        }
        return false;
    }        
    
    private int currentColumn;

    public final void setCurrentColumnIndex(final int col) {
        currentColumn = col;
    }
    
    private int firstVisibleColumn=-1;
    
    public final void setFirstVisibleColumnIndex(final int col){
        firstVisibleColumn = col;
    }
    
    public final int getFirstVisibleColumnIndex(){
        return firstVisibleColumn;
    }

    private static QFont getBoldFont(final QFont font) {
        if (!font.bold()) {
            final ExplorerFont explorerFont = ExplorerFont.Factory.getFont(font);
            return explorerFont.changeWeight(EFontWeight.BOLD).getQFont();
        }
        return font;
    }
    
    @Override
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    public final Object headerData(final int section, final Qt.Orientation orientation, final int role) {
        switch (role) {
            case Qt.ItemDataRole.DisplayRole:
                if (orientation == Qt.Orientation.Horizontal) {
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    return column==null ? "" : getHeaderTitle(column);
                } else {
                    return "  ";
                }
            case Qt.ItemDataRole.UserRole:                
                if (orientation == Qt.Orientation.Horizontal) {
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    return column==null ? super.headerData(section, orientation, role) : column.getId();
                } else {
                    return super.headerData(section, orientation, role);
                }
            case COLUMN_ITEM_ROLE:
                return orientation == Qt.Orientation.Horizontal ? getSelectorColumn(section) : null;
            case Qt.ItemDataRole.FontRole: {
                if (orientation == Qt.Orientation.Horizontal) {
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    final QFont font;
                    if (column==null){
                        font = getDefaultHeaderFont();                        
                    }else{
                        font = getHeaderFont(column, section==currentColumn);
                    }
                    return font==null ? QApplication.font() : font;
                } else {
                    return super.headerData(section, orientation, role);
                }
            }
            case Qt.ItemDataRole.TextAlignmentRole: {
                if (orientation == Qt.Orientation.Horizontal) {
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    final Qt.AlignmentFlag alignment;
                    if (column==null){
                        alignment = getDefaultHeaderTextAlignment();
                    }else{
                        alignment = getHeaderTextAlignment(column);
                    }
                    return alignment==null ? Qt.AlignmentFlag.AlignLeft.value() : alignment;                    
                } else {
                    return super.headerData(section, orientation, role);
                }
            }
            case Qt.ItemDataRole.BackgroundRole: {
                if (orientation == Qt.Orientation.Horizontal) {
                    if (section == currentColumn && !root.isEmpty()) {
                        return QApplication.palette().alternateBase().color();
                    } else {
                        return QApplication.palette().button().color();
                    }
                }
                return super.headerData(section, orientation, role);
            }
            case Qt.ItemDataRole.ToolTipRole: {
                if (orientation == Qt.Orientation.Horizontal){
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    if (column==null && isSelectionEnabled() && section==0){                        
                        final MessageProvider mp = getEnvironment().getMessageProvider();
                        if (selection.isEmpty()){
                            if (getRootGroupModel().getRestrictions().getIsSelectAllRestricted()){
                                return super.headerData(section, orientation, role);
                            }else{
                                return mp.translate("Selector", "Select All Objects");
                            }                            
                        }else{
                            return mp.translate("Selector", "Clear Selection");
                        }
                    }
                    return column==null ? super.headerData(section, orientation, role) : getHeaderToolTip(column);
                }else{
                    return super.headerData(section, orientation, role);
                }
            }
            case SORT_INDICATOR_ITEM_ROLE: {
                final SelectorColumnModelItem curColumn = getSelectorColumn(section);
                if (curColumn==null){
                    return super.headerData(section, orientation, role);
                }
                final RadSortingDef sorting = root.getCurrentSorting();
                if(sorting != null) {
                    int priority = 0;
                    for(RadSortingDef.SortingItem i : sorting.getSortingColumns()) {
                        priority++;
                        if( i.propId.equals(curColumn.getId()) ) {
                            return SelectorSortUtils.getSortIndicator(priority, i.sortDesc ? RadSortingDef.SortingItem.SortOrder.DESC:RadSortingDef.SortingItem.SortOrder.ASC);
                        }
                    }
                }
                return super.headerData(section, orientation, role);
            }
            case Qt.ItemDataRole.DecorationRole: {
                if (orientation == Qt.Orientation.Horizontal) {
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    return column==null ? null : getHeaderIcon(column);
                }else{
                    return super.headerData(section, orientation, role);
                }
            }
            case WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE:{
                if (orientation == Qt.Orientation.Horizontal){
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    return column==null ? null : column.getId().toString();
                }else{
                    return null;
                }
            }
            default:
                return super.headerData(section, orientation, role);
        }
    }
    
    protected String getHeaderTitle(final SelectorColumnModelItem column){
        if (column.getHeaderMode()==ESelectorColumnHeaderMode.ONLY_ICON){
            return "";                        
        }else{
            return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), column.getTitle());
        }        
    }
    
    protected QFont getHeaderFont(final SelectorColumnModelItem column, final boolean isCurrentColumn){
        final QFont actualFont = getDefaultHeaderFont();
        if (isCurrentColumn && !root.isEmpty()) {
            return getBoldFont(actualFont);
        }
        return actualFont;        
    }
    
    protected QFont getDefaultHeaderFont(){
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);

        final QFont font = settings.readQFont(SettingNames.Selector.Common.HEADER_FONT_IN_SELECTOR);

        settings.endGroup();
        settings.endGroup();
        settings.endGroup();

        return font != null ? font : QApplication.font();        
    }
    
    protected Qt.AlignmentFlag getHeaderTextAlignment(final SelectorColumnModelItem column){
        return getDefaultHeaderTextAlignment();
    }
    
    protected Qt.AlignmentFlag getDefaultHeaderTextAlignment(){
        final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
        final Qt.AlignmentFlag alignmentFlag;

        settings.beginGroup(SettingNames.SYSTEM);
        settings.beginGroup(SettingNames.SELECTOR_GROUP);
        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
        alignmentFlag = settings.readAlignmentFlag(SettingNames.Selector.Common.TITLES_ALIGNMENT);
        settings.endGroup();
        settings.endGroup();
        settings.endGroup();

        return alignmentFlag == null ? Qt.AlignmentFlag.AlignLeft : alignmentFlag;
    }
    
    protected String getHeaderToolTip(final SelectorColumnModelItem column){
        final String toolTip = column.getHint();
        if (column.getHeaderMode()==ESelectorColumnHeaderMode.ONLY_ICON
            && (toolTip==null || toolTip.isEmpty())
           ){
            return column.getTitle();
        }
        return toolTip;
    }    
    
    protected QIcon getHeaderIcon(final SelectorColumnModelItem column){
        if (column.getHeaderMode()==ESelectorColumnHeaderMode.ONLY_TEXT){
            return null;
        }else{
            final Icon headerIcon = column.getHeaderIcon();
            if (headerIcon==null){
                return null;
            }else{
                return ImageManager.getQIcon(headerIcon);
            }
        }        
    }
    
    @Override
    public final int rowCount(final QModelIndex index) {
        final SelectorNodeInItemModel node = getNode(index);
        return node==null ? 0 : node.getNumberOfLoadedInnerNodes();
    }
    
    private boolean hasChildren(final SelectorNode parentNode){
        if (parentNode==null){
            return false;
        } else if (parentNode.isChildrenInited()){
            return parentNode.hasChildren();
        }else{
            final EntityModel parentEntity = getEntity(parentNode);
            if (parentEntity==null || (parentEntity instanceof BrokenEntityModel) || !tryLock()) {
                return false;
            }
            try {
                final Id propertyId = getHasChildrenPropertyId(parentEntity);
                final boolean hasChildren;                
                if (propertyId != null) {
                    final Property property = parentEntity.getProperty(propertyId);
                    if (!property.isActivated()){
                        final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Unable to get information about subobjects for \'%1$s\' object.\nProperty \'%2$s\' (#%3$s) was not activated.\nIt is necessary to add this property in selector columns set in #%4$s presentation");
                        final GroupModel parentGroupModel = getOwnerGroupModel(parentEntity);
                        final String formattedMessage = String.format(message, parentEntity.getTitle(), property.getDefinition().getName(), property.getId().toString(), parentGroupModel.getSelectorPresentationDef().getId().toString());
                        getEnvironment().getTracer().error(formattedMessage);
                        parentNode.setHasChildren(false);
                        return false;
                    }
                    if (property instanceof PropertyInt) {
                        final Object value = property.getValueObject();
                        hasChildren = value != null ? ((Long) value).longValue() > 0 : false;
                    } else if (property instanceof PropertyBool) {
                        final Object value = property.getValueObject();
                        hasChildren = value != null ? ((Boolean) value).booleanValue() : false;
                    } else {
                        hasChildren = false;
                    }
                } else {
                    hasChildren = hasChildren(parentEntity);
                }
                parentNode.setHasChildren(hasChildren);
                return hasChildren;
            } catch (Throwable ex) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't get information about children of object \'%s\'");
                getEnvironment().getTracer().error(String.format(title, parentEntity.getTitle()), ex);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(title, parentEntity.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
                parentNode.setHasChildren(false);
                return false;
            } finally {
                unlock();
            }                
        }
    }
    
    @Override
    public final boolean hasChildren(final QModelIndex index) {
        return hasChildren(getNode(index));
    }
    
    public final SelectorColumnModelItem getSelectorColumn(final int rawIndex){
        final int logicalIndex = isSelectionEnabled() ? rawIndex-1 : rawIndex;
        if (logicalIndex>=0 && logicalIndex<columns.size()){
            return columns.get(logicalIndex);
        }else{
            return null;
        }
    }

    @Override
    public final int columnCount(final QModelIndex index) {
        return isSelectionEnabled() ? columns.size() + 1 : columns.size();
    }

    public List<SelectorColumnModelItem> getSelectorColumns() {
        return Collections.unmodifiableList(columns);
    }

    protected final int getTextAlignment(final QModelIndex index) {
        final SelectorColumnModelItem column = getSelectorColumn(index.column());
        if (column==null){
            return WidgetUtils.getQtAlignmentValue(Qt.AlignmentFlag.AlignVCenter, Qt.AlignmentFlag.AlignLeft);
        }else{
            final EntityModel entityModel = getEntity(index);            
            final Qt.Alignment alignment = getTextAlignmentFlags(column, entityModel);
            if (alignment==null){
                Qt.AlignmentFlag alignmentFlag = getTextAlignment(column, entityModel);
                if (alignmentFlag==null){
                    return -1;
                }
                return WidgetUtils.getQtAlignmentValue(Qt.AlignmentFlag.AlignVCenter, alignmentFlag);
            }else{
                return alignment.value();
            }
        }
    }
    
    protected Qt.Alignment getTextAlignmentFlags(final SelectorColumnModelItem column, final EntityModel entityModel) {
        return null;
    }
    
    protected Qt.AlignmentFlag getTextAlignment(final SelectorColumnModelItem column, final EntityModel entityModel) {
        final EValType valType = column.getPropertyDef().getType();
        ESelectorColumnAlign alignment = column.getAlignment();
                
        if (alignment == null || alignment == ESelectorColumnAlign.DEFAULT) {
            final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
            
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
            settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
            alignment = ESelectorColumnAlign.getForValue((long)settings.readInteger(valType.getName()));
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        switch (alignment) {
            case CENTER:
                return Qt.AlignmentFlag.AlignCenter;
            case LEFT:
                return Qt.AlignmentFlag.AlignLeft;
            case RIGHT:
                return Qt.AlignmentFlag.AlignRight;
            default:
                return Qt.AlignmentFlag.AlignLeft;
        }        
    }

    protected final QColor getBackground(final QModelIndex index) {
        final boolean isSelected = isSelected(index);
        final ExplorerTextOptions options;
        if (isSelected && index.column()==0 && isSelectionEnabled()){
            options = 
                ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.SELECTOR_ROW,ETextOptionsMarker.CHOOSEN_OBJECT);
        }else{
            options = getTextOptions(index);
        }        
        if (!isSelected && index.row() % 2==0){
            final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.SELECTOR_GROUP);
            settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
            final int sliderValue = settings.readInteger(SettingNames.Selector.Common.SLIDER_VALUE);
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
            
            if (sliderValue>0){
                return ExplorerTextOptions.getDarker(options.getBackgroundColor(), sliderValue);
            }
        }
        return options.getBackground();
    }

    @Override
    public boolean removeRows(final int row, final int count, final QModelIndex parent) {
        final QModelIndex normalizedParent;
        if (parent==null || parent.column()==0){
            normalizedParent = parent;
        }else{
            normalizedParent = index(parent.row(),0,parent.parent());
        };
        final SelectorNodeInItemModel parentNode = getNode(normalizedParent);
        if (parentNode==null){
            return false;
        }
        final int rowCount = parentNode.getNumberOfLoadedInnerNodes();
        if (row >= 0 && (row + count - 1) < rowCount) {       
            beginRemoveRows(normalizedParent, row, row + count - 1);
            try{
                final GroupModel group = parentNode.getChildGroupModel();
                {
                    QModelIndex index;
                    SelectorNodeInItemModel childNode;
                    for (int i = 0; i < count; i++) {
                        index = index(row + i, 0, normalizedParent);
                        if (index!=null){
                            group.removeRow(row);
                            clear(index);
                            childNode = nodes.remove(index.internalId());
                            parentNode.removeChildNode(childNode);
                        }
                    }
                }

                //Нужно обновить индексы подъелементов, родительские
                //элементы которых следуют за удаленными элементами
                QModelIndex index, childIndex;
                SelectorNodeInItemModel node, childNode;
                for (int i = row; i < (rowCount - count); i++) {
                    //Цикл по элементам, которые следуют за удаленными элементами
                    index = index(i, 0, normalizedParent);
                    for (int j = rowCount(index) - 1; j >= 0; j--) {//Цикл по их дочерним элементам
                        //Обновление закэшированных родительских индексов
                        childIndex = index(j, 0, index);
                        childNode = nodes.get(childIndex.internalId());
                        if (childNode!=null){
                            childNode.setParentIndex(index);
                        }
                    }
                    //Обновление номера строки
                    node = nodes.get(index.internalId());
                    if (node!=null){
                        node.setRow(i);
                    }
                }
                parentNode.setNumberOfLoadedInnerNodes(rowCount - count);
            }finally{
                endRemoveRows();
            }
            return true;
        }
        return false;
    }

    public void reread(final QModelIndex parent) throws InterruptedException, ServiceClientException {
        reset(parent);
        readMore(parent);
    }
    
    public void reset(final QModelIndex parent){
        final SelectorNodeInItemModel parentNode = getNode(parent);
        final GroupModel group = parentNode==null ? null : parentNode.getChildGroupModel();
        if (group != null) {
            lock();
            try {
                if (parent == null) {
                    selection.store(null);
                    reset();
                    group.reset();
                    clear((QModelIndex)null);
                } else if (rowCount(parent) > 0) {
                    selection.store(parentNode);
                    final QModelIndex normalizedParent;
                    if (parent.column()==0){
                        normalizedParent = parent;
                    }else{
                        normalizedParent = index(parent.row(),0,parent.parent());
                    }
                    beginRemoveRows(normalizedParent , 0, rowCount(parent) - 1);
                    group.reset();
                    clear(parent);
                    endRemoveRows();
                } else {
                    group.reset();
                    parentNode.invalidate();
                }
            } finally {
                unlock();
            }
        }
    }

    public int findEntity(final QModelIndex parent, final Pid pid) throws InterruptedException, ServiceClientException{
        if (isLocked()) {
            return -1;
        }
        final SelectorNodeInItemModel parentNode = getNode(parent);
        pushContextNode(parentNode);
        try{
            final GroupModel group = getChildGroup(parent);
            if (group==null || !tryLock()) {
                return -1;
            }
            try {
                final int idx;
                if (parentNode.wasErrorOnDataLoading()) {
                    idx = group.findEntityByPid(pid);
                } else {
                    idx = group.readToEntity(pid, getEnvironment().getMessageProvider().translate("Wait Dialog", "Restoring Position"));
                }
                updateRowsCount(parent, parentNode, idx+ROWS_LOAD_LIMIT);
                return idx;
            }catch(ServiceCallFault fault){
                throw preprocessServiceCallFault(parentNode, fault);
            }
            finally {
                unlock();
            }
        }finally{
            popContextNode();
        }
    }
    
    private void clear(final SelectorNodeInItemModel parentNode){
        selection.clear(parentNode==rootNode ? null : parentNode, false, false, false);        
        if (parentNode==rootNode){
            nodes.clear();
            rootNode.invalidate();
        }else if (parentNode!=null){
            final Collection<SelectorNodeInItemModel> nodesForRemove = parentNode.getChildren(true);
            
            GroupModel childGroup;
            for (SelectorNodeInItemModel node: nodesForRemove) {
                nodes.remove(node.internalIndexId);
                childGroup = node.getChildGroupModel();
                if (childGroup!=null){
                    changeChildGroupContext(childGroup, null);                    
                    if (childGroup.getView()!=null
                        && (childGroup.getView()==root.getView() || root.getView()==null)){                    
                        childGroup.setView(null);
                    }
                    selection.removeSubscription(childGroup);
                }               
            }
            parentNode.invalidate();
        }        
        rowsLimit = getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_CONFIG_PATH, 1000);
    }

    protected void clear(final QModelIndex parent) {
        clear(getNode(parent));
    }

    public void clear() {
        lock();
        clear(rootNode);
        reset();
    }

    public boolean canReadMore(final QModelIndex parent) {
        final SelectorNodeInItemModel parentNode = getNode(parent);
        if (parentNode==null){
            return false;
        }
        pushContextNode(parentNode);
        try {
            if (parentNode!=rootNode){
                getChildGroup(parent);//init childGroup;
            }            
        } catch (Throwable ex) {
            final EntityModel parentEntity = getEntity(parent);
            final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \'%s\'");
            getEnvironment().getTracer().error(String.format(title, parentEntity.getTitle()), ex);
            getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                    String.format(title, parentEntity.getTitle(),
                    ClientException.exceptionStackToString(ex)),
                    EEventSource.EXPLORER);
            return false;
        }finally{
            popContextNode();
        }
                
        return !isLocked() && parentNode.canReadMore();
    }    

    public final boolean isLocked() {
        return locked;
    }

    protected final void lock() {
        locked = true;
    }

    protected final void unlock() {
        locked = false;
    }

    protected final boolean tryLock() {
        if (!locked) {
            locked = true;
            return true;
        }
        return false;
    }

    public boolean readMore(final QModelIndex parent) throws ServiceClientException, InterruptedException {
        final SelectorNodeInItemModel parentNode = getNode(parent);
        if (parentNode==null){
            return false;
        }
        final GroupModel group;
        try {
            group = parentNode == rootNode ? root : getChildGroup(parent);
            if (group == null && parent != null && !isLocked()) {                
                parentNode.setNumberOfLoadedInnerNodes(0);
            }
        } catch (Exception ex) {
            final EntityModel parentEntity = getEntity(parent);
            final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \'%s\'");
            getEnvironment().getTracer().error(String.format(title, parentEntity.getTitle()), ex);
            getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                    String.format(title, parentEntity.getTitle(),
                    ClientException.exceptionStackToString(ex)),
                    EEventSource.EXPLORER);
            getEnvironment().messageException(getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data"), ClientException.getExceptionReason(getEnvironment().getMessageProvider(), ex), ex);
            return false;
        }

        if (parentNode.canReadMore() && tryLock()){
            pushContextNode(parentNode);
            boolean wasException = true;
            try {                
                if (readMoreInternal(parentNode)) {
                    updateRowsCount(parent, parentNode, ROWS_LOAD_LIMIT);
                }else{
                    if (parent != null && parentNode.getChildGroupModel().isEmpty()){
                        parentNode.setNumberOfLoadedInnerNodes(0);
                    }
                    return false;
                }
                wasException = false;
                return true;
            } catch (ServiceCallFault fault) {
                throw preprocessServiceCallFault(parentNode, fault);
            } catch (InterruptedException exception){
                wasException = false;//Если прерывание было инициировано пользователем, то можно читать дальше.
                throw exception;
            }
            finally {
                popContextNode();
                if (wasException){
                    parentNode.registerErrorOnDataLoading();
                }
                unlock();
            }
        }else if (group!=null && group.getEntitiesCount()==0 && !group.hasMoreRows()){
            parentNode.setNumberOfLoadedInnerNodes(0);
        }
        return false;
    }

    protected void showErrorOnReceivingData(final ServiceClientException exception) {
        final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
        getEnvironment().getTracer().error(title, exception);
        root.showException(title, exception);
    }
    
    private ServiceCallFault preprocessServiceCallFault(final SelectorNodeInItemModel parentNode, final ServiceCallFault fault){
        ObjectNotFoundError objectNotFound = null;
        for (Throwable cause = fault; cause != null; cause = cause.getCause()) {
            if (cause instanceof ObjectNotFoundError) {
                objectNotFound = (ObjectNotFoundError) cause;
            }            
        }
                
        if (objectNotFound != null) {
            EntityModel parentEntity;
            for (SelectorNodeInItemModel node = parentNode; node != null; node = node.parentNode) {
                parentEntity = node.getEntityModel();
                if (parentEntity != null && !(parentEntity instanceof BrokenEntityModel) && objectNotFound.inContextOf(parentEntity)) {
                    objectNotFound.setContextEntity(parentEntity);
                    return objectNotFound;
                }
                parentEntity = getEntity(node);
                if (parentEntity != null && !(parentEntity instanceof BrokenEntityModel) && objectNotFound.inContextOf(parentEntity)) {
                    objectNotFound.setContextEntity(parentEntity);
                    return objectNotFound;
                }                
            }
        }
        return fault;
    }

    private boolean readMoreInternal(final SelectorNodeInItemModel parent) throws ServiceClientException, InterruptedException {
        int rowCount = parent.getNumberOfLoadedInnerNodes();
        final GroupModel group = parent.getChildGroupModel();
        for(;;){
            try{
                if ((rowCount >= group.getEntitiesCount()) && (group.getEntity(rowCount) == null)) {
                    return false;
                }
                else{                    
                    break;
                }
            }
            catch(BrokenEntityObjectException exception){
                rowCount++;
            }
        }        
        return true;
    }
    
    private void updateRowsCount(final QModelIndex parentIndex, 
                                                   final SelectorNodeInItemModel parentNode,
                                                   final int rowLimit){
        final GroupModel group = parentNode==null ? null : parentNode.getChildGroupModel();        
        final QModelIndex normalizedParentIndex;
        if (parentIndex==null || parentIndex.column()==0){
            normalizedParentIndex = parentIndex;
        }else{
            normalizedParentIndex = index(parentIndex.row(), 0, parentIndex.parent());
        }
        if (group!=null){
            final int oldCount = parentNode.getNumberOfLoadedInnerNodes();            
            final int newCount;
            if (rowLimit>0){
                newCount = Math.min(group.getEntitiesCount(), oldCount+rowLimit);
            }else{
                newCount = group.getEntitiesCount();
            }
            
            if (oldCount<newCount){
                parentNode.setNumberOfLoadedInnerNodes(newCount);//temporary change row count to make index method work with new rows
                final int row;
                try{
                    row = verifyRows(normalizedParentIndex, oldCount, newCount - 1);
                }finally{
                    parentNode.setNumberOfLoadedInnerNodes(oldCount);
                }
                if (row>=oldCount){
                    setTextOptionsCacheEnabled(true);                
                    beginInsertRows(normalizedParentIndex, oldCount, row);
                    try{
                        parentNode.setNumberOfLoadedInnerNodes(row + 1);
                        if (normalizedParentIndex==null && selection.isSomeSelectionStored()){
                            selection.restoreSelection(null);
                        }
                    }finally{
                        endInsertRows();
                        setTextOptionsCacheEnabled(false);
                    }
                }
            }
        }
    }

    void updateRowsCount(final QModelIndex parent) {
        final SelectorNodeInItemModel node = getNode(parent);
        if (node!=null){
            updateRowsCount(parent, node, -1);
        }
    }

    /**
     *Get values of selector columns and call isEntityChoosable to ensure that all necessary data was received
     *before data method  invoked
     * returns last verified row
     */
    private int verifyRows(final QModelIndex parentIndex, final int startWith, final int finishAt) {
        Id propertyId;
        SelectorColumnModelItem column;
        SelectorNodeInItemModel node;
        EntityModel entity;
        int row;
        boolean wasRead;
        for (row = startWith; row <= finishAt; row++) {
            try {
                node = getNode(index(row, 0, parentIndex));
                entity = getEntity(node);//need to call overridable method replaceEntityModel
                if (entity==null || entity instanceof BrokenEntityModel){
                    continue;
                }                
                final GroupModel groupModel = getOwnerGroupModel(entity);
                for (int i = 0; i < columns.size(); ++i) {
                    column = columns.get(i);
                    try {
                        propertyId = mapSelectorColumn(column, groupModel);
                        if (propertyId != null) {
                            final Property property = entity.getProperty(propertyId);
                            property.getValueObject();
                            property.getValueAsString();
                        }
                    } catch (Throwable ex) {
                        final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't get property value corresponding to column #%s with title \'%s\'");
                        getEnvironment().getTracer().error(String.format(title, column.getId(), column.getTitle()), ex);
                        getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(title, column.getId(), column.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
                        node.registerErrorOnGetPropertyValue(column.getId());
                    }
                }
                wasRead = entity.wasRead();
                try{
                    groupModel.getEntitySelectionController().isEntityChoosable(entity);
                }catch (Throwable ex) {
                    node.registerErrorOnCheckChoosable();
                    getEnvironment().getTracer().error(ex);
                    break;
                }
                if (!wasRead && entity.wasRead()){
                    final String info = 
                        getEnvironment().getMessageProvider().translate("ExplorerMessage", "Reading object while presenting in selector");
                    getEnvironment().getTracer().put(EEventSeverity.WARNING, info, EEventSource.EXPLORER);
                }                
            } catch (Throwable ex) {
                final SelectorNodeInItemModel parentNode = getNode(parentIndex);
                if (parentNode!=null){
                    parentNode.registerErrorOnDataLoading();
                }
                getEnvironment().getTracer().error(ex);
                break;
            }
        }
        return row - 1;
    }
    
    private static GroupModel getGroupModel(final EntityModel entity){
        return entity==null ? null : ((IContext.SelectorRow) entity.getContext()).parentGroupModel;
    }
    
    public final GroupModel getCachedChildGroup(final QModelIndex parent){
        final SelectorNodeInItemModel node = getNode(parent);
        return node==null ? null : node.getChildGroupModel();
    }
    
    public final boolean errorWasDetected(final QModelIndex index){
        final SelectorNodeInItemModel node = getNode(index);
        return node==null ? false : node.wasErrorOnDataLoading();
    }

    public final GroupModel getChildGroup(final QModelIndex parent) {
        return getChildGroup(getNode(parent));
    }
    
    private static GroupModel getFirstChildGroup(final SelectorNode node){
        if (node instanceof SelectorNodeInItemModel){
            return ((SelectorNodeInItemModel)node).getChildGroupModel();
        }
        final List<GroupModel> childGroups = node==null ? null : node.getChildGroupModels();
        return childGroups==null || childGroups.isEmpty() ? null : childGroups.get(0);
    }
    
    protected final GroupModel getChildGroup(final SelectorNode parentNode) {
        if (parentNode==null){
            return null;
        }else{
            GroupModel group = getFirstChildGroup(parentNode);
            if (group==null) {
                final EntityModel parentEntity = getEntity(parentNode);
                if (parentEntity==null || parentEntity instanceof BrokenEntityModel){
                    return null;
                }
                else if ((hasChildren(parentNode) || canCreateChild(parentEntity))
                        && tryLock()) {
                    try {
                        group = createChildGroupModel(parentEntity);
                        if (group == null) {
                            getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format("Child group model was not created for parent entity \'%s\'", parentEntity.getTitle()), EEventSource.EXPLORER);
                        } else {
                            selection.subscribe(parentNode, group);
                            changeChildGroupContext(group, root.getGroupContext());                        
                            group.getRestrictions().add(calcChildGroupRestrictions(parentEntity, group));
                            final GroupModel parentGroup = getFirstChildGroup(parentNode.getParentNode());
                            prepareChildGroupFilter(parentGroup, group);
                            prepareGroupCondition(parentEntity, group);
                            group.setView(root.getView());
                            parentNode.addChildGroupModel(group);
                            if (selection.isSomeSelectionStored()){
                                selection.restoreSelection(parentNode);
                            }
                        }
                    } finally {
                        unlock();
                    }
                }
            }
            return group;            
        }
    }
    
    private static void changeChildGroupContext(final GroupModel childGroup, final IContext.Group rootGroupContext){
        if (childGroup instanceof ProxyGroupModel==false){            
            final Restrictions currentRestrictions = 
                Restrictions.Factory.sum(childGroup.getRestrictions(), Restrictions.NO_RESTRICTIONS);
            final IContext.Group context = childGroup.getGroupContext().copy();
            context.setRootGroupContext(rootGroupContext);
            childGroup.setContext(context);         
            childGroup.getRestrictions().add(currentRestrictions);
        }
    }
    
    protected void prepareChildGroupFilter(final GroupModel parentGroup, final GroupModel childGroup){
        if (parentGroup.getCurrentFilter()!=null 
            && parentGroup.getDefinition().getOwnerClassId().equals(childGroup.getDefinition().getOwnerClassId())){
            final FilterModel currentFilter = parentGroup.getCurrentFilter();
            FilterModel childGroupFilter = childGroup.getFilters().findById(currentFilter.getId());
            if (childGroupFilter==null){
                if (currentFilter.isUserDefined()){
                    childGroupFilter = new FilterModel(childGroup.getEnvironment(),currentFilter.getFilterDef());
                    childGroupFilter.setContext(new IContext.Filter(childGroup));
                }else if (currentFilter.isCommon()){
                    final RadCommonFilter commonFilter = (RadCommonFilter)currentFilter.getFilterDef();
                    final RadUserFilter userFilterDef = 
                        commonFilter.convertToUserFilter(childGroup.getEnvironment(), childGroup.getSelectorPresentationDef());
                    childGroupFilter = new FilterModel(childGroup.getEnvironment(), userFilterDef);
                    childGroupFilter.setContext(new IContext.Filter(childGroup));                    
                }else{
                    final String traceMessage = 
                        getEnvironment().getMessageProvider().translate("ExplorerException", "Filter #%1$s was not found in child group model");
                    getEnvironment().getTracer().error(String.format(traceMessage, currentFilter.getDefinition().getId().toString()));
                    return;
                }
            }
            for (Property property: currentFilter.getActiveProperties()){
                childGroupFilter.getProperty(property.getId()).setValueObject(property.getValueObject());
            }
            try{
                childGroup.setFilter(childGroupFilter);
            }catch(PropertyIsMandatoryException | InvalidPropertyValueException | ServiceClientException exception){
                final String traceMessage = 
                    getEnvironment().getMessageProvider().translate("ExplorerException", "Failed to apply filter in child group model");
                getEnvironment().getTracer().error(traceMessage, exception);
            }catch(InterruptedException exception){//NOPMD
                //cancelled by user
            }
        }
    }    

    @Override
    public final QModelIndex parent(final QModelIndex child) {
        final SelectorNodeInItemModel node = getNode(child);
        return node==null ? null : node.getParentIndex();
    }

    @Override
    public final QModelIndex index(final int row, final int column, final QModelIndex parent) {
        if (row < 0 || row >= rowCount(parent)) {
            //throw new ArrayIndexOutOfBoundsException(row);
            return null;//for internal call on remove last row.
        }
        if (column < 0 || column >= (isSelectionEnabled() ? columns.size()+1 : columns.size())){ //throw new ArrayIndexOutOfBoundsException(column);
            return null;
        }
        final SelectorNodeInItemModel parentNode = getNode(parent);
        if (parentNode==null){
            return null;//invalid parent index
        }
        final int nodeHashCode = SelectorNodeInItemModel.calcChildNodeHashCode(parentNode, row);
        if (nodeHashCode == 0) {
            return null;//invalid parent index
        }
        final QModelIndex result = createIndex(row, column, parentNode.hashCode()*67+nodeHashCode);
        final long nodeInternalId = result.internalId();
        SelectorNodeInItemModel node = nodes.get(nodeInternalId);
        if (node==null){
            node = new SelectorNodeInItemModel(nodeInternalId, row, parent, parentNode);
            nodes.put(nodeInternalId, node);
        }
        return result;
    }
    
    public final SelectorNode getSelectorNode(final QModelIndex index){
        final SelectorNode node = getNode(index);
        return node==rootNode ? null : node;
    }
    
    final void pushContextNode(final SelectorNodeInItemModel node){
        context.push(node);
        contextNode = node;
    }
    
    final SelectorNodeInItemModel popContextNode(){
        context.pop();
        contextNode = context.isEmpty() ? null : context.peek();
        return contextNode;
    }
    
    private SelectorNodeInItemModel getNode(final QModelIndex index){
        if (index==null){
            return rootNode;
        }else{            
            if (contextNode!=null && contextNode.internalIndexId == index.internalId()){
                return contextNode;
            }else{
                return nodes.get(index.internalId());
            }
        }
    }
    
    private QModelIndex getIndex(final SelectorNodeInItemModel node){
        if (node==null || node==rootNode){
            return null;
        }else{
            return index(node.getRow(), 0, node.getParentIndex());
        }
    }   

    public EntityModel getEntity(final QModelIndex index) {
        return getEntity(getNode(index));
    }
    
    protected final EntityModel getEntity(final SelectorNode node){
        final EntityModel result = node==null ? null : node.getEntityModel();
        if (result==null){
            return null;
        }else{
            final GroupModel groupModel = getFirstChildGroup(node.getParentNode());
            return replaceEntityModel(groupModel, result, node.getNestingLevel());
        }
    }
    
    protected EntityModel replaceEntityModel(final GroupModel ownerGroupModel, 
                                                                    final EntityModel originalEntityModel,
                                                                    final int nestingLevel){
        return originalEntityModel;
    }
    
    private String getRowName(final QModelIndex index){
        final EntityModel entity = getEntity(index);
        final Pid pid = entity==null ? null : entity.getPid();
        return pid==null ? null : pid.toStr();
    }
    
    private String getCellName(final QModelIndex index){
        if (isSelectionEnabled() && index.column()==0){
            return "rx_selection_cell";
        }else{
            final Property property = getProperty(index);
            return property==null ? null : property.getId().toString();
        }
    }
    
    private String getCellValueAsStr(final QModelIndex index){
        final Property property = getProperty(index);
        if (property==null){
            return null;
        }else{
            final Object rawValue = property.getValueObject();
            final ValAsStr valAsStr = ValueConverter.obj2ValAsStr(rawValue, property.getType());
            return valAsStr==null ? null : valAsStr.toString();
        }
    }
    
    private boolean isCellValueNull(final QModelIndex index){
        final Property property = getProperty(index);
        return property==null || property.getValueObject()==null;
    }
    
    public final boolean isBrokenEntity(QModelIndex index){
        return getEntity(index) instanceof BrokenEntityModel;
    }
    
    public final boolean isBrokenPropertyValue(final QModelIndex index){
        final SelectorNodeInItemModel node = getNode(index);
        final EntityModel entity = getEntity(node);        
        if (entity == null || (entity instanceof BrokenEntityModel)) {
            return false;
        }
        final SelectorColumnModelItem column = getSelectorColumn(index.column());
        return column==null ? false : node.wasErrorOnGetPropVal(column.getId());
    }    
    
    protected final EntityModel createBrokenEntityModel(final GroupModel ownerGroupModel, final BrokenEntityObjectException exception){
        return new BrokenEntityModel(getEnvironment(), ownerGroupModel.getSelectorPresentationDef(), exception);
    }

    public final Property getProperty(final QModelIndex index) {
        final EntityModel entity = getEntity(index);        
        if (entity == null || (entity instanceof BrokenEntityModel)) {
            return null;
        }
        final SelectorColumnModelItem column = getSelectorColumn(index.column());
        if (column==null){
            return null;
        }
        final Id propertyId;
        try {
            propertyId = mapSelectorColumn(column, getOwnerGroupModel(entity));
            if (propertyId == null) {
                return null;
            }
            return entity.getProperty(propertyId);
        } catch (Throwable ex) {
            final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't get property corresponding to column #%s with title \'%s\'");
            getEnvironment().getTracer().error(String.format(title, column.getId(), column.getTitle()), ex);
            getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(title, column.getId(), column.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
            return null;
        }
    }    

    private void prepareGroupCondition(final EntityModel parentEntity, final GroupModel childGroup) {
        if (parentEntity instanceof BrokenEntityModel){
            return;
        }
        final Map<Id, Object> propertyValues = new LinkedHashMap<>();
        fillConditionalProperties(parentEntity, propertyValues);
        if (!propertyValues.isEmpty()) {
            final org.radixware.schemas.xscml.Sqml condition = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
            final RadClassPresentationDef classDef = childGroup.getSelectorPresentationDef().getClassPresentation();
            RadPropertyDef propertyDef;
            org.radixware.schemas.xscml.Sqml.Item.PropSqlName propertyTag;
            org.radixware.schemas.xscml.Sqml.Item.TypifiedValue valueTag;
            for (Map.Entry<Id, Object> entry : propertyValues.entrySet()) {
                propertyDef = classDef.getPropertyDefById(entry.getKey());

                if (!condition.getItemList().isEmpty()) {
                    condition.addNewItem().setSql(" AND ");
                }

                propertyTag = condition.addNewItem().addNewPropSqlName();
                propertyTag.setOwner(org.radixware.schemas.xscml.Sqml.Item.PropSqlName.Owner.THIS);
                propertyTag.setPropId(propertyDef.getId());
                propertyTag.setTableId(classDef.getTableId());

                condition.addNewItem().setSql(" = ");

                valueTag = condition.addNewItem().addNewTypifiedValue();
                valueTag.setPropId(propertyDef.getId());
                valueTag.setTableId(classDef.getTableId());
                valueTag.setValue(ValAsStr.toStr(entry.getValue(), propertyDef.getType()));
            }

            try {
                childGroup.setCondition(SqmlExpression.mergeConditions(childGroup.getCondition(), condition));
            } catch (InterruptedException ex) {
            } catch (ServiceClientException ex) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on set condition in child group of parent object \'%s\'");
                getEnvironment().getTracer().error(String.format(title, parentEntity.getTitle()), ex);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, parentEntity.getTitle(),
                        ClientException.exceptionStackToString(ex)),
                        EEventSource.EXPLORER);
            }
        }

    }

    //
    //                     Methods for overriding
    //
    protected Id mapSelectorColumn(final SelectorColumnModelItem sourceColumn, final GroupModel mapTo) {
        return sourceColumn.getId();
    }

    /**
     * Если у {@code parentEntity} нет дочерних элеметов (определяется вызовами  {@link #getHasChildrenPropertyId(org.radixware.kernel.explorer.models.EntityModel) } и
     * {@link #hasChildren(org.radixware.kernel.explorer.models.EntityModel) } ), то вызывается этот метод, чтобы определить
     * можно ли создать дочерний элемент. Если дочерний элемент создать нельзя, то дочерняя группа не создается
     * (метод {@link #createChildGroupModel(org.radixware.kernel.explorer.models.EntityModel) } вызван не будет).
     * @param parentEntity
     * @return
     */
    public boolean canCreateChild(final EntityModel parentEntity) {
        if (parentEntity instanceof BrokenEntityModel){
            return false;
        }
        else if (parentEntity != null) {
            return !getOwnerGroupModel(parentEntity).getRestrictions().getIsCreateRestricted();
        } else {
            return !root.getRestrictions().getIsCreateRestricted();
        }
    }
    
    public final boolean canCreateChild(final QModelIndex index){
        final EntityModel entityModel = getEntity(index);
        return entityModel==null ? null : canCreateChild(entityModel);
    }

    /*Создать дочернюю группу для
     */
    protected GroupModel createChildGroupModel(final EntityModel parentEntity) {
        return null;
    }

    //Метод к-рый позволяет сформировать доп условие к значением колонок
    //в дочерней группе
    public void fillConditionalProperties(final EntityModel parent, final Map<Id, Object> propertyValues) {
    }

    //Идентификатор свойства по которому можно узнать есть ли дочерние элементы
    protected Id getHasChildrenPropertyId(EntityModel parent) {
        return null;
    }

    //Если метод getHasChildrenPropertyId вернул null/empty
    //Вызывается этот метод
    protected boolean hasChildren(EntityModel parent) {
        return false;
    }

    /**
     * Возвращает дополнительные ограничения дочерней группы.
     * Вызывается после создания дочерней группы
     * По умолчанию возвращает контекстные ограничения корневой группы +
     * ограничение корневой группы  CDbuRestriction.EDITOR (если установлено)
     */
    public Restrictions calcChildGroupRestrictions(final EntityModel parent, final GroupModel child) {
        if (getRootGroupModel().getRestrictions().getIsEditorRestricted()) {
            //DBP-1681
            return Restrictions.EDITOR_RESTRICTION;
        } else {
            return Restrictions.NO_RESTRICTIONS;
        }
    }
    
    public void updateAll() {
        lock();
        try {
            selection.store(null);
            clear((QModelIndex)null);
            reset();
        } finally {
            unlock();
        }
    }
        
    protected final int calcNewRowsLimit(){
        final int delta = getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_CONFIG_PATH, 1000);
        if (delta<0){
            return -1;
        }else{
            if (rootNode.isChildrenInited()){
                return rootNode.getNumberOfLoadedInnerNodes()+delta;
            }else{
                return 0;
            }
        }
    }
    
    public final void setRowsLimit(final int newLimit){
        rowsLimit = newLimit;
    }
    
    public final int increaseRowsLimit(){
        if (rowsLimit<0){
            return Integer.MAX_VALUE;
        }
        final int newLimit = calcNewRowsLimit();
        setRowsLimit(newLimit);
        return newLimit;
    }
    
    public int getRowsLimit(){
        return rowsLimit<0 ? Integer.MAX_VALUE : rowsLimit;
    }
    
    public final long getTotalRowsCount(){
        long count = rootNode.getNumberOfLoadedInnerNodes();
        for (SelectorNodeInItemModel node: nodes.values()){
            count+=node.getNumberOfLoadedInnerNodes();
        }
        return count;
    }
    
    public final void enableSelection(){
        if (!selectionEnabled){
            beginInsertColumns(null, 0, 0);
            selectionEnabled = true;
            endInsertColumns();
        }
    }
    
    public final void disableSelection(){
        if (selectionEnabled){
            beginRemoveColumns(null, 0, 0);
            selectionEnabled = false;
            endRemoveColumns();
        }
    }
    
    public final boolean isSelectionEnabled(){
        return selectionEnabled;
    }
    
    public final boolean isSelected(final QModelIndex index){
        if (isSelectionEnabled()){
            final SelectorNode node = getSelectorNode(index);
            final EntityModel entity = node==null ? null : getEntity(index);
            if (entity == null || (entity instanceof BrokenEntityModel)) {
                return false;
            }
            return selection.isSelected(node);
        }else{
            return false;
        }
    }
    
    protected GroupModel getOwnerGroupModel(final EntityModel entityModel){
        return getGroupModel(entityModel);
    }
    
    public final int getNestingLevelOfEntityModel(final EntityModel entityModel){
        if (entityModel.getContext() instanceof IContext.SelectorRow==false){
            throw new IllegalArgumentException("Unknown entity model");
        }
        final GroupModel groupModel = getOwnerGroupModel(entityModel);
        try{
            return getNestingLevelOfGroupModel(groupModel);
        }catch(IllegalArgumentException ex){
            throw new IllegalArgumentException("Unknown entity model",ex);
        }
    }
    
    public final int getNestingLevelOfGroupModel(final GroupModel groupModel){
        if (groupModel==null){
            throw new IllegalArgumentException("Group model must not be null");
        }else if (groupModel==root){
            return 0;
        }else{
            for (SelectorNodeInItemModel node: nodes.values()){
                if (node.getChildGroupModel()==groupModel){
                    return node.getNestingLevel();
                }
            }
            throw new IllegalArgumentException("Unknown group model");
        }
    }
    
    public final HierarchicalSelection<SelectorNode> getSelection(){
        return selection;
    }
    
    public final void setDefaultAdditionalSelectionModes(final List<EHierarchicalSelectionMode> mode){
        additionalSelectionModes = mode==null ? Collections.<EHierarchicalSelectionMode>emptyList() : new LinkedList<>(mode);
    }
    
    public final List<EHierarchicalSelectionMode> getDefaultAdditionalSelectionModes(){
        return Collections.unmodifiableList(additionalSelectionModes);
    }
    
    protected List<EHierarchicalSelectionMode> getAdditionalSelectionModes(final SelectorNode node){
        return getDefaultAdditionalSelectionModes();
    }
    
    public final List<EHierarchicalSelectionMode> calcAdditionalSelectionModes(final SelectorNode node){
        final List<EHierarchicalSelectionMode> additionalSelectionModes = getAdditionalSelectionModes(node);
        if (additionalSelectionModes==null){
            return Collections.<EHierarchicalSelectionMode>emptyList();
        }else if (additionalSelectionModes.isEmpty()){
            return additionalSelectionModes;
        }else{
            final List<EHierarchicalSelectionMode> modes = new LinkedList<>(additionalSelectionModes);
            removeRestrictedSelectionModes(modes, node);
            return modes;
        }
    }

    public final void setDefaultPrimarySelectionMode(final EnumSet<EHierarchicalSelectionMode> mode){
        if (mode==null){
            primarySelectionMode = EnumSet.noneOf(EHierarchicalSelectionMode.class);
        }else{
            primarySelectionMode = mode.clone();            
        }
    }
    
    public final EnumSet<EHierarchicalSelectionMode> getDefaultPrimarySelectionMode(){
        return primarySelectionMode.clone();
    }
    
    protected EnumSet<EHierarchicalSelectionMode> getPrimarySelectionMode(final SelectorNode node){
        return getDefaultPrimarySelectionMode();
    }
    
    public final EnumSet<EHierarchicalSelectionMode> calcPrimarySelectionMode(final SelectorNode node){
        final EnumSet<EHierarchicalSelectionMode> mode = getPrimarySelectionMode(node);
        if (mode==null){
            return EnumSet.noneOf(EHierarchicalSelectionMode.class);
        }
        if (!mode.isEmpty()){
            removeRestrictedSelectionModes(mode, node);
        }
        return mode;
    }
    
    final EnumSet<EHierarchicalSelectionMode> calcAllSelectionModes(final SelectorNode node){
        EnumSet<EHierarchicalSelectionMode> mode = getPrimarySelectionMode(node);
        if (mode==null){
            mode = EnumSet.noneOf(EHierarchicalSelectionMode.class);
        }
        final List<EHierarchicalSelectionMode> additionalSelectionModes = getAdditionalSelectionModes(node);
        if (additionalSelectionModes!=null){
            mode.addAll(additionalSelectionModes);
        }
        removeRestrictedSelectionModes(mode, node);
        return mode;
    }
    
    private void removeRestrictedSelectionModes(final Collection<EHierarchicalSelectionMode> modes,
                                                                          final SelectorNode node){
        if (modes.contains(EHierarchicalSelectionMode.SINGLE_OBJECT)
            && !canSelectNode(node)){
            modes.remove(EHierarchicalSelectionMode.SINGLE_OBJECT);
        }
        if (modes.contains(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE)
            && !hasChildren(node)){
            modes.remove(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE);
        }
        if (modes.contains(EHierarchicalSelectionMode.EXPLICIT_NESTED_OBJECTS)
            && !canSelectExplisitChildren(node)){
            modes.remove(EHierarchicalSelectionMode.ALL_NESTED_OBJECTS_CASCADE);
        }        
    }
    
    private boolean canSelectNode(final SelectorNode node){
        final EntityModel entityModel = getEntity(node);
        if (entityModel==null){
            return true;
        }else{
            final GroupModel groupModel = getGroupModel(entityModel);
            return groupModel==null
                      || (!groupModel.getRestrictions().getIsMultipleSelectionRestricted()                           
                          && (node instanceof SelectorNodeInItemModel==false || !((SelectorNodeInItemModel)node).wasErrorOnCheckChoosable())
                          && groupModel.getEntitySelectionController().isEntityChoosable(entityModel));            
        }
    }
    
    private boolean canSelectExplisitChildren(final SelectorNode node){
        if (hasChildren(node)){
            if (node instanceof SelectorNodeInItemModel){
                final SelectorNodeInItemModel n = (SelectorNodeInItemModel)node;
                final GroupModel groupModel = n.getChildGroupModel();
                return groupModel==null ? true : !groupModel.getRestrictions().getIsMultipleSelectionRestricted();
            }
            return true;
        }else{
            return false;
        }
    }
}
