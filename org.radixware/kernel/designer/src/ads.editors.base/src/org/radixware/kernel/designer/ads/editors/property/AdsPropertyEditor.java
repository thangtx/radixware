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

package org.radixware.kernel.designer.ads.editors.property;

import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import javax.swing.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;


public class AdsPropertyEditor extends RadixObjectEditor<AdsPropertyDef> {

    public AdsPropertyEditor(AdsPropertyDef prop) {
        super(prop);
        this.panel = null;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsPropertyDef> {

        @Override
        public IRadixObjectEditor<AdsPropertyDef> newInstance(AdsPropertyDef prop) {
            return new AdsPropertyEditor(prop);
        }
    }

    @Override
    public void update() {
        panel.update();
    }
    private AdsPropertyEditorPanel panel;

    @Override
    public boolean open(OpenInfo info) {
        final AdsPropertyDef prop = getRadixObject();

        if (panel == null) {
            panel = new AdsPropertyEditorPanel(prop);
            this.add(panel);
        }
        panel.open(prop, info);
        return super.open(info);
    }
}
