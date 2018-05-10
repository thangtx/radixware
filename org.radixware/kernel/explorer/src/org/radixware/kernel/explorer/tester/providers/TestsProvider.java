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
import org.radixware.kernel.explorer.tester.tests.ITest;


public abstract class TestsProvider {

    private final TestsProvider parent;
    private final IClientEnvironment environment;

    public TestsProvider(IClientEnvironment environment, TestsProvider parentProvider) {
        parent = parentProvider;
        this.environment = environment;

    }

    public abstract ITest createNextTest(TestsOptions options);

    public TestsProvider getParentProvider() {
        return parent;
    }
    
    public void close(){        
    }

    public IClientEnvironment getEnvironment() {
        return environment;
    }
}
