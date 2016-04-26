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

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.DbQuery.InputTypifiedValParam;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.common.sqml.tags.TypifiedValueTag;
import org.radixware.kernel.common.utils.ValueToSqlConverter;
import org.radixware.kernel.server.dbq.DbQuery;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;

class TypifiedValueTagTranslator<T extends TypifiedValueTag> extends QueryTagTranslator<T>{

    protected TypifiedValueTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
		if (getTranslationMode() == QuerySqmlTranslator.EMode.SQL_CONSTRUCTION) {
			final EValType valType;
			final Id propOwnerId = tag.getPropOwnerId();
			if (propOwnerId == null)
				throw new TagTranslateError(tag);
			if (propOwnerId.getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
				final DdsTableDef tab = getQueryBuilder().getArte().getDefManager().getTableDef(propOwnerId);
				final DdsColumnDef prop = tab.getColumns().getById(tag.getPropId(), ExtendableDefinitions.EScope.ALL);
				valType = prop.getValType();
			} else {
				final RadClassDef clsDef = getQueryBuilder().getArte().getDefManager().getClassDef(propOwnerId);
				final RadPropDef prop = clsDef.getPropById(tag.getPropId());
				valType = prop.getValType();
			}
            if (tag.isLiteral())
                cp.print(ValueToSqlConverter.toSql(tag.getValue(), valType));
            else{
                cp.print("?");
                final Object val = tag.getValue().toObject(valType);
                final InputTypifiedValParam inValParam = new DbQuery.InputTypifiedValParam( val, valType);
                getQueryBuilder().addParameter(inValParam);
            }
		} else
			throw new IllegalUsageError("Unsupported translation mode: " + getTranslationMode().toString());
	}
}
