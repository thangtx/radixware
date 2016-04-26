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

package org.radixware.kernel.explorer.editors.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlCursor.TokenType;

public class XElementTools {

    public static final int OCCURS = 1;
    public static final int NO_CHILDS = -1;

    public static SchemaLocalAttribute getAttrDef(String name, SchemaType type) {
        if (type != null) {
            SchemaLocalAttribute[] all = getAttributesDefs(type);
            for (SchemaLocalAttribute i : all) {
                String defName = i.getName().getLocalPart();
                if (defName.equals(name)) {
                    return i;
                }
            }
        }
        return null;
    }

    public static SchemaLocalAttribute[] getAttributesDefs(SchemaType type) {
//        if (doc != null) {
//            SchemaType type = doc.schemaType();
            if (type != null) {
                SchemaAttributeModel model = type.getAttributeModel();
                SchemaLocalAttribute[] attrs = model != null ? model.getAttributes() : new SchemaLocalAttribute[0];
                return attrs;
//            }
        }

        return new SchemaLocalAttribute[0];
    }

    public static ArrayList<XmlObject> getCurrentAttributes(XmlObject doc) {
        ArrayList<XmlObject> all = new ArrayList<>();
        XmlCursor c = doc.newCursor();
        c.toNextToken();
        while (c.currentTokenType().intValue() != TokenType.INT_END
                && c.currentTokenType().intValue() != TokenType.INT_START) {
            int token = c.currentTokenType().intValue();
            if (token == TokenType.INT_ATTR) {
                XmlObject a = c.getObject();
                all.add(a);
            }
            c.toNextToken();
        }
        return all;
    }

    public static XmlObject getAttribute(XmlObject doc, String local) {
        XmlCursor c = doc.newCursor();
        c.toNextToken();
        while (c.currentTokenType().intValue() != TokenType.INT_END
                && c.currentTokenType().intValue() != TokenType.INT_START) {
            int token = c.currentTokenType().intValue();
            if (token == TokenType.INT_ATTR) {
                XmlObject a = c.getObject();
                if (a.getDomNode().getLocalName().equals(local)) {
                    return a;
                }
            }
            c.toNextToken();
        }
        return null;
    }

    public static int getAttributeIndex(XmlObject doc, String local) {
        final XmlCursor cursor = doc.newCursor();
        try{
            cursor.toNextToken();
            int index = 0;
            while (cursor.currentTokenType().intValue() != TokenType.INT_END
                    && cursor.currentTokenType().intValue() != TokenType.INT_START) {
                int token = cursor.currentTokenType().intValue();
                if (token == TokenType.INT_ATTR) {
                    XmlObject a = cursor.getObject();
                    if (a.getDomNode().getLocalName().equals(local)) {
                        return index;
                    }
                    if (isXsiNilAttr(cursor)){
                        cursor.toNextToken();
                        continue;
                    }
                }
                index++;
                cursor.toNextToken();
            }
            return NO_CHILDS;
        }finally{
            cursor.dispose();
        }
    }
    
    public static boolean isXsiNilAttr(XmlCursor cursor){
        return cursor.currentTokenType().isAttr()
               && "nil".equals(cursor.getName().getLocalPart())
               && "xsi".equals(cursor.getName().getPrefix());
    }
    
    public static void checkXsiNSDeclaration(final XmlObject node){
        final XmlCursor cursor = node.newCursor();
        try{
            cursor.toNextToken();
            cursor.push();
            while (cursor.currentTokenType().intValue() != TokenType.INT_END
                    && cursor.currentTokenType().intValue() != TokenType.INT_START) {
                final int token = cursor.currentTokenType().intValue();
                if ( token == TokenType.INT_ATTR) {
                    final String prefix = cursor.getName().getPrefix();
                    if ("xsi".equals(prefix)){
                        return;
                    }
                }
                cursor.toNextToken();
            }
            cursor.pop();
            while (cursor.currentTokenType().intValue() != TokenType.INT_END
                    && cursor.currentTokenType().intValue() != TokenType.INT_START) {
                final int token = cursor.currentTokenType().intValue();
                if (token == TokenType.INT_NAMESPACE
                    && "xsi".equals(cursor.getName().getLocalPart())){
                    cursor.removeXml();
                    break;
                }
                cursor.toNextToken();
            }
        }finally{
            cursor.dispose();
        }
    }
    
