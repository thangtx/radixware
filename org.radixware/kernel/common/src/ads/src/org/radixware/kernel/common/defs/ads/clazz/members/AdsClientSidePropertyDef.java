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

package org.radixware.kernel.common.defs.ads.clazz.members;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;

import org.radixware.schemas.adsdef.AbstractPropertyDefinition;


public abstract class AdsClientSidePropertyDef extends AdsPropertyDef{
    
    protected AdsClientSidePropertyDef(AbstractPropertyDefinition xProp){
        super( xProp);
    }
    protected AdsClientSidePropertyDef(Id id,String name){
        super( id,name);
    }

    protected AdsClientSidePropertyDef(AdsPropertyDef source, boolean forOverride) {
        super(source, forOverride);
    }

    protected AdsClassDef findServerSideClassDef(){
        AdsClassDef clazz = getOwnerClass();
        if(clazz instanceof AdsModelClassDef)
            return ((AdsModelClassDef)clazz).findServerSideClasDef();
        else
            return null;
    }
}
