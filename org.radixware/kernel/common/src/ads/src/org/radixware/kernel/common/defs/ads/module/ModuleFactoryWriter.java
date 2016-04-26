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
package org.radixware.kernel.common.defs.ads.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.radixware.kernel.common.defs.IDefinitionFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;

class ModuleFactoryWriter extends CodeWriter {

    private final AdsModule module;
    private static final char[] DEFINITION_NOT_FOUND_ERROR = ("throw new " + DefinitionNotFoundError.class.getName() + "(id);").toCharArray();
    //private static final char[] SUPERCALL = "return super.newInstance(id);".toCharArray();
    private static final char[] DEFINITION_FACTORY_INTERFACE_NAME = IDefinitionFactory.class.getName().toCharArray();

    public ModuleFactoryWriter(JavaSourceSupport support, UsagePurpose usagePurpose, AdsModule module) {
        super(support, usagePurpose);
        this.module = module;
    }

    static String getFactoryClassName(AdsModule module) {
        return "Factory_" + module.getId().toString();
    }

    @Override
    public boolean writeCode(CodePrinter printer) {
        if (usagePurpose == UsagePurpose.SERVER_EXECUTABLE) {
            final List<AdsDefinition> defs = new ArrayList<AdsDefinition>(definitions(module));
            //sort list to prevent classfile mutation
            Collections.sort(defs, new Comparator<AdsDefinition>() {

                @Override
                public int compare(AdsDefinition o1, AdsDefinition o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            String className = getFactoryClassName(module);

            WriterUtils.writePackageDeclaration(printer, module, UsagePurpose.SERVER_EXECUTABLE);
            printer.println();
            printer.print("public class ");
            printer.print(className);
            printer.printSpace();

//            AdsModule ovr = module.findOverwritten();
//
//            if (ovr != null) {
//                printer.print("extends ");
//                WriterUtils.writePackage(printer, ovr, UsagePurpose.SERVER_EXECUTABLE);
//                printer.printSpace();
//                printer.print(className);
//                printer.printSpace();
//            }
            printer.print("implements ");
            printer.print(DEFINITION_FACTORY_INTERFACE_NAME);

            printer.enterBlock();
            printer.println('{');

            printer.print("private static final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.print(" MODULE_ID = ");
            WriterUtils.writeIdUsage(printer, module.getId());
            printer.printlnSemicolon();

            for (AdsDefinition def : defs) {
                printer.print("private static final ");
                printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
                printer.print(" IDOF_");
                printer.print(def.getId());
                printer.print(" = ");
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printlnSemicolon();
            }

            printer.print("public ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.println(" getModuleId(){ return MODULE_ID; }");
            printer.println();
            printer.println();
            printer.println("@SuppressWarnings(\"deprecation\")");
            printer.print("public Object newInstance(final ");
            printer.print(WriterUtils.RADIX_ID_CLASS_NAME);
            printer.enterBlock();
            printer.println(" id, Object[] constructorArguments){");

            for (AdsDefinition def : defs) {
                printer.print("if(id == IDOF_");
                printer.print(def.getId());
                printer.print(")return new ");
                switch (def.getDefinitionType()) {
                    case CLASS:
                        AdsClassDef clazz = (AdsClassDef) def;
                        printer.print(JavaSourceSupport.getPackageName(clazz, UsagePurpose.SERVER_EXECUTABLE));
                        printer.print(".");
                        printer.print(clazz.getRuntimeLocalClassName());
                        switch (clazz.getClassDefType()) {
                            case FORM_HANDLER:
                                printer.print("((");
                                printer.print(AdsFormHandlerClassDef.PLATFORM_CLASS_NAME);
                                printer.print(")constructorArguments[0]");
                                printer.println(");");
                                break;
                            case PRESENTATION_ENTITY_ADAPTER:
                                printer.print("((");
                                printer.print(AdsEntityClassDef.PLATFORM_CLASS_NAME);
                                printer.print(")constructorArguments[0]");
                                printer.println(");");
                                break;
                            case ENTITY_GROUP:
                                printer.print("(((java.lang.Boolean)constructorArguments[0]).booleanValue());");
                                break;
                            default:
                                printer.println("();");
                        }
                        break;
                    default:
                        printer.print(def.getId());
                        printer.println("();");
                }

                printer.print("else ");
            }

            printer.leaveBlock();

            printer.println(DEFINITION_NOT_FOUND_ERROR);

            printer.println('}');
            printer.leaveBlock();
            printer.println('}');
            return true;

        } else {
            return false;
        }
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        printer.print(module.getId());
    }

    static boolean factoryRequired(AdsModule currentModule) {
        AdsModule module = currentModule;
	if (module != null && module.getLayer() != null && module.getLayer().isLocalizing()) {
            return false;
        }
        while (module != null) {
            for (AdsDefinition def : module.getDefinitions()) {
                if (precondition(def)) {
                    return true;
                }
            }
            module = module.findOverwritten();
        }
        return false;
    }

    private static boolean precondition(AdsDefinition def) {
        switch (def.getDefinitionType()) {
            case CLASS:
                AdsClassDef clazz = (AdsClassDef) def;
                switch (clazz.getClassDefType()) {
                    case ALGORITHM:
                    case APPLICATION:
                    case ENTITY:
                    case ENTITY_GROUP:
                    case FORM_HANDLER:
                    case REPORT:
                    case PRESENTATION_ENTITY_ADAPTER:
                        if (clazz.getAccessFlags().isAbstract() || clazz instanceof AdsUserReportClassDef) {
                            return false;
                        } else {
                            return true;
                        }
                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    static Collection<AdsDefinition> definitions(AdsModule currentModule) {
        Map<Id, AdsDefinition> result = new HashMap<Id, AdsDefinition>();
        AdsModule module = currentModule;
        while (module != null) {
            for (AdsDefinition def : module.getDefinitions()) {
                if (precondition(def) && !result.containsKey(def.getId())) {
                    result.put(def.getId(), def);
                }
            }
            module = module.findOverwritten();
        }
        return result.values();
    }
}
