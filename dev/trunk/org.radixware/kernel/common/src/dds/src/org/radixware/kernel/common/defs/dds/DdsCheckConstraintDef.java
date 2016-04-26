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

package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.Sqml;

/**
 * Метаинформация об ограничении на значение {@link DdsColumnDef колонки} в базе данных.
 */
public class DdsCheckConstraintDef extends DdsConstraintDef {

    protected DdsCheckConstraintDef() {
        super(EDefinitionIdPrefix.DDS_CHECK_CONSTRAINT);
    }

    protected DdsCheckConstraintDef(org.radixware.schemas.ddsdef.Column.CheckConstraint xCheckConstraint) {
        super(xCheckConstraint);
        condition.loadFrom(xCheckConstraint.getCondition());
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CONST;
    }

    @Override
    public String getName() {
        return "CheckConstraint";
    }

    public DdsColumnDef getOwnerColumn() {
        return (DdsColumnDef) getContainer();
    }

    protected void setOwnerColumn(DdsColumnDef ownerColumn) {
        setContainer(ownerColumn);
    }
    private final Sqml condition = new DdsSqml(this);

    /**
     * Получить SQL код ограничения на колонку.
     * Используется для генерации скрипта создания колонки в базе данных.
     * Используется базой данных для контроля корректности.
     */
    public Sqml getCondition() {
        return condition;
    }

    @Override
    public boolean isGeneratedInDb() {
        final DdsColumnDef ownerColumn = getOwnerColumn();
        return ownerColumn.isGeneratedInDb();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsCheckConstraintDef newInstance() {
            return new DdsCheckConstraintDef();
        }

        public static DdsCheckConstraintDef loadFrom(org.radixware.schemas.ddsdef.Column.CheckConstraint xCheckConstraint) {
            return new DdsCheckConstraintDef(xCheckConstraint);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        condition.visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.CHECK_CONSTRAINT;
    }

    @Override
    public String calcAutoDbName() {
        DdsColumnDef column = getOwnerColumn();
        DdsTableDef table = column.getOwnerTable();

        String columnDbName = column.getDbName();
        String tableDbName = table.getDbName();

        String autoDbName = "CKC_" + columnDbName + "_" + tableDbName;

        if (autoDbName.length() > 30) {
            if (columnDbName.length() <= 19) {
                autoDbName = DbNameUtils.calcAutoDbName("CKC", columnDbName, tableDbName);
            } else {
                autoDbName = DbNameUtils.calcAutoDbName("CKC", columnDbName.substring(0, 12), tableDbName);
            }
        }

        return autoDbName;
    }
}
