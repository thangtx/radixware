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

package org.radixware.kernel.common.client.editors.traceprofile;

import java.util.*;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.trace.TraceProfile;

/**
 * Узел в дереве профиля трассировки. Инстанция данного класса создается в
 * {@link TraceProfileTreeController} и содержит ассоциацию конкретного
 * источника событий с текущим минимальным
 * {@link TraceProfileTreeController.EventSeverity уровнем важности сообщения},
 * при котором оно должно быть сохранено в трассе. Инстанция позволяет
 * определить было ли значение этого уровеня важности задано явно или оно
 * унаследовано из вышестоящего источника событий, а также допустима ли в данный
 * момент возможность его модификации пользователем при помощи GUI-средств.
 * <p>
 * Данный класс используется также для хранения уровня важности сообщения,
 * применяемого источниками событий верхнего уровня по умолчанию. В этом случае
 * метод {@link #getParentNode()} будет возвращать
 * <code>null</code>.
 *
 * @param <T> класс графического компонента, который используется для
 * представления узлов дерева профиля трассировки
 * @see ITraceProfileEditor
 * @see TraceProfileTreeController
 */
public final class TraceProfileTreeNode<T extends IWidget> {

    private static interface INodeVisitor<T extends IWidget> {

        boolean visit(TraceProfileTreeNode<T> node);
    }

    private static class NodesComparator implements java.util.Comparator<TraceProfileTreeNode> {

        @Override
        public int compare(TraceProfileTreeNode o1, TraceProfileTreeNode o2) {
            return o2.getTitle().compareTo(o1.getTitle());
        }
    }
    private final String eventSource;
    private final TraceProfileTreeNode<T> parentNode;
    private final List<TraceProfileTreeNode<T>> childNodes = new LinkedList<>();
    private final Set<String> childrenWithSpecifiedEventSeverity = new HashSet<>();
    private String title;
    private boolean isReadOnly;
    private TraceProfileTreeController.EventSeverity eventSeverity;
    private T widget;
    private TraceProfile.EventSourceOptions options;

