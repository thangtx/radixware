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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.tests.FilterSingleTest;
import org.radixware.kernel.explorer.tester.tests.ITest;
import org.radixware.kernel.explorer.views.selector.Selector;


public class FiltersTestProvider extends TestsProvider {

    private List<FilterModel> filters;
    private Selector selector;

    public FiltersTestProvider(IClientEnvironment environment, TestsProvider parent, List<FilterModel> filters, Selector selector) {
        super(environment, parent);
        this.filters = filters;
        this.selector = selector;
    }
    private int lastTested = -1;
    private Set<Id> testedFilters = new HashSet<Id>();

    @Override
    public ITest createNextTest(TestsOptions options) {
        if (filters != null && !filters.isEmpty()) {
            if (options.filtersCount != -1) {
                int testedCount = testedFilters.size();
                if (testedCount == options.filtersCount) {
                    return null;
                }
            }
            lastTested++;
            if (lastTested < filters.size()) {
                FilterModel item = filters.get(lastTested);
                testedFilters.add(item.getId());
                return new FilterSingleTest(item, selector);
            }
        }
        return null;
    }
}
