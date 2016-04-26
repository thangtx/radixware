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

package org.radixware.kernel.explorer.tester;


public class TestsOptions {

    public long inserts = 0;
    public long filtersCount = 0;
    public long pagesCount = 0;
    public boolean testFilters = false;
    public boolean testInserts = false;
    public boolean testPages = false;
    public boolean testPropDialog = false;
    public boolean testCreationDialog = false;
    public long insertsTimeBoundary = 1000;
    public long openingTimeBoundary = 1000;
    public long closingTimeBoundary = 1000;
    public long filtersTimeBoundary = 1000;
    public long pagesTimeBoundary = 1000;
    public long propDialogTimeBoundary = 1000;
    public long creationDialogTimeBoundary = 1000;
}
