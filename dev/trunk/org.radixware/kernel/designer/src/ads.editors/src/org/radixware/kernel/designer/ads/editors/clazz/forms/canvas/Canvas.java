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

package org.radixware.kernel.designer.ads.editors.clazz.forms.canvas;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Canvas extends JPanel {

    public static abstract class Item {

        private int localZIndex = 0;
        private Item parent = null;
        private Rectangle geometry = new Rectangle(0, 0, 10, 10);
        private List<Item> children = null;

        public Item getParent() {
            return parent;
        }

        public abstract Painter getPainter();

        public Rectangle getGeometry() {
            return geometry;
        }

        public void setGeometry(Rectangle rect) {
            this.geometry = new Rectangle(rect);
        }

        public Rectangle getClientRect() {
            return geometry;
        }

        public List<Item> getChildren() {
            if (children == null) {
                return Collections.emptyList();
            } else {
                return new ArrayList<Item>(children);
            }
        }

        public void addChild(Item item) {
            if (children == null) {
                children = new LinkedList<Item>();
            }
            if (item.parent != null) {
                if (item.parent.children != null) {
                    item.parent.children.remove(item);
                }
            }
            item.parent = this;
            children.add(item);
            item.bringToFront();
        }

        private void bringToFront() {
            if (parent != null && parent.children != null && parent.children.contains(this)) {
                List<Item> sorted = new ArrayList<Item>(parent.children);
                Collections.sort(sorted, new Comparator<Item>() {

                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.localZIndex == o2.localZIndex ? 0 : o1.localZIndex > o2.localZIndex ? 1 : -1;
                    }
                });

                sorted.remove(this);
                sorted.add(this);
                for (int i = 0; i < sorted.size(); i++) {
                    sorted.get(i).localZIndex = i + 1;
                }
                Collections.sort(parent.children, new Comparator<Item>() {

                    @Override
                    public int compare(Item o1, Item o2) {
                        return o1.localZIndex == o2.localZIndex ? 0 : o1.localZIndex > o2.localZIndex ? 1 : -1;
                    }
                });
            }
        }

        public boolean isContainerFor(Item item) {
            return true;
        }

        public Rectangle getLocatorBounds(Point point) {
            return getGeometry();
        }

        private boolean isParentOf(Item item) {
            if (item == null) {
                return false;
            }
            item = item.getParent();
            while (item != null) {
                if (item == this) {
                    return true;
                }
                item = item.getParent();
            }
            return false;
        }
    }

    public interface Painter<T extends Item> {

        public void drawItem(T item, Rectangle bounds, Graphics2D graphics);
    }
    private Item root;
    private List<Item> selection = new LinkedList<Item>();
    private Point mousePos = null;
    private Set<Item> locatableItems = new HashSet<Item>();

    public Canvas() {
        setOpaque(true);
        setVisible(true);
        MouseAdapter adapter = new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mousePos != null) {
                    Item item = getSelectedItem();
                    if (item != null && item != root) {
                        Rectangle r = new Rectangle(item.getGeometry());
                        Point p = e.getPoint();
                        r.x += (p.x - mousePos.x);
                        r.y += (p.y - mousePos.y);
                        mousePos = p;
                        item.setGeometry(r);

                        Item parentCandidate = findItemAtPoint(mousePos, Collections.singleton(item));
                        locatableItems.clear();
                        if (parentCandidate != null && parentCandidate.isContainerFor(item)) {
                            locatableItems.add(item);
                        }

                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                revalidate();
                                repaint();
                            }
                        });
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Item item = getSelectedItem();
                if (item != null && item != root) {
                    Item parentCandidate = findItemAtPoint(mousePos, Collections.singleton(item));
                    if (parentCandidate != item && !item.isParentOf(parentCandidate) && parentCandidate != null && parentCandidate.isContainerFor(item)) {
                        //recompute coordinates according to parent
                        Point parentLocation = getAbsoluteLocation(parentCandidate);
                        Point thisLocation = getAbsoluteLocation(item);
                        Rectangle itemGeometry = new Rectangle(item.getGeometry());
                        Rectangle parentClientBounds = new Rectangle(parentCandidate.getClientRect());
                        itemGeometry.x = thisLocation.x - (parentLocation.x + parentClientBounds.x);
                        itemGeometry.y = thisLocation.y - (parentLocation.y + parentClientBounds.y);
                        item.setGeometry(itemGeometry);
                        parentCandidate.addChild(item);
                    }
                }
                mousePos = null;
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        revalidate();
                        repaint();
                    }
                });
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mousePos = e.getPoint();
                Item pointedItem = findItemAtPoint(mousePos);
                if (pointedItem != null) {
                    pointedItem.bringToFront();
                    setSelectedItem(pointedItem);
                }
            }
        };
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
    }

    public Item getRoot() {
        return root;
    }

    public void setRoot(Item root) {
        this.root = root;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Rectangle clip = g.getClipBounds();
        if (clip == null) {
            clip = new Rectangle(0, 0, getWidth(), getHeight());
        }
        Graphics2D graphics = (Graphics2D) g.create();
        try {
            graphics.setBackground(Color.white);
            graphics.clearRect(clip.x, clip.y, clip.width, clip.height);
            Item rootItem = getRoot();
            if (rootItem != null) {
                graphics.translate(5, 5);
                paintItem(rootItem, graphics, true);
            }
        } finally {
            graphics.dispose();
        }
        if (!selection.isEmpty()) {

            for (Item item : selection) {
                Graphics2D selectedItemGraphics = (Graphics2D) g.create();
                try {
                    Item parent = item.getParent();
                    if (parent == null) {
                        selectedItemGraphics.translate(5, 5);
                    } else {
                        Point abs = getAbsoluteLocation(parent);
                        Rectangle cr = parent.getClientRect();
                        abs.x += cr.x;
                        abs.y += cr.y;
                        selectedItemGraphics.translate(abs.x, abs.y);
                    }
                    paintItem(item, selectedItemGraphics, false);
                } finally {
                    selectedItemGraphics.dispose();
                }
                drawHandle(g, item);
            }
        }
    }

    private void drawHandle(Graphics graphics, Item item) {
        Rectangle itemRect = new Rectangle(item.getGeometry());
        itemRect.setLocation(getAbsoluteLocation(item));
        itemRect.x -= 4;
        itemRect.y -= 4;
        itemRect.width += 8;
        itemRect.height += 8;
        Graphics2D handleGraphics = (Graphics2D) graphics.create();
        try {
            handleGraphics.setColor(Color.black);
            handleGraphics.drawRect(itemRect.x, itemRect.y, itemRect.width, itemRect.height);
            int halfWidth = itemRect.width / 2;
            int halfHeight = itemRect.height / 2;
            Graphics2D rectGraphics = (Graphics2D) graphics.create();
            try {
                rectGraphics.setBackground(Color.blue);
                rectGraphics.setColor(Color.LIGHT_GRAY);
                drawHandleRect(rectGraphics, new Point(itemRect.x, itemRect.y));
                drawHandleRect(rectGraphics, new Point(itemRect.x + halfWidth, itemRect.y));
                drawHandleRect(rectGraphics, new Point(itemRect.x + itemRect.width, itemRect.y));
                drawHandleRect(rectGraphics, new Point(itemRect.x + itemRect.width, itemRect.y + halfHeight));
                drawHandleRect(rectGraphics, new Point(itemRect.x + itemRect.width, itemRect.y + itemRect.height));
                drawHandleRect(rectGraphics, new Point(itemRect.x + halfWidth, itemRect.y + itemRect.height));
                drawHandleRect(rectGraphics, new Point(itemRect.x, itemRect.y + itemRect.height));
                drawHandleRect(rectGraphics, new Point(itemRect.x, itemRect.y + halfHeight));
            } finally {
                rectGraphics.dispose();
            }
        } finally {
            handleGraphics.dispose();
        }
    }

    private void drawHandleRect(Graphics graphics, Point center) {
        graphics.clearRect(center.x - 3, center.y - 3, 6, 6);
        graphics.drawRect(center.x - 3, center.y - 3, 6, 6);
    }

    private Point getAbsoluteLocation(Item item) {
        Point p = new Point(5, 5);
        while (item != null) {
            Rectangle r = item.getGeometry();
            p.x += r.x;
            p.y += r.y;
            item = item.getParent();
            if (item != null) {
                r = item.getClientRect();
                p.x += r.x;
                p.y += r.y;
            }
        }
        return p;
    }

    public Item getSelectedItem() {
        return selection.isEmpty() ? null : selection.get(0);
    }

    public Item findItemAtPoint(Point point) {
        return findItemAtPoint(point, Collections.<Item>emptySet());
    }

    private Item findItemAtPoint(Point point, Set<Item> exclude) {
        if (root == null) {
            return null;
        } else {
            return findItemAtPoint(root, new Point(point.x - 5, point.y - 5), exclude);
        }
    }

    private Item findItemAtPoint(Item current, Point point, Set<Item> exclude) {
        Rectangle rect = current.getGeometry();
        if (rect.x <= point.x && rect.y <= point.y && rect.x + rect.width >= point.x && rect.y + rect.height >= point.y) {
            Rectangle clientRect = current.getClientRect();
            Point localPoint = new Point(point);
            localPoint.x -= (rect.x + clientRect.x);
            localPoint.y -= (rect.y + clientRect.y);
            List<Item> matchedChildren = new LinkedList<Item>();
            for (Item child : current.getChildren()) {
                Item result = findItemAtPoint(child, localPoint, exclude);
                if (result != null && !exclude.contains(result)) {
                    matchedChildren.add(result);
                }
            }
            if (matchedChildren.isEmpty()) {
                return exclude.contains(current) ? null : current;
            } else {
                Item result = matchedChildren.get(0);
                int zIndex = getZIndex(result);
                for (Item child : matchedChildren) {
                    int childZIndex = getZIndex(child);
                    if (childZIndex > zIndex) {
                        zIndex = childZIndex;
                        result = child;
                    }
                }
                return result;
            }
        } else {
            return null;
        }
    }

    private int getZIndex(Item item) {
        int zIndex = 0;
        while (item != null) {
            zIndex += item.localZIndex;
            item = item.getParent();
        }
        return zIndex;
    }

    public void setSelectedItem(Item item) {
        selection.clear();
        selection.add(item);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                revalidate();
                repaint();
            }
        });
    }

    private void paintItem(Item item, Graphics2D graphics, boolean ignoreSelection) {
        if (ignoreSelection && selection.contains(item)) {
            return;
        }
        Painter painter = item.getPainter();
        Rectangle itemRect = item.getGeometry();
        painter.drawItem(item, itemRect, graphics);

        Rectangle clientRect = item.getClientRect();

        Rectangle clipRect = new Rectangle(0, 0, clientRect.width, clientRect.height);

        for (Item child : item.getChildren()) {

            Graphics2D childGraphics = (Graphics2D) graphics.create();
            try {
                childGraphics.translate(clientRect.x + itemRect.x, clientRect.y + itemRect.y);
                childGraphics.setClip(clipRect);
                paintItem(child, childGraphics, ignoreSelection);


            } finally {
                childGraphics.dispose();
            }
        }

        
    }
}
