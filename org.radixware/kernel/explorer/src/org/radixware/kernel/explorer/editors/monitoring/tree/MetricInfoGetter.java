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

package org.radixware.kernel.explorer.editors.monitoring.tree;

import com.trolltech.qt.gui.QIcon;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs.Channel;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs.Instance;
import org.radixware.schemas.monitoringcommand.SysMonitoringRs.Unit;


public class MetricInfoGetter {
    private Map<Long,InstanceInfo> instancesMap=new HashMap<>();
    private Map<Long,UnitInfo> unitsMap=new HashMap<>();
    private Map<Long,ChannelInfo> channelsMap=new HashMap<>();
    private final IClientEnvironment environment;

    
    public class TreeItemInfo{
         private final Long id;
         private final String pid;
         private final String title;
         private final String classGUID;
         private final String imageId;
         //private Map<Id,String> props=new HashMap<>();
         //private Map<String,MetricInfo> metrics=new HashMap<>();
         
         protected TreeItemInfo(Long id, String pid,String title, String classGUID, String sIcon/*,List<Prop> props, List<Metric> metric*/){
             this.id=id;
             this.title=title;
             this.classGUID=classGUID;
             this.pid=pid;
             this.imageId=sIcon;
         }
         
         public Long getId(){
           return id;
         }
         
         public String getPid(){
           return pid;
         }
         
         public String getTitle(){
           return id.toString()+ (title==null ? "" : ") " + title);
         }
         
         public String getPureTitle() {
             return title;
         }
         
         public String getClassGUID(){
           return classGUID;
         }
         
         public QIcon getIcon(){
           Icon icon;
           try{
                if(imageId!=null){
                     icon = environment.getDefManager().getImage(Id.Factory.loadFrom(imageId));
                }else{
                     icon = environment.getDefManager().getClassPresentationDef(Id.Factory.loadFrom(classGUID)).getIcon();
                }
           }catch(DefinitionNotFoundError ex){ 
               return null;
           }
           return ExplorerIcon.getQIcon(icon);
         }
         
        /* protected String getPropValueById(Id propId){
            return props.get(propId);
        }
    
        protected MetricInfo getMetricValueByKind(String kind){
            return metrics.get(kind);
        }*/
        
        /*public void update(List<Prop> props, List<Metric> metric){
             for(Prop p:props){
                 this.props.put(p.getPropId(), p.getPropVal());
             }
             for(Metric m:metric){
                Double maxVal= m.getMaxVal()==null ? null : m.getMaxVal().doubleValue();
                Double minVal= m.getMaxVal()==null ? null : m.getMaxVal().doubleValue();
                Double avgVal= m.getMaxVal()==null ? null : m.getMaxVal().doubleValue();
                Double endVal= m.getMaxVal()==null ? null : m.getMaxVal().doubleValue();
                MetricInfo mInfo=new MetricInfo(maxVal, minVal, avgVal, endVal);
                metrics.put(m.getKind(), mInfo);
             }
        }*/
    }
    
    public class InstanceInfo extends TreeItemInfo{      
       private boolean isStarted;
       private Long maxArteInstCnt;
       private Long arteInstCnt;
       private Double avgArteSessionCnt;
       
       public InstanceInfo(Instance xInst){
           super( xInst.getId(), xInst.getId().toString(), xInst.getTitle(),xInst.getClassGUID(), xInst.getImageId()/*,
                  xInst.getPropsList(),xInst.getMetricsList()*/);
           this.isStarted=xInst.getStarted();
           this.maxArteInstCnt=xInst.getMaxArteInstanceCnt();
           this.arteInstCnt=xInst.getArteInstanceCnt();
           this.avgArteSessionCnt=xInst.getAvgArteSessionCnt()==null? null :xInst.getAvgArteSessionCnt().doubleValue();
       }
       
