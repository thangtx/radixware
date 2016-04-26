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
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLayoutItemInterface;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QWidget;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.views.IPropEditor;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter;
import org.radixware.kernel.common.client.widgets.propertiesgrid.IPropertiesGridPresenter.IPresenterItem;
import org.radixware.kernel.common.client.widgets.propertiesgrid.PropertiesGridController;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.radixware.kernel.explorer.views.EditorPageView;
import org.radixware.kernel.explorer.widgets.propeditors.AbstractPropEditor;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditor;
import org.radixware.kernel.explorer.widgets.propeditors.PropEditorsPool;


public class PropertiesGrid extends QScrollArea implements IExplorerModelWidget {

    public static final class SetFocusEvent extends QEvent {

        public SetFocusEvent() {
            super(QEvent.Type.User);
        }
    }

    private final class Presenter implements IPropertiesGridPresenter<PropLabel, IExplorerModelWidget> {

        private boolean firstUpdate = true;
        private final int minimumEditorCellWidth;
        private final Map<QWidget, java.awt.Point> tabOrderForEditor = new HashMap<>();

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
            if (property.getDefinition().getType() == EValType.BOOL) {
                editor.asQWidget().setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
            } else {
                editor.asQWidget().setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Fixed);
            }
            return editor;
        }

        @Override
        public void destroyWidgets(final PropLabel label, final IExplorerModelWidget editor) {
            if (editor != null
                    && (editor.asQWidget() instanceof PropEditor == false
                    || !PropEditorsPool.getInstance().cachePropEditor((PropEditor) editor.asQWidget()))) {
                editor.asQWidget().setVisible(false);
                editor.asQWidget().close();
            }
            if (label != null) {
                PropLabelsPool.getInstance().cachePropLabel(label);
            }
        }

        @Override
        public int getCellHeight(final IPresenterItem<PropLabel, IExplorerModelWidget> item) {
            return item.getPropertyEditor().asQWidget().sizeHint().height();
        }

        @Override
        public void beforeUpdateCellsPresentation(final int columnsCount, final int rowsCount) {
            PropertiesGrid.this.setUpdatesEnabled(false);
            if (firstUpdate) {
                setWidget(PropertiesGrid.this.content);
            } else {
                final QGridLayout layout = PropertiesGrid.this.layout;
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
        public void presentCell(final IPresenterItem<PropLabel, IExplorerModelWidget> item, final int columnsCount) {
            final EValType propertyType = item.getProperty().getDefinition().getType();
            final Qt.AlignmentFlag titleAlignment;
            final QGridLayout layout = PropertiesGrid.this.layout;
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
                    layout.addWidget(label, row, column * 3);
                } else {
                    layout.addWidget(label, row, column * 3, titleAlignment);
                }
                if (propertyType == EValType.BOOL) {
                    layout.addWidget(editor, row, column * 3 + 1, 1, span, Qt.AlignmentFlag.AlignLeft);
                } else {
                    layout.addWidget(editor, row, column * 3 + 1, 1, span);
                }
            } else {
                if (titleAlignment == null) {
                    layout.addWidget(label, row, column);
                } else {
                    layout.addWidget(label, row, column, titleAlignment);
                }
                if (propertyType == EValType.BOOL) {
                    layout.addWidget(editor, row, column + 1, 1, span, Qt.AlignmentFlag.AlignLeft);
                } else {
                    layout.addWidget(editor, row, column + 1, 1, span);
                }
            }

            if (editor instanceof AbstractPropEditor) {
                AbstractPropEditor pEditor = (AbstractPropEditor) editor;
                pEditor.focused.connect(PropertiesGrid.this, "editorFocusIn()");
                pEditor.unfocused.connect(PropertiesGrid.this, "editorFocusOut()");
            }
            tabOrderForEditor.put(editor, new Point(column, row));
        }

        @Override
        public void presentSpanColumn(final int col) {
            PropertiesGrid.this.layout.addItem(new QSpacerItem(minimumEditorCellWidth, 0, QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.Minimum), 0, col * 3 + 1);
        }

        @Override
        public void clearCellPresentation(final IPresenterItem<PropLabel, IExplorerModelWidget> item) {
            final IExplorerModelWidget editor = item.getPropertyEditor();
            final QGridLayout layout = PropertiesGrid.this.layout;
            if (PropertiesGrid.this.currentEditor == editor) {//NOPMD
                PropertiesGrid.this.currentEditor = null;
            }
            if (editor instanceof IPropEditor) {
                ((IPropEditor) editor).finishEdit();
            }
            layout.removeWidget(item.getPropertyLabel());
            layout.removeWidget(editor.asQWidget());
            tabOrderForEditor.remove(editor.asQWidget());
        }

        @Override
        public void afterUpdateCellsPresentation() {
            if (firstUpdate) {
                PropertiesGrid.this.setFocusProxy(PropertiesGrid.this.content);
                PropertiesGrid.this.setWidgetResizable(true);
            }
            PropertiesGrid.this.setUpdatesEnabled(true);
            layout.invalidate();
            PropertiesGrid.this.updateColumnsWidth();
            PropertiesGrid.this.updateGeometry();
            firstUpdate = false;
            final List<QWidget> editors = new ArrayList<>(tabOrderForEditor.keySet());
            final Comparator<QWidget> editorsComparator = new Comparator<QWidget>() {
                @Override
                public int compare(final QWidget e1, final QWidget e2) {
                    final java.awt.Point p1 = tabOrderForEditor.get(e1);
                    final java.awt.Point p2 = tabOrderForEditor.get(e2);
                    final int rowsComparing = Integer.compare(p1.y, p2.y);
                    return rowsComparing == 0 ? Integer.compare(p1.x, p2.x) : rowsComparing;
                }
            };
            Collections.sort(editors, editorsComparator);
            tabOrder.clear();
            for (QWidget editor : editors) {
                if (editor.focusProxy() != null) {
                    if (editor.focusProxy().focusPolicy() != Qt.FocusPolicy.NoFocus) {
                        tabOrder.add(editor.focusProxy());
                    }
                } else if (editor.focusPolicy() != Qt.FocusPolicy.NoFocus) {
                    tabOrder.add(editor);
                }
            }
        }

        @Override
        public void scrollToCell(final IPresenterItem<PropLabel, IExplorerModelWidget> item) {
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
    private final QWidget content = new QWidget(this) {
        @Override
        protected void focusInEvent(final QFocusEvent event) {
            super.focusInEvent(event);
            if (!closed) {
                if (currentEditor == null) {
                    focusNextChild();
                    verticalScrollBar().setValue(verticalScrollBar().minimum());
                    horizontalScrollBar().setValue(horizontalScrollBar().minimum());
                } else {
                    currentEditor.setFocus();
                }
            }
        }

        @Override
        protected boolean focusNextPrevChild(final boolean next) {
            final int index = tabOrder.indexOf(QApplication.focusWidget());
            if (index > -1 && !closed) {
                if (next) {
                    for (int i = index + 1; i < tabOrder.size(); i++) {
                        if (focusWidget(i)) {
                            return true;
                        }
                    }
                    for (int i = 0; i < index; i++) {
                        if (focusWidget(i)) {
                            return true;
                        }
                    }
                } else {
                    for (int i = index - 1; i >= 0; i--) {
                        if (focusWidget(i)) {
                            return true;
                        }
                    }
                    for (int i = tabOrder.size() - 1; i > index; i--) {
                        if (focusWidget(i)) {
                            return true;
                        }
                    }
                }
            }
            return super.focusNextPrevChild(next);
        }

        private boolean focusWidget(int index) {
            final QWidget widget = tabOrder.get(index);
            if (widget.isEnabled()) {
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
    };
    private final PropertiesGridController<PropLabel, IExplorerModelWidget> controller;
    private final List<QWidget> tabOrder = new ArrayList<>();
    private final QGridLayout layout = new QGridLayout(content);
    private final EditorPageView pageView;
    private AbstractPropEditor currentEditor;
    private int space = 10;
    private boolean closed = false;

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
        controller = new PropertiesGridController<>(new Presenter(), environment);
        pageView = page;
        if (properties != null && !properties.isEmpty()) {
            controller.addProperties(properties);
        }
        setupUi();
    }

    private void setupUi() {
        content.setLayout(layout);
        //При создании редактора свойства или при получении значения свойства может выполняться запрос на сервер.
        //Чтобы избежать показа недооткрытого виджета, содержимое становится видимым только по окончании вызова метода bind.
        content.setVisible(false);
        layout.setVerticalSpacing(4);
        layout.setAlignment(new Alignment(new AlignmentFlag[]{AlignmentFlag.AlignTop}));
        layout.setHorizontalSpacing(4);
        setFrameShape(QFrame.Shape.NoFrame);
        setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
    }

    public int getColumnsCount() {
        return controller.getColumnsCount();
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
        controller.bind(pageView == null ? null : pageView.getPage());
        content.setVisible(true);
    }

    @Override
    public void refresh(ModelItem changedItem) {
        controller.refresh(changedItem);
    }

    @Override
    public boolean setFocus(final Property property) {
        return controller.setFocus(property);
    }

    public void finishEdit() {
        controller.finishEdit();
    }

    @Override
    protected void closeEvent(QCloseEvent event) {
        closed = true;
        tabOrder.clear();
        controller.close(pageView == null ? null : pageView.getPage());
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
            final int maxHeight = controller.calcMaxHeight();
            final QSize s = new QSize(0, maxHeight + layout.getContentsMargins().top
                    + layout.getContentsMargins().bottom);
            return s.add(selfSize());
        }
    }

    @Override
    public QSize sizeHint() {
        if (widget() == null) {
            return super.sizeHint();
        } else {
            return widget().sizeHint().add(new QSize(frameWidth() * 2, frameWidth() * 2));
        }
    }

    private QSize selfSize() {
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
        return new QSize(width, height);
    }

    @Override
    public QWidget asQWidget() {
        return this;
    }
}