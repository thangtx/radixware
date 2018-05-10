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

package org.radixware.kernel.designer.ads.build.dialogs;

import java.awt.Dialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.radixware.kernel.common.repository.Layer;


public class ReleaseNotesPanelExecutor {

    private ReleaseNotesPanelExecutor() {
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //open simple html file to put it as an example of release note

                final FileFilter filter = new FileNameExtensionFilter("html files filter", "html");
                final JFileChooser openChooser = new JFileChooser();
                openChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                openChooser.setFileFilter(filter);
                openChooser.showDialog(null, "Open");
                File opened = openChooser.getSelectedFile();
                if (opened != null) {
                    opened.setReadable(true);
                    try {
                        FileReader reader = new FileReader(opened);
                        Integer intLength = Integer.valueOf(String.valueOf(opened.length()));
                        char[] target = new char[intLength];
                        reader.read(target);

                        String commentExample = String.valueOf(target);
                        Layer layer = Layer.Factory.newInstance();
                        Map<Layer, String> map = new HashMap<Layer, String>();
                        map.put(layer, commentExample);

                        ReleaseNotesPanel panel = new ReleaseNotesPanel(map);

                        DialogDescriptor descriptor = new DialogDescriptor(panel, "Release Notes");
                        Dialog dialog = DialogDisplayer.getDefault().createDialog(descriptor);

                        dialog.setBounds(300, 300, panel.getPreferredSize().width, panel.getPreferredSize().height);
                        dialog.setVisible(true);
                        dialog.dispose();

//                        JFileChooser chooser = new JFileChooser();
//                        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
//                        chooser.setFileFilter(filter);
//                        chooser.showDialog(panel, "Save");
//                        File file = chooser.getSelectedFile();
//                        if (file != null) {
//                            file.setWritable(true);
//                            try {
//                                OutputStream stream = new FileOutputStream(file);
//                                stream.write(panel.getHtml().getBytes());
//                                stream.close();
//                            } catch (FileNotFoundException ex) {
//                                System.out.println("Cannot find file: " + ex.getMessage());
//                            } catch (IOException exp) {
//                                System.out.println("Problem writing to file: " + exp.getMessage());
//                            }
//                        }
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot find file: " + ex.getMessage());
                    } catch (IOException exp) {
                        System.out.println("Problem reading file: " + exp.getMessage());
                    }
                }
            }
        });


    }
}
