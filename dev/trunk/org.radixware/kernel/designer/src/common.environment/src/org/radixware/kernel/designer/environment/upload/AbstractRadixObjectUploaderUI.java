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

package org.radixware.kernel.designer.environment.upload;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import org.radixware.kernel.common.repository.Branch;


public abstract class AbstractRadixObjectUploaderUI {

    static AbstractRadixObjectUploaderUI INSTANCE = null; //  registered in BranchesUpdater and UploaderTest

    public static AbstractRadixObjectUploaderUI getDefault() {
        return INSTANCE;
    }

    public abstract void addOpenProjectsChangedListener(PropertyChangeListener openProjectsListener);

    public abstract void logStatus(String status);

    public abstract boolean confirmRetry(String message, IOException cause);

    public abstract boolean confirmReload(String message);

    public abstract boolean confirmClose(String message);

    public abstract void closeProject(Branch branch);

    public abstract boolean isIgnoreAll();

    public abstract void resetIgnoreAll();
}
