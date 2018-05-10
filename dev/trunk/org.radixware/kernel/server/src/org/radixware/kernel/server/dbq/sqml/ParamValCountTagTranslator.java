/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.server.dbq.sqml;

import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.tags.ParamValCountTag;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.dbq.SelectQuery;


final class ParamValCountTagTranslator <T extends ParamValCountTag> extends QueryTagTranslator<T> {
    
    protected ParamValCountTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode){
        super(queryBuilder, translationMode);    
    }   

    @Override
    public void translate(final T tag, final CodePrinter codePrinter) {
        if (getTranslationMode() == QuerySqmlTranslator.EMode.SQL_CONSTRUCTION) {
            codePrinter.print("?");
            getQueryBuilder().addParameter(new SelectQuery.ValuesCountInFilterParam(tag.getParameterId()));            
        }else{
            throw new IllegalUsageError("Unsupported translation mode: " + getTranslationMode().toString());
        }
    }        

}
