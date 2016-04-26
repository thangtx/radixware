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

import java.awt.BorderLayout;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsPropertyAccessorEditor extends RadixObjectEditor<AdsPropertyDef.Accessor> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsPropertyDef.Accessor> {

        @Override
        public IRadixObjectEditor<AdsPropertyDef.Accessor> newInstance(AdsPropertyDef.Accessor accessor) {
            return new AdsPropertyAccessorEditor(accessor);
        }
    }
    private final PropertyAccessorEditorPanel editor = new PropertyAccessorEditorPanel();

    public AdsPropertyAccessorEditor(AdsPropertyDef.Accessor accessor) {
        super(accessor);
        setLayout(new BorderLayout());
        add(editor, BorderLayout.CENTER);
    }

    public AdsPropertyDef.Accessor getAccessor() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo info) {
        final AdsPropertyDef.Accessor accessor = getAccessor();
        // editor.open(accessor.getSource(), info);
        editor.open(info, accessor);
        editor.requestFocusInWindow();
        return super.open(info);
    }

    @Override
    public void update() {
        editor.open(null, getAccessor());
    }

    @Override
    public void requestFocus() {
        editor.requestFocusInWindow();
    }
}
