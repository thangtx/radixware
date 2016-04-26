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

package org.radixware.kernel.server.dbq;

import org.apache.xmlbeans.XmlException;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.server.sqml.Sqml;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.types.QueryTranslateResult;
import org.radixware.kernel.server.types.SqmlTranslateResult;
import org.radixware.schemas.xscml.SqmlDocument;

public final class SqmlTranslator extends QuerySqlBuilder {

	//Constructor
    private SqmlTranslator(final Arte arte, final DdsTableDef table, final Id entityClassId, final String alias) {
        super(arte, table, alias, EQueryContextType.OTHER);
        this.entityClassId = entityClassId == null && table != null ? RadClassDef.getEntityClassIdByTableId(table.getId()) : entityClassId;
    }

    @Override
	public RadClassDef getEntityClass() {
    	if (entityClass == null && entityClassId != null)
    		entityClass = getArte().getDefManager().getClassDef(entityClassId);
		return entityClass;
	}
    private RadClassDef entityClass = null;
    private final Id entityClassId;
    
    
    public static SqmlTranslateResult translate(final Arte arte, final String xsqml, final Id tableId, final Id entityClassId, final String alias) {
        return new SqmlTranslator(arte, tableId == null ? null : arte.getDefManager().getTableDef(tableId), entityClassId, alias).translate(xsqml);
    }
    public static SqmlTranslateResult translate(final Arte arte, final String xsqml, final DdsTableDef table, final Id entityClassId, final String alias) {
        return new SqmlTranslator(arte, table, entityClassId, alias).translate(xsqml);
    }
    public static SqmlTranslateResult translate(final Arte arte, final org.radixware.schemas.xscml.Sqml xsqml, final Id tableId, final Id entityClassId, final String alias) {
        return new SqmlTranslator(arte, tableId == null ? null : arte.getDefManager().getTableDef(tableId), entityClassId, alias).translate(xsqml);
    }
    public static SqmlTranslateResult translate(final Arte arte, final org.radixware.schemas.xscml.Sqml xsqml, final DdsTableDef table, final Id entityClassId, final String alias) {
        return new SqmlTranslator(arte, table, entityClassId, alias).translate(xsqml);
    }
    public static SqmlTranslateResult translate(final Arte arte, final Sqml sqml, final Id tableId, final Id entityClassId, final String alias) {
        return new SqmlTranslator(arte, tableId == null ? null : arte.getDefManager().getTableDef(tableId), entityClassId, alias).translate(sqml);
    }
    public static SqmlTranslateResult translate(final Arte arte, final Sqml sqml, final DdsTableDef table, final Id entityClassId, final String alias) {
        return new SqmlTranslator(arte, table, entityClassId, alias).translate(sqml);
    }
    
    public static QueryTranslateResult translateSingleQuery(final Arte arte, final Sqml sqml, final Id contextEntityId) {
        return new SqmlTranslator(arte, null, contextEntityId, null).translateQuery(sqml);
    }
    
    private SqmlTranslateResult translate(final String xsqml){
        final SqmlDocument expr;
 	   	try {
 			expr = SqmlDocument.Factory.parse(xsqml);
 	   	} catch (XmlException e) {
 			throw new WrongFormatError("Can't parse xsqml expression: "+ ExceptionTextFormatter.getExceptionMess(e), e);
 	   	}
 	   	return translate(expr.getSqml());
    }
    
    private SqmlTranslateResult translate(final org.radixware.schemas.xscml.Sqml xSqml) {
 	   	return translate(Sqml.Factory.loadFrom("SqmlTranslatorArgument", xSqml));
    }
    
    private QueryTranslateResult translateQuery(final Sqml sqml) {
        if(sqml == null) {
            return null;
        }
        addPropsFromSqml(sqml);
        getMainBuilder().querySql = new StringBuilder();
        final CharSequence resultSql = translateSqml(sqml);
        return new QueryTranslateResult(resultSql == null ? null : resultSql.toString(), getQueryParams());
    }
    
    private SqmlTranslateResult translate(final Sqml sqml){
        if (sqml == null)
            return null;
        addPropsFromSqml(sqml);
        getMainBuilder().querySql = new StringBuilder();
    	final CharSequence expressionSql = translateSqml(sqml);
        getMainBuilder().querySql = new StringBuilder();
        String joinsSql = null;
        if (getTable() != null) {
            appendTablesStr();
            joinsSql = getQuerySql();
        }
        return new SqmlTranslateResult(expressionSql == null ? null : expressionSql.toString(), joinsSql, getAlias(), getQueryParams());
    }
    
    public static String translateEventCode(final Id bundleId, final Id stringId) {
        return String.valueOf(bundleId) + "-" + String.valueOf(stringId);
    }

}
