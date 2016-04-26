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
import javax.swing.Action;
import org.openide.util.Lookup;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.builder.BuildActionExecutor.EBuildActionType;
import org.radixware.kernel.common.builder.DirectoryFileChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.designer.common.dialogs.build.DesignerBuildEnvironment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;


public abstract class DirectoryIndexUpdateAction extends AbstractContextAwareAction {

    protected abstract EBuildActionType getBuildActionType();
    protected final RadixObject context;

    public DirectoryIndexUpdateAction(RadixObject context) {
        this.context = context;
    }

    public DirectoryIndexUpdateAction() {
        this.context = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                if (RadixMutex.getLongProcessLock().tryLock()) {
                    try {
                        DirectoryFileChecker checker = DirectoryFileChecker.Factory.newInstance(context, new DesignerBuildEnvironment(false, getBuildActionType()));
                        if (getBuildActionType() == EBuildActionType.CHECK_DIST) {
                            checker.check();
                        } else {
                            checker.update();
                        }
                    } catch (IllegalUsageError ex) {
                        DialogUtils.messageError("Action can not be performed in current context: " + context.getQualifiedName());
                    } finally {
                        RadixMutex.getLongProcessLock().unlock();
                    }
                } else {
                    DialogUtils.messageError("Another long action is still in progress");
                }
            }
        });

    }

    public static final class Update extends DirectoryIndexUpdateAction {

        public Update(RadixObject context) {
            super(context);
        }

        public Update() {
        }

        @Override
        protected EBuildActionType getBuildActionType() {
            return EBuildActionType.UPDATE_DIST;
        }

        @Override
        public Action createContextAwareInstance(Lookup actionContext) {
            return new Update(actionContext.lookup(RadixObject.class));
        }
    }

    public static final class Check extends DirectoryIndexUpdateAction {

        public Check(RadixObject context) {
            super(context);
        }

        public Check() {
        }

        @Override
        protected EBuildActionType getBuildActionType() {
            return EBuildActionType.CHECK_DIST;
        }

        @Override
        public Action createContextAwareInstance(Lookup actionContext) {
            return new Check(actionContext.lookup(RadixObject.class));
        }
    }
}
