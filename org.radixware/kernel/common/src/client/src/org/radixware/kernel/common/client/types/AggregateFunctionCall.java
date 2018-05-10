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

package org.radixware.kernel.common.client.types;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.exceptions.WrongFormatException;
import org.radixware.kernel.common.types.Id;


public class AggregateFunctionCall extends org.radixware.kernel.common.types.AggregateFunctionCall{
    
    public final static int DEFAULT_PRECISION = 3;
    private final int precision;
    
    public AggregateFunctionCall(final Id columnId, final EAggregateFunction function){
        this(columnId, function, DEFAULT_PRECISION);
    }
    
    public AggregateFunctionCall(final Id columnId, final EAggregateFunction function, final int precision){
        super(columnId, function);
        this.precision = precision;
    }    
    
    public int getPrecision(){
        return precision;
    }
    
    public static int getPrecisionForColumn(final Id columnId, final List<AggregateFunctionCall> functions){
        int precision = DEFAULT_PRECISION;
        for (AggregateFunctionCall functionCall: functions){
            if (Objects.equals(columnId, functionCall.getColumnId()) && functionCall.getPrecision()>-1){
                if (functionCall.getFunction()==EAggregateFunction.AVG){
                    return functionCall.getPrecision();
                }else{
                    precision = functionCall.getPrecision();
                }
            }
        }
        return precision;                
    }
    
    public static String writeToString(final List<AggregateFunctionCall> functions){
        final StringBuilder strBuilder = new StringBuilder();
        boolean firstFunction = true;
        for (AggregateFunctionCall function: functions){
            if (!firstFunction){
                strBuilder.append(';');
            }
            if (function.getColumnId()!=null){
                strBuilder.append(function.getColumnId().toString());
            }
            strBuilder.append(' ');
            strBuilder.append(function.getFunction().getValue());
            strBuilder.append(' ');
            strBuilder.append(String.valueOf(function.precision));
            firstFunction = false;
        }
        return strBuilder.toString();
    }
    
    public static List<AggregateFunctionCall> parseListFromString(final String functionsAsStr) throws WrongFormatException{
        final String[] arrFunctions = functionsAsStr.split(";");
        final List<AggregateFunctionCall> result = new LinkedList<>();
        for (String functionAsStr: arrFunctions){
            if (!functionAsStr.isEmpty()){
                result.add(parseFromString(functionAsStr));
            }
        }
        return result;
    }
    
    public static int findFunctionCall(final Id columnId, final EAggregateFunction function, final List<AggregateFunctionCall> functions){
        for (int i=0; i<functions.size()-1; i++){
            final AggregateFunctionCall functionCall = functions.get(i);
            if (Objects.equals(columnId, functionCall.getColumnId()) && function==functionCall.getFunction()){
                return i;
            }
        }
        return -1;
    }    
    
    public static AggregateFunctionCall parseFromString(final String functionAsStr) throws WrongFormatException{
        final String[] functionParams = functionAsStr.split(" ");
        int paramsCount = 0;
        for (String param: functionParams){
            if (!param.isEmpty()){
                paramsCount++;
            }
        }
        if (paramsCount<2){
            final String messageTemplate = "Failed to restore AggregateFunctionCall instance from string \'%1$s\'";
            throw new WrongFormatException(String.format(messageTemplate, functionAsStr));
        }
        final boolean isColumnIdDefined = paramsCount>2;
        int paramIndex = isColumnIdDefined ? 0 : 1;
        Id columnId = null;
        EAggregateFunction function = null;
        int precision = DEFAULT_PRECISION;
        for (String param: functionParams){
            if (!param.isEmpty()){
                switch(paramIndex){
                    case 0:{
                        columnId = Id.Factory.loadFrom(param);
                        break;
                    }
                    case 1:{
                        try{
                            function = EAggregateFunction.getForValue(param);
                        }catch(NoConstItemWithSuchValueError error){
                            final String messageTemplate = "Failed to restore AggregateFunctionCall instance from string \'%1$s\':\nUnknown function type: '%2$s'";
                            throw new WrongFormatException(String.format(messageTemplate, functionAsStr, param));
                        }
                        break;
                    }
                    case 2:{
                        try{
                            precision = Integer.parseInt(param);
                        }catch(NumberFormatException exception){
                            final String messageTemplate = "Failed to restore AggregateFunctionCall instance from string \'%1$s\':\n'%2$s' is not a number";
                            throw new WrongFormatException(String.format(messageTemplate, functionAsStr, param));
                        }
                        break;
                    }                  
                }
                paramIndex++;
            }
        }
        return new AggregateFunctionCall(columnId, function, precision);
    }
    
    
}
