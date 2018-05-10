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

import java.util.EnumSet;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.utils.Utils;

public abstract class DdsConstraintDef extends DdsDefinition implements IDdsAutoDbNamedDefinition {

    public DdsConstraintDef(final EDefinitionIdPrefix idPrefix) {
        super(idPrefix, "");
    }

    public DdsConstraintDef(org.radixware.schemas.ddsdef.Constraint xConstraint) {
        super(xConstraint);

        this.dbName = xConstraint.getDbName();

        if (xConstraint.isSetAutoDbName()) {
            this.autoDbName = xConstraint.getAutoDbName();
        }

        if (xConstraint.isSetRely()) {
            final boolean rely = xConstraint.getRely();
            if (rely) {
                this.dbOptions.add(EDdsConstraintDbOption.RELY);
            }
        }

        if (xConstraint.isSetDisable()) {
            final boolean disable = xConstraint.getDisable();
            if (disable) {
                this.dbOptions.add(EDdsConstraintDbOption.DISABLE);
            }
        }

        if (xConstraint.isSetNovalidate()) {
            final boolean novalidate = xConstraint.getNovalidate();
            if (novalidate) {
                this.dbOptions.add(EDdsConstraintDbOption.NOVALIDATE);
            }
        }

        if (xConstraint.isSetInitiallyDeferred()) {
            final boolean initiallyDeferred = xConstraint.getInitiallyDeferred();
            if (initiallyDeferred) {
                this.dbOptions.add(EDdsConstraintDbOption.INITIALLY_DEFERRED);
            }
        }

        if (xConstraint.isSetDeferrable()) {
            final boolean deferrable = xConstraint.getDeferrable();
            if (deferrable) {
                this.dbOptions.add(EDdsConstraintDbOption.DEFERRABLE);
            }
        }
    }
    private String dbName = "";

    @Override
    public String getDbName() {
        return dbName;
    }

    /**
     * Set database name of the definition.
     */
    @Override
    public void setDbName(String dbName) {
        if (!Utils.equals(this.dbName, dbName)) {
            this.dbName = dbName;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    private boolean autoDbName = true;

    @Override
    public boolean isAutoDbName() {
        return autoDbName;
    }

    public void setAutoDbName(boolean autoDbName) {
        if (this.autoDbName != autoDbName) {
            this.autoDbName = autoDbName;
            setEditState(EEditState.MODIFIED);
        }
    }
    private final EnumSet<EDdsConstraintDbOption> dbOptions = EnumSet.noneOf(EDdsConstraintDbOption.class);

    /**
     * Получить опции в базе данных.
     */
    public EnumSet<EDdsConstraintDbOption> getDbOptions() {
        return dbOptions;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.toString());
        result.append("; DbName: ");
        result.append(dbName);
        result.append("; DbOptions: ");
        result.append(dbOptions);

        return result.toString();
    }
}
