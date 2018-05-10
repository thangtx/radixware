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

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadPropertyDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.html.Table;
import org.radixware.kernel.common.types.Id;


public class SelectorColumnsStatistic {
    
    public static interface ITable{
        void setDimension(int rowCount, int colCount);
        void addTitledRow(int index, String title);
        void addTitledColumn(int index, String title);
        void setCellData(int rowIndex, int colIndex, String data);
        void setColSpan(int rowIndex, int colIndex, int span);
    }
    
    private final static class HtmlTable implements ITable{
        
        private final Table htmlTable = new Table();
        
        public HtmlTable(final boolean isFirstHeaderCellEmpty){
            htmlTable.setAttr("border", "1");
            if (isFirstHeaderCellEmpty){
                htmlTable.getHeader().addCell();
            }
        }

        @Override
        public void setDimension(final int rowCount, final int colCount) {
        }

        @Override
        public void addTitledRow(final int index, final String title) {            
            htmlTable.addRow().addCell(title).setCss("font-weight", "bold");
        }

        @Override
        public void addTitledColumn(final int index, final String title) {
            htmlTable.getHeader().addCell(title);
        }

        @Override
        public void setCellData(final int rowIndex, final int colIndex, final String data) {
            final Table.Row row = htmlTable.getRow(rowIndex);
            if (colIndex<row.cellCount()){
                row.getCell(colIndex).setInnerText(data);
            }else{
                row.addCell(data).setCss("text-align", "right");
            }            
        }

        @Override
        public void setColSpan(final int rowIndex, final int colIndex, final int span) {
            htmlTable.getRow(rowIndex).getCell(colIndex+1).setAttr("colspan", span);
        }

        @Override
        public String toString() {
            return htmlTable.toString();
        }            
        
        public Html getHtml(){
            return htmlTable;
        }
    }
    
    private final static SelectorColumnsStatistic EMPTY = new SelectorColumnsStatistic();
    
    private final HashMap<Id,EnumMap<EAggregateFunction,String>> statistic = new HashMap<>();
    private long rowsCount = -1;        
    
    private SelectorColumnsStatistic(){        
    }
    
    public Collection<Id> getColumnIds(){
        return statistic.keySet();
    }
    
    public EnumSet<EAggregateFunction> getAggregationsForColumn(final Id columnId){
        final EnumMap<EAggregateFunction,String> columnStatistic = statistic.get(columnId);
        final EnumSet<EAggregateFunction> aggregations = EnumSet.noneOf(EAggregateFunction.class);
        if (columnStatistic!=null){
            for (EAggregateFunction function: columnStatistic.keySet()){
                aggregations.add(function);
            }
        }
        return aggregations;
    }
    
    public long getRowsCount(){
        return rowsCount;
    }
    
    public int getColumnsCount(){
        return statistic.size();
    }
    
    public EnumSet<EAggregateFunction> getAggregateFunctions(){
        final EnumSet<EAggregateFunction> result = EnumSet.noneOf(EAggregateFunction.class);
        final int functionsCount = EAggregateFunction.values().length;
        for (EnumMap<EAggregateFunction,String> values: statistic.values()){
            result.addAll(values.keySet());
            if (result.size()-1==functionsCount){
                break;
            }
        }
        return result;
    }
    
    public String getColumnStatistic(final Id columnId, final EAggregateFunction function){
        final EnumMap<EAggregateFunction,String> columnStatistic = statistic.get(columnId);
        if (columnStatistic==null){
            return null;
        }else{
            return columnStatistic.get(function);
        }
    }
    
    public String exportToHtml(final GroupModel groupModel, 
                                             final List<Id> columnsOrder,
                                             final Map<Id,Integer> precisionByColumnId,
                                             final boolean isLandscape){
        final HtmlTable htmlTable = new HtmlTable(!isLandscape);        
        writeToTable(groupModel, columnsOrder, precisionByColumnId, htmlTable, isLandscape);
        final StringBuilder htmlBuilder = new StringBuilder();        
        htmlBuilder.append("<!DOCTYPE html>\n<html>\n <head>\n  <meta charset=\"utf-8\">\n </head>");
        htmlBuilder.append("\n <body>");
        htmlBuilder.append(htmlTable.getHtml().toString(true, false, 3, false));
        if (isLandscape && getRowsCount()>-1){
            htmlBuilder.append("\n  <div>\n   <p style=\"font-weight: bold;\">");
            htmlBuilder.append(getAggregationFunctionTitle(EAggregateFunction.COUNT,groupModel.getEnvironment().getMessageProvider()));
            htmlBuilder.append(": ");
            htmlBuilder.append(String.valueOf(getRowsCount()));
            htmlBuilder.append("</p>\n  </div>");
        }
        htmlBuilder.append("\n </body>\n</html>");
        return htmlBuilder.toString();
    }
    
