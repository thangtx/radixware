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

package org.radixware.kernel.designer.uds.tree.actions;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


public class ActionUtil {

    private static File lastSelectedFile = null;

    public static File chooseFile(int dialogType, String title) {
        JFileChooser chooser = new JFileChooser(lastSelectedFile == null ? null : lastSelectedFile.getParentFile());
        FileFilter ff = new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || (f.isFile() && f.getName().endsWith(".xml"));
            }

            @Override
            public String getDescription() {
                return "Xml Files (*.xml)";
            }
        };
        chooser.addChoosableFileFilter(ff);
        chooser.setFileFilter(ff);
        chooser.setDialogType(dialogType);
        chooser.setDialogTitle(title);
        if (chooser.showDialog(null, dialogType == JFileChooser.SAVE_DIALOG ? "Save" : "Open") == JFileChooser.APPROVE_OPTION) {
            return lastSelectedFile = chooser.getSelectedFile();
        } else {
            return null;
        }
    }
}
