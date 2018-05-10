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

package org.radixware.kernel.explorer.editors.monitoring.diagram;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.*;
import java.awt.Color;
import java.awt.Point;
import java.sql.Timestamp;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.explorer.editors.monitoring.diagram.AbstaractMetricView.MetricValue;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.EDiagramType;
import org.radixware.kernel.explorer.editors.monitoring.diagram.MetricHistWidget.MetricHistIcons;
import org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs.AbstractMetricSettings;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;


public class DiagramPanel extends QWidget{
    private final MetricHistWidget parent;
    private QHBoxLayout diagramLayout;
    private final IClientEnvironment environment;
    private DiagramWidget diagram;
    private boolean isHistorical=false;
    private QLabel lbTite;
    private boolean isSelected=false;
    private final EDiagramType diagramType;
    
    private enum Edge {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
    
    private int xMm, yMm;
    private Edge edge;
    private int oldWidth, oldHeight;
    private boolean isDragging=false;
    private int GRID_SIZE_MM=10;
    
    public DiagramPanel(final MetricHistWidget parent,final String title,final EDiagramType diagramType,final IClientEnvironment environment,final boolean isHistorical,final int index){
        super(parent);  
        this.diagramType=diagramType;
        this.environment=environment;
        this.parent=parent;
        this.isHistorical=isHistorical;
        setAutoFillBackground(true);
        createUi(title);
    }
    
    public boolean isSelected(){        
        return isSelected;
    }
    
    public void setSelected(final boolean isSelected){
        if(isSelected){
            parent.clearSelections();
            this.setFocus();
        }
        if(isSelected!=this.isSelected){
            this.isSelected=isSelected;
            raise();
            update();
        }
    }

    @Override
    protected void keyPressEvent(final QKeyEvent qke) {
        super.keyPressEvent(qke);
        if(isSelected && qke.key()==Qt.Key.Key_Delete.value()){
            btnClosePressed();
        }
    }

    @Override
    protected void paintEvent(final QPaintEvent qpe) {
        super.paintEvent(qpe);        
        if(isSelected){ 
            final QPainter p = new QPainter(this);
            p.setPen(QColor.gray);
            paintBorder(p);
        }
    }
    
    public static final int EDGE_SIZE_PX = 5;
    protected void paintBorder(final QPainter p ) {        
        final int width = width() - 1;
        final int height = height() - 1;
        p.drawRect(EDGE_SIZE_PX / 2, EDGE_SIZE_PX / 2, width - EDGE_SIZE_PX + 1, height - EDGE_SIZE_PX + 1);
        final QColor color=this.palette().color(QPalette.ColorRole.Window);
        // top
        drawEdge(p, (width - EDGE_SIZE_PX) / 2, 0,color);
        // right
        drawEdge(p, width - EDGE_SIZE_PX, (height - EDGE_SIZE_PX) / 2,color);
        // bottom
        drawEdge(p, (width - EDGE_SIZE_PX) / 2, height - EDGE_SIZE_PX,color);
        // left
        drawEdge(p, 0, (height - EDGE_SIZE_PX) / 2,color);
        // top left
        drawEdge(p, 0, 0,color);
        // top right
        drawEdge(p, width - EDGE_SIZE_PX, 0,color);
        // bottom left
        drawEdge(p, 0, height - EDGE_SIZE_PX,color);
        // bottom right
        drawEdge(p, width - EDGE_SIZE_PX, height - EDGE_SIZE_PX,color);        
    }    
        
    private static void drawEdge(final QPainter p, final int x, final int y,final QColor color) {
         p.drawRect(x, y, EDGE_SIZE_PX, EDGE_SIZE_PX);         
         p.fillRect(x + 1, y + 1, EDGE_SIZE_PX - 1, EDGE_SIZE_PX - 1,color);
    }
    
