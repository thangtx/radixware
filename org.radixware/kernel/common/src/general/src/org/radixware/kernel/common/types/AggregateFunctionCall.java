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

package org.radixware.kernel.common.types;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.enums.EAggregateFunction;

public class AggregateFunctionCall {   
    
    private final Id columnId;
    private final EAggregateFunction functionType;
    
    public AggregateFunctionCall(final Id columnId, final EAggregateFunction function){
        this.columnId = columnId;
        functionType = function;
    }

    public Id getColumnId() {
        return columnId;
    }

    public EAggregateFunction getFunction() {
        return functionType;
    }
    
    public static org.radixware.schemas.eas.AggregateFunctions writeToXml(final List<AggregateFunctionCall> functions, final org.radixware.schemas.eas.AggregateFunctions xml){
        if (functions!=null && !functions.isEmpty()){
            final org.radixware.schemas.eas.AggregateFunctions xmlFunctions;
            if (xml==null){
                xmlFunctions = org.radixware.schemas.eas.AggregateFunctions.Factory.newInstance();
            }else{
                xmlFunctions = xml;
            }
            org.radixware.schemas.eas.AggregateFunctions.FunctionCall xmlFunction;
            for (AggregateFunctionCall func: functions){
                xmlFunction = xmlFunctions.addNewFunctionCall();
                if (func.columnId!=null){
                    xmlFunction.setColumnId(func.columnId);
                }
                xmlFunction.setFunctionName(func.functionType);
            }
            return xmlFunctions;            
        }else{
            return null;
        }
    }
    
    public static List<AggregateFunctionCall> parseFromXml(final org.radixware.schemas.eas.AggregateFunctions xml){
        final List<AggregateFunctionCall> result = new LinkedList<>();
        if (xml!=null && xml.getFunctionCallList()!=null){
            for (org.radixware.schemas.eas.AggregateFunctions.FunctionCall functionCall: xml.getFunctionCallList()){
                result.add(new AggregateFunctionCall(functionCall.getColumnId(), functionCall.getFunctionName()));
            }
        }
        return result;
    }        
}