       public void update(Instance xInst){
           this.isStarted=xInst.getStarted();
           this.maxArteInstCnt=xInst.getMaxArteInstanceCnt();
           this.arteInstCnt=xInst.getArteInstanceCnt();
           this.avgArteSessionCnt=xInst.getAvgArteSessionCnt()==null? null :xInst.getAvgArteSessionCnt().doubleValue();
           //update(xInst.getPropsList(), xInst.getMetricsList());
       }
       
       public boolean isStarted(){
           return isStarted;
       }
       
       public Long getMaxArteInstCnt(){
           return maxArteInstCnt;
       }
       
       public Long getArteInstCnt(){
           return arteInstCnt;
       }
       
       public Double getInstArteSessionCnt(){
           return avgArteSessionCnt;
       }
    }
    
    public class UnitInfo  extends TreeItemInfo{
       private boolean isStarted;
       private boolean isUse;
       private Double unitArteSessionCnt;
       private Double jobExecCnt;
       private Long maxJobExecCnt;
       private Double jobWaitCnt;
       private Long maxArteSessionCnt;
       private Long unitType;
       
       public UnitInfo(Unit xUnit){
           super( xUnit.getId(), xUnit.getId().toString(),  xUnit.getTitle(),xUnit.getClassGUID(), xUnit.getImageId()/*,
                  xUnit.getPropsList(),xUnit.getMetricsList()*/);
           this.isStarted=xUnit.getStarted();
           this.isUse=xUnit.getUsed();
           this.unitArteSessionCnt=xUnit.getAvgArteSessionCnt()==null? null:xUnit.getAvgArteSessionCnt().doubleValue();
           this.jobExecCnt=xUnit.getAvgJobExecCnt()==null? null:xUnit.getAvgJobExecCnt().doubleValue();
           this.jobWaitCnt=xUnit.getAvgJobWaitCnt()==null? null:xUnit.getAvgJobWaitCnt().doubleValue();
           this.unitType=xUnit.getUnitType();
           this.maxArteSessionCnt=xUnit.getMaxArteSessionCnt();
           this.maxJobExecCnt=xUnit.getMaxJobExecCnt();
       }
       
       public void update(Unit xUnit){
           this.isStarted=xUnit.getStarted();
           this.isUse=xUnit.getUsed();
           this.unitArteSessionCnt=xUnit.getAvgArteSessionCnt()==null? null : xUnit.getAvgArteSessionCnt().doubleValue();
           this.jobExecCnt=xUnit.getAvgJobExecCnt()==null? null:xUnit.getAvgJobExecCnt().doubleValue();
           this.jobWaitCnt=xUnit.getAvgJobWaitCnt()==null? null:xUnit.getAvgJobWaitCnt().doubleValue();
           this.unitType=xUnit.getUnitType();
           this.maxArteSessionCnt=xUnit.getMaxArteSessionCnt();
           this.maxJobExecCnt=xUnit.getMaxJobExecCnt();
           //update(xUnit.getPropsList(), xUnit.getMetricsList());
       }
       
       public boolean isStarted(){
           return isStarted;
       }
       
       public boolean isUse(){
           return isUse;
       }
       
       public Double getUnitArteSessionCnt(){
           return unitArteSessionCnt;
       }
       
       public Double getJobExecCnt(){
           return jobExecCnt;
       }
       
       public Long  getMaxJobExecCnt(){
           return maxJobExecCnt;
       }
       
       public Double getJobWaitCnt(){
           return jobWaitCnt;
       }
       
       public Long  getMaxArteInstCnt(){
           return maxArteSessionCnt;
       }
       
       public Long getUnitType(){
           return unitType;
       }
    }
     
    public class ChannelInfo  extends TreeItemInfo{
        //private boolean isWriteConnectState;
        //private boolean isConnected;
        private boolean isListener;
        private Long curSessionCnt;
        private Long maxSessionCnt;
        private Long unitId;
        