    @Override
    protected void mouseReleaseEvent(final QMouseEvent qme) {
        super.mouseReleaseEvent(qme);
        if(isDragging){                       
            final int x = this.geometry().x();
            final int y = this.geometry().y();
            if(this.parentWidget()!=null && this.parentWidget().layout()!=null && 
               this.parentWidget().layout() instanceof GridLayout){
               final GridLayout layout=(GridLayout) this.parentWidget().layout();
               if(edge==null){
                   layout.updateIndexes(this, new Point(x,y));
               }else{
                   final boolean isLeft= edge ==Edge.LEFT || edge ==Edge.BOTTOM_LEFT || edge ==Edge.TOP_LEFT;
                   final boolean isUp= edge ==Edge.TOP || edge ==Edge.TOP_LEFT || edge ==Edge.TOP_RIGHT;
                   layout.updateSpan(this,oldWidth,oldHeight, isLeft, isUp);                   
               }
               parent.saveDiagramsToCfg();
            }
            isDragging=false; 
            if(this.parentWidget()!=null &&  this.parentWidget() instanceof PageWidget){
                ((PageWidget)this.parentWidget()).setIsDragging (false);
                ((PageWidget)this.parentWidget()).repaint();
            }
            ((GridLayout)this.parentWidget().layout()).adjust();
        }
        setSelected(true);
        update();  
        qme.accept();
    }
    
    public boolean isDragging(){
        return isDragging;
    }
    
    @Override
    protected void mousePressEvent(final QMouseEvent qme) {
        super.mousePressEvent(qme);
        if(isSelected && qme.button()==MouseButton.RightButton){
            final ExplorerMenu menu=new ExplorerMenu(this);
            final QIcon icon = null;//getImageManager().loadSvgIcon("classpath:images/class.svg", QColor.transparent);
            final ExplorerAction editAction = new ExplorerAction(icon, Application.translate("JmlEditor", "Edit") + "...", this);
            editAction.triggered.connect(this, "editDiagram()");
            final ExplorerAction deleteAction = new ExplorerAction(icon, Application.translate("JmlEditor", "Delete") , this);
            deleteAction.triggered.connect(this, "btnClosePressed()");
            menu.addAction((Action)editAction);
            menu.addAction((Action)deleteAction);
            menu.popup(qme.globalPos());
        }else if(qme.button()==MouseButton.LeftButton){
            final int x = qme.x();
            final int y = qme.y();
            xMm=x;
            yMm=y;
            edge = null;
            oldWidth = width();
            oldHeight = height();
            final int EDGE =EDGE_SIZE_PX + 1;
            //oldWidth=width;oldHeight=height;
            if (isSelected()) {
                if (x <= EDGE && y < EDGE) {
                    edge = Edge.TOP_LEFT;
                } else if (x >= oldWidth - EDGE && y < EDGE) {
                    edge = Edge.TOP_RIGHT;
                } else if (x <= EDGE && y >= oldHeight - EDGE) {
                    edge = Edge.BOTTOM_LEFT;
                } else if (x >= oldWidth - EDGE && y >= oldHeight - EDGE) {
                    edge = Edge.BOTTOM_RIGHT;
                } else if (x >= (oldWidth - EDGE) / 2 && x <= (oldWidth + EDGE) / 2 && y <= EDGE) {
                    edge = Edge.TOP;
                } else if (y >= (oldHeight - EDGE) / 2 && y <= (oldHeight + EDGE) / 2 && x >= oldWidth - EDGE) {
                    edge = Edge.RIGHT;
                } else if (x >= (oldWidth - EDGE) / 2 && x <= (oldWidth + EDGE) / 2 && y >= oldHeight - EDGE) {
                    edge = Edge.BOTTOM;
                } else if (y >= (oldHeight - EDGE) / 2 && y <= (oldHeight + EDGE) / 2 && x <= EDGE) {
                    edge = Edge.LEFT;
                }                
            }
        }  
        qme.accept();
    }
    
