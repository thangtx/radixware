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

package org.radixware.kernel.designer.environment.project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.Icon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.radixware.kernel.common.defs.RadixObjectIcon;

class RadixProjectInformation implements ProjectInformation {

    final RadixProject project;

    public RadixProjectInformation(RadixProject project) {
        super();
        this.project = project;
    }

    @Override
    public String getName() {
        return project.getProjectDirectory().getName();
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public Icon getIcon() {
        return RadixObjectIcon.BRANCH.getIcon();
    }

    @Override
    public Project getProject() {
        return project;
    }
    
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        // nether changed
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        // nether changed
    }
}
