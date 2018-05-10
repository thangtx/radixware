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

import java.util.Map;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.common.repository.DbConfiguration;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.types.Id;

public class ServerPreprocessorConfig implements ISqmlPreprocessorConfig {

    private final DbConfiguration dbConfiguration;
    private final Map<Id, Object> paramValues;

    /**
     * Construct preprocessor configuration which will preprocess SQML *ONLY*
     * based on database parameters, other preprocessor directives will be
     * ignored.
     */
    public ServerPreprocessorConfig(final DbConfiguration dbConfiguration) {
        this(dbConfiguration, null);
    }

    /**
     * Construct preprocessor configuration which will preprocess both database
     * and ordinary parameters
     */
    public ServerPreprocessorConfig(final DbConfiguration dbConfiguration, final Map<Id, Object> paramValues) {
        this.dbConfiguration = dbConfiguration;
        this.paramValues = paramValues;
    }

    @Override
    public PreprocessorParameter getParameter(final Id paramId) {
        if (paramValues == null || !paramValues.containsKey(paramId)) {
            return null;
        }
        final Object value = paramValues.get(paramId);
        return new PreprocessorParameter() {

            @Override
            public Object getValue() {
                return value;
            }

            @Override
            public Id getId() {
                return paramId;
            }

            @Override
            public String getName() {
                return null;
            }
        };
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