     @Override
    protected void mouseMoveEvent(final QMouseEvent qme) {
        super.mouseMoveEvent(qme);
        if(!isSelected) return;
        isDragging=true;
        if(this.parentWidget()!=null &&  this.parentWidget() instanceof PageWidget){
            ((PageWidget)this.parentWidget()).setIsDragging (true);
            ((PageWidget)this.parentWidget()).repaint();
        }
            
        final int dXMm = qme.pos().x()-xMm;
        final int dYMm = qme.pos().y()-yMm;  

        // calculate position on band (prepare to snap to grid)
        int posXMm = x() + dXMm;        
        if (edge == Edge.TOP_RIGHT || edge == Edge.BOTTOM_RIGHT || edge == Edge.RIGHT) {
            posXMm += width();
        }
        int posYMm = y() + dYMm;
        if (edge == Edge.BOTTOM_LEFT || edge == Edge.BOTTOM_RIGHT || edge == Edge.BOTTOM) {
            posYMm += height();
        }
        
        // snap to grid
        //posXMm = MmUtils.snapToGrid(posXMm);
        //posYMm = MmUtils.snapToGrid(posYMm);

        // perform
        if (edge == Edge.TOP_RIGHT || edge == Edge.BOTTOM_RIGHT || edge == Edge.RIGHT) {
            posXMm = Math.max(posXMm, x() + GRID_SIZE_MM);
            final int oldWidthMm = width();
            final int newWidthMm = posXMm - x();
            setGeometry(x(), y(), newWidthMm, height());
            xMm += (newWidthMm - oldWidthMm);
        } else {
            posXMm = Math.max(0, posXMm);
            if (edge == Edge.TOP_LEFT || edge == Edge.BOTTOM_LEFT || edge == Edge.LEFT) {
                posXMm = Math.min(posXMm, x() +  width() - GRID_SIZE_MM);
                final int dWidthMm = x() - posXMm;
                final int newLeftMm = x()- dWidthMm;
                final int newWidthMm =  width() + dWidthMm;
                setGeometry(newLeftMm, y(), newWidthMm, height());
            } else if (edge == null) {
                final int dMm = posXMm - x();
                final int newLeftMm = x() + dMm;                        
                setGeometry(newLeftMm, y(), width(), height());
            }
        }        
        
        if (edge == Edge.BOTTOM_LEFT || edge == Edge.BOTTOM_RIGHT || edge == Edge.BOTTOM) {
            posYMm = Math.max(posYMm, y() + GRID_SIZE_MM);
            final int oldHeightMm = height();
            final int newHeightMm = posYMm - y();
            setGeometry(x(), y(), width(), newHeightMm);
            yMm += (newHeightMm - oldHeightMm);
        } else {
            posYMm = Math.max(0, posYMm);
            if (edge == Edge.TOP_LEFT || edge == Edge.TOP_RIGHT || edge == Edge.TOP) {
                posYMm = Math.min(posYMm, y() + height() - GRID_SIZE_MM);
                final int dMm = y() - posYMm;
                final int newTopMm = y() - dMm;
                final int newHeightMm = height() + dMm;
                setGeometry(x(),newTopMm, width(), newHeightMm);
            } else if (edge == null) {
                final int dMm = posYMm - y();  
                final int newTopMm = y() + dMm;
                setGeometry(x(),newTopMm, width(), height());
            }
        }   
       qme.accept();
    }
    
    @SuppressWarnings("unused")
    private void editDiagram(){
//        AbstaractMetricView metricView=parent.findView(this);
//        if(metricView!=null && diagram!=null){
//            DiagramEditorDialog chooseInst = new DiagramEditorDialog(metricView.getMetricSettings(),parent,isHistorical);
//            if (chooseInst.exec() == QDialog.DialogCode.Accepted.value()) { 
//                parent.updateView( metricView,chooseInst.getMetricSettings());
//            }
//        }
    }
    
    public void setDiagram(final EDiagramType diagramType){
        diagramLayout.removeWidget(diagram);
        this.diagram=createDiagram(diagramType);
        diagramLayout.addWidget(diagram);
    }
    
    public String getTitle(){
        return lbTite.text();
    }
    
    public void setTitle(final String title){
        lbTite.setText(title);
    }
   
