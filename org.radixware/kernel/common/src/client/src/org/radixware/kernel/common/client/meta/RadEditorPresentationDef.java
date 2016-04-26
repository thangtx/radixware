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
package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchNameError;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;

/**
 * Класс дефиниции презентации редактора. Содержит настройки, определяющие
 * внешний вид диалога редактирования объекта сущности
 * <ul>
 * <li>{@link RadEditorPages Набор дефиниций страниц редактора}
 * <li>{@link RadExplorerItems Дочерние элементы проводника}
 * <li>{@link RadPropertyPresentationAttributes Перекрытые презентационные атрибуты свойства}
 * </ul>
 * <p>
 * Эти настройки могут быть явно определены в данной презентации редактора или
 * наследоваться из базовой. Также данный класс предоставляет доступ к
 * дефинициям свойств, объевленных в классе сущности, к которому относится
 * данная презентация. Конструктор вызывается в классе, сгенерированном
 * radix-дизайнером.
 *
 * @see EntityModel
 * @see org.radixware.kernel.common.client.views.IEditor
 */
public class RadEditorPresentationDef extends RadPresentationDef implements IEditorPagesHolder, IExplorerItemsHolder {

    private final static long EDITOR_PRESENTATION_RESTRICTIONS
            = ERestriction.toBitField(EnumSet.of(ERestriction.CREATE, ERestriction.UPDATE, ERestriction.DELETE, ERestriction.ANY_COMMAND));

    /**
     * Страницы редактора, определенные в данной презентации. Заполняется в
     * конструкторе
     */
    private final RadEditorPageDef[] pages;

    /**
     * Идентификаторы и уровни вложенности всех страниц редактора (с учетом
     * наследования), расположенные в порядке развертывания в глубину дерева
     * страниц.
     */
    private RadEditorPages.PageOrder[] pageOrder;
    private RadEditorPages editorPages = null;

    /**
     * Получение набора страниц редактора. Метод возвращает конечный (с учетом
     * наследования) набор дефиниций страниц (вкладок), которые должны быть
     * показаны в редакторе объекта сущности, открытом по данной презентации.
     *
     * @return набор страниц редактора. Не может быть <code>null</code>
     */
    @Override
    public final RadEditorPages getEditorPages() {
        if (editorPages == null) {
            if (this.inheritanceMask.isPagesInherited() && getBasePresentation() != null) {
                editorPages = getBasePresentation().getEditorPages();
            } else {
                editorPages = new RadEditorPages(pageOrder, this);
            }
        }
        return editorPages;
    }

    /**
     * Поиск дефиниции страницы редактора. Метод осуществляет поиск по
     * указанному идентификатору страницы редактора, которая была объявлена в
     * данной презентации или в одной из базовых.
     *
     * @param pageId идентификатор страницы редактора
     * @return дефиниция страницы редактора или <code>null</code>, если не
     * найдена
     */
    @Override
    public RadEditorPageDef findEditorPageById(final Id pageId) {
        RadEditorPresentationDef presentation = this;
        while (presentation != null) {
            if (presentation.pages != null && presentation.pages.length > 0) {
                for (RadEditorPageDef page : presentation.pages) {
                    if (page.getId().equals(pageId)) {
                        return page;
                    }
                }
            }
            presentation = presentation.getBasePresentation();
        }
        return null;
    }

    private final Object propertyAttributesSem = new Object();
    private final RadPropertyPresentationAttributes[] propertyAttributes;
    private final Map<Id, Map<Id, RadPropertyPresentationAttributes>> linkedPropertyAttributes = new HashMap<>();