    TraceProfileTreeNode(final String eventSource, final String title, final TraceProfileTreeNode<T> parentNode) {
        this(eventSource, title, parentNode, null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    TraceProfileTreeNode(final String eventSource, final String title, final TraceProfileTreeNode<T> parentNode, final TraceProfile.EventSourceOptions options) {
        this.eventSource = eventSource;
        this.title = title;
        this.parentNode = parentNode;
        if (parentNode != null) {
            parentNode.childNodes.add(this);
        }
        this.options = options == null ? new TraceProfile.EventSourceOptions("") : options;
    }

    void createPresentations(final ITraceProfileTreePresenter<T> presenter) {
        widget = presenter.createTreeNodeWidget(this);
        visitChildren(new INodeVisitor<T>() {
            @Override
            public boolean visit(TraceProfileTreeNode<T> node) {
                node.widget = presenter.createTreeNodeWidget(node);
                return true;
            }
        });
    }

    /**
     * Возвращает список вложенных узлов дерева редактора профиля трассировки.
     *
     * @return список узлов дерева редактора профиля трассировки
     */
    public List<TraceProfileTreeNode<T>> getChildNodes() {
        return Collections.unmodifiableList(childNodes);
    }

    /**
     * Возвращает текущий уровень важности сообщения. Если для данного узла
     * уровень важности сообщения не был установлен, то метод вернет значение
     * взятое из ближайшего вышестоящего узла, где он был задан.
     *
     * @return минимальный уровень важности сообщения, при котором оно должно
     * быть сохранено в трассе. Не может быть </code>null<code>.
     * @see #eventSeverityWasInherited()
     * @see #hasChildWithSpecifiedEventSeverity()
     */
    public TraceProfileTreeController.EventSeverity getEventSeverity() {
        for (TraceProfileTreeNode node = this; node != null; node = node.getParentNode()) {
            if (node.eventSeverity != null) {
                return node.eventSeverity;
            }
        }
        return null;
    }

    /**
     * Возвращает имя источника событий.
     *
     * @return имя источника событий, ассоциированного с данным узлом
     * или <code>null</code>, если данный узел используется для хранения уровня
     * важности сообщения, применяемого источниками событий верхнего уровня по
     * умолчанию.
     */
    public String getEventSource() {
        return eventSource;
    }

    /**
     * Возвращает узел верхнего уровня вложенности в дереве событий.
     *
     * @return узел верхнего уровня вложенности в дереве событий
     * или <code>null</code>, если данный узел является корневым.
     */
    public TraceProfileTreeNode<T> getParentNode() {
        return parentNode;
    }

    /**
     * Возвращает заголовок узла дерева.
     *
     * @return заголовок узла дерева. Не может быть <code>null</code>.
     */
    public String getTitle() {
        return title;
    }

    void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Возвращает графический компонент для отображения данного узла.
     *
     * @return инстанция графического компонента
     */
    public T getWidget() {
        return widget;
    }

    void resetChildrenEventSeverity() {
        visitChildren(new INodeVisitor<T>() {
            @Override
            public boolean visit(TraceProfileTreeNode<T> node) {
                node.eventSeverity = null;
                node.childrenWithSpecifiedEventSeverity.clear();
                return true;
            }
        });
        childrenWithSpecifiedEventSeverity.clear();
    }

    void setEventSeverity(final TraceProfileTreeController.EventSeverity eventSeverity) {
        if (this.eventSeverity != eventSeverity) {
            this.eventSeverity = eventSeverity;
            for (TraceProfileTreeNode<T> node = getParentNode(); node != null; node = node.getParentNode()) {
                if (eventSeverity == null) {
                    node.childrenWithSpecifiedEventSeverity.remove(eventSource);
                } else {
                    node.childrenWithSpecifiedEventSeverity.add(eventSource);
                }
            }
        }
    }

    void changeEventSeverity(final TraceProfileTreeController.EventSeverity eventSeverity, final String options, final ITraceProfileTreePresenter<T> presenter) {
        if (this.eventSeverity != eventSeverity || !getOptions().toString().equals(options)) {
            final boolean severityChanged = this.eventSeverity != eventSeverity;
            if (severityChanged) {
                this.eventSeverity = eventSeverity;
                for (TraceProfileTreeNode<T> node = getParentNode(); node != null; node = node.getParentNode()) {
                    if (eventSeverity == null) {
                        node.childrenWithSpecifiedEventSeverity.remove(eventSource);
                    } else {
                        node.childrenWithSpecifiedEventSeverity.add(eventSource);
                    }
                }
            }
            setOptions(new TraceProfile.EventSourceOptions(options));
            presenter.presentWidget(this);
            if (severityChanged) {
                visitChildren(new INodeVisitor<T>() {
                    @Override
                    public boolean visit(final TraceProfileTreeNode<T> node) {
                        presenter.presentWidget(node);
                        return true;
                    }
                });
            }

            for (TraceProfileTreeNode<T> parent = getParentNode(); parent != null; parent = parent.getParentNode()) {
                presenter.presentWidget(parent);
            }
        }
    }

    /**
     * Возвращает признак наследования уровня важности события. Метод позволяет
     * определить было ли значение, возвращаемое методом {@link #getEventSeverity()
     * } явно задано или оно унаследовано из вышестоящего узла дерева.
     *
     * @return признак наследования уровня важности события.
     * @see #getEventSeverity()
     * @see #hasChildWithSpecifiedEventSeverity()
     */
    public boolean eventSeverityWasInherited() {
        return eventSeverity == null;
    }

    String getTraceProfileAsStr() {
        final StringBuilder profileBuilder = new StringBuilder(eventSeverity.getValue());
        visitChildren(new INodeVisitor<T>() {
            @Override
            public boolean visit(final TraceProfileTreeNode<T> node) {
                if (!node.eventSeverityWasInherited()) {
                    profileBuilder.append(';');
                    profileBuilder.append(node.getEventSource());
                    profileBuilder.append('=');
                    profileBuilder.append(node.getEventSeverity().getValue());
                    final String optionsStr = node.getOptions().toString();
                    if (!optionsStr.isEmpty()) {
                        profileBuilder.append('[');
                        profileBuilder.append(optionsStr);
                        profileBuilder.append(']');
                    }
                }
                return true;
            }
        });
        return profileBuilder.toString();
    }

    /**
     * Возвращает признак наличия вложенного узла с явно заданным уровнем
     * важности события.
     *
     * @return <code>true</code> если у данного узла имеется хотя бы один
     * вложенный дочерний узел, в котором уровень важности события был задан
     * явно и <code>false</code> если на всех уровнях вложенности дочерние узлы
     * наследуют значение уровня важности сообщения.
     */
    public boolean hasChildWithSpecifiedEventSeverity() {
        return !childrenWithSpecifiedEventSeverity.isEmpty();
    }

    private void visitChildren(final INodeVisitor<T> visitor) {
        final Stack<TraceProfileTreeNode<T>> nodes = new Stack<>();
        nodes.addAll(getChildNodes());
        while (!nodes.isEmpty() && visitor.visit(nodes.peek())) {
            nodes.addAll(nodes.pop().getChildNodes());
        }
    }

    void setReadOnly(final boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * Возвращает признак возможности изменения уровня важности события в данном
     * узле.
     *
     * @return <code>true</code> если в данный момент пользователь должен иметь
     * возможность при помощи {@link #getWidget() GUI-компонента} изменить
     * текущее значение минимального уровня важности события и
     * <code>false</code> в противном случае
     */
    public boolean isReadOnly() {
        for (TraceProfileTreeNode node = this; node != null; node = node.getParentNode()) {
            if (node.isReadOnly) {
                return true;
            }
        }
        return false;
    }

    void sortByTitle() {
        Collections.sort(childNodes, new NodesComparator());
        visitChildren(new INodeVisitor<T>() {
            @Override
            public boolean visit(final TraceProfileTreeNode<T> node) {
                Collections.sort(node.childNodes, new NodesComparator());
                return true;
            }
        });
    }

    public TraceProfile.EventSourceOptions getOptions() {
        return options;
    }

    public void setOptions(TraceProfile.EventSourceOptions options) {
        this.options = options;
    }
}