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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.Arrays;
import java.util.EnumSet;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

public abstract class RadPresentationDef extends TitledDefinition implements IModelDefinition {

    static final class PresentationInheritance {

        private final EnumSet<EPresentationAttrInheritance> inheritedAttributes;

        public PresentationInheritance(final long inheritanceMask) {
            inheritedAttributes = EPresentationAttrInheritance.fromBitField(inheritanceMask);
        }

        public boolean isAddonsInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.ADDONS);
        }

        public boolean isIconInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.ICON);
        }

        public boolean isRestrictionsInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.RESTRICTIONS);
        }

        public boolean isSelectorColumnsInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.COLUMNS);
        }

        public boolean isTitleInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.TITLE);
        }

        public boolean isChildrenInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.CHILDREN);
        }

        public boolean isPagesInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.PAGES);
        }
        
        public boolean isChildrenOrderInherited() {
            return inheritedAttributes.contains(EPresentationAttrInheritance.CHILDREN_ORDER);
        }
        
        public boolean isPresentationPropertyAttributesInherited(){
            return inheritedAttributes.contains(EPresentationAttrInheritance.PROPERTY_PRESENTATION_ATTRIBUTES);
        }
    }
    
    
    protected final Id classId;
    private final Id tableId;
    private final Id iconId;
    private final ERuntimeEnvironmentType envType;
    protected final Id basePresentationId;
    final PresentationInheritance inheritanceMask;
    protected final Restrictions restrictions;
    protected List<RadCommandDef> enabledCommands = null;
    private RadClassPresentationDef presentationClass;
    private final Object presentationClassSem = new Object();
    private final int sizeX;
    private final int sizeY;

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    public RadPresentationDef(
            final Id id,
            final String name,
            final ERuntimeEnvironmentType type,
            final Id basePresentationId,
            final Id classId,
            final Id tableId,
            final Id titleId,
            final Id iconId,
            final Id[] contextlessCommandIds,
            final long restrictionsMask,
            final Id[] enabledCommandIds,
            final long inheritanceMask,
            final int sizeX,
            final int sizeY) {
        super(id, name, classId == null ? tableId : classId, titleId);
        this.envType = type;
        this.iconId = iconId;
        this.classId = classId;
        this.tableId = tableId;
        this.contextlessCommandIds = contextlessCommandIds;
        this.basePresentationId = basePresentationId;
        this.inheritanceMask = new PresentationInheritance(inheritanceMask);
        final List<Id> enabledCmds = enabledCommandIds != null ? Arrays.asList(enabledCommandIds) : null;
        restrictions = Restrictions.Factory.sum(
                getClassPresentation().getRestrictions(),
                Restrictions.Factory.newInstance(ERestriction.fromBitField(restrictionsMask), enabledCmds));
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }  

    /**
     * @return Базовая презентация или NULL, если отсутствует
     *
     * Метод должен быть перекрыт в сгенерированном наследнике презентации редактора или селектора
     *  return org.radixware.kernel.explorer.Environment.defManager.get<Editor/Selector>PresentationClassDef(classId,presentationId);
     */
    protected abstract RadPresentationDef getBasePresentation();

    /**
     * @return Класс, в котором определена данная презентация
     */
    public final RadClassPresentationDef getClassPresentation() {
        synchronized(presentationClassSem){
            if (presentationClass == null) {
                presentationClass = getDefManager().getClassPresentationDef(classId);
            }
            return presentationClass;
        }
    }

    @Override
    public Id getOwnerClassId() {
        return getClassPresentation().getId();
    }

    public Id getTableId() {
        return tableId;
    }

    @Override
    public Icon getIcon() {
        if (inheritanceMask.isIconInherited()) {
            if (getBasePresentation() != null) {
                return getBasePresentation().getIcon();
            } else if (iconId != null) {//inherited from previous layer:  RADIX-2093
                return getIcon(iconId);
            } else {
                return getClassPresentation().getIcon();
            }
        }
        return getIcon(iconId);
    }

    @Override
    public Restrictions getRestrictions() {
        if (inheritanceMask.isRestrictionsInherited()) {
            if (getBasePresentation() != null) {
                return getBasePresentation().getRestrictions();
            }
        }
        return restrictions;
    }
    //Команды
    protected Id[] contextlessCommandIds;
    protected Map<Id, RadCommandDef> contextlessCommandsById;
    private final Object commandsSemaphore = new Object();

    /**
     * callback метод определяет доступность команды для конкретного
     * типа презентации. Реализуется в SelectorPresentationDef и EditorPresentationDef.
     * @return true если command доступна для данного типа презентации.
     */
    protected abstract boolean isCommandEnabled(RadPresentationCommandDef command);

    private void fillCommands() {
        final List<Id> commandsByOrder;

        if (getRestrictions().getEnabledCommandIDs() != null) {
            commandsByOrder = getRestrictions().getEnabledCommandIDs();
        } else {
            commandsByOrder = getClassPresentation().getCommandsByOrder();
        }

        enabledCommands = new ArrayList<>(commandsByOrder.size());
        contextlessCommandsById = new HashMap<>();
        for (Id commandId : commandsByOrder) {
            if (!getRestrictions().getIsCommandRestricted(commandId)) {
                if (commandId.getPrefix() == EDefinitionIdPrefix.CONTEXTLESS_COMMAND) {
                    try {
                        RadCommandDef command = getDefManager().getContextlessCommandDef(commandId);
                        contextlessCommandsById.put(commandId, command);
                        //Проверка на Environment.isContextlessCommandAccessible производиться в модели
                        enabledCommands.add(command);
                    } catch (NoDefinitionWithSuchIdError e) {
                        getApplication().getTracer().debug("Command not found: #" + commandId);
                    } catch (DefinitionError err) {
                        final String msg = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get contextless command #%s for presentation %s");
                        getApplication().getTracer().error(String.format(msg, commandId, toString()), err);
                    }
                } else {
                    try {
                        final RadPresentationCommandDef presentationCommand = (RadPresentationCommandDef) getClassPresentation().getCommandDefById(commandId);
                        if (isCommandEnabled(presentationCommand)) {
                            enabledCommands.add(presentationCommand);
                        }
                    } catch (NoDefinitionWithSuchIdError e) {
                        getApplication().getTracer().debug("Command not found: #" + commandId);
                    }
                }
            }
        }
        RadPresentationDef presentation = this;
        while (presentation != null) {
            if (presentation.contextlessCommandIds != null && presentation.contextlessCommandIds.length > 0) //Дозаполняем массив contextlessCommandsById командами не вошедшими в commandsByOrder
            {
                for (Id commandId : presentation.contextlessCommandIds) {
                    if (!contextlessCommandsById.containsKey(commandId)) {
                        try {
                            RadCommandDef command = getDefManager().getContextlessCommandDef(commandId);
                            contextlessCommandsById.put(commandId, command);
                            //Проверка на Environment.isContextlessCommandAccessible производиться в модели
                            //Чтобы бесконтекстная команда была доступна в панели команд она должна быть
                            //явно определена в Restrictions
                        } catch (NoDefinitionWithSuchIdError e) {
                            getApplication().getTracer().debug("Command not found: #" + commandId);
                        } catch (DefinitionError err) {
                            final String msg = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get contextless command #%s for presentation %s");
                            getApplication().getTracer().error(String.format(msg, commandId, toString()), err);
                        }
                    }
                }
            }
            presentation = presentation.getBasePresentation();
        }
    }
    
    
    /**
     * Получение типа среды выполнения, для которой описана данная презентация
     * @return тип среды выполнения. Не может быть <code>null</code>
     */
    public ERuntimeEnvironmentType getRuntimeEnvironmentType() {
        return envType;
    }        

    /**
     * @return Полный список команд (в т.ч. бесконтекстных),
     * разрешенных в данной презентации, с учетом CommandScope и наследования.
     */
    @Override
    public List<RadCommandDef> getEnabledCommands() {
        synchronized(commandsSemaphore){
            if (enabledCommands == null) {
                fillCommands();
            }
            return Collections.unmodifiableList(enabledCommands);
        }
    }

    /**
     * Метод возвращает метоописание команды по идентификатору.
     * Поиск производится по всем командам (в т.ч. бесконтекстным и запрещенным)
     * с учетом начледования.
     * Если команда не найдена генерируется ошибка NoDefinitionWithSuchIdError
     * @param commandId - идентификатор команды
     * @return Презентационные атрибуты команды с идентификатором commandId
     * @see getEnabledCommands()
     */
    @Override
    public RadCommandDef getCommandDefById(final Id commandId) {
        synchronized(commandsSemaphore){
            if (contextlessCommandsById == null) {
                fillCommands();
            }
            if (contextlessCommandsById.containsKey(commandId)) {
                return contextlessCommandsById.get(commandId);
            }
        }            
        return getClassPresentation().getCommandDefById(commandId);
    }

    @Override
    public boolean isCommandDefExistsById(final Id commandId) {
        synchronized(commandsSemaphore){
            if (contextlessCommandsById == null) {
                fillCommands();
            }

            if (contextlessCommandsById.containsKey(commandId)) {
                return true;
            }
        }
        return getClassPresentation().isCommandDefExistsById(commandId);
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "%s in class #%s");
        return String.format(desc, super.getDescription(), classId);
    }    
        
    public int getDefaultDialogWidth(){
        return sizeX;
    }
    
    public int getDefaultDialogHeight(){
        return sizeY;
    }
}
