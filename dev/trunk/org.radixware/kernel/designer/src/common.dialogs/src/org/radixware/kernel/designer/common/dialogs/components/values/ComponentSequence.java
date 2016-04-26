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

/*
 * 10/17/11 11:02 AM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import java.awt.Component;
import java.awt.Container;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;


public class ComponentSequence<TComponentKey> {

    protected final Map<TComponentKey, Entry<Object, Component>> components;
    protected final List<TComponentKey> componentSequence;
    private final Container container;

    public ComponentSequence(Container container) {
        super();

        assert container != null : "null container";

        this.container = container;
        components = new HashMap<>();
        componentSequence = new LinkedList<>();
    }

    public final void addComponent(TComponentKey key, Component component, Object constraints) {
        addComponent(componentSequence.size(), key, component, constraints);
    }

    public final void addComponent(TComponentKey key, Component component) {
        addComponent(key, component, null);
    }

    public final void addComponent(int index, TComponentKey key, Component component, Object constraints) {
        if (component == null) {
            return;
        }
        if (components.containsKey(key)) {
            throw new IllegalStateException("component whith key = \"" + key + "\" alrady mapped");
        }
        components.put(key, new SimpleEntry<>(constraints, component));
        componentSequence.add(index, key);
    }

    public final void build() {
        for (TComponentKey key : componentSequence) {
            container.remove(components.get(key).getValue());
        }

        for (TComponentKey key : componentSequence) {
            container.add(components.get(key).getValue(), components.get(key).getKey());
        }
    }

    public final Component replaceComponent(TComponentKey key, Component component, Object constraints) {
        int index = componentSequence.indexOf(key);
        Component comp = removeComponent(key);
        if (index == -1) {
            addComponent(key, component, constraints);
        } else {
            addComponent(index, key, component, constraints);
        }
        return comp;
    }

    public final void moveComponent(TComponentKey key, int newIndex) {
        int oldIndex = componentSequence.indexOf(key);
        if (oldIndex != -1 && newIndex <= componentSequence.size()) {
            componentSequence.remove(oldIndex);
            componentSequence.add(newIndex, key);
        }
    }

    public final Component removeComponent(TComponentKey key) {
        Component comp = getComponentBy(key);
        components.remove(key);
        componentSequence.remove(key);
        container.remove(comp);
        return comp;
    }

    public final void clear() {
        for (TComponentKey key : componentSequence) {
            container.remove(components.get(key).getValue());
        }
        components.clear();
        componentSequence.clear();
    }

    public Container getContainer() {
        return container;
    }

    public final List<TComponentKey> getSequence() {
        return componentSequence;
    }

    public final int getSequenceCount() {
        return componentSequence.size();
    }

    public final Component getComponentBy(TComponentKey key) {
        return components.get(key).getValue();
    }

    public final Class<? extends Component> getComponentClass(TComponentKey key) {
        return components.get(key).getValue().getClass();
    }

    public final <TComponent extends Component> TComponent getComponentBy(TComponentKey key, Class<TComponent> componentClass) {

        if (componentClass == null) {
            return null;
        }

        Component comp = getComponentBy(key);
        if (comp == null || !componentClass.isInstance(comp.getClass())) {
            return null;
        }

        return (TComponent) comp;
    }

    public final <TComponent extends Component> List<Entry<TComponentKey, TComponent>> getComlonentListBy(Class<TComponent> componentClass) {
        List<Entry<TComponentKey, TComponent>> comp = new ArrayList<>();
        for (TComponentKey key : components.keySet()) {
            TComponent c = getComponentBy(key, componentClass);
            if (c != null) {
                comp.add(new SimpleEntry<>(key, c));
            }
        }
        return comp;
    }
}
