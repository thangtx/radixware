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

package org.radixware.kernel.server.dbq.sqml;

import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.common.repository.DbConfiguration;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.repository.Layer;


public class PreprocessorConfigByDbConfiguration implements ISqmlPreprocessorConfig {

    private final DbConfiguration dbConfiguration;

    public PreprocessorConfigByDbConfiguration(DbConfiguration dbConfiguration) {
        this.dbConfiguration = dbConfiguration;
    }

    @Override
    public Sqml copy(Sqml sqml) {
        if (sqml == null) {
            return null;
        }
        return org.radixware.kernel.server.sqml.Sqml.Factory.loadFrom("", sqml);
    }

    @Override
    public Object getProperty(String propertyName) {
        if (dbConfiguration != null) {
            for (DbOptionValue opt : dbConfiguration.getOptions()) {
                if (opt != null && opt.getOptionName().equals(propertyName) && opt.getMode() == EOptionMode.ENABLED) {
                    return Boolean.TRUE;
                }
            }
        }
        return null;
    }

    @Override
    public String getDbTypeName() {
        return dbConfiguration == null ? null : dbConfiguration.getTargetDbType();
    }

    @Override
    public String getDbVersion() {
        return dbConfiguration == null ? null : dbConfiguration.getTargetDbVersion().toString();
    }

    @Override
    public boolean alwaysCreateCopy() {
        return false;
    }
}
