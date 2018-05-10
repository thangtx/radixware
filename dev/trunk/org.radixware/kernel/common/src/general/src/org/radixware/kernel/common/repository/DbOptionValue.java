/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.repository;

import java.io.Serializable;
import java.util.Objects;
import org.radixware.kernel.common.enums.EOptionMode;

/**
 *
 * @author dsafonov
 */
public class DbOptionValue implements Serializable{

    private final String optionName;
    private final EOptionMode mode;

    public DbOptionValue(String optionName, EOptionMode mode) {
        this.optionName = optionName;
        this.mode = mode;
    }

    public String getOptionName() {
        return optionName;
    }

    public EOptionMode getMode() {
        return mode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.optionName);
        hash = 79 * hash + Objects.hashCode(this.mode);
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
        final DbOptionValue other = (DbOptionValue) obj;
        if (!Objects.equals(this.optionName, other.optionName)) {
            return false;
        }
        if (this.mode != other.mode) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DbOptionValue{" + "optionName=" + optionName + ", mode=" + mode + '}';
    }

}
