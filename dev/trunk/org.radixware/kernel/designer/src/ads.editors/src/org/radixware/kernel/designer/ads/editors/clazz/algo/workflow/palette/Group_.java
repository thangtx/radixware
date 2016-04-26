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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;


public class Group_ extends RadixObject {

    public static final Group_ GROUP_ALGBLOCKS = new Group_(NbBundle.getMessage(Group_.class, "GROUP_ALGBLOCKS"));
    public static final Group_ GROUP_APPBLOCKS = new Group_(NbBundle.getMessage(Group_.class, "GROUP_APPBLOCKS"));

    private String title;

    public Group_(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}