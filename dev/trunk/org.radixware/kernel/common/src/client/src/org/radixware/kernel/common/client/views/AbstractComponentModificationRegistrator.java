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

package org.radixware.kernel.common.client.views;

import org.radixware.kernel.common.client.widgets.IModificationListener;
import org.radixware.kernel.common.client.widgets.IModifableComponent;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.models.items.properties.Property;


public abstract class AbstractComponentModificationRegistrator {

    private IModificationListener listenerToNotify;
    private final IModifableComponent modifableComponent;
    private final List<IModifableComponent> children = new LinkedList<IModifableComponent>();

    public interface ModificationListenerLookup {

        public IModificationListener findParentModificationListener();
    }

    public AbstractComponentModificationRegistrator(final IModifableComponent component, ModificationListenerLookup lookup) {
        this(component, lookup == null ? null : lookup.findParentModificationListener());
    }

    public AbstractComponentModificationRegistrator(final IModifableComponent component, IModificationListener listener) {
        listenerToNotify = listener;
        modifableComponent = component;
        if (listenerToNotify == null) {
            listenerToNotify = new IModificationListener() {

                @Override
                public void childComponentWasClosed(final IModifableComponent childComponent) {
                }

                @Override
                public void notifyPropertyModificationStateChanged(Property property, boolean modified) {
                }

                @Override
                public void notifyComponentModificationStateChanged(IModifableComponent childComponent, boolean modified) {
                }
            };
        }
    }

    public void registerChildComponent(IModifableComponent component) {
        if (!children.contains(component)) {
            children.add(component);
        }
    }

    public void unregisterChildComponent(IModifableComponent component) {
        children.remove(component);
    }

    public void notifyModificationStateChanged(IModifableComponent childComponent, boolean isModified) {
        registerChildComponent(childComponent);
        listenerToNotify.notifyComponentModificationStateChanged(modifableComponent, isModified);
    }

    public void close() {
        children.clear();
        listenerToNotify.childComponentWasClosed(modifableComponent);
    }

    public boolean isSomeChildComponentInModifiedState() {
        for (IModifableComponent component : children) {
            if (component.inModifiedStateNow()) {
                return true;
            }
        }
        return false;
    }

    public Collection<IModifableComponent> getRegisteredComponents() {
        return Collections.unmodifiableCollection(children);
    }

    public IModificationListener getListener() {
        return listenerToNotify;
    }
}
