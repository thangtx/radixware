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

package org.radixware.kernel.common.defs;

import java.util.Map;
import org.radixware.kernel.common.defs.Dependences.Dependence;
import org.radixware.kernel.common.types.Id;

/**
 * Dependence provider interface. Used by searcher engine for summary dependance
 * list configuring
 *
 */
public interface IDependenceProvider {

    /**
     * Adds all available dependences to parameter list
     */
    void collect(final Map<Id, Dependence> moduleId2Dependence);

    Map<Id, Dependence> get();
}
