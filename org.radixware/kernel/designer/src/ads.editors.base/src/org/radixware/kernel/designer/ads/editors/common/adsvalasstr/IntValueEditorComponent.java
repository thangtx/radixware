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

/*
 * 9/23/11 4:59 PM
 */
package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.awt.Component;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;


public abstract class IntValueEditorComponent extends BaseEditorComponent {

    private JFormattedTextField editor;

    public IntValueEditorComponent() {

        super(null);
        editor = new JFormattedTextField(new NumberFormatter());
    }

    @Override
    public void updateModelValue() {
    }


    @Override
    public Component getEditorComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    protected void updateEditorComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
