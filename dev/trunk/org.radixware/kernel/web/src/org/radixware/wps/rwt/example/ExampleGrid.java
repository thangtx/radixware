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

package org.radixware.wps.rwt.example;

import org.radixware.wps.rwt.Grid;
import org.radixware.wps.rwt.UIObject.SizePolicy;


public class ExampleGrid extends Grid {

    public ExampleGrid() {
        addColumn("One");
        addColumn("Two");
        addColumn("Three");
        addColumn("Four");
        addColumn("Five");
        addColumn("Six");
        addColumn("Seven");
        addColumn("Eight");
        addColumn("Nine");
        addColumn("Ten");
        addColumn("Eleven");
        addColumn("Twelve");
        for (int i = 0; i < 50; i++) {
            Row r = addRow();
            for (int j = 0; j < 12; j++) {
                r.getCell(j).setValue(String.valueOf(i * j));
            }
        }
        setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
    }
}
