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

import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IEnumDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ConstValueTag;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValueToSqlConverter;

class ConstValueTagTranslator<T extends ConstValueTag> extends QueryTagTranslator<T> {

    protected ConstValueTagTranslator(final SqlBuilder queryBuilder,  final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
		if (getTranslationMode() == QuerySqmlTranslator.EMode.SQL_CONSTRUCTION) {
			final Id id = tag.getEnumId();
			IEnumDef cs;
			try {
				cs = getQueryBuilder().getArte().getDefManager().getEnumDef(id);
			} catch (DefinitionNotFoundError e){
				//may be it is transparent const set
				if(id != null && id.getPrefix() == EDefinitionIdPrefix.ADS_ENUMERATION){
					try {
						cs = getQueryBuilder().getArte().getDefManager().getEnumDef(Id.Factory.loadFrom(EDefinitionIdPrefix.DDS_ENUM.getValue() + id.toString().substring(3)));
					} catch (DefinitionNotFoundError cantBeTransparent){
						throw e; // throw source exception
					}
				}else
					throw e;
			}
			final Object val = cs.getItems().findItemById(tag.getItemId(), EScope.ALL).getValue().toObject(cs.getItemType());
			cp.print(ValueToSqlConverter.toSql(val, cs.getItemType()));
		} else
			throw new IllegalUsageError("Unsupported translation mode: " + getTranslationMode().toString());
	}
}
