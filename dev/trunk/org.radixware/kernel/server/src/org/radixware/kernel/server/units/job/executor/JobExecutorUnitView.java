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

package org.radixware.kernel.server.units.job.executor;

import javax.swing.JTabbedPane;

import org.radixware.kernel.server.units.UnitListener;
import org.radixware.kernel.server.units.UnitMenu;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitState;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.widgets.TraceView;
import org.radixware.kernel.server.widgets.StatisticPanel;

final class JobExecutorUnitView extends UnitView {
	
	private static final long serialVersionUID = -3994939025122944473L;
	private StatisticPanel statisticPanel;
	
	final private JobExecutorUnit getJobExecutorUnit(){
		return (JobExecutorUnit)getUnit();
	}
	
	private final class StatisticProvider implements StatisticPanel.Provider {
		
		@Override
		public String getValuesAxeCaption() {
			return JobExecutorMessages.LBL_TASKS_QUANTITY;
		}
		
		@Override
		public int getValue() {
			return (int)getJobExecutorUnit().getExecutingJobsCount();
		}
		
		@Override
		public String getTitle() {
			return getJobExecutorUnit().getFullTitle();
		}
		
		@Override
		public boolean isStarted() {
			return getJobExecutorUnit().getState() == UnitState.STARTED;
		}
		
	}
	
	public JobExecutorUnitView(final JobExecutorUnit unit) {
		super(unit);
	}
	@Override
	protected void init() {
		super.init();
		final JTabbedPane tabs = new JTabbedPane();
		tabs.addTab(JobExecutorMessages.TAB_TRACE, traceList);
		statisticPanel = new StatisticPanel(new StatisticProvider());
		tabs.addTab(JobExecutorMessages.TAB_STATISTIC, statisticPanel);
		getDialog().getContentPane().add(tabs);
		getJobExecutorUnit().registerListener(new UnitListener(){
			@Override
			public void stateChanged(final Unit unit, final UnitState oldState, final UnitState newState) {
				if (newState == UnitState.STOPPING){
					statisticPanel.stop();
					statisticPanel.setStateOn(false);
				}else if (newState == UnitState.STARTED){
					statisticPanel.setStateOn(true);
				}
			}
		});
	}
	@Override
	protected void initTraceList() { // �����������, ����� ������ �� ���������� � contentPane
		traceList = new TraceView(getUnit().getTrace());
	}
	
	@Override
	protected UnitMenu initMenu() {
		return new UnitMenu(this);
	}

}
