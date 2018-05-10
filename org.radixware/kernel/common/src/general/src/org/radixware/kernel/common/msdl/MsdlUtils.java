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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.w3c.dom.Node;

public class MsdlUtils {
    
    public static class SchemeInternalVisitor implements IVisitor {

        public AbstractFieldModel target = null;

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof MsdlField) {
                target = ((MsdlField) radixObject).getFieldModel();
            } else if (radixObject instanceof AbstractFieldModel) {
                target = (AbstractFieldModel) radixObject;
            }
        }
    }

    public static class SchemeInternalVisitorProvider extends VisitorProvider {

        String path = "/";
        RootMsdlScheme context = null;
        EFieldType type = EFieldType.STRUCTURE;

        public SchemeInternalVisitorProvider(String path, RootMsdlScheme context, EFieldType type) {
            this.path = path;
            this.context = context;
            this.type = type;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (path == null) {
                return false;
            }
            String[] componentsArr = path.split("/");
            List<String> components = new LinkedList<>();
            for (String s : componentsArr) {
                if (!s.isEmpty()) {
                    components.add(s);
                }
            }

            ListIterator<String> it = components.listIterator(components.size());
            RadixObject cur = radixObject;
            boolean fail = false;
            while (it.hasPrevious()) {
                String component = it.previous();
                if (component.equals(cur.getName())) {
                    cur = cur.getContainer();
                } else {
                    fail = true;
                    break;
                }
            }
            return !fail;
        }

        @Override
        public boolean isContainer(RadixObject object) {
            boolean isMsdlField = object instanceof MsdlField;
            boolean isFieldModel = object instanceof AbstractFieldModel;
            boolean isFieldContainer = object instanceof MsdlStructureFields || object instanceof MsdlVariantFields;

            if (isMsdlField) {
                MsdlField f = (MsdlField) object;
                return f.getFieldModel().getType() == type;
            }
            if (isFieldModel) {
                return ((AbstractFieldModel) object).getType() == type;
            }
            if (isFieldContainer) {
                return true;
            }

            return false;
        }
    }

    private static class SchemaElement {

        public final String type;
        public final QName name;

        public SchemaElement(String type, QName name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 31 * hash + Objects.hashCode(this.type);
            hash = 31 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SchemaElement other = (SchemaElement) obj;
            if (!Objects.equals(this.type, other.type)) {
                return false;
            }
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "SchemaElement{" + "type=" + type + ", name=" + name + '}';
        }
    }

    private static class HtmlTableRow {

        private final StringBuilder rowString = new StringBuilder("<tr>");
        private final int requiredColumnCount;

        private int currentColumnCount = 0;

        public HtmlTableRow(int requiredColumnCount) {
            this.requiredColumnCount = requiredColumnCount;
        }

        public void addColumn(Object val) {
            if (currentColumnCount < requiredColumnCount) {
                rowString.append("<td>").append(val.toString()).append("</td>");
                currentColumnCount++;
            }

            if (currentColumnCount >= requiredColumnCount && !rowString.toString().endsWith("</tr>")) {
                rowString.append("</tr>");
            }
        }

        @Override
        public String toString() {
            if (currentColumnCount < requiredColumnCount) {
                StringBuilder rowCopy = new StringBuilder(rowString);
                for (int i = currentColumnCount; i < requiredColumnCount; i++) {
                    rowCopy.append("<td>").append("</td>");
                }
                rowCopy.append("</tr>");
                return rowCopy.toString();
            } else {
                return rowString.toString();
            }
        }
    }

    private static final String BASE_PREF_CHAR = "&gt;";

    public static String msdlMessageToHtmlTable(XmlObject message) throws WrongFormatError {
        return msdlMessageToHtmlTable(message, null);
    }

    public static String msdlMessageToHtmlTable(XmlObject message, ClassLoader loader) throws WrongFormatError {
        if (message == null) {
            return "";
        }

        XmlObject msdlMessage = prepareMsdlMessage(message, loader);

        Map<SchemaElement, String> comments = new HashMap<>();

        StringBuilder html = new StringBuilder();
        html.append("<table>");
        html.append("<tr>");
        html.append("<th>").append("Name").append("</th>");
        html.append("<th>").append("Value").append("</th>");
        html.append("<th>").append("Description").append("</th>");
        html.append("</tr>");

        StringBuilder prefix = new StringBuilder();

        XmlCursor msgCursor = msdlMessage.newCursor();
        try {
            HtmlTableRow tr = null;
            String comment = "";
            while (msgCursor.hasNextToken()) {
                XmlCursor.TokenType tokenType = msgCursor.toNextToken();
                switch (tokenType.intValue()) {
                    case XmlCursor.TokenType.INT_STARTDOC:
                    case XmlCursor.TokenType.INT_START: {
                        String name = msgCursor.getName().getLocalPart();

                        XmlObject obj = msgCursor.getObject();
                        SchemaElement currentElement = new SchemaElement(obj.schemaType().toString(), msgCursor.getName());
                        processParticles(obj, comments);
                        if ("MessageInstance".equals(name)) {
                            break;
                        }
                        prefix.append(BASE_PREF_CHAR);

                        if (tr != null) {
                            if (tr.currentColumnCount < 2) {
                                tr.addColumn("");
                            }
                            tr.addColumn(comment);
                            html.append(tr);
                        }
                        tr = new HtmlTableRow(3);

                        comment = comments.containsKey(currentElement) ? comments.get(currentElement) : "";

                        tr.addColumn(prefix.toString() + name);

                        break;
                    }
                    case XmlCursor.TokenType.INT_TEXT: {
                        if (tr != null) {
                            tr.addColumn(msgCursor.getTextValue());
                        }
                        break;
                    }
                    case XmlCursor.TokenType.INT_END: {
                        if (prefix.length() > 0) {
                            int prefStart = prefix.lastIndexOf(BASE_PREF_CHAR);
                            int prefEnd = prefStart + BASE_PREF_CHAR.length();
                            prefix.delete(prefStart, prefEnd);
                        }
                        break;
                    }
                    case XmlCursor.TokenType.INT_ENDDOC: {
                        if (tr != null) {
                            if (tr.currentColumnCount < 2) {
                                tr.addColumn("");
                            }
                            tr.addColumn(comment);
                            html.append(tr);
                        }
                        break;
                    }
                }
            }
        } finally {
            msgCursor.dispose();
        }
        html.append("</table>");

        return html.toString();
    }

    private static void processParticles(XmlObject obj, Map<SchemaElement, String> comments) {
        SchemaType objSchemaType = obj.schemaType();
        if (objSchemaType.getContentType() == SchemaType.ELEMENT_CONTENT
                || objSchemaType.getContentType() == SchemaType.MIXED_CONTENT) {
            SchemaParticle particle = objSchemaType.getContentModel();
            navigateParticle(particle, comments);
        }
    }

    private static void navigateParticle(SchemaParticle particle, Map<SchemaElement, String> comments) {
        switch (particle.getParticleType()) {
            case SchemaParticle.ALL:
            case SchemaParticle.CHOICE:
            case SchemaParticle.SEQUENCE:
                SchemaParticle[] children = particle.getParticleChildren();
                for (int i = 0; i < children.length; i++) {
                    navigateParticle(children[i], comments);
                }
                break;
            case SchemaParticle.ELEMENT:
                SchemaElement element = new SchemaElement(particle.getType().toString(), particle.getName());
                String comment = getElementComment((SchemaLocalElement) particle);

                comments.put(element, comment);
                break;
            default:
        }
    }

    private static String getElementComment(SchemaLocalElement element) {
        SchemaAnnotation annotation = element.getAnnotation();
        StringBuilder commentBuilder = new StringBuilder();
        if (annotation != null && annotation.getUserInformation() != null) {
            boolean isFirst = true;
            for (XmlObject userInfo : annotation.getUserInformation()) {
                Node userInfoNode = userInfo.getDomNode();
                if (userInfoNode != null && "documentation".equals(userInfoNode.getLocalName())) {
                    if (!isFirst) {
                        commentBuilder.append("\n");
                    } else {
                        isFirst = false;
                    }

                    XmlCursor tmpCursor = userInfo.newCursor();
                    commentBuilder.append(tmpCursor.getTextValue());
                    tmpCursor.dispose();
                }
            }
        }

        return commentBuilder.toString().replace("\n", "<br>");
    }

    private static XmlObject prepareMsdlMessage(XmlObject message, ClassLoader loader) throws WrongFormatError {
        if (loader == null) {
            return message;
        }

        XmlObject messageInstanceObj = findMessageInstanceObject(message);
        if (messageInstanceObj == null) {
            throw new WrongFormatError("MessageInstance element is expected in MSDL-scheme");
        }

        SchemaTypeLoader typeLoader = XmlBeans.typeLoaderForClassLoader(loader);
        if (typeLoader == null) {
            return messageInstanceObj.copy();
        }

        SchemaType messageInstanceType = typeLoader.findDocumentType(new QName(messageInstanceObj.getDomNode().getNamespaceURI(), "MessageInstance"));
        if (messageInstanceType == null) {
            return messageInstanceObj.copy();
        }

        if (message.schemaType() == messageInstanceType) {
            return message;
        }

        try {
            XmlOptions op = new XmlOptions().setSaveOuter();
            XmlObject preparedMessage = typeLoader.parse(messageInstanceObj.xmlText(op), messageInstanceType, null);

            return preparedMessage;
        } catch (XmlException ex) {
            throw new WrongFormatError("Given message does not match to type \"" + messageInstanceType.getFullJavaName() + "\"", ex);
        }
    }

    private static XmlObject findMessageInstanceObject(XmlObject message) {
        XmlCursor msgCursor = message.newCursor();
        try {
            while (msgCursor.hasNextToken()) {
                XmlCursor.TokenType tokenType = msgCursor.toNextToken();
                if (tokenType.isStart()) {
                    String name = msgCursor.getName().getLocalPart();
                    if ("MessageInstance".equals(name)) {
                        return msgCursor.getObject();
                    }
                }
            }
        } finally {
            msgCursor.dispose();
        }
        return null;
    }
}
