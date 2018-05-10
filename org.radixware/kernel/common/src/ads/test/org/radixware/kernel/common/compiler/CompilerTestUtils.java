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


package org.radixware.kernel.common.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CompilerTestUtils {

    public static String getJmlSampleSource(String jmlSampleFilename) {

        String result = "";

        try {

            InputStream is = CompilerTestUtils.class.getResourceAsStream("/org/radixware/kernel/common/compiler/jmlsamples/" + jmlSampleFilename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }

            is.close();

        } catch (IOException ex) {
            Logger.getLogger(CompilerTestUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;

    }
}
