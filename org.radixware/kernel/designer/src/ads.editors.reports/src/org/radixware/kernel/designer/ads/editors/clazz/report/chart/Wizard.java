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

package org.radixware.kernel.designer.ads.editors.clazz.report.chart;


import java.util.HashMap;
import java.util.Map;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportChartCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.IReportWidgetContainer;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;



public class Wizard  extends WizardSteps {

    private final Settings settings;
    
    public Wizard(final AdsReportChartCell cell,IReportWidgetContainer band){     
        settings=new Settings(cell, band);       
    }  
    
    public Map<Id,IMultilingualStringDef> getMlStrings(){
        return settings.getMlStringList();
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public Step createInitial() {
       return new AdsReportChartCellSetupStep1();
    }

    @Override
    public Object createSettings() {
        return settings;
    }

    @Override
    public String getDisplayName() {
         return NbBundle.getMessage(Wizard.class, "CreateChartCellWizardTitle");
    }
    
    static final class Settings {
        private final AdsReportChartCell cell;
        private final AdsReportClassDef ownerReport;
        private Map<Id,IMultilingualStringDef> mlStringList=new HashMap<>();
        private final IReportWidgetContainer band;

        public Settings(final AdsReportChartCell cell,final IReportWidgetContainer band) {
            this.cell=cell;
            this.band=band;
            this.ownerReport=band.getOwnerForm().getOwnerReport();
        }
        
        AdsReportChartCell getCell(){
            return cell;
        }
        
        AdsReportClassDef getOwnerReport(){
            return ownerReport;
        }
        
        IReportWidgetContainer getOwnerBand(){
            return band;
        }
        
        Map<Id,IMultilingualStringDef> getMlStringList(){
            return mlStringList;
        }
    }
    
}
