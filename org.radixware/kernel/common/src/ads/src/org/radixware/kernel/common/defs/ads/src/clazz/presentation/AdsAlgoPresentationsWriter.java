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

package org.radixware.kernel.common.defs.ads.src.clazz.presentation;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject.Kind;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsCatchObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsEdge;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsIncludeObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsNoteObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPage;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPin;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsScopeObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsVarObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AlgoClassPresentations;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


public class AdsAlgoPresentationsWriter extends AdsClassPresentationsWriter<AlgoClassPresentations> {

    public AdsAlgoPresentationsWriter(JavaSourceSupport support, AlgoClassPresentations presentations, JavaSourceSupport.UsagePurpose usagePurpose) {
        super(support, presentations, usagePurpose);
    }
    private static final String ALGO_CLASS_NAME = "org.radixware.kernel.common.client.meta.RadAlgoDef";
    private static final String PAGE_CLASS_NAME = "org.radixware.kernel.common.client.meta.RadAlgoDef.Page";
    private static final String NODE_CLASS_NAME = "org.radixware.kernel.common.client.meta.RadAlgoDef.Node";
    private static final String EDGE_CLASS_NAME = "org.radixware.kernel.common.client.meta.RadAlgoDef.Edge";

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case EXPLORER:
            case WEB:
                AdsAlgoClassDef algo = (AdsAlgoClassDef)ownerClass;
                if (algo.getReplacementId() != null)
                    algo = algo.getReplacementDef();
                
                final AdsPage page = algo.getPage();
                printer.print("new " + ALGO_CLASS_NAME + "(");
                printer.enterBlock(1);
                printer.println("/*Class Id*/");
                WriterUtils.writeIdUsage(printer, algo.getId());
                printer.printComma();
                printer.println();
                printer.println("/*Class Name*/");
                printer.printStringLiteral(algo.getName());
                printer.printComma();
                printer.println();
                printer.println("/*Title Id*/");
                WriterUtils.writeIdUsage(printer, algo.getTitleId());
                printer.printComma();
                printer.println();
                
                printer.println("new Object() {");
                printer.enterBlock(1);
                printer.println(PAGE_CLASS_NAME + " getPage() {");
                printer.enterBlock(1);
                writePage(printer, page);
                printer.println("return " + page.getId() + ";");
                printer.leaveBlock(1);
                printer.println("}");
                printer.leaveBlock(1);
                printer.print("}.getPage()");                
                printer.printComma();                
                printer.println();
                
                printer.println("new Object() {");
                printer.enterBlock(1);
                printer.println("java.util.Map<org.radixware.kernel.common.types.Id, String> getTitles() {");
                printer.enterBlock(1);
                writeTitles(printer);
                printer.println("return titles;");
                printer.leaveBlock(1);
                printer.println("}");
                printer.leaveBlock(1);
                printer.print("}.getTitles()");
                
