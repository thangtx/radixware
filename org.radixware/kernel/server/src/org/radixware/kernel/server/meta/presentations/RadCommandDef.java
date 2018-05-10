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

package org.radixware.kernel.server.meta.presentations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.common.meta.RadDefinition;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;

public class RadCommandDef extends RadDefinition {

    public final ECommandScope scope;
    public final ECommandNature nature;
    private final List<Id> propIds;  // if not null then properties are restricted
    private final Id classId; // if not null then classes are restricted
    private final boolean isReadOnly;
    private final boolean traceGuiActivity;
    public final ECommandAccessibility accessibility;
    
    public RadCommandDef(
            final Id id,
            final String name,
            final ECommandScope scope,
            final Id[] propIds,
            final ECommandAccessibility accessibility,
            final ECommandNature nature,
            final Id classId) {
        this(id, name, scope, propIds, accessibility, nature, classId, false, true);
    }
    
    public RadCommandDef(
            final Id id,
            final String name,
            final ECommandScope scope,
            final Id[] propIds,
            final ECommandAccessibility accessibility,
            final ECommandNature nature,
            final Id classId,
            final boolean isReadOnly) {
        this(id, name, scope, propIds, accessibility, nature, classId, isReadOnly, true);
    }

//Constructor
    public RadCommandDef(
            final Id id,
            final String name,
            final ECommandScope scope,
            final Id[] propIds,
            final ECommandAccessibility accessibility,
            final ECommandNature nature,
            final Id classId,
            final boolean isReadOnly, 
            final boolean traceGuiActivity) {
        super(id, name);
        this.scope = scope;
        this.nature = nature;
        if (propIds != null) {
            this.propIds = Collections.unmodifiableList(Arrays.asList(propIds));
        } else {
            this.propIds = null; //props are not restricted
        }
        this.accessibility = accessibility;
        this.classId = classId;
        this.isReadOnly = isReadOnly;
        this.traceGuiActivity = traceGuiActivity;
    }
    
    
    private Release release = null;

    void link(final Release release) {
        this.release = release;
    }

//Public methods 
    public final boolean isApplicableForProperty(final Id propId) {
        return (propIds == null) || (propIds.contains(propId));
    }

    public final boolean isApplicableForClass(final Id classId) {
        if (this.classId == null) {
            return true;
        }
        if (this.classId.equals(classId)) {
            return true;
        }
        if (classId != null) {
            final RadClassDef classDef = release.getClassDef(classId);
            if (classDef.getAncestorId() != null) {
                return isApplicableForClass(classDef.getAncestorId());
            }
        }
        return false;
    }
    
    public boolean isReadOnly(){
        return isReadOnly;
    }
    
    public boolean getUsrActionsIsTraced() {
        return traceGuiActivity;
    }
}
