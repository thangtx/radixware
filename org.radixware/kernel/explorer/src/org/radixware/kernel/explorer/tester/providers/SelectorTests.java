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

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.tests.CreationDialogTest;
import org.radixware.kernel.explorer.tester.tests.FiltersTest;
import org.radixware.kernel.explorer.tester.tests.ITest;
import org.radixware.kernel.explorer.tester.tests.InsertionTest;
import org.radixware.kernel.explorer.views.selector.Selector;


public class SelectorTests extends TestsProvider {

    private final Selector selector;
    private boolean insertionsTest = true, filtersTest = true, creationDialogTest = true;

    public SelectorTests(IClientEnvironment environment, final TestsProvider parent, final Selector selector) {
        super(environment, parent);
        this.selector = selector;
    }

    @Override
    public ITest createNextTest(TestsOptions options) {
        if (insertionsTest && options.testInserts) {
            insertionsTest = false;
            return new InsertionTest(selector);
        }
        if (filtersTest && options.testFilters) {
            filtersTest = false;
            return new FiltersTest(selector);
        }
        if (creationDialogTest && options.testCreationDialog) {
            creationDialogTest = false;
            return new CreationDialogTest(selector, null, "");
        }
        return null;
    }
}
