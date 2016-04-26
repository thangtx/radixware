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

package org.radixware.kernel.common.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;

/**
 * Custom key manager с фильтрацией списка доверенных сертификатов
 * Используется сервером
 *
 */

class TrustManagerFilter implements X509TrustManager {
    private X509Certificate[] certs = null;
    final private X509TrustManager backTrustManager;

    public TrustManagerFilter(final TrustManager trustManager, final List<String> aliases, final KeystoreController keystoreController){
        backTrustManager = (X509TrustManager)trustManager;
        //если aliases!=null, загружаем все сертификаты по aliases в certs
        if (aliases!=null){
            final ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
            X509Certificate certificate;
            for (String alias : aliases){
                alias = checkEracomCertAlias(alias);
                try{
                    certificate = keystoreController.getCertificate(alias);
                } catch (KeystoreControllerException e){
                    certificate = null;
                }
                if (certificate!=null)
                    certList.add(certificate);
            }
            certs = certList.toArray(new X509Certificate[0]);
        }
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException{
        backTrustManager.checkClientTrusted(chain, authType);
        //если certs!=null, проверяем, что в chain есть один из certs
        if (certs!=null){
            final List<X509Certificate> certList = Arrays.asList(chain);
            for (X509Certificate certificate : certs)
                if (certList.contains(certificate))
                    return;
            throw new CertificateException("Client's certificate chain is not trusted by TrustManagerFilter");
        }
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException{
        backTrustManager.checkServerTrusted(chain, authType);
        //если certs!=null, проверяем, что в chain есть один из certs
        if (certs!=null){
            final List<X509Certificate> certList = Arrays.asList(chain);
            for (X509Certificate certificate : certs)
                if (certList.contains(certificate))
                    return;
            throw new CertificateException("Server's certificate chain is not trusted by TrustManagerFilter");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers(){
        if (certs!=null)
            return certs;
        return backTrustManager.getAcceptedIssuers();
    }

    private String checkEracomCertAlias(String alias){
        /** Eracom returns trusted certificates' aliases with trailing 00h byte,
          * which is changed to 3Fh in database CLOB. In this case we must change
          * it back to 00h before requesting a certificate.
          */
        if (alias!=null){
            int length = alias.length();
            if (length>0 && alias.charAt(length-1)==0x3f)
                return alias.substring(0, length-1)+(char)0x00;
        }
        return alias;
    }
}