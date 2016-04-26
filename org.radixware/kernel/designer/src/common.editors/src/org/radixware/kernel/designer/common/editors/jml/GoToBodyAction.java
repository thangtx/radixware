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

package org.radixware.kernel.designer.common.editors.jml;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class GoToBodyAction extends AbstractAction {

    public static final String GO_TO_BODY_ACTION = "go-to-body-action";
    private final JmlEditor editor;

    public GoToBodyAction(final JmlEditor editor) {
        super(GO_TO_BODY_ACTION);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Jml jml = editor.getSource();
        final AdsClassDef classDef = getAdsClassDef(jml);
        if (classDef != null) {
            EditorsManager.getDefault().open(classDef.getBody().get(0));
        }
    }

    private AdsClassDef getAdsClassDef(final Jml jml) {
        RadixObject target = jml.getOwnerDefinition();
        while (target != null && !(target instanceof AdsClassDef)) {
            target = target.getOwnerDefinition();
        }
        if (target != null) {
            return (AdsClassDef) target;
        }
        return null;
    }
}
