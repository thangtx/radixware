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

package org.radixware.kernel.server.units.netport;

import javax.swing.JTabbedPane;

import org.radixware.kernel.server.units.UnitListener;
import org.radixware.kernel.server.units.Unit;
import org.radixware.kernel.server.units.UnitState;
import org.radixware.kernel.server.units.UnitView;
import org.radixware.kernel.server.widgets.TraceView;

/**
 * ���� ������ NetPortHandler. 
 * 
 */
final class NetPortHandlerUnitView extends UnitView {
    private static final long serialVersionUID = 4520872535011008254L;
	
    private JTabbedPane tabs;
    private ChannelsPanel netChannelsPanel;

    private NetPortHandlerUnit getNetPortHandlerUnit(){
        return (NetPortHandlerUnit)getUnit();
    }

    public NetPortHandlerUnitView(NetPortHandlerUnit unit) {
        super(unit);
    }
    
    @Override
    protected void init() {
        super.init();
        tabs = new JTabbedPane();
        tabs.addTab(NetPortHandlerMessages.TAB_TRACE, traceList);
        netChannelsPanel = new ChannelsPanel(getNetPortHandlerUnit());
        tabs.addTab(NetPortHandlerMessages.TAB_NET_CHANNELS, netChannelsPanel);
        getDialog().getContentPane().add(tabs);
        getNetPortHandlerUnit().registerListener(new UnitListener(){
            @Override
            public void stateChanged(Unit unit, UnitState oldState, UnitState newState) {
                if (newState == UnitState.STOPPED) {
                    netChannelsPanel.stop();
                } else if (newState == UnitState.STARTED) {
                    if (netChannelsPanel.isVisible())
                        netChannelsPanel.start();
                }
            }
        });
    }
    
    @Override
    protected void initTraceList() { // �����������, ����� ������ �� ���������� � contentPane
        traceList = new TraceView(getUnit().getTrace());
    }
}