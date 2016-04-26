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

package org.eclipse.jdt.internal.compiler.lookup;

import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.LineMatcher;


public class AdsCompilationUnitDeclaration extends CompilationUnitDeclaration {

    final Definition contextAdsDefinition;
    public final String extPath;
    public final RadixObjectLocator radixObjectLookup;
    private LineMatcher lineMatcher;
    public boolean processed;
    private String fileName;
    private final JavaSourceSupport.UsagePurpose usagePurpose;

    public AdsCompilationUnitDeclaration(Definition definition, AdsWorkspace ws, JavaSourceSupport.UsagePurpose up, ProblemReporter problemReporter, AdsWorkspace.AdsCompilationResult result, CompilerOptions options, boolean meta, CodePrinter printer) {
        this(definition, ws, up, problemReporter, result, options, meta, null, "", printer);
    }

    public Definition getContextDefinition() {
        return contextAdsDefinition;
    }

    public AdsWorkspace getWorkspace() {
        return ((AdsCompilationUnitScope) scope).getWorkspace();
    }

    public String getUnitFileName() {
        return fileName;
    }

    @Override
    public char[] getFileName() {
        return (getUnitFileName().replace('.', '/') + ".java").toCharArray();
    }

    public AdsCompilationUnitDeclaration(Definition definition, AdsWorkspace ws, JavaSourceSupport.UsagePurpose up, ProblemReporter problemReporter, AdsWorkspace.AdsCompilationResult result, CompilerOptions options, boolean meta, String extPath, String unitFileName, CodePrinter printer) {
        super(problemReporter, result, -1);
        if (printer != null) {
            radixObjectLookup = new RadixObjectLocator(printer);
            lineMatcher = printer.getLineMatcher();
        } else {
            radixObjectLookup = null;
        }
        this.extPath = extPath;
        this.contextAdsDefinition = (Definition) definition;
        this.usagePurpose = up;
        this.scope = new AdsCompilationUnitScope(this, ws);
        this.fileName = unitFileName==null?"":unitFileName;

        AdsTypeDeclaration decl = null;
//        if (meta) {
//            decl = new AdsMetaTypeDeclaration((AdsCompilationUnitScope) this.scope, definition, env, compilationResult, radixObjectLookup);
//        } else {
//            if (definition instanceof AdsClassDef) {
//                decl = new AdsClassDeclaration((AdsCompilationUnitScope) this.scope, (AdsClassDef) definition, env, compilationResult, radixObjectLookup);
//            } else if (definition instanceof AdsEnumDef) {
//                decl = new AdsEnumTypeDeclaration((AdsCompilationUnitScope) this.scope, (AdsEnumDef) definition, compilationResult, radixObjectLookup);
//            } else if (definition instanceof AbstractXmlDefinition) {
//                decl = new AdsXmlTypeDeclaration((AdsCompilationUnitScope) this.scope, (AbstractXmlDefinition) definition, extPath, compilationResult, radixObjectLookup);
//            }
//        }
//
//        if (decl != null) {
//            decl.attachOuterDeclarations(this, scope);
//            if (this.types == null) {
//                this.types = new TypeDeclaration[1];
//            } else {
//                TypeDeclaration[] expanded = new TypeDeclaration[this.types.length + 1];
//                System.arraycopy(this.types, 0, expanded, 0, this.types.length);
//            }
//            this.types[this.types.length - 1] = decl;
//        } else {
        this.types = new TypeDeclaration[0];
        //}
    }

    public ERuntimeEnvironmentType getEnvironmentType() {
        return usagePurpose.getEnvironment();
    }

    public JavaSourceSupport.UsagePurpose getUsagePurpose() {
        return usagePurpose;
    }

    public void beginInitialization() {
        if (initializationStatus == 0) {
            this.scope.buildTypeBindings(null);
            this.initializationStatus++;
        } else {
            System.err.println("fall through");
        }
    }
    private int initializationStatus = 0;

    public enum InitializationPhase {

        BuildTypeBinding,
        CheckAnsSetImports,
        ConnectTypeHierarchy,
        CheckParameterizedTypes,
        BuildFieldsAndMethods
    }

    public void initialize(InitializationPhase phase) {
        switch (phase) {
            case BuildTypeBinding:
                this.scope.buildTypeBindings(null);
                break;
            case CheckAnsSetImports:
                this.scope.checkAndSetImports();
                break;
            case ConnectTypeHierarchy:
                this.scope.connectTypeHierarchy();
                ;
                break;
            case CheckParameterizedTypes:
                this.scope.checkParameterizedTypes();
                break;
            case BuildFieldsAndMethods:
                this.scope.buildFieldsAndMethods();
                break;
        }
    }

    public void completeInitialization() {
        this.scope.checkAndSetImports();
        this.scope.connectTypeHierarchy();
        this.scope.checkParameterizedTypes();
        this.scope.buildFieldsAndMethods();
    }

    public char[] getSMAPData() {
        if (lineMatcher != null) {
            String fullFileName = String.valueOf(getFileName());
            fullFileName = fullFileName.replace('\\', '/');
            final int lastIndex = fullFileName.lastIndexOf('/');
            if (lastIndex != -1) {
                fullFileName = fullFileName.substring(lastIndex + 1, fullFileName.length());
            }
            return lineMatcher.getJSR045(fullFileName).toCharArray();
        } else {
            return null;
        }
    }

    public void process() {
        this.scope.faultInTypes();
    }

    public Module getModule() {
        return contextAdsDefinition.getModule();
    }

    public AdsTypeDeclaration findTypeFor(AdsDefinition clazz) {
        if (types == null) {
            return null;
        } else {
            for (int i = 0; i < types.length; i++) {
                if (types[i] instanceof AdsTypeDeclaration) {
                    AdsTypeDeclaration decl = (AdsTypeDeclaration) types[i];
                    if (decl.getDefinition() == clazz && decl.scope != null) {
                        decl.scope.connectTypeHierarchy();
                        return decl;
                    }
                }
            }
        }
        return null;
    }

    public ReferenceBinding findTypeFor(AdsDefinition clazz, String innerName) {
        if (types == null) {
            return null;
        } else {
            String[] innerNames = innerName.split("\\.");
            final char[][] innerNameChars = new char[innerNames.length][];
            for (int i = 0; i < innerNames.length; i++) {
                innerNameChars[i] = innerNames[i].toCharArray();
            }
            if (innerNameChars.length == 0) {
                return null;
            }
            char[] matchName = innerNameChars[0];
            for (int i = 0; i < types.length; i++) {
                if (types[i] instanceof AdsXmlTypeDeclaration) {
                    AdsXmlTypeDeclaration decl = (AdsXmlTypeDeclaration) types[i];
                    if (decl.getDefinition() == clazz && decl.scope != null && CharOperation.equals(decl.name, matchName)) {
                        decl.scope.connectTypeHierarchy();
                        int currentIndex = 1;
                        ReferenceBinding currentType = decl.binding;

                        while (currentIndex < innerNameChars.length) {
                            matchName = innerNameChars[currentIndex++];
                            currentType = currentType.getMemberType(matchName);
                            if (currentType == null) {
                                return null;
                            }
                        }
                        return currentType;
                    }
                }
            }
        }
        return null;
    }
}