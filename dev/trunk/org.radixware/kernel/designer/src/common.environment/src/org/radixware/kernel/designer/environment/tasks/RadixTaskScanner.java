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

package org.radixware.kernel.designer.environment.tasks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.netbeans.spi.tasklist.PushTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.netbeans.spi.tasklist.TaskScanningScope;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.jml.JmlTagTask;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class RadixTaskScanner extends PushTaskScanner {

    public RadixTaskScanner() {
        super("RadixWare Tasks", "", "");
    }

    private static class RADIXUrlStreamHandler extends URLStreamHandler {

        private final JmlTagTask task;

        RADIXUrlStreamHandler(JmlTagTask task) {
            this.task = task;
        }

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            return new URLConnectionImpl(u);
        }

        private static class URLConnectionImpl extends URLConnection {

            public URLConnectionImpl(URL u) {
                super(u);
            }

            @Override
            public void connect() throws IOException {
            }
        }

        @Override
        protected String toExternalForm(URL u) {
            Definition def = task.getOwnerDefinition();
            return def == null ? "<inaccessible>" : def.getQualifiedName();
        }
    }

    private final class TaskActionListener implements ActionListener {

        private final JmlTagTask task;

        public TaskActionListener(JmlTagTask task) {
            this.task = task;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (task.isInBranch()) {
                DialogUtils.goToObject(task);
            }
        }
    };

    private Task createTask(JmlTagTask tag) {
        URL url;
        try {
            AdsDefinition topLevel = tag.getOwnerJml().getOwnerDef().findTopLevelDef();
            if (topLevel == null) {
                return null;
            }
            int index = tag.getOwnerJml().getItems().indexOf(tag);
            url = new URL("radix", "branch", index, tag.getOwnerJml().getOwnerDef().getQualifiedName(), new RADIXUrlStreamHandler(tag));
            String groupname;
            switch (tag.getType()) {
                case FIXME:
                    groupname = "nb-tasklist-error";
                    break;
                case TODO:
                    groupname = "nb-tasklist-todo";
                    break;
                case NOTE:
                    groupname = "nb-tasklist-warning";
                    break;
                default:
                    groupname = "etc";
            }
            return Task.create(url, groupname, tag.getDisplayName(), new TaskActionListener(tag), null);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    private static RadixTaskScanner scanner = null;
    private static final Object monitor = new Object();

    public static RadixTaskScanner create() {
        synchronized (monitor) {
            if (scanner == null) {
                scanner = new RadixTaskScanner();
            }
            return scanner;
        }
    }
    final ReentrantLock lock = new ReentrantLock();

    @Override
    public void setScope(TaskScanningScope scope, final Callback callback) {

        if (callback != null) {
            if (lock.tryLock()) {
                try {
                    final Collection<? extends RadixObject> objects = scope.getLookup().lookupAll(RadixObject.class);
                    //final List<RadixObject> contexts = new LinkedList<RadixObject>();
            /*
                     *
                     * final RadixObject radixObject = WindowManager.getDefault().getRegistry().getActivated().getLookup().lookup(RadixObject.class);


                    if (radixObject == null) {
                    DialogUtils.messageError("There is no object selected.");
                    return;
                    }
                     *
                     */
//                    RequestProcessor.getDefault().post(new Runnable() {
//
//                        @Override
//                        public void run() {
                    try {
                        callback.started();

                        //Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
                        final List<Task> tasks = new LinkedList<Task>();
                        for (RadixObject b : objects) {
                            b.visit(new IVisitor() {

                                @Override
                                public void accept(RadixObject radixObject) {
                                    Task t = createTask((JmlTagTask) radixObject);

                                    if (t != null) {
                                        tasks.add(t);
                                    }
                                }
                            }, new VisitorProvider() {

                                @Override
                                public boolean isTarget(RadixObject radixObject) {
                                    return radixObject instanceof JmlTagTask;
                                }

                                @Override
                                public boolean isContainer(RadixObject radixObject) {
                                    if (radixObject instanceof DdsSegment || radixObject instanceof DdsModule) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                            });
                        }

                        callback.setTasks(tasks);
                    } finally {
                        callback.finished();
                    }
//                        }
//                    });
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
