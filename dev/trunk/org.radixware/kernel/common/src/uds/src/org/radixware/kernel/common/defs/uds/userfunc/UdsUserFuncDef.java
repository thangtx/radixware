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
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionProblems;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
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
import org.radixware.kernel.common.defs.ads.userfunc.xml.ParseInfo;
import org.radixware.kernel.common.defs.ads.userfunc.xml.UserFuncImportInfo;
import org.radixware.kernel.common.defs.uds.IInnerLocalizingDef;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.UdsSearcher;
import org.radixware.kernel.common.defs.uds.UdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.uds.module.Loader;
import org.radixware.kernel.common.enums.EClassType;
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
import org.radixware.schemas.adsdef.AdsUserFuncDefinitionDocument.AdsUserFuncDefinition;
import org.radixware.schemas.adsdef.UserFuncProfile;
import org.radixware.schemas.udsdef.UdsDefinitionDocument;
import org.radixware.schemas.udsdef.UdsDefinitionListDocument;
import org.radixware.schemas.udsdef.UserFunctionDefinition;
import org.radixware.schemas.xscml.JmlType;


public class UdsUserFuncDef extends UdsDefinition implements IEnvDependent, ICompilable, IJavaSource, IUserFuncDef, IJmlSource, IAdsTypeSource, IInnerLocalizingDef {
    private final Jml source;
    private Id classId;
    private Id propId;
    private Id ownerClassId;
    private Id ownerEntityClassId;
    private Id methodId;
    private Problems warningsSupport = null;
    private AdsMethodDef innerMethod;
    private UdsLocalizingBundleDef innerBundle;

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

        public static UdsUserFuncDef loadFrom(UserFuncImportInfo info, boolean importMode) {
            return new UdsUserFuncDef(info, importMode);
        }
        
