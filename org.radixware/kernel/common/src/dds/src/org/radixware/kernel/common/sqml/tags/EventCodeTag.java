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

package org.radixware.kernel.common.sqml.tags;

import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;


public class EventCodeTag extends Sqml.Tag{
    
    private final Id mlStringBundleId;
    private final Id mlStringId;
    
    protected EventCodeTag(final Id mlStringBundleId, final Id mlStringId){
        this.mlStringBundleId = mlStringBundleId;
        this.mlStringId = mlStringId;
    }
    
    public Id getBundleId(){
        return mlStringBundleId;
    }
    
    public Id getStringId(){
        return mlStringId;
    }
    
    public static final class Factory {

        private Factory() {
        }

        public static EventCodeTag newInstance(final Id mlStringBundleId, final Id mlStringId) {
            return new EventCodeTag(mlStringBundleId, mlStringId);
        }
    }
    
    public static final String EVENT_CODE_TAG_TYPE_TITLE = "Event Code Parameter Tag";
    public static final String EVENT_CODE_TAG_TYPES_TITLE = "Event Code Parameter Tags";

    @Override
    public String getTypeTitle() {
        return EVENT_CODE_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return EVENT_CODE_TAG_TYPES_TITLE;
    }    
}