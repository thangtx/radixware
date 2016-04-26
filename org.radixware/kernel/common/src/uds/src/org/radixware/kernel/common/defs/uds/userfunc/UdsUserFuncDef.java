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

package org.radixware.kernel.common.defs.uds.userfunc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.userfunc.AdsUserFuncWriter;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.ads.userfunc.IUserFuncDef;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.UdsSearcher;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument;
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition;
import org.radixware.schemas.adsdef.UserFuncProfile;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;
import org.radixware.schemas.udsdef.UdsDefinitionListDocument;
import org.radixware.schemas.udsdef.UserFunctionDefinition;
import org.radixware.schemas.xscml.JmlType;


public class UdsUserFuncDef extends UdsDefinition implements IEnvDependent, ICompilable, IJavaSource, IUserFuncDef, IJmlSource, IAdsTypeSource {

    private class InnerBundle extends AdsLocalizingBundleDef {

        public InnerBundle(Id ownerId) {
            super(ownerId);
        }

        @Override
        public boolean isSaveable() {
            return false;
        }
    }
    private final Jml source;
    private Id classId;
    private Id propId;
    private Id ownerClassId;
    private Id methodId;
    private Problems warningsSupport = null;
    private AdsMethodDef innerMethod;
    private AdsLocalizingBundleDef innerBundle;

    public static final class Problems extends AdsDefinitionProblems {

        private Problems(UdsUserFuncDef prop, List<Integer> warnings) {
            super(prop);
            if (warnings != null) {
                int arr[] = new int[warnings.size()];
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = warnings.get(i);
                }
                setSuppressedWarnings(arr);
            }
        }

