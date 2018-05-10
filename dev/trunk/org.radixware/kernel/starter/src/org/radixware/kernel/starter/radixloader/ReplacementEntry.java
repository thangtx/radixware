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
package org.radixware.kernel.starter.radixloader;

import java.io.File;


public class ReplacementEntry {
    
    private final String remote;
    private final String local;

    public ReplacementEntry(String remote, String local) {
        this.remote = remote.replace(File.separator, "/");
        this.local = local;
    }

    public String getRemote() {
        return remote;
    }

    public String getLocal() {
        return local;
    }

    @Override
    public String toString() {
        return "Replacement entry: " + remote + " -> " + local;
    }
    
}
