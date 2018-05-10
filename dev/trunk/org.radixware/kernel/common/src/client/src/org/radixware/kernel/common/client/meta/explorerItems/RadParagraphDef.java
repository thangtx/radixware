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
package org.radixware.kernel.common.client.meta.explorerItems;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.errors.ModelCreationError;
import org.radixware.kernel.common.client.errors.NoDefinitionWithSuchIdError;
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.IModelDefinition;
import org.radixware.kernel.common.client.meta.RadCommandDef;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.types.Id;

/**
 * Class for paragraph explorer item.
 *
 */
public class RadParagraphDef extends RadExplorerItemDef implements IExplorerItemsHolder {
    /*
     * В конструкторе наследника должен быть создан explorerItems, установлены
     * поля iconId, logoId
     *
     *
     */

    public static class LookupHelper {

        public static RadParagraphDef findChild(RadParagraphDef root, Id[] path) {
            RadParagraphDef p = root;

            while (p != null) {
                if (p.getId() != path[0]) {
                    continue;
                }
                RadParagraphDef child = findChild(path, 1, p);
                if (child != null) {
                    return child;
                }
                p = p.base;
            }
            return null;
        }

        private static RadParagraphDef findChild(Id[] path, int index, RadParagraphDef context) {
            if (context.arrExplorerItems == null || context.arrExplorerItems.length == 0) {
                return null;
            }
            for (RadExplorerItemDef explorerItem : context.arrExplorerItems) {
                if (explorerItem.getId() == path[index] && explorerItem instanceof RadParagraphDef) {
                    if (index == path.length - 1) {
                        return (RadParagraphDef) explorerItem;
                    } else {
                        return findChild(path, index + 1, (RadParagraphDef) explorerItem);
                    }
                }
            }
            return null;
        }
    }

    protected final Id iconId, logoId;
    private final Id classId;
    private final List<Id> contextlessCommandIds = new ArrayList<>();
    private final Map<Id, RadExplorerItemsSettings> childExplorerItemSettingsByParentId = new HashMap<>();
    private final boolean isRoot;
    private final boolean isHidden;
    private final RadParagraphDef base;
    private final RadExplorerItemDef[] arrExplorerItems;
    private final String layerUri;
    private RadExplorerItems explorerItems;
    private final Id[] explicitelyInheritedItems;

