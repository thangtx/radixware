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

package org.radixware.kernel.designer.ads.editors.classcatalogs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import javax.swing.event.ChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.Index;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.Inheritance.ClassHierarchySupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsClassCatalogDef;
import org.radixware.kernel.designer.ads.editors.classcatalogs.ClassCatalogItemNode.ClassCatalogCopyEvent;


public class ItemChildren extends Children.Keys<RadixObject> implements Index {

    private class ClassCatalogNodeIndex extends Index.Support {

        @Override
        public Node[] getNodes() {
            return ItemChildren.this.getNodes();
        }

        @Override
        public int getNodesCount() {
            return ItemChildren.this.getChildCount();
        }

        @Override
        public void reorder(int[] perm) {
            List<RadixObject> klCopy = ItemChildren.this.keyList;

            final List<RadixObject> old = new LinkedList<RadixObject>(klCopy);

            boolean canReoder = ItemChildren.this.draggedChild != null;

            if (canReoder) {
                final List<Double> oldOrder = new LinkedList<Double>();
                for (RadixObject r : old) {
                    oldOrder.add(((AdsClassCatalogDef.IClassCatalogItem) r).getOrder());
                }
                for (int i = 0, size = old.size() - 1; i <= size; i++) {
                    RadixObject r = old.get(i);
                    klCopy.set(perm[i], r);
                }

                int kIndex = 0, size = klCopy.size();
                boolean stop = false;
                while (kIndex < size && !stop) {
                    RadixObject current = klCopy.get(kIndex);
                    if (current.equals(ItemChildren.this.draggedChild.getRadixObject())) {
                        double next = kIndex < size - 1 ? ((AdsClassCatalogDef.IClassCatalogItem) klCopy.get(kIndex + 1)).getOrder() : 0D;
                        double prev = kIndex > 0 ? ((AdsClassCatalogDef.IClassCatalogItem) klCopy.get(kIndex - 1)).getOrder() : 0D;
                        double o = Math.abs(next - prev) / 2 + prev;

                        if (prev == 0D) {
                            o = next == 0D ? -100D : next / 2;
                        } else {
                            if (next < 0 && prev < 0) {
                                o = -((Math.abs(prev) - Math.abs(next)) / 2 + Math.abs(next));
                            } else if (prev < 0 && next > 0) {
                                o = 0D;
                            } else if (prev < 0 && next == 0) {
                                o = prev / 2;
                            }
                        }

                        if (o != ((AdsClassCatalogDef.IClassCatalogItem) current).getOrder()) {
                            ((AdsClassCatalogDef.IClassCatalogItem) draggedChild.getRadixObject()).setOrder(o);
                        }
                        stop = true;
                    } else {
                        kIndex++;
                    }
                }
            }
            ItemChildren.this.addNotify();
        }
    }
    private final Index index = new ClassCatalogNodeIndex();
    private HashMap<RadixObject, Object> map;
    private LinkedList<RadixObject> keyList;
    private AdsClassCatalogDef rootContext;
    private ClassCatalogItemNode draggedChild;
    private ClassCatalogItemNode.ClassCatalogCopyListener copyListener = new ClassCatalogItemNode.ClassCatalogCopyListener() {

        @Override
        public void onEvent(ClassCatalogCopyEvent e) {
            ItemChildren.this.draggedChild = e.dragged;
        }
    };

    private ClassHierarchySupport hSupport;

    ItemChildren(final AdsClassCatalogDef rootContext, HashMap<RadixObject, Object> children, ClassHierarchySupport hSupport) {
        this.rootContext = rootContext;
        this.hSupport = hSupport;
        this.map = children;
        this.keyList = new LinkedList<RadixObject>();
        if (map != null) {
            keyList.addAll(map.keySet());
        }
    }

    public HashMap<RadixObject, Object> getChildrenMap() {
        return map;
    }

   
    public boolean removeNodes(final Node[] nodes) {
        if (map != null) {
            for (Node n : nodes) {
                map.remove(((ClassCatalogItemNode) n).getRadixObject());
                keyList.remove(((ClassCatalogItemNode) n).getRadixObject());
            }
            destroyNodes(nodes);
            addNotify();
            return true;
        }
        return false;
    }

