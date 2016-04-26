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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.enums.EOptionMode;


public class DbConfiguration {

    private final BigDecimal targetDbVersion;
    private final String targetDbType;
    private final List<DbOptionValue> options;

    public DbConfiguration(final String targetDbType, final BigDecimal targetDbVersion, List<DbOptionValue> enabledOptions) {
        this.targetDbType = targetDbType;
        this.targetDbVersion = targetDbVersion;
        this.options = Collections.unmodifiableList(new ArrayList<>(enabledOptions == null ? Collections.<DbOptionValue>emptyList() : enabledOptions));
    }

    public List<DbOptionValue> getOptions() {
        return options;
    }

    public BigDecimal getTargetDbVersion() {
        return targetDbVersion;
    }

    public String getTargetDbType() {
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
        try (PreparedStatement qryReadDbConfiguration = connection.prepareStatement("select layeruri, name, value from rdx_ddsoption")) {
            try (ResultSet rs = qryReadDbConfiguration.executeQuery()) {
                String targetDbType = null;
                BigDecimal targetDbVersion = null;
                final List<DbOptionValue> options = new ArrayList<>();
                while (rs.next()) {
                    final String optLayer = rs.getString("layeruri");
                    final String optName = rs.getString("name");
                    final String optVal = rs.getString("value");
                    boolean special = false;
                    if (Layer.ORG_RADIXWARE_LAYER_URI.equals(optLayer)) {
                        if (Layer.DatabaseOption.TARGET_DB_TYPE.equals(optName)) {
                            targetDbType = optVal;
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
                }
                return new DbConfiguration(targetDbType, targetDbVersion, options);
            }
        }
    }
}
