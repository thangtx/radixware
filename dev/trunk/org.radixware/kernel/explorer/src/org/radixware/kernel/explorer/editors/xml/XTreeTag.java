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

import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import java.util.List;
import javax.xml.namespace.QName;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.env.Application;

class XTreeTag extends XTreeElement {
    
    private static final QName NIL_ATTRIBUTE_NAME = new QName("http://www.w3.org/2001/XMLSchema-instance", "nil", "xsi");

    private final boolean allowsAny;
    private boolean odd = false;
    private boolean firstChoice = true;
    private final SchemaType nativeSchemaType;    
    private XmlObject sourceNode;        

    public XTreeTag(final TreeWindow tw, 
                    final XmlObject n, 
                    final SchemaType alternative,
                    final XmlObject sourceNode) {
        super(tw);
        this.nativeSchemaType = alternative;
        this.sourceNode = sourceNode;
        node = n;
        allowsAny = XElementTools.allowsAnyAsChild(getSchemaType());
        initValEditor();
    }

    public XTreeTag(final TreeWindow tw, final XmlObject n) {
        this(tw, n, (SchemaType)null, null);
    }
    
    private void initValEditor(){
        final ValEditor editor = setupEditor();
        final boolean readonly = tw.getEditor().isReadOnlyMode();
        editor.setReadOnly(readonly || !mightHaveContent());
        setEditor(editor);
        udateTagName();
        setSizeHint(0, editor.sizeHint());  
    }

    @Override
    public boolean isExternalTypeSystem() {
        return nativeSchemaType != null;
    }

    @Override
    public final SchemaType getSchemaType() {
        return nativeSchemaType == null && node!=null ? node.schemaType() : nativeSchemaType;
    }

    @Override
    public void applyChanges() {
        if (sourceNode!=null){
            /*
             * Нужно переписать содержимое типизированного xml-узла node в редактируемый xml-документ
            */
            final XmlObject parentNode;
            final QName name;
            //Курсор для записи изменений в редактируемый xml-документ
            final XmlCursor targetCursor =  sourceNode.newCursor();
            try{
                //Сохранили имя изменяемого элемента
                name = targetCursor.getName();
                //Сохранили вышестоящий элемент в редактируемом xml-документе
                targetCursor.push();
                targetCursor.toParent();
                parentNode = targetCursor.getObject();
                targetCursor.pop();
                //Курсор для чтения содержимого типизированного узла
                final XmlCursor srcNodeCursor = node.newCursor();
                //Перезапись данных в узле изменяемого документа
                try{
                    targetCursor.removeXml();
                    srcNodeCursor.copyXml(targetCursor);
                }
                finally{
                    srcNodeCursor.dispose();
                }
            }
            finally{
                targetCursor.dispose();
            }
            //после этого sourceNode стал невалидным. Получаем его снова
            final XmlCursor parentNodeCursor = parentNode.newCursor();
            try{
                parentNodeCursor.toChild(name);
                sourceNode = parentNodeCursor.getObject();
            }
            finally{
                parentNodeCursor.dispose();
            }
        }
        super.applyChanges();
    }

    private boolean hasChildOfAnyType() {
        final SchemaType type = getSchemaType();
        return (type.getElementProperties() != null && type.getElementProperties().length > 0
                && type.getElementProperties()[0].getType().getBuiltinTypeCode() == SchemaType.BTC_ANY_TYPE);
    }

    public boolean isAnyType() {
        final SchemaType type = getSchemaType();
        if (type.getBuiltinTypeCode() == SchemaType.BTC_ANY_TYPE) {
            return true;
        } else if (parent() instanceof XTreeTag) {
            return ((XTreeTag) parent()).hasChildOfAnyType();
        }
        return false;
    }

    //Вызывается после добавления узла в дерево
    public void init() {
        final SchemaType type = getSchemaType();
        final SchemaParticle[] contentModel;
        final SchemaParticle schemaParticle;
        if (type!=null && type.getContentModel()!=null){
            contentModel = null;
            schemaParticle=type.getContentModel();
        }else if (parent() instanceof XTreeTag && node!=null){
            final String name = node.getDomNode().getLocalName();
            contentModel = XElementTools.takeContentModel(((XTreeTag)parent()).getSchemaType());
            schemaParticle = XElementTools.getModelByName(contentModel, name);
        }else{
            contentModel = null;
            schemaParticle = null;
        }
        if (node instanceof XmlAnyTypeImpl) {
            if (parent() instanceof XTreeTag) {
                final XTreeTag p = (XTreeTag) parent();                
                if (schemaParticle == null
                    && contentModel != null
                    && !XElementTools.allowsWildcards(p.getSchemaType())){
                    setForeground(0, new QBrush(QColor.red));
                    odd = true;
                }
            } else {
                setForeground(0, new QBrush(QColor.red));
            }
        }
        final ValEditor editor = getEditor();
        if (editor!=null){
            editor.setMandatory(schemaParticle==null || !schemaParticle.isNillable());
        }
    }