    public boolean addNodes(final Node[] nodes) {
        for (Node n : nodes) {
            if (map == null) {
                map = new HashMap<RadixObject, Object>();
            }
            final RadixObject nodeRdx = ((ClassCatalogItemNode) n).getRadixObject();
            if (nodeRdx instanceof AdsClassCatalogDef.ClassReference) {
                map.put(nodeRdx, null);
            } else {
                map.put(nodeRdx, ((ItemChildren) n.getChildren()).getChildrenMap());
            }
            keyList.add(((ClassCatalogItemNode) n).getRadixObject());
        }
        addNotify();
        return true;
    }

    public void addNodeAt(ClassCatalogItemNode node, int index) {
        final RadixObject nodeRdx = node.getRadixObject();

        keyList.add(index, nodeRdx);
        if (map == null) {
            map = new HashMap<RadixObject, Object>();
        }
        if (nodeRdx instanceof AdsClassCatalogDef.ClassReference) {
            map.put(nodeRdx, null);
        } else {
            map.put(nodeRdx, ((ItemChildren) node.getChildren()).getChildrenMap());
        }
        addNotify();
    }

    public void addNodeAt(RadixObject obj, int index) {
        keyList.add(index, obj);
        if (map == null) {
            map = new HashMap<RadixObject, Object>();
        }
        if (obj instanceof AdsClassCatalogDef.ClassReference) {
            map.put(obj, null);
        } else {
            map.put(obj, new LinkedHashMap<RadixObject, Object>());
        }

        addNotify();
    }

    public int getChildCount() {
        return keyList.size();
    }

    public int getChildIndex(Node node) {
        return keyList.indexOf(((ClassCatalogItemNode) node).getRadixObject());
    }

    public RadixObject getChildAfter(RadixObject obj) {
        if (keyList.contains(obj) &&
                !obj.equals(keyList.getLast())) {
            return keyList.get(keyList.indexOf(obj) + 1);
        }
        return null;
    }

    @Override
    protected void addNotify() {
        setKeys(keyList);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Node[] createNodes(RadixObject key) {
        if (map != null) {
            Object obj = map.get(key);
            boolean inCurrentCatalog = false;
            boolean inOveriddenCatalog = false;

            AdsClassCatalogDef overidden = rootContext.getHierarchy().findOverridden().get();
            
            if (key instanceof AdsClassCatalogDef.ClassReference) {
                inCurrentCatalog = rootContext.equals(((AdsClassCatalogDef.ClassReference) key).getOwnerClassCatalog());
                inOveriddenCatalog = overidden != null && overidden.equals(((AdsClassCatalogDef.ClassReference) key).getOwnerClassCatalog());
            } else {
                inCurrentCatalog = rootContext.equals(((AdsClassCatalogDef.Topic) key).getOwnerClassCatalog());
                inOveriddenCatalog = overidden != null && overidden.equals(((AdsClassCatalogDef.Topic) key).getOwnerClassCatalog());
            }
            if (obj != null) {
                ClassCatalogItemNode newNode = new ClassCatalogItemNode(inOveriddenCatalog, inCurrentCatalog, rootContext.isReadOnly(), key, new ItemChildren(rootContext, (HashMap<RadixObject, Object>) obj, hSupport), hSupport);
                newNode.getCopySupport().addEventListener(copyListener);
                return new Node[]{newNode};
            } else {
                ClassCatalogItemNode newNode = new ClassCatalogItemNode(inOveriddenCatalog, inCurrentCatalog, rootContext.isReadOnly(), key, LEAF, hSupport);
                newNode.getCopySupport().addEventListener(copyListener);
                return new Node[]{newNode};
            }
        }
        return new Node[0];
    }

    @Override
    public void removeChangeListener(ChangeListener chl) {
        index.removeChangeListener(chl);
    }

    @Override
    public void addChangeListener(ChangeListener chl) {
        index.addChangeListener(chl);
    }

    @Override
    public void move(int x, int y) {
        index.move(x, y);
    }

    @Override
    public void moveDown(int x) {
        index.moveDown(x);
    }

    @Override
    public void moveUp(int x) {
        index.moveUp(x);
    }

    @Override
    public void exchange(int x, int y) {
        index.exchange(x, y);
    }

    @Override
    public void reorder(int[] perm) {
        index.reorder(perm);
    }

    @Override
    public void reorder() {
        index.reorder();
    }

    @Override
    public int indexOf(Node node) {
        return index.indexOf(node);
    }
}
