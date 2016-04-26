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

package org.radixware.kernel.common.design.msdleditor;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import org.radixware.kernel.common.msdl.MsdlField;


public abstract class AbstractEditItem extends JPanel {

    protected ArrayList<AbstractEditItem> children = new ArrayList();
    private MsdlField field;

    public void open(MsdlField field) {
        this.field = field;
    }

    public boolean isReadOnly() {
        return field.isReadOnly();
    }

    public void update() {
        //setReadOnly(isReadOnly());
    }

    public void setReadOnly(boolean isReadOnly) {
        //setReadOnly(this,isReadOnly);
    }

    final private void setReadOnly(java.awt.Component panel, boolean isReadOnly) {
        for (Component component : ((JComponent) panel).getComponents()) {
            if (component instanceof AbstractEditItem) {
                ((AbstractEditItem) component).setReadOnly(isReadOnly);
            } else {
                if (component instanceof JComboBox) {
                    ((JComboBox) component).setEnabled(!isReadOnly);
                } else {
                    if (component instanceof JCheckBox) {
                        ((JCheckBox) component).setEnabled(!isReadOnly);
                    } else {
                        if (component instanceof JFormattedTextField) {
                            ((JFormattedTextField) component).setEnabled(!isReadOnly);
                        } else {
                            if (component instanceof JTextField) {
                                ((JTextField) component).setEnabled(!isReadOnly);
                            } else {
                                if (component instanceof JSpinner) {
                                    ((JSpinner) component).setEnabled(!isReadOnly);
                                } else {
                                    if (component instanceof JSpinner) {
                                        ((JSpinner) component).setEnabled(!isReadOnly);
                                    } else {
                                        if (component instanceof JButton) {
                                            ((JButton) component).setEnabled(!isReadOnly);
                                        } else {
                                            if (component instanceof JPanel || component instanceof JTabbedPane) {
                                                setReadOnly(component, isReadOnly);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected MsdlField getMsdlField() {
        return field;
    }
    
    public void setControlEnabled(boolean state) {
        
    }
}
