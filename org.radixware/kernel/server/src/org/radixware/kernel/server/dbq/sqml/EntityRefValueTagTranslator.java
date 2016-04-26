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
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef.ColumnInfo;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.sqml.tags.EntityRefValueTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValueToSqlConverter;
import org.radixware.kernel.server.dbq.DbQuery;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

class EntityRefValueTagTranslator <T extends EntityRefValueTag> extends QueryTagTranslator<T> {

    protected EntityRefValueTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
		if (getTranslationMode() == QuerySqmlTranslator.EMode.SQL_CONSTRUCTION) {
            final Pid pid = new Pid(getQueryBuilder().getArte(), tag.getReferencedTableId(), tag.getReferencedPidAsStr());
			if (tag.getPidTranslationMode() == EPidTranslationMode.AS_STR) {
                if (tag.isLiteral()) {
                    cp.print(ValueToSqlConverter.toSql(pid.toString(), EValType.STR));
                } else {
                    cp.print('?');
                    getQueryBuilder().addParameter(new DbQuery.InputEntityPidParam(pid));
                }
			} else if (tag.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS || tag.getPidTranslationMode() == EPidTranslationMode.SECONDARY_KEY_PROPS) {
				final DdsTableDef tab = pid.getTable();
				final DdsIndexDef.ColumnsInfo key;
				if (tag.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS) {
					key = tab.getPrimaryKey().getColumnsInfo();
				} else {
					key = tab.getIndices().getById(tag.getPidTranslationSecondaryKeyId(), ExtendableDefinitions.EScope.ALL).getColumnsInfo();
				}
				cp.print('(');
                final Entity ent = tag.isLiteral() ? getQueryBuilder().getArte().getEntityObject(pid) : null;
				for (byte i = 0; i < key.size(); i++) {
					if (i > 0) {
						cp.print(',');
					}
                    final ColumnInfo keyPropInfo = key.get(i);
                    final Id keyPropId = keyPropInfo.getColumnId();
                    if (tag.isLiteral()) {
                        cp.print(ValueToSqlConverter.toSql(ent.getProp(keyPropId), keyPropInfo.getColumn().getValType()));                        
                    } else {
        				cp.print('?');
    					getQueryBuilder().addParameter(new DbQuery.InputEntityPropParam(pid, keyPropId));
                    }
				}
				cp.print(')');
			} else {
				throw new TagTranslateError(tag);
			}
		} else
			throw new IllegalUsageError("Unsupported translation mode: " + getTranslationMode().toString());
	}
}
