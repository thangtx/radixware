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

package org.radixware.kernel.common.client.views;

import java.util.List;
import org.radixware.kernel.common.types.Arr;


public interface IArrayEditorDialog extends IDialogWithStandardButtons {

    void setCurrentValue(Arr arr);

    Arr getCurrentValue();

    boolean isReadonly();

    void setReadonly(boolean readOnly);
    
    void setEditorReadonly(boolean readonly);
    
    void setMandatory(boolean mandatory);

    void setItemMandatory(boolean mandatory);
    
    boolean isMandatory();

    boolean isItemMandatory();

    void setDuplicatesEnabled(final boolean duplicates);
    
    boolean isDuplicatesEnabled();
    
    void setItemsMovable(boolean isMovable);
    
    boolean isItemsMovable();
    
    void setOperationsVisible(boolean isVisible);
    
    boolean isEmptyArray();
    
    Object getSelectedValue();
    
    void setPredefinedValues(List<Object> values);
    
    void addEventListener(ArrayEditorEventListener listener);
    
    void removeEventListener(ArrayEditorEventListener listener);    
}
