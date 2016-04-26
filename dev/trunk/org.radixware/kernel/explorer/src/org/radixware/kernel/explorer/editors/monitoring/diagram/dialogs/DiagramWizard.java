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

package org.radixware.kernel.explorer.editors.monitoring.diagram.dialogs;

import com.trolltech.qt.gui.*;
import org.radixware.kernel.common.client.dashboard.DiagramSettings;
import org.radixware.kernel.explorer.editors.jmleditor.dialogs.BaseWizard;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;


public class DiagramWizard extends BaseWizard{
    private final HistoricalDiagramPage diagramEditorPage;
    private boolean isHistorical=true;
    
    public DiagramWizard(/*MetricHistWidget*/ MonitoringConfigHelper helper /*, final ExplorerSettings settings, String dlgName*/){
	super(helper.getAdsBridge().getDashboardView(), (ExplorerSettings) helper.getAdsBridge().getDashboardView().getEnvironment().getConfigStore(), "DiagramWizard");
        addPage(new DiagramTypePage(helper.getAdsBridge().getDashboardView()));
        diagramEditorPage=new HistoricalDiagramPage(helper);
        addPage(diagramEditorPage);
        setWindowTitle(Application.translate("SystemMonitoring", "Create Diagram"));
    }

  
   /* @SuppressWarnings("unused")
    private void btnBackWasClicked() {
        curItem = curDef;
    }

    public RadixObject getSelectedDef() {
        return curItem;
    }
    
    public EValType getValTypeDef() {
       if(isArrType){
           if(curItem instanceof AdsEnumDef)
               return  ((AdsEnumDef)curItem).getItemType()==EValType.STR?EValType.ARR_STR : EValType.ARR_INT;
            return EValType.ARR_REF;
       }
       return null;
    }

    @Override
    public void onItemClick(QModelIndex modelIndex) {
        setCurItem(modelIndex);
    }

    @Override
    public void onItemDoubleClick(QModelIndex modelIndex) {
        if (setCurItem(modelIndex)) {
            accept();
        } else if (this.button(QWizard.WizardButton.NextButton).isEnabled()) {
            TypeWizard.this.next();
        }
    }

    @Override
    public boolean setCurItem(QModelIndex modelIndex) {
        if (modelIndex != null) {
            if (modelIndex.model() instanceof ListModel) {
                AdsUserFuncDef.Lookup.DefInfo di = ((ListModel) modelIndex.model()).getDefList().get(modelIndex.row());
                if (di != null) {
                    curItem = AdsUserFuncDef.Lookup.findTopLevelDefinition(editor.getUserFunc(), di.getId());
                    curDef = curItem;
                }
            } else {
                curItem = ((ListModelForRadixObj) modelIndex.model()).getDefList().get(modelIndex.row());
            }
            this.button(QWizard.WizardButton.NextButton).setEnabled(true);
            this.button(QWizard.WizardButton.FinishButton).setEnabled(true);
            if (curItem instanceof AdsXmlSchemeDef || curItem instanceof AdsMsdlSchemeDef) {
                return false;
            }
            return true;
        }
        this.button(QWizard.WizardButton.NextButton).setEnabled(false);
        this.button(QWizard.WizardButton.FinishButton).setEnabled(false);
        return false;
    }
    
    @Override
    public boolean setSelectedDefinition(AdsDefinition def) {
        return false;
    }*/  
    
    //public MetricView getMetricView(){
   //    return metricView; 
    //}
    
    public /*AbstractMetricSettings*/ DiagramSettings getMetricSettings(){
            return diagramEditorPage.getMetricSettings();
        }

    public /*private*/ class DiagramTypePage extends QWizardPage {
        private final QWidget /*MetricHistWidget*/ parent;
        
        public DiagramTypePage(QWidget /*MetricHistWidget*/ parent) {
            super(DiagramWizard.this);           
            this.parent=parent;
            QVBoxLayout layout = new QVBoxLayout();
            this.setObjectName("ChooseDiagramTypePage");
            this.setTitle(Application.translate("SystemMonitoring", "Select Diagram Type"));
            QGroupBox groupBox = new QGroupBox("");
            groupBox.setObjectName("groupBox");

            RadioButton rbHistoricalValue = new RadioButton(Application.translate("SystemMonitoring", "History of Values"));
            rbHistoricalValue.setObjectName("rbHistoricalValue");
            rbHistoricalValue.clicked.connect(this, "historicalValueSelected()");

            RadioButton rbCorrelation = new RadioButton(Application.translate("SystemMonitoring", "Correlation Diagram"));
            rbCorrelation.setObjectName("rbCorrelation");
            rbCorrelation.clicked.connect(this, "correlationSelected()");

            rbHistoricalValue.setChecked(true);
           // historicalValueSelected() ;

            QVBoxLayout vbox = new QVBoxLayout();
            vbox.setObjectName("vbox");
            vbox.addWidget(rbHistoricalValue);
            vbox.addWidget(rbCorrelation);
            vbox.addStretch(1);
            groupBox.setLayout(vbox);
            layout.addWidget(groupBox);
            this.setLayout(layout);
            this.setFinalPage(false);
        }

        @SuppressWarnings("unused")
        private void historicalValueSelected() {
            isHistorical=true;
        }

        @SuppressWarnings("unused")
        private void correlationSelected() {
            isHistorical=false;
        }       

        private class RadioButton extends QRadioButton {

            RadioButton(String s) {
                super(s);
            }

            @Override
            protected void mouseDoubleClickEvent(QMouseEvent e) {
                super.mouseDoubleClickEvent(e);
//                DiagramWizard.this.next();
            }
        }
    }

