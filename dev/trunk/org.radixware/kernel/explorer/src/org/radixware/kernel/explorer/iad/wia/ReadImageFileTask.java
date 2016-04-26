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

package org.radixware.kernel.explorer.iad.wia;

import com.trolltech.qt.gui.QImage;


final class ReadImageFileTask implements Runnable{

    private QImage image;
    private final String filePath;
    private final Object semaphore = new Object();
    
    public ReadImageFileTask(final String filePath){
        this.filePath = filePath;
    }
        
    @Override
    public void run() {
        synchronized(semaphore){
            image = new QImage(filePath);
        }
    }
    
    public QImage getQImage(){
        synchronized(semaphore){
            return image;
        }
    }

}
