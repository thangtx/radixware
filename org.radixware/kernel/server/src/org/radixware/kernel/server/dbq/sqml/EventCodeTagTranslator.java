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

package org.radixware.kernel.server.dbq.sqml;

import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.EventCodeTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.dbq.SqmlTranslator;

/**
 * @param <T>
 */
class EventCodeTagTranslator<T extends EventCodeTag> extends QueryTagTranslator<T> {

    protected EventCodeTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final EventCodeTag tag, final CodePrinter cp) {
        cp.printStringLiteral(SqmlTranslator.translateEventCode(tag.getBundleId(), tag.getStringId()));
    }
}
