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

package org.radixware.kernel.common.defs.ads.clazz.algo.object;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.schemas.algo.ScopeDef;


public class AdsPage extends AdsClassMember {

    private AdsDefinitions<AdsBaseObject> nodes = new AdsDefinitions<AdsBaseObject>(this);
    private AdsDefinitions<AdsEdge> edges = new AdsDefinitions<AdsEdge>(this);

    protected AdsPage(final AdsDefinition owner) {
        super(ObjectFactory.createPageId(owner.getId()), owner instanceof AdsAlgoClassDef ? RadixResourceBundle.getMessage(AdsPage.class, "Scheme-Display-Name") : owner.getName());
        setContainer(owner);
    }

    protected AdsPage(final ScopeDef xPage, final AdsDefinition owner) {
        super(ObjectFactory.createPageId(owner.getId()), owner instanceof AdsAlgoClassDef ? RadixResourceBundle.getMessage(AdsPage.class, "Scheme-Display-Name") : owner.getName());
        HashMap<Id, AdsPin> pins = new HashMap<Id, AdsPin>();

        for (org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode : xPage.getNodes().getNodeList()) {
            AdsBaseObject node = ObjectFactory.createNode(xNode);
            for (AdsPin pin : node.getPins()) {
                pins.put(pin.getId(), pin);
            }
            nodes.add(node);
        }

        for (org.radixware.schemas.algo.ScopeDef.Edges.Edge xEdge : xPage.getEdges().getEdgeList()) {
            AdsEdge edge = ObjectFactory.createEdge(xEdge);
            AdsPin sourcePin = pins.get(edge.getSource().getId());
            AdsPin targetPin = pins.get(edge.getTarget().getId());
            assert sourcePin != null : "source pin for edge " + edge.getId() + " is not correct: " + edge.getSource();
            assert targetPin != null : "target pin for edge " + edge.getId() + " is not correct: " + edge.getTarget();
            edge.source = sourcePin;
            edge.target = targetPin;
            edges.add(edge);
        }

        setContainer(owner);
    }

    protected AdsPage(final AdsPage page, final AdsDefinition owner) {
        super(ObjectFactory.createPageId(owner.getId()), owner instanceof AdsAlgoClassDef ? RadixResourceBundle.getMessage(AdsPage.class, "Scheme-Display-Name") : owner.getName());

        HashMap<AdsPin, AdsPin> pinPins = new HashMap<AdsPin, AdsPin>();
        for (AdsBaseObject origNode : page.nodes) {
            AdsBaseObject node = ObjectFactory.createNode(origNode);

            List<AdsPin> origPins = origNode.getPins();
            List<AdsPin> pins = node.getPins();
            assert origPins.size() == pins.size();

            for (int i = 0; i < pins.size(); i++) {
                pinPins.put(origPins.get(i), pins.get(i));
            }

            nodes.add(node);
        }

        for (AdsEdge origEdge : page.edges) {
            AdsPin source = pinPins.get(origEdge.getSource());
            AdsPin target = pinPins.get(origEdge.getTarget());
            if (source != null && target != null) {
                edges.add(ObjectFactory.createEdge(origEdge, source, target));
            }
        }

        setContainer(owner);
    }

//    public void setStructModified() {
//        getOwnerClass().setStructModified();
//    }
    @Override
    public String getToolTip() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");

        String typeTitle = RadixResourceBundle.getMessage(AdsPage.class, "Scheme-Display-Name");
        String objectName = getOwnerDefinition().getName();
        sb.append("<b>" + typeTitle + " '" + objectName + "'</b>");

