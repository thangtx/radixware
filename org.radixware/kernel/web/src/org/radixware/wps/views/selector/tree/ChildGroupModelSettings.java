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

package org.radixware.wps.views.selector.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.models.items.properties.PropertyInt;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.scml.SqmlExpression;
import org.radixware.kernel.common.types.Id;

/**
 * Параметры модели группы (Radix::Web.Widgets.SelectorTree::ChildGroupModelSettings). Класс содержит параметры, необходимые для создания и настройки инстанции модели группы, а также
 * настройки для отображения ее содержимого в дереве селектора.
 */
public class ChildGroupModelSettings {
    
    private GroupModel groupModel;
    private final Id explorerItemId;
    private final Id classId;
    private final Id selectorPresentationId;
    
    private Restrictions addintionalRestrictions;
    private SqmlExpression addintionalCondition;
    private Map<Id,Id> columnsMapping;
    private Id hasRecordsPropertyId;
    private Boolean hasRecordsPropertyValue;
    private Map<Id,Object> propertyConditions;
    private Map<Id,Object> propertyValues;
    private Map<Id,Object> newObjectProperties;
    private Icon icon;
    private boolean groupIsEmpty = false;
    private boolean canCreateObject = true;
        
    /**
     * Конструктор параметров модели группы. Используется для создания модели группы на основе элемента проводника.
     * @param explorerItemId идентификатор элемента проводника
     * @param columnsMapping карта соответствия идентификаторов колонок в древовидном селекторе идентификаторам свойств объектов, 
     * содержащихся в модели группы. Ключ карты - идентификатор колонки, значение - идентификатор свойства. Параметр может быть <code>null</code>.
     */
    public ChildGroupModelSettings(final Id explorerItemId, final Map<Id,Id> columnsMapping){
        this.explorerItemId = explorerItemId;
        classId = null;
        selectorPresentationId = null;
        if (columnsMapping!=null){
            this.columnsMapping = new HashMap<>(columnsMapping);
        }        
    }

    /**
     * Конструктор параметров модели группы. Используется для создания модели группы на основе элемента проводника.
     * @param explorerItemId идентификатор элемента проводника
     */
    public ChildGroupModelSettings(final Id explorerItemId){
        this(explorerItemId,(Map<Id,Id>)null);
    }
    
    /**
     * Конструктор параметров модели группы. Используется для создания бесконтекстной модели группы.
     * @param classId идентификатор класса в котором определена презентация селектора
     * @param selectorPresentationId идентификатор презентации селектора
     * @param columnsMapping карта соответствия идентификаторов колонок в древовидном селекторе идентификаторам свойств объектов, 
     * содержащихся в модели группы. Параметр может быть <code>null</code>.
     */
    public ChildGroupModelSettings(final Id classId, final Id selectorPresentationId, final Map<Id,Id> columnsMapping){
        this.classId = classId;
        this.selectorPresentationId = selectorPresentationId;
        this.explorerItemId = null;
        if (columnsMapping!=null){
            this.columnsMapping = new HashMap<>(columnsMapping);
        }
    }
    
    /**
     * Конструктор параметров модели группы. Используется для создания бесконтекстной модели группы.
     * @param classId идентификатор класса в котором определена презентация селектора
     * @param selectorPresentationId идентификатор презентации селектора
     */
    public ChildGroupModelSettings(final Id classId, final Id selectorPresentationId){
        this(classId,selectorPresentationId,null);
    }

    /**
     * Конструктор копий
     * @param source 
     */    
    protected ChildGroupModelSettings(final ChildGroupModelSettings source){
        groupModel = source.groupModel;
        explorerItemId = source.explorerItemId;
        classId = source.classId;
        selectorPresentationId = source.selectorPresentationId;
        addintionalRestrictions = source.addintionalRestrictions;
        addintionalCondition = source.addintionalCondition;
        if (source.columnsMapping!=null){
            columnsMapping = new HashMap<>(source.columnsMapping);
        }
        hasRecordsPropertyId = source.hasRecordsPropertyId;
        if (source.propertyConditions!=null){
            propertyConditions = new HashMap<>(source.propertyConditions);
        }
        if (source.newObjectProperties!=null){
            newObjectProperties = new HashMap<>(source.newObjectProperties);
        }
        if (source.propertyValues!=null){
            propertyValues = new HashMap<>(source.propertyValues);
        }
        icon = source.icon;
        groupIsEmpty = source.groupIsEmpty;
        canCreateObject = source.canCreateObject;
    }        
    
