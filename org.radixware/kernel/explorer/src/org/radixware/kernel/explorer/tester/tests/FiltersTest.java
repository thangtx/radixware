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
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.FiltersTestProvider;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;
import org.radixware.kernel.explorer.views.selector.Selector;


public class FiltersTest implements ITest {

    private boolean interrupted = false;
    private FiltersTestResult testResult;
    private Selector selector;

    public FiltersTest(Selector selector) {
        this.selector = selector;
        this.testResult = new FiltersTestResult(selector.getEnvironment(), selector);
        this.testResult.operation = TesterConstants.TEST_FILTERS.getTitle();
    }
    private List<FilterModel> forChildTests = new ArrayList<FilterModel>();

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        if (forChildTests.size() > 0) {
            return new FiltersTestProvider(parentProvider.getEnvironment(), parentProvider, forChildTests, selector);
        }
        return null;
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment env) {
        try {
            forChildTests.clear();
            GroupModel group = selector.getGroupModel();
            if (group != null) {
                if (group.isEmpty() || group.getEntitiesCount() == 0) {
                    testResult.result = TesterConstants.RESULT_EMPTY_SELECTOR.getTitle();
                } else {

                    if (interrupted) {
                        return;
                    }

                    Filters filters = group.getFilters();
                    final long limit = options.filtersCount;
                    int tested = 0;
                    final int size = filters.getPredefinedAddonsByOrder().size();

                    if (size > 0) {
                        if (limit == -1) {
                            for (FilterModel filter : filters.getPredefinedAddonsByOrder()) {
                                if (filter.canApply() && !filter.isUserDefined()) {
                                    //group.setFilter(filter);
                                    //testResult.addFilter(filter);
                                    forChildTests.add(filter);
                                    tested++;
                                }
                            }
                        } else {
                            int i = 0;
                            while (tested < limit && i < size) {
                                FilterModel filter = filters.getPredefinedAddonsByOrder().get(i);
                                if (filter.canApply() && !filter.isUserDefined()) {
//                                    group.setFilter(filter);
//                                    testResult.addFilter(filter);
                                    forChildTests.add(filter);
                                    tested++;
                                }
                                i++;
                            }
                        }
                        if (tested > 0) {
                            testResult.result = "";//TesterConstants.RESULT_FILTERS_SCS;
                        } else {
                            testResult.result = TesterConstants.RESULT_FILTERS_NO_APPLICABLE.getTitle();
                        }
                    } else {
                        testResult.result = TesterConstants.RESULT_FILTERS_NO_AVAILABLE.getTitle();
                    }
                }
            }
        } catch (Throwable ex) {
            TesterEngine.processThrowable(selector.getEnvironment(), ex, testResult);
        }
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }

    @Override
    public void interrupt() {
        interrupted = true;
        selector.getEnvironment().getEasSession().breakRequest();
    }
}
