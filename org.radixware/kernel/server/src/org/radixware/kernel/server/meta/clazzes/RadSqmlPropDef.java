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

package org.radixware.kernel.server.meta.clazzes;

import org.apache.xmlbeans.XmlException;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EPropInitializationPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.schemas.xscml.SqmlDocument;

public final class RadSqmlPropDef extends RadPropDef {

    private final Sqml expression;
    private final String dbType;
    private final boolean isVisibleForArte;

    public RadSqmlPropDef(
            final Id id,
            final String name,
            final Id titleId,
            final EValType valType,
            final String dbType,
            final Id constSetId,
            final boolean isValInheritable,
            final ValAsStr valInheritMarkValAsStr,
            final ValInheritancePath[] valInheritPathes,
            final EPropInitializationPolicy initPolicy, final RadixDefaultValue initVal,
            final String sExpression,
            final boolean isVisibleForArte,
            final IRadPropAccessor accessor) {
        super(id, name, titleId, valType, constSetId, isValInheritable, valInheritMarkValAsStr, valInheritPathes, initPolicy, initVal, accessor);
        this.dbType = dbType == null ? valType.getDefaultDbType() : dbType;
        if (sExpression == null) {
            throw new WrongFormatError("Expression isn't defined for SQML column \"" + name + "\", #" + id, null);
        }
        try {
            final SqmlDocument xExpression = SqmlDocument.Factory.parse(sExpression);
            expression = xExpression != null ? Sqml.Factory.loadFrom("SqmlPropExpr", xExpression.getSqml()) : null;
        } catch (XmlException e) {
            throw new WrongFormatError("Can't parse \"" + name + "\" (#" + id + ") propertie's  expression: " + ExceptionTextFormatter.getExceptionMess(e), e);
        }
        if (expression != null) {
            expression.switchOnWriteProtection();
        }
        this.isVisibleForArte = isVisibleForArte;
    }

    /**
     * @return the expression
     */
    @Override
    public Sqml getExpression() {
        return expression;
    }

    @Override
    public String getDbName() {
        return null;
    }

    @Override
    public String getDbType() {
        return dbType;
    }

    @Override
    public boolean isGeneratedInDb() {
        return false;
    }

    @Override
    public boolean isVisibleForArte() {
        return isVisibleForArte;
    }
}
