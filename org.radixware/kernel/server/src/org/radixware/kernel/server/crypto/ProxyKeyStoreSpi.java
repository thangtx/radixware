/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import org.radixware.kernel.common.exceptions.KeystoreControllerException;
import org.radixware.kernel.common.ssl.KeystoreController;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.utils.DebugLog;

public class ProxyKeyStoreSpi extends KeyStoreSpi {

    private final KeystoreController ksc = KeystoreController.getServerKeystoreController();
    private final KeyStore ks = ksc.getKeystore();
    public final boolean ksInited = ks.size() >= 0;
    private ArrStr aliases = null;
    
    public ProxyKeyStoreSpi() throws KeystoreControllerException, KeyStoreException {
    }
    
    @Override
    public Key engineGetKey(String alias, char[] password) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        try {
            return ks.getKey(alias, password);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineGetKey: KeyStoreException ex:\n" + ex);
        }
        return null;
    }

    @Override
    public Certificate[] engineGetCertificateChain(String alias) {
        try {
            return ks.getCertificateChain(alias);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineGetCertificateChain: KeyStoreException ex:\n" + ex);
        }
        return null;
    }

    @Override
    public Certificate engineGetCertificate(String alias) {
        try {
            return ks.getCertificate(alias);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineGetCertificate: KeyStoreException ex:\n" + ex);
        }
        return null;
    }

    @Override
    public Date engineGetCreationDate(String alias) {
        try {
            return ks.getCreationDate(alias);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineGetCreationDate: KeyStoreException ex:\n" + ex);
        }
        return null;
    }

    @Override
    public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain) throws KeyStoreException {
        ks.setKeyEntry(alias, key, password, chain);
    }

    @Override
    public void engineSetKeyEntry(String alias, byte[] key, Certificate[] chain) throws KeyStoreException {
        ks.setKeyEntry(alias, key, chain);
    }

    @Override
    public void engineSetCertificateEntry(String alias, Certificate cert) throws KeyStoreException {
        ks.setCertificateEntry(alias, cert);
    }

    @Override
    public void engineDeleteEntry(String alias) throws KeyStoreException {
        ks.deleteEntry(alias);
    }

    @Override
    public Enumeration<String> engineAliases() {
        final ArrStr _aliases = aliases != null ? aliases : ProxyCryptoProvider.NULL_ALIASES;
        return Collections.enumeration(_aliases);
    }

    @Override
    public boolean engineContainsAlias(String alias) {
        try {
            return aliases == null ? ks.containsAlias(alias) : aliases.contains(alias);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineContainsAlias: KeyStoreException ex:\n" + ex);
        }
        return false;
    }

    @Override
    public int engineSize() {
        try {
            return ks.size();
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineSize: KeyStoreException ex:\n" + ex);
        }
        return -1;
    }

    @Override
    public boolean engineIsKeyEntry(String alias) {
        try {
            return ks.isKeyEntry(alias);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineIsKeyEntry: KeyStoreException ex:\n" + ex);
        }
        return false;
    }

    @Override
    public boolean engineIsCertificateEntry(String alias) {
        try {
            return ks.isCertificateEntry(alias);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineIsCertificateEntry: KeyStoreException ex:\n" + ex);
        }
        return false;
    }

    @Override
    public String engineGetCertificateAlias(Certificate cert) {
        try {
            final String alias = ks.getCertificateAlias(cert);
            return aliases == null || aliases.contains(alias) ? alias : null;
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineGetCertificateAlias: KeyStoreException ex:\n" + ex);
        }
        return null;
    }

    @Override
    public void engineStore(OutputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        try {
            ks.store(stream, password);
        } catch (KeyStoreException ex) {
            DebugLog.logUpDown("ProxyKeyStoreSpi.engineStore: KeyStoreException ex:\n" + ex);
        }
    }

    @Override
    public void engineLoad(InputStream stream, char[] password) throws IOException, NoSuchAlgorithmException, CertificateException {
        final String pwd = new String(password);
        aliases = pwd.equals("null") ? null : ArrStr.fromValAsStr(pwd);
    }
    
}
