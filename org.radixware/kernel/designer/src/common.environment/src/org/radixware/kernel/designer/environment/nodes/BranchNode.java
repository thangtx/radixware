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
package org.radixware.kernel.designer.environment.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.swing.Action;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.api.project.Project;
import org.openide.util.actions.SystemAction;
import org.radixeare.kernel.designer.ads.build.release.actions.MakeReleaseAction;
import org.radixware.kernel.common.builder.repository.BranchFormatUtils;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.enums.ERepositoryBranchType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.ads.build.actions.*;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.BuildCookie;

import org.radixware.kernel.designer.common.annotations.registrators.NodeFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.repository.layer.creation.LayerCreature;
import org.radixware.kernel.designer.common.general.creation.CreationSupport;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;
import org.radixware.kernel.designer.common.general.nodes.INodeFactory;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.common.tree.RadixObjectsNodeSortedChildren;
import org.radixware.kernel.designer.common.tree.actions.RadixdocAction;
import org.radixware.kernel.designer.subversion.util.SvnBridge;

public class BranchNode extends RadixObjectNode {

    private static class BranchNodeChildren extends RadixObjectsNodeSortedChildren<Layer> {

        private final Branch branch;

        public BranchNodeChildren(Branch branch) {
            super();
            this.branch = branch;
        }

        @Override
        protected RadixObjects<Layer> getRadixObjects() {
            return branch.getLayers();
        }

        @Override
        protected List<Layer> getOrderedList() {
            return branch.getLayers().getInOrder();
        }
    }

    public BranchNode(final Branch branch) {
        super(branch, new BranchNodeChildren(branch));
        addCookie(new BuildCookie(branch));

        addCookie(new MakeReleaseAction.MakeReleaseCookie(branch));
        addCookie(new RadixdocAction.RadixdocCookie(null, null, null, branch));
        //addCookie(new ConfigureNotificationParamtersAction.ConfigureNotificationParamtersCookie(branch));
    }

    public void registerProject(Project project) {
        getLookupContent().add(project);
    }

    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        actions.add(SystemAction.get(BuildAction.class));
        actions.add(SystemAction.get(CleanAndBuildAction.class));
        actions.add(SystemAction.get(CleanAction.class));
        actions.add(SystemAction.get(RadixdocAction.class));
        actions.add(null);
        if (((Branch) getRadixObject()).getType() != ERepositoryBranchType.PATCH) {
            actions.add(SystemAction.get(MakeReleaseAction.class));
        }
//        actions.add(SystemAction.get(CreatePatchAction.class));
//        actions.add(null);
//        actions.add(SystemAction.get(ConfigureNotificationParamtersAction.class));
    }

    @NodeFactoryRegistration
    public static final class Factory implements INodeFactory<Branch> {

        @Override
        public RadixObjectNode newInstance(final Branch branch) {
            //final ReentrantLock lock = new ReentrantLock();
            final CountDownLatch ul = new CountDownLatch(1);
            if (BranchFormatUtils.upgradeRequired(branch)) {
                Thread ut = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RadixMutex.writeAccess(new Runnable() {
                            @Override
                            public void run() {
                                ul.countDown();
                                ProgressUtils.showProgressDialogAndRun(new Runnable() {
                                    @Override
                                    public void run() {
                                        BranchFormatUtils.upgrade(branch, SvnBridge.getClientAdapter(branch.getDirectory()));
                                    }
                                }, "Please wait...");
                            }
                        });
                    }
                });
                ut.setName("Format Upgrader");
                ut.setDaemon(true);
                ut.start();
            } else {
                ul.countDown();
            }
            try {
                //try{
                //  lock.lock();
                ul.await();
            } catch (InterruptedException ex) {
            }
            return new BranchNode(branch);
            //}finally{
            //    lock.unlock();
            //}
        }
    }

    public static class LayerCreationSupport extends CreationSupport {

        @Override
        public ICreatureGroup[] createCreatureGroups(final RadixObject object) {
            if (object instanceof Branch) {
                return new ICreatureGroup[]{
                    new ICreatureGroup() {
                        @Override
                        public List<ICreature> getCreatures() {
                            ArrayList<ICreature> list = new ArrayList<ICreature>();
                            list.add(new LayerCreature((Branch) object));
                            return list;
                        }

                        @Override
                        public String getDisplayName() {
                            return "Repository";
                        }
                    }};
            } else {
                return null;
            }
        }
    }

    @Override
    protected CreationSupport createNewCreationSupport() {
        return new LayerCreationSupport();
    }
}
