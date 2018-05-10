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

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchNameError;
import org.radixware.kernel.common.client.meta.editorpages.IEditorPagesHolder;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.models.FormModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * Презентационные атрибуты формы
 * Наследники генерируются дизайнером
 * Предоставляет доступ к описаниям страниц, свойств и команд формы.
 *
 */
public class RadFormDef extends TitledDefinition implements IModelDefinition, IEditorPagesHolder {
    /*
     *  В конструкторе сгенерированного наследника должны быть заполнены поля:
     *  	pages[] , pageOrder[], commands[], commandOrder[], properties[] (см. комментарии)
     *  А также реализован метод getBaseFormDef
     */

    /**
     * Страницы редактора, определенные в данной форме.
     * Заполняется в конструкторе наследника.
     */
    protected final RadEditorPageDef[] pages;

    @Override
    public RadEditorPageDef findEditorPageById(final Id pageId) {
        RadFormDef form = this;
        while (form != null) {
            if (form.pages != null && form.pages.length > 0) {
                for (RadEditorPageDef page : form.pages) {
                    if (page.getId().equals(pageId)) {
                        return page;
                    }
                }
            }
            form = form.getBaseFormDef();
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.EDITOR_PAGE, pageId);
    }
    /**
     *Идентификаторы и уровни вложенности всех страниц формы (с учетом наследования),
     *расположенные в порядке развертывания в глубину дерева страниц.
     */
    protected final RadEditorPages.PageOrder[] pageOrder;
    private RadEditorPages editorPages = null;

    @Override
    public final RadEditorPages getEditorPages() {
        if (editorPages == null) {
            if (this.inheritanceMask.isPagesInherited()) {
                editorPages = getBaseFormDef().getEditorPages();
            } else {
                editorPages = new RadEditorPages(pageOrder, this);
            }
        }
        return editorPages;
    }

    @Override
    public boolean isCommandDefExistsById(final Id id) {
        try{
            return findCommandDef(id)!=null;
        }catch(DefinitionError error){
            return false;
        }
    }

    static protected final class FormInheritance {

        private final EnumSet<EPresentationAttrInheritance> inheritedAttributes;

        public FormInheritance(final long inheritanceMask) {
            inheritedAttributes = EPresentationAttrInheritance.fromBitField(inheritanceMask);
        }

