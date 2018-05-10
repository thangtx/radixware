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
package org.radixware.kernel.common.svn.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.radixware.kernel.common.svn.RadixSvnException;

/**
 *
 * @author akrylov
 */
public class SvnCredentials {

    private final SvnAuthType authType;
    private final String userName;
    private char[] password;
    private final String sshKeyFilePath;
    private final String certificatePath;
    private char[] passPhrase;
    private char[] privateKey;
    private final ISvnPasswordProvider passwordGetter;
    private static final Map<String, int[]> credentialsCache = new HashMap<>();
    private String myrealm;
    private static final Class[] classes = new Class[]{SvnCredentials.class,
        SvnRepository.class,
        SvnRARepository.class};

    void reset() {
        if (myrealm != null) {
            credentialsCache.remove(myrealm);
        }
    }

    public final static class Factory {

        private Factory() {
        }

        public static SvnCredentials newSshKeyFileInstance(String userName, File keyFile) {
            return new SvnCredentials(SvnAuthType.SSH_KEY_FILE, userName, null, keyFile.getAbsolutePath(), null, null, null);
        }

        public static SvnCredentials newSshKeyFileInstance(String userName, File keyFile, char[] keyFilePassPhrase) {
            return new SvnCredentials(SvnAuthType.SSH_KEY_FILE, userName, null, keyFile.getAbsolutePath(), null, keyFilePassPhrase, null);
        }

        public static SvnCredentials newSshKeyFileInstance(String userName, File keyFile, ISvnPasswordProvider passwordGetter) {
            return new SvnCredentials(SvnAuthType.SSH_KEY_FILE, userName, null, keyFile.getAbsolutePath(), null, null, passwordGetter);
        }

        public static SvnCredentials newCertificateInstance(String userName, File certFile, char[] certPassPhrase) {
            return new SvnCredentials(SvnAuthType.SSL, userName, null, null, certFile.getAbsolutePath(), certPassPhrase, null);
        }

        public static SvnCredentials newCertificateInstance(String userName, File certFile, ISvnPasswordProvider passwordGetter) {
            return new SvnCredentials(SvnAuthType.SSL, userName, null, null, certFile.getAbsolutePath(), null, passwordGetter);
        }

        public static SvnCredentials newSVNPasswordInstance(String userName, ISvnPasswordProvider passwordGetter) {
            return new SvnCredentials(SvnAuthType.SVN_PASSWORD, userName, null, null, null, null, passwordGetter);
        }

        public static SvnCredentials newSSHPasswordInstance(String userName, ISvnPasswordProvider passwordGetter) {
            return new SvnCredentials(SvnAuthType.SSH_PASSWORD, userName, null, null, null, null, passwordGetter);
        }

        public static SvnCredentials newEmptyInstance() {
            return new SvnCredentials(SvnAuthType.NONE, "", null, null, null, null, null);
        }
    }

    public SvnCredentials(SvnAuthType authType, String userName, char[] password, String sshKeyFilePath, String certificatePath, char[] passPhrase, ISvnPasswordProvider passwordGetter) {
        this.authType = authType;
        this.userName = userName;
        this.password = password;
        this.sshKeyFilePath = sshKeyFilePath;
        this.certificatePath = certificatePath;
        this.passPhrase = passPhrase;
        this.passwordGetter = passwordGetter;
        this.privateKey = null;
    }

    private static final int mask = ((new Random().nextInt(3)) ^ SvnCredentials.class.hashCode());

    public SvnAuthType getAuthType() {
        return authType;
    }

    public String getUserName() {
        return userName;
    }

    void store(String realm, char[] pwd) {
        String asString = String.valueOf(pwd);
        try {
            byte[] bytes = asString.getBytes("UTF-8");
            int[] ints = new int[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                int value = bytes[i] << 24;
                ints[i] = value ^ getMask();
            }
            myrealm = realm;
            credentialsCache.put(realm, ints);
            char[] test = restore(realm);
        } catch (UnsupportedEncodingException ex) {

        }
    }
    
