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

package org.radixware.kernel.explorer.dialogs.pkcs11;

import org.radixware.kernel.common.client.eas.connections.Pkcs11Config;


public class Pkcs11ConfigurationException extends Exception {
    private final static long serialVersionUID = 3500718888653102961L;
    private final Pkcs11Config.Field field;
    
    public Pkcs11ConfigurationException(final String message) {
        super(message);
        field = null;
    }
    
    public Pkcs11ConfigurationException(final String message, final Throwable reason) {
        super(message, reason);
        field = null;
    }
    
    public Pkcs11ConfigurationException(final String message, final Pkcs11Config.Field field) {
        super(message);
        this.field = field;
    }
    
    public Pkcs11ConfigurationException(final String message, final Throwable reason, final Pkcs11Config.Field field) {
        super(message, reason);
        this.field = field;
    }
    
    public Pkcs11Config.Field getField() {
        return field;
    }
}
