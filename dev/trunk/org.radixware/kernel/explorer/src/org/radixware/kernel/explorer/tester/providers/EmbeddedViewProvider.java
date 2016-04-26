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
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.ITestableModel;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.tests.CustomTest;
import org.radixware.kernel.explorer.tester.tests.ITest;
import org.radixware.kernel.explorer.tester.tests.OpenViewTest;
import org.radixware.kernel.explorer.tester.tests.PropDialogsSingleEntityTest;
import org.radixware.kernel.explorer.views.IExplorerView;


public class EmbeddedViewProvider extends TestsProvider {

    private IExplorerView view;
    private boolean openTestPassed = false;
    private boolean hasCustomTest = false;
    private boolean customTestPassed = false;
    private boolean needPropDialogsTest = false;
    private boolean propDialogsPassed = false;

    public EmbeddedViewProvider(final IClientEnvironment environment, final TestsProvider provider, final IExplorerView view) {
        super(environment, provider);
        this.view = view;
        this.hasCustomTest = view.getModel() != null && view.getModel() instanceof ITestableModel;
        this.needPropDialogsTest = view.getModel() != null && view.getModel() instanceof EntityModel;
    }

    @Override
    public ITest createNextTest(TestsOptions options) {
        if (!openTestPassed) {
            openTestPassed = true;
            return new OpenViewTest(getEnvironment(), view);
        }
        if (hasCustomTest && !customTestPassed) {
            customTestPassed = true;
            return new CustomTest(getEnvironment(), (ITestableModel) view.getModel());
        }
        if (needPropDialogsTest && !propDialogsPassed && options.testPropDialog) {
            propDialogsPassed = true;
            return new PropDialogsSingleEntityTest((EntityModel) view.getModel());
        }
        return null;
    }
}
