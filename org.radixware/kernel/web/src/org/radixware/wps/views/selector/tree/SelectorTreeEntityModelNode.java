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

import java.util.*;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.rwt.Alignment;
import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.settings.ISettingsChangeListener;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.selector.PropertyCellRendererProvider;
import org.radixware.wps.views.selector.PropertyTreeCellEditorProvider;

/**
 * Стандартный узел древовидного селектора, ассоциированный с моделью сущности
 * (Radix::Web.Widgets.SelectorTree::EntityModelNode). Класс узла древовидного селектора, который используется
 * для показа значений свойств объекта сущности. Для формировании узлов следующего уровня используется метод
 * дерева {@link IRwtSelectorTree#initChildren(SelectorTreeNode) initChildren}. Узел считается конечным если
 * метод дерева {@link IRwtSelectorTree#hasChildNodes(SelectorTreeNode)  hasChildNodes()} возвращает для него
 * <code>false</code>.
 *
 */
public class SelectorTreeEntityModelNode extends SelectorTreeNode {

    private final EntityModel entityModel;
    private final IRwtSelectorTree tree;
    private final Map<Id, Id> columnsMap = new HashMap<>();
    private final ISettingsChangeListener l = new ISettingsChangeListener() {
        @Override
        public void onSettingsChanged() {
            updateTextOptions();
        }
    };

    /**
     * Конструктор стандартного узла древовидного селектора.
     *
     * @param entityModel объект сущности, значения свойств которого, требуется показать в узле дерева
     * @param selectorTree интерфейс древовидного селектора
     * @param columnsMap карта соответствия идентификаторов колонок в презентации селектора идентификаторам
     * свойств объекта
     */
    public SelectorTreeEntityModelNode(final EntityModel entityModel, final IRwtSelectorTree selectorTree, Map<Id, Id> columnsMap) {
        super(selectorTree);
        if (entityModel.getContext() instanceof IContext.SelectorRow == false) {
            throw new IllegalUsageError("EntityModel instance should have SelectorRow context");
        }
        this.entityModel = entityModel;
        this.tree = selectorTree;
        if (columnsMap != null) {
            this.columnsMap.putAll(columnsMap);
        }
        setUserData(entityModel);
       // initCells();
        updateNodeDisplayName();

        ((WpsEnvironment) getEnvironment()).addSettingsChangeListener(l);
    }

    /**
     * Получение модели сущности. Метод возвращает объект сущности, значения свойств которого, показываются в
     * данном узле дерева.
     *
     * @return объект сущности
     */
    public final EntityModel getEntityModel() {
        return entityModel;
    }

    /**
     * Получение модели группы. Метод возвращает модель группы, которая содержит объект сущности,
     * соответствующий данному узлу дерева.
     *
     * @return модель группы
     */
    public final GroupModel getGroupModel() {
        return ((IContext.SelectorRow) entityModel.getContext()).parentGroupModel;
    }

    protected final void updateTextOptions() {
        final List<SelectorColumnModelItem> columns = tree.getSelectorColumns();
        Property property;
        int visibleColumnIndex = 0;

        for (SelectorColumnModelItem column : columns) {
            if (column.isVisible()) {
                property = getPropertyForColumn(column);
                Alignment alignment = applyBodyAlignmentSettings(property, column.getAlignment());
                WpsTextOptions options = getTextOptions(property).changeAlignment(ETextAlignment.fromStr(alignment.name()));
                setColumnTextOptions(visibleColumnIndex, options);
            }
            visibleColumnIndex++;
        }
    }
    
    protected final void updateNodeDisplayName(){
        final List<SelectorColumnModelItem> columns = tree.getSelectorColumns();
        int visibleColumnIndex = 0;
        for (SelectorColumnModelItem column : columns) {
            if (column.isVisible()) {
                setupCell(visibleColumnIndex, column);
                visibleColumnIndex++;
            }
        }
    }
    
    public final void setupCell(final int index, final SelectorColumnModelItem column){
        final Property property = getPropertyForColumn(column);
        final Alignment alignment = applyBodyAlignmentSettings(property, column.getAlignment());
        final WpsTextOptions options = getTextOptions(property).changeAlignment(ETextAlignment.fromStr(alignment.name()));
        setColumnTextOptions(index, options);
        if (property != null && property.isVisible()) {
            setCellRendererProvider(index, PropertyCellRendererProvider.getInstanceForTree());
            if (property.isEnabled()) {
                setCellEditorProvider(index, PropertyTreeCellEditorProvider.getInstance());
            }
            setCellValue(index, property);
        } else {
            setCellValue(index, "");
        }
    }

    private Alignment applyBodyAlignmentSettings(Property prop, ESelectorColumnAlign defaultAlign) {
        ESelectorColumnAlign align = null;
        Alignment alignment;
        if (prop == null) {
            return null;
        } else {
            WpsSettings settings = ((WpsEnvironment) prop.getEnvironment()).getConfigStore();
            try {
                if (defaultAlign != ESelectorColumnAlign.DEFAULT) {
                    align = defaultAlign;
                } else {
                    EValType valType = prop.getType();
                    settings.beginGroup(SettingNames.SYSTEM);
                    settings.beginGroup(SettingNames.SELECTOR_GROUP);
                    settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                    settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
                    Long a = new Long(settings.readInteger(valType.getName(), defaultAlign.getValue().intValue()));
                    align = ESelectorColumnAlign.getForValue(a);
                }
            } finally {
                if (settings.group() != null) {
                    settings.endGroup();
                    settings.endGroup();
                    settings.endGroup();
                    settings.endGroup();
                }
                switch (align) {
                    case CENTER:
                        alignment = Alignment.CENTER;
                        break;
                    case DEFAULT:
                        alignment = Alignment.LEFT;
                        break;
                    case LEFT:
                        alignment = Alignment.LEFT;
                        break;
                    case RIGHT:
                        alignment = Alignment.RIGHT;
                        break;
                    default:
                        alignment = Alignment.LEFT;
                        break;
                }
            }
        }
        return alignment;
    }

