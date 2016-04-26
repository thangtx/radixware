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

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.scml.ITagTranslator;
import org.radixware.kernel.common.sqml.tags.*;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.dbq.sqml.QuerySqmlTranslator.EMode;


public class SqmlTagTranslatorFactory extends org.radixware.kernel.common.sqml.translate.SqmlTagTranslatorFactory {
	private final QuerySqmlTranslator.EMode transalatorMode;
	private final SqlBuilder queryBuilder;

	public SqmlTagTranslatorFactory(final SqlBuilder queryBuilder, final EMode transalatorMode) {
        super(new IProblemHandler() {
            @Override
            public void accept(final RadixProblem problem) {
                //do nothing because check is design time activity
            }
        });
		this.transalatorMode = transalatorMode;
		this.queryBuilder = queryBuilder;
	}

	@Override
	public ITagTranslator findTagTranslator(final Tag tag) {
		if (tag instanceof PropSqlNameTag) {
			return new PropSqlNameTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof ParentConditionTag) {
			return new ParentConditionTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof ParentRefPropSqlNameTag) {
			return new ParentRefPropSqlNameTagTranslator(queryBuilder, transalatorMode);
		}
		
		if (transalatorMode == EMode.QUERY_TREE_CONSTRUCTION) //optimization
			return new NullTagTranslator(); //Query tree does not depend on this tag;

		if (tag instanceof ThisTableSqlNameTag){
			return new ThisTableSqlNameTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof ConstValueTag){
			return new ConstValueTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof TypifiedValueTag){
			return new TypifiedValueTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof EntityRefParameterTag){
			return new EntityRefParameterTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof EntityRefValueTag){
			return new EntityRefValueTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof ThisTableRefTag){
			return new ThisTableRefTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof ParameterTag){
			return new ParameterTagTranslator(queryBuilder, transalatorMode);
		} else if (tag instanceof IdTag){
			return new IdTagTranslator(queryBuilder, transalatorMode);
		}  else if (tag instanceof EventCodeTag){
			return new EventCodeTagTranslator(queryBuilder, transalatorMode);
                }
		return super.findTagTranslator(tag);
	}

}
