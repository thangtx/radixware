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
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDescribable;

import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;


public final class DescriptionModel implements IDescribable, ILocalizedDescribable {

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
    
    static String toString(EDescriptionType editorMode, Map<EIsoLanguage, String> localizedDescription, String stringDescription) {
        if (editorMode == EDescriptionType.LOCALIZED) {

            if (localizedDescription == null) {
                return "";
            } else {
                final StringBuilder builder = new StringBuilder();
                boolean first = true;
                for (final EIsoLanguage lang : localizedDescription.keySet()) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append("; ");
                    }
                    builder.append(lang.getName()).append(": ").append(localizedDescription.get(lang));
                }
                return builder.toString();
            }
        } else {
            return stringDescription != null ? stringDescription : "";
        }
    }
    private final Definition descriptionLocation;
    private String stringDescription;
    private Map<EIsoLanguage, String> localizedDescription;
    private EDescriptionType descriptionType;
    private boolean spellcheck = false;

    public DescriptionModel(Definition descriptionLocation, String stringDescription) {
        this.descriptionLocation = descriptionLocation;
        this.descriptionType = EDescriptionType.STRING;
        this.stringDescription = stringDescription;
    }

    public DescriptionModel(Definition descriptionLocation, Map<EIsoLanguage, String> localizedDescription) {
        this.descriptionLocation = descriptionLocation;
        this.descriptionType = EDescriptionType.LOCALIZED;
        this.localizedDescription = localizedDescription != null ? new HashMap<>(localizedDescription) : null;
        spellcheck = true;
    }

    private DescriptionModel(Object definition) {
        assert definition instanceof ILocalizedDescribable && definition instanceof IDescribable;
        
        final ILocalizedDescribable localizedDescribable = (ILocalizedDescribable)definition;
        final IDescribable describable = (IDescribable)definition;
        
        this.descriptionLocation = localizedDescribable.getDescriptionLocation();
        this.descriptionType = DescriptionHandleInfo.getEditorModeFor(definition);

        stringDescription = describable.getDescription();
        if (localizedDescribable.getDescriptionId() != null) {
            createDescription();

            final IMultilingualStringDef localizedString = localizedDescribable.getDescriptionLocation().findLocalizedString(localizedDescribable.getDescriptionId());
            if (localizedString != null) {
                for (final EIsoLanguage lang : localizedString.getLanguages()) {
                    localizedDescription.put(lang, localizedString.getValue(lang));
                }
            }

            spellcheck = isSpellcheckEnabled(localizedDescribable);
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
        return null;
    }

    @Override
    public void setDescriptionId(Id id) {
    }

    @Override
    public String getDescription(EIsoLanguage language) {
        return localizedDescription != null ? localizedDescription.get(language) : null;
    }

    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        if (localizedDescription != null) {
            localizedDescription.put(language, description);
            return true;
        }
        return false;
    }

    @Override
    public Definition getDescriptionLocation() {
        return descriptionLocation;
    }

    @Override
    public String toString() {
        return toString(getDescriptionType(), localizedDescription, stringDescription);
    }

    public boolean isSpellcheckEnabled() {
        return spellcheck;
    }

    public void setSpellcheckEnabled(boolean spellcheck) {
        this.spellcheck = spellcheck;
    }

    public void removeDescription() {
        localizedDescription = null;
        spellcheck = false;
    }

    public void createDescription() {
        localizedDescription = new EnumMap<>(EIsoLanguage.class);
        spellcheck = true;
    }

    public Map<EIsoLanguage, String> getLocalizedDescriptions() {
        return localizedDescription != null ? new EnumMap<>(localizedDescription) : null;
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

            final Map<EIsoLanguage, String> localizedDescriptions = getLocalizedDescriptions();
            if (localizedDescriptions != null) {
                for (final EIsoLanguage lang : localizedDescriptions.keySet()) {
                    if (!object.setDescription(lang, localizedDescriptions.get(lang))) {
                        return false;
                    }
                }
            } else {
                object.setDescriptionId(null);
            }

            if (object.getDescriptionId() != null) {
                final IMultilingualStringDef localizedString = object.getDescriptionLocation().findLocalizedString(object.getDescriptionId());

                if (localizedString != null) {
                    localizedString.setSpellCheckEnabled(spellcheck);
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
}
