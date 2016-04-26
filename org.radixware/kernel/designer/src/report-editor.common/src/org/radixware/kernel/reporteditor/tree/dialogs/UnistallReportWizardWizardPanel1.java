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

package org.radixware.kernel.reporteditor.tree.dialogs;

import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

public class UnistallReportWizardWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {

    public static final String ACTION_DISABLE = "disable";
    public static final String ACTION_CLEAR = "remove";
    public static final String ACTION_ENABLE = "enable";
    private String action;
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private UnistallReportWizardVisualPanel1 component;
    private final ChangeSupport changeSupport = new ChangeSupport(this);

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public UnistallReportWizardVisualPanel1 getComponent() {
        if (component == null) {
            component = new UnistallReportWizardVisualPanel1();
        }
        return component;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        return action != null;
    }

    @Override
    public void addChangeListener(final ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(final ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public void readSettings(final WizardDescriptor wiz) {
        action = (String) wiz.getProperties().get("action");
        if (action == null) {
            action = ACTION_DISABLE;
        }
        getComponent().setup(this);
        changeSupport.fireChange();
    }

    @Override
    public void storeSettings(final WizardDescriptor wiz) {
        getComponent().store(this);
        wiz.putProperty("action", action);
        changeSupport.fireChange();
    }
}
