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

import java.awt.CardLayout;
import java.awt.Component;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.UserReport;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.kernel.reporteditor.common.RequestExecutor;
import org.radixware.schemas.reports.DisableReportPubsRq;
import org.radixware.schemas.reports.DisableReportPubsRqDocument;
import org.radixware.schemas.reports.DisableReportPubsRs;
import org.radixware.schemas.reports.DisableReportPubsRsDocument;

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
// @ActionID(category="...", id="org.radixware.kernel.reporteditor.tree.dialogs.UnistallReportWizardWizardAction")
// @ActionRegistration(displayName="Open UnistallReportWizard Wizard")
// @ActionReference(path="Menu/Tools", position=...)
public final class UnistallReportWizardWizardAction {

    private static final Id DISABLE_COMMAND_ID = Id.Factory.loadFrom("clcLEYNKJOI3FGL5DLPODQPLOCHTE");

    public void execute(UserReport report) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(new UnistallReportWizardWizardPanel1());
        panels.add(new UnistallReportWizardWizardPanel2());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()        
        wiz.putProperty("reportId", report.getId());

        wiz.setTitleFormat(new MessageFormat("{0}"));

        wiz.setTitle("Report Publications - " + report.getName());

        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
            execDisableAction(report.getId(), (String) wiz.getProperty("action"));
        }
    }

    public static List<String> execDisableAction(final Id reportId, final String command) {
        final List<String> result = new LinkedList<>();
        UserExtensionManager.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerActionWithWaitImpl() {

            @Override
            public void execute(IClientEnvironment env) {
                DisableReportPubsRqDocument rqDoc = DisableReportPubsRqDocument.Factory.newInstance();
                DisableReportPubsRq rq = rqDoc.addNewDisableReportPubsRq();
                rq.setAction(command);
                rq.setReportId(reportId);
                try {
                    DisableReportPubsRsDocument rsDoc = (DisableReportPubsRsDocument) env.getEasSession().executeContextlessCommand(DISABLE_COMMAND_ID, rqDoc, DisableReportPubsRsDocument.class);
                    if (rsDoc != null) {
                        DisableReportPubsRs rs = rsDoc.getDisableReportPubsRs();
                        if (rs != null) {
                            for (DisableReportPubsRs.PubInfo info : rs.getPubInfoList()) {
                                String message = "<html><b>" + info.getPubName() + "</b><br>";
                                if (info.getLocation() != null) {
                                    String locationStr = info.getLocation();
                                    locationStr = locationStr.replace("\n", "<br>");
                                    message += "<i>" + locationStr + "</i>";
                                }
                                message += "</html>";
                                result.add(message);
                            }
                        }
                    }
                } catch (ServiceClientException | InterruptedException ex) {
                    env.processException(ex);
                }
            }
        });
        return result;
    }
}
