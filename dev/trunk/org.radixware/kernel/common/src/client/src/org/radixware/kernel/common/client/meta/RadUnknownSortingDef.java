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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.types.Id;


public class RadUnknownSortingDef extends RadSortingDef{
    
    public RadUnknownSortingDef(final Id sortingId){
        super(sortingId,"",null,null,null);
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    protected String getTitle(Id definitionId, Id titleId) {
        return getApplication().getMessageProvider().translate("Selector", "<Unknown sorting>");
    }

    @Override
    public String getTitle() {
        return getApplication().getMessageProvider().translate("Selector", "<Unknown sorting>");
    }

    @Override
    public boolean hasTitle() {
        return true;
    }        
}
