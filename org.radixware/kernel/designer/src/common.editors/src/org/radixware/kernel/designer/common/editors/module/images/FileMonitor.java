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

package org.radixware.kernel.designer.common.editors.module.images;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


class FileMonitor {

    public interface FileListener {
        void fileChanged (File file);
    }

    private static FileMonitor fileMonitor = null;

    private Timer timer;
    private HashMap<File, Long> modifiedTimeOfFile;
    private HashMap<File, FileListener> listenerOfFile;

    protected FileMonitor() {
        modifiedTimeOfFile = new HashMap<File, Long>();
        listenerOfFile = new HashMap<File, FileListener>();
        timer = new Timer (true);
        timer.schedule (new FileMonitorNotifier(), 0, 1000);
    }

    public static void register(File file, FileListener listener) {
        if (fileMonitor == null)
            fileMonitor = new FileMonitor();
        if (!fileMonitor.modifiedTimeOfFile.containsKey (file)) {
            long modifiedTime = file.exists() ? file.lastModified() : -1;
            fileMonitor.modifiedTimeOfFile.put (file, new Long (modifiedTime));
        }
        fileMonitor.listenerOfFile.put(file, listener);
    }

    public static void unregister(File file) {
        if (fileMonitor == null)
            return;
        fileMonitor.modifiedTimeOfFile.remove(file);
        fileMonitor.listenerOfFile.remove(file);
        if (fileMonitor.listenerOfFile.isEmpty()) {
            fileMonitor.timer.cancel();
            fileMonitor = null;
        }
    }

    public static void cancel() {
        if (fileMonitor == null)
            return;
        fileMonitor.timer.cancel();
        Collection allFiles = new ArrayList (fileMonitor.modifiedTimeOfFile.keySet());
        for (Iterator i = allFiles.iterator(); i.hasNext(); ) {
            File file = (File) i.next();
            file.delete();
        }
        fileMonitor.modifiedTimeOfFile.clear();
        fileMonitor.listenerOfFile.clear();
        fileMonitor = null;
    }

    private class FileMonitorNotifier extends TimerTask {

        public void run() {
            // Loop over the registered files and see which have changed.
            // Use a copy of the list in case listener wants to alter the
            // list within its fileChanged method.
            Collection allFiles = new ArrayList (modifiedTimeOfFile.keySet());

            for (Iterator i = allFiles.iterator(); i.hasNext(); ) {
                File file = (File) i.next();
                long lastModifiedTime = (modifiedTimeOfFile.get (file)).longValue();
                long newModifiedTime = file.exists() ? file.lastModified() : -1;

                // Chek if file has changed
                if (newModifiedTime != lastModifiedTime) {

                    // Register new modified time
                    modifiedTimeOfFile.put (file, new Long (newModifiedTime));

                    // Notify listener
                    FileListener listener = listenerOfFile.get(file);
                    if (listener != null)
                        listener.fileChanged(file);
                }
            }
        }
        
    }

}