    public WpsTextOptions getTextOptions(Property property) {
        if (property == null) {
            return WpsTextOptions.Factory.getOptions((WpsEnvironment) getEnvironment(), ETextOptionsMarker.SELECTOR_ROW);
        } else {
            final EnumSet<ETextOptionsMarker> propertyMarkers = property.getTextOptionsMarkers();
            if (propertyMarkers.contains(ETextOptionsMarker.INVALID_VALUE)) {
                propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
            }
            return (WpsTextOptions) property.getValueTextOptions().getOptions(propertyMarkers);
        }
    }

    /**
     * Сопоставление свойства объекта колонке селектора. Для заданной колонки селектора метод возвращает
     * соответствующее ей свойство объекта сущности. В стандартной реализации для получения свойства объекта
     * используется карта, переданная в конструкторе.
     *
     * @param column колонка селектора
     * @return свойство объекта. Может быть <code>null</code>
     */
    protected Property getPropertyForColumn(final SelectorColumnModelItem column) {
        final Id propertyId = columnsMap.get(column.getId());
        return propertyId == null ? null : getEntityModel().getProperty(propertyId);
    }

    /**
     * Удаление узла. После выполнения метода базового класса происходит удаление соответствующей строки из
     * модели группы.
     */
    @Override
    public void remove() {
        super.remove();
        if (entityModel != null && entityModel.getPid() != null) {
            final int idx = getGroupModel().findEntityByPid(entityModel.getPid());
            if (idx >= 0) {
                getGroupModel().removeRow(idx);
            }
        }
        if (l != null) {
            ((WpsEnvironment) getEnvironment()).removeSettingsChangeListener(l);
        }
    }

    /**
     * Получение списка дочерних моделей групп. Метод возвращает список дочерних моделей групп для данного
     * узла. На основе содержимого этих групп будут созданы узлы следующего уровня вложенности. Стандартная
     * реализация использует набор дочерних элементов, установленный в методе древовидного селектора
     * {@link IRwtSelectorTree#initChildren(SelectorTreeNode)  initChildren}. Если этот набор является
     * инстанцией {@link SelectorTreeChildNodes}, то для создания моделей групп будет использован набор
     * параметров, который возвращает метод
     * {@link SelectorTreeChildNodes#getChildGroupModelSettings()  getChildGroupModelSettings}, иначе метод
     * возвращает пустой список. При создании модели группы на основе параметров из
     * {@link ChildGroupModelSettings} учитываются результаты вызовов методов
     * {@link ChildGroupModelSettings#groupIsEmpty(EntityModel)  ChildGroupModelSettings.groupIsEmpty} и
     * {@link ChildGroupModelSettings#canCreateObject() ChildGroupModelSettings.canCreateObject}. Модель
     * группы не будет создана если метод
     * <code>groupIsEmpty</code> вернул
     * <code>true</code>, а метод
     * <code>canCreateObject</code> вернул
     * <code>false</code>. Если для создания модели группы используется дочерний элемент проводника, то
     * проверяется доступность этого элемента в объекте сущности, ассоциированного с данным узлом дерева.
     *
     * @return список моделей групп
     */
    public List<GroupModel> createChildGroupModels() {
        if (getChildren() instanceof SelectorTreeChildNodes) {
            final List<ChildGroupModelSettings> settings = ((SelectorTreeChildNodes) getChildren()).getChildGroupModelSettings();
            return ChildGroupModelSettings.createChildGroupModels(settings, getEntityModel());
        } else {
            return Collections.<GroupModel>emptyList();
        }
    }

    /**
     * Поиск подузлов. Метод для поиска дочерних узлов, с которыми ассоциированы объекты сущности с указанным
     * идентификатором. Для получения объекта сущности из дочернего узла используется метод
     * <code>getUserData()</code>. Поиск выполняется нерекурсивно.
     *
     * @param pid идентификатор объекта сущности
     * @return список узлов следующего уровня вложенности, с которыми ассоциированы объекты сущности с
     * указанным идентификатором
     */
    public List<Node> findChildNodes(final Pid pid) {
        final List<Node> nodes = new LinkedList<>();
        if (pid != null) {
            for (Node node : getChildren().getNodes()) {
                if (node.getUserData() instanceof EntityModel && pid.equals(((EntityModel) node.getUserData()).getPid())) {
                    nodes.add(node);
                }
            }
        }
        return nodes;
    }

    /**
     * Получение строки с описанием данного узла.
     *
     * @return Возвращает строку с описанием узла, содержащую заголовок модели объекта сущности.
     */
    @Override
    public String getDescription() {
        final String desc = getEnvironment().getMessageProvider().translate("Selector", "node with entity object \'%s\'");
        return String.format(desc, getEntityModel().getTitle());
    }
}