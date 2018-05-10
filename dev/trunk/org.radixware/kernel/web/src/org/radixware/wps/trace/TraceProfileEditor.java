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

package org.radixware.wps.trace;

import org.radixware.kernel.common.html.Html;
import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEditor;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileEventSourceOptionsEditor;
import org.radixware.kernel.common.client.editors.traceprofile.ITraceProfileTreePresenter;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController.EventSeverity;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeNode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.trace.TraceProfile;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.InputBox.ValueController;

import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.INodeCellEditor;
import org.radixware.wps.rwt.tree.Node.INodeCellRenderer;
import org.radixware.wps.rwt.tree.Tree;
import org.radixware.wps.text.ECssPropertyName;
import org.radixware.wps.text.WpsTextOptions;
import org.radixware.wps.views.editors.valeditors.InputBoxController;

/**
 * Редактор профиля трассировки (Radix::Web.Widgets::TraceProfileEditor).
 * Реализация интерфейса {@link ITraceProfileEditor}, в окружении WEB с
 * использованием инстанции {@link TraceProfileTreeController}.
 *
 * @see ITraceProfileEditor
 */
public final class TraceProfileEditor extends Tree implements ITraceProfileEditor {

    private static class CellValue {

        private final String eventSource;
        private final TraceProfileTreeController.EventSeverity eventSeverity;
        private final Icon inheritanceIcon;
        private final String inheritanceTitle;
        private final boolean isInherited;
        private final boolean isReadOnly;

        public CellValue(final TraceProfileTreeNode profileNode, final MessageProvider mp) {
            eventSource = profileNode.getEventSource();
            eventSeverity = profileNode.getEventSeverity();
            isInherited = profileNode.eventSeverityWasInherited();
            final TraceProfileTreeController.EventSeverity parentNodeEventSeverity =
                    profileNode.getParentNode() == null ? null : profileNode.getParentNode().getEventSeverity();
            if (parentNodeEventSeverity == null) {
                inheritanceIcon = null;
                inheritanceTitle = null;
            } else {
                inheritanceIcon = parentNodeEventSeverity.getIcon();
                inheritanceTitle =
                        mp.translate("TraceDialog", "Inherit") + " (" + parentNodeEventSeverity.getTitle() + " )";
            }
            isReadOnly = profileNode.isReadOnly();
        }

        public EventSeverity getEventSeverity() {
            return eventSeverity;
        }

        public String getEventSource() {
            return eventSource;
        }

        public boolean isInherited() {
            return isInherited;
        }

        public boolean isReadOnly() {
            return isReadOnly;
        }

        public Icon getInheritanceIcon() {
            return inheritanceIcon;
        }

        public String getInheritanceTitle() {
            return inheritanceTitle;
        }
    }

    private static class EventSeverityCellEditor implements INodeCellEditor {

        private static class DropDownListDelegateImpl extends DropDownListDelegate<String> {

            private final List<DropDownListItem<String>> listBoxItems;

            public DropDownListDelegateImpl(final IClientEnvironment env, final List<TraceProfileTreeController.EventSeverity> eventSeverityList, final CellValue value) {
                listBoxItems = new LinkedList<>();
                if (value.getInheritanceIcon() != null) {
                    final DropDownListItem<String> inheritanceItem =
                            new DropDownListItem<>(value.getInheritanceTitle(), null, value.getInheritanceIcon());
                    inheritanceItem.setForeground(Color.gray);
                    listBoxItems.add(inheritanceItem);
                }
                DropDownListItem<String> dropDownItem;
                for (TraceProfileTreeController.EventSeverity eventSeverity : eventSeverityList) {
                    dropDownItem = new DropDownListItem<>(eventSeverity.getTitle(), eventSeverity.getValue(), eventSeverity.getIcon());
                    listBoxItems.add(dropDownItem);
                }
            }

            @Override
            protected List<DropDownListItem<String>> getItems() {
                return listBoxItems;
            }
        }

        public static class EventSeverityEditorController extends InputBoxController<String, EditMaskList> {

            private final DropDownListDelegate<String> dropDownDelegate;
            private final Icon inheritanceIcon;
            private ToolButton dropDownButton;

            public EventSeverityEditorController(final IClientEnvironment environment,
                    final DropDownListDelegate<String> dropDownDelegate,
                    final Icon inheritanceIcon) {
                super(environment);
                this.dropDownDelegate = dropDownDelegate;
                this.inheritanceIcon = inheritanceIcon;
            }

            @Override
            protected ValueController<String> createValueController() {
                return null;
            }

            @Override
            @SuppressWarnings("unchecked")
            protected void applyEditMask(final InputBox box) {
                dropDownButton = box.addDropDownDelegate(dropDownDelegate);
                box.setClearController(null);
                super.applyEditMask(box);
            }

