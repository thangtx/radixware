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

package org.radixware.kernel.server.types;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.DbQuery.Param;
import org.radixware.kernel.server.dbq.GroupQuery.FilterParam;

public final class SqmlTranslateResult {
	final String expressionSql;
	final String joinsSql;
	final String mainTableAlias;
    private final List<Param> parameters;

	public SqmlTranslateResult(
		final String expressionSql,
		final String joinsSql,
		final String mainTableAlias,
        final List<Param> parameters
	){
		this.expressionSql = expressionSql;
		this.joinsSql = joinsSql;
		this.mainTableAlias = mainTableAlias;
	        this.parameters = parameters;
    }
	
	@Override
	public String toString(){
		return "{\n\texpressionSql = \"" + expressionSql + "\"" +
			   "\n\tjoinsSql = \"" + joinsSql + "\"" + 
			   "\n\tmainTableAlias = \"" + mainTableAlias + "\"" +
			   "\n}";
	}

	public String getExpressionSql() {
		return expressionSql;
	}

	public String getJoinsSql() {
		return joinsSql;
	}

	public String getMainTableAlias() {
		return mainTableAlias;
	}

    public List<Param> getParameters() {
        return parameters;
    }
    
    public List<Id> getParameterIds() {
        List<Id> res = new ArrayList<Id>();
        for (Param p : parameters){
            if (p instanceof FilterParam){
                res.add(((FilterParam)p).getId());
            }
        }
        return res;
    }
    
}

