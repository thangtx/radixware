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

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;

import com.trolltech.qt.gui.QTreeWidgetItem;
import java.nio.charset.Charset;
import java.util.EnumSet;
import org.apache.xmlbeans.SchemaType;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.utils.ValueConverter;
import org.radixware.kernel.common.types.Bin;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

abstract class XTreeElement extends QTreeWidgetItem {

    protected XmlObject node;
    private ValEditor editor;
    private String xPath;
    protected final TreeWindow tw;

    public abstract boolean isExternalTypeSystem();

    public abstract SchemaType getSchemaType();

    public void applyChanges(){
        if (parent()!=null){
            ((XTreeElement)parent()).applyChanges();
        }
    }

    public XTreeElement(final TreeWindow tw) {
        this.tw = tw;
        setupColors();
    }

    @SuppressWarnings("unchecked")
    private void setupColors() {//added by yremizov        
        final ExplorerTextOptions valueOptions;
        if (tw.getEditor().isReadOnlyMode()){
            valueOptions = ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.READONLY_VALUE);            
        }else{
            valueOptions = ExplorerTextOptions.Factory.getDefault();            
        }        
        if (editor!=null){
            if (tw.getEditor().isReadOnlyMode()){
                editor.addTextOptionsMarkers(ETextOptionsMarker.READONLY_VALUE);                
            }else if (!editor.isReadOnly()){
                editor.removeTextOptionsMarkers(ETextOptionsMarker.READONLY_VALUE);
            }
            if (editor instanceof ValBoolEditor || editor instanceof AdvancedValBoolEditor){
                editor.setStyleSheet(valueOptions.getStyleSheet());
            }
        }
        
