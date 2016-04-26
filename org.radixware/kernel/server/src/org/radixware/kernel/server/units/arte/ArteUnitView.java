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

package org.radixware.kernel.server.units.arte;

import javax.swing.JTabbedPane;

import org.radixware.kernel.server.units.UnitState;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.widgets.StatisticPanel;
import org.radixware.kernel.server.widgets.TraceView;

/**
 * ���� ������ ARTE. 
 * 
 */
final class ArteUnitView extends UnitView {

    private ArteUnit getArteUnit() {
        return (ArteUnit) getUnit();
    }

    private final class StatisticProvider implements StatisticPanel.Provider {

        @Override
        public String getValuesAxeCaption() {
            return ArteUnitMessages.LBL_USAGE;
        }

        @Override
        public int getValue() {
            try {
                return getArteUnit().getActiveArteCount();
            } catch (Throwable e) {
                return 0;
            }
        }

        @Override
        public String getTitle() {
            return getArteUnit().getFullTitle();
        }

        @Override
        public boolean isStarted() {
            return getArteUnit().getState() == UnitState.STARTED;
        }
    }

    public ArteUnitView(final ArteUnit unit) {
        super(unit);
    }

    @Override
    protected void init() {
        super.init();
        final JTabbedPane tabs = new JTabbedPane();
        tabs.addTab(ArteUnitMessages.TAB_TRACE, traceList);
        tabs.addTab(ArteUnitMessages.TAB_STATISTIC, new StatisticPanel(new StatisticProvider()));
        getDialog().getContentPane().add(tabs);
    }

    @Override
    protected void initTraceList() { // �����������, ����� ������ �� ���������� � contentPane
        traceList = new TraceView(getUnit().getTrace());
    }
    private static final long serialVersionUID = -4689390336837155412L;
}
