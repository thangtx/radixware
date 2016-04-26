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

import java.util.List;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.XBEANSXMLINPUTSTREAM_TYPE_NAME;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.XMLSTREAMEXCEPTION_TYPE_NAME;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createQualifiedType;
import static org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator.createSuppressWarningsAnnotation;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReenterableSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.radixware.kernel.common.compiler.core.ast.JMLSingleNameReference;


public class XBeansFactoryGenerator extends BaseGenerator {

    public XBeansFactoryGenerator(CompilationResult compilationResult, ReferenceContext referenceContext) {
        super(compilationResult, referenceContext);
    }
    private static final char[] VAR_NAME_XIS = "xis".toCharArray();
    private static final char[] VAR_NAME_IS = "is".toCharArray();
    private static final char[] VAR_NAME_NODE = "node".toCharArray();
    private static final char[] VAR_NAME_FILE = "file".toCharArray();
    private static final char[] VAR_NAME_OPTIONS = "options".toCharArray();

    private MethodDeclaration createNewInstance(TypeReference ownerType, boolean withArg, boolean isAbstract) {
        final MethodDeclaration method = createMethod("newInstance", false, ownerType, isAbstract ? ClassFileConstants.AccDeprecated | ClassFileConstants.AccStatic | ClassFileConstants.AccPublic : ClassFileConstants.AccStatic | ClassFileConstants.AccPublic);
        if (withArg) {
            method.arguments = new Argument[]{
                new Argument(VAR_NAME_OPTIONS, 0, new QualifiedTypeReference(XMLOPTIONS_TYPE_NAME, new long[BaseGenerator.XMLOPTIONS_TYPE_NAME.length]), 0)
            };
        }
        final MessageSend newInstance = new MessageSend();
        newInstance.selector = "newInstance".toCharArray();
        newInstance.receiver = createGetContextTypeLoader();


        newInstance.arguments = new Expression[]{
            new JMLSingleNameReference("type".toCharArray(), 0, 0),
            withArg ? new JMLSingleNameReference(VAR_NAME_OPTIONS, 0, 0) : new NullLiteral(0, 0)
        };

        method.statements = new Statement[]{new ReturnStatement(new CastExpression(newInstance, ownerType), 0, 0)};
        return method;
    }

    private Expression createGetContextTypeLoader() {
        final MessageSend getContextTypeLoader = new MessageSend();
        getContextTypeLoader.selector = "getContextTypeLoader".toCharArray();
        getContextTypeLoader.receiver = new QualifiedNameReference(XMLBEANS_TYPE_NAME, new long[XMLBEANS_TYPE_NAME.length], 0, 0);

        return getContextTypeLoader;
    }

    private MethodDeclaration createParse(TypeReference ownerType, Argument argument, boolean withOptions, TypeReference extException) {
        final MethodDeclaration parseMethod = new MethodDeclaration(compilationResult);
        parseMethod.annotations = new Annotation[]{createSuppressWarningsAnnotation("deprecation")};
        parseMethod.returnType = ownerType;
        parseMethod.selector = "parse".toCharArray();
        parseMethod.arguments = withOptions ? new Argument[]{
            argument,
            new Argument(VAR_NAME_OPTIONS, 0, new QualifiedTypeReference(XMLOPTIONS_TYPE_NAME, new long[XMLOPTIONS_TYPE_NAME.length]), 0)
        } : new Argument[]{
            argument
        };
        parseMethod.modifiers = ClassFileConstants.AccStatic | ClassFileConstants.AccPublic;

        final MessageSend innerParse = new MessageSend();
        innerParse.receiver = createGetContextTypeLoader();
        innerParse.selector = "parse".toCharArray();


        innerParse.arguments = new Expression[]{
            new ReenterableSingleNameReference(argument.name),
            new ReenterableSingleNameReference("type".toCharArray()),
            withOptions ? new JMLSingleNameReference(VAR_NAME_OPTIONS, 0, 0) : new NullLiteral(0, 0)
        };
        parseMethod.statements = new Statement[]{
            new ReturnStatement(new CastExpression(innerParse, ownerType), 0, 0)
        };
        parseMethod.thrownExceptions = extException == null ? new TypeReference[]{
            createQualifiedType(XMLEXCEPTION_TYPE_NAME)
        } : new TypeReference[]{
            createQualifiedType(XMLEXCEPTION_TYPE_NAME),
            extException
        };
        return parseMethod;
    }
    