    public void writeToTable(final GroupModel groupModel, 
                                          final List<Id> columnsOrder, 
                                          final Map<Id,Integer> precisionByColumnId,                                          
                                          final ITable table,
                                          final boolean isLandscape){        
        final IClientEnvironment environment = groupModel.getEnvironment();
        final MessageProvider mp = environment.getMessageProvider();        
        final EnumSet<EAggregateFunction> allAggregateFunctions = getAggregateFunctions();        
        if (isLandscape){
            final int tableRowsCount = getColumnsCount();                    
            final int tableColumnsCount = allAggregateFunctions.size()+1;
            table.setDimension(tableRowsCount, tableColumnsCount );
            if (tableColumnsCount==0){
                return;                
            }
            {
                table.addTitledColumn(0, mp.translate("Selector", "Column"));
                int colIndex = 1;
                String title;
                for (EAggregateFunction function: allAggregateFunctions){
                    title = getAggregationFunctionTitle(function, mp);
                    table.addTitledColumn(colIndex, ClientValueFormatter.capitalizeIfNecessary(environment, title));
                    colIndex++;
                }
            }
            int rowIndex = 0;
            for (Id columnId: columnsOrder){
                final EnumSet<EAggregateFunction> aggregations = getAggregationsForColumn(columnId);
                if (!aggregations.isEmpty()){
                    final SelectorColumnModelItem column = groupModel.getSelectorColumn(columnId);
                    table.addTitledRow(rowIndex, "");
                    table.setCellData(rowIndex, 0, column.getTitle());
                    int colIndex = 1;
                    for (EAggregateFunction function: allAggregateFunctions){
                        final String valueAsString = getColumnStatistic(columnId, function);
                        final String formattedValue = 
                            formatValue(valueAsString, function, groupModel, column.getPropertyDef(), precisionByColumnId.get(columnId));
                        table.setCellData(rowIndex, colIndex, formattedValue);
                        colIndex++;
                    }
                    rowIndex++;
                }                    
            }        
        }else{
            final int tableRowsCount = allAggregateFunctions.size()+(getRowsCount()>-1 ? 1 : 0);
            final int tableColumnsCount = getColumnsCount()==0 ? 1 : getColumnsCount();
            table.setDimension(tableRowsCount, tableColumnsCount );
            {
                int rowIndex = 0;
                String title;
                for (EAggregateFunction function: allAggregateFunctions){
                    title = getAggregationFunctionTitle(function, mp);
                    table.addTitledRow(rowIndex, ClientValueFormatter.capitalizeIfNecessary(environment, title));
                    rowIndex++;
                }
                if (getRowsCount()>-1){
                    title = getAggregationFunctionTitle(EAggregateFunction.COUNT, mp);
                    table.addTitledRow(rowIndex, ClientValueFormatter.capitalizeIfNecessary(environment, title));
                }        
            }
            int columnIndex = 0;
            for (Id columnId: columnsOrder){
                final EnumSet<EAggregateFunction> aggregations = getAggregationsForColumn(columnId);
                if (!aggregations.isEmpty()){
                    final SelectorColumnModelItem column = groupModel.getSelectorColumn(columnId);
                    table.addTitledColumn(columnIndex, column.getTitle());                
                    int rowIndex = 0;
                    for (EAggregateFunction function: allAggregateFunctions){
                        final String valueAsString = getColumnStatistic(columnId, function);
                        final String formattedValue = 
                            formatValue(valueAsString, function, groupModel, column.getPropertyDef(), precisionByColumnId.get(columnId));
                        table.setCellData(rowIndex, columnIndex, formattedValue);
                        rowIndex++;
                    }
                    columnIndex++;
                }
            }
            if (getRowsCount()>-1){
                final int rowIndex = tableRowsCount-1;
                final String formattedValue = new EditMaskInt().toStr(environment, getRowsCount());
                table.setCellData(rowIndex, 0, formattedValue);         
                if (getColumnsCount()>0){
                    table.setColSpan(rowIndex, 0, getColumnsCount());                
                }
            }
        }
    }
    
