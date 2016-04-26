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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.items.properties.Property;


public class PropDialogsSingleEntityTestResult extends TestResult {

    private List<Property> properties;
    private List<String> results;
    private List<String> times;

    public PropDialogsSingleEntityTestResult(EntityModel model) {
        super(model.getEnvironment(), model);
        this.properties = new ArrayList<Property>();
        this.results = new ArrayList<String>();
        this.times = new ArrayList<String>();
    }

    public int getTestedPropertiesCount() {
        return this.properties.size();
    }

    public void addTestResult(Property property, String result, String timeResult) {
        if (property != null) {
            this.properties.add(property);
            this.results.add(result);
            this.times.add(timeResult);
        }
    }

    public Property getProperty(int index) {
        return this.properties.get(index);
    }

    public String getResult(int index) {
        return this.results.get(index);
    }

    public String getTimeStr(int index) {
        return this.times.get(index);
    }
}
