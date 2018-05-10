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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QMargins;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.PropertiesGroupModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter.IPresenterItem;
import org.radixware.kernel.common.client.widgets.propertiesgrid.PropertiesGridController;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.types.ICachableWidget;
import org.radixware.kernel.explorer.views.EditorPageView;
import org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditorsPool;


public class PropertiesGrid extends QScrollArea implements IExplorerModelWidget, ICachableWidget {

    public static final class SetFocusEvent extends QEvent {

        public SetFocusEvent() {
            super(QEvent.Type.User);
        }
    }

    private final class Presenter implements IPropertiesGridPresenter<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> {

        private boolean firstUpdate = true;
        private final int minimumEditorCellWidth;
        private final Map<IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget>, java.awt.Point> tabOrderForEditor = new HashMap<>();
        private AlignmentFlag headerAlignmentFlag;

        public Presenter() {
            final QFontMetrics fm = ExplorerTextOptions.getDefault().getFont().getQFontMetrics();
            minimumEditorCellWidth = fm.maxWidth() + 4;
        }

        @Override
        public PropLabel createPropertyLabel(final Property property) {
            final PropLabel label =
                    PropLabelsPool.getInstance().getPropLabel(PropertiesGrid.this.content, property);
            label.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
            label.setObjectName("propLabel #" + property.getId());
            return label;
        }

        @Override
        public IExplorerModelWidget createPropertyEditor(final Property property) {
            final IExplorerModelWidget editor =
                    (IExplorerModelWidget) property.getOwner().createPropertyEditor(property.getId());
            editor.asQWidget().setParent(PropertiesGrid.this.content);
            editor.asQWidget().setObjectName("propEditor #" + property.getId());
            //editor.asQWidget().setWindowTitle(property.getTitle());
            if (property.getDefinition().getType() == EValType.BOOL) {
                editor.asQWidget().setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
            } else {
                editor.asQWidget().setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
            }
            return editor;
        }                

        @Override
        public PropertiesGroupWidget createPropertiesGroup(final PropertiesGroupModelItem propertiesGroup) {
            final PropertiesGroupWidget propGroup = new PropertiesGroupWidget(propertiesGroup, PropertiesGrid.this.controller, PropertiesGrid.this, PropertiesGrid.this.content);
            propGroup.asQWidget().setObjectName("propGroup #"+propertiesGroup.getId());
            return propGroup;
        }

        @Override
        public void destroyWidgets(final PropLabel label, 
                                                 final IExplorerModelWidget editor,
                                                 final PropertiesGroupWidget group) {
            if (editor != null
                    && (editor.asQWidget() instanceof PropEditor == false
                    || !PropEditorsPool.getInstance().cachePropEditor((PropEditor) editor.asQWidget()))) {
                editor.asQWidget().setVisible(false);
                editor.asQWidget().close();
            }
            if (label != null) {
                PropLabelsPool.getInstance().cachePropLabel(label);
            }
            if (group!=null){
                group.close();
            }
        }

        @Override
        public int getCellHeight(final IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> item) {
            if (item.getPropertyEditor()!=null){
                return item.getPropertyEditor().asQWidget().sizeHint().height();
            }else if (item.getPropertiesGroupWidget()!=null){
                return item.getPropertiesGroupWidget().getMaxRowHeight();
            }
            return 0;
        }

        @Override
        public void beforeUpdateCellsPresentation(final int columnsCount, final int rowsCount) {
            PropertiesGrid.this.setUpdatesEnabled(false);
            ExplorerSettings settings = (ExplorerSettings)environment.getConfigStore();
                AlignmentFlag alignmentFlag;
                try {
                    settings.beginGroup(SettingNames.SYSTEM);
                    settings.beginGroup(SettingNames.EDITOR_GROUP);
                    settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
                    alignmentFlag = settings.readAlignmentFlag(SettingNames.Editor.Common.TITLES_ALIGNMENT);
                } finally {
                    settings.endGroup();
                    settings.endGroup();
                    settings.endGroup();
                }
                this.headerAlignmentFlag = alignmentFlag;
            if (firstUpdate) {
                setWidget(PropertiesGrid.this.content);
            } else {
                final HierarchicalGridLayout layout = PropertiesGrid.this.layout;
                QLayoutItemInterface item;
                for (int col = layout.columnCount() - 1; col >= 0; col--) {
                    item = layout.itemAtPosition(0, col);
                    if (item instanceof QSpacerItem) {
                        layout.removeItem(item);
                    }
                }
            }
        }

