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

package org.radixware.kernel.utils.keyStoreAdmin.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;



public class KeyStoreAdminFileFilter extends FileFilter{

    private final String[] extensions;
    private final String description;

    public KeyStoreAdminFileFilter(final String[] extensions, final String description){
        this.extensions = new String[extensions.length];
        for (int i = 0; i<extensions.length; i++)
            this.extensions[i] = (extensions[i].length()>0&&extensions[i].charAt(0)=='.' ? "" : ".")+extensions[i];
        this.description = description;
    }

    public boolean accept(final File file){
        if (file.isDirectory())
            return true;

        final String filename = file.getName();
        for (String extension : extensions)
            if (filename.endsWith(extension))
                return true;
        return false;
    }

    public String getDescription(){
        return description;
    }

    public String[] getExtensions(){
        return extensions;
    }
}