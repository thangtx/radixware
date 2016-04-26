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

import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchNameError;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * Презентационныe атрибуты базового или производного класса сущности.
 * Наследники генерируются дизайнером.
 * Предоставляет доступ к описаниям свойств, комманд, фильтров,
 * 	сортировок и цветовых схем, определенных в классе.
 */
public class RadClassPresentationDef extends NamedDefinition {

    /*
     *  В конструкторе сгенерированного наследника должны быть заполнены следующие поля:
     *  	properties	- массив свойств
     *  	commands - массив команд
     *		filters - массив фильтров
     *		sortings - массив сортировок
     *		colorSchemes - массив цветовых схем
     *		presentations - идентификаторы презентаций, объявленные в данном классе
     *  А также реализован метод getAncestorPresentationClassDef
     */
    private final RadClassPresentationDef baseClassDef;
    private final Id selPresentationId;
    /**
     * Список дочерних классов, у которых был вызван метод link
     * Нужен чтобы избежать повторной линковки.
     */
    private final List<Id> childClasses = new ArrayList<>();
    /**
     * THIS is presentation of Entity Class.
     * Some options (defaultSelectorPresId, restrictions, titles)
     * always takes from Entity Class Presentation
     */
    private final boolean isEntityClassPresentation;
    private final Restrictions restrictions;
    private final Id iconId;
    private final Id classTitleId;
    private final Id grpTitleId;
    private final Id objTitleId;
    private final Id baseClassId;
    private final boolean isClassCatalogExists;
    private final boolean isAuditEnabled;
    private final boolean isUserFunction;

    public RadClassPresentationDef(
            final Id id,
            final String name,
            final Id baseClassId,
            final Id iconId,
            final Id defaultSelectorPresentationId,
            final Id classTitleId,
            final Id grpTitleId,
            final Id objTitleId,
            final long restrictionsMask,
            final RadPropertyDef[] properties,
            final RadCommandDef[] commands,
            final RadFilterDef[] filters,
            final RadSortingDef[] sortings,
            final RadReferenceDef[] references,
            final Id[] presentationIds,
            final boolean isClassCatalogsDefined,
            final boolean isAuditEnabled,
            final boolean isUserFunction) {
        super(id, name);

        isEntityClassPresentation = id.getPrefix() == EDefinitionIdPrefix.ADS_ENTITY_CLASS;
        this.baseClassId = baseClassId;
        baseClassDef = getAncestorClassPresentationDef();
        if (isEntityClassPresentation) {
            this.selPresentationId = defaultSelectorPresentationId;
            this.grpTitleId = grpTitleId;
            this.objTitleId = objTitleId;
            this.classTitleId = objTitleId;
            this.iconId = iconId;
            this.restrictions = Restrictions.Factory.newInstance(ERestriction.fromBitField(restrictionsMask), null);
        } else {
            this.selPresentationId = null;
            this.grpTitleId = null;
            this.objTitleId = objTitleId;
            this.classTitleId = classTitleId;
            this.iconId = null;
            this.restrictions = null;
        }
        this.properties = properties;
        this.commands = commands;
        this.filters = filters;
        this.sortings = sortings;
        this.references = references;
        this.presentations = presentationIds;
        isClassCatalogExists = isClassCatalogsDefined;
        this.isAuditEnabled = isAuditEnabled;
        this.isUserFunction = isUserFunction;
    }

    /**
     * @return Презентационные настройки базового класса или
     * NULL, если данный класс ни от кого не наследуется
     *
     * Метод должен быть перекрыт в сгенерированном наследнике
     *  return org.radixware.kernel.explorer.Environment.defManager.getPresentationClassDef(baseClassDefId);
     */
    protected final RadClassPresentationDef getAncestorClassPresentationDef() {
        if (baseClassId == null) {
            return null;
        } else {
            return getDefManager().getClassPresentationDef(baseClassId);
        }
    }

    /**
     * Метод  регестрирует в базовом классе презентации, определенные в данном классе.
     * Вызывается в конструкторе.
     * @see getDefinedPresentationIDs
     */
    private void link() {
        if (presentations != null && presentations.length > 0) {
            presentationIds = new ArrayList<>(presentations.length);
            presentationIds.addAll(Arrays.asList(presentations));
        } else {
            presentationIds = new ArrayList<>(0);
        }
        if (baseClassDef != null) {
            baseClassDef.linkChildClass(this);
        }
    }