        public static UdsDummyUserFuncDef loadFrom(UserFuncImportInfo info, RadixObject container) {
            return new UdsDummyUserFuncDef(info, container);
        }
        
    }


    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
    
    protected UdsUserFuncDef(UserFuncImportInfo xDefinition, boolean importMode) {
        super(xDefinition.getId() != null ? xDefinition.getId()
                : Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS),
                xDefinition.getName());
        innerBundle = new UdsLocalizingBundleDef(getId(), this);
        if (xDefinition.getSource() != null) {
            this.source = Jml.Factory.loadFrom(this, xDefinition.getSource(), "source");
        } else {
            this.source = Jml.Factory.newInstance(this, "source");
        }
        if (xDefinition.getOwnerClassId() != null) {
            this.ownerClassId = xDefinition.getOwnerClassId();
        } else {
            this.ownerEntityClassId = Id.Factory.changePrefix(xDefinition.getOwnerEntityId(),
                    EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        }
        this.propId = xDefinition.getPropId();
        this.classId = xDefinition.getClassId();
        this.methodId = xDefinition.getMethodId();
        if (xDefinition.getUserFuncProfile() != null) {
            checkInnerMethod(xDefinition.getUserFuncProfile());
        }
        if (xDefinition.getSuppressedWarnings() != null) {
            List<Integer> list = xDefinition.getSuppressedWarnings();
            if (!list.isEmpty()) {
                this.warningsSupport = new Problems(this, list);
            }
        }
        if (xDefinition.getStrings() != null) {
            innerBundle.loadStrings(xDefinition.getStrings());
        }
    }
    
    private UdsUserFuncDef(UserFunctionDefinition xDefinition, boolean importMode) {
        super(xDefinition);
        if (importMode && getId().getPrefix() != EDefinitionIdPrefix.LIB_USERFUNC_PREFIX) {
            setId(Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS));
        }
        innerBundle = new UdsLocalizingBundleDef(getId(), this);
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
    
    private void createInnerMethod(String name) {
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
        innerMethod = new InnerMethod(this.methodId, name);
    }

    private void checkInnerMethod(UserFuncImportInfo.ProfileInfo xProfile) {
        createInnerMethod(xProfile.getMethodName());
        innerMethod.updateSignature(xProfile.getMethodName(), xProfile.getRetType(),
                xProfile.getParams(), xProfile.getExceptions());
    }

    private void checkInnerMethod(UserFuncProfile xProfile) {
        createInnerMethod(xProfile.getMethodName());
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
        innerBundle = new UdsLocalizingBundleDef(getId(), this);
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
        innerBundle = new UdsLocalizingBundleDef(getId(), this);
        if (property != null) {
            this.ownerClassId = property.getOwnerClass().getId();
            this.propId = property.getId();
        }
        this.classId = method.getOwnerClass().getId();

        this.methodId = method.getId();
    }

    @Override
    public AdsLocalizingBundleDef findLocalizingBundleImpl(boolean createIfNotExists) {
        return innerBundle;
    }

    public Id getBaseClassId() {
        return classId;
    }

    public Id getOwnerClassId() {
        return ownerClassId;
    }

    public Id getOwnerEntityClassId() {
        return ownerEntityClassId;
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
        if (ownerClassId == null) {
            AdsClassDef context = UdsSearcher.Factory.newAdsClassSearcher(this).findById(ownerEntityClassId).get();
            if (findOwnerProperty(context) != null){
                ownerClassId = ownerEntityClassId;
                return context;
            }
            final VisitorProvider provider = AdsVisitorProviders.newClassVisitorProvider(EnumSet.of(EClassType.APPLICATION));
            final AdsClassDef[] currentClass = new AdsClassDef[1];
            AdsEntityClassDef entityClassDef = context instanceof AdsEntityClassDef ? (AdsEntityClassDef) context : null;
            
            if (!isInBranch() || entityClassDef == null){
                return context;
            }
            final Id id = entityClassDef.getEntityId();
            getBranch().visit(new IVisitor() {
                @Override
                public void accept(RadixObject radixObject) {
                    final AdsApplicationClassDef application = (AdsApplicationClassDef) radixObject;
                    if (Objects.equals(id, application.getEntityId())) {
                        final SearchResult<AdsClassDef> overwriteBase = application.getHierarchy().findOverwriteBase();
                        if (!overwriteBase.isEmpty()) {
                            AdsApplicationClassDef clazz = (AdsApplicationClassDef) overwriteBase.get();
                            if (findOwnerProperty(clazz) != null) {
                                ownerClassId = clazz.getId();
                                currentClass[0] = clazz;
                                provider.cancel();
                            }
                        }
                    }
                }
        }, provider);
            return currentClass[0];
        }
        
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
        return findOwnerProperty(clazz);
    }
    
    private AdsUserPropertyDef findOwnerProperty(AdsClassDef clazz){
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
            innerMethod.appendTo(xDef.addNewUserFuncProfile());
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

        public int chooseId(List<UserFuncImportInfo> ufFuncs);
    }

    public void exportBody(File file) throws IOException {
        UdsDefinitionListDocument xDoc = UdsDefinitionListDocument.Factory.newInstance();
        UdsDefinitionListDocument.UdsDefinitionList xList = xDoc.addNewUdsDefinitionList();
        appendTo(xList.addNewUdsDefinition());
        XmlFormatter.save(xDoc, file);
    }

    public void importBody(File file, UICallback cb) throws IOException {
        final List<ParseInfo> ufInfos;
        try {
            ufInfos = Loader.parseXmlFile(file);
        } catch (XmlException e) {
            throw new IOException("Error on parse xml file", e);
        }
        List<UserFuncImportInfo> infos = new ArrayList<>();
        for (ParseInfo info : ufInfos) {
            if (info instanceof UserFuncImportInfo) {
                infos.add((UserFuncImportInfo) info);
            }
        }
        JmlType newSource = null;
        if (infos.isEmpty()) {
            throw new IOException("Unable to find user function document");
        } else if (infos.size() > 1) {
            final int choosen = cb.chooseId(infos);
            if (choosen != -1) {
                newSource = infos.get(choosen).getSource();
            } else {
                return;
            }
        } else {
            newSource = infos.get(0).getSource();
        }

        if (newSource != null) {
            Jml jml = Jml.Factory.loadFrom(this, newSource, "src");
            this.source.getItems().clear();
            for (Scml.Item item : jml.getItems().list()) {
                item.delete();
                this.source.getItems().add(item);
            }
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
