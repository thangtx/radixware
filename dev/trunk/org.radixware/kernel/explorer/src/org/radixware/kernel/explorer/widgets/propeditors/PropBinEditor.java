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

package org.radixware.kernel.explorer.widgets.propeditors;

import org.radixware.kernel.common.client.enums.EWidgetMarker;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.explorer.editors.valeditors.ValEditorFactory;


public class PropBinEditor extends PropEditor {

    public PropBinEditor(final Property property) {
        this(property, getValEditorFactory());
    }
    
    protected PropBinEditor(final Property property, final ValEditorFactory factory){
        super(property,factory);
    }
    
    public static ValEditorFactory getValEditorFactory(){
        return ValEditorFactory.getDefault();
    }

    @Override
    public final EWidgetMarker getWidgetMarker() {
        return EWidgetMarker.BIN_PROP_EDITOR;
    }    
}
