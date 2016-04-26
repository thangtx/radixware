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

package org.eclipse.jdt.internal.compiler.ast;

import java.util.List;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.IAdsScope;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.ICompilable;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;


public class AdsTypeReference extends TypeReference {

    private int dimensions = 0;
    private AdsTypeDeclaration token;
    private Definition referencedDefinition;
    private Definition referenceContext;
    private boolean isReferenceToMetaType;

    public AdsTypeReference(Definition referenceContext, AdsTypeDeclaration decl) {
        this.token = decl;
        this.referenceContext = referenceContext;
    }

    public AdsTypeReference(Definition referenceContext, AdsTypeDeclaration decl, int start, int end) {
        this.token = decl;
        this.referenceContext = referenceContext;
        this.sourceStart = start;
        this.sourceEnd = end;
    }

    public AdsTypeReference(Definition referenceContext, Definition referencedDefinition) {
        this(referenceContext, referencedDefinition, false);
    }

    public AdsTypeReference(Definition referenceContext, Definition referencedDefinition, int start, int end) {
        this(referenceContext, referencedDefinition, false);
        this.sourceStart = start;
        this.sourceEnd = end;
    }

    public AdsTypeReference(Definition referenceContext, Definition referencedDefinition, boolean meta) {
        this.referenceContext = referenceContext;
        this.referencedDefinition = referencedDefinition;
        this.isReferenceToMetaType = meta;
    }

    public AdsTypeReference(Definition referenceContext, AdsTypeDeclaration decl, int dimension) {
        this.token = decl;
        this.dimensions = dimension;
        this.referenceContext = referenceContext;
    }

    @Override
    public TypeReference copyDims(int dim) {
        return new AdsTypeReference(referenceContext, token, dim);
    }

    @Override
    public char[] getLastToken() {
        return null;
    }

    @Override
    public int dimensions() {
        return dimensions;
    }

    @Override
    protected TypeBinding getTypeBinding(Scope scope) {
        return null;//this function is never used
    }

    @Override
    public char[][] getTypeName() {
        return null;
    }

    @Override
    public void traverse(ASTVisitor visitor, BlockScope scope) {
    }

    @Override
    public void traverse(ASTVisitor visitor, ClassScope scope) {
    }

    @Override
    public StringBuffer printExpression(int indent, StringBuffer output) {
        if (token != null) {
            output.append("`").append(token.getQualifiedName(referenceContext.getDefinition())).append("`");
        } else {
            output.append("`").append(referencedDefinition.getQualifiedName(referenceContext.getDefinition())).append("`");
        }
        return output;
    }

    protected IAdsScope findAdsScope(Scope scope) {
        Scope s = scope;
        while (s != null) {
            if (s instanceof IAdsScope) {
                return (IAdsScope) s;
            }
            s = s.parent;
        }
        return null;
    }

    @Override
    protected TypeBinding internalResolveType(Scope scope) {
        this.constant = Constant.NotAConstant;
        if (this.resolvedType != null) { // is a shared type reference which was already resolved
            if (this.resolvedType.isValidBinding()) {
                return this.resolvedType;
            } else {
                switch (this.resolvedType.problemId()) {
                    case ProblemReasons.NotFound:
                    case ProblemReasons.NotVisible:
                    case ProblemReasons.InheritedNameHidesEnclosingName:
                        TypeBinding type = this.resolvedType.closestMatch();
                        if (type == null) {
                            return null;
                        }
                        return scope.environment().convertToRawType(type, false /*do not force conversion of enclosing types*/);
                    default:
                        return null;
                }
            }
        }
        IAdsScope ads = findAdsScope(scope);

        if (ads != null) {
            if (referencedDefinition != null) {
                return this.resolvedType = ads.getType(referencedDefinition, isReferenceToMetaType);
            } else {
                switch (token.getTypeId()) {
                    case JAVA_TYPE:
                        return this.resolvedType = scope.getType(token.getExtStr().toCharArray());
                    case JAVA_CLASS:
                        return resolveJavaClass(ads, scope, null);
                }
                AdsType type = token.resolve(referenceContext.getDefinition()).get();
                if (type instanceof AdsClassType) {
                    final AdsClassDef clazz = ((AdsClassType) type).getSource();
                    if (clazz.getTransparence() != null && clazz.getTransparence().isTransparent() && !clazz.getTransparence().isExtendable()) {
                        return resolveJavaClass(ads, scope, clazz.getTransparence().getPublishedName());
                    }
                } else if (type instanceof AdsEnumType) {
                    final AdsEnumDef enumeration = ((AdsEnumType) type).getSource();
                    if (enumeration.isPlatformEnumPublisher() && !enumeration.isExtendable()) {
                        return resolveJavaClass(ads, scope, enumeration.getPublishedPlatformEnumName());
                    }

                }
                this.resolvedType = ads.getType(referenceContext, token);
            }
        }
        if (this.resolvedType == null) {
            return new ProblemReferenceBinding(JAVA_LANG, ReferenceBinding.LUB_GENERIC, T_boolean);
        }
        return this.resolvedType;
    }

    private TypeBinding resolveJavaClass(IAdsScope adsScope, Scope scope, String name) {
        String[] tokensStr = (name == null ? token.getExtStr() : name).split("\\.");
        char[][] tokens = new char[tokensStr.length][];
        long[] positions = new long[tokens.length];
        int start = this.sourceStart;
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokensStr[i].toCharArray();
            int end = start + tokens[i].length;
            positions[i] = (((long) start) << 32) | ((long) end);
            start = end + 2;
        }

        TypeReference dummy;
        if (token.getGenericArguments() != null && !token.getGenericArguments().isEmpty()) {//parameterized type
            List<AdsTypeDeclaration.TypeArgument> args = token.getGenericArguments().getArgumentList();
            TypeReference[][] typeArguments = new TypeReference[tokens.length][];
            typeArguments[typeArguments.length - 1] = new TypeReference[args.size()];
            for (int i = 0; i < args.size(); i++) {
                typeArguments[typeArguments.length - 1][i] = new AdsTypeReference(referenceContext, args.get(i).getType(), args.get(i).getType().getArrayDimensionCount());
            }
            dummy = new ParameterizedQualifiedTypeReference(tokens, typeArguments, dimensions, positions);
        } else {
            dummy = new QualifiedTypeReference(tokens, positions);
        }
        if (scope instanceof ClassScope) {
            return this.resolvedType = dummy.resolveType((ClassScope) scope);
        } else {
            return this.resolvedType = dummy.resolveType((BlockScope) scope);
        }
    }

    @Override
    public TypeBinding resolveSuperType(ClassScope scope) {
        return super.resolveSuperType(scope); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TypeBinding resolveTypeArgument(BlockScope blockScope, ReferenceBinding genericType, int rank) {
        return super.resolveTypeArgument(blockScope, genericType, rank); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public TypeBinding resolveTypeArgument(ClassScope classScope, ReferenceBinding genericType, int rank) {
        return super.resolveTypeArgument(classScope, genericType, rank); //To change body of generated methods, choose Tools | Templates.
    }
}