                printer.leaveBlock(1);
                printer.print(')');
                printer.printlnSemicolon();
                printer.println();
                return true;
            default:
                WriterUtils.writeNull(printer);
                return true;
        }
    }

    private void writePage(CodePrinter printer, AdsPage page) {
        int dx = Integer.MAX_VALUE;
        int dy = Integer.MAX_VALUE;
        for (AdsBaseObject node: page.getNodes()) {
            final Rectangle r = node.getBounds();
            dx = Math.min(dx, r.x);
            dy = Math.min(dy, r.y);
        }
        for (AdsEdge edge: page.getEdges()) {
            for (int i = 1; i < edge.getPoints().size()-1; i++) {
                final Point p = edge.getPoints().get(i);
                dx = Math.min(dx, p.x);
                dy = Math.min(dy, p.y);
            }
        }
        
        final Map<AdsPin, AdsBaseObject> pin2Node = new HashMap<>();
        for (AdsBaseObject node: page.getNodes()) {
            for (AdsPin pin: node.getSourcePins()) {
                pin2Node.put(pin, node);
            }
            for (AdsPin pin: node.getTargetPins()) {
                pin2Node.put(pin, node);
            }
        }
        
        for (AdsBaseObject node: page.getNodes()) {
            AdsPage nodePage = null;
            if (node instanceof AdsScopeObject) {
                nodePage = ((AdsScopeObject)node).getPage();
            }
            if (node instanceof AdsCatchObject) {
                nodePage = ((AdsCatchObject)node).getPage();
            }
            if (nodePage != null) {
                printer.println();
                printer.enterBlock(1);
                writePage(printer, nodePage);
                printer.leaveBlock(1);
                printer.println();
            }
            Id algoId = null;
            if (node instanceof AdsIncludeObject) {
                algoId = ((AdsIncludeObject)node).getAlgoId();
            }
            printer.print("final " + NODE_CLASS_NAME + " " + node.getId() + " = new " + NODE_CLASS_NAME + "(");
            WriterUtils.writeIdUsage(printer, node.getId());
            printer.print(", ");
            WriterUtils.writeEnumFieldInvocation(printer, node.getKind());
            printer.print(", ");
            if (node.getKind() == Kind.NOTE)
                printer.printStringLiteral(((AdsNoteObject)node).getText());
            else
                printer.printStringLiteral(node.getName());
            printer.print(", ");
            printer.print("new java.awt.Rectangle(" + (node.getBounds().x - dx) + ", " + (node.getBounds().y - dy) + ", " + (node.getBounds().width <= 0 ? 56 : node.getBounds().width) + ", " + (node.getBounds().height <= 0 ? 56 : node.getBounds().height) + ")");
            if (nodePage != null) {
                printer.print(", " + nodePage.getId());
            }
            if (algoId != null) {
                printer.print(", ");
                WriterUtils.writeIdUsage(printer, algoId);
            }
            printer.println(");");
        }
        printer.println();
        
        for (AdsEdge edge: page.getEdges()) {
            final AdsBaseObject source = pin2Node.get(edge.getSource());
            final AdsBaseObject target = pin2Node.get(edge.getTarget());
            printer.print("final " + EDGE_CLASS_NAME + " " + edge.getId() + " = new " + EDGE_CLASS_NAME + "(" + source.getId() + ", " + target.getId() + ", ");
            if (edge.getPoints().isEmpty())
                printer.print("new java.util.ArrayList<java.awt.Point>()");
            else {            
                printer.print("java.util.Arrays.asList(");
                int idx = 0;
                for (Point point: edge.getPoints()) {
                    printer.print("new java.awt.Point(" + (point.x - dx) + ", " + (point.y - dy) + ")");
                    if (++idx < edge.getPoints().size())
                        printer.print(", ");
                }
                printer.print(")");
            }
            printer.println(");");
        }
        printer.println();
        
        printer.print("final " + PAGE_CLASS_NAME + " " + page.getId() + " = new " + PAGE_CLASS_NAME + "(");
        if (page.getNodes().isEmpty())
            printer.print("new java.util.ArrayList<" + NODE_CLASS_NAME + ">(), ");
        else {
            printer.print("java.util.Arrays.asList(");
            int idx = 0;
            for (AdsBaseObject node: page.getNodes()) {
                printer.print(node.getId());
                if (++idx < page.getNodes().size())
                    printer.print(", ");
            }
            printer.print("), ");
        }
        if (page.getEdges().isEmpty())
            printer.print("new java.util.ArrayList<" + EDGE_CLASS_NAME + ">()");
        else {
            final List<AdsEdge> edges = page.getEdges().list();
            for (int i=0; i<edges.size(); i++)
            for (int j=0; j<edges.size(); j++) {
                if (i == j)
                    continue;
                final AdsEdge edge1 = edges.get(i);
                final AdsEdge edge2 = edges.get(j);
                final AdsBaseObject node1 = pin2Node.get(edge1.getSource());
                final AdsBaseObject node2 = pin2Node.get(edge2.getSource());
                if (node1 == node2) {
                    final int idx1 = node1.getPins().indexOf(edge1.getSource());
                    final int idx2 = node2.getPins().indexOf(edge2.getSource());
                    if (idx1 < idx2) {
                        edges.set(i, edge2);
                        edges.set(j, edge1);
                    }
                }
            }
/*            
            Collections.sort(edges, new Comparator<AdsEdge>() {
                @Override
                public int compare(AdsEdge edge1, AdsEdge edge2) {
                    final AdsBaseObject node1 = pin2Node.get(edge1.getSource());
                    final AdsBaseObject node2 = pin2Node.get(edge2.getSource());
                    if (node1 == node2) {
                        final int idx1 = node1.getPins().indexOf(edge1.getSource());
                        final int idx2 = node2.getPins().indexOf(edge2.getSource());
                        return idx1 < idx2 ? +1 : -1;
                    }
                    return 0;
                }
            });
*/
            printer.print("java.util.Arrays.asList(");
            int idx = 0;
            for (AdsEdge edge: edges) {
                printer.print(edge.getId());
                if (++idx < page.getEdges().size())
                    printer.print(", ");
            }
            printer.print(")");
        }
        printer.println(");");
    }

    private void writeTitles(final CodePrinter printer) {
        printer.println("final java.util.Map<org.radixware.kernel.common.types.Id, String> titles = new java.util.HashMap<org.radixware.kernel.common.types.Id, String>();");
        ownerClass.visitAll(new IVisitor() {
            @Override
            public void accept(RadixObject obj) {
                Id id = null;
                String title = null;
                if (obj instanceof AdsAlgoClassDef.Param || obj instanceof AdsVarObject/* || obj instanceof AdsAppObject || obj instanceof AdsIncludeObject*/) {
                    final Definition def = (Definition)obj;
                    id = def.getId();
                    title = def.getName();
                } else if (obj instanceof AdsIncludeObject.Param) {
                    final AdsIncludeObject.Param p = (AdsIncludeObject.Param)obj;
                    id = p.getId();
                    title = p.getOwnerDefinition().getName() + "." + p.getName();
                } else if (obj instanceof AdsAppObject.Prop) {
                    final AdsAppObject.Prop p = (AdsAppObject.Prop)obj;
                    if ((p.getMode() & AdsAppObject.Prop.PUBLIC) != 0) {
                        id = p.getId();
                        title = p.getOwnerDefinition().getName() + "." + p.getName();
                    }
                }
                if (id != null) {
                    printer.print("titles.put(");
                    WriterUtils.writeIdUsage(printer, id);
                    printer.print(", ");
                    printer.printStringLiteral(title);
                    printer.println(");");
                }
            }
        });
    }
    
    @Override
    public char[] getExplorerMetaClassName() {
        return ALGO_CLASS_NAME.toCharArray();
    }
}