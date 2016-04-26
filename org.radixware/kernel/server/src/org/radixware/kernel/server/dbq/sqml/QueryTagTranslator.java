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
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.sqml.Sqml.Tag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.JoinSqlBuilder;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.dbq.UpValSqlBuilder;
import org.radixware.kernel.server.dbq.sqml.QuerySqmlTranslator.EMode;
import org.radixware.kernel.server.meta.clazzes.IRadRefPropertyDef;
import org.radixware.kernel.server.meta.clazzes.RadDetailParentRefPropDef;
import org.radixware.kernel.server.meta.clazzes.RadInnatePropDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef;
import org.radixware.kernel.server.meta.clazzes.RadUserPropDef;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.server.meta.clazzes.RadDetailPropDef;


abstract class QueryTagTranslator<T extends Sqml.Tag> implements ITagTranslator<T> {

	private final SqlBuilder queryBuilder;
	private final EMode translationMode;

    protected QueryTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super();
		this.translationMode = translationMode;
		this.queryBuilder = queryBuilder;
    }

	public SqlBuilder getQueryBuilder() {
		return queryBuilder;
	}

	public EMode getTranslationMode() {
		return translationMode;
	}

    protected void printDbPropBuilderFieldSqlPres( final CodePrinter cp, final SqlBuilder builder, final Id propId, String tabAlias, final Tag tag) throws TagTranslateError {
        final RadPropDef prop = builder.getEntityClass().getPropById(propId);
        DdsColumnDef ddsProp = null;
        org.radixware.kernel.common.sqml.Sqml expression = null;
        if (prop instanceof RadInnatePropDef) {
            ddsProp = builder.getTable().getColumns().getById(propId, ExtendableDefinitions.EScope.ALL);
            expression = ddsProp.getExpression();
        } else if (prop instanceof RadSqmlPropDef) {
            expression =((RadSqmlPropDef) prop).getExpression();
        } else if (prop instanceof RadDetailPropDef) {
            final RadDetailPropDef detailProp = (RadDetailPropDef) prop;
            final JoinSqlBuilder joinBuilder = builder.getJoinBuilder(detailProp.getDetailReference(), true);
            printDbPropBuilderFieldSqlPres(cp, joinBuilder, detailProp.getJoinedPropId(), joinBuilder.getAlias(), tag);
            return;
        }
		if (expression != null) {
			cp.print('('); //TODO ? tabAlias (from Tag) in expressions
			cp.print(builder.translateSqml(expression));
			cp.print(')');
		} else if (ddsProp != null) {
            if (tabAlias == null)
                tabAlias = builder.getAlias();
            if (tabAlias != null && !tabAlias.isEmpty()) {
				cp.print(tabAlias);
				cp.print('.');
            }
			cp.print(ddsProp.getDbName());
		} else {
			throw new TagTranslateError(tag, new WrongFormatError("Unsupported column type \"" + prop.getClass().getName() + "\"", null));
		}
	}

    protected void printPropSqlPres(final CodePrinter cp, SqlBuilder builder, final Id propId, String tabAlias, final Tag tag) throws TagTranslateError {
        if (tabAlias == null)
            tabAlias = builder.getAlias();
		final RadPropDef prop = builder.getEntityClass().getPropById(propId);
        if (prop instanceof RadUserPropDef) {
            cp.print(UpValSqlBuilder.getValGetSql("'" + prop.getId() + "'", prop.getValType(), "'" + builder.getTable().getId() + "'", SqlBuilder.getPidScript(getQueryBuilder().getTable(), tabAlias)));
        } else if ((prop instanceof IRadRefPropertyDef) && ((IRadRefPropertyDef) prop).getReference() != null) {
            if (prop instanceof RadDetailParentRefPropDef) {
                builder = builder.getJoinBuilder(((RadDetailParentRefPropDef) prop).getDetailReference(), true);
                tabAlias = builder.getAlias();
            }
            cp.print('(');
            boolean isFirst = true;
            final DdsReferenceDef ref = ((IRadRefPropertyDef) prop).getReference();
            for (DdsReferenceDef.ColumnsInfoItem refProp : ref.getColumnsInfo()) {
                if (!isFirst) {
                    cp.print(',');
                }
                printDbPropBuilderFieldSqlPres(cp, builder, refProp.getChildColumnId(), tabAlias, tag);
                isFirst = false;
            }
            cp.print(')');
        } else {
            printDbPropBuilderFieldSqlPres(cp, builder, prop.getId(), tabAlias, tag);
        }
    }
}
