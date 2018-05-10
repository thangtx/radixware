/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QApplication;
import java.util.LinkedList;
import java.util.Queue;
import org.radixware.kernel.explorer.widgets.PropertiesGrid;


public final class PropertiesGridDisassembler extends QObject{

    private static PropertiesGridDisassembler INSTANCE = new PropertiesGridDisassembler();
    
    public static PropertiesGridDisassembler getInstance(){
        return INSTANCE;
    }
    
    private static class DisassembleEvent extends QEvent{        
        public DisassembleEvent(){
            super(QEvent.Type.User);
        }
    }
    
    private final Queue<PropertiesGrid> queue = new LinkedList<>();
    private boolean disassembleScheduled;
    private boolean blocked;
    
    public void disassemble(final PropertiesGrid grid){
        if (grid!=null){
            grid.setVisible(false);
            grid.setParent(null);
            grid.prepareForClose();
            queue.add(grid);
            scheduleDisassemble();
        }
    }
    
    private void scheduleDisassemble(){
        if (!disassembleScheduled && !blocked && !queue.isEmpty()){
            disassembleScheduled = true;
            QApplication.postEvent(this, new DisassembleEvent());
        }
    }
    
    private void processDisassembleEvent(){
        if (disassembleScheduled && !blocked && !queue.isEmpty()){
            disassembleScheduled = false;
            final PropertiesGrid grid = queue.poll();
            grid.close();
            grid.dispose();
            scheduleDisassemble();
        }
    }
    
    public void block(){
        blocked = true;
    }
    
    public void unblock(){
        blocked = false;
        scheduleDisassemble();
    }
    
    public void clear(){
        queue.clear();
        disassembleScheduled = false;        
    }

    @Override
    protected void customEvent(final QEvent event) {
        if (event instanceof DisassembleEvent){
            event.accept();
            processDisassembleEvent();            
        }else{
            super.customEvent(event);
        }
    }
    
}
