/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.auth;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Set;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.WrongFormatError;

public final class PasswordHash {           
    
    public static enum Algorithm{
        SHA1("SHA-1"), SHA256("SHA-256");
        
        private final String asString;
        
        private Algorithm(final String algo){
            asString = algo;
        }
        
        public String getTitle(){
            return asString;
        }
        
        public static Algorithm getForTitle(final String title){
            for (Algorithm algo: Algorithm.values()){
                if (title.toUpperCase().equals(algo.asString)){
                    return algo;
                }
            }
            throw new IllegalArgumentException("Hash algorithm \'"+title+"\' is not supported");
        }
    }
    
    private static final int SHA_1_MAX_HASH_LENGTH = 16;  
    
    public static final Algorithm DEFAULT_ALGORITHM = Algorithm.SHA256;
    
    public static class Factory{
        private Factory(){            
        }
        
        public static PasswordHash newInstance(final String userName, final String password){
            return new PasswordHash(userName, password);
        }
        
        public static PasswordHash newInstance(final String userName, final char[] password){
            return new PasswordHash(userName, password);
        }
        
        public static PasswordHash fromBytes(final byte[] data){
            return new PasswordHash(data);
        }
        
        public static PasswordHash fromBytes(final byte[] data, Algorithm algorithm) {
            return new PasswordHash(data, algorithm);
        }
    }
    
    private final EnumMap<Algorithm,ByteBuffer> hashes = new EnumMap<>(Algorithm.class);
    
    private PasswordHash(final String userName, final char[] password){
        addSha1(userName, password);
        add(userName, password, Algorithm.SHA256.getTitle());
    }
    
    private PasswordHash(final String userName, final String password){
        addSha1(userName, password);
        add(userName, password,Algorithm.SHA256.getTitle());
    }
    
    private PasswordHash(final byte[] data){
        int offset = 0;
        offset = importHash(Algorithm.SHA1, data, offset);
        offset = importHash(Algorithm.SHA256, data, offset);
        if (offset!=data.length){
            erase();
            throw new WrongFormatError("Wrong password hash format");
        }
    }
    
    private PasswordHash(final byte[] data, Algorithm algorithm) {
        final byte[] dataWithCount = new byte[data.length + 1];
        dataWithCount[0] = (byte)data.length;
        System.arraycopy(data, 0, dataWithCount, 1, data.length);
        int offset = importHash(algorithm, dataWithCount, 0);
        Arrays.fill(dataWithCount, (byte)0);
        if (offset!=data.length + 1){
            erase();
            throw new WrongFormatError("Wrong password hash format");
        }
    }
    
    private PasswordHash(final PasswordHash copy){
        Set<Algorithm> copyAlgoSet = copy.hashes.keySet();
        for (Algorithm algo: Algorithm.values()){
            if (copyAlgoSet.contains(algo)) {
                final byte[] hash = copy.getBytes(algo);            
                final byte[] hashCopy = new byte[hash.length];
                System.arraycopy(hash, 0, hashCopy, 0, hash.length);
                hashes.put(algo, ByteBuffer.wrap(hashCopy));
            }
        }
    }
        
    public byte[] getBytes(final Algorithm algorithm){
        final ByteBuffer bytes = hashes.get(algorithm);
        if (bytes==null){
            final String message = "%1$s hash was not defined";
            throw new IllegalArgumentError(String.format(message, algorithm.getTitle()));
        }
        return bytes.array();
    }
    
    public byte[] export(){
        int totalLength=0;
        for (ByteBuffer hash: hashes.values()){
            totalLength+=hash.array().length+1;
        }
        final byte[] data = new byte[totalLength];
        int offset = exportHash(Algorithm.SHA1,data,0);
        exportHash(Algorithm.SHA256, data, offset);
        return data;
    }
    
    private int exportHash(Algorithm algo, byte[] data, int offset){
        if (hashes.containsKey(algo)) {
            final byte[] hash = hashes.get(algo).array();
            data[offset]=(byte)hash.length;
            System.arraycopy(hash, 0, data, offset+1, hash.length);
            return offset+1+hash.length;
        } else {
            return offset;
        }
    }
    
    private int importHash(final Algorithm algo, final byte[] data, final int offset){
        if (data.length<offset+2){
            erase();
            throw new WrongFormatError("Wrong password hash format");
        }
        final byte length = data[offset];
        if (data.length<offset+1+length){
            erase();
            throw new WrongFormatError("Wrong password hash format");
        }
        final byte[] hash = new byte[length];
        hashes.put(algo, ByteBuffer.wrap(hash));
        System.arraycopy(data, offset+1, hash, 0, length);
        return offset+1+length;
    }    
    