    public static int getItemsCount(final XmlCursor cursor) {
        cursor.push();
        int result = 0;
        cursor.toNextToken();
        while (!cursor.isEnddoc() && !cursor.isEnd()) {
            if (isItemToken(cursor)) {
                result++;
            }
            if (cursor.isStart()) {
                cursor.toEndToken();
            }
            cursor.toNextToken();
        }
        cursor.pop();
        return result;
    }
    
    public static boolean hasChildNodes(final XmlObject node){
        final XmlCursor cursor = node.newCursor();
        try{
            cursor.toNextToken();
            while (!cursor.isEnddoc() && !cursor.isEnd()) {
                if (isItemToken(cursor)) {
                    return true;
                }
                cursor.toNextToken();
            }
            return false;
        }finally{
            cursor.dispose();
        }
    }
    
    private static boolean isItemToken(final XmlCursor cursor) {
        if (XElementTools.isXsiNilAttr(cursor)){
            return false;
        }else{
            final XmlCursor.TokenType tokenType = cursor.currentTokenType();
            return tokenType.isStart() || tokenType.isAttr() || tokenType.isProcinst() || tokenType.isComment();
        }
    }    

    public static XmlObject getParentNode(XmlObject doc) {
        if (doc != null) {
            XmlCursor c = doc.newCursor();
            if (c.toParent()) {
                return c.getObject();
            }
        }
        return null;
    }

    public static SchemaParticle[] takeContentModel(SchemaType type) {
//        if (doc != null) {
//            SchemaType type = doc.schemaType();
            SchemaParticle part = type.getContentModel();
            if (part != null) {
                if (part.getParticleType() == SchemaParticle.ELEMENT) {
                    SchemaParticle[] result = {part};
                    return result;
                } else {
                    if (part.getParticleChildren() != null) {
                        List<SchemaParticle> listedChildren = getListedElements(part.getParticleChildren());
                        SchemaParticle[] result = new SchemaParticle[listedChildren.size()];
                        System.arraycopy(listedChildren.toArray(), 0, result, 0, result.length);
                        return result;
                    }
                }
            }
//        }
        return null;
    }

    public static List<SchemaParticle> getListedElementsOfChoice(SchemaParticle part) {
        List<SchemaParticle> result = new ArrayList<>();
        for (SchemaParticle p : part.getParticleChildren()) {
            if (p.getParticleType() == SchemaParticle.ALL
                    || p.getParticleType() == SchemaParticle.SEQUENCE
                    || p.getParticleType() == SchemaParticle.CHOICE) {
                result.addAll(getListedElementsOfChoice(p));
            } else {
                result.add(p);
            }
        }

        return result;
    }

    private static List<SchemaParticle> getListedElements(SchemaParticle[] part) {
        List<SchemaParticle> result = new ArrayList<>();
        for (SchemaParticle p : part) {
            if (p.getParticleType() == SchemaParticle.ALL
                    || p.getParticleType() == SchemaParticle.SEQUENCE) {
                result.addAll(getListedElements(p.getParticleChildren()));
            } else {
                result.add(p);
            }
        }

        return result;
    }

    public static int getMaxOccurs(SchemaType type, SchemaParticle particle) {
        int ownOccurs = particle.getIntMaxOccurs();
        int res = -1;
        if (type != null && particle.getParticleType() != SchemaParticle.ELEMENT) {
            SchemaParticle model = type.getContentModel();
            if (model != null) {
                if (model.equals(particle)) {
                    return ownOccurs;
                } else {
                    res = findChildMaxOccurs(type, model, particle);
                }
            }
        }
        return ownOccurs < res ? res : ownOccurs;
    }

