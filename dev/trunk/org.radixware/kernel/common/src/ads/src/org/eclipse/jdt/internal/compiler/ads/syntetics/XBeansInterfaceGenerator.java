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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.ArrayQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ArrayTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ExplicitConstructorCall;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedSingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.lookup.AdsCompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.AdsPropertyDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.AdsXmlTypeDeclaration;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.radixware.kernel.common.build.xbeans.XBeansPropAcceptor;
import org.radixware.kernel.common.build.xbeans.XBeansTypeAcceptor;
import static org.radixware.kernel.common.build.xbeans.XbeansSchemaCodePrinter.javaStringEscape;
import org.radixware.kernel.common.compiler.core.ast.JMLArgument;
import org.radixware.kernel.common.compiler.core.ast.JMLCastExpression;
import org.radixware.kernel.common.compiler.core.ast.JMLExplicitConstructorCall;
import org.radixware.kernel.common.compiler.core.ast.JMLFieldDeclaration;
import org.radixware.kernel.common.compiler.core.ast.JMLForStatement;
import org.radixware.kernel.common.compiler.core.ast.JMLLocalDeclaration;
import org.radixware.kernel.common.compiler.core.ast.JMLReturnStatement;
import org.radixware.kernel.common.compiler.core.ast.JMLSingleNameReference;
import org.radixware.kernel.common.compiler.core.ast.JMLSingleTypeReference;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansIfaceProp;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansInterface;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.TypeDeclaration;


public class XBeansInterfaceGenerator {

    private final CompilationResult compilationResult;

    public XBeansInterfaceGenerator(CompilationResult compilationResult) {
        this.compilationResult = compilationResult;
    }

