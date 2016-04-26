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

package org.radixware.kernel.common.client.models;

import java.util.List;


public interface IReaderController {
    
    public enum ScrollPosition {
        TOP,
        BOTTOM,
        CURRENT
    }
 
    /**
     * возвращает false если нужно остановиться (дальше не читать)
     * @param newModel
     */
    boolean hasMore(EntityModel newModel);
   
    /**
     * делает слияние старых и новых сущностей, возвращает новый список
     * @param currentModels
     * @param newModels
     * @param currentModel
     * @return 
     */
    List<EntityModel> merge(List<EntityModel> currentModels, List<EntityModel> newModels, EntityModel currentModel);
    /**
     * возвращает новую позицию скроллинга
     * @return 
     */
    ScrollPosition scrollPosition();
    
    
}