    private static String formatValue(final String valueAsStr,
                                                     final EAggregateFunction function,
                                                     final GroupModel groupModel, 
                                                     final RadPropertyDef propertyDef,
                                                     final Integer precision){        
        if (valueAsStr==null){
            return groupModel.getDisplayString(propertyDef.getId(), null, "", false);
        }else{
            final IClientEnvironment environment = groupModel.getEnvironment();
            final EditMask editMask = propertyDef.getEditMask();
            if (editMask instanceof EditMaskNum && precision!=null){
                ((EditMaskNum)editMask).setPrecision(precision);
            }
            final EValType valType = propertyDef.getType();
            final Object value;                        
            if (function==EAggregateFunction.AVG){
                value = ValAsStr.fromStr(valueAsStr, EValType.NUM);
            }else{
                value = ValAsStr.fromStr(valueAsStr, valType);
            }
            final String formattedValue;
            if (function==EAggregateFunction.AVG){
                if (editMask instanceof EditMaskNum){
                    formattedValue = ((EditMaskNum)editMask).getRounded(environment, value);
                }else{
                    final EditMaskNum maskForAvgValue = new EditMaskNum();
                    maskForAvgValue.setPrecision(precision==null ? AggregateFunctionCall.DEFAULT_PRECISION : precision);
                    formattedValue = maskForAvgValue.getRounded(environment, value);
                }
            }else{
                formattedValue = editMask.toStr(environment, value);
            }
            return groupModel.getDisplayString(propertyDef.getId(), value,  formattedValue, false);
        }
    }
    
    public static SelectorColumnsStatistic parse(final org.radixware.schemas.eas.AggregateFunctions xmlAggregations){
        if (xmlAggregations==null ||  xmlAggregations.getFunctionCallList()==null){
            return EMPTY;
        }else{
            final SelectorColumnsStatistic result = new SelectorColumnsStatistic();
            for (org.radixware.schemas.eas.AggregateFunctions.FunctionCall call: xmlAggregations.getFunctionCallList()){
                if (call.getFunctionName()==EAggregateFunction.COUNT){
                    result.rowsCount = Long.parseLong(call.getResult());
                }else{
                    final Id columnId = call.getColumnId();
                    EnumMap<EAggregateFunction,String> columnStatistic = result.statistic.get(columnId);
                    if (columnStatistic==null){
                        columnStatistic = new EnumMap<>(EAggregateFunction.class);
                        result.statistic.put(columnId, columnStatistic);
                    }
                    columnStatistic.put(call.getFunctionName(), call.getResult());
                }                
            }
            return result;
        }        
    }
    
    public static List<Id> getCompatibleColumns(final GroupModel groupModel, final List<Id> columns){
        final List<Id> compatibleColumns = new LinkedList<>();
        for (Id columnId: columns){
            final SelectorColumnModelItem column = groupModel.getSelectorColumn(columnId);
            final RadPropertyDef propertyDef = column.getPropertyDef();
            final EValType valType = propertyDef.getType();
            if ( propertyDef .canBeUsedInSorting()
                && propertyDef .getConstSet()==null                
                && !column.isForbidden()
                && (valType==EValType.INT || valType==EValType.NUM)
               ){
                compatibleColumns.add(column.getId());
           }
        }
        return compatibleColumns;
    }
    
    public static String getAggregationFunctionTitle(final EAggregateFunction function, final MessageProvider messageProvider){
        switch(function){
            case MAX:
                return messageProvider.translate("Selector", "Maximum value");
            case MIN:
                return messageProvider.translate("Selector", "Minimum value");
            case AVG:
                return messageProvider.translate("Selector", "Average value");
            case SUM:
                return messageProvider.translate("Selector", "Sum of values");
            case COUNT:
                return messageProvider.translate("Selector", "Number of rows");
            default:
                throw new IllegalArgumentException("Unknown aggregate function "+function.getName());
        }
    }    
}
