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
package org.radixware.kernel.designer.common.operation;

import java.io.File;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.client.ISvnFSClient;
import org.radixware.kernel.common.utils.RadixObjectsUtils;

public class SvnUpdateOperation extends BaseOperation {

    final private List<RadixObject> list;

    final private File files[];
    private final ISvnFSClient client;
    final private boolean canCloseRepositopySession;

    public SvnUpdateOperation(final List<RadixObject> list, final ISvnFSClient client, final File[] files, final String title, final boolean canCloseRepositopySession) {
        super(title);
        this.list = list;
        this.client = client;
        this.files = files;
        this.canCloseRepositopySession = canCloseRepositopySession;
    }

    @Override
    protected boolean process() throws Exception {
        getOut().println("Updating " + String.valueOf(list.size()) + " definitions (" + String.valueOf(files.length) + " files and directories) ...");
        getOut().println("The following files have been updated:");
        SVN.update(getOut(), getErr(), client, files);

        RadixObjectsUtils.sortByQualifiedName(list);

        getOut().println("The following definitions have been updated:");
        for (RadixObject obj : list) {
            getOut().println(obj.getQualifiedName());
        }
        getOut().flush();
        if (canCloseRepositopySession) {
        //    client.close();
        }
        return true;
    }

}
