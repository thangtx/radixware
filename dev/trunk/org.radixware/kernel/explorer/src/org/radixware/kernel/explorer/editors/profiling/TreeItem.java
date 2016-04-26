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

package org.radixware.kernel.explorer.editors.profiling;

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QTreeWidgetItem;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;


public class TreeItem extends QTreeWidgetItem {

     @Override
    public boolean operator_less(final QTreeWidgetItem qtwi) {
        final int column = treeWidget().sortColumn();
        if ((!text(column).isEmpty()) && (!qtwi.text(column).isEmpty())) {
            if (column == TITLE_COL_INDEX) {
                if ((isPeriod) && (period != null) && (((TreeItem) qtwi).period != null)) {
                    return period.before(((TreeItem) qtwi).period);
                }
            } else {
                if (column == PURE_LOAD_COL_INDEX) {
                    return pureDuration < ((TreeItem) qtwi).getPureDuration();
                } else {
                    return Double.valueOf(getStrWithoutSpaces(text(column))) < Double.valueOf(getStrWithoutSpaces(qtwi.text(column)));
                }
            }
        }
        return super.operator_less(qtwi);

    }
    
    String getStrWithoutSpaces(String numberAsStr) {
        return numberAsStr.replaceAll(" ", "").replace(",", ".");
    }
    
