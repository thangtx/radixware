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

package org.radixware.wps.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.views.IEmbeddedView;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.wps.rwt.UIObject;


public class ViewSupport<T extends UIObject & IView> {

    private final T container;
    private final List<IView.IViewListener<T>> listeners = new LinkedList<>();

    public ViewSupport(T container) {
        this.container = container;
    }

    public IWidget getParentWindow() {
        return container.findRoot();
    }

    public IView findParentView() {
        UIObject parent = container.getParent();
        while (parent != null) {
            if (parent instanceof IView) {
                return (IView) parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
    
    public List<IEmbeddedView> findEmbeddedViews(){
        final EmbeddedViewsFinder finder = new EmbeddedViewsFinder(container);
        container.visit(finder);
        return finder.getEmbeddedViews();
    }

    public void addViewListener(IView.IViewListener<T> l) {
        synchronized (listeners) {
            if (!listeners.contains(l)) {
                listeners.add(l);
            }
        }
    }

    public void removeViewListener(IView.IViewListener<T> l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    public void fireViewOpened() {
        final List<IView.IViewListener<T>> ls;
        synchronized (listeners) {
            ls = new ArrayList<>(listeners);
        }
        for (IView.IViewListener<T> l : ls) {
            l.opened(container);
        }
    }

    public void fireViewClosed() {
        final List<IView.IViewListener<T>> ls;
        synchronized (listeners) {
            ls = new ArrayList<>(listeners);
        }
        for (IView.IViewListener<T> l : ls) {
            l.closed(container);
        }
    }
}
