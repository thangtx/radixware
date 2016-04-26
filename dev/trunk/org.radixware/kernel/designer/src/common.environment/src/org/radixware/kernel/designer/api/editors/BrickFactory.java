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

package org.radixware.kernel.designer.api.editors;

import java.awt.GridBagConstraints;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.api.editors.components.HierarchyBrick;
import org.radixware.kernel.designer.api.editors.components.MembersBrick;
import org.radixware.kernel.designer.api.editors.components.OverviewBrick;


public class BrickFactory {

    public static BrickFactory getDefault() {
        return new BrickFactory();
    }
    public static final int OVERVIEW = 100;
    public static final int HIERARCHY = 1000;
    public static final int MEMBERS = 1100;
    //

    public BrickFactory() {
    }

    public Brick<? extends RadixObject> create(int key, RadixObject object, GridBagConstraints constraints) {
        switch (key) {
            case OVERVIEW:
                return new OverviewBrick(object, constraints, this);
            case HIERARCHY:
                return new HierarchyBrick(object, constraints);
            case MEMBERS:
                return new MembersBrick<>(object, constraints);
        }
        return null;
    }
}
