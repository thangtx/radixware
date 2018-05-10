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

package org.radixware.kernel.common.builder.check.dds;

import javax.swing.JOptionPane;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.utils.DbTypeUtils;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Layer;


@RadixObjectCheckerRegistration
public class DdsColumnChecker<T extends DdsColumnDef> extends DdsColumnTemplateChecker<T> {

    public DdsColumnChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsColumnDef.class;
    }

    @Override
    public void check(T column, IProblemHandler problemHandler) {
        super.check(column, problemHandler);

        final String    dbName = column.getDbName();
        
        if (!DbNameUtils.isCorrectDbName(dbName)) {
            warning(column, problemHandler, "Column database name illegal ("+dbName+") : #" + String.valueOf(column.getDbName()));
        }
        for (Layer.TargetDatabase item : column.getLayer().getTargetDatabases()) {
            if (!DdsColumnDef.testCanUseAsDbName(dbName,item.getDatabaseType())) {
                warning(column, problemHandler, "Column database name ("+dbName+") intersects with ["+item.getDatabaseType()+"] reserved SQL keywords: #" + String.valueOf(column.getDbName()));
            }
        }
        
        if (column.getTemplateId() != null) {
            DdsColumnTemplateDef columnTemplate = column.findTemplate();
            if (columnTemplate != null) {
                if (!Utils.equals(columnTemplate.getValType(), column.getValType())) {
                    warning(column, problemHandler, "Column value type doesn't match to template one. Reopen it in editor.");
                }
                if (!DbTypeUtils.isDbTypeEquals(columnTemplate.getDbType(), column.getDbType())) {
                    warning(column, problemHandler, "Column database type doesn't match to template one. Reopen it in editor.");
                }
            } else {
                error(column, problemHandler, "Column template not found: #" + String.valueOf(column.getTemplateId()));
            }
        }

        for (DdsColumnDef anotherColumn : column.getOwnerTable().getColumns().get(EScope.LOCAL_AND_OVERWRITE)) {
            if (anotherColumn == column) {
                break;
            }
            if (column.isGeneratedInDb() && anotherColumn.isGeneratedInDb() && Utils.equals(anotherColumn.getDbName(), column.getDbName())) {
                error(column, problemHandler, "Duplicated column database name: '" + String.valueOf(column.getDbName()));
                break;
            }
        }

        if (column.getSequenceId() != null) {
            DdsSequenceDef sequence = column.findSequence();
            if (sequence == null) {
                error(column, problemHandler, "Sequence not found: #" + String.valueOf(column.getSequenceId()));
            }
            if (column.getValType() != EValType.INT) {
                error(column, problemHandler, "Sequence defined for " + column.getValType().getName());
            }
        }

        if (column.getExpression() != null && column.getInitialValues().size() > 0) {
            error(column, problemHandler, "Expression column can't have initial values");
        }

        if (column.isNotNull()) {
            for (ValAsStr valAsStr : column.getInitialValues()) {
                if (valAsStr == null) {
                    error(column, problemHandler, "Not null column contains null initial value.");
                    break;
                }
            }
        }

        if (column.getAuditInfo().isOnUpdate()) {
            final String dbType = column.getDbType().toUpperCase();
            if ("BLOB".equals(dbType) || "CLOB".equals(dbType) || column.getValType() == EValType.NATIVE_DB_TYPE) {
                error(column, problemHandler, "Audit for " + dbType);
            }
        }
    }
}
