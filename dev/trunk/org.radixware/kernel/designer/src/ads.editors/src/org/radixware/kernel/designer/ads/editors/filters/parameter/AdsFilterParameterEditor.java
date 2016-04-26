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

package org.radixware.kernel.designer.ads.editors.filters.parameter;

import java.awt.BorderLayout;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsFilterParameterEditor extends RadixObjectEditor<AdsFilterDef.Parameter> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsFilterDef.Parameter> {

        @Override
        public IRadixObjectEditor<AdsFilterDef.Parameter> newInstance(AdsFilterDef.Parameter parameter) {
            return new AdsFilterParameterEditor(parameter);
        }
    }

    private FilterParameterEditorPanel panel = new FilterParameterEditorPanel();

    protected AdsFilterParameterEditor(AdsFilterDef.Parameter parameter) {
        super(parameter);
        setLayout(new BorderLayout());
        javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(panel);
        add(scroll, BorderLayout.CENTER);
    }

    public AdsFilterDef.Parameter getParameter() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo info) {
        final AdsFilterDef.Parameter parameter = getParameter();
        panel.open(parameter);
        return super.open(info);
    }

    @Override
    public void update() {
        panel.update();
    }

}
