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
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsThrowWriter;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.utils.Utils;


public class AdsThrowObject extends AdsBaseObject implements IJavaSource, IJmlSource {

    private Id exceptionId = null;
    private Jml parameters;

    protected AdsThrowObject() {
        this(ObjectFactory.createNodeId(), EMPTY_NAME);
    }

    protected AdsThrowObject(Id id, String name) {
        super(Kind.THROW, id, EMPTY_NAME);
        parameters = Jml.Factory.newInstance(this, "Parameters");
        pins.add(new AdsPin());
    }

    protected AdsThrowObject(final AdsThrowObject node) {
        super(node);
        exceptionId = node.exceptionId;
        parameters = Jml.Factory.newCopy(this, node.parameters);
    }

    protected AdsThrowObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        exceptionId = xNode.getExceptionId();
        parameters = Jml.Factory.loadFrom(this, xNode.getParameters(), "Source");
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return false;
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return pins.contains(pin);
    }

    public AdsExceptionClassDef getExceptionDef() {
        if (exceptionId == null) {
            return null;
        }
        try {
            return (AdsExceptionClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(getOwnerDefinition()).findById(exceptionId).get();
        } catch (Exception e) {
            return null;
        }
    }

    public void setExceptionDef(AdsExceptionClassDef exceptionDef) {
        if (!Utils.equals(getExceptionDef(), exceptionDef)) {
            exceptionId = (exceptionDef == null) ? null : exceptionDef.getId();
            setEditState(EEditState.MODIFIED);
        }
    }

    public Jml getParameters() {
        return parameters;
    }

    @Override
    public Jml getSource(String name) {
        return getParameters();
    }

    public void setParameters(Jml parameters) {
        if (!Utils.equals(this.parameters, parameters)) {
            this.parameters = parameters;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        AdsExceptionClassDef exDef = getExceptionDef();
        if (exDef != null) {
            list.add(exDef);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        parameters.visit(visitor, provider);
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        xNode.setExceptionId(exceptionId);
        parameters.appendTo(xNode.addNewParameters(), saveMode);
    }

    private class ThrowJavaSourceSupport extends JavaSourceSupport {

        public ThrowJavaSourceSupport() {
            super(AdsThrowObject.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsThrowWriter(this, AdsThrowObject.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new ThrowJavaSourceSupport();
    }
    private static final String TYPE_TITLE = "Throw Node";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}
