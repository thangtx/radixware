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
package org.radixware.kernel.reporteditor.tree;

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import static org.openide.nodes.Children.LEAF;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RemovedEvent;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.designer.common.general.utils.RequestProcessor;
import org.radixware.kernel.reporteditor.common.UserExtensionManager;

public class ReportsNode extends AbstractNode {

    private static final class ReportsNodeChildren extends Children.Keys<Object> {

        private final java.util.Map<ReportsModule, ReportModuleNode> nodesCache = new WeakHashMap<>();
        private final Object createLock = new Object();
        private final Object updateLock = new Object();
        private final RadixObject.IRemoveListener moduleRemoveListener = new RadixObject.IRemoveListener() {
            @Override
            public void onEvent(RemovedEvent e) {
                updateKeys();
            }
        };
        private final PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("report-module-list".equals(evt.getPropertyName())) {
                    updateKeys();
                }
            }
        };

        public ReportsNodeChildren() {
            super();
            setKeys(new Object[]{new Object()});
        }

        private void updateKeys() {
            Callable<List<ReportsModule>> listGetter = new Callable<List<ReportsModule>>() {
                @Override
                public List<ReportsModule> call() throws Exception {
                    List<ReportsModule> allModules = UserExtensionManager.getInstance().getUserReports().getReportModules();
                    Collections.sort(allModules, new Comparator<ReportsModule>() {
                        @Override
                        public int compare(ReportsModule o1, ReportsModule o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    return allModules;
                }
            };

            final Future<List<ReportsModule>> task = RequestProcessor.submit(listGetter);
            try {
                List<ReportsModule> result = task.get();
                updateKeysImpl(result);
            } catch (InterruptedException | ExecutionException ex) {
            }

        }

        private void updateKeysImpl(final List<ReportsModule> allModules) {
            synchronized (updateLock) {
                List<ReportsModule> currentKeys = new ArrayList<>(nodesCache.keySet());
                Collections.sort(currentKeys, new Comparator<ReportsModule>() {
                    @Override
                    public int compare(ReportsModule o1, ReportsModule o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                for (ReportsModule module : currentKeys) {
                    if (!allModules.contains(module)) {
                        module.getRemoveSupport().removeEventListener(moduleRemoveListener);
                    }
                }
                Children.MUTEX.postReadRequest(new Runnable() {
                    @Override
                    public void run() {
                        setKeys(allModules);
                    }
                });

            }
        }

        @Override
        protected Node[] createNodes(Object key) {
            synchronized (createLock) {
                if (key instanceof ReportsModule) {
                    ReportModuleNode node = new ReportModuleNode((ReportsModule) key);
                    ((ReportsModule) key).getRemoveSupport().addEventListener(moduleRemoveListener);
                    return new Node[]{node};
                } else {
                    Node node = new AbstractNode(LEAF);
                    node.setDisplayName("Loading...");
                    RequestProcessor.submit(new Runnable() {
                        @Override
                        public void run() {
                            updateKeys();
                            UserExtensionManager.getInstance().getUserReports().addPropertyChangeListener(listener);
                        }
                    });
                    return new Node[]{node};
                }


            }
        }
    }
    private InstanceContent lookupContent;

    public ReportsNode() {
        this(new InstanceContent());
    }
    private final ExportAllReportsAction.Cookie exportAllCookie;

    public ReportsNode(InstanceContent lookupContent) {
        super(new ReportsNodeChildren(), new AbstractLookup(lookupContent));
        this.lookupContent = lookupContent;
        setDisplayName("User-Defined Reports");
        this.lookupContent.add(new ReloadReportsAction.Cookie());
        this.lookupContent.add(new AddNewReportModuleAction.Cookie());
        this.lookupContent.add(new CompileAllReportsAction.Cookie());
        this.lookupContent.add(exportAllCookie = new ExportAllReportsAction.Cookie(null));
    }

    @Override
    public SystemAction[] getActions(boolean context) {
        return new SystemAction[]{
            SystemAction.get(AddNewReportModuleAction.class),
            SystemAction.get(ReloadReportsAction.class),
            SystemAction.get(ExportAllReportsAction.class),
            null,
            SystemAction.get(CompileAllReportsAction.class)
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
