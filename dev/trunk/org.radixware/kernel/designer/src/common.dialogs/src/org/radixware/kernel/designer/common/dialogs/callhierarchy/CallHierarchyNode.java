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

package org.radixware.kernel.designer.common.dialogs.callhierarchy;

import java.awt.Image;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.designer.common.dialogs.actions.GoToRadixObjectAction;


final class CallHierarchyNode extends AbstractNode {
    
    private Action preferredAction;
    private final RadixObject object;
    
    public CallHierarchyNode(Children children, RadixObject object) {
        super(children);
        this.object = object;
        
        assert object != null : "Node source object is null";
        
        RadixObject definition = getDefinition();
        
        assert definition != null : "Owner definition is null";
        
        if (definition instanceof AdsMethodDef) {
            setDisplayName(getMethodFullName((AdsMethodDef) definition));
        } else {
            setDisplayName(definition.getQualifiedName());
        }
        
        setShortDescription(definition.getToolTip());
    }
    
    @Override
    public Image getIcon(int type) {
        if (getDefinition() != null && getDefinition().getIcon() != null) {
            return getDefinition().getIcon().getImage();
        } else {
            return super.getIcon(type);
        }
    }
    
    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
    
    @Override
    public Action getPreferredAction() {
        if (preferredAction == null) {
            preferredAction = new GoToRadixObjectAction(getRadixObject());
        }
        return preferredAction;
    }
    
    @Override
    public Action[] getActions(boolean context) {
        final Action action = getPreferredAction();
        return new Action[]{action};
    }
    
    private RadixObject getRadixObject() {
        return object;
    }
    
    private Definition getDefinition() {
        return object.getDefinition();
    }
    
    private String getMethodFullName(AdsMethodDef method) {
        String name = method.getProfile().getName();
        
        String ownerName = method.getOwnerDefinition().getQualifiedName();
        
        return name + "   [" + ownerName + "]";
    }
}