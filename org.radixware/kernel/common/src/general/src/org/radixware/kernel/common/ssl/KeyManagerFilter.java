/*
 * Copyright (c) 2008-2017, Compass Plus Limited. All rights reserved.
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
import org.apache.commons.lang.StringUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.DebugLog;

/**
 * Custom key manager с фильтрацией списка ключей
 * Используется сервером
 *
 */

public class KeyManagerFilter extends X509ExtendedKeyManager{

    private final X509KeyManager backKeyManager;
    private final List<String> myKeyAliases;
    
    public KeyManagerFilter(final KeyManager km, final List<String> aliases){
        DebugLog.logUpDown("KeyManagerFilter(): aliases = " + StringUtils.join(aliases, ";"));
        LocalTracer.debugNonSensitive("KeyManagerFilter.<init>: aliases = " + StringUtils.join(aliases, ";"));
        backKeyManager = (X509KeyManager)km;
        myKeyAliases = aliases;
    }

    @Override
    public String[] getServerAliases(final String keyType, final Principal[] issuers){
        DebugLog.funcIn("KeyManagerFilter.getServerAliases: keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n"));
        final String[] backKeyManagerAliases = backKeyManager.getServerAliases(keyType, issuers);
        if (myKeyAliases==null || backKeyManagerAliases==null) {
            DebugLog.funcOut("return backKeyManagerAliases = " + StringUtils.join(backKeyManagerAliases, ";"));
            LocalTracer.debugNonSensitive("KeyManagerFilter.getServerAliases(): keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n") + ", backKeyManagerAliases = " + StringUtils.join(backKeyManagerAliases, ";"));
            return backKeyManagerAliases;
        }
        //если myKeyAliases!=null, возвращаем общие alias для backKeyManagerAliases и myKeyAliases
        //порядок остается как в myKeyAliases
        final List<String> backKeyManagerAliasList = Arrays.asList(backKeyManagerAliases);
        final ArrayList<String> resList = new ArrayList<String>();
        DebugLog.log("myKeyAliases = " + StringUtils.join(myKeyAliases, ";") + ", backKeyManagerAliasList = " + StringUtils.join(backKeyManagerAliasList, ";"));
        for (String alias : myKeyAliases){
            if (backKeyManagerAliasList.contains(alias))
                resList.add(alias);
        }
        DebugLog.funcOut("return resList = " + StringUtils.join(resList, ";"));
        LocalTracer.debugNonSensitive("KeyManagerFilter.getServerAliases(): keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n")
                + "myKeyAliases = " + StringUtils.join(myKeyAliases, ";") + ", backKeyManagerAliasList = " + StringUtils.join(backKeyManagerAliasList, ";")
                + ", resList = " + StringUtils.join(resList, ";"));
        return resList.toArray(new String[0]);
    }

    @Override
    public String[] getClientAliases(final String keyType, final Principal[] issuers){
        DebugLog.funcIn("KeyManagerFilter.getClientAliases: keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n"));
        final String[] backKeyManagerAliases = backKeyManager.getClientAliases(keyType, issuers);
        if (myKeyAliases==null || backKeyManagerAliases==null) {
            DebugLog.funcOut("return backKeyManagerAliases = " + StringUtils.join(backKeyManagerAliases, ";"));
            LocalTracer.debugNonSensitive("KeyManagerFilter.getClientAliases(): keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n") + ", backKeyManagerAliases = " + StringUtils.join(backKeyManagerAliases, ";"));
            return backKeyManagerAliases;
        }
        //если myKeyAliases!=null, возвращаем общие alias для backKeyManagerAliases и myKeyAliases
        //порядок остается как в myKeyAliases
        final List<String> backKeyManagerAliasList = Arrays.asList(backKeyManagerAliases);
        final ArrayList<String> resList = new ArrayList<String>();
        DebugLog.log("myKeyAliases = " + StringUtils.join(myKeyAliases, ";") + ", backKeyManagerAliasList = " + StringUtils.join(backKeyManagerAliasList, ";"));
        for (String alias : myKeyAliases){
            if (backKeyManagerAliasList.contains(alias))
                resList.add(alias);
        }
        DebugLog.funcOut("return resList = " + StringUtils.join(resList, ";"));
        LocalTracer.debugNonSensitive("KeyManagerFilter.getClientAliases(): keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n")
                + "myKeyAliases = " + StringUtils.join(myKeyAliases, ";") + ", backKeyManagerAliasList = " + StringUtils.join(backKeyManagerAliasList, ";")
                + ", resList = " + StringUtils.join(resList, ";"));
        return resList.toArray(new String[0]);
    }

    @Override
    public String chooseClientAlias(final String[] keyTypes, final Principal[] issuers, final Socket socket){
        DebugLog.funcIn("KeyManagerFilter.chooseClientAlias: keyTypes = " + StringUtils.join(keyTypes, ";") + ", issuers = " + StringUtils.join(issuers, "\n"));
        for (String keyType : keyTypes){
            String[] aliases = getClientAliases(keyType, issuers);
            if (aliases!=null && aliases.length>0) {
                DebugLog.funcOut("return aliases[0] = " + aliases[0]);
                return aliases[0];
            }
        }
        String res = backKeyManager.chooseClientAlias(keyTypes, issuers, socket);
        DebugLog.log("backKeyManager.chooseClientAlias: " + res);
        res = myKeyAliases == null || myKeyAliases.contains(res) ? res : null;
        DebugLog.funcOut("return: " + res);
        LocalTracer.debugNonSensitive("KeyManagerFilter.chooseClientAlias(): keyTypes = " + StringUtils.join(keyTypes, ";") + ", issuers = " + StringUtils.join(issuers, "\n") + ", res = " + res);
        return res;
    }

    @Override
    public String chooseServerAlias(final String keyType, final Principal[] issuers, final Socket socket){
        DebugLog.funcIn("KeyManagerFilter.chooseServerAlias: keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n"));
        String[] aliases = getServerAliases(keyType, issuers);
        if (aliases!=null && aliases.length>0) {
            DebugLog.funcOut("return aliases[0] = " + aliases[0]);
            return aliases[0];
        }
        
        String res = backKeyManager.chooseServerAlias(keyType, issuers, socket);
        DebugLog.log("backKeyManager.chooseClientAlias: " + res);
        res = myKeyAliases == null || myKeyAliases.contains(res) ? res : null;
        DebugLog.funcOut("return: " + res);
        LocalTracer.debugNonSensitive("KeyManagerFilter.chooseClientAlias(): keyType = " + keyType + ", issuers = " + StringUtils.join(issuers, "\n") + ", res = " + res);
        return res;
    }
    
    @Override
    public X509Certificate[] getCertificateChain(final String alias){
        DebugLog.funcIn("KeyManagerFilter.getCertificateChain: alias = " + alias);
        X509Certificate[] res = backKeyManager.getCertificateChain(alias);
        DebugLog.funcOut("return backKeyManager.getCertificateChain: \n" + DebugLog.certsToString(res));
        LocalTracer.debugNonSensitive("KeyManagerFilter.getCertificateChain(): alias = " + alias + ", res (backKeyManager.getCertificateChain) = \n" + DebugLog.certsToString(res));
        return res;
    }

    @Override
    public PrivateKey getPrivateKey(final String alias){
        DebugLog.funcIn("KeyManagerFilter.getPrivateKey: alias = " + alias);
        PrivateKey res = backKeyManager.getPrivateKey(alias);
        if (res != null) {
            DebugLog.log("PrivateKey res = backKeyManager.getPrivateKey: class = " + res.getClass().getCanonicalName());
            DebugLog.funcOut("return res = " + res);
            LocalTracer.debugNonSensitive("KeyManagerFilter.getPrivateKey(): alias = " + alias + ", res.class = " + res.getClass());
        } else {
            DebugLog.funcOut("return backKeyManager.getPrivateKey = " + res);
            LocalTracer.debugNonSensitive("KeyManagerFilter.getPrivateKey(): alias = " + alias + ", res = null");
        }
        return res;
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