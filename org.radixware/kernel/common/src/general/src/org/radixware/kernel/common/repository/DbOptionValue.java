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

import org.radixware.kernel.common.enums.EOptionMode;

/**
 *
 * @author dsafonov
 */
public class DbOptionValue {

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
    public String toString() {
        return "DbOptionValue{" + "optionName=" + optionName + ", mode=" + mode + '}';
    }

}