    public /*private*/ class HistoricalDiagramPage extends QWizardPage {
        private QVBoxLayout layout;        
        private /*MetricHistWidget*/ MonitoringConfigHelper helper;
        private BaseDiagramEditor diagramEditor;

        public HistoricalDiagramPage(/*MetricHistWidget*/  MonitoringConfigHelper helper) {
            super(DiagramWizard.this);
            this.helper = helper;
            this.setObjectName("HistoricalDiagramPage");
            this.setTitle(Application.translate("SystemMonitoring", "Diagram Settings"));
            this.setFinalPage(true);        
            layout = new QVBoxLayout();
            layout.setMargin(0);            
            this.setLayout(layout);
        }
        
        @Override
        public void initializePage() {
            if(isHistorical){
                diagramEditor = new HistoricalDiagramEditor(null,helper);
                ((HistoricalDiagramEditor)diagramEditor).rangeChanged.connect(this,"metricSet()");
            }else{                
                diagramEditor = new CorrelationDiagramEditor(null,helper);
            }
            diagramEditor.metricSet.connect(this, "metricSet()"); 
            layout.addWidget(diagramEditor);
        }
        
        private void clear() {
            if (layout.indexOf(diagramEditor) != -1) {
                layout.removeWidget(diagramEditor);
            }
            diagramEditor.hide();
            layout.update();
        }
        
        @SuppressWarnings("unused")        
        private void metricSet(){
            this.completeChanged.emit();
        }

        @Override
        public boolean isComplete() {            
            return diagramEditor.isComplete();
        }      
        
        public /*AbstractMetricSettings*/DiagramSettings getMetricSettings(){
            return diagramEditor.getMetricSettings();
        }
        
        @Override
        public void cleanupPage() {
            clear();
        }
        
        

       /* public void clear() {
            if (layout.indexOf(ed) != -1) {
                layout.removeWidget(ed);
            }
            ed.hide();
            layout.update();
        }*/

       /* @Override
        public void initializePage() {
            boolean isParentRef=isArrType && selectedTypes.contains(EDefType.CLASS);
            ed = new HistoricalDiagramEditor(null,parent);
            chooseDefPanel.setObjectName("ChooseDefinitionPanel");
            layout.addWidget(chooseDefPanel);
            if (!((selectedTypes.contains(EDefType.XML_SCHEME)) || (selectedTypes.contains(EDefType.MSDL_SCHEME)))) {
                if (this.nextId() != -1) {
                    this.setFinalPage(true);
                    TypeWizard.this.removePage(this.nextId());
                }
            } else {
                this.setFinalPage(false);
                if (this.nextId() == -1) {
                    addPage(new org.radixware.kernel.explorer.editors.jmleditor.dialogs.TypeWizard.ChooseSchemaTypePage(parent));
                }
            }
        }*/

       /* @Override
        public void cleanupPage() {
            clear();
            chooseDefPanel.closeTread();
        }*/

        @Override
        public boolean validatePage() {
            if (diagramEditor instanceof HistoricalDiagramEditor){
                return ((HistoricalDiagramEditor)diagramEditor).isDateIntervalCorrect();
            }else{
                return true;
            }
        }

    }

   /* private class ChooseSchemaTypePage extends QWizardPage {

        private ChooseObjectPanel chooseObjPanel;

        public ChooseSchemaTypePage(QWidget parent) {
            super(parent);
            this.setObjectName("ChooseSchemaTypePage");
            QVBoxLayout layout = new QVBoxLayout();
            layout.setMargin(0);
            chooseObjPanel = new ChooseObjectPanel(TypeWizard.this, null);
            chooseObjPanel.setObjectName("ChooseObjectPanel");

            layout.addWidget(chooseObjPanel);
            this.setLayout(layout);
        }
        
        @Override
        public void initializePage() {
            chooseObjPanel.update(getTypeDeclarationList());
        }

        private Collection<RadixObject> getTypeDeclarationList() {
            Collection<RadixObject> allowedDefinitions = new ArrayList<RadixObject>();
            if (curItem instanceof AdsXmlSchemeDef) {
                AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) curItem;
                if (scheme == null) {
                    return allowedDefinitions;
                }
                Collection<String> types = scheme.getSchemaTypeList();
                for (String s : types) {
                    if (!s.contains("impl.")) {
                        AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newInstance(EValType.XML, scheme, s, 0);
                        type.setName(s);
                        allowedDefinitions.add(type);
                    }
                }
            }
            if (curItem instanceof AdsMsdlSchemeDef) {
                AdsMsdlSchemeDef scheme = (AdsMsdlSchemeDef) curItem;
                if (scheme == null) {
                    return allowedDefinitions;
                }
                Collection<String> types = scheme.getSchemaTypeList();
                for (String s : types) {
                    if (!s.contains("impl.")) {
                        AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newInstance(EValType.XML, scheme, s, 0);
                        type.setName(s);
                        allowedDefinitions.add(type);
                    }
                }
            }
            return allowedDefinitions;
        }
    }  */  
}