        public boolean isIconInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.ICON);
        }

        public boolean isTitleInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.TITLE);
        }

        public boolean isPagesInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.PAGES);
        }

        public boolean isCommandsInherited() {
            return false;
        }
    }
    
    
    private final Id iconId;
    private final FormInheritance inheritanceMask;
    private final Id baseFormId;
    private final int sizeX;
    private final int sizeY;

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadFormDef(final Id id,
            final String name,
            final Id baseFormId,
            final Id titleId,
            final Id iconId,
            final long inheritanceMask,
            final RadPropertyDef[] properties,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadCommandDef[] commands,
            final Id[] commandOrder,
            final int sizeX,
            final int sizeY) {
        super(id, name, titleId);
        this.iconId = iconId;
        this.inheritanceMask = new FormInheritance(inheritanceMask);
        this.baseFormId = baseFormId;
        this.properties = properties;
        this.pageOrder = pageOrder;
        this.commandOrder = commandOrder;
        this.commands = commands;
        this.pages = editorPages;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
    
    public RadFormDef(final Id id,
            final String name,
            final Id baseFormId,
            final Id titleId,
            final Id iconId,
            final long inheritanceMask,
            final RadPropertyDef[] properties,
            final RadEditorPageDef[] editorPages,
            final RadEditorPages.PageOrder[] pageOrder,
            final RadCommandDef[] commands,
            final Id[] commandOrder) {
        this(id, 
             name, 
             baseFormId, 
             titleId, 
             iconId, 
             inheritanceMask, 
             properties, 
             editorPages, 
             pageOrder, 
             commands, 
             commandOrder,
             0,
             0);
    }    
    
    
    /**
     * @return Дефиниция базовой формы или
     * NULL, если данная форма не имеет базовую
     *
     * Метод должен быть перекрыт в сгенерированном наследнике
     *  return org.radixware.kernel.explorer.Environment.defManager.getFormDef(baseClassDefId);
     */
    private RadFormDef baseForm;

    final RadFormDef getBaseFormDef() {
        if (baseForm == null && baseFormId != null) {
            baseForm = getDefManager().getFormDef(baseFormId);
        }
        return baseForm;
    }

    @Override
    public FormModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model model = createModelImpl(context.getEnvironment());
        model.setContext(context);
        return (FormModel) model;
    }

    protected Model createModelImpl(final IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_FORM_MODEL_CLASS);
        try {
            final Class<Model> classModel = environment.getDefManager().getDefinitionModelClass(modelClassId);
            final Constructor<Model> constructor = 
                classModel.getConstructor(IClientEnvironment.class,RadFormDef.class);
            return constructor.newInstance(environment,this);
        } catch (Exception e) {
            throw new ModelCreationError(ModelCreationError.ModelType.FORM_MODEL, this, null, e);
        }
    }

    @Override
    public IView createStandardView(final IClientEnvironment environment) {
        return getApplication().getStandardViewsFactory().newStandardForm(environment);
    }

    @Override
    public Icon getIcon() {
        if (inheritanceMask.isIconInherited()) {
            getBaseFormDef().getIcon();
        }
        return getIcon(iconId);
    }

    @Override
    public String getTitle() {
        if (getBaseFormDef() != null && (inheritanceMask.isTitleInherited() || !hasTitle())) {//RADIX-1199
            return getBaseFormDef().getTitle();
        }
        return super.getTitle();
    }

    @Override
    public Id getOwnerClassId() {
        return getId();
    }
    //Свойства:
    /**
     * Собственные свойства формы.
     * Заполняется в конструкторе наследника
     */
    protected final RadPropertyDef[] properties;
    private Map<Id, RadPropertyDef> propertiesById = null;
    private Map<String, RadPropertyDef> propertiesByName = null;

    /**
     * Метод возвращает метоописание свойства по идентификатору.
     * Поиск свойства производится в данной форме и во всех ее предках.
     * Если свойство не найдено генерируется ошибка NoDefinitionWithSuchIdError
     * @param propertyId - идентификатор свойства
     * @return Презентационные атрибуты свойства с идентификатором propertyId
     */
    @Override
    public final RadPropertyDef getPropertyDefById(final Id propertyId) {
        if (propertiesById == null) {
            fillPropertiesById();
        }

        if (propertiesById.containsKey(propertyId)) {
            return propertiesById.get(propertyId);
        }

        if (getBaseFormDef() != null) {
            return getBaseFormDef().getPropertyDefById(propertyId);
        }

        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, propertyId);
    }

    @Override
    public boolean isPropertyDefExistsById(final Id id) {
        if (propertiesById == null) {
            fillPropertiesById();
        }

        if (propertiesById.containsKey(id)) {
            return true;
        }

        return getBaseFormDef() != null && getBaseFormDef().isPropertyDefExistsById(id);
    }

    private void fillPropertiesById() {
        if (properties != null && properties.length > 0) {
            propertiesById = new HashMap<>(properties.length * 2);
            for (RadPropertyDef property : properties) {
                propertiesById.put(property.getId(), property);
            }
        } else {
            propertiesById = Collections.emptyMap();
        }
    }

    /**
     * Метод возвращает метоописание свойства по имени.
     * Поиск свойства производится в данной форме и во всех ее предках.
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
        if (getBaseFormDef() != null) {
            return getBaseFormDef().getPropertyDefByName(propertyName);
        }
        throw new NoDefinitionWithSuchNameError((Definition) this, NoDefinitionWithSuchNameError.SubDefinitionType.PROPERTY, propertyName);
    }

    public boolean isPropertyDefExistsByName(final String propertyName) {
        if (propertiesByName == null) {
            fillPropertiesByName();
        }

        if (propertiesByName.containsKey(propertyName)) {
            return true;
        }

        return getBaseFormDef() != null && getBaseFormDef().isPropertyDefExistsByName(propertyName);
    }
    
    public Collection<RadPropertyDef> getProperties(){
        return properties==null || properties.length==0 ? Collections.<RadPropertyDef>emptyList() : Arrays.<RadPropertyDef>asList(properties);
    }

    private void fillPropertiesByName() {
        if (properties != null && properties.length > 0) {
            propertiesByName = new HashMap<>(properties.length * 2);
            for (RadPropertyDef property : properties) {
                propertiesByName.put(property.getName(), property);
            }
        } else {
            propertiesByName = Collections.emptyMap();
        }
    }
    //Команды:
    /**
     * Собственные команды формы.
     * Заполняется в конструкторе наследника
     */
    protected RadCommandDef[] commands = null;
    /**
     * Идентификаторы всех команд доступных в форме, с учетом наследования
     * Заполняется в конструкторе наследника
     */
    protected Id[] commandOrder = null;
    private List<RadCommandDef> enabledCommands = null;
    private final Object commandsSemaphore = new Object();

    private RadCommandDef findCommandDef(final Id commandId) {
        if (commands != null && commands.length > 0) {
            for (RadCommandDef command : commands) {
                if (command.getId().equals(commandId)) {
                    return command;
                }
            }
        }
        if (commandId.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
            return getDefManager().getContextlessCommandDef(commandId);
        }
        if (getBaseFormDef() != null) {
            return getBaseFormDef().findCommandDef(commandId);
        }
        return null;
    }

    private void linkCommands() {
        enabledCommands = new ArrayList<>();
        if (commandOrder != null && commandOrder.length > 0) {
            RadCommandDef command;
            for (Id commandId : commandOrder) {
                command = findCommandDef(commandId);
                if (command!=null){
                    enabledCommands.add(command);
                }
            }
        }
        for (RadFormDef formDef = this; formDef != null; formDef = formDef.getBaseFormDef()) {
            if (formDef.commands != null && formDef.commands.length > 0) {
                for (RadCommandDef command : formDef.commands) {
                    if (!enabledCommands.contains(command)) {
                        enabledCommands.add(command);
                    }
                }
            }
        }
    }

    /**
     * Метод возвращает набор команд, определенных в данной форме и во всех ее предках.
     * Команды, идентификаторы которых переданы в списке commandOrder, будут первыми.
     * @return  List<CommandDef>
     */
    @Override
    public final List<RadCommandDef> getEnabledCommands() {
        synchronized(commandsSemaphore){
            if (enabledCommands == null) {
                linkCommands();
            }
            return Collections.unmodifiableList(enabledCommands);
        }        
    }

    /**
     * Метод возвращает метоописание команды по идентификатору.
     * Поиск команды производится в данной форме и во всех ее предках.
     * Если команда не найдена генерируется ошибка NoDefinitionWithSuchIdError
     * @param commandId - идентификатор команды
     * @return Презентационные атрибуты команды с идентификатором commandId
     */
    @Override
    public final RadCommandDef getCommandDefById(final Id commandId) {
        final RadCommandDef command = findCommandDef(commandId);
        if (command == null) {
            throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.COMMAND, commandId);
        }
        return command;
    }

    @Override
    public Restrictions getRestrictions() {
        return Restrictions.NO_RESTRICTIONS;
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "form %s");
        return String.format(desc, super.getDescription());
    }
    
    public int getDefaultWidth(){
        return sizeX;
    }
    
    public int getDefaultHeight(){
        return sizeY;
    }
}
