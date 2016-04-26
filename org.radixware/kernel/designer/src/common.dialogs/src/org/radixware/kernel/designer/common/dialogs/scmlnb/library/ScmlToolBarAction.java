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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.netbeans.editor.BaseAction;
import org.openide.awt.Actions;
import org.openide.filesystems.FileObject;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;

/**
 * Parent class for all Scml specific toolbar actions.<br>
 * Supports locilized tooltip and title. They are searched
 * in Bundle of class provided by getClassForBundle() method.
 * Keys for searching are {@code getValue(NAME) + "-title"} and
 * {@code getValue(NAME) + "-tooltip"}
 */
public abstract class ScmlToolBarAction extends AbstractAction implements Presenter.Toolbar, Presenter.Popup {

    private ScmlEditor editor;
    private Component toolbarPresenter;
    private JMenuItem presenter;
    private boolean valuesInitied = false;

    public ScmlToolBarAction(String name, ScmlEditor editor) {
        super(name);
        this.editor = editor;
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (getToolbarPresenter() != null) {
                    if (isEnabled()) {
                        getToolbarPresenter().setEnabled(true);
                    } else {
                        getToolbarPresenter().setEnabled(false);
                    }
                }
            }
        });
        editor.addPropertyChangeListener("editable", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateState();
            }
        });
        editor.getPane().addPropertyChangeListener("scml", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateState();
            }
        });
    }

    @Override
    public Object getValue(String key) {
        if (!valuesInitied) {
            //Flag must be set before actual initialization
            //to prevent stack overflow in case when initValues()
            //will call getValue() itself.
            valuesInitied = true;
            initValues();
        }
        return super.getValue(key);
    }

    private void initValues() {
        putValue(Action.SMALL_ICON, getIcon());
        putValue(Action.SHORT_DESCRIPTION, getTooltip());
        putValue(Action.ACCELERATOR_KEY, getAcceleratorKey());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformed(e, getPane());
    }

    protected ScmlEditorPane getPane() {
        return editor.getPane();
    }

    protected ScmlEditor getEditor() {
        return editor;
    }

    /**
     * ToolBar presenter for this action.
     * Must always return the same component.
     * @return
     */
    @Override
    public Component getToolbarPresenter() {
        if (toolbarPresenter != null) {
            return toolbarPresenter;
        }
        JButton button = new JButton(this);
        if (getValue(ACCELERATOR_KEY) instanceof String) {
            String ack = (String) getValue(ACCELERATOR_KEY);
            KeyStroke ks = KeyStroke.getKeyStroke(ack);
            if (ks != null) {
                button.setToolTipText(button.getToolTipText() + " (" + getKeyMnemonic(ks) + ")");
            }
        }
        button.setText("");
        toolbarPresenter = button;
        return toolbarPresenter;
    }

    protected static String getKeyMnemonic(KeyStroke key) {
        String sk = org.openide.util.Utilities.keyToString(key);
        StringBuffer sb = new StringBuffer();
        int mods = key.getModifiers();
        if ((mods & KeyEvent.CTRL_MASK) != 0) {
            sb.append("Ctrl+"); // NOI18N
        }
        if ((mods & KeyEvent.ALT_MASK) != 0) {
            sb.append("Alt+"); // NOI18N
        }
        if ((mods & KeyEvent.SHIFT_MASK) != 0) {
            sb.append("Shift+"); // NOI18N
        }
        if ((mods & KeyEvent.META_MASK) != 0) {
            sb.append("Meta+"); // NOI18N
        }

        int i = sk.indexOf('-'); //NOI18N
        if (i != -1) {
            sk = sk.substring(i + 1);
        }
        sb.append(sk);

        return sb.toString();
    }

    /**
     * PopupMenu presenter for this component.
     * As getToolbarPresenter(), it must always return the same component.
     * @return
     */
    @Override
    public JMenuItem getPopupPresenter() {
        if (presenter == null) {
            presenter = new JMenuItem(Actions.cutAmpersand((String) getValue(NAME)));
            presenter.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ScmlToolBarAction.this.actionPerformed(e);
                }
            });
            Object icon = getIcon();
            if (icon != null) {
                presenter.setIcon((Icon) icon);
            }
            Object title = getTitle();
            if (presenter != null) {
                presenter.setText((String) title);
            }

            Object tooltip = getTooltip();
            if (tooltip != null) {
                presenter.setToolTipText((String) tooltip);
            }
        }

        return presenter;
    }

    public String getTitle() {
        return NbBundle.getMessage(getClassForBundle(), createTitleKey(this));
    }

    public String getTooltip() {
        return NbBundle.getMessage(getClassForBundle(), createTooltipKey(this));
    }

    public abstract void actionPerformed(ActionEvent e, ScmlEditorPane pane);

    public abstract Icon getIcon();

    /**
     * Show this action in toolbar or not.
     */
    public abstract boolean isAvailable(Scml scml);

    /**
     * Must check whether this action is available in current situation,
     * and set "enabled" property to correct value.
     */
    public abstract void updateState();

    /**
     * Group of this action.<br>
     * Used for sorting.
     * All actions first are sorted by groups, and then by positions.<br>
     * All groups a divided by separator.
     * @return
     */
    public abstract int getGroupType();

    /**
     * Position of this action.<br>
     * Used for sorting.
     * All actions first are sorted by groups, and then by positions.<br>
     * All groups a divided by separator.
     * @return
     */
    public abstract int getPosition();

    /**
     * String representation of action's HotKey.
     * @return
     */
    public abstract String getAcceleratorKey();

    /**
     * Bundle for search locilized strings (tooltip, title, etc.).
     * @return
     */
    protected abstract Class getClassForBundle();

    public static String createTooltipKey(Action a) {
        return a.getValue(BaseAction.NAME) + "-tooltip";
    }

    public static String createTitleKey(Action a) {
        return a.getValue(BaseAction.NAME) + "-title";
    }

    public static Action create(FileObject fo) {
        String actionClassName = (String) fo.getAttribute("actionClass");
        if (actionClassName == null) {
            throw new NullPointerException("Action class is not defined");
        }
        try {
            Class actionClass = Thread.currentThread().getContextClassLoader().loadClass(actionClassName);
            return new ContextAwareFactory(actionClass);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class ContextAwareFactory extends AbstractAction implements ContextAwareAction, Presenter.Toolbar {

        private final Class actionClass;

        public ContextAwareFactory(Class actionClass) {
            this.actionClass = actionClass;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Action createContextAwareInstance(Lookup actionContext) {
            ScmlEditor editor = actionContext.lookup(ScmlEditor.class);
            if (editor == null) {
                throw new NullPointerException("No ScmlEditor in context");
            }
            Class typeParameter = editor.getClass();
            while (typeParameter != null) {
                try {
                    Constructor constructor = actionClass.getConstructor(typeParameter);
                    try {
                        Action action = (Action) constructor.newInstance(editor);
                        if (action instanceof ScmlToolBarAction) {
                            if (((ScmlToolBarAction) action).isAvailable(editor.getSource())) {
                                return action;
                            } else {
                                return null;
                            }
                        }
                    } catch (InstantiationException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    } catch (IllegalArgumentException ex) {
                        throw new RuntimeException(ex);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (NoSuchMethodException ex) {
                    typeParameter = typeParameter.getSuperclass();
                } catch (SecurityException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(new NoSuchMethodException("No appropriate constructor found"));
        }

        @Override
        public Component getToolbarPresenter() {
            JPanel panel = new JPanel();
            Dimension zeroSize = new Dimension(0, 0);
            panel.setSize(zeroSize);
            panel.setPreferredSize(zeroSize);
            panel.setMinimumSize(zeroSize);
            panel.setMaximumSize(zeroSize);
            return panel;
        }
    }
}
