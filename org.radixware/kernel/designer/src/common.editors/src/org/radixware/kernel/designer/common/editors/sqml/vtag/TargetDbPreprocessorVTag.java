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

package org.radixware.kernel.designer.common.editors.sqml.vtag;

import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqlscript.parser.SQLPreprocessor;
import org.radixware.kernel.common.sqml.tags.TargetDbPreprocessorTag;


public class TargetDbPreprocessorVTag extends SqmlVTag<TargetDbPreprocessorTag> {

    public TargetDbPreprocessorVTag(TargetDbPreprocessorTag tag) {
        super(tag);
    }

    @Override
    protected void printTitle(CodePrinter cp) {
        cp.print("#IF " + Layer.DatabaseOption.TARGET_DB_TYPE + " == \"");
        cp.print(getTag().getDbTypeName());
        cp.print("\"");
        if (getTag().isCheckVersion()) {
            cp.print(" AND " + Layer.DatabaseOption.TARGET_DB_VERSION + " ");
            cp.print(getTag().getVersionOperator().getValue());
            cp.print(" ");
            cp.print(getTag().getDbVersion());
        }
        if (getTag().isCheckOptions()) {
            if (getTag().getDbOptions() != null) {
                for (DbOptionValue dbOpt : getTag().getDbOptions()) {
                    cp.print(" AND ");
                    if (dbOpt.getMode() == EOptionMode.DISABLED) {
                        cp.print("!");
                    }
                    cp.print(SQLPreprocessor.FUNC_IS_ENABLED);
                    cp.print("(\"");
                    cp.print(dbOpt.getOptionName());
                    cp.print("\")");
                }
            }
        }
        cp.print(" THEN");
    }

    @Override
    public String getTokenName() {
        return "target-db-preprocessor-tag";
    }
}
