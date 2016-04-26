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

import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.algo.generation.AdsProgramWriter;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public class AdsProgramObject extends AdsBaseObject implements IJavaSource, IJmlSource {

    private Jml source;

    protected AdsProgramObject() {
        this(ObjectFactory.createNodeId(), DEFAULT_NAME);
    }

    protected AdsProgramObject(Id id, String name) {
        super(Kind.PROGRAM, id, name);
        source = Jml.Factory.newInstance(this, "Source");
        pins.add(new AdsPin());
        pins.add(new AdsPin());
    }

    protected AdsProgramObject(final AdsProgramObject node) {
        super(node);
        source = Jml.Factory.newCopy(this, node.source);
    }

    protected AdsProgramObject(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode) {
        super(xNode);
        source = Jml.Factory.loadFrom(this, xNode.getSource(), "Source");
    }

    public Jml getSource() {
        return source;
    }

    @Override
    public Jml getSource(String name) {
        return getSource();
    }

    public void setSource(Jml source) {
        if (!Utils.equals(this.source, source)) {
            this.source = source;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean isSourcePin(AdsPin pin) {
        return pins.indexOf(pin) > 0;
    }

    @Override
    public boolean isTargetPin(AdsPin pin) {
        return pins.indexOf(pin) == 0;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        source.visit(visitor, provider);
    }

    private class ProgramJavaSourceSupport extends JavaSourceSupport {

        public ProgramJavaSourceSupport() {
            super(AdsProgramObject.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsProgramWriter(this, AdsProgramObject.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new ProgramJavaSourceSupport();
    }

    @Override
    public void appendTo(org.radixware.schemas.algo.ScopeDef.Nodes.Node xNode, ESaveMode saveMode) {
        super.appendTo(xNode, saveMode);
        source.appendTo(xNode.addNewSource(), saveMode);
    }
    private static final String TYPE_TITLE = "Program Node";

    @Override
    public String getTypeTitle() {
        return TYPE_TITLE;
    }
}
