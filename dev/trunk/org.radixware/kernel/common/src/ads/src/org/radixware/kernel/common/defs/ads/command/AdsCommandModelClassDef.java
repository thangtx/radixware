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

package org.radixware.kernel.common.defs.ads.command;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.radixdoc.DynamicClassRadixdoc;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class AdsCommandModelClassDef extends AdsDynamicClassDef {

    @Override
    public EClassType getClassDefType() {
        return EClassType.COMMAND_MODEL;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.COMMAND;
    }

    public AdsCommandModelClassDef(AdsModelClassDef owner, ClassDefinition xDef) {
        super(Id.Factory.changePrefix(xDef.getId(), EDefinitionIdPrefix.ADS_COMMAND_MODEL_CLASS), xDef);

    }

    public AdsCommandModelClassDef(AdsModelClassDef owner, AdsCommandDef command) {
        super(Id.Factory.changePrefix(command.getId(), EDefinitionIdPrefix.ADS_COMMAND_MODEL_CLASS), command.getName(), owner.getUsageEnvironment());
    }

    public AdsCommandDef findCommand() {

        AdsModelClassDef ownerModel = findOwnerModelClass();
        if (ownerModel == null) {
            return null;
        }
        AdsPresentationDef ownerPresentation = null;
        if (ownerModel instanceof AdsEntityModelClassDef) {
            ownerPresentation = ((AdsEntityModelClassDef) ownerModel).getOwnerEditorPresentation();
        } else if (ownerModel instanceof AdsGroupModelClassDef) {
            ownerPresentation = ((AdsGroupModelClassDef) ownerModel).getOwnerSelectorPresentation();
        }
        if (ownerPresentation != null) {
            Id contextCmdId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.COMMAND);
            AdsScopeCommandDef command = ownerPresentation.getCommandsLookup().findById(contextCmdId, ExtendableDefinitions.EScope.ALL).get();
            if (command != null) {
                return command;
            }
        }
        Id clsCmdId = Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.CONTEXTLESS_COMMAND);
        return AdsSearcher.Factory.newAdsContextlessCommandSearcher(getModule()).findById(clsCmdId).get();
    }

    public AdsModelClassDef findOwnerModelClass() {
        for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof AdsModelClassDef) {
                return (AdsModelClassDef) obj;
            }
        }
        return null;
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        AdsModelClassDef model = findOwnerModelClass();
        if (model == null) {
            return ERuntimeEnvironmentType.COMMON_CLIENT;
        } else {
            return model.getUsageEnvironment();
        }

    }
    public static final Id EXEC_METHOD_ID = Id.Factory.loadFrom("mthCmdClassExecute___________");

    public void sync() {
        //create reqired constructor 
        AdsUserMethodDef method = findDefaultConstructor();
        if (method == null) {
            method = AdsUserMethodDef.Factory.newConstructorInstance();
            method.getProfile().getParametersList().add(MethodParameter.Factory.newInstance("model", AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.client.models.Model")));
            method.getProfile().getParametersList().add(MethodParameter.Factory.newInstance("command", AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.client.meta.RadCommandDef")));
            method.setUsageEnvironment(ERuntimeEnvironmentType.COMMON_CLIENT);
            method.getSource().getItems().add(Scml.Text.Factory.newInstance("super(model,command);"));
            getMethods().getLocal().add(method);
        } else {
            method.getAccessFlags().setPublic();
            method.getProfile().getThrowsList().clear();

        }
        /* AdsMethodDef exec = findExecuteMethod();

         boolean createLocal = true;
         if (exec != null) {
         if (!isValidExecMethodProfile(exec)) {
         if (exec.getOwnerClass() == this) {
         exec.getProfile().getParametersList().clear();
         exec.getProfile().getParametersList().add(MethodParameter.Factory.newInstance("propertyId", getExecPropIdParamType()));
         exec.getProfile().getThrowsList().clear();
         exec.getAccessFlags().setPublic();
         createLocal = false;
         }
         } else {
         createLocal = false;
         }
         }
         if (createLocal) {
         AdsUserMethodDef newExec = AdsUserMethodDef.Factory.newPredefinedInstance(EXEC_METHOD_ID, "execute");
         newExec.getProfile().getParametersList().add(MethodParameter.Factory.newInstance("propertyId", getExecPropIdParamType()));
         getMethods().getLocal().add(newExec);
         }*/
    }

    private AdsTypeDeclaration getExecPropIdParamType() {
        return AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.types.Id");
    }

    private AdsTypeDeclaration[] getConstructorParamTypes() {
        return new AdsTypeDeclaration[]{AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.client.models.Model"),
            AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.client.meta.RadCommandDef")
        };
    }

//    private boolean isValidExecMethodProfile(AdsMethodDef exec) {
//        return exec.getAccessFlags().isPublic()
//                && exec.getProfile().getParametersList().size() == 1
//                && AdsTypeDeclaration.equals(this, getExecPropIdParamType(), exec.getProfile().getParametersList().get(0).getType())
//                && exec.getProfile().getThrowsList().isEmpty();
//    }

    private boolean isMatchedConstructorProfile(AdsMethodDef init) {
        AdsTypeDeclaration[] constructorProfile = getConstructorParamTypes();
        AdsMethodDef.Profile profile = init.getProfile();
        return profile.getParametersList().size() == 2
                && AdsTypeDeclaration.equals(this, profile.getParametersList().get(0).getType(), constructorProfile[0])
                && AdsTypeDeclaration.equals(this, profile.getParametersList().get(1).getType(), constructorProfile[1]);
    }

    private boolean isValidConstructorProfile(AdsMethodDef init) {
        return init.getAccessFlags().isPublic()
                && isMatchedConstructorProfile(init)
                && init.getProfile().getThrowsList().isEmpty();
    }

    private AdsUserMethodDef findDefaultConstructor() {
        for (AdsMethodDef method : getMethods().getLocal()) {
            if (method instanceof AdsUserMethodDef && method.isConstructor()) {
                if (isMatchedConstructorProfile(method)) {
                    return (AdsUserMethodDef) method;
                }
            }
        }
        AdsClassDef clazz = getHierarchy().findOverwritten().get();
        if (clazz instanceof AdsCommandModelClassDef) {
            return ((AdsCommandModelClassDef) clazz).findDefaultConstructor();
        }
        return null;
    }

    private AdsMethodDef findExecuteMethod() {
        return getMethods().findById(EXEC_METHOD_ID, ExtendableDefinitions.EScope.ALL).get();
    }

    public void check(IProblemHandler problemHandler) {
        AdsMethodDef init = findDefaultConstructor();
        if (init == null || !isValidConstructorProfile(init)) {
            AdsTypeDeclaration[] cp = getConstructorParamTypes();
            problemHandler.accept(RadixProblem.Factory.newError(this, "Command model class must define public constructor " + getName() + " (" + cp[0].getQualifiedName(this) + ", " + cp[1].getQualifiedName() + ") with no exception thrown"));
        }
        /* AdsMethodDef execMethod = findExecuteMethod();
         if (execMethod == null || !isValidExecMethodProfile(execMethod)) {
         AdsTypeDeclaration propIdParamType = getExecPropIdParamType();
         problemHandler.accept(RadixProblem.Factory.newError(this, "Command model class must define method execute(" + propIdParamType.getQualifiedName(this) + ") with no exception thrown"));
         }*/
    }

    @Override
    public boolean isDual() {
        return getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DynamicClassRadixdoc(getSource(), page, options) {
                    @Override
                    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
                        super.writeClassDefInfo(overview, overviewTable);

                        AdsCommandModelClassDef commClass = (AdsCommandModelClassDef) source;
                        getClassWriter().addStr2RefRow(overviewTable, "Handled command", commClass.findCommand(), source);
                    }
                };
            }
        };
    }
}
