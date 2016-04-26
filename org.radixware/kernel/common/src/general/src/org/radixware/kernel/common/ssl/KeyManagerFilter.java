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

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;

/**
 * Custom key manager с фильтрацией списка ключей
 * Используется сервером
 *
 */

class KeyManagerFilter extends X509ExtendedKeyManager{

    private final X509KeyManager backKeyManager;
    private final List<String> myKeyAliases;

    public KeyManagerFilter(final KeyManager km, final List<String> aliases){
        backKeyManager = (X509KeyManager)km;
        myKeyAliases = aliases;
    }

    @Override
    public String[] getServerAliases(final String keyType, final Principal[] issuers){
        final String[] backKeyManagerAliases = backKeyManager.getServerAliases(keyType, issuers);
        if (myKeyAliases==null || backKeyManagerAliases==null)
            return backKeyManagerAliases;
        //если myKeyAliases!=null, возвращаем общие alias для backKeyManagerAliases и myKeyAliases
        //порядок остается как в myKeyAliases
        final List<String> backKeyManagerAliasList = Arrays.asList(backKeyManagerAliases);
        final ArrayList<String> resList = new ArrayList<String>();
        for (String alias : myKeyAliases){
            if (backKeyManagerAliasList.contains(alias))
                resList.add(alias);
        }
        return resList.toArray(new String[0]);
    }

    @Override
    public String[] getClientAliases(final String keyType, final Principal[] issuers){
        final String[] backKeyManagerAliases = backKeyManager.getClientAliases(keyType, issuers);
        if (myKeyAliases==null || backKeyManagerAliases==null)
            return backKeyManagerAliases;
        //если myKeyAliases!=null, возвращаем общие alias для backKeyManagerAliases и myKeyAliases
        //порядок остается как в myKeyAliases
        final List<String> backKeyManagerAliasList = Arrays.asList(backKeyManagerAliases);
        final ArrayList<String> resList = new ArrayList<String>();
        for (String alias : myKeyAliases){
            if (backKeyManagerAliasList.contains(alias))
                resList.add(alias);
        }
        return resList.toArray(new String[0]);
    }

    @Override
    public String chooseClientAlias(final String[] keyTypes, final Principal[] issuers, final Socket socket){
        for (String keyType : keyTypes){
            String[] aliases = getClientAliases(keyType, issuers);
            if (aliases!=null && aliases.length>0)
                return aliases[0];
        }

        return backKeyManager.chooseClientAlias(keyTypes, issuers, socket);
    }

    @Override
    public String chooseServerAlias(final String keyType, final Principal[] issuers, final Socket socket){
        String[] aliases = getServerAliases(keyType, issuers);
        if (aliases!=null && aliases.length>0)
            return aliases[0];
        
        return backKeyManager.chooseServerAlias(keyType, issuers, socket);
    }

    @Override
    public X509Certificate[] getCertificateChain(final String alias){
        return backKeyManager.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(final String alias){
        return backKeyManager.getPrivateKey(alias);
    }

    @Override
    public String chooseEngineServerAlias(final String keyType, final Principal[] issuers, final SSLEngine engine){
        return chooseServerAlias(keyType, issuers, null);
    }

    @Override
    public String chooseEngineClientAlias(final String[] keyType, final Principal[] issuers, final SSLEngine engine){
        return chooseClientAlias(keyType, issuers, null);
    }
}