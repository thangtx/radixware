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

package org.radixware.kernel.explorer.editors.monitoring;

import java.math.BigDecimal;
import java.util.EnumSet;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.common.client.models.GroupModelReader;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget.EExplorerItemName;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget.IdsGetter;
import org.radixware.kernel.explorer.editors.monitoring.UnitsWidget.EPropertyName;


public class MetricGetter {    
    private final GroupModel metricStateModel;
    //private final GroupModel instanceModel;
    //private final GroupModel unitModel;
   //private final GroupModel channelModel;
    private final IdsGetter idsGetter;

    MetricGetter(final GroupModel sumSourceGroup,final GroupModel instanceModel,final GroupModel unitModel,final GroupModel channelModel, final IdsGetter idsGetter) {
        this.metricStateModel = sumSourceGroup;
        //this.instanceModel=instanceModel;
        //this.unitModel=unitModel;
        //this.channelModel=channelModel;
        this.idsGetter = idsGetter;        
    }
    
   /* public EntityModel findInstanceModel(final Long id){
        final GroupModelReader metricStateReader=createReader(instanceModel, true);
        for (EntityModel entity: metricStateReader) {
            Long entityInstanceId=(Long) entity.getProperty(idsGetter.getPropId(EPropertyName.INSTANCE_ID)).getValueObject();
            if(entityInstanceId!=null && entityInstanceId.equals(id)){
                return entity;
            }
        }        
        return null;   
    }
    
    public EntityModel findUnitModel(final Long id){
        final GroupModelReader metricStateReader=createReader(unitModel, true);
        for (EntityModel entity: metricStateReader) {
            Long entityInstanceId=(Long) entity.getProperty(idsGetter.getPropId(EPropertyName.UNIT_ID)).getValueObject();
            if(entityInstanceId!=null && entityInstanceId.equals(id)){
                return entity;
            }
        }
        return null;   
    }
    
     public EntityModel findChannelModel(final Long id){
        final GroupModelReader metricStateReader=createReader(channelModel, true);
        for (EntityModel entity: metricStateReader) {
            Long entityInstanceId=(Long) entity.getProperty(idsGetter.getPropId(EPropertyName.NETCHANNEL_ID)).getValueObject();
            if(entityInstanceId!=null && entityInstanceId.equals(id)){
                return entity;
            }
        }
        return null;   
    }*/
    
    public IdsGetter getIdsGetter(){
        return idsGetter;
    }
    
    public Id getPropId(final EPropertyName propName) {
        return idsGetter.getPropId(propName);
    }

    public Object getPropValue(final EntityModel entity, final Id propId) {
        return entity.getProperty(propId).getValueObject();
    }

    public Id getExplorerItemId(final EExplorerItemName explorerItemName) {
        return idsGetter.getExplorerItemId(explorerItemName);
    }

   /* public void setMetric(final MetricData metricData, Long id, EPropertyName sensorInType, EPropertyName sensorInState,boolean isAsync) {
        //this.metricData = metricData;
        String metricKind = metricData.getMetricKind();
        Id propId = metricData.getPropId();
        if (metricKind != null && propId != null) {
            try {
               getMetricValues(metricData,id,metricKind,sensorInType,sensorInState,propId, isAsync);                
            } catch (ServiceClientException ex) {
                metricStateModel.showException(ex);
            } catch (InterruptedException ex) {                
            } catch (BrokenEntityObjectException ex) {
                metricStateModel.showException(ex);
            }
        }
    }*/

    /*private void getMetricSync(Long id, String kind, EPropertyName sensor, Id propId) {
        updateMetricType(id, kind, sensor, null);
        getMetricValues(id,kind,sensor,propId, false);
    }

    private void getMetricAsync(Long id, String kind, EPropertyName sensor, Id propId) {
        MetricTypeResponseListener respotceListener = new MetricTypeResponseListener(propId);
        updateMetricType(id, kind, sensor, respotceListener);
    }*/

   /* private Range getRange(Double val, EntityModel entity) {
        Id pMaxVal = idsGetter.getPropId(EPropertyName.METRICTYPE_HIGHT_ERROR_VAL);
        Id pMinVal = idsGetter.getPropId(EPropertyName.METRICTYPE_LOW_ERROR_VALl);
        if (isInRangeVals(val, pMaxVal, pMinVal, entity)) {
            return Range.ERROR;
        }
        pMaxVal = idsGetter.getPropId(EPropertyName.METRICTYPE_HIGHT_WARN_VALl);
        pMinVal = idsGetter.getPropId(EPropertyName.METRICTYPE_LOW_WARN_VALl);
        if (isInRangeVals(val, pMaxVal, pMinVal, entity)) {
            return Range.WARNING;
        }
        return Range.NORMAL;
    }*/

