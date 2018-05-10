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

import java.util.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsFilterModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Properties;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.RadixObjectWriter;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import static org.radixware.kernel.common.enums.EClassType.GROUP_MODEL;
import static org.radixware.kernel.common.enums.EClassType.PROP_EDITOR_MODEL;
import org.radixware.kernel.common.enums.EPropNature;
import static org.radixware.kernel.common.enums.EPropNature.DYNAMIC;
import static org.radixware.kernel.common.enums.EPropNature.EVENT_CODE;
import static org.radixware.kernel.common.enums.EPropNature.PROPERTY_PRESENTATION;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;

public class AdsPropertiesWriter extends RadixObjectWriter<Properties> {

    private final String ACCESSOR_INSTANCE_NAME = "INSTANCE";
    private final String READ_ACCESSOR_CLASS_NAME = "Access______rdx_r_prop";
    private final String READ_WRITE_ACCESSOR_CLASS_NAME = "Access______rdx_rw_prop";

    public AdsPropertiesWriter(final JavaSourceSupport support, final Properties properties, final UsagePurpose usagePurpose) {
        super(support, properties, usagePurpose);
    }

    private static void writePropertyCreator(IModelPublishableProperty prop, CodePrinter printer, char[] refPropCastClassName) {
        final char[] propId = prop.getId().toCharArray();
        printer.print("if(id == ");
        WriterUtils.writeAutoVariable(printer, propId);
        printer.print(") return new ");
        printer.print(propId);
        final EValType valType = prop.getTypedObject().getType().getTypeId();
        if (valType == EValType.ARR_REF || valType == EValType.PARENT_REF || valType == EValType.OBJECT) {
            printer.print("(this,(");
            printer.print(refPropCastClassName);
            printer.println(")def);");
        } else {
            printer.println("(this,def);");
        }
        printer.println("else ");

    }
    public static final Id CREATE_PROP_METHOD_ID = Id.Factory.loadFrom("mthATSWVRFVCZA77JH7QMXVA2SOMQ");

    public static boolean writePropertiesCreator(AdsClassDef propOwner, List<AdsPropertyDef> props, CodePrinter printer, UsagePurpose purpose) {
        WriterUtils.enterHumanUnreadableBlock(printer);
        final char[] refPropCastClassName = propOwner.getClassDefType() == EClassType.FILTER_MODEL ? PropertyPresentationWriter.EXPLORER_FILTER_PARAM_PRESENTATION_CLASS_NAME : PropertyPresentationWriter.EXPLORER_PARENT_REF_PROPERTY_PRESENTATION_CLASS_NAME;

        printer.print("protected ");
        printer.print(PropertyPresentationPropertyWriter.EXPLORER_PROPERTY_CLASS_NAME);
        printer.print(" createProperty(");
        printer.print(PropertyPresentationWriter.EXPLORER_PROPERTY_PRESENTATION_CLASS_NAME);
        printer.println(" def) {");
        printer.enterBlock();

        printer.println("@SuppressWarnings(\"unused\")");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.println(" id = def.getId();");
        final Set<Id> writtenProps = new HashSet<Id>();
        EClassType ct = propOwner.getClassDefType();
        for (AdsPropertyDef prop : props) {
            //final char[] propId = prop.getId().toCharArray();
            EPropNature nature = prop.getNature();
            if (nature == EPropNature.PROPERTY_PRESENTATION || (ct == EClassType.ENTITY_GROUP && nature == EPropNature.GROUP_PROPERTY)) {
                if (AdsClassWriter.checkEnv(prop.getUsageEnvironment(), purpose)) {
                    writePropertyCreator(prop, printer, refPropCastClassName);
                }
                writtenProps.add(prop.getId());
            }
        }
        if (ct == EClassType.FILTER_MODEL) {
            AdsFilterDef filter = ((AdsFilterModelClassDef) propOwner).getOwnerFilterDef();
            if (filter != null) {
                for (AdsFilterDef.Parameter param : filter.getParameters()) {
                    if (writtenProps.contains(param.getId())) {
                        continue;
                    }
                    if (AdsClassWriter.checkEnv(param.getUsageEnvironment(), purpose)) {
                        writePropertyCreator(param, printer, refPropCastClassName);
                    }
                }
            }
        }

        AdsMethodDef userMethod = propOwner.getMethods().findById(CREATE_PROP_METHOD_ID, EScope.LOCAL_AND_OVERWRITE).get();
        if (userMethod instanceof AdsUserMethodDef) {
            printer.println("{");
            printer.enterBlock();
            printer.print(PropertyPresentationPropertyWriter.EXPLORER_PROPERTY_CLASS_NAME);
            printer.println(" userResult = mthATSWVRFVCZA77JH7QMXVA2SOMQ(def);");
            printer.println("if(userResult != null) return userResult;");
            printer.leaveBlock();
            printer.println("}");
        }

        printer.println("return super.createProperty(def);");
        printer.leaveBlock();
        printer.println('}');
        WriterUtils.leaveHumanUnreadableBlock(printer);
        return true;
    }

