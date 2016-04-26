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
package org.radixware.kernel.common.svn.client.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.SvnRAConnection;
import org.radixware.kernel.common.svn.client.SvnRepository;

/**
 *
 * @author akrylov
 */
public abstract class SvnAuthenticator {

    protected static final String SUCCESS = "success";
    protected static final String FAILURE = "failure";
    protected static final String STEP = "step";
    protected final SvnRAConnection connection;
    private InputStream in;
    private OutputStream out;
    private InputStream plainIn;
    private OutputStream plainOut;

    public SvnAuthenticator(SvnRAConnection connection) throws RadixSvnException {
        this.connection = connection;
        try {
            this.in = this.plainIn = connection.getInputStream();
            this.out = this.plainOut = connection.getOutputStream();
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }

    protected SvnAuthentication getNextAuthentication(String realm, URI location) throws RadixSvnException {
        return null;
    }

    public abstract SvnAuthentication authenticate(List<String> mechs, String realm, SvnRepository repository) throws RadixSvnException;

    public InputStream getInputStream() {
        return in;
    }

    public OutputStream getOutputStream() {
        return out;
    }

    protected void setInputStream(InputStream in) {
        this.in = in;
    }

    protected void setOutputStream(OutputStream out) {
        this.out = out;
    }

    protected InputStream getPlainInputStream() {
        return plainIn;
    }

    protected OutputStream getPlainOutputStream() {
        return plainOut;
    }
}
