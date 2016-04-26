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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.designer.ads.editors.clazz.report.chart.Wizard.Settings;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;


public class AdsReportChartCellSetupStep1 extends WizardSteps.Step<AdsReportChartCellSetupStep1Panel> implements ChangeListener  {

        @Override
        public void stateChanged(final ChangeEvent e) {
            fireChange();
        }

        @Override
        public String getDisplayName() {
            return NbBundle.getMessage(AdsReportChartCellSetupStep1.class, "AdsReportChartCellSetupStep1Title");
        }
        
        @Override
        public void apply(final Object settings) {
            final Settings s = (Settings) settings;
            if(s.getCell().getChartType()!=getVisualPanel().getChartType()){
                s.getCell().setChartType(getVisualPanel().getChartType());//gisCategory=getVisualPanel().isCategory();
                s.getCell().getChartSeries().clear();
            }
        }

        @Override
        protected AdsReportChartCellSetupStep1Panel createVisualPanel() {
            final AdsReportChartCellSetupStep1Panel panel = new AdsReportChartCellSetupStep1Panel();
            panel.addChangeListener(this);
            return panel;
        }

        @Override
        public void open(final Object settings) {
            final Settings s = (Settings) settings;
            getVisualPanel().open(s.getCell());
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public boolean isFinishiable() {
            return true;
        }
        
        @Override
        public boolean hasNextStep() {
            return true;
        }
        
        @Override
        @SuppressWarnings("rawtypes")
        public Step createNextStep() {
            //if(getVisualPanel().getChartType()==EReportChartCellType.PIE){
           //     return new AdsReportPieChartCellSetupStep2();
            //}else{
                return new AdsReportChartCellSetupStep2();
            //}
        }

    }

