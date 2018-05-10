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
package org.radixware.kernel.common.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.enums.EOptionMode;

public class DbConfiguration {

    static final String DB_LAYER_URI = "layeruri";
    static final String DB_NAME = "name";
    static final String DB_VALUE = "value";
    private static final String DB_SQL = "select " + DB_LAYER_URI + ", " + DB_NAME + ", " + DB_VALUE + " from rdx_ddsoption";

    private final BigDecimal targetDbVersion;
    private final EDatabaseType targetDbType;
    private final List<DbOptionValue> options;

    @Deprecated
    public DbConfiguration(final String targetDbType, final BigDecimal targetDbVersion, List<DbOptionValue> enabledOptions) {
        this(Layer.TargetDatabase.string2DatabaseType(targetDbType), targetDbVersion, enabledOptions);
    }

    public DbConfiguration(final EDatabaseType targetDbType, final BigDecimal targetDbVersion, List<DbOptionValue> enabledOptions) {
        if (targetDbType == null) {
            throw new IllegalArgumentException("Database type can't be null");
        } else if (targetDbVersion == null) {
            throw new IllegalArgumentException("Database version can't be null");
        } else {
            this.targetDbType = targetDbType;
            this.targetDbVersion = targetDbVersion;
            this.options = Collections.unmodifiableList(new ArrayList<>(enabledOptions == null ? Collections.<DbOptionValue>emptyList() : enabledOptions));
        }
    }

    public List<DbOptionValue> getOptions() {
        return options;
    }

    public BigDecimal getTargetDbVersion() {
        return targetDbVersion;
    }

    public String getTargetDbType() {
        return targetDbType.toString();
    }

    public EDatabaseType getTargetDb() {
        return targetDbType;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.targetDbVersion);
        hash = 59 * hash + Objects.hashCode(this.targetDbType);
        hash = 59 * hash + Objects.hashCode(this.options);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DbConfiguration other = (DbConfiguration) obj;
        if (!Objects.equals(this.targetDbVersion, other.targetDbVersion)) {
            return false;
        }
        if (!Objects.equals(this.targetDbType, other.targetDbType)) {
            return false;
        }
        if (!Objects.equals(this.options, other.options)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DbConfiguration{" + "targetDbVersion=" + targetDbVersion + ", targetDbType=" + targetDbType + ", enabledOptions=" + options + '}';
    }

    public static DbConfiguration read(final Connection connection) throws SQLException {
        if (connection == null) {
            throw new IllegalArgumentException("Database connection can't be null");
        } else {
            try (final PreparedStatement qryReadDbConfiguration = connection.prepareStatement(DB_SQL)) {
                try (final ResultSet rs = qryReadDbConfiguration.executeQuery()) {
                    boolean recordsWereFound = false;
                    EDatabaseType targetDbType = null;
                    BigDecimal targetDbVersion = null;
                    final List<DbOptionValue> options = new ArrayList<>();

                    while (rs.next()) {
                        final String optLayer = rs.getString(DB_LAYER_URI);
                        final String optName = rs.getString(DB_NAME);
                        final String optVal = rs.getString(DB_VALUE);
                        boolean special = false;

                        if (Layer.ORG_RADIXWARE_LAYER_URI.equals(optLayer)) {
                            if (Layer.DatabaseOption.TARGET_DB_TYPE.equals(optName)) {
                                try {
                                    targetDbType = EDatabaseType.valueOf(optVal.trim().toUpperCase());
                                } catch (IllegalArgumentException ex) {
                                    targetDbType = EDatabaseType.ORACLE;
                                }
                                special = true;
                            } else if (Layer.DatabaseOption.TARGET_DB_VERSION.equals(optName)) {
                                try {
                                    targetDbVersion = new BigDecimal(optVal);
                                } catch (NumberFormatException ex) {
                                    targetDbVersion = BigDecimal.ZERO;
                                }
                                special = true;
                            }
                        }
                        if (!special) {
                            EOptionMode mode = EOptionMode.ENABLED.name().equals(optVal) ? EOptionMode.ENABLED : EOptionMode.DISABLED;
                            options.add(new DbOptionValue(Layer.DatabaseOption.getQualifiedOptionName(optLayer, optName), mode));
                        }
                        recordsWereFound = true;
                    }

                    if (recordsWereFound) {
                        return new DbConfiguration(targetDbType, targetDbVersion, options);
                    } else {
                        throw new SQLException("Database table [rdx_ddsoption] does not contain any valid data (possibly errors on database installation stage by RadixWare Manager)");
                    }
                }
            }
        }
    }
}
