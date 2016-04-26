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

package org.radixware.kernel.designer.common.general.upload;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PostUploadAction {
    
    public static final PostUploadAction INSTANCE = new PostUploadAction();
    private final List<Runnable> actionQueue = new LinkedList<>();
    
    public static PostUploadAction getInstance() {
        return INSTANCE;
    }
    
    public void put(Runnable action) {        
        synchronized (actionQueue) {
            actionQueue.add(action);
        }
    }
    
    public void process() {
        final List<Runnable> actionsToPerform;
        synchronized (actionQueue) {
            actionsToPerform = new ArrayList<>(actionQueue);
            actionQueue.clear();
        }
        for (Runnable r : actionsToPerform) {
            try {
                r.run();
            } catch (Throwable e) {
                Logger.getLogger(PostUploadAction.class.getName()).log(Level.FINE, e.getMessage(), e);
            }
        }
    }
}
