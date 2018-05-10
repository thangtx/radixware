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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IDescribable;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;


public final class DescriptionModel implements IDescribable, ILocalizedDescribable, ILocalizedDescribable.Inheritable {

    public static final class Factory {
        public static <T extends Object & IDescribable & ILocalizedDescribable> DescriptionModel newInstance(T definition) {
            return new DescriptionModel(definition);
        }
        
        public static DescriptionModel tryNewInstance(Object definition) {
            if (definition instanceof ILocalizedDescribable && definition instanceof IDescribable) {
                return new DescriptionModel(definition);
            }
            
            return null;
        }
    }
    
    static String toString(EDescriptionType editorMode, List<IMultilingualStringDef.StringStorage> list, String stringDescription) {
        if (editorMode == EDescriptionType.LOCALIZED) {

            if (list == null) {
                return "";
            } else {
                final StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (IMultilingualStringDef.StringStorage storage : list) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append("; ");
                    }
                    builder.append(storage.getLanguage().getName()).append(": ").append(storage.getValue());
                }
                return builder.toString();
            }
        } else {
            return stringDescription != null ? stringDescription : "";
        }
    }
    private boolean isDescriptionInheritable = false;
    private boolean isDescriptionInherited = false;
    private ILocalizedDescribable.Inheritable describableDef = null;
    private final ILocalizedDescribable localizedDescribable;
    private final Definition descriptionLocation;
    private String stringDescription;
