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

package org.radixware.kernel.designer.dds.editors.diagram.widgets;

import org.netbeans.api.visual.widget.Scene;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;

/**
 * {@linkplain DdsColumnDef widget}.
 */
class DdsColumnWidget extends ImageLabelWidget {

    protected DdsColumnWidget(Scene scene, DdsColumnDef column) {
        super(scene, column.getIcon().getImage(), column.getName());

        if (column.isPrimaryKey()) {
            this.setUnderline(true);
        }
        if (column.isNotNull()) {
            this.setBold(true);
        }
        if (column.isDeprecated()) {
            this.setStrykeOut(true);
        }
    }
}
