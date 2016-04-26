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

package org.radixware.kernel.designer.tree.actions.dds;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.tigris.subversion.svnclientadapter.SVNClientException;


public class DdsSegmentFixAction extends CookieAction {

    public static class Cookie implements Node.Cookie, Runnable {

        private DdsSegment segment;

        public Cookie(DdsSegment segment) {
            this.segment = segment;
        }

        @Override
        public void run() {
            Children.MUTEX.writeAccess(new Runnable() {

                @Override
                public void run() {
                    if (!segment.isInBranch()) {
                        return; // was deleted
                    }

                    final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("DDS Structure Committing");
                    progressHandle.start();
                    try {
                        final DdsSegmentCommiter commiter = new DdsSegmentCommiter(segment);
                        commiter.commit(progressHandle);
                        NodesManager.updateOpenedEditors();
                    } catch (IOException cause) {
                        RadixObjectError error = new RadixObjectError("Unable to switch segment to structure fixed state.", segment, cause);
                        DialogUtils.messageError(error);
                    } catch (SVNClientException cause) {
                        RadixObjectError error = new RadixObjectError("Unable to switch segment to structure fixed state.", segment, cause);
                        DialogUtils.messageError(error);
                    } finally {
                        progressHandle.finish();
                    }
                }
            });

        }

        public void switchToStructureFixedState() {

            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        if (DialogUtils.messageConfirmation("Switch DDS segment '" + segment.getQualifiedName() + "' and lowers to structure fixed state?")) {
                            RadixMutex.writeAccess(Cookie.this);
                        }
                    }
                });
            } catch (InterruptedException | InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    @Override
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    @Override
    protected Class<?>[] cookieClasses() {
        return new Class[]{Cookie.class};
    }

    @Override
    protected void performAction(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            Cookie cookie = nodes[i].getCookie(DdsSegmentFixAction.Cookie.class);
            if (cookie != null) {
                cookie.switchToStructureFixedState();
            }
        }
    }

    @Override
    public String getName() {
        return "Commit Structure"; // TODO: translate
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return true; // to prevent deadlock because tree can be recreated
    }
}