    /**
     * Получение презентационных атрибутов свойства. Метод возвращает конечный
     * (с учетом всех линий наследования) набор настроек редактора значения
     * свойства, идентификатор которого передается в качестве параметра. Если в
     * контексте данной презентации редактора не удается найти
     * {@link RadPropertyDef дефиницию свойства}, то метод генерирует
     * исключение.
     *
     * @param propertyId идентификатор свойства
     * @param contextClassDef дефиниция класса, которому принадлежит свойство
     * @return набор настроек редактора свойства. Не может быть
     * <code>null</code>.
     * @throws NoDefinitionWithSuchIdError генерируется, если в контексте данной
     * презентации редактора не удается найти дефиницию свойства
     */
    public final RadPropertyPresentationAttributes getPropertyAttributesByPropId(final Id propertyId, final RadClassPresentationDef contextClassDef) {
        synchronized (propertyAttributesSem) {
            RadPropertyPresentationAttributes attributes = null;
            Map<Id, RadPropertyPresentationAttributes> propertyAttributesMap
                    = linkedPropertyAttributes.get(contextClassDef.getId());
            if (propertyAttributesMap == null) {
                propertyAttributesMap = new HashMap<>();
                linkedPropertyAttributes.put(contextClassDef.getId(), propertyAttributesMap);
                if (propertyAttributes != null && propertyAttributes.length > 0) {
                    for (RadPropertyPresentationAttributes propAttributes : propertyAttributes) {
                        final RadPropertyPresentationAttributes linkedAttributes
                                = new RadPropertyPresentationAttributes(propAttributes, this, contextClassDef);
                        propertyAttributesMap.put(propAttributes.getPropertyId(), linkedAttributes);
                        if (propertyId.equals(propAttributes.getPropertyId())) {
                            attributes = linkedAttributes;
                        }
                    }
                    if (attributes != null) {
                        return attributes;
                    }
                }
            }
            attributes = propertyAttributesMap.get(propertyId);
            if (attributes == null) {
                if (inheritanceMask.isPresentationPropertyAttributesInherited()
                        && getBasePresentation() != null
                        && getBasePresentation().isPropertyDefExistsById(propertyId)) {
                    return getBasePresentation().getPropertyAttributesByPropId(propertyId, contextClassDef);
                } else {
                    return new RadPropertyPresentationAttributes(contextClassDef.getPropertyDefById(propertyId));
                }
            }
            return attributes;
        }
    }

    /**
     * Метод возвращает метаописание свойства по идентификатору. Поиск свойства
     * производится в классе, которому принадлежит данная презентация и во всех
     * предках этого класса. Если свойство не найдено генерируется ошибка
     * NoDefinitionWithSuchIdError
     *
     * @param propertyId - идентификатор свойства
     * @return презентационные атрибуты свойства с указанным идентификатором. Не
     * могут быть <code>null</code>
     * @throws NoDefinitionWithSuchIdError генерируется, если в контексте данной
     * презентации редактора не удается найти соответствующую дефиницию свойства
     */
    @Override
    public final RadPropertyDef getPropertyDefById(final Id propertyId) {
        return getClassPresentation().getPropertyDefById(propertyId);
    }

    /**
     * Метод возвращает метаописание свойства по имени. Поиск свойства
     * производится в классе, которому принадлежит данная презентация и во всех
     * предках этого класса. Если свойство не найдено генерируется ошибка
     * NoDefinitionWithSuchNameError
     *
     * @param propertyName - наименование свойства
     * @return презентационные атрибуты свойства с указанным именем. Не могут
     * быть <code>null</code>
     * @throws NoDefinitionWithSuchNameError генерируется, если в контексте
     * данной презентации редактора не удается найти соответствующую дефиницию
     * свойства
     */
    public final RadPropertyDef getPropertyByName(final String propertyName) {
        return getClassPresentation().getPropertyDefByName(propertyName);
    }

    /**
     * Проверка на существование дефиниции свойства с указанным идентификатором.
     * Поиск свойства производится в классе, которому принадлежит данная
     * презентация и во всех предках этого класса.
     *
     * @param propertyId идентификатор свойства
     * @return <code>true</code>, если в контексте данной презентации редактора
     * найдена дефиниция свойства и <code>false</code> в противном случае
     */
    @Override
    public final boolean isPropertyDefExistsById(final Id propertyId) {
        return getClassPresentation().isPropertyDefExistsById(propertyId);
    }

