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
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef;

final class UserRefJoinSqlBuilder extends JoinSqlBuilder {
	protected final RadUserRefPropDef refProp;
    
//Constructor
    protected UserRefJoinSqlBuilder(
    	final QuerySqlBuilder mainBuilder,
    	final SqlBuilder      parentBuilder,
    	final RadUserRefPropDef refProp
    ) {
        super(
        	mainBuilder,
        	parentBuilder,
        	refProp.getDestinationTable(),
        	false
        );
        this.refProp = refProp;	
    }
    
	@Override
	public final RadClassDef getEntityClass(){
		if (entityClass == null) {
			entityClass = getArte().getDefManager().getClassDef(RadClassDef.getEntityClassIdByTableId(getTable().getId()));
		}
		return entityClass;
    }
	private RadClassDef entityClass = null;
	
    @Override
    protected final void appendJoinCondStr(){
        //append condition of join to Table of Parent SqlConstructor ( != Parent Table)
    	final StringBuilder qry = getMainBuilder().querySql;
    	qry.append(UpValSqlBuilder.getValGetSql(
    		"'"+refProp.getId()+"'",
    		EValType.PARENT_REF,
    		"'"+parentBuilder.getTable().getId()+"'",
    		getPidScript(parentBuilder.getTable(),parentBuilder.getAlias())// PID основной таблицы, по нему мы берем значение свойства типа ref
    	));//значение свойства типа Ref - PID который должен быть присоединен
        qry.append("=");
		qry.append(getPidScript(getTable(),getAlias()));
    }
    
}
