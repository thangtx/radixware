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

package org.radixware.kernel.designer.common.dialogs.utils;

import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils.CheckResult;
import org.radixware.kernel.common.repository.Layer;


final class DbNameAcceptor implements IAdvancedAcceptor<String> {

    private final Layer root;

    public DbNameAcceptor(Layer layer) {
        this.root = layer;
    }

    @Override
    public IAcceptResult getResult(final String candidate) {
        final CheckResult result = DbNameUtils.checkRestriction(root, candidate);
        return new DefaultAcceptResult(result.valid, result.message);
    }
}
