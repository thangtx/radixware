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

package org.radixware.kernel.common.client.models.items;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.types.Id;

public abstract class ModelItem {

    protected final Model owner;
    private final Id id;
    protected List<IModelWidget> widgets = null;
    private List<ModelItem> dependentItems;
    private boolean isNotificationScheduled;
    private boolean isNotificationsBlocked;

    protected ModelItem(Model owner, Id id) {
        this.owner = owner;
        this.id = id;
    }

    public final Id getId() {
        return id;
    }
    
    public final void addDependent(final ModelItem item){
        if (owner!=item.owner){            
            throw new IllegalArgumentException(String.format("item #%s has another owner", item.id));
        }
        if ((dependentItems==null || !dependentItems.contains(item)) && item!=this ){
            if (dependentItems==null){
              dependentItems = new LinkedList<>();
            }
            dependentItems.add(item);
        }        
    }
    
    public final boolean removeDependent(final ModelItem item){
        return dependentItems==null ? false : dependentItems.remove(item);
    }

    public final boolean hasSubscriber() {
        return widgets != null && !widgets.isEmpty();
    }

    public final void subscribe(IModelWidget w) {
        if (widgets == null) {
            widgets = new ArrayList<IModelWidget>(1);
        }
        if (!widgets.contains(w)) {
            widgets.add(w);
        }
    }

    public final void unsubscribe(IModelWidget w) {
        if (widgets != null) {
            widgets.remove(w);
        }
    }

    public final void unsubscribeAll() {
        if (widgets != null) {
            widgets.clear();
        }
    }

    public final void afterModify() {
        if (isNotificationsBlocked()){
            isNotificationScheduled = true;
        }else{
            isNotificationScheduled = false;
            afterModifyImpl();
            if (dependentItems!=null){
                final List<ModelItem> depends = new LinkedList<ModelItem>(dependentItems);
                for (ModelItem item: depends){
                    item.afterModifyImpl();
                }
            }
        }
    }
    
    private void afterModifyImpl(){
        if (widgets != null) {
            final List<IModelWidget> subscribedWidgets = new ArrayList<IModelWidget>(1);
            subscribedWidgets.addAll(widgets);
            for (IModelWidget w : subscribedWidgets) {
                w.refresh(this);
            }
        }
    }
    
    public final boolean isNotificationScheduled(){
        return isNotificationScheduled;
    }
    
    public final void setNotificationsBlocked(final boolean isBlocked){
        isNotificationsBlocked = isBlocked;
    }
    
    public final boolean isNotificationsBlocked(){
        return isNotificationsBlocked;
    }

    public final IClientEnvironment getEnvironment() {
        return getOwner().getEnvironment();
    }

    public Model getOwner() {
        return owner;
    }
}