        @Override
        public void presentCell(final IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> item, final int columnsCount) {
            if (item.getModelItem() instanceof Property){
                final Property property = (Property)item.getModelItem();
                final EValType propertyType = property.getDefinition().getType();
                final Qt.AlignmentFlag titleAlignment;
                final HierarchicalGridLayout layout = PropertiesGrid.this.layout;            
                final PropLabel label = item.getPropertyLabel();
                final QWidget editor = item.getPropertyEditor().asQWidget();
                final int row = item.getRow(), column = item.getColumn();
                final int span = item.getColumnSpan() * 3 - 2;

                if (editor instanceof AbstractPropEditor) {
                    titleAlignment = ((AbstractPropEditor) editor).getTitleAlignment();
                } else {
                    titleAlignment = null;
                }
                if (columnsCount > 1) {
                    if (titleAlignment == null) {
                        layout.addWidget(label, row, column * 3, Qt.AlignmentFlag.AlignVCenter, headerAlignmentFlag);
                    } else {
                        layout.addWidget(label, row, column * 3, titleAlignment, headerAlignmentFlag);
                    }
                    if (propertyType == EValType.BOOL) {
                        layout.addWidget(editor, row, column * 3 + 1, 1, span, Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
                    } else {
                        layout.addWidget(editor, row, column * 3 + 1, 1, span, Qt.AlignmentFlag.AlignVCenter);
                    }
                } else {
                    if (titleAlignment == null) {
                        layout.addWidget(label, row, column, Qt.AlignmentFlag.AlignVCenter, headerAlignmentFlag);
                    } else {
                        layout.addWidget(label, row, column, titleAlignment, Qt.AlignmentFlag.AlignVCenter, headerAlignmentFlag);
                    }
                    if (propertyType == EValType.BOOL) {
                        layout.addWidget(editor, row, column + 1, 1, span, Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
                    } else {
                        layout.addWidget(editor, row, column + 1, 1, span, Qt.AlignmentFlag.AlignVCenter);
                    }
                }

                if (editor instanceof AbstractPropEditor) {
                    AbstractPropEditor pEditor = (AbstractPropEditor) editor;
                    pEditor.focused.connect(PropertiesGrid.this, "editorFocusIn()");
                    pEditor.unfocused.connect(PropertiesGrid.this, "editorFocusOut()");
                }
                tabOrderForEditor.put(item, new Point(column, row));
            }else if (item.getModelItem() instanceof PropertiesGroupModelItem){
                final HierarchicalGridLayout layout = PropertiesGrid.this.layout;            
                final PropertiesGroupWidget group = 
                        (PropertiesGroupWidget)item.getPropertiesGroupWidget().asQWidget();
                final HierarchicalGridLayout groupLayout = group.getPropertiesGrid().layout;
                final int row = item.getRow(), column = item.getColumn();
                final int rowSpan = item.getRowSpan(), columnSpan = item.getColumnSpan() * 3 - 1;                
                if (columnsCount>1){
                    layout.addWidget(group, row, column*3, rowSpan, columnSpan, Qt.AlignmentFlag.AlignTop);
                    layout.linkChildLayout(groupLayout, row, column*3);
                }else{
                    layout.addWidget(group, row, column, rowSpan, columnSpan, Qt.AlignmentFlag.AlignTop);
                    layout.linkChildLayout(groupLayout, row, column);
                }
                tabOrderForEditor.put(item, new Point(column, row));
            }
        }

        @Override
        public void presentSpanColumn(final int col) {
            PropertiesGrid.this.layout.addItem(new QSpacerItem(minimumEditorCellWidth, 0, QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.Minimum), 0, col * 3 + 1);
        }

        @Override
        public void presentSpanRow(final int row) {
        }

        @Override
        public void clearCellPresentation(final IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> item) {
            final IExplorerModelWidget editor = item.getPropertyEditor();
            if (editor!=null){
                final HierarchicalGridLayout layout = PropertiesGrid.this.layout;
                if (PropertiesGrid.this.currentEditor == editor) {//NOPMD
                    PropertiesGrid.this.currentEditor = null;
                }
                if (editor instanceof IPropEditor) {
                    ((IPropEditor) editor).finishEdit();
                }
                layout.removeWidget(item.getPropertyLabel());
                layout.removeWidget(editor.asQWidget());
                tabOrderForEditor.remove(item);
            }
            final PropertiesGroupWidget group = item.getPropertiesGroupWidget();
            if (group!=null){
                group.finishEdit();
                group.refresh(group.getPropertiesGroup());
                final HierarchicalGridLayout layout = PropertiesGrid.this.layout;                
                layout.removeWidget(group.asQWidget());                
                tabOrderForEditor.remove(item);                
            }
            currentItems.remove(item);
        }

        @Override
        public void afterUpdateCellsPresentation() {
            if (firstUpdate) {
                PropertiesGrid.this.setFocusProxy(PropertiesGrid.this.content);
                PropertiesGrid.this.setWidgetResizable(true);
            }
            final HierarchicalGridLayout layout = PropertiesGrid.this.layout;
            QWidget itemWidget;
            final int[] rowsHeight = new int[layout.rowCount()]; 
            Arrays.fill(rowsHeight, 0);
            for (int row=0,rowsCount=rowsHeight.length; row<rowsCount; row++){
                for (int col=0,columnCount=layout.columnCount(); col<columnCount; col++){
                    if (layout.itemAtPosition(row, col)!=null){
                        itemWidget = layout.itemAtPosition(row, col).widget();
                        if (itemWidget instanceof PropertiesGroupWidget){
                            itemWidget.setMinimumHeight(0);
                            rowsHeight[row] = Math.max(rowsHeight[row], itemWidget.sizeHint().height());
                        }
                    }
                }
            }
            for (int row=0,rowsCount=rowsHeight.length; row<rowsCount; row++){
                final int rowHeight = rowsHeight[row];
                if (rowHeight>0){
                    for (int col=0,columnCount=layout.columnCount(); col<columnCount; col++){
                        if (layout.itemAtPosition(row, col)!=null){
                            itemWidget = layout.itemAtPosition(row, col).widget();
                            if (itemWidget instanceof PropertiesGroupWidget && itemWidget.sizeHint().height()<rowHeight){
                                itemWidget.setMinimumHeight(rowHeight);
                            }
                        }
                    }
                }
            }            
            PropertiesGrid.this.setUpdatesEnabled(true);
            layout.invalidate();
            PropertiesGrid.this.updateColumnsWidth();
            PropertiesGrid.this.updateGeometry();
            firstUpdate = false;
            final List<IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget>> items = new ArrayList<>(tabOrderForEditor.keySet());
            final Comparator<IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget>> editorsComparator = 
                    new Comparator<IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget>>() {
                        @Override
                        public int compare(final IPresenterItem<PropLabel, IExplorerModelWidget,PropertiesGroupWidget> i1, final IPresenterItem<PropLabel, IExplorerModelWidget,PropertiesGroupWidget> i2) {
                            final java.awt.Point p1 = tabOrderForEditor.get(i1);
                            final java.awt.Point p2 = tabOrderForEditor.get(i2);
                            final int rowsComparing = Integer.compare(p1.y, p2.y);
                            return rowsComparing == 0 ? Integer.compare(p1.x, p2.x) : rowsComparing;
                        }
            };
            Collections.sort(items, editorsComparator);
            tabOrder.clear();
            currentItems.clear();
            currentItems.addAll(items);
            startFocusWidget = null;
            QWidget cellWidget;
            for (IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> item : items) {               
                if (item.getPropertyEditor()!=null){
                    cellWidget = item.getPropertyEditor().asQWidget();
                }else if (item.getPropertiesGroupWidget()!=null){
                    cellWidget = item.getPropertiesGroupWidget().asQWidget();
                }else{
                    continue;
                }
                if (cellWidget.focusProxy() != null) {
                    if (cellWidget.focusProxy().focusPolicy() != Qt.FocusPolicy.NoFocus) {
                        tabOrder.add(cellWidget.focusProxy());
                        if (startFocusWidget==null && !item.isModelItemReadOnly()){
                            startFocusWidget = cellWidget;
                        }
                    }
                } else if (cellWidget.focusPolicy() != Qt.FocusPolicy.NoFocus) {
                    tabOrder.add(cellWidget);
                    if (startFocusWidget==null && !item.isModelItemReadOnly()){
                        startFocusWidget = cellWidget;
                    }
                }
            }
            if (startFocusWidget==null && !tabOrder.isEmpty()){
                startFocusWidget = tabOrder.get(0);
            }
            PropertiesGrid.this.updateVisibility();
        }

        @Override
        public void scrollToCell(final IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> item) {
            if (item.getPropertyEditor().asQWidget() != null) {
                PropertiesGrid.this.ensureWidgetVisible(item.getPropertyLabel());
                PropertiesGrid.this.ensureWidgetVisible(item.getPropertyEditor().asQWidget());
            }
            if (item.getPropertyEditor() instanceof AbstractPropEditor) {
                PropertiesGrid.this.currentEditor = (AbstractPropEditor) item.getPropertyEditor();
                QApplication.postEvent(PropertiesGrid.this, new SetFocusEvent());
            }
        }

        @Override
        public void updateGeometry() {
            PropertiesGrid.this.updateGeometry();
        }        
    }
    
    private final class ContentWidget extends QWidget{
        
        public ContentWidget(final QWidget parent){
            super(parent);            
        }
                
        @Override
        protected void focusInEvent(final QFocusEvent event) {
            super.focusInEvent(event);
            if (!closed && !preparedForClose) {
                if (currentEditor == null) {
                    focusNextChild();
                    verticalScrollBar().setValue(verticalScrollBar().minimum());
                    horizontalScrollBar().setValue(horizontalScrollBar().minimum());
                } else {
                    currentEditor.setFocus();
                }
            }
        }
        
        public boolean focusFirstChild(){
            return startFocusWidget==null ? focusNextChild() : focusWidget(true, startFocusWidget);
        }        
        
        public boolean focusLastChild(){
            return tabOrder.isEmpty() ? false : focusWidget(false, tabOrder.size()-1);
        }

        @Override
        protected boolean focusNextPrevChild(final boolean next) {
            return focusNextPrevChild(next, QApplication.focusWidget());
        }
        
        private boolean focusNextPrevChild(final boolean next, final QWidget current) {
            if (closed || preparedForClose){
                return false;
            }
            final int index = tabOrder.indexOf(current);
            if (index > -1) {
                if (next) {
                    for (int i = index + 1; i < tabOrder.size(); i++) {
                        if (focusWidget(next,i)) {
                            return true;
                        }
                    }
                    final PropertiesGrid parentGrid = getParentPropertiesGrid();
                    if (parentGrid!=null){
                        return moveFocusToParentGrid(true, parentGrid);
                    }
                    for (int i = 0; i < index; i++) {
                        if (focusWidget(next,i)) {
                            return true;
                        }
                    }
                } else {
                    for (int i = index - 1; i >= 0; i--) {
                        if (focusWidget(next,i)) {
                            return true;
                        }
                    }
                    final PropertiesGrid parentGrid = getParentPropertiesGrid();
                    if (parentGrid!=null){
                        return moveFocusToParentGrid(false, parentGrid);
                    }
                    for (int i = tabOrder.size() - 1; i > index; i--) {
                        if (focusWidget(next,i)) {
                            return true;
                        }
                    }
                }
            }else if (startFocusWidget!=null){
                return focusWidget(next, startFocusWidget);
            }
            return super.focusNextPrevChild(next);            
        }
        
        private boolean moveFocusToParentGrid(final boolean next, final PropertiesGrid parentGrid){
            if (parentGrid.content.focusNextPrevChild(next, PropertiesGrid.this)){
                if (currentEditor!=null){
                    currentEditor.setHighlightedFrame(false);
                }
                currentEditor = null;
                return true;
            }else{
                return false;
            }
            
        }

        private boolean focusWidget(final boolean next, final int index) {
            return focusWidget(next, tabOrder.get(index));
        }
        
        private boolean focusWidget(final boolean next, final QWidget widget){
            if (widget.isEnabled()) {
                if (widget instanceof PropertiesGrid){
                    final PropertiesGrid childGrid = (PropertiesGrid)widget;
                    final boolean result = next ? childGrid.content.focusFirstChild() : childGrid.content.focusLastChild();
                    if (result){
                        if (currentEditor!=null){
                            currentEditor.setHighlightedFrame(false);
                            currentEditor = null;
                        }                        
                        return true;
                    }else{
                        return false;
                    }
                }
                widget.setFocus();
                return widget.hasFocus();
            } else {
                return false;
            }            
        }

        @Override
        protected void resizeEvent(final QResizeEvent resizeEvent) {
            super.resizeEvent(resizeEvent);
            PropertiesGrid.this.updateGeometry();
        }        
    }
    
    private final ContentWidget content = new ContentWidget(this);
    private final PropertiesGridController<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> controller;
    private final List<QWidget> tabOrder = new ArrayList<>();
    private final Collection<IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget>> currentItems = new LinkedList<>();
    private final HierarchicalGridLayout layout = new HierarchicalGridLayout();
    private final EditorPageView pageView;
    private final PropertiesGroupModelItem propertiesGroup;
    private final QSize tmpSize = new QSize(0, 0);
    private AbstractPropEditor currentEditor;
    private QWidget startFocusWidget;
    private int space = 10;
    private boolean closed = false;
    private boolean autoHide;
    private boolean preparedForClose;
    private IClientEnvironment environment;

    public PropertiesGrid(final IClientEnvironment environment) {
        this(null, null, null, environment);
    }

    public PropertiesGrid(final QWidget parent, final IClientEnvironment environment) {
        this(parent, null, null, environment);
    }

    public PropertiesGrid(final QWidget parent, final List<Property> properties, final IClientEnvironment environment) {
        this(parent, properties, null, environment);
    }

    public PropertiesGrid(final EditorPageView page, final IClientEnvironment environment) {
        this(null, null, page, environment);
    }

    public PropertiesGrid(final QWidget parent, final List<Property> properties, final EditorPageView page, final IClientEnvironment environment) {
        super(parent);
        this.environment = environment;
        controller = new PropertiesGridController<>(new Presenter(), environment);
        pageView = page;
        propertiesGroup = null;
        if (properties != null && !properties.isEmpty()) {
            controller.addProperties(properties);
        }
        setupUi();
    }
    
    PropertiesGrid(final PropertiesGroupWidget propertiesGroupWidget){
        super(propertiesGroupWidget);
        propertiesGroup = propertiesGroupWidget.getPropertiesGroup();
        final PropertiesGridController<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> parentController = propertiesGroupWidget.getParentPropertiesGrid().controller;
        controller = new PropertiesGridController<>(new Presenter(), propertiesGroup.getId(), parentController,  propertiesGroup.getEnvironment());
        pageView = null;
        environment = propertiesGroupWidget.getPropertiesGroup().getEnvironment();
        setupUi();
    }

    private void setupUi() {
        content.setLayout(layout);
        //При создании редактора свойства или при получении значения свойства может выполняться запрос на сервер.
        //Чтобы избежать показа недооткрытого виджета, содержимое становится видимым только по окончании вызова метода bind.
        content.setVisible(false);
        layout.setVerticalSpacing(4);
        layout.setAlignment(new Alignment(new AlignmentFlag[]{AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft}));
        layout.setHorizontalSpacing(4);
        setFrameShape(QFrame.Shape.NoFrame);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }
    
    private PropertiesGrid getParentPropertiesGrid(){
        if (parentWidget() instanceof PropertiesGroupWidget){
            return ((PropertiesGroupWidget)parentWidget()).getParentPropertiesGrid();
        }else{
            return null;
        }
    }

    public int getColumnsCount() {
        return controller.getColumnsCount();
    }
    
    public int getRowsCount(){
        return controller.getRowsCount();
    }

    public int getHorizontalSpacing() {
        return space;
    }

    public void setHorizontalSpacing(final int space) {
        this.space = space;
        updateColumnsWidth();
    }

    public void setVerticalSpacing(final int space) {
        layout.setVerticalSpacing(space);
    }

    public int getVerticalSpacing() {
        return layout.verticalSpacing();
    }

    public AbstractPropEditor getCurrentPropEditor() {
        return currentEditor;
    }

    public void addProperty(final Property property, final int column, final int row) {
        controller.addProperty(property, column, row);
        if (widget() != null) {//was binded?
            updateColumnsWidth();
            updateGeometry();
        }
    }

    public void addProperty(final Property property) {
        controller.addProperty(property);
    }

    public void removeProperty(final Property property) {
        controller.removeProperty(property);
    }

    public void clear() {
        controller.clear();
    }

    public void addProperties(final Collection<Property> properties) {
        controller.addProperties(properties);
    }

    public List<Property> getProperties() {
        return controller.getProperties();
    }

    @SuppressWarnings("unused")
    private void editorFocusIn() {
        if (QObject.signalSender() instanceof AbstractPropEditor) {
            if (currentEditor != QObject.signalSender()) {
                if (currentEditor != null) {
                    currentEditor.setHighlightedFrame(false);
                }
                currentEditor = (AbstractPropEditor) QObject.signalSender();
            }
            currentEditor.setHighlightedFrame(true);
        }
    }

    @SuppressWarnings("unused")
    private void editorFocusOut() {
        if (QObject.signalSender() instanceof AbstractPropEditor) {
            ((AbstractPropEditor) QObject.signalSender()).setHighlightedFrame(false);
            //currentEditor = null;
        }
    }

    private void updateColumnsWidth() {
        if (widget() != null) {
            final int visibleCount = controller.getColumnsCount();
            boolean isSpaceColumn;
            for (int i = 1; i < layout.columnCount(); i++) {
                isSpaceColumn = ((i + 1) % 3 == 0) && ((i + 1) < visibleCount * 3);
                layout.setColumnMinimumWidth(i, isSpaceColumn ? space : 0);
            }
        }
    }

    @Override
    public void bind() {
        if (propertiesGroup==null){
            final EditorPageModelItem page = getPage();
            controller.bind(page);
            if (page!=null){
                layout.setObjectName("layout for #"+page.getId());
                page.subscribe(this);
            }
        }else{
            layout.setObjectName("layout for #"+propertiesGroup.getId());
            controller.bind(propertiesGroup);
        }
        ((Application)environment.getApplication()).getActions().settingsChanged.connect(this, "applySettings()");
        if (!isAutoHide()){
            content.setVisible(true);
        }
    }
    
    public final void applySettings() {
        setupAlignmentSettings(); 
        update();
    }
    
    private void setupAlignmentSettings() {
        ExplorerSettings settings = (ExplorerSettings)environment.getConfigStore();
        AlignmentFlag alignmentFlag;
        try {
            settings.beginGroup(SettingNames.SYSTEM);
            settings.beginGroup(SettingNames.EDITOR_GROUP);
            settings.beginGroup(SettingNames.Editor.COMMON_GROUP);
            alignmentFlag = settings.readAlignmentFlag(SettingNames.Editor.Common.TITLES_ALIGNMENT);
        } finally {
            settings.endGroup();
            settings.endGroup();
            settings.endGroup();
        }
        final HierarchicalGridLayout layout = PropertiesGrid.this.layout;
        QWidget item;
        for (int idx = 0; idx < layout.count(); idx++) {
            item = layout.itemAt(idx).widget();
            if (item instanceof PropLabel) {
                layout.setAlignment((PropLabel)item, alignmentFlag);
            }
        }
    }
     
    private EditorPageModelItem getPage(){
        return pageView == null ? null : pageView.getPage();
    }

    @Override
    public void refresh(final ModelItem changedItem) {
        if (!preparedForClose){
            controller.refresh(changedItem);
            if (changedItem instanceof EditorPageModelItem){
                updateVisibility();
            }
        }
    }
    
    private void updateVisibility(){
        if (isAutoHide() && !closed && !preparedForClose && controller.wasBinded()){
            final EditorPageModelItem page = getPage();
            if (page!=null && !page.isVisible()){
                content.setVisible(false);
                setVisible(false);
                return;
            }
            if (controller.isSomeCellVisible()){
                content.setVisible(true);
                setVisible(true);
            }else{
                content.setVisible(false);
                setVisible(false);
            }
        }
    }

    @Override
    public boolean setFocus(final Property property) {
        return controller.setFocus(property);
    }

    public void finishEdit() {
        if (!preparedForClose){
            controller.finishEdit();
        }
    }    
    
    void prepareForClose(){
        if (!preparedForClose){
            preparedForClose = true;
            Application.getInstance().getActions().settingsChanged.disconnect(this);            
            startFocusWidget = null;
            tabOrder.clear();
            currentEditor = null;
            if (propertiesGroup==null){
                final EditorPageModelItem page = getPage();
                if (page!=null){
                    page.unsubscribe(this);
                }
            }
            for (IPresenterItem<PropLabel, IExplorerModelWidget, PropertiesGroupWidget> item: currentItems){
                if (item.getPropertyEditor() instanceof AbstractPropEditor){
                    ((AbstractPropEditor)item.getPropertyEditor()).prepareForClear();
                }else{
                    final PropertiesGroupWidget innerGroup = item.getPropertiesGroupWidget();
                    if (innerGroup!=null && innerGroup.getPropertiesGrid()!=null){
                        innerGroup.getParentPropertiesGrid().prepareForClose();
                    }
                }
            }
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent event) {
        if (!preparedForClose){
            prepareForClose();
        }
        closed = true;
        controller.close(null /*page model item was already unsubscribed in prepareForClose*/);
        super.closeEvent(event);
    }

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof SetFocusEvent) {
            qevent.accept();
            setFocus();
        } else {
            super.customEvent(qevent);
        }
    }

