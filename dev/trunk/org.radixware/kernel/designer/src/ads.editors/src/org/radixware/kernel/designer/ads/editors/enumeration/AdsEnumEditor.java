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

package org.radixware.kernel.designer.ads.editors.enumeration;

import java.awt.BorderLayout;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;

import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsEnumEditor extends RadixObjectEditor<AdsEnumDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsEnumDef> {

        @Override
        public IRadixObjectEditor<AdsEnumDef> newInstance(AdsEnumDef adsEnumDef) {
            return new AdsEnumEditor(adsEnumDef);
        }
    }

    protected AdsEnumEditor(AdsEnumDef adsEnumDef) {
        super(adsEnumDef);
        setLayout(new BorderLayout());
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane();
        scroll.setViewportView(panel);
        add(scroll, BorderLayout.CENTER);
    }

    public AdsEnumDef getAdsEnumDef() {
        return getRadixObject();
    }

    private AdsEnumEditorPanel panel = new AdsEnumEditorPanel();
    @Override
    public boolean open(OpenInfo info) {
        panel.open(getAdsEnumDef(), info);
        return super.open(info);
    }

    @Override
    public void update() {
        panel.update();
    }

}
