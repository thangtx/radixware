/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.builder.utils;

import java.io.File;
import java.io.IOException;
import org.junit.Test;
import org.radixware.kernel.common.utils.Base64;
import org.radixware.kernel.common.utils.FileUtils;

/**
 *
 * @author akrylov
 */
public class File2Base64LengthCounter {

    @Test
    public void test() throws IOException {
        byte[] bytes = FileUtils.readBinaryFile(new File("/home/akrylov/BUG/тунмф-куз/6min.xls"));
        String string = Base64.encode(bytes);
        System.out.println(1024 * 200);
        System.out.println(bytes.length);
        System.out.println(string.length());
    }
}