    private static int findChildMaxOccurs(SchemaType type, SchemaParticle parent, SchemaParticle particle) {
        if (parent.getParticleChildren() != null && parent.getParticleChildren().length > 0) {
            for (SchemaParticle p : parent.getParticleChildren()) {
                if (p.equals(particle)) {
                    int parentMax = parent.getIntMaxOccurs();
                    if (parentMax == particle.getIntMaxOccurs()) {
                        return getMaxOccurs(type, parent);
                    } else {
                        return parentMax;
                    }
                } else {
                    if (p.getParticleType() != SchemaParticle.ELEMENT) {
                        int res = findChildMaxOccurs(type, p, particle);
                        if (res != -1) {
                            return res;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static int getContentModelType(SchemaType type) {
//        if (doc != null) {
//            SchemaType type = doc.schemaType();
            SchemaParticle part = type.getContentModel();
            if (part != null) {
                return part.getParticleType();
            }
//        }
        return 0;//none of known particles types
    }

    public static SchemaParticle getModelByName(SchemaParticle[] model, String iName) {
        int i = 0;
        boolean stop = false;
        if (model != null && iName != null) {
            while (i <= model.length - 1 && !stop) {
                String m = "";
                if (model[i].getParticleType() == SchemaParticle.ELEMENT) {
                    m = model[i].getName().getLocalPart();
                } else {
                    m = getTitleForSequence(model[i]);
                }
                if (m.equals(iName)) {
                    stop = true;
                } else {
                    i++;
                }
            }
            if (i <= model.length - 1) {
                return model[i];
            }
        }
        return null;
    }

    public static int getModelIndexByName(SchemaParticle[] model, String iName) {
        int i = 0;
        boolean stop = false;
        while (i <= model.length - 1 && !stop) {
            String m = "";
            if (model[i].getParticleType() == SchemaParticle.ELEMENT) {
                m = model[i].getName().getLocalPart();
            } else {
                m = getTitleForSequence(model[i]);
            }
            if (m.equals(iName)) {
                stop = true;
            } else {
                i++;
            }
        }
        if (i <= model.length - 1) {
            return i;
        }
        return NO_CHILDS;
    }

    public static String getTitleForSequence(SchemaParticle p) {
        SchemaParticle[] childs = p.getParticleChildren();
        if (childs != null) {
            String sep = "";
            if (p.getParticleType() == SchemaParticle.CHOICE) {
                sep = "|";
            } else {
                sep = ",";
            }
            if (childs.length == 1) {
                sep = "";
            }
            final StringBuilder titleBuilder = new StringBuilder();
            for (SchemaParticle ch : childs) {
                if (ch.getParticleType() == SchemaParticle.ELEMENT) {
                    titleBuilder.append(ch.getName().getLocalPart());
                    titleBuilder.append(sep);
                } else {
                    titleBuilder.append("{");
                    titleBuilder.append(getTitleForSequence(ch));
                    titleBuilder.append("}");
                }
            }
            titleBuilder.deleteCharAt(titleBuilder.length()-1);
            return titleBuilder.toString();
        }
        return "";
    }

    public static boolean allowsWildcards(SchemaType type) {
//        SchemaType type = node.schemaType();
        if (type != null
                && type.getContentModel() != null) {
            SchemaParticle[] model = type.getContentModel().getParticleChildren();
            if (model != null) {
                for (SchemaParticle p : model) {
                    if (p.getParticleType() == SchemaParticle.WILDCARD) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean allowsAnyAsChild(final SchemaType type) {
        if (type!=null){
            if (type.getContentModel() != null) {
                final SchemaParticle[] model = type.getContentModel().getParticleChildren();
                if (model != null) {
                    for (SchemaParticle p : model) {
                        if (p.getParticleType() == SchemaParticle.WILDCARD) {
                            return true;
                        }
                    }
                } else if (type.getElementProperties().length > 0
                        && type.getElementProperties()[0].getType().getBuiltinTypeCode() == SchemaType.BTC_ANY_TYPE
                        || type.getBuiltinTypeCode() == SchemaType.BTC_ANY_TYPE) {
                    return true;
                }
            } else if (type.getContentModel() == null) {
                return type.getElementProperties().length > 0
                        && type.getElementProperties()[0].getType().getBuiltinTypeCode() == SchemaType.BTC_ANY_TYPE
                        || type.getBuiltinTypeCode() == SchemaType.BTC_ANY_TYPE;
            }
        }
        return false;
    }

    public static boolean checkForModelMembers(XTreeTag node, SchemaParticle[] model) {
        if (node != null
                && model != null) {
            XTreeTag parentNode = (XTreeTag) node.parent();
            if (parentNode != null) {
                int index = parentNode.indexOfChild(node) - 1;
                while (index != 0
                        && !(parentNode.child(index) instanceof XTreeAttribute)) {
                    XTreeTag child = (XTreeTag) parentNode.child(index);
                    String local = child.getNode().getDomNode().getLocalName();
                    if (__isModelMember(local, model)) {
                        return true;
                    } else {
                        index--;
                    }
                }
            }
        }
        return false;
    }

    private static boolean __isModelMember(String local, SchemaParticle[] model) {
        if (model != null
                && local != null
                && !local.isEmpty()) {
            for (SchemaParticle p : model) {
                if (p.getParticleType() == SchemaParticle.ELEMENT) {
                    if (p.getName().getLocalPart().equals(local) == true) {
                        return true;
                    }
                } else {
                    if (__isModelMember(local, p.getParticleChildren()) == true) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
