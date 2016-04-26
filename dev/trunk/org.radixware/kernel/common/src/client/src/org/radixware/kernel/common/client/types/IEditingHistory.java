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

package org.radixware.kernel.common.client.types;

import java.util.List;


public interface IEditingHistory {

    void addEntry(final String entry);

    /**
     * Clears history for current setting
     */
    void clear();

    void flush() throws EditingHistoryException;

    String getEntry(final int row);

    int getSize();
    
    List<String> getEntries();
    
    boolean isEmpty();
}
