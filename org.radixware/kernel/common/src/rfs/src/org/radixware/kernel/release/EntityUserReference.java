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

package org.radixware.kernel.release;

import java.util.Objects;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


public class EntityUserReference {
    private final Id entityId;
    private final Id userPropId;
    private final EValType type;
    private final EDeleteMode deleteMode;
    private final boolean confirmationRequired;

    public EntityUserReference(Id entityId, Id userPropId, EValType type, EDeleteMode deleteMode, final boolean confirmationRequired) {
        this.entityId = entityId;
        this.userPropId = userPropId;
        this.type = type;
        this.deleteMode = deleteMode;
        this.confirmationRequired = confirmationRequired;
    }

    public boolean isConfirmationRequired() {
        return confirmationRequired;
    }

    public EDeleteMode getDeleteMode() {
        return deleteMode;
    }

    public Id getEntityId() {
        return entityId;
    }

    public EValType getType() {
        return type;
    }

    public Id getUserPropId() {
        return userPropId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityUserReference other = (EntityUserReference) obj;
        if (!Objects.equals(this.entityId, other.entityId)) {
            return false;
        }
        if (!Objects.equals(this.userPropId, other.userPropId)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.deleteMode != other.deleteMode) {
            return false;
        }
        if (this.confirmationRequired != other.confirmationRequired) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.entityId);
        hash = 13 * hash + Objects.hashCode(this.userPropId);
        hash = 13 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 13 * hash + (this.deleteMode != null ? this.deleteMode.hashCode() : 0);
        hash = 13 * hash + (this.confirmationRequired ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "EntityUserReference[entityId: " + entityId + "; userPropId: " + userPropId + "; type: " + type + "; deleteMode: " + deleteMode + "; confirmation required: " + confirmationRequired + "]";
    }
    
}
