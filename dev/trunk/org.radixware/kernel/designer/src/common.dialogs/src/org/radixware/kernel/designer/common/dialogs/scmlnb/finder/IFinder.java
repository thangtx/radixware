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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;

/**
 * Finder for Scml.
 *
 */
public interface IFinder extends IAcceptor<RadixObject> {

    /**
     * @return List of all occurrences of what we are trying to find. This list
     * is supposed to be unmodifiable.
     */
    public List<IOccurrence> list(RadixObject radixObject);

    /**
     * @return true if this finder is able to search in specified radixObject
     * radixObject
     */
    @Override
    public boolean accept(RadixObject radixObject);
}
