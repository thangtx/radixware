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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.ui.ICustomDialog;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomPropEditorDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPropertyValueStorePossibility;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;


public abstract class EditOptions extends RadixObject implements ILocalizedDef {
    
    private Id customDialogId;
    private Id webCustomDialogId;
    private boolean customEditOnly;
    private EditMask editMask;
    private boolean isMemo;
    private boolean isNotNull;
    private Id nullValTitleId;
    private Id emptyArrValTitleId;
    private boolean showDialogButton;
    private boolean storeEditHistory;
    private ERuntimeEnvironmentType editEnv = null;
    private EPropertyValueStorePossibility storePossibility = EPropertyValueStorePossibility.NONE;
    
    protected EditOptions(RadixObject context, EditOptions source) {
        setContainer(context);
        this.customDialogId = source.customDialogId;
        this.webCustomDialogId = source.webCustomDialogId;
        this.customEditOnly = source.customEditOnly;
        this.editMask = source.editMask;
        this.isMemo = source.isMemo;
        this.isNotNull = source.isNotNull;
        this.nullValTitleId = source.nullValTitleId;
        this.showDialogButton = source.showDialogButton;
        this.storeEditHistory = source.storeEditHistory;
        this.storePossibility = source.storePossibility;
    }
    
    protected EditOptions(RadixObject context, org.radixware.schemas.adsdef.EditOptions xEditing) {
        setContainer(context);
        if (xEditing == null) {
            this.customDialogId = null;
            this.customEditOnly = false;
            this.editMask = null;
            this.isMemo = false;
            this.isNotNull = false;
            this.nullValTitleId = null;
            this.emptyArrValTitleId = null;
            this.showDialogButton = false;
            this.storeEditHistory = false;
            this.webCustomDialogId = null;
        } else {
            this.customDialogId = xEditing.getCustomDialogId();
            this.customEditOnly = xEditing.isSetCustomEditOnly() ? xEditing.getCustomEditOnly() : false;
            this.editMask = EditMask.Factory.loadFrom(this, xEditing.getEditMask());
            this.isMemo = xEditing.isSetIsMemo() ? xEditing.getIsMemo() : false;
            this.isNotNull = xEditing.isSetNotNull() ? xEditing.getNotNull() : false;
            this.nullValTitleId = xEditing.getNullValTitleId();
            this.showDialogButton = xEditing.isSetShowDialogButton() ? xEditing.getShowDialogButton() : false;
            this.emptyArrValTitleId = xEditing.getEmptyValTitleId();
            this.storeEditHistory = xEditing.isSetStoreEditHistory() ? xEditing.getStoreEditHistory() : false;
            this.webCustomDialogId = xEditing.getWebCustomDialogId();
            if (xEditing.isSetEditEnvironment()) {
                this.editEnv = xEditing.getEditEnvironment();
            }
            if (xEditing.isSetValueStorePossibility()) {
                this.storePossibility = xEditing.getValueStorePossibility();
            }
        }
    }
    
