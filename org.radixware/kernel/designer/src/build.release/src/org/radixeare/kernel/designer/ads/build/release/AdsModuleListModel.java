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

package org.radixeare.kernel.designer.ads.build.release;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.radixware.kernel.common.defs.ads.module.AdsModule;


class AdsModuleListModel implements ListModel {

    private final List<AdsModule> selectedModules = new LinkedList<AdsModule>();

    @Override
    public int getSize() {
        return selectedModules.size();
    }

    @Override
    public Object getElementAt(int index) {
        return selectedModules.get(index);
    }
    private final List<ListDataListener> listeners = new LinkedList<ListDataListener>();

    @Override
    public void addListDataListener(ListDataListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    void fireChange() {
        final ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, selectedModules.size());
        synchronized (listeners) {
            for (ListDataListener l : listeners) {
                l.contentsChanged(e);
            }
        }
    }

    public boolean contains(AdsModule module) {
        return selectedModules.contains(module);
    }

    public void add(AdsModule module) {
        if (!contains(module)) {
            selectedModules.add(module);
            Collections.sort(selectedModules, new Comparator<AdsModule>() {

                @Override
                public int compare(AdsModule o1, AdsModule o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            fireChange();
        }
    }

    public void remove(AdsModule module) {
        if (contains(module)) {
            selectedModules.remove(module);
            fireChange();
        }
    }

    public AdsModule get(int index) {
        return selectedModules.get(index);
    }

    public List<AdsModule> list() {
        return Collections.unmodifiableList(selectedModules);
    }

    public void set(List<AdsModule> list) {
        if (list == null) {
            selectedModules.clear();
        } else {
            selectedModules.clear();
            selectedModules.addAll(list);
            Collections.sort(selectedModules, new Comparator<AdsModule>() {

                @Override
                public int compare(AdsModule o1, AdsModule o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        fireChange();
    }

    public boolean isEmpty() {
        return selectedModules.isEmpty();
    }
}
