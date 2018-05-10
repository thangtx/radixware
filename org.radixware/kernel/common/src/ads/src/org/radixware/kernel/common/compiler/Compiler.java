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
package org.radixware.kernel.common.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilation;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.build.Make;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.module.IJavaModule;
import org.radixware.kernel.common.defs.ads.src.IJavaSourceProvider;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;

public class Compiler {

    public static class WorkspaceCache {

        private final Map<Layer, Map<ERuntimeEnvironmentType, AdsWorkspace>> workspaceMap = new HashMap<>();
        private AdsProblemReporter reporter;
        private static final Map<Branch, List<WorkspaceCache>> workspaceCaches = new WeakHashMap<>();

        public WorkspaceCache(AdsProblemReporter reporter) {

            this.reporter = reporter;
        }

        public AdsWorkspace get(Definition definition, ERuntimeEnvironmentType env) {
            final Layer layer = definition.getLayer();
            return get(layer, env);
        }

        public AdsWorkspace get(Layer layer, ERuntimeEnvironmentType env) {
            if (layer == null) {
                return null;
            }
            synchronized (workspaceMap) {
                Map<ERuntimeEnvironmentType, AdsWorkspace> map = workspaceMap.get(layer);
                if (map == null) {
                    map = new HashMap<>();
                    workspaceMap.put(layer, map);
                }
                AdsWorkspace ws = map.get(env);
                if (ws == null) {
                    ws = new AdsWorkspace(layer, env, reporter, false, true);
                    map.put(env, ws);
                }
                return ws;
            }
        }
    }
    private final WorkspaceCache workspaceCache;
    private IProblemHandler problemHandler;

    public Compiler() {
        this(null);
    }

    public Compiler(IProblemHandler problemHandler) {
        workspaceCache = new WorkspaceCache(new AdsProblemReporter(problemHandler));
        this.problemHandler = problemHandler;
    }

    public IProblemHandler getProblemHandler() {
        return problemHandler;
    }

    public CompilationResult compile(Definition definition, ERuntimeEnvironmentType env) {
        return compile(definition, env, false);
    }

    public CompilationResult compile(Definition definition, ERuntimeEnvironmentType env, boolean meta) {
        AdsWorkspace workspace = workspaceCache.get(definition, env);
        CompilationResult result = compile(workspace.findCompilationUnitDeclaration(definition, meta));
        if (workspace.getReporter().hasErrors()) {
            return null;
        } else {
            return result;
        }
    }

    public <T extends AdsDefinition & IJavaSourceProvider> CompilationResult compile(T definition, ERuntimeEnvironmentType env, String sourceId) {
        if (!AdsWorkspace.hasCompilableData(definition, env)) {
            return null;
        }
        AdsWorkspace workspace = workspaceCache.get(definition, env);
        CompilationResult result = compile(workspace.findCompilationUnitDeclaration(definition, false, sourceId));
        if (workspace.getReporter().hasErrors()) {
            return null;
        } else {
            return result;
        }
    }

    public void compile(Layer contextLayer, Collection<Definition> definitions, ERuntimeEnvironmentType env, Make.Requestor requestor, boolean force, IProgressHandle progressHandle, ICancellable cancellable, boolean useBinariesLookup) {
        AdsWorkspace workspace = new AdsWorkspace(contextLayer, env, new AdsProblemReporter(problemHandler), true, true);
        workspace.useBinariesLookup = useBinariesLookup;
        compile(workspace, definitions, requestor, force, progressHandle, cancellable, useBinariesLookup);
    }

