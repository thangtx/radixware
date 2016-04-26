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

import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import org.netbeans.spi.tasklist.TaskScanningScope;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.RadixObject;


abstract class SelectionScanningScope extends TaskScanningScope {

    private Callback callback;
    //private RadixObject lastSelected;
    private final PropertyChangeListener listener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            synchronized (this) {
                if (callback != null) {
                    callback.refresh();
                }
            }
        }
    };

    SelectionScanningScope(String name, String hint, Image image) {
        super(name, hint, image);
        WindowManager.getDefault().getRegistry().addPropertyChangeListener(listener);
    }

    @Override
    public boolean isInScope(FileObject resource) {
        return false;
    }

    @Override
    public void attach(Callback callback) {
        synchronized (listener) {
            this.callback = callback;
        }
    }

    @Override
    public Lookup getLookup() {
        TopComponent tc = WindowManager.getDefault().getRegistry().getActivated();
        RadixObject obj = null;
        if (tc != null) {
            obj = tc.getLookup().lookup(RadixObject.class);
        }
        if (obj == null) {
            Node[] nodes = WindowManager.getDefault().getRegistry().getActivatedNodes();
            for (Node node : nodes) {
                obj = node.getLookup().lookup(RadixObject.class);
                if (obj != null) {
                    break;
                }
            }
        }

        RadixObject radixObject = filter(obj);
//        if (radixObject == null) {
//            if (lastSelected != null && lastSelected.isInBranch()) {
//                radixObject = lastSelected;
//            }
//        }
        if (radixObject != null) {
           // lastSelected = radixObject;
            return Lookups.fixed(radixObject);
        }

        return Lookups.fixed();
    }

    protected abstract RadixObject filter(RadixObject obj);

    @Override
    public Iterator<FileObject> iterator() {
        return new Iterator<FileObject>() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public FileObject next() {
                return null;
            }

            @Override
            public void remove() {
            }
        };
    }
}
