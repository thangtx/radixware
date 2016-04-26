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
 * 9/21/11 9:16 AM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import java.awt.Component;


public interface IValueEditorComponent<TValue> extends IValueEditor<TValue> {
    /**
     * 
     * @return editor component
     */
    Component getEditorComponent();
    
    int getBaseline(int width, int height);
    
    void requestFocus();
    
    boolean isFocusOwner();
    
    void setEnabled(boolean enabled);
    
    boolean isEnabled();
}