            @Override
            public void setValue(final String value) {
                super.setValue(value);
                if (value == null) {
                    getInputBox().setValueIcon(inheritanceIcon);
                }
            }

            public void expose() {
                if (dropDownButton != null) {
                    dropDownButton.getAction().trigger();
                }
            }
        }
        private final TraceProfileTreeController<Node.DefaultNode> treeController;
        private final List<TraceProfileTreeController.EventSeverity> eventSeverityList;
        private String eventSource;
        private EventSeverityEditorController editorController;

        public EventSeverityCellEditor(final List<TraceProfileTreeController.EventSeverity> eventSeverityList,
                final TraceProfileTreeController<Node.DefaultNode> controller) {
            treeController = controller;
            this.eventSeverityList = eventSeverityList;
        }

        @Override
        public void setValue(final Node node, final int c, final Object value) {
            final CellValue cellValue = (CellValue) value;
            if (cellValue.isReadOnly()) {
                editorController = null;
                eventSource = null;
            } else {
                final DropDownListDelegateImpl dropDownListDelegate =
                        new DropDownListDelegateImpl(treeController.getEnvironment(), eventSeverityList, cellValue);
                editorController =
                        new EventSeverityEditorController(treeController.getEnvironment(), dropDownListDelegate, cellValue.getInheritanceIcon());
                eventSource = cellValue.getEventSource();
                final EditMaskList editMaskList = new EditMaskList();
                editMaskList.setNoValueStr(cellValue.getInheritanceTitle());
                for (TraceProfileTreeController.EventSeverity eventSeverity : eventSeverityList) {
                    editMaskList.addItem(eventSeverity.getTitle(), eventSeverity.getValue());
                }
                editorController.setEditMask(editMaskList);
                editorController.setValue(cellValue.isInherited() ? null : cellValue.getEventSeverity().getValue());
                editorController.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {

                    @Override
                    public void onValueChanged(String oldValue, String newValue) {
                        applyChanges();
                    }
                });
            }
        }

        @Override
        public CellValue getValue() {
            return null;
        }

        @Override
        public void applyChanges() {
            treeController.changeEventSeverity(eventSource, editorController.getValue(), "");
        }

        @Override
        public void cancelChanges() {
        }

