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

package org.radixware.kernel.designer.common.general.editors;

import org.radixware.kernel.common.defs.RadixObject;

/**
 * Base interface for Radix object editors.
 * All editors are registered in layer.xml files.
 */
public interface IRadixObjectEditor<T extends RadixObject> {

    /**
     * Update editor content and select object that specified by openInfo.getTarget().
     * @return deprecated
     */
    public boolean open(OpenInfo openInfo);

    /**
     * Update editor, called each time when focused.
     */
    public void update();

    public boolean isOpeningAfterNewObjectCreationRequired();
}
