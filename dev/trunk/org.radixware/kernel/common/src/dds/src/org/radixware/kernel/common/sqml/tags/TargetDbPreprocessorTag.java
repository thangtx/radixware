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
import org.radixware.kernel.common.enums.EComparisonOperator;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.sqml.Sqml;


public class TargetDbPreprocessorTag extends Sqml.Tag {

    public static final class Factory {

        private Factory() {
        }

        public static TargetDbPreprocessorTag newInstance() {
            return new TargetDbPreprocessorTag();
        }
    }
    private String dbTypeName;
    private String version;
    private EComparisonOperator versionOperator;
    private boolean checkVersion;
    private List<DbOptionValue> options;
    private boolean checkOptions;

    protected TargetDbPreprocessorTag() {
    }

    public String getDbTypeName() {
        return dbTypeName;
    }

    public void setDbTypeName(String dbTypeName) {
        this.dbTypeName = dbTypeName;
    }

    public String getDbVersion() {
        return version;
    }

    public void setDbVersion(String version) {
        this.version = version;
    }

    public EComparisonOperator getVersionOperator() {
        return versionOperator;
    }

    public void setVersionOperator(EComparisonOperator versionOperator) {
        this.versionOperator = versionOperator;
    }

    public boolean isCheckVersion() {
        return checkVersion;
    }

    public void setCheckVersion(boolean checkVersion) {
        this.checkVersion = checkVersion;
    }

    public List<DbOptionValue> getDbOptions() {
        return options;
    }

    public boolean isCheckOptions() {
        return checkOptions;
    }

    public void setCheckOptions(boolean checkOptions) {
        this.checkOptions = checkOptions;
    }

    public void setOptions(List<DbOptionValue> options) {
        this.options = options;
    }
}
