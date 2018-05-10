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

package org.radixware.kernel.server.arte.services.eas;

import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EAggregateFunction;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;
import org.radixware.kernel.common.exceptions.ServiceProcessFault;
import org.radixware.kernel.common.exceptions.ServiceProcessServerFault;
import org.radixware.kernel.common.types.AggregateFunctionCall;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.dbq.QuerySqlBuilder;
import org.radixware.kernel.server.dbq.SelectQuery;
import org.radixware.kernel.server.dbq.SelectQuery.Result;
import org.radixware.kernel.server.exceptions.FilterParamNotDefinedException;
import org.radixware.kernel.server.exceptions.PropNotLoadedException;
import org.radixware.kernel.server.meta.clazzes.RadClassDef;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;
import org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn;
import org.radixware.kernel.server.types.EntityPropVals;
import org.radixware.kernel.server.types.Pid;
import org.radixware.schemas.eas.CalcSelectionStatisticMess;
import org.radixware.schemas.eas.CalcSelectionStatisticRq;
import org.radixware.schemas.eas.CalcSelectionStatisticRs;
import org.radixware.schemas.eas.Definition;
import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.schemas.easWsdl.CalcSelectionStatisticDocument;


final class CalcSelectionStatisticRequest  extends SessionRequest {
    
    CalcSelectionStatisticRequest (final ExplorerAccessService presenter) {
        super(presenter);
    }    
        
    public final CalcSelectionStatisticDocument process(final CalcSelectionStatisticMess request) throws ServiceProcessFault, InterruptedException {
        final CalcSelectionStatisticRq rqParams = request.getCalcSelectionStatisticRq();
        final RadClassDef classDef;        
        final Definition classXml = rqParams.getClass1();
        if (classXml != null) {
            final Id classId = classXml.getId();
            try {
                classDef = getArte().getDefManager().getClassDef(classId);
            } catch (DefinitionNotFoundError e) {
                throw EasFaults.newDefWithIdNotFoundFault("Class", rqParams.getDomNode().getNodeName(), classId);
            }
        } else {
            throw EasFaults.newParamRequiedFault("Class", rqParams.getDomNode().getNodeName());
        }
        final Id grpClassId = classDef.getId();
        final RadClassDef grpClassDef = getArte().getDefManager().getClassDef(grpClassId);
        final PresentationOptions presOptions = getPresentationOptions(rqParams, grpClassDef, true, true, rqParams.getPresentation());
        getArte().switchToReadonlyTransaction();
        final Group group = getGroup(rqParams, grpClassDef, presOptions, false, true);
        final Pid parentPid = presOptions.context != null ? presOptions.context.getContextObjectPid() : null;
        final List<AggregateFunctionCall> functions = AggregateFunctionCall.parseFromXml(rqParams.getAggregateFunctions());
        final List<SelectorColumn> selectorColumns = group.entityGroup.getPresentation().getSelectorColumns();
        for (AggregateFunctionCall functionCall: functions){            
            if (functionCall.getFunction()!=EAggregateFunction.COUNT){
                final Id columnId = functionCall.getColumnId();
                boolean found = false;                
                for (SelectorColumn column: selectorColumns){
                    if (column.getPropId().equals(columnId)){
                        found = true;
                        break;
                    }
                }
                if (!found){
                    final String messageTemplate = "Column #%1$s was not defined in selector presentation #%2$s";
                    final String message = 
                        String.format(messageTemplate, columnId.toString(), group.entityGroup.getPresentation().getId().toString());
                    throw EasFaults.newAccessViolationFault(getArte(), message);
                }
            }
        }
        final Result rs;
        SelectQuery qry = null;
        try{
            qry = getArte().getDefManager().getDbQueryBuilder().buildStatisticQuery(group.entityGroup, functions);            
            rs = qry.select(group.entityGroup, 0, 1, parentPid);
        }catch (FilterParamNotDefinedException e) {
            final String preprocessedExStack = getArte().getTrace().exceptionStackToString(e);
            throw new ServiceProcessClientFault(ExceptionEnum.MISSING_FILTER_PARAM.toString(), e.getMessage(), e, preprocessedExStack);
        } catch (RuntimeException e) {
            throw EasFaults.exception2Fault(getArte(), e, "\"Select\" query raised exception");
        } finally {
            if (qry != null) {
                qry.free();
            }
        }
        final CalcSelectionStatisticDocument document = CalcSelectionStatisticDocument.Factory.newInstance();
        final CalcSelectionStatisticRs response = document.addNewCalcSelectionStatistic().addNewCalcSelectionStatisticRs();
        if (!rs.rows.isEmpty()) {
            final EntityPropVals propVals = rs.rows.get(0);
            final org.radixware.schemas.eas.AggregateFunctions xmlFunctions = response.addNewAggregateFunctions();
            org.radixware.schemas.eas.AggregateFunctions.FunctionCall xmlFunctionCall;
            for (AggregateFunctionCall functionCall: functions){
                final Id columnId = functionCall.getColumnId();
                final EAggregateFunction function = functionCall.getFunction();
                final Id fieldId;                
                if (function==EAggregateFunction.COUNT){                    
                    fieldId = QuerySqlBuilder.ROWS_COUNT_FIELD_COL_ID;                        
                }else{
                    fieldId = Id.Factory.append(columnId, "_"+function.getValue());                        
                }
                final Object value;
                try{
                    value = propVals.getPropValById(fieldId);
                }catch(PropNotLoadedException exception){
                    continue;
                }
                if (value!=null){
                    final EValType valType;
                    switch(function){
                        case AVG:
                            valType = EValType.NUM;
                            break;
                        case COUNT:
                            valType = EValType.INT;
                            break;
                        default:
                            final RadPropDef propDef = grpClassDef.getPropById(columnId);
                            valType = propDef.getValType();
                    }
                    xmlFunctionCall = xmlFunctions.addNewFunctionCall();
                    xmlFunctionCall.setFunctionName(function);
                    if (functionCall.getFunction()!=EAggregateFunction.COUNT)
                        xmlFunctionCall.setColumnId(columnId);                    
                    xmlFunctionCall.setResult(ValAsStr.toStr(value, valType));
                }
            }
        }        
        return document;
    }
    
    @Override
    XmlObject process(final XmlObject rq) throws ServiceProcessFault, InterruptedException {
        CalcSelectionStatisticDocument doc = null;
        try{
            doc = process((CalcSelectionStatisticMess) rq);
        }finally{
            postProcess(rq, doc==null ? null : doc.getCalcSelectionStatistic().getCalcSelectionStatisticRs());
        }
        return doc;
    }

    @Override
    void prepare(final XmlObject rqXml) throws ServiceProcessServerFault, ServiceProcessClientFault {
        super.prepare(rqXml);
        prepare(((CalcSelectionStatisticMess) rqXml).getCalcSelectionStatisticRq());
    }    

}