    public PasswordHash copy(){
        return new PasswordHash(this);
    }
    
    public void erase(){
        for (ByteBuffer hash: hashes.values()){
            Arrays.fill(hash.array(), (byte)0);            
        }
        hashes.clear();
    }
        
    private void addSha1(final String userName, final String password) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            sha1.reset();
            sha1.update((userName.toUpperCase() + "-" + password).getBytes("UTF-16"));
            final byte[] byteHash = sha1.digest();
            if (byteHash.length <= SHA_1_MAX_HASH_LENGTH) {
                hashes.put(Algorithm.SHA1, ByteBuffer.wrap(byteHash));
            } else {
                final byte[] pwdHash = new byte[SHA_1_MAX_HASH_LENGTH];
                System.arraycopy(byteHash, 0, pwdHash, 0, SHA_1_MAX_HASH_LENGTH);
                Arrays.fill(byteHash, (byte)0);
                hashes.put(Algorithm.SHA1, ByteBuffer.wrap(pwdHash));
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }
        
    private void addSha1(final String userName, final char[] password) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            sha1.reset();
            final CharBuffer cb = CharBuffer.allocate(userName.length() + password.length + 1);
            cb.append(userName.toUpperCase());
            cb.append('-');
            for (int i = 0; i < password.length; i++) {
                cb.append(password[i]);
            }
            cb.position(0);
            final ByteBuffer bb = Charset.forName("UTF-16").encode(cb);
            final byte[] bufferContent = bb.array();
            final byte[] actualContent = new byte[bb.limit()];
            System.arraycopy(bufferContent, bb.arrayOffset(), actualContent, 0, bb.limit());
            try {
                sha1.update(actualContent);
                final byte[] byteHash = sha1.digest();
                if (byteHash.length <= SHA_1_MAX_HASH_LENGTH) {
                    hashes.put(Algorithm.SHA1, ByteBuffer.wrap(byteHash));
                } else {
                    final byte[] pwdHash = new byte[SHA_1_MAX_HASH_LENGTH];
                    System.arraycopy(byteHash, 0, pwdHash, 0, SHA_1_MAX_HASH_LENGTH);
                    Arrays.fill(byteHash, (byte) 0);
                    hashes.put(Algorithm.SHA1, ByteBuffer.wrap(pwdHash));
                }
            } finally {
                Arrays.fill(bufferContent, (byte) 0);
                Arrays.fill(actualContent, (byte) 0);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }    
    
    private void add(final String userName, final String password, final String hashAlgo) {
        try {
            final MessageDigest sha256 = MessageDigest.getInstance(hashAlgo);
            sha256.reset();
            sha256.update((userName.toUpperCase() + "-" + password).getBytes("UTF-8"));
            final byte[] byteHash = sha256.digest();
            if (byteHash.length!=32){
                throw new RadixError("Failed to calculate SHA-256 hash");
            }else{
                hashes.put(Algorithm.SHA256, ByteBuffer.wrap(byteHash));
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }    

    private void add(final String userName, final char[] password, final String hashAlgo) {
        try {
            final MessageDigest sha256 = MessageDigest.getInstance(hashAlgo);
            sha256.reset();
            final CharBuffer cb = CharBuffer.allocate(userName.length() + password.length + 1);
            cb.append(userName.toUpperCase());
            cb.append('-');
            for (int i = 0; i < password.length; i++) {
                cb.append(password[i]);
            }
            cb.position(0);
            final ByteBuffer bb = Charset.forName("UTF-8").encode(cb);
            final byte[] bufferContent = bb.array();
            final byte[] actualContent = new byte[bb.limit()];
            System.arraycopy(bufferContent, bb.arrayOffset(), actualContent, 0, bb.limit());
            try {
                sha256.update(actualContent);
                final byte[] byteHash = sha256.digest();
                if (byteHash.length!=32){
                    throw new RadixError("Failed to calculate SHA-256 hash");
                }else{
                    hashes.put(Algorithm.SHA256, ByteBuffer.wrap(byteHash));
                }
            } finally {
                Arrays.fill(bufferContent, (byte) 0);
                Arrays.fill(actualContent, (byte) 0);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }        
}
