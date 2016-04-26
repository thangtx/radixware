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
package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.CodePrinter;

class AdsTypeDeclarationCodeWriter extends JavaSourceSupport.CodeWriter {

    private static final char[] ARRAY_BRACKETS = "[]".toCharArray();
    private static final char[] EXTENDS = "extends".toCharArray();
    private static final char[] SUPER = "super".toCharArray();
    private final AdsTypeDeclaration decl;

    public AdsTypeDeclarationCodeWriter(JavaSourceSupport support, AdsTypeDeclaration decl, UsagePurpose usagePurpose) {
        super(support, usagePurpose);
        this.decl = decl;
    }

    private static Definition findTypeResolitionContext(JavaSourceSupport support) {
        RadixObject obj = (RadixObject) support.getCurrentObject();
        while (obj != null) {
            if (obj instanceof Definition) {
                return (Definition) obj;
            }
            obj = obj.getContainer();
        }
        return null;
    }

    @Override
    public boolean writeCode(CodePrinter printer) {
        Definition definition = findTypeResolitionContext(getSupport());
        if (definition == null) {
            throw new DefinitionError("Could not generate code because no type resolution context found", decl);
        } else {
            writeCode(printer, definition, decl, null);
        }
        return true;
    }

    public boolean writeSuperClassRef(CodePrinter printer, AdsClassDef receiver) {
        Definition definition = findTypeResolitionContext(getSupport());
        if (definition == null) {
            throw new DefinitionError("Could not generate code because no type resolution context found", decl);
        } else {
            writeCode(printer, definition, decl, receiver);
        }
        return true;
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        writeCode(printer);
    }

    private final boolean writeCode(CodePrinter printer, Definition definition, AdsTypeDeclaration declaration, AdsClassDef receiver) {
        if (declaration == AdsTypeDeclaration.VOID) {
            return writeCode(printer, VoidType.getInstance());
        } else if (declaration == AdsTypeDeclaration.UNDEFINED) {
            return false;
        } else {
            final AdsType type = declaration.resolve(definition).get();
            int printDimensions = 0;
            if (type != null) {

                boolean printed = false;
                if (type instanceof AdsClassType) {
                    final AdsClassType classType = (AdsClassType) type;
                    final AdsClassDef source = classType.getSource();
                    if (source != null && source.isNested()) {
                        final RadixObject obj = (RadixObject) getSupport().getCurrentObject();

                        if (obj instanceof Jml) {
                            printer.print(source.getId().toString());
                            printed = true;
                        }
                    }
                }
                AdsType typeToPrint = type;
                if (typeToPrint instanceof ArrayType) {                    
                    printDimensions = ((ArrayType) typeToPrint).getDimensions();
                    typeToPrint = ((ArrayType) typeToPrint).getItemType();
                }

                if (!printed && !writeCode(printer, typeToPrint)) {
                    return false;
                }
            } else {
                return false;
            }
            /**
             * Appends type generic signature
             */
            if (declaration.getGenericArguments() != null && !declaration.getGenericArguments().isEmpty()) {
                if (!writeCode(printer, declaration.getGenericArguments(), (RadixObject) getSupport().getCurrentObject())) {
                    return false;
                }
            }
            /**
             * Appends type array dimansion
             */

            for (int i = 0; i < printDimensions; i++) {
                printer.print(ARRAY_BRACKETS);
            }
            return true;
        }
    }

    public static class ArgumentsWriter extends JavaSourceSupport.CodeWriter {

        private AdsTypeDeclaration.TypeArguments arguments;
        private Definition definition;

        ArgumentsWriter(AdsTypeDeclaration.TypeArguments arguments, JavaSourceSupport support, UsagePurpose usagePurpose) {
            super(support, usagePurpose);
            this.arguments = arguments;
            definition = findTypeResolitionContext(getSupport());
        }

        @Override
        public boolean writeCode(CodePrinter printer) {
            if (definition == null) {
                throw new DefinitionError("Could not generate code because no type resolution context found", arguments);
            }
            if (!arguments.isEmpty()) {
                printer.print('<');
                boolean isFirst = true;
                for (AdsTypeDeclaration.TypeArgument a : arguments.getArgumentList()) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        printer.printComma();
                    }
                    if (a.derivation != AdsTypeDeclaration.TypeArgument.Derivation.NONE) {
                        printer.print(a.getName());
                        printer.printSpace();
                        if (a.type != null) {
                            if (a.derivation == AdsTypeDeclaration.TypeArgument.Derivation.EXTENDS) {
                                printer.print(EXTENDS);
                                printer.printSpace();
                            } else if (a.derivation == AdsTypeDeclaration.TypeArgument.Derivation.SUPER) {
                                printer.print(SUPER);
                                printer.printSpace();
                            }
                        }
                    }
                    if (a.type != null) {
                        writeCode(printer, a.type, definition);
                    } else {//wildcard
                        printer.print("?");
                    }
                }
                printer.print('>');
            }
            return true;
        }

        @Override
        public void writeUsage(CodePrinter printer) {
            writeCode(printer);
        }
    }
}
