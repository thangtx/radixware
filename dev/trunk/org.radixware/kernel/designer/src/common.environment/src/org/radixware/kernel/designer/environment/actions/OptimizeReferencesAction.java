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
import java.util.ArrayList;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Cancellable;
import org.openide.util.RequestProcessor;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public final class OptimizeReferencesAction implements ActionListener {

    private class Collector implements IVisitor {

        final ArrayList<JmlTagId> collection = new ArrayList<JmlTagId>();

        @Override
        public void accept(RadixObject radixObject) {
            collection.add((JmlTagId) radixObject);
        }
    }

    private class Scanner extends VisitorProvider {

        @Override
        public boolean isTarget(RadixObject radixObject) {
            return radixObject instanceof JmlTagId;
        }
    }
    int updatedCount = 0;

    private void process(ArrayList<JmlTagId> ids, ProgressHandle handle) {
        handle.switchToDeterminate(ids.size());
        int index = 0;
        for (JmlTagId tag : ids) {
            if (isCanceled) {
                return;
            }
            if (tag.isReadOnly()) {
                continue;
            }
            Definition def = tag.resolve(tag.getOwnerJml().getOwnerDef());
            if (def != null) {
                AdsPath path = tag.getPath();
                AdsPath newPath = new AdsPath(def.getIdPath());
                if (!Utils.equals(path, newPath)) {
                    tag.setPath(newPath);
                    updatedCount++;
                }
            }
            index++;
            handle.progress("Updating references", index);
        }
    }
    private boolean isCanceled = false;

    private void scan(Layer layer) {
        updatedCount = 0;

        final Scanner scanner = new Scanner();
        ProgressHandle handle = ProgressHandleFactory.createHandle("Optimize references", new Cancellable() {

            @Override
            public boolean cancel() {
                scanner.setCancelled(true);
                isCanceled = true;
                return true;
            }
        });
        try {
            handle.start();
            handle.switchToIndeterminate();
            Collector collector = new Collector();
            layer.visit(collector, scanner);
            process(collector.collection, handle);
        } finally {
            handle.finish();
            StatusDisplayer.getDefault().setStatusText(updatedCount > 0 ? updatedCount + " references updated" : "No references updated");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        isCanceled = false;

        final Layer layer = WindowManager.getDefault().getRegistry().getActivated().getLookup().lookup(Layer.class);
        if (layer == null) {
            DialogUtils.messageError("There is not layer selected.");
            return;
        }

        if (!DialogUtils.messageConfirmation("Optimize references of all modules of layer '" + layer.getName() + "'?")) {
            return;
        }

        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                RadixMutex.writeAccess(new Runnable() {

                    @Override
                    public void run() {
                        scan(layer);
                    }
                });
            }
        });
    }
}
