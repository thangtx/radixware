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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ClassHierarchyIterator;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.types.Id;


class PropertyAttributeHierarchyIterator extends HierarchyIterator<PropertyPresentation> {
    
    private final ClassHierarchyIterator internal;
    private List<PropertyPresentation> next;
    private final Id propertyId;
    
    public PropertyAttributeHierarchyIterator(PropertyPresentation prop, EScope scope, HierarchyIterator.Mode mode) {
        super(mode);
        this.internal = new ClassHierarchyIterator(prop.getOwnerProperty().getOwnerClass(), scope, mode);
        this.next = null;
        this.propertyId = prop.getOwnerProperty().getId();
    }
    
    @Override
    public boolean hasNext() {
        if (next == null && internal.hasNext()) {
            List<PropertyPresentation> result = new LinkedList<>();
            while (internal.hasNext()) {
                Chain<AdsClassDef> classes = internal.next();
                
                for (AdsClassDef clazz : classes) {
                    AdsPropertyDef classProp = clazz.getProperties().getLocal().findById(propertyId);
                    if (classProp instanceof IAdsPresentableProperty) {
                        ServerPresentationSupport ps = ((IAdsPresentableProperty) classProp).getPresentationSupport();
                        if (ps != null && ps.getPresentation() != null && !result.contains(ps.getPresentation())) {
                            result.add(ps.getPresentation());
                        }
                    }
                }
                if (!result.isEmpty()) {
                    break;
                }
            }
            if (result.isEmpty()) {
                next = null;
            } else {
                next = new ArrayList<>(result);
            }
            
        }
        return next != null;
    }
    
    @Override
    public Chain<PropertyPresentation> next() {
        if (hasNext()) {
            Chain<PropertyPresentation> result = Chain.newInstance(next);
            next = null;
            return result;
        } else {
            return Chain.empty();
        }
    }
}
