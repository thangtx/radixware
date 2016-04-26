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

package org.radixware.kernel.designer.ads.editors.clazz.enumeration;

import java.awt.BorderLayout;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsEnumClassFieldEditor extends RadixObjectEditor<AdsEnumClassFieldDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsEnumClassFieldDef> {

        @Override
        public IRadixObjectEditor<AdsEnumClassFieldDef> newInstance(AdsEnumClassFieldDef field) {
            return new AdsEnumClassFieldEditor(field);
        }
    }

    private FieldEditorPanel editorPanel = new FieldEditorPanel();

    public AdsEnumClassFieldEditor(AdsEnumClassFieldDef field) {
        super(field);

        setLayout(new BorderLayout());
        add(editorPanel, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        editorPanel.update();
    }

    @Override
    public boolean open(OpenInfo openInfo) {
        editorPanel.open(getRadixObject());
        return true;
    }
}
