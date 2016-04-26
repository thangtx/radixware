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

package org.radixware.kernel.designer.environment.upload;

import org.radixware.kernel.common.repository.dds.DdsScripts;
import org.radixware.kernel.common.repository.dds.DdsSegment;


class DdsSegmentUploader extends SegmentUploader<DdsSegment> {

    public DdsSegmentUploader(DdsSegment segment) {
        super(segment);
    }
    
    private void updateScripts() {
        final DdsSegment segment = getSegment();
        final DdsScripts scripts = segment.getScripts();
        final DdsScriptsUploader scriptsUpdater = new DdsScriptsUploader(scripts);
        scriptsUpdater.update();
    }

    @Override
    public void updateChildren() {
        super.updateChildren();
        updateScripts();
    }
}
