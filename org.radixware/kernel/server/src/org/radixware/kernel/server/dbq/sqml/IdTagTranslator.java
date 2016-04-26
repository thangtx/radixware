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

import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.IdTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.SqlBuilder;
/**
 * Server has his own IdTag translation implementation in oder to optimize
 * @param <T>
 */
class IdTagTranslator<T extends IdTag> extends QueryTagTranslator<T>{

    protected IdTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
        final Id[] path = tag.getPath();
        if (path.length == 0)
            throw new TagTranslateError(tag, "Wrong IdTag format: Empty id path");
        else
            cp.printStringLiteral(String.valueOf(path[path.length-1]));
    }
}