    public static boolean searchCachedPassword(final String realm) {
        return credentialsCache.containsKey(realm);
    }

    public static void tryRemoveCachedPassword(final String realm) {
        credentialsCache.remove(realm);
    }
    
    public static char[] findCachedPassword(final String realm) {
        return restore(realm);
    }

    private static int getMask() {
        if (mask < 0) {
            return classes[(mask ^ SvnCredentials.class.hashCode())].hashCode();
        }
        return mask;
    }
    
    protected static char[] restore(String realm) {
        int[] data = credentialsCache.get(realm);
        if (data == null) {
            return null;
        } else {
            int[] conveted = new int[data.length];
            for (int i = 0; i < conveted.length; i++) {
                conveted[i] = data[i] ^ getMask();
            }
            byte[] bytes = new byte[conveted.length];
            for (int i = 0; i < conveted.length; i++) {
                bytes[i] = (byte) ((conveted[i] & 0xFF000000) >> 24);
            }
            String asString;
            try {
                asString = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                return null;
            }
            return asString.toCharArray();
        }

    }

    public File getKeyFile() {
        return sshKeyFilePath == null ? null : new File(sshKeyFilePath);
    }

    public InputStream getCertificateData() throws IOException {
        return certificatePath == null ? null
                : new FileInputStream(new File(certificatePath));
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public char[] getPrivateKey() {
        if (privateKey != null) {
            return privateKey;
        }
        File keyFile = getKeyFile();
        if (keyFile != null && keyFile.exists()) {
            try {
                return privateKey = FileUtils.readTextFile(keyFile, "UTF-8").toCharArray();
            } catch (IOException e) {
                return null;
            }
        }
        return privateKey;
    }

    boolean fistTime = true;

    public char[] getPassPhrase() throws RadixSvnException {
        if (passPhrase == null) {
            File keyFile = getKeyFile();
            String keyFilePath = keyFile == null ? "Unknown" : keyFile.getPath();
            passPhrase = restore(keyFilePath);
            if (passwordGetter != null) {
                try {
                    boolean ft = fistTime;
                    fistTime = false;
                    char[] pwd = passwordGetter.getPassword(ft, authType, "Enter passphrase", "Key file", keyFilePath, null);
                    if (pwd != null) {
                        passPhrase = pwd;
                        store(keyFilePath, pwd);
                    }
                } catch (RadixSvnException ex) {
                    throw ex;
                }
            }
        }
        return passPhrase;
    }

    public char[] getCertificatePassword() throws RadixSvnException {
        if (passPhrase == null) {
            final String keyFilePath = getCertificatePath() == null ? "Unknown" : getCertificatePath();
            passPhrase = restore(keyFilePath);
            if (password != null) {
                return password;
            }
            if (passwordGetter != null) {
                try {
                    boolean ft = fistTime;
                    fistTime = false;
                    char[] pwd = passwordGetter.getPassword(ft, authType, "Enter passphrase", "Certificate file", keyFilePath, null);
                    if (pwd != null) {
                        passPhrase = pwd;
                        store(keyFilePath, pwd);
                    }
                } catch (RadixSvnException ex) {
                    throw ex;
                }
            }
        }
        return passPhrase;
    }

    public char[] getPassword(String url) throws RadixSvnException {
        if (password == null) {
            password = restore(url);
            if (password != null) {
                return password;
            }
            if (passwordGetter != null) {
                try {
                    boolean ft = fistTime;
                    fistTime = false;
                    char[] pwd = passwordGetter.getPassword(ft, authType, "Enter password", "Repository", url, userName);
                    if (pwd != null) {
                        password = pwd;
                        store(url, pwd);
                    }
                } catch (RadixSvnException ex) {
                    throw ex;
                }
            }
        }
        return password;
    }

    public String computeId(String host, int port) {
        String key = userName + ":" + host + ":" + port;
        if (privateKey != null) {
            key += ":" + new String(privateKey);
        }
        if (passPhrase != null) {
            key += ":" + new String(passPhrase);
        }
        if (password != null) {
            key += ":" + new String(password);
        }
        return key;
    }
}