        sb.append("</html>");
        return sb.toString();
    }

    public AdsDefinitions<AdsBaseObject> getNodes() {
        return nodes;
    }

    public void add(AdsBaseObject node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public boolean remove(AdsBaseObject node) {
        return nodes.remove(node);
    }

    public AdsDefinitions<AdsEdge> getEdges() {
        return edges;
    }

    public void add(AdsEdge edge) {
        if (!edges.contains(edge)) {
            edges.add(edge);
        }
    }

    public boolean remove(AdsEdge edge) {
        return edges.remove(edge);
    }

    @Override
    public AdsAlgoClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsAlgoClassDef) {
                return (AdsAlgoClassDef) owner;
            }
        }
        return null;
    }

    public void addEventListener(RadixObjects.ContainerChangesListener listener) {
        nodes.getContainerChangesSupport().addEventListener(listener);
        edges.getContainerChangesSupport().addEventListener(listener);
    }

    public void removeEventListener(RadixObjects.ContainerChangesListener listener) {
        nodes.getContainerChangesSupport().removeEventListener(listener);
        edges.getContainerChangesSupport().removeEventListener(listener);
    }

    public void appendTo(ScopeDef xPage, ESaveMode saveMode) {
        xPage.addNewNodes();
        for (AdsBaseObject node : nodes) {
            node.appendTo(xPage.getNodes().addNewNode(), saveMode);
        }
        xPage.addNewEdges();
        for (AdsEdge edge : edges) {
            edge.appendTo(xPage.getEdges().addNewEdge(), saveMode);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        nodes.visit(visitor, provider);
        edges.visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        Definition ownerDefinition = getOwnerDefinition();
        if (ownerDefinition instanceof AdsScopeObject) {
            return AdsDefinitionIcon.CLASS_ALGORITHM_SCOPE;
        }
        if (ownerDefinition instanceof AdsCatchObject) {
            return AdsDefinitionIcon.CLASS_ALGORITHM_CATCH;
        }
        return AdsDefinitionIcon.CLASS_ALGORITHM_SCOPE;
    }

    private class PageClipboardSupport extends AdsClipboardSupport<AdsPage> {

        public PageClipboardSupport() {
            super(AdsPage.this);
        }

        @Override
        public boolean canCopy() {
            return false;
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.algo.ScopeDef xPage = org.radixware.schemas.algo.ScopeDef.Factory.newInstance();
            AdsPage.this.appendTo(xPage, ESaveMode.NORMAL);
            return xPage;
        }

        @Override
        protected AdsPage loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.algo.ScopeDef xPage = (org.radixware.schemas.algo.ScopeDef) xmlObject;
            return ObjectFactory.createPage(xPage, null);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            if (isReadOnly()) {
                return CanPasteResult.NO;
            }

            for (Transfer objectInClipboard : objectsInClipboard) {
                boolean isSupportedClass =
                        (objectInClipboard.getObject() instanceof AdsBaseObject)
                        || (objectInClipboard.getObject() instanceof AdsEdge);

                if (!isSupportedClass) {
                    return CanPasteResult.NO;
                }
            }

            return CanPasteResult.YES;
        }

        @Override
        public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            checkForCanPaste(objectsInClipboard,resolver);

            List<AdsBaseObject> nodes = new ArrayList<AdsBaseObject>();
            List<AdsEdge> edges = new ArrayList<AdsEdge>();

            HashMap<Id, AdsPin> pins = new HashMap<Id, AdsPin>();
            for (Transfer objectInClipboard : objectsInClipboard) {
                if (objectInClipboard.getObject() instanceof AdsBaseObject) {
                    AdsBaseObject node = (AdsBaseObject) objectInClipboard.getObject();
                    for (AdsPin pin : node.getPins()) {
                        pins.put(pin.getId(), pin);
                    }
                    nodes.add(node);
                }
                if (objectInClipboard.getObject() instanceof AdsEdge) {
                    edges.add((AdsEdge) objectInClipboard.getObject());
                }
            }

            for (AdsBaseObject node : nodes) {
                node.getBounds().translate(5, 5);
                getNodes().add(node);
            }

            for (AdsEdge edge : edges) {
                AdsPin sourcePin = pins.get(edge.getSource().getId());
                AdsPin targetPin = pins.get(edge.getTarget().getId());
                if (sourcePin != null && targetPin != null) {
                    edge.source = sourcePin;
                    edge.target = targetPin;
                    List<Point> points = edge.getPoints();
                    for (Point p : points) {
                        p.translate(5, 5);
                    }
                    getEdges().add(edge);
                }
            }
        }
    }

    @Override
    public ClipboardSupport<? extends AdsPage> getClipboardSupport() {
        return new PageClipboardSupport();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ALGO_PAGE;
    }
    private static final String TYPE_TITLE = "Page";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
    private NodeUpdateSupport nodeUpdateSupport = null;

    public synchronized NodeUpdateSupport getNodeUpdateSupport() {
        if (nodeUpdateSupport == null) {
            nodeUpdateSupport = new NodeUpdateSupport();
        }
        return nodeUpdateSupport;
    }

    public void fireNodeUpdate() {
        if (nodeUpdateSupport != null) {
            nodeUpdateSupport.fireEvent(new NodeUpdateEvent(this));
        }
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
}
