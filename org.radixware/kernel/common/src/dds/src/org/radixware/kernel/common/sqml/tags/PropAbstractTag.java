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

package org.radixware.kernel.common.sqml.tags;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Tag that used some class property or table column.
 */
public abstract class PropAbstractTag extends Sqml.Tag {

    protected PropAbstractTag() {
        super();
    }
    private Id propOwnerId = null;

    /**
     * Get property owner GUID (DdsTableDef or AdsClassDef).
     */
    public Id getPropOwnerId() {
        return propOwnerId;
    }

    public void setPropOwnerId(Id propOwnerId) {
        if (!Utils.equals(this.propOwnerId, propOwnerId)) {
            this.propOwnerId = propOwnerId;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id propId = null;

    /**
     * Get property GUID.
     */
    public Id getPropId() {
        return propId;
    }

    public void setPropId(Id propId) {
        if (!Utils.equals(this.propId, propId)) {
            this.propId = propId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find property.
     * @return property or null if not found.
     */
    public ISqmlProperty findProperty() {
        // overrided in PropSqlNameTag.
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findPropertyById(propOwnerId, propId);
        } else {
            return null;
        }
    }

    /**
     * Find property.
     * @throw DefinitionNotFoundError if not found.
     */
    public ISqmlProperty getProperty() {
        final ISqmlProperty property = findProperty();
        if (property == null) {
            throw new DefinitionNotFoundError(propId);
        }
        return property;
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        final ISqmlProperty property = findProperty();
        if (property != null) {
            final Definition definition = property.getDefinition();
            if (definition != null) {
                list.add(definition);
            }
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        sb.append("<br>Property Id: " + String.valueOf(getPropId()));
    }
}