        @Override
        public boolean canSuppressWarning(int code) {
            return super.canSuppressWarning(code);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static UdsUserFuncDef newInstance(String name, AdsUserPropertyDef property, AdsMethodDef method) {
            return new UdsUserFuncDef(name, property, method);
        }

        public static UdsUserFuncDef loadFrom(UserFunctionDefinition xDef, boolean importMode) {
            return new UdsUserFuncDef(xDef, importMode);
        }

        public static UdsUserFuncDef loadFrom(AdsUserFuncDefinition xDef, boolean importMode) {
            return new UdsUserFuncDef(xDef, importMode);
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }

    private UdsUserFuncDef(UserFunctionDefinition xDefinition, boolean importMode) {
        super(xDefinition);
        if (importMode && getId().getPrefix() != EDefinitionIdPrefix.LIB_USERFUNC_PREFIX) {
            setId(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS));
        }
        innerBundle = new InnerBundle(getId());
        if (xDefinition.isSetSource()) {
            this.source = Jml.Factory.loadFrom(this, xDefinition.getSource(), "source");
        } else {
            this.source = Jml.Factory.newInstance(this, "source");
        }

        this.ownerClassId = xDefinition.getOwnerClassId();
        this.propId = xDefinition.getPropId();
        this.classId = xDefinition.getClassId();
        this.methodId = xDefinition.getMethodId();
        if (xDefinition.getUserFuncProfile() != null) {
            checkInnerMethod(xDefinition.getUserFuncProfile());
        }
        if (xDefinition.isSetSuppressedWarnings()) {
            List<Integer> list = xDefinition.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = new Problems(this, list);
            }
        }
        if (xDefinition.getStrings() != null) {
            innerBundle.loadStrings(xDefinition.getStrings());
        }
    }

    private void checkInnerMethod(UserFuncProfile xProfile) {
        class InnerMethod extends AdsMethodDef {

            public InnerMethod(Id id, String name) {
                super(id, name);
                setContainer(UdsUserFuncDef.this);
            }

            @Override
            public EMethodNature getNature() {
                return EMethodNature.USER_DEFINED;
            }
        }
        innerMethod = new InnerMethod(this.methodId, xProfile.getMethodName());

        if (xProfile.getReturnType() != null && xProfile.getReturnType().getTypeId() != null) {
            innerMethod.getProfile().getReturnValue().setType(AdsTypeDeclaration.Factory.loadFrom(xProfile.getReturnType()));
        } else {
            innerMethod.getProfile().getReturnValue().setType(AdsTypeDeclaration.VOID);
        }

        if (xProfile.getParameters() != null) {
            for (org.radixware.schemas.adsdef.ParameterDeclaration xPDecl : xProfile.getParameters().getParameterList()) {
                innerMethod.getProfile().getParametersList().add(MethodParameter.Factory.loadFrom(innerMethod.getProfile().getParametersList(), xPDecl));
            }
        }

        if (xProfile.getThrownExceptions() != null) {
            for (UserFuncProfile.ThrownExceptions.Exception xEx : xProfile.getThrownExceptions().getExceptionList()) {
                innerMethod.getProfile().getThrowsList().add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(AdsTypeDeclaration.Factory.loadFrom(xEx)));
            }
        }
    }

    private UdsUserFuncDef(AdsUserFuncDefinition xDefinition, boolean importMode) {
        super(xDefinition);
        if (importMode && getId().getPrefix() != EDefinitionIdPrefix.LIB_USERFUNC_PREFIX) {
            setId(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS));
        }
        innerBundle = new InnerBundle(getId());
        if (xDefinition.isSetSource()) {
            this.source = Jml.Factory.loadFrom(this, xDefinition.getSource(), "source");
        } else {
            this.source = Jml.Factory.newInstance(this, "source");
        }


        this.ownerClassId = xDefinition.getOwnerClassId();
        this.propId = xDefinition.getPropId();
        this.classId = xDefinition.getClassId();
        this.methodId = xDefinition.getMethodId();

        if (xDefinition.getUserFuncProfile() != null) {
            checkInnerMethod(xDefinition.getUserFuncProfile());
        }
        if (xDefinition.isSetSuppressedWarnings()) {
            List<Integer> list = xDefinition.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = new Problems(this, list);
            }
        }
        if (xDefinition.getStrings() != null) {
            innerBundle.loadStrings(xDefinition.getStrings());
        }

    }

    private UdsUserFuncDef(String name, AdsUserPropertyDef property, AdsMethodDef method) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS), name);
        this.source = Jml.Factory.newInstance(this, "source");
        innerBundle = new InnerBundle(getId());
        if (property != null) {
            this.ownerClassId = property.getOwnerClass().getId();
            this.propId = property.getId();
        }
        this.classId = method.getOwnerClass().getId();

        this.methodId = method.getId();
    }

    @Override
    protected AdsLocalizingBundleDef findLocalizingBundleImpl(boolean createIfNotExists) {
        return innerBundle;
    }

    public Id getBaseClassId() {
        return classId;
    }

    public Id getOwnerClassId() {
        return ownerClassId;
    }

    public Id getPropId() {
        return propId;
    }

    public Id getMethodId() {
        return methodId;
    }

    @Override
    public Jml getSource() {
        return source;
    }

    public AdsClassDef findBaseClass() {
        return UdsSearcher.Factory.newAdsClassSearcher(this).findById(classId).get();
    }

    public AdsClassDef findOwnerClass() {
        return UdsSearcher.Factory.newAdsClassSearcher(this).findById(ownerClassId).get();
    }

    @Override
    public AdsMethodDef findMethod() {
        if (innerMethod != null) {
            return innerMethod;
        }
        AdsClassDef clazz = findBaseClass();
        if (clazz != null) {
            return clazz.getMethods().findById(methodId, EScope.ALL).get();
        } else {
            return null;
        }
    }

    public AdsUserPropertyDef findOwnerProperty() {
        AdsClassDef clazz = findOwnerClass();
        if (clazz != null) {
            AdsPropertyDef prop = clazz.getProperties().findById(propId, EScope.ALL).get();
            if (prop instanceof AdsUserPropertyDef) {
                return (AdsUserPropertyDef) prop;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        source.visit(visitor, provider);
        innerBundle.visit(visitor, provider);
    }

    @Override
    public void appendTo(UdsDefinitionDocument.UdsDefinition xDef) {
        appendTo(xDef.addNewUserFunc());
    }

    public void appendTo(UserFunctionDefinition xDef) {
        super.appendTo(xDef);
        xDef.setClassId(classId);
        xDef.setPropId(propId);
        xDef.setMethodId(methodId);
        xDef.setOwnerClassId(ownerClassId);


        if (innerMethod != null) {
            UserFuncProfile xProfile = xDef.addNewUserFuncProfile();
            innerMethod.getProfile().getReturnValue().getType().appendTo(xProfile.addNewReturnType());
            for (MethodParameter param : innerMethod.getProfile().getParametersList()) {
                UserFuncProfile.Parameters xParams = xProfile.ensureParameters();
                param.appendTo(xParams.addNewParameter());
            }
            for (AdsMethodThrowsList.ThrowsListItem item : innerMethod.getProfile().getThrowsList()) {
                UserFuncProfile.ThrownExceptions xExs = xProfile.ensureThrownExceptions();
                item.getException().appendTo(xExs.addNewException());
            }
        }

        if (warningsSupport != null && !warningsSupport.isEmpty()) {
            int[] warnings = warningsSupport.getSuppressedWarnings();
            List<Integer> list = new ArrayList<>(warnings.length);
            for (int w : warnings) {
                list.add(Integer.valueOf(w));
            }
            xDef.setSuppressedWarnings(list);
        }

        this.source.appendTo(xDef.addNewSource(), ESaveMode.NORMAL);
        innerBundle.appendTo(xDef.addNewStrings(), ESaveMode.NORMAL);
    }

    @Override
    public RadixProblem.WarningSuppressionSupport getWarningSuppressionSupport(boolean createIfAbsent) {
        synchronized (this) {
            if (warningsSupport == null && createIfAbsent) {
                warningsSupport = new Problems(this, null);
            }
            return warningsSupport;
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsClassDef clazz = findOwnerClass();
        if (clazz != null) {
            list.add(clazz);
        }
        Definition def = findOwnerProperty();
        if (def != null) {
            list.add(def);
        }
        def = findMethod();
        if (def != null) {
            ((AdsMethodDef) def).getProfile().collectDependences(list);
            list.add(def);
        }
    }

    @Override
    public void collectDirectDependences(List<Definition> list) {
        collectDependences(list);
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AbstractDefinitionWriter(this, UdsUserFuncDef.this, purpose) {
                    @Override
                    protected boolean writeExecutable(CodePrinter printer) {
                        return AdsUserFuncWriter.writeUfExecutable(UdsUserFuncDef.this, printer, usagePurpose, this);
                    }
                };
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
                if (sc == ERuntimeEnvironmentType.SERVER) {
                    return EnumSet.of(CodeType.EXCUTABLE);
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    public Jml getSource(String name) {
        return getSource();
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return new AdsUserFuncDef.UserFuncType(this);
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER);


    }

    public interface UICallback {

        public void showError(String message);

        public Id chooseId(Map<Id, UserFunctionDefinition> id2title);
    }

    public void exportBody(File file) throws IOException {
        UdsDefinitionListDocument xDoc = UdsDefinitionListDocument.Factory.newInstance();
        UdsDefinitionListDocument.UdsDefinitionList xList = xDoc.addNewUdsDefinitionList();
        appendTo(xList.addNewUdsDefinition());
        XmlFormatter.save(xDoc, file);
    }

    public void importBody(File file, UICallback cb) throws IOException {
        JmlType newSource = null;
        try {
            UdsDefinitionListDocument xDoc = UdsDefinitionListDocument.Factory.parse(file);
            UdsDefinitionListDocument.UdsDefinitionList xList = xDoc.getUdsDefinitionList();
            if (xList != null) {
                UserFunctionDefinition exactMatch = null;
                List<UserFunctionDefinition> sutable = new LinkedList<UserFunctionDefinition>();
                for (UdsDefinitionDocument.UdsDefinition xDef : xList.getUdsDefinitionList()) {
                    if (xDef.getUserFunc() != null) {

                        UserFunctionDefinition xUf = xDef.getUserFunc();

                        if (xUf.getClassId() == getBaseClassId() && xUf.getMethodId() == getMethodId()) {
                            sutable.add(xUf);
                        }

                    }
                }
                if (sutable.isEmpty()) {
                    cb.showError("No suitable function found");
                } else {
                    UserFunctionDefinition xUf;
                    if (sutable.size() == 1) {
                        xUf = sutable.get(0);
                    } else {
                        Map<Id, UserFunctionDefinition> map = new HashMap<Id, UserFunctionDefinition>();
                        for (UserFunctionDefinition x : sutable) {
                            map.put(x.getId(), x);
                        }
                        Id choosen = cb.chooseId(map);
                        if (choosen != null) {
                            xUf = map.get(choosen);
                        } else {
                            return;
                        }
                    }
                    newSource = xUf.getSource();
                }
            }

        } catch (XmlException ex) {
            try {
                AdsUserFuncDefinitionDocument xDoc = AdsUserFuncDefinitionDocument.Factory.parse(file);
                AdsUserFuncDefinition xDef = xDoc.getAdsUserFuncDefinition();
                newSource = xDef.getSource();
            } catch (XmlException ex1) {
                throw new IOException(ex);
            }
        }
        if (newSource != null) {
            Jml jml = Jml.Factory.loadFrom(this, newSource, "src");
            this.source.getItems().clear();
            for (Scml.Item item : jml.getItems().list()) {
                item.delete();
                this.source.getItems().add(item);
            }
            AdsModule module = getModule();
            delete();
            module.getDefinitions().add(this);
        }
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public boolean isPublished() {
        return false;
    }

    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.USERFUNC;
    }

    @Override
    public String getTypeTitle() {
        return "User-Defined Function";
    }
}