    private void linkChildClass(RadClassPresentationDef presentationClass) {
        if (!childClasses.contains(presentationClass.getId())) {
            childClasses.add(presentationClass.getId());
            presentationIds.addAll(presentationClass.getDefinedPresentationIDs());
            if (baseClassDef != null) {
                baseClassDef.linkChildClass(presentationClass);
            }
        }
    }
    //Свойства:
    protected RadPropertyDef[] properties;
    private Map<Id, RadPropertyDef> propertiesById = null;
    private Map<String, RadPropertyDef> propertiesByName = null;
    private List<RadPropertyDef> linkedProperties = null;

    /**
     * Метод возвращает метоописание свойства по идентификатору.
     * Поиск свойства производится в данном классе и во всех его предках.
     * Если свойство не найдено генерируется ошибка NoDefinitionWithSuchIdError
     * @param propertyId - идентификатор свойства
     * @return Презентационные атрибуты свойства с идентификатором propertyId
     */
    public final RadPropertyDef getPropertyDefById(final Id propertyId) {
        if (propertiesById == null) {
            fillPropertiesById();
        }
        if (propertiesById.containsKey(propertyId)) {
            return propertiesById.get(propertyId);
        }
        if (baseClassDef != null) {
            return baseClassDef.getPropertyDefById(propertyId);
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, propertyId);
    }

    /**
     * Метод возвращает метоописание свойства по имени.
     * Поиск свойства производится в данном классе и во всех его предках.
     * Если свойство не найдено генерируется ошибка NoDefinitionWithSuchNameError
     * @param propertyId - идентификатор свойства
     * @return Презентационные атрибуты свойства с именем propertyName
     */
    public final RadPropertyDef getPropertyDefByName(final String propertyName) {
        if (propertiesByName == null) {
            fillPropertiesByName();
        }
        if (propertiesByName.containsKey(propertyName)) {
            return propertiesByName.get(propertyName);
        }
        if (baseClassDef != null) {
            return baseClassDef.getPropertyDefByName(propertyName);
        }
        throw new NoDefinitionWithSuchNameError(this, NoDefinitionWithSuchNameError.SubDefinitionType.PROPERTY, propertyName);
    }

    /**
     * Метод проверяет существует ли метоописание свойства с заданным идентификатором.
     * Поиск свойства производится в данном классе и во всех его предках.
     * @param propertyId - идентификатор свойства
     * @return true если описание свойства найдено
     */
    public final boolean isPropertyDefExistsById(final Id propertyId) {
        if (propertiesById == null) {
            fillPropertiesById();
        }
        if (propertiesById.containsKey(propertyId)) {
            return true;
        } else if (baseClassDef != null) {
            return baseClassDef.isPropertyDefExistsById(propertyId);
        }
        return false;
    }

    /**
     * Метод проверяет существует ли метоописание свойства с заданным идентификатором.
     * Поиск свойства производится в данном классе и во всех его предках.
     * @param propertyId - идентификатор свойства
     * @return true если описание свойства найдено
     */
    public final boolean isPropertyDefExistsByName(final String propertyName) {
        if (propertiesByName == null) {
            fillPropertiesByName();
        }
        if (propertiesByName.containsKey(propertyName)) {
            return true;
        } else if (baseClassDef != null) {
            return baseClassDef.isPropertyDefExistsByName(propertyName);
        }
        return false;
    }

