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

package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.awt.Dimension;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


final class SynchronizeDisplayer extends ModalDisplayer {

    public SynchronizeDisplayer() {
        super(new SynchronizePublishedClassPanel());

        getComponent().setMinimumSize(new Dimension(300, 200));
        setTitle(NbBundle.getMessage(SynchronizeDisplayer.class, "SynchronizeDialogTitle"));

//        final JButton btnApply = new JButton(NbBundle.getMessage(SynchronizeDisplayer.class, "TreeTable-Dialog-Apply"));
//        btnApply.setEnabled(false);
//        getComponent().addChangeListener(new ChangeListener() {
//
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                btnApply.setEnabled(true);
//            }
//        });
//        btnApply.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                apply();
//                AdsClassDef transparentClass = getComponent().getTransparentClass();
//
//                open(transparentClass);
//                btnApply.setEnabled(false);
//            }
//        });
//
//        final JButton btnCancel = new JButton(NbBundle.getMessage(SynchronizeDisplayer.class, "TreeTable-Dialog-Close"));
//        btnCancel.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                SynchronizeDisplayer.this.getDialog().setVisible(false);
//            }
//        });
//
//        getDialogDescriptor().setOptions(new Object[]{ btnApply, btnCancel });
    }

    @Override
    protected void apply() {
        getComponent().apply();
    }

    @Override
    public SynchronizePublishedClassPanel getComponent() {
        return (SynchronizePublishedClassPanel) super.getComponent();
    }

    public void open(AdsClassDef transparentClass) {

        if (AdsTransparence.isTransparent(transparentClass)) {
            final PlatformLib kernelLib = ((AdsSegment) transparentClass.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(transparentClass.getUsageEnvironment());
            final RadixPlatformClass platformClass = kernelLib.findPlatformClass(transparentClass.getTransparence().getPublishedName());
            if (platformClass != null) {
                getComponent().open(platformClass, transparentClass);

            } else {
                DialogUtils.messageError(NbBundle.getMessage(SynchronizePublishedClassPanel.class, "SynchronizationErrors-Load") + transparentClass.getTransparence().getPublishedName());
            }
        } else {
            DialogUtils.messageError(NbBundle.getMessage(SynchronizePublishedClassPanel.class, "SynchronizationErrors-NotTranparent") + transparentClass.getQualifiedName());
        }
    }
}