    public AbstractMethodDeclaration[] createXmlProperties(List<XBeansIfaceProp> props) {
        final Map<Id, IXmlDefinition> xmlMap = new HashMap<>();
        List<AbstractMethodDeclaration> addutionalMethods = new ArrayList<>(props.size() * 3);
        for (XBeansIfaceProp prop : props) {
            final PropAcceptor acceptor = new PropAcceptor(prop, addutionalMethods);
            try {
                acceptor.process();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        return addutionalMethods.toArray(new AbstractMethodDeclaration[addutionalMethods.size()]);
    }

    private static TypeReference computePropertyType(String propType) {
        int dim = 0;
        int indexOfArr = propType.indexOf("[]");
        if (indexOfArr > 0) {
            int start = indexOfArr;
            while (indexOfArr > 0) {
                dim++;
                indexOfArr = propType.indexOf("[]", indexOfArr + 1);
            }
            propType = propType.substring(0, start);
        }
        List<String> names = new LinkedList<>();
        int start = 0;
        int generic = 0;
        for (int i = 0; i < propType.length(); i++) {
            char c = propType.charAt(i);
            switch (c) {
                case '.':
                    if (generic == 0) {
                        names.add(propType.substring(start, i));
                        start = i + 1;
                    }
                    break;
                case '<':
                    generic++;
                    break;
                case '>':
                    generic--;
                    break;
            }
        }
        if (start < propType.length()) {
            names.add(propType.substring(start));
        }
        String[] components = names.toArray(new String[names.size()]);


        TypeReference[][] typeArguments = new TypeReference[components.length][];
        boolean hasArgs = false;
        for (int i = 0; i < components.length; i++) {
            String componentString = components[i];
            int index = componentString.indexOf("<");
            if (index > 0) {
                hasArgs = true;
                int end = componentString.indexOf(">", index + 1);
                String parameterType = componentString.substring(index + 1, end);
                String[] parameterNames = parameterType.split("\\.");
                typeArguments[i] = new TypeReference[]{createType(parameterNames, null, 0)};
                componentString = componentString.substring(0, index);
            }
            components[i] = componentString;
        }
        return createType(components, hasArgs ? typeArguments : null, dim);
    }

    private static TypeReference createType(String[] components, TypeReference[][] typeArguments, int dim) {
        char[][] componentsChar = new char[components.length][];
        for (int i = 0; i < components.length; i++) {
            componentsChar[i] = components[i].toCharArray();
        }
        if (typeArguments != null) {
            if (componentsChar.length > 1) {
                return new ParameterizedQualifiedTypeReference(componentsChar, typeArguments, dim, new long[componentsChar.length]);
            } else {
                return new ParameterizedSingleTypeReference(componentsChar[0], typeArguments[0], dim, 0);
            }
        } else {
            if (componentsChar.length > 1) {
                if (dim > 0) {
                    return new ArrayQualifiedTypeReference(componentsChar, dim, new long[componentsChar.length]);
                } else {
                    return new QualifiedTypeReference(componentsChar, new long[componentsChar.length]);
                }
            } else {
                if (dim > 0) {
                    return new ArrayTypeReference(componentsChar[0], dim, 0);
                } else {
                    return new SingleTypeReference(componentsChar[0], 0);
                }
            }
        }
    }
    private static final char[] INDEX_ARG_NAME = "i".toCharArray();
    private static final char[] VAL_ARG_NAME = "value".toCharArray();

    private class PropAcceptor implements XBeansPropAcceptor {

        private XBeansIfaceProp property;
        private TypeReference propertyType;
        private TypeReference propertyXmlType;
        private boolean isDeprecated;
        private boolean isAttr;
        private String propertyName;
        private String arrayName;
        private List<AbstractMethodDeclaration> methods;
        private int modifiers;

        public PropAcceptor(XBeansIfaceProp property, List<AbstractMethodDeclaration> methods) {
            this.property = property;

            this.propertyType = computePropertyType(property.getType());
            this.propertyXmlType = computePropertyType(property.getXType());
            this.isDeprecated = property.isDeprecated();
            this.isAttr = property.isAttr();
            this.propertyName = property.getName();
            this.arrayName = propertyName + "Array";
            this.methods = methods;
            this.modifiers = ClassFileConstants.AccPublic;
            if (isDeprecated) {
                this.modifiers |= ClassFileConstants.AccDeprecated;
            }
        }

        private Argument[] createIndexArgs() {
            return new Argument[]{
                new JMLArgument(TypeReference.baseTypeReference(TypeIds.T_int, 0), INDEX_ARG_NAME)
            };
        }

        private Argument[] createPropTypeArgs() {
            return new Argument[]{
                new JMLArgument(getPropertyType(), VAL_ARG_NAME)
            };
        }

        private Argument[] createPropArrTypeArgs() {
            return new Argument[]{
                new JMLArgument(createArrType(getPropertyType()), VAL_ARG_NAME)
            };
        }

        private Argument[] createPropListTypeArgs() {
            return new Argument[]{
                new JMLArgument(createListOfType(getPropertyType()), VAL_ARG_NAME)
            };
        }

        private Argument[] createPropXmlTypeArgs() {
            return new Argument[]{
                new JMLArgument(getPropertyXmlType(), VAL_ARG_NAME)
            };
        }

        private TypeReference getPropertyType() {
            return propertyType;
        }

        private TypeReference getPropertyArrayType() {
            return createArrType(propertyType);
        }

        private TypeReference getPropertyXmlType() {
            return propertyXmlType;
        }

        private TypeReference getPropertyXmlArrayType() {
            return createArrType(propertyXmlType);
        }

        private TypeReference getPropertyXmlListType() {
            return createListOfType(propertyXmlType);
        }

        private TypeReference createArrType(TypeReference reference) {
            return reference.copyDims(1);
        }

        private TypeReference createListOfType(TypeReference reference) {
            return new ParameterizedQualifiedTypeReference(BaseGenerator.JAVAUTILLIST_TYPE_NAME, new TypeReference[][]{
                null,
                null,
                new TypeReference[]{reference}
            }, 0, new long[3]);
        }

        @Override
        public void acceptSingletonPropGetter(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("get" + propertyName, getPropertyType(), modifiers, compilationResult));
        }

        @Override
        public void acceptSingletonPropXmlGetter(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("xget" + propertyName, getPropertyXmlType(), modifiers, compilationResult));
        }

        @Override
        public void acceptSingletonPropNullCheck(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("isNil" + propertyName, TypeReference.baseTypeReference(TypeIds.T_boolean, 0), modifiers, compilationResult));
        }

