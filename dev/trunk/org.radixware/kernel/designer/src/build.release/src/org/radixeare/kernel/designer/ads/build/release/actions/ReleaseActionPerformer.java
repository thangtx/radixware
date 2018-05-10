/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixeare.kernel.designer.ads.build.release.actions;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import org.openide.DialogDescriptor;
import org.openide.util.RequestProcessor;
import org.radixeare.kernel.designer.ads.build.release.SetupRelease;
import org.radixeare.kernel.designer.ads.build.release.scripts.ScriptsDialog;

import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.builder.release.ReleaseSettings;
import org.radixware.kernel.common.dialogs.IDialogStyler;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.build.FlowLoggerFactory;
import org.radixware.kernel.designer.common.dialogs.spellchecker.Spellchecker;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.subversion.util.SvnBridge;

/**
 *
 * @author akrylov
 */
public class ReleaseActionPerformer {

    public ReleaseActionPerformer() {
    }

    public static void perform(Branch branch) {

        try {
            final ReleaseSettings settings = new ReleaseSettings(branch, FlowLoggerFactory.newReleaseLogger(), new DesignerBuildEnvironment(false, BuildActionExecutor.EBuildActionType.RELEASE), false, SvnBridge.getClientAdapter(branch.getDirectory()));
            settings.setDialogStyler(new IDialogStyler() {

                @Override
                public void setupTextComponent(JTextComponent text, EIsoLanguage textLanguage) {
                    Spellchecker.register(text, textLanguage, null);
                }
            });
            settings.setScriptDialog(new ScriptsDialog ());
            final SetupRelease setupPanel = new SetupRelease(settings);

            final ModalDisplayer modalDisplayer = new ModalDisplayer(setupPanel);
            modalDisplayer.setTitle("Make Release");

            final DialogDescriptor descriptor = modalDisplayer.getDialogDescriptor();

            setupPanel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent arg0) {
                    descriptor.setValid(setupPanel.isReady());
                }
            });
            setupPanel.initialize(modalDisplayer);

            if (modalDisplayer.showModal()) {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        org.radixware.kernel.common.builder.release.ReleaseBuilder builder = new org.radixware.kernel.common.builder.release.ReleaseBuilder(settings);
                        try {
                            if (builder.process()) {
                                settings.getLogger().success();
                            } else {
                                settings.getLogger().failure();
                            }
                        } catch (Exception e) {
                            DialogUtils.messageError(e);
                            settings.getLogger().fatal(e);
                            settings.getLogger().failure();
                        }
                    }
                });
            }
        } catch (RuntimeException ex) {
            DialogUtils.messageError(ex);
        }
    }
}
