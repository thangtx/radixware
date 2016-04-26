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

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import javax.swing.*;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;


public class AdsXmlSchemeEditor extends RadixObjectEditor {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory {

        @Override
        public IRadixObjectEditor newInstance(RadixObject radixObject) {
            return new AdsXmlSchemeEditor(radixObject);
        }
    }
    private AdsXmlEditor panel;

    @Override
    public boolean canClose() {
        if (panel!=null && !panel.canClose())
            return false;
        return super.canClose();
    }

    public AdsXmlSchemeEditor(RadixObject radixObject) {
        super(radixObject);
        this.panel = null;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public boolean open(OpenInfo info) {
        if (panel == null) {
            panel = new AdsXmlEditor();
            this.add(panel);
        }

        panel.open(getRadixObject(), info);
        return super.open(info);
    }

    @Override
    public void update() {
        panel.update(false);
    }
}
