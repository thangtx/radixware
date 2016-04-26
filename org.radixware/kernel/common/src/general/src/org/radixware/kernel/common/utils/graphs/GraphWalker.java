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

package org.radixware.kernel.common.utils.graphs;

import java.util.*;


public class GraphWalker<ElementType> {

    public static abstract class NodeFilter<ElementType> {

        private boolean stop = false;

        protected void stop() {
            stop = true;
        }

        boolean isStoped() {
            return stop;
        }

        /**
         * Specifies whether to view the descendants nodes or not.
         */
        protected boolean view(ElementType node, int level) {
            return true;
        }

        protected Collection<ElementType> collectNodes(ElementType source) {
            return Collections.<ElementType>emptyList();
        }

        protected abstract boolean accept(ElementType node, int level);
    }

    private static final class WalkItem<T> {

        private final int level;
        private final T node;
        private final WalkItem<T> parent;

        public WalkItem(int level, T node, WalkItem<T> parent) {
            this.level = level;
            this.node = node;
            this.parent = parent;
        }

        public int getLevel() {
            return level;
        }

        public T getNode() {
            return node;
        }

        public WalkItem<T> getParent() {
            return parent;
        }
    }

    private static abstract class WalkCollection<T> {

        final Set<T> used = new LinkedHashSet<>();

        void put(Collection<T> nodes, WalkItem<T> parent, int level) {
            if (nodes != null) {
                for (final T node : nodes) {
                    put(node, parent, level);
                }
            }
        }

        final void put(T node, WalkItem<T> parent, int level) {
            if (!viewed(node)) {
                put(new WalkItem<>(level, node, parent));
                used.add(node);
            }
        }

        boolean viewed(T node) {
            return used.contains(node);
        }

        abstract WalkItem<T> get();

        abstract boolean isEmpty();

        abstract void put(WalkItem<T> item);
    }

    private static final class QueueCollection<T> extends WalkCollection<T> {

        private Queue<WalkItem<T>> queue = new LinkedList<>();

        @Override
        void put(WalkItem<T> item) {
            queue.add(item);
        }

        @Override
        public WalkItem get() {
            return queue.poll();
        }

        @Override
        boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    private static final class StackCollection<T> extends WalkCollection<T> {

        private Stack<WalkItem<T>> stack = new Stack<>();

        @Override
        void put(WalkItem<T> item) {
            stack.push(item);
        }

        @Override
        public WalkItem get() {
            return stack.pop();
        }

        @Override
        boolean isEmpty() {
            return stack.isEmpty();
        }
    }

    private static final class WalkResult<ElementType> {

        final Set<ElementType> used;
        final WalkItem<ElementType> stopNode;

        public WalkResult(Set<ElementType> used, WalkItem<ElementType> stopNode) {
            this.used = used;
            this.stopNode = stopNode;
        }
    }

    private WalkResult<ElementType> walk(NodeFilter<ElementType> filter, ElementType start, WalkCollection<ElementType> collection) {

        if (filter == null) {
            throw new NullPointerException("Filter is null");
        }

        if (start == null) {
            throw new NullPointerException("Node is null");
        }

        collection.put(start, null, 0);
        WalkItem<ElementType> curr = null;
        while (!collection.isEmpty() && !filter.isStoped()) {

            curr = collection.get();
            final ElementType node = curr.getNode();

            filter.accept(node, curr.getLevel());
            if (filter.isStoped()) {
                break;
            }
            if (filter.view(node, curr.getLevel())) {
                collection.put(collectNodes(node, filter), curr, curr.getLevel() + 1);
            }
        }

        return new WalkResult(collection.used, curr);
    }

    /**
     *
     * @param filter
     * @param start
     */
    public final Set<ElementType> depthWalk(NodeFilter<ElementType> filter, ElementType start) {
        return walk(filter, start, new StackCollection()).used;
    }

    /**
     *
     * @param filter
     * @param start
     */
    public final Set<ElementType> breadthWalk(NodeFilter<ElementType> filter, ElementType start) {
        return walk(filter, start, new QueueCollection()).used;
    }

    protected Collection<ElementType> collectNodes(ElementType source, NodeFilter<ElementType> filter) {
        return filter.collectNodes(source);
    }

    
    private SearchResult<ElementType> search(SearchNodeFilter<ElementType> filter, ElementType start, WalkCollection<ElementType> collection) {

        final WalkResult<ElementType> result = walk(filter, start, collection);

        if (filter.isObtained()) {
            List<ElementType> way = new ArrayList<>();
            WalkItem<ElementType> curr = result.stopNode;
            
            while (curr != null) {
                way.add(curr.node);
                if (curr == curr.parent) {
                    break;
                }
                curr = curr.parent;
            }
            Collections.reverse(way);
            return new SearchResult<>(way, start, filter.target);
        }

        return new SearchResult<>(Collections.EMPTY_LIST, start, null);
    }
    
    public final SearchResult<ElementType> breadthSearch(SearchNodeFilter<ElementType> filter, ElementType start) {
        return search(filter, start, new QueueCollection());
    }
    
    public final SearchResult<ElementType> depthSearch(SearchNodeFilter<ElementType> filter, ElementType start) {
        return search(filter, start, new StackCollection());
    }
    
    public static abstract class SearchNodeFilter<ElementType> extends NodeFilter<ElementType> {

        private ElementType target;

        protected abstract boolean isTarget(ElementType node, int level);
        
        @Override
        protected final boolean accept(ElementType node, int level) {
            if (isTarget(node, level)) {
                target = node;
                stop();
            }
            return true;
        }

        public final boolean isObtained() {
            return target != null;
        }
    }

    public static final class SearchResult<ElementType> {

        public final List<ElementType> way;
        public final ElementType target;
        public final ElementType start;
        public final boolean isObtained;

        private SearchResult(List<ElementType> way, ElementType start, ElementType target) {
            this.way = way;
            this.target = target;
            this.start = start;
            this.isObtained = !way.isEmpty();
        }
    }
}