    public RadParagraphDef(
            final Id id,
            final Id titleOwnerId,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] explorerItems,
            final RadExplorerItemsSettings[] explorerItemsSettings,
            final Id[] commandIds,
            final Id iconId,
            final Id logoId,
            final boolean isRoot,
            final boolean isHidden,
            final RadParagraphDef baseParagraph) {
        this(id,
                titleOwnerId,
                titleId,
                layerUri,
                explorerItems,
                explorerItemsSettings,
                commandIds,
                iconId,
                logoId,
                isRoot,
                isHidden,
                baseParagraph, null);
    }

    public RadParagraphDef(
            final Id id,
            final Id titleOwnerId,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] explorerItems,
            final RadExplorerItemsSettings[] explorerItemsSettings,
            final Id[] commandIds,
            final Id iconId,
            final Id logoId,
            final boolean isRoot,
            final boolean isHidden,
            final RadParagraphDef baseParagraph,
            final Id[] explicitelyInheritedItems) {
        super(id, titleOwnerId, titleId, 0, true);
        if (explorerItemsSettings != null) {
            for (RadExplorerItemsSettings settings : explorerItemsSettings) {
                childExplorerItemSettingsByParentId.put(settings.getParentExplorerItemId(), settings);
            }
        }
        classId = titleOwnerId;
        this.iconId = iconId;
        this.logoId = logoId;
        this.layerUri = layerUri;
        this.isRoot = isRoot;
        this.isHidden = isHidden;
        this.explicitelyInheritedItems = explicitelyInheritedItems;
        if (commandIds != null && commandIds.length > 0) {
            Collections.addAll(contextlessCommandIds, commandIds);
        }
        arrExplorerItems = explorerItems;
        base = baseParagraph;
    }

    /**
     * Returns paragraph logo image. This image will be shown at center of
     * paragraph view when {@link StandardParagraph} openes.
     *
     * @return image for paragraph.
     */
    public final Icon getLogo() {
        return (logoId != null ? getDefManager().getImage(logoId) : null);
    }

    @Override
    public IModelDefinition getModelDefinition() {
        return this;
    }

    @Override
    public Id getModelDefinitionId() {
        return getId();
    }

    @Override
    public Id getModelDefinitionClassId() {
        return classId;
    }

    public boolean isHidden() {
        return isHidden;
    }

    //Реализация методов ModelDefinition
    @Override
    public Id getOwnerClassId() {
        return classId;
    }

    @Override
    public IView createStandardView(IClientEnvironment environment) {
        return getApplication().getStandardViewsFactory().newStandardParagraphEditor(environment);
    }

    @Override
    public ParagraphModel createModel(final IContext.Abstract context) {
        if (context == null) {
            throw new NullPointerException("Context must be not null");
        }
        final Model model = createModelImpl(context.getEnvironment());
        model.setContext(context);
        return (ParagraphModel) model;
    }

    protected Model createModelImpl(IClientEnvironment environment) {
        final Id modelClassId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_PARAGRAPH_MODEL_CLASS);
        try {
            Class<Model> classModel = environment.getApplication().getDefManager().getDefinitionModelClass(modelClassId);
            Constructor<Model> constructor = classModel.getConstructor(IClientEnvironment.class, RadParagraphDef.class);
            return constructor.newInstance(environment, this);
        } catch (Exception e) {
            throw new ModelCreationError(ModelCreationError.ModelType.PARAGRAPH_MODEL, this, null, e);
        }
    }

    @Override
    public RadCommandDef getCommandDefById(Id id) {
        if (isCommandDefExistsById(id)) {
            return getDefManager().getContextlessCommandDef(id);
        }
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.COMMAND, id);
    }

    @Override
    public boolean isCommandDefExistsById(Id id) {
        return contextlessCommandIds.indexOf(id) > -1;
    }
    private List<RadCommandDef> contextlessCommands;
    private final Object commandsSemaphore = new Object();

    @Override
    public List<RadCommandDef> getEnabledCommands() {
        synchronized(commandsSemaphore){
            if (contextlessCommands == null) {
                contextlessCommands = new ArrayList<>();
                for (Id commandId : contextlessCommandIds) {
                    contextlessCommands.add(getDefManager().getContextlessCommandDef(commandId));
                }
            }
            return contextlessCommands;
        }        
    }

    /**
     * @param id property identifier
     * @return always throws {@link NoDefinitionWithSuchIdError}
     */
    @Override
    public RadPropertyDef getPropertyDefById(Id id) {
        throw new NoDefinitionWithSuchIdError((Definition) this, NoDefinitionWithSuchIdError.SubDefinitionType.PROPERTY, id);
    }

    /**
     * @param id property identifier
     * @return always false
     */
    @Override
    public boolean isPropertyDefExistsById(Id id) {
        return false;
    }

    @Override
    public Restrictions getRestrictions() {
        return Restrictions.NO_RESTRICTIONS;
    }

    @Override
    public Icon getIcon() {
        return getIcon(iconId);
    }

    public Id getIconId() {
        return iconId;
    }

    public final boolean isRoot() {
        return isRoot;
    }

    public final String getLayerUri() {
        return layerUri;
    }

    @Override
    public String getDescription() {
        if (getId().equals(getOwnerClassId())) {
            final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "paragraph %s");
            return String.format(desc, super.getDescription());
        } else {
            final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "paragraph %s, owner definition is #%s");
            return String.format(desc, super.getDescription(), getOwnerClassId());
        }
    }

    private RadParagraphDef getBaseParagraphDef() {
        return base;
    }

    //IExplorerItemsHolder implementation
    /**
     * Возвращает набор дочерних элементов проводника. Реализация метода
     * {@link IExplorerItemsHolder#getChildrenExplorerItems()}. Метод возвращает
     * конечный (с учетом наследования) набор элементов проводника.
     *
     * @return набор элементов проводника. Не может быть <code>null</code>
     */
    @Override
    public RadExplorerItems getChildrenExplorerItems() {
        if (explorerItems == null) {
            final RadExplorerItems inheritedEI;
            if (base == null) {
                inheritedEI = null;
            } else {
                inheritedEI = base.getChildrenExplorerItems();
            }
            if ((arrExplorerItems == null || arrExplorerItems.length == 0) && (inheritedEI == null || inheritedEI.isEmpty())) {
                explorerItems = RadExplorerItems.EMPTY;
            } else {
                final Id[] itemsOrder = getChildrenExplorerItemsOrder(getId());
                explorerItems = new RadExplorerItems(arrExplorerItems, inheritedEI, itemsOrder, explicitelyInheritedItems);
            }
        }
        return explorerItems;
    }

    /**
     * Возвращает упорядоченный массив идентификаторов элементов проводника, в
     * составе дефиниции с указанным идентификатором. Реализация метода {@link IExplorerItemsHolder#getChildrenExplorerItemsOrder(Id)
     * }. Результат работы метода определяет порядок следования элементов
     * проводника в дереве. Метод получает порядок элементов, из
     * {@link RadExplorerItemsSettings настроек элементов проводника},
     * переданных в конструкторе. Если данный параграф не содержит настройки
     * упорядочивания элементов, то возвращается результат работы аналогичного
     * метода у базового параграфа (при его отсутствии возвращается пустой
     * массив).
     *
     * @param parentId идентификатор дефиниции верхнего уровня. Не может быть
     * <code>null</code>.
     * @return массив идентификаторов элементов проводника. Не может быть
     * <code>null</code>.
     */
    @Override
    public Id[] getChildrenExplorerItemsOrder(final Id parentId) {
        if (!Objects.equals(getId(), getOwnerClassId())) {
            final Definition ownerDefinition = getDefManager().getDefinition(getOwnerClassId());
            if (ownerDefinition instanceof IExplorerItemsHolder) {
                return ((IExplorerItemsHolder) ownerDefinition).getChildrenExplorerItemsOrder(parentId);
            }
        }
        final RadExplorerItemsSettings itemsSettings
                = childExplorerItemSettingsByParentId.get(parentId);
        if (itemsSettings == null || itemsSettings.getItemsOrder() == null) {
            final RadParagraphDef baseParagraph = getBaseParagraphDef();
            if (baseParagraph == null) {
                return new Id[]{};
            } else {
                final boolean isTopLevelItem = getId().equals(parentId);
                return baseParagraph.getChildrenExplorerItemsOrder(isTopLevelItem ? baseParagraph.getId() : parentId);
            }
        } else {
            return itemsSettings.getItemsOrder();
        }
    }

    /**
     * Возвращает признак того, что элемент проводника должен быть показан в
     * дереве. Реализация метода {@link IExplorerItemsHolder#isExplorerItemVisible(Id, Id)
     * }. М * Метод получает сведения о видимости элемента, из
     * {@link RadExplorerItemsSettings настроек элементов проводника},
     * переданных в конструкторе. Если данный параграф не содержит настройки
     * видимости для указанного элемента, то возвращается результат работы
     * аналогичного метода у базового параграфа (при его отсутствии возвращается
     * <code>true</code>).
     *
     * @param parentId идентификатор дефиниции верхнего уровня. Не может быть
     * <code>null</code>.
     * @param explorerItemId идентификатор вложенного элемента проводника. Не
     * может быть <code>null</code>.
     * @return <code>true</code> если элемент проводника должен быть показан в
     * дереве проводника и <code>false</code> в противном случае
     */
    @Override
    public boolean isExplorerItemVisible(final Id parentId, final Id explorerItemId) {
        if (!Objects.equals(getId(), getOwnerClassId())) {
            final Definition ownerDefinition = getDefManager().getDefinition(getOwnerClassId());
            if (ownerDefinition instanceof IExplorerItemsHolder) {
                return ((IExplorerItemsHolder) ownerDefinition).isExplorerItemVisible(parentId, explorerItemId);
            }
        }
        final boolean isTopLevelItem = getId().equals(parentId);
        for (RadParagraphDef paragraph = this; paragraph != null; paragraph = paragraph.getBaseParagraphDef()) {
            final RadExplorerItemsSettings itemsSettings
                    = childExplorerItemSettingsByParentId.get(isTopLevelItem ? paragraph.getId() : parentId);
            if (itemsSettings != null && itemsSettings.isItemVisibilityDefined(explorerItemId)) {
                return itemsSettings.isItemVisible(explorerItemId);
            }
        }
        return true;//assume explorer item is visible by default
    }
}
