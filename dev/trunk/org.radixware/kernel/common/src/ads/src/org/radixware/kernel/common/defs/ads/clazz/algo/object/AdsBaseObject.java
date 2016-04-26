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

import java.util.List;
import java.util.HashMap;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.utils.Utils;


public class AdsBaseObject extends AdsClassMember implements IProfileable {

    public static final String DEFAULT_NAME = "unknown";
    public static final String EMPTY_NAME = "";//node";
    protected final AdsProfileSupport profileSupport = new AdsProfileSupport(this);

    public enum Kind {

        START(0),
        PROGRAM(1),
        SCOPE(11),
        INCLUDE(41),
        FINISH(8),
        RETURN(7),
        TERMINATE(9),
        THROW(21),
        CATCH(22),
        FORK(31),
        MERGE(32),
        EMPTY(3),
        APP(101),
        NOTE(14),
        VAR(15);
        private final int value;

        private Kind(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return toString();
        }

        public static Kind getForValue(final int value) {
            for (Kind v : Kind.values()) {
                if (v.getValue() == value) {
                    return v;
                }
            }
            throw new NoConstItemWithSuchValueError("Kind has no item with value: " + String.valueOf(value),value);
        }
    }

    private Kind kind;
    private Rectangle bounds = null;
    protected AdsDefinitions<AdsPin> pins = new AdsDefinitions<AdsPin>(this);

    protected AdsBaseObject(Kind kind, Id id, String name) {
        super(id, name);
        this.kind = kind;
    }

    protected AdsBaseObject(final AdsBaseObject node) {
        this(node.getKind(), ObjectFactory.createNodeId(), node.getName());
        if (node.bounds != null) {
            bounds = new Rectangle(node.bounds);
        }
        for (AdsPin pin : node.pins) {
            pins.add(new AdsPin(pin));
        }
    }

    protected AdsBaseObject(final org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        kind = Kind.getForValue(xNode.getKind());
        bounds = new Rectangle(xNode.getX(), xNode.getY(), xNode.getWidth(), xNode.getHeight());
        if (xNode.getProfileInfo() != null) {
            profileSupport.loadFrom(xNode.getProfileInfo());
        }
        for (org.radixware.schemas.algo.ScopeDef.Nodes.Node.Pins.Pin xPin : xNode.getPins().getPinList()) {
            if (xPin.getName() == null) // fix later
            {
                xPin.setName("");
            }
            pins.add(new AdsPin(xPin));
        }
    }

    public Kind getKind() {
        return kind;
    }

    public AdsPage getOwnerPage() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsPage) {
                return (AdsPage) owner;
            }
        }
        return null;
    }

    @Override
    public AdsAlgoClassDef getOwnerClass() {
        return getOwnerPage().getOwnerClass();
    }

