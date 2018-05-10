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
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.IllegalUsageError;
import org.radixware.kernel.common.exceptions.TagTranslateError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.enums.EPidTranslationMode;
import org.radixware.kernel.common.sqml.tags.EntityRefParameterTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.SqlBuilder;
import org.radixware.kernel.server.dbq.SelectQuery;

class EntityRefParameterTagTranslator<T extends EntityRefParameterTag> extends QueryTagTranslator<T> {

    protected EntityRefParameterTagTranslator(final SqlBuilder queryBuilder, final QuerySqmlTranslator.EMode translationMode) {
        super(queryBuilder, translationMode);
    }

    @Override
    public void translate(final T tag, final CodePrinter cp) {
        if (getTranslationMode() == QuerySqmlTranslator.EMode.SQL_CONSTRUCTION) {
            final Id paramterId = tag.getParameterId();
            if (tag.getPidTranslationMode() == EPidTranslationMode.AS_STR) {                
                if (tag.isExpressionList()){
                    if (getQueryBuilder().isParameterDefined(paramterId)){
                        final int numberOfItems = getQueryBuilder().getNumberOfItemsInParameterValue(paramterId);
                        if (numberOfItems>0){
                            cp.print("( ");
                            for (int i=0; i<numberOfItems; i++){
                                if (i>0){
                                    cp.print(", ");
                                }
                                cp.print("?");
                                getQueryBuilder().addParameter(new SelectQuery.FilterKeyAsPidStrParam(paramterId, i));
                            }
                            cp.print(" )");
                        }else{
                            cp.print("(select NULL from DUAL where 1=2)");
                        }
                    }else{
                        cp.print("( ? )");
                        getQueryBuilder().addParameter(new SelectQuery.FilterKeyAsPidStrParam(paramterId));                        
                    }
                }else{
                    cp.print('?');
                    getQueryBuilder().addParameter(new SelectQuery.FilterKeyAsPidStrParam(paramterId));
                }
            } else if (tag.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS || tag.getPidTranslationMode() == EPidTranslationMode.SECONDARY_KEY_PROPS) {
                final DdsTableDef tab = getQueryBuilder().getArte().getDefManager().getTableDef(tag.getReferencedTableId());
                final DdsIndexDef.ColumnsInfo key;
                if (tag.getPidTranslationMode() == EPidTranslationMode.PRIMARY_KEY_PROPS) {
                    key = tab.getPrimaryKey().getColumnsInfo();
                } else {
                    key = tab.getIndices().getById(tag.getPidTranslationSecondaryKeyId(), ExtendableDefinitions.EScope.ALL).getColumnsInfo();
                }
                if (tag.isExpressionList()){
                    if (getQueryBuilder().isParameterDefined(paramterId)){
                        final int numberOfItems = getQueryBuilder().getNumberOfItemsInParameterValue(paramterId);
                        if (numberOfItems>0){                        
                            cp.print("( ");                        
                            for (int i=0; i<numberOfItems; i++){
                                if (i>0){
                                    cp.print(", ");
                                }
                                printKeyColumns(cp, paramterId, tab, key, i);
                            }
                            cp.print(" )");
                        }else{
                            cp.print("(select NULL from DUAL where 1=2)");
                        }
                    }else{
                        cp.print("( ? )");
                        getQueryBuilder().addParameter(new SelectQuery.FilterKeyAsPidStrParam(paramterId));                        
                    }
                }else{
                    printKeyColumns(cp, paramterId, tab, key, -1);
                }
            } else {
                throw new TagTranslateError(tag);
            }
        } else {
            throw new IllegalUsageError("Unsupported translation mode: " + getTranslationMode().toString());
        }
    }
    
    private void printKeyColumns(final CodePrinter cp, final Id parameterId, final DdsTableDef table, final DdsIndexDef.ColumnsInfo key, final int arrayIndex){
        if (key.size()==1){            
            cp.print('?');
            getQueryBuilder().addParameter( new SelectQuery.FilterKeyColumnParam(parameterId, table, key.get(0).getColumn(), arrayIndex) );
        }else{
            cp.print('(');
            for (byte i = 0; i < key.size(); i++) {
                if (i > 0) {
                    cp.print(',');
                }
                cp.print('?');
                getQueryBuilder().addParameter( new SelectQuery.FilterKeyColumnParam(parameterId, table, key.get(i).getColumn(), arrayIndex) );
            }
            cp.print(')');        
        }
    }
}