        final EnumSet<ETextOptionsMarker> labelMarkers = EnumSet.of(ETextOptionsMarker.LABEL);
        if (editor!=null){
            final EnumSet<ETextOptionsMarker> editorTextOptions = editor.getTextOptionsMarkers();
            labelMarkers.addAll(editorTextOptions);
            if (editor.isMandatory() && !editor.isReadOnly()){
                if (editor.getValue()==null){
                    labelMarkers.add(ETextOptionsMarker.MANDATORY_VALUE);
                }else{
                    labelMarkers.remove(ETextOptionsMarker.MANDATORY_VALUE);
                }
            }            
        }else if (tw.getEditor().isReadOnlyMode()){
            labelMarkers.add(ETextOptionsMarker.READONLY_VALUE);
        }
        final ExplorerTextOptions labelOptions = 
            (ExplorerTextOptions)tw.getEnvironment().getTextOptionsProvider().getOptions(labelMarkers,null);
        labelOptions.applyTo(this, 0);        
        valueOptions.applyTo(this, 1);        
    }    

    public XmlObject getNode() {
        return node;
    }

    public ValEditor getEditor() {
        return editor;
    }

    public void setEditor(final ValEditor e) {
        if (e!=null){
            e.setParent(tw.getTree());
        }
        editor = e;
        setupColors();
        /*
        if (e instanceof XValBinEditor){
            setupBinEditor((XValBinEditor)e);
        }*/
    }

    protected final void setTWModified() {
        applyChanges();
        setupColors();
        if (tw != null) {
            tw.setModified(true);
        }
    }

    public String getAnnotationText() {
        if (node.getDomNode().getNodeType() == Node.ELEMENT_NODE) {
            final SchemaAnnotation a = XEditorBuilder.getElementAnnotation(this);
            if (a != null) {
                return XEditorBuilder.getDocumentation(a);
            }
        }
        if (node.getDomNode().getNodeType() == Node.ATTRIBUTE_NODE) {
            final XTreeAttribute atr = (XTreeAttribute) this;
            if (atr.getDefinition() != null
                    && atr.getDefinition().getAnnotation() != null) {
                return XEditorBuilder.getDocumentation(atr.getDefinition().getAnnotation());
            }
        }
        return "";
    }

    protected void changeValue(final Object value) {
        if (editor != null) {
            if (value instanceof Bin) {
                final Bin v = (Bin) value;
                if (node != null) {
                    node.newCursor().setTextValue(ValueConverter.arrByte2Str(v.get(), null));
                    setTWModified();
                }
            }
            if (value instanceof Character) {
                final Character v = (Character) value;
                if (node != null) {
                    node.newCursor().setTextValue(v.toString());
                    setTWModified();
                }
            }
            if (value instanceof String) {
                final String v = (String) value;
                if (node != null) {
                    node.newCursor().setTextValue(v);
                    setTWModified();
                }
            }
            if (value instanceof Boolean) {
                final Boolean v = (Boolean) value;
                if (!v) {
                    if (node != null) {
                        node.newCursor().setTextValue("false");
                        setTWModified();
                    }
                } else {
                    if (node != null) {
                        node.newCursor().setTextValue("true");
                        setTWModified();
                    }
                }

            }
            if (value instanceof Long) {
                final Long v = (Long) value;
                if (node != null) {
                    node.newCursor().setTextValue(v.toString());
                    setTWModified();
                }
            }
            if (value instanceof BigDecimal) {
                final BigDecimal v = (BigDecimal) value;
                if (node != null) {
                    node.newCursor().setTextValue(v.toString());
                    setTWModified();
                }
            }
            if (value instanceof Timestamp) {
                final Timestamp v = (Timestamp) value;
                final XmlDateTime v_xml = XmlDateTime.Factory.newValue(v);
                if (node != null) {
                    final XmlCursor c = node.newCursor();
                    c.setTextValue(v_xml.getStringValue());
                    setTWModified();
                }
            }
        }
    }

    public String getElementPrefix() {
        if (node != null) {
            return node.getDomNode().getPrefix();
        }
        return "";
    }

    public String getElementNS() {
        if (node != null) {
            return node.getDomNode().getNamespaceURI();
        }
        return "";
    }

    protected final ValEditor editorForUndefinedConstDef(final EditMask mask) {
        if (mask != null) {
            return XEditorBuilder.getEditorByMask(tw.getEnvironment(), mask);
        } else {
            return XEditorTools.getRelevantEditor(tw.getEnvironment(), getSchemaType());
        }
    }
    
    private void setupBinEditor(final XValBinEditor binEditor){
        final String nodeXPath = getXPath();
        if (!nodeXPath.isEmpty()){
            final Charset charset = tw.findCharsetForXPath(nodeXPath);
            binEditor.blockSignals(true);
            try{
                binEditor.setCharset(charset);
            }finally{
                binEditor.blockSignals(false);
            }
            binEditor.charsetChanged.connect(this,"onCharsetChanged(Charset)");
        }
    }
    
    @SuppressWarnings("unused")
    private void onCharsetChanged(final Charset charset){
        tw.setCharsetForXPath(getXPath(), charset);
    }
    
    private String getXPath(){
        if (xPath==null){
            final Node domNode = getNodeForXPath();
            xPath = domNode==null ? "" : calcXPath(domNode);
        }
        return xPath;
    }
    
    protected Node getNodeForXPath(){
        final XmlObject xmlNode = getNode();
        return xmlNode==null ? null : xmlNode.getDomNode();
    }
    
    private static String calcXPath(final Node xmlNode){
        if (xmlNode==null){
          return "";
        }
        if ( xmlNode.getNodeType()!=Node.ELEMENT_NODE
            && xmlNode.getNodeType()!=Node.ATTRIBUTE_NODE
            && xmlNode.getNodeType()!=Node.DOCUMENT_NODE){
            throw new IllegalArgumentException("Unexpected node type: "+xmlNode.getNodeType());
        }
        
        Node current = xmlNode;
        final StringBuffer buffer = new StringBuffer();

        do{
            boolean handled = false;

            if (current.getNodeType() == Node.ELEMENT_NODE) {
                final Node parent = current.getParentNode();
                final Element e = (Element) current;

                // is this the root element?
                if (parent==null || parent.getNodeType()==Node.DOCUMENT_NODE) {
                    // root element - simply append element name
                    buffer.insert(0,current.getNodeName());
                } else {                    
                    if (current.hasAttributes()) {
                        // see if the element has a name or id attribute
                        if (e.hasAttribute("id")) {
                            // id attribute found - use that
                            buffer.insert(0,"']");
                            buffer.insert(0, e.getAttribute("id"));
                            buffer.insert(0,"[@id='");                    
                            handled = true;
                        } else if (e.hasAttribute("name")) {
                            // name attribute found - use that
                            buffer.insert(0,"']");
                            buffer.insert(0, e.getAttribute("name"));
                            buffer.insert(0,"[@name='");                    
                            handled = true;
                        }
                    }

                    if (!handled) {
                        // no known attribute we could use - get sibling index
                        int prev_siblings = 1;
                        Node prev_sibling = current.getPreviousSibling();
                        while (null != prev_sibling) {
                            if (   prev_sibling.getNodeType() == current.getNodeType()
                                && prev_sibling.getNodeName().equalsIgnoreCase(current.getNodeName()) ){
                                prev_siblings++;
                            }
                            prev_sibling = prev_sibling.getPreviousSibling();
                        }
                        buffer.insert(0,"]");
                        buffer.insert(0,String.valueOf(prev_siblings));
                        buffer.insert(0,"[");
                    }
                    // child element - append slash and element name                    
                    buffer.insert(0,current.getNodeName());
                    buffer.insert(0,"/");
                }
                current = parent;
            } else if (current.getNodeType() == Node.ATTRIBUTE_NODE) {
                buffer.insert(0,current.getNodeName());
                buffer.insert(0,"/@");
                current = ((Attr) current).getOwnerElement();
            }
        }while (null != current && current.getNodeType() != Node.DOCUMENT_NODE);        
        return buffer.toString();        
    }
}