    private PropertyPresentation getOwnerPropertyPresentation() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof PropertyPresentation) {
                return (PropertyPresentation) owner;
            }
        }
        return null;
    }
    
    public boolean isStoreEditHistory() {
        return storeEditHistory;
    }
    
    public void setStoreEditHistory(boolean storeEditHistory) {
        this.storeEditHistory = storeEditHistory;
        setEditState(EEditState.MODIFIED);
    }
    
    public Id getCustomDialogId(ERuntimeEnvironmentType env) {
        if (env == ERuntimeEnvironmentType.WEB) {
            return webCustomDialogId;
        } else {
            return customDialogId;
        }
    }
    
    public ICustomDialog findCustomDialog(ERuntimeEnvironmentType env) {
        final Id customDialogId = getCustomDialogId(env);
        if (customDialogId == null) {
            return null;
        }
        AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(getOwnerDefinition()).findById(customDialogId).get();
        if (env == ERuntimeEnvironmentType.EXPLORER) {
            if (def instanceof AdsCustomPropEditorDef) {
                return (AdsCustomPropEditorDef) def;
            } else {
                return null;
            }
        } else if (env == ERuntimeEnvironmentType.WEB) {
            if (def instanceof AdsRwtCustomPropEditorDef) {
                return (AdsRwtCustomPropEditorDef) def;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
    public ERuntimeEnvironmentType getEditEnvironment() {
        PropertyPresentation pres = getOwnerPropertyPresentation();
        if (pres == null) {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        }
        AdsPropertyDef ownerProp = pres.getOwnerProperty();
        if (ownerProp == null) {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        }
        ERuntimeEnvironmentType propEnv = ownerProp.getClientEnvironment();
        if (propEnv.isClientEnv()) {
            if (propEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return editEnv == null ? ERuntimeEnvironmentType.COMMON_CLIENT : editEnv;
            } else {
                return propEnv;
            }
        } else {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        }
    }
    
    public boolean canChangeEditEnvironment() {
        PropertyPresentation pres = getOwnerPropertyPresentation();
        if (pres == null) {
            return false;
        }
        AdsPropertyDef ownerProp = pres.getOwnerProperty();
        if (ownerProp == null) {
            return false;
        }
        ERuntimeEnvironmentType propEnv = ownerProp.getClientEnvironment();
        if (propEnv.isClientEnv()) {
            if (propEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public void setEditEnvironment(ERuntimeEnvironmentType env) {
        PropertyPresentation pres = getOwnerPropertyPresentation();
        if (pres == null) {
            return;
        }
        AdsPropertyDef ownerProp = pres.getOwnerProperty();
        if (ownerProp == null) {
            return;
        }
        ERuntimeEnvironmentType propEnv = ownerProp.getClientEnvironment();
        if (propEnv.isClientEnv()) {
            if (propEnv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                ERuntimeEnvironmentType valueToSet = env == ERuntimeEnvironmentType.COMMON_CLIENT ? null : env;
                if (this.editEnv != valueToSet) {
                    this.editEnv = valueToSet;
                    setEditState(EEditState.MODIFIED);
                }
            }
        }
    }
    
    public void setCustomDialogId(ERuntimeEnvironmentType env, Id customDialogId) {
        if (env == ERuntimeEnvironmentType.EXPLORER) {
            this.customDialogId = customDialogId;
        } else if (env == ERuntimeEnvironmentType.WEB) {
            this.webCustomDialogId = customDialogId;
        } else {
            return;
        }
        setEditState(EEditState.MODIFIED);
    }
    
    public boolean isCustomEditOnly() {
        return customEditOnly;
    }
    
    public void setCustomEditOnly(boolean customEditOnly) {
        this.customEditOnly = customEditOnly;
        setEditState(EEditState.MODIFIED);
    }
    
    public boolean isMemo() {
        return isMemo;
    }
    
    public void setMemo(boolean isMemo) {
        this.isMemo = isMemo;
        setEditState(EEditState.MODIFIED);
    }
    
    public boolean isNotNull() {
        return isNotNull;
    }
    
    public void setNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
        setEditState(EEditState.MODIFIED);
    }
    
    public boolean isShowDialogButton() {
        return showDialogButton;
    }
    
    public void setShowDialogButton(boolean showDialogButton) {
        this.showDialogButton = showDialogButton;
        setEditState(EEditState.MODIFIED);
    }

    /**
     * Sets markup for property with null value for given language Returns true
     * if title suscsessfully updated
     */
    public boolean setNullValTitle(EIsoLanguage language, String title) {
        nullValTitleId = getOwnerPropertyPresentation().getOwnerProperty().setLocalizedStringValue(language, nullValTitleId, title);
        return nullValTitleId != null;
    }

    /**
     * *
     * Returns edit mask descriptor If no editmask is specified returns null
     */
    public EditMask getEditMask() {
        return editMask;
    }
    
    public abstract IAdsTypedObject getTypedObject();
    
    public EPropertyValueStorePossibility getValueStorePossibility() {
        return storePossibility;
    }
    
    public void setValueStorePossibility(EPropertyValueStorePossibility storePossibility) {
        if (this.storePossibility != storePossibility) {
            this.storePossibility = storePossibility;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public boolean setEditMaskType(EEditMaskType maskType) {
        if (editMask != null && editMask.getType() == maskType) {
            return false;
        }
        if (maskType == null) {
            
            editMask = null;
        } else {
            editMask = EditMask.Factory.newInstance(this, maskType);
        }
        setEditState(EEditState.MODIFIED);
        return true;
    }

    /**
     * Returns value markup string for property with null value for given
     * language. if not defined or could not be determined returns null
     */
    public String getNullValTitle(EIsoLanguage language) {
        if (nullValTitleId != null) {
            AdsLocalizingBundleDef bundle = getOwnerPropertyPresentation().findLocalizingBundle();
            if (bundle != null) {
                AdsMultilingualStringDef string = bundle.getStrings().findById(nullValTitleId, EScope.LOCAL_AND_OVERWRITE).get();
                if (string != null) {
                    return string.getValue(language);
                }
            }
        }
        return null;
    }
    
    public void appendTo(org.radixware.schemas.adsdef.EditOptions xDef) {
        if (customDialogId != null) {
            xDef.setCustomDialogId(customDialogId);
        }
        if (webCustomDialogId != null) {
            xDef.setWebCustomDialogId(webCustomDialogId);
        }
        if (customEditOnly) {
            xDef.setCustomEditOnly(customEditOnly);
        }
        if (isMemo) {
            xDef.setIsMemo(isMemo);
        }
        if (isNotNull) {
            xDef.setNotNull(isNotNull);
        }
        if (nullValTitleId != null) {
            xDef.setNullValTitleId(this.nullValTitleId);
        }
        if (emptyArrValTitleId != null) {
            xDef.setEmptyValTitleId(this.emptyArrValTitleId);
        }
        if (storeEditHistory) {
            xDef.setStoreEditHistory(storeEditHistory);
        }
        if (showDialogButton) {
            xDef.setShowDialogButton(showDialogButton);
        }
        if (this.editMask != null) {
            this.editMask.appendTo(xDef.addNewEditMask());
        }
        if (this.editEnv != null && this.editEnv != ERuntimeEnvironmentType.COMMON_CLIENT) {
            xDef.setEditEnvironment(editEnv);
        }
        if (storePossibility != null && storePossibility != EPropertyValueStorePossibility.NONE) {
            xDef.setValueStorePossibility(storePossibility);
        }
    }
    
    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (this.editMask != null) {
            this.editMask.visit(visitor, provider);
        }
    }
    
    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        ICustomDialog view = findCustomDialog(ERuntimeEnvironmentType.EXPLORER);
        if (view != null) {
            list.add(view.getDialogDef());
        }
        view = findCustomDialog(ERuntimeEnvironmentType.WEB);
        if (view != null) {
            list.add(view.getDialogDef());
        }
        
        if (nullValTitleId != null) {
            AdsLocalizingBundleDef bundle = getOwnerDefinition().findLocalizingBundle();
            if (bundle != null) {
                AdsMultilingualStringDef string = bundle.getStrings().findById(nullValTitleId, EScope.LOCAL_AND_OVERWRITE).get();
                if (string != null) {
                    list.add(string);
                }
            }
        }
        if (emptyArrValTitleId != null) {
            AdsLocalizingBundleDef bundle = getOwnerDefinition().findLocalizingBundle();
            if (bundle != null) {
                AdsMultilingualStringDef string = bundle.getStrings().findById(emptyArrValTitleId, EScope.LOCAL_AND_OVERWRITE).get();
                if (string != null) {
                    list.add(string);
                }
            }
        }
        
    }
    
    public Id getNullValTitleId() {
        return nullValTitleId;
    }
    
    public void setNullValTitleId(Id id) {
        nullValTitleId = id;
        setEditState(EEditState.MODIFIED);
    }
    
    public Id getEmptyArrayValTitleId() {
        return emptyArrValTitleId;
    }
    
    public void setEmptyArrayValTitleId(Id id) {
        emptyArrValTitleId = id;
        setEditState(EEditState.MODIFIED);
    }
    
    @Override
    public String getName() {
        return "Edit Options";
    }
    
    @Override
    public AdsDefinition getOwnerDefinition() {
        return (AdsDefinition) super.getOwnerDefinition();
    }
    
    @Override
    public void collectUsedMlStringIds(Collection<MultilingualStringInfo> ids) {
        ids.add(new MultilingualStringInfo(EditOptions.this) {
            @Override
            public Id getId() {
                return nullValTitleId;
            }
            
            @Override
            public void updateId(Id newId) {
                nullValTitleId = newId;
            }
            
            @Override
            public EAccess getAccess() {
                PropertyPresentation pp = getOwnerPropertyPresentation();
                if (pp == null) {
                    return EAccess.DEFAULT;
                }
                AdsPropertyDef def = pp.getOwnerProperty();
                if (def == null) {
                    return EAccess.DEFAULT;
                }
                return def.getAccessMode();
            }
            
            @Override
            public String getContextDescription() {
                return "Title for Empty Value";
            }
            
            @Override
            public boolean isPublished() {
                PropertyPresentation pp = getOwnerPropertyPresentation();
                if (pp == null) {
                    return false;
                }
                AdsPropertyDef def = pp.getOwnerProperty();
                if (def == null) {
                    return false;
                }
                return def.isPublished();
            }
        });
        ids.add(new MultilingualStringInfo(EditOptions.this) {
            @Override
            public Id getId() {
                return emptyArrValTitleId;
            }
            
            @Override
            public void updateId(Id newId) {
                emptyArrValTitleId = newId;
            }
            
            @Override
            public EAccess getAccess() {
                PropertyPresentation pp = getOwnerPropertyPresentation();
                if (pp == null) {
                    return EAccess.DEFAULT;
                }
                AdsPropertyDef def = pp.getOwnerProperty();
                if (def == null) {
                    return EAccess.DEFAULT;
                }
                return def.getAccessMode();
            }
            
            @Override
            public String getContextDescription() {
                return "Empty Array Title";
            }
            
            @Override
            public boolean isPublished() {
                PropertyPresentation pp = getOwnerPropertyPresentation();
                if (pp == null) {
                    return false;
                }
                AdsPropertyDef def = pp.getOwnerProperty();
                if (def == null) {
                    return false;
                }
                return def.isPublished();
            }
        });
    }
    
    @Override
    public AdsMultilingualStringDef findLocalizedString(Id stringId) {
        return getOwnerDefinition().findLocalizedString(stringId);
    }
}
