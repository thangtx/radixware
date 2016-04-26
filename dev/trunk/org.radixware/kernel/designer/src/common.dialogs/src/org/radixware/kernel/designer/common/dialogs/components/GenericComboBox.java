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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


public class GenericComboBox extends JComboBox<String> {

    private final static class MultiselectComboBoxPopup extends JPopupMenu {

        private final GenericComboBox comboBox;
        private final PopupView view;

        public MultiselectComboBoxPopup(GenericComboBox comboBox, PopupView view) {
            this.comboBox = comboBox;
            this.view = view;

            setLayout(new BorderLayout());
            add(view.getComponent(), BorderLayout.CENTER);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }

        public void toggle() {
            if (!isVisible()) {
                showPopup();
            } else {
                hidePopup();
            }
        }

        void showPopup() {
            view.update();
            show(comboBox, 0, comboBox.getHeight());
        }

        void hidePopup() {
            setVisible(false);
        }

        @Override
        public Dimension getPreferredSize() {
            final Dimension preferredSize = super.getPreferredSize();
            final Dimension cmbSize = comboBox.getSize();
            if (preferredSize.width >= cmbSize.width) {
                return preferredSize;
            }
            return new Dimension(cmbSize.width, preferredSize.height);
        }
    }

    private final static class MouseHandler implements MouseListener, PopupMenuListener {

        private final GenericComboBox comboBox;

        public MouseHandler(GenericComboBox comboBox) {
            this.comboBox = comboBox;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e) || !comboBox.isEnabled()) {
                return;
            }
            if (comboBox.isRequestFocusEnabled()) {
                comboBox.requestFocus();
            }
            comboBox.togglePopup();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            final Component src = (Component) e.getSource();
            final Dimension size = src.getSize();
            final Rectangle bounds = new Rectangle(0, 0, size.width - 1, size.height - 1);
            if (!bounds.contains(e.getPoint())) {
                comboBox.hidePopup();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            comboBox.fireActionEvent();
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
//            comboBox.fireActionEvent();
        }
    }

    public static abstract class Model {

        public abstract String getName();
    }

    public static abstract class PopupView {

//        public abstract void open(Model model);

        public abstract void update();

        public abstract JComponent getComponent();

        public abstract Model getModel();
    }

    private final MultiselectComboBoxPopup popup;
    private final MouseHandler mouseHandler;
    private final PopupView view;
//    private final EventsSupport<ActionListener, ActionEvent> eventsSupport;

    public GenericComboBox(PopupView view) {
        super(new String[]{view.getModel().getName()});

        this.view = view;

        mouseHandler = new MouseHandler(this);
        popup = new MultiselectComboBoxPopup(this, view);
        popup.addPopupMenuListener(mouseHandler);

        JButton btn = null;
        for (Component component : getComponents()) {
            if (component instanceof JButton) {
                btn = (JButton) component;
            }
        }

        // hack to replace default popup
        // remove default listeners
        if (btn != null) {
            final MouseListener[] listeners = btn.getListeners(MouseListener.class);
            if (listeners != null) {
                for (MouseListener listener : listeners) {
                    if (listener.getClass().getName().contains("BasicComboPopup$Handler")) {
                        btn.removeMouseListener(listener);
                    }
                }
            }

            btn.addMouseListener(mouseHandler);
        }

        final MouseListener[] listeners = this.getListeners(MouseListener.class);
        if (listeners != null) {
            for (MouseListener listener : listeners) {
                if (listener.getClass().getName().contains("BasicComboPopup$Handler")) {
                    this.removeMouseListener(listener);
                }
            }
        }

        addMouseListener(mouseHandler);
    }

    @Override
    public void setPopupVisible(boolean visible) {
        if (popup != null) {
            if (visible) {
                popup.showPopup();
            } else {
                popup.hidePopup();
            }
        }
    }

    public void togglePopup() {
        popup.toggle();
    }

    @Override
    public boolean isPopupVisible() {
        return popup != null && popup.isVisible();
    }

    public PopupView getView() {
        return view;
    }
}