//    private Map<EIsoLanguage, String> localizedDescription;
    private IMultilingualStringDef string = null;
    private EDescriptionType descriptionType;
    private boolean spellcheck = false;

    private DescriptionModel(Object definition) {
        assert definition instanceof ILocalizedDescribable && definition instanceof IDescribable;
        
        this.localizedDescribable = (ILocalizedDescribable)definition;
        final IDescribable describable = (IDescribable)definition;
        
        this.descriptionLocation = localizedDescribable.getDescriptionLocation();
        this.descriptionType = DescriptionHandleInfo.getEditorModeFor(definition);

        stringDescription = describable.getDescription();
        
        if (localizedDescribable instanceof ILocalizedDescribable.Inheritable) {
            describableDef = (ILocalizedDescribable.Inheritable) localizedDescribable;
            isDescriptionInheritable = describableDef.isDescriptionInheritable();
            if (isDescriptionInheritable) {
                isDescriptionInherited = describableDef.isDescriptionInherited();
            }
            
            if (!isDescriptionInherited) {
                loadStringValues(localizedDescribable);
            }
            
            
        } else {
            loadStringValues(localizedDescribable);
            isDescriptionInheritable = false;
        }
        spellcheck = isSpellcheckEnabled(localizedDescribable);
    }
    
    private void loadStringValues(ILocalizedDescribable localizedDescribable) {
        if (localizedDescribable.getDescriptionId() != null) {
            final IMultilingualStringDef localizedString = localizedDescribable.getDescriptionLocation().findLocalizedString(localizedDescribable.getDescriptionId());
            if (localizedString != null) {
                string = localizedString.cloneString(null);
            }
        }
        createNewString();

    }
    
    private void createNewString(){
        if (string == null) {
            string = AdsMultilingualStringDef.Factory.newDescriptionInstance();
        }
    }

    @Override
    public String getDescription() {
        return stringDescription != null ? stringDescription : "";
    }

    @Override
    public void setDescription(String description) {
        this.stringDescription = description;
    }

    @Override
    public Id getDescriptionId() {
        if(describableDef != null){
            return describableDef.getDescriptionId(isDescriptionInherited);
        }
        return localizedDescribable.getDescriptionId();
    }

    @Override
    public Id getDescriptionId(boolean inherited) {
        return getDescriptionId();
    }


    @Override
    public void setDescriptionId(Id id) {
        if (id == null){
            string = null;
        }
    }

    @Override
    public String getDescription(EIsoLanguage language) {
        if (isDescriptionInherited()){
            return getDescriptionLocation().getDescription(language);
        }
        return string == null ? null : string.getValue(language);
    }

    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        if (!isDescriptionInherited()){
            createNewString();
            string.setValue(language, description);
        }
        return true;
    }

    @Override
    public Definition getDescriptionLocation() {
        if(describableDef != null){
            return describableDef.getDescriptionLocation(isDescriptionInherited);
        }
        return descriptionLocation;
    }
    
    @Override
    public Definition getDescriptionLocation(boolean inherited) {
        return getDescriptionLocation();
    }

    public boolean isSpellcheckEnabled() {
        return spellcheck;
    }

    public void setSpellcheckEnabled(boolean spellcheck) {
        this.spellcheck = spellcheck;
    }

    public EDescriptionType getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(EDescriptionType descriptionType) {
        this.descriptionType = descriptionType;
    }

    public boolean applyFor(IDescribable object) {
        if (object != null) {
            object.setDescription(getDescription());
            return true;
        }
        return false;
    }

    public boolean applyFor(ILocalizedDescribable object) {
        if (object != null) {
            if (object instanceof ILocalizedDescribable.Inheritable) {
                ILocalizedDescribable.Inheritable def = (ILocalizedDescribable.Inheritable) object;
                def.setDescriptionInherited(isDescriptionInherited);
            } 
            
            if (!(object instanceof ILocalizedDescribable.Inheritable) || !isDescriptionInherited()) {
                if (string != null) {
                    IMultilingualStringDef localizedString = null;
                    
                    if (object.getDescriptionId() != null) {
                        localizedString = object.getDescriptionLocation().findLocalizedString(object.getDescriptionId());
                    }
                    
                    if (localizedString != null) {
                        localizedString.setSpellCheckEnabled(spellcheck);
                    }
                    List<IMultilingualStringDef.StringStorage> values = string.getValues(ExtendableDefinitions.EScope.LOCAL);
                    for (IMultilingualStringDef.StringStorage storage : values) {
                        if (localizedString == null || !storage.getValue().equals(localizedString.getValue(storage.getLanguage()))){
                            if (!object.setDescription(storage.getLanguage(), storage.getValue())) {
                                return false;
                            }
                        }
                    }
                } else {
                    object.setDescriptionId(null);
                }
            }

            return true;
        }
        return false;
    }

    public <T extends Object & IDescribable & ILocalizedDescribable> boolean applyFor(T object) {
        return applyFor((ILocalizedDescribable) object) && applyFor((IDescribable) object);
    }

    private boolean isSpellcheckEnabled(ILocalizedDescribable object) {
        if (object != null && object.getDescriptionId() != null) {
            final IMultilingualStringDef localizedString = object.getDescriptionLocation().findLocalizedString(object.getDescriptionId());

            if (localizedString != null) {
                return localizedString.isSpellCheckEnabled();
            }
        }
        return false;
    }
    
    @Override
    public boolean isDescriptionInheritable() {
        return isDescriptionInheritable;
    }

    @Override
    public boolean isDescriptionInherited() {
        if (isDescriptionInheritable()){
            return isDescriptionInherited;
        }
        return false;
    }

    @Override
    public void setDescriptionInherited(boolean inherit) {
        if (isDescriptionInheritable()){
            if (isDescriptionInherited != inherit) {
                isDescriptionInherited = inherit;
                if (!isDescriptionInherited){
                    Id stringId = getDescriptionId();
                    if (stringId != null) {
                        final IMultilingualStringDef localizedString = getDescriptionLocation().findLocalizedString(stringId);
                        if (localizedString != null) {
                            string = localizedString.cloneString(null);
                        }
                    }
                    if (string == null) {
                        string = AdsMultilingualStringDef.Factory.newDescriptionInstance();
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        List<IMultilingualStringDef.StringStorage> values = null;
        if (isDescriptionInherited()){
            Id stringId = getDescriptionId();
            if (stringId != null) {
                final IMultilingualStringDef localizedString = getDescriptionLocation().findLocalizedString(stringId);
                if (localizedString != null) {
                    values = localizedString.getValues(ExtendableDefinitions.EScope.LOCAL);
                }
            }
        } else {
            if (string != null){
                values = string.getValues(ExtendableDefinitions.EScope.LOCAL);
            }
        }
        return toString(getDescriptionType(), values, stringDescription);
    }

    
}
