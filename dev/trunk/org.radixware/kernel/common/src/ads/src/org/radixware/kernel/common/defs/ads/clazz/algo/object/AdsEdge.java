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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsEdgeWriter;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class AdsEdge extends AdsClassMember implements IJavaSource, IJmlSource, IProfileable {

    public static final String EMPTY_NAME = "";//edge";
    AdsPin source;
    AdsPin target;
    private List<Point> points = new ArrayList<Point>();
    private EEventSeverity traceSeverity = EEventSeverity.DEBUG;
    private Jml traceSource = Jml.Factory.newInstance(this, "TraceSource");
    protected final AdsProfileSupport profileSupport = new AdsProfileSupport(this);

    protected AdsEdge() {
        super(ObjectFactory.createEdgeId(), EMPTY_NAME);
    }

    protected AdsEdge(AdsPin source, AdsPin target) {
        this();
        this.source = source;
        this.target = target;
    }

    protected AdsEdge(final AdsEdge edge, AdsPin source, AdsPin target) {
        this();

        traceSeverity = edge.traceSeverity;
        traceSource = Jml.Factory.newCopy(this, edge.traceSource);

        if (edge.points != null) {
            for (Point point : edge.points) {
                points.add(new Point(point));
            }
        }

        this.source = source;
        this.target = target;
    }

    protected AdsEdge(org.radixware.schemas.algo.ScopeDef.Edges.Edge xEdge) {
        super(xEdge);

        traceSeverity = EEventSeverity.getForValue(xEdge.getTraceSeverity());
        traceSource = Jml.Factory.loadFrom(this, xEdge.getTraceSource(), "Source");

        points.clear();
        for (org.radixware.schemas.algo.ScopeDef.Edges.Edge.Points.Point point : xEdge.getPoints().getPointList()) {
            points.add(new Point(point.getX(), point.getY()));
        }

        source = ObjectFactory.createPin(xEdge.getSourceId());
        target = ObjectFactory.createPin(xEdge.getTargetId());

        if (xEdge.getProfileInfo() != null) {
            profileSupport.loadFrom(xEdge.getProfileInfo());
        }
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
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsAlgoClassDef) {
                return (AdsAlgoClassDef) owner;
            }
        }
        return null;
    }

//    public void setStructModified() {
//        getOwnerClass().setStructModified();
//    }
    public AdsPin getSource() {
        return source;
    }

    public AdsPin getTarget() {
        return target;
    }

    public void setSource(AdsPin source) {
        if (!Utils.equals(this.source, source)) {
            this.source = source;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setTarget(AdsPin target) {
        if (!Utils.equals(this.target, target)) {
            this.target = target;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Jml getTraceSource() {
        return traceSource;
    }

    @Override
    public Jml getSource(String name) {
        return getTraceSource();
    }

    public void setTraceSource(Jml traceSource) {
        if (!Utils.equals(this.traceSource, traceSource)) {
            this.traceSource = traceSource;
            setEditState(EEditState.MODIFIED);
        }
    }

    public EEventSeverity getTraceSeverity() {
        return traceSeverity;
    }

    public void setTraceSeverity(EEventSeverity traceSeverity) {
        if (!Utils.equals(this.traceSeverity, traceSeverity)) {
            this.traceSeverity = traceSeverity;
            setEditState(EEditState.MODIFIED);
        }
    }

    public void setPoints(List<Point> points) {
        if (!Utils.equals(this.points, points)) {
            this.points = points;
            setEditState(EEditState.MODIFIED);
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public Rectangle getBounds() {
        int x1 = Integer.MAX_VALUE;
        int x2 = Integer.MIN_VALUE;
        int y1 = Integer.MAX_VALUE;
        int y2 = Integer.MIN_VALUE;

        for (Point point : points) {
            x1 = Math.min(x1, point.x);
            x2 = Math.max(x2, point.x);
            y1 = Math.min(y1, point.y);
            y2 = Math.max(y2, point.y);
        }

        return new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        traceSource.visit(visitor, provider);
    }

    public void appendTo(org.radixware.schemas.algo.ScopeDef.Edges.Edge xEdge, AdsDefinition.ESaveMode saveMode) {
        super.appendTo(xEdge, saveMode);

        assert source != null;
        xEdge.setSourceId(source.getId());

        assert target != null;
        xEdge.setTargetId(target.getId());

        xEdge.setTraceSeverity(traceSeverity.getValue());
        traceSource.appendTo(xEdge.addNewTraceSource(), saveMode);

        if (isProfileable() && profileSupport.isProfiled()) {
            profileSupport.appendTo(xEdge.addNewProfileInfo());
        }

        xEdge.addNewPoints();
        for (Point point : points) {
            org.radixware.schemas.algo.ScopeDef.Edges.Edge.Points.Point xPoint = xEdge.getPoints().addNewPoint();
            xPoint.setX(point.x);
            xPoint.setY(point.y);
        }
    }

    private class EdgeJavaSourceSupport extends JavaSourceSupport {

        public EdgeJavaSourceSupport() {
            super(AdsEdge.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsEdgeWriter(this, AdsEdge.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new EdgeJavaSourceSupport();
    }

    private class EdgeClipboardSupport extends AdsClipboardSupport<AdsEdge> {

        public EdgeClipboardSupport() {
            super(AdsEdge.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.algo.ScopeDef.Edges.Edge xEdge = org.radixware.schemas.algo.ScopeDef.Edges.Edge.Factory.newInstance();
            AdsEdge.this.appendTo(xEdge, ESaveMode.NORMAL);
            return xEdge;
        }

        @Override
        protected AdsEdge loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.algo.ScopeDef.Edges.Edge xEdge = (org.radixware.schemas.algo.ScopeDef.Edges.Edge) xmlObject;
            return ObjectFactory.createEdge(xEdge);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsEdge> getClipboardSupport() {
        return new EdgeClipboardSupport();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.ALGO_EDGE;
    }
    private static final String TYPE_TITLE = "Edge";

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
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_ALGORITHM;
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