    private MethodDeclaration createNewValidatingXMLInputStream(Argument argument, boolean withOptions) {
        final MethodDeclaration parseMethod = new MethodDeclaration(compilationResult);
        parseMethod.annotations = new Annotation[]{createSuppressWarningsAnnotation("deprecation")};
        parseMethod.returnType = createQualifiedType(XBEANSXMLINPUTSTREAM_TYPE_NAME);
        parseMethod.selector = "newValidatingXMLInputStream".toCharArray();
        parseMethod.arguments = withOptions ? new Argument[]{
            argument,
            new Argument(VAR_NAME_OPTIONS, 0, new QualifiedTypeReference(XMLOPTIONS_TYPE_NAME, new long[XMLOPTIONS_TYPE_NAME.length]), 0)
        } : new Argument[]{
            argument
        };
        parseMethod.modifiers = ClassFileConstants.AccStatic | ClassFileConstants.AccPublic;

        final MessageSend innerParse = new MessageSend();
        
        innerParse.receiver = createGetContextTypeLoader();
        innerParse.selector = "newValidatingXMLInputStream".toCharArray();


        innerParse.arguments = new Expression[]{
            new ReenterableSingleNameReference(argument.name),
            new ReenterableSingleNameReference("type".toCharArray()),
            withOptions ? new JMLSingleNameReference(VAR_NAME_OPTIONS, 0, 0) : new NullLiteral(0, 0)
        };        
        parseMethod.statements = new Statement[]{
            new ReturnStatement(innerParse, 0, 0)
        };
        parseMethod.thrownExceptions = new TypeReference[]{
            createQualifiedType(XMLEXCEPTION_TYPE_NAME),
            createQualifiedType(XMLSTREAMEXCEPTION_TYPE_NAME)
        };
        return parseMethod;
    }

