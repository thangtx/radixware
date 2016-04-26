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

import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * Tag - parameter transfer.
 * Translated in '?' (with transfer of parameter in run time).
 * Available in SQL classes, filters.
 */
public class ParameterTag extends ParameterAbstractTag {

    protected ParameterTag() {
        super();
    }

    public static final class Factory {

        private Factory() {
        }

        public static ParameterTag newInstance() {
            return new ParameterTag();
        }
    }
    private boolean literal = false;

    public boolean isLiteral() {
        return literal;
    }

    public void setLiteral(boolean literal) {
        if (this.literal != literal) {
            this.literal = literal;
            setEditState(EEditState.MODIFIED);
        }
    }
    private EParamDirection direction = EParamDirection.IN;

    public EParamDirection getDirection() {
        return direction;
    }

    public void setDirection(final EParamDirection direction) {
        if (!Utils.equals(this.direction, direction)) {
            this.direction = direction;
            setEditState(EEditState.MODIFIED);
        }
    }
    private Id propId = null;

    /**
     * Get PkParameter property GUID.
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
}