    private ValEditor setupEditor() {
        ValEditor editor = null;
        if (node != null) {
            final SchemaAnnotation sa = XEditorBuilder.getElementAnnotation(this);
            final XmlObject presentation = XEditorBuilder.getPresentationTag(sa);
            //***********************************************************************************
            final EditMask mask = XEditorBuilder.getMaskFromPresentation(presentation);
            final Id constID = XEditorBuilder.getConstSetID(sa);
            try {
                //editor
                if (constID != null) {
                    final RadEnumPresentationDef constDef = tw.getEnvironment().getApplication().getDefManager().getEnumPresentationDef(constID);
                    if (constDef != null) {
                        editor = XEditorBuilder.getConstSetEditor(tw.getEnvironment(), constDef);
                    } else {
                        editor = XEditorTools.getRelevantEditor(tw.getEnvironment(), getSchemaType());
                    }
                    //edit mask
                    if (mask != null) {
                        editor.setEditMask(mask);
                    }
                } else {
                    editor = editorForUndefinedConstDef(mask);
                }
            } catch (DefinitionError defError) {
                editorForUndefinedConstDef(mask);
                final String mess = Application.translate("XmlEditor", "Cannot get enumeration #%s");
                tw.getEnvironment().getTracer().error(String.format(mess, constID.toString()), defError);
                return editor;
            }
            XEditorBuilder.setupEditorWithPresentation(editor, presentation);

            //***********************************************************************************
            updateEditorValue(editor);
            editor.valueChanged.connect(this, "changeValue(Object)");
            editor.changeStateForGrid();
            if (editor instanceof XValueArray) {
                ((XValueArray) editor).setDialogTitle(node.getDomNode().getNodeName());
            }
        }
        return editor;
    }
    
    private void updateEditorValue(final ValEditor editor){
        if (editor!=null && node!=null){
            final XmlCursor cursor = node.newCursor();
            try{                
                cursor.toNextToken();
                boolean isNill = false;
                while (cursor.currentTokenType().intValue() != TokenType.INT_TEXT
                        && cursor.currentTokenType().intValue() != TokenType.INT_END
                        && cursor.currentTokenType().intValue() != TokenType.INT_START
                        && cursor.currentTokenType().intValue() != TokenType.INT_ENDDOC) {
                    if (!isNill && XElementTools.isXsiNilAttr(cursor)){
                        isNill = true;
                    }
                    cursor.toNextToken();
                }
                if (cursor.currentTokenType().intValue() == TokenType.INT_TEXT) {
                    XEditorTools.setProperValueToEditor(editor, cursor.getTextValue());
                }else if (editor instanceof XValueArray && !isNill){
                    ((XValueArray)editor).setValue("");
                }
            }finally{
                cursor.dispose();
            }
        }
    }
    
    @Override
    protected void changeValue(final Object value) {
        final ValEditor editor = getEditor();
        if (value==null && editor!=null && editor.isMandatory()){
            updateEditorValue(editor);//do not allow to set null value
            return;
        }
        if (node!=null){
            final XmlCursor cursor = node.newCursor();            
            try{                
                if (value==null){                    
                    cursor.setAttributeText(NIL_ATTRIBUTE_NAME, "true");
                    cursor.setTextValue("");
                }else{
                    cursor.removeAttribute(NIL_ATTRIBUTE_NAME);
                }
            }finally{
                cursor.dispose();
            }
        }
        if (value==null){
            setTWModified();
        }else{
            super.changeValue(value);
            XElementTools.checkXsiNSDeclaration(node);
        }
        udateTagName();
    }

    private void udateTagName() {
        if (node!=null){
            final boolean hasText = getEditor()!=null && getEditor().getValue()!=null;
            final StringBuilder nodeName = new StringBuilder();
            nodeName.append("<");
            nodeName.append(node.getDomNode().getNodeName());            
            if (!XElementTools.hasChildNodes(node) && !hasText) {                
                nodeName.append("/");
            }
            nodeName.append(">");            
            setText(0, nodeName.toString());
        }
    }

