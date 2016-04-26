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

package org.radixware.kernel.explorer.tester.tests;

import com.trolltech.qt.gui.QIcon;
import org.radixware.kernel.common.client.models.FilterModel;

import org.radixware.kernel.explorer.tester.TesterConstants;


public class FilterSingleTestResult extends TestResult {

    public FilterSingleTestResult(FilterModel obj) {
        super(obj.getEnvironment(), obj);
        this.icon = (QIcon) obj.getIcon();
        this.title = obj.getTitle();
        this.type = TesterConstants.OBJ_FILTER.getTitle();
        this.path = "";
    }
}