    public void compile(AdsWorkspace workspace, Collection<Definition> definitions, Make.Requestor requestor, boolean force, IProgressHandle progressHandle, ICancellable cancellable, boolean useBinariesLookup) {
        final Layer contextLayer = workspace.getContextLayer();
        List<AdsCompilationUnitDeclaration> declarations = new ArrayList<>(definitions.size() * 2);
//        workspace.setSessionData(sessionData);
        int totalCount = definitions.size();
        final ERuntimeEnvironmentType env = workspace.getEnvType();

        if (progressHandle != null && totalCount > 0) {
            progressHandle.setDisplayName("Build (" + env.getName() + "): Javaize definitions");
            progressHandle.switchToDeterminate(totalCount);
        }

        //step 1
        int progress = 0;
        for (Definition definition : definitions) {
            if (cancellable != null && cancellable.wasCancelled()) {
                return;
            }
            if (definition.getLayer() != contextLayer) {
                continue;
            }
            if (definition instanceof AdsXmlSchemeDef) {
                AdsXmlSchemeDef xml = (AdsXmlSchemeDef) definition;
                if (xml.isTransparent()) {//TODO: only save xml source to result jar
                    if (requestor != null) {
                        requestor.acceptDefinition(xml);
                    }
                    continue;
                }
            }
            if (!AdsWorkspace.hasCompilableData(definition, env)) {
                continue;
            }
            AdsWorkspace.DeclarationData[] defDecls = workspace.findCompilationUnitDeclarations(definition);
            final File baseDir = JavaFileSupport.getBaseDirOrJarFile(definition instanceof IJavaModule ? (IJavaModule) definition : (IJavaModule) definition.getModule(), env, JavaFileSupport.EKind.SOURCE);

            final long definitionLastModifiedTime = definition instanceof AdsDefinition ? ((AdsDefinition) definition).getFileLastModifiedTime() : -1;

            if (defDecls != null) {
                if (defDecls.length == 0) {
                    if (definition instanceof AdsXmlSchemeDef) {
                        AdsXmlSchemeDef xml = (AdsXmlSchemeDef) definition;
                        if (requestor != null) {
                            requestor.acceptDefinition(xml);
                        }
                    }
                } else {
                    for (int i = 0; i < defDecls.length; i++) {
                        if (cancellable != null && cancellable.wasCancelled()) {
                            return;
                        }

                        if (defDecls[i] != null && defDecls[i].getEnvironmentType() == env) {
                            if (defDecls[i].declarationProcessed()) {
                                continue;
                            }

                            if (!force && definitionLastModifiedTime > 0) {
                                final String declFileName = defDecls[i].getDeclarationFileName();
                                File classFileName = new File(baseDir, declFileName.replace('.', '/') + ".class");
                                if (classFileName.exists() && classFileName.lastModified() > definitionLastModifiedTime) {
                                    continue;
                                }
                            }
                            Make.Requestor.UnitProcessingStatus status = requestor.getUnitProcessingStatus(defDecls[i]);
                            if (status != Make.Requestor.UnitProcessingStatus.NONE) {
                                if (status == Make.Requestor.UnitProcessingStatus.SUCCESS) {
                                    defDecls[i].reject();
                                }
                                continue;
                            }
                            declarations.add(defDecls[i].getDeclaration());
                        }
                    }
                }
            }
            if (progressHandle != null) {
                progress++;
                progressHandle.progress(progress);
            }
        }
        //step 2
        totalCount = declarations.size() * 6;
        progress = 0;

        if (progressHandle != null && totalCount > 0) {
            progressHandle.setDisplayName("Build (" + env.getName() + "): Compiling");
            progressHandle.switchToDeterminate(totalCount);
        }
        try {
            for (AdsCompilationUnitDeclaration.InitializationPhase phase : new AdsCompilationUnitDeclaration.InitializationPhase[]{
                AdsCompilationUnitDeclaration.InitializationPhase.BuildTypeBinding,
                AdsCompilationUnitDeclaration.InitializationPhase.CheckAnsSetImports,
                AdsCompilationUnitDeclaration.InitializationPhase.ConnectTypeHierarchy,
                AdsCompilationUnitDeclaration.InitializationPhase.CheckParameterizedTypes,
                AdsCompilationUnitDeclaration.InitializationPhase.BuildFieldsAndMethods}) {
                for (AdsCompilationUnitDeclaration declaration : declarations) {
                    if (cancellable != null && cancellable.wasCancelled()) {
                        return;
                    }
                    declaration.initialize(phase);
                    if (progressHandle != null) {
                        progress++;
                        progressHandle.progress(progress);
                    }
                }
            }

            //step 4        
            List<AdsCompilationUnitDeclaration> declarationsToProcess = declarations;

            while (!declarationsToProcess.isEmpty()) {
                for (AdsCompilationUnitDeclaration declaration : declarationsToProcess) {
                    if (cancellable != null && cancellable.wasCancelled()) {
                        return;
                    }
                    if (!declaration.compilationResult.hasErrors()) {
                        declaration.process();
                    }
                    if (cancellable != null && cancellable.wasCancelled()) {
                        return;
                    }
                    if (!declaration.compilationResult.hasErrors()) {
                        declaration.scope.verifyMethods(declaration.scope.environment().methodVerifier());
                    }
                    if (cancellable != null && cancellable.wasCancelled()) {
                        return;
                    }
                    try {
                        if (!declaration.compilationResult.hasErrors()) {
                            declaration.resolve();
                        }
                    } catch (AbortCompilation ex) {
                        if (requestor != null && requestor.problemHandler != null) {
                            requestor.problemHandler.accept(RadixProblem.Factory.newError(declaration.getContextDefinition(), ex.getMessage()));
                        }
                        throw ex;
                    }
                    if (cancellable != null && cancellable.wasCancelled()) {
                        return;
                    }
                    if (!declaration.compilationResult.hasErrors()) {
                        if (cancellable != null && cancellable.wasCancelled()) {
                            return;
                        }
                        //follow if no errors was found 
                        declaration.analyseCode(); // flow analysis
                        if (cancellable != null && cancellable.wasCancelled()) {
                            return;
                        }
                        if (!declaration.compilationResult.hasErrors()) {
                            declaration.generateCode(); // code generation   
                        }
                        declaration.finalizeProblems();
                    }
                    if (requestor != null) {
                        requestor.acceptResult(declaration.compilationResult);
                    }

                    declaration.processed = true;

                    if (progressHandle != null) {
                        progress++;
                        progressHandle.progress(progress);
                    }
                }
                workspace.markProcessedDeclarations();
                declarationsToProcess.clear();
                declarationsToProcess.addAll(workspace.getLookupEnvironment().cleanupAcceptedUnits());
            }
        } catch (AbortCompilation ex) {
            //ex.printStackTrace();

        }

    }

