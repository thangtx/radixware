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

package org.radixware.kernel.explorer.editors.sqmleditor;

import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.xscml.Sqml.Item.ParentCondition.Operator;

public enum ESqlConditionOperator {
    
    IS_NULL("is null",Operator.IS_NULL),
    IS_NOT_NULL("is not null",Operator.IS_NOT_NULL),
    EQUAL("=",Operator.EQUAL),
    NOT_EQUAL("<>",Operator.NOT_EQUAL),
    IN("in"),
    NOT_IN("not in"),
    LESS("<"),
    GREATER(">"),
    LESS_OR_EQUAL("<="),
    GREATER_OR_EQUAL(">="),
    LIKE("like"),
    BETWEEN("between");
    
    private final String text;
    private final Operator.Enum parentConditionOperator;
    
    private ESqlConditionOperator(final String text){
        this(text,null);
    }
    
    private ESqlConditionOperator(final String text, final Operator.Enum parentConditionOperator){
        this.text = text;
        this.parentConditionOperator = parentConditionOperator;
    }
  
    public final String getText(){
        return text;
    }
    
    public final Operator.Enum getParentConditionOperator(){
        return parentConditionOperator;
    }
    
    @SuppressWarnings({"PMD.DefaultLabelNotLastInSwitchStmt", "PMD.SwitchWithoutBreak", "fallthrough"})
    public boolean isApplicableToType(final EValType valType){
        if (!valType.isArrayType()){
            switch(valType){
                case STR:
                case INT:
                case NUM:{
                    if (this==LIKE){
                        return true;
                    }
                }
                default:{
                    if (this==LESS || this==GREATER || this==LESS_OR_EQUAL || this==GREATER_OR_EQUAL || this==BETWEEN){
                        return true;
                    }
                }
                case PARENT_REF:{
                    if (this==NOT_EQUAL || this==IN || this==NOT_IN){
                        return true;
                    }
                }            
                case BOOL:{
                    if (this==EQUAL){
                        return true;
                    }
                }
            }
        }
        return this==IS_NULL || this==IS_NOT_NULL;
    }
    
    public static ESqlConditionOperator findByParentConditionOperator(Operator.Enum parentConditionOperator){
        if (parentConditionOperator!=null){
            for (ESqlConditionOperator operator: ESqlConditionOperator.values()){
                if (operator.getParentConditionOperator()==parentConditionOperator){
                    return operator;
                }
            }
        }
        return null;
    }
}
