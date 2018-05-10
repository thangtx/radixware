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
package org.radixware.kernel.designer.ads.editors.xml;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class XsdDiffDispalyer {

    

    public static void display(final String diff, String dialogTitle) {
        final JTextPane htmlPane = new JTextPane();
        htmlPane.setContentType("text/html");

        JButton saveHtmlButton = new JButton("Save HTML");
        saveHtmlButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML (*.html)", "html");

                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setFileFilter(filter);

                int returnVal = chooser.showSaveDialog(htmlPane);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String choosedPath = chooser.getSelectedFile().getAbsolutePath();
                    File file = new File(choosedPath.lastIndexOf(".") > choosedPath.replace("\\", "/").lastIndexOf("/") ? choosedPath : choosedPath + ".html");
                    try {
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileUtils.writeString(file, diff, "UTF-8");
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });
        
        htmlPane.setText(diff);
        htmlPane.setEditable(false);

        ModalDisplayer displayer = new ModalDisplayer(htmlPane, dialogTitle, new Object[]{saveHtmlButton, DialogDescriptor.CLOSED_OPTION});

        displayer.showModal();
    }

    private static boolean isHtmlEmpty(String html) {
        return Utils.emptyOrNull(html) || "<html></html>".equals(html);
    }
}
