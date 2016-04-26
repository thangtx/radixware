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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.types.Id;

/**
 * Property list resolver. Supports {@Linkplain AdsClassDef} and {@Linkplain AdsFilterDef}
 * contexts
 *
 */
public class PropertyUsageSupport {

    public class PropertyRef {

        private Id propertyId;
        private AdsDefinition property;

        public AdsDefinition findProperty() {
            synchronized (this) {
                if (property == null) {
                    property = PropertyUsageSupport.this.findProperty(propertyId);
                }
                return property;
            }
        }

        public PropertyRef(Id propertyId) {
            this.propertyId = propertyId;
        }

        public Id getPropertyId() {
            return propertyId;
        }
    }

    private final Map<Id, PropertyRef> id2RefMap = new HashMap<>();
    private final List<Id> list;
    private final AdsDefinition contextDef;

    private PropertyUsageSupport(AdsDefinition contextClass, List<Id> propertyIds) {
        this.contextDef = contextClass;
        if (propertyIds == null) {
            list = Collections.emptyList();
        } else {
            list = new ArrayList<>(propertyIds);
        }
    }

    public static final PropertyUsageSupport newInstance(AdsDefinition context, List<Id> ids) {
        for (RadixObject obj = context.getContainer(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof AdsFilterDef || obj instanceof AdsClassDef) {
                return new PropertyUsageSupport((AdsDefinition) obj, ids);
            }
        }
        return new PropertyUsageSupport(null, ids);
    }

    public List<PropertyRef> get() {
        final List<PropertyRef> refs = new ArrayList<>(list.size());
        for (Id id : list) {
            refs.add(getReference(id));
        }
        return refs;
    }

    public PropertyRef getReference(Id id) {
        synchronized (this) {
            PropertyRef ref = id2RefMap.get(id);
            if (ref == null) {
                ref = new PropertyRef(id);
                id2RefMap.put(id, ref);
            }
            return ref;
        }
    }

    public List<Id> getIds() {
        return new ArrayList<>(list);
    }

    private AdsDefinition findProperty(Id id) {
        if (contextDef == null) {
            return null;
        }
        if (contextDef instanceof AdsClassDef) {
            return ((AdsClassDef) contextDef).getProperties().findById(id, EScope.ALL).get();
        } else if (contextDef instanceof AdsFilterDef) {
            return ((AdsFilterDef) contextDef).getParameters().findById(id);
        } else {
            return null;
        }
    }

    public final List<Id> availablePropIds(IFilter<RadixObject> filter) {
        if (contextDef instanceof AdsClassDef) {
            final List<AdsPropertyDef> allProps = ((AdsClassDef) contextDef).getProperties().get(EScope.ALL);
            final ArrayList<Id> refs = new ArrayList<>(allProps.size());
            for (AdsPropertyDef prop : allProps) {
                if (filter == null || filter.isTarget(prop)) {
                    refs.add(prop.getId());
                }
            }
            return refs;
        } else if (contextDef instanceof AdsFilterDef) {
            final List<AdsFilterDef.Parameter> allParams = ((AdsFilterDef) contextDef).getParameters().list();
            final ArrayList<Id> refs = new ArrayList<>(allParams.size());
            for (AdsFilterDef.Parameter param : allParams) {
                if (filter == null || filter.isTarget(param)) {
                    refs.add(param.getId());
                }
            }
            return refs;
        } else {
            return Collections.emptyList();
        }
    }

    public final List<Id> availablePropIds() {
        return availablePropIds(null);
    }

    void collectDependences(List<Definition> list) {
        if (contextDef == null) {
            return;
        }
        for (Id id : this.list) {
            AdsDefinition p = getReference(id).findProperty();
            if (p != null) {
                list.add(p);
            }
        }
    }
}