    /**
     * Установка соответствия колонки селектора свойству объекта. 
     * Метод позволяет установить соответствие между колонкой селектора и свойством объекта, содержащегося в модели группы.
     * @param columnId идентификатор колонки селектора
     * @param propertyId идентификатор свойства объекта
     */
    public final void setPropertyIdForSelectorColumn(final Id columnId, final Id propertyId){
        if (columnsMapping==null){
            columnsMapping = new HashMap<>();
        }
        columnsMapping.put(columnId, propertyId);
    }
    
    /**
     * Установка соответствия между колонками селектора и свойствами объекта. 
     * Метод позволяет установить соответствия между колонками селектора и свойствами объектов, содержащихся в модели группы.
     * @param columnsMap карта соответствия идентификаторов колонок в древовидном селекторе идентификаторам свойств объектов, 
     * содержащихся в модели группы. 
     */
    public final void setColumnsMapping(final Map<Id,Id> columnsMap){
        if (columnsMap!=null){
            this.columnsMapping = new HashMap<>(columnsMap);
        }
    }
    
    /**
     * Очистка текущей карты соответствия между колонками селектора и свойствами объекта. 
     * Метод позволяет очистить текущую карту соответствия колонок селектора свойствам объекта.
     */
    public final void clearColumnsMapping(){
        columnsMapping = null;
    }

    /**
     * Получение идентификатора свойства объекта, соответствующего колонке селектора. 
     * Метод возвращает идентификатор свойства объекта сущности {@link EntityModel}, соответствующий идентификатору колонки в селекторе.
     * Стандартная реализация возвращает результат на основе карты соответствия, заполненной вызовами методов 
     * {@link #setPropertyIdForSelectorColumn(Id, Id)  setPropertyIdForSelectorColumn} и {@link #setColumnsMapping(Map) setColumnMapping},
     * или переданной в конструкторе.
     * @param entityModel объект сущности
     * @param columnId идентификатор колонки селектора
     * @return идентификатор свойства объекта сущности
     */
    public Id getPropertyIdForSelectorColumn(final EntityModel entityModel, final Id columnId){
        if (entityModel.getEntityContext() instanceof IContext.SelectorRow){
            final GroupModel parentGroupModel = 
                    ((IContext.SelectorRow)entityModel.getEntityContext()).parentGroupModel;
            if (groupModel==parentGroupModel){
                return columnsMapping==null ? null : columnsMapping.get(columnId);
            }
        }
        return null;
    }
    
    /**
     * Установка дополнительного условия выборки.
     * Метод позволяет установить дополнительное условие на выборку объектов в модели группы.
     * Условие будет передано в модель группы один раз сразу после ее создания.
     * @param condition sqml-условие
     */
    public final void setAddintionalCondition(final SqmlExpression condition){
        this.addintionalCondition = condition;
    }
    
    /**
     * Получение дополнительного условия выборки.
     * Метод позволяет получить дополнительное условие на выборку объектов в модели группы, 
     * заданное в методе {@link #setAddintionalCondition(SqmlExpression) setAddintionalCondition}.
     * @return sqml-условие
     */
    public final SqmlExpression getAddintionalCondition(){
        return addintionalCondition;
    }

    /**
     * Установить условие на значение свойства.
     * Метод позволяет установить ограничение на значение свойства с указанным идентификатором.
     * После создания модели группы будет сформировано такое условие, что в нее войдут только те объекты, 
     * свойство с идентификатором <code>propertyId</code> которых имеет значение <code>value</code>.
     * Данное условие будет объеденено с условием, 
     * заданным в методе {@link #setAddintionalCondition(SqmlExpression) setAddintionalCondition}, оператором <code>"AND"</code>.
     * @param propertyId идентификатор свойства
     * @param value значение свойства
     */
    public final void setPropertyValueCondition(final Id propertyId, final Object value){
        if (propertyConditions==null){
            propertyConditions = new HashMap<>(8);
        }
        propertyConditions.put(propertyId, value);
    }
    
