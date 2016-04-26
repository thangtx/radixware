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

package org.radixware.kernel.common.utils.dist;

import org.radixware.kernel.common.resources.icons.RadixIcon;


public class DistributionIcon extends RadixIcon {

    public static final DistributionIcon PATCH = new DistributionIcon("manager/patch.svg");
    public static final DistributionIcon DISTRIBUTIVE = new DistributionIcon("manager/distributive.svg");
    public static final DistributionIcon RELEASE = new DistributionIcon("manager/release.svg");

    public DistributionIcon(String name) {
        super(name);
    }
}
