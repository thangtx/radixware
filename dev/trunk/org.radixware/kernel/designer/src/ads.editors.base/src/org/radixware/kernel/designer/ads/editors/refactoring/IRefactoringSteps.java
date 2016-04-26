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

package org.radixware.kernel.designer.ads.editors.refactoring;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;


public interface IRefactoringSteps extends ISettingProvider {
    
    public class Settings implements ISettings {
        private Map<Class<?>, OperationStatus> status = new HashMap<>();

        public OperationStatus getStatus() {
            final OperationStatus sumStatus = new OperationStatus();
            
            for (final OperationStatus stepStatus : status.values()) {
                sumStatus.merge(stepStatus);
            }
            return sumStatus;
        }
        
        public OperationStatus getStatus(Class<?> cls) {
            return status.get(cls);
        }
        
        public void setStatus(Class<?> cls, OperationStatus status) {
            this.status.put(cls, status);
        }
    }
    
    public interface ISettings {

    }

    ExecutorService getExecutor();
    
    ISettings createSettings();
}
