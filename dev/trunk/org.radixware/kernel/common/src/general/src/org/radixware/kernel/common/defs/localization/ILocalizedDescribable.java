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

package org.radixware.kernel.common.defs.localization;

import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.Definition;


public interface ILocalizedDescribable {

    Id getDescriptionId();

    void setDescriptionId(Id id);

    String getDescription(EIsoLanguage language);

    boolean setDescription(EIsoLanguage language, String description);

    Definition getDescriptionLocation();
    
    public interface Inheritable {
        
        public boolean isDescriptionInheritable();
        
        public boolean isDescriptionInherited();
        
        void setDescriptionInherited(boolean inherit);
        
        Id getDescriptionId();
    }
    
    public interface ILocalizedCalculatedDef {
        public Id getDescriptionCalculatedId();
        
        public boolean isDescriptionIdCalculated();
   }
}
