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
package org.radixware.kernel.common.build.xbeans;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.InterfaceExtension;
import org.apache.xmlbeans.InterfaceExtension.MethodSignature;
import org.apache.xmlbeans.PrePostExtension;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaCodePrinter;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.SystemProperties;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.apache.xmlbeans.impl.schema.SchemaTypeImpl;
import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;


import org.w3c.dom.Node;

/**
 * Prints the java code for a single schema type
 */
public class XbeansSchemaCodePrinter implements SchemaCodePrinter {

    Writer _writer;
    int _indent;
    boolean _useJava15;
    boolean noRadixEnums = false;
    static final String LINE_SEPARATOR
            = SystemProperties.getProperty("line.separator") == null
                    ? "\n"
                    : SystemProperties.getProperty("line.separator");
    static final String MAX_SPACES = "                                        ";
    static final int INDENT_INCREMENT = 4;
    public static final String INDEX_CLASSNAME = "TypeSystemHolder";
    static final String RADIX_TYPES_URI = "http://schemas.radixware.org/types.xsd";
    static final String THIS_PACKAGE = "org.radixware.kernel.common.build.xbeans";

    abstract static class RadixType {

        abstract public String getJavaClassName();

        public String getFullJavaClassName() {
            return getJavaClassName();
        }

        abstract public String jsetMethod();

        abstract public String jgetMethod();

        public boolean isArray() {
            return false;
        }

        public String jUnboxed(String varName, int javaType) {
            switch (javaType) {
                case SchemaProperty.JAVA_BOOLEAN:
                    return varName + ".booleanValue()";
                case SchemaProperty.JAVA_LONG:
                    return varName + ".longValue()";
                case SchemaProperty.JAVA_CALENDAR:
                    return varName + "_tmp_radix";
            }
            return varName;
        }
    }

    static class RadixBool extends RadixType {

        @Override
        public String getJavaClassName() {
            return "java.lang.Boolean";
        }

        @Override
        public String jsetMethod() {
            return "target.setBooleanValue";
        }

        @Override
        public String jgetMethod() {
            return "org.radixware.kernel.common.build.xbeans.XBeansValueGetter.getBooleanValue(target)";            
        }
    }

    static class RadixChar extends RadixType {

        @Override
        public String getJavaClassName() {
            return "java.lang.Character";
        }

        @Override
        public String jsetMethod() {
            return "target.setStringValue";
        }

        @Override
        public String jgetMethod() {
            return "org.radixware.kernel.common.build.xbeans.XBeansValueGetter.getCharValue(target)";            
        }

        @Override
        public String jUnboxed(String varName, @SuppressWarnings("unused") int javaType) {
            return "String.valueOf(" + varName + ")";
        }
    }

    static class RadixNum extends RadixType {

        @Override
        public String getJavaClassName() {
            return "java.math.BigDecimal";
        }

        @Override
        public String jsetMethod() {
            return "target.setBigDecimalValue";
        }

        @Override
        public String jgetMethod() {
            return "target.getBigDecimalValue()";
        }
    }

    static class RadixInt extends RadixType {

        @Override
        public String getJavaClassName() {
            return "java.lang.Long";
        }

        @Override
        public String jsetMethod() {
            return "target.setLongValue";
        }

        @Override
        public String jgetMethod() {            
            return "org.radixware.kernel.common.build.xbeans.XBeansValueGetter.getLongValue(target)";            
        }
    }

    static class RadixStr extends RadixType {

        @Override
        public String getJavaClassName() {
            return "java.lang.String";
        }

        @Override
        public String jsetMethod() {
            return "target.setStringValue";
        }

        @Override
        public String jgetMethod() {
            return "target.getStringValue()";
        }
    }

    static class RadixSafeStr extends RadixType {

        @Override
        public String getJavaClassName() {
            return "java.lang.String";
        }

        @Override
        public String jsetMethod() {
            return "target.setStringValue(org.radixware.kernel.common.build.xbeans.XmlEscapeStr.getSafeXmlString(";
        }

        @Override
        public String jgetMethod() {
            return "org.radixware.kernel.common.build.xbeans.XmlEscapeStr.parseSafeXmlString(target.getStringValue())";
        }
    }

    static class RadixId extends RadixType {

        @Override
        public String getJavaClassName() {
            return "org.radixware.kernel.common.types.Id";
        }

        @Override
        public String jsetMethod() {
            return "target.setStringValue";
        }

        @Override
        public String jgetMethod() {
            return "(target == null || target.isNil() ? null : org.radixware.kernel.common.types.Id.Factory.loadFrom(target.getStringValue()))";
        }

        @Override
        public String jUnboxed(String varName, int javaType) {
            return varName + ".toString()";
        }
    }

    static class RadixDateTime extends RadixType {

        @Override
        public String getJavaClassName() {
            return "java.sql.Timestamp";
        }

        @Override
        public String jsetMethod() {
            //XMLCALENDARreturn "target.setCalendarValue";
            return "target.setGDateValue";
        }

        @Override
        public String jgetMethod() {
            return "target.getGDateValue()";
        }
    }

    static class RadixBinBase64 extends RadixType {

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public String getJavaClassName() {
            return "byte";
        }

        @Override
        public String getFullJavaClassName() {
            return "byte[]";
        }

        @Override
        public String jsetMethod() {
            return "target.setByteArrayValue";
        }

        @Override
        public String jgetMethod() {
            return "target.getByteArrayValue()";
        }
    }

    static class RadixBinHex extends RadixType {

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public String getJavaClassName() {
            return "byte";
        }

        @Override
        public String getFullJavaClassName() {
            return "byte[]";
        }

        @Override
        public String jsetMethod() {
            return "target.setByteArrayValue";
        }

        @Override
        public String jgetMethod() {
            return "target.getByteArrayValue()";
        }
    }

    static class RadixIntEnum extends RadixInt {

        String radixClass;

        public RadixIntEnum(String radixClass) {
            this.radixClass = radixClass;
        }

        @Override
        public String getJavaClassName() {
            return radixClass;
        }

        @Override
        public String jgetMethod() {
            return "(target == null || target.isNil()) ? null : " + radixClass + ".getForValue(target.getLongValue())";
        }

        @Override
        public String jUnboxed(String varName, @SuppressWarnings("unused") int javaType) {
            return varName + " == null ? null : " + varName + ".getValue()";
        }
    }

    static class RadixStrEnum extends RadixStr {

        String radixClass;

        public RadixStrEnum(String radixClass) {
            this.radixClass = radixClass;
        }

        @Override
        public String getJavaClassName() {
            return radixClass;
        }

        @Override
        public String jgetMethod() {
            return "target == null ? null : " + radixClass + ".getForValue(target.getStringValue())";
        }

        @Override
        public String jUnboxed(String varName, @SuppressWarnings("unused") int javaType) {
            return varName + " == null ? null : " + varName + ".getValue()";
        }
    }

    static class RadixSafeStrEnum extends RadixSafeStr {

        String radixClass;

        public RadixSafeStrEnum(String radixClass) {
            this.radixClass = radixClass;
        }

        @Override
        public String getJavaClassName() {
            return radixClass;
        }

        @Override
        public String jgetMethod() {
            return "target == null ? null : " + radixClass + ".getForValue(org.radixware.kernel.common.build.xbeans.XmlEscapeStr.parseSafeXmlString(target.getStringValue()))";
        }

        @Override
        public String jUnboxed(String varName, @SuppressWarnings("unused") int javaType) {
            return varName + " == null ? null : " + varName + ".getValue()";
        }
    }

    static class RadixCharEnum extends RadixChar {

        String radixClass;

        public RadixCharEnum(String radixClass) {
            this.radixClass = radixClass;
        }

        @Override
        public String getJavaClassName() {
            return radixClass;
        }

        @Override
        public String jgetMethod() {
            return "target == null ? null : " + radixClass + ".getForValue(target.getStringValue().charAt(0))";
        }

        @Override
        public String jUnboxed(String varName, @SuppressWarnings("unused") int javaType) {
            return varName + " == null ? null : " + varName + ".getValue().toString()";
        }
    }
    static final private Map<String, Class<? extends RadixType>> radixToJava = new HashMap<>();

    static {
        radixToJava.put("Bool", RadixBool.class);
        radixToJava.put("Char", RadixChar.class);
        radixToJava.put("Num", RadixNum.class);
        radixToJava.put("Int", RadixInt.class);
        radixToJava.put("Str", RadixStr.class);
        radixToJava.put("SafeStr", RadixSafeStr.class);
        radixToJava.put("Id", RadixId.class);
        radixToJava.put("DateTime", RadixDateTime.class);
        radixToJava.put("BinBase64", RadixBinBase64.class);
        radixToJava.put("BinHex", RadixBinHex.class);
    }

    public interface IJavaAnnotationProvider {

        public String getTypeAnnotations(SchemaType sType);

        public String getPropertyAnnotations(SchemaProperty sProp);
    }
    private IJavaAnnotationProvider annotationProvider;

    public XbeansSchemaCodePrinter() {
        this(null);
    }

    public XbeansSchemaCodePrinter(IJavaAnnotationProvider ap) {
        _indent = 0;
        annotationProvider = ap;

//        String genversion = null;
//
//        if (opt != null && XmlOptions.hasOption(opt, XmlOptions.GENERATE_JAVA_VERSION))
//            genversion = (String)opt.get(XmlOptions.GENERATE_JAVA_VERSION);
//
//        if (genversion == null)
//            genversion = XmlOptions.GENERATE_JAVA_14;
        _useJava15 = true;//XmlOptions.GENERATE_JAVA_15.equals(genversion);
    }

    public void setup(XmlOptions options) {
        if (options.hasOption("No Radix Enums")) {
            noRadixEnums = true;
        }
    }

    void indent() {
        _indent += INDENT_INCREMENT;
    }

    void outdent() {
        _indent -= INDENT_INCREMENT;
    }

    String encodeString(String s) {
        StringBuilder sb = new StringBuilder();

        sb.append('"');

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (ch == '"') {
                sb.append('\\');
                sb.append('\"');
            } else if (ch == '\\') {
                sb.append('\\');
                sb.append('\\');
            } else if (ch == '\r') {
                sb.append('\\');
                sb.append('r');
            } else if (ch == '\n') {
                sb.append('\\');
                sb.append('n');
            } else if (ch == '\t') {
                sb.append('\\');
                sb.append('t');
            } else {
                sb.append(ch);
            }
        }

        sb.append('"');

