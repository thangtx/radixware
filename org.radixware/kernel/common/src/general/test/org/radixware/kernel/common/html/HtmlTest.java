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

package org.radixware.kernel.common.html;

import org.junit.Assert;
import org.junit.Test;

public class HtmlTest {
    @Test
    public void htmlParseTest() {
        String htmlStr = "<table><tbody><tr><td>Test</td><td></td></tr></tbody></table>";
        Html html = Html.parseFromStr(htmlStr).get(0);
        Assert.assertEquals(html.toString(true, false, 0, false, true).replace("\n", "").replace(" ", ""), htmlStr);
    }
}
