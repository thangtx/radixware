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

package org.radixware.kernel.designer.common.dialogs.utils;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.repository.Layer;



public class NameAcceptorFactory {

    public static IAdvancedAcceptor<String> newAcceptorForRename(RadixObject radixObject) {
        return new RenameAcceptor(radixObject);
    }

    public static IAdvancedAcceptor<String> newCreateAcceptor(RadixObjects container, EDefType defType) {
        return new CreateAcceptor(container, null);
    }

    public static IAdvancedAcceptor<String> newDbNameAcceptor(Layer layer) {
        return new DbNameAcceptor(layer);
    }
}
