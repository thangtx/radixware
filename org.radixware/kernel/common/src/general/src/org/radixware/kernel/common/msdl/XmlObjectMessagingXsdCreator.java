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
package org.radixware.kernel.common.msdl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.xmlbeans.XmlCursor;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.radixware.schemas.msdl.AnyField;
import org.radixware.schemas.msdl.ChoiceField;
import org.radixware.schemas.msdl.ChoiceFieldVariant;
import org.radixware.schemas.msdl.Field;
import org.radixware.schemas.msdl.Message;
import org.radixware.schemas.msdl.MessageElementDocument;
import org.radixware.schemas.msdl.SequenceField;
import org.radixware.schemas.msdl.SimpleField;
import org.radixware.schemas.msdl.StrField;
import org.radixware.schemas.msdl.Structure;
import org.radixware.schemas.msdl.StructureField;

public class XmlObjectMessagingXsdCreator {

    public static void main(String[] args) {
        /*String[] args = new String[]{
         "http://schemas.radixware.org/msdlIso.xsd",
         "/tmp/schemPMdXAJ.msdl",
         "/tmp/schemzSxPL7.xsd" 
         };*/

        /*
         MessageInstanceDocument doc = null;
         doc = MessageInstanceDocument.Factory.newInstance();
         doc.addNewMessageInstance();
         XmlObjectMessagingHandler.getRootMsdlScheme(doc);
         */
        for (int i = 0; i < args.length; i++) {
            log(args[i]);
        }

        if (args.length < 1) {
            log("Product name not specified");
        }
        if (args.length < 2) {
            log("Input file not specified");
        }
        if (args.length < 3) {
            log("Output file not specified");
        }
        if (args.length < 4) {
            log("Definition Id not defined");
        }

        InputStream f;
        MessageElementDocument me;
        try {
            f = new FileInputStream(args[1]);
            me = MessageElementDocument.Factory.parse(f);
            XmlOptions options = new XmlOptions();
            ArrayList<XmlError> errors = new ArrayList<>();
            options.put(XmlOptions.ERROR_LISTENER, errors);
            if (!me.validate(options)) {
                for (XmlError error : errors) {
                    logError(error.toString());
                }
                return;
            }
            f.close();
            XmlObject o = createXSD(me.getMessageElement(), args[3]);
            FileOutputStream xmlObject = null;
            try {
                xmlObject = new FileOutputStream(args[2]);
                XmlOptions saveOptions = new XmlOptions();
                saveOptions.setCharacterEncoding("US-ASCII");
                log(args[1]);
                log("save schema....");
                o.save(xmlObject, saveOptions);
                log("done");
            } finally {
                if (xmlObject != null) {
                    try {
                        xmlObject.close();
                    } catch (IOException ex) {
                        Logger.getLogger(XmlObjectMessagingXsdCreator.class.getName()).log(Level.FINE, null, ex);
                    }
                }
            }

        } catch (XmlException | IOException ex) {
            Logger.getLogger(XmlObjectMessagingXsdCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void log(String message) {
        System.out.println(message);
    }

    private static void logError(String message) {
        System.err.println(message);
    }

    public interface DefinitionTypeResolver {

        String getEnumTypeName(String enumId);

        String getMsdlSchemeNs(String schemeId);
    }

    public static SchemaDocument createXSD(Message me, String id) throws XmlException {
        return createXSD(me, id, null);
    }

    public static SchemaDocument createXSD(Message me, String id, DefinitionTypeResolver typeResolver) throws XmlException {
        StructureField ss = me;

        String ns = me.getClassTargetNamespace();
        Map<String, String> ns2Prefix = new HashMap<>();

        String mainStructureTypes = getMainStructureTypes(ss, typeResolver, ns2Prefix);
        String mainStructure = getMainStructure(ss, typeResolver, ns2Prefix);

        StringBuilder nss = new StringBuilder();
        StringBuilder imports = new StringBuilder();
        for (Map.Entry<String, String> e : ns2Prefix.entrySet()) {
            nss.append(" xmlns:").append(e.getValue()).append("=\"").append(e.getKey()).append("\"");
            imports.append("\n<xs:import namespace=\"").append(e.getKey()).append("\"/> ");
        }
        String s
                = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<xs:schema xmlns:me=\"" + ns + "\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdl=\"http://schemas.radixware.org/msdl.xsd\"" + nss.toString() + " xmlns:this=\"" + ns + "\" targetNamespace=\"" + ns + "\" elementFormDefault=\"qualified\" attributeFormDefault=\"qualified\""
                + " xmlns:t=\"http://schemas.radixware.org/types.xsd\">"
                + "\n<xs:import namespace=\"http://schemas.radixware.org/types.xsd\" schemaLocation=\"types.xsd\"/> "
                + imports
                + getMainStructureEnums(ss, typeResolver)
                + mainStructureTypes
                + "\n   <xs:complexType name=\"MessageType\">"
                + "\n     <xs:annotation>"
                + "\n       <xs:documentation>" + id + "</xs:documentation>"
                + "\n     </xs:annotation>"
                + "\n     " + mainStructure
                + "\n   </xs:complexType>"
                + "\n   <xs:element name=\"MessageInstance\" type=\"me:MessageType\">"
                + "\n   </xs:element>"
                + "\n</xs:schema>";

        return SchemaDocument.Factory.parse(s);
    }

    static private String getMainStructure(StructureField field, DefinitionTypeResolver typeResolver, Map<String, String> ns2Prefix) {
        StringBuffer sb = new StringBuffer();
        getStructure(sb, field.getStructure(), typeResolver, 0, ns2Prefix);
        return sb.toString();
    }

    static private String getMainStructureEnums(StructureField field, DefinitionTypeResolver typeResolver) {
        StringBuffer sb = new StringBuffer();
        getStructureEnums(sb, field.getStructure(), typeResolver);
        return sb.toString();
    }

    static private String getMainStructureTypes(StructureField field, DefinitionTypeResolver typeResolver, Map<String, String> ns2Prefix) {
        StringBuffer sb = new StringBuffer();
        Structure structure = field.getStructure();
        int count = structure.sizeOfHeaderFieldArray();
        for (int i = 0; i < count; i++) {
            AnyField af = structure.getHeaderFieldArray(i);
            if (af.isSetStructure()) {
                getFieldStructure(sb, af.getStructure(), false, typeResolver, true, 0, ns2Prefix);
            }
        }

        count = structure.sizeOfFieldArray();
        for (int i = 0; i < count; i++) {
            AnyField af = structure.getFieldArray(i);
            if (af.isSetStructure()) {
                getFieldStructure(sb, af.getStructure(), false, typeResolver, true, 0, ns2Prefix);
            }
        }
        return sb.toString();
    }

    static private void getStructure(StringBuffer sb, Structure structure, DefinitionTypeResolver typeResolver, int level, Map<String, String> ns2Prefix) {

        sb.append("\n<xs:sequence>");

        int count = structure.sizeOfHeaderFieldArray();
        for (int i = 0; i < count; i++) {
            AnyField af = structure.getHeaderFieldArray(i);
            getField(sb, af, false, typeResolver, level, ns2Prefix);
        }

        count = structure.sizeOfFieldArray();
        for (int i = 0; i < count; i++) {
            AnyField af = structure.getFieldArray(i);
            getField(sb, af, false, typeResolver, level, ns2Prefix);
        }

        sb.append("\n</xs:sequence>");
        if (structure.isSetBitmap()) {
            sb.append("\n<xs:attribute name=\"Bitmap\" type=\"t:BinHex\"/>");
        }
    }

    static private void getStructureEnums(StringBuffer sb, Structure structure, DefinitionTypeResolver typeResolver) {

        int count = structure.sizeOfHeaderFieldArray();
        for (int i = 0; i < count; i++) {
            AnyField af = structure.getHeaderFieldArray(i);
            getFieldEnums(sb, af, typeResolver);
        }

        count = structure.sizeOfFieldArray();
        for (int i = 0; i < count; i++) {
            AnyField af = structure.getFieldArray(i);
            getFieldEnums(sb, af, typeResolver);
        }

    }

    @SuppressWarnings("boxing")
    static private void getField(StringBuffer sb, AnyField af, boolean unbounded, DefinitionTypeResolver typeResolver, int level, Map<String, String> ns2Prefix) {

        if (af.isSetInt()) {
            if (af.getInt().isSetEnumClassId()) {
                addFieldIntEnum(sb, unbounded, af.getInt());
            } else {
                addField(sb, "Int", unbounded, af.getInt());
            }
            return;
        }

        if (af.isSetNum()) {
            addField(sb, "Num", unbounded, af.getNum());
            return;
        }

        if (af.isSetStr()) {
            if (af.getStr().isSetEnumClassId()) {
                addFieldStrEnum(sb, unbounded, af.getStr(), typeResolver);
            } else {
                addField(sb, "Str", unbounded, af.getStr());
            }
            return;
        }

        if (af.isSetDateTime()) {
            addField(sb, "DateTime", unbounded, af.getDateTime());
            return;
        }

        if (af.isSetStructure()) {
            getFieldStructure(sb, af.getStructure(), unbounded, typeResolver, false, level, ns2Prefix);
            return;
        }

        if (af.isSetSequence()) {
            addFieldSequence(sb, af.getSequence(), unbounded, typeResolver, level, ns2Prefix);
            return;
        }

        if (af.isSetChoice()) {
            addChoiceField(sb, af.getChoice(), unbounded, typeResolver, level, ns2Prefix);
            return;
        }

        if (af.isSetBin()) {
            addField(sb, "BinHex", unbounded, af.getBin());
            return;
        }

        if (af.isSetBCH()) {
            addField(sb, "Str", unbounded, af.getBCH());
        }
    }

    @SuppressWarnings("boxing")
    static private void getFieldEnums(StringBuffer sb, AnyField af, DefinitionTypeResolver typeResolver) {

        if (af.isSetInt() && af.getInt().isSetEnumClassId()) {
            sb.append("\n<xs:simpleType name=\"")
                    .append(af.getInt().getName())
                    .append("Enum\" >\n<xs:annotation>   \n<xs:documentation>Enum ")
                    .append(af.getInt().getName())
                    .append("</xs:documentation>    \n<xs:appinfo source=\"http://schemas.radixware.org/types.xsd\">      \n<t:class>")
                    .append(af.getInt().getEnumClassId())
                    .append("</t:class>     \n</xs:appinfo>    \n</xs:annotation>\n <xs:restriction base=\"t:IntEnum\"/>\n</xs:simpleType>");
        }
        if (af.isSetStr() && af.getStr().isSetEnumClassId()) {
            sb.append("\n<xs:simpleType name=\"")
                    .append(af.getStr().getName())
                    .append("Enum\" >\n<xs:annotation>   \n<xs:documentation>Enum ")
                    .append(af.getStr().getName())
                    .append("</xs:documentation>    \n<xs:appinfo source=\"http://schemas.radixware.org/types.xsd\">      \n<t:class>")
                    .append(af.getStr().getEnumClassId())
                    .append("</t:class>     \n</xs:appinfo>    \n</xs:annotation>" + "\n <xs:restriction base=\"t:")
                    .append(typeResolver == null ? "Str" : typeResolver.getEnumTypeName(af.getStr().getEnumClassId()))
                    .append("Enum\"/>\n</xs:simpleType>");

        }
        if (af.isSetStructure()) {
            getStructureEnums(sb, af.getStructure().getStructure(), typeResolver);
        }

        if (af.isSetSequence()) {
            AnyField field = af.getSequence().getItem();
            if (field != null) {
                getFieldEnums(sb, field, typeResolver);
            }
        }

        if (af.isSetChoice()) {
            List<ChoiceFieldVariant> list = af.getChoice().getVariantList();
            if (list != null) {
                for (ChoiceFieldVariant item : list) {
                    if (item.getField() != null) {
                        getFieldEnums(sb, item.getField(), typeResolver);
                    }
                }
            }
        }

    }

    @SuppressWarnings("boxing")
    static private void getFieldStructure(StringBuffer sb, StructureField structure, boolean unbounded, DefinitionTypeResolver typeResolver, boolean writeType, int level, Map<String, String> ns2Prefix) {
        if (structure.getTemplateSchemeId() != null && structure.getTemplateFieldPath() != null) {
            if (writeType) {
                return;
            }
            String ns = typeResolver == null ? null : typeResolver.getMsdlSchemeNs(structure.getTemplateSchemeId());
            if (ns != null) {
                String prefix = ns2Prefix.get(ns);
                if (prefix == null) {
                    prefix = "template_" + ns2Prefix.size();
                    ns2Prefix.put(ns, prefix);
                }
                sb.append("\n   <xs:element name=\"").append(structure.getName()).append("\"");

                String typeName = structure.getTemplateFieldPath();
                if (typeName.startsWith("/")) {
                    typeName = typeName.substring(1);
                }

                sb.append(" type=\"").append(prefix).append(":").append(typeName).append("\"");

                if (!structure.getIsRequired()) {
                    sb.append(" minOccurs=\"0\"");
                }
                if (unbounded) {
                    sb.append(" maxOccurs=\"unbounded\"");
                }

                if (isNeedAnnotation(structure)) {
                    sb.append(">");
                    addAnnotation(sb, structure);
                    sb.append("\n   </xs:element>");
                } else {
                    sb.append("/>");
                }
//                sb.append("\n  <xs:complexType>\n   <xs:complexContent>\n");
//                sb.append("<xs:extension base=\"").append(prefix).append(":").append(typeName).append("\"/>");
//                sb.append("\n   </xs:complexContent>\n </xs:complexType>\n </xs:element>");
                return;
            }
        }
        if (writeType) {
            sb.append("<xs:complexType name=\"").append(structure.getName()).append("\">");
            getStructure(sb, structure.getStructure(), typeResolver, level + 1, ns2Prefix);
            sb.append("\n</xs:complexType>\n");
        } else {
            if (level == 0 && structure.isSetAbstract() && structure.getAbstract() == Boolean.TRUE) {
                return;
            }
            sb.append("\n   <xs:element name=\"").append(structure.getName()).append("\"");
            if (level == 0) {
                sb.append(" type=\"").append("this:").append(structure.getName()).append("\"");
            }
            if (!structure.getIsRequired()) {
                sb.append(" minOccurs=\"0\"");
            }
            if (unbounded) {
                sb.append(" maxOccurs=\"unbounded\"");
            }

            if (level > 0) {
                sb.append(">");
                addAnnotation(sb, structure);
                sb.append("\n <xs:complexType>");
                getStructure(sb, structure.getStructure(), typeResolver, level, ns2Prefix);
                sb.append("\n </xs:complexType>\n </xs:element>");
            } else {
                if (isNeedAnnotation(structure)) {
                    sb.append(">");
                    addAnnotation(sb, structure);
                    sb.append("\n   </xs:element>");
                } else {
                    sb.append("/>");
                }
//                sb.append(">\n  <xs:complexType>\n   <xs:complexContent>\n");
//                sb.append("<xs:extension base=\"").append("this").append(":").append(structure.getName()).append("\"/>");
//                sb.append("\n   </xs:complexContent>\n </xs:complexType>\n </xs:element>");
            }
        }

    }

    @SuppressWarnings("boxing")
    static private void addContent(StringBuffer sb, SimpleField f, boolean unbounded) {
        if (!f.getIsRequired()) {
            sb.append(" minOccurs=\"0\"");
        }
        if (unbounded) {
            sb.append(" maxOccurs=\"unbounded\"");
        }
        if (f.isSetIsNilable()) {
            if (f.getIsNilable()) {
                sb.append(" nillable=\"true\"");
            } else {
                sb.append(" nillable=\"false\"");
            }
        }
    }

    static private void addField(StringBuffer sb, String type, boolean unbounded, SimpleField f) {
        sb.append("\n   <xs:element name=\"").append(f.getName()).append("\" type=\"t:").append(type).append('"');
        boolean trullyUnbounded = unbounded;
        if (f.getIsFieldArray1() != null && f.getIsFieldArray1().booleanValue()) {
            trullyUnbounded = true;
        }
        addContent(sb, f, trullyUnbounded);

        if (isNeedAnnotation(f)) {
            sb.append(">");
            addAnnotation(sb, f);
            sb.append("\n   </xs:element>");
        } else {
            sb.append("/>");
        }
    }

    static private void addFieldStrEnum(StringBuffer sb, boolean unbounded, StrField f, DefinitionTypeResolver typeResolver) {
        sb.append("\n   <xs:element name=\"").append(f.getName()).append("\" type=\"me:").append(f.getName()).append("Enum\"");
        addContent(sb, f, unbounded);
        if (isNeedAnnotation(f)) {
            sb.append(">");
            addAnnotation(sb, f);
            sb.append("\n   </xs:element>");
        } else {
            sb.append("/>");
        }
    }

    static private void addFieldIntEnum(StringBuffer sb, boolean unbounded, SimpleField f) {
        sb.append("\n   <xs:element name=\"").append(f.getName()).append("\" type=\"me:").append(f.getName()).append("Enum\"");
        addContent(sb, f, unbounded);
        if (isNeedAnnotation(f)) {
            sb.append(">");
            addAnnotation(sb, f);
            sb.append("\n   </xs:element>");
        } else {
            sb.append("/>");
        }
    }

    @SuppressWarnings("boxing")
    static private void addChoiceField(StringBuffer sb, ChoiceField choice, boolean unbounded, DefinitionTypeResolver typeResolver, int level, Map<String, String> ns2Prefix) {
        sb.append("\n   <xs:element name=\"").append(choice.getName()).append('"');
        if (!choice.getIsRequired()) {
            sb.append(" minOccurs=\"0\"");
        }
        if (unbounded) {
            sb.append(" maxOccurs=\"unbounded\"");
        }
        sb.append(">");
        addAnnotation(sb, choice);
        sb.append("\n <xs:complexType>\n <xs:choice>");
        for (ChoiceFieldVariant f : choice.getVariantList()) {
            getField(sb, f.getField(), false, typeResolver, level + 1, ns2Prefix);
        }
        sb.append("\n </xs:choice>\n </xs:complexType>\n </xs:element>");
    }

    @SuppressWarnings("boxing")
    static private void addFieldSequence(StringBuffer sb, SequenceField f, boolean unbounded, DefinitionTypeResolver typeResolver, int level, Map<String, String> ns2Prefix) {
        sb.append("\n   <xs:element name=\"").append(f.getName()).append('"');
        if (!f.getIsRequired()) {
            sb.append(" minOccurs=\"0\"");
        }
        if (unbounded) {
            sb.append(" maxOccurs=\"unbounded\"");
        }
        sb.append(">");
        addAnnotation(sb, f);
        sb.append("\n <xs:complexType>\n <xs:sequence>");
        getField(sb, f.getItem(), true, typeResolver, level + 1, ns2Prefix);
        sb.append("\n </xs:sequence>\n </xs:complexType>\n </xs:element>");
    }

    static private boolean isNeedAnnotation(Field f) {
        return f.getComment() != null;
    }

    static private void addAnnotation(StringBuffer sb, Field f) {
        if (isNeedAnnotation(f)) {
            sb.append("\n      <xs:annotation>");
            sb.append("\n         <xs:documentation>");
            
            XmlCursor tmpCursor = f.getComment().newCursor();
            sb.append(StringEscapeUtils.escapeXml(tmpCursor.getTextValue()));
            tmpCursor.dispose();
            
            sb.append("</xs:documentation>");
            sb.append("\n      </xs:annotation>");
        }
    }
}
