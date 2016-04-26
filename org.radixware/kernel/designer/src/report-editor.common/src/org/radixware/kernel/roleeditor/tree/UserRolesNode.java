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
package org.radixware.kernel.roleeditor.tree;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.role.AppRole;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;

public class UserRolesNode extends AbstractNode {

    private static class AppRoleKey {

        public static final AppRoleKey DEFAULT_KEY = new AppRoleKey(null, null, null, null);
        private final AppRole appRole;
        private final AdsRoleDef role;
        private final PropertyChangeListener roleNamesListener;
        private final RadixObject.RenameListener roleRenameListener;

        public AppRoleKey(AppRole appRole, AdsRoleDef role, PropertyChangeListener roleNamesListener, RadixObject.RenameListener roleRenameListener) {
            this.appRole = appRole;
            this.role = role;
            this.roleNamesListener = roleNamesListener;
            if (roleNamesListener != null && appRole != null) {
                this.appRole.addPropertyChangeListener(roleNamesListener);
            }
            this.roleRenameListener = roleRenameListener;
            if (roleRenameListener != null && role != null) {
                this.role.addRenameListener(roleRenameListener);
            }

        }

        private void dispose() {
            this.role.removeRenameListener(roleRenameListener);
            this.appRole.removePropertyChangeListener(roleNamesListener);
        }
    }

    private static final class RolesNodeChildren extends Children.Keys<AppRoleKey> {

        private final java.util.Map<AppRoleKey, Node> nodesCache = new WeakHashMap<>();
        private final Object createLock = new Object();
        private final Object updateLock = new Object();
        private final PropertyChangeListener roleNamesListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateKeys();
            }
        };
        private final ChangeListener rolesManagerListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateKeys();
            }
        };
        private final RadixObject.RenameListener renameListener = new RadixObject.RenameListener() {
            @Override
            public void onEvent(RenameEvent e) {
                updateKeys();
            }
        };

        public RolesNodeChildren() {
            super();
            setKeys(new AppRoleKey[]{AppRoleKey.DEFAULT_KEY});
        }

        private void updateKeys() {
            Callable<java.util.List<AppRole>> listGetter = new Callable<java.util.List<AppRole>>() {
                @Override
                public List<AppRole> call() throws Exception {
                    return UserExtensionManager.getInstance().getAppRoles().getRoles();
                }
            };

            final Future<java.util.List<AppRole>> task = RequestProcessor.submit(listGetter);

            RequestProcessor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        updateKeysImpl(task.get());
                    } catch (InterruptedException | ExecutionException ex) {
                    }
                }
            });
        }

        private void updateKeysImpl(final java.util.List<AppRole> roles) {
            final java.util.List<AppRoleKey> newKeys = new LinkedList<>();
            synchronized (updateLock) {

                java.util.Map< Id, AppRoleKey> keys = new HashMap<>();
                for (AppRoleKey known : nodesCache.keySet()) {
                    keys.put(known.appRole.getId(), known);
                }

                for (AppRole role : roles) {
                    AdsRoleDef def = role.findRoleDefinition();
                    AppRoleKey exKey = keys.get(role.getId());
                    if (exKey != null) {
                        if (exKey.appRole == role && exKey.role == def) {
                            newKeys.add(exKey);
                        } else {
                            newKeys.add(new AppRoleKey(role, def, roleNamesListener, renameListener));
                        }
                    } else {
                        newKeys.add(new AppRoleKey(role, def, roleNamesListener, renameListener));
                    }
                }
                for (AppRoleKey key : new ArrayList<>(nodesCache.keySet())) {
                    if (!newKeys.contains(key)) {
                        key.dispose();
                    }
                }

                Collections.sort(newKeys, new Comparator<AppRoleKey>() {
                    @Override
                    public int compare(AppRoleKey o1, AppRoleKey o2) {
                        return o1.role.getName().compareTo(o2.role.getName());
                    }
                });
            }
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    setKeys(newKeys);
                }
            });
        }

        @Override
        protected Node[] createNodes(AppRoleKey key) {
            synchronized (createLock) {
                if (AppRoleKey.DEFAULT_KEY == key) {
                    Node node = new AbstractNode(LEAF);
                    node.setDisplayName("Loading...");
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            updateKeys();
                            UserExtensionManager.getInstance().addChangeListener(rolesManagerListener);
                            UserExtensionManager.getInstance().getAppRoles().addChangeListener(rolesManagerListener);
                        }
                    });
                    return new Node[]{node};
                } else {
                    Node node = nodesCache.get(key);
                    if (node == null) {
                        node = new AppRoleDefinitionNode(key.appRole, key.role);
                        nodesCache.put(key, node);
                    }
                    if (node == null) {
                        return null;
                    } else {
                        return new Node[]{node};
                    }
                }
            }
        }
    }
    private InstanceContent lookupContent;

    public UserRolesNode() {
        this(new InstanceContent());
    }

    public UserRolesNode(InstanceContent lookupContent) {
        super(new RolesNodeChildren(), new AbstractLookup(lookupContent));
        this.lookupContent = lookupContent;
        setDisplayName("Application Roles");
        this.lookupContent.add(new ReloadRolesAction.Cookie());
        this.lookupContent.add(new CreateAppRoleAction.Cookie());
    }

    @Override
    public SystemAction[] getActions(boolean context) {
        return new SystemAction[]{
            SystemAction.get(CreateAppRoleAction.class),
            SystemAction.get(ReloadRolesAction.class)
        };

    }

    @Override
    public Image getIcon(int type) {
        return UdsDefinitionIcon.SEGMENT.getImage();
    }

    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
}
