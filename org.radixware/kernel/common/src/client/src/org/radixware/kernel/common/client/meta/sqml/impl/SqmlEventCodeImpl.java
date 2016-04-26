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

package org.radixware.kernel.common.client.meta.sqml.impl;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEventCodeDef;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.types.Id;


public class SqmlEventCodeImpl extends SqmlDefinitionImpl implements ISqmlEventCodeDef {
    private static final int LEN_LIMIT = 30;
    private final Id mlStringId;
    private final EEventSeverity severity;
    private final String source;
    private final String eventCode;
    private final RadMlStringBundleDef bundle;
    
    public SqmlEventCodeImpl(final IClientEnvironment env, final RadMlStringBundleDef bundle, final Id stringId) {
        super(env, bundle);
        this.mlStringId = stringId;
        severity = bundle.getEventSeverity(stringId);
        source = bundle.getEventSource(stringId);
        eventCode = bundle.get(stringId);
        this.bundle = bundle;
    }
    
    @Override
    public String getEventCode() {
        return eventCode;
    }
    
    @Override
    public ClientIcon getIcon() {
        return ClientIcon.TraceLevel.getIconBySeverity(severity);
    }
    
    @Override
    public Id getId() {
        return mlStringId;
    }

    @Override
    public String getShortName() {
        return getShortText();
    }
    
    @Override
    public String getFullName() {
        return getShortText();
    }

    @Override
    public String getTitle() {
        return getShortText();
    }
    
    @Override
    public EEventSeverity getEventSeverity() {
        return severity;
    }

    @Override
    public String getEventSource() {
        return source;
    }

    @Override
    public String getText() {
        return eventCode;
    }
    
    private String getShortText() {
        int newLine = eventCode.indexOf(System.lineSeparator());
        if(newLine < 0 || newLine >= LEN_LIMIT) {
            return eventCode.substring(0, Math.min(eventCode.length()-1, LEN_LIMIT)) + "...";
        } else {
            return eventCode.substring(0, newLine) + "...";
        }
    }

    @Override
    public Id getOwner() {
        return bundle.getId();
    }
}