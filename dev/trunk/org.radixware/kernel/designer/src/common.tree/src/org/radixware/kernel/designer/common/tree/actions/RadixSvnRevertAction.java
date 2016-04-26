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
import java.util.List;
import org.netbeans.modules.subversion.ui.update.RevertModificationsAction;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.openide.nodes.Node;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.tree.actions.RefreshAction;


public class RadixSvnRevertAction extends RadixSvnAction {

    @Override
    protected boolean enable(Node[] activatedNodes) {
        if (!super.enable(activatedNodes)) {
            return false;
        }

        // disable for branch and layer to prevent revert of kernel.
        for (Node node : activatedNodes) {
            final Branch branch = node.getLookup().lookup(Branch.class);
            final Layer layer = node.getLookup().lookup(Layer.class);
            if (branch != null || layer != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        final RevertModificationsAction revertAction = SystemAction.get(RevertModificationsAction.class);
        //javax.swing.Action a = revertAction.createContextAwareInstance(Lookup.getDefault());

        List<String> files = new ArrayList<String>(activatedNodes.length);
        for (Node node : activatedNodes) {
            node.getLookup();
            final Definition definition = node.getLookup().lookup(Definition.class);
            if (definition != null) {
                if(definition instanceof AdsLocalizingBundleDef){
                    final AdsLocalizingBundleDef bundle =(AdsLocalizingBundleDef)definition;
                    final List<File> bundleFiles = bundle.getFiles();
                    if (bundleFiles != null) {
                        for(File f:bundleFiles){
                            files.add(f.getAbsolutePath());
                        }
                    }
                }else{
                    File file = definition.getFile();
                    if (file != null) {
                        files.add(file.getAbsolutePath());
                    }
                }
            }
        }
        RefreshAction propertyChangeListenerEx = new RefreshAction(files);
        //a.addPropertyChangeListener(propertyChangeListenerEx);
        revertAction.addPropertyChangeListener(propertyChangeListenerEx);
        revertAction.performAction();

    }

    @Override
    public String getName() {
        return "Revert Modifications...";
    }

    @Override
    protected String iconResource() {
        return RadixWareIcons.SUBVERSION.REVERT.getResourceUri();
    }
}
