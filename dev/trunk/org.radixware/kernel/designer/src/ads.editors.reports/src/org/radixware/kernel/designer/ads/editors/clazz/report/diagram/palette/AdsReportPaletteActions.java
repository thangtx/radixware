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

package org.radixware.kernel.designer.ads.editors.clazz.report.diagram.palette;

import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.openide.util.Lookup;

/**
 * Empty stub for report palette actions.
 */
class AdsReportPaletteActions extends PaletteActions {

    @Override
    public Action[] getCustomCategoryActions(final Lookup category) {
        return null;
    }

    @Override
    public Action[] getCustomItemActions(final Lookup item) {
        return null;
    }

    @Override
    public Action[] getCustomPaletteActions() {
        return null;
    }

    @Override
    public Action[] getImportActions() {
        return null;
    }

    @Override
    public Action getPreferredAction(final Lookup item) {
        return null;
    }
}