    /**
     * Убрать условие на значение свойства.
     * Метод позволяет снять ограничение на значение свойства с указанным идентификатором.
     * Вызов метода снимает ограничение на значение свойства объектов, находящихся в модели группы, наложенное методом 
     * {@link #setPropertyValueCondition(Id, Object) setPropertyValueCondition}.
     * @param propertyId идентификатор свойства
     */
    public final void removePropertyValueCondition(final Id propertyId){
        if (propertyConditions!=null){
            propertyConditions.remove(propertyId);
        }        
    }

    /**
     * Убрать все ограничения на значения свойств.
     * Метод позволяет снять ограничения на значения всех свойств.
     * Вызов метода снимает ограничения на значение всех свойств объектов, находящихся в модели группы, наложенные методом 
     * {@link #setPropertyValueCondition(Id, Object) setPropertyValueCondition}.
     */
    public final void clearPropertyValueConditions(){
        propertyConditions = null;
    }

    /**
     * Расчитать окончательное условие для настройки модели группы.
     * Метод расчитывает конечное условие, которое должно быть передано модели группы, после ее создания.
     * На основе ограничений на значения свойств объектов, заданных при помощи {@link #setPropertyValueCondition(Id, Object) setPropertyValueCondition}, 
     * и дополнительного условия, заданного при помощи {@link #setAddintionalCondition(SqmlExpression)  setAddintionalCondition}, 
     * данный метод формирует единое условие на выборку объектов сущности.
     * @param classDef дефиниция класса модели группы
     * @return Sqml-условие
     */
    protected org.radixware.schemas.xscml.Sqml calcFinalCondition(final RadClassPresentationDef classDef){
        if (propertyConditions!=null && !propertyConditions.isEmpty()) {
            final org.radixware.schemas.xscml.Sqml condition = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
            RadPropertyDef propertyDef;
            org.radixware.schemas.xscml.Sqml.Item.PropSqlName propertyTag;
            org.radixware.schemas.xscml.Sqml.Item.TypifiedValue valueTag;
            for (Map.Entry<Id, Object> entry : propertyConditions.entrySet()) {
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
            return addintionalCondition==null ? condition : SqmlExpression.mergeConditions(addintionalCondition.asXsqml(), condition);
        }
        else{
            return addintionalCondition==null ? null : addintionalCondition.asXsqml();
        }
    }
    
    /**
     * Установка значения свойству модели группы.
     * После создания модели группы ее свойству будет передано указанное значение.
     * @param propertyId идентификатор свойства модели группы
     * @param value значение свойства модели группы
     */
    public final void setGroupPropertyValue(final Id propertyId, final Object value){
        if (propertyValues==null){
            propertyValues = new HashMap<>(8);
        }
        propertyValues.put(propertyId, value);
    }
    
    /**
     * Отмена установки значения свойству группы.
     * Метод позволяет отменить установку значения указанного свойству группы, запланированную в методе {@link #setGroupPropertyValue(Id, Object) }.
     * @param propertyId идентификатор свойства модели группы
     */
    public final void removeGroupPropertyValue(final Id propertyId){
        if (propertyValues!=null){
            propertyValues.remove(propertyId);
        }
    }
    
    /**
     * Отмена установки значений всех свойств группы.
     * Метод позволяет отменить установку значений всех свойств группы, запланированную в методе {@link #setGroupPropertyValue(Id, Object) }.
     */
    public final void clearGroupPropertyValues(){
        propertyValues = null;
    }    
    
    /**
     * Установка дополнительных ограничений модели группы.
     * Метод позволяет установить дополнительные ограничения, которые
     * будут переданы в модель группы один раз после ее создания.
     * @param restrictions ограничения модели группы
     */
    public final void setAddintionalRestrictions(final Restrictions restrictions){
        addintionalRestrictions = restrictions;
    }
    
    /**
     * Получение дополнительных ограничений модели группы.
     * Возвращает ограничения установленные в методе {@link #setAddintionalRestrictions(Restrictions) setAddintionalRestrictions}.
     * @return ограничения в модели группы.
     */
    public final Restrictions getAddintionalRestrictions(){
        return addintionalRestrictions;
    }
    
    /**
     * Установка пиктограммы.
     * Метод позволяет установить пиктограмму, которая будет использоваться при отображении узлов древовидного селектора, соответствующих
     * объектам группы.
     * @param icon пиктограмма.
     */
    public final void setObjectIcon(final Icon icon){
        this.icon = icon;
    }
    
    /**
     * Получение пиктограммы.
     * Метод позволяет получить пиктограмму, которая будет использоваться при отображении узла древовидного селектора, соответствующего
     * указанному объекту сущности.
     * Если объект сущности переданный в параметре метода принадлежит модели группы, созданной по данным параметрам, то метод возвращает
     * пиктограмму, установленную в setObjectIcon, иначе метод возвращает <code>null</code>.
     * @param object объект сущности
     * @return пиктограмма
     */
    public final Icon getObjectIcon(final EntityModel object){
        if (object.getEntityContext() instanceof IContext.SelectorRow){
            final GroupModel parentGroupModel = 
                    ((IContext.SelectorRow)object.getEntityContext()).parentGroupModel;
            return groupModel==parentGroupModel ? icon : null;
        }
        return null;
    }
    
    /**
     * Установка идентификатора свойства-признака наличия объектов в модели группы.
     * Позволяет установить идентификатор свойства родительского объекта, по которому можно определить содержит ли
     * модель группы, создаваемая на основе данных параметров, хоть один объект сущности или является пустой.
     * Если свойство объекта, заданное указанным идентификатором является логическим, 
     * то модель группы содержит объекты сущности, если его значение равно <code>true</code> в других случаях модель группы считается пустой.
     * Если свойство объекта, заданное указанным идентификатором является целым числом, 
     * то модель группы содержит объекты сущности, если его значение больше нуля в других случаях модель группы считается пустой.
     * Если свойство объекта, заданное указанным идентификатором не является ни целым числом ни логическим типом,
     * то модель группы считается пустой если его значение равно <code>null</code>.
     * @param propertyId идентификатор свойства родительского объекта сущности
     */
    public final void setHasObjectsPropertyId(final Id propertyId){
        this.hasRecordsPropertyId = propertyId;
    }
    
    /**
     * Получение идентификатора свойства-признака наличия объектов в модели группы.
     * Возвращает идентификатор свойства, заданный в методе {@link #setHasObjectsPropertyId(Id) setHasObjectsPropertyId}.
     * @return идентификатор свойства
     */
    public Id getHasObjectsPropertyId(){
        return hasRecordsPropertyId;
    }

    /**
     * Изменение признака наличия объектов сущности в модели группы.
     * Метод позволяет установить признак того, что модель группы заданная данными параметрами является пустой.
     * @param isEmpty признак отсутствия объектов сущности в модели группы
     */
    public final void setGroupIsEmpty(final boolean isEmpty){
        groupIsEmpty = isEmpty;
    }

    /**
     * Получение информации о наличии объектов в модели группы.
     * Метод возвращает признак того, что модель группы, заданная данными параметрами, не содержит ни одного объекта сущности.
     * Данный метод используется в древовидном селекторе и позволяет без создания и чтения модели группы определить 
     * является ли узел, соответствующий указанному объекту сущности, раскрываемым или нет.
     * Стандартная реализация возвращает результат на основе настроек, 
     * заданных методами {@link #setHasObjectsPropertyId(Id) setHasObjectsPropertyId} и {@link #setGroupIsEmpty(boolean) setGroupIsEmpty}.
     * Если ранее вызовом метода setHasObjectsPropertyId был установлен идентификатор свойства родительского объекта, 
     * то результат работы метода зависит от значения этого свойства, иначе 
     * метод возвращает значение переданное в параметре {@link #setGroupIsEmpty(boolean) setGroupIsEmpty} (по умолчанию <code>false</code>).
     * @param parentEntityModel родительский объект сущности
     * @return признак отсутствия объектов в модели группы
     */
    public boolean groupIsEmpty(final EntityModel parentEntityModel){
        if (hasRecordsPropertyId!=null && parentEntityModel!=null){
            if (hasRecordsPropertyValue==null){
                try{
                    final Property property = parentEntityModel.getProperty(hasRecordsPropertyId);
                    if (property instanceof PropertyInt) {
                        final Object value = property.getValueObject();
                        hasRecordsPropertyValue = value == null ? false : ((Long) value).longValue() > 0;
                    } else if (property instanceof PropertyBool) {
                        final Object value = property.getValueObject();
                        hasRecordsPropertyValue = value == null ? false : ((Boolean) value);
                    }
                    else{
                        hasRecordsPropertyValue = property.getValueObject()!=null;
                    }
                }
                catch(Exception ex){
                    final IClientEnvironment env = parentEntityModel.getEnvironment();
                    final String title = env.getMessageProvider().translate("ExplorerException", "Can't get information about children of object \'%s\'");
                    env.getTracer().error(String.format(title, parentEntityModel.getTitle()), ex);
                    env.getTracer().put(EEventSeverity.DEBUG, String.format(title, parentEntityModel.getTitle(), ClientException.exceptionStackToString(ex)), EEventSource.EXPLORER);
                    hasRecordsPropertyValue = false; 
                }
            }
            return !hasRecordsPropertyValue.booleanValue();
        }
        return groupIsEmpty;
    }
    
    /**
     * Изменение ограничения на создание объекта сущности.
     * Метод позволяет изменить признак того, что модель группы, созданную по данным параметрам, можно использовать для создания новых объектов.
     * @param canCreate признак возможности использования модели группы для создания объекта
     */
    public final void setCanCreateObject(final boolean canCreate){
        canCreateObject = canCreate;
    }
    
    /**
     * Получение информации о возможности выполнения операции создания объекта сущности.
     * Метод возвращает признак того, что модель группы, созданную по данным параметрам, можно использовать для создания новых объектов сущности.
     * Возвращает значение, установленное в методе {@link #setCanCreateObject(boolean) setCanCreateObject} (значение по умолчанию равно <code>true</code>).
     * Используется в древовидном селекторе для определения возможности выполнения операции создания подузла.
     * @return признак возможности использования модели группы для создания объекта.
     */
    public boolean canCreateObject(){
        return canCreateObject;
    }

    /**     
     * Установка начального значения свойства нового объекта сущности.
     * Значение будет установлено при выполнении операции создания нового объекта сущности 
     * в модели группы перед показом стандартного диалога создания.
     * @param propertyId идентификатор свойства
     * @param value значение свойства
     */
    public final void setPropertyValueForNewObject(final Id propertyId, final Object value){
        if (newObjectProperties==null){
            newObjectProperties = new HashMap<>();
        }
        newObjectProperties.put(propertyId, value);
    }

    /**
     * Установка набора начальных значений свойств нового объекта сущности.
     * Значения будут установлены при выполнении операции создания нового объекта сущности 
     * в модели группы перед показом стандартного диалога создания.
     * @param propertyValues карта соответствия идентификатора свойства его значению.
     */
    public final void setPropertyValuesForNewObject(final Map<Id,Object> propertyValues){
        if (propertyValues!=null){
            newObjectProperties = new HashMap<>(propertyValues);
        }
    }
    
    /**
     * Очистка набора начальных значений свойств нового объекта сущности.
     * Метод позволяет отменить установку начальных значений свойств нового объекта сущности, 
     * заданных в методах {@link #setPropertyValueForNewObject(Id, Object) setPropertyValueForNewObject} и 
     * {@link #setPropertyValuesForNewObject(Map)  setPropertyValuesForNewObject}.
     */
    public final void clearPropertyValuesForNewObject(){
        newObjectProperties = null;
    }
    
    /**
     * Получение начального значения свойства нового объекта сущности.
     * Метод позволяет получить начальное значение свойства с указанным идентификатором, которое было задано в 
     * методе {@link #setPropertyValueForNewObject(Id, Object) setPropertyValueForNewObject} или {@link #setPropertyValuesForNewObject(Map)  setPropertyValuesForNewObject}.
     * Если значение свойства с указанным идентификатором не было задано, то метод возвращает <code>null</code>.
     * @param propertyId идентификатор свойства
     * @return начальное значение свойства
     */
    public Object getNewObjectPropertyValue(final Id propertyId){
        return newObjectProperties==null ? null : newObjectProperties.get(propertyId);
    }

    /**
     * Заполнение свойств нового объекта сущности начальными значениями.
     * Метод устанавлмвает, заданные ранее в методах {@link #setPropertyValueForNewObject(Id, Object) setPropertyValueForNewObject} и 
     * {@link #setPropertyValuesForNewObject(Map)  setPropertyValuesForNewObject}, значения свойств переданному объекта сущности.
     * Вызывается в древовидном селекторе при выполнении операции создания подузла, ассоциированного с объектом сущности,
     * до показа стандартного диалога создания объекта.
     * @param newObject новый объект сущности
     */
    public final void fillNewObjectPropertyValues(final EntityModel newObject){
        if (newObjectProperties!=null && newObject.getContext() instanceof IContext.InSelectorCreating){
            final GroupModel parentGroupModel = ((IContext.InSelectorCreating)newObject.getContext()).group;
            if (parentGroupModel==groupModel){
                for (Map.Entry<Id,Object> propertyValue: newObjectProperties.entrySet()){
                    try{
                        newObject.getProperty(propertyValue.getKey()).setValueObject(propertyValue.getValue());
                    }
                    catch(Exception exception){
                        final IClientEnvironment environment = newObject.getEnvironment();
                        final String title = environment.getMessageProvider().translate("ExplorerException", "Can't set value to property #%s of new object '%s'");
                        environment.getTracer().error(String.format(title,propertyValue.getKey().toString(),
                                                                   newObject.getTitle()), exception);
                        environment.getTracer().put(EEventSeverity.DEBUG,
                                String.format(title, propertyValue.getKey().toString(), newObject.getTitle())+":\n"+
                                ClientException.exceptionStackToString(exception),
                                EEventSource.EXPLORER);                
                        continue;                        
                    }
                }
            }
        }
    }
            
    /**
     * Получение модели группы, созданной по данным настройкам.
     * Создает инстанцию модели группы (если она еще не была создана) по заданным параметрам вызовом метода {@link #createChildGroupModel(EntityModel) createChildGroupModel}.
     * Если в конструкторе был задан идентификатор элемента проводника, то для получения инстанции модели группы будет использоваться переданная 
     * модель сущности: у нее будет вызван метод {@link Model#getChildModel(Id)  getChildModel}. 
     * После создания модели группы ей устанавливается условие, сформированное в методе {@link #calcFinalCondition(RadClassPresentationDef) calcFinalCondition}, 
     * значения свойств, переданные в методе {@link #setGroupPropertyValue(Id, Object) } и ограничения, переданные в методе {@link #setAddintionalRestrictions(Restrictions) setAddintionalRestrictions}.
     * Если метод {@link #canCreateObject() canCreateObject} возвращает <code>false</code>, то к ограничениям группы добавляется еще ограничение на создание нового объекта сущности.
     * После создания и настройки ссылка на модель группы сохраняется и при повторном вызове метода возвращается ее значение.
     * @param parentEntityModel объект сущности
     * @return инстанция модели группы
     * @throws ServiceClientException генерируется при возникновении ошибки во время выполнения операции получения инстанции модели группы, связанной с взаимодействием с сервером
     * @throws InterruptedException генерируется при отмене операции получения инстанции модели группы
     */
    public final GroupModel getChildGroupModel(final EntityModel parentEntityModel) throws ServiceClientException, InterruptedException{
        if (groupModel==null){
            groupModel = createChildGroupModel(parentEntityModel);
            updateGroupModel();
        }
        return groupModel;
    }
    
    /**
     * Получение идентификатора элемента проводника.
     * Возвращает идентификатор элемента проводника, переданный в конструкторе.
     * Метод используется в древовидном селекторе для проверки доступности элемента проводника в контексте конкретного объекта сущности.
     * @return идентификатор элемента проводника или <code>null</code>, если идентификатор не был задан.
     */
    public final Id getExplorerItemId(){
        return explorerItemId;
    }
    
    /**
     * Cоздание копии инстанции.
     * @return новая инстанция - копия данной.
     */
    public ChildGroupModelSettings createCopy(){
        return new ChildGroupModelSettings(this);
    }

    /**
     * Создание инстанции модели группы.
     * Создает инстанцию модели группы по заданным параметрам.
     * Если в конструкторе был задан идентификатор элемента проводника, то для получения инстанции модели группы будет использоваться переданная 
     * модель сущности: у нее будет вызван метод {@link Model#getChildModel(Id) getChildModel}, иначе будет создана бесконтекстная модель группы по заданныой презентации селектора.
     * @param parentEntityModel объект сущности
     * @return новая инстанция модели группы
     * @throws ServiceClientException генерируется при возникновении ошибки во время выполнения операции получения инстанции модели группы, связанной с взаимодействием с сервером
     * @throws InterruptedException генерируется при отмене операции получения инстанции модели группы
     */
    protected GroupModel createChildGroupModel(final EntityModel parentEntityModel) throws ServiceClientException, InterruptedException{        
        if (explorerItemId!=null){
            return (GroupModel)parentEntityModel.getChildModel(explorerItemId);
        }
        else if (classId!=null && selectorPresentationId!=null){
            return GroupModel.openTableContextlessSelectorModel(parentEntityModel, classId, selectorPresentationId);
        }
        return null;
    }
            
    private void updateGroupModel() throws ServiceClientException, InterruptedException{
        if (groupModel!=null){
            if (addintionalRestrictions!=null){
                groupModel.getRestrictions().add(addintionalRestrictions);
                if (!canCreateObject()){
                    groupModel.getRestrictions().setCreateRestricted(true);
                }
            }
            if (propertyValues!=null){
                for (Map.Entry<Id,Object> property: propertyValues.entrySet()){
                    groupModel.getProperty(property.getKey()).setValueObject(property.getValue());
                }
            }
            final org.radixware.schemas.xscml.Sqml condition = 
                calcFinalCondition(groupModel.getSelectorPresentationDef().getClassPresentation());
            if (condition!=null){
                groupModel.setCondition(condition);
            }
        }
    }
    
    static List<GroupModel> createChildGroupModels(final List<ChildGroupModelSettings> settings, final EntityModel parentEntityModel){
            final List<GroupModel> groups = new LinkedList<>();
            try{
                for (ChildGroupModelSettings groupSettings: settings){
                    if ((!groupSettings.groupIsEmpty(parentEntityModel) || groupSettings.canCreateObject()) && checkGroupModelSettings(groupSettings, parentEntityModel)){
                        try{
                            groups.add(groupSettings.getChildGroupModel(parentEntityModel));
                        }
                        catch (ServiceClientException exception){
                            final String title = parentEntityModel.getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model for parent object \'%s\'");
                            parentEntityModel.getEnvironment().getTracer().error(String.format(title,
                                                                            parentEntityModel.getTitle()), exception);
                            parentEntityModel.getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                                    String.format(title, parentEntityModel.getTitle())+":\n"+
                                    ClientException.exceptionStackToString(exception),
                                    EEventSource.EXPLORER);                
                            continue;                    
                        }
                    }
                }
            }
            catch(InterruptedException exception){
                return groups;
            }
            return groups;        
    }
    
