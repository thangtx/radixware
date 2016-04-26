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

package org.radixware.kernel.common.client.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.starter.Starter;
import org.radixware.kernel.starter.radixloader.RadixLoader;


public class TokenProcessor {

    private static final String ALG_NAME_DES = "DES";

    private char[] generateSeed() {
        final StringBuffer seedBuffer = new StringBuffer();
        seedBuffer.append(System.getProperty("user.name"));
        seedBuffer.append(System.getProperty("os.version"));
        if (Starter.getProductName() != null) {
            seedBuffer.append(Starter.getProductName());
        }
        if (RadixLoader.getInstance() != null && RadixLoader.getInstance().getRepositoryUri() != null) {
            seedBuffer.append(RadixLoader.getInstance().getRepositoryUri());
        }
        seedBuffer.append(System.getProperty("java.version"));
        return seedBuffer.toString().toCharArray();
    }

    public byte[] encrypt(char[] clearPwd) {
        try {
            return encrypt(generateSeed(), clearPwd);
        } catch (Exception exception) {
            throw new RadixError("Can't encrypt data", exception);
        }
    }

    public byte[] encrypt(byte[] clearData) {
        try {
            return encrypt(generateSeed(), clearData);
        } catch (Exception exception) {
            throw new RadixError("Can't encrypt data", exception);
        }
    }

    public char[] decrypt(byte[] encryptedPwd) {
        try {
            return decrypt(generateSeed(), encryptedPwd);
        } catch (Exception exception) {
            throw new RadixError("Can't dencrypt data", exception);
        }
    }

    public byte[] decryptBytes(byte[] encryptedPwd) {
        try {
            return decryptBytes(generateSeed(), encryptedPwd);
        } catch (Exception exception) {
            throw new RadixError("Can't dencrypt data", exception);
        }
    }

    private static byte[] encrypt(char[] seed, char[] cleartext) throws Exception {
        byte[] rawKey = getRawKey(getBytes(seed));
        final byte[] encrypted = encrypt(rawKey, getBytes(cleartext));
        Arrays.fill(rawKey, (byte) 0);
        Arrays.fill(seed, ' ');
        return encrypted;
    }

    private static byte[] encrypt(char[] seed, byte[] cleardata) throws Exception {
        byte[] rawKey = getRawKey(getBytes(seed));
        final byte[] encrypted = encrypt(rawKey, cleardata);
        Arrays.fill(rawKey, (byte) 0);
        Arrays.fill(seed, ' ');
        return encrypted;
    }

    private static byte[] getBytes(char[] string) {
        final ByteBuffer bb =
                Charset.forName("UTF-8").encode(CharBuffer.wrap(string));
        final byte[] content = bb.array();
        final byte[] result = new byte[bb.limit()];
        System.arraycopy(content, bb.arrayOffset(), result, 0, bb.limit());
        Arrays.fill(content, (byte) 0);
        return result;
    }

    private static char[] decrypt(char[] seed, byte[] encrypted) throws Exception {
        byte[] rawKey = getRawKey(getBytes(seed));
        byte[] result = decrypt(rawKey, encrypted);
        final char decrypted[] = ValueConverter.arrByte2arrChar(result, null);
        Arrays.fill(rawKey, (byte) 0);
        Arrays.fill(result, (byte) 0);
        Arrays.fill(seed, ' ');
        return decrypted;
    }

    private static byte[] decryptBytes(char[] seed, byte[] encrypted) throws Exception {
        byte[] rawKey = getRawKey(getBytes(seed));
        try {
            return decrypt(rawKey, encrypted);
        } finally {
            Arrays.fill(rawKey, (byte) 0);
            Arrays.fill(seed, ' ');
        }
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(ALG_NAME_DES);
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(56, sr); // 192 and 256 bits may not be available
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALG_NAME_DES);
        Cipher cipher = Cipher.getInstance(ALG_NAME_DES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, ALG_NAME_DES);
        Cipher cipher = Cipher.getInstance(ALG_NAME_DES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}
