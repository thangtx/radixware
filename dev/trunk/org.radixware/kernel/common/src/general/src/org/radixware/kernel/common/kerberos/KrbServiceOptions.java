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

package org.radixware.kernel.common.kerberos;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Objects;
import javax.security.auth.kerberos.KerberosPrincipal;
import org.radixware.kernel.common.utils.SystemTools;


public abstract class KrbServiceOptions {
    
    private final String keyTabPath;
    private final String principalName;
    private final boolean spnego;
    /**
     *
     * @param keyTabPath
     * @param principalName
     */
    public KrbServiceOptions(final String keyTabPath, final String principalName, final boolean isSpnego) {
        this.keyTabPath = keyTabPath;
        this.principalName = principalName;
        spnego = isSpnego;
    }

    public String getKeyTabPath() {
        return keyTabPath;
    }
    
    public boolean isSpnego(){
        return spnego;
    }

    /**
     * @throws InvalidPathException if keyTabPath is incorrect
     */
    public String getAbsoluteKeyTabPath() throws InvalidPathException {
        if (keyTabPath == null) {
            return keyTabPath;
        }
        if (Paths.get(keyTabPath).isAbsolute()) {
            return keyTabPath;
        } else {
            return SystemTools.getApplicationDataPath(keyTabPath).getAbsolutePath();
        }
    }

    public String getPrincipalName() {
        return principalName;
    }
    
    protected abstract String getDefaultPrincipalName();

    /**
     * @throws IllegalArgumentException if principalName is incorrect
     */
    public KerberosPrincipal getKerberosPrincipal() throws IllegalArgumentException {
        if (principalName == null || principalName.isEmpty()) {
            return new KerberosPrincipal(getDefaultPrincipalName(), KerberosPrincipal.KRB_NT_SRV_INST);
        } else {
            return new KerberosPrincipal(principalName, KerberosPrincipal.KRB_NT_SRV_INST);
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof KrbServiceOptions)) {
            return false;
        } else {
            if (!Objects.equals(principalName, ((KrbServiceOptions) other).principalName)) {
                return false;
            }
            if (!Objects.equals(keyTabPath, ((KrbServiceOptions) other).keyTabPath)) {
                return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(keyTabPath);
        hash = 97 * hash + Objects.hashCode(principalName);
        return hash;
    }
    
    @Override
    public String toString() {
        final StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("\t\tservice principal name: ");
        strBuffer.append(principalName==null || principalName.isEmpty() ? "default" : principalName);
        strBuffer.append("\n\t\tkeytab file path: ");
        strBuffer.append(keyTabPath==null || keyTabPath.isEmpty() ? "default" : keyTabPath);
        return strBuffer.toString();
    }
    
}
