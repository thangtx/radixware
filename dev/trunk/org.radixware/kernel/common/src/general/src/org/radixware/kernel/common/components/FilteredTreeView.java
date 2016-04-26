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

package org.radixware.kernel.common.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;


public class FilteredTreeView extends TreeView {

    public static class Item implements Comparable {

        private class Node extends TreeView.Node {

            public Node(Object userObject) {
                super(userObject);
            }

            @Override
            public boolean isLeaf() {
                return Item.this.isLeaf();
            }

            @Override
            public String getToolTipText() {
                return Item.this.getToolTipText();
            }

            private Item getItem() {
                return Item.this;
            }

            @Override
            public int compareTo(Object obj) {
                if (obj instanceof Node) {
                    final Node node = (Node) obj;
                    return this.getItem().compareTo(node.getItem());
                } else {
                    throw new IllegalStateException();
                }
            }
        }
        private final TreeView.Node node;
        private ArrayList<Item> children = null;
        private Item parent;
        private boolean visible = true;

        public Item(Object userObject) {
            this.node = new Node(userObject);
        }

        protected Item(TreeView.Node rootNode) {
            this.node = rootNode;
        }

        public final Color getColor() {
            return node.getColor();
        }

        public void setColor(Color color) {
            node.setColor(color);
        }

        public final String getDisplayName() {
            return node.getDisplayName();
        }

        public void setDisplayName(String displayName) {
            node.setDisplayName(displayName);
        }

        public final Icon getIcon() {
            return node.getIcon();
        }

        public void setIcon(Icon icon) {
            node.setIcon(icon);
        }

        public Object getUserObject() {
            return node.getUserObject();
        }

        public String getToolTipText() {
            return null;
        }

        public Item getParent() {
            return parent;
        }

        public boolean isEmpty() {
            return children == null || children.isEmpty();
        }

        public final boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            if (this.visible != visible) {
                this.visible = visible;
                if (visible) {
                    if (parent != null) {
                        parent.node.add(node);
                    }
                } else {
                    if (node.getParent() != null) {
                        node.remove();
                    }
                }
            }
        }

        private void register() {
            final Root root = findRoot();
            if (root != null) {
                if (children != null) {
                    for (Item child : children) {
                        child.register();
                    }
                }
                root.registerItem(this);
            }
        }

        private void unregister() {
            final Root root = findRoot();
            if (root != null) {
                if (children != null) {
                    for (Item child : children) {
                        child.unregister();
                    }
                }
                root.unregisterItem(this);
            }
        }
        
        public void add(Item item) {
            add(item, true);
        }

        public void add(Item item, boolean sorted) {
            if (item.parent != null) {
                throw new IllegalStateException("Attempt to add item twice");
            }

            item.parent = this;
            if (children == null) {
                children = new ArrayList<Item>();
            }
            children.add(item);
            item.register();

            if (item.visible) {
                node.add(item.node, sorted);
            }
        }

        public void remove() {
            if (parent != null) {
                unregister();
                parent.children.remove(this);
                parent = null;
                if (visible) {
                    node.remove();
                }
            }
        }

        public boolean isLeaf() {
            return false;
        }

        private Root findRoot() {
            for (Item item = this; item != null; item = item.getParent()) {
                if (item instanceof Root) {
                    return (Root) item;
                }
            }
            return null;
        }

        public int getChildCount() {
            return (children != null ? children.size() : 0);
        }

        /**
         * @return non-ordered collection of children
         */
        public Collection<Item> getChildren() {
            if (children == null) {
                return Collections.emptyList();
            } else {
                return Collections.unmodifiableList(children);
            }
        }

        public void clear() {
            if (!isEmpty()) {
                for (int i = 0; i < children.size(); i++) {
                    final Item child = children.get(i);
                    child.unregister();
                    child.parent = null;
                }
                children.clear();
                node.clear();
            }
        }

        public void setExpanded(boolean expanded) {
            node.setExpanded(expanded);
        }

        public boolean isExpanded() {
            return node.isExpanded();
        }

        @Override
        public int compareTo(Object obj) {
            if (obj instanceof Item) {
                final Item item = (Item) obj;
                return getDisplayName().compareTo(item.getDisplayName());
            } else {
                throw new IllegalStateException();
            }
        }
    }

    private static class Root extends Item {

        private final Map<Object, Item> userObject2Item = new HashMap<Object, Item>();

        public Root(Node rootNode) {
            super(rootNode);
        }

        public Item findItemByUserObject(Object userObject) {
            return userObject2Item.get(userObject);
        }

        public void registerItem(Item item) {
            final Object userObject = item.getUserObject();
            userObject2Item.put(userObject, item);
        }

        public void unregisterItem(Item item) {
            final Object userObject = item.getUserObject();
            userObject2Item.remove(userObject);

        }
    }
    private final Root root;

    public FilteredTreeView() {
        super();
        final Node rootNode = getRootNode();
        root = new Root(rootNode);
    }

    public Item findItemByUserObject(Object userObject) {
        return root.findItemByUserObject(userObject);
    }

    public Item getRoot() {
        return root;
    }
}