    public void compile(Definition definition, ERuntimeEnvironmentType env, ICompilerRequestor requestor) {
        AdsWorkspace workspace = new AdsWorkspace(definition.getLayer(), env, new AdsProblemReporter(problemHandler), false, true);
        compile(workspace, definition, env, requestor);
    }

    public void compile(AdsWorkspace workspace, Definition definition, ERuntimeEnvironmentType env, ICompilerRequestor requestor) {
        if (!AdsWorkspace.hasCompilableData(definition, env)) {
            return;
        }
        if (definition instanceof AdsXmlSchemeDef) {
            AdsXmlSchemeDef xml = (AdsXmlSchemeDef) definition;
            if (xml.isTransparent()) {
                return;
            }
        } else if (definition instanceof AdsClassDef) {
            AdsClassDef clazz = (AdsClassDef) definition;
            if (clazz.getTransparence() != null && clazz.getTransparence().isTransparent() && !clazz.getTransparence().isExtendable()) {
                return;
            }
        }

        AdsWorkspace.DeclarationData[] declarations = workspace.findCompilationUnitDeclarations(definition);

        if (declarations != null) {
            for (int i = 0; i < declarations.length; i++) {
                AdsCompilationUnitDeclaration declaration = declarations[i].getDeclaration();
                if (declaration != null) {
                    declaration.beginInitialization();
                }
            }

            for (int i = 0; i < declarations.length; i++) {
                AdsCompilationUnitDeclaration declaration = declarations[i].getDeclaration();
                if (declaration == null) {
                    continue;
                }
                declaration.completeInitialization();
            }

            for (int i = 0; i < declarations.length; i++) {
                AdsCompilationUnitDeclaration declaration = declarations[i].getDeclaration();
                if (declaration == null) {
                    continue;
                }
                if (!declaration.compilationResult.hasErrors()) {
                    declaration.process();
                }
                if (!declaration.compilationResult.hasErrors()) {
                    declaration.scope.verifyMethods(declaration.scope.environment().methodVerifier());
                }
                if (!declaration.compilationResult.hasErrors()) {
                    declaration.resolve();
                }
                if (!declaration.compilationResult.hasErrors()) {
                    //follow if no errors was found 
                    declaration.analyseCode(); // flow analysis
                    if (!declaration.compilationResult.hasErrors()) {
                        declaration.generateCode(); // code generation   
                    }
                    declaration.finalizeProblems();
                }
                if (requestor != null) {
                    requestor.acceptResult(declaration.compilationResult);
                }
            }
        }
    }

    private CompilationResult compile(final AdsCompilationUnitDeclaration declaration) {
        if (declaration != null) {
            // System.out.println("Compiling " + declaration.getContextDefinition().getQualifiedName());
            declaration.beginInitialization();
            if (!declaration.compilationResult.hasErrors()) {
                declaration.completeInitialization();
            }

            if (!declaration.compilationResult.hasErrors()) {
                declaration.process();
            }

            if (!declaration.compilationResult.hasErrors()) {
                declaration.scope.verifyMethods(declaration.scope.environment().methodVerifier());
            }

            if (!declaration.compilationResult.hasErrors()) {
                declaration.resolve();
            }

            if (!declaration.compilationResult.hasErrors()) {
                //follow if no errors was found 
                declaration.analyseCode(); // flow analysis
                if (!declaration.compilationResult.hasErrors()) {
                    declaration.generateCode(); // code generation   
                }
                declaration.finalizeProblems();
            }
            return declaration.compilationResult;

        } else {
            return null;
        }
    }
}
