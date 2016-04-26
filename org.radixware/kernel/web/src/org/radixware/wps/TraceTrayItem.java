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

package org.radixware.wps;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.trace.ClientTraceItem;
import org.radixware.kernel.common.client.trace.TraceBuffer;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.wps.rwt.ToolButton;
import org.radixware.wps.rwt.UIObject;


class TraceTrayItem extends ToolButton implements ITrayItem{
    
    private class TraceBufferListener implements TraceBuffer.TraceBufferListener<ClientTraceItem>{

        @Override
        public void newItemInBuffer(final ClientTraceItem traceItem) {
            setVisible(true);
        }

        @Override
        public void maxSeverityChanged(final EEventSeverity eventSeverity) {
            if (eventSeverity==EEventSeverity.NONE){
                setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.DEBUG));
            }else{
                setIcon(ClientIcon.TraceLevel.findEventSeverityIcon(eventSeverity, environment));
            }            
        }

        @Override
        public void cleared() {            
            setIcon(environment.getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.DEBUG));
        }        
    }
    
    private final TraceBufferListener bufferListener = new TraceBufferListener();
    private final WpsEnvironment environment;
    
    public TraceTrayItem(final WpsEnvironment environment){
        super();
        this.environment = environment;           
        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final IButton source) {
                environment.showTraceAction.trigger();
            }
        });
        setIconSize(15, 15);
        setTop(2);
        getHtml().setCss("position", "relative");
        setSizePolicy(SizePolicy.MINIMUM_EXPAND, UIObject.SizePolicy.MINIMUM_EXPAND);
        final TraceBuffer traceBuffer = environment.getTracer().getBuffer();
        traceBuffer.addTraceBufferListener(bufferListener);        
        if (traceBuffer.isEmpty()){            
            setVisible(false);            
        }
        else{
            setIcon(ClientIcon.TraceLevel.findEventSeverityIcon(traceBuffer.getMaxSeverity(), environment));
        }
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return environment;
    }
            
    @Override
    public void onTrayClose(){
        environment.getTracer().getBuffer().removeTraceBufferListener(bufferListener);
    }
}
