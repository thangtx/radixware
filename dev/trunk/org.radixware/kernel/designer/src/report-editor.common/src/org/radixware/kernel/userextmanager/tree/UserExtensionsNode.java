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

package org.radixware.kernel.userextmanager.tree;

import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.msdleditor.tree.MsdlSchemesNode;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;
import org.radixware.kernel.reporteditor.tree.AddNewReportModuleAction;
import org.radixware.kernel.reporteditor.tree.ReloadReportsAction;
import org.radixware.kernel.reporteditor.tree.ReportsNode;
import org.radixware.kernel.roleeditor.tree.UserRolesNode;


public class UserExtensionsNode extends AbstractNode {

    private static class ExtensionKey {
    }
    private static final ExtensionKey REPORTS = new ExtensionKey();
    private static final ExtensionKey ROLES = new ExtensionKey();
    private static final ExtensionKey MSDL_SCHEMES = new ExtensionKey();

    private static final class ExtensionsNodeChildren extends Children.Keys<ExtensionKey> {

        private final java.util.Map<ExtensionKey, Node> nodesCache = new WeakHashMap<>();
        private final Object createLock = new Object();
        private final Object updateLock = new Object();
        private final ChangeListener reportManagerListener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Thread updaterThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        updateKeys();
                    }
                });
                updaterThread.setName("Module list updater thread");
                updaterThread.setDaemon(true);
                updaterThread.start();
            }
        };

        public ExtensionsNodeChildren() {
            super();
            updateKeys();
            UserExtensionManager.getInstance().addChangeListener(reportManagerListener);
        }

        private void updateKeys() {

            synchronized (updateLock) {
            }

            setKeys(Arrays.asList(UserExtensionsNode.REPORTS, UserExtensionsNode.ROLES,UserExtensionsNode.MSDL_SCHEMES));

        }

        @Override
        protected Node[] createNodes(ExtensionKey key) {
            synchronized (createLock) {
                Node node = nodesCache.get(key);
                if (node == null) {
                    if (key == UserExtensionsNode.REPORTS) {
                        node = new ReportsNode();
                    } else if (key == UserExtensionsNode.ROLES) {
                        node = new UserRolesNode();
                    } else if (key == UserExtensionsNode.MSDL_SCHEMES) {
                        node = new MsdlSchemesNode();
                    }                    

                    if (node != null) {
                        nodesCache.put(key, node);
                    }
                }
                if (node != null) {
                    return new Node[]{node};
                } else {
                    return null;
                }
            }
        }
    }
    private InstanceContent lookupContent;

    public UserExtensionsNode() {
        this(new InstanceContent());
    }

    public UserExtensionsNode(InstanceContent lookupContent) {
        super(new ExtensionsNodeChildren(), new AbstractLookup(lookupContent));
        this.lookupContent = lookupContent;
        setDisplayName("User Extensions");
        this.lookupContent.add(new ReloadReportsAction.Cookie());
        this.lookupContent.add(new AddNewReportModuleAction.Cookie());
    }

    @Override
    public SystemAction[] getActions(boolean context) {
        return new SystemAction[]{
                    SystemAction.get(AddNewReportModuleAction.class),
                    SystemAction.get(ReloadReportsAction.class)
                };
    }
}