        public ChannelInfo(Channel xChannel){
            super( xChannel.getId(),  xChannel.getId().toString(), xChannel.getTitle(),xChannel.getClassGUID(), xChannel.getImageId()/*,
                   xChannel.getPropsList(),xChannel.getMetricsList()*/);
            //this.isWriteConnectState=xChannel.getWriteConnectStateToDb();
            //this.isConnected=xChannel.getConnected();
            this.curSessionCnt=xChannel.getCurSessionCount();
            this.maxSessionCnt=xChannel.getMaxSessionCount();  
            this.isListener=xChannel.getListener();
            this.unitId=xChannel.getUnitId();
        }
        
        public void update(Channel xChannel){
            //this.isWriteConnectState=xChannel.getWriteConnectStateToDb();
            //this.isConnected=xChannel.getConnected();
            this.curSessionCnt=xChannel.getCurSessionCount();
            this.maxSessionCnt=xChannel.getMaxSessionCount();  
            this.isListener=xChannel.getListener();
            this.unitId=xChannel.getUnitId();
            //update(xChannel.getPropsList(), xChannel.getMetricsList());
        }
        
       /* public boolean isWriteConnectState(){
           return isWriteConnectState;
        }
        
        public boolean isConnected(){
           return isConnected;
        }*/
        
        public boolean isListener(){
           return isListener;
        }
        
        public Long getCurSessionCnt(){
           return curSessionCnt;
        }
        
        public Long getMaxSessionCnt(){
           return maxSessionCnt;
        }
        
        public Long getUnitId(){
           return unitId;
        }
    }
    
    public MetricInfoGetter(final IClientEnvironment environment){  
        this.environment=environment;
    }    
    
    public InstanceInfo getInstanceInfo(Long id){
        return instancesMap.get(id);
    }
    
    public UnitInfo getUnitInfo(Long id){
        return unitsMap.get(id);
    }
    
    public ChannelInfo getChannelInfo(Long id){
        return channelsMap.get(id);
    }
    
    public void update(final SysMonitoringRs metricRs){
        List<Instance> xInstances=metricRs.getInstanceList();
        if(xInstances!=null && !xInstances.isEmpty()){
            for(Instance xInst:xInstances){
                Long id=Long.valueOf(xInst.getId());
                if(instancesMap.containsKey(id)){
                    instancesMap.get(id).update(xInst);
                }else{
                    instancesMap.put(id, createInstanceInfo(xInst));
                }
            }
        }
        List<Unit> xUnits=metricRs.getUnitList();
        if(xUnits!=null && !xUnits.isEmpty()){
            for(Unit xUnit:xUnits){
                Long id=Long.valueOf(xUnit.getId());
                if(unitsMap.containsKey(id)){
                    unitsMap.get(id).update(xUnit);
                }else{
                    unitsMap.put(id, createUnitInfo(xUnit));
                }
            }
        }        
        List<Channel> xChannels=metricRs.getChannelList();
        if(xChannels!=null && !xChannels.isEmpty()){
            for(Channel xChannel:xChannels){
                Long id=Long.valueOf(xChannel.getId());
                if(channelsMap.containsKey(id)){
                    channelsMap.get(id).update(xChannel);
                }else{
                    channelsMap.put(id, createChannelInfo(xChannel));
                }
            }
        }
    }
    
     public UnitInfo createUnitInfo(Unit xUnit){
        UnitInfo unitInfo=new UnitInfo(xUnit);
        unitsMap.put(xUnit.getId(), unitInfo);
        return unitInfo;
    }
     
    public InstanceInfo createInstanceInfo(Instance xInst){
        InstanceInfo instanceInfo=new InstanceInfo(xInst);
        instancesMap.put(instanceInfo.getId(), instanceInfo);
        return instanceInfo;
    }
    
    public ChannelInfo createChannelInfo(Channel xChannel){
        ChannelInfo channelInfo=new ChannelInfo(xChannel);
        channelsMap.put(channelInfo.getId(), channelInfo);
        return channelInfo;
    }

    public Map<Long,InstanceInfo> getInstanceInfoMap(){
        return instancesMap;
    }

    
    public void clear(){
        instancesMap.clear();
        unitsMap.clear();
        channelsMap.clear();
    }
}
