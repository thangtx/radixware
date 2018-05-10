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

package org.radixware.kernel.designer.tree.actions.dds;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.windows.InputOutput;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsScriptsDir;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.designer.subversion.util.RadixSvnUtils;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseRadixObject;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseRadixObjectCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.common.dialogs.db.DdsScriptUtils;


class CreateDdsScriptWorker implements Runnable {

    private final DdsScriptsDir scriptsDir;
    private final boolean reverse;

    public CreateDdsScriptWorker(DdsScriptsDir scriptsDir, boolean reverse) {
        this.scriptsDir = scriptsDir;
        this.reverse = reverse;
    }

    @Override
    public void run() {
        if (!scriptsDir.isInBranch()) {
            return; // was deleted
        }
        DdsUpdateInfo straight = null;
        if (reverse) {
            straight = (DdsUpdateInfo) ChooseRadixObject.chooseRadixObject(ChooseRadixObjectCfg.Factory.newInstance(scriptsDir.getOwnerDatabaseScripts().getOwnerScripts(), new VisitorProvider() {
                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof DdsUpdateInfo && !((DdsUpdateInfo) radixObject).isReverse();
                }
            }));
            if (straight == null) {
                return;
            }
        }

        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Create New Script");
        progressHandle.start();

        try {
            final DdsScripts scripts = scriptsDir.getOwnerDatabaseScripts().getOwnerScripts();
            final DdsSegment segment = scripts.getOwnerSegment();
            final Branch branch = segment.getBranch();
            final RadixSvnUtils svn = RadixSvnUtils.Factory.newInstance(branch);
            final DdsSegmentCommiter commiter = new DdsSegmentCommiter(segment);
            progressHandle.progress("Check DDS versioning status");
            if (commiter.checkUpToDate(svn, InputOutput.NULL)) {
                JPanel panel = new JPanel();
                final JRadioButton compatible = new JRadioButton("Compatible");
                final JRadioButton incompatible = new JRadioButton("Not Compatible");
                panel.setLayout(new FlowLayout());
                panel.add(compatible);
                panel.add(incompatible);
                ButtonGroup group = new ButtonGroup();
                group.add(compatible);
                group.add(incompatible);
                incompatible.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (incompatible.isSelected() && !DdsScriptUtils.showCompatibleWarning()){
                            compatible.setSelected(true);
                        }
                    }
                });

                ModalDisplayer displayer = new ModalDisplayer(panel, "Script compatibility") {
                    @Override
                    public boolean canClose() {
                        if (!compatible.isSelected() && !incompatible.isSelected()) {
                            DialogUtils.messageError("Please, select on of compatibility options");
                            return false;
                        }
                        return true;
                    }
                };

                displayer.getDialog().setPreferredSize(new Dimension(400, 300));
                if (!displayer.showModal()) {
                    return;
                }
                boolean isCompatible = compatible.isSelected();

                progressHandle.progress("Create File");
                final DdsScript script = scriptsDir.addNew(reverse);
                script.setContent("");
                progressHandle.progress("Register File");
                scripts.getUpdatesInfo().registerNew(script, isCompatible /*
                         * backwardCompatible
                         */, straight);
                scripts.save();
                editOnAwt(script);
            }
        } catch (Exception cause) {
            RadixObjectError error = new RadixObjectError("Unable to create new script.", scriptsDir, cause);
            DialogUtils.messageError(error);
        } finally {
            progressHandle.finish();
        }
    }

    private void editOnAwt(final DdsScript script) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DialogUtils.editFile(script.getFile());
            }
        });
    }
}
