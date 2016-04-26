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

import java.awt.Component;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;



public class KeyStoreAdminFileSelector{

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/radixware/kernel/utils/keyStoreAdmin/resources/KeyStoreAdminFileSelector");

    public static enum SelectNewFileMode {FORBIDDEN, ALLOWED, REQUIRED};

    public static File selectFileForOpen(Component parent, String title, KeyStoreAdminFileFilter[] filters, String defaultPath, SelectNewFileMode selectNewFileMode){
        return selectFile(parent, title, filters, defaultPath, true, selectNewFileMode);
    }

    public static File selectFileForSave(Component parent, String title, KeyStoreAdminFileFilter[] filters, String defaultPath){
        return selectFile(parent, title, filters, defaultPath, false, SelectNewFileMode.ALLOWED);
    }

    private static File selectFile(Component parent, String title, KeyStoreAdminFileFilter[] filters, String defaultPath, boolean openMode, SelectNewFileMode selectNewFileMode){
        String messageBoxTitle;
        String message;
        boolean tryAgain;
        File selectedFile;
        JFileChooser fileChooser = new JFileChooser(defaultPath);
        fileChooser.setDialogTitle(title);
        fileChooser.setFileHidingEnabled(false);
        for (KeyStoreAdminFileFilter filter : filters)
            fileChooser.addChoosableFileFilter(filter);
        do{
            tryAgain = false;
            selectedFile = null;
            int option = (openMode ? fileChooser.showOpenDialog(parent) : fileChooser.showSaveDialog(parent));
            if (option==JFileChooser.APPROVE_OPTION){
                selectedFile = fileChooser.getSelectedFile();
                FileFilter activeFilter = fileChooser.getFileFilter();
                if (openMode){
                    if (!selectedFile.exists()){
                        if (selectNewFileMode==SelectNewFileMode.FORBIDDEN){
                            messageBoxTitle = bundle.getString("JOptionPaneFileError.title");
                            message = bundle.getString("FileNotExists");
                            JOptionPane.showMessageDialog(parent, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
                            tryAgain = true;
                        } else{
                            selectedFile = checkFileExtension(selectedFile, activeFilter);
                        }
                    } else{
                        if (!selectedFile.isFile()){
                            messageBoxTitle = bundle.getString("JOptionPaneFileError.title");
                            message = bundle.getString("NotAFile");
                            JOptionPane.showMessageDialog(parent, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
                            tryAgain = true;
                        }
                        if (selectNewFileMode==SelectNewFileMode.REQUIRED){
                            messageBoxTitle = bundle.getString("JOptionPaneWarning.title");
                            message = String.format(bundle.getString("FileExists"), selectedFile.getAbsoluteFile());
                            int confirm = JOptionPane.showConfirmDialog(parent, message, messageBoxTitle, JOptionPane.YES_NO_OPTION);
                            if (confirm==JOptionPane.NO_OPTION){
                                tryAgain = true;
                            }
                        }
                    }
                } else{
                    selectedFile = checkFileExtension(selectedFile, activeFilter);
                    if (selectedFile.exists()){
                        if (!selectedFile.isFile()){
                            messageBoxTitle = bundle.getString("JOptionPaneFileError.title");
                            message = bundle.getString("NotAFile");
                            JOptionPane.showMessageDialog(parent, message, messageBoxTitle, JOptionPane.ERROR_MESSAGE);
                            tryAgain = true;
                        } else{
                            messageBoxTitle = bundle.getString("JOptionPaneWarning.title");
                            message = String.format(bundle.getString("FileExists"), selectedFile.getAbsoluteFile());
                            int confirm = JOptionPane.showConfirmDialog(parent, message, messageBoxTitle, JOptionPane.YES_NO_OPTION);
                            if (confirm==JOptionPane.NO_OPTION){
                                tryAgain = true;
                            }
                        }
                    }
                }
            }
        } while (tryAgain);
        return selectedFile;
    }

    private static File checkFileExtension(File selectedFile, FileFilter filter){
        if (filter instanceof KeyStoreAdminFileFilter
                &&((KeyStoreAdminFileFilter)filter).getExtensions().length>0
                &&!selectedFile.getName().contains("."))
            return new File(selectedFile.getAbsolutePath()+((KeyStoreAdminFileFilter)filter).getExtensions()[0]);
        else
            return selectedFile;
    }
}