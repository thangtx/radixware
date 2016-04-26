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
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;

public class XComboBox {//extends ValListEditor {

    private List<String> values = new ArrayList<String>();

    public XComboBox(final XTreeTag node, final List<String> values) {
        //super(null, mask, true, false);
        //this.setValue(i.getNode().getDomNode().getNodeName());
        if (values != null) {
            if (node.getNode() != null) {
                String nodeName = node.getNode().getDomNode().getLocalName();
                String prefix = node.getNode().getDomNode().getPrefix();
                if (prefix != null && !prefix.isEmpty()){
                    nodeName = prefix + ":" + nodeName;
                }
                if (values.contains(nodeName)) {
                    values.remove(nodeName);
                }
            }
            this.values = values;
        } else {
            this.values = Collections.emptyList();
        }

    }

    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    public static void choiceTagChanged(String iName, final XTreeTag node, final TreeBuilder treeBuilder) {
        if (iName.contains(":")) {
            int colon = iName.indexOf(":");
            iName = iName.substring(colon + 1, iName.length());
        }
        if (node != null) {
            SchemaParticle[] model = node.getChoices();
            if (model != null) {
                SchemaParticle def = XElementTools.getModelByName(model, iName);
                if (def != null) {
                    XmlCursor c = node.getNode().newCursor();
                    XmlObject old = c.getObject();
                    //old sequence filling
                    ArrayList<XmlObject> oldS = new ArrayList<XmlObject>();
                    SchemaParticle[] seqInChoice = node.checkForSequenceInChoice(old, model);
                    if (seqInChoice != null) {
                        XmlCursor cursor = old.newCursor();
                        XmlObject current = cursor.getObject();
                        while (node.isPartOfSequence(current, seqInChoice)) {
                            oldS.add(current);
                            if (cursor.toNextSibling()) {
                                current = cursor.getObject();
                            }
                        }
                    } else {
                        oldS.add(old);
                    }
                    //*******************
                    ArrayList<XmlObject> newS = new ArrayList<XmlObject>();
                    int index = 0;
                    XTreeTag parent = (XTreeTag) node.parent();
                    if (parent != null) {//otherwise do nothing (if parent==null,node is root tag, which cannot be changed via XComboBox)
                        //reforming XML-document
                        final int nodeindex = parent.indexOfChild(node);//save index to select node in tree after reforming tree
                        index = nodeindex;
                        if (def.getParticleType() == SchemaParticle.ELEMENT) {
                            QName qn = def.getName();
                            c.insertElement(qn);
                            c.toPrevSibling();
                            XmlObject newnode = c.getObject();
                            buildAddedNode(newnode);
                            newS.add(newnode);
                        } else {
                            XmlBuilder xb = new XmlBuilder();
                            ArrayList<Integer> inserts = new ArrayList<Integer>();
                            xb.buildNodeContent(newS, parent.getNode(), c, def, inserts);
                        }
                        //deleting old nodes from document
                        for (int o = 0; o <= oldS.size() - 1; o++) {
                            Node old_item = oldS.get(o).getDomNode();
                            old_item.getParentNode().removeChild(old_item);
                        }
                        //reforming the tree
                        int last = index + oldS.size() - 1;
                        for (int l = index; l <= last; l++) {
                            XTreeTag child = (XTreeTag) parent.child(index);
                            parent.removeChild(child);
                        }
                        for (int t = 0; t <= newS.size() - 1; t++) {
                            final XmlObject objToBuild = newS.get(t);
                            treeBuilder.insertChildItems(parent, objToBuild, index);
                            parent.child(index).setExpanded(true);
                            index++;
                        }

                        XTreeTag boxhandler = (XTreeTag) parent.child(nodeindex);
                        boxhandler.treeWidget().setCurrentItem(boxhandler);
                        node.tw.setupToolsForItem();
                    }
                }
            }
        }
    }

    private static void buildAddedNode(XmlObject x) {
        XmlBuilder xb = new XmlBuilder();
        xb.buildAttributes(x);
        ArrayList<Integer> inserts = new ArrayList<Integer>();
        xb.buildNodeContent(x, inserts);
    }
}
