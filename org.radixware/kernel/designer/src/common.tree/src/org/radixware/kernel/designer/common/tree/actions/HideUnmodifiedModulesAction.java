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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.Node;
import org.openide.util.Cancellable;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Utilities;
import org.openide.util.actions.CallableSystemAction;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.nodes.hide.Hidable;
import org.radixware.kernel.designer.common.general.nodes.hide.HideSettings;
import org.radixware.kernel.designer.subversion.util.SvnBridge;

public class HideUnmodifiedModulesAction extends CallableSystemAction  {
    
    protected static class Canceller implements Cancellable {
        public boolean isCancele = false;

        @Override
        public boolean cancel() {
            isCancele = true;
            return true;
        }
    };
    
    protected volatile ChangeSupport changeSupport = new ChangeSupport(this);
    
    public final void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public final void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    @Override
    public void performAction() {
        setEnabled(false);
        Canceller canceller = new Canceller();
        final ProgressHandle handle = ProgressHandleFactory.createHandle("Hide unmodified nodes...", canceller);
        try {
            Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
            handle.start();
            for (Branch b : branches) {
                if (canceller.isCancele) {
                    break;
                }
                List<RadixObject> list = new ArrayList<>();
                collectRadixObjects(b, list);
                hideNodes(handle, b, list, canceller);
                handle.switchToIndeterminate();
            }
            changeSupport.fireChange();
        } finally {
            setEnabled(true);
            handle.finish();
        }
    }
    
    protected void collectRadixObjects(RadixObject context, List<RadixObject> list) {
        collectRadixObjects(context, list, new IFilter() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof Module;
            }
        });
    }
    
    protected void collectRadixObjects(RadixObject context, List<RadixObject> list, IFilter filter) {
        HideSettings.collectHidableRadixObjects(context, list, filter);
    }
    
    protected boolean isModified(RadixObject ro, ISvnFSClient client) throws ISvnFSClient.SvnFsClientException {
        List<File> files = RadixFileUtil.getVersioningFiles(ro);
        for (File f : files) {
            final Reference r = new Reference();
            client.getStatus(f, true, false, false, true, new ISvnFSClient.ISvnStatusCallback() {

                @Override
                public void doStatus(String string, ISvnFSClient.ISvnStatus status) {
                    ISvnFSClient.SvnStatusKind contentsStatus = status.getTextStatus();

                    if (!contentsStatus.isNormal()) {
                        r.set(Boolean.TRUE);
                    }
                }
            });
            if (r.get() == Boolean.TRUE) {
                return true;
            }
        }
        return false;
    }
    
    protected void hideNodes(final ProgressHandle handle, Branch b, final List<RadixObject> list, Canceller canceller) {
        ISvnFSClient client = SvnBridge.getClientAdapter(b.getDirectory());
        handle.switchToDeterminate(list.size());
        int i = 0;
        try {
            for (RadixObject ro : list) {
                if (canceller.isCancele) {
                    break;
                }
                boolean isModified = isModified(ro, client);
                Node n = NodesManager.findNode(ro);
                if (canceller.isCancele) {
                    break;
                }
                if (n != null) {
                    Hidable hidable = n.getLookup().lookup(Hidable.class);
                    if (hidable != null) {
                        if (!isModified) {
                            hidable.hide(true);
                        }
                    }
                } else if (!isModified) {
                    HideSettings.hide(true, ro);
                }
                handle.progress(++i);

            }
        } catch (ISvnFSClient.SvnFsClientException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public String getName() {
        return "Hide Unmodified Modules";
    }

    @Override
    public HelpCtx getHelpCtx() {
         return HelpCtx.DEFAULT_HELP;
    }
    
    @Override
    protected boolean asynchronous() {
        return true;
    }
    
    
}
