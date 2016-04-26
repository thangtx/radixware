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
package org.radixware.kernel.common.defs.ads.clazz.algo.generation;

import java.util.Set;
import java.util.List;
import java.util.Queue;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Map;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef.Param;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsClassWriter;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.JavaType;
import org.radixware.kernel.common.defs.ads.type.JavaType.PrimitiveTypeJavaSourceSupport.PrimitiveTypeCodeWriter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDwfFormSubmitVariant;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.lang.ReflectiveCallable;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;

public class AdsAlgoWriter extends AdsClassWriter<AdsAlgoClassDef> {

    private static final char[] REFLECTIVE_CALLABLE_CLASS_NAME = ReflectiveCallable.class.getName().toCharArray();

    enum PrinterType {

        REGISTER_IN_EXECUTOR, // public abstract void registerInExecutor(AlgorithmExecutor exec, String path);
        BLOCK_TYPE, // public abstract int getBlockType(int block);
        NEXT_BLOCK, // public abstract Id getNextBlock(Id block, int output);
        NEXT_BLOCK_INPUT, // public abstract int getNextBlockInput(Id block, int output);
        BLOCK_SCOPE, // public abstract Id getBlockScope(Id block);
        FIRST_SCOPE_BLOCK, // public abstract Id getFirstScopeBlock(Id scope);
        FIRST_SCOPE_BLOCK_INPUT, // public abstract int getFirstScopeBlockInput(Id scope);
        FORK_NEXT_BLOCKS, // public abstract Id[] getForkNextBlocks(Id block);
        FORK_NEXT_BLOCKS_INPUT, // public abstract int[] getForkNextBlocksInput(Id block);
        MERGE_BLOCK, // public abstract Id getMergeBlock(Id block);
        MERGE_BLOCK_N, // public abstract int getMergeBlockN(Id block);
        MERGE_BLOCK_M, // public abstract int getMergeBlockM(Id block);
        INCLUDE_BLOCK_ALGORITHM, // public abstract Id getIncludeBlockAlgorithm(Id block);
        WRITE_INLCUDE_BLOCK_PARAMETERS, // public abstract void writeIncludeBlockParameters(Id block);
        READ_INLCUDE_BLOCK_PARAMETERS, // public abstract void readIncludeBlockParameters(Id block);
        BLOCK_PROP_ID, // public abstract Id getBlockPropId(int Id, String propName);
        // Исполнить указанный блок. return номер следующего блока, -1 - выход из scope, 0 - переход к ожиданию
        EXECUTE_BLOCK, // public abstract int executeBlock(Id block) throws Throwable;
        EXECUTE_BLOCK_BODY, // public abstract int executeBlock(Id block) throws Throwable;
        // Поиск обработчика исключения указанного scope. return номер следующего блока
        FIND_CATCH, // public abstract Id findCatch(Id scope, Throwable e) throws Throwable;
        RETURN, // public abstract int getReturn(Id block) throws Throwable;
        VAR_GET_SET, // extendeds variable code
        TYPE_BY_ID // public abstract String getTitleById(Id id);
    };