    private static boolean checkGroupModelSettings(final ChildGroupModelSettings settings, final EntityModel parentEntityModel) throws InterruptedException{
        final Id explorerItemId = settings.getExplorerItemId();
        if (explorerItemId==null){
            return true;
        }
        else{
            final boolean isAccessible;
            try{
                isAccessible = 
                    parentEntityModel.isExplorerItemAccessible(settings.getExplorerItemId());
            }
            catch(ServiceClientException exception){
                final String title = parentEntityModel.getEnvironment().getMessageProvider().translate("ExplorerException", "Error on creating child group model by explorer item #%s for parent object \'%s\'");
                parentEntityModel.getEnvironment().getTracer().error(String.format(title,
                                                                explorerItemId.toString(),
                                                                parentEntityModel.getTitle()), exception);
                parentEntityModel.getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(title, explorerItemId.toString(), parentEntityModel.getTitle())+":\n"+
                        ClientException.exceptionStackToString(exception),
                        EEventSource.EXPLORER);
                return false;
            }
            if (!isAccessible){
                final String message = parentEntityModel.getEnvironment().getMessageProvider().translate("TraceMessage", "Explorer item #%s is not accessible for parent object \'%s\'");
                parentEntityModel.getEnvironment().getTracer().put(EEventSeverity.DEBUG,
                        String.format(message, explorerItemId.toString(), parentEntityModel.getTitle()),
                        EEventSource.EXPLORER);
            }
            return isAccessible;
        }
    }    
}