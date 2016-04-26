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

package org.radixware.kernel.explorer.tester.providers;

import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.tests.ITest;
import org.radixware.kernel.explorer.tester.tests.CreationDialogTest;
import org.radixware.kernel.explorer.views.selector.Selector;


public class CreationSelectDialogTestProvider extends TestsProvider {

    private Selector selector;
    private Map<Id, String> classId2Titles;

    public CreationSelectDialogTestProvider(IClientEnvironment environment, TestsProvider parent, Selector selector, Map<Id, String> classId2Titles) {
        super(environment, parent);
        this.selector = selector;
        this.classId2Titles = classId2Titles;
    }
    private int lastTested = -1;

    @Override
    public ITest createNextTest(TestsOptions options) {
        if (selector != null && classId2Titles != null && !classId2Titles.isEmpty()) {
            lastTested++;
            if (lastTested < classId2Titles.size()) {
                Id currentId = (Id) classId2Titles.keySet().toArray()[lastTested];
                return new CreationDialogTest(selector, currentId, classId2Titles.get(currentId));
            }
        }
        return null;
    }
}
