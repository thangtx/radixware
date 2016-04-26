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

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.ITestableModel;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.tests.CloseViewTest;
import org.radixware.kernel.explorer.tester.tests.CustomTest;
import org.radixware.kernel.explorer.tester.tests.ITest;
import org.radixware.kernel.explorer.tester.tests.OpenViewTest;
import org.radixware.kernel.explorer.tester.tests.PropDialogsSingleEntityTest;


public class RestrictedForInsertionsEditorsTests extends TestsProvider {

    private List<EntityModel> restricted;

    public RestrictedForInsertionsEditorsTests(IClientEnvironment environment, TestsProvider parentProvider, List<EntityModel> restricted) {
        super(environment, parentProvider);
        this.restricted = restricted;
    }
    int lastUsed = -1;
    boolean openTest = false;
    boolean customTest = false;
    boolean propDiaologsTest = false;

    @Override
    public ITest createNextTest(TestsOptions options) {
        if (restricted != null && !restricted.isEmpty()) {
            lastUsed++;
            if (!openTest && lastUsed < restricted.size()) {
                final int index = lastUsed;
                lastUsed--;
                openTest = true;
                customTest = !(restricted.get(index) instanceof ITestableModel);
                return new OpenViewTest(restricted.get(index));
            } else if (lastUsed < restricted.size() && !customTest) {
                customTest = true;
                final int index = lastUsed;
                lastUsed--;
                return new CustomTest(getEnvironment(), (ITestableModel) restricted.get(index));
            } else if (lastUsed < restricted.size() && !propDiaologsTest && options.testPropDialog) {
                propDiaologsTest = true;
                final int index = lastUsed;
                lastUsed--;
                return new PropDialogsSingleEntityTest(restricted.get(index));
            } else if (lastUsed < restricted.size() && customTest) {
                openTest = false;
                customTest = false;
                return new CloseViewTest(restricted.get(lastUsed));
            }
        }
        return null;
    }
}