    private static final int accuracy = 3;

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TreeItem other = (TreeItem) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.context == null) ? (other.context != null) : !this.context.equals(other.context)) {
            return false;
        }
        if ((this.period == null) ? (other.period != null) : !this.period.equals(other.period)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int res = 1;
        res = res * prime + (name != null ? name.hashCode() : 0); 
        res = res * prime + (context != null ? context.hashCode() : 0);
        res = res * prime + (period != null ? period.hashCode() : 0); 
        return res;
    }
    
    private long pureDomainCPU = 0;
    private long pureDomainDb = 0;
    private long pureDomainExt = 0;
    private long domainCPU = 0;
    private long domainDb = 0;
    private long domainExt = 0;
    //private boolean isOutOfDomrn=false;
    
    private final Pid pid;
    private final String name;
    private String title;
    private final Timestamp period;
    private final String context;
    private long duration;
    private long minDuration;
    private long maxDuration;
    private long pureDuration;
    private long count;
    private double load;
    private double pureLoad;
    private double avgLoad;
    private double avgPureLoad;
    private final Long instanceId;
    private boolean isPeriod = false;
    private boolean isCalculated = false;
    private EntityModel entity;
    private boolean isPercentMode = true;
    
    public static final int TITLE_COL_INDEX=0;
    public static final int LOAD_COL_INDEX=1;
    public static final int MIN_LOAD_COL_INDEX=2;
    public static final int MAX_LOAD_COL_INDEX=3;
    public static final int AVG_LOAD_COL_INDEX=4;
    public static final int PURE_LOAD_COL_INDEX=5;
    public static final int PURE_AVG_LOAD_COL_INDEX=6;
    public static final int COUNT_COL_INDEX=7;
    public static final int CPU_COL_INDEX=8;
    public static final int DB_COL_INDEX=9;
    public static final int EXT_COL_INDEX=10;
    public static final int PURE_CPU_COL_INDEX=11;
    public static final int PURE_DB_COL_INDEX=12;
    public static final int PURE_EXT_COL_INDEX=13;


    TreeItem(final String name, final Long instanceId, final boolean isPersentMode) {
        this(name, null, "", 0l, 0l, 0l, 0l, instanceId, 0l, null, null, null, null, null, name, isPersentMode);
        isPeriod = true;
    }

    TreeItem(final String name, final Timestamp period, final Long instanceId,final  EntityModel entity, final boolean isPersentMode) {
        this(name, period, "", 0l, 0l, 0l, 0l, instanceId, 0l, null, null, null, null, null, null, isPersentMode);
        isPeriod = true;
        this.entity = entity;
        this.setText(TITLE_COL_INDEX, getStrPeriod(0));
    }

    TreeItem(final String name, final Timestamp period, final String context, final Long duration, final Long minDuration, final Long maxDuration, final Long count, final Long instanceId, final long pureDuration,
           final  Double load, final Double pureLoad, final Double avgLoad, final Double avgPureLoad, final Pid pid, final String title, final boolean isPercentMode) {
        this.name = name;
        this.pid = pid;
        this.period = period;
        this.context = context;
        this.duration = duration;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.count = count;
        this.pureDuration = pureDuration;
        this.load = load == null ? 0.0 : load;
        this.pureLoad = pureLoad == null ? 0.0 : pureLoad;
        this.avgLoad = avgLoad == null ? 0.0 : load;
        this.avgPureLoad = avgPureLoad == null ? 0.0 : pureLoad;
        this.instanceId = instanceId;
        this.title = title;
        this.isPercentMode = isPercentMode;
    }

    public void setItemText() {
        String sName = title;
        if (title == null && isPeriod) {
            sName = getStrPeriod(0);
        }
        this.setText(TITLE_COL_INDEX, sName);

        String str = isMeaningful() ? String.valueOf(count) : "";
        this.setText(COUNT_COL_INDEX, str);
        this.setTextAlignment(COUNT_COL_INDEX, AlignmentFlag.AlignRight.value() | AlignmentFlag.AlignVCenter.value());
        
        str = isMeaningful() ? translateToMicroseconds(Double.valueOf(minDuration)) : "";
        this.setText(MIN_LOAD_COL_INDEX, isPeriod ? "" : str);

        str = isMeaningful() ? translateToMicroseconds(Double.valueOf(maxDuration)) : "";
        this.setText(MAX_LOAD_COL_INDEX, isPeriod ? "" : str);

        changePercentMode(isPercentMode);
    }

    public void changePercentMode(final boolean isPercentMode) {
        if (this.isPercentMode != isPercentMode) {
            this.isPercentMode = isPercentMode;
        }
        
        String str = durationStaticticToStr(load, duration);
        this.setText(LOAD_COL_INDEX, isPeriod ? "" : str);
        
        str = isMeaningful() ? durationStaticticToStr(avgLoad / count, duration / count) : "";
        this.setText(AVG_LOAD_COL_INDEX, str);

        str = isMeaningful() ? durationStaticticToStr(pureLoad, pureDuration) : "";
        this.setText(PURE_LOAD_COL_INDEX, str);

        str=isMeaningful() ? durationStaticticToStr(avgPureLoad / count,pureDuration/count):"";
        this.setText(PURE_AVG_LOAD_COL_INDEX, str);       
        
        str = domainStaticticToStr(domainCPU,getDuration());
        this.setText(CPU_COL_INDEX, str);

        str = domainStaticticToStr(domainDb,getDuration());
        this.setText(DB_COL_INDEX, str);

        str = domainStaticticToStr(domainExt,getDuration());
        this.setText(EXT_COL_INDEX, str);
        
        final Double d=/*isOutOfDomrn ? getDuration():*/getPureDuration();
        str = domainStaticticToStr(pureDomainCPU,d);
        this.setText(PURE_CPU_COL_INDEX, str);

        str = domainStaticticToStr(pureDomainDb,d);
        this.setText(PURE_DB_COL_INDEX, str);

        str = domainStaticticToStr(pureDomainExt,d);
        this.setText(PURE_EXT_COL_INDEX, str);
        
        final int alignmentFlag=AlignmentFlag.AlignRight.value() | AlignmentFlag.AlignVCenter.value();
        for(int i=1;i<this.columnCount();i++){
            this.setTextAlignment(i, alignmentFlag);
        }

    }

    private String durationStaticticToStr(final double percentDuration, final long duration) {
        String str = "";
        if (!isPeriod) {
            if (isPercentMode) {
                final Double n = scaleNumber(percentDuration, 2);
                str = numberToString(n, "0.00");
            } else {
                str = translateToMicroseconds(Double.valueOf(duration));
            }
        }
        return str;
    }

    private String domainStaticticToStr(final long domain,final Double duration) {
        String str = "";
        if (isMeaningful() || name.equals(AbstractProfileTree.STR_CONTEXT_TREE_NODE)) {
            if (isPercentMode) {
                final double n = duration==0.0 ? 0 : scaleNumber((domain / duration) * 100.0, 2);
                str = numberToString(n, "0.00");
            } else {
                str = translateToMicroseconds(Double.valueOf(domain));
            }
        }
        return str;
    }

    private String translateToMicroseconds(Double n) {
        if (n != 0) {
            n = n * Math.pow(10, -3);
            n = scaleNumber(n, accuracy);
        }
        return numberToString(n, "#,##0.000");
    }

    private Double scaleNumber(final Double n, final int accuracy) {
        if (n == 0) {
            return n;
        }
        BigDecimal bigDecimal = new BigDecimal(n);
        bigDecimal = bigDecimal.setScale(accuracy, BigDecimal.ROUND_HALF_EVEN);
        return bigDecimal.doubleValue();
    }

    private String numberToString(final Double n, final String format) {
        if (n == 0) {
            return "0";
        }
        final DecimalFormat f = new DecimalFormat(format);
        final DecimalFormatSymbols symbols = f.getDecimalFormatSymbols(); 
        symbols.setGroupingSeparator(' ');
        f.setDecimalFormatSymbols(symbols);
        f.setGroupingUsed(true);
        
        final String res = f.format(n);
        if (res.equals(f.format(0))) {
            return "0";
        }
        return f.format(n);
    }

    public void copyItemTextFrom(final TreeItem item, final RadEnumPresentationDef timeSectionEnum, final ItemTextCalculator itemCalc) {
        load = item.load;
        pureLoad = item.pureLoad;
        pureDuration = item.pureDuration;
        avgLoad = item.avgLoad;
        avgPureLoad = item.avgPureLoad;
        setPureDomain(item.pureDomainCPU, item.pureDomainDb, item.pureDomainExt);
        setDomain(item.domainCPU, item.domainDb, item.domainExt);
        setTitleWithContext(timeSectionEnum,itemCalc);
        for (int i = 1; i < item.columnCount(); i++) {
            this.setText(i, item.text(i));
            this.setTextAlignment(i, AlignmentFlag.AlignRight.value() | AlignmentFlag.AlignVCenter.value());
        }
    }

    public void setTitleWithContext(final RadEnumPresentationDef timeSectionEnum, final ItemTextCalculator itemCalc) {
        if ("".equals(context)) {
            this.setText(TITLE_COL_INDEX, title);
        } else {
            this.setText(TITLE_COL_INDEX, title + " {" + getStrContext(context, timeSectionEnum,itemCalc) + "}");
        }
    }

    private String getStrContext(final String context, final RadEnumPresentationDef timeSectionEnum,final ItemTextCalculator itemCalc) {
        String res = "";
        final String[] prefixes = context.split(";");
        res += itemCalc.getItemTitle(prefixes[0], timeSectionEnum);
        if (prefixes.length > 1) {
            for (int i = 1; i < prefixes.length; i++) {
                res += "->" + itemCalc.getItemTitle(prefixes[i], timeSectionEnum);
            }
        }
        return res;
    }

    public List<Pid> calcChildPids(final TreeItem item, List<Pid> pids) {
        for (int i = 0; i < item.childCount(); i++) {
            final TreeItem childItem = (TreeItem) item.child(i);
            final Pid childPid = childItem.getPid();
            if ((childPid != null) && (!pids.contains(childPid))) {
                pids.add(childPid);
            }
            pids = calcChildPids(childItem, pids);
        }
        return pids;

    }

    public void sumItemTextWith(final TreeItem item, final boolean withChilds, final boolean isContext) {
        duration += item.duration;
        pureDuration += item.pureDuration;
        count += item.count;
        pureDomainCPU += item.pureDomainCPU;
        pureDomainDb += item.pureDomainDb;
        pureDomainExt += item.pureDomainExt;
        domainCPU += item.domainCPU;
        domainDb += item.domainDb;
        domainExt += item.domainExt;
        if (withChilds) {
            for (int i = 0; i < item.childCount(); i++) {
                boolean b = false;
                final TreeItem child = (TreeItem) item.child(i);
                for (int j = 0; j < this.childCount(); j++) {
                    final TreeItem ch = (TreeItem) this.child(j);
                    if (ch.isCanSumWith(child, isContext)) {
                        item.removeChild(child);
                        i--;
                        ch.sumItemTextWith(child, withChilds, isContext);
                        b = true;
                        break;
                    }
                }
                if (!b) {
                    item.removeChild(child);
                    i--;
                    this.addChild(child);
                }
            }
        }
    }

    public boolean isCanSumWith(final TreeItem item, final boolean isContext) {
        return isContext ? this.getName().equals(item.getName()) && this.getContext().equals(item.getContext())
                : this.getName().equals(item.getName());
    }

    public void calculatePureDuration() {
        for (int i = 0; i < this.childCount(); i++) {
            final TreeItem child = (TreeItem) this.child(i);
            child.calculatePureDuration();
        }
        if (duration <= 0) {
            pureDuration = 0;
            duration = getChildDuration();
        } else {
            pureDuration = duration - getChildDuration();
        }
        setItemText();
    }

    private Long getChildDuration() {
        Long chilsDuration = 0l;
        for (int i = 0; i < this.childCount(); i++) {
            final TreeItem child = (TreeItem) this.child(i);
            chilsDuration += child.duration;
        }
        return chilsDuration;
    }
    
    public void calculateDuration() {
        for (int i = 0; i < this.childCount(); i++) {
            final TreeItem child = (TreeItem) this.child(i);
            child.calculateDuration();
        }
        if (duration <= 0) {
            pureDuration = 0;
            duration = getChildDuration();
        } else {
            duration = pureDuration + getChildDuration();
        }
        setItemText();
    }

    @Override
    public QTreeWidgetItem clone() {
        final TreeItem res = new TreeItem(name, period, context, duration, minDuration, maxDuration, count, instanceId, pureDuration, load, pureLoad, avgLoad, avgPureLoad, pid, title, isPercentMode);
        res.entity = this.entity;
        res.isPeriod = this.isPeriod;
        res.isCalculated = this.isCalculated;
        res.setDomain(domainCPU, domainDb, domainExt);
        res.setPureDomain(pureDomainCPU, pureDomainDb, pureDomainExt);
        for (int i = 0; i < this.childCount(); i++) {
            final TreeItem item = (TreeItem) this.child(i).clone();
            res.addChild(item);
        }
        res.setText(TITLE_COL_INDEX, text(TITLE_COL_INDEX));
        for (int i = 1; i < columnCount(); i++) {
            res.setText(i, text(i));
            res.setTextAlignment(i, AlignmentFlag.AlignRight.value() | AlignmentFlag.AlignVCenter.value());
        }
        return res;
    }

    public String getName() {
        return name;
    }

    public void calcTitleWithoutPrefixes() {
        final String[] prefixes = title.split(" - ");
        title = prefixes[prefixes.length - 1];
        for (int k = 0; k < childCount(); k++) {
            final TreeItem child = (TreeItem) child(k);
            child.calcTitleWithoutPrefixes();
        }

    }

    public String getContext() {
        return context;
    }

    public Timestamp getPeriod() {
        return period;
    }

    protected final String getStrPeriod(final int length) {
        String strPeriod = "";
        if (period != null) {
            strPeriod = period.toString();
            final int n = strPeriod.lastIndexOf(".");
            if (n != -1) {
                strPeriod = strPeriod.substring(0, n);
            }
            final String s = "Inst #" + instanceId + " - " + strPeriod;

            String space = "";
            for (int i = 0; i < length - s.length(); i++) {
                space += " ";
            }
            strPeriod = "Inst #" + instanceId + " - " + space + strPeriod;
        }
        return strPeriod;
    }

    public void calcDomains(final RadEnumPresentationDef timeSectionEnum) {
        if (name.equals(AbstractProfileTree.STR_CONTEXT_TREE_NODE)) {           
            calcChildSumDomains( timeSectionEnum, AbstractProfileTree.domainCpuId, AbstractProfileTree.domainDbId, AbstractProfileTree.domainExtId); 
            //calcChildPureDomains( timeSectionEnum, AbstractProfileTree.domainCpuId, AbstractProfileTree.domainDbId, AbstractProfileTree.domainExtId); 
        } else {          
            calcSumDomain( timeSectionEnum, AbstractProfileTree.domainCpuId, AbstractProfileTree.domainDbId, AbstractProfileTree.domainExtId); 
            calcPureDomain(timeSectionEnum, AbstractProfileTree.domainCpuId, AbstractProfileTree.domainDbId, AbstractProfileTree.domainExtId);
        }    
    }
        
   private RadEnumPresentationDef.Item getConstItem(final RadEnumPresentationDef timeSectionEnum){
        String constItemName=name;
        final int index=name.indexOf(":");
        if(index!=-1){
                constItemName=name.substring(0,index);
        }
        return timeSectionEnum.findItemByValue(constItemName);
    }
        
    private void calcPureDomain(final RadEnumPresentationDef timeSectionEnum,final Id domainCpuId,final Id domainDbId,final Id domainExtId){
        final RadEnumPresentationDef.Item constItem=getConstItem( timeSectionEnum);
        if(constItem!=null && constItem.getConstant().isInDomain(domainCpuId)){
            setPureDomain(pureDuration, 0, 0);
        } else if (constItem!=null && constItem.getConstant().isInDomain(domainDbId)){
            setPureDomain(0, pureDuration, 0);
        } else if (constItem!=null && constItem.getConstant().isInDomain(domainExtId)){
            setPureDomain(0, 0, pureDuration);
        } else {
            //isOutOfDomrn=true;
            //if (childCount() > 0) {
            //    setPureDomain(pureDuration, 0, 0);
            //    calcChildPureDomains(timeSectionEnum, domainCpuId, domainDbId, domainExtId);
           // }else{
                setPureDomain(pureDuration, 0, 0);
            //}
        }    
    }
    
    private void calcChildPureDomains(final RadEnumPresentationDef timeSectionEnum, final Id domainCpuId, final Id domainDbId, final Id domainExtId) {
        //pureDomainCPU = pureDuration;
        for (int i = 0; i < childCount(); i++) {
            final TreeItem item = (TreeItem) child(i);
            if (!name.equals(AbstractProfileTree.STR_CONTEXT_TREE_NODE)) {
                item.calcPureDomain(timeSectionEnum, domainCpuId, domainDbId, domainExtId);
            }
            pureDomainCPU += item.domainCPU;
            pureDomainDb += item.domainDb;
            pureDomainExt += item.domainExt;
         }
    }
    
    private void calcSumDomain(final RadEnumPresentationDef timeSectionEnum,final Id domainCpuId,final Id domainDbId,final Id domainExtId){
        final RadEnumPresentationDef.Item constItem=getConstItem( timeSectionEnum);        
        if (constItem!=null && constItem.getConstant().isInDomain(domainDbId)){
            setDomain(0, pureDuration, 0);
        } else if (constItem!=null && constItem.getConstant().isInDomain(domainExtId)){
            setDomain(0, 0, pureDuration);
        } else{
            setDomain(pureDuration, 0, 0);
        }
        if(this.childCount()>0){
            calcChildSumDomains(timeSectionEnum, domainCpuId, domainDbId, domainExtId);
        }    
    }  
    

    private void calcChildSumDomains(final RadEnumPresentationDef timeSectionEnum, final Id domainCpuId, final Id domainDbId, final Id domainExtId) {
        //pureDomainCPU = pureDuration;
        for (int i = 0; i < childCount(); i++) {
            final TreeItem item = (TreeItem) child(i);
            if (!name.equals(AbstractProfileTree.STR_CONTEXT_TREE_NODE)) {
                item.calcSumDomain(timeSectionEnum, domainCpuId, domainDbId, domainExtId);
            }
            domainCPU += item.domainCPU;
            domainDb += item.domainDb;
            domainExt += item.domainExt;
         }
    }

    public Pid getPid() {
        return pid;
    }

    public boolean isPeriod() {
        return isPeriod;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public Double getDuration() {
        return duration > 0 ? Double.valueOf(duration) : Double.valueOf(getChildDuration());
    }
    
    public Double getPureDuration() {
        return Double.valueOf(pureDuration);
    }

    public void setLoad(final Double load) {
        this.load = load;
        this.avgLoad = load;
    }
       
    public void setPureLoad(final Double pureLoad) {
        this.pureLoad = pureLoad;        
        this.avgPureLoad = pureLoad;
    }

   /* public void setAvgLoad(Double avgLoad) {
        this.avgLoad = avgLoad;
    }

    public void setAvgPureLoad(Double avgPureLoad) {
        this.avgPureLoad = avgPureLoad;
    }*/

    public void setPureDomain(final long domainCPU, final long domainDb, final long domainExt) {
        this.pureDomainCPU = domainCPU;
        this.pureDomainDb = domainDb;
        this.pureDomainExt = domainExt;
    }
    
    public void setDomain(final long domainCPU, final long domainDb, final long domainExt) {
        this.domainCPU = domainCPU;
        this.domainDb = domainDb;
        this.domainExt = domainExt;
    }

    public Double getLoad() {
        return this.load;
    }

    public Double getPureLoad() {
        return this.pureLoad;
    }

    /*public Double getAvgDuration() {
        return isMeaningful() ? Double.valueOf(duration / count) : 0;
    }

    public Double getAvgPureDuration() {
        return isMeaningful() ? Double.valueOf(pureDuration / count) : 0;
    }*/

    public boolean isCalculated() {
        return isCalculated;
    }

    public void setIsCalced(final boolean isCalced) {
        this.isCalculated = isCalced;
    }

    public boolean isMeaningful() {
        return count > 0;
    }

    public boolean isPercentMode() {
        return isPercentMode;
    }

    public EntityModel getEntity() {
        return entity;
    }    
    
    public String childsToHtml(final String tab, final List<Integer> columns) {
        String res = "";
        for (int i = 0; i < childCount(); i++) {
            final TreeItem item = (TreeItem) child(i);
            res += "<tr>";
            for (int j = 0; j < columns.size(); j++) {
                final Integer columnIndex=columns.get(j);
                res += columnIndex == 0 ? "<td nowrap=\"nowrap\">" : "<td align=\"right\">";
                res += "<font color=\""+ item.foreground(0).color().name()+"\">";
                res += "<tt>" + tab + item.text(columnIndex) + "</tt></font></td>";
            }
            res += "</tr>";            
            res += item.childsToHtml( tab + AbstractProfileTree.strTab, columns);            
        }
        return res;
    }
    
    public String itemToHtml(final String tab, final List<Integer> columns) {
        String res = "";
        res += "<tr>";
        for (int j = 0; j < columns.size(); j++) {
                Integer columnIndex=columns.get(j);
                res += columnIndex == 0 ? "<td nowrap=\"nowrap\">" : "<td align=\"right\">";
                res += "<tt>" + tab + text(columnIndex) + "</tt></td>";
        }
        res += "</tr>";
        res += childsToHtml( tab + AbstractProfileTree.strTab, columns);
        return res;
    }    
}
