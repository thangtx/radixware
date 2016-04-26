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

package org.radixware.kernel.explorer.env.session;

import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.eas.AbstractSslTrustManager;
import org.radixware.kernel.common.client.exceptions.SslTrustManagerException;
import org.radixware.kernel.explorer.dialogs.certificates.CertificateConfirmationDialog;
import org.radixware.kernel.explorer.utils.QtJambiExecutor;


public final class SslTrustManager extends AbstractSslTrustManager implements Callable<AbstractSslTrustManager.Confirmation> {
    private final IClientEnvironment environment;
    private final QtJambiExecutor executor;
    private X509Certificate currentCertificate;
    
    private AbstractSslTrustManager.ConfirmationReason reason = AbstractSslTrustManager.ConfirmationReason.UNKNOWN;
    
    public SslTrustManager(final IClientEnvironment environment, final String trustStorePath, final X509Certificate clientCertificate) {
        super(environment, trustStorePath,clientCertificate);
        this.environment = environment;
        this.executor = new QtJambiExecutor();        
    }
    
    @Override
    protected Confirmation confirm(final X509Certificate cert, AbstractSslTrustManager.ConfirmationReason reason) throws SslTrustManagerException {
        try {
            currentCertificate = cert;
            this.reason = reason;
            return executor.invoke(this);
        } catch (InterruptedException ex) {
            throw new SslTrustManagerException(null, ex);
        } catch (ExecutionException ex) {
            throw new SslTrustManagerException(null, ex);
        }
    }

    @Override
    public Confirmation call() throws ExecutionException {
        environment.getProgressHandleManager().blockProgress();
        final CertificateConfirmationDialog dlg = new CertificateConfirmationDialog(environment, null, currentCertificate, reason == AbstractSslTrustManager.ConfirmationReason.UNKNOWN);
        dlg.exec();
        environment.getProgressHandleManager().unblockProgress();
        return dlg.getConfirmation();
    }

    
    
}
