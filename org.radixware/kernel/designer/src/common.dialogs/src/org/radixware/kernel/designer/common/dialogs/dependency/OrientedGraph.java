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
package org.radixware.kernel.designer.common.dialogs.dependency;

import java.util.*;
import org.radixware.kernel.common.utils.graphs.GraphWalker;

public abstract class OrientedGraph<T> {

    public enum EEdgeDirection {

        IN, OUT
    }

    public interface IFilter<T extends OrientedGraph.Node> {

        boolean accept(T node);
    }

    public class Node implements Comparable<Node> {

        private Map<String, Node> out = new HashMap<>();
        private Map<String, Node> in = new HashMap<>();
        private T object;

        public Node(T object) {
            this.object = object;
        }

        @Override
        public int compareTo(Node o) {
            return Objects.compare(getNodeName(this.getObject()), getNodeName(o.getObject()), String.CASE_INSENSITIVE_ORDER);
        }

        @Override
        public String toString() {
            return "[name: " + getName() + ", key: " + getKey() + "]";
        }

        public Map<String, Node> getOutNodes() {
            return out;
        }

        public Map<String, Node> getInNodes() {
            return in;
        }

        public T getObject() {
            return object;
        }

        public boolean isRoot() {
            return getInNodes().isEmpty();
        }

        public boolean isLeaf() {
            return getOutNodes().isEmpty();
        }

        public OrientedGraph<T> getOwnerGraph() {
            return OrientedGraph.this;
        }

        public void addIn(Node node) {
            in.put(node.getKey(), node);
        }

        public void addOut(Node node) {
            out.put(node.getKey(), node);
        }

        public String getKey() {
            return getNodeKey(getObject());
        }

        public String getName() {
            return getNodeName(getObject());
        }

        public List<Node> getAllInNodes() {

            final Set<Node> allInNodes = new HashSet<>();

            depthWalk(new GraphWalker.NodeFilter<Node>() {
                @Override
                protected boolean accept(Node node, int level) {
                    allInNodes.add(node);
                    return true;
                }

                @Override
                protected boolean view(Node node, int level) {
                    return true;
                }
            }, this, EEdgeDirection.IN);

            allInNodes.remove(this);
            return new ArrayList<>(allInNodes);
        }

        public List<Node> getAllOutNodes() {
            final Set<Node> allOutNodes = new HashSet<>();

            depthWalk(new GraphWalker.NodeFilter<Node>() {
                @Override
                protected boolean accept(Node node, int level) {
                    allOutNodes.add(node);
                    return true;
                }

                @Override
                protected boolean view(Node node, int level) {
                    return true;
                }
            }, this, EEdgeDirection.OUT);

            allOutNodes.remove(this);
            return new ArrayList<>(allOutNodes);
        }
    }

    public class NodesWay {

        private final List<Node> way;

        public NodesWay() {
            this(new LinkedList<Node>());
        }

        public NodesWay(List<Node> way) {
            this.way = new LinkedList<>(way);
        }

        boolean containsNode(Node node) {
            return way.contains(node);
        }

        public List<Node> getNodes() {
            return way;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();

            boolean first = true;
            for (Node node : way) {
                if (first) {
                    first = false;
                } else {
                    builder.append("=>");
                }
                builder.append(node.getName());
            }

            return builder.toString();
        }

        void add(Node node) {
            way.add(node);
        }

        void remove(int count) {
            if (count > 0) {
                if (way.size() >= count) {
                    for (int i = 0; i < count; i++) {
                        way.remove(way.size() - 1);
                    }
                } else {
                    throw new IndexOutOfBoundsException();
                }
            }
        }
    }

    private final Map<String, Node> nodes = new HashMap<>();
    private final GraphWalker<Node> inWalker = new GraphWalker<Node>() {
        @Override
        protected Collection<Node> collectNodes(Node source, GraphWalker.NodeFilter<Node> filter) {
            return source.getInNodes().values();
        }
    };
    private final GraphWalker<Node> outWalker = new GraphWalker<Node>() {
        @Override
        protected Collection<Node> collectNodes(Node source, GraphWalker.NodeFilter<Node> filter) {
            return source.getOutNodes().values();
        }
    };

    public OrientedGraph() {
    }

    public final Node findNode(T object) {
        return getNodes().get(getNodeKey(object));
    }

    public final boolean containsNode(T object) {
        return getNodes().containsKey(getNodeKey(object));
    }

    protected final Node findOrCreateNode(T object) {
        Node node = getNodes().get(getNodeKey(object));

        if (node == null) {
            node = createNode(object);
            getNodes().put(node.getKey(), node);
        }

        return node;
    }

    protected Node createNode(T object) {
        return new Node(object);
    }

    public final Map<String, Node> getNodes() {
        return nodes;
    }

    public final void build() {
        collectLinks();
    }

    public final void depthWalk(GraphWalker.NodeFilter<Node> filter, Node start, EEdgeDirection direction) {
        switch (direction) {
            case IN:
                inWalker.depthWalk(filter, start);
                break;
            case OUT:
                outWalker.depthWalk(filter, start);
                break;
        }
    }

    public final void breadthWalk(GraphWalker.NodeFilter<Node> filter, Node start, EEdgeDirection direction) {
        switch (direction) {
            case IN:
                inWalker.breadthWalk(filter, start);
                break;
            case OUT:
                outWalker.breadthWalk(filter, start);
                break;
        }
    }

    public boolean hasWay(final IFilter<Node> filter, final Node from, final Node to, EEdgeDirection direction) {
        final boolean[] result = {false};
        breadthWalk(new GraphWalker.NodeFilter<Node>() {
            @Override
            protected boolean accept(Node node, int level) {
                if (node == to) {
                    result[0] = true;
                    stop();
                }
                return true;
            }

            @Override
            protected boolean view(Node node, int level) {
                return (filter == null || filter.accept(node));
            }
        }, from, direction);
        return result[0];
    }

    protected abstract String getNodeKey(T object);

    protected abstract void collectLinks();

    protected abstract String getNodeName(T object);
}
