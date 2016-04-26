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
package org.radixware.kernel.common.svn.client.auth;

import java.io.IOException;
import java.util.List;
import org.radixware.kernel.common.svn.client.SvnRAConnection;
import org.radixware.kernel.common.svn.client.SvnRepository;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.RAMessage;
import org.radixware.kernel.common.svn.client.SvnUtil;

/**
 *
 * @author akrylov
 */
public class SvnPlainAuthenticator extends SvnAuthenticator {

    public SvnPlainAuthenticator(SvnRAConnection connection) throws RadixSvnException {
        super(connection);
    }

    @Override
    public SvnAuthentication authenticate(List<String> mechs, String realm, SvnRepository repository) throws RadixSvnException {

        if (mechs == null || mechs.size() == 0) {
            return null;
        }
        if (mechs.contains("ANONYMOUS") && mechs.contains("CRAM-MD5")) {
            mechs.remove("ANONYMOUS");
        }
        String failureReason;
        URI location = repository.getLocation();
        SvnAuthentication auth = null;
        if (repository.getExternalUserName() != null && mechs.contains("EXTERNAL")) {
            connection.write(RAMessage.MessageItem.newWord("EXTERNAL"), RAMessage.MessageItem.newList(RAMessage.MessageItem.newString("")));
            failureReason = readAuthResponse();
        } else if (mechs.contains("ANONYMOUS")) {
            connection.write(RAMessage.MessageItem.newWord("ANONYMOUS"), RAMessage.MessageItem.newList(RAMessage.MessageItem.newString("")));
            failureReason = readAuthResponse();
        } else if (mechs.contains("CRAM-MD5")) {
            while (true) {
                if (location != null) {
                    realm = "<" + location.getScheme() + "://"
                            + location.getHost() + ":"
                            + location.getPort() + "> " + realm;
                }

                auth = getNextAuthentication(realm, location);

                if (auth == null) {
                    throw new RadixSvnException("Authentication cancelled");
                }
                if (auth.getUserName() == null || auth.getPassword() == null) {
                    failureReason = "Can''t get password. Authentication is required";
                    break;
                }
                //"(w())"
                connection.write(RAMessage.MessageItem.newWord("CRAM-MD5"), RAMessage.MessageItem.emptyList());
                while (true) {
                    List<RAMessage.MessageItem> items = connection.read(false, RAMessage.WORD, RAMessage.LIST_OPT);
                    String status = items.get(0).getString();
                    if (SvnAuthenticator.SUCCESS.equals(status)) {
                        return auth;
                    } else if (SvnAuthenticator.FAILURE.equals(status)) {
                        failureReason = "Authentication error";
                        String message = items.get(1).getList().get(0).getString();
                        if (message != null) {
                            //TODO:
                        }
                        break;
                    } else if (SvnAuthenticator.STEP.equals(status)) {
                        try {
                            byte[] response = buildChallengeResponse(auth, items.get(1).getByteArray());
                            getOutputStream().write(response);
                            getOutputStream().flush();
                        } catch (IOException e) {
                            throw new RadixSvnException(e);
                        }
                    }
                }
            }
        } else {
            throw new RadixSvnException("Cannot negotiate authentication mechanism");
        }
        if (failureReason != null) {
            throw new RadixSvnException(failureReason);
        }

        return auth;
    }

    protected String readAuthResponse() throws RadixSvnException {
        //"w(?s)"
        List<RAMessage.MessageItem> items = connection.read(false, RAMessage.WORD, RAMessage.LIST_OPT);
        if ("success".equals(items.get(0).getString())) {
            return null;
        } else if ("failure".equals(items.get(0).getString())) {
            if (items.size() > 1) {
                return items.get(1).getList().get(0).getString();
            }
        }
        return "Unexpected server response to authentication";
    }

    public byte[] buildChallengeResponse(SvnAuthentication auth, byte[] challenge) throws RadixSvnException {
        byte[] password = EncodingUtil.getBytes(auth.getPassword(), "UTF-8");
        byte[] secret = new byte[64];
        Arrays.fill(secret, (byte) 0);
        System.arraycopy(password, 0, secret, 0, Math.min(secret.length, password.length));
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        for (int i = 0; i < secret.length; i++) {
            secret[i] ^= 0x36;
        }
        digest.update(secret);
        digest.update(challenge);
        byte[] result = digest.digest();
        for (int i = 0; i < secret.length; i++) {
            secret[i] ^= (0x36 ^ 0x5c);
        }
        digest.update(secret);
        digest.update(result);
        result = digest.digest();
        String hexDigest = "";
        for (int i = 0; i < result.length; i++) {
            hexDigest += SvnUtil.getHexNumberFromByte(result[i]);
        }
        String response = auth.getUserName() + " " + hexDigest;
        try {
            response = response.getBytes("UTF-8").length + ":" + response + " ";
            return response.getBytes("UTF-8");
        } catch (IOException ex) {
            throw new RadixSvnException(ex);
        }
    }

}
