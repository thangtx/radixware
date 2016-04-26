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

package org.radixware.kernel.common.mail;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.radixware.kernel.common.utils.Base64;

public final class MailSecuritySettings {

    private final static class MailSecretKey implements SecretKey {

        private byte[] key = new byte[]{122, -72, -86, 44, 101, -76, 17, 78};

        @Override
        public String getAlgorithm() {
            return "DES";
        }

        @Override
        public String getFormat() {
            return "RAW";
        }

        @Override
        public byte[] getEncoded() {
            return key;
        }
    }
    private static SecretKey key;
    private static Cipher ecipher;
    private static Cipher dcipher;

    static {
        try {
            key = new MailSecretKey();
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception ex) {
            Logger.getLogger(MailSecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String encrypt(String str) {
        try {
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            return Base64.encode(enc);
        } catch (Exception ex) {
            Logger.getLogger(MailSecuritySettings.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return null;
    }

    public static String decrypt(String str) {
        try {
            byte[] dec = Base64.decode(str);
            byte[] utf8 = dcipher.doFinal(dec);
            return new String(utf8, "UTF8");
        } catch (Exception ex) {
            Logger.getLogger(MailSecuritySettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}