    @Override
    public QSize minimumSizeHint() {
        if (widget() == null) {
            return super.minimumSizeHint();
        } else {
            if (propertiesGroup==null){
                final int maxHeight = controller.calcMaxHeight();
                final Dimension selfSize = calcSelfSize();
                tmpSize.setWidth(selfSize.width);
                tmpSize.setHeight(maxHeight + layout.getContentsMargins().top + layout.getContentsMargins().bottom + selfSize.height);
                return tmpSize;
            }else{
                return sizeHint();
            }
        }
    }

    @Override
    public QSize sizeHint() {
        if (widget() == null) {
            return super.sizeHint();
        } else {
            final int frameWidth = frameWidth() * 2;
            tmpSize.setHeight(frameWidth);
            tmpSize.setWidth(frameWidth);
            return widget().sizeHint().add(tmpSize);
        }
    }

    private Dimension calcSelfSize() {
        int width = frameWidth() * 2;
        int height = frameWidth() * 2;
        if (verticalScrollBar() != null) {
            if (verticalScrollBar().isVisible()) {
                width += verticalScrollBar().width();
            }
        }
        if (horizontalScrollBar() != null) {
            if (horizontalScrollBar().isVisible()) {
                height += horizontalScrollBar().height();
            }
        }
        return new Dimension(width, height);
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }
    
    public final void setAutoHide(final boolean autoHide){
        if (autoHide!=this.autoHide){
            this.autoHide = autoHide;            
            updateVisibility();                    
        }
    }
    
    public boolean isAutoHide(){
        return autoHide;
    }

    @Override
    public boolean isInCache() {
        return preparedForClose;
    }        
    
    int getMaxRowHeight(){
        return controller.calcMaxHeight();
    }
    
    void setContentVerticalMargins(final int margin){
        final QMargins currentMargins  = layout.contentsMargins();
        layout.setContentsMargins(currentMargins.left(), margin, currentMargins.right(), margin);        
    }
}