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

package org.radixware.kernel.server.instance;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class LayerDbConfiguration {

    private final String layerUri;
    private final String targetDbType;
    private final double version;
    private final Set<String> options;

    public LayerDbConfiguration(String layerUri, String targetDbType, double version, Set<String> options) {
        this.layerUri = layerUri;
        this.version = version;
        this.targetDbType = targetDbType;
        this.options = Collections.unmodifiableSet(new HashSet<>(options == null ? Collections.<String>emptySet() : options));
    }

    public String getLayerUri() {
        return layerUri;
    }

    public String getTargetDbTypeName() {
        return targetDbType;
    }

    public double getTargetDbVersion() {
        return version;
    }

    public Set<String> getDbOptions() {
        return options;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.layerUri);
        hash = 23 * hash + Objects.hashCode(this.targetDbType);
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.version) ^ (Double.doubleToLongBits(this.version) >>> 32));
        hash = 23 * hash + Objects.hashCode(this.options);
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
        final LayerDbConfiguration other = (LayerDbConfiguration) obj;
        if (!Objects.equals(this.layerUri, other.layerUri)) {
            return false;
        }
        if (!Objects.equals(this.targetDbType, other.targetDbType)) {
            return false;
        }
        if (Double.doubleToLongBits(this.version) != Double.doubleToLongBits(other.version)) {
            return false;
        }
        if (!Objects.equals(this.options, other.options)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LayerDbConfiguration{" + "layerUri=" + layerUri + ", targetDbType=" + targetDbType + ", version=" + version + ", options=" + options + '}';
    }
}