//    public void setStructModified() {
//        getOwnerClass().setStructModified();
//    }

    public List<AdsPin> getPins() {
        List<AdsPin> list = new ArrayList<AdsPin>(this.pins.size());
        list.addAll(this.pins.list());
        return list;
    }

    public void setPins(List<AdsPin> list) {
        if (list.equals(pins.list())) {
            return;
        }
        pins.clear();
        for (AdsPin pin : list) {
            pins.add(pin);
        }
        setEditState(EEditState.MODIFIED);
    }

    public List<AdsPin> getSourcePins() {
        List<AdsPin> list = new ArrayList<AdsPin>(pins.size());
        for (AdsPin pin : pins) {
            if (isSourcePin(pin)) {
                list.add(pin);
            }
        }
        return list;
    }

    public void setSourcePins(List<AdsPin> pins) {
        List<AdsPin> list = new ArrayList<AdsPin>();
        for (AdsPin pin : getPins()) {
            if (isTargetPin(pin)) {
                list.add(pin);
            }
        }
        list.addAll(pins);
        setPins(list);
    }

    public List<AdsPin> getTargetPins() {
        List<AdsPin> list = new ArrayList<AdsPin>(pins.size());
        for (AdsPin pin : pins) {
            if (isTargetPin(pin)) {
                list.add(pin);
            }
        }
        return list;
    }

    public void setTargetPins(List<AdsPin> pins) {
        List<AdsPin> list = new ArrayList<AdsPin>(pins);
        for (AdsPin pin : getPins()) {
            if (isSourcePin(pin)) {
                list.add(pin);
            }
        }
        setPins(list);
    }

    public boolean isSourcePin(AdsPin pin) {
        return false;
    }

    public boolean isTargetPin(AdsPin pin) {
        return false;
    }

    /*
     *  sync internal data
     */
    public void sync() {
    }

    protected void syncOrigPins(AdsPage page, boolean bUseStart) {
        AdsPage ownerPage = getOwnerPage();

        List<AdsPin> pinList = getPins();
        ArrayList<AdsBaseObject> nodes = new ArrayList<AdsBaseObject>();

        if (page != null) {
            for (AdsBaseObject node : page.getNodes()) {
                if ((node instanceof AdsReturnObject) || (bUseStart && (node instanceof AdsStartObject))) {
                    nodes.add(node);
                }
                if (bUseStart && (node instanceof AdsStartObject) && pinList.size() > 0) { // !!! importer algorithm bug
                    if (pinList.get(0).getOrigId() == null) {
                        pinList.get(0).setOrigId(node);
                    }
                }
            }
            Collections.sort(nodes, new Comparator<AdsBaseObject>() {
                @Override
                public int compare(AdsBaseObject node1, AdsBaseObject node2) {
                    if (node1 instanceof AdsStartObject) {
                        return -1;
                    }
                    if (node2 instanceof AdsStartObject) {
                        return +1;
                    }
                    return node1.bounds.x >= node2.bounds.x ? +1 : -1;
                }
            });
        }

        List<Id> ids = new ArrayList<Id>();
        for (AdsPin pin : pinList) {
            ids.add(pin.getOrigId());
        }

        List<Id> idsNew = new ArrayList<Id>();
        for (AdsBaseObject node : nodes) {
            idsNew.add(node.getId());
        }

        if (ids.equals(idsNew)) {
            for (int i = 0; i < pinList.size(); i++) {
                AdsPin pin = pinList.get(i);
                AdsBaseObject node = nodes.get(i);
                pin.setName(node instanceof AdsStartObject ? "" : node.getName());
            }
        }

        HashMap<Id, AdsPin> idPins = new HashMap<Id, AdsPin>();
        for (int i = 0; i < ids.size(); i++) {
            Id id = ids.get(i);
            AdsPin pin = pinList.get(i);
            if (idsNew.contains(id)) {
                idPins.put(id, pin);
            } else {
                AdsDefinitions<AdsEdge> edges = ownerPage.getEdges();
                for (int j = 0; j < edges.size(); j++) {
                    AdsEdge edge = edges.get(j);
                    if (pin.equals(edge.getSource()) || pin.equals(edge.getTarget())) {
                        ownerPage.remove(edge);
                    }
                }
            }
        }

        List<AdsPin> pinsNew = new ArrayList<AdsPin>();
        for (int i = 0; i < nodes.size(); i++) {
            AdsBaseObject node = nodes.get(i);
            Id origId = idsNew.get(i);
            AdsPin pinNew = idPins.get(origId);
            if (pinNew == null) {
                pinNew = new AdsPin();
                pinNew.setOrigId(origId);
            }
            pinNew.setName(node instanceof AdsStartObject ? "" : node.getName());
            pinsNew.add(pinNew);
        }

        setPins(pinsNew);
    }

    @Override
    public boolean delete() {
        AdsPage page = getOwnerPage();
        AdsDefinitions<AdsEdge> edges = page.getEdges();
        for (int i = edges.size() - 1; i >= 0; i--) {
            AdsEdge edge = edges.get(i);
            if (pins.contains(edge.getSource()) || pins.contains(edge.getTarget())) {
                edge.delete();
            }
        }
        return super.delete();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        if (!Utils.equals(this.bounds, bounds)) {
            this.bounds = bounds;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        xNode.setKind(getKind().getValue());
        xNode.setX(bounds.x);
        xNode.setY(bounds.y);
        xNode.setWidth(bounds.width);
        xNode.setHeight(bounds.height);
        xNode.addNewPins();
        if (isProfileable() && profileSupport.isProfiled()) {
            profileSupport.appendTo(xNode.addNewProfileInfo());
        }
        for (AdsPin pin : pins) {
            pin.appendTo(xNode.getPins().addNewPin(), saveMode);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        pins.visit(visitor, provider);
    }

    private class BaseNodeClipboardSupport extends AdsClipboardSupport<AdsBaseObject> {

        public BaseNodeClipboardSupport() {
            super(AdsBaseObject.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode = org.radixware.schemas.algo.ScopeDef.Nodes.Node.Factory.newInstance();
            AdsBaseObject.this.appendTo(xNode, ESaveMode.NORMAL);
            return xNode;
        }

        @Override
        protected AdsBaseObject loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode = (org.radixware.schemas.algo.ScopeDef.Nodes.Node) xmlObject;
            return ObjectFactory.createNode(xNode);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsBaseObject> getClipboardSupport() {
        return new BaseNodeClipboardSupport();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ALGO_NODE;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.WORKFLOW.calcIcon(getKind());
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

    @Override
    public AdsProfileSupport getProfileSupport() {
        if (isProfileable()) {
            return profileSupport;
        } else {
            return null;
        }
    }

    @Override
    public boolean isProfileable() {
        return getUsageEnvironment() == ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return this;
    }
}
