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

package org.radixware.kernel.common.utils.agents;

import javax.swing.event.ChangeListener;


public interface IObjectAgent<T> {

    T getObject();

    /**
     * Trying to get the actual object and synchronize it with the temporary
     * object, if need.
     *
     * @return true if synchronization successful, false otherwise
     */
    boolean invite(boolean force);

    boolean isActual();

    void addActualizeListener(ChangeListener listener);

    void removeActualizeListener(ChangeListener listener);
}
