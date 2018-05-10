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


package org.radixware.kernel.designer.common.dialogs.components.description;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.RadixObject;

import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;


public final class MixedDescriptionWrapper implements IDescribable, ILocalizedDescribable, ILocalizedDescribable.Inheritable, IReadOnly {

    public static final class Factory {
        private Factory() {}
        
        public static MixedDescriptionWrapper tryNewInstance(Object object) {
            if (object instanceof IDescribable && object instanceof ILocalizedDescribable) {
                return new MixedDescriptionWrapper((IDescribable) object, (ILocalizedDescribable) object);
            }
            return null;
        }
    }
    
    private final IDescribable describable;
    private final ILocalizedDescribable localizedDescribable;

    private MixedDescriptionWrapper(IDescribable describable, ILocalizedDescribable localizedDescribable) {
        
        assert describable == localizedDescribable;
        
        this.describable = describable;
        this.localizedDescribable = localizedDescribable;
    }

    @Override
    public String getDescription() {
        return describable.getDescription();
    }

    @Override
    public void setDescription(String description) {
        describable.setDescription(description);
    }

    @Override
    public Id getDescriptionId() {
        return localizedDescribable.getDescriptionId();
    }
   
    @Override
    public Id getDescriptionId(boolean inherited) {
        if (localizedDescribable instanceof ILocalizedDescribable.Inheritable){
            return ((ILocalizedDescribable.Inheritable)localizedDescribable).getDescriptionId(inherited);
        }
        return localizedDescribable.getDescriptionId();
    }

    @Override
    public void setDescriptionId(Id id) {
        localizedDescribable.setDescriptionId(id);
    }

    @Override
    public String getDescription(EIsoLanguage language) {
        return localizedDescribable.getDescription(language);
    }
    
        @Override
    public boolean isDescriptionInheritable() {
        if (localizedDescribable instanceof ILocalizedDescribable.Inheritable){
            return ((ILocalizedDescribable.Inheritable)localizedDescribable).isDescriptionInheritable();
        }
        return false;
    }

    @Override
    public boolean isDescriptionInherited() {
        if (isDescriptionInheritable()){
            return ((ILocalizedDescribable.Inheritable)localizedDescribable).isDescriptionInherited();
        }
        return false;
    }

    @Override
    public void setDescriptionInherited(boolean inherit) {
        if (isDescriptionInheritable()){
            ((ILocalizedDescribable.Inheritable)localizedDescribable).setDescriptionInherited(inherit);
        }
    }
    

    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        return localizedDescribable.setDescription(language, description);
    }

    @Override
    public Definition getDescriptionLocation() {
        return localizedDescribable.getDescriptionLocation();
    }

    @Override
    public Definition getDescriptionLocation(boolean inherited) {
        return localizedDescribable.getDescriptionLocation();
    }

    @Override
    public boolean isReadOnly() {
        if (describable instanceof RadixObject) {
            return ((RadixObject)describable).isReadOnly();
        }
        
        return true;
    }
}