    private static final class PropertyComparator implements Comparator<AdsPropertyDef> {

        @Override
        public int compare(AdsPropertyDef o1, AdsPropertyDef o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }

    @Override
    protected boolean writeExecutable(final CodePrinter printer) {
        List<AdsPropertyDef> list = def.get(EScope.LOCAL_AND_OVERWRITE);
        final PropertyComparator comparator = new PropertyComparator();
        Collections.sort(list, comparator);
        final boolean isClientWriter = usagePurpose.getEnvironment().isClientEnv();
        for (AdsPropertyDef prop : def.get(EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsPropertyDef>() {
            @Override
            public boolean isTarget(AdsPropertyDef radixObject) {
                if (isClientWriter) {
                    if (!AdsClassWriter.checkEnv(radixObject.getUsageEnvironment(), usagePurpose)) {
                        return false;
                    }
                }
                if (radixObject.getNature() == EPropNature.EXPRESSION) {
                    if (((AdsExpressionPropertyDef) radixObject).isInvisibleForArte()) {
                        return false;
                    }
                }
                return true;

            }
        })) {

            if (!writeCode(printer, prop)) {
                return false;
            }
        }
        final EClassType classType = def.getOwnerClass().getClassDefType();
        switch (classType) {
            case ENTITY_MODEL:
            case FORM_MODEL:
            case REPORT_MODEL:
            case FILTER_MODEL:
            case GROUP_MODEL:
            case PROP_EDITOR_MODEL:
            case PARAGRAPH_MODEL:
            case DIALOG_MODEL:
            case CUSTOM_WDGET_MODEL:
                //if (classType != EClassType.FILTER_MODEL || usagePurpose.getEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                final List<AdsPropertyDef> allProps = def.get(EScope.LOCAL_AND_OVERWRITE);

                if (allProps.isEmpty() && classType != EClassType.FILTER_MODEL) {
                    return true;
                }

                Collections.sort(allProps, comparator);
                if (!writePropertiesCreator(def.getOwnerClass(), allProps, printer, usagePurpose)) {
                    return false;
                }
                //}
                break;
            default:
                break;
        }
        return writeAccessorCode(printer);
    }

