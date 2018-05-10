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
package org.radixware.kernel.common.compiler.core.completion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.codeassist.complete.CompletionNodeFound;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnArgumentName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnBranchStatementLabel;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnClassLiteralAccess;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnExplicitConstructorCall;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnFieldName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnFieldType;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnKeyword;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnLocalName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMarkerAnnotationName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMemberAccess;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMemberValueName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMessageSend;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMessageSendName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnMethodName;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedAllocationExpression;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedNameReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnQualifiedTypeReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleNameReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionOnSingleTypeReference;
import org.eclipse.jdt.internal.codeassist.complete.CompletionScanner;
import org.eclipse.jdt.internal.codeassist.complete.InvalidCursorLocation;
import org.eclipse.jdt.internal.codeassist.impl.Keywords;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AbstractVariableDeclaration;
import org.eclipse.jdt.internal.compiler.ast.CaseStatement;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Initializer;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SwitchStatement;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.AdsBinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnit;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsRawTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.AdsSourceTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsTypeBinding;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.util.ObjectVector;

import org.radixware.kernel.common.compiler.CompilerUtils;
import org.radixware.kernel.common.compiler.IWorkspaceProvider;
import org.radixware.kernel.common.compiler.core.ast.JMLQualifiedNameReference;
import org.radixware.kernel.common.compiler.core.ast.JMLSingleNameReference;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEmbeddedClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.command.AdsContextlessCommandDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansInterface;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.userfunc.IUserFuncDef;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;
import org.radixware.kernel.common.utils.Utils;

public class JmlCompletionEngine {
    
    final static AdsProblemReporter completionReporter = new AdsProblemReporter();
    final AdsCompletionParser parser = new AdsCompletionParser(completionReporter, true);
    static final char[] classField = "class".toCharArray();
    static final char[] lengthField = "length".toCharArray();  //$NON-NLS-1$
    static final char[] cloneMethod = "clone".toCharArray();  //$NON-NLS-1$
    static final char[] THIS = "this".toCharArray();  //$NON-NLS-1$
    static final char[] THROWS = "throws".toCharArray();  //$NON-NLS-1$
    private char[] completionToken;
    private ScmlCompletionProvider.CompletionRequestor requestor;
    int startPosition, endPosition;
    Layer contextLayer;
    ERuntimeEnvironmentType contextEnv;
    private RadixObjectLocator.RadixObjectData itemData;
    private char[] source;
    private int completionPosition;
    private ReferenceBinding expectedBaseType;
    private Scml contextScml;

    private static class IntersectionProxyRequestor implements ScmlCompletionProvider.CompletionRequestor {

        private class AcceptableRef {

            ScmlCompletionProvider.CompletionItem item;
            boolean accepted = false;

            public void accept() {
                accepted = true;
                if (targetRequestor != null) {
                    targetRequestor.accept(item);
                }
            }

            public AcceptableRef(ScmlCompletionProvider.CompletionItem item) {
                this.item = item;
            }
        }
        private Map<String, AcceptableRef> items = new HashMap<>();
        private ScmlCompletionProvider.CompletionRequestor targetRequestor;
        private boolean isSecondStep = false;

        public IntersectionProxyRequestor(ScmlCompletionProvider.CompletionRequestor requestor) {
            this.targetRequestor = requestor;
        }

        @Override
        public void accept(ScmlCompletionProvider.CompletionItem item) {
            StringBuilder uuid = new StringBuilder();

            uuid.append(item.getLeadDisplayText()).append(" ").append(item.getTailDisplayText());

            String key = uuid.toString();
            final AcceptableRef ref = items.get(key);
            if (ref != null) {
                if (isSecondStep) {
                    ref.accept();
                }
            } else {
                if (!isSecondStep) {
                    items.put(key, new AcceptableRef(item));
                }
            }
        }

        @Override
        public boolean isAll() {
            return false;
        }
    }
    
    public static AdsProblemReporter getReporter() {
        return completionReporter;
    }