    /**
     * Проверка на существование дефиниции свойства с указанным именем. Поиск
     * свойства производится в классе, которому принадлежит данная презентация и
     * во всех предках этого класса.
     *
     * @param propertyName наименование свойства
     * @return <code>true</code>, если в контексте данной презентации редактора
     * найдена дефиниция свойства и <code>false</code> в противном случае
     */
    public final boolean isPropertyExistsByName(final String propertyName) {
        return getClassPresentation().isPropertyDefExistsByName(propertyName);
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadEditorPresentationDef(final Id id,
            final String name,
            final ERuntimeEnvironmentType type,
            final Id basePresentationId,
            final Id classId,
            final Id tableId,
            final Id titleId,
            final Id iconId,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadExplorerItemDef[] explorerItems,
            final RadExplorerItemsSettings[] explorerItemsSettings,
            final Id[] contextlesCommandsIds,
            final long restrictionsMask,
            final Id[] enabledCommandIds,
            final RadPropertyPresentationAttributes[] presPropertyAttributes,
            final long inheritanceMask,
            final int sizeX,
            final int sizeY) {
        this(id, name, type, basePresentationId, classId, tableId, titleId, iconId,
                editorPages, pageOrder, explorerItems, explorerItemsSettings,
                contextlesCommandsIds, restrictionsMask, enabledCommandIds,
                presPropertyAttributes, inheritanceMask, sizeX, sizeY, null);
    }

    /**
     * Конструктор дефиниции презентации редактора. Вызвается в коде,
     * сгенерированном в radix-дизайнере.
     *
     * @param id идентификатор презентации редактора
     * @param name наименование презентации редактора
     * @param type тип среды выполнения, для которой описана данная презентация
     * @param basePresentationId идентификатор базовой презентации редактора
     * @param classId идентификатор класса, к которому относится данная
     * презентация редактора
     * @param tableId идентификатор таблицы, отображение которой описывает
     * данная презентация
     * @param titleId идентификатор мультиязычной строки, являющийся заголовком
     * данной презентации редактора
     * @param iconId идентификатор пиктограммы, связанной с данной презентацией
     * редактора
     * @param editorPages набор дефиниций страниц редактирования, описанных в
     * данной презентации редактора
     * @param pageOrder порядок отображения страниц редактирования в данной
     * презентации
     * @param explorerItems набор дочерних элементов проводника, описанных в
     * данной презентации редактора
     * @param explorerItemsSettings массив настроек отображения дочерних
     * элементов проводника
     * @param contextlesCommandsIds набор идентификаторов бесконтекстных команд,
     * запуск которых может быть произведен из диалога, созданного по данной
     * презентации
     * @param restrictionsMask набор ограничений на действия, которые можно
     * производить в контексте данной презентации
     * @param enabledCommandIds набор идентификаторов контекстных команд, запуск
     * которых разрешен в данной презентации
     * @param presPropertyAttributes набор презентационных атрибутов свойств,
     * перекрытых в презентации
     * @param inheritanceMask маска наследования атрибутов из базовой
     * презентации
     */
    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadEditorPresentationDef(final Id id,
            final String name,
            final ERuntimeEnvironmentType type,
            final Id basePresentationId,
            final Id classId,
            final Id tableId,
            final Id titleId,
            final Id iconId,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadExplorerItemDef[] explorerItems,
            final RadExplorerItemsSettings[] explorerItemsSettings,
            final Id[] contextlesCommandsIds,
            final long restrictionsMask,
            final Id[] enabledCommandIds,
            final RadPropertyPresentationAttributes[] presPropertyAttributes,
            final long inheritanceMask,
            final int sizeX,
            final int sizeY,
            final Id[] explicitItems) {
        super(id,
                name,
                type,
                basePresentationId,
                classId,
                tableId,
                titleId,
                iconId,
                contextlesCommandsIds,
                fixRestrictionsMask(restrictionsMask),
                enabledCommandIds,
                inheritanceMask,
                sizeX,
                sizeY);
        this.pages = editorPages;
        this.pageOrder = pageOrder;
        this.explorerItems = explorerItems;
        propertyAttributes = presPropertyAttributes;
        this.explicitItems = explicitItems;
        if (explorerItemsSettings != null) {
            for (RadExplorerItemsSettings itemsSettings : explorerItemsSettings) {
                childExplorerItemSettingsByParentId.put(itemsSettings.getParentExplorerItemId(), itemsSettings);
            }
        }
    }

    public RadEditorPresentationDef(final Id id,
            final String name,
            final ERuntimeEnvironmentType type,
            final Id basePresentationId,
            final Id classId,
            final Id tableId,
            final Id titleId,
            final Id iconId,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadExplorerItemDef[] explorerItems,
            final RadExplorerItemsSettings[] explorerItemsSettings,
            final Id[] contextlesCommandsIds,
            final long restrictionsMask,
            final Id[] enabledCommandIds,
            final RadPropertyPresentationAttributes[] presPropertyAttributes,
            final long inheritanceMask) {
        this(id,
                name,
                type,
                basePresentationId,
                classId,
                tableId,
                titleId,
                iconId,
                editorPages,
                pageOrder,
                explorerItems,
                explorerItemsSettings,
                contextlesCommandsIds,
                restrictionsMask,
                enabledCommandIds,
                presPropertyAttributes,
                inheritanceMask,
                0,
                0, null);
    }

    private static long fixRestrictionsMask(final long rawRestrictions) {
        return EDITOR_PRESENTATION_RESTRICTIONS & rawRestrictions;
    }

    /**
     * Проверка доступности команды. Метод возвращает <code>true</code> когда
     * указанная дефиниция является описанием команды у свойства или объекта
     * сущности.
     *
     * @param command дефиниция команды
     * @return <code>true</code>, если команда предназначена для свойства или
     * объекта сущности и <code>false</code> в противном случае.
     */
    @Override
    protected boolean isCommandEnabled(final RadPresentationCommandDef command) {
        return command.scope == ECommandScope.OBJECT || command.scope == ECommandScope.PROPERTY;
    }

    /**
     * Создание модели объекта сущности. Метод создает инстанцию модели объекта
     * сущности при помощи вызова метода
     * {@link #createModelImpl(org.radixware.kernel.common.client.IClientEnvironment)}
     * и назначает ей переданный контекст.
     *
     * @param context контекст модели
     * @return инстанция модели объекта. Не может быть <code>null</code>
     * @throws ModelCreationError во время создания модели произошла ошибка
     */
    @Override
    public EntityModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model model = createModelImpl(context.getEnvironment());
        model.setContext(context);
        return (EntityModel) model;
    }

