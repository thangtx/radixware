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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AdsTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ConditionalExpression;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.radixware.kernel.common.compiler.core.problems.AdsProblemReporter;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.src.clazz.sql.AdsSqlClassFieldsBindingProcessor;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;


public class FieldPropertyGetterGenerator extends PropertyGetterGenerator<AdsFieldPropertyDef> {
    
    public FieldPropertyGetterGenerator(Definition referenceContext, CompilationResult result, ReferenceContext context) {
        super(referenceContext, result, context);
    }
    
    @Override
    protected Statement[] createGetterBody(AdsFieldPropertyDef property, AdsPropertyDeclaration propDecl, AdsProblemReporter reporter) {
        EValType typeId = property.getValue().getType().getTypeId();
        if (typeId == null) {
            return null;
        }
        List<Statement> stmts = new LinkedList<>();
        if (property.getNature() == EPropNature.FIELD_REF) {
            MessageSend getFieldEntity = new MessageSend();
            getFieldEntity.selector = "getFieldEntity".toCharArray();
            getFieldEntity.receiver = ThisReference.implicitThis();
            getFieldEntity.arguments = new Expression[]{createIdAccessExpression(property)};
            stmts.add(new ReturnStatement(createCastExpression(getFieldEntity, propDecl.type), 0, 0));
        } else {
            switch (typeId) {
                case INT:
                case CHAR:
                case STR:
                    AdsType type = property.getValue().getType().resolve(contextDefinition).get();
                    if (type instanceof AdsEnumType) {
                        
                        LocalDeclaration x = new LocalDeclaration("x".toCharArray(), 0, 0);
                        x.type = new AdsTypeReference(contextDefinition, AdsTypeDeclaration.Factory.newInstance(typeId));
                        x.initialization = createSimplePropRead(property, typeId);
                        stmts.add(x);
                        
                        MessageSend getForValueCall = new MessageSend();
                        getForValueCall.selector = "getForValue".toCharArray();
                        getForValueCall.arguments = new Expression[]{new SingleNameReference("x".toCharArray(), 0)};
                        getForValueCall.receiver = new TaggedSingleNameReference(contextDefinition, ((AdsEnumType) type).getSource(), false);
                        
                        ConditionalExpression cond = new ConditionalExpression(new EqualExpression(new SingleNameReference("x".toCharArray(), 0), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL),
                                new NullLiteral(0, 0), getForValueCall);
                        
                        stmts.add(new ReturnStatement(cond, 0, 0));
                    } else {
                        Statement stmt = createDefaultStdGetBodyForCursor(property, typeId);
                        if (stmt != null) {
                            stmts.add(stmt);
                        }
                    }
                    break;
                default:
                    Statement stmt = createDefaultStdGetBodyForCursor(property, typeId);
                    if (stmt != null) {
                        stmts.add(stmt);
                    }
            }
        }
        return stmts.toArray(new Statement[stmts.size()]);
    }
    
    private String getJDBCAccessSuffix(EValType typeId) {
        switch (typeId) {
            case INT:
                return "Long";
            
            case NUM:
                return "BigDecimal";
            
            case STR:
                return "String";
            
            case DATE_TIME:
                return "Timestamp";
            case BLOB:
                return "Blob";
            
            case CLOB:
                return "Clob";
            
            case BOOL:
                return "Boolean";
            case CHAR:
                return "Char";
            default:
                return null;
        }
    }
    
    private FieldReference getIndexAccessorField(AdsFieldPropertyDef def) {
        FieldReference idxAccessor = new FieldReference(AdsSqlClassFieldsBindingProcessor.getFieldIndexVarName(def.getId()).toCharArray(), 0);
        idxAccessor.receiver = ThisReference.implicitThis();
        return idxAccessor;
    }
    
    private Expression createSimplePropRead(AdsFieldPropertyDef def, EValType typeId) {
        
        String suffix = getJDBCAccessSuffix(typeId);
        if (suffix == null) {
            return null;
        }
        char[] name = ("get" + suffix).toCharArray();
        MessageSend ms = new MessageSend();
        ms.selector = name;
        FieldReference idxAccessor = getIndexAccessorField(def);
        idxAccessor.receiver = ThisReference.implicitThis();
        ms.arguments = new Expression[]{
            idxAccessor
        };
        ms.receiver = ThisReference.implicitThis();
        return ms;
    }
    
    private Statement createDefaultStdGetBodyForCursor(AdsFieldPropertyDef def, EValType typeId) {
        return new ReturnStatement(createSimplePropRead(def, typeId), 0, 0);
    }
    
    @Override
    public void createBackstoreFields(AdsPropertyDef property, AdsPropertyDeclaration propDecl, List<FieldDeclaration> fields) {
        super.createBackstoreFields(property, propDecl, fields);
        if (property.getNature() == EPropNature.FIELD_REF) {
            return;
        }
        char[] bsName = AdsSqlClassFieldsBindingProcessor.getFieldIndexVarName(property.getId()).toCharArray();
        FieldDeclaration bs = new FieldDeclaration(bsName, 0, 0);
        bs.type = new SingleTypeReference("int".toCharArray(), 0);
        bs.modifiers = ClassFileConstants.AccPrivate;
        fields.add(bs);
    }
}