    /**
     * Метод возвращает метоописания свойств объявленных в данном классе,
     * и во всех его подклассах.
     * @return List<PropertyDef> список свойств
     */
    public final List<RadPropertyDef> getProperties() {
        if (linkedProperties == null) {
            linkedProperties = new ArrayList<>();
            for (RadClassPresentationDef classDef = this; classDef != null; classDef = classDef.baseClassDef) {
                if (classDef.properties != null) {
                    linkedProperties.addAll(Arrays.asList(classDef.properties));
                }
            }
            //Добавление свойств из группового обработчика
            if (isEntityClassPresentation) {
                final Id groupEventHandlerId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS);
                try {
                    final RadGroupHandlerDef groupClass = getDefManager().getGroupHandlerDef(groupEventHandlerId);
                    linkedProperties.addAll(groupClass.getGroupProperties());
                } catch (DefinitionError ex) {
                    //Групповой обработчик событий отсутствует - нормальная ситуация
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        return Collections.unmodifiableList(linkedProperties);
    }

    private void fillPropertiesById() {
        if (properties != null && properties.length > 0) {
            propertiesById = new HashMap<>(properties.length * 2);
            for (RadPropertyDef property : properties) {
                propertiesById.put(property.getId(), property);
            }
        } else {
            propertiesById = new HashMap<>(4);
        }

        //Добавление свойств из группового обработчика
        if (isEntityClassPresentation) {
            final Id groupEventHandlerId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS);
            try {
                final RadGroupHandlerDef groupClass = getDefManager().getGroupHandlerDef(groupEventHandlerId);
                final List<RadPropertyDef> groupProperties = groupClass.getGroupProperties();
                for (RadPropertyDef property : groupProperties) {
                    propertiesById.put(property.getId(), property);
                }
            } catch (DefinitionError ex) {
                //Групповой обработчик событий отсутствует - нормальная ситуация
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }

    }

    private void fillPropertiesByName() {
        if (properties != null && properties.length > 0) {
            propertiesByName = new HashMap<>(properties.length * 2);
            for (RadPropertyDef property : properties) {
                propertiesByName.put(property.getName(), property);
            }
        } else {
            propertiesByName = new HashMap<>(4);
        }

        //Добавление свойств из группового обработчика
        if (isEntityClassPresentation) {
            final Id groupEventHandlerId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS);
            try {
                final RadGroupHandlerDef groupClass = getDefManager().getGroupHandlerDef(groupEventHandlerId);
                final List<RadPropertyDef> groupProperties = groupClass.getGroupProperties();
                for (RadPropertyDef property : groupProperties) {
                    propertiesByName.put(property.getName(), property);
                }
            } catch (DefinitionError ex) {
                //Групповой обработчик событий отсутствует - нормальная ситуация
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }
    //Команды:
    protected RadCommandDef[] commands = null;//Заполняется в кострукторе наследника
    private final Object commandsSemaphore = new Object();
    private List<Id> linkedCommandsByOrder;//synchronized by commandsSemaphore
    private Map<Id, RadCommandDef> linkedCommandsById;//synchronized by commandsSemaphore

    private void linkCommands() {//synchronized by commandsSemaphore
        linkedCommandsById = new HashMap<>();
        linkedCommandsByOrder = new ArrayList<>();
        //Сначала обрабатываются собственные команды
        if (commands != null && commands.length > 0) {
            for (RadCommandDef command : commands) {
                linkedCommandsById.put(command.getId(), command);
                linkedCommandsByOrder.add(command.getId());
            }
        }
        //Потом команды базовых классов
        if (baseClassDef != null) {
            List<Id> commandsOfBaseClass = baseClassDef.getCommandsByOrder();
            for (Id commandId : commandsOfBaseClass) {
                //Команда базового класса может быть перекрыта
                if (!linkedCommandsById.containsKey(commandId)) {
                    linkedCommandsById.put(commandId, baseClassDef.getCommandDefById(commandId));
                    linkedCommandsByOrder.add(commandId);
                }
            }
        }
        //Потом команды в групповом обработчике
        if (isEntityClassPresentation) {
            final Id groupEventHandlerId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_ENTITY_GROUP_CLASS);
            try {
                RadGroupHandlerDef groupClass = getDefManager().getGroupHandlerDef(groupEventHandlerId);
                final List<RadCommandDef> groupCommands = groupClass.getGroupCommands();
                for (RadCommandDef command : groupCommands) {
                    linkedCommandsById.put(command.getId(), command);
                    linkedCommandsByOrder.add(command.getId());
                }
            } catch (DefinitionError ex) {
                //Групповой обработчик событий отсутствует - нормальная ситуация
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
    }

    /**
     * Метод возвращает набор идентификаторов команд, определенных в данном классе и во всех его предках.
     * @return  Collection<CommandDef>
     */
    public List<Id> getCommandsByOrder() {
        synchronized(commandsSemaphore){
            if (linkedCommandsById == null) {
                linkCommands();
            }
            return Collections.unmodifiableList(linkedCommandsByOrder);
        }
    }

    /**
     * Метод возвращает метоописание команды по идентификатору.
     * Поиск команды производится в данном классе и во всех его предках.
     * Если команда не найдена генерируется ошибка NoDefinitionWithSuchIdError
     * @param commandId - идентификатор команды
     * @return Презентационные атрибуты команды с идентификатором commandId
     */
    public RadCommandDef getCommandDefById(final Id commandId) {
        synchronized(commandsSemaphore){
            if (linkedCommandsById == null) {
                linkCommands();
            }
            if (linkedCommandsById.containsKey(commandId)) {
                return linkedCommandsById.get(commandId);
            }
            throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.COMMAND, commandId);
        }
    }

    public boolean isCommandDefExistsById(final Id commandId) {
        synchronized(commandsSemaphore){
            if (linkedCommandsById == null) {
                linkCommands();
            }
            return linkedCommandsById.containsKey(commandId);
        }
    }
    //Фильтры
    protected RadFilterDef[] filters = null; //Заполняется в конструкторе наследника
    private Map<Id, RadFilterDef> filtersById = null;

    /**
     * Метод возвращает описание фильтра по идентификатору.
     * Поиск фильтра производится в данном классе и во всех его предках.
     * Если фильтр не найден генерируется ошибка NoDefinitionWithSuchIdError
     * @param filterId - идентификатор фильтра
     * @return  Описание фильтра с идентификатором filterId
     */
    public final RadFilterDef getFilterDefById(final Id filterId) {
        if (filtersById == null) {
            fillFilters();
        }
        if (filtersById.containsKey(filterId)) {
            return filtersById.get(filterId);
        }

        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.FILTER, filterId);
    }

    public final boolean isFilterDefExists(final Id filterId) {
        if (filtersById == null) {
            fillFilters();
        }

        return filtersById.containsKey(filterId);
    }

    private void fillFilters() {
        filtersById = new HashMap<>();        
        for (RadClassPresentationDef classDef = this; classDef != null; classDef = classDef.baseClassDef) {
            if (classDef.filters != null && classDef.filters.length > 0) {
                for (RadFilterDef filter : classDef.filters) {
                    if (filter.getEnvironmentType() == ERuntimeEnvironmentType.COMMON_CLIENT ||
                        filter.getEnvironmentType() == getApplication().getRuntimeEnvironmentType()
                       )
                    filtersById.put(filter.getId(), filter);
                }
            }
        }
    }

    public final Collection<RadFilterDef> getFilters() {
        if (filtersById == null) {
            fillFilters();
        }
        return Collections.unmodifiableCollection(filtersById.values());
    }
    //Сортировки
    protected RadSortingDef[] sortings = null; //Заполняется в конструкторе наследника
    private Map<Id, RadSortingDef> sortingsById = null;

    private void fillSortings() {
        sortingsById = new HashMap<>();        
        for (RadClassPresentationDef classDef = this; classDef != null; classDef = classDef.baseClassDef) {
            if (classDef.sortings != null && classDef.sortings.length > 0) {
                for (RadSortingDef sorting : classDef.sortings) {
                    sortingsById.put(sorting.getId(), sorting);
                }
            }
        }
    }

    /**
     * Метод возвращает описание сортировки по идентификатору.
     * Поиск сортировки производится в данном классе и во всех его предках.
     * Если сортировка не найдена генерируется ошибка NoDefinitionWithSuchIdError
     * @param sortingId идентификатор сортировки
     * @return  Описание сортировки с идентификатором sortingId
     */
    public final RadSortingDef getSortingDefById(final Id sortingId) {
        if (sortingsById == null) {
            fillSortings();
        }
        if (sortingsById.containsKey(sortingId)) {
            return sortingsById.get(sortingId);
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.SORTING, sortingId);
    }

    public final Collection<RadSortingDef> getSortings() {
        if (sortingsById == null) {
            fillSortings();
        }
        return Collections.unmodifiableCollection(sortingsById.values());
    }

    //Объявленные презентации
    protected Id[] presentations;
    private List<Id> presentationIds = null;

    /**
     * Метод возвращает список презентаций, определенных в данном классе и во всех
     * загруженных потомках.
     * Используется при подъеме версии дефиниций.
     * @return
     */
    public final List<Id> getDefinedPresentationIDs() {
        if (presentationIds == null) {
            link();
        }
        return Collections.unmodifiableList(presentationIds);
    }

    /**
     * Метод возвращает набор идентификаторов презентаций редактора, определенных в данном классе
     * @return
     */
    public final Collection<Id> getEditorPresentationIds() {
        final Collection<Id> result = new ArrayList<>();
        if (presentations != null && presentations.length > 0) {
            for (Id id : presentations) {
                if (id.getPrefix() == EDefinitionIdPrefix.EDITOR_PRESENTATION) {
                    result.add(id);
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    /**
     * Метод возвращает набор идентификаторов презентаций селектора, определенных в данном классе
     * @return
     */
    public final Collection<Id> getSelectorPresentationIds() {
        final Collection<Id> result = new ArrayList<>();
        if (presentations != null && presentations.length > 0) {
            for (Id id : presentations) {
                if (id.getPrefix() == EDefinitionIdPrefix.SELECTOR_PRESENTATION) {
                    result.add(id);
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }
    protected RadReferenceDef[] references;
    private List<RadReferenceDef> linkedReferences = null;

    /**
     * Метод возвращает список связей таблицы данного класса.
     * Используется в sqml редакторе.
     * @return список связей таблицы данного класса.
     */
    public List<RadReferenceDef> getReferences() {
        if (linkedReferences == null) {
            linkedReferences = new ArrayList<>();
            if (references != null) {
                linkedReferences.addAll(Arrays.asList(references));
            }
            RadClassPresentationDef classDef = this;
            while (classDef.baseClassDef != null) {
                classDef = classDef.baseClassDef;
                if (classDef.references != null) {
                    linkedReferences.addAll(Arrays.asList(classDef.references));
                }
            }
        }
        return Collections.unmodifiableList(linkedReferences);
    }

    public RadReferenceDef findReferenceDefById(final Id referenceId) {
        final List<RadReferenceDef> references = getReferences();
        for (RadReferenceDef reference : references) {
            if (reference.getId().equals(referenceId)) {
                return reference;
            }
        }
        return null;
    }

    /**
     * @return Ограничения в сущности
     * (Copy, MultipleCopy, Move, Create, Update, Delete)
     */
    public Restrictions getRestrictions() {  // by BAO
        if (isEntityClassPresentation) {
            return restrictions;
        } else {
            return baseClassDef.getRestrictions();
        }
    }

    /**
     * @return Пиктограмма сущности.
     */
    public Icon getIcon() {
        if (isEntityClassPresentation) {
            return getIcon(iconId);
        } else {
            return baseClassDef.getIcon();
        }
    }

    /**
     * @return Короткое имя сущности не включающее имя модуля и имя слоя
     */
    public String getSimpleName() {
        final int idx = getName().lastIndexOf("::");
        return idx > -1 ? getName().substring(idx + 2) : getName();
    }

    /**
     * @return Заголовок сущности во множественном числе.
     * Если заголовок получить не удается возвращается <No Title>
     */
    public String getGroupTitle() {
        if (isEntityClassPresentation) {
            try {
                if (grpTitleId != null) {
                    return getDefManager().getMlStringValue(getId(), grpTitleId);
                } else {
                    return getApplication().getMessageProvider().translate("ExplorerItem", "<No Title>");
                }
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get group title #%s for presentation class %s #%s");
                getApplication().getTracer().error(String.format(mess, grpTitleId, getName(), getId()), err);
                return getApplication().getMessageProvider().translate("ExplorerItem", "<No Title>");
            }
        } else {
            return baseClassDef.getGroupTitle();
        }
    }

    public boolean hasGroupTitle() {
        if (isEntityClassPresentation) {
            if (grpTitleId != null) {
                try {
                    return !getDefManager().getMlStringBundleDef(getId()).get(grpTitleId).isEmpty();
                } catch (DefinitionError err) {
                    final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get group title #%s for presentation class %s #%s");
                    getApplication().getTracer().error(String.format(mess, grpTitleId, getName(), getId()), err);
                }
            }
            return false;
        } else {
            return baseClassDef.hasGroupTitle();
        }
    }

    public String getClassTitle() {
        if (!isEntityClassPresentation) {
            try {
                if (classTitleId != null) {
                    return getDefManager().getMlStringValue(getId(), classTitleId);
                } else {
                    return "";
                }
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get class title  #%s for presentation class %s");
                getApplication().getTracer().error(String.format(mess, classTitleId, getDefinitionDebugInfo()), err);
                return "";
            }
        } else {
            return "";
        }
    }

    public boolean hasObjectTitle() {
        final Id titleId = isEntityClassPresentation ? objTitleId : classTitleId;
        if (titleId != null) {
            try {
                return !getDefManager().getMlStringValue(getId(), titleId).isEmpty();
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get entity title #%s for presentation class %s");
                getApplication().getTracer().error(String.format(mess, titleId, getDefinitionDebugInfo()), err);
            }
            return false;
        } else if (baseClassDef != null) {
            return baseClassDef.hasObjectTitle();
        } else {
            return false;
        }
    }

    /**
     * @return Заголовок сущности в единственном числе.
     * Если заголовок получить не удается возвращается <No Title>
     */
    public String getObjectTitle() {
        //RADIX-3460
        final Id titleId = isEntityClassPresentation ? objTitleId : classTitleId;
        if (titleId != null) {
            try {
                return getDefManager().getMlStringValue(getId(), titleId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get entity title #%s for presentation class %s");
                getApplication().getTracer().error(String.format(mess, titleId, getDefinitionDebugInfo()), err);
                return getApplication().getMessageProvider().translate("ExplorerItem", "<No Title>");
            }
        } else if (baseClassDef != null) {
            return baseClassDef.getObjectTitle();
        } else {
            return "";
        }
    }

    /**
     * @return Презентация селектора, которая используется по-умолчания для
     * выбора объекта (например, в свойствах parentRef).
     */
    public RadSelectorPresentationDef getDefaultSelectorPresentation() {
        //Default selector presentation is defined in Entity Class Presentation //by BAO
        if (isEntityClassPresentation) {//by BAO
            if (selPresentationId != null) {
                return getDefManager().getSelectorPresentationDef(selPresentationId);
            }
            return null;
        } else//by BAO
        {
            return baseClassDef.getDefaultSelectorPresentation();
        }
    }

    public boolean isAncestorOf(RadClassPresentationDef classDef) {
        RadClassPresentationDef baseClass = classDef.getAncestorClassPresentationDef();
        while (baseClass != null) {
            if (baseClass.getId().equals(getId())) {
                return true;
            }
            baseClass = baseClass.getAncestorClassPresentationDef();
        }
        return false;
    }

    public boolean isDerivedFrom(RadClassPresentationDef classDef) {
        return classDef.isAncestorOf(this);
    }

    public Id getTableId() {
        RadClassPresentationDef baseClass = this;
        while (!baseClass.isEntityClassPresentation) {
            baseClass = baseClass.baseClassDef;
        }
        return Id.Factory.changePrefix(baseClass.getId(), EDefinitionIdPrefix.DDS_TABLE);
    }

    @Override
    public String toString() {
        final String classTitle = getClassTitle();
        final String message;
        if (!classTitle.isEmpty()) {
            message = getApplication().getMessageProvider().translate("DefinitionDescribtion","%s with class title \"%s\"");
            return String.format(message, super.toString(), classTitle);
        } else if (hasGroupTitle()) {
            message = getApplication().getMessageProvider().translate("DefinitionDescribtion","%s with group title \"%s\"");
            return String.format(message, super.toString(), getGroupTitle());
        }
        return super.toString();
    }
    
    private String getDefinitionDebugInfo(){
        return (getName() != null && !getName().isEmpty() ? "\"" + getName() + "\" " : "") + "#"+String.valueOf(getId());
    }

    @Override
    public String getDescription() {
        if (isEntityClassPresentation) {
            return getApplication().getMessageProvider().translate("DefinitionDescribtion", "entity class ") + super.getDescription();
        } else {
            return getApplication().getMessageProvider().translate("DefinitionDescribtion", "application class") + super.getDescription();
        }
    }

    public boolean isClassCatalogExists() {
        return isClassCatalogExists || (getAncestorClassPresentationDef() != null && getAncestorClassPresentationDef().isClassCatalogExists());
    }

    public boolean isAuditEnabled() {
        RadClassPresentationDef baseClass = this;
        while (!baseClass.isEntityClassPresentation) {
            baseClass = baseClass.baseClassDef;
        }
        return baseClass.isAuditEnabled;
    }
    
    public boolean isUserFunction(){
        return isUserFunction;
    }
}