    /**
     * Создание модели объекта сущности. Метод находит класс модели объекта
     * сущности для данной презентации на основе ее идентификатора и создает
     * инстанцию этого класса. Может быть перекрыт в коде, сгенерированном в
     * radix-дизайнере.
     *
     * @param environment инстанция среды выполнения. Не может быть
     * <code>null</code>
     * @return инстанция модели объекта. Не может быть <code>null</code>
     * @throws ModelCreationError во время создания модели произошла ошибка
     * @see
     * DefManager#getDefinitionModelClass(org.radixware.kernel.common.types.Id)
     */
    @SuppressWarnings("UseSpecificCatch")
    protected Model createModelImpl(final IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_ENTITY_MODEL_CLASS);
        try {
            Class<Model> classModel = environment.getDefManager().getDefinitionModelClass(modelClassId);
            final Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, RadEditorPresentationDef.class);
            return constructor.newInstance(environment, this);
        } catch (Exception e) {
            throw new ModelCreationError(ModelCreationError.ModelType.ENTITY_MODEL, this, null, e);
        }
    }

    /**
     * Создание инстанции стандартного диалога редактирования объекта сущности.
     *
     * @param environment инстанция среды выполнения. Не может быть
     * <code>null</code>
     * @return инстанция диалога редактирования объекта сущности. Не может быть
     * <code>null</code>
     */
    @Override
    public final IView createStandardView(final IClientEnvironment environment) {
        return getApplication().getStandardViewsFactory().newStandardEditor(environment);
    }

    @Override
    public boolean hasTitle() {
        if (inheritanceMask.isTitleInherited()) {
            if (getBasePresentation() != null && classId.equals(getBasePresentation().classId)) {
                return getBasePresentation().hasTitle();
            } else {
                return getClassPresentation().hasObjectTitle();
            }
        }
        return super.hasTitle();
    }

    /**
     * Получение заголовка презентации. Метод возвращает заголовок презентации
     * (с учетом наследования). Если заголовок не задан, то метод возвращает
     * строку <code>"<No Title>"</code>.
     *
     * @return заголовок презентации. Не может быть <code>null</code>
     */
    @Override
    public String getTitle() {
        if (inheritanceMask.isTitleInherited()) {
            if (getBasePresentation() != null && classId.equals(getBasePresentation().classId)) {
                return getBasePresentation().getTitle();
            } else if (super.hasTitle()) { //inherited from previous layer: RADIX-2093
                return super.getTitle();
            } else {
                return getClassPresentation().getObjectTitle();
            }
        }
        return super.getTitle();
    }

    private RadEditorPresentationDef basePres = null;
    private final Object basePresSem = new Object();

    /**
     * Получение дефиниции базовой презентации редактора.
     *
     * @return дефиниция базовой презентации редактора или <code>null</code>,
     * если не задана.
     */
    @Override
    protected RadEditorPresentationDef getBasePresentation() {
        synchronized (basePresSem) {
            if (basePres == null && basePresentationId != null) {
                basePres = getDefManager().getEditorPresentationDef(basePresentationId);
            }
            return basePres;
        }
    }

    /**
     * Получение строкового описания презентации
     *
     * @return описание презентации
     */
    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "editor presentation %s");
        return String.format(desc, super.getDescription());
    }

    PresentationInheritance getInheritanceMask() {
        return inheritanceMask;
    }

    //IExplorerItemsHolder implementation
    /**
     * Массив дочерних элементов проводника, определенных в данной презентации
     */
    private final RadExplorerItemDef[] explorerItems;
    private final Id[] explicitItems;
    private final Map<Id, RadExplorerItemsSettings> childExplorerItemSettingsByParentId = new HashMap<>();
    private RadExplorerItems childrenExplorerItems = null;

    /**
     * Получение набора дочерних элементов проводника. Метод возвращает конечный
     * (с учетом наследования) набор элементов проводника.
     *
     * @return набор элементов проводника. Не может быть <code>null</code>
     */
    @Override
    public final RadExplorerItems getChildrenExplorerItems() {
        if (childrenExplorerItems == null) {
            final RadExplorerItems inheritedEI;
            Id[] filterIds = null;
            if (inheritanceMask.isChildrenInherited() && getBasePresentation() != null) {
                inheritedEI = getBasePresentation().getChildrenExplorerItems();
            } else {
                if (explicitItems == null) {
                    inheritedEI = null;
                } else {
                    filterIds = explicitItems;
                    inheritedEI = getBasePresentation().getChildrenExplorerItems();
                }
            }
            if ((explorerItems == null || explorerItems.length == 0) && (inheritedEI == null || inheritedEI.isEmpty())) {
                childrenExplorerItems = RadExplorerItems.EMPTY;
            } else {
                final Id[] itemsOrder = getChildrenExplorerItemsOrder(getId());
                if (explorerItems == null || explorerItems.length == 0) {
                    if(getBasePresentation() != null){
                        if(inheritanceMask.isChildrenInherited()){
                            childrenExplorerItems = new RadExplorerItems(explorerItems, inheritedEI, itemsOrder);                            
                        }else{
                            childrenExplorerItems = new RadExplorerItems(explorerItems, inheritedEI, itemsOrder, filterIds);
                        }
                    }else{
                        childrenExplorerItems = RadExplorerItems.EMPTY;
                    }                    
                } else {
                    childrenExplorerItems = new RadExplorerItems(explorerItems, inheritedEI, itemsOrder, filterIds);
                }
            }
        }
        return childrenExplorerItems;
    }

    /**
     * Возвращает упорядоченный массив идентификаторов элементов проводника, в
     * составе дефиниции с указанным идентификатором. Реализация метода {@link IExplorerItemsHolder#getChildrenExplorerItemsOrder(Id)
     * }. Результат работы метода определяет порядок следования элементов
     * проводника в дереве. Метод получает порядок элементов, из
     * {@link RadExplorerItemsSettings настроек элементов проводника},
     * переданных в конструкторе. Если данная презентация не содержит настройки
     * упорядочивания элементов, то возвращается результат работы аналогичного
     * метода у базовой презентации (при ее отсутствии возвращается пустой
     * массив).
     *
     * @param parentId идентификатор элемента проводника верхнего уровня. Не
     * может быть <code>null<code>.
     * @return массив идентификаторов элементов проводника. Не может быть <code>null<code>.
     */
    @Override
    public Id[] getChildrenExplorerItemsOrder(final Id parentId) {
        final RadExplorerItemsSettings itemsSettings
                = childExplorerItemSettingsByParentId.get(parentId);
        if (itemsSettings == null || itemsSettings.getItemsOrder() == null) {
            final RadEditorPresentationDef basePresentation = getBasePresentation();
            if (basePresentation == null) {
                return new Id[]{};
            } else {
                final boolean isTopLevelItem = getId().equals(parentId);
                return basePresentation.getChildrenExplorerItemsOrder(isTopLevelItem ? basePresentation.getId() : parentId);
            }
        } else {
            return itemsSettings.getItemsOrder();
        }

    }

    /**
     * Возвращает признак того, что элемент проводника должен быть показан в
     * дереве. Реализация метода {@link IExplorerItemsHolder#isExplorerItemVisible(Id, Id)
     * }. Метод получает сведения о видимости элемента, из
     * {@link RadExplorerItemsSettings настроек элементов проводника},
     * переданных в конструкторе. Если данная презентация не содержит настройки
     * видимости для указанного элемента, то возвращается результат работы
     * аналогичного метода у базовой презентации (при ее отсутствии возвращается <code>true<code>).
     *
     * @param parentId идентификатор дефиниции верхнего уровня. Не может быть <code>null<code>.
     * @param explorerItemId идентификатор вложенного элемента проводника. Не
     * может быть <code>null<code>.
     * @return <code>true<code> если элемент проводника должен быть показан в дереве проводника и
     * <code>false<code> в противном случае
     */
    @Override
    public boolean isExplorerItemVisible(final Id parentId, final Id explorerItemId) {
        final boolean isTopLevelItem = getId().equals(parentId);
        for (RadEditorPresentationDef presentation = this; presentation != null; presentation = presentation.getBasePresentation()) {
            final RadExplorerItemsSettings itemsSettings
                    = presentation.childExplorerItemSettingsByParentId.get(isTopLevelItem ? presentation.getId() : parentId);
            if (itemsSettings != null && itemsSettings.isItemVisibilityDefined(explorerItemId)) {
                return itemsSettings.isItemVisible(explorerItemId);
            }
        }
        return true;//assume explorer item is visible by default
    }
}
