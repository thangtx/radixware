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

import java.util.ArrayList;
import java.util.List;
import org.netbeans.modules.subversion.util.Context;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.operation.SvnUpdateOperation;
import org.radixware.kernel.designer.subversion.util.SvnBridge;

public class RadixSvnUpdateAction extends RadixSvnAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        final Context context = getUpdateContext(activatedNodes);
//        final String title = getContextDisplayName(activatedNodes);
//        org.netbeans.modules.subversion.ui.update.UpdateAction.performUpdate(context, title);  

        List<RadixObject> radixObjects = new ArrayList<RadixObject>(activatedNodes.length);

        for (Node node : activatedNodes) {
            RadixObject radixObject = node.getLookup().lookup(RadixObject.class);
            if (radixObject != null) {
                radixObjects.add(radixObject);
            }
        }
        if (radixObjects.isEmpty()) {
            return;
        }

//        SvnAuthType authType = SVNPreferences.getAuthType();
//        String sshKeyFile = SVNPreferences.getSSHKeyFilePath();
//        String userName = SVNPreferences.getUserName();
//
//        //final ReleaseSettings settings = new ReleaseSettings(list.get(0).getBranch(),
//        //        //FlowLoggerFactory.newReleaseLogger()
//        //        null, new DesignerBuildEnvironment(EBuildActionType.RELEASE), false);            
//        //ESvnAuthType authType = authType = settings.getAuthType();
//        //String sshKeyFile = sshKeyFile = settings.getSSHKeyFile();
//        //String userName = settings.getUserName();
////        final SVNRepositoryAdapter repository;
////        try {
////            try {
////                repository = SVNRepositoryAdapter.Factory.newInstance(radixObjects.get(0).getBranch().getFile().getParentFile(), userName, authType, sshKeyFile);
////            } catch (RadixSvnException ex) {
////                DialogUtils.messageError(ex);
////                return;
////            }
////
////        } catch (AuthenticationCancelledException ex) {
////            return;
////        }
        SvnUpdateOperation svnUpdateOperation = new SvnUpdateOperation(radixObjects, SvnBridge.getClientAdapter(radixObjects.get(0).getBranch().getFile().getParentFile()), context.getFiles(), "SVN Update", true);
        svnUpdateOperation.start();

    }

    @Override
    public String getName() {
        return "Update";
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.SUBVERSION.UPDATE.getResourceUri();
    }
}