        @Override
        public void acceptOptionalPropExistanceCheck(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("isSet" + propertyName, TypeReference.baseTypeReference(TypeIds.T_boolean, 0), modifiers, compilationResult));
        }

        @Override
        public void acceptSeveralPropListGetter(String wrappedType) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("get" + propertyName + "List", createListOfType(computePropertyType(wrappedType)), modifiers, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayGetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("get" + arrayName, getPropertyArrayType(), modifiers | ClassFileConstants.AccDeprecated, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayElementGetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("get" + arrayName, getPropertyType(), modifiers, createIndexArgs(), compilationResult));
        }

        @Override
        public void acceptSeveralPropListXmlGetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("xget" + propertyName + "List", getPropertyXmlListType(), modifiers, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayXmlGetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("xget" + arrayName, getPropertyXmlArrayType(), modifiers | ClassFileConstants.AccDeprecated, compilationResult));

        }

        @Override
        public void acceptSeveralPropArrayElementXmlGetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("xget" + arrayName, getPropertyXmlType(), modifiers, createIndexArgs(), compilationResult));
        }

        @Override
        public void acceptSeveralPropNullCheck() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("isNil" + arrayName, TypeReference.baseTypeReference(TypeIds.T_boolean, 0), modifiers, createIndexArgs(), compilationResult));
        }

        @Override
        public void acceptSeveralPropSizeAccess() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("sizeOf" + arrayName, TypeReference.baseTypeReference(TypeIds.T_int, 0), modifiers, compilationResult));
        }

        @Override
        public void acceptSingletonPropSetter(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("set" + propertyName, modifiers, createPropTypeArgs(), compilationResult));
        }

        @Override
        public void acceptSingletonPropXmlSetter(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("xset" + propertyName, modifiers, createPropXmlTypeArgs(), compilationResult));
        }

        @Override
        public void acceptSingletonXmlCreation() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("addNew" + propertyName, getPropertyXmlType(), modifiers, compilationResult));
            methods.add(BaseGenerator.createMethodDeclaration("ensure" + propertyName, getPropertyXmlType(), modifiers, compilationResult));
        }

        @Override
        public void acceptSingletonPropSetNull(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("setNil" + propertyName, modifiers, compilationResult));
        }

        @Override
        public void acceptOptionalPropUnset(boolean several) throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("unset" + propertyName, modifiers, compilationResult));
        }

        @Override
        public void acceptSeveralPropArraySetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("set" + arrayName, modifiers, createPropArrTypeArgs(), compilationResult));
        }

        @Override
        public void acceptSeveralPropListAssignment() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("assign" + propertyName + "List", modifiers, createPropListTypeArgs(), compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayElementSetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("set" + arrayName, modifiers, new Argument[]{
                new JMLArgument(TypeReference.baseTypeReference(TypeIds.T_int, 0), INDEX_ARG_NAME),
                new JMLArgument(getPropertyType(), VAL_ARG_NAME)
            }, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayXmlSetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("xset" + arrayName, modifiers, new Argument[]{
                new JMLArgument(getPropertyXmlArrayType(), VAL_ARG_NAME)
            }, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayElementXmlSetter() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("xset" + arrayName, modifiers, new Argument[]{
                new JMLArgument(TypeReference.baseTypeReference(TypeIds.T_int, 0), INDEX_ARG_NAME),
                new JMLArgument(getPropertyXmlType(), VAL_ARG_NAME)
            }, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayElementSetNull() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("setNil" + arrayName, modifiers, createIndexArgs(), compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayElementInsertion() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("insert" + propertyName, modifiers, new Argument[]{
                new JMLArgument(TypeReference.baseTypeReference(TypeIds.T_int, 0), INDEX_ARG_NAME),
                new JMLArgument(getPropertyType(), VAL_ARG_NAME)
            }, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayElementAddition() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("add" + propertyName, modifiers, new Argument[]{
                new JMLArgument(getPropertyType(), VAL_ARG_NAME)
            }, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayNewElementInsertion() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("insertNew" + propertyName, getPropertyXmlType(), modifiers, createIndexArgs(), compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayNewElementAddition() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("addNew" + propertyName, getPropertyXmlType(), modifiers, compilationResult));
        }

        @Override
        public void acceptSeveralPropArrayElementRemoving() throws IOException {
            methods.add(BaseGenerator.createMethodDeclaration("remove" + propertyName, getPropertyXmlType(), modifiers, createIndexArgs(), compilationResult));
        }

        @Override
        public void beginProperty(QName qName, String propertyName, String arrayName, String type, String xtype, boolean isAttr, boolean isDeprecated) {
        }

        @Override
        public void endProperty() {
        }

        public void process() throws IOException {
            boolean several = false;
            if (property.hasOptionalExistanceCheck()) {
                acceptOptionalPropExistanceCheck(several);
            }
            if (property.hasOptionalUnset()) {
                acceptOptionalPropUnset(several);
            }
            if (property.hasSeveralPropArrayElementAddition()) {
                acceptSeveralPropArrayElementAddition();
            }
            if (property.hasSeveralPropArrayElementGetter()) {
                acceptSeveralPropArrayElementGetter();
            }
            if (property.hasSeveralPropArrayElementInsertion()) {
                acceptSeveralPropArrayElementInsertion();
            }
            if (property.hasSeveralPropArrayElementRemoving()) {
                acceptSeveralPropArrayElementRemoving();
            }
            if (property.hasSeveralPropArrayElementSetNull()) {
                acceptSeveralPropArrayElementSetNull();
            }
            if (property.hasSeveralPropArrayElementSetter()) {
                acceptSeveralPropArrayElementSetter();
            }
            if (property.hasSeveralPropArrayElementXmlGetter()) {
                acceptSeveralPropArrayElementXmlGetter();
            }
            if (property.hasSeveralPropArrayElementXmlSetter()) {
                acceptSeveralPropArrayElementXmlSetter();
            }
            if (property.hasSeveralPropArrayGetter()) {
                acceptSeveralPropArrayGetter();
            }
            if (property.hasSeveralPropArrayNewElementAddition()) {
                acceptSeveralPropArrayNewElementAddition();
            }
            if (property.hasSeveralPropArrayNewElementInsertion()) {
                acceptSeveralPropArrayNewElementInsertion();
            }
            if (property.hasSeveralPropArraySetter()) {
                acceptSeveralPropArraySetter();
            }
            if (property.hasSeveralPropArrayXmlGetter()) {
                acceptSeveralPropArrayXmlGetter();
            }
            if (property.hasSeveralPropArrayXmlSetter()) {
                acceptSeveralPropArrayXmlSetter();
            }
            if (property.hasSeveralPropListAssignment()) {
                acceptSeveralPropListAssignment();
            }
            if (property.hasSeveralPropListGetter()) {
                acceptSeveralPropListGetter(property.getSeveralPropListGetterWrappedType());
            }
            if (property.hasSeveralPropListXmlGetter()) {
                acceptSeveralPropListXmlGetter();
            }
            if (property.hasSeveralPropNullCheck()) {
                acceptSeveralPropNullCheck();
            }
            if (property.hasSeveralPropSizeAccess()) {
                acceptSeveralPropSizeAccess();
            }
            if (property.hasSingletonCreator()) {
                acceptSingletonXmlCreation();
            }
            if (property.hasSingletonGetter()) {
                acceptSingletonPropGetter(several);
            }
            if (property.hasSingletonGetterXml()) {
                acceptSingletonPropXmlGetter(several);
            }
            if (property.hasSingletonNullCheck()) {
                acceptSingletonPropNullCheck(several);
            }
            if (property.hasSingletonSetter()) {
                acceptSingletonPropSetter(several);
            }
            if (property.hasSingletonSetterXml()) {
                acceptSingletonPropXmlSetter(several);
            }
            if (property.hasSingletonUnset()) {
                acceptSingletonPropSetNull(several);
            }
        }
    }

    public AdsXmlTypeDeclaration createEnumTableClass(AdsXmlTypeDeclaration parent, XBeansInterface.SimpleContent sc) {
        final List<XBeansInterface.SimpleContent.EnumerationElement> enumElementList = sc.getElementList();
        if (enumElementList != null && !enumElementList.isEmpty()) {
            AdsXmlTypeDeclaration enumClass = new AdsXmlTypeDeclaration(parent, (AdsCompilationUnitScope) parent.scope.compilationUnitScope(), (AbstractXmlDefinition) parent.getDefinition(), null, compilationResult, null);
            enumClass.name = "Enum".toCharArray();
            enumClass.superclass = new QualifiedTypeReference(new char[][]{
                BaseGenerator.ORG,
                BaseGenerator.APACHE,
                BaseGenerator.XMLBEANS,
                "StringEnumAbstractBase".toCharArray()
            }, new long[4]);
            AbstractMethodDeclaration forString = BaseGenerator.createMethodDeclaration("forString", ClassFileConstants.AccPublic | ClassFileConstants.AccStatic, new Argument[]{
                new JMLArgument(BaseGenerator.createQualifiedType(TypeReference.JAVA_LANG_STRING), "s")
            }, compilationResult);
            forString.statements = new Statement[]{
                new JMLReturnStatement(new JMLCastExpression(
                BaseGenerator.createMessageSend(new JMLSingleNameReference("table"), "forString", new JMLSingleNameReference("s")), new SingleTypeReference("Enum".toCharArray(), 0)))
            };
            AbstractMethodDeclaration forInt = BaseGenerator.createMethodDeclaration("forInt", ClassFileConstants.AccPublic | ClassFileConstants.AccStatic, new Argument[]{
                new JMLArgument(TypeReference.baseTypeReference(TypeIds.T_int, 0), "i")
            }, compilationResult);
            forInt.statements = new Statement[]{
                new JMLReturnStatement(new JMLCastExpression(
                BaseGenerator.createMessageSend(new JMLSingleNameReference("table"), "forInt", new JMLSingleNameReference("i")), new SingleTypeReference("Enum".toCharArray(), 0)))
            };
            ConstructorDeclaration init = new ConstructorDeclaration(compilationResult);
            init.modifiers = ClassFileConstants.AccPrivate;
            init.arguments = new Argument[]{
                new JMLArgument(BaseGenerator.createQualifiedType(TypeReference.JAVA_LANG_STRING), "s"),
                new JMLArgument(TypeReference.baseTypeReference(TypeIds.T_int, 0), "i"),};
            init.constructorCall = new JMLExplicitConstructorCall(ExplicitConstructorCall.Super);
            init.constructorCall.arguments = new Expression[]{
                new JMLSingleNameReference("s"),
                new JMLSingleNameReference("i"),};

            for (XBeansInterface.SimpleContent.EnumerationElement element : enumElementList) {
                int intValue = element.getIntValue();
                JMLFieldDeclaration field = new JMLFieldDeclaration();
                field.name = ("INT_" + element.getName()).toCharArray();
                field.modifiers = ClassFileConstants.AccStatic | ClassFileConstants.AccFinal;
                field.initialization = IntLiteral.buildIntLiteral(String.valueOf(intValue).toCharArray(), 0, 0);
            }
            //TODO: table field

            return enumClass;

//             emit("static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase");
//            emit("{");
//            indent();
//            emit("/**");
//            emit(" * Returns the enum value for a string, or null if none.");
//            emit(" */");
//            emit("public static Enum forString(java.lang.String s)");
//            emit("    { return (Enum)table.forString(s); }");
//            emit("/**");
//            emit(" * Returns the enum value corresponding to an int, or null if none.");
//            emit(" */");
//            emit("public static Enum forInt(int i)");
//            emit("    { return (Enum)table.forInt(i); }");
//            emit("");
//            emit("private Enum(java.lang.String s, int i)");
//            emit("    { super(s, i); }");
//            emit("");
//            for (int i = 0; i < entries.length; i++) {
//                String constName = "INT_" + entries[i].getEnumName();
//                int intValue = entries[i].getIntValue();
//                emit("static final int " + constName + " = " + intValue + ";");
//            }
//            emit("");
//            emit("public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =");
//            emit("    new org.apache.xmlbeans.StringEnumAbstractBase.Table");
//            emit("(");
//            indent();
//            emit("new Enum[]");
//            emit("{");
//            indent();
//            for (int i = 0; i < entries.length; i++) {
//                String enumValue = entries[i].getString();
//                String constName = "INT_" + entries[i].getEnumName();
//                emit("new Enum(\"" + javaStringEscape(enumValue) + "\", " + constName + "),");
//            }
//            outdent();
//            emit("}");
//            outdent();
//            emit(");");
//            emit("private static final long serialVersionUID = 1L;");
//            emit("private java.lang.Object readResolve() { return forInt(intValue()); } ");
//            outdent();
//            emit("}");
//             
//             
        } else {
            return null;
        }
    }
}
