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
import org.radixware.kernel.common.types.Id;

public class UnistallReportWizardWizardPanel2 implements WizardDescriptor.Panel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private UnistallReportWizardVisualPanel2 component;
    private boolean requestCompleted;
    private boolean hasData = false;
    private ChangeSupport changeSupport = new ChangeSupport(this);

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public UnistallReportWizardVisualPanel2 getComponent() {
        if (component == null) {
            component = new UnistallReportWizardVisualPanel2(this);
        }
        return component;
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
        // If it is always OK to press Next or Finish, then:
        return requestCompleted && hasData;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
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
        final String action = (String) wiz.getProperty("action");
        final Id reportId = (Id) wiz.getProperty("reportId");
        getComponent().startAction(reportId, action);
    }

    void requestCompleted(final boolean hasData) {
        requestCompleted = true;
        this.hasData = hasData;
        changeSupport.fireChange();
    }

    @Override
    public void storeSettings(final WizardDescriptor wiz) {
        // use wiz.putProperty to remember current panel state
    }
}
