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

import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;

public final class UpValSqlBuilder {

	public static final String getIsDefinedSql(final String propIdLiteral, final EValType propValType, final String tableIdLiteral, final String pidLiteral) {
		return "RDX_Entity.isUserPropValDefined("+tableIdLiteral+", "+pidLiteral+", "+propIdLiteral+", "+propValType.getValue().toString()+")";
	}

	public static final String getValGetSql(final String propIdLiteral, final EValType valType, final String tableIdLiteral, final String pidLiteral) {

		String funcName = "";
		switch (valType) {
			case BOOL:
			case INT:
				funcName = "getUserPropInt";
				break;

			case NUM:
				funcName = "getUserPropNum";
				break;

			case STR:
				funcName = "getUserPropStr";
				break;

			case DATE_TIME:
				funcName = "getUserPropDateTime";
				break;

			case PARENT_REF:
			case OBJECT:
				funcName = "getUserPropRef";
				break;

			case BIN:
				funcName = "getUserPropBin";
				break;

			case BLOB:
				funcName = "getUserPropBlob";
				break;

			case CLOB:
				funcName = "getUserPropClob";
				break;

			case ARR_BOOL:
			case ARR_INT:
			case ARR_NUM:
			case ARR_STR:
			case ARR_DATE_TIME:
			case ARR_REF:
			case ARR_BIN:
			case ARR_CLOB:
			case ARR_BLOB:
				funcName = "getUserPropArrAsStr";
				break;

			default:
				throw new IllegalArgumentError("Value type \"" + valType.getName() + "\" is not supported in UpValSqlBuilder.getValGetSql()");
		}

		return "RDX_Entity."+funcName+"("+tableIdLiteral+", "+pidLiteral+", "+propIdLiteral+")";
	}
}