    /*
     public CodePrinter registerInExecutor = CodePrinter.Factory.newJavaPrinter(); // public abstract void registerInExecutor(AlgorithmExecutor exec, String path);
     public CodePrinter getBlockType = CodePrinter.Factory.newJavaPrinter(); // public abstract int getBlockType(int block);
     public CodePrinter getNextBlock = CodePrinter.Factory.newJavaPrinter(); // public abstract Id getNextBlock(Id block, int output);
     public CodePrinter getNextBlockInput = CodePrinter.Factory.newJavaPrinter(); // public abstract int getNextBlockInput(Id block, int output);

     public CodePrinter getBlockScope = CodePrinter.Factory.newJavaPrinter(); // public abstract Id getBlockScope(Id block);
     public CodePrinter getFirstScopeBlock = CodePrinter.Factory.newJavaPrinter(); // public abstract Id getFirstScopeBlock(Id scope);
     public CodePrinter getFirstScopeBlockInput = CodePrinter.Factory.newJavaPrinter(); // public abstract int getFirstScopeBlockInput(Id scope);

     public CodePrinter getForkNextBlocks = CodePrinter.Factory.newJavaPrinter(); // public abstract Id[] getForkNextBlocks(Id block);
     public CodePrinter getForkNextBlocksInput = CodePrinter.Factory.newJavaPrinter(); // public abstract int[] getForkNextBlocksInput(Id block);

     public CodePrinter getMergeBlock = CodePrinter.Factory.newJavaPrinter(); // public abstract Id getMergeBlock(Id block);
     public CodePrinter getMergeBlockN = CodePrinter.Factory.newJavaPrinter(); // public abstract int getMergeBlockN(Id block);
     public CodePrinter getMergeBlockM = CodePrinter.Factory.newJavaPrinter(); // public abstract int getMergeBlockM(Id block);

     public CodePrinter getIncludeBlockAlgorithm = CodePrinter.Factory.newJavaPrinter(); // public abstract Id getIncludeBlockAlgorithm(Id block);
     public CodePrinter writeIncludeBlockParameters = CodePrinter.Factory.newJavaPrinter(); // public abstract void writeIncludeBlockParameters(Id block);
     public CodePrinter readIncludeBlockParameters = CodePrinter.Factory.newJavaPrinter(); // public abstract void readIncludeBlockParameters(Id block);
     public CodePrinter getBlockPropId = CodePrinter.Factory.newJavaPrinter(); // public abstract Id getBlockPropId(int Id, String propName);
     // Исполнить указанный блок. return номер следующего блока, -1 - выход из scope, 0 - переход к ожиданию
     public CodePrinter executeBlock = CodePrinter.Factory.newJavaPrinter(); // public abstract int executeBlock(Id block) throws Throwable;
     public CodePrinter executeBlockBody = CodePrinter.Factory.newJavaPrinter(); // public abstract int executeBlock(Id block) throws Throwable;
     // Поиск обработчика исключения указанного scope. return номер следующего блока
     public CodePrinter findCatch = CodePrinter.Factory.newJavaPrinter(); // public abstract Id findCatch(Id scope, Throwable e) throws Throwable;
     public CodePrinter getReturn = CodePrinter.Factory.newJavaPrinter(); // public abstract int getReturn(Id block) throws Throwable;
     public CodePrinter varGetSet = CodePrinter.Factory.newJavaPrinter(); // extended variable code
     */
    public AdsAlgoWriter(JavaSourceSupport support, AdsAlgoClassDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    protected void writeCustomHeader(CodePrinter printer) {
        printer.println();
        printer.println("import org.radixware.kernel.common.exceptions.AppError;");
        printer.println("import org.radixware.kernel.server.arte.Arte;");
        printer.println("import org.radixware.kernel.server.algo.AlgorithmExecutor;");
        printer.println("import org.radixware.kernel.common.types.Id;");
    }

    private void writePageBody(CodePrinter printer, PrinterType printerType, AdsPage page, Id scopeId) {
        final PageGraph graph = new PageGraph(page);

        for (AdsEdge edge : page.getEdges()) {
            if (!AppUtils.isExecutable(edge)) {
                continue;
            }

            final Id id = edge.getId();
            if (printerType == PrinterType.EXECUTE_BLOCK_BODY) {
                writeCode(printer, edge);
            }

            if (printerType == PrinterType.BLOCK_TYPE) {
                printer.println("if (block == " + id + ")");
                printer.enterBlock();
                printer.println("return AlgorithmExecutor.TRACE_BLOCK_TYPE;");
                printer.leaveBlock();
            }
            if (printerType == PrinterType.BLOCK_SCOPE) {
                printer.println("if (block == " + id + ")");
                printer.enterBlock();
                printer.println("return " + String.valueOf(scopeId) + ";");
                printer.leaveBlock();
            }
            if (printerType == PrinterType.EXECUTE_BLOCK) {
                printer.println("if (block == " + id + ")");
                printer.enterBlock();
                printer.println("return executeBlock" + id + "();");
                printer.leaveBlock();
            }

            AdsPin pin = edge.getTarget();
            Id next = graph.next(pin);
            int nextInput = graph.nextInput(pin);

            if (printerType == PrinterType.NEXT_BLOCK) {
                printer.println("if (block == " + id + ")");
                printer.enterBlock();
                printer.println("return " + next + ";");
                printer.leaveBlock();
            }
            if (printerType == PrinterType.NEXT_BLOCK_INPUT) {
                printer.println("if (block == " + id + ")");
                printer.enterBlock();
                printer.println("return " + nextInput + ";");
                printer.leaveBlock();
            }
        }

        for (AdsBaseObject node : page.getNodes()) {
            if (node instanceof AdsVarObject) {
                AdsVarObject var = (AdsVarObject) node;
                if (printerType == PrinterType.REGISTER_IN_EXECUTOR) {
                    writeDataRegistration(printer, var.getId(), var.getType(), var.getValue(), var.isValPersistent());
                }
                if (printerType == PrinterType.VAR_GET_SET) {
                    writeDataGetter(printer, var.getId(), var.getType());
                    writeDataSetter(printer, var.getId(), var.getType());
                }
            }

            if (node instanceof AdsStartObject) {
                AdsStartObject start = (AdsStartObject) node;
                AdsPin pin = start.getPins().get(0);

                Id first = graph.next(pin);
                int firstInput = graph.nextInput(pin);

                if (printerType == PrinterType.FIRST_SCOPE_BLOCK) {
                    printer.println("if (scope == " + String.valueOf(scopeId) + ")");
                    printer.enterBlock();
                    printer.println("return " + first + ";");
                    printer.leaveBlock();
                }
                if (printerType == PrinterType.FIRST_SCOPE_BLOCK_INPUT) {
                    printer.println("if (scope == " + String.valueOf(scopeId) + ")");
                    printer.enterBlock();
                    printer.println("return " + firstInput + ";");
                    printer.leaveBlock();
                }
            }

            if (!AppUtils.isExecutable(node)) {
                continue;
            }

            final Id id = node.getId();
            if (node instanceof AdsProgramObject
                    || node instanceof AdsScopeObject
                    || node instanceof AdsCatchObject
                    || node instanceof AdsIncludeObject
                    || node instanceof AdsAppObject
                    || node instanceof AdsMergeObject) {

                if (printerType == PrinterType.NEXT_BLOCK) {
                    printer.println("if (block == " + id + ") {");
                    printer.enterBlock();
                    printer.println("switch (output) {");
                    printer.enterBlock();
                }
                if (printerType == PrinterType.NEXT_BLOCK_INPUT) {
                    printer.println("if (block == " + id + ") {");
                    printer.enterBlock();
                    printer.println("switch (output) {");
                    printer.enterBlock();
                }

                List<AdsPin> pins = node.getSourcePins();
                for (int i = 0; i < pins.size(); i++) {
                    AdsPin pin = pins.get(i);

                    Id next = graph.next(pin);
                    int nextInput = graph.nextInput(pin);

                    if (printerType == PrinterType.NEXT_BLOCK) {
                        printer.println("case " + i + ":");
                        printer.enterBlock();
                        printer.println("return " + next + ";");
                        printer.leaveBlock();
                    }
                    if (printerType == PrinterType.NEXT_BLOCK_INPUT) {
                        printer.println("case " + i + ":");
                        printer.enterBlock();
                        printer.println("return " + nextInput + ";");
                        printer.leaveBlock();
                    }
                }

                if (printerType == PrinterType.NEXT_BLOCK) {
                    printer.println("default:");
                    printer.enterBlock();
                    printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
                    printer.leaveBlock(2);
                    printer.println("}");
                    printer.leaveBlock();
                    printer.println("}");
                }
                if (printerType == PrinterType.NEXT_BLOCK_INPUT) {
                    printer.println("default:");
                    printer.enterBlock();
                    printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
                    printer.leaveBlock(2);
                    printer.println("}");
                    printer.leaveBlock();
                    printer.println("}");
                }

            } else if (node.getSourcePins().size() > 0) {
                AdsPin pin = node.getSourcePins().get(0);

                Id next = graph.next(pin);
                int nextInput = graph.nextInput(pin);

                if (printerType == PrinterType.NEXT_BLOCK) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.println("return " + next + ";");
                    printer.leaveBlock();
                }
                if (printerType == PrinterType.NEXT_BLOCK_INPUT) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.println("return " + nextInput + ";");
                    printer.leaveBlock();
                }
            }

            if (printerType == PrinterType.BLOCK_TYPE) {
                printer.println("if (block == " + id + ")");
                printer.enterBlock();
                printer.println("return AlgorithmExecutor." + node.getKind() + "_BLOCK_TYPE;");
                printer.leaveBlock();
            }
            if (printerType == PrinterType.BLOCK_SCOPE) {
                printer.println("if (block == " + id + ")");
                printer.enterBlock();
                printer.println("return " + String.valueOf(scopeId) + ";");
                printer.leaveBlock();
            }

            if (AppUtils.isExecutableCode(node)) {
                if (printerType == PrinterType.EXECUTE_BLOCK_BODY) {
                    writeCode(printer, (IJavaSource) node);
                }
                if (printerType == PrinterType.EXECUTE_BLOCK) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.println("return executeBlock" + id + "();");
                    printer.leaveBlock();
                }
            }

