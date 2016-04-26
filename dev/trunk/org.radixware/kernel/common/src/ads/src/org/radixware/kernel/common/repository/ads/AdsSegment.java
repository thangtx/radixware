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

package org.radixware.kernel.common.repository.ads;

import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.platform.BuildPath;
import org.radixware.kernel.common.enums.ERepositorySegmentType;

import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.fs.IRepositoryAdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public class AdsSegment extends Segment<AdsModule> {

    private BuildPath buildPath = new BuildPath(this);

    @Override
    protected synchronized Module.Factory<AdsModule> getModuleFactory() {
        return AdsModule.Factory.getDefault();
    }

    protected AdsSegment(Layer layer) {
        super(layer);
    }

    @Override
    public ERepositorySegmentType getType() {
        return ERepositorySegmentType.ADS;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.SEGMENT;
    }

    public BuildPath getBuildPath() {
        return buildPath;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        buildPath.visit(visitor, provider);
    }

    @Override
    public ClipboardSupport<? extends Segment> getClipboardSupport() {
        return new SegmentClipboardSupport(AdsModule.class);
    }

    @Override
    public IRepositoryAdsSegment getRepository() {
        return (IRepositoryAdsSegment) super.getRepository();
    }
}
