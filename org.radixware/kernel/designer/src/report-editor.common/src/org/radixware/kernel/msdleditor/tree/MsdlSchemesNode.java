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

package org.radixware.kernel.msdleditor.tree;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
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
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.userreport.repository.msdl.MsdlScheme;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;


public class MsdlSchemesNode  extends AbstractNode {
     
    private static class MsdlSchemesKey {
        private final AdsMsdlSchemeDef msdlSchemeDef;
        private final MsdlScheme msdlScheme;
        private final PropertyChangeListener schemeNamesListener;
        private final RadixObject.RenameListener schemeRenameListener;

        public MsdlSchemesKey( MsdlScheme msdlScheme,AdsMsdlSchemeDef msdlSchemeDef, PropertyChangeListener schemeNamesListener, RadixObject.RenameListener schemeRenameListener) {
            this.msdlSchemeDef = msdlSchemeDef;
            this.msdlScheme = msdlScheme;
            this.schemeNamesListener = schemeNamesListener;
            this.msdlScheme.addPropertyChangeListener(schemeNamesListener);
            this.schemeRenameListener = schemeRenameListener;
            this.msdlSchemeDef.addRenameListener(schemeRenameListener);

        }

        private void dispose() {
            this.msdlSchemeDef.removeRenameListener(schemeRenameListener);
            this.msdlScheme.removePropertyChangeListener(schemeNamesListener);
        }
    }

    private static final class SchemesNodeChildren extends Children.Keys<MsdlSchemesKey> {

        private final java.util.Map<MsdlSchemesNode.MsdlSchemesKey, Node> nodesCache = new WeakHashMap<>();
        private final Object createLock = new Object();
        private final Object updateLock = new Object();
        private final PropertyChangeListener schemeNamesListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateKeys();
            }
        };
        private final ChangeListener schemesManagerListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateKeys();
            }
        };
        private final RadixObject.RenameListener renameListener = new RadixObject.RenameListener() {
            @Override
            public void onEvent(RadixObject.RenameEvent e) {
                updateKeys();
            }
        };

        public SchemesNodeChildren() {
            super();
            updateKeys();
            UserExtensionManager.getInstance().addChangeListener(schemesManagerListener);
            UserExtensionManager.getInstance().getMsdlSchemes().addChangeListener(schemesManagerListener);
        }
        private static  ExecutorService executor = Executors.newFixedThreadPool(3);

        private void updateKeys() {
            Callable<java.util.List<MsdlScheme>> listGetter = new Callable<java.util.List<MsdlScheme>>() {
                @Override
                public List<MsdlScheme> call() throws Exception {
                    return UserExtensionManager.getInstance().getMsdlSchemes().getMsdlSchemes();
                }
            };

            final Future<java.util.List<MsdlScheme>> task = executor.submit(listGetter);

            if (!task.isDone()) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            updateKeysImpl(task.get());
                        } catch (                InterruptedException | ExecutionException ex) {
                        }
                    }
                });
            } else {
                try {
                    updateKeysImpl(task.get());
                } catch ( InterruptedException | ExecutionException ex) {
                }
            }
        }

        private void updateKeysImpl(final java.util.List<MsdlScheme> msdlSchemes) {
            final java.util.List<MsdlSchemesNode.MsdlSchemesKey> newKeys = new LinkedList<>();
            synchronized (updateLock) {


                java.util.Map< Id,MsdlSchemesNode.MsdlSchemesKey> keys = new HashMap<>();
                for (MsdlSchemesNode.MsdlSchemesKey known : nodesCache.keySet()) {
                    keys.put(known.msdlSchemeDef.getId(), known);
                }

                for (MsdlScheme msdlScheme : msdlSchemes) {
                    AdsMsdlSchemeDef def = msdlScheme.findMsdlSchemeDefinition();
                    MsdlSchemesNode.MsdlSchemesKey exKey = keys.get(msdlScheme.getId());
                    if (exKey != null) {
                        if (exKey.msdlScheme == msdlScheme && exKey.msdlSchemeDef == def) {
                            newKeys.add(exKey);
                        } else {
                            newKeys.add(new MsdlSchemesNode.MsdlSchemesKey(msdlScheme, def, schemeNamesListener, renameListener));
                        }
                    } else {
                        newKeys.add(new MsdlSchemesNode.MsdlSchemesKey(msdlScheme, def, schemeNamesListener, renameListener));
                    }
                }
                for (MsdlSchemesNode.MsdlSchemesKey key : new ArrayList<>(nodesCache.keySet())) {
                    if (!newKeys.contains(key)) {
                        key.dispose();
                    }
                }



                Collections.sort(newKeys, new Comparator<MsdlSchemesNode.MsdlSchemesKey>() {
                    @Override
                    public int compare(MsdlSchemesNode.MsdlSchemesKey o1, MsdlSchemesNode.MsdlSchemesKey o2) {
                        return o1.msdlSchemeDef.getName().compareTo(o2.msdlSchemeDef.getName());
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
        protected Node[] createNodes(MsdlSchemesNode.MsdlSchemesKey key) {
            synchronized (createLock) {
                Node node = nodesCache.get(key);
                if (node == null) {
                    node = new MsdlSchemeDefinitionNode(key.msdlScheme, key.msdlSchemeDef);
                    nodesCache.put(key, node);
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
    
    public MsdlSchemesNode() {
        this(new InstanceContent());
    }
    
    public MsdlSchemesNode(InstanceContent lookupContent) {
        super(new MsdlSchemesNode.SchemesNodeChildren(), new AbstractLookup(lookupContent));
        this.lookupContent = lookupContent;
        setDisplayName("MsdlSchemes");  
        this.lookupContent.add(new ReloadMsdlSchemesAction.Cookie());
        this.lookupContent.add(new CreateMsdlSchemeAction.Cookie());        
    }

    @Override
    public SystemAction[] getActions(boolean context) {
        return new SystemAction[]{
            SystemAction.get(CreateMsdlSchemeAction.class),
            SystemAction.get(ReloadMsdlSchemesAction.class)
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