        return sb.toString();
    }

    void emit(String s) throws IOException {
        int indent = _indent;

        if (indent > MAX_SPACES.length() / 2) {
            indent = MAX_SPACES.length() / 4 + indent / 2;
        }
        if (indent > MAX_SPACES.length()) {
            indent = MAX_SPACES.length();
        }
        _writer.write(MAX_SPACES.substring(0, indent));
        try {
            _writer.write(s);
        } catch (CharacterCodingException cce) {
            _writer.write(makeSafe(s));
        }
        _writer.write(LINE_SEPARATOR);

        // System.out.print(MAX_SPACES.substring(0, indent));
        // System.out.println(s);
    }

    @SuppressWarnings("cast")
    private static String makeSafe(String s) {
        Charset charset = Charset.forName(System.getProperty("file.encoding"));
        if (charset == null) {
            throw new IllegalStateException("Default character set is null!");
        }
        CharsetEncoder cEncoder = charset.newEncoder();
        StringBuilder result = new StringBuilder();
        int i;
        for (i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!cEncoder.canEncode(c)) {
                break;
            }
        }
        for (; i < s.length(); i++) {
            char c = s.charAt(i);
            if (cEncoder.canEncode(c)) {
                result.append(c);
            } else {
                String hexValue = Integer.toHexString((int) c);
                switch (hexValue.length()) {
                    case 1:
                        result.append("\\u000").append(hexValue);
                        break;
                    case 2:
                        result.append("\\u00").append(hexValue);
                        break;
                    case 3:
                        result.append("\\u0").append(hexValue);
                        break;
                    case 4:
                        result.append("\\u").append(hexValue);
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }
        }
        return result.toString();
    }

    protected XBeansJavaAcceptor createJavaAcceptor() {
        return new XBeansJavaAcceptor() {
            @Override
            public XBeansTypeAcceptor createTypeAcceptor() {
                return new DefaultTypeAcceptor();
            }

            @Override
            public void acceptTopComment(SchemaType sType) throws IOException {
                printTopComment(sType);
            }

            @Override
            public void acceptPackageDeclaration(String packageName, boolean isIface) throws IOException {
                emit("package " + packageName + ";");
                if (isIface) {
                    emit("");
                }
            }

            @Override
            public XBeansTypeImplAcceptor createTypeImplAcceptor() {
                return new DefaultTypeImplAcceptor();
            }
        };
    }

    @Override
    public void printType(Writer w, SchemaType sType) throws IOException {
        try {
            _writer = w;
            XBeansJavaAcceptor acceptor = createJavaAcceptor();
            acceptor.acceptTopComment(sType);
            printPackage(sType, true, acceptor);
            printInnerType(sType, sType.getTypeSystem(), acceptor.createTypeAcceptor());
            _writer.flush();
        } catch (RuntimeException e) {
            java.util.logging.Logger.getLogger(XbeansSchemaCodePrinter.class.getName()).log(Level.FINE, "XBeans code print failure", e);
        }
    }

    @Override
    public void printTypeImpl(Writer w, SchemaType sType) throws IOException {
        try {
            _writer = w;
            XBeansJavaAcceptor acceptor = createJavaAcceptor();
            acceptor.acceptTopComment(sType);
            printPackage(sType, false, acceptor);
            printInnerTypeImpl(sType, sType.getTypeSystem(), false, acceptor.createTypeImplAcceptor());
            _writer.flush();
        } catch (RuntimeException e) {
            java.util.logging.Logger.getLogger(XbeansSchemaCodePrinter.class.getName()).log(Level.FINE, "XBeans code print failure", e);
        }
    }

    /**
     * Since not all schema types have java types, this skips over any that
     * don't and gives you the nearest java base type.
     */
    String findJavaType(SchemaType sType) {
        while (sType.getFullJavaName() == null) {
            sType = sType.getBaseType();
        }
        return sType.getFullJavaName();
    }

    static String prettyQName(QName qname) {
        String result = qname.getLocalPart();
        if (qname.getNamespaceURI() != null) {
            result += "(@" + qname.getNamespaceURI() + ")";
        }
        return result;
    }

    void printInnerTypeJavaDoc(SchemaType sType) throws IOException {
        QName name = sType.getName();
        if (name == null) {
            if (sType.isDocumentType()) {
                name = sType.getDocumentElementName();
            } else if (sType.isAttributeType()) {
                name = sType.getAttributeTypeAttributeName();
            } else if (sType.getContainerField() != null) {
                name = sType.getContainerField().getName();
            }
        }

        emit("/**");
        if (sType.isDocumentType()) {
            emit(" * A document containing one " + prettyQName(name) + " element.");
        } else if (sType.isAttributeType()) {
            emit(" * A document containing one " + prettyQName(name) + " attribute.");
        } else if (name != null) {
            emit(" * An XML " + prettyQName(name) + ".");
        } else {
            emit(" * An anonymous inner XML type.");
        }
        emit(" *");
        switch (sType.getSimpleVariety()) {
            case SchemaType.NOT_SIMPLE:
                emit(" * This is a complex type.");
                break;
            case SchemaType.ATOMIC:
                emit(" * This is an atomic type that is a restriction of " + getFullJavaName(sType) + ".");
                break;
            case SchemaType.LIST:
                emit(" * This is a list type whose items are " + sType.getListItemType().getFullJavaName() + ".");
                break;
            case SchemaType.UNION:
                emit(" * This is a union type. Instances are of one of the following types:");
                SchemaType[] members = sType.getUnionConstituentTypes();
                for (int i = 0; i < members.length; i++) {
                    emit(" *     " + members[i].getFullJavaName());
                }
                break;
        }
        emit(" */");
    }

    private String getFullJavaName(SchemaType sType) {

        SchemaTypeImpl sTypeI = (SchemaTypeImpl) sType;
        String ret = sTypeI.getFullJavaName();

        while (sTypeI.isRedefinition()) {
            ret = sTypeI.getFullJavaName();
            sTypeI = (SchemaTypeImpl) sTypeI.getBaseType();
        }
        return ret;
    }

    public static String indexClassForSystem(SchemaTypeSystem system) {
        String name = system.getName();
        return name + "." + INDEX_CLASSNAME;
    }

    static String shortIndexClassForSystem(@SuppressWarnings("unused") SchemaTypeSystem system) {
        return INDEX_CLASSNAME;
    }

    void printStaticTypeDeclaration(SchemaType sType, SchemaTypeSystem system) throws IOException {
        String interfaceShortName = sType.getShortJavaName();
        emit("public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)");
        indent();
        emit("org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader("
                + interfaceShortName + ".class.getClassLoader(), \"" + system.getName() + "\")"
                + ".resolveHandle(\""
                + ((SchemaTypeSystemImpl) system).handleForType(sType) + "\");");
        outdent();
    }

    /**
     * @deprecated
     */
    @SuppressWarnings("unused")
    @Deprecated
    @Override
    public void printLoader(
            Writer writer,
            SchemaTypeSystem system) throws IOException {
        // deprecated
    }

    private class DefaultTypeAcceptor implements XBeansTypeAcceptor {

        private SchemaType sType;
        private SchemaType baseEnumType;
        private String baseEnumClass;
        private SchemaTypeSystem system;

        @Override
        public void beginType(SchemaType sType, SchemaTypeSystem system) throws IOException {
            this.sType = sType;
            this.system = system;
            emit("");
        }

        @Override
        public XBeansTypeAcceptor createInnerType() {
            return new DefaultTypeAcceptor();
        }

        @Override
        public XBeansPropAcceptor createPropAcceptor() {
            return new DefaultAcceptor();
        }

        @Override
        public void acceptTypeJavaDoc() throws IOException {
            printInnerTypeJavaDoc(sType);
        }

        @Override
        public void acceptInterfaceStart(String shortName, String baseInterface, String[] extensionInterfaces, boolean isDeprecated, boolean isDocType) throws IOException {
            if (isDeprecated) {
                emit("@Deprecated");
            }
            StringBuilder ext = new StringBuilder();
            for (int i = 0; i < extensionInterfaces.length; i++) {
                ext.append(", ").append(extensionInterfaces[i]);
            }
            emit("public interface " + shortName + " extends " + baseInterface + ext.toString());
            emit("{");
            indent();
        }

        @Override
        public void acceptStaticDeclaration() throws IOException {
            printStaticTypeDeclaration(sType, system);
        }

        @Override
        public void acceptStringEnumeration(SchemaType baseEnumType, String baseEnumClass) throws IOException {
            this.baseEnumType = baseEnumType;
            this.baseEnumClass = baseEnumClass;
            if (baseEnumType == sType) {
                emit("");
                emit("org.apache.xmlbeans.StringEnumAbstractBase enumValue();");
                emit("void set(org.apache.xmlbeans.StringEnumAbstractBase e);");
            }

            emit("");
        }

        @Override
        public void acceptStringEnumerationItem(String constName, String enumValue) throws IOException {
            if (baseEnumType != sType) {
                emit("static final " + baseEnumClass + ".Enum " + constName + " = " + baseEnumClass + "." + constName + ";");
            } else {
                emit("static final Enum " + constName + " = Enum.forString(\"" + javaStringEscape(enumValue) + "\");");
            }
        }

        @Override
        public void acceptStringEnumerationItemAsInt(String constName) throws IOException {
            if (baseEnumType != sType) {
                emit("static final int " + constName + " = " + baseEnumClass + "." + constName + ";");
            } else {
                emit("static final int " + constName + " = Enum." + constName + ";");
            }
        }

        @Override
        public void acceptStringEnumerationClassWithTable(SchemaStringEnumEntry[] entries) throws IOException {
            emit("");
            emit("/**");
            emit(" * Enumeration value class for " + baseEnumClass + ".");
            emit(" * These enum values can be used as follows:");
            emit(" * <pre>");
            emit(" * enum.toString(); // returns the string value of the enum");
            emit(" * enum.intValue(); // returns an int value, useful for switches");
            if (entries.length > 0) {
                emit(" * // e.g., case Enum.INT_" + entries[0].getEnumName());
            }
            emit(" * Enum.forString(s); // returns the enum value for a string");
            emit(" * Enum.forInt(i); // returns the enum value for an int");
            emit(" * </pre>");
            emit(" * Enumeration objects are immutable singleton objects that");
            emit(" * can be compared using == object equality. They have no");
            emit(" * public constructor. See the constants defined within this");
            emit(" * class for all the valid values.");
            emit(" */");
            emit("static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase");
            emit("{");
            indent();
            emit("/**");
            emit(" * Returns the enum value for a string, or null if none.");
            emit(" */");
            emit("public static Enum forString(java.lang.String s)");
            emit("    { return (Enum)table.forString(s); }");
            emit("/**");
            emit(" * Returns the enum value corresponding to an int, or null if none.");
            emit(" */");
            emit("public static Enum forInt(int i)");
            emit("    { return (Enum)table.forInt(i); }");
            emit("");
            emit("private Enum(java.lang.String s, int i)");
            emit("    { super(s, i); }");
            emit("");
            for (int i = 0; i < entries.length; i++) {
                String constName = "INT_" + entries[i].getEnumName();
                int intValue = entries[i].getIntValue();
                emit("static final int " + constName + " = " + intValue + ";");
            }
            emit("");
            emit("public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =");
            emit("    new org.apache.xmlbeans.StringEnumAbstractBase.Table");
            emit("(");
            indent();
            emit("new Enum[]");
            emit("{");
            indent();
            for (int i = 0; i < entries.length; i++) {
                String enumValue = entries[i].getString();
                String constName = "INT_" + entries[i].getEnumName();
                emit("new Enum(\"" + javaStringEscape(enumValue) + "\", " + constName + "),");
            }
            outdent();
            emit("}");
            outdent();
            emit(");");
            emit("private static final long serialVersionUID = 1L;");
            emit("private java.lang.Object readResolve() { return forInt(intValue()); } ");
            outdent();
            emit("}");
        }

        @Override
        public void acceptSpecializedDecimalAccessor(int size) throws IOException {
            if (size == SchemaType.SIZE_BIG_INTEGER) {
                emit("java.math.BigInteger getBigIntegerValue();");
                emit("void setBigIntegerValue(java.math.BigInteger bi);");
                emit("/** @deprecated */");
                emit("java.math.BigInteger bigIntegerValue();");
                emit("/** @deprecated */");
                emit("void set(java.math.BigInteger bi);");
            } else if (size == SchemaType.SIZE_LONG) {
                emit("long getLongValue();");
                emit("void setLongValue(long l);");
                emit("/** @deprecated */");
                emit("long longValue();");
                emit("/** @deprecated */");
                emit("void set(long l);");
            } else if (size == SchemaType.SIZE_INT) {
                emit("int getIntValue();");
                emit("void setIntValue(int i);");
                emit("/** @deprecated */");
                emit("int intValue();");
                emit("/** @deprecated */");
                emit("void set(int i);");
            } else if (size == SchemaType.SIZE_SHORT) {
                emit("short getShortValue();");
                emit("void setShortValue(short s);");
                emit("/** @deprecated */");
                emit("short shortValue();");
                emit("/** @deprecated */");
                emit("void set(short s);");
            } else if (size == SchemaType.SIZE_BYTE) {
                emit("byte getByteValue();");
                emit("void setByteValue(byte b);");
                emit("/** @deprecated */");
                emit("byte byteValue();");
                emit("/** @deprecated */");
                emit("void set(byte b);");
            }
        }

        @Override
        public void acceptSpecializedObjectAccessor() throws IOException {
            emit("java.lang.Object getObjectValue();");
            emit("void setObjectValue(java.lang.Object val);");
            emit("/** @deprecated */");
            emit("java.lang.Object objectValue();");
            emit("/** @deprecated */");
            emit("void objectSet(java.lang.Object val);");
            emit("org.apache.xmlbeans.SchemaType instanceType();");
        }

        @Override
        public void acceptSpecializedListAccessor(String wildcard) throws IOException {
            emit("java.util.List" + wildcard + " getListValue();");
            emit("java.util.List" + wildcard + " xgetListValue();");
            emit("void setListValue(java.util.List" + wildcard + " list);");
            emit("/** @deprecated */");
            emit("java.util.List" + wildcard + " listValue();");
            emit("/** @deprecated */");
            emit("java.util.List" + wildcard + " xlistValue();");
            emit("/** @deprecated */");
            emit("void set(java.util.List" + wildcard + " list);");
        }

        @Override
        public void endType() throws IOException {
            endBlock();
        }

        @Override
        public void acceptTypeFactory(String fullName, boolean fullFactory, boolean isSimpleType, boolean isAbstract) throws IOException {
            emit("");
            emit("/**");
            emit(" * A factory class with static methods for creating instances");
            emit(" * of this type.");
            emit(" */");
            emit("");
            // BUGBUG - Can I use the name loader here?  could it be a
            // nested type name?  It is lower case!
          /*  if (annotationProvider != null) {
             String annotations = annotationProvider.getTypeAnnotations(sType);
             if (annotations != null) {
             emit(annotations);
             }
             }*/
            emit("public static final class Factory");
            emit("{");
            indent();

            if (isSimpleType) {
                emit("public static " + fullName + " newValue(java.lang.Object obj) {");
                emit("  return (" + fullName + ") type.newValue( obj ); }");
                emit("");
            }

            // Only need newInstance() for non-abstract types
            if (isAbstract) {
                emit("/** @deprecated No need to be able to create instances of abstract types */");
            }
            emit("public static " + fullName + " newInstance() {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }");
            emit("");

            // Only need newInstance() for non-abstract types
            if (isAbstract) {
                emit("/** @deprecated No need to be able to create instances of abstract types */");
            }
            emit("public static " + fullName + " newInstance(org.apache.xmlbeans.XmlOptions options) {");
            emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }");
            emit("");

            if (fullFactory) {
                emit("/** @param xmlAsString the string value to parse */");
                emit("public static " + fullName + " parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }");
                emit("");

                emit("public static " + fullName + " parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }");
                emit("");

                emit("/** @param file the file from which to load an xml document */");
                emit("public static " + fullName + " parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }");
                emit("");

                emit("public static " + fullName + " parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }");
                emit("");

                emit("public static " + fullName + " parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }");
                emit("");

                emit("public static " + fullName + " parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }");
                emit("");

                emit("public static " + fullName + " parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }");
                emit("");

                emit("public static " + fullName + " parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }");
                emit("");

                emit("public static " + fullName + " parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }");
                emit("");

                emit("public static " + fullName + " parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }");
                emit("");

                emit("public static " + fullName + " parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }");
                emit("");

                emit("public static " + fullName + " parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }");
                emit("");

                emit("public static " + fullName + " parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }");
                emit("");

                emit("public static " + fullName + " parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }");
                emit("");

                emit("/** @deprecated {@link XMLInputStream} */");
                emit("public static " + fullName + " parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }");
                emit("");

                emit("/** @deprecated {@link XMLInputStream} */");
                emit("public static " + fullName + " parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
                emit("  return (" + fullName + ") org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }");
                emit("");

                // Don't have XMLInputStream anymore
                emit("/** @deprecated {@link XMLInputStream} */");
                emit("public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
                emit("  return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }");
                emit("");

                // Don't have XMLInputStream anymore
                emit("/** @deprecated {@link XMLInputStream} */");
                emit("public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {");
                emit("  return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }");
                emit("");
            }

            emit("private Factory() { } // No instance of this class allowed");
            outdent();
            emit("}");
        }
    }

    void printInnerType(SchemaType sType, SchemaTypeSystem system, XBeansTypeAcceptor acceptor) throws IOException {
        if (annotationProvider != null) {
            String annontations = annotationProvider.getTypeAnnotations(sType);
            if (annontations != null) {
                emit(annontations);
            }
        }
        acceptor.beginType(sType, system);
        acceptor.acceptTypeJavaDoc();
        startInterface(sType, acceptor);
        acceptor.acceptStaticDeclaration();

        if (sType.isSimpleType()) {
            if (sType.hasStringEnumValues()) {
                printStringEnumeration(sType, acceptor);
            }
        } else {
            SchemaProperty[] props = getDerivedProperties(sType);

            for (int i = 0; i < props.length; i++) {
                XBeansPropAcceptor propAcceptor = acceptor.createPropAcceptor();
                SchemaProperty prop = props[i];
                String propertyName = prop.getJavaPropertyName();

                propAcceptor.beginProperty(prop.getName(), propertyName, propertyName + "Array", javaTypeForProperty(prop),
                        xmlTypeForProperty(prop), prop.isAttribute(), propIsDeprecated(prop));

                printPropertyGetters(
                        prop.getJavaTypeCode(),
                        javaTypeForProperty(prop),
                        prop.hasNillable() != SchemaProperty.NEVER,
                        prop.extendsJavaOption(),
                        prop.extendsJavaArray(),
                        prop.extendsJavaSingleton(),
                        isRadixType(prop), propAcceptor);

                if (!prop.isReadOnly()) {
                    try {
                        //System.out.println(prop.getName() + " " + prop.extendsJavaOption() + " " + prop.extendsJavaArray() + " " + prop.extendsJavaSingleton() + " " + isRadixPrimitiveType(prop));
                        printPropertySetters(
                                prop.getJavaTypeCode(),
                                prop.hasNillable() != SchemaProperty.NEVER,
                                prop.extendsJavaOption(),
                                prop.extendsJavaArray(),
                                prop.extendsJavaSingleton(),
                                isRadixPrimitiveType(prop), propAcceptor);
                    } catch (RuntimeException e) {
                        java.util.logging.Logger.getLogger(XbeansSchemaCodePrinter.class.getName()).log(Level.FINE, "XBeans code print failure", e);
                    }

                }
                propAcceptor.endProperty();
            }

        }

        printNestedInnerTypes(sType, system, acceptor);

        printFactory(sType, acceptor);

        acceptor.endType();
    }

    private boolean propIsDeprecated(SchemaProperty prop) {
        SchemaType sType = prop.getContainerType();
        boolean isDeprecated = false;
        if (prop.isAttribute()) {
            SchemaLocalAttribute attr = prop.getContainerType().getAttributeModel().getAttribute(prop.getName());
            if (attr != null) {
                isDeprecated = containsDeprecatedAnnotation(attr.getAnnotation());
            }
        } else {
            isDeprecated = propIsDeprecated(prop, sType.getContentModel());
        }
        if (annotationProvider != null) {
            String annotations = annotationProvider.getPropertyAnnotations(prop);
            if (annotations.contains("@Deprecated")) {
                isDeprecated = true;
            }

        }
        return isDeprecated;
    }

    private boolean propIsDeprecated(SchemaProperty prop, SchemaParticle particle) {
        switch (particle.getParticleType()) {
            case SchemaParticle.ALL:
            case SchemaParticle.CHOICE:
            case SchemaParticle.SEQUENCE:
                for (SchemaParticle p : particle.getParticleChildren()) {
                    if (propIsDeprecated(prop, p)) {
                        return true;
                    }
                }
                return false;
            case SchemaParticle.ELEMENT:
                SchemaLocalElement e = (SchemaLocalElement) particle;
                if (e.getName() != null && e.getName().equals(prop.getName())) {
                    return containsDeprecatedAnnotation(e.getAnnotation());
                } else {
                    return false;
                }
        }
        return false;
    }

    private boolean containsDeprecatedAnnotation(SchemaAnnotation a) {
        if (a != null) {
            XmlObject[] appInfos = a.getApplicationInformation();
            if (appInfos != null) {
                for (XmlObject obj : appInfos) {
                    Node node = obj.getDomNode();
                    Node src = node.getAttributes().getNamedItem("source");
                    if (src != null && RADIX_TYPES_URI.equals(src.getNodeValue())) {
                        //  System.out.println("Source found at " + node.getNodeName().toString());
                        for (int i = 0, len = node.getChildNodes().getLength(); i < len; i++) {
                            Node child = node.getChildNodes().item(i);
                            //  System.out.println("Look at " + child.getNodeName().toString());
                            if ("deprecated".equals(child.getLocalName()) && RADIX_TYPES_URI.equals(child.getNamespaceURI())) {
                                //System.out.println("Deprecated property found");
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    void printFactory(SchemaType sType, XBeansTypeAcceptor acceptor) throws IOException {
        // Only need full factories for top-level types
        boolean fullFactory = true;
        if (sType.isAnonymousType() && !sType.isDocumentType() && !sType.isAttributeType()) {
            fullFactory = false;
        }
        String fullName = sType.getFullJavaName().replace('$', '.');

        acceptor.acceptTypeFactory(fullName, fullFactory, sType.isSimpleType(), sType.isAbstract());

    }

    void printNestedInnerTypes(SchemaType sType, SchemaTypeSystem system, XBeansTypeAcceptor acceptor) throws IOException {
        boolean redefinition = sType.getName() != null
                && sType.getName().equals(sType.getBaseType().getName());

        while (sType != null) {
            SchemaType[] anonTypes = sType.getAnonymousTypes();
            for (int i = 0; i < anonTypes.length; i++) {
                if (anonTypes[i].isSkippedAnonymousType()) {
                    printNestedInnerTypes(anonTypes[i], system, acceptor);
                } else {
                    printInnerType(anonTypes[i], system, acceptor.createInnerType());
                }
            }
            // For redefinition other than by extension for complex types, go ahead and print
            // the anonymous types in the base
            if (!redefinition
                    || (sType.getDerivationType() != SchemaType.DT_EXTENSION && !sType.isSimpleType())) {
                break;
            }
            sType = sType.getBaseType();
        }
    }

    void printTopComment(SchemaType sType) throws IOException {
        emit("/*");
        if (sType.getName() != null) {
            emit(" * XML Type:  " + sType.getName().getLocalPart());
            emit(" * Namespace: " + sType.getName().getNamespaceURI());
        } else {
            QName thename = null;

            if (sType.isDocumentType()) {
                thename = sType.getDocumentElementName();
                emit(" * An XML document type.");
            } else if (sType.isAttributeType()) {
                thename = sType.getAttributeTypeAttributeName();
                emit(" * An XML attribute type.");
            } else {
                assert false;
            }
            assert (thename != null);

            emit(" * Localname: " + thename.getLocalPart());
            emit(" * Namespace: " + thename.getNamespaceURI());
        }
        emit(" * Java type: " + sType.getFullJavaName());
        emit(" *");
        emit(" * Automatically generated - do not modify.");
        emit(" */");
    }

    void printPackage(SchemaType sType, boolean intf, XBeansJavaAcceptor acceptor) throws IOException {
        String fqjn;
        if (intf) {
            fqjn = sType.getFullJavaName();
        } else {
            fqjn = sType.getFullJavaImplName();
        }
        int lastdot = fqjn.lastIndexOf('.');
        if (lastdot < 0) {
            return;
        }
        String pkg = fqjn.substring(0, lastdot);
        acceptor.acceptPackageDeclaration(pkg, intf);
    }

    void startInterface(SchemaType sType, XBeansTypeAcceptor acceptor) throws IOException {
        String shortName = sType.getShortJavaName();

        String baseInterface = findJavaType(sType.getBaseType());

        /*
         StringBuilder specializedInterfaces = new StringBuilder();
        
         if (sType.getSimpleVariety() == SchemaType.ATOMIC &&
         sType.getPrimitiveType().getBuiltinTypeCode() == SchemaType.BTC_DECIMAL)
         {
         int bits = sType.getDecimalSize();
         if (bits == SchemaType.SIZE_BIG_INTEGER)
         specializedInterfaces.append(", org.apache.xmlbeans.BigIntegerValue");
         if (bits == SchemaType.SIZE_LONG)
         specializedInterfaces.append(", org.apache.xmlbeans.LongValue");
         if (bits <= SchemaType.SIZE_INT)
         specializedInterfaces.append(", org.apache.xmlbeans.IntValue");
         }
         if (sType.getSimpleVariety() == SchemaType.LIST)
         specializedInterfaces.append(", org.apache.xmlbeans.ListValue");
        
         if (sType.getSimpleVariety() == SchemaType.UNION)
         {
         SchemaType ctype = sType.getUnionCommonBaseType();
         String javaTypeHolder = javaTypeHolderForType(ctype);
         if (javaTypeHolder != null)
         specializedInterfaces.append(", " + javaTypeHolder);
         }
         */
        acceptor.acceptInterfaceStart(shortName, baseInterface, getExtensionInterfaces(sType), containsDeprecatedAnnotation(sType.getAnnotation()), sType.isDocumentType());
        emitSpecializedAccessors(sType, acceptor);
    }

    private static String[] getExtensionInterfaces(SchemaType sType) {
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl == null) {
            return new String[0];
        }
        //StringBuilder sb = new StringBuilder();

        InterfaceExtension[] exts = sImpl.getInterfaceExtensions();
        if (exts != null) {
            List<String> res = new ArrayList<>(exts.length);

            for (int i = 0; i < exts.length; i++) {
                res.add(exts[i].getInterface());
            }
            Collections.sort(res);
            return res.toArray(new String[exts.length]);
        }
        return new String[0];
    }

    private static SchemaTypeImpl getImpl(SchemaType sType) {
        if (sType instanceof SchemaTypeImpl) {
            return (SchemaTypeImpl) sType;
        } else {
            return null;
        }
    }

    private void emitSpecializedAccessors(SchemaType sType, XBeansTypeAcceptor acceptor) throws IOException {
        if (sType.getSimpleVariety() == SchemaType.ATOMIC
                && sType.getPrimitiveType().getBuiltinTypeCode() == SchemaType.BTC_DECIMAL) {
            int bits = sType.getDecimalSize();
            int parentBits = sType.getBaseType().getDecimalSize();
            if (bits != parentBits || sType.getBaseType().getFullJavaName() == null) {
                acceptor.acceptSpecializedDecimalAccessor(bits);
            }
        }

        if (sType.getSimpleVariety() == SchemaType.UNION) {
            acceptor.acceptSpecializedObjectAccessor();
            SchemaType ctype = sType.getUnionCommonBaseType();
            if (ctype != null && ctype.getSimpleVariety() != SchemaType.UNION) {
                emitSpecializedAccessors(ctype, acceptor);
            }
        }

        if (sType.getSimpleVariety() == SchemaType.LIST) {
            String wildcard = "";
            RadixType radixType = getRadixType(sType.getListItemType());
            if (_useJava15 && radixType != null) {
                wildcard = "<" + radixType.getFullJavaClassName() + ">";
            }
            acceptor.acceptSpecializedListAccessor(wildcard);

        }
    }

    void startBlock() throws IOException {
        emit("{");
        indent();
    }

    void endBlock() throws IOException {
        outdent();
        emit("}");
    }

    void printJavaDoc(String sentence) throws IOException {
        emit("");
        emit("/**");
        emit(" * " + sentence);
        emit(" */");
    }

    void printShortJavaDoc(String sentence) throws IOException {
        emit("/** " + sentence + " */");
    }

    public static String javaStringEscape(String str) {
        // forbidden: \n, \r, \", \\.
        test:
        {
            for (int i = 0; i < str.length(); i++) {
                switch (str.charAt(i)) {
                    case '\n':
                    case '\r':
                    case '\"':
                    case '\\':
                        break test;
                }
            }
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(ch);
                    break;

            }
        }
        return sb.toString();
    }

    void printStringEnumeration(SchemaType sType, XBeansTypeAcceptor acceptor) throws IOException {
        SchemaType baseEnumType = sType.getBaseEnumType();
        String baseEnumClass = baseEnumType.getFullJavaName();

        acceptor.acceptStringEnumeration(baseEnumType, baseEnumClass);

        SchemaStringEnumEntry[] entries = sType.getStringEnumEntries();
        HashSet<String> seenValues = new HashSet<>();
        HashSet<String> repeatValues = new HashSet<>();
        for (int i = 0; i < entries.length; i++) {
            String enumValue = entries[i].getString();
            if (seenValues.contains(enumValue)) {
                repeatValues.add(enumValue);
                continue;
            } else {
                seenValues.add(enumValue);
            }
            String constName = entries[i].getEnumName();

            acceptor.acceptStringEnumerationItem(constName, enumValue);
        }
        //emit("");
        for (int i = 0; i < entries.length; i++) {
            if (repeatValues.contains(entries[i].getString())) {
                continue;
            }
            String constName = "INT_" + entries[i].getEnumName();

            acceptor.acceptStringEnumerationItemAsInt(constName);

        }
        if (baseEnumType == sType) {
            acceptor.acceptStringEnumerationClassWithTable(entries);
        }
    }

    String xmlTypeForProperty(SchemaProperty sProp) {
        SchemaType sType = sProp.javaBasedOnType();
        return findJavaType(sType).replace('$', '.');
    }

    static boolean xmlTypeForPropertyIsUnion(SchemaProperty sProp) {
        SchemaType sType = sProp.javaBasedOnType();
        return (sType.isSimpleType() && sType.getSimpleVariety() == SchemaType.UNION);
    }

    static boolean isJavaPrimitive(int javaType) {
        return (javaType < SchemaProperty.JAVA_FIRST_PRIMITIVE ? false : (javaType > SchemaProperty.JAVA_LAST_PRIMITIVE ? false : true));
    }

    /**
     * Returns the wrapped type for a java primitive.
     */
    static String javaWrappedType(int javaType) {
        switch (javaType) {
            case SchemaProperty.JAVA_BOOLEAN:
                return "java.lang.Boolean";
            case SchemaProperty.JAVA_FLOAT:
                return "java.lang.Float";
            case SchemaProperty.JAVA_DOUBLE:
                return "java.lang.Double";
            case SchemaProperty.JAVA_BYTE:
                return "java.lang.Byte";
            case SchemaProperty.JAVA_SHORT:
                return "java.lang.Short";
            case SchemaProperty.JAVA_INT:
                return "java.lang.Integer";
            case SchemaProperty.JAVA_LONG:
                return "java.lang.Long";

            // anything else is not a java primitive
            default:
                assert false;
                throw new IllegalStateException();
        }
    }

    boolean isRadixType(SchemaProperty prop) {
        if (prop.getJavaTypeCode() == SchemaProperty.JAVA_LIST) {
            return isRadixType(prop.getType().getListItemType());
        }
        return isRadixType(prop.getType());
    }

    private boolean isRadixPrimitiveType(SchemaProperty prop) {
//        if (prop.getJavaTypeCode() == SchemaProperty.JAVA_LIST) {
//            return isRadixType(prop.getType().getListItemType());
//        }

        SchemaType type = prop.getType();

        for (; type != null && type != type.getBaseType(); type = type.getBaseType()) {
            if (!type.isSimpleType()) {
                return false;
            }
            if (type.getName() != null) {
                if (type.getName().getNamespaceURI().equals(RADIX_TYPES_URI)) {
                    return !"MapStrStr".equals(type.getName().getLocalPart());
                }
            }
        }

        return false;
    }

    /**
     * Get Radix type for XML type to handle it in special way
     *
     * @param type - a schema type
     * @return SchemaProperty.JAVA(INT|DOUBLE|...) on success, or
     * SchemaProperty.JAVA_OBJECT on failure
     */
    boolean isRadixType(SchemaType type) {
        /*    	switch( prop.getJavaTypeCode() ) {
         case SchemaProperty.JAVA_BOOLEAN:
         case SchemaProperty.JAVA_STRING:
         case SchemaProperty.JAVA_BIG_DECIMAL:
         case SchemaProperty.JAVA_LONG:
         case SchemaProperty.JAVA_CALENDAR:
         */
        for (; type != null && type != type.getBaseType(); type = type.getBaseType()) {
            if (type.getName() != null) {
                if (type.getName().getNamespaceURI().equals(RADIX_TYPES_URI)) {
                    //      System.out.println("Radix: " + type.getName());
                    return true;
                }
            }
        }
//    		break;
//    	}
        return false;
    }

    RadixType getRadixType(SchemaType baseType) {
        if (!baseType.isSimpleType()) {
            return null;
        }
        SchemaType type = baseType;
      
        
        

        for (; type != null && type != type.getBaseType(); type = type.getBaseType()) {
            if (type.getName() != null) {
                if (type.getName().getNamespaceURI().equals(RADIX_TYPES_URI)) {
                    String name = type.getName().getLocalPart();

                    if (radixToJava.containsKey(name)) {
                        try {
                            return radixToJava.get(name).newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            java.util.logging.Logger.getLogger(XbeansSchemaCodePrinter.class.getName()).log(Level.FINE, "XBeans code print failure", e);
                        }
                    } else if (!noRadixEnums) { // this is enum 

                        if (type.isSimpleType() /*|| type.getContentType() == SchemaType.SIMPLE_CONTENT*/) {
                            String radixClass = null;
                            for (SchemaType childType = baseType; childType != type; childType = childType.getBaseType()) {
                                if (childType.getAnnotation() != null) {
                                    XmlObject[] appInfo = childType.getAnnotation().getApplicationInformation();
                                    for (XmlObject o : appInfo) {
                                        XmlCursor cursor = o.newCursor();
                                        cursor.toFirstChild();
                                        Node child = cursor.getObject().getDomNode();
                                        cursor.dispose();

                                        if (child.getLocalName().equals("class")
                                                && child.getNamespaceURI().equals(RADIX_TYPES_URI)) {
                                            Node c = child.getFirstChild();
                                            StringBuilder sb = new StringBuilder();
                                            while (c != null) {
                                                sb.append(c.getNodeValue());
                                                c = c.getNextSibling();
                                            }
                                            radixClass = sb.toString();
                                        }

                                        if (radixClass != null) {
                                            // System.out.println(child.getNodeName()+":
                                            // "+radixClass);
                                            if (name.equals("IntEnum")) {
                                                return new RadixIntEnum(radixClass);
                                            } else if (name.equals("CharEnum")) {
                                                return new RadixCharEnum(radixClass);
                                            } else if (name.equals("StrEnum")) {
                                                return new RadixStrEnum(radixClass);
                                            } else if (name.equals("SafeStrEnum")) {
                                                return new RadixSafeStrEnum(radixClass);
                                            }
                                        }
                                    }
                                }
                            }
                            throw new IllegalArgumentException("Type " + baseType + " does not have appinfo or proper class declaration");
                        }
                    }
                }
            }
        }
        return null;
    }

    String javaTypeForProperty(SchemaProperty sProp) {
        // The type to use is the XML object....
        if (sProp.getJavaTypeCode() == SchemaProperty.XML_OBJECT) {
            SchemaType sType = sProp.javaBasedOnType();
            return findJavaType(sType).replace('$', '.');
        }

        RadixType radixType = getRadixType(sProp.getType());
        if (radixType != null) {
            return radixType.getFullJavaClassName();
//        System.out.println( "DBP type for property '" + sProp.getName() + " = " + sProp.getType() + "', isDBP = " + String.valueOf(isDBP) ); 
        }
        switch (sProp.getJavaTypeCode()) {
            case SchemaProperty.JAVA_BOOLEAN:
                return "boolean";
            case SchemaProperty.JAVA_FLOAT:
                return "float";
            case SchemaProperty.JAVA_DOUBLE:
                return "double";
            case SchemaProperty.JAVA_BYTE:
                return "byte";
            case SchemaProperty.JAVA_SHORT:
                return "short";
            case SchemaProperty.JAVA_INT:
                return "int";
            case SchemaProperty.JAVA_LONG:
                return "long";

            case SchemaProperty.JAVA_BIG_DECIMAL:
                return "java.math.BigDecimal";
            case SchemaProperty.JAVA_BIG_INTEGER:
                return "java.math.BigInteger";
            case SchemaProperty.JAVA_STRING:
                return "java.lang.String";
            case SchemaProperty.JAVA_BYTE_ARRAY:
                return "byte[]";
            case SchemaProperty.JAVA_GDATE:
                return "org.apache.xmlbeans.GDate";
            case SchemaProperty.JAVA_GDURATION:
                return "org.apache.xmlbeans.GDuration";
            case SchemaProperty.JAVA_DATE:
                return "java.util.Date";
            case SchemaProperty.JAVA_QNAME:
                return "javax.xml.namespace.QName";
            case SchemaProperty.JAVA_LIST:
                RadixType radixListItemType = getRadixType(sProp.getType().getListItemType());
                if (_useJava15 && radixListItemType != null) {
                    return "java.util.List<" + radixListItemType.getJavaClassName() + ">";
                }
                return "java.util.List";
            case SchemaProperty.JAVA_CALENDAR:
                return "java.util.Calendar";

            case SchemaProperty.JAVA_ENUM:
                SchemaType sType = sProp.javaBasedOnType();
                if (sType.getSimpleVariety() == SchemaType.UNION) {
                    sType = sType.getUnionCommonBaseType();
                }
                assert sType.getBaseEnumType() != null;
                return findJavaType(sType.getBaseEnumType()).replace('$', '.') + ".Enum";

            case SchemaProperty.JAVA_OBJECT:
                return "java.lang.Object";

            default:
                assert (false);
                throw new IllegalStateException();
        }
    }

    private final class DefaultAcceptor implements XBeansPropAcceptor {

        private String propdesc;
        private String propertyName;
        private boolean isDeprecated;
        private String type, xtype;
        private String safeVarName;
        private String arrayName;

        @Override
        public void beginProperty(QName qName, String propertyName, String arrayName, String type, String xtype, boolean isAttr, boolean isDeprecated) {
            this.propdesc = "\"" + qName.getLocalPart() + "\"" + (isAttr ? " attribute" : " element");
            this.isDeprecated = isDeprecated;
            this.propertyName = propertyName;
            this.type = type;
            this.xtype = xtype;
            safeVarName = NameUtil.nonJavaKeyword(NameUtil.lowerCamelCase(propertyName));
            if (safeVarName.equals("i")) {
                safeVarName = "iValue";
            }
            this.arrayName = arrayName;
        }

        @Override
        public void endProperty() {
        }

        @Override
        public void acceptSingletonPropGetter(boolean several) throws IOException {
            printJavaDoc((several ? "Gets first " : "Gets the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(type + " get" + propertyName + "();");
        }

        @Override
        public void acceptSingletonPropXmlGetter(boolean several) throws IOException {
            printJavaDoc((several ? "Gets (as xml) first " : "Gets (as xml) the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(xtype + " xget" + propertyName + "();");
        }

        @Override
        public void acceptSingletonPropNullCheck(boolean several) throws IOException {
            printJavaDoc((several ? "Tests for nil first " : "Tests for nil ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("boolean isNil" + propertyName + "();");
        }

        @Override
        public void acceptOptionalPropExistanceCheck(boolean several) throws IOException {
            printJavaDoc((several ? "True if has at least one " : "True if has ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("boolean isSet" + propertyName + "();");
        }

        @Override
        public void acceptSeveralPropListGetter(String wrappedType) throws IOException {
            printJavaDoc("Gets a List of " + propdesc + "s");
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("java.util.List<" + wrappedType + "> get" + propertyName + "List();");
        }

        @Override
        public void acceptSeveralPropArrayGetter() throws IOException {
            if (_useJava15) {
                emit("");
                emit("/**");
                emit(" * Gets array of all " + propdesc + "s");
                emit(" * @deprecated");
                emit(" */");
            } else {
                printJavaDoc("Gets array of all " + propdesc + "s");
            }
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(type + "[] get" + arrayName + "();");
        }

        @Override
        public void acceptSeveralPropArrayElementGetter() throws IOException {
            printJavaDoc("Gets ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(type + " get" + arrayName + "(int i);");
        }

        @Override
        public void acceptSeveralPropListXmlGetter() throws IOException {
            printJavaDoc("Gets (as xml) a List of " + propdesc + "s");
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("java.util.List<" + xtype + "> xget" + propertyName + "List();");
        }

        @Override
        public void acceptSeveralPropArrayXmlGetter() throws IOException {
            if (_useJava15) {
                emit("");
                emit("/**");
                emit(" * Gets (as xml) array of all " + propdesc + "s");
                emit(" * @deprecated");
                emit(" */");
            } else {
                printJavaDoc("Gets (as xml) array of all " + propdesc + "s");
            }
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(xtype + "[] xget" + arrayName + "();");
        }

        @Override
        public void acceptSeveralPropArrayElementXmlGetter() throws IOException {
            printJavaDoc("Gets (as xml) ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(xtype + " xget" + arrayName + "(int i);");
        }

        @Override
        public void acceptSeveralPropNullCheck() throws IOException {
            printJavaDoc("Tests for nil ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("boolean isNil" + arrayName + "(int i);");
        }

        @Override
        public void acceptSeveralPropSizeAccess() throws IOException {
            printJavaDoc("Returns number of " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("int sizeOf" + arrayName + "();");
        }

        @Override
        public void acceptSingletonPropSetter(boolean several) throws IOException {
            printJavaDoc((several ? "Sets first " : "Sets the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void set" + propertyName + "(" + type + " " + safeVarName + ");");
        }

        @Override
        public void acceptSingletonPropXmlSetter(boolean several) throws IOException {
            printJavaDoc((several ? "Sets (as xml) first " : "Sets (as xml) the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void xset" + propertyName + "(" + xtype + " " + safeVarName + ");");
        }

        @Override
        public void acceptSingletonXmlCreation() throws IOException {
            printJavaDoc("Appends and returns a new empty " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(xtype + " addNew" + propertyName + "();");

            printJavaDoc("Appends (if not exists) and returns a new empty " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(xtype + " ensure" + propertyName + "();");
        }

        @Override
        public void acceptSingletonPropSetNull(boolean several) throws IOException {
            printJavaDoc((several ? "Nils the first " : "Nils the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void setNil" + propertyName + "();");
        }

        @Override
        public void acceptOptionalPropUnset(boolean several) throws IOException {
            printJavaDoc((several ? "Removes first " : "Unsets the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void unset" + propertyName + "();");
        }

        @Override
        public void acceptSeveralPropArraySetter() throws IOException {
            printJavaDoc("Sets array of all " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void set" + arrayName + "(" + type + "[] " + safeVarName + "Array);");
        }

        @Override
        public void acceptSeveralPropListAssignment() throws IOException {
            printJavaDoc("Sets list of all " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void assign" + propertyName + "List(java.util.List<" + type + "> " + safeVarName + "List);");
        }

        @Override
        public void acceptSeveralPropArrayElementSetter() throws IOException {
            printJavaDoc("Sets ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void set" + arrayName + "(int i, " + type + " " + safeVarName + ");");
        }

        @Override
        public void acceptSeveralPropArrayXmlSetter() throws IOException {
            printJavaDoc("Sets (as xml) array of all " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void xset" + arrayName + "(" + xtype + "[] " + safeVarName + "Array);");

        }

        @Override
        public void acceptSeveralPropArrayElementXmlSetter() throws IOException {
            printJavaDoc("Sets (as xml) ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void xset" + arrayName + "(int i, " + xtype + " " + safeVarName + ");");
        }

        @Override
        public void acceptSeveralPropArrayElementSetNull() throws IOException {
            printJavaDoc("Nils the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void setNil" + arrayName + "(int i);");
        }

        @Override
        public void acceptSeveralPropArrayElementInsertion() throws IOException {
            printJavaDoc("Inserts the value as the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void insert" + propertyName + "(int i, " + type + " " + safeVarName + ");");
        }

        @Override
        public void acceptSeveralPropArrayElementAddition() throws IOException {
            printJavaDoc("Appends the value as the last " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void add" + propertyName + "(" + type + " " + safeVarName + ");");
        }

        @Override
        public void acceptSeveralPropArrayNewElementInsertion() throws IOException {
            printJavaDoc("Inserts and returns a new empty value (as xml) as the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(xtype + " insertNew" + propertyName + "(int i);");
        }

        @Override
        public void acceptSeveralPropArrayNewElementAddition() throws IOException {
            printJavaDoc("Appends and returns a new empty value (as xml) as the last " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit(xtype + " addNew" + propertyName + "();");
        }

        @Override
        public void acceptSeveralPropArrayElementRemoving() throws IOException {
            printJavaDoc("Removes the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("void remove" + propertyName + "(int i);");
        }
    }

    void printPropertyGetters(
            int javaType,
            String type,
            boolean nillable, boolean optional,
            boolean several, boolean singleton, boolean isRadix, XBeansPropAcceptor acceptor)
            throws IOException {

        boolean xmltype = (javaType == SchemaProperty.XML_OBJECT);

        if (singleton) {
            acceptor.acceptSingletonPropGetter(several);

            if (!xmltype && !isRadix) {
                acceptor.acceptSingletonPropXmlGetter(several);
            }

            if (nillable && !isRadix) {
                acceptor.acceptSingletonPropNullCheck(several);
            }
        }

        if (optional) {
            acceptor.acceptOptionalPropExistanceCheck(several);
        }

        if (several) {
            if (_useJava15) {
                String wrappedType = type;
                if (!isRadix && isJavaPrimitive(javaType)) {
                    wrappedType = javaWrappedType(javaType);
                }
                acceptor.acceptSeveralPropListGetter(wrappedType);
            }

            if (!isRadix) {
                acceptor.acceptSeveralPropArrayGetter();
                acceptor.acceptSeveralPropArrayElementGetter();

                if (!xmltype) {
                    if (_useJava15) {
                        acceptor.acceptSeveralPropListXmlGetter();

                    }
                    acceptor.acceptSeveralPropArrayXmlGetter();
                    acceptor.acceptSeveralPropArrayElementXmlGetter();

                }

                if (nillable) {
                    acceptor.acceptSeveralPropNullCheck();
                }
                acceptor.acceptSeveralPropSizeAccess();
            }
        }
    }

    void printPropertySetters(
            int javaType,
            boolean nillable, boolean optional,
            boolean several, boolean singleton, boolean isRadixPrimitive, XBeansPropAcceptor acceptor)
            throws IOException {

        boolean xmltype = (javaType == SchemaProperty.XML_OBJECT);

        if (singleton) {
            acceptor.acceptSingletonPropSetter(several);

            if (!xmltype && !isRadixPrimitive) {
                acceptor.acceptSingletonPropXmlSetter(several);
            }

            if (xmltype && !several && !isRadixPrimitive) {
                acceptor.acceptSingletonXmlCreation();
            }

            if (nillable && !isRadixPrimitive) {
                acceptor.acceptSingletonPropSetNull(several);
            }
        }

        if (optional && (!isRadixPrimitive || nillable)) {
            acceptor.acceptOptionalPropUnset(several);
        }

        if (several && !isRadixPrimitive) {
            acceptor.acceptSeveralPropArraySetter();
            if (_useJava15 && !isJavaPrimitive(javaType)) {
                acceptor.acceptSeveralPropListAssignment();

            }
            acceptor.acceptSeveralPropArrayElementSetter();

            //  RadixTypeTestType xType;
            if (!xmltype) {
                acceptor.acceptSeveralPropArrayXmlSetter();
                acceptor.acceptSeveralPropArrayElementXmlSetter();

            }
            if (nillable) {
                acceptor.acceptSeveralPropArrayElementSetNull();
            }

            if (!xmltype) {
                acceptor.acceptSeveralPropArrayElementInsertion();
                acceptor.acceptSeveralPropArrayElementAddition();
            }

            acceptor.acceptSeveralPropArrayNewElementInsertion();
            acceptor.acceptSeveralPropArrayNewElementAddition();
            acceptor.acceptSeveralPropArrayElementRemoving();
        }
    }

    String getAtomicRestrictionType(SchemaType sType) {
        SchemaType pType = sType.getPrimitiveType();
        switch (pType.getBuiltinTypeCode()) {
            case SchemaType.BTC_ANY_SIMPLE:
                return "org.apache.xmlbeans.impl.values.XmlAnySimpleTypeImpl";
            case SchemaType.BTC_BOOLEAN:
                return "org.apache.xmlbeans.impl.values.JavaBooleanHolderEx";
            case SchemaType.BTC_BASE_64_BINARY:
                return "org.apache.xmlbeans.impl.values.JavaBase64HolderEx";
            case SchemaType.BTC_HEX_BINARY:
                return "org.apache.xmlbeans.impl.values.JavaHexBinaryHolderEx";
            case SchemaType.BTC_ANY_URI:
                return "org.apache.xmlbeans.impl.values.JavaUriHolderEx";
            case SchemaType.BTC_QNAME:
                return "org.apache.xmlbeans.impl.values.JavaQNameHolderEx";
            case SchemaType.BTC_NOTATION:
                return "org.apache.xmlbeans.impl.values.JavaNotationHolderEx";
            case SchemaType.BTC_FLOAT:
                return "org.apache.xmlbeans.impl.values.JavaFloatHolderEx";
            case SchemaType.BTC_DOUBLE:
                return "org.apache.xmlbeans.impl.values.JavaDoubleHolderEx";
            case SchemaType.BTC_DECIMAL:
                switch (sType.getDecimalSize()) {
                    case SchemaType.SIZE_BIG_DECIMAL:
                        return "org.apache.xmlbeans.impl.values.JavaDecimalHolderEx";
                    case SchemaType.SIZE_BIG_INTEGER:
                        return "org.apache.xmlbeans.impl.values.JavaIntegerHolderEx";
                    case SchemaType.SIZE_LONG:
                        return "org.apache.xmlbeans.impl.values.JavaLongHolderEx";
                    case SchemaType.SIZE_INT:
                    case SchemaType.SIZE_SHORT:
                    case SchemaType.SIZE_BYTE:
                        return "org.apache.xmlbeans.impl.values.JavaIntHolderEx";
                    default:
                        assert (false);
                        return ""; // to shut compiler up
                }
            case SchemaType.BTC_STRING:
                if (sType.hasStringEnumValues()) {
                    return "org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx";
                } else {
                    return "org.apache.xmlbeans.impl.values.JavaStringHolderEx";
                }
            case SchemaType.BTC_DATE_TIME:
            case SchemaType.BTC_TIME:
            case SchemaType.BTC_DATE:
            case SchemaType.BTC_G_YEAR_MONTH:
            case SchemaType.BTC_G_YEAR:
            case SchemaType.BTC_G_MONTH_DAY:
            case SchemaType.BTC_G_DAY:
            case SchemaType.BTC_G_MONTH:
                return "org.apache.xmlbeans.impl.values.JavaGDateHolderEx";

            case SchemaType.BTC_DURATION:
                return "org.apache.xmlbeans.impl.values.JavaGDurationHolderEx";
            default:
                assert false : "unrecognized primitive type";
                return null;
        }
    }

    static SchemaType findBaseType(SchemaType sType) {
        while (sType.getFullJavaName() == null) {
            sType = sType.getBaseType();
        }
        return sType;
    }

    String getBaseClass(SchemaType sType) {
        SchemaType baseType = findBaseType(sType.getBaseType());

        switch (sType.getSimpleVariety()) {
            case SchemaType.NOT_SIMPLE:
                // non-simple-content: inherit from base type impl
                if (!XmlObject.type.equals(baseType)) {
                    return baseType.getFullJavaImplName();
                }
                return "org.apache.xmlbeans.impl.values.XmlComplexContentImpl";

            case SchemaType.ATOMIC:
                // We should only get called for restrictions
                assert (!sType.isBuiltinType());
                return getAtomicRestrictionType(sType);

            case SchemaType.LIST: {
                RadixType type = getRadixType(sType.getListItemType());
                if (!noRadixEnums && type instanceof RadixIntEnum) {
                    return THIS_PACKAGE + ".XmlIntEnumList";
                } else if (!noRadixEnums && type instanceof RadixStrEnum) {
                    return THIS_PACKAGE + ".XmlStrEnumList";
                } else if (!noRadixEnums && type instanceof RadixSafeStrEnum) {
                    return THIS_PACKAGE + ".XmlSafeStrEnumList";
                } else if (!noRadixEnums && type instanceof RadixCharEnum) {
                    return THIS_PACKAGE + ".XmlCharEnumList";
                } else if (type instanceof RadixId) {
                    return THIS_PACKAGE + ".XmlIdList";
                } else if (type instanceof RadixDateTime) {
                    return THIS_PACKAGE + ".XmlDateTimeList";
                } else {
                    return "org.apache.xmlbeans.impl.values.XmlListImpl";
                }
            }

            case SchemaType.UNION:
                return "org.apache.xmlbeans.impl.values.XmlUnionImpl";

            default:
                throw new IllegalStateException();
        }
    }

    void printConstructor(SchemaType sType, String shortName) throws IOException {
        emit("");
        emit("public " + shortName + "(org.apache.xmlbeans.SchemaType sType)");
        startBlock();

        RadixType radixItemType = null;
        if (sType.getSimpleVariety() == SchemaType.LIST) {
            radixItemType = getRadixType(sType.getListItemType());
        }
        if (radixItemType instanceof RadixIntEnum || radixItemType instanceof RadixStrEnum || radixItemType instanceof RadixSafeStrEnum) {
            emit("super(" + radixItemType.getFullJavaClassName() + ".class, sType" + (sType.getSimpleVariety() == SchemaType.NOT_SIMPLE ? "" : ", " + !sType.isSimpleType()) + ");");
        } else {
            emit("super(sType" + (sType.getSimpleVariety() == SchemaType.NOT_SIMPLE ? "" : ", " + !sType.isSimpleType()) + ");");
        }
        endBlock();

        if (sType.getSimpleVariety() != SchemaType.NOT_SIMPLE) {
            emit("");
            emit("protected " + shortName + "(org.apache.xmlbeans.SchemaType sType, boolean b)");
            startBlock();
            if (radixItemType instanceof RadixIntEnum || radixItemType instanceof RadixStrEnum || radixItemType instanceof RadixCharEnum || radixItemType instanceof RadixSafeStrEnum) {
                emit("super(" + radixItemType.getFullJavaClassName() + ".class, sType, b);");
            } else {
                emit("super(sType, b);");
            }
            endBlock();
        }
    }

    void startClass(SchemaType sType, boolean isInner, XBeansTypeImplAcceptor acceptor) throws IOException {
        String baseClass = getBaseClass(sType);
        List<String> interfaces = new LinkedList<>();
        interfaces.add(sType.getFullJavaName().replace('$', '.'));

        if (sType.getSimpleVariety() == SchemaType.UNION) {
            SchemaType[] memberTypes = sType.getUnionMemberTypes();
            for (int i = 0; i < memberTypes.length; i++) {
                interfaces.add(memberTypes[i].getFullJavaName().replace('$', '.'));
            }
        }

        acceptor.acceptClassStart(baseClass, interfaces.toArray(new String[interfaces.size()]));
    }

    void makeAttributeDefaultValue(String jtargetType, SchemaProperty prop, String identifier) throws IOException {
        String fullName = jtargetType;
        if (fullName == null) {
            fullName = prop.javaBasedOnType().getFullJavaName().replace('$', '.');
        }
        emit("target = (" + fullName + ")get_default_attribute_value(" + identifier + ");");
    }

    void makeMissingValue(int javaType, boolean isRadix) throws IOException {
        if (isRadix) {
            emit("return null;");
            return;
        }
        switch (javaType) {
            case SchemaProperty.JAVA_BOOLEAN:
                emit("return false;");
                break;

            case SchemaProperty.JAVA_FLOAT:
                emit("return 0.0f;");
                break;

            case SchemaProperty.JAVA_DOUBLE:
                emit("return 0.0;");
                break;

            case SchemaProperty.JAVA_BYTE:
            case SchemaProperty.JAVA_SHORT:
            case SchemaProperty.JAVA_INT:
                emit("return 0;");
                break;

            case SchemaProperty.JAVA_LONG:
                emit("return 0L;");
                break;

            case SchemaProperty.XML_OBJECT:
            case SchemaProperty.JAVA_BIG_DECIMAL:
            case SchemaProperty.JAVA_BIG_INTEGER:
            case SchemaProperty.JAVA_STRING:
            case SchemaProperty.JAVA_BYTE_ARRAY:
            case SchemaProperty.JAVA_GDATE:
            case SchemaProperty.JAVA_GDURATION:
            case SchemaProperty.JAVA_DATE:
            case SchemaProperty.JAVA_QNAME:
            case SchemaProperty.JAVA_LIST:
            case SchemaProperty.JAVA_CALENDAR:
            case SchemaProperty.JAVA_ENUM:
            case SchemaProperty.JAVA_OBJECT:
            default:
                emit("return null;");
                break;
        }
    }

    private void printRadixDateTimeGetter(RadixDateTime radixDateTime, String acceptingCode) throws IOException {
        final String gdateVarName = "target_tmp_radix_gdate";
        final String calendarVarName = "target_tmp_radix_calendar";
        emit("org.apache.xmlbeans.GDate " + gdateVarName + " = " + radixDateTime.jgetMethod() + ";");
        emit("if (" + gdateVarName + " == null) return null;");
        emit("if (" + gdateVarName + ".hasTimeZone())");
        {
            startBlock();
            emit(acceptingCode + " new java.sql.Timestamp(" + gdateVarName + ".getCalendar().getTimeInMillis());");
            endBlock();
        }
        emit("else");
        {
            startBlock();
            emit("java.util.Calendar " + calendarVarName + " = java.util.Calendar.getInstance();");
            emit(calendarVarName + ".set("
                    + gdateVarName + ".getYear(),"
                    + gdateVarName + ".getMonth()-1,"
                    + gdateVarName + ".getDay(),"
                    + gdateVarName + ".getHour(),"
                    + gdateVarName + ".getMinute(),"
                    + gdateVarName + ".getSecond()"
                    + ");");
            emit(calendarVarName + ".set(java.util.Calendar.MILLISECOND, " + gdateVarName + ".getMillisecond());");
            emit(acceptingCode + " new java.sql.Timestamp(" + calendarVarName + ".getTimeInMillis());");
            endBlock();
        }
    }

    void printJGetArrayValue(int javaType, String type, RadixType radixType) throws IOException {
        if (radixType != null) {
            type = radixType.getJavaClassName();
            String suffix = radixType.isArray() ? "[]" : "";
            emit(type + "[]" + suffix + " result = new " + type + "[targetList.size()]" + suffix + ";");
            emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
            startBlock();
            emit("org.apache.xmlbeans.SimpleValue target = ((org.apache.xmlbeans.SimpleValue)targetList.get(i));");
            if (radixType instanceof RadixDateTime) {//XMLCALENDAR
                printRadixDateTimeGetter((RadixDateTime) radixType, "result[i] = ");
            } else {
                emit("result[i] = " + radixType.jgetMethod() + ";");
            }
            endBlock();
        } else {
            switch (javaType) {
                case SchemaProperty.XML_OBJECT:
                    emit(type + "[] result = new " + type + "[targetList.size()];");
                    emit("targetList.toArray(result);");
                    break;

                case SchemaProperty.JAVA_ENUM:
                    emit(type + "[] result = new " + type + "[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = (" + type + ")((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getEnumValue();");
                    break;

                case SchemaProperty.JAVA_BOOLEAN:
                    emit("boolean[] result = new boolean[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getBooleanValue();");
                    break;

                case SchemaProperty.JAVA_FLOAT:
                    emit("float[] result = new float[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getFloatValue();");
                    break;

                case SchemaProperty.JAVA_DOUBLE:
                    emit("double[] result = new double[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getDoubleValue();");
                    break;

                case SchemaProperty.JAVA_BYTE:
                    emit("byte[] result = new byte[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getByteValue();");
                    break;

                case SchemaProperty.JAVA_SHORT:
                    emit("short[] result = new short[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getShortValue();");
                    break;

                case SchemaProperty.JAVA_INT:
                    emit("int[] result = new int[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getIntValue();");
                    break;

                case SchemaProperty.JAVA_LONG:
                    emit("long[] result = new long[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getLongValue();");
                    break;

                case SchemaProperty.JAVA_BIG_DECIMAL:
                    emit("java.math.BigDecimal[] result = new java.math.BigDecimal[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getBigDecimalValue();");
                    break;

                case SchemaProperty.JAVA_BIG_INTEGER:
                    emit("java.math.BigInteger[] result = new java.math.BigInteger[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getBigIntegerValue();");
                    break;

                case SchemaProperty.JAVA_STRING:
                    emit("java.lang.String[] result = new java.lang.String[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getStringValue();");
                    break;

                case SchemaProperty.JAVA_BYTE_ARRAY:
                    emit("byte[][] result = new byte[targetList.size()][];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getByteArrayValue();");
                    break;

                case SchemaProperty.JAVA_CALENDAR:
                    //emit("java.util.Calendar[] result = new java.util.Calendar[targetList.size()];");
                    emit("java.util.Calendar[] result = new java.util.Calendar[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getCalendarValue();");
                    break;

                case SchemaProperty.JAVA_DATE:
                    emit("java.util.Date[] result = new java.util.Date[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getDateValue();");
                    break;

                case SchemaProperty.JAVA_GDATE:
                    emit("org.apache.xmlbeans.GDate[] result = new org.apache.xmlbeans.GDate[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getGDateValue();");
                    break;

                case SchemaProperty.JAVA_GDURATION:
                    emit("org.apache.xmlbeans.GDuration[] result = new org.apache.xmlbeans.GDuration[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getGDurationValue();");
                    break;

                case SchemaProperty.JAVA_QNAME:
                    emit("javax.xml.namespace.QName[] result = new javax.xml.namespace.QName[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getQNameValue();");
                    break;

                case SchemaProperty.JAVA_LIST:
                    emit("java.util.List[] result = new java.util.List[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getListValue();");
                    break;

                case SchemaProperty.JAVA_OBJECT:
                    emit("java.lang.Object[] result = new java.lang.Object[targetList.size()];");
                    emit("for (int i = 0, len = targetList.size() ; i < len ; i++)");
                    emit("    result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getObjectValue();");
                    break;

            }
        }
        emit("return result;");
    }

    void printJGetValue(SchemaProperty prop, String type) throws IOException {
        RadixType radixType = getRadixType(prop.getType());
        if (radixType != null) {
            if (prop.hasNillable() != SchemaProperty.NEVER) {
                emit("if (target.isNil()) return null;");
            }
            if (radixType instanceof RadixDateTime) {//XMLCALENDAR
                printRadixDateTimeGetter((RadixDateTime) radixType, "return ");
            } else {
                emit("return " + radixType.jgetMethod() + ";");
            }
            return;
        }

        switch (prop.getJavaTypeCode()) {
            case SchemaProperty.XML_OBJECT:
                emit("return target;");
                break;

            case SchemaProperty.JAVA_BOOLEAN:
                emit("return target.getBooleanValue();");
                break;

            case SchemaProperty.JAVA_FLOAT:
                emit("return target.getFloatValue();");
                break;

            case SchemaProperty.JAVA_DOUBLE:
                emit("return target.getDoubleValue();");
                break;

            case SchemaProperty.JAVA_BYTE:
                emit("return target.getByteValue();");
                break;

            case SchemaProperty.JAVA_SHORT:
                emit("return target.getShortValue();");
                break;

            case SchemaProperty.JAVA_INT:
                emit("return target.getIntValue();");
                break;

            case SchemaProperty.JAVA_LONG:
                emit("return target.getLongValue();");
                break;

            case SchemaProperty.JAVA_BIG_DECIMAL:
                emit("return target.getBigDecimalValue();");
                break;

            case SchemaProperty.JAVA_BIG_INTEGER:
                emit("return target.getBigIntegerValue();");
                break;

            case SchemaProperty.JAVA_STRING:
                emit("return target.getStringValue();");
                break;

            case SchemaProperty.JAVA_BYTE_ARRAY:
                emit("return target.getByteArrayValue();");
                break;

            case SchemaProperty.JAVA_GDATE:
                emit("return target.getGDateValue();");
                break;

            case SchemaProperty.JAVA_GDURATION:
                emit("return target.getGDurationValue();");
                break;

            case SchemaProperty.JAVA_CALENDAR:
                emit("return target.getCalendarValue();");
                break;

            case SchemaProperty.JAVA_DATE:
                emit("return target.getDateValue();");
                break;

            case SchemaProperty.JAVA_QNAME:
                emit("return target.getQNameValue();");
                break;

            case SchemaProperty.JAVA_LIST:
                emit("return target.getListValue();");
                break;

            case SchemaProperty.JAVA_ENUM:
                emit("return (" + type + ")target.getEnumValue();");
                break;

            case SchemaProperty.JAVA_OBJECT:
                emit("return target.getObjectValue();");
                break;
        }
    }

    String jsetMethod(SchemaProperty prop) {
        RadixType type = getRadixType(prop.getType());
        if (type != null) {
            return type.jsetMethod();
        }
        switch (prop.getJavaTypeCode()) {
            case SchemaProperty.XML_OBJECT:
                return "target.set";

            case SchemaProperty.JAVA_BOOLEAN:
                return "target.setBooleanValue";

            case SchemaProperty.JAVA_FLOAT:
                return "target.setFloatValue";

            case SchemaProperty.JAVA_DOUBLE:
                return "target.setDoubleValue";

            case SchemaProperty.JAVA_BYTE:
                return "target.setByteValue";

            case SchemaProperty.JAVA_SHORT:
                return "target.setShortValue";

            case SchemaProperty.JAVA_INT:
                return "target.setIntValue";

            case SchemaProperty.JAVA_LONG:
                return "target.setLongValue";

            case SchemaProperty.JAVA_BIG_DECIMAL:
                return "target.setBigDecimalValue";

            case SchemaProperty.JAVA_BIG_INTEGER:
                return "target.setBigIntegerValue";

            case SchemaProperty.JAVA_STRING:
                return "target.setStringValue";

            case SchemaProperty.JAVA_BYTE_ARRAY:
                return "target.setByteArrayValue";

            case SchemaProperty.JAVA_GDATE:
                return "target.setGDateValue";

            case SchemaProperty.JAVA_GDURATION:
                return "target.setGDurationValue";

            case SchemaProperty.JAVA_CALENDAR:
                return "target.setCalendarValue";

            case SchemaProperty.JAVA_DATE:
                return "target.setDateValue";

            case SchemaProperty.JAVA_QNAME:
                return "target.setQNameValue";

            case SchemaProperty.JAVA_LIST:
                return "target.setListValue";

            case SchemaProperty.JAVA_ENUM:
                return "target.setEnumValue";

            case SchemaProperty.JAVA_OBJECT:
                return "target.setObjectValue";

            default:
                throw new IllegalStateException();
        }
    }

    String getIdentifier(Map<QName, String[]> qNameMap, QName qName) {
        return qNameMap.get(qName)[0];
    }

    String getSetIdentifier(Map<QName, String[]> qNameMap, QName qName) {
        String[] identifiers = qNameMap.get(qName);
        return identifiers[1] == null ? identifiers[0] : identifiers[1];
    }

    Map<QName, String[]> printStaticFields(SchemaProperty[] properties) throws IOException {
        final Map<QName, String[]> results = new HashMap<>();

        emit("");
        for (int i = 0; i < properties.length; i++) {
            final String[] identifiers = new String[2];
            final SchemaProperty prop = properties[i];
            final QName name = prop.getName();
            results.put(name, identifiers);
            final String javaName = prop.getJavaPropertyName();
            identifiers[0] = (javaName + "$" + (i * 2)).toUpperCase();
            final String uriString = "\"" + name.getNamespaceURI() + "\"";

            emit("private static final javax.xml.namespace.QName " + identifiers[0]
                    + " = ");
            indent();
            emit("new javax.xml.namespace.QName("
                    + uriString + ", \"" + name.getLocalPart() + "\");");
            outdent();

            if (properties[i].acceptedNames() != null) {
                final QName[] qnames = properties[i].acceptedNames();

                if (qnames.length > 1) {
                    identifiers[1] = (javaName + "$" + (i * 2 + 1)).toUpperCase();

                    emit("private static final org.apache.xmlbeans.QNameSet " + identifiers[1]
                            + " = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { ");
                    indent();
                    for (int j = 0; j < qnames.length; j++) {
                        emit("new javax.xml.namespace.QName(\"" + qnames[j].getNamespaceURI()
                                + "\", \"" + qnames[j].getLocalPart() + "\"),");
                    }

                    outdent();

                    emit("});");
                }
            }
        }
        emit("");
        return results;
    }

    void emitImplementationPreamble() throws IOException {
        emit("synchronized (monitor())");
        emit("{");
        indent();
        emit("check_orphaned();");
    }

    void emitImplementationPostamble() throws IOException {
        outdent();
        emit("}");
    }

    void emitDeclareTarget(boolean declareTarget, String xtype)
            throws IOException {
        if (declareTarget) {
            emit(xtype + " target = null;");
        }
    }

    void emitAddTarget(
            String identifier,
            boolean isAttr,
            @SuppressWarnings("unused") boolean declareTarget,
            String xtype) throws IOException {
        if (isAttr) {
            emit("target = (" + xtype + ")get_store().add_attribute_user(" + identifier + ");");
        } else {
            emit("target = (" + xtype + ")get_store().add_element_user(" + identifier + ");");
        }
    }

    void emitPre(SchemaType sType, int opType, String identifier, boolean isAttr) throws IOException {
        emitPre(sType, opType, identifier, isAttr, "-1");
    }

    void emitPre(SchemaType sType, int opType, String identifier, boolean isAttr, String index) throws IOException {
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl == null) {
            return;
        }
        PrePostExtension ext = sImpl.getPrePostExtension();
        if (ext != null) {
            if (ext.hasPreCall()) {
                emit("if ( " + ext.getStaticHandler() + ".preSet(" + prePostOpString(opType) + ", this, " + identifier + ", " + isAttr + ", " + index + "))");
                startBlock();
            }
        }
    }

    void emitPost(SchemaType sType, int opType, String identifier, boolean isAttr) throws IOException {
        emitPost(sType, opType, identifier, isAttr, "-1");
    }

    void emitPost(SchemaType sType, int opType, String identifier, boolean isAttr, String index) throws IOException {
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl == null) {
            return;
        }
        PrePostExtension ext = sImpl.getPrePostExtension();
        if (ext != null) {
            if (ext.hasPreCall()) {
                endBlock();
            }

            if (ext.hasPostCall()) {
                emit(ext.getStaticHandler() + ".postSet(" + prePostOpString(opType) + ", this, " + identifier + ", " + isAttr + ", " + index + ");");
            }
        }
    }

    String prePostOpString(int opType) {
        switch (opType) {
            case PrePostExtension.OPERATION_SET:
                return "org.apache.xmlbeans.PrePostExtension.OPERATION_SET";
            case PrePostExtension.OPERATION_INSERT:
                return "org.apache.xmlbeans.PrePostExtension.OPERATION_INSERT";
            case PrePostExtension.OPERATION_REMOVE:
                return "org.apache.xmlbeans.PrePostExtension.OPERATION_REMOVE";
            default:
                assert false;
                return ""; // to shut compiler up
        }
    }
    private static final int NOTHING = 1;
    private static final int ADD_NEW_VALUE = 3;
    private static final int THROW_EXCEPTION = 4;

    void emitGetTarget(String setIdentifier,
            String identifier,
            boolean isAttr,
            String index,
            int nullBehaviour,
            String xtype)
            throws IOException {
        assert setIdentifier != null && identifier != null;

        emit(xtype + " target = null;");

        if (isAttr) {
            emit("target = (" + xtype + ")get_store().find_attribute_user(" + identifier + ");");
        } else {
            emit("target = (" + xtype + ")get_store().find_element_user(" + setIdentifier + ", " + index + ");");
        }
        if (nullBehaviour == NOTHING) {
            return;
        }
        emit("if (target == null)");

        startBlock();

        switch (nullBehaviour) {
            case ADD_NEW_VALUE:
                // target already emited, no need for emitDeclareTarget(false, xtype);
                emitAddTarget(identifier, isAttr, false, xtype);
                break;

            case THROW_EXCEPTION:
                emit("throw new IndexOutOfBoundsException();");
                break;

            case NOTHING:
                break;

            default:
                assert false : "Bad behaviour type: " + nullBehaviour;
        }

        endBlock();
    }

    void printListGetter15Impl(
            String parentJavaName,
            String wrappedType,
            boolean xmltype,
            boolean xget, XBeansPropImplAcceptor acceptor) throws IOException {
        String parentThis = parentJavaName + ".this.";
        acceptor.acceptListGetter15GetList(xget, wrappedType, parentThis, xmltype);

    }

    private class DefaultPropImplAcceptor implements XBeansPropImplAcceptor {

        private String propdesc;
        private String propertyName;
        private boolean isAttr;
        private boolean several;
        private String type;
        private String xtype;
        private String arrayName;
        private String listName;
        private String safeVarName;
        private RadixType radixType;
        private boolean isDeprecated;

        @Override
        public void beginProperty(SchemaProperty prop, QName qName, String propertyName, String arrayName, String listName, String type, String xtype, boolean isAttr, boolean several, boolean isDeprecated) {
            propdesc = "\"" + qName.getLocalPart() + "\"" + (isAttr ? " attribute" : " element");
            this.propertyName = propertyName;
            this.isAttr = isAttr;
            this.isDeprecated = isDeprecated;
            this.several = several;
            this.type = type;
            this.xtype = xtype;
            this.arrayName = arrayName;
            this.listName = listName;
            safeVarName = NameUtil.nonJavaKeyword(NameUtil.lowerCamelCase(propertyName));
            if ("i".equals(safeVarName)) {
                safeVarName = "iValue";
            } else if ("target".equals(safeVarName)) {
                safeVarName = "targetValue";
            }
            radixType = getRadixType(prop.getType());
        }

        @Override
        public void endProperty() {
        }

        @Override
        public void acceptSingletonPropGetter(SchemaProperty prop, String jtargetType, String identifier, String setIdentifier, int javaType, boolean isRadix) throws IOException {
            printJavaDoc((several ? "Gets first " : "Gets the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + type + " get" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();

            emitGetTarget(setIdentifier, identifier, isAttr, "0", NOTHING, jtargetType);

            if (isAttr && (prop.hasDefault() == SchemaProperty.CONSISTENTLY
                    || prop.hasFixed() == SchemaProperty.CONSISTENTLY)) {
                emit("if (target == null)");
                startBlock();
                makeAttributeDefaultValue(jtargetType, prop, identifier);
                endBlock();
            }
            emit("if (target == null)");
            startBlock();
            makeMissingValue(javaType, isRadix);
            endBlock();

            printJGetValue(prop, type);

            emitImplementationPostamble();

            endBlock();
        }

        private void printRadixDateTimeSetterPreparation() throws IOException {
            String calendarVarName = safeVarName + "_tmp_radix_calendar";
            emit("java.util.Calendar " + calendarVarName + " = java.util.Calendar.getInstance();");
            emit(calendarVarName + "." + "setTimeInMillis(" + safeVarName + ".getTime());");
            emit("org.apache.xmlbeans.GDate " + safeVarName + "_tmp_radix = new org.apache.xmlbeans.GDate("
                    + calendarVarName + ".get(java.util.Calendar.YEAR),"
                    + calendarVarName + ".get(java.util.Calendar.MONTH)+1,"
                    + calendarVarName + ".get(java.util.Calendar.DAY_OF_MONTH),"
                    + calendarVarName + ".get(java.util.Calendar.HOUR_OF_DAY),"
                    + calendarVarName + ".get(java.util.Calendar.MINUTE),"
                    + calendarVarName + ".get(java.util.Calendar.SECOND),"
                    + "java.math.BigDecimal.valueOf(" + calendarVarName + ".get(java.util.Calendar.MILLISECOND), 3)"
                    + ");");
        }

        @Override
        public void acceptSingletonPropSetter(SchemaType sType, SchemaProperty prop, String jtargetType, int javaType, String identifier, String setIdentifier, boolean isRadix, boolean xmltype, String jSet, boolean optional, boolean nillable) throws IOException {
            printJavaDoc((several ? "Sets first " : "Sets the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void set" + propertyName + "(" + type + " " + safeVarName + ")");
            startBlock();
            emitImplementationPreamble();
            String closingPar;
            if (jSet.endsWith("(")) {
                closingPar = "));";
                jSet = jSet.substring(0, jSet.length() - 1);
            } else {
                closingPar = ");";
            }
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
            if (isRadix || xmltype) {
                emit("if (" + safeVarName + " != null)");
                startBlock();
                emitGetTarget(setIdentifier, identifier, isAttr, "0", ADD_NEW_VALUE, jtargetType);
                if (radixType instanceof RadixDateTime) {
                    //emit("java.util.Calendar " + safeVarName + "_tmp_radix = java.util.Calendar.getInstance();");
                    //emit(safeVarName + "_tmp_radix.setTimeInMillis(" + safeVarName + ".getTime());");

                    //emit("org.apache.xmlbeans.XmlCalendar " + safeVarName + "_tmp_radix = new org.apache.xmlbeans.XmlCalendar(" + safeVarName + ");");
//                    String calendarVarName = safeVarName + "_tmp_radix_calendar";
//                    emit("org.apache.xmlbeans.XmlCalendar " + calendarVarName + " = new org.apache.xmlbeans.XmlCalendar(" + safeVarName + ");");
//                    emit("org.apache.xmlbeans.GDate " + safeVarName + "_tmp_radix = new org.apache.xmlbeans.GDate("
//                            + calendarVarName + ".get(java.util.Calendar.YEAR),"
//                            + calendarVarName + ".get(java.util.Calendar.MONTH),"
//                            + calendarVarName + ".get(java.util.Calendar.DAY_OF_MONTH),"
//                            + calendarVarName + ".get(java.util.Calendar.HOUR),"
//                            + calendarVarName + ".get(java.util.Calendar.MINUTE),"
//                            + calendarVarName + ".get(java.util.Calendar.SECOND),"
//                            + "java.math.BigDecimal.valueOf(" + calendarVarName + ".get(java.util.Calendar.MILLISECOND), 3)"
//                            + ");");
                    printRadixDateTimeSetterPreparation();

                }

                emit(jSet + "(" + (radixType != null ? radixType.jUnboxed(safeVarName, javaType) : safeVarName) + closingPar);
                endBlock();
                emit("else");
                startBlock();
                if (optional && !nillable) {
                    emitPre(sType, PrePostExtension.OPERATION_REMOVE, identifier, isAttr, several ? "0" : "-1");
                    emitGetTarget(setIdentifier, identifier, isAttr, "0", NOTHING, jtargetType);
                    emit("if (target != null)");
                    indent();
                    if (isAttr) {
                        emit("get_store().remove_attribute(" + identifier + ");");
                    } else {
                        emit("get_store().remove_element(" + setIdentifier + ", 0);");
                    }
                    outdent();
                    emitPost(sType, PrePostExtension.OPERATION_REMOVE, identifier, isAttr, several ? "0" : "-1");
                } else if (nillable) {
                    emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
                    emitGetTarget(setIdentifier, identifier, isAttr, "0", ADD_NEW_VALUE, xtype);
                    emit("target.setNil();");
                    emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
                } else {
                    emit("throw new NullPointerException(\"Setting null to non-nillable property\");");
                }
                endBlock();
            } else {
                emitGetTarget(setIdentifier, identifier, isAttr, "0", ADD_NEW_VALUE, jtargetType);
                emit(jSet + "(" + safeVarName + closingPar);
            }
            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSingletonPropXmlGetter(SchemaProperty prop, String identifier, String setIdentifier) throws IOException {
            // Value xgetProp()
            printJavaDoc((several ? "Gets (as xml) first " : "Gets (as xml) the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + xtype + " xget" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitGetTarget(setIdentifier, identifier, isAttr, "0", NOTHING, xtype);

            if (isAttr && (prop.hasDefault() == SchemaProperty.CONSISTENTLY
                    || prop.hasFixed() == SchemaProperty.CONSISTENTLY)) {
                emit("if (target == null)");
                startBlock();
                makeAttributeDefaultValue(xtype, prop, identifier);
                endBlock();
            }

            emit("return target;");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSingletonPropXmlSetter(SchemaType sType, String identifier, String setIdentifier) throws IOException {
            // void xsetProp(Value v)
            printJavaDoc((several ? "Sets (as xml) first " : "Sets (as xml) the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void xset" + propertyName + "(" + xtype + " " + safeVarName + ")");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
            emitGetTarget(setIdentifier, identifier, isAttr, "0", ADD_NEW_VALUE, xtype);
            emit("target.set(" + safeVarName + ");");
            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSingletonPropNullCheck(String identifier, String setIdentifier) throws IOException {
            // boolean isNilProp()
            printJavaDoc((several ? "Tests for nil first " : "Tests for __ nil ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public boolean isNil" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitGetTarget(setIdentifier, identifier, isAttr, "0", NOTHING, xtype);

            emit("if (target == null) return false;");
            emit("return target.isNil();");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptOptionalPropExistanceCheck(String identifier, String setIdentifier) throws IOException {
            // boolean isSetProp()
            printJavaDoc((several ? "True if has at least one " : "True if has ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public boolean isSet" + propertyName + "()");

            startBlock();
            emitImplementationPreamble();

            if (isAttr) {
                emit("return get_store().find_attribute_user(" + identifier + ") != null;");
            } else {
                emit("return get_store().count_elements(" + setIdentifier + ") != 0;");
            }
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArrayAccess(SchemaProperty prop, String setIdentifier, int javaType) throws IOException {
            // Value[] getProp()
            printJavaDoc("Gets array of all " + propdesc + "s");
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + type + "[] get" + arrayName + "()");
            startBlock();
            emitImplementationPreamble();

            emit("java.util.List targetList = new java.util.ArrayList();");
            emit("get_store().find_all_element_users(" + setIdentifier + ", targetList);");

            printJGetArrayValue(javaType, type, getRadixType(prop.getType()));

            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArrayElementAccess(SchemaProperty prop, String identifier, String setIdentifier, String jtargetType) throws IOException {
            // Value getProp(int i)
            printJavaDoc("Gets ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + type + " get" + arrayName + "(int i)");
            startBlock();
            emitImplementationPreamble();

            emitGetTarget(setIdentifier, identifier, isAttr, "i", THROW_EXCEPTION, jtargetType);
            printJGetValue(prop, type);

            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArrayXmlGetter(String setIdentifier) throws IOException {
            // Value[] xgetProp()
            printJavaDoc("Gets (as xml) array of all " + propdesc + "s");
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + xtype + "[] xget" + arrayName + "()");
            startBlock();
            emitImplementationPreamble();
            emit("java.util.List targetList = new java.util.ArrayList();");
            emit("get_store().find_all_element_users(" + setIdentifier + ", targetList);");
            emit(xtype + "[] result = new " + xtype + "[targetList.size()];");
            emit("targetList.toArray(result);");
            emit("return result;");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArrayElementXmlGetter(String identifier, String setIdentifier) throws IOException {
            // Value xgetProp(int i)
            printJavaDoc("Gets (as xml) ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + xtype + " xget" + arrayName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitGetTarget(setIdentifier, identifier, isAttr, "i", THROW_EXCEPTION, xtype);
            emit("return (" + xtype + ")target;");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSingletonPropCreation(SchemaType sType, String identifier) throws IOException {
            printJavaDoc("Appends and returns a new empty " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + xtype + " addNew" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitDeclareTarget(true, xtype);
            emitPre(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr);
            emitAddTarget(identifier, isAttr, true, xtype);
            emitPost(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr);
            emit("return target;");
            emitImplementationPostamble();
            endBlock();

            printJavaDoc("Appends and returns a new empty " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + xtype + " ensure" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emit(xtype + " existing = get" + propertyName + "();");
            emit("return existing == null ? addNew" + propertyName + "() : existing;");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSingletonPropSetNull(SchemaType sType, String identifier, String setIdentifier) throws IOException {
            printJavaDoc((several ? "Nils the first " : "Nils the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void setNil" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
            emitGetTarget(setIdentifier, identifier, isAttr, "0", ADD_NEW_VALUE, xtype);
            emit("target.setNil();");
            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, several ? "0" : "-1");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropNullCheck(String identifier, String setIdentifier) throws IOException {
            // boolean isNil(int i);
            printJavaDoc("Tests for nil ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public boolean isNil" + arrayName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitGetTarget(setIdentifier, identifier, isAttr, "i", THROW_EXCEPTION, xtype);
            emit("return target.isNil();");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptOptionalPropUnset(SchemaType sType, String identifier, String setIdentifier) throws IOException {
            printJavaDoc((several ? "Removes first " : "Unsets the ") + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void unset" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_REMOVE, identifier, isAttr, several ? "0" : "-1");
            if (isAttr) {
                emit("get_store().remove_attribute(" + identifier + ");");
            } else {
                emit("if(get_store().count_elements(" + setIdentifier + ")>0){");
                emit("get_store().remove_element(" + setIdentifier + ", 0);");
                emit("}");
            }
            emitPost(sType, PrePostExtension.OPERATION_REMOVE, identifier, isAttr, several ? "0" : "-1");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArraySetter(SchemaType sType, String identifier, String setIdentifier, boolean isobj, boolean isSubstGroup) throws IOException {
            printJavaDoc("Sets array of all " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void set" + arrayName + "(" + type + "[] " + safeVarName + "Array)");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr);

            if (isobj) {
                if (!isSubstGroup) {
                    emit("unionArraySetterHelper(" + safeVarName + "Array" + ", " + identifier + ");");
                } else {
                    emit("unionArraySetterHelper(" + safeVarName + "Array" + ", " + identifier + ", " + setIdentifier + ");");
                }
            } else {
                if (!isSubstGroup) {
                    emit("arraySetterHelper(" + safeVarName + "Array" + ", " + identifier + ");");
                } else {
                    emit("arraySetterHelper(" + safeVarName + "Array" + ", " + identifier + ", " + setIdentifier + ");");
                }
            }

            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr);
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArrayElementSetter(SchemaType sType, SchemaProperty prop, String identifier, String setIdentifier, String jtargetType, String jSet, int javaType) throws IOException {
            printJavaDoc("Sets ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void set" + arrayName + "(int i, " + type + " " + safeVarName + ")");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, "i");
            emitGetTarget(setIdentifier, identifier, isAttr, "i", THROW_EXCEPTION, jtargetType);
            String closingPar;
            if (jSet.endsWith("(")) {
                closingPar = "));";
                jSet = jSet.substring(0, jSet.length() - 1);
            } else {
                closingPar = ");";
            }
            if (radixType instanceof RadixDateTime) {
                //emit("java.util.Calendar " + safeVarName + "_tmp_radix = java.util.Calendar.getInstance();");
                //emit(safeVarName + "_tmp_radix.setTimeInMillis(" + safeVarName + ".getTime());");
                //emit("org.apache.xmlbeans.XmlCalendar " + safeVarName + "_tmp_radix = new org.apache.xmlbeans.XmlCalendar(" + safeVarName + ");");
                emit("if (" + safeVarName + " == null)");
                startBlock();
                emit(jSet + "(null" + closingPar);
                endBlock();
                emit("else");
                startBlock();
                printRadixDateTimeSetterPreparation();
                emit(jSet + "(" + safeVarName + "_tmp_radix" + closingPar);
                endBlock();
            } else if (radixType instanceof RadixType) {
                emit("if (" + safeVarName + "== null)");
                indent();
                emit("target.setNil();");
                outdent();
                emit("else");
                //emit(jSet + "(" + safeVarName + ");");
                emit(jSet + "(" + (radixType != null ? radixType.jUnboxed(safeVarName, javaType) : safeVarName) + closingPar);
            } else {
                emit(jSet + "(" + (radixType != null ? radixType.jUnboxed(safeVarName, javaType) : safeVarName) + closingPar);
            }
            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, "i");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArrayXmlSetter(SchemaType sType, String identifier) throws IOException {
            printJavaDoc("Sets (as xml) array of all " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void xset" + arrayName + "(" + xtype + "[]" + safeVarName + "Array)");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr);
            emit("arraySetterHelper(" + safeVarName + "Array" + ", " + identifier + ");");
            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr);
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropArrayElementXmlSetter(SchemaType sType, String identifier, String setIdentifier) throws IOException {
            printJavaDoc("Sets (as xml) ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void xset" + arrayName + "(int i, " + xtype + " " + safeVarName + ")");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, "i");
            emitGetTarget(setIdentifier, identifier, isAttr, "i", THROW_EXCEPTION, xtype);
            emit("target.set(" + safeVarName + ");");
            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, "i");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropSetNull(SchemaType sType, String identifier, String setIdentifier) throws IOException {
            printJavaDoc("Nils the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void setNil" + arrayName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, "i");
            emitGetTarget(setIdentifier, identifier, isAttr, "i", THROW_EXCEPTION, xtype);
            emit("target.setNil();");
            emitPost(sType, PrePostExtension.OPERATION_SET, identifier, isAttr, "i");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropElementInsertion(SchemaType sType, String identifier, String setIdentifier, String jtargetType, boolean isSubstGroup, String jSet, int javaType) throws IOException {
            printJavaDoc("Inserts the value as the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void insert" + propertyName + "(int i, " + type + " " + safeVarName + ")");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr, "i");
            emit(jtargetType + " target = ");
            indent();
            if (!isSubstGroup) {
                emit("(" + jtargetType + ")get_store().insert_element_user(" + identifier + ", i);");
            } else // This is a subst group case
            {
                emit("(" + jtargetType + ")get_store().insert_element_user(" + setIdentifier + ", "
                        + identifier + ", i);");
            }
            outdent();
            String closingPar;
            if (jSet.endsWith("(")) {
                closingPar = "));";
                jSet = jSet.substring(0, jSet.length() - 1);
            } else {
                closingPar = ");";
            }
            if (radixType instanceof RadixDateTime) {
                //emit("java.util.Calendar " + safeVarName + "_tmp_radix = java.util.Calendar.getInstance();");
                //emit(safeVarName + "_tmp_radix.setTimeInMillis(" + safeVarName + ".getTime());");
                //emit("org.apache.xmlbeans.XmlCalendar " + safeVarName + "_tmp_radix = " + safeVarName + " == null ? null : new org.apache.xmlbeans.XmlCalendar(" + safeVarName + ");");
                emit("if (" + safeVarName + " == null)");
                startBlock();
                emit(jSet + "(null" + closingPar);
                endBlock();
                emit("else");
                startBlock();
                printRadixDateTimeSetterPreparation();
                emit(jSet + "(" + safeVarName + "_tmp_radix" + closingPar);
                endBlock();
            } else if (radixType instanceof RadixType) {
                emit("if (" + safeVarName + "== null)");
                indent();
                emit("target.setNil();");
                outdent();
                emit("else");
                //emit(jSet + "(" + safeVarName + ");");
                emit(jSet + "(" + (radixType != null ? radixType.jUnboxed(safeVarName, javaType) : safeVarName) + closingPar);
            } else {
                emit(jSet + "(" + (radixType != null ? radixType.jUnboxed(safeVarName, javaType) : safeVarName) + closingPar);
            }
            emitPost(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr, "i");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropElementAddition(SchemaType sType, String identifier, String jtargetType, int javaType, String jSet) throws IOException {
            printJavaDoc("Appends the value as the last " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void add" + propertyName + "(" + type + " " + safeVarName + ")");
            startBlock();
            emitImplementationPreamble();
            emitDeclareTarget(true, jtargetType);
            emitPre(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr);
            emitAddTarget(identifier, isAttr, true, jtargetType);
            String closingPar;
            if (jSet.endsWith("(")) {
                closingPar = "));";
                jSet = jSet.substring(0, jSet.length() - 1);
            } else {
                closingPar = ");";
            }
            if (radixType instanceof RadixDateTime) {
                // emit("java.util.Calendar " + safeVarName + "_tmp_radix = java.util.Calendar.getInstance();");
                // emit(safeVarName + "_tmp_radix.setTimeInMillis(" + safeVarName + ".getTime());");
                //emit("org.apache.xmlbeans.XmlCalendar " + safeVarName + "_tmp_radix = " + safeVarName + " == null ? null : new org.apache.xmlbeans.XmlCalendar(" + safeVarName + ");");
                emit("if (" + safeVarName + " == null)");
                startBlock();
                emit(jSet + "(null" + closingPar);
                endBlock();
                emit("else");
                startBlock();
                printRadixDateTimeSetterPreparation();
                emit(jSet + "(" + safeVarName + "_tmp_radix" + closingPar);
                endBlock();
            } else if (radixType instanceof RadixType) {
                emit("if (" + safeVarName + "== null)");
                indent();
                emit("target.setNil();");
                outdent();
                emit("else");
                //emit(jSet + "(" + safeVarName + ");");
                emit(jSet + "(" + (radixType != null ? radixType.jUnboxed(safeVarName, javaType) : safeVarName) + closingPar);
            } else {
                emit(jSet + "(" + (radixType != null ? radixType.jUnboxed(safeVarName, javaType) : safeVarName) + closingPar);
            }
            emitPost(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr);
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropNewElementInsertion(SchemaType sType, String identifier, String setIdentifier, boolean isSubstGroup) throws IOException {
            printJavaDoc("Inserts and returns a new empty value (as xml) as the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + xtype + " insertNew" + propertyName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitDeclareTarget(true, xtype);
            emitPre(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr, "i");
            if (!isSubstGroup) {
                emit("target = (" + xtype + ")get_store().insert_element_user(" + identifier + ", i);");
            } else // This is a subst group case
            {
                emit("target = (" + xtype + ")get_store().insert_element_user("
                        + setIdentifier + ", " + identifier + ", i);");
            }
            emitPost(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr, "i");
            emit("return target;");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropNewElementAddition(SchemaType sType, String identifier) throws IOException {
            printJavaDoc("Appends and returns a new empty value (as xml) as the last " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public " + xtype + " addNew" + propertyName + "()");
            startBlock();
            emitImplementationPreamble();
            emitDeclareTarget(true, xtype);
            emitPre(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr);
            emitAddTarget(identifier, isAttr, true, xtype);
            emitPost(sType, PrePostExtension.OPERATION_INSERT, identifier, isAttr);
            emit("return target;");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropElementRemove(SchemaType sType, String identifier, String setIdentifier) throws IOException {
            printJavaDoc("Removes the ith " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void remove" + propertyName + "(int i)");
            startBlock();
            emitImplementationPreamble();
            emitPre(sType, PrePostExtension.OPERATION_REMOVE, identifier, isAttr, "i");
            emit("get_store().remove_element(" + setIdentifier + ", i);");
            emitPost(sType, PrePostExtension.OPERATION_REMOVE, identifier, isAttr, "i");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptSeveralPropSizeAccess(String setIdentifier) throws IOException {
            // int countProp();
            printJavaDoc("Returns number of " + propdesc);
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public int sizeOf" + arrayName + "()");
            startBlock();
            emitImplementationPreamble();
            emit("return get_store().count_elements(" + setIdentifier + ");");
            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptListGetter15GetList(boolean xget, String wrappedType, String parentThis, boolean xmltype) throws IOException {
            String xgetMethod = (xget ? "x" : "") + "get";
            String xsetMethod = (xget ? "x" : "") + "set";

            printJavaDoc("Gets " + (xget ? "(as xml) " : "") + "a List of " + propdesc + "s");
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public java.util.List<" + wrappedType + "> " + xgetMethod + listName + "()");
            startBlock();

            emit("final class " + listName + " extends java.util.AbstractList<" + wrappedType + ">");
            startBlock();

            // Object get(i)
            emit("public " + wrappedType + " get(int i)");
            emit("    { return " + parentThis + xgetMethod + arrayName + "(i); }");
            emit("");

            // Object set(i, o)
            emit("public " + wrappedType + " set(int i, " + wrappedType + " o)");
            startBlock();
            emit(wrappedType + " old = " + parentThis + xgetMethod + arrayName + "(i);");
            emit(parentThis + xsetMethod + arrayName + "(i, o);");
            emit("return old;");
            endBlock();
            emit("");

            // void add(i, o)
            emit("public void add(int i, " + wrappedType + " o)");
            if (xmltype || xget) {
                emit("    { " + parentThis + "insertNew" + propertyName + "(i).set(o); }");
            } else {
                emit("    { " + parentThis + "insert" + propertyName + "(i, o); }");
            }
            emit("");

            // Object remove(i)
            emit("public " + wrappedType + " remove(int i)");
            startBlock();
            emit(wrappedType + " old = " + parentThis + xgetMethod + arrayName + "(i);");
            emit(parentThis + "remove" + propertyName + "(i);");
            emit("return old;");
            endBlock();
            emit("");

            // int size()
            emit("public int size()");
            emit("    { return " + parentThis + "sizeOf" + arrayName + "(); }");
            emit("");

            endBlock();

            emit("");

            emitImplementationPreamble();

            emit("return new " + listName + "();");

            emitImplementationPostamble();
            endBlock();
        }

        @Override
        public void acceptListSetter15AssignList(SchemaType sType, String identifier, String setIdentifier, boolean isobj, boolean isSubstGroup) throws IOException {
            printJavaDoc("Assign a List of " + propdesc + "s");
            if (isDeprecated) {
                emit("@Deprecated");
            }
            emit("public void assign" + listName + "(java.util.List<" + type + "> " + safeVarName + "List" + ")");
            startBlock();
            String arrayTypeString = type;
            int arrayMark = arrayTypeString.indexOf('[');
            if (arrayMark > 0) {
                arrayTypeString = arrayTypeString.substring(0, arrayMark) + "[" + safeVarName + "List.size()]" + arrayTypeString.substring(arrayMark);
            } else {
                arrayTypeString += "[" + safeVarName + "List.size()]";
            }
            emit("set" + arrayName + "(" + safeVarName + "List.toArray(new " + arrayTypeString + "));");
            endBlock();

        }
    }

    void printGetterImpls(String parentJavaName,
            SchemaProperty prop, String propertyName,
            int javaType, String type, String xtype, boolean nillable,
            boolean optional, boolean several, boolean singleton,
            boolean isunion,
            String identifier, String setIdentifier, XBeansPropImplAcceptor acceptor)
            throws IOException {

        boolean isRadix = isRadixType(prop);
        boolean xmltype = (javaType == SchemaProperty.XML_OBJECT);
        String jtargetType = (isunion || !xmltype) ? "org.apache.xmlbeans.SimpleValue" : xtype;

        if (singleton) {
            // Value getProp()
            acceptor.acceptSingletonPropGetter(prop, jtargetType, identifier, setIdentifier, javaType, isRadix);

            if (!xmltype && !isRadix) {
                acceptor.acceptSingletonPropXmlGetter(prop, identifier, setIdentifier);
            }

            if (nillable && !isRadix) {
                acceptor.acceptSingletonPropNullCheck(identifier, setIdentifier);
            }
        }

        if (optional) {
            acceptor.acceptOptionalPropExistanceCheck(identifier, setIdentifier);
        }

        if (several) {
            if (_useJava15) {
                // use boxed type if the java type is a primitive and jdk1.5
                // jdk1.5 will box/unbox for us
                String wrappedType = type;
                if (!isRadix && isJavaPrimitive(javaType)) {
                    wrappedType = javaWrappedType(javaType);
                }
                printListGetter15Impl(parentJavaName, wrappedType, xmltype, false, acceptor);

            }

            acceptor.acceptSeveralPropArrayAccess(prop, setIdentifier, javaType);
            acceptor.acceptSeveralPropArrayElementAccess(prop, identifier, setIdentifier, jtargetType);

            if (!xmltype && !isRadix) {
                if (_useJava15) {
                    printListGetter15Impl(parentJavaName, xtype, xmltype, true, acceptor);
                }

                acceptor.acceptSeveralPropArrayXmlGetter(setIdentifier);
                acceptor.acceptSeveralPropArrayElementXmlGetter(identifier, setIdentifier);
            }

            if (nillable) {
                acceptor.acceptSeveralPropNullCheck(identifier, setIdentifier);
            }

            acceptor.acceptSeveralPropSizeAccess(setIdentifier);
        }
    }

    void printSetterImpls(QName qName, SchemaProperty prop,
            String propertyName, int javaType, String xtype,
            boolean nillable, boolean optional, boolean several, boolean singleton,
            boolean isunion, String identifier, String setIdentifier, SchemaType sType, XBeansPropImplAcceptor acceptor)
            throws IOException {
        boolean isRadixPrimitive = isRadixPrimitiveType(prop);

        boolean xmltype = (javaType == SchemaProperty.XML_OBJECT);
        boolean isobj = (javaType == SchemaProperty.JAVA_OBJECT);
        boolean isSubstGroup = !Objects.equals(identifier, setIdentifier);
        String jSet = jsetMethod(prop);
        String jtargetType = (isunion || !xmltype) ? "org.apache.xmlbeans.SimpleValue" : xtype;

        if (singleton) {
            // void setProp(Value v);
            acceptor.acceptSingletonPropSetter(sType, prop, jtargetType, javaType, identifier, setIdentifier, isRadixType(prop), xmltype, jSet, optional, nillable);

            if (!xmltype && !isRadixPrimitive) {
                acceptor.acceptSingletonPropXmlSetter(sType, identifier, setIdentifier);
            }

            if (xmltype && !several && !isRadixPrimitive) {
                // Value addNewProp()
                acceptor.acceptSingletonPropCreation(sType, identifier);
            }

            if (nillable && !isRadixPrimitive) {
                acceptor.acceptSingletonPropSetNull(sType, identifier, setIdentifier);
            }
        }

        if (optional && (!isRadixPrimitive || nillable)) {
            acceptor.acceptOptionalPropUnset(sType, identifier, setIdentifier);
        }

        if (several) {
            if (!isRadixPrimitive) {
                // JSET_INDEX
                acceptor.acceptSeveralPropArraySetter(sType, identifier, setIdentifier, isobj, isSubstGroup);

                if (_useJava15 && !isJavaPrimitive(javaType)) {
                    acceptor.acceptListSetter15AssignList(sType, identifier, setIdentifier, isobj, isSubstGroup);
                }
            }
            acceptor.acceptSeveralPropArrayElementSetter(sType, prop, identifier, setIdentifier, jtargetType, jSet, javaType);

            if (!xmltype && !isRadixPrimitive) {
                acceptor.acceptSeveralPropArrayXmlSetter(sType, identifier);
                acceptor.acceptSeveralPropArrayElementXmlSetter(sType, identifier, setIdentifier);
            }

            if (nillable && !isRadixPrimitive) {
                acceptor.acceptSeveralPropSetNull(sType, identifier, setIdentifier);
            }

            if (!xmltype) {
                acceptor.acceptSeveralPropElementInsertion(sType, identifier, setIdentifier, jtargetType, isSubstGroup, jSet, javaType);
                acceptor.acceptSeveralPropElementAddition(sType, identifier, jtargetType, javaType, jSet);
            }

            acceptor.acceptSeveralPropNewElementInsertion(sType, identifier, setIdentifier, isSubstGroup);
            acceptor.acceptSeveralPropNewElementAddition(sType, identifier);
            acceptor.acceptSeveralPropElementRemove(sType, identifier, setIdentifier);
        }
    }

    @SuppressWarnings("unchecked")
    static void getTypeName(Class c, StringBuilder sb) {
        int arrayCount = 0;
        while (c.isArray()) {
            c = c.getComponentType();
            arrayCount++;
        }

        sb.append(c.getName());

        for (int i = 0; i < arrayCount; i++) {
            sb.append("[]");
        }
    }

    private class DefaultTypeImplAcceptor implements XBeansTypeImplAcceptor {

        private SchemaType sType;
        private String shortName;
        private boolean isInner;

        @Override
        public void beginType(SchemaType sType, String shortName, boolean isInner) throws IOException {
            this.sType = sType;
            this.shortName = shortName;
            this.isInner = isInner;
        }

        @Override
        public void acceptTypeJavaDoc() throws IOException {
            printInnerTypeJavaDoc(sType);
        }

        @Override
        public void acceptClassStart(String baseClass, String[] interfaces) throws IOException {
            StringBuilder ifacesStr = new StringBuilder();
            boolean first = true;
            for (String iface : interfaces) {
                if (first) {
                    first = false;
                } else {
                    ifacesStr.append(", ");
                }
                ifacesStr.append(iface);
            }
            emit("public " + (isInner ? "static " : "") + "class " + shortName + " extends " + baseClass + " implements " + ifacesStr.toString());
            startBlock();
            emit("private static final long serialVersionUID = 0;");
        }

        @Override
        public void acceptConstructor() throws IOException {
            emit("");
            emit("public " + shortName + "(org.apache.xmlbeans.SchemaType sType)");
            startBlock();

            RadixType radixItemType = null;
            if (sType.getSimpleVariety() == SchemaType.LIST) {
                radixItemType = getRadixType(sType.getListItemType());
            }
            if (radixItemType instanceof RadixIntEnum || radixItemType instanceof RadixStrEnum || radixItemType instanceof RadixSafeStrEnum) {
                emit("super(" + radixItemType.getFullJavaClassName() + ".class, sType" + (sType.getSimpleVariety() == SchemaType.NOT_SIMPLE ? "" : ", " + !sType.isSimpleType()) + ");");
            } else {
                emit("super(sType" + (sType.getSimpleVariety() == SchemaType.NOT_SIMPLE ? "" : ", " + !sType.isSimpleType()) + ");");
            }
            endBlock();

            if (sType.getSimpleVariety() != SchemaType.NOT_SIMPLE) {
                emit("");
                emit("protected " + shortName + "(org.apache.xmlbeans.SchemaType sType, boolean b)");
                startBlock();
                if (radixItemType instanceof RadixIntEnum || radixItemType instanceof RadixStrEnum || radixItemType instanceof RadixCharEnum || radixItemType instanceof RadixSafeStrEnum) {
                    emit("super(" + radixItemType.getFullJavaClassName() + ".class, sType, b);");
                } else {
                    emit("super(sType, b);");
                }
                endBlock();
            }
        }

        @Override
        public void acceptExtensionImplMethodDecl(String handler, MethodSignature method) throws IOException {
            printJavaDoc("Implementation method for interface " + handler);
            printInterfaceMethodDecl(method);
        }

        @Override
        public void acceptExtensionImplMethodImpl(String handler, MethodSignature method) throws IOException {
            startBlock();
            printInterfaceMethodImpl(handler, method);
            endBlock();
        }

        @Override
        public void endType() throws IOException {
            endBlock();
        }

        @Override
        public XBeansTypeImplAcceptor createInnerType() {
            return new DefaultTypeImplAcceptor();
        }

        @Override
        public XBeansPropImplAcceptor createPropAcceptor() {
            return new DefaultPropImplAcceptor();
        }
    }

    void printExtensionImplMethods(SchemaType sType, XBeansTypeImplAcceptor acceptor) throws IOException {
        SchemaTypeImpl sImpl = getImpl(sType);
        if (sImpl == null) {
            return;
        }
        InterfaceExtension[] exts = sImpl.getInterfaceExtensions();
        Arrays.sort(exts, new Comparator<InterfaceExtension>() {
            @Override
            public int compare(InterfaceExtension o1, InterfaceExtension o2) {
                return o1.getStaticHandler().compareTo(o2.getStaticHandler());
            }
        });
        if (exts != null) {
            for (int i = 0; i < exts.length; i++) {
                InterfaceExtension.MethodSignature[] methods = exts[i].getMethods();
                Arrays.sort(methods, new Comparator<MethodSignature>() {
                    @Override
                    public int compare(MethodSignature o1, MethodSignature o2) {
                        return o1.toString().compareTo(o2.toString());
                    }
                });
                if (methods != null) {
                    for (int j = 0; j < methods.length; j++) {
                        acceptor.acceptExtensionImplMethodDecl(exts[i].getStaticHandler(), methods[j]);
                        acceptor.acceptExtensionImplMethodImpl(exts[i].getStaticHandler(), methods[j]);
                    }
                }
            }
        }
    }

    void printInnerTypeImpl(SchemaType sType, SchemaTypeSystem system, boolean isInner, XBeansTypeImplAcceptor acceptor) throws IOException {
        String shortName = sType.getShortJavaImplName();

        acceptor.beginType(sType, shortName, isInner);
        acceptor.acceptTypeJavaDoc();
        startClass(sType, isInner, acceptor);
        acceptor.acceptConstructor();
        printExtensionImplMethods(sType, acceptor);

        if (!sType.isSimpleType()) {
            SchemaProperty[] properties;

            if (sType.getContentType() == SchemaType.SIMPLE_CONTENT) {
                // simple content types impls derive directly from "holder" impls
                // in order to handle the case (for ints or string enums e.g.) where
                // there is a simple type restriction.  So property getters need to
                // be implemented "from scratch" for each derived complex type

                properties = sType.getProperties();
            } else {
                // complex content type implementations derive from base type impls
                // so derived property impls can be reused

                properties = getDerivedProperties(sType);
            }

            Map<QName, String[]> qNameMap = printStaticFields(properties);

            for (int i = 0; i < properties.length; i++) {
                SchemaProperty prop = properties[i];
                XBeansPropImplAcceptor propAcceptor = acceptor.createPropAcceptor();
                QName name = prop.getName();
                String xmlType = xmlTypeForProperty(prop);
                String propName = prop.getJavaPropertyName();
                propAcceptor.beginProperty(prop, name, propName, propName + "Array", propName + "List", javaTypeForProperty(prop), xmlTypeForProperty(prop), prop.isAttribute(), prop.extendsJavaArray(), propIsDeprecated(prop));
                printGetterImpls(
                        shortName,
                        prop,
                        prop.getJavaPropertyName(),
                        prop.getJavaTypeCode(),
                        javaTypeForProperty(prop),
                        xmlType,
                        prop.hasNillable() != SchemaProperty.NEVER,
                        prop.extendsJavaOption(),
                        prop.extendsJavaArray(),
                        prop.extendsJavaSingleton(),
                        xmlTypeForPropertyIsUnion(prop),
                        getIdentifier(qNameMap, name),
                        getSetIdentifier(qNameMap, name), propAcceptor);

                if (!prop.isReadOnly()) {
                    printSetterImpls(
                            name,
                            prop,
                            prop.getJavaPropertyName(),
                            prop.getJavaTypeCode(),
                            xmlType,
                            prop.hasNillable() != SchemaProperty.NEVER,
                            prop.extendsJavaOption(),
                            prop.extendsJavaArray(),
                            prop.extendsJavaSingleton(),
                            xmlTypeForPropertyIsUnion(prop),
                            getIdentifier(qNameMap, name),
                            getSetIdentifier(qNameMap, name),
                            sType, propAcceptor);
                }
                propAcceptor.endProperty();
            }
        }

        printNestedTypeImpls(sType, system, acceptor);
        acceptor.endType();
    }

    private SchemaProperty[] getDerivedProperties(SchemaType sType) {
        // We have to see if this is redefined, because if it is we have
        // to include all properties associated to its supertypes
        QName name = sType.getName();
        if (name != null && name.equals(sType.getBaseType().getName())) {
            SchemaType sType2 = sType.getBaseType();
            // Walk all the redefined types and record any properties
            // not present in sType, because the redefined types do not
            // have a generated class to represent them
            SchemaProperty[] props = sType.getDerivedProperties();
            Map<QName, SchemaProperty> propsByName = new LinkedHashMap<>();
            for (int i = 0; i < props.length; i++) {
                propsByName.put(props[i].getName(), props[i]);
            }
            while (sType2 != null && name.equals(sType2.getName())) {
                props = sType2.getDerivedProperties();
                for (int i = 0; i < props.length; i++) {
                    if (!propsByName.containsKey(props[i].getName())) {
                        propsByName.put(props[i].getName(), props[i]);
                    }
                }
                sType2 = sType2.getBaseType();
            }
            return propsByName.values().toArray(new SchemaProperty[0]);
        } else {
            return sType.getDerivedProperties();
        }
    }

    void printInterfaceMethodDecl(InterfaceExtension.MethodSignature method) throws IOException {
        StringBuilder decl = new StringBuilder(60);

        decl.append("public ").append(method.getReturnType());
        decl.append(' ').append(method.getName()).append('(');

        String[] paramTypes = method.getParameterTypes();
        for (int i = 0; i < paramTypes.length; i++) {
            if (i != 0) {
                decl.append(", ");
            }
            decl.append(paramTypes[i]).append(" p").append(i);
        }

        decl.append(')');

        String[] exceptions = method.getExceptionTypes();
        for (int i = 0; i < exceptions.length; i++) {
            decl.append(i == 0 ? " throws " : ", ").append(exceptions[i]);
        }
        emit(decl.toString());
    }

    void printInterfaceMethodImpl(String handler, InterfaceExtension.MethodSignature method) throws IOException {
        StringBuilder impl = new StringBuilder(60);

        if (!method.getReturnType().equals("void")) {
            impl.append("return ");
        }
        impl.append(handler).append('.').append(method.getName()).append("(this");

        String[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            impl.append(", p").append(i);
        }
        impl.append(");");

        emit(impl.toString());
    }

    void printNestedTypeImpls(SchemaType sType, SchemaTypeSystem system, XBeansTypeImplAcceptor acceptor) throws IOException {
        boolean redefinition = sType.getName() != null
                && sType.getName().equals(sType.getBaseType().getName());
        while (sType != null) {
            SchemaType[] anonTypes = sType.getAnonymousTypes();
            for (int i = 0; i < anonTypes.length; i++) {
                if (anonTypes[i].isSkippedAnonymousType()) {
                    printNestedTypeImpls(anonTypes[i], system, acceptor);
                } else {
                    printInnerTypeImpl(anonTypes[i], system, true, acceptor.createInnerType());
                }
            }
            // For redefinition by extension, go ahead and print the anonymous
            // types in the base
            if (!redefinition
                    || (sType.getDerivationType() != SchemaType.DT_EXTENSION && !sType.isSimpleType())) {
                break;
            }
            sType = sType.getBaseType();
        }
    }
}
