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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import java.util.LinkedList;
import java.util.List;


public class MetricData {
    private LinkedList<QIcon> icons;
    private Type type;
    private Range range;
    private Double val;
    private String text;    
    //private String metricKind;
    //private Id propId;
    private final LinkedList<MonitoringTreeItemDecoration> decorations = new LinkedList<>();
    private int minVal = 0;
    private int maxVal = 100;
    private QColor severityColor = null;
    private QFont textFont = null;
    private Qt.Alignment textAlignment = Qt.AlignmentFlag.createQFlags(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
    
    
    public  static enum Range{
        NORMAL, ERROR, WARNING
    }
    
    public  static enum Type{
        DIAGRAM, HTML, TEXT, DECORATIONS
    }

     public MetricData(/*TreeItem parent, String metricKind,Id propId*/){
          icons=new LinkedList<>();
          //this.parent=parent;
         // this.metricKind=metricKind;
          //this.propId=propId;
     }
    
    public MetricData(Type type/*, String metricKind,Id propId, TreeItem parent*/){
        this(/*parent,metricKind,propId*/);
        this.type=type;
    }
    
   /* public String getMetricKind(){
        return metricKind;
    }
    
    public Id getPropId(){
        return propId;
    }*/
    
    public void setDecorations(final List<MonitoringTreeItemDecoration> decorations) {
        this.decorations.clear();
        this.decorations.addAll(decorations);
    }

    public List<MonitoringTreeItemDecoration> getDecorations() {
        return decorations;
    }
    
    public void addIcon(QIcon icon){
        icons.add(icon);
    }
    
    public void addIcon(int index,QIcon icon){
        icons.add(index, icon);
    }
    
    public List<QIcon> getIconList(){
        return icons;
    }
    
    public Type getType(){
        return type;
    }
    
    public void setType(Type type){
        this.type=type;
    }
    
    public void setVal(Double val){
        this.val=val;
    }
    
    public Double  getVal(){
       return val;
    }
    
    public void setText(String text){
        this.text=text;
    }
    
    public String  getText(){
       return text==null ? "":text;
    }
    
    public void setRange(Range range){
        this.range=range;
    }
    
    public Range getRange(){
        return range;
    }
    
    //public TreeItem getTreeItem(){
    //    return parent;
    //}
    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public QColor getSeverityColor() {
        return severityColor;
    }

    public void setSeverityColor(QColor severityColor) {
        this.severityColor = severityColor;
    }

    public Qt.Alignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(Qt.Alignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public QFont getTextFont() {
        return textFont;
    }

    public void setTextFont(QFont textFont) {
        this.textFont = textFont;
    }
    
    
}
