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

    public SvnAuthType getAuthType() {
        return authType;
    }

    public String getUserName() {
        return userName;
    }

    public File getKeyFile() {
        return sshKeyFilePath == null ? null : new File(sshKeyFilePath);
    }

    public InputStream getCertificateData() throws IOException {
        return certificatePath == null ? new InputStream() {

            @Override
            public int read() throws IOException {
                return -1;
            }
        }
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
            if (passwordGetter != null) {
                try {
                    boolean ft = fistTime;
                    fistTime = false;
                    File keyFile = getKeyFile();
                    String keyFilePath = keyFile == null ? "Unknown" : keyFile.getPath();
                    char[] pwd = passwordGetter.getPassword(ft, authType, "Enter passphrase", "Key file", keyFilePath, null);
                    if (pwd != null) {
                        passPhrase = pwd;
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
            if (passwordGetter != null) {
                try {
                    boolean ft = fistTime;
                    fistTime = false;
                    String keyFilePath = getCertificatePath() == null ? "Unknown" : getCertificatePath();
                    char[] pwd = passwordGetter.getPassword(ft, authType, "Enter passphrase", "Certificate file", keyFilePath, null);
                    if (pwd != null) {
                        passPhrase = pwd;
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
            if (passwordGetter != null) {
                try {
                    boolean ft = fistTime;
                    fistTime = false;
                    char[] pwd = passwordGetter.getPassword(ft, authType, "Enter password", "Repository", url, userName);
                    if (pwd != null) {
                        password = pwd;
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
