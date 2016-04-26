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
import java.util.Collection;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Cancellable;
import org.openide.util.RequestProcessor;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public final class ActualizeDependenciesAction implements ActionListener {

    private void actualizeDependencies(final Collection<? extends Module> modules) {
        final VisitorProvider provider = VisitorProviderFactory.createDefaultVisitorProvider();

        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Dependencies actualization", new Cancellable() {

            @Override
            public boolean cancel() {
                provider.setCancelled(true);
                return true;
            }
        });

        progressHandle.start(modules.size());
        try {
            int progress = 0;
            for (final Module module : modules) {
                progressHandle.progress(module.getQualifiedName(), progress++);
                RadixMutex.writeAccess(new Runnable() {

                    @Override
                    public void run() {
                        module.getDependences().actualize();
                    }
                });
                if (provider.isCancelled()) {
                    break;
                }
            }
        } finally {
            progressHandle.finish();
        }

        StatusDisplayer.getDefault().setStatusText("Dependencies actualized.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Collection<? extends Module> modules = WindowManager.getDefault().getRegistry().getActivated().getLookup().lookupAll(Module.class);
        if (modules == null || modules.isEmpty()) {
            DialogUtils.messageError("There are no module selected.");
            return;
        }

        if (!DialogUtils.messageConfirmation("Actualize dependencies of selected modules ?")) {
            return;
        }

        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                actualizeDependencies(modules);
            }
        });
    }
}
