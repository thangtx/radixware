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

import java.io.File;
import java.io.IOException;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.repository.Branch;


public class BranchLoader {

    private static Branch branch;

    public final Branch getBranch() throws IOException {
        if (branch == null) {
            
            branch = Branch.Factory.loadFromDir(new File("E:\\radix"));
            branch.visit(new IVisitor() {
                @Override
                public void accept(RadixObject ro) {
                }
            }, VisitorProviderFactory.createDefaultVisitorProvider());
        }
        return branch;
    }
}
