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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.defs.IDbType;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public abstract class DbPropertyValue extends PropertyValue implements IDbType {

    private static final int DEFAULT_LEN = 9;
    private static final int DEFAULT_PRECISION = 0;
    private int length = DEFAULT_LEN;
    private int precision = DEFAULT_PRECISION;

    public DbPropertyValue(AdsPropertyDef context, AbstractPropertyDefinition xDef) {
        super(context, xDef);
        if (xDef.isSetDbTypeLength()) {
            length = xDef.getDbTypeLength();
        }
        if (xDef.isSetDbTypePrecision()) {
            precision = xDef.getDbTypePrecision();
        }
    }

    public DbPropertyValue(AdsPropertyDef context, PropertyValue source) {
        super(context, source);
        if (source instanceof DbPropertyValue) {
            DbPropertyValue dbs = (DbPropertyValue) source;
            this.length = dbs.length;
            this.precision = dbs.precision;
        }
    }

    public DbPropertyValue(AdsPropertyDef context) {
        super(context);
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        if (this.length != length) {
            this.length = length;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public int getPrecision() {
        return precision;
    }

    @Override
    public void setPrecision(int precision) {
        if (this.precision != precision) {
            this.precision = precision;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public String calcAutoDbType() {
        EValType valType = getType().getTypeId();
        if (valType == null) {
            return "";
        }
        return DbTypeUtils.calcAutoDbType(valType, length, precision, null, null);
    }

    @Override
    public String getDbType() {
        return calcAutoDbType();
    }

    @Override
    protected void appendTo(AbstractPropertyDefinition xDef) {
        super.appendTo(xDef);
        if (length != DEFAULT_LEN) {
            xDef.setDbTypeLength(length);
        }
        if (precision != DEFAULT_PRECISION) {
            xDef.setDbTypePrecision(precision);
        }
    }

    @Override
    public EValType getValType() {
        return getType().getTypeId();
    }
}