    /*private void updateMetricState(GroupModel metricStateModel,Long id, EPropertyName sensorId, IResponseListener responseListener) {
        metricStateModel.getProperty(idsGetter.getPropId(EPropertyName.METRICSTATE_UNIT_ID)).setValueObject(null);
        metricStateModel.getProperty(idsGetter.getPropId(EPropertyName.METRICSTATE_INSTANCE_ID)).setValueObject(null);
        metricStateModel.getProperty(idsGetter.getPropId(EPropertyName.METRICSTATE_NETLISTENER_ID)).setValueObject(null);
        metricStateModel.getProperty(idsGetter.getPropId(EPropertyName.METRICSTATE_NETCLIENT_ID)).setValueObject(null);

        metricStateModel.getProperty(idsGetter.getPropId(sensorId)).setValueObject(id);
        //metricTypeModel.getProperty(idsGetter.getPropId(EPropertyName.METRICTYPE_KIND)).setValueObject(kind);
        try {
            read(metricStateModel,responseListener);
        } catch (ServiceClientException ex) {
            metricStateModel.showException(ex);
        }
    }

    private void read(GroupModel metricStateModel,IResponseListener responseListener) throws ServiceClientException {
        if (responseListener != null) {
            metricStateModel.reset();
            metricStateModel.readMoreAsync().addListener(responseListener);
        } else {
            metricStateModel.reread();
        }
    }*/

   /* private boolean isInRangeVals(Double val, Id pMaxVal, Id pMinVal, EntityModel entity) {
        if (entity != null) {
            BigDecimal maxVal = (BigDecimal) entity.getProperty(pMaxVal).getValueObject();
            BigDecimal minVal = (BigDecimal) entity.getProperty(pMinVal).getValueObject();
            return (maxVal!= null && val <= maxVal.doubleValue()) || (minVal != null && val > minVal.doubleValue());
        }
        return false;
    }*/
    

    
    
  /*  private void getMetricValues(MetricData metricData, Long id, String kind,EPropertyName sensorInType, EPropertyName sensorInState,Id propId, boolean isAsinc) throws ServiceClientException, InterruptedException, BrokenEntityObjectException {
         List<EntityModel> metricStatesForCheck=new ArrayList<>();        
         final GroupModelReader metricStateReader=createReader(metricStateModel, isAsinc);
         for (EntityModel metricStateEntity: metricStateReader) {
            String entity_kind=(String) metricStateEntity.getProperty(idsGetter.getPropId(EPropertyName.METRICTYPE_KIND)).getValueObject();
            if(entity_kind.equals(kind)){
                //Long sensorType=(Long) metricStateEntity.getProperty(idsGetter.getPropId(sensorInType)).getValueObject();                  
                //if(sensorType!=null && sensorType.equals(id)){
                if(checkSensor( id, sensorInType, metricStateEntity)){
                    Double res = getMetricData(metricStateEntity, propId);
                    if(res!=null){
                        metricData.setVal(res);
                        metricData.setRange(getRange(res,  metricStateEntity));
                        return ;
                    }                     
                }else {                    
                    //Long sensorState=(Long) metricStateEntity.getProperty(idsGetter.getPropId(sensorInState)).getValueObject();
                    //if(sensorState!=null && sensorState.equals(id)){
                    if(checkSensor( id, sensorInState, metricStateEntity)){
                        metricStatesForCheck.add(metricStateEntity);   
                    }
                }
            }
         }
         if(metricStateReader.wasInterrupted()){
            throw new InterruptedException();
         }
         if (!metricStatesForCheck.isEmpty()) {              
             EntityModel typeModel = metricStatesForCheck.get(0);  
             Double res = getMetricData(metricStatesForCheck.get(0), propId);
             if(res!=null){
                 metricData.setVal(res);
                 metricData.setRange(getRange(res,  typeModel));
             }                    
         } 
    }*/
    
    private boolean checkSensor(final Long id, final EPropertyName sensor,final EntityModel metricStateEntity){
        final Id sensorInTypeId=idsGetter.getPropId(sensor);
        Property prop;
        if(sensorInTypeId!=null &&(prop=(metricStateEntity.getProperty(sensorInTypeId)))!=null) {       
           final Long sensorType=(Long) prop.getValueObject();                  
           if(sensorType!=null && sensorType.equals(id)){
               return true;
           }
        }
        return false;
    }
    
