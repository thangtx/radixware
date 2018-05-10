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

    private static File lastSelectedFilesDir = null;

    public static File chooseXmlFile(int dialogType, String title) {
        JFileChooser chooser = new JFileChooser(lastSelectedFilesDir == null ? null : lastSelectedFilesDir);
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
            lastSelectedFilesDir = chooser.getSelectedFile().getParentFile();
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }
    
    public static File[] openFiles(String title) {
        JFileChooser chooser = new JFileChooser(lastSelectedFilesDir == null ? null : lastSelectedFilesDir);
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
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setDialogTitle(title);
        if (chooser.showDialog(null, "Open") == JFileChooser.APPROVE_OPTION) {
            lastSelectedFilesDir = chooser.getCurrentDirectory();
            return chooser.getSelectedFiles();
        } else {
            return null;
        }
    }
}