    private Double[][] errorVals;
    private Double[][] warnVals;
    public void setErrorWarnArea(final Double lowErrorValue,final Double upErrorValue,final Double lowWarningValue,final Double upWarningValue){
        final double maxVal=Double.MAX_VALUE;
        final double minVal=Double.MIN_VALUE;
        if(diagram==null){
            return;
        }
        diagram.clearMarkers();
        errorVals=new Double[2][2];
        warnVals=new Double[2][2];
        if(lowWarningValue != null){
            if(lowErrorValue!=null && lowErrorValue<lowWarningValue){
               diagram.setWarnArea(lowErrorValue,lowWarningValue);
               warnVals[0][0]=lowErrorValue;
               warnVals[0][1]=lowWarningValue;
            }else{
               diagram.setWarnArea(minVal,lowWarningValue);  
               warnVals[0][0]=minVal;
               warnVals[0][1]=lowWarningValue;
            }            
        }
        if(upWarningValue != null){
            if(upErrorValue!=null && upErrorValue>upWarningValue){
               diagram.setWarnArea(upWarningValue,upErrorValue); 
               warnVals[1][0]=upWarningValue;
               warnVals[1][1]=upErrorValue;
            }else{
               diagram.setWarnArea(upWarningValue,maxVal); 
               warnVals[1][0]=upWarningValue;
               warnVals[1][1]=maxVal;
            }
        }
        if(lowErrorValue != null){
            if(lowWarningValue!=null && lowWarningValue<lowErrorValue){
               diagram.setErrorArea(lowWarningValue,lowErrorValue); 
               errorVals[0][0]=lowWarningValue;
               errorVals[0][1]=lowErrorValue;
            }else{
               diagram.setErrorArea(minVal,lowErrorValue); 
               errorVals[0][0]=minVal;
               errorVals[0][1]=lowErrorValue;
            }            
        }
        if(upErrorValue != null){
            if(upWarningValue!=null && upWarningValue>upErrorValue){
               diagram.setErrorArea(upErrorValue,upWarningValue); 
               errorVals[1][0]=upErrorValue;
               errorVals[1][1]=upWarningValue;
            }else{
               diagram.setErrorArea(upErrorValue,maxVal); 
               errorVals[1][0]=upErrorValue;
               errorVals[1][1]=maxVal;
            }
        }
    }
    
    public EDiagramType getDiagramType(){
        return  diagramType;
    }
    
    private void createUi(final String title){
        final QVBoxLayout mainLayout=new QVBoxLayout();
        mainLayout.setMargin(1);
        mainLayout.setContentsMargins(0, 0, 0, 0);
        mainLayout.setWidgetSpacing(0);
        
         diagramLayout=new QHBoxLayout();
        diagramLayout.setContentsMargins(0, 0, 0, 0);
        diagramLayout.setWidgetSpacing(0);
        diagram=createDiagram( diagramType);
        diagramLayout.setMargin(6);

        if(diagram!=null){
            diagramLayout.addWidget(diagram);    
        }
        
        final QHBoxLayout titleLayout=new QHBoxLayout();
        titleLayout.setWidgetSpacing(0);
        titleLayout.setContentsMargins(6, 6, 6, 0);
        lbTite=new QLabel(title,this);
        final QFont font=lbTite.font();
        font.setPixelSize(16);
        font.setBold(true);
        lbTite.setFont(font);
        titleLayout.addWidget(lbTite);
        
        QIcon icon=ExplorerIcon.getQIcon(MetricHistIcons.VALUE_SCALE);
        String toolTip=Application.translate("SystemMonitoring", "Edit Diagram");
        final QToolButton btnConfig=createBtn( icon,"editDiagram()",toolTip);
        titleLayout.addWidget(btnConfig, 1, AlignmentFlag.AlignRight);
        
        icon=ExplorerIcon.getQIcon(ExplorerIcon.Dialog.BUTTON_CANCEL );
        toolTip=Application.translate("SystemMonitoring", "Remove Diagram");
        final QToolButton btnClose=createBtn( icon,"btnClosePressed()",toolTip);
        titleLayout.addWidget(btnClose);
        
        mainLayout.addLayout(titleLayout);
        mainLayout.addLayout(diagramLayout);  
        this.setLayout(mainLayout);
    }
    
