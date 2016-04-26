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

package org.radixware.kernel.designer.environment.navigation;

import java.io.File;
import javax.swing.Icon;
import org.openide.filesystems.FileObject;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupportsManager;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

class RadixObjectDescriptorAdapter {

    private final RadixObject radixObject;

    public RadixObjectDescriptorAdapter(RadixObject radixObject) {
        super();
        this.radixObject = radixObject;
    }

    public String getContextName() {
        final RadixObject project = findProject();
        if (project != null && project != radixObject) {
            final RadixObject owner = radixObject.getOwnerForQualifedName();
            if (owner != null && owner != project) {
                return " in " + owner.getQualifiedName(project);
            }
        }
        return "";
    }

    public FileObject getFileObject() {
        final File file = radixObject.getFile();
        if (file != null) {
            return RadixFileUtil.toFileObject(file);
        } else {
            return null;
        }
    }

    public Icon getIcon() {
        return radixObject.getIcon().getIcon();
    }

    public int getOffset() {
        return 0;
    }

    public String getOuterName() {
        return null;
    }

    protected RadixObject findProject() {
        for (RadixObject object = radixObject.getOwnerForQualifedName(); object != null; object = object.getOwnerForQualifedName()) {
            if ((object instanceof Module) || (object instanceof Layer) || (object instanceof Branch)) {
                return object;
            }
        }
        return null;
    }

    public Icon getProjectIcon() {
        final RadixObject project = findProject();
        if (project != null) {
            return project.getIcon().getIcon();
        } else {
            return null;
        }
    }

    public String getProjectName() {
        final RadixObject project = findProject();
        if (project != null) {
            return project.getQualifiedName();
        } else {
            return null;
        }
    }

    public String getSimpleName() {
//        HtmlNameSupport support = HtmlNameSupportsManager.newInstance(radixObject);
//        if (support != null) {
//            return //"<html>" +
//                    support.getHtmlName();
//        }
        return radixObject.getName();
    }

    public String getTypeName() {
        return getSimpleName();
    }

    public void open() {
        DialogUtils.goToObject(radixObject);
    }
}
