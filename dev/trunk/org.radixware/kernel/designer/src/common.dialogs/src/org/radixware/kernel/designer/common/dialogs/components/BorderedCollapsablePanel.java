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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;


public class BorderedCollapsablePanel extends CollapsablePanel {

    public static final String PROPERTY_BORDER = "Border.Paint";

    public static abstract class TopComponent implements ITopComponent {

        private final JPanel topPanel;
        private final JPanel topContent;

        public TopComponent() {
            topPanel = new JPanel();
            topPanel.setOpaque(false);
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            topPanel.add(Box.createHorizontalStrut(8));
            topContent = new JPanel();
            topContent.setLayout(new BoxLayout(topContent, BoxLayout.X_AXIS));
            topPanel.add(topContent);
        }

        public final void addComponent(Component component, int index) {
            topContent.add(component, index);
        }

        @Override
        public final JPanel getComponent() {
            return topPanel;
        }
        
        public void setVisible(boolean visible) {
            topContent.setVisible(visible);
        }
        
        protected final JPanel getContentContainer() {
            return topContent;
        }
    }

    protected static class CheckBoxComponent extends TopComponent {

        private final JCheckBox checkBox;

        public CheckBoxComponent(String title) {
            checkBox = new JCheckBox(title);
            checkBox.setFocusable(false);
            addComponent(checkBox, 0);
        }

        @Override
        public void addActionListener(ActionListener listener) {
            checkBox.addActionListener(listener);
        }

        @Override
        public void removeActionListener(ActionListener listener) {
            checkBox.removeActionListener(listener);
        }

        @Override
        public boolean isExpanded() {
            return checkBox.isSelected();
        }

        @Override
        public void setExpanded(boolean expand) {
            checkBox.setSelected(expand);
        }

        @Override
        public void setTitle(String title) {
            checkBox.setText(title);
        }

        @Override
        public void setEnabled(boolean enabled) {
            checkBox.setEnabled(enabled);
        }
        
        public void setVisible(boolean visible) {
            checkBox.setVisible(visible);
        }
    }

    protected static class ContentProvider implements IContentProvider {

        private final JPanel panel;

        public ContentProvider(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public JPanel getContent() {
            return panel;
        }
    }

    public BorderedCollapsablePanel(JPanel panel, String title, boolean expand) {
        this(new CheckBoxComponent(title), new ContentProvider(panel), expand);
    }

    public BorderedCollapsablePanel(ITopComponent component, IContentProvider contentProvider, boolean expand) {
        super(component, contentProvider, expand);

        putClientProperty(PROPERTY_BORDER, Boolean.TRUE);

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case PROPERTY_BORDER:
                        getTopComponent().getComponent().setVisible(Objects.equals(Boolean.TRUE, evt.getNewValue()));
                        revalidate();
                        repaint();
                        break;
                }
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintComponentBorder(g);
    }

    protected void paintComponentBorder(Graphics g) {
        final Object property = getClientProperty(PROPERTY_BORDER);

        if (Objects.equals(Boolean.TRUE, property)) {
            final Rectangle bounds = getBounds();
            final Border border = UIManager.getBorder("TitledBorder.border");
            Component component  = getTopComponent().getComponent();
            final int indent = 2;
            final int height = component.getPreferredSize().height;

            border.paintBorder(this, g, indent, height / 2, bounds.width - indent * 2, bounds.height - height / 2 - indent );
        }
    }

    @Override
    public Insets getInsets() {
        Border border = UIManager.getBorder("TitledBorder.border");
        Insets borderInsets = border.getBorderInsets(getTopComponent().getComponent());
        return new Insets(0, borderInsets.left + 4, borderInsets.bottom + 4, borderInsets.right + 4);//new Insets(borderInsets.top, borderInsets.left + 4, borderInsets.bottom + 4, borderInsets.right + 4);
    }

    @Override
    public Insets getInsets(Insets insets) {
        if (insets != null) {
            Insets currentInsets  = getInsets();
            insets.set(currentInsets.top, currentInsets.left, currentInsets.bottom, currentInsets.right);
            return insets;
        }
        return getInsets();
    }

    @Override
    public final void setBorder(Border border) {
    }
}
