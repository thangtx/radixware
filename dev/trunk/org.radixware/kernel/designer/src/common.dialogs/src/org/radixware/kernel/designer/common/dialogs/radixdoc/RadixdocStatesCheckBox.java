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
package org.radixware.kernel.designer.common.dialogs.radixdoc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ActionMapUIResource;

public final class RadixdocStatesCheckBox extends JCheckBox {

//    void setState(Boolean TRUE) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    public static class State {

        private State() {
        }
    }
    static final long serialVersionUID = 0;
    public final static State NOT_SELECTED = new State();
    public final static State SELECTED = new State();
    public final static State DONT_CARE = new State();
    private final StateDecorator stateDecorator; 

    /**
     *
     * @param text
     * @param icon
     * @param initial
     */
    public RadixdocStatesCheckBox(String text, Icon icon, State initial) {
        super(text, icon);
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                grabFocus();
                stateDecorator.nextState();
            }
        });
        ActionMap map = new ActionMapUIResource();
        map.put("pressed", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                grabFocus();
                stateDecorator.nextState();
            }
        });
        map.put("released", null);
        SwingUtilities.replaceUIActionMap(this, map);
        stateDecorator = new StateDecorator(getModel());
        setModel(stateDecorator);
        setState(initial);
    }

    public RadixdocStatesCheckBox(String text, State initial) {
        this(text, null, initial);
    }

    public RadixdocStatesCheckBox(String text) {
        this(text, DONT_CARE);
    }

    public RadixdocStatesCheckBox() {
        this(null);
    }

    @Override
    public void addMouseListener(MouseListener l) {
    }

    public void setState(State state) {
        stateDecorator.setState(state);
    }

    public State getState() {
        return stateDecorator.getState();
    }

    @Override
    public void setSelected(boolean b) {
        if (b) {
            setState(SELECTED);
        } else {
            setState(NOT_SELECTED);
        }
    }

    private class StateDecorator implements ButtonModel {

        private final ButtonModel other;

        private StateDecorator(ButtonModel other) {
            this.other = other;
        }

        private void setState(State state) {
            if (state == NOT_SELECTED) {
                other.setArmed(false);
                setPressed(false);
                setSelected(false);
            } else if (state == SELECTED) {
                other.setArmed(false);
                setPressed(false);
                setSelected(true);
            } else {
                other.setArmed(true);
                setPressed(true);
                setSelected(false);
            }
        }

        private State getState() {
            if (isSelected() && !isArmed()) {
                return SELECTED;
            } else if (isSelected() && isArmed()) {
                return DONT_CARE;
            } else {
                return NOT_SELECTED;
            }
        }

        private void nextState() {
            State current = getState();
            if (current == NOT_SELECTED) {
                setState(SELECTED);
            } else if (current == SELECTED) {
                setState(DONT_CARE);
            } else if (current == DONT_CARE) {
                setState(NOT_SELECTED);
            }
        }

        @Override
        public void setArmed(boolean b) {
            other.setArmed(b);
        }

        @Override
        public void setEnabled(boolean b) {
            setFocusable(b);
            other.setEnabled(b);
        }

        @Override
        public boolean isArmed() {
            return other.isArmed();
        }

        @Override
        public boolean isSelected() {
            return other.isSelected();
        }

        @Override
        public boolean isEnabled() {
            return other.isEnabled();
        }

        @Override
        public boolean isPressed() {
            return other.isPressed();
        }

        @Override
        public boolean isRollover() {
            return other.isRollover();
        }

        @Override
        public int getMnemonic() {
            return other.getMnemonic();
        }

        @Override
        public String getActionCommand() {
            return other.getActionCommand();
        }

        @Override
        public Object[] getSelectedObjects() {
            return other.getSelectedObjects();
        }

        @Override
        public void setSelected(boolean b) {
            other.setSelected(b);
        }

        @Override
        public void setPressed(boolean b) {
            other.setPressed(b);
        }

        @Override
        public void setRollover(boolean b) {
            other.setRollover(b);
        }

        @Override
        public void setMnemonic(int key) {
            other.setMnemonic(key);
        }

        @Override
        public void setActionCommand(String s) {
            other.setActionCommand(s);
        }

        @Override
        public void setGroup(ButtonGroup group) {
            other.setGroup(group);
        }

        @Override
        public void addActionListener(ActionListener l) {
            other.addActionListener(l);
        }

        @Override
        public void removeActionListener(ActionListener l) {
            other.removeActionListener(l);
        }

        @Override
        public void addItemListener(ItemListener l) {
            other.addItemListener(l);
        }

        @Override
        public void removeItemListener(ItemListener l) {
            other.removeItemListener(l);
        }

        @Override
        public void addChangeListener(ChangeListener l) {
            other.addChangeListener(l);
        }

        @Override
        public void removeChangeListener(ChangeListener l) {
            other.removeChangeListener(l);
        }
    }
}
