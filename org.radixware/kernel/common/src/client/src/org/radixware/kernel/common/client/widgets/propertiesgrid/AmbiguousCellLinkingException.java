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

package org.radixware.kernel.common.client.widgets.propertiesgrid;

import org.radixware.kernel.common.client.localization.MessageProvider;


final class AmbiguousCellLinkingException extends Exception{

    private static final long serialVersionUID = 7019838446163572191L;
    private final IPropertiesGridCell linkingCell, variant1, variant2;
    
    public AmbiguousCellLinkingException(final IPropertiesGridCell linkingCell, final IPropertiesGridCell variant1, final IPropertiesGridCell variant2){
        this.linkingCell = linkingCell;
        this.variant1 = variant1;
        this.variant2 = variant2;        
    }
    
    public String getMessage(final MessageProvider mp){
        final String message = mp.translate("Editor", "Can't stick %1$s to %2$s because it already sticked to %3$s");
        return String.format(message, variant1.getDescription(mp), linkingCell.getDescription(mp), variant2.getDescription(mp));
    }
    
}