   /* private void getMetricValues(MetricData metricData,Long id, String kind,EPropertyName sensorInType, EPropertyName sensorInState,Id propId, boolean isAsinc) throws ServiceClientException, InterruptedException {
         List<EntityModel> metricTypesForCheck=new ArrayList<EntityModel>();
         //final GroupModelReader metricTypeReader= new GroupModelReader(metricTypeModel, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));
         final GroupModelReader metricTypeReader=createReader(metricTypeModel, isAsinc);
         for (EntityModel metricTypeEntity: metricTypeReader) {            
               String entity_kind=(String) metricTypeEntity.getProperty(idsGetter.getPropId(EPropertyName.METRICTYPE_KIND)).getValueObject();
               if(entity_kind.equals(kind)){
                   Long metricTypeSensorId=(Long) metricTypeEntity.getProperty(idsGetter.getPropId(sensorInType)).getValueObject();
                   if(metricTypeSensorId!=null && metricTypeSensorId.equals(id)){
                       final Id state_id = idsGetter.getExplorerItemId(UnitsWidget.EExplorerItemName.METRIC_STATE);
                       Model stateModel = metricTypeEntity.getChildModel(state_id);                        
                       if((stateModel instanceof GroupModel) && setMetric( metricTypeEntity,(GroupModel) stateModel, metricData, id,  sensorInState, propId, isAsinc)){
                           return;
                       }
                    }else if(metricTypeSensorId==null){
                        metricTypesForCheck.add(metricTypeEntity);                      
                    }             
               }
          }
          for (EntityModel metricTypeEntity: metricTypesForCheck) {              
                final Id state_id = idsGetter.getExplorerItemId(UnitsWidget.EExplorerItemName.METRIC_STATE);
                Model stateModel = metricTypeEntity.getChildModel(state_id);  
                if(stateModel instanceof GroupModel){
                    if(setMetric( metricTypeEntity,(GroupModel) stateModel, metricData, id,  sensorInState, propId, isAsinc)){
                       return;
                    }
                }
          }      
    }
     
     private boolean setMetric(EntityModel metricTypeEntity,GroupModel stateModel,MetricData metricData,Long id, EPropertyName sensorInState,Id propId,boolean isAsinc){
        EntityModel entityState=findStateEntity(metricTypeEntity,(GroupModel) stateModel,metricData, id, sensorInState,propId, isAsinc);
        if(entityState!=null) {    
              Double res = getMetricData(entityState, propId);
              if(res!=null){
                    metricData.setVal(res);
                    metricData.setRange(getRange(res,  metricTypeEntity));
                    return true;
              }
         }
        return false;
     }
     
     private EntityModel findStateEntity(EntityModel metricTypeEntity,GroupModel stateModel,MetricData metricData,Long id,EPropertyName sensorId,Id propId,boolean isAsinc){         
         final GroupModelReader metricStateReader=createReader(stateModel, isAsinc);         
         for (EntityModel metricStateEntity: metricStateReader) {             
            Long sensor=(Long) metricStateEntity.getProperty(idsGetter.getPropId(sensorId)).getValueObject();//setValueObject(id);
            if(sensor.equals(id)){ 
                return metricStateEntity;
            }
         }
         if(isAsinc  && stateModel.hasMoreRows()){
                stateModel.readMoreAsync().addListener(new MetricStateResponseListener( metricData,metricTypeEntity,stateModel,  id, sensorId,propId));             
         }
         return null;
    }*/
     
     private GroupModelReader createReader(final GroupModel model,final boolean isAsinc){
         if(isAsinc){
             return new GroupModelReader(model, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION,GroupModelReader.EReadingFlags.DONT_LOAD_MORE)); 
         }
         return new GroupModelReader(model, EnumSet.of(GroupModelReader.EReadingFlags.SHOW_SERVICE_CLIENT_EXCEPTION));        
     }

     private Double getMetricData(final EntityModel entity, final Id propId) {  
         final Property prop = entity.getProperty(propId);
         if(prop!=null){
             final BigDecimal metricVal = (BigDecimal) prop.getValueObject();
             return metricVal==null ? Double.valueOf(0.) : metricVal.doubleValue();
         }else{
             return Double.valueOf(0.);
         }          
     }
}
