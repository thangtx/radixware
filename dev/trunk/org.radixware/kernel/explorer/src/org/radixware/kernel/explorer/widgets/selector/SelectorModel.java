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
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.ItemFlags;
import com.trolltech.qt.core.Qt.Orientation;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EFontWeight;
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
import org.radixware.kernel.common.client.models.EntityObjectsSelection;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.ProxyGroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
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
import org.radixware.kernel.common.exceptions.ServiceCallFault;
import org.radixware.kernel.explorer.env.ImageManager;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.editors.valeditors.TristateCheckBoxStyle;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class SelectorModel extends QAbstractItemModel {    
    
    public final static int SORT_INDICATOR_ITEM_ROLE = Qt.ItemDataRole.UserRole + 1;    
        
    private final static int STRING_MAX_LENGTH = 1000; //максимально допустимая длина строки в ячейке DBP-1658
    private final static EnumSet<ETextOptionsMarker> BROKEN_ENTITY_MARKERS = 
        EnumSet.of(ETextOptionsMarker.SELECTOR_ROW, ETextOptionsMarker.BROKEN_REFERENCE);
    private final static String ROWS_LIMIT_CONFIG_PATH = 
        SettingNames.SYSTEM+"/"+SettingNames.SELECTOR_GROUP+"/"+SettingNames.Selector.COMMON_GROUP+"/"+SettingNames.Selector.Common.ROWS_LIMIT_FOR_NAVIGATION;

    private final GroupModel root;    
    private final Map<Long, GroupModel> childGroups = new HashMap<>();
    private final Map<Long, QModelIndex> parentIndexes = new HashMap<>();    
    private final Map<Long, Integer> rowsCount = new HashMap<>();
    private final List<Long> errors = new ArrayList<>();
    private final List<SelectorColumnModelItem> columns = new ArrayList<>();    
    private final QStyleOptionViewItem styleOption = new QStyleOptionViewItem();
    private final Map<String,ExplorerTextOptions> textOptionsCache = new HashMap<>(512);     
    private boolean textOptionsCacheEnabled;
    private int textMargin = QApplication.style().pixelMetric(QStyle.PixelMetric.PM_FocusFrameHMargin);
    private int rowsLimit = 0;
    private boolean selectionEnabled;
        
    public SelectorModel(final GroupModel rootModel) {
        super();
        if (rootModel == null) {
            throw new IllegalArgumentException("root group model must be not null");
        }
        root = rootModel;
        final RadSelectorPresentationDef presentationDef = root.getSelectorPresentationDef();
        final RadSelectorPresentationDef.SelectorColumns selectorColumns = presentationDef.getSelectorColumns();

        for (RadSelectorPresentationDef.SelectorColumn column : selectorColumns) {
            if (column.getVisibility() != ESelectorColumnVisibility.NEVER) {
                columns.add(root.getSelectorColumn(column.getPropertyId()));
            }
        }

        final int count = root.getEntitiesCount();
        if (count > 0) {
            verifySelectorColumns(null, 0, count - 1);
        }
        rowsCount.put(null, count);//no in WrapGroupModel
        increaseRowsLimit();
        styleOption.setDecorationPosition(QStyleOptionViewItem.Position.Left);
        styleOption.setDecorationAlignment(Qt.AlignmentFlag.AlignCenter);
        styleOption.setDisplayAlignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
    }
    
    public void setIconSize(final QSize size){
        styleOption.setDecorationSize(size);
    }
    
    public void setTextMargin(final int margin){
        textMargin = margin;
    }

    protected IClientEnvironment getEnvironment() {
        return root.getEnvironment();
    }

    public GroupModel getRootGroupModel() {
        return root;
    }
        
    @Override
    public ItemFlags flags(final QModelIndex index) {
        final ItemFlags flags = new ItemFlags();
        flags.set(ItemFlag.ItemIsSelectable);
        flags.set(ItemFlag.ItemIsEnabled);
        if (isSelectionEnabled() && index.column()==0 && !isBrokenEntity(index)){
            final EntityModel entity = getEntity(index);
            if (getGroupModel(entity).getEntitySelectionController().isEntityChoosable(entity)){
                flags.set(ItemFlag.ItemIsUserCheckable);
            }
            return flags;
        }
        final Property property = (Property) index.data(ItemDataRole.UserRole);
        if (property == null) {
            return flags;
        }
        // для bool свойств показывать флажок
        if (canUseStandardCheckBox(property)) {
            if (!property.isMandatory()) {
                flags.set(ItemFlag.ItemIsTristate);
            }
            if (canEditPropertyValue(property) && property.isEnabled()) {
                flags.set(ItemFlag.ItemIsUserCheckable);
            }
        } else if (property.isEnabled()) {
            flags.set(ItemFlag.ItemIsEditable);
        }
        return flags;
    }
    
    @Override
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    public Object data(QModelIndex index, int role) {
        if (index == null) {
            return null;
        }
        switch (role) {
            case ItemDataRole.CheckStateRole:
                return getCheckState(index);
            case ItemDataRole.DecorationRole:
                return getDecoration(index);
            case ItemDataRole.DisplayRole:
                return getDisplay(index);
            case ItemDataRole.FontRole:
                return getFont(index);
            case ItemDataRole.SizeHintRole:
                return getSizeHint(index, false);
            case ItemDataRole.TextAlignmentRole:
                final Qt.AlignmentFlag alignmentFlag = getTextAlignment(index);
                if (alignmentFlag != null) {
                    final Qt.Alignment alignment = new Qt.Alignment(Qt.AlignmentFlag.AlignVCenter, alignmentFlag);
                    return alignment.value();
                }
                return null;
            case ItemDataRole.UserRole:
                return getProperty(index);
            case ItemDataRole.BackgroundRole: {
                return getBackground(index);
            }
            case ItemDataRole.ForegroundRole: {
                return getForeground(index);
            }
            case ItemDataRole.ToolTipRole:{
                return getToolTip(index);
            }
            default:
                return null;
        }
    }
    
    public QSize getSizeHint(final QModelIndex index, final boolean approximately){
        final Property property = getProperty(index);
        final int widthMargin = 2*(textMargin+1);
        if (property==null){
            final QFontMetrics fontMetrics = getTextOptions(index).getFont().getQFontMetrics();
            final String text = getDisplay(index);
            return new QSize(WidgetUtils.calcTextWidth(text, fontMetrics, approximately) + widthMargin, fontMetrics.height());
        }else{
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
            final boolean hasIcon = getIconForProperty(property)!=null;            
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
    }
    
    public int getHeightHint(final QModelIndex index){
        final Property property = getProperty(index);
        if (property==null){
            return getTextOptions(index).getFont().getQFontMetrics().height();
        }else{
            final boolean hasCheckBox = getCheckState(property)!=null;
            final QFontMetrics fontMetrics = getTextOptions(property).getFont().getQFontMetrics();
            if (hasCheckBox){
                return Math.max(TristateCheckBoxStyle.INDICATOR_SIZE, fontMetrics.height());
            }
            final boolean hasIcon = getIconForProperty(property)!=null;            
            if (hasIcon){
                return Math.max(styleOption.decorationSize().height(), fontMetrics.height());
            }else{
                return fontMetrics.height();
            }
        }
    }
    
    private Object getCheckState(final QModelIndex index) {
        if (isSelectionEnabled() && index.column()==0){
            final EntityModel entity = getEntity(index);
            if (entity == null || (entity instanceof BrokenEntityModel)) {
                return null;
            }
            final GroupModel groupModel = getGroupModel(entity);
            if (groupModel.getSelection().isObjectSelected(entity)){
                return Qt.CheckState.Checked;
            }else{
                return groupModel.getEntitySelectionController().isEntityChoosable(entity) ?  Qt.CheckState.Unchecked : null;
            }
        }
        final Property property = getProperty(index);
        return getCheckState(property);
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
        final Property property = getProperty(index);
        if (property == null) {
            return null;
        }
        return getIconForProperty(property);
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
            return getEnvironment().getMessageProvider().translate("Value", "<!!!Error!!!>");
        }
    }

    @Override
    public boolean setData(final QModelIndex index, final Object data, final int role) {
        if (role == ItemDataRole.CheckStateRole) {
            if (isSelectionEnabled() && index.column()==0){
                final EntityModel entity = getEntity(index);        
                if (entity != null && entity instanceof BrokenEntityModel==false) {
                    final GroupModel groupModel = getGroupModel(entity);
                    if (groupModel.getEntitySelectionController().isEntityChoosable(entity)){
                        groupModel.getSelection().invertSelection(entity.getPid());
                        return true;
                    }else{
                        return false;
                    }     
                }
            }
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
    
    public void setTextOptionsCacheEnabled(final boolean enabled){
        textOptionsCacheEnabled = enabled;
        if (!enabled){
            textOptionsCache.clear();
        }
    }
    
    private static String indexCacheKey(final QModelIndex index){
        return index.row()+"_"+index.column()+"_"+index.internalId();
    }
    
    public ExplorerTextOptions getTextOptions(final QModelIndex index){
        ExplorerTextOptions textOptions;
        if (textOptionsCacheEnabled){
            textOptions = textOptionsCache.get(indexCacheKey(index));
        }else{
            textOptions = null;
        }
        if (textOptions==null){
            if (isBrokenEntity(index)){
                textOptions = (ExplorerTextOptions)getEnvironment().getTextOptionsProvider().getOptions(BROKEN_ENTITY_MARKERS, null);
            }else{
                textOptions = getTextOptions(getProperty(index));
            }
            if (textOptionsCacheEnabled){
                textOptionsCache.put(indexCacheKey(index), textOptions);
            }
        }
        return textOptions;
    }
    
    private ExplorerTextOptions getTextOptions(final Property property){
        if (property==null){
            return ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.SELECTOR_ROW);
        }else{
            final EnumSet<ETextOptionsMarker> propertyMarkers =  property.getTextOptionsMarkers();
            if (propertyMarkers.contains(ETextOptionsMarker.INVALID_VALUE) && !canEditPropertyValue(property)){
                propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
            }
            return (ExplorerTextOptions)property.getValueTextOptions().getOptions(propertyMarkers);
        }        
    }
    
    protected QFont getFont(final QModelIndex index) {
        return getTextOptions(index).getQFont();
    }

    protected QColor getForeground(QModelIndex index) {
        return getTextOptions(index).getForeground();
    }

    protected String getToolTip(final QModelIndex index){
        final Property property = getProperty(index);
        return property==null ? null : getToolTip(property);
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
                    return state.getInvalidValueReason().toString();
                }
                state = property.getOwner().getPropertyValueState(property.getId());
                if (state!=ValidationResult.ACCEPTABLE){
                    return state.getInvalidValueReason().toString();
                }                
            }        
        }
        return property.getHint();
    }
    
    protected String getHeaderToolTip(final SelectorColumnModelItem selectorColumn){
        final String toolTip = selectorColumn.getHint();
        if (selectorColumn.getHeaderMode()==ESelectorColumnHeaderMode.ONLY_ICON
            && (toolTip==null || toolTip.isEmpty())
           ){
            return selectorColumn.getTitle();
        }
        return toolTip;
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
        final GroupModel groupModel = getGroupModel(entity);
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
    public Object headerData(final int section, final Qt.Orientation orientation, final int role) {
        switch (role) {
            case Qt.ItemDataRole.DisplayRole:
                if (orientation == Qt.Orientation.Horizontal) {
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    if (column==null){
                        return " ";
                    }
                    if (column.getHeaderMode()==ESelectorColumnHeaderMode.ONLY_ICON){
                        return "";                        
                    }else{
                        return ClientValueFormatter.capitalizeIfNecessary(getEnvironment(), column.getTitle());
                    }
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
            case Qt.ItemDataRole.FontRole: {
                if (orientation == Qt.Orientation.Horizontal) {
                    final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();

                    settings.beginGroup(SettingNames.SYSTEM);
                    settings.beginGroup(SettingNames.SELECTOR_GROUP);
                    settings.beginGroup(SettingNames.Selector.COMMON_GROUP);

                    final QFont font = settings.readQFont(SettingNames.Selector.Common.HEADER_FONT_IN_SELECTOR);

                    settings.endGroup();
                    settings.endGroup();
                    settings.endGroup();

                    final QFont actualFont = font != null ? font : QApplication.font();
                    if (section == currentColumn && !root.isEmpty()) {
                        return getBoldFont(actualFont);
                    }
                    return actualFont;
                } else {
                    return super.headerData(section, orientation, role);
                }
            }

            //
            case ItemDataRole.TextAlignmentRole: {
                if (orientation == Orientation.Horizontal) {
                    final ExplorerSettings settings = (ExplorerSettings) getEnvironment().getConfigStore();
                    final AlignmentFlag alignmentFlag;

                    settings.beginGroup(SettingNames.SYSTEM);
                    settings.beginGroup(SettingNames.SELECTOR_GROUP);
                    settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                    alignmentFlag = settings.readAlignmentFlag(SettingNames.Selector.Common.TITLES_ALIGNMENT);
                    settings.endGroup();
                    settings.endGroup();
                    settings.endGroup();

                    return alignmentFlag != null ? alignmentFlag.value() : AlignmentFlag.AlignLeft.value();
                } else {
                    return super.headerData(section, orientation, role);
                }
            }
            case ItemDataRole.BackgroundRole: {
                if (orientation == Qt.Orientation.Horizontal) {
                    if (section == currentColumn && !root.isEmpty()) {
                        return QApplication.palette().alternateBase().color();
                    } else {
                        return QApplication.palette().button().color();
                    }
                }
                return super.headerData(section, orientation, role);
            }
            case ItemDataRole.ToolTipRole: {
                if (orientation == Qt.Orientation.Horizontal){
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    if (column==null && isSelectionEnabled() && section==0){
                        final EntityObjectsSelection selection = getRootGroupModel().getSelection();
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
            case ItemDataRole.DecorationRole: {
                if (orientation == Qt.Orientation.Horizontal) {
                    final SelectorColumnModelItem column = getSelectorColumn(section);
                    if (column==null){
                        return super.headerData(section, orientation, role);
                    }
                    if (column.getHeaderMode()==ESelectorColumnHeaderMode.ONLY_TEXT){
                        return super.headerData(section, orientation, role);
                    }else{
                        final Icon headerIcon = column.getHeaderIcon();
                        if (headerIcon==null){
                            return super.headerData(section, orientation, role);
                        }else{
                            return ImageManager.getQIcon(headerIcon);
                        }
                    }
                }else{
                    return super.headerData(section, orientation, role);
                }
            }
            default:
                return super.headerData(section, orientation, role);
        }
    }
    
    @Override
    public final int rowCount(final QModelIndex index) {
        final Long key = index == null ? null : index.internalId();
        if (rowsCount.containsKey(key)) {
            final Integer rowCount = rowsCount.get(key);
            return rowCount != null && rowCount > 0 ? rowCount : 0;
        } else {
            return 0;
        }
    }
    
    @Override
    public final boolean hasChildren(final QModelIndex index) {
        final Long key = index == null ? null : index.internalId();
        if (rowsCount.containsKey(key)) {
            final Integer rowCount = rowsCount.get(key);
            return rowCount != null ? rowCount > 0 : true;
        } else if (key == null) {
            return false;
        } else {
            final EntityModel parentEntity = getEntity(index);
            if ((parentEntity instanceof BrokenEntityModel) || !tryLock()) {
                return false;
            }
            try {
                final Id propertyId = getHasChildrenPropertyId(parentEntity);
                final boolean hasChildren;                
                if (propertyId != null) {
                    final Property property = parentEntity.getProperty(propertyId);
                    if (!property.isActivated()){
                        final String message = getEnvironment().getMessageProvider().translate("ExplorerError", "Unable to get information about subobjects for \'%1$s\' object.\nProperty \'%2$s\' (#%3$s) was not activated.\nIt is necessary to add this property in selector columns set in #%4$s presentation");
                        final GroupModel parentGroupModel = getGroupForParentIndex(index==null ? null : index.parent());
                        final String formattedMessage = String.format(message, parentEntity.getTitle(), property.getDefinition().getName(), property.getId().toString(), parentGroupModel.getSelectorPresentationDef().getId().toString());
                        getEnvironment().getTracer().error(formattedMessage);
                        rowsCount.put(index==null ? null : index.internalId(), 0);
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

                if (hasChildren) {
                    rowsCount.put(index==null ? null : index.internalId(), null);
                } else {
                    rowsCount.put(index==null ? null : index.internalId(), 0);
                }
                return hasChildren;
            } catch (Throwable ex) {
                final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't get information about children of object \'%s\'");
                getEnvironment().getTracer().error(String.format(title, parentEntity.getTitle()), ex);
                getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(title, parentEntity.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
                rowsCount.put(index==null ? null : index.internalId(), 0);                
                return false;
            } finally {
                unlock();
            }
        }
    }
    
    private SelectorColumnModelItem getSelectorColumn(final int rawIndex){        
        final int logicalIndex = isSelectionEnabled() ? rawIndex-1 : rawIndex;
        if (logicalIndex>=0 && logicalIndex<columns.size()){
            return columns.get(logicalIndex);
        }else{
            return null;
        }
    }

    @Override
    public int columnCount(final QModelIndex index) {
        return isSelectionEnabled() ? columns.size() + 1 : columns.size();
    }

    public List<SelectorColumnModelItem> getSelectorColumns() {
        return Collections.unmodifiableList(columns);
    }

    protected AlignmentFlag getTextAlignment(QModelIndex index) {
        final SelectorColumnModelItem column = getSelectorColumn(index.column());        
        if (column==null){
            return AlignmentFlag.AlignLeft;
        }
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
                return AlignmentFlag.AlignCenter;
            case LEFT:
                return AlignmentFlag.AlignLeft;
            case RIGHT:
                return AlignmentFlag.AlignRight;
            default:
                return AlignmentFlag.AlignLeft;
        }
    }        

    protected QColor getBackground(final QModelIndex index) {
        final boolean isSelected = isSelected(index);
        final ExplorerTextOptions options;
        if (isSelected && index.column()==0){
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
        if (row >= 0 && (row + count - 1) < rowCount(parent)) {            
            beginRemoveRows(parent, row, row + count - 1);
            final Integer rowCount = rowsCount.get(parent != null ? parent.internalId() : null);
            if (rowCount == null || rowCount <= 0) {
                return false;
            }
            final GroupModel group = getGroupForParentIndex(parent);
            QModelIndex index;
            for (int i = 0; i < count; i++) {
                index = index(row + i, 0, parent);
                group.removeRow(row);
                clear(index);
                parentIndexes.remove(index.internalId());
                childGroups.remove(index.internalId());
            }

            //Нужно обновить индексы подъелементов, родительские
            //элементы которых следуют за удаленными элементами
            for (int i = row + count - 1; i < (rowCount - count); i++) {
                //Цикл по элементам, которые следуют за удаленными элементами
                index = index(i, 0, parent);
                for (int j = rowCount(index) - 1; j >= 0; j--) {//Цикл по их дочерним элементам
                    //Обновление закэшированных индексов
                    parentIndexes.put(index(j, 0, index).internalId(), index);
                }
            }
            rowsCount.put(parent != null ? parent.internalId() : null, rowCount - count);
            endRemoveRows();
            return true;
        }
        return false;
    }

    public void reread(final QModelIndex parent) throws InterruptedException, ServiceClientException {
        reset(parent);
        readMore(parent);
    }
    
    public void reset(final QModelIndex parent){
        final GroupModel group = getGroupForParentIndex(parent);
        if (group != null) {
            lock();
            try {
                if (parent == null) {
                    reset();
                    group.reset();
                    clear(null);
                } else if (rowCount(parent) > 0) {
                    beginRemoveRows(parent, 0, rowCount(parent) - 1);
                    group.reset();
                    clear(parent);
                    endRemoveRows();
                } else {
                    group.reset();
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
        if (!readMore(parent) || !tryLock()) {
            return -1;
        }
        final GroupModel group = getGroupForParentIndex(parent);
        try {
            final int idx;

            if (errors.contains(parent != null ? parent.internalId() : null)) {
                idx = group.findEntityByPid(pid);
            } else {
                idx = group.readToEntity(pid, getEnvironment().getMessageProvider().translate("Wait Dialog", "Restoring Position"));
            }
            updateRowsCount(parent, group);
            return idx;
        }catch(ServiceCallFault fault){
            throw preprocessServiceCallFault(parent,fault);
        }
        finally {
            unlock();
        }
    }

    protected void clear(final QModelIndex parent) {
        if (parent != null) {
            final List<Long> itemsForRemove = new ArrayList<>();
            final Stack<Long> stack = new Stack<>();
            long currentId;

            stack.push(parent.internalId());
            while (!stack.isEmpty()) {
                currentId = stack.pop();
                for (Map.Entry<Long, QModelIndex> entry : parentIndexes.entrySet()) {
                    if (entry.getValue().internalId() == currentId) {
                        itemsForRemove.add(entry.getKey());
                        stack.push(entry.getKey());
                    }
                }
            }

            GroupModel childGroup;
            for (Long internalId : itemsForRemove) {
                errors.remove(internalId);
                parentIndexes.remove(internalId);
                childGroup = childGroups.get(internalId);
                if (childGroup!=null){
                    changeChildGroupContext(childGroup, null);                    
                    if (childGroup.getView()!=null
                        && (childGroup.getView()==root.getView() || root.getView()==null)){                    
                        childGroup.setView(null);
                    }
                }
                childGroups.remove(internalId);
                rowsCount.remove(internalId);
            }

            errors.remove(parent.internalId());
            rowsCount.remove(parent.internalId());
            rowsLimit = getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_CONFIG_PATH, 1000);
        } else {
            errors.clear();
            parentIndexes.clear();
            for (GroupModel childGroup: childGroups.values()){
                if (childGroup!=null){
                    changeChildGroupContext(childGroup, null);
                    if (childGroup.getView()!=null
                         && (childGroup.getView()==root.getView() || root.getView()==null)){
                        childGroup.setView(null);
                    }
                }
            }
            childGroups.clear();
            rowsCount.clear();
            rowsLimit = getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_CONFIG_PATH, 1000);
        }

    }

    public void clear() {
        lock();
        clear(null);
        reset();
    }

    public boolean canReadMore(final QModelIndex parent) {
        final GroupModel group;
        try {
            group = parent != null ? getChildGroup(parent) : root;
        } catch (Throwable ex) {
            final EntityModel parentEntity = getEntity(parent);
            final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \'%s\'");
            getEnvironment().getTracer().error(String.format(title, parentEntity.getTitle()), ex);
            getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                    String.format(title, parentEntity.getTitle(),
                    ClientException.exceptionStackToString(ex)),
                    EEventSource.EXPLORER);
            return false;
        }

        final int rowCount = rowCount(parent);

        return (group != null)
                && !isLocked()
                && !errors.contains(parent == null ? null : parent.internalId())
                && (rowCount < group.getEntitiesCount() || group.hasMoreRows());
    }
    private boolean locked = false;

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
        final GroupModel group;
        try {
            group = parent != null ? getChildGroup(parent) : root;
            if (group == null && parent != null && !isLocked()) {
                rowsCount.put(parent.internalId(), 0);
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

        final int rowCount = rowCount(parent);

        if (group != null
                && !errors.contains(parent == null ? null : parent.internalId())
                && (rowCount < group.getEntitiesCount() || group.hasMoreRows())
                && tryLock()) {
            boolean wasException = true;
            try {                
                if (!readMoreInternal(parent)) {
                    wasException = false;
                    if (parent != null
                            && (!rowsCount.containsKey(parent.internalId()) || rowsCount.get(parent.internalId()) == null)) {
                        rowsCount.put(parent.internalId(), 0);
                    }
                    return false;
                }
                wasException = false;
                return true;
            } catch (ServiceCallFault fault) {
                throw preprocessServiceCallFault(parent,fault);
            } catch (InterruptedException exception){
                wasException = false;//Если прерывание было инициировано пользователем, то можно читать дальше.
                throw exception;
            }
            finally {
                if (wasException){
                    errors.add(parent == null ? null : parent.internalId());
                }
                unlock();
            }
        }else if (group!=null && group.getEntitiesCount()==0 && !group.hasMoreRows()){
            rowsCount.put(parent == null ? null : parent.internalId(), 0);
        }
        return false;
    }

    protected void showErrorOnReceivingData(final ServiceClientException exception) {
        final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Error on receiving data");
        getEnvironment().getTracer().error(title, exception);
        root.showException(title, exception);
    }
    
    private ServiceCallFault preprocessServiceCallFault(final QModelIndex parent, final ServiceCallFault fault){
        ObjectNotFoundError objectNotFound = null;
        for (Throwable cause = fault; cause != null; cause = cause.getCause()) {
            if (cause instanceof ObjectNotFoundError) {
                objectNotFound = (ObjectNotFoundError) cause;
            }            
        }
                
        if (objectNotFound != null) {
            EntityModel parentEntity;
            for (QModelIndex index = parent; index != null; index = index.parent()) {
                parentEntity = getEntity(index);
                if (parentEntity != null && !(parentEntity instanceof BrokenEntityModel) && objectNotFound.inContextOf(parentEntity)) {
                    objectNotFound.setContextEntity(parentEntity);
                    return objectNotFound;
                }
            }
        }                    
        return fault;
    }

    private boolean readMoreInternal(final QModelIndex parent) throws ServiceClientException, InterruptedException {
        int rowCount = rowCount(parent);
        final GroupModel group = getGroupForParentIndex(parent);
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
        updateRowsCount(parent, group);
        return true;
    }

    void updateRowsCount(final QModelIndex parent, final GroupModel group) {
        final int oldCount = rowCount(parent),
                newCount = group.getEntitiesCount();
        if (oldCount < newCount) {
            rowsCount.put(parent != null ? parent.internalId() : null, newCount);
            final int row = verifySelectorColumns(parent, oldCount, newCount - 1);
            if (row>=oldCount){
                setTextOptionsCacheEnabled(true);
                beginInsertRows(parent, oldCount, row);
                rowsCount.put(parent != null ? parent.internalId() : null, row + 1);
                endInsertRows();
                setTextOptionsCacheEnabled(false);
            }
            else{
                rowsCount.put(parent != null ? parent.internalId() : null, oldCount);
            }
        }
    }

    /**
     *Get values of selector columns to ensure all necessary data received
     *before data method  invoked
     * returns last verified row
     */
    private int verifySelectorColumns(final QModelIndex parent, final int startWith, final int finishAt) {
        Id propertyId;
        SelectorColumnModelItem column;
        EntityModel entity;
        int row;        
        for (row = startWith; row <= finishAt; row++) {
            try {                
                entity = getEntity(index(row, 0, parent));
                if (entity==null || entity instanceof BrokenEntityModel){
                    continue;
                }                
                final GroupModel groupModel = getGroupModel(entity);
                for (int i = 0; i < columns.size(); ++i) {
                    column = columns.get(i);
                    try {
                        propertyId = mapSelectorColumn(column, groupModel);
                        if (propertyId != null) {
                            entity.getProperty(propertyId).getValueObject();
                        }
                    } catch (Throwable ex) {
                        errors.add(parent != null ? parent.internalId() : null);
                        final String title = getEnvironment().getMessageProvider().translate("ExplorerException", "Can't get property value corresponding to column #%s with title \'%s\'");
                        getEnvironment().getTracer().error(String.format(title, column.getId(), column.getTitle()), ex);
                        getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format(title, column.getId(), column.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
                        throw ex;
                    }
                }
            } catch (Throwable ex) {
                break;
            }
        }
        return row - 1;
    }
    
    private static GroupModel getGroupModel(final EntityModel entity){
        return ((IContext.SelectorRow) entity.getContext()).parentGroupModel;
    }
    
    public final GroupModel getCachedChildGroup(final QModelIndex parent){
        return getGroupForParentIndex(parent);
    }
    
    public final boolean errorWasDetected(final QModelIndex index){
        return errors.contains(index==null ? null : index.internalId());
    }

    public GroupModel getChildGroup(final QModelIndex parent) {
        GroupModel group = getGroupForParentIndex(parent);
        if (group == null) {
            final EntityModel parentEntity = getEntity(parent);
            if (parentEntity instanceof BrokenEntityModel){
                childGroups.put(parent.internalId(), null);
            }
            else if ((hasChildren(parent) || canCreateChild(parentEntity))
                    && !errors.contains(parent.internalId())
                    && tryLock()) {
                try {
                    group = createChildGroupModel(parentEntity);
                    if (group != null) {
                        changeChildGroupContext(group, root.getGroupContext());
                        group.getRestrictions().add(calcChildGroupRestrictions(parentEntity, group));
                        final GroupModel parentGroup;
                        if (parent.parent()==null){
                            parentGroup = getRootGroupModel();
                        }else{
                            parentGroup = getGroupForParentIndex(parent.parent());
                        }
                        prepareChildGroupFilter(parentGroup, group);
                        prepareGroupCondition(parentEntity, group);
                        group.setView(root.getView());
                    } else {
                        getEnvironment().getTracer().put(EEventSeverity.DEBUG, String.format("Child group model was not created for parent entity \'%s\'", parentEntity.getTitle()), EEventSource.EXPLORER);
                    }
                    childGroups.put(parent.internalId(), group);
                } finally {
                    unlock();
                }
            }
        }//if (group==null && mutex.tryLock())
        return group;
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
    public QModelIndex parent(QModelIndex child) {
        return parentIndexes.get(child.internalId());
    }

    @Override
    public QModelIndex index(final int row, final int column, final QModelIndex parent) {
        if (row < 0 || row >= rowCount(parent)) {
            //throw new ArrayIndexOutOfBoundsException(row);
            return null;//for internal call on remove last row.
        }
        if (column < 0 || column >= (isSelectionEnabled() ? columns.size()+1 : columns.size())){ //throw new ArrayIndexOutOfBoundsException(column);        
            return null;
        }
        final int internalId = calcEntityHashCode(row, parent);
        if (internalId == 0) {
            return null;//invalid parent index
        }
        final QModelIndex result = createIndex(row, column, internalId);
        if (parent != null && !parentIndexes.containsKey(result.internalId())) {
            parentIndexes.put(result.internalId(), parent);
        }
        return result;
    }

    private int calcEntityHashCode(final int row, final QModelIndex parent) {
        int r = row, hash = 7;
        EntityModel entity;
        for (QModelIndex index = parent; index != null; index = index.parent()) {
            entity = getEntity(r, index);            
            hash = 67 * hash + (entity==null || entity.getPid()==null ? r : entity.getPid().toString().hashCode());
            r = index.row();
        }
        entity = getEntity(r, null);        
        return 67 * hash + (entity==null || entity.getPid()==null ? r : entity.getPid().toString().hashCode());
    }

    private GroupModel getGroupForParentIndex(final QModelIndex parent) {
        return parent != null ? childGroups.get(parent.internalId()) : root;
    }

    private EntityModel getEntity(final int row, final QModelIndex parent) {
        final GroupModel group = getGroupForParentIndex(parent);
        try {
            return group != null ? group.getEntity(row) : null;
        } catch (ServiceClientException | InterruptedException ex) {
            getEnvironment().processException(ex);
        }
        catch (BrokenEntityObjectException ex) {
            return createBrokenEntityModel(group, ex);
        }        
        return null;
    }

    public EntityModel getEntity(QModelIndex index) {
        return index != null ? getEntity(index.row(), index.parent()) : null;
    }
    
    public boolean isBrokenEntity(QModelIndex index){
        return getEntity(index) instanceof BrokenEntityModel;
    }
    
    protected final EntityModel createBrokenEntityModel(final GroupModel ownerGroupModel, final BrokenEntityObjectException exception){
        return new BrokenEntityModel(getEnvironment(), ownerGroupModel.getSelectorPresentationDef(), exception);
    }

    private Property getProperty(final QModelIndex index) {
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
            propertyId = mapSelectorColumn(column, getGroupModel(entity));
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
            return !getGroupModel(parentEntity).getRestrictions().getIsCreateRestricted();
        } else {
            return !root.getRestrictions().getIsCreateRestricted();
        }
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
            clear(null);
            reset();
        } finally {
            unlock();
        }
    }
    
    
    protected final int calcNewRowsLimit(){
        final Integer currentRows = rowsCount.get(null);        
        final int delta = getEnvironment().getConfigStore().readInteger(ROWS_LIMIT_CONFIG_PATH, 1000);
        return delta<0 ? -1 : (currentRows==null ? 0 : currentRows.intValue())+delta;
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
        long count = 0;
        for (Integer rowsCountInBranch: rowsCount.values()){
            if (rowsCountInBranch!=null && rowsCountInBranch.intValue()>0){
                count+=rowsCountInBranch.intValue();
            }
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
            final EntityModel entity = getEntity(index);
            if (entity == null || (entity instanceof BrokenEntityModel)) {
                return false;
            }
            final GroupModel groupModel = getGroupModel(entity);            
            return groupModel.getSelection().isObjectSelected(entity);
        }else{
            return false;
        }
    }
}