        @Override
        public UIObject getUI() {
            return editorController == null ? null : (UIObject) editorController.getValEditor();
        }
    }

    private static class EventSeverityCellEditorProvider implements ICellEditorProvider {

        private final List<TraceProfileTreeController.EventSeverity> eventSeverityList;
        final TraceProfileTreeController<Node.DefaultNode> treeController;

        public EventSeverityCellEditorProvider(final TraceProfileTreeController<Node.DefaultNode> controller) {
            this.treeController = controller;
            eventSeverityList =
                    TraceProfileTreeController.getEventSeverityItemsByOrder(controller.getEnvironment());
        }

        @Override
        public EventSeverityCellEditor newCellEditor(final Node node, final int c) {
            return new EventSeverityCellEditor(eventSeverityList, treeController);
        }
    }

    private static class EventSeverityCellRenderer extends UIObject implements INodeCellRenderer {

        private final Html labelComponent = new Html("label");
        private final IClientEnvironment environment;

        public EventSeverityCellRenderer(final IClientEnvironment environment) {
            super(new Div());
            this.environment = environment;
            html.add(labelComponent);
        }

        @Override
        public void update(final Node node, final int c, final Object value) {
            final CellValue cellValue = (CellValue) value;            
            final String text;
            final ETextOptionsMarker marker;
            if (cellValue.isInherited()) {
                text = cellValue.getInheritanceTitle();
                marker = ETextOptionsMarker.INHERITED_VALUE;
            } else {
                text = cellValue.getEventSeverity().getTitle();
                marker = ETextOptionsMarker.REGULAR_VALUE;
            }
            final Map<ECssPropertyName, String> css = 
                WpsTextOptions.Factory.getOptions((WpsEnvironment)environment, marker).getCssPropertyValues();
            labelComponent.setInnerText(text);
            for (Map.Entry<ECssPropertyName, String> e : css.entrySet()) {
                if (e.getKey()!=ECssPropertyName.BACKGROUND_COLOR){
                    labelComponent.setCss(e.getKey().getPropertyName(), e.getValue());
                }
            }
        }

        @Override
        public void selectionChanged(final Node node, final int c, final Object value, final ICell cell, final boolean isSelected) {
            final ETextOptionsMarker marker;
            if (((CellValue) value).isInherited()) {
                marker = ETextOptionsMarker.INHERITED_VALUE;
            } else {
                marker = ETextOptionsMarker.REGULAR_VALUE;
            }
            final Map<ECssPropertyName, String> css = 
                WpsTextOptions.Factory.getOptions((WpsEnvironment)environment, marker).getCssPropertyValues();            
            for (Map.Entry<ECssPropertyName, String> e : css.entrySet()) {
                if (e.getKey()!=ECssPropertyName.BACKGROUND_COLOR){
                    labelComponent.setCss(e.getKey().getPropertyName(), e.getValue());
                }
            }
            if (isSelected) {
                labelComponent.setCss("color", "white");
            }
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
        }

        @Override
        public UIObject getUI() {
            return this;
        }
    }

    private static class EventSeverityCellRendererProvider implements ICellRendererProvider {

        private IClientEnvironment environment;

        public EventSeverityCellRendererProvider(final IClientEnvironment environment) {
            this.environment = environment;
        }

        @Override
        public INodeCellRenderer newCellRenderer(Node n, int c) {
            return new EventSeverityCellRenderer(environment);
        }
    }

    private static class TraceProfileTreePresenter implements ITraceProfileTreePresenter<Node.DefaultNode> {

        private final MessageProvider messageProvider;

        public TraceProfileTreePresenter(final MessageProvider mp) {
            messageProvider = mp;
        }

        @Override
        public Node.DefaultNode createTreeNodeWidget(final TraceProfileTreeNode<Node.DefaultNode> treeNode) {
            final Node.DefaultNode newNode = new Node.DefaultNode(treeNode.getTitle());
            newNode.setUserData(treeNode);
            if (treeNode.getParentNode() != null) {
                treeNode.getParentNode().getWidget().add(newNode);
            }
            return newNode;
        }

        @Override
        public void presentWidget(final TraceProfileTreeNode<Node.DefaultNode> profileNode) {
            final Node treeNode = profileNode.getWidget();
            treeNode.finishEdit();
            treeNode.setDisplayName(profileNode.getTitle());
            treeNode.setToolTip(profileNode.getEventSource());
            treeNode.setCellValue(1, new CellValue(profileNode, messageProvider));
        }

        @Override
        public void destroyPresentations() {
        }

        @Override
        public ITraceProfileEventSourceOptionsEditor createEventSourceOptionsEditor(final EEventSource eventSource, final EEventSeverity eventSeverity, final TraceProfile.EventSourceOptions options) {
            return null;
        }                
    }
    
    private boolean isReadOnly;
    private final TraceProfileTreeController<Node.DefaultNode> controller;
    private final Tree.Column eventSeverityColumn;

    /**
     * Конструктор редактора профиля трассировки.
     *
     * @param environment текущее окружение
     * @param isReadOnly признак работы в режиме "только чтение"
     */
    public TraceProfileEditor(final IClientEnvironment environment, final boolean isReadOnly) {
        super();
        final String eventSourceColumnTitle = environment.getMessageProvider().translate("TraceDialog", "Event Source");
        final String eventSeverityColumnTitle = environment.getMessageProvider().translate("TraceDialog", "Event Severity");
        getTreeColumn().setTitle(eventSourceColumnTitle);
        getTreeColumn().setPersistenceKey("RdxTraceProfileTreeSrc");
        eventSeverityColumn = addColumn(eventSeverityColumnTitle);
        controller =
                new TraceProfileTreeController<>(environment, new TraceProfileTreePresenter(environment.getMessageProvider()));
        setPersistenceKey("RdxTraceProfileTree");
        eventSeverityColumn.setPersistenceKey("RdxTraceProfileTreeSeverity");
        eventSeverityColumn.setCellRendererProvider(new EventSeverityCellRendererProvider(environment));
        setReadOnly(isReadOnly);
        afterOpen();
    }

    private void afterOpen() {
        eventSeverityColumn.setCellEditorProvider(isReadOnly() ? null : new EventSeverityCellEditorProvider(controller));
        setRootNode(controller.getRootNode().getWidget());
        controller.getRootNode().getWidget().expand();
    }

    //implementation of ITraceProfileEditor
    /**
     * Устанавливает профиль трассировки. Реализация метода {@link ITraceProfileEditor#setTraceProfile(java.lang.String)
     * }. При вызове управление передается методу {@link TraceProfileTreeController#setProfile(java.lang.String)
     * }.
     *
     * @param traceProfileAsStr строковое представление профиля трассировки.
     * Значение равное
     * <code>null</code> или
     * <code>""</code> равносильно значению
     * <code>"None"</code>.
     * @throws org.radixware.kernel.common.exceptions.WrongFormatError если
     * переданная строка имеет неправильный формат или не найдено описание для
     * указанного в ней источника события или уровеня важности сообщения.
     * @see #getTraceProfile()
     */
    @Override
    public void setTraceProfile(final String traceProfileAsStr) {
        controller.setProfile(traceProfileAsStr);
    }

    /**
     * Возвращает строковое представление профиля трассировки. Реализация метода {@link ITraceProfileEditor#getTraceProfile()
     * }, возвращает результат работы
     * {@link TraceProfileTreeController#getProfile() }.
     *
     * @return строковое представление профиля трассировки. Значение не может
     * быть
     * <code>null</code> или пустой строкой.
     * @see #setTraceProfile(java.lang.String)
     */
    @Override
    public String getTraceProfile() {
        return controller.getProfile();
    }

    /**
     * Возвращает признак того, что уровень важности сообщения был изменен
     * пользователем. Метод возвращает результат вызова {@link TraceProfileTreeController#isEdited()
     * }.
     *
     * @return
     * <code>true</code>, если пользователь изменил уровень важности события для
     * хотябы одного источника, иначе -
     * <code>false</code>
     */
    @Override
    public boolean isEdited() {
        return controller.isEdited();
    }

    /**
     * Добавляет обработчик изменения уровня важности события пользователем.
     * Реализация вызывает метод {@link TraceProfileTreeController#addListener(ITraceProfileEditor.IEventSeverityChangeListener)
     * }.
     *
     * @param listener инстанция обработчика, который необходимо
     * зарегистрировать
     * @see #removeListener(IEventSeverityChangeListener)
     */
    @Override
    public void addListener(final IEventSeverityChangeListener listener) {
        controller.addListener(listener);
    }

    /**
     * Удаляет обработчик изменения уровня важности события пользователем.
     * Реализация вызывает метод {@link TraceProfileTreeController#removeListener(ITraceProfileEditor.IEventSeverityChangeListener)
     * }
     *
     * @param listener инстанция обработчика, зарегистрированного в методе {@link #addListener(IEventSeverityChangeListener)
     * }
     * @see #addListener(IEventSeverityChangeListener)
     */
    @Override
    public void removeListener(final IEventSeverityChangeListener listener) {
        controller.removeListener(listener);
    }

    /**
     * Устанавливает режим "только чтение". Если переданный параметр равен
     * <code>true</code>, то пользователь не сможет изменять текущие значения
     * уровней важности событий.
     *
     * @param isReadOnly логическое значение включающее или выключающее режим
     * "только чтение"
     * @see #isReadOnly()
     */
    @Override
    public void setReadOnly(final boolean isReadOnly) {
        if (isReadOnly != this.isReadOnly) {
            this.isReadOnly = isReadOnly;
            eventSeverityColumn.setCellEditorProvider(isReadOnly() ? null : new EventSeverityCellEditorProvider(controller));
        }
    }

    /**
     * Возвращает признак работы редактора в режиме "только чтение"
     *
     * @return
     * <code>true</code> если редактор находится в режиме "только чтение" и
     * <code>false</code> в противном случае.
     * @see #setReadOnly(boolean)
     */
    @Override
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * Устанавливает набор источников событий, с запрещенной трассировкой.
     * Реализация вызывает метод {@link TraceProfileTreeController#setRestrictedEventSources(java.util.Collection)
     * }.
     *
     * @param eventSources набор c именами источников событий
     */
    @Override
    public void setRestrictedEventSources(final Collection<String> eventSources) {
        controller.setRestrictedEventSources(eventSources);
    }

    /**
     * Возвращает набор имен всех источников событий, отображаемых на данный
     * момент в редакторе. Реализация возвращает результат работы метода {@link TraceProfileTreeController#getEventSources()
     * }.
     *
     * @return набор имен всех источников событий
     */
    @Override
    public Collection<String> getEventSources() {
        return controller.getEventSources();
    }

    /**
     * Обновляет текущий набор источников событий. Реализация вызывает метод {@link TraceProfileTreeController#rereadEventSources()
     * }.
     *
     * @param profileAsStr строковое представление профиля трассировки, который
     * следует установить после обновления набора источников событий. Если
     * значение равно <codr>null</code>, то будет установлен тот профиль
     * трассировки, который был на момент вызова метода.
     */
    @Override
    public void rereadEventSources(final String profileAsStr) {
        controller.rereadEventSources(profileAsStr);
        afterOpen();
    }

    @Override
    protected String[] clientScriptsRequired() {
        final String[] parentScripts = super.clientScriptsRequired();
        final String[] result = new String[parentScripts.length+1];
        System.arraycopy(parentScripts, 0, result, 0, parentScripts.length);
        result[parentScripts.length] = "org/radixware/wps/rwt/tree/inputBox.js";
        return result;
    }
    
    
}