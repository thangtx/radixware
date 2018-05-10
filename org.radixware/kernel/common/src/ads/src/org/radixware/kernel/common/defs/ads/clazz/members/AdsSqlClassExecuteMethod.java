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

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsDescriptionDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ELocalizedStringKind;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.environment.IMlStringBundle;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;


public class AdsSqlClassExecuteMethod extends AdsSqlMethodDef implements ILocalizedDescribable.ILocalizedCalculatedDef{
    private class AdsSqlClassExecuteMethodParameter extends MethodParameter implements ILocalizedDescribable.ILocalizedCalculatedDef{
        public AdsSqlClassExecuteMethodParameter() {
            super("", "", null, AdsTypeDeclaration.UNDEFINED, false);
        }

        private AdsPropertyDef findSqlClassParameter() {
            final AdsSqlClassDef sqlClass = getOwnerClass();
            if (sqlClass == null) {
                return null;
            }
            final List<AdsParameterPropertyDef> inputParams = sqlClass.getInputParameters();
            int idx = getProfile().getParametersList().indexOf(this);
            if (idx >= 0 && idx < inputParams.size()) {
                return inputParams.get(idx);
            } else {
                return null;
            }
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.CALC;
        }

        @Override
        public Id getId() {
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                return Id.Factory.changePrefix(prop.getId(), EDefinitionIdPrefix.ADS_METHOD_PARAMETER);
            } else {
                return super.getId();
            }
        }

        @Override
        public String getName() {
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                return prop.getName();
            } else {
                return super.getName();
            }
        }

        @Override
        public boolean setName(String newName) {
            final boolean result = super.setName(newName);

            // delegate to property
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                prop.setName(newName);
            }

            return result;
        }

        @Override
        public String getDescription() {
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                return prop.getDescription();
            } else {
                return super.getDescription();
            }
        }

        @Override
        public void setDescription(String description) {
            super.setDescription(description);

            // delegate to property
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                prop.setDescription(description);
            }
        }
    
        @Override
        public AdsTypeDeclaration getType() {
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                return prop.getValue().getType();
            } else {
                return super.getType();
            }
        }

        @Override
        public boolean isTypeAllowed(EValType type) {
            return ValTypes.ADS_SQL_CLASS_PARAM_TYPES.contains(type);
        }

        @Override
        public void setType(AdsTypeDeclaration type) {
            super.setType(type);

            // delegate to property
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                prop.getValue().setType(type);
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            final AdsPropertyDef prop = findSqlClassParameter();
            if (prop != null) {
                list.add(prop);
            }
        }

        @Override
        public void setDescriptionId(Id id) {
            Id calcId = getDescriptionCalculatedId();
            if (id != null){
                ILocalizingBundleDef bundleDef = getDescriptionLocation().findLocalizingBundle();
                if (bundleDef.getStrings().findById(calcId, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get() == null){
                    AdsMultilingualStringDef adsMultilingualStringDef = AdsMultilingualStringDef.Factory.newDescriptionInstance(calcId);
                    bundleDef.getStrings().getLocal().add(adsMultilingualStringDef);
                }
                super.setDescriptionId(calcId);
            } else { 
                removeDescriptionString();
                super.setDescriptionId(id); 
            }
        }

        @Override
        public Id getDescriptionId() {
            return getDescriptionCalculatedId();
        }
        
        @Override
        public Id getDescriptionCalculatedId(){
            return Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.ADS_LOCALIZED_STRING);
        }

        @Override
        public boolean isDescriptionIdCalculated() {
            return true;
        }
        
        @Override
        public boolean isDescriptionIdChanged(){
            return (!isDescriptionIdCalculated() && super.isDescriptionIdChanged()) || (isDescriptionIdCalculated()&& getDescriptionLocation().findLocalizedString(getDescriptionCalculatedId()) != null);
        }

        @Override
        public boolean isDescriptionInheritable() {
            return false;
        }
    }

    private class AdsSqlClassExecuteMethodReturnValue extends MethodReturnValue implements ILocalizedDescribable.ILocalizedCalculatedDef{

        public AdsSqlClassExecuteMethodReturnValue(AdsMethodDef md) {
            super(md);
        }

        public AdsSqlClassExecuteMethodReturnValue(AdsMethodDef md, AbstractMethodDefinition.ReturnType xDef) {
            super(md, xDef);
        }

        @Override
        public Id getDescriptionCalculatedId() {
            return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZED_STRING.getValue() + getId().toString() + "return");
        }

        @Override
        public boolean isDescriptionIdCalculated() {
            return true;
        }

        @Override
        public void setDescriptionId(Id id) {
            Id calcId = getDescriptionCalculatedId();
            if (id != null){
                ILocalizingBundleDef bundleDef = getDescriptionLocation().findLocalizingBundle();
                if (bundleDef != null){
                    if (bundleDef.getStrings().findById(calcId, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get() == null){
                        AdsMultilingualStringDef adsMultilingualStringDef = AdsMultilingualStringDef.Factory.newDescriptionInstance(calcId);
                        bundleDef.getStrings().getLocal().add(adsMultilingualStringDef);
                    }  
                }
                super.setDescriptionId(calcId);
            } else { 
                removeDescriptionString();
                super.setDescriptionId(id); 
            }
        }

        @Override
        public Id getDescriptionId() {
            return getDescriptionCalculatedId();
        }
        
        @Override
        public boolean isDescriptionIdChanged(){
            return (!isDescriptionIdCalculated() && super.isDescriptionIdChanged()) || (isDescriptionIdCalculated()&& getDescriptionLocation().findLocalizedString(getDescriptionCalculatedId()) != null);
        }

        @Override
        public boolean isDescriptionInheritable() {
            return false;
        }
        
    }
    
    private class AdsSqlClassExecuteMethodProfile extends AdsMethodDef.Profile {
        private AdsSqlClassExecuteMethodReturnValue returnValue;
        public AdsSqlClassExecuteMethodProfile() {
            getAccessFlags().setStatic(true);
            returnValue = new AdsSqlClassExecuteMethodReturnValue(AdsSqlClassExecuteMethod.this);
        }
        
        private AdsTypeDeclaration calcReturnValueTypeDeclaration() {
            final AdsSqlClassDef sqlClass = getOwnerClass();

            switch (sqlClass.getClassDefType()) {
                case SQL_STATEMENT:
                case REPORT:
                    return AdsTypeDeclaration.VOID;
                default:
                    return AdsTypeDeclaration.Factory.newInstance(sqlClass);
            }
        }

        @Override
        public MethodReturnValue getReturnValue() {
            final AdsTypeDeclaration typeDeclaration = calcReturnValueTypeDeclaration();
            
            if (!Utils.equals(returnValue.getType(), typeDeclaration)) { 
                returnValue.setType(typeDeclaration);
            }

            return returnValue;
        }

        @Override
        public AdsMethodParameters getParametersList() {
            final AdsSqlClassDef sqlClass = getOwnerClass();

            final AdsMethodParameters params = super.getParametersList();
            final List<AdsParameterPropertyDef> inputParams = sqlClass.getInputParameters();

            for (MethodParameter param : params) {
                if (!(param instanceof AdsSqlClassExecuteMethodParameter)) {
                    params.clear();
                    break;
                }
            }

            while (params.size() > inputParams.size()) {
                params.remove(params.size() - 1);
            }

            while (params.size() < inputParams.size()) {
                final AdsSqlClassExecuteMethodParameter param = new AdsSqlClassExecuteMethodParameter();
                params.add(param);
            }

            return params;
        }

        @Override
        protected boolean isPersistent() {
            return false;
        }
    }
    private final AdsSqlClassExecuteMethodProfile profile;

    protected AdsSqlClassExecuteMethod(final String name) {
        super(AdsSystemMethodDef.ID_STMT_EXECUTE, name);
        this.profile = new AdsSqlClassExecuteMethodProfile();
    }

    protected AdsSqlClassExecuteMethod(AbstractMethodDefinition xMethod) {
        super(xMethod);
        this.profile = new AdsSqlClassExecuteMethodProfile();
    }

    @Override
    public AdsSqlClassDef getOwnerClass() {
        return (AdsSqlClassDef) super.getOwnerClass();
    }

    @Override
    public Profile getProfile() {
        final AdsSqlClassDef sqlClass = getOwnerClass();
        if (sqlClass != null && !sqlClass.getSqml().getItems().isEmpty()) {
            return profile;
        } else {
            return super.getProfile(); // RADIX-3889
        }
    }

    @Override
    public boolean canDelete() {
        return false;
    }

    @Override
    protected boolean isPersistent() {
        return false;
    }

    public void checkProfileState() {
        fireNameChange();
    }
    
    @Override
    public Id getDescriptionCalculatedId() {
        return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_LOCALIZED_STRING.getValue() + getId());
    }

    @Override
    public boolean isDescriptionIdCalculated() {
        Id id = super.getDescriptionId();
        return id == null || id == getDescriptionCalculatedId() || getDescriptionLocation().findLocalizedString(id) == null;
    }

    @Override
    public void setDescriptionId(Id id) {
        if (isDescriptionIdCalculated()){
            Id calcId = getDescriptionCalculatedId();           
            if (id != null){
                ILocalizingBundleDef bundleDef = getDescriptionLocation().findLocalizingBundle();
                if (bundleDef.getStrings().findById(calcId, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get() == null){
                    AdsMultilingualStringDef adsMultilingualStringDef = AdsMultilingualStringDef.Factory.newDescriptionInstance(calcId);
                    bundleDef.getStrings().getLocal().add(adsMultilingualStringDef);
                }
                super.setDescriptionId(calcId);
            } else { 
                final IMultilingualStringDef localizedString = getDescriptionLocation().findLocalizedString(calcId);
                if (localizedString != null) {
                    getDescriptionLocation().findLocalizingBundle().getStrings().getLocal().remove((RadixObject)localizedString);
                }
                super.setDescriptionId(id); 
            }
        } else{
            super.setDescriptionId(id);
        }
    }

    @Override
    public Id getDescriptionId() {
        if (isDescriptionIdCalculated()){
            return getDescriptionCalculatedId();
        } else {
            return super.getDescriptionId();
        }
    }
    
}
