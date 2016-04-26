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

package org.radixware.kernel.designer.environment.project;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.cookies.EditCookie;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;
import org.radixware.kernel.designer.common.general.nodes.hide.Restorable;
import org.radixware.kernel.designer.common.tree.RadixObjectFilterNode;
import org.radixware.kernel.designer.environment.nodes.BranchNode;

class RadixLogicalViewProvider implements LogicalViewProvider {

    private final RadixProject project;
    private BranchNode branchNode;//used for search
    private Node filterNode;//displayed in tree

    public RadixLogicalViewProvider(RadixProject project) {
        super();
        this.project = project;
    }

    @Override
    public Node createLogicalView() {
        final Branch branch = project.getBranch();
        branchNode = (BranchNode) NodesManager.findOrCreateNode(branch);
        branchNode.registerProject(project);
        filterNode = new RadixObjectFilterNode(branchNode, new EditBranchAction());
        return filterNode;
//        return branchNode;
    }

    //workaround for RADIX-4080: take (ONLY) one arbitrary node from context and perform edit action on it.
    //it prevents Branch editor from double execution
    private static final class EditBranchAction extends AbstractAction implements ContextAwareAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Action createContextAwareInstance(final Lookup actionContext) {
            return new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Node node = actionContext.lookup(Node.class);
                    if (node != null) {
                        EditCookie ec = node.getLookup().lookup(EditCookie.class);
                        if (ec != null) {
                            ec.edit();
                        } else {
                            Toolkit.getDefaultToolkit().beep();
                        }
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            };
        }
    }

    private Node findPath(final Node parent, final RadixObject target) {
        final Class<? extends RadixObject> targetClass = target.getClass();

        // lokup all for AdsEditorPageNode, it contains link and page.
        final RadixObject nodeObject = parent.getLookup().lookup(targetClass);
        if (nodeObject == target) {
            return parent;
        }

        // impossible to place in findPath(root, target), because root is not branch node, but ProjectsNode
        final Branch branch = parent.getLookup().lookup(Branch.class);
        if (branch != null && !branch.isParentOf(target)) {
            return null;
        }

        final Node[] childNodes = parent.getChildren().getNodes(true);

        for (RadixObject curObject = target; curObject != null; curObject = curObject.getContainer()) {
            for (Node childNode : childNodes) {

                final RadixObject childObject = childNode.getLookup().lookup(curObject.getClass());
                if (childObject == curObject) {
                    return findPath(childNode, target);
                }
            }
        }

        // for method in group (method displayed in group but group is separate object and not container for method).
        for (Node childNode : childNodes) {
            final Node result = findPath(childNode, target);
            if (result != childNode) {
                return result;
            }
        }

        return parent;
    }

    @Override
    public Node findPath(Node root, Object target) {
        RadixObject radixObject = null;

        if (target instanceof FileObject) {
            final FileObject fileObject = (FileObject) target;
            radixObject = RadixFileUtil.getLastSelectedRadixObject(fileObject);
            if (radixObject == null) {
                radixObject = RadixFileUtil.findRadixObject(fileObject);
            }
        } else if (target instanceof RadixObject) {
            radixObject = (RadixObject) target;
        }

        if (radixObject == null) {
            return null;
        }

        for (RadixObject ro = radixObject; ro != null; ro = ro.getContainer()) {
            if (ro instanceof AdsLocalizingBundleDef) {
                final AdsLocalizingBundleDef bundle = (AdsLocalizingBundleDef) ro;
                radixObject = bundle.findBundleOwner();
            }
        }

        restoreIfHidden(radixObject);

        return findPath(root, radixObject);
    }

    private void restoreIfHidden(RadixObject target) {
        Node node = findPath(branchNode, target);
        Restorable restorable;
        while (node != null) {
            restorable = node.getLookup().lookup(Restorable.class);
            if (restorable != null) {
                restorable.restore();
            }
            node = node.getParentNode();
        }
    }
}