    public SchemaParticle[] getChoices() {
        final XTreeTag parent = (XTreeTag) this.parent();
        if (parent != null) {
            return defineChoices(parent.getSchemaType());
        } else {
            final XmlCursor c = node.newCursor();
            c.toParent();
            final XmlObject p = c.getObject();
            if (p != null) {
                return defineChoices(p.schemaType());
            }
        }
        return null;
    }

    private SchemaParticle[] defineChoices(final SchemaType type) {
        if (XElementTools.getContentModelType(type) == SchemaParticle.CHOICE) {
            return XElementTools.takeContentModel(type);
        } else {
            final SchemaParticle[] cm = XElementTools.takeContentModel(type);
            if (cm != null) {
                for (SchemaParticle p : cm) {
                    if (p.getParticleType() == SchemaParticle.CHOICE) {
                        final SchemaParticle[] choices = p.getParticleChildren();
                        for (SchemaParticle c : choices) {
                            if (c.getParticleType() == SchemaParticle.ELEMENT
                                    && c.getName().getLocalPart().equals(node.getDomNode().getLocalName())) {

                                final List<SchemaParticle> res = XElementTools.getListedElementsOfChoice(p);
                                final SchemaParticle[] arrResult = new SchemaParticle[res.size()];
                                System.arraycopy(res.toArray(), 0, arrResult, 0, res.size());
                                return arrResult;

                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public int hasAChild(final String local, final int start) {
        final NodeList childs = node.getDomNode().getChildNodes();
        if (childs != null) {
            Node childNode;
            for (int i = start, length = childs.getLength(); i < length; i++) {
                childNode = childs.item(i);//NodeList.item(i) returns null if corresponding node is invalid
                if (childNode != null && childNode.getNodeType() == Node.ELEMENT_NODE && childNode.getLocalName().equals(local)) {
                    return i;
                }
            }
        }
        return XElementTools.NO_CHILDS;
    }

    public int hasSequencedChild(final SchemaParticle p, final int start) {
        final SchemaParticle[] parties = p.getParticleChildren();
        if (parties != null) {
            for (SchemaParticle pChild : parties) {
                if (pChild.getParticleType() == SchemaParticle.ELEMENT) {
                    if (hasAChild(pChild.getName().getLocalPart(), start) != XElementTools.NO_CHILDS) {
                        return hasAChild(pChild.getName().getLocalPart(), start);
                    }
                } else {
                    final int res = hasSequencedChild(pChild, start);
                    if (res != XElementTools.NO_CHILDS) {
                        return res;
                    }
                }
            }
        }
        return XElementTools.NO_CHILDS;
    }

    public int sameChildsCount(final String local) {        
        Node childNode = node.getDomNode().getFirstChild();
        if (childNode==null){
            return XElementTools.NO_CHILDS;
        }
        while(childNode!=null && (childNode.getNodeType()!=Node.ELEMENT_NODE || !childNode.getLocalName().equals(local))){
            childNode = childNode.getNextSibling();
        }
        int count=0;
        while(childNode!=null && childNode.getNodeType()==Node.ELEMENT_NODE && childNode.getLocalName().equals(local)){
            count++;
            do{
                childNode = childNode.getNextSibling();
            }while(childNode!=null && childNode.getNodeType()==Node.TEXT_NODE);
        }
        return count;
    }

    public int sameSequencesCount(final SchemaParticle p) {
        final NodeList childs = node.getDomNode().getChildNodes();
        int sCount = 0;
        int s = findLastSequencedChild(p, 0);
        if (s != XElementTools.NO_CHILDS) {
            sCount++;
            s = findLastSequencedChild(p, s + 1);
            while (s != XElementTools.NO_CHILDS
                    && s <= childs.getLength() - 1) {
                sCount++;
                s = findLastSequencedChild(p, s + 1);
            }
            return sCount;
        }
        return XElementTools.NO_CHILDS;
    }

    private int findLastSequencedChild(final SchemaParticle p, final int start) {
        final SchemaParticle[] parties = p.getParticleChildren();
        if (parties != null) {
            for (int i = parties.length - 1; i >= 0; i--) {
                if (parties[i].getParticleType() == SchemaParticle.ELEMENT) {
                    if (hasAChild(parties[i].getName().getLocalPart(), start) != XElementTools.NO_CHILDS) {
                        return hasAChild(parties[i].getName().getLocalPart(), start);
                    }
                } else {
                    final int res = findLastSequencedChild(parties[i], start);
                    if (res != XElementTools.NO_CHILDS) {
                        return res;
                    }
                }
            }
        }
        return XElementTools.NO_CHILDS;
    }

    private boolean mightHaveContent() {
        final SchemaType t = getSchemaType();//node.schemaType();
        if (t.isSimpleType()
                || t.getContentType() == SchemaType.MIXED_CONTENT
                || t.getContentType() == SchemaType.SIMPLE_CONTENT) {
            return true;
        }
        return false;
    }

    public int getMinOccurs() {
        final XmlObject p = XElementTools.getParentNode(node);
        final SchemaParticle model = parent() != null ? ((XTreeElement) parent()).getSchemaType().getContentModel() : p.schemaType().getContentModel();
        if (model != null && model.getParticleChildren() != null) {
            final SchemaParticle[] parties = model.getParticleChildren();
            SchemaParticle particle;
            for (SchemaParticle i : parties) {
                particle = findMinOccurs(i);
                if (particle!=null) {
                    return i.getIntMinOccurs()==XElementTools.OCCURS ? particle.getIntMinOccurs() : i.getIntMinOccurs();
                }
            }
        }
        return XElementTools.OCCURS;
    }

    public int getMaxOccurs() {
        int res = 0;
        final XmlObject p = XElementTools.getParentNode(node);
        final SchemaParticle model = parent() != null ? ((XTreeElement) parent()).getSchemaType().getContentModel() : p.schemaType().getContentModel();
        if (model != null && model.getParticleChildren() != null) {
            final SchemaParticle[] parties = model.getParticleChildren();
            for (SchemaParticle i : parties) {
                res = findMaxOccurs(i);
                if (res != XElementTools.OCCURS) {
                    return res;
                }
            }
        }
        return XElementTools.OCCURS;
    }

    private SchemaParticle findMinOccurs(final SchemaParticle i) {
        if (i.getParticleType() == SchemaParticle.ELEMENT) {            
            if (i.getName().getLocalPart().equals(node.getDomNode().getLocalName())) {
                return i;
            }
        } else {
            SchemaParticle particle;
            if (i.getParticleChildren() != null) {
                for (SchemaParticle p : i.getParticleChildren()) {
                    particle = findMinOccurs(p);
                    if (particle!=null) {                        
                        return p.getIntMinOccurs()==XElementTools.OCCURS ? particle : p;
                    }
                }
            }
        }
        return null;
    }

    private int findMaxOccurs(final SchemaParticle i) {
        int r = 0;
        if (i.getParticleType() == SchemaParticle.ELEMENT) {
            if (i.getName().getLocalPart().equals(node.getDomNode().getLocalName())) {
                return i.getIntMaxOccurs();
            }
        } else {
            if (i.getParticleChildren() != null) {
                for (SchemaParticle p : i.getParticleChildren()) {
                    r = findMaxOccurs(p);
                    if (r != XElementTools.OCCURS) {
                        return r;
                    }
                }
            }
        }
        return XElementTools.OCCURS;
    }

    public int getLastAttributeTreeIndex() {
        int i = 0;
        while (child(i) instanceof XTreeAttribute
                && i <= childCount() - 1) {
            i++;
        }
        return i;
    }

    public boolean anyElementAllowed() {
        return allowsAny;
    }

    public boolean isOdd() {
        return odd;
    }

    public boolean isFirstChoice() {
        return firstChoice;
    }

    public void setFirstChoice(final boolean v) {
        firstChoice = v;
    }

    public boolean isPartOfSequence(final XmlObject node, final SchemaParticle[] sequence) {
        if (node != null
                && sequence != null) {
            for (SchemaParticle p : sequence) {
                if (p.getParticleType() == SchemaParticle.ELEMENT) {
                    if (p.getName().getLocalPart().equals(node.getDomNode().getLocalName())) {
                        return true;
                    }
                } else {
                    return isPartOfSequence(node, p.getParticleChildren());
                }
            }
        }
        return false;
    }

    public SchemaParticle[] checkForSequenceInChoice(final XmlObject node, final SchemaParticle[] choice) {
        if (choice != null
                && node != null) {
            for (SchemaParticle p : choice) {
                if (p.getParticleType() == SchemaParticle.ELEMENT) {
                    if (p.getName().getLocalPart().equals(node.getDomNode().getLocalName())) {
                        return null;
                    }
                } else {
                    if (isPartOfSequence(node, p.getParticleChildren())) {
                        return p.getParticleChildren();
                    }
                }
            }
        }
        return null;
    }
}
