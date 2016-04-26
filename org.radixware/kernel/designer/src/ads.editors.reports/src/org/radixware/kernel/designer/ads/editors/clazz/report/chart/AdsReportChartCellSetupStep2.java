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


public class AdsReportChartCellSetupStep2 extends WizardSteps.Step<AdsReportChartCellEditor> implements ChangeListener {

        @Override
        public void stateChanged(final ChangeEvent e) {
            fireChange();
        }


        @Override
        public String getDisplayName() {
            return NbBundle.getMessage(AdsReportChartCellSetupStep2.class, "AdsReportChartCellSetupStep2Title");
        }

        @Override
        protected AdsReportChartCellEditor createVisualPanel() {
            final AdsReportChartCellEditor panel = new AdsReportChartCellEditor(null);
            panel.addChangeListener(this);
            return panel;
        }

        @Override
        public void open(final Object settings) {
            //super.open(creature);
            final Settings s = (Settings) settings;    
            getVisualPanel().open(s.getCell(),s.getOwnerReport(),s.getMlStringList());
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
            return new AdsReportChartCellSetupStep3();
        }

    }
