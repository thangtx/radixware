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

package org.radixware.kernel.common.defs.ads.src.clazz;

import java.util.List;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportModelClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IdPrefixes;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


class PropertyPresentationPropertyWriter extends AdsPropertyWriter<AdsPropertyPresentationPropertyDef> {

    private final AdsPropertyDef overridenProp;
    private final CodeWriter overridenPropertyOwnerTypeWriter;
    public static final char[] EXPLORER_PROPERTY_CLASS_NAME = "org.radixware.kernel.common.client.models.items.properties.Property".toCharArray();
    public static final char[] EXPLORER_PROPERTY_VALUE_CLASS_NAME = "org.radixware.kernel.common.client.models.items.properties.PropertyValue".toCharArray();
    private static final char[] EXPLORER_REF_PROPERTY_DEF_CLASS_NAME = "org.radixware.kernel.common.client.meta.RadParentRefPropertyDef".toCharArray();

    PropertyPresentationPropertyWriter(JavaSourceSupport support, AdsPropertyPresentationPropertyDef property, UsagePurpose usagePurpose) {
        super(support, property, usagePurpose);
        overridenProp = (property.getContainer() instanceof IModelPublishableProperty) ? null : property.getHierarchy().findOverridden().get();
        if (overridenProp != null) {
            overridenPropertyOwnerTypeWriter = overridenProp.getOwnerClass().getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose);
        } else {
            overridenPropertyOwnerTypeWriter = null;
        }
    }

    protected boolean writeStdBaseClassForPropClass(CodePrinter printer) {
        printer.print(EXPLORER_PROPERTY_CLASS_NAME);
        return writeStdBaseClassForPropSuffix(printer);
    }

    public boolean writeStdBaseClassForPropSuffix(CodePrinter printer) {
        if (def.getValue().getType().getTypeId() == null) {
            return false;
        }
        switch (def.getValue().getType().getTypeId()) {
            case OBJECT:
                printer.print("Object");
                break;
            case PARENT_REF:
                printer.print("Ref");
                break;
            case ARR_BIN:
                printer.print("ArrBin");
                break;
            case ARR_BLOB:
                printer.print("ArrBlob");
                break;
            case ARR_BOOL:
                printer.print("ArrBool");
                break;
            case ARR_CHAR:
                printer.print("ArrChar");
                break;
            case ARR_DATE_TIME:
                printer.print("ArrDateTime");
                break;
            case ARR_INT:
                printer.print("ArrInt");
                break;
            case ARR_NUM:
                printer.print("ArrNum");
                break;
            case ARR_REF:
                printer.print("ArrRef");
                break;
            case ARR_STR:
                printer.print("ArrStr");
                break;
            case XML:
                printer.print("Xml");
                break;
            case STR:
                printer.print("Str");
                break;
            case NUM:
                printer.print("Num");
                break;
            case INT:
                printer.print("Int");
                break;
            case DATE_TIME:
                printer.print("DateTime");
                break;
            case CHAR:
                printer.print("Char");
                break;
            case BOOL:
                printer.print("Bool");
                break;
            case BLOB:
                printer.print("Blob");
                break;
            case CLOB:
                printer.print("Clob");
                break;
            case BIN:
                printer.print("Bin");
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    protected boolean writeInitialization(CodePrinter printer) {
        printer.println();
//        if (!getAccessWriter().writeCode(printer)) {
//            return false;
//        }

        printer.print("public class ");
        printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
        printer.print(" extends ");
        Id modelAdapterPresentationId = null;
        AdsClassDef serverClass = null;

        if (overridenPropertyOwnerTypeWriter != null) {// property overrides another property
            if (def.getContainer() instanceof IModelPublishableProperty) {//default model
                //We should never be here
                return false;
            } else {
                overridenPropertyOwnerTypeWriter.writeCode(printer);
                printer.print('.');
                printer.print(JavaSourceSupport.getName(overridenProp, printer instanceof IHumanReadablePrinter, true));
            }
        } else {

            if (def.isLocal() || def.getContainer() instanceof IModelPublishableProperty) {//default model
                if (!writeStdBaseClassForPropClass(printer)) {
                    return false;
                }
            } else {
                boolean isGroupModel = false;
                boolean written = false;
                if (ownerClass instanceof AdsEntityModelClassDef) {
                    serverClass = ((AdsEntityModelClassDef) ownerClass).findServerSideClasDef();
                    final AdsEditorPresentationDef epr = ((AdsEntityModelClassDef) ownerClass).getOwnerEditorPresentation();
                    if (epr == null) {
                        return false;
                    }
                    if (epr.getBasePresentationId() != null) {
                        AdsEditorPresentationDef base = epr;
                        do {
                            base = base.findBaseEditorPresentation().get();
                            if (base != null && base.getOwnerClass().getId() != serverClass.getId()) {
//                                AdsModelClassDef model = base.isUseDefaultModel() ? base.getModel() : base.findFinalModel();
                                if (base.findFinalModel() != null) {
                                    modelAdapterPresentationId = base.getId();
                                }
                                break;
                            }

                        } while (base != null && base.getBasePresentationId() != null);
                    }
                } else if (ownerClass instanceof AdsGroupModelClassDef) {
                    isGroupModel = true;
                    serverClass = ((AdsGroupModelClassDef) ownerClass).findServerSideClasDef();
                    final AdsEntityClassDef entity = ((AdsEntityObjectClassDef) serverClass).findRootBasis();
                    if (entity == null) {
                        return false;
                    }
                    final AdsSelectorPresentationDef epr = ((AdsGroupModelClassDef) ownerClass).getOwnerSelectorPresentation();
                    if (epr == null) {
                        return false;
                    }

                    if (epr.getBasePresentationId() != null) {
                        AdsSelectorPresentationDef base = epr;
                        do {
                            base = base.findBaseSelectorPresentation().get();
                            if (base != null && base.getOwnerClass().getId() != serverClass.getId()) {
//                                AdsModelClassDef model = base.isUseDefaultModel() ? base.getModel() : base.findFinalModel();
                                if (base.findFinalModel() != null) {
                                    modelAdapterPresentationId = base.getId();
                                }
                                break;
                            }

                        } while (base != null && base.getBasePresentationId() != null);
                    }
                    serverClass = entity;
                } else if (ownerClass instanceof AdsFormModelClassDef) {
                    serverClass = ((AdsFormModelClassDef) ownerClass).findServerSideClasDef();
                } else if (ownerClass instanceof AdsReportModelClassDef) {
                    serverClass = ((AdsReportModelClassDef) ownerClass).findServerSideClasDef();
//                } else if (ownerClass instanceof AdsFilterModelClassDef && ownerClass.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
//                    //serverClass = ((AdsReportModelClassDef) ownerClass).findServerSideClasDef();
//                    AdsClassDef baseModel = ownerClass.getInheritance().findSuperClass();
//                    if (baseModel == null) {
//                        return false;
//                    }
//                    baseModel.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(baseModel.getUsageEnvironment(), usagePurpose.getCodeType())).writeUsage(printer);
//                    printer.print('.');
//                    printer.print(def.getId());
//                    written = true;
                } else {
                    if (!writeStdBaseClassForPropClass(printer)) {
                        return false;
                    }
                    written = true;
                }
                if (!written) {
                    if (serverClass == null) {
                        return false;
                    } else {
                        //serverClass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON_CLIENT, usagePurpose.getCodeType())).writeCode(printer);
                        writeUsage(printer, serverClass.getType(EValType.USER_CLASS, null));
                        if (modelAdapterPresentationId != null) {
                            printer.print('.');
                            printer.print(JavaSourceSupport.getName(serverClass, printer instanceof IHumanReadablePrinter));
                            printer.print("_DefaultModel.");
                            printer.print(modelAdapterPresentationId);
                            printer.print("_ModelAdapter");
                        } else {
                            if (isGroupModel) {
                                printer.print('.');
                                printer.print(AdsModelClassDef.getDefaultModelLocalClassName(EClassType.GROUP_MODEL));
                            }
                        }
                        printer.print('.');
                        printer.print(def.getId());
                    }
                }
            }
        }
        printer.enterBlock();
        printer.println('{');


        //------------------ generating constructor -----------------------
        printer.print("public ");
        printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
        printer.print("(org.radixware.kernel.common.client.models.Model ");

//        if (def.getContainer() instanceof AdsPropertyDef) {//temporary property storage
//            AdsPropertyDef container = (AdsPropertyDef) def.getContainer();
//            AdsClassDef coc = container.getOwnerClass();
//            if (coc == null) {
//                return false;
//            }
//            switch (coc.getClassDefType()) {
//                case ENTITY:
//                case APPLICATION:
//                    printer.print(AdsModelClassDef.getDefaultModelLocalClassName(EClassType.ENTITY_MODEL));
//                    break;
//                case FORM_HANDLER:
//                    printer.print(AdsModelClassDef.getDefaultModelLocalClassName(EClassType.FORM_MODEL));
//                    break;
//                case REPORT:
//                    printer.print(AdsModelClassDef.getDefaultModelLocalClassName(EClassType.REPORT_MODEL));
//                    break;
//                default:
//                    return false;
//            }
//        } else {//normal property declaration
//            if (externalBasePresentation != null) {
//                writeUsage(printer, serverClass.getType(EValType.USER_CLASS, null));
//                printer.print('.');
//                printer.print(externalBasePresentation.getId());
//                printer.print("_ModelAdapter");
//            } else {
//                ownerClass.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
//            }
//        }
        printer.print(" model,");
        if (def.getValue().getType() == null || def.getValue().getType().getTypeId() == null) {
            return false;
        }
        switch (def.getValue().getType().getTypeId()) {
            case PARENT_REF:
            case OBJECT:
            case ARR_REF:
                if (def.getOwnerClass() != null && def.getOwnerClass().getClassDefType() == EClassType.FILTER_MODEL || def.getContainer() instanceof AdsFilterDef.Parameter) {
                    printer.print(PropertyPresentationWriter.EXPLORER_FILTER_PARAM_PRESENTATION_CLASS_NAME);
                } else {
                    printer.print(EXPLORER_REF_PROPERTY_DEF_CLASS_NAME);
                }
                break;
            default:
                printer.print(PropertyPresentationWriter.EXPLORER_PROPERTY_PRESENTATION_CLASS_NAME);
        }
        printer.enterBlock();
        printer.println(" def){");
        printer.println("super(model,def);");


        writeDependencies(printer);

        if (def.isLocal() && def.getValue().getInitial() != null) {
            printer.print("setInternalVal(");
            if (!WriterUtils.writeAdsValAsStr(def.getValue().getInitial(), def.getValue().getInitialValueController(), this, printer)) {
                //if (!WriterUtils.printValAsStrAsJavaInitializerForVariable(def.getValue().getInitial(), def.getValue().getType(), def, this, printer)) {
                return false;
            }
            printer.println(");");
            printer.println("setValEdited(false);");
        }

        printer.leaveBlock();
        printer.println("}");
        //-------------------- constructor generated -----------------------
        if (def.getValue().getType().getTypeId() == EValType.PARENT_REF || def.getValue().getType().getTypeId() == EValType.OBJECT) {
            //---------public EntityModel openEntityModel()  throws ServiceClientException, InterruptedException{
            printer.println("@Override");
            printer.println("public ");
            final IModelPublishableProperty prop = def.findServerSideProperty();
            final AdsType type2 = prop.getTypedObject().getType().resolve(def).get();
            if (type2 instanceof AdsClassType.EntityObjectType) {
                final AdsEntityObjectClassDef src = ((AdsClassType.EntityObjectType) type2).getSource();
                if (src == null) {
                    return false;
                }
                final AdsEntityClassDef aec = src.findRootBasis();
                if (aec == null) {
                    return false;
                }

                final AdsType type3 = aec.getType(EValType.USER_CLASS, null);
//                CodeWriter typeWriter = type3.getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON_CLIENT, usagePurpose.getCodeType()));
                //              typeWriter.writeUsage(printer);

                writeUsage(printer, type3);
                printer.print('.');
                printer.print(JavaSourceSupport.getName(aec, printer instanceof IHumanReadablePrinter));
                printer.println("_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{");
                printer.print("     return (");
                writeUsage(printer, type3);
                //typeWriter.writeUsage(printer);
                printer.print('.');
                printer.print(JavaSourceSupport.getName(aec, printer instanceof IHumanReadablePrinter));
                printer.println("_DefaultModel)super.openEntityModel();");
                printer.println("}");

                if (type2 instanceof ParentRefType) {
                    printer.println("@Override");
                    printer.println("public ");
                    writeUsage(printer, type3);
                    //typeWriter.writeUsage(printer);
                    printer.println(".DefaultGroupModel openGroupModel(){");
                    printer.print("     return (");
                    writeUsage(printer, type3);
//                    typeWriter.writeUsage(printer);
                    printer.println(".DefaultGroupModel)super.openGroupModel();");
                    printer.println("}");
                }

            } else {
                return false;
            }
        }

        //-------------------- generating setServerValObject for enum-based properties -----------------------
        //public void setServerValObject(final Object x, final boolean own,	final boolean defined, final boolean readonly);

        final AdsType type = getResolvedType();

        if (type instanceof AdsEnumType) {
            final CodeWriter tw = getTypeWriter();

            final IModelPublishableProperty sp = def.findServerSideProperty();
            if (sp instanceof AdsPropertyDef && ((AdsPropertyDef) sp).isDeprecated()) {
                printer.println("@Deprecated");
            }

            printer.println("@Override");
            printer.print("public void setServerValue(");
            printer.print(EXPLORER_PROPERTY_VALUE_CLASS_NAME);
            printer.println(" val){");
            printer.enterBlock();
            printer.println("Object x = val.getValue();");
            writeEnumBasedTypeValueConversion(printer, type, tw, "x", "dummy", true);
            printer.println("val.refineValue(dummy);");
            printer.println("super.setServerValue(val);");
            printer.leaveBlock();
            printer.println('}');
            writeGetValClassMethod(printer, tw);
        } else if (type instanceof XmlType) {
            final CodeWriter tw = getTypeWriter();
            writeGetValClassMethod(printer, tw);
        }

        if (findOverriddenProp() == null) {

            //generating standard method getValueObject() invoked by explorer
            if (!findBySignature("getValueObject()Ljava.lang.Object;")) {
                printer.println("@Override\npublic Object getValueObject(){return getValue();}");
            }

            //generating standard method setValueObject() invoked by explorer
            if (!findBySignature("setValueObject(Ljava.lang.Object;)V")) {
                IModelPublishableProperty sp = def.findServerSideProperty();
                if (sp instanceof AdsPropertyDef && ((AdsPropertyDef) sp).isDeprecated()) {
                    printer.println("@Deprecated");
                }

                printer.println("@Override\npublic void setValueObject(final Object x){");
                printer.enterBlock();
                writeVarConversionBeforeSetToStringBuffer(printer);
                printer.println("setValue(dummy);");
                printer.leaveBlock();
                printer.println('}');
            }
        }

        writeEmbeddedClass(printer);

        return true;
    }

    private void writeDependencies(CodePrinter printer) {
        List<Id> ids = def.getDependents().getDependents(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE, AdsPropertyPresentationPropertyDef.COMMAND_FILTER);
        if (!ids.isEmpty()){
            for (final Id id : ids) {
                printer.print("if (");
                printer.print(def.getOwnerClass().getId().toString());
                printer.print(".this.getDefinition().isCommandDefExistsById(");
                WriterUtils.writeIdUsage(printer, id);
                printer.println(")) {");
                printer.enterBlock();
                writeDependence(printer, id);
                printer.leaveBlock();
                printer.println("}");
            }
        }
        
        ids = def.getDependents().getDependents(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE, AdsPropertyPresentationPropertyDef.PROPERTY_FILTER);
        if (!ids.isEmpty()){
            for (final Id id : ids) {
                printer.print("if (");
                printer.print(def.getOwnerClass().getId().toString());
                printer.print(".this.getDefinition().isPropertyDefExistsById(");
                WriterUtils.writeIdUsage(printer, id);
                printer.println(")) {");
                printer.enterBlock();
                writeDependence(printer, id);
                printer.leaveBlock();
                printer.println("}");
            }
        }
    }
    
    private void writeDependence(CodePrinter printer, Id id) {
        printer.print("this.addDependent(");
        printer.print(def.getOwnerClass().getId().toString());
        printer.print(".this.");

        if (IdPrefixes.isCommandId(id)) {
            printer.print("getCommand(");
        } else {
            printer.print("getProperty(");
        }
        
        WriterUtils.writeIdUsage(printer, id);
        printer.print(")");
        printer.println(");");
    }

    private boolean isGetEditMaskMethodDefined() {
        final AdsClassDef embeddedClass = def.findEmbeddedClass(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();

        if (embeddedClass != null) {
            final List<AdsMethodDef> getEditMask = embeddedClass.getMethods().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsMethodDef>() {
                @Override
                public boolean isTarget(AdsMethodDef radixObject) {

                    char[] prefix = ("getEditMask()L" + getEditMaskPrefix() + ".EditMask").toCharArray();
                    char[] signature = radixObject.getProfile().getSignature(radixObject.getOwnerClass());
                    return CharOperation.prefixEquals(prefix, signature);
                }
            });

            return !getEditMask.isEmpty();
        }
        return false;
    }

    private void writeGetEditMask(CodePrinter printer) {
        final IModelPublishableProperty serverSideProperty = def.findServerSideProperty();

        if (serverSideProperty instanceof IAdsPresentableProperty) {

            final IAdsPresentableProperty prop = (IAdsPresentableProperty) def.findServerSideProperty();
            if (prop == null) {
                return;
            }

            final ServerPresentationSupport presentationSupport = prop.getPresentationSupport();
            if (presentationSupport == null || presentationSupport.getPresentation() == null) {
                return;
            }

            final EditMask editMask = presentationSupport.getPresentation().getEditOptions().getEditMask();
            if (editMask != null) {
                final String editMaskName = getEditMaskClassName(editMask);

                printer.println("@Override");
                printer.print("public ");
                printer.print(editMaskName);
                printer.println(" getEditMask() {");
                printer.enterBlock();
                printer.print("return (");
                printer.print(editMaskName);
                printer.println(") super.getEditMask();");
                printer.leaveBlock();
                printer.println("}");
            }
        }
    }

    private static String getEditMaskPrefix() {
        return String.valueOf(WriterUtils.PRESENTATIONS_META_EXPLORER_PACKAGE_NAME) + ".mask";
    }

    private String getEditMaskClassName(EditMask editMask) {
        return getEditMaskPrefix() + "." + editMask.getType().getClassName();
    }

    private boolean findBySignature(String signature) {
        final SearchResult<AdsClassDef> embeddedClass = def.findEmbeddedClass(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
        if (!embeddedClass.isEmpty()) {

            AdsMethodDef getValueObject = embeddedClass.get().getMethods().findBySignature(signature.toCharArray(), ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);

            return getValueObject != null;
        }

        return false;
    }

    private void writeGetValClassMethod(CodePrinter printer, CodeWriter tw) {
        //        public abstract Class<?> getValClass();
        IModelPublishableProperty sp = def.findServerSideProperty();
        if (sp instanceof AdsPropertyDef && ((AdsPropertyDef) sp).isDeprecated()) {
            printer.println("@Deprecated");
        }
        printer.println("@Override");
        printer.print("public Class<");
        tw.writeCode(printer);
        printer.println("> getValClass(){");
        printer.enterBlock();
        printer.print("return ");
        tw.writeCode(printer);
        printer.println(".class;");
        printer.leaveBlock();
        printer.println('}');

    }
    private boolean isAbstractDeclaration = false;

    @Override
    protected boolean writePropertyIdRef() {
        return !isAbstractDeclaration;
    }

    public static void writePropertyGetterById(CodePrinter printer, Id propId) {
        writePropertyGetterById(printer, propId, null);
    }
    
    public static void writePropertyGetterById(CodePrinter printer, Id propId, char[] type) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        writePropertyIdRef(printer, propId);
        printer.print("public ");
        if (type != null) {
            printer.print(type).print(".");
        }
        printer.print(propId);
        printer.print(" get");
        printer.print(propId);
        printer.print("(){return (");
        if (type != null) {
            printer.print(type).print(".");
        }
        printer.print(propId);
        printer.print(")getProperty(");
        WriterUtils.writeAutoVariable(printer, propId.toCharArray());
        printer.println(");}");
        WriterUtils.leaveHumanUnreadableBlock(printer);
    }

    @Override
    public boolean writeAbstractDeclaration(CodePrinter printer) {
        synchronized (this) {
            try {
                isAbstractDeclaration = true;
                return writeCode(printer);
            } finally {
                isAbstractDeclaration = false;
            }
        }
    }

    @Override
    public boolean writeConcreteDeclaration(CodePrinter printer) {
        synchronized (this) {
            writePropertyIdRef(printer);
            return writeObjectGetter(printer);
        }
    }

    private boolean writeObjectGetter(CodePrinter printer) {
        // getAccessWriter().writeCode(printer);
        printer.print("public ");
        printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
        printer.print(" get");
        printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
        synchronized (this) {
            if (isAbstractDeclaration) {
                printer.print("();");
            } else {
                printer.print("(){return (");
                printer.print(JavaSourceSupport.getName(def, printer instanceof IHumanReadablePrinter, true));
                printer.print(")getProperty(");
                WriterUtils.writeAutoVariable(printer, propId);
                printer.println(");}");
            }
        }
        return true;
    }

    @Override
    protected boolean writeFinalization(CodePrinter printer) {
        printer.leaveBlock();
        printer.println('}');
        return writeObjectGetter(printer);
    }

    @Override
    protected void writePropertyName(CodePrinter printer, boolean capitalize) {
        printer.print("Value");
    }

    @Override
    protected void writeAccessMethodName(CodePrinter printer) {
        printer.print("ValObjectImpl");
    }

    @Override
    protected boolean isIdRequiredSystemValueSet() {
        return false;
    }

    @Override
    protected boolean isIdRequiredSystemValueGet() {
        return false;
    }

    @Override
    protected boolean ignoreOvrMode() {
        return true;
    }

    private void writeEmbeddedClass(CodePrinter printer) {
        final SearchResult<AdsClassDef> embeddedClass = def.findEmbeddedClass(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);

        if (!embeddedClass.isEmpty()) {
            writeCode(printer, embeddedClass.get());
        }
    }

    @Override
    protected boolean isWriteInitialization() {
        return true;
    }
}