            if (node instanceof AdsReturnObject) {
                if (printerType == PrinterType.RETURN) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.println("return " + getReturnIndex((AdsReturnObject) node) + ";");
                    printer.leaveBlock();
                }
            }

            if (node instanceof AdsScopeObject) {
                AdsScopeObject sc = (AdsScopeObject) node;
                writePageBody(printer, printerType, sc.getPage(), id);
            }

            if (node instanceof AdsCatchObject) {
                AdsCatchObject ch = (AdsCatchObject) node;
                writePageBody(printer, printerType, ch.getPage(), id);
            }

            if (node instanceof AdsAppObject) {
                AdsAppObject app = (AdsAppObject) node;

                if (printerType == PrinterType.BLOCK_PROP_ID) {
                    printer.println("if (block == " + id + ") {");
                    printer.enterBlock();
                }

                for (AdsAppObject.Prop prop : app.getProps()) {
                    if (printerType == PrinterType.REGISTER_IN_EXECUTOR) {
                        writeDataRegistration(printer, prop.getId(), prop.getType(), prop.getValue(), prop.getPersistent());
                    }
                    if (printerType == PrinterType.BLOCK_PROP_ID) {
                        printer.print("if (propName.equals(");
                        printer.printStringLiteral(prop.getSourceId() == null ? prop.getName() : prop.getSourceId().toString());
                        printer.println("))");
                        printer.enterBlock();
                        printer.print("return ");
                        WriterUtils.writeIdUsage(printer, prop.getId());
                        printer.println(";");
                        printer.leaveBlock();
                    }

                    if ((prop.getMode() & AdsAppObject.Prop.PUBLIC) == 0) {
                        continue;
                    }

                    if (printerType == PrinterType.VAR_GET_SET) {
                        writeDataGetter(printer, prop.getId(), prop.getType());
                        writeDataSetter(printer, prop.getId(), prop.getType());
                    }
                }

                if (printerType == PrinterType.BLOCK_PROP_ID) {
                    printer.println("throw new AppError(\"Invalid algorithm, unknown property #\" + propName);");
                    printer.leaveBlock();
                    printer.println("}");
                }
            }

            if (node instanceof AdsIncludeObject) {
                AdsIncludeObject include = (AdsIncludeObject) node;
                AdsAlgoClassDef algo = include.getAlgoDef();

                if (printerType == PrinterType.REGISTER_IN_EXECUTOR) {
                    writeAlgoRegistration(printer, id, algo.getId());
                }

                if (printerType == PrinterType.INCLUDE_BLOCK_ALGORITHM) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.print("return ");
                    WriterUtils.writeIdUsage(printer, algo.getId());
                    printer.println(";");
                    printer.leaveBlock();
                }

                if (printerType == PrinterType.WRITE_INLCUDE_BLOCK_PARAMETERS) {
                    printer.println("if (block == " + id + ") {");
                    printer.enterBlock();
                }

                if (printerType == PrinterType.READ_INLCUDE_BLOCK_PARAMETERS) {
                    printer.println("if (block == " + id + ") {");
                    printer.enterBlock();
                }

                for (AdsIncludeObject.Param param : include.getParams()) {
                    AdsAlgoClassDef.Param p = null;
                    try {
                        p = algo.getParams().getById(param.getOrigId());
                    } catch (Exception e) {
                        continue;
                    }
                    if (p == null) {
                        continue;
                    }

                    //assert p != null;
                    if (EParamDirection.IN.equals(p.getDirection()) || EParamDirection.BOTH.equals(p.getDirection())) {
                        if (printerType == PrinterType.WRITE_INLCUDE_BLOCK_PARAMETERS) {
                            printer.print("executor.setData(");
                            WriterUtils.writeIdUsage(printer, Id.Factory.append(param.getOrigId(), "-" + id));
                            printer.print(", executor.getData(");
                            WriterUtils.writeIdUsage(printer, param.getId());
                            printer.println("));");
                        }
                    }

                    if (EParamDirection.OUT.equals(p.getDirection()) || EParamDirection.BOTH.equals(p.getDirection())) {
                        if (printerType == PrinterType.READ_INLCUDE_BLOCK_PARAMETERS) {
                            printer.print("executor.setData(");
                            WriterUtils.writeIdUsage(printer, param.getId());
                            printer.print(", executor.getData(");
                            WriterUtils.writeIdUsage(printer, Id.Factory.append(param.getOrigId(), "-" + id));
                            printer.println("));");
                        }
                    }

                    if (printerType == PrinterType.REGISTER_IN_EXECUTOR) {
                        writeDataRegistration(printer, param.getId(), param.getType(), null, true);
                    }

                    if (printerType == PrinterType.VAR_GET_SET) {
                        writeDataGetter(printer, param.getId(), param.getType());
                        writeDataSetter(printer, param.getId(), param.getType());
                    }
                }

                if (printerType == PrinterType.WRITE_INLCUDE_BLOCK_PARAMETERS) {
                    printer.println("return;");
                    printer.leaveBlock();
                    printer.println("}");
                }

                if (printerType == PrinterType.READ_INLCUDE_BLOCK_PARAMETERS) {
                    printer.println("return;");
                    printer.leaveBlock();
                    printer.println("}");
                }
            }

            if (node instanceof AdsForkObject) {
                AdsMergeObject merge = null;
                AdsForkObject fork = (AdsForkObject) node;

                AdsPin pin = fork.getSourcePins().get(0);
                int size = graph.nextSize(pin);

                Id[] nexts = new Id[size];
                int[] nextInputs = new int[size];

                for (int i = 0; i < size; i++) {
                    AdsBaseObject ne = graph.nextNode(pin, i);

                    nexts[i] = graph.next(pin, i);
                    nextInputs[i] = graph.nextInput(pin, i);

                    if (merge != null) {
                        continue;
                    }

                    Set<AdsBaseObject> nodes = new HashSet<AdsBaseObject>();
                    Queue<AdsBaseObject> queue = new LinkedList<AdsBaseObject>();
                    queue.add(ne);

                    while (!queue.isEmpty()) {
                        AdsBaseObject n = queue.poll();

                        if (n instanceof AdsMergeObject) {
                            merge = (AdsMergeObject) n;
                            break;
                        }

                        if (nodes.contains(n)) {
                            continue;
                        } else {
                            nodes.add(n);
                        }

                        assert n != null && n.getSourcePins() != null : "node or node source pins can not be null"; // RADIX-8228
                        for (AdsPin p : n.getSourcePins()) {
                            AdsBaseObject nx = graph.nextNode(p);
                            queue.add(nx);
                        }
                    }

                }

                if (printerType == PrinterType.FORK_NEXT_BLOCKS) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.print("return ");
                    WriterUtils.writeIdArrayUsage(printer, nexts);
                    printer.println(";");
                    printer.leaveBlock();
                }

                if (printerType == PrinterType.FORK_NEXT_BLOCKS_INPUT) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.print("return ");
                    WriterUtils.writeIntArrayUsage(printer, nextInputs);
                    printer.println(";");
                    printer.leaveBlock();
                }

                assert merge != null : "fork must contain merge";

                merge.setN(size);
                int m = merge.getM();
                m = Math.max(m, 1);
                m = Math.min(m, size);
                merge.setM(m);

                final Id mergeId = merge.getId();
                if (printerType == PrinterType.MERGE_BLOCK) {
                    printer.println("if (block == " + id + ")");
                    printer.enterBlock();
                    printer.println("return " + mergeId + ";");
                    printer.leaveBlock();
                }

                if (printerType == PrinterType.MERGE_BLOCK_M) {
                    printer.println("if (block == " + mergeId + ")");
                    printer.enterBlock();
                    printer.println("return " + merge.getM() + ";");
                    printer.leaveBlock();
                }

                if (printerType == PrinterType.MERGE_BLOCK_N) {
                    printer.println("if (block == " + mergeId + ")");
                    printer.enterBlock();
                    printer.println("return " + merge.getN() + ";");
                    printer.leaveBlock();
                }
            }
        }

        // generate catches
        if (printerType == PrinterType.FIND_CATCH) {
            printer.println("if (scope == " + String.valueOf(scopeId) + ") {");
            printer.enterBlock();
            printer.println("try {");
            printer.enterBlock();
            printer.println("throw e0;");
            printer.leaveBlock();
            printer.println("}");

            List<AdsCatchObject> chList = new ArrayList<AdsCatchObject>();
            for (AdsBaseObject node : page.getNodes()) {
                if (node instanceof AdsCatchObject) {
                    chList.add((AdsCatchObject) node);
                }
            }

            Collections.sort(chList, new Comparator<AdsCatchObject>() {
                @Override
                public int compare(AdsCatchObject o1, AdsCatchObject o2) {
                    AdsExceptionClassDef ex1 = o1.getExceptionDef();
                    AdsExceptionClassDef ex2 = o2.getExceptionDef();
                    if (ex1 == null || ex2 == null) {
                        return 0;
                    }
                    if (ex1.getInheritance().isSubclassOf(ex2)) {
                        return -1;
                    }
                    if (ex2.getInheritance().isSubclassOf(ex1)) {
                        return +1;
                    }
                    return 0;
                }
            });

            for (AdsCatchObject ch : chList) {
                final Id id = ch.getId();
                printer.print("catch(");
                writeUsage(printer, AdsTypeDeclaration.Factory.newInstance(ch.getExceptionDef()), def);
                printer.println(" e) {");
                printer.enterBlock();
                printer.println("return " + id + ";");
                printer.leaveBlock();
                printer.println("}");
            }

            printer.println("catch(java.lang.Throwable e) {");
            printer.enterBlock();
            printer.println("throw e;");
            printer.leaveBlock();
            printer.println("}");
            printer.leaveBlock();
            printer.println("}");
        }
    }

    @Override
    protected void writeCustomBody(CodePrinter printer) {
        List<Id> ids = collectIds(def.getPage());
        for (Id id : ids) {
            printer.print("private final Id " + id + " = ");
            WriterUtils.writeIdUsage(printer, id);
            printer.println(";");
        }

        printer.println("public java.util.Set<Id> getBlockIds() {");
        printer.enterBlock();
        printer.print("return new java.util.HashSet<Id>(java.util.Arrays.asList(");
        int idx = 0;
        for (Id id : ids) {
            printer.print(String.valueOf(id));
            if (++idx < ids.size()) {
                printer.print(", ");
            }
        }
        printer.println("));");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public void registerInExecutor(AlgorithmExecutor exec, String path) {");
        printer.enterBlock();
        for (Param param : def.getParams()) {
            writeDataRegistration(printer, param.getId(), param.getType(), null, true);
        }
        writePageBody(printer, PrinterType.REGISTER_IN_EXECUTOR, def.getPage(), null);
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int getBlockType(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.BLOCK_TYPE, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id getNextBlock(Id block, int output) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.NEXT_BLOCK, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int getNextBlockInput(Id block, int output) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.NEXT_BLOCK_INPUT, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id getBlockScope(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.BLOCK_SCOPE, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id getFirstScopeBlock(Id scope) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.FIRST_SCOPE_BLOCK, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown scope #\" + scope);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int getFirstScopeBlockInput(Id scope) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.FIRST_SCOPE_BLOCK_INPUT, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown scope #\" + scope);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id[] getForkNextBlocks(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.FORK_NEXT_BLOCKS, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int[] getForkNextBlocksInput(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.FORK_NEXT_BLOCKS_INPUT, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id getMergeBlock(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.MERGE_BLOCK, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int getMergeBlockN(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.MERGE_BLOCK_N, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int getMergeBlockM(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.MERGE_BLOCK_M, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id getIncludeBlockAlgorithm(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.INCLUDE_BLOCK_ALGORITHM, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public void writeIncludeBlockParameters(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.WRITE_INLCUDE_BLOCK_PARAMETERS, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public void readIncludeBlockParameters(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.READ_INLCUDE_BLOCK_PARAMETERS, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id getBlockPropId(Id block, String propName) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.BLOCK_PROP_ID, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int executeBlock(Id block) throws Throwable {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.EXECUTE_BLOCK, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id findCatch(Id scope, Throwable e0) throws Throwable {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.FIND_CATCH, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown scope #\" + scope);");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public int getReturn(Id block) {");
        printer.enterBlock();
        writePageBody(printer, PrinterType.RETURN, def.getPage(), null);
        printer.println("throw new AppError(\"Invalid algorithm, unknown block #\" + block);");
        printer.leaveBlock();
        printer.println("}");

//        printer.println("public String getTitleById(Id id) {");
//        printer.enterBlock();
//        writeTitleById(printer);
//        printer.println("return null;");
//        printer.leaveBlock();
//        printer.println("}");
        for (Param param : def.getParams()) {
            writeDataGetter(printer, param.getId(), param.getType());
            writeDataSetter(printer, param.getId(), param.getType());
        }
        writePageBody(printer, PrinterType.VAR_GET_SET, def.getPage(), null);
        writePageBody(printer, PrinterType.EXECUTE_BLOCK_BODY, def.getPage(), null);

        // local definitions
        printer.print("public ");
        writeUsage(printer);
        printer.println("() {");
        printer.enterBlock();
        printer.println("super();");
        printer.leaveBlock();
        printer.println("}");

        printer.println("@SuppressWarnings(\"unused\")");
        printer.print("private final ");
        writeType(printer, def.getProcessType());
        printer.println(" getprocess() {");
        printer.enterBlock();
        printer.print("return (");
        writeType(printer, def.getProcessType());
        printer.println(")super.getProcess();");
        printer.leaveBlock();
        printer.println("}");

        printer.println("public Id getProcessClassId() {");
        printer.enterBlock();
        printer.print("return ");
        WriterUtils.writeIdUsage(printer, def.getProcessId());
        printer.println(";");
        printer.leaveBlock();
        printer.println("}");

        printer.print('@');
        printer.println(REFLECTIVE_CALLABLE_CLASS_NAME);
        printer.println("public static Id getWorkingClassId() {");
        printer.enterBlock();
        if (def.getReplacementId() != null && !org.radixware.kernel.common.utils.Utils.equals(def.getReplacementId(), def.getId())) {
            printer.print("return ");
            writeType(printer, AdsTypeDeclaration.Factory.newInstance(def.getReplacementDef()));
            printer.println(".getWorkingClassId();");
        } else {
            printer.print("return ");
            WriterUtils.writeIdUsage(printer, def.getId());
            printer.println(";");
        }
        printer.leaveBlock();
        printer.println("}");
    }

    /*
     * system utils
     */
    private void writeType(CodePrinter printer, AdsTypeDeclaration decl) {
        EValType typeId = decl.getTypeId();
        if (typeId == EValType.INT && decl.getExtStr() != null) {
            printer.print(decl.getExtStr());
            return;
        }
        if (typeId == EValType.STR && decl.getExtStr() != null) {
            printer.print(decl.getExtStr());
            return;
        }
        if (typeId == EValType.XML && decl.getPath() == null && decl.getExtStr() != null) {
            printer.print(decl.getExtStr());
            return;
        }
        writeUsage(printer, decl, def);
    }

    private void writeTypeId(CodePrinter printer, AdsTypeDeclaration decl) {
        EValType typeId = decl.getTypeId();
        if (AdsTypeDeclaration.equals(def, decl, AdsAlgoClassDef.ID_TYPE)) {
            typeId = EValType.STR;
        } else {
            final AdsType t = decl.resolve(def).get();
            if (t instanceof JavaType) {
                typeId = ((JavaType) t).getConversionType();
            }
        }
        WriterUtils.writeEnumFieldInvocation(printer, typeId);
    }
    private static final String ENTITY = "pdcEntity____________________";

    private void writeValue(CodePrinter printer, AdsTypeDeclaration decl, ValAsStr val) {
        if (val == null) {
            WriterUtils.writeNull(printer);
        } else {
            if (AdsTypeDeclaration.equals(def, decl, AdsAlgoClassDef.ID_TYPE)) {
                printer.printStringLiteral(val.toString());
                return;
            }
            EValType typeId = decl.getTypeId();
            if ((typeId == EValType.OBJECT || typeId == EValType.PARENT_REF) && decl.getPath() != null) {
                Id id = decl.getPath().getTargetId();
                if (EDefinitionIdPrefix.ADS_ENTITY_CLASS == id.getPrefix() || EDefinitionIdPrefix.ADS_APPLICATION_CLASS == id.getPrefix() || ENTITY.equals(id.toString())) {
                    Id entityId = Id.Factory.changePrefix(id, EDefinitionIdPrefix.DDS_TABLE);
                    String v = val.toString();
                    int idx = v.indexOf('\n');
                    if (idx >= 0) {
                        entityId = Id.Factory.loadFrom(v.substring(0, idx));
                        v = v.substring(idx + 1);
                    }
                    WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                    printer.print(".getEntityObject(new org.radixware.kernel.server.types.Pid(");
                    WriterUtils.writeServerArteAccessMethodInvocation(def, printer);
                    printer.print(", ");
                    WriterUtils.writeIdUsage(printer, entityId);
                    printer.print(", ");
                    printer.printStringLiteral(v);
                    printer.print("))");
                    return;
                }
            }
            AdsType t = decl.resolve(def).get();
            if (t instanceof JavaType) {
                typeId = ((JavaType) t).getConversionType();
            }

            WriterUtils.writeRadixValAsStr(printer, val);
            printer.print(".toObject(");
            WriterUtils.writeEnumFieldInvocation(printer, typeId);
            printer.print(")");
        }
    }

    private void writeDataRegistration(CodePrinter printer, Id id, AdsTypeDeclaration type, ValAsStr value, boolean persistent) {
        final boolean isCustomMap = type.equals(AppUtils.SUBMITVARIANT_BOOL_MAP_TYPE) || type.equals(AppUtils.SUBMITVARIANT_STR_MAP_TYPE);
        if (isCustomMap) {
            final String name = id.toString() + "_value";
            writeType(printer, type);
            printer.print(" " + name + " = new ");
            writeType(printer, type);
            printer.println("();");
            if (value != null) {
                final Map<EDwfFormSubmitVariant, String> values = AppUtils.parseAsStrMap(value.toString());
                for (Map.Entry<EDwfFormSubmitVariant, String> e : values.entrySet()) {
                    printer.print(name + ".put(");
                    writeType(printer, AppUtils.SUBMITVARIANT_TYPE);
                    printer.print(".getForValue(");
                    printer.printStringLiteral(e.getKey().getValue());
                    printer.print(")");
                    printer.printComma();
                    if (type.equals(AppUtils.SUBMITVARIANT_BOOL_MAP_TYPE)) {
                        printer.print("Boolean.valueOf(");
                        printer.printStringLiteral(e.getValue());
                        printer.print(")");
                    } else {
                        printer.printStringLiteral(e.getValue());
                    }
                    printer.println(");");
                }
            }
        }

        printer.print("exec.registerData(path, ");
        WriterUtils.writeIdUsage(printer, id);
        printer.printComma();
        writeTypeId(printer, type);
        printer.printComma();
        if ((type.getTypeId() == EValType.INT || type.getTypeId() == EValType.NUM) && type.getPath() != null) { // const
            printer.printStringLiteral(String.valueOf(type.getPath().getTargetId()));
        } else if ((type.getTypeId() == EValType.OBJECT || type.getTypeId() == EValType.PARENT_REF) && type.getPath() != null) { // entity
            printer.printStringLiteral(String.valueOf(type.getPath().getTargetId()));
        } else if (type.getTypeId() == EValType.XML) { // xml
            printer.print("\"");
            writeType(printer, type);
            printer.print("\"");
        } else {
            WriterUtils.writeNull(printer);
        }
        printer.printComma();
        if (isCustomMap) {
            printer.print(id.toString() + "_value");
        } else {
            writeValue(printer, type, value);
        }
        printer.printComma();
        printer.print(persistent);
        printer.println(");");
    }

    private void writeAlgoRegistration(CodePrinter printer, Id block, Id algoId) {
        printer.print("exec.registerAlgo(");
        printer.printStringLiteral("-" + block);
        printer.print(" + path, ");
        WriterUtils.writeIdUsage(printer, algoId);
        printer.print(", ");

        AdsClassDef algoDef = (AdsClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(def).findById(algoId).get();
        writeType(printer, AdsTypeDeclaration.Factory.newInstance(algoDef));

        printer.println(".getWorkingClassId());");
    }

    private void writeDataGetter(CodePrinter printer, Id id, AdsTypeDeclaration type) {
        printer.println("@SuppressWarnings({\"unused\",\"unchecked\"})");
        printer.print("public ");
        writeType(printer, type);
        printer.println(" get" + id + "() {");

        printer.enterBlock();
        AdsType t = type.resolve(def).get();
        if (AdsTypeDeclaration.equals(def, type, AdsAlgoClassDef.ID_TYPE)) {
            printer.print("Object dummy = getData(");
            WriterUtils.writeIdUsage(printer, id);
            printer.println(");");

            printer.println("if (dummy == null) {");

            printer.enterBlock();
            printer.println("return null;");
            printer.leaveBlock();

            printer.print("} else if (dummy instanceof ");
            writeType(printer, type);
            printer.println(") {");

            printer.enterBlock();
            printer.print("return (");
            writeType(printer, type);
            printer.println(") dummy;");
            printer.leaveBlock();

            printer.println("} else {");

            printer.enterBlock();
            printer.print("return ");
            writeType(printer, type);
            printer.println(".Factory.loadFrom((String) dummy);");
            printer.leaveBlock();

            printer.println("}");
        } else if (t instanceof JavaType) {
            printer.print("return ");
            PrimitiveTypeCodeWriter writer = (PrimitiveTypeCodeWriter) ((JavaType) t).getJavaSourceSupport().getCodeWriter(usagePurpose);
            writer.writeConversionFromObjTypeCode(printer, "getData(" + String.valueOf(WriterUtils.RADIX_ID_CLASS_NAME) + ".Factory.loadFrom(\"" + id.toString() + "\"))");
            printer.println(";");
        } else {
            EValType typeId = type.getTypeId();
            if (typeId == EValType.XML) {
                printer.print("Object dummy = getData(");
                WriterUtils.writeIdUsage(printer, id);
                printer.println(");");

                AdsTypeDeclaration clobType = AdsTypeDeclaration.Factory.newInstance(EValType.CLOB);
                printer.print("if (dummy instanceof ");
                writeType(printer, clobType);
                printer.println(") {");

                printer.enterBlock();
                printer.println("java.io.Reader stream = null;");
                printer.println("try {");
                printer.enterBlock();
                printer.print("stream = ((");
                writeType(printer, clobType);
                printer.println(")dummy).getCharacterStream();");
                printer.print("dummy = ");
                writeType(printer, type);
                printer.println(".Factory.parse(stream, new org.apache.xmlbeans.XmlOptions().setCharacterEncoding(\"UTF-8\"));");
                printer.print("setData(");
                WriterUtils.writeIdUsage(printer, id);
                printer.println(", dummy);");
                printer.leaveBlock();
                printer.println("} catch (java.lang.Exception e) {");
                printer.enterBlock();
                printer.println("throw new org.radixware.kernel.common.exceptions.WrongFormatError(\"Unable to read xml data\", e);");
                printer.leaveBlock();
                printer.println("} finally {");
                printer.enterBlock();
                printer.println("if (stream != null) { try { stream.close(); } catch(java.io.IOException e) {} }");
                printer.leaveBlock();
                printer.println("}");
                printer.leaveBlock();

                printer.println("}");

                printer.print("return (");
                writeType(printer, type);
                printer.println(")dummy;");
            } else if ((typeId == EValType.INT || typeId == EValType.STR) && (type.getPath() != null || type.getExtStr() != null)) {
                printer.print("Object dummy = getData(");
                WriterUtils.writeIdUsage(printer, id);
                printer.println(");");

                printer.println("if (dummy == null) {");

                printer.enterBlock();
                printer.println("return null;");
                printer.leaveBlock();

                printer.print("} else if (dummy instanceof ");
                writeType(printer, type);
                printer.println(") {");

                printer.enterBlock();
                printer.print("return (");
                writeType(printer, type);
                printer.println(") dummy;");
                printer.leaveBlock();

                printer.println("} else {");

                printer.enterBlock();
                printer.print("return ");
                writeType(printer, type);
                printer.print(".getForValue(");
                if (typeId == EValType.INT) {
                    printer.print("((");
                    writeType(printer, AdsTypeDeclaration.Factory.newInstance(typeId));
                    printer.print(") dummy).longValue()");
                } else {
                    printer.print("(");
                    writeType(printer, AdsTypeDeclaration.Factory.newInstance(typeId));
                    printer.print(") dummy");
                }
                printer.println(");");
                printer.leaveBlock();
                printer.println("}");
            } else if ((typeId == EValType.ARR_INT || typeId == EValType.ARR_STR) && (type.getPath() != null || type.getExtStr() != null)) {
                printer.print("Object dummy = getData(");
                WriterUtils.writeIdUsage(printer, id);
                printer.println(");");

                printer.println("if (dummy == null) {");

                printer.enterBlock();
                printer.println("return null;");
                printer.leaveBlock();

                printer.print("} else if (dummy instanceof ");
                writeType(printer, type);
                printer.println(") {");

                printer.enterBlock();
                printer.print("return (");
                writeType(printer, type);
                printer.println(") dummy;");
                printer.leaveBlock();

                printer.println("} else {");

                printer.enterBlock();
                printer.print("return new ");
                writeType(printer, type);
                printer.print("((");
                if (typeId == EValType.ARR_INT) {
                    writeType(printer, AdsTypeDeclaration.Factory.newInstance(EValType.ARR_INT));
                } else {
                    writeType(printer, AdsTypeDeclaration.Factory.newInstance(EValType.ARR_STR));
                }
                printer.println(") dummy);");
                printer.leaveBlock();
                printer.println("}");
            } else {
                printer.print("return (");
                writeType(printer, type);
                printer.print(")getData(");
                WriterUtils.writeIdUsage(printer, id);
                printer.println(");");
            }
        }

        printer.leaveBlock();
        printer.println("}");
    }

    private void writeDataSetter(CodePrinter printer, Id id, AdsTypeDeclaration type) {
        printer.println("@SuppressWarnings({\"unused\",\"unchecked\"})");
        printer.print("public void set" + id + "(");
        writeType(printer, type);
        printer.println(" v) {");
        printer.enterBlock();

        printer.print("setData(");
        WriterUtils.writeIdUsage(printer, id);
        printer.print(", ");

        AdsType t = type.resolve(def).get();
        if (t instanceof JavaType) {
            PrimitiveTypeCodeWriter writer = (PrimitiveTypeCodeWriter) ((JavaType) t).getJavaSourceSupport().getCodeWriter(usagePurpose);
            writer.writeConversionToObjTypeCode(printer, "v");
        } else {
            if (AdsTypeDeclaration.equals(def, type, AdsAlgoClassDef.ID_TYPE)) {
                printer.print("v == null ? null : v.toString()");
            } else {
                printer.print("v");
            }
        }
        printer.println(");");

        printer.leaveBlock();
        printer.println("}");
    }

    private int getReturnIndex(AdsReturnObject node) {
        AdsPage page = node.getOwnerPage();

        List<AdsReturnObject> nodes = new ArrayList<AdsReturnObject>();
        for (AdsBaseObject n : page.getNodes()) {
            if (n instanceof AdsReturnObject) {
                nodes.add((AdsReturnObject) n);
            }
        }

        Collections.sort(nodes, new Comparator<AdsReturnObject>() {

            @Override
            public int compare(AdsReturnObject node1, AdsReturnObject node2) {
                return node1.getBounds().x >= node2.getBounds().x ? +1 : -1;
            }
        });

        return nodes.indexOf(node);
    }
    /*
     private void writeTitleById(final CodePrinter printer) {
     def.visitAll(new IVisitor() {
     @Override
     public void accept(RadixObject obj) {
     Id id = null;
     String title = null;
     if (
     obj instanceof AdsVarObject ||
     obj instanceof AdsAlgoClassDef.Param ||
     //                    obj instanceof AdsAlgoClassDef.Var ||
     obj instanceof AdsAppObject ||
     obj instanceof AdsIncludeObject
     ) {
     final Definition def = (Definition)obj;
     id = def.getId();
     title = def.getName();
     } else if (obj instanceof AdsIncludeObject.Param) {
     final AdsIncludeObject.Param p = (AdsIncludeObject.Param)obj;
     id = p.getId();
     title = p.getOwnerDefinition().getName() + "." + p.getName();
     } else if (obj instanceof AdsAppObject.Prop) {
     final AdsAppObject.Prop p = (AdsAppObject.Prop)obj;
     if ((p.getMode() & AdsAppObject.Prop.PUBLIC) != 0) {
     id = p.getId();
     title = p.getOwnerDefinition().getName() + "." + p.getName();
     }
     }
     if (id != null) {
     printer.print("if (id == ");
     WriterUtils.writeIdUsage(printer, id);
     printer.println(")");
     printer.enterBlock();
     printer.print("return \"" + title + "\"");
     printer.println(";");
     printer.leaveBlock();
     }
     }
     });
     }
     */

    private List<Id> collectIds(AdsPage page) {
        final Set<Id> ids = new HashSet<Id>();

        for (AdsEdge edge : page.getEdges()) {
            if (!AppUtils.isExecutable(edge)) {
                continue;
            }
            ids.add(edge.getId());
        }

        for (AdsBaseObject node : page.getNodes()) {
            if (!AppUtils.isExecutable(node)) {
                continue;
            }
            ids.add(node.getId());
            if (node instanceof AdsScopeObject) {
                AdsScopeObject sc = (AdsScopeObject) node;
                ids.addAll(collectIds(sc.getPage()));
            }
            if (node instanceof AdsCatchObject) {
                AdsCatchObject ch = (AdsCatchObject) node;
                ids.addAll(collectIds(ch.getPage()));
            }
        }
        List<Id> result = new ArrayList<Id>(ids);
        Collections.sort(result);
        return result;
    }
}
