/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.types;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.meta.clazzes.RadPropDef;

/**
 *
 * @author dsafonov
 */
public class PropInheritanceCycleException extends RadixError {

    private String message;
    private final List<Entity> entities = new ArrayList<>();
    private final List<RadPropDef> props = new ArrayList<>();
    boolean cycleRecorded = false;

    public PropInheritanceCycleException(final String baseMessage, final Entity cycledEntity, final RadPropDef cycledPropDef, final String descr) {
        super(baseMessage + ": " + getPathElementAsStr(cycledEntity, cycledPropDef, descr));
        message = super.getMessage();
        entities.add(cycledEntity);
        props.add(cycledPropDef);
    }

    @Override
    public String getMessage() {
        return message;
    }
    
    public void appendStep(final String descr, final Entity entity, final RadPropDef propDef) {
        if (cycleRecorded) {
            return;
        }
        message = message + " <- " + getPathElementAsStr(entity, propDef, descr);
        entities.add(entity);
        props.add(propDef);
        if (entity == entities.get(0) && propDef == props.get(0)) {
            cycleRecorded = true;
        }
    }

    private static String getPathElementAsStr(final Entity cycledEntity, final RadPropDef cycledPropDef, final String descr) {
        return cycledEntity.getRadMeta().getName() + "[" + cycledEntity.getPid() + "]." + cycledPropDef.getName() + "." + descr;
    }

}
