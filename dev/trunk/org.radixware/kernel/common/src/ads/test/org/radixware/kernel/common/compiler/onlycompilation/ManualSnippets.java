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
package org.radixware.kernel.common.compiler.onlycompilation;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.radixware.kernel.common.compiler.TextCompilerTest;

public class ManualSnippets {

    @Test
    public void testDiamondOnMethodCall() {
        assertTrue("Compilation failed", TextCompilerTest.compileCode("Test",
                "class Test{\n"
                + "    public java.util.List<String> getProp(){return null;}\n"
                + "    public void setProp(java.util.List<String> list){\n"
                + "    }\n"
                + "    public void test(){\n"
                + "       Prop = new java.util.ArrayList<>();\n"
                + "    }\n"
                + "}", null));
    }
}
