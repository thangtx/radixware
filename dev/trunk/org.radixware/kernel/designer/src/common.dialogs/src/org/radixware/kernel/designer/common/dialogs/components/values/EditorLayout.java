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

package org.radixware.kernel.designer.common.dialogs.components.values;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class EditorLayout implements LayoutManager2 {

    public static final String LEADER_CONSTRAINT = "LEADER_COMPONENT";
    private final Map<Component, Object> constraints = new HashMap<>();
    private Component leader;
    //---------------------------------------------------------------------
    private int leaderWidth;
    private int center;
    private int height;
    private int startX;
    private int startY;
    private volatile boolean isValid = false;
    //---------------------------------------------------------------------
    private final int defaultSize;
    
    public EditorLayout() {
        this(-1);
    }
    
    public EditorLayout(int defaultSize) {
        this.defaultSize = defaultSize;
    }
    
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        //  for the future, to expansion
        if (constraints != null) {
            this.constraints.put(comp, constraints);
        }
        if (comp != null && LEADER_CONSTRAINT.equals(constraints)) {
            leader = comp;
        }
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
        isValid = false;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, name);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        if (leader == comp) {
            leader = null;
        }
        constraints.remove(comp);
    }

    @Override
    public Dimension maximumLayoutSize(Container parent) {
        final int maxHeight = leader != null ? leader.getMaximumSize().height : Integer.MAX_VALUE;
        return new Dimension(Integer.MAX_VALUE, maxHeight);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            evaluate(parent);

            final Dimension prefLeaderSize = leader != null ? leader.getPreferredSize() : new Dimension(0, 25);
            return new Dimension(getVisibleComponentSize(parent) + prefLeaderSize.width, prefLeaderSize.height);
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        final int minHeight = leader != null ? leader.getMinimumSize().height : 0;
        return new Dimension(0, minHeight);
    }

    @Override
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {

            evaluate(parent);

            int currentX = startX;
            int currentY = startY;
            for (final Component component : parent.getComponents()) {
                final int width = getWidth(component, height);

                component.setBounds(currentX, currentY, width, height);

                currentX += width;
            }
        }
    }

    private int getWidth(Component component, int def) {
        if (component == leader) {
            return leaderWidth;
        }
        return def;
    }

    private int getVisibleComponentSize(Container parent) {
        int size = 0;

        for (final Component component : parent.getComponents()) {
            if (component != leader && component.isVisible()) {
                size += getWidth(component, height);
            }
        }
        return size;
    }

    private void evaluate(Container parent) {
        if (!isValid) {
            isValid = true;

            final Insets insets = parent.getInsets();
            final int top = insets.top;
            final int bottom = parent.getHeight() - insets.bottom;
            final int left = insets.left;
            final int right = parent.getWidth() - insets.right;

            startX = left;
            startY = top;
            height = defaultSize != -1 ? defaultSize : bottom - top; //Math.min(bottom - top, leader.getPreferredSize().height);
            center = Math.round(top + height / 2.0f);
            leaderWidth = (right - left) - getVisibleComponentSize(parent);
        }
    }
}