    @Override
    protected boolean writeMeta(final CodePrinter printer) {
        final ERuntimeEnvironmentType env = usagePurpose.getEnvironment();
        switch (env) {
            case SERVER:
                new WriterUtils.ObjectArrayWriter<AdsPropertyDef>(AdsPropertyWriter.PROPERTY_SERVER_META_CLASS_NAME) {
                    @Override
                    public void writeItemConstructor(final CodePrinter printer, final AdsPropertyDef prop) {
                        writeCode(printer, prop);
                    }
                }.write(printer, def.get(EScope.LOCAL_AND_OVERWRITE));
                return true;
            case WEB:
            case EXPLORER:

                new WriterUtils.ObjectArrayWriter<PropertyPresentation>(PropertyPresentationWriter.EXPLORER_PROPERTY_PRESENTATION_CLASS_NAME) {
                    @Override
                    public void writeItemConstructor(final CodePrinter printer, final PropertyPresentation p) {
                        writeCode(printer, p);
                    }
                }.write(printer, listPresentations(env));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void writeUsage(final CodePrinter printer) {
        //dont use in code directly
    }

    private List<PropertyPresentation> listPresentations(final ERuntimeEnvironmentType env) {
        boolean isClientEnv = env.isClientEnv();
        final List<PropertyPresentation> presentations = new ArrayList<>();
        for (AdsPropertyDef prop : def.get(EScope.LOCAL_AND_OVERWRITE)) {

            AdsPropertyDef property;
            if (prop instanceof IAdsPresentableProperty) {
                property = prop;
            } else if (prop instanceof AdsPropertyPresentationPropertyDef && ((AdsPropertyPresentationPropertyDef) prop).isLocal()) {
                property = (AdsPropertyDef) ((AdsPropertyPresentationPropertyDef) prop).findServerSideProperty();
            } else {
                continue;
            }
            if (property instanceof IAdsPresentableProperty) {
                if (env.isClientEnv() && !property.isTransferableAsMeta(env)) {
                    continue;
                }
//                if (isClientEnv/* && !AdsClassWriter.checkEnv(prop.getUsageEnvironment(), UsagePurpose.getPurpose(env, JavaSourceSupport.CodeType.META))*/) {
//                    continue;
//                }
                final ServerPresentationSupport ps = ((IAdsPresentableProperty) property).getPresentationSupport();
                if (ps != null) {
                    PropertyPresentation pp = ps.getPresentation();
                    if (pp != null && pp.isPresentable()) {
                        presentations.add(pp);
                    }
                }
            }
        }
        return presentations;
    }

    @Override
    protected boolean writeAddon(final CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                new WriterUtils.ObjectArrayWriter<PropertyPresentation>(PropertyPresentationWriter.PROPERTY_PRESENTATION_CLASS_NAME) {
                    @Override
                    public void writeItemConstructor(final CodePrinter printer, final PropertyPresentation p) {
                        p.getJavaSourceSupport().getCodeWriter(UsagePurpose.SERVER_META).writeCode(printer);
                    }
                }.write(printer, listPresentations(ERuntimeEnvironmentType.SERVER));
                return true;
            default:
                return false;
        }
    }
    private JavaSourceSupport.CodeWriter ownerClassTypeWriter2 = null;

    public static boolean willHaveAccessors(final AdsClassDef classDef, final UsagePurpose usagePurpose) {
        if (usagePurpose == null || usagePurpose.getEnvironment() != ERuntimeEnvironmentType.SERVER) {
            return false;
        }

        if (classDef == null || classDef.isInner() || classDef.getClassDefType() == EClassType.INTERFACE) {
            return false;
        }

        if (classDef.getUsageEnvironment() != ERuntimeEnvironmentType.SERVER) {
            return false;
        }

        if (classDef.getTransparence() != null && classDef.getTransparence().isTransparent() && !classDef.getTransparence().isExtendable()) {
            return false;
        }
        return true;
    }

    private boolean writeAccessorCode(final CodePrinter printer) {
        if (!willHaveAccessors(def.getOwnerClass(), usagePurpose)) {
            return true;
        }

        List<AdsPropertyDef> constantProperties = new LinkedList<>();
        List<AdsPropertyDef> modifiableProperties = new LinkedList<>();
        for (AdsPropertyDef prop : def.get(EScope.LOCAL_AND_OVERWRITE)) {

            switch (prop.getNature()) {
                case PROPERTY_PRESENTATION:
                    continue;
                case EXPRESSION:
                    AdsExpressionPropertyDef exp = (AdsExpressionPropertyDef) prop;
                    if (exp.isInvisibleForArte()) {
                        continue;
                    }
                    break;
                case DYNAMIC:
                case EVENT_CODE:
                    if (prop.getUsageEnvironment() != ERuntimeEnvironmentType.SERVER) {
                        continue;
                    } else {
                        break;
                    }
                default:
                    break;
            }
            if (prop.isConst()) {
                constantProperties.add(prop);
            } else {
                modifiableProperties.add(prop);
            }
        }

        final Comparator<AdsPropertyDef> comparator = new Comparator<AdsPropertyDef>() {
            @Override
            public int compare(AdsPropertyDef o1, AdsPropertyDef o2) {
                return o1.getId().toString().compareTo(o2.getId().toString());
            }
        };
        Collections.sort(constantProperties, comparator);

        WriterUtils.enterHumanUnreadableBlock(printer);
        //read accessor
        printer.println(AdsPropertyWriter.PROPERTY_ACCESSOR_WARNING);
        printer.print("public static final class " + READ_ACCESSOR_CLASS_NAME);
        printer.print(" implements ");
        printer.print(AdsPropertyWriter.READ_ACCESSOR_CLASS_NAME);
        printer.println('{');
        printer.enterBlock();
        printer.println("public static final " + READ_ACCESSOR_CLASS_NAME + " " + ACCESSOR_INSTANCE_NAME + " = new " + READ_ACCESSOR_CLASS_NAME + "();");
        printer.leaveBlock();
        writeReadOnlyAccessorCodePart(printer, constantProperties, READ_ACCESSOR_CLASS_NAME);
        printer.println('}');
        //read write accessor
        printer.println(AdsPropertyWriter.PROPERTY_ACCESSOR_WARNING);
        printer.print("public static final class " + READ_WRITE_ACCESSOR_CLASS_NAME);
        printer.print(" implements ");
        printer.print(AdsPropertyWriter.READ_WRITE_ACCESSOR_CLASS_NAME);
        printer.println('{');
        printer.enterBlock();
        printer.println("public static final " + READ_WRITE_ACCESSOR_CLASS_NAME + " " + ACCESSOR_INSTANCE_NAME + " = new " + READ_WRITE_ACCESSOR_CLASS_NAME + "();");
        printer.leaveBlock();
        List<AdsPropertyDef> allProps = new ArrayList<>(modifiableProperties);
        if (!constantProperties.isEmpty()) {
            allProps.addAll(constantProperties);
        }
        Collections.sort(allProps, comparator);
        writeReadOnlyAccessorCodePart(printer, allProps, READ_WRITE_ACCESSOR_CLASS_NAME);
        printer.enterBlock();

        printer.println("@SuppressWarnings({\"deprecation\",\"unchecked\"})");
        printer.print("public void set(final Object owner, final ");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.println(" propId, final Object x){");
        printer.enterBlock();
        for (AdsPropertyDef prop : modifiableProperties) {
            ((AdsPropertyWriter) prop.getJavaSourceSupport().getCodeWriter(usagePurpose)).writeAccessorSetter(printer);
        }
        writeSuperAccessorDelegationOrThrowDNFE(printer, READ_WRITE_ACCESSOR_CLASS_NAME, "set(owner, propId, x)");
        printer.leaveBlock();
        printer.println("}");
        printer.leaveBlock();
        printer.println('}');

        printer.println("public " + AdsPropertyWriter.READ_ACCESSOR_CLASS_NAME + " " + AdsPropertyWriter.GET_READ_PROP_ACCESSOR_METHOD_NAME + "() {");
        printer.enterBlock();
        printer.print("return ");
        printer.print(READ_ACCESSOR_CLASS_NAME + "." + ACCESSOR_INSTANCE_NAME);
        printer.println(";");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public " + AdsPropertyWriter.READ_WRITE_ACCESSOR_CLASS_NAME + " " + AdsPropertyWriter.GET_READ_WRITE_PROP_ACCESSOR_METHOD_NAME + "() {");
        printer.enterBlock();
        printer.print("return ");
        printer.print(READ_WRITE_ACCESSOR_CLASS_NAME + "." + ACCESSOR_INSTANCE_NAME);
        printer.println(";");
        printer.leaveBlock();
        printer.println("}");
        WriterUtils.leaveHumanUnreadableBlock(printer);
        return true;
    }

    private void writeReadOnlyAccessorCodePart(CodePrinter printer, List<AdsPropertyDef> propList, final String delegateAccessorClass) {
        printer.enterBlock(1);
        printer.println();
        printer.println("@SuppressWarnings({\"deprecation\",\"unchecked\"})");
        //printer.enterBlock(1);
        printer.print("public Object get(final Object owner, final ");
        printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
        printer.println(" propId){");
        printer.enterBlock();
        for (AdsPropertyDef prop : propList) {
            ((AdsPropertyWriter) prop.getJavaSourceSupport().getCodeWriter(usagePurpose)).writeAccessorGetter(printer);
        }
        writeSuperAccessorDelegationOrThrowDNFE(printer, delegateAccessorClass, "get(owner, propId)");
        printer.leaveBlock();
        printer.println("}");
        printer.leaveBlock();
    }

    private void writeSuperAccessorDelegationOrThrowDNFE(final CodePrinter printer, final String accessorClassName, final String delegationMethodCall) {
        final AdsClassDef superClassDefWithAccessors = findSuperClassDefWithAccessors();
        if (superClassDefWithAccessors != null) {
            if (delegationMethodCall.startsWith("get")) {
                printer.print("return ");
            }
            getCodeWriter(def).writeCode(printer, AdsTypeDeclaration.Factory.newInstance(superClassDefWithAccessors), def);
            printer.print(".");
            printer.print(accessorClassName);
            printer.print(".");
            printer.print(ACCESSOR_INSTANCE_NAME);
            printer.print(".");
            printer.print(delegationMethodCall);
            printer.println(";");
        } else {
            printer.print("throw new org.radixware.kernel.common.exceptions.DefinitionNotFoundError(");
            printer.println("propId);");
        }
    }

    private AdsClassDef findSuperClassDefWithAccessors() {
        final AdsTypeDeclaration superClassDecl = def.getOwnerClass().getInheritance().getSuperClassRef();

        if (superClassDecl != null) {
            SearchResult<AdsType> searchResult = superClassDecl.resolve(def.getOwnerClass());
            if (searchResult != null && searchResult.get() instanceof AdsClassType) {
                AdsClassDef superClassDef = ((AdsClassType) searchResult.get()).getSource();
                if (willHaveAccessors(superClassDef, usagePurpose)) {
                    return superClassDef;
                }
            }
        }
        return null;
    }
}
