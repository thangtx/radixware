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

package org.radixware.kernel.designer.ads.editors.role;

import java.util.concurrent.CountDownLatch;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import javax.swing.*;
import org.netbeans.api.progress.ProgressUtils;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.designer.ads.editors.role.AdsRoleEditorPanel;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;


public class AdsRoleEditor extends RadixObjectEditor<AdsRoleDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsRoleDef> {

        @Override
        public IRadixObjectEditor<AdsRoleDef> newInstance(AdsRoleDef role) {
            return new AdsRoleEditor(role);
        }
    }
    private AdsRoleEditorPanel panel;

    public AdsRoleEditor(AdsRoleDef role) {
        super(role);
        this.panel = null;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }


    AdsRoleDef role = null;
    LocalizingEditorPanel localizingPaneList = null;
    @Override
    public boolean open(final OpenInfo info) {
        role = getRadixObject();
        final CountDownLatch latch = new CountDownLatch(1);
        final Boolean[] result = new Boolean[1];
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    if (panel == null) {
                        addComponents();
                    }
                    panel.open(role, info);
                    result[0] = AdsRoleEditor.super.open(info);
                } finally {
                    latch.countDown();
                }
            }
        };

        if (role.getModule() != null && role.getModule().isUserModule()) {
            ProgressUtils.showProgressDialogAndRun(run,
                    NbBundle.getMessage(AdsRoleEditor.class, "AdsRoleEditor.OpenMessage"));
            try {
                latch.await();
            } catch (InterruptedException ex) {
                //ignore
            }
        } else {
            run.run();
        }

        return result[0];
    }

    @Override
    public void update() {
        panel.update();
    }

    private void addComponents()
    {
            panel = new AdsRoleEditorPanel();
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(new JScrollPane(panel));
    }
}
