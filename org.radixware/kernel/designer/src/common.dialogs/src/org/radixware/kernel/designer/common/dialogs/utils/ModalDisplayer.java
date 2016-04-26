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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * Utility class, allows to display modal any component.
 * Designed for overriding.
 * Remembers previous size for each displayed component.
 */
public class ModalDisplayer {

    private final JComponent component;
    private final DialogDescriptor dialogDescriptor;
    private final Dialog dialog;
    private RadixIcon radixIcon = null;
    //private final JButton OK_BUTTON = new JButton("OK");
    //return new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION};
    private final Object[] buttonOptions;
    private final ActionListener superListener;
    private final ActionListener okListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == DialogDescriptor.OK_OPTION) {
                if (canClose()) {
                    close(true);
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            showClosingProblems();
                        }
                    });
                }
            } else {
                if (superListener != null) {
                    superListener.actionPerformed(e);
                }
            }
        }        
    };
    
    public ModalDisplayer(JComponent component, String title) {
        this(component, title, new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION});
    }

    /**
     * @param component - component for modal displaying.
     * @param title - title for modal dialog.
     * @param buttonOptions - button options. Example - Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION}
     */
    public ModalDisplayer(JComponent component, String title, Object[] buttonOptions) {
        this.component = component;
        this.buttonOptions = buttonOptions;

        this.dialogDescriptor = new DialogDescriptor(component, title) {

            @Override
            public void setValue(Object newValue) {
                super.setValue(newValue);
            }
        };
        dialogDescriptor.setOptions(buttonOptions);
        
        this.dialogDescriptor.setHelpCtx(null);
        this.dialogDescriptor.setModal(true);
        this.dialog = DialogDisplayer.getDefault().createDialog(dialogDescriptor);

        if (component instanceof TopComponent) {
            dialog.setIconImage(((TopComponent) component).getIcon());
        }
        superListener = this.dialogDescriptor.getButtonListener();
        this.dialogDescriptor.setButtonListener(okListener);
        //OK_BUTTON.addActionListener(okListener);
    }
    private PropertyChangeListener componentBoundsListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            initializeBounds();
        }
    };

    private void initializeBounds() {
        final int dx = dialog.getSize().width - component.getSize().width + 20;
        final int dy = dialog.getSize().height - component.getSize().height + 20;

        final Dimension componentMinimumSize = component.getMinimumSize();
        if (componentMinimumSize != null) {
            dialog.setMinimumSize(new Dimension(componentMinimumSize.width + dx, componentMinimumSize.height + dy));
        } else {
            dialog.setMinimumSize(new Dimension(dx, dy));
        }

        final Dimension componentMaximumSize = null;//component.getMaximumSize();
        if (componentMaximumSize != null) {
            dialog.setMaximumSize(new Dimension(componentMaximumSize.width + dx, componentMaximumSize.height + dy));
        }

        final Dimension componentPreferredSize = component.getPreferredSize();
        if (componentPreferredSize != null) {
            dialog.setPreferredSize(new Dimension(componentPreferredSize.width + dx, componentPreferredSize.height + dy));
        }

//        final boolean fixedSize = componentMaximumSize != null && componentMaximumSize.equals(componentMinimumSize);
//        dialog.setResizable(!fixedSize);
    }

    private static String calcTitle(JComponent component) {
        if (component instanceof TopComponent) {
            TopComponent topComponent = (TopComponent) component;
            return topComponent.getDisplayName();
        } else {
            return "";
        }
    }

    /**
     * @param component - component for modal displaying, title will be calculated automatically (for example, as title of TopComponent).
     */
    public ModalDisplayer(JComponent component) {
        this(component, calcTitle(component));
    }

    /**
     * Get modal dialog title.
     * Final - use setTitle().
     */
    public final String getTitle() {
        return dialogDescriptor.getTitle();
    }

    /**
     * Set modal dialog title.
     */
    public void setTitle(String title) {
        dialogDescriptor.setTitle(title);
    }

    /**
     * Get modal dialog Radix icon.
     * Final - use setIcon().
     */
    public final RadixIcon getIcon() {
        return radixIcon;
    }

    /**
     * Set modal dialog Radix icon.
     */
    public void setIcon(RadixIcon radixIcon) {
        this.radixIcon = radixIcon;
        Image image = (radixIcon != null ? radixIcon.getImage() : null);
        dialog.setIconImage(image);
    }

    /**
     * Get modal dialog.
     */
    public Dialog getDialog() {
        return dialog;
    }

    /**
     * Get modal dialog descriptor.
     */
    public DialogDescriptor getDialogDescriptor() {
        return dialogDescriptor;
    }

    @Deprecated
    public void setMinimumSize(Dimension size) {
    }

    @Deprecated
    public void setMaximumSize(Dimension size) {
    }

    @Deprecated
    public void setPrefferedSize(Dimension size) {
    }

    /**
     * Get modal dialog component.
     */
    public JComponent getComponent() {
        return component;
    }
    private static final String DIMENSIONS_KEY = "Dimensions";
    private static final String WIDTH_KEY = "Width";
    private static final String HEIGHT_KEY = "Height";

    private static String getComponentDimensionPreferencesKey(Class<? extends Component> componentClass) {
        final String result = componentClass.getSimpleName();
        if(result == null || result.trim().isEmpty()) {
            return componentClass.getName();
        }
        return result;
    }

    private static Dimension restoreDimension(Class<? extends Component> componentClass) {
        try {
            final Preferences dimensions = Utils.findPreferences(DIMENSIONS_KEY);
            if (dimensions != null) {
                String componentDimensionPreferencesKey = getComponentDimensionPreferencesKey(componentClass);
                if (dimensions.nodeExists(componentDimensionPreferencesKey)) {
                    Preferences componentDimensionPreferences = dimensions.node(componentDimensionPreferencesKey);
                    int width = componentDimensionPreferences.getInt(WIDTH_KEY, 0);
                    int height = componentDimensionPreferences.getInt(HEIGHT_KEY, 0);
                    if (width > 0 && height > 0 && width < 10000 && height < 10000) {
                        return new Dimension(width, height);
                    }
                }
            }
            return null;
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
            return null;
        }
    }

    private static void storeDimension(Class<? extends Component> componentClass, Dimension dimension) {
        final Preferences dimensions = Utils.findOrCreatePreferences(DIMENSIONS_KEY);
        final Preferences componentDimensionPreferences = dimensions.node(getComponentDimensionPreferencesKey(componentClass));
        componentDimensionPreferences.putInt(WIDTH_KEY, dimension.width);
        componentDimensionPreferences.putInt(HEIGHT_KEY, dimension.height);
        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException ex) {
            DialogUtils.messageError(ex);
        }
    }

    /**
     * Called when OK button pressed.
     */
    protected void apply() {
    }

    public Object[] getOptions() {
        return buttonOptions;//new Object[]{DialogDescriptor.OK_OPTION, DialogDescriptor.CANCEL_OPTION};
    }

    public void onClosing() {
    }

    /**
     * Show dialog modal.
     * @return true if OK button pressed.
     */
    public boolean showModal() {
        synchronized (this) {
            if (wasClosed) {
                return wasClosedWithOk;
            }
        }
        initializeBounds();

        Dimension size = restoreDimension(component.getClass());
        if (size == null || !dialog.isResizable()) {
            size = dialog.getPreferredSize();
        }
        if (size == null) {
            size = new Dimension(200, 100);
        }

        // Center the dialog after the size changed.
        final Rectangle screenBounds = Utilities.getUsableScreenBounds();
        final int maxW = (screenBounds.width * 9) / 10;
        final int maxH = (screenBounds.height * 9) / 10;

        size.width = Math.min(size.width, maxW);
        size.height = Math.min(size.height, maxH);
        dialog.setBounds(Utilities.findCenterBounds(size));
        dialog.setResizable(true);
        
        final Object[] options = getOptions();        
        if (!Arrays.equals(dialogDescriptor.getOptions(),options))
             dialogDescriptor.setOptions(options);
        
        dialogDescriptor.setClosingOptions(new Object[]{DialogDescriptor.CANCEL_OPTION});
        
        component.addPropertyChangeListener("minimumSize", componentBoundsListener);
        component.addPropertyChangeListener("maximumSize", componentBoundsListener);

        try {
            beforeShow();
            component.requestFocus();
            dialog.setVisible(true);
        } finally {
            component.removePropertyChangeListener("minimumSize", componentBoundsListener);
            component.removePropertyChangeListener("maximumSize", componentBoundsListener);
        }

        size = dialog.getSize();
        storeDimension(component.getClass(), size);

        boolean result = dialogDescriptor.getValue() == DialogDescriptor.OK_OPTION;
        if (result) {
            apply();
        }

        onClosing();

        dialog.dispose();
        return result;
    }

    protected void beforeShow() {}
    
    
    
    protected boolean canClose() {
        return true;
    }
    private boolean wasClosed = false;
    private boolean wasClosedWithOk = false;
    
    /**
     * Close dialog with specified modal result (OK button pressed).
     */
    public void close(boolean modalResult) {

        if (!canClose()) {
            showClosingProblems();
            return;
        }
        synchronized (this) {
            wasClosed = true;
            wasClosedWithOk = modalResult;
        }

        if (modalResult) {
            dialogDescriptor.setValue(DialogDescriptor.OK_OPTION);
        } else {
            dialogDescriptor.setValue(DialogDescriptor.CANCEL_OPTION);
        }
        dialog.setVisible(false);
    }

    protected void showClosingProblems() {
    }

    public static final void showModal(JDialog dialog) {
        dialog.setModal(true);
        Dimension size = restoreDimension(dialog.getClass());
        if (size == null) {
            size = dialog.getPreferredSize();
        }
        if (size == null) {
            size = new Dimension(200, 100);
        }

        // Center the dialog after the size changed.
        final Rectangle screenBounds = Utilities.getUsableScreenBounds();
        final int maxW = (screenBounds.width * 9) / 10;
        final int maxH = (screenBounds.height * 9) / 10;

        size.width = Math.min(size.width, maxW);
        size.height = Math.min(size.height, maxH);
        dialog.setBounds(Utilities.findCenterBounds(size));

        dialog.requestFocus();
        // dialog.pack();
        dialog.setVisible(true);

        size = dialog.getSize();
        storeDimension(dialog.getClass(), size);
        dialog.dispose();
    }
}
