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

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsTableDef;

public abstract class JoinSqlBuilder extends SqlBuilder {
    protected final QuerySqlBuilder mainBuilder;
    protected final SqlBuilder      parentBuilder;
    protected final boolean         isChildRef;
    
//Constructor
    protected JoinSqlBuilder(
    	final QuerySqlBuilder mainBuilder,
    	final SqlBuilder      parentBuilder,
    	final DdsTableDef       table,
    	final boolean         isChildRef
    ) {
        super(
			mainBuilder.getArte(),
			table,SqlBuilder.TABLE_ALIAS_PREFIX + mainBuilder.incrementTableCount(),
			mainBuilder.getQueryCntxType(),
			mainBuilder.getAliasPolicy()
		);
        this.mainBuilder   = mainBuilder; 
        this.parentBuilder = parentBuilder; 
        this.isChildRef    = isChildRef;
    }
    
//Protected methods
    @Override
	protected final QuerySqlBuilder getMainBuilder(){
        return mainBuilder;
    }
    
    @Override
	public final void addParameter(final DbQuery.Param param){
        mainBuilder.addParameter(param);
    }

    protected final static String getJoinHashKey(final Id refId,final boolean bChild){
        return (bChild ? "C" : "P") + refId.toString();
    }
    
    abstract protected void appendJoinCondStr();

    void appendJoinTypeStr() {
        getMainBuilder().querySql.append("LEFT");
    }
}
