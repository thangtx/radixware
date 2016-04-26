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
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.common.sqml.tags.ParentRefPropSqlNameTag;
import org.radixware.kernel.common.types.Id;

class ParentRefPropSqlNameTagTranslator<T extends ParentRefPropSqlNameTag> extends QueryTagTranslator<T> {

    protected ParentRefPropSqlNameTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
		if (getTranslationMode() == QuerySqmlTranslator.EMode.QUERY_TREE_CONSTRUCTION){
			getQueryBuilder().addParentRefProp(tag.getReferenceIds(), tag.getPropId(), null, getQueryBuilder().getTable());
		} else {
			final StringBuilder fullPropId = new StringBuilder("");
			for (Id refId : tag.getReferenceIds()) {
				fullPropId.append(refId.toString());
			}
			fullPropId.append(tag.getPropId().toString());
			final SqlBuilder.Field f = getQueryBuilder().getDestField(fullPropId.toString());
            printPropSqlPres(cp, f.getBuilder(), tag.getPropId(), null, tag);
		}
    }

}
