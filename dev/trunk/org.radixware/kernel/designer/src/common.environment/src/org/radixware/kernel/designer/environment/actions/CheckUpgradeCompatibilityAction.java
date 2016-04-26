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

package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.api.IBuildEnvironment;
import org.radixware.kernel.common.builder.usages.UsageComparator;
import org.radixware.kernel.common.exceptions.RadixPrivateException;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.environment.actions.dialogs.ConfigureUpgradeCompatibilityCheck;


public class CheckUpgradeCompatibilityAction extends AbstractContextAwareAction implements ActionListener {

    private final Branch context;

    protected CheckUpgradeCompatibilityAction(final Branch context) {
        this.context = context;
    }

    public CheckUpgradeCompatibilityAction() {
        this(null);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final ConfigureUpgradeCompatibilityCheck panel = new ConfigureUpgradeCompatibilityCheck();
        final ModalDisplayer displayer = new ModalDisplayer(panel, "Check Upgrade Compatibility");
        if (displayer.showModal()) {
            RequestProcessor.getDefault().post(new Runnable() {
                @Override
                public void run() {
                    RadixMutex.writeAccess(new Runnable() {
                        @Override
                        public void run() {
                            if (RadixMutex.getLongProcessLock().tryLock()) {
                                try {
                                    final IBuildEnvironment env = new DesignerBuildEnvironment(false, EBuildActionType.API_COMPATIBILTY_CHECK);

                                    UsageComparator comparator;
                                    try {
                                        comparator = new UsageComparator(context, panel.getFiles(), env);
                                        if (comparator.process()) {
                                            env.getFlowLogger().success();
                                        } else {
                                            env.getFlowLogger().failure();
                                        }
                                    } catch (IOException | RadixPrivateException ex) {
                                        DialogUtils.messageError(ex);
                                    }

                                } finally {
                                    RadixMutex.getLongProcessLock().unlock();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public Action createContextAwareInstance(final Lookup actionContext) {
        return new CheckUpgradeCompatibilityAction(actionContext.lookup(Branch.class));
    }
}