    public void complete(final Scml.Item context, final int offset, ScmlCompletionProvider.CompletionRequestor requestor) {
        final Scml scml = context.getOwnerScml();
        if (scml instanceof Jml) {
            this.contextScml = scml;
            final Jml jml = (Jml) scml;
            final ERuntimeEnvironmentType env = jml.getUsageEnvironment();

            final Definition owner = findCompilationUnitOwner(jml);
            if (owner instanceof IJavaSource) {
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    if (owner instanceof AdsClassDef && ((AdsClassDef) owner).isDual()) {
                        final IntersectionProxyRequestor proxyRequestor = new IntersectionProxyRequestor(requestor);
                        this.requestor = proxyRequestor;
                        assist(owner, ERuntimeEnvironmentType.EXPLORER, jml, context, offset);
                        proxyRequestor.isSecondStep = true;
                        assist(owner, ERuntimeEnvironmentType.WEB, jml, context, offset);
                        return;
                    }
                }
                this.requestor = requestor;
                assist(owner, env, jml, context, offset);
            }
        }
    }

    private Definition findCompilationUnitOwner(Jml jml) {
        AdsDefinition def = jml.getOwnerDef();
        while (def != null) {
            if (def instanceof AdsClassDef) {
                AdsClassDef clazz = (AdsClassDef) def;
                if (!clazz.isNested()) {
                    return clazz;
                } else {
                    def = clazz.getOwnerDef();
                }
            } else if (def instanceof AdsContextlessCommandDef) {
                return def;
            } else if (def instanceof IUserFuncDef) {
                return def;
            } else {
                def = def.getOwnerDef();
            }
        }
        return null;
    }
    AdsWorkspace ws;
        
    private void assist(Definition cuOwner, ERuntimeEnvironmentType env, Jml jml, Scml.Item context, int offset) {

        contextLayer = cuOwner.getLayer();
        contextEnv = env;
        if (requestor instanceof IWorkspaceProvider) {
            ws = ((IWorkspaceProvider)  requestor).getWorkspace(contextLayer, env);
        } else {
            ws = new AdsWorkspace(contextLayer, env, completionReporter, false, true);
        }
        final JavaSourceSupport.UsagePurpose up = JavaSourceSupport.UsagePurpose.getPurpose(env, JavaSourceSupport.CodeType.EXCUTABLE);
        final AdsCompilationUnit unit = new AdsCompilationUnit(cuOwner, up);
        final CodePrinter printer = CodePrinter.Factory.newJavaPrinter();

        char[][] className = ((IJavaSource) cuOwner).getJavaSourceSupport().getMainClassName(up);
        String unitFileName = String.valueOf(CharOperation.concatWith(className, '.'));
        final AdsCompilationUnitDeclaration declaration = new AdsCompilationUnitDeclaration(cuOwner, ws, up, completionReporter, new AdsWorkspace.AdsCompilationResult(unit), AdsWorkspace.getCompilerOptions(), false, null, unitFileName, printer);
        printer.putProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME, declaration.radixObjectLookup);
        ((IJavaSource) cuOwner).getJavaSourceSupport().getCodeWriter(JavaSourceSupport.UsagePurpose.getPurpose(env, JavaSourceSupport.CodeType.EXCUTABLE)).writeCode(printer);

        itemData = declaration.radixObjectLookup.take(context);

        if (itemData == null) {
            return;
        }
        source = printer.getContents();
        int cursorLocation = itemData.start;

        int o = 0;
        while (o < offset) {
            char c = source[cursorLocation];
            cursorLocation++;
            if (c == '\t') {
                continue;
            }
            o++;
        }

        cursorLocation--;

        this.completionPosition = cursorLocation + 1;
        try {
            parser.dietParse(declaration, source, cursorLocation);
        } catch (InvalidCursorLocation loc) {
            return;
        }

        if (declaration.types != null) {
            for (int i = 0; i < declaration.types.length; i++) {
                declaration.types[i] = new AdsSourceTypeDeclaration((AdsCompilationUnitScope) declaration.scope, cuOwner, env, declaration.types[i], null);
            }
            try {
                ((AdsCompilationUnitScope) declaration.scope).buildTypeBindings(null);

                if ((declaration.scope) != null) {
                    //this.source = declaration.getContents();                    
                    ws.getLookupEnvironment().completeTypeBindings(declaration, true);
                    declaration.scope.faultInTypes();
                    parseBlockStatements(declaration, source, cursorLocation);
                    declaration.traverse(new JMLCompletionAstConverter(), declaration.scope);
                    declaration.resolve();
                }
            } catch (CompletionNodeFound e) {
                if (e.astNode != null) {
                    ws.getLookupEnvironment().unitBeingCompleted = declaration;
                    assist(
                            e.astNode,
                            this.parser.assistNodeParent,
                            this.parser.enclosingNode,
                            declaration,
                            e.qualifiedBinding,
                            e.scope,
                            e.insideTypeAnnotation);
                }
            }
        }

    }

    protected ASTNode parseBlockStatements(CompilationUnitDeclaration unit, char[] source, int position) {
        int length = unit.types.length;
        for (int i = 0; i < length; i++) {
            TypeDeclaration type = unit.types[i];
            if (type.declarationSourceStart < position
                    && type.declarationSourceEnd >= position) {
                parser.scanner.setSource(source);
                return parseBlockStatements(type, unit, position);
            }
        }
        return null;
    }

    private ASTNode parseBlockStatements(
            TypeDeclaration type,
            CompilationUnitDeclaration unit,
            int position) {
        //members
        TypeDeclaration[] memberTypes = type.memberTypes;
        if (memberTypes != null) {
            int length = memberTypes.length;
            for (int i = 0; i < length; i++) {
                TypeDeclaration memberType = memberTypes[i];
//                if (memberType.headerStart > position) {
//                    continue;
//                }
                if (memberType.sourceStart > position) {
                    continue;
                }
                if (memberType.declarationSourceEnd >= position) {
                    return parseBlockStatements(memberType, unit, position);
                }
            }
        }
        //methods
        AbstractMethodDeclaration[] methods = type.methods;
        if (methods != null) {
            int length = methods.length;
            for (int i = 0; i < length; i++) {
                AbstractMethodDeclaration method = methods[i];
                if (method.bodyStart > position + 1) {
                    continue;
                }

                if (method.isDefaultConstructor()) {
                    continue;
                }

                if (method.declarationSourceEnd >= position) {
                    try {
                        parser.parseBlockStatements(method, unit);
                        return method;
                    } catch (InvalidCursorLocation e) {
                        Logger.getLogger(JmlCompletionEngine.class.getName()).log(Level.FINE, e.getMessage(), e);
                        continue;
                    }
                }
            }
        }
        //initializers
        FieldDeclaration[] fields = type.fields;
        if (fields != null) {
            int length = fields.length;
            for (int i = 0; i < length; i++) {
                FieldDeclaration field = fields[i];
                if (field.sourceStart > position) {
                    continue;
                }
                if (field.declarationSourceEnd >= position) {
                    if (field instanceof Initializer) {
                        try {
                            parser.parseBlockStatements((Initializer) field, type, unit);
                        } catch (InvalidCursorLocation e) {
                            Logger.getLogger(JmlCompletionEngine.class.getName()).log(Level.FINE, e.getMessage(), e);
                            continue;
                        }
                    }
                    return field;
                }
            }
        }

        return null;
    }

    private boolean assist(
            ASTNode astNode,
            ASTNode astNodeParent,
            ASTNode enclosingNode,
            CompilationUnitDeclaration compilationUnitDeclaration,
            Binding qualifiedBinding,
            Scope scope,
            boolean insideTypeAnnotation) {
        if (astNode instanceof CompletionOnFieldType) {
            completionOnFieldType(astNode, scope);
        } else if (astNode instanceof CompletionOnSingleNameReference) {
            assistOnOnSingleNameReference(astNode, astNodeParent, scope, insideTypeAnnotation);
        } else if (astNode instanceof CompletionOnSingleTypeReference) {
            assistOnSingleTypeReference(astNode, astNodeParent, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnQualifiedNameReference) {
            assistOnQualifiedNameReference(astNode, enclosingNode, qualifiedBinding, scope, insideTypeAnnotation);
        } else if (astNode instanceof CompletionOnQualifiedTypeReference) {
            assistOnQualifiedTypeReference(astNode, astNodeParent, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnMemberAccess) {
            assistOnMemberAccess(astNode, enclosingNode, qualifiedBinding, scope, insideTypeAnnotation);
        } else if (astNode instanceof CompletionOnMessageSend) {
            assistOnMessageSend(astNode, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnExplicitConstructorCall) {
            assistOnExplicitConstructorCall(astNode, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnQualifiedAllocationExpression) {
            assistOnQualifiedAllocationExpression(astNode, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnClassLiteralAccess) {
            assistOnClassLiteralAccess(astNode, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnMethodName) {
            assistOnMethodName(astNode, scope);
        } else if (astNode instanceof CompletionOnFieldName) {
            assistOnFieldName(astNode, scope);
        } else if (astNode instanceof CompletionOnLocalName) {
            assistOnLocalOrArgumentName(astNode, scope);
        } else if (astNode instanceof CompletionOnArgumentName) {
            assistOnLocalOrArgumentName(astNode, scope);
        } else if (astNode instanceof CompletionOnKeyword) {
            assistOnKeyword(astNode);
        } else if (astNode instanceof CompletionOnParameterizedQualifiedTypeReference) {
            assistOnParameterizedQualifiedTypeReference(astNode, astNodeParent, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnMarkerAnnotationName) {
            assistOnMarkerAnnotationName(astNode, qualifiedBinding, scope);
        } else if (astNode instanceof CompletionOnMemberValueName) {
            assistOnMemberValueName(astNode, astNodeParent, scope, insideTypeAnnotation);
        } else if (astNode instanceof CompletionOnBranchStatementLabel) {
            assistOnBranchStatementLabel(astNode);
        } else if (astNode instanceof CompletionOnMessageSendName) {
            assistOnMessageSendName(astNode, qualifiedBinding, scope);
        }
        return true;
    }

    private void setSourceRange(int start, int end) {
        this.setSourceRange(start, end, true);
    }

    private void setSourceRange(int start, int end, boolean emptyTokenAdjstment) {
        this.startPosition = start;
        if (emptyTokenAdjstment) {
            int endOfEmptyToken = ((CompletionScanner) this.parser.scanner).endOfEmptyToken;
            this.endPosition = endOfEmptyToken > end ? endOfEmptyToken + 1 : end + 1;
        } else {
            this.endPosition = end + 1;
        }
    }

    private void completionOnFieldType(ASTNode astNode, Scope scope) {
        CompletionOnFieldType field = (CompletionOnFieldType) astNode;
        CompletionOnSingleTypeReference type = (CompletionOnSingleTypeReference) field.type;
        this.completionToken = type.token;
        setSourceRange(type.sourceStart, type.sourceEnd);

        findTypesAndPackages(this.completionToken);

        findKeywordsForMember(this.completionToken, field.modifiers);

        if (!field.isLocalVariable && field.modifiers == ClassFileConstants.AccDefault) {
            SourceTypeBinding enclosingType = scope.enclosingSourceType();
            if (!enclosingType.isAnnotationType()) {
                findMethodDeclarations(
                        this.completionToken,
                        enclosingType,
                        scope,
                        new ObjectVector(),
                        null,
                        null,
                        null,
                        false);
            }
        }
    }

//    private int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
//        int len0 = lhs.length() + 1;
//        int len1 = rhs.length() + 1;
//
//        // the array of distances                                                       
//        int[] cost = new int[len0];
//        int[] newcost = new int[len0];
//
//        // initial cost of skipping prefix in String s0                                 
//        for (int i = 0; i < len0; i++) {
//            cost[i] = i;
//        }
//
//    // dynamically computing the array of distances                                  
//        // transformation cost for each letter in s1                                    
//        for (int j = 1; j < len1; j++) {
//            // initial cost of skipping prefix in String s1                             
//            newcost[0] = j;
//
//            // transformation cost for each letter in s0                                
//            for (int i = 1; i < len0; i++) {
//                // matching current letters in both strings                             
//                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;
//
//                // computing cost for each transformation                               
//                int cost_replace = cost[i - 1] + match;
//                int cost_insert = cost[i] + 1;
//                int cost_delete = newcost[i - 1] + 1;
//
//                // keep minimum cost                                                    
//                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
//            }
//
//            // swap cost/newcost arrays                                                 
//            int[] swap = cost;
//            cost = newcost;
//            newcost = swap;
//        }
//
//        // the distance is the cost for transforming all letters in both strings        
//        return cost[len0 - 1];
//    }
    /**
     * Test matching of name with completion filter in a way that
     * JmlCompletionEngine matches filter JmCo
     *
     */
    public static boolean nameMatchesCamelCaseIgnoringDots(final String name, final String filter) {
        if (Objects.equals(name, filter)) {
            return true;
        }
        if (name == null || filter == null) {
            return false;
        }

        if (startsWithIgnoreCaseAndDots(name, filter)) {
            return true;
        }
        
        return splitToWordsAndCheckForPrefix(name, filter);
    }
    
    private static boolean startsWithIgnoreCaseAndDots(final String name, final String filter) {
        if (name.isEmpty()) {
            return filter.isEmpty();
        }
        int i = 0;
        int j = 0;        
        for (; i < filter.length(); i++, j++) {
            if (j >= name.length()) {
                return false;
            }
            while (name.charAt(j) == '.') {
                j++;
                if (j >= name.length()) {
                    return false;
                }
            }
            if (Character.toLowerCase(filter.charAt(i)) != Character.toLowerCase(name.charAt(j))) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean splitToWordsAndCheckForPrefix(final String name, final String filter) {
        if (name.isEmpty()) {
            return filter.isEmpty();
        }
        int i = 0;
        int j = 0;
        for (; i < filter.length(); i++, j++) {
            if (j >= name.length()) {
                    return false;
            }
            if (i != 0 && Character.isUpperCase(filter.charAt(i))) {
                while(!Character.isUpperCase(name.charAt(j))) {
                    j++;
                    if (j >= name.length()) {
                        return false;
                    }
                }
            }
            if (filter.charAt(i) != name.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    private static String[] splitToSubWords(final String str) {
        final List<String> result = new ArrayList<>();
        if (str != null && !str.equals("")) {
            int curWordStart = 0;
            for (int i = 1; i <= str.length(); i++) {
                if (i == str.length() || isWordStart(str.charAt(i))) {
                    result.add(str.substring(curWordStart, i));
                    curWordStart = i;
                }
            }
        }
        return result.toArray(new String[result.size()]);
    }

    private static boolean isWordStart(final char c) {
        return Character.isUpperCase(c);
    }

    private int nameRelevance(String name, String filter) {
        if (filter.isEmpty()) {
            return 0;//no correction
        }
        if (name.equals(filter)) {
            return 100;
        }

        if (nameMatchesCamelCaseIgnoringDots(name, filter)) {
            return 70;
        }
        return -1;
    }

    private void findTypesAndPackages(char[] token) {
        final Set<Id> addedIds = new HashSet<>();
        final String tokenAsStr = String.valueOf(token);
        new Layer.HierarchyWalker().go(contextLayer, new Layer.HierarchyWalker.DefaultAcceptor() {
            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer radixObject) {
                radixObject.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof IAdsTypeSource) {

                            Id id = radixObject.getDefinition().getId();
                            if (addedIds.contains(id)) {
                                return;
                            }
                            addedIds.add(id);

                            IAdsTypeSource ts = (IAdsTypeSource) radixObject;

                            boolean envMatch = false;
                            for (ERuntimeEnvironmentType env : ts.getTypeUsageEnvironments()) {
                                if (env == ERuntimeEnvironmentType.COMMON) {
                                    envMatch = true;
                                    break;
                                } else if (env == ERuntimeEnvironmentType.SERVER || env == ERuntimeEnvironmentType.EXPLORER || env == ERuntimeEnvironmentType.WEB) {
                                    if (contextEnv == env) {
                                        envMatch = true;
                                        break;
                                    }
                                } else if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                                    if (contextEnv.isClientEnv()) {
                                        envMatch = true;
                                        break;
                                    }
                                }
                            }
                            if (envMatch) {
                                if (radixObject instanceof AbstractXmlDefinition) {
                                    AbstractXmlDefinition xml = (AbstractXmlDefinition) radixObject;
                                    if (xml.getSchemaTypeSystem() != null && xml.getSchemaTypeSystem().getInterfaceList() != null) {
                                        for (XBeansInterface iface : xml.getSchemaTypeSystem().getInterfaceList()) {
                                            acceptXbeansInterface(ts, 1, iface, tokenAsStr);
                                        }
                                    }
                                    return;
                                }
                                int nameRelevance = nameRelevance(radixObject.getName(), tokenAsStr);
                                if (nameRelevance < 0) {
                                    return;
                                }
                                boolean add;
                                if (expectedBaseType != null) {
                                    Definition definition = radixObject.getDefinition();
                                    List<String> subtypes = new LinkedList<>();
                                    if (definition instanceof AdsClassDef) {
                                        AdsClassDef clazz = (AdsClassDef) definition;
                                        while (clazz != null && clazz.isInner()) {
                                            subtypes.add(clazz.getId().toString());
                                            clazz = clazz.getOwnerClass();
                                        }
                                        definition = clazz;
                                    }
                                    ReferenceBinding binding = ws.getLookupEnvironment().findType(definition, contextEnv, null);
                                    if (binding == null || !binding.isValidBinding()) {
                                        return;
                                    }
                                    for (String sub : subtypes) {
                                        binding = binding.getMemberType(sub.toCharArray());
                                        if (binding == null || !binding.isValidBinding()) {
                                            return;
                                        }
                                    }
                                    add = isSubClassOf(binding, expectedBaseType);
                                } else {
                                    add = true;
                                }
                                if (add) {
                                    int relevance = 2 + nameRelevance;
                                    requestor.accept(new AdsTypeCompletionItem(ts, null, false, relevance, completionPosition - startPosition, endPosition - completionPosition));
                                }
                            }
                        }
                    }
                }, new VisitorProvider() {

                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return true;
                    }

                    @Override
                    public boolean isContainer(RadixObject radixObject) {
                        return !(radixObject instanceof DdsSegment);
                    }

                    @Override
                    public boolean isClassContainer(Class c) {
                        if (ILocalizingBundleDef.class.isAssignableFrom(c)) {
                            return false;
                        }
                        return super.isClassContainer(c);
                    }
                });
            }
        });

        if (expectedBaseType != null) {
            Definition def = null;
            if (expectedBaseType instanceof AdsBinaryTypeBinding) {
                def = ((AdsBinaryTypeBinding) expectedBaseType).getDefinition();
            } else if (expectedBaseType instanceof AdsTypeBinding) {
                def = ((AdsTypeBinding) expectedBaseType).getDefinition();
            } else if (expectedBaseType.isMemberType()) {

                ReferenceBinding enclosingType = expectedBaseType.enclosingType();
                while (enclosingType != null) {
                    if (enclosingType instanceof AdsTypeBinding) {
                        def = ((AdsTypeBinding) enclosingType).getDefinition();
                        break;
                    }
                    enclosingType = enclosingType.enclosingType();
                }
            }
            if (def != null) {
                return;
            }
        }

        ws.getLookupEnvironment().getNameEnvironment().invokeRequest(new AdsNameEnvironment.NameRequest() {
            @Override
            public boolean accept(char[][] packageName, char[] className, AdsNameEnvironment.ClassDataProvider provider) {
                String classNameAsString = String.valueOf(className);
                int index = classNameAsString.indexOf("$");
                if (index > 0) {
                    String partName = classNameAsString.substring(index + 1);
                    if (partName.length() > 0 && Character.isDigit(partName.charAt(0))) {
                            return false;
                        }
                    }
                int nameRelevance = nameRelevance(classNameAsString, tokenAsStr);
                if (nameRelevance >= 0) {
                    requestor.accept(new JmlTypeCompletionItem(className, packageName, false, nameRelevance + 1, completionPosition - startPosition, endPosition - completionPosition));
                }
                return false;
            }
        });
    }

    private static boolean isSubClassOf(ReferenceBinding type, ReferenceBinding parent) {
        ReferenceBinding b = type;
        while (b != null) {
            if (b == parent) {
                return true;
            }
            b = b.superclass();
        }

        if (parent.isInterface() && type.superInterfaces() != null) {
            ReferenceBinding[] ifaces = type.superInterfaces();
            for (int i = 0; i < ifaces.length; i++) {
                ReferenceBinding iface = ifaces[i];
                if (iface == parent) {
                    return true;
                }
            }
            for (int i = 0; i < ifaces.length; i++) {
                if (isSubClassOf(ifaces[i], parent)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void acceptXbeansInterface(IAdsTypeSource ts, int relevance, XBeansInterface iface, String tokenAsStr) {
        int nameRelevance = nameRelevance(iface.getName(), tokenAsStr);
        boolean foundByOwnerSchemeName = false;
        if (nameRelevance < 0) {
            final String ownerName = ((RadixObject) ts).getName();
            nameRelevance = nameRelevance(ownerName + iface.getName(), tokenAsStr);
            foundByOwnerSchemeName = true;
        }
        if (nameRelevance >= 0) {
            boolean add;
            if (expectedBaseType != null) {
                ReferenceBinding binding = ws.getLookupEnvironment().findType((Definition) ts, contextEnv, iface.getName());
                if (binding == null || !binding.isValidBinding()) {
                    return;
                }
                add = isSubClassOf(binding, expectedBaseType);
            } else {
                add = true;
            }
            if (add) {
                final AdsTypeCompletionItem item = new AdsTypeCompletionItem(ts, iface, false, relevance + nameRelevance, completionPosition - startPosition, endPosition - completionPosition);
                if (foundByOwnerSchemeName) {
                    item.sortText = ((RadixObject) ts).getName() + ":" + item.sortText;
                }
                requestor.accept(item);
            }
        }
        if (iface.getInnerInterfaces() != null) {
            final int rel = relevance + (foundByOwnerSchemeName ? 0 : 1);
            for (XBeansInterface inner : iface.getInnerInterfaces()) {
                acceptXbeansInterface(ts, rel, inner, tokenAsStr);
            }
        }
    }

    private void assistOnOnSingleNameReference(ASTNode astNode, ASTNode astNodeParent, Scope scope, boolean insideTypeAnnotation) {
        setSourceRange(astNode.sourceStart, astNode.sourceEnd);
        CompletionOnSingleNameReference singleNameReference = (CompletionOnSingleNameReference) astNode;
        this.completionToken = singleNameReference.token;

        final SwitchStatement outerSwitch = astNodeParent instanceof SwitchStatement ? (SwitchStatement) astNodeParent : null;
        if (outerSwitch != null && outerSwitch.expression != null && outerSwitch.expression.resolvedType == null) {
            try {
                outerSwitch.expression.resolveType((BlockScope) scope);
            } catch (Throwable ex) {
            }
        }
        if (outerSwitch != null && outerSwitch.expression != null && outerSwitch.expression.resolvedType != null && outerSwitch.expression.resolvedType.isEnum()) {
            final TypeBinding switchConditionType = outerSwitch.expression.resolvedType;
            if (switchConditionType != null && switchConditionType.isEnum()) {
                final ReferenceBinding enumType = (ReferenceBinding) switchConditionType;
                final boolean[] pubInfo = new boolean[]{false};
                final Definition definition = getDefinition(enumType, pubInfo);
                final String matchName = String.valueOf(this.completionToken);
                final CaseStatement[] cases = outerSwitch.cases;
                Set<String> usedNames = new HashSet<>();

                for (int i = 0; i < outerSwitch.caseCount; i++) {
                    Expression caseExpression = cases[i].constantExpression;
                    if ((caseExpression instanceof SingleNameReference)
                            && (caseExpression.resolvedType != null && caseExpression.resolvedType.isEnum())) {
                        usedNames.add(String.valueOf(((SingleNameReference) cases[i].constantExpression).token));
                    }
                }
                final FieldBinding[] fields = enumType.fields();
                final boolean isPub = pubInfo[0];
                AdsEnumDef enumeration = null;
                AdsEnumClassDef enumClass = null;
                if (definition instanceof AdsEnumDef) {
                    enumeration = (AdsEnumDef) definition;
                } else if (definition instanceof AdsEnumClassDef) {
                    enumClass = (AdsEnumClassDef) definition;
                }
                if (fields != null) {
                    for (int i = 0; i < fields.length; i++) {
                        final FieldBinding field = fields[i];
                        if (field.isSynthetic() || (field.modifiers & Flags.AccEnum) == 0) {
                            continue;
                        }

                        String fieldName = String.valueOf(field.name);
                        if (usedNames.contains(fieldName)) {
                            continue;
                        }
                        AdsEnumItemDef item = null;
                        AdsEnumClassFieldDef itemField = null;
                        if (enumeration != null) {
                            item = enumeration.getItems().findById(Id.Factory.loadFrom(fieldName), ExtendableDefinitions.EScope.ALL).get();
                            if (item == null) {
                                for (AdsEnumItemDef test : enumeration.getItems().get(ExtendableDefinitions.EScope.ALL)) {
                                    if (fieldName.equals(test.getPlatformItemName())) {
                                        item = test;
                                        break;
                                    }
                                }
                            }
                        }

                        if (item != null) {
                            fieldName = item.getName();
                        } else {
                            if (enumClass != null) {
                                Id id = Id.Factory.loadFrom(String.valueOf(field.name));
                                itemField = enumClass.getFields().findById(id, ExtendableDefinitions.EScope.ALL).get();
                                if (itemField != null) {
                                    fieldName = itemField.getName();
                                }
                            }
                        }
                        int nameRelevance = nameRelevance(fieldName, matchName);
                        if (nameRelevance >= 0) {
                            boolean added = false;
                            if (item != null) {
                                requestor.accept(new AdsEnumCompletionItem(item, 10,
                                        completionPosition - startPosition,
                                        endPosition - completionPosition));
                                added = true;
                            }
                            if (!added) {
                                if (itemField != null) {
                                    requestor.accept(new JmlFieldCompletionItem(field.name, true, true, itemField, enumClass.getQualifiedName(), nameRelevance + 10,
                                            completionPosition - startPosition,
                                            endPosition - completionPosition));
                                } else {
                                    requestor.accept(new JmlFieldCompletionItem(fieldName.toCharArray(),
                                            field.isStatic(),
                                            true,
                                            item,
                                            enumeration == null ? String.valueOf(switchConditionType.readableName()) : enumeration.getQualifiedName(),
                                            nameRelevance + 10,
                                            completionPosition - startPosition,
                                            endPosition - completionPosition));
                                }
                            }
                        }
                    }
                }
            }
        }

        findVariablesAndMethods(this.completionToken, scope, singleNameReference);

        findTypesAndPackages(this.completionToken);

        if (this.completionToken != null && this.completionToken.length != 0) {
            findKeywords(this.completionToken, singleNameReference.possibleKeywords, false, false);
        }
    }

    private void assistOnSingleTypeReference(ASTNode astNode, ASTNode astNodeParent, Binding qualifiedBinding, Scope scope) {
        if (astNode instanceof CompletionOnSingleTypeReference) {
            CompletionOnSingleTypeReference completionNode = (CompletionOnSingleTypeReference) astNode;
            this.completionToken = completionNode.token;
            if (astNodeParent instanceof AbstractVariableDeclaration) {
                AbstractVariableDeclaration decl = (AbstractVariableDeclaration) astNodeParent;
                if (decl.type != null && decl.type.resolvedType instanceof ReferenceBinding && decl.type.resolvedType.isValidBinding()) {
                    this.expectedBaseType = (ReferenceBinding) decl.type.resolvedType;
                }
            }
            setSourceRange(astNode.sourceStart, astNode.sourceEnd);
            findTypesAndPackages(this.completionToken);
        }
    }

    private void assistOnQualifiedNameReference(ASTNode astNode, ASTNode enclosingNode, Binding qualifiedBinding, Scope scope, boolean insideTypeAnnotation) {
        CompletionOnQualifiedNameReference ref = (CompletionOnQualifiedNameReference) astNode;
        this.completionToken = ref.completionIdentifier;
        final long pos = ref.sourcePositions[ref.sourcePositions.length - 1];

        if (qualifiedBinding.problemId() == ProblemReasons.NotFound) {
            setSourceRange((int) (pos >>> 32), (int) pos);
            if (ref.tokens.length == 1) {
                findMembersFromMissingType(ref.tokens[0], ref.sourcePositions[0], null, scope, ref, ref.isInsideAnnotationAttribute);
            }
        } else if (qualifiedBinding instanceof VariableBinding) {
            setSourceRange((int) (pos >>> 32), (int) pos);
            TypeBinding receiverType = ((VariableBinding) qualifiedBinding).type;
            if (receiverType != null && (receiverType.tagBits & TagBits.HasMissingType) == 0) {
                findFieldsAndMethods(this.completionToken, receiverType.capture(scope, ref.sourceEnd), scope, qualifiedBinding, ref);
            }
        } else if (qualifiedBinding instanceof ReferenceBinding && !(qualifiedBinding instanceof TypeVariableBinding)) {
            ReferenceBinding receiverType = (ReferenceBinding) qualifiedBinding;
            setSourceRange((int) (pos >>> 32), (int) pos);

            NameReference ref2 = ref.tokens.length > 1 ? new JMLQualifiedNameReference(ref, null) : new JMLSingleNameReference(ref.tokens[0], 0, 0);
            if (scope instanceof ClassScope) {
                ref2.resolveType((ClassScope) scope);
            } else if (scope instanceof BlockScope) {
                ref2.resolveType((BlockScope) scope);
            }

            boolean staticOnly = false;

            if (ref2.binding instanceof TypeBinding) {
                staticOnly = true;
            }

            findMembers(this.completionToken, receiverType, scope, ref, qualifiedBinding, staticOnly, true, true, true, staticOnly);

        } else if (qualifiedBinding instanceof PackageBinding) {
            setSourceRange(astNode.sourceStart, (int) pos);
            findTypesAndSubpackages(this.completionToken, (PackageBinding) qualifiedBinding, scope);
        }
    }

    private void assistOnQualifiedTypeReference(ASTNode astNode, ASTNode astNodeParent, Binding qualifiedBinding, Scope scope) {
    }

    private void assistOnMemberAccess(ASTNode astNode, ASTNode enclosingNode, Binding qualifiedBinding, Scope scope, boolean insideTypeAnnotation) {
        CompletionOnMemberAccess access = (CompletionOnMemberAccess) astNode;
        long pos = access.nameSourcePosition;
        setSourceRange((int) (pos >>> 32), (int) pos);

        this.completionToken = access.token;

        if (qualifiedBinding.problemId() == ProblemReasons.NotFound) {
        } else {
            if (!access.isInsideAnnotation) {
                //  findKeywords(this.completionToken, new char[][]{Keywords.NEW}, false, false);
                findFieldsAndMethods(this.completionToken, ((TypeBinding) qualifiedBinding).capture(scope, access.receiver.sourceEnd), scope, qualifiedBinding, access);
            }
        }
    }

    private void assistOnMessageSend(ASTNode astNode, Binding qualifiedBinding, Scope scope) {
        setSourceRange(astNode.sourceStart, astNode.sourceEnd, false);
        CompletionOnMessageSend messageSend = (CompletionOnMessageSend) astNode;
        TypeBinding[] argTypes = computeTypes(messageSend.arguments);
        this.completionToken = messageSend.selector;
        if (qualifiedBinding == null) {

            findImplicitMessageSends(this.completionToken, argTypes, scope, messageSend);

        } else {
            findMethods(this.completionToken, null, argTypes, (ReferenceBinding) ((ReferenceBinding) qualifiedBinding).capture(scope, messageSend.receiver.sourceEnd), scope, false, qualifiedBinding, messageSend);
        }
    }

    private void findImplicitMessageSends(
            char[] token,
            TypeBinding[] argTypes,
            Scope scope,
            InvocationSite invocationSite) {

        if (token == null) {
            return;
        }
        boolean staticsOnly = false;
        while (true) {
            switch (scope.kind) {

                case Scope.METHOD_SCOPE:
                    // handle the error case inside an explicit constructor call (see MethodScope>>findField)
                    MethodScope methodScope = (MethodScope) scope;
                    staticsOnly |= methodScope.isStatic | methodScope.isConstructorCall;
                    break;

                case Scope.CLASS_SCOPE:
                    ClassScope classScope = (ClassScope) scope;
                    SourceTypeBinding enclosingType = classScope.referenceContext.binding;
                    staticsOnly |= enclosingType.isStatic();
                    findMethods(token,
                            null,
                            argTypes,
                            enclosingType,
                            classScope,
                            staticsOnly,
                            null,
                            invocationSite);
                    break;
                case Scope.COMPILATION_UNIT_SCOPE:
                    return;
            }
            scope = scope.parent;
        }
    }

    private TypeBinding[] computeTypes(Expression[] arguments) {
        if (arguments == null) {
            return null;
        }
        int argsLength = arguments.length;
        TypeBinding[] argTypes = new TypeBinding[argsLength];
        for (int a = argsLength; --a >= 0;) {
            argTypes[a] = arguments[a].resolvedType;
        }
        return argTypes;
    }

    private void assistOnExplicitConstructorCall(ASTNode astNode, Binding qualifiedBinding, Scope scope) {
    }

    private void assistOnQualifiedAllocationExpression(ASTNode astNode, Binding qualifiedBinding, Scope scope) {
    }

    private void assistOnClassLiteralAccess(ASTNode astNode, Binding qualifiedBinding, Scope scope) {
    }

    private void assistOnMethodName(ASTNode astNode, Scope scope) {
    }

    private void assistOnFieldName(ASTNode astNode, Scope scope) {
    }

    private void assistOnLocalOrArgumentName(ASTNode astNode, Scope scope) {
    }

    private void assistOnKeyword(ASTNode astNode) {
    }

    private void assistOnParameterizedQualifiedTypeReference(ASTNode astNode, ASTNode astNodeParent, Binding qualifiedBinding, Scope scope) {
    }

    private void assistOnMarkerAnnotationName(ASTNode astNode, Binding qualifiedBinding, Scope scope) {
    }

    private void assistOnMemberValueName(ASTNode astNode, ASTNode astNodeParent, Scope scope, boolean insideTypeAnnotation) {
    }

    private void assistOnBranchStatementLabel(ASTNode astNode) {
    }

    private void assistOnMessageSendName(ASTNode astNode, Binding qualifiedBinding, Scope scope) {
    }

    private void findKeywordsForMember(char[] completionToken, int modifiers) {
    }

    private void findMethodDeclarations(char[] completionToken, SourceTypeBinding enclosingType, Scope scope, ObjectVector objectVector, Object object, Object object0, Object object1, boolean b) {
    }

    private void findFieldsAndMethods(char[] token,
            TypeBinding receiverType,
            Scope scope,
            Binding binding,
            InvocationSite invocationSite) {
        if (receiverType instanceof ReferenceBinding) {
            ReferenceBinding referenceBinding = (ReferenceBinding) receiverType;
//            findFields(token,
//                    referenceBinding,
//                    scope,
//                    false,
//                    binding,
//                    invocationSite);
//            findProperties(token,
//                    referenceBinding,
//                    scope,
//                    false,
//                    binding,
//                    invocationSite);
//            findMethods(token,
//                    null,
//                    null,
//                    referenceBinding,
//                    scope,
//                    false,
//                    binding,
//                    invocationSite);
            findMembers(token, referenceBinding, scope, invocationSite, binding, false, true, true, true, false);
        }
    }

    private void findMembersFromMissingType(char[] c, long l, Object object, Scope scope, CompletionOnQualifiedNameReference ref, boolean insideAnnotationAttribute) {
    }

    private void findTypesAndSubpackages(final char[] token, final PackageBinding packageBinding, Scope scope) {
        final Set<String> foundPackages = new HashSet<>();
        final String tokenAsStr = String.valueOf(token);
        this.ws.getLookupEnvironment().getNameEnvironment().invokeRequest(new AdsNameEnvironment.PackageNamePackageRequest() {
            @Override
            public boolean accept(char[][] packageName, char[] className, AdsNameEnvironment.ClassDataProvider provider) {
                String classNameAsString = String.valueOf(className);
                int index = classNameAsString.indexOf("$");
                if (index > 0) {
                    String partName = classNameAsString.substring(index + 1);
                    if (partName.length() > 0 && Character.isDigit(partName.charAt(0))) {
                        return false;
                    }
                }
                int nameRelevance = nameRelevance(classNameAsString, tokenAsStr);
                if (nameRelevance >= 0) {
                    requestor.accept(new JmlTypeCompletionItem(className, packageName, false, nameRelevance + 1, completionPosition - startPosition, endPosition - completionPosition));
                }

                return false;
            }

            @Override
            public boolean accept(char[][] packageName) {
                if (!CharOperations.startsWith(packageName[packageName.length - 1], token, 0)) {
                    return false;
                }
                String name = String.valueOf(CharOperation.concatWith(packageName, '.'));
                if (!foundPackages.contains(name)) {
                    foundPackages.add(name);
                    int index = name.lastIndexOf('.');
                    if (index > 0) {
                        requestor.accept(new JmlPackageCompletionItem(name.substring(index + 1), name.substring(0, index), 1, completionPosition - startPosition, 0));
                    } else {
                        requestor.accept(new JmlPackageCompletionItem(name, "", 1, completionPosition - startPosition, 0));
                    }
                }
                return false;
            }

            @Override
            public char[][] getPackageName() {
                return packageBinding.compoundName;
            }
        });
    }

    public String getTypeDisplayName(TypeBinding type) {
        if (type instanceof ReferenceBinding) {
            ReferenceBinding binding = (ReferenceBinding) type;
            boolean[] pubInfo = new boolean[]{false};
            Definition definition = getDefinition(binding, pubInfo);

            if (definition != null) {
                if (type instanceof ReferenceBinding) {
                    return CompilerUtils.getTypeDisplayName(definition, ((ReferenceBinding) type).compoundName);
                } else {
                    return String.valueOf(type.shortReadableName());
                }

            }
        }
        return String.valueOf(type.shortReadableName());
    }

    private Definition getDefinition(ReferenceBinding receiverType, boolean[] pubInfo) {
        boolean isPub = false;
        Definition definition = null;
        if (receiverType instanceof AdsRawTypeBinding) {
            receiverType = ((AdsRawTypeBinding) receiverType).genericType();
        }
        if (receiverType instanceof AdsTypeBinding) {
            definition = ((AdsTypeBinding) receiverType).getDefinition();
        } else if (receiverType instanceof AdsBinaryTypeBinding) {
            definition = ((AdsBinaryTypeBinding) receiverType).getDefinition();
            if (definition == null) {
                final IPlatformClassPublisher pub = ((AdsSegment) contextLayer.getAds()).getBuildPath().getPlatformPublishers().findPublisherByName(String.valueOf(CharOperation.concatWith(receiverType.compoundName, '.')).replace("$", "."));
                if (pub instanceof Definition) {
                    definition = (Definition) pub;
                    isPub = true;
                }
            }
        }
        if (definition == null && receiverType.isMemberType()) {
            ReferenceBinding type = receiverType;
            List<String> subdefinitions = new LinkedList<>();
            while (type != null) {

                if (type instanceof AdsTypeBinding) {
                    definition = ((AdsTypeBinding) type).getDefinition();
                } else if (type instanceof AdsBinaryTypeBinding) {
                    definition = ((AdsBinaryTypeBinding) type).getDefinition();
                }
                if (definition != null) {
                    break;
                } else {
                    subdefinitions.add(0, String.valueOf(type.sourceName));
                }
                type = type.enclosingType();
            }
            if (definition instanceof AdsDefinition) {
                AdsDefinition def = (AdsDefinition) definition;
                for (String subdef : subdefinitions) {
                    if ("DefaultGroupModel".equals(subdef) && definition instanceof AdsEntityObjectClassDef) {
                        AdsEntityClassDef entity = ((AdsEntityObjectClassDef) definition).findRootBasis();
                        if (entity != null) {
                            def = entity.findEntityGroup();
                            if (def != null) {
                                break;
                            }
                        }
                    }
                    Id id = Id.Factory.loadFrom(subdef);
                    if (id.getPrefix() == null) {
                        def = null;
                        break;
                    }
                    def = def.findComponentDefinition(id).get();
                    if (def instanceof AdsPropertyPresentationPropertyDef) {
                        AdsPropertyPresentationPropertyDef prop = (AdsPropertyPresentationPropertyDef) def;
                        if (prop.getEmbeddedClassAgent().getObject() != null) {
                            def = prop.getEmbeddedClassAgent().getObject();
                        }
                    }
                    if (def == null) {
                        break;
                    }
                }
//                if (def != null) {
//                    definition = def;
//                } else {
//                    definition = null;
//                }
                definition = def;
            }
        }
        if (definition instanceof AdsClassDef) {
            isPub = ((AdsClassDef) definition).getTransparence() != null && ((AdsClassDef) definition).getTransparence().isTransparent();
        }
        if (pubInfo != null) {
            pubInfo[0] = isPub;
        }
        return definition;
    }

    private void checkMethod(AdsClassDef clazz, boolean isPub, boolean lookForFields, boolean lookForProperty, boolean lookForMethod, MethodBinding method, String tokenAsStr, int relevance, Set<String> addedMethods) {
        if (method.isConstructor()) {
            return;
        }
        if (clazz != null) {
            boolean accepted = false;

            if (method.selector.length >= 29) {
                Id id = Id.Factory.loadFrom(String.valueOf(method.selector));
                if (lookForMethod) {
                    final AdsMethodDef adsMethod = clazz.getMethods().findById(id, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
                    if (adsMethod != null && isAccessible(adsMethod)) {
                        boolean checkId = tokenAsStr.length() >= 29 && tokenAsStr.startsWith("mth");

                        if (tokenAsStr.isEmpty()) {
                            String sign = String.valueOf(adsMethod.getProfile().getSignature(clazz));
                            if (!addedMethods.contains(sign)) {
                                addedMethods.add(sign);
                                requestor.accept(new AdsMethodCompletionItem(adsMethod, relevance, completionPosition - startPosition, endPosition - completionPosition));
                                accepted = true;
                            }
                        } else {
                            if (checkId) {
                                if (adsMethod.getId().toString().equals(tokenAsStr)) {
                                    String sign = String.valueOf(adsMethod.getProfile().getSignature(clazz));
                                    if (!addedMethods.contains(sign)) {
                                        addedMethods.add(sign);
                                        requestor.accept(new AdsMethodCompletionItem(adsMethod, relevance, completionPosition - startPosition, endPosition - completionPosition));
                                        accepted = true;
                                    }
                                }
                            } else {
                                int nameRelevance = nameRelevance(adsMethod.getName(), tokenAsStr);
                                if (nameRelevance >= 0) {
                                    String sign = String.valueOf(adsMethod.getProfile().getSignature(clazz));
                                    if (!addedMethods.contains(sign)) {
                                        addedMethods.add(sign);
                                        requestor.accept(new AdsMethodCompletionItem(adsMethod, relevance + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                        accepted = true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (lookForProperty) {
                    if (CharOperation.prefixEquals("get".toCharArray(), method.selector)) {
                        id = Id.Factory.loadFrom(String.valueOf(method.selector).substring(3));
                        IModelPublishableProperty.Support support = clazz.getModelPublishablePropertySupport();
                        AdsPropertyDef property = null;
                        if (support != null) {
                            IModelPublishableProperty prop = support.findById(id, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
                            if (prop instanceof AdsPropertyDef) {
                                property = (AdsPropertyDef) prop;
                            }
                        }
                        if (property == null) {
                            property = clazz.getProperties().findById(id, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
                        }
                        if (property != null && isAccessible(property)) {
                            int nameRelevance = nameRelevance(property.getName(), tokenAsStr);
                            if (nameRelevance >= 0) {
                                requestor.accept(new AdsPropertyCompletionItem(property, relevance + nameRelevance, completionPosition - startPosition, endPosition - completionPosition, contextEnv));
                                accepted = true;
                            }
                        }
                    }
                }
                
                if (lookForFields) {
                    if (clazz instanceof AdsEnumClassDef) {
                        if (CharOperation.prefixEquals("get".toCharArray(), method.selector)) {
                            id = Id.Factory.loadFrom(String.valueOf(method.selector, 3, method.selector.length - 3));
                            AdsEnumClassDef enumClassDef = (AdsEnumClassDef) clazz;
                            AdsFieldParameterDef field = enumClassDef.getFieldStruct().findById(id, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
                            if (field != null && isAccessible(field)) {
                                int nameRelevance = nameRelevance(field.getName(), tokenAsStr);
                                if (nameRelevance >= 0) {
                                    String name = field.getName();
                                    requestor.accept(new JmlFieldCompletionItem(name.toCharArray(), method.isStatic(), true, field, clazz.getQualifiedName(), relevance + 2 + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                    accepted = true;
            }
                            }
                        }
                    }
                }
            }
            if (isPub && !accepted) {
                if (lookForMethod) {
                    char[] sign = CharOperation.concat(method.selector, method.signature());
                    CharOperation.replace(sign, '/', '.');
                    final AdsMethodDef adsMethod = clazz.getMethods().findBySignature(
                            sign, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
                    if (adsMethod != null && isAccessible(adsMethod)) {
                        String adssign = String.valueOf(adsMethod.getProfile().getSignature(clazz));
                        if (!addedMethods.contains(adssign)) {
                            addedMethods.add(adssign);
                            if (tokenAsStr.isEmpty() || adsMethod.getName().startsWith(tokenAsStr)) {
                                requestor.accept(new AdsMethodCompletionItem(adsMethod, relevance, completionPosition - startPosition, endPosition - completionPosition));
                                accepted = true;
                            }
                        }
                    } else {
                        String selectorStr = String.valueOf(method.selector);

                        int nameRelevanceForMethod = nameRelevance(selectorStr, tokenAsStr);
                        int nameRelevanceForProperty = selectorStr.startsWith("get") ? nameRelevance(selectorStr.substring(3), tokenAsStr) : -1;

                        if (nameRelevanceForMethod >= 0 || nameRelevanceForProperty >= 0) {
                            char[] javaSign = method.genericSignature();
                            if (javaSign == null) {
                                javaSign = method.signature();
                            }
                            String signature = selectorStr + String.valueOf(javaSign).replace("/", ".");
                            if (!addedMethods.contains(signature)) {
                                addedMethods.add(signature);
                                if (nameRelevanceForMethod >= 0) {
                                    requestor.accept(new JmlMethodCompletionItem(method, relevance + nameRelevanceForMethod, completionPosition - startPosition, endPosition - completionPosition));
                                    accepted = true;
                                }
                                if (nameRelevanceForProperty >= 0) {
                                    //check type 
                                    if (method.parameters == null || method.parameters.length == 0 && method.returnType != null && method.returnType.id != TypeIds.T_void) {
                                        requestor.accept(new JmlFieldCompletionItem(selectorStr.substring(3).toCharArray(), method.isStatic(), false, null, String.valueOf(method.returnType.readableName()), nameRelevanceForProperty + relevance + 1, completionPosition - startPosition, endPosition - completionPosition));
                                        accepted = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {//pure java method
            String selectorStr = String.valueOf(method.selector);
            int relevanceAsMethod = nameRelevance(selectorStr, tokenAsStr);
            int relevanceAsProperty = selectorStr.startsWith("get") ? nameRelevance(selectorStr.substring(3), tokenAsStr) : -1;

            if (relevanceAsMethod >= 0 || relevanceAsProperty >= 0) {
                char[] javaSign = method.genericSignature();
                if (javaSign == null) {
                    javaSign = method.signature();
                }
                String sign = selectorStr + String.valueOf(javaSign).replace("/", ".");
                if (!addedMethods.contains(sign)) {
                    addedMethods.add(sign);
                    if (relevanceAsMethod >= 0) {
                        requestor.accept(new JmlMethodCompletionItem(method, relevance + relevanceAsMethod, completionPosition - startPosition, endPosition - completionPosition));
                    }
                    if (relevanceAsProperty >= 0) {
                        //check type 
                        if (method.parameters == null || method.parameters.length == 0 && method.returnType != null && method.returnType.id != TypeIds.T_void) {
                            requestor.accept(new JmlFieldCompletionItem(selectorStr.substring(3).toCharArray(), method.isStatic(), false, null, String.valueOf(method.returnType.readableName()), relevance + 1 + relevanceAsProperty, completionPosition - startPosition, endPosition - completionPosition));
                        }
                    }
                }
            }
        }
    }

    private boolean isAccessible(RadixObject obj) {
        boolean envMatches = false;
        if (obj instanceof IEnvDependent) {
            ERuntimeEnvironmentType env = ((IEnvDependent) obj).getUsageEnvironment();
            if (env == contextEnv) {
                envMatches = true;
            } else if (env == ERuntimeEnvironmentType.COMMON) {
                envMatches = true;
            } else if (env == ERuntimeEnvironmentType.COMMON_CLIENT && contextEnv.isClientEnv()) {
                envMatches = true;
            } else if (obj instanceof AdsPropertyDef && env == ERuntimeEnvironmentType.SERVER && contextEnv.isClientEnv()) {
                env = ((AdsPropertyDef) obj).getClientEnvironment();
                if (env == ERuntimeEnvironmentType.COMMON_CLIENT || env == contextEnv) {
                    envMatches = true;
                }
            }

        } else {
            envMatches = true;
        }
        if (!envMatches) {
            return false;
        }
        if (obj instanceof AdsDefinition) {
            if (!AdsUtils.checkAccessibility(contextScml, (AdsDefinition) obj, false, null)) {
                return false;
            }
        }
        return true;
    }

    private void findMembers(
            final char[] token,
            final ReferenceBinding receiverType,
            final Scope scope,
            final InvocationSite invocationSite,
            final Binding binding,
            final boolean staticsOnly,
            final boolean lookForFields,
            final boolean lookForProps,
            final boolean lookForMethods,
            final boolean lookForMemberTypes) {
        final String tokenAsStr = String.valueOf(token);
        final Set<String> signs = new HashSet<>();
        iterateHierarchy(receiverType, new TypeProcessor() {
            @Override
            public void process(ReferenceBinding type, int level) {
                boolean[] pubInfo = new boolean[]{false};
                Definition definition = getDefinition(type, pubInfo);
                boolean isPub = pubInfo[0];

                final AdsClassDef clazz = definition instanceof AdsClassDef ? ((AdsClassDef) definition) : null;

                int relevance = 1 - level;

                if (clazz != null) {
                    if (lookForMethods) {
                        for (final AdsMethodDef method : clazz.getMethods().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE, new IFilter<AdsMethodDef>() {
                            @Override
                            public boolean isTarget(AdsMethodDef radixObject) {
                                if (staticsOnly && !radixObject.getAccessFlags().isStatic()) {
                                    return false;
                                }
                                return !radixObject.isConstructor() && isAccessible(radixObject);
                            }
                        })) {
                            int nameRelevance = nameRelevance(method.getName(), tokenAsStr);
                            if (nameRelevance >= 0) {
                                String sign = String.valueOf(method.getProfile().getSignature(clazz));
                                if (!signs.contains(sign)) {
                                    signs.add(sign);
                                    requestor.accept(new AdsMethodCompletionItem(method, relevance + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                }
                            }
                        }
                    }
                }

                if (lookForProps || lookForMethods || lookForFields) {
                    final MethodBinding[] methods = type.methods();
                    for (int i = 0; i < methods.length; i++) {
                        final MethodBinding method = methods[i];
                        if (staticsOnly && !method.isStatic()) {
                            continue;
                        }
                        if (String.valueOf(method.selector).contains("$")) {
                            continue;
                        }
                        if (!method.canBeSeenBy(invocationSite, scope)) {
                            //continue;
                        }
                        checkMethod(clazz, isPub, lookForFields, lookForProps, lookForMethods, method, tokenAsStr, relevance, signs);
                    }
                }
                if (lookForFields) {
                    final FieldBinding[] fields = type.fields();
                    for (int i = 0; i < fields.length; i++) {
                        final FieldBinding field = fields[i];
                        if (staticsOnly && !field.isStatic()) {
                            continue;
                        }
                        if (!field.canBeSeenBy(receiverType, invocationSite, scope)) {
                            continue;
                        }
                        String fieldNameStr = String.valueOf(field.name);
                        if (fieldNameStr.contains("$")) {
                            continue;
                        }
                        if (isPub && clazz != null) {
                            char[] sign = field.type.genericTypeSignature();
                            if (sign == null) {
                                sign = field.type.signature();
                            }
                            if (sign != null) {
                                char[] fieldSign = CharOperation.concat(field.name, sign);
                                AdsPropertyDef property = clazz.getProperties().findBySignature(fieldSign, ExtendableDefinitions.EScope.ALL);
                                if (property != null) {
                                    requestor.accept(new AdsPropertyCompletionItem(property, relevance, completionPosition - startPosition, endPosition - completionPosition, contextEnv));
                                    continue;
                                }
                            }
                        }
                        if ((field.modifiers & ClassFileConstants.AccEnum) != 0 && field.type instanceof ReferenceBinding) {
                            Definition possibleEnum = getDefinition((ReferenceBinding) field.type, pubInfo);
                            if (possibleEnum instanceof AdsEnumDef) {
                                AdsEnumDef enumeration = (AdsEnumDef) possibleEnum;
                                Id asId = Id.Factory.loadFrom(fieldNameStr);
                                String matchName = fieldNameStr;
                                AdsEnumItemDef match = enumeration.getItems().findById(asId, ExtendableDefinitions.EScope.ALL).get();
                                if (match == null) {
                                    for (AdsEnumItemDef item : enumeration.getItems().get(ExtendableDefinitions.EScope.ALL)) {
                                        if (Utils.equals(item.getPlatformItemName(), fieldNameStr)) {
                                            match = item;
                                            break;
                                        }
                                    }
                                    if (match != null) {
                                        matchName = match.getName();
                                    }
                                } else {
                                    matchName = match.getName();
                                }
                                int nameRelevance = nameRelevance(matchName, tokenAsStr);
                                if (nameRelevance >= 0) {
                                    String typeName = getTypeDisplayName(field.type);
                                    requestor.accept(new JmlFieldCompletionItem(field.name, field.isStatic(), true, match, typeName, relevance + 1 + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                }
                            } else if (possibleEnum instanceof AdsEnumClassDef) {
                                Id asId = Id.Factory.loadFrom(fieldNameStr);

                                AdsEnumClassDef enumeration = (AdsEnumClassDef) possibleEnum;
                                AdsEnumClassFieldDef match = enumeration.getFields().findById(asId, ExtendableDefinitions.EScope.ALL).get();
                                if (match != null) {
                                    String matchName = match.getName();
                                    int nameRelevance = nameRelevance(matchName, tokenAsStr);
                                    if (nameRelevance >= 0) {
                                        String typeName = getTypeDisplayName(field.type);
                                        requestor.accept(new JmlFieldCompletionItem(field.name, field.isStatic(), true, match, typeName, relevance + 1 + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                    }
                                } else {
                                    int nameRelevance = nameRelevance(fieldNameStr, tokenAsStr);
                                    if (nameRelevance >= 0) {
                                        String typeName = getTypeDisplayName(field.type);
                                        requestor.accept(new JmlFieldCompletionItem(field.name, field.isStatic(), false, null, typeName, relevance + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                    }
                                }
                            } else {
                                int nameRelevance = nameRelevance(fieldNameStr, tokenAsStr);
                                if (nameRelevance >= 0) {
                                    String typeName = getTypeDisplayName(field.type);
                                    requestor.accept(new JmlFieldCompletionItem(field.name, field.isStatic(), true, null, typeName, relevance + 1 + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                }
                            }
                        } else {
                            if (field.name.length >= 29) {
                                Definition def = getDefinition(field.declaringClass, pubInfo);
                                if (def instanceof AdsDefinition) {
                                    if (!((AdsDefinition) def).findComponentDefinition(Id.Factory.loadFrom(String.valueOf(field.name))).isEmpty()) {
                                        continue;
                                    }
                                }

                            }
                            int nameRelevance = nameRelevance(fieldNameStr, tokenAsStr);
                            if (nameRelevance >= 0) {
                                String typeName = getTypeDisplayName(field.type);
                                requestor.accept(new JmlFieldCompletionItem(field.name, field.isStatic(), false, null, typeName, relevance + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                            }
                        }
                    }
                    if (staticsOnly) {
                        int nameRelevance = nameRelevance("class", tokenAsStr);
                        if (nameRelevance >= 0) {
                            String typeName = "java.lang.Class";
                            requestor.accept(new JmlFieldCompletionItem("class".toCharArray(), true, false, null, typeName, relevance + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                        }
                    }
                }
                if (lookForMemberTypes) {
                    final ReferenceBinding[] members = type.memberTypes();
                    if (members != null) {
                        for (int i = 0; i < members.length; i++) {
                            ReferenceBinding memberType = members[i];

                            boolean[] memberPubInfo = new boolean[]{false};
                            Definition def = getDefinition(memberType, memberPubInfo);
                            String memberNameStr = String.valueOf(memberType.sourceName);
                            if (def instanceof IAdsTypeSource) {
                                if (def instanceof AdsEmbeddedClassDef) {
                                    continue;
                                }
                                int nameRelevance = nameRelevance(memberNameStr, tokenAsStr);
                                if (nameRelevance >= 0) {
                                    requestor.accept(new AdsTypeCompletionItem((IAdsTypeSource) def, null, true, relevance + 1 + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                }
                            } else {
                                int nameRelevance = nameRelevance(memberNameStr, tokenAsStr);
                                if (nameRelevance >= 0) {
                                    List<char[]> suffixes = new LinkedList<>();
                                    suffixes.add(memberType.sourceName);
                                    ReferenceBinding owner = memberType.enclosingType();
                                    while (owner != null) {
                                        def = getDefinition(owner, pubInfo);
                                        if (def instanceof IAdsTypeSource) {
                                            break;
                                        }
                                        suffixes.add(0, owner.sourceName);
                                        owner = owner.enclosingType();
                                    }
                                    if (def instanceof AdsDefinition) {

                                        AdsDefinition test = (AdsDefinition) def;
                                        for (char[] s : suffixes) {
                                            Id id = Id.Factory.loadFrom(String.valueOf(s));
                                            test = test.findComponentDefinition(id).get();
                                            if (test == null) {
                                                break;
                                            } else {
                                                def = test;
                                            }
                                        }
                                    }
                                    if (def instanceof IAdsTypeSource) {
                                        char[][] names = suffixes.toArray(new char[suffixes.size()][]);
                                        if (names.length == 1) {
                                            String asStr = String.valueOf(names[0]);
                                            if (asStr.startsWith("Access___")) {
                                                continue;
                                            }
                                        }
                                        requestor.accept(new JmlTypeCompletionItem(names, contextScml, AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) def), true, relevance + 1 + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                    } else {
                                        requestor.accept(new JmlTypeCompletionItem(memberType.sourceName, memberType.enclosingType().compoundName, true, relevance + 1 + nameRelevance, completionPosition - startPosition, endPosition - completionPosition));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
                0);
    }

    private interface TypeProcessor {

        public void process(ReferenceBinding type, int level);
    }

    private void iterateHierarchy(ReferenceBinding type, TypeProcessor func, int level) {
        if (type != null) {
            func.process(type, level);
            if (type.superclass() != null) {
                iterateHierarchy(type.superclass(), func, level + 1);
            }

            ReferenceBinding[] ifaces = type.superInterfaces();
            if (ifaces != null) {
                for (int i = 0; i < ifaces.length; i++) {
                    iterateHierarchy(ifaces[i], func, level + 1);
                }
            }
        }
    }

    private void findKeywords(char[] keyword, char[][] choices, boolean staticFieldsAndMethodOnly, boolean ignorePackageKeyword) {
        if (choices == null || choices.length == 0) {
            return;
        }
        int length = keyword.length;
        for (int i = 0; i < choices.length; i++) {
            if (length <= choices[i].length
                    && CharOperation.prefixEquals(keyword, choices[i], false /* ignore case */)) {
                if (ignorePackageKeyword && CharOperation.equals(choices[i], Keywords.PACKAGE)) {
                    continue;
                }
                int relevance = 1;
                this.requestor.accept(new KeywordCompletionItem(choices[i], relevance, completionPosition - startPosition, endPosition - completionPosition));
            }
        }
    }

    private void findVariablesAndMethods(char[] token, Scope scope, InvocationSite invocationSite) {
        if (token == null) {
            return;
        }
        boolean staticsOnly = false;
        int tokenLength = token.length;

        Scope scopeVar = scope;
        loop:
        while (true) {
            switch (scopeVar.kind) {
                case Scope.METHOD_SCOPE:
                    MethodScope methodScope = (MethodScope) scopeVar;
                    staticsOnly |= methodScope.isStatic | methodScope.isConstructorCall;
                case Scope.BLOCK_SCOPE:
                    BlockScope blockScope = (BlockScope) scopeVar;
                    next:
                    for (int i = 0, length = blockScope.locals.length; i < length; i++) {
                        LocalVariableBinding local = blockScope.locals[i];
                        if (local == null) {
                            break next;
                        }
                        if (tokenLength > local.name.length) {
                            continue next;
                        }
                        if (!CharOperation.prefixEquals(token, local.name, false)) {
                            continue next;
                        }
                        if (local.isSecret()) {
                            continue next;
                        }
                        if (local.declaration.initialization != null) {
                            continue next;
                        }

                        int relevance = 2;
                        String typeName;

                        if (local.type != null) {
                            typeName = getTypeDisplayName(local.type);
                        } else {
                            typeName = local.declaration.type.toString();
                        }
                        this.requestor.accept(new LocalVariableCompletionItem(local.name, typeName, relevance, completionPosition - startPosition, endPosition - completionPosition));
                    }
                    break;

                case Scope.COMPILATION_UNIT_SCOPE:
                    break loop;
            }
            scopeVar = scopeVar.parent;
        }

        staticsOnly = false;
        scopeVar = scope;
        loop:
        while (true) {
            switch (scopeVar.kind) {
                case Scope.METHOD_SCOPE:
                    MethodScope methodScope = (MethodScope) scopeVar;
                    staticsOnly |= methodScope.isStatic | methodScope.isConstructorCall;
                    break;
                case Scope.CLASS_SCOPE:
                    ClassScope classScope = (ClassScope) scopeVar;
                    SourceTypeBinding enclosingType = classScope.referenceContext.binding;
//                    findFields(token, enclosingType, classScope, staticsOnly, null, invocationSite);
//                    findProperties(token, enclosingType, classScope, staticsOnly, null, invocationSite);
//                    findMethods(token, null, null, enclosingType, classScope, staticsOnly, null, invocationSite);
                    findMembers(token, enclosingType, classScope, invocationSite, null, staticsOnly, true, true, true, false);
                    findMembers(token, enclosingType, scope, invocationSite, null, staticsOnly, false, false, false, true);
                    staticsOnly |= enclosingType.isStatic();
                    break;

                case Scope.COMPILATION_UNIT_SCOPE:
                    break loop;
            }
            scopeVar = scopeVar.parent;
        }

    }

    private void findMethods(char[] selector,
            TypeBinding[] typeArgTypes,
            TypeBinding[] argTypes,
            ReferenceBinding receiverType,
            Scope scope,
            boolean staticsOnly,
            Binding binding,
            InvocationSite invocationSite) {
        findMembers(selector, receiverType, scope, invocationSite, binding, staticsOnly, false, false, true, false);
    }

    private void findFields(char[] fieldName,
            ReferenceBinding receiverType,
            Scope scope,
            boolean staticsOnly,
            Binding binding,
            InvocationSite invocationSite) {
        findMembers(fieldName, receiverType, scope, invocationSite, binding, staticsOnly, true, false, false, false);
    }

    private void findProperties(char[] fieldName,
            ReferenceBinding receiverType,
            Scope scope,
            boolean staticsOnly,
            Binding binding,
            InvocationSite invocationSite) {
        findMembers(fieldName, receiverType, scope, invocationSite, binding, staticsOnly, false, true, false, false);
    }
}
