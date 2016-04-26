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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Cancellable;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.designer.ads.common.lookup.AdsClassLookupSupport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public class FixupStdClassesAction extends CallableSystemAction {

    @Override
    public void performAction() {
        final RadixObject radixObject = WindowManager.getDefault().getRegistry().getActivated().getLookup().lookup(RadixObject.class);
        if (radixObject == null) {
            DialogUtils.messageError("There is no object selected.");
            return;
        }

        final Canceller canceller = new Canceller();
        ProgressHandle handle = ProgressHandleFactory.createHandle("Standard class fixup", canceller);
        final ArrayList<AdsClassDef> classes = new ArrayList<AdsClassDef>();
        try {
            handle.start();
            handle.progress("Prepare...");

            radixObject.visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    classes.add((AdsClassDef) radixObject);
                }
            }, new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    if (radixObject instanceof AdsClassDef) {
                        switch (((AdsClassDef) radixObject).getClassDefType()) {
                            case ALGORITHM:
                            case ENTITY:
                            case ENTITY_GROUP:
                            case SQL_CURSOR:
                                return true;
                        }
                    }
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return canceller.isCancelled;
                }
            });

            if (classes.isEmpty()) {
                DialogUtils.messageInformation("No standard classes (Cursor,Entity,Entity Group,Algorithm) found");
                return;
            }
            if (canceller.isCancelled) {
                return;
            }

            if (!DialogUtils.messageConfirmation(classes.size() + " classes found. Continue?")) {
                return;
            }
            handle.switchToDeterminate(classes.size());
            int index = 0;
            for (AdsClassDef c : classes) {
                if (canceller.isCancelled) {
                    break;
                }
                final AdsClassDef c1 = c;

                RadixMutex.writeAccess(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            switch (c1.getClassDefType()) {
                                case ALGORITHM:
                                    AdsClassLookupSupport.setupAlgoClass((AdsAlgoClassDef) c1);
                                    break;
                                case ENTITY:
                                    AdsClassLookupSupport.setupEntityClass((AdsEntityClassDef) c1);
                                    break;
                                case ENTITY_GROUP:
                                    AdsClassLookupSupport.setupEntityGroupClass((AdsEntityGroupClassDef) c1);
                                    break;
                                case SQL_CURSOR:
                                    AdsClassLookupSupport.setupCursorClass((AdsCursorClassDef) c1);
                                    break;
                            }

                        } catch (Throwable ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                });
                handle.progress("Fixup...", index++);
            }
            if (DialogUtils.messageConfirmation(classes.size() + " classes updated. Save changes?")) {
                handle.switchToDeterminate(classes.size());
                for (AdsClassDef clazz : classes) {
                    if (canceller.isCancelled) {
                        break;
                    }
                    try {
                        clazz.save();
                    } catch (IOException ex) {
                        DialogUtils.messageError(new RadixError("Unable to save class " + clazz.getQualifiedName(), ex));
                    }
                    handle.progress("Save...", index++);
                }
            }
        } finally {
            handle.finish();
        }
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(FixupStdClassesAction.class, "CTL_FixupStdClassesAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    private class Canceller implements Cancellable {

        private boolean isCancelled = false;

        @Override
        public boolean cancel() {
            isCancelled = true;
            return true;
        }
    }
}
