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

package org.radixware.kernel.designer.common.tree.actions;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.concurrent.CountDownLatch;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.netbeans.modules.subversion.util.Context;
import org.openide.nodes.Node;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.upload.PostUploadAction;
import org.radixware.kernel.designer.subversion.ui.commit.CommitAction;
import org.radixware.kernel.designer.subversion.ui.commit.CommitAction.CommitInputAwtParameters;


public class RadixSvnCommitAction extends RadixSvnAction {

    @Override
    protected void performAction(final Node[] activatedNodes) {
        final Context context = getCommitContext(activatedNodes);
        final String title = getContextDisplayName(activatedNodes);





        final JDialog dialog = new JDialog(WindowManager.getDefault().getMainWindow(), "Wait", ModalityType.APPLICATION_MODAL);

        final CommitInputAwtParameters awtParameters = new CommitAction.CommitInputAwtParameters();

        final CountDownLatch latch = new CountDownLatch(1);

        final Runnable wait = new Runnable() {
            @Override
            public void run() {
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(progressBar, BorderLayout.CENTER);
                JLabel label = new JLabel(" Please wait...", SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(200, 30));
                //panel.setBorder( BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                panel.add(label, BorderLayout.PAGE_START);
                JPanel panel2 = new JPanel();
                panel2.setLayout(new FlowLayout());
                panel2.add(panel);
                panel.setMaximumSize(new Dimension(panel.getPreferredSize()));
                panel2.setPreferredSize(new Dimension(panel.getPreferredSize().width+20, panel.getPreferredSize().height + 20) );
                
                dialog.add(panel2);
                dialog.pack();
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                latch.countDown();
                dialog.setVisible(true);
                
            }
        };


        SwingUtilities.invokeLater(wait);
        PostUploadAction.getInstance().put(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                    for (int i=0; i<10 || !dialog.isVisible(); i++){
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ex) {
                    DialogUtils.messageError(ex);
                } 
                dialog.setVisible(false);
                CommitAction.commit(awtParameters, title, context, false, true);
                dialog.setVisible(false);//MacOs bug fix?
            }
        });
    }

    @Override
    public String getName() {
        return "Commit...";
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.SUBVERSION.COMMIT.getResourceUri();
    }
}