    public void createFactoryMethods(TypeReference ownerType, boolean fullFactory, boolean isSimpleType, boolean isAbstract, List<AbstractMethodDeclaration> result) {

        if (isSimpleType) {
            final MethodDeclaration method = new MethodDeclaration(compilationResult);
            method.selector = "newValue".toCharArray();
            method.modifiers = ClassFileConstants.AccStatic | ClassFileConstants.AccPublic;
            method.arguments = new Argument[]{
                new Argument("obj".toCharArray(), 0, new QualifiedTypeReference(BaseGenerator.JAVALANGOBJECT_TYPENAME, new long[BaseGenerator.JAVALANGOBJECT_TYPENAME.length]), 0)
            };
            final MessageSend getNewValue = new MessageSend();
            getNewValue.selector = "newValue".toCharArray();
            getNewValue.receiver = new SingleNameReference("type".toCharArray(), 0);
            getNewValue.arguments = new Expression[]{
                new JMLSingleNameReference("obj".toCharArray(), 0, 0)
            };
            method.statements = new Statement[]{new ReturnStatement(new CastExpression(getNewValue, ownerType), 0, 0)};
            method.returnType = ownerType;

            result.add(method);
        }

        result.add(createNewInstance(ownerType, false, isAbstract));
        result.add(createNewInstance(ownerType, true, isAbstract));

        if (fullFactory) {
            result.add(createParse(ownerType, new Argument("xmlAsString".toCharArray(), 0, new SingleTypeReference("String".toCharArray(), 0), 0), false, null));
            result.add(createParse(ownerType, new Argument("xmlAsString".toCharArray(), 0, new SingleTypeReference("String".toCharArray(), 0), 0), true, null));

            result.add(createParse(ownerType, new Argument(VAR_NAME_FILE, 0, createQualifiedType(JAVAIOFILE_TYPE_NAME), 0), false, createQualifiedType(IOEXCEPTION_TYPE_NAME)));
            result.add(createParse(ownerType, new Argument(VAR_NAME_FILE, 0, createQualifiedType(JAVAIOFILE_TYPE_NAME), 0), true, createQualifiedType(IOEXCEPTION_TYPE_NAME)));


            result.add(createParse(ownerType, new Argument("u".toCharArray(), 0, createQualifiedType(JAVANETURL_TYPE_NAME), 0), false, createQualifiedType(IOEXCEPTION_TYPE_NAME)));
            result.add(createParse(ownerType, new Argument("u".toCharArray(), 0, createQualifiedType(JAVANETURL_TYPE_NAME), 0), true, createQualifiedType(IOEXCEPTION_TYPE_NAME)));


            result.add(createParse(ownerType, new Argument(VAR_NAME_IS, 0, createQualifiedType(JAVAIOINPUTSTREAM_TYPE_NAME), 0), false, createQualifiedType(IOEXCEPTION_TYPE_NAME)));
            result.add(createParse(ownerType, new Argument(VAR_NAME_IS, 0, createQualifiedType(JAVAIOINPUTSTREAM_TYPE_NAME), 0), true, createQualifiedType(IOEXCEPTION_TYPE_NAME)));

            result.add(createParse(ownerType, new Argument("r".toCharArray(), 0, createQualifiedType(JAVAIOREADER_TYPE_NAME), 0), false, createQualifiedType(IOEXCEPTION_TYPE_NAME)));
            result.add(createParse(ownerType, new Argument("r".toCharArray(), 0, createQualifiedType(JAVAIOREADER_TYPE_NAME), 0), true, createQualifiedType(IOEXCEPTION_TYPE_NAME)));

            result.add(createParse(ownerType, new Argument("sr".toCharArray(), 0, createQualifiedType(JAVAXXMLSTREAMXMLSTREAMREADER_TYPE_NAME), 0), false, createQualifiedType(IOEXCEPTION_TYPE_NAME)));
            result.add(createParse(ownerType, new Argument("sr".toCharArray(), 0, createQualifiedType(JAVAXXMLSTREAMXMLSTREAMREADER_TYPE_NAME), 0), true, createQualifiedType(IOEXCEPTION_TYPE_NAME)));

            result.add(createParse(ownerType, new Argument(VAR_NAME_NODE, 0, createQualifiedType(ORGW3CDOMNODE_TYPE_NAME), 0), false, createQualifiedType(IOEXCEPTION_TYPE_NAME)));
            result.add(createParse(ownerType, new Argument(VAR_NAME_NODE, 0, createQualifiedType(ORGW3CDOMNODE_TYPE_NAME), 0), true, createQualifiedType(IOEXCEPTION_TYPE_NAME)));

            MethodDeclaration annotated = createParse(ownerType, new Argument(VAR_NAME_XIS, 0, createQualifiedType(XBEANSXMLINPUTSTREAM_TYPE_NAME), 0), false, createQualifiedType(XMLSTREAMEXCEPTION_TYPE_NAME));

            result.add(annotated);
            annotated = createParse(ownerType, new Argument(VAR_NAME_XIS, 0, createQualifiedType(XBEANSXMLINPUTSTREAM_TYPE_NAME), 0), true, createQualifiedType(XMLSTREAMEXCEPTION_TYPE_NAME));

            result.add(annotated);
            annotated = createNewValidatingXMLInputStream(new Argument(VAR_NAME_XIS, 0, createQualifiedType(XBEANSXMLINPUTSTREAM_TYPE_NAME), 0), false);

            result.add(annotated);
            annotated = createNewValidatingXMLInputStream(new Argument(VAR_NAME_XIS, 0, createQualifiedType(XBEANSXMLINPUTSTREAM_TYPE_NAME), 0), true);

            result.add(annotated);

            final ConstructorDeclaration init = new ConstructorDeclaration(compilationResult);
            init.modifiers = ClassFileConstants.AccPrivate;
            init.constructorCall = new ExplicitConstructorCall(ExplicitConstructorCall.ImplicitSuper);
            init.selector = FACTORY;
            result.add(init);
        }
    }
}