    public void update(final List<MetricValue> vals,final Timestamp begTime,final Timestamp endTime,final AbstractMetricSettings metricSettings,final boolean isAsinc){
//        if(diagram!=null && metricSettings!=null){
//            setTitle(metricSettings.getTitle());
//            diagram.update(vals,begTime,endTime,metricSettings,isAsinc);         
//        }
    }
    
    public void changeTimeScale(final Timestamp begTime,final Timestamp endTime){
       if(begTime!=null && endTime!=null && diagram!=null){
            diagram.setTimeScale(begTime.getTime(),endTime.getTime());   
       }
    }
    
    private QToolButton createBtn(final QIcon icon,final String act,final String toolTip){
        final QToolButton btn=new QToolButton(this);
        btn.pressed.connect(this,act);
        btn.setAutoRaise(true);
        btn.setFocusPolicy(FocusPolicy.NoFocus);
        btn.setIcon(icon);
        btn.setToolTip(toolTip);
        btn.setIconSize(new QSize(16,16));
        return btn;
    }    
    
    private DiagramWidget createDiagram(final EDiagramType diagramType){
        DiagramWidget diagramWidget=null;
        if(diagramType==EDiagramType.STATISTIC){
            
            diagramWidget = new StatisticalDiagramWidget(environment, this);                
        }else if(diagramType==EDiagramType.DOT){
            diagramWidget = new DotDiagramWidget(environment, this);
        }else if(diagramType==EDiagramType.STEP){
            diagramWidget = new StepDiagramWidget(environment, this) ;           
        }if(diagramType==EDiagramType.CORRELATION){
            diagramWidget = new CorrelationDiagramWidget(environment, this);                
        }        
        return diagramWidget;
    }
    
    @SuppressWarnings("unused")
    private void btnConfigPressed(){
       /*boolean isAutoRange=diagram.getValScale()==null;
       CfgValueRangeDialog dialog=new CfgValueRangeDialog(environment,this,diagram.getMinVal(),diagram.getMaxVal(),isAutoRange);
       if (dialog.exec() == QDialog.DialogCode.Accepted.value()) {
           if(dialog.isAutoRange()){
              diagram.setAutoValueRange();
           }else{
              diagram.setValueScale(dialog.getMinVal(), dialog.getMaxVal());
           }
           valScale.skipValue();
       }  */          
    }
    
    @SuppressWarnings("unused")
    private void btnClosePressed(){
        final String title=Application.translate("SystemMonitoring","Confirm to Delete Diagram");
        final String msg=Application.translate("SystemMonitoring","Do you really want to delete diagram?");
        if(environment.messageConfirmation(title, msg)){
            final AbstaractMetricView metricView=parent.findView(this);
            if(metricView!=null){
                metricView.remove();
                parent.saveDiagramsToCfg();
            }
            //parent.deleteDiagram(parent.findView(this));
            //parent.saveDiagramsToCfg();
            //parent.layout().removeWidget(this);
            //this.setParent(null);   
        }
    }
    
    public Color getErrorColor(){
        return diagram.getErrorColor();
    }
    
    public Color getWarningColor(){
        return diagram.getWarningColor();
    }
    
    public Color getNormalColor(){
        return diagram.getNormalColor();
    }
    
    public void setErrorColor(final Color color){
        if(color!=null){
            diagram.setErrorColor(color);
        }
    }
    
    public void setWarningColor(final Color color){
        if(color!=null){
            diagram.setWarningColor(color);
        }
    }
    
    public void setNormalColor(final Color color){
        if(color!=null){
            diagram.setNormalColor(color);
        }
    }
    
     public void setValueScale(final double minVal,final double maxVal){
         diagram.setValueScale(minVal, maxVal);
     }
     
      public void setAutoValueRange(){
         diagram.setAutoValueRange();
     }
}
