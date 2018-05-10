/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.client.views;

import org.radixware.kernel.common.client.enums.EReferenceStringFormat;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.utils.IValAsStrConverter;
import org.radixware.kernel.common.enums.EValType;


public interface IProxyPropEditor extends IPropEditor{
    
    void changeValueType(EValType valType, EditMask mask);

    EValType getValueType();
    
    EditMask getEditMask();
    
    boolean isInErrorMode();
    
    IValAsStrConverter getValueConverter();
    
    void setValueConverter(final IValAsStrConverter converter);
    
    EReferenceStringFormat getReferenceValueFormat();
    
    void setReferenceValueFormat(EReferenceStringFormat format);
    
}
