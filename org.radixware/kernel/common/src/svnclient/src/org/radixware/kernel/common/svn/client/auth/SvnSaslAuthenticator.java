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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslClientFactory;
import javax.security.sasl.SaslException;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.RAMessage;
import org.radixware.kernel.common.svn.client.SvnRAConnection;
import org.radixware.kernel.common.svn.client.SvnRepository;

public class SvnSaslAuthenticator extends SvnAuthenticator {

    private SaslClient saslClient;
    private SvnAuthentication authentication;
    private List<SvnAuthentication> others;

    public SvnSaslAuthenticator(SvnRAConnection connection, List<SvnAuthentication> others) throws RadixSvnException {
        super(connection);
        this.others = others;
    }

    @Override
    protected SvnAuthentication getNextAuthentication(String realm, URI location) throws RadixSvnException {
        if (others == null) {
            return null;
        }
        if (others.isEmpty()) {
            return null;
        }
        return others.remove(0);
    }

    @Override
    public SvnAuthentication authenticate(List<String> howTos, String realm, SvnRepository repository) throws RadixSvnException {
        boolean failed = true;
        authentication = null;
        boolean isAnonymous = false;

        if (howTos.contains("EXTERNAL") && repository.getExternalUserName() != null) {
            howTos = new ArrayList();
            howTos.add("EXTERNAL");
        } else {
            for (String m : howTos) {
                if ("ANONYMOUS".equals(m) || "EXTERNAL".equals(m) || "PLAIN".equals(m)) {
                    howTos = new ArrayList();
                    isAnonymous = "ANONYMOUS".equals(m);
                    howTos.add(m);
                    break;
                }
            }
        }
        dispose();
        try {
            saslClient = createSaslClient(new ArrayList<>(howTos), realm, repository, repository.getLocation());
            if (saslClient == null) {
                return new SvnPlainAuthenticator(connection).authenticate(howTos, realm, repository);
            }
            while (true) {

                // reiterate from the first available credentials next time:
                boolean startOver = false;
                try {
                    if (tryAuthentication(repository, getMechanismName(saslClient, isAnonymous))) {
                        failed = false;
                        setEncryption(repository);
                        break;
                    }
                    // some sort of authentication error.
                } catch (SaslException e) {
                    // it may be plain replaced with anonymous.
                    String mechName = getMechanismName(saslClient, isAnonymous);
                    howTos.remove(mechName);
                    startOver = true;
                }
                dispose();
                if (howTos.isEmpty()) {
                    failed = true;
                    break;
                }
                if (startOver) {
                    authentication = null;
                }
                saslClient = createSaslClient(howTos, realm, repository, repository.getLocation());
            }
        } finally {
            if (failed) {
                dispose();
            }
        }

        return authentication;
    }

    public void dispose() {
        if (saslClient != null) {
            try {
                saslClient.dispose();
            } catch (SaslException e) {
                //
            }
        }
    }

    protected boolean tryAuthentication(SvnRepository repository, String howTo) throws SaslException, RadixSvnException {
        String initialChallenge = null;
        boolean expectChallenge = !("ANONYMOUS".equals(howTo) || "EXTERNAL".equals(howTo) || "PLAIN".equals(howTo));
        if ("EXTERNAL".equals(howTo) && repository.getExternalUserName() != null) {
            initialChallenge = "";
        } else if (saslClient.hasInitialResponse()) {
            byte[] initialResponse = saslClient.evaluateChallenge(new byte[0]);
            if (initialResponse == null) {
                throw new RadixSvnException("Unexpected authentication response");
            }
            initialChallenge = toBase64(initialResponse);
        }
        if (initialChallenge != null) {
            connection.write(RAMessage.MessageItem.newWord(howTo), RAMessage.MessageItem.newList(RAMessage.MessageItem.newString(initialChallenge)));
        } else {
            connection.write(RAMessage.MessageItem.newWord(howTo), RAMessage.MessageItem.emptyList());
        }

        String status = SvnAuthenticator.STEP;

        while (SvnAuthenticator.STEP.equals(status)) {
            List<RAMessage.MessageItem> items = connection.read(false, RAMessage.WORD, RAMessage.LIST_OPT);
            status = (String) items.get(0).getString();
            if (SvnAuthenticator.FAILURE.equals(status)) {
                if (items.size() > 1) {
                    RAMessage.MessageItem item = items.get(1);
                    if (item.type == RAMessage.MessageItemType.LIST) {
                        List<RAMessage.MessageItem> list = item.getList();
                        if (list.isEmpty()) {
                            throw new RadixSvnException("Unknown auth error");
                        } else {
                            throw new RadixSvnException(list.get(0).getString());
                        }
                    }
                }
                throw new RadixSvnException("Unknown auth error");
            }
            String challenge = null;
            if (items.size() > 1 && items.get(1).type == RAMessage.MessageItemType.LIST) {
                List<RAMessage.MessageItem> list = items.get(1).getList();
                if (!list.isEmpty()) {
                    challenge = list.get(0).getString();
                }
            }

            if (challenge == null && ("CRAM-MD5".equals(howTo) || "GSSAPI".equals(howTo)) && SvnAuthenticator.SUCCESS.equals(status)) {
                challenge = "";
            }
            if ((!SvnAuthenticator.STEP.equals(status) && !SvnAuthenticator.SUCCESS.equals(status))
                    || (challenge == null && expectChallenge)) {
                throw new RadixSvnException("Unexpected authentication response");
            }
            byte[] challengeBytes = "CRAM-MD5".equals(howTo) ? challenge.getBytes() : fromBase64(challenge);
            byte[] response = null;
            while (!saslClient.isComplete()) {
                response = saslClient.evaluateChallenge(challengeBytes);
            }
            if (SvnAuthenticator.SUCCESS.equals(status)) {
                return true;
            }
            if (response == null) {
                throw new RadixSvnException("Unexpected response from " + howTo);
            }

            if (response.length > 0) {
                String responseStr = "CRAM-MD5".equals(howTo) ? new String(response) : toBase64(response);
                connection.writeWithoutEnvelope(RAMessage.MessageItem.newString(responseStr));
            } else {
                connection.writeWithoutEnvelope(RAMessage.MessageItem.newString(""));
            }
        }
        return true;

    }

    protected void setEncryption(SvnRepository repository) {
        if (connection.isEncrypted()) {
            dispose();
            return;
        }
        String qop = (String) saslClient.getNegotiatedProperty(Sasl.QOP);
        String buffSizeStr = (String) saslClient.getNegotiatedProperty(Sasl.MAX_BUFFER);
        String sendSizeStr = (String) saslClient.getNegotiatedProperty(Sasl.RAW_SEND_SIZE);

        if ("auth-int".equals(qop) || "auth-conf".equals(qop)) {
            int outBuffSize = 1000;
            int inBuffSize = 1000;
            if (sendSizeStr != null) {
                try {
                    outBuffSize = Integer.parseInt(sendSizeStr);
                } catch (NumberFormatException nfe) {
                    outBuffSize = 1000;
                }
            }
            if (buffSizeStr != null) {
                try {
                    inBuffSize = Integer.parseInt(buffSizeStr);
                } catch (NumberFormatException nfe) {
                    inBuffSize = 1000;
                }
            }
            try {
                getPlainOutputStream().flush();
            } catch (IOException e) {
                //
            }
            final OutputStream os = new SaslOutputStream(saslClient, outBuffSize, getPlainOutputStream());
            setOutputStream(os);
            final InputStream is = new SaslInputStream(saslClient, inBuffSize, getPlainInputStream());
            setInputStream(is);
            connection.setEncrypted(this);
        } else {
            dispose();
        }
    }

    protected SaslClient createSaslClient(List<String> mechs, String realm, SvnRepository repos, URI location) throws RadixSvnException {
        Map<String, String> props = new HashMap<>();
        props.put(Sasl.QOP, "auth-conf,auth-int,auth");
        props.put(Sasl.MAX_BUFFER, "8192");
        props.put(Sasl.RAW_SEND_SIZE, "8192");
        props.put(Sasl.POLICY_NOPLAINTEXT, "false");
        props.put(Sasl.REUSE, "false");
        props.put(Sasl.POLICY_NOANONYMOUS, "true");

        String[] mechsArray = (String[]) mechs.toArray(new String[mechs.size()]);
        SaslClient client = null;
        for (int i = 0; i < mechsArray.length; i++) {
            String mech = mechsArray[i];
            try {
                if ("ANONYMOUS".equals(mech) || "EXTERNAL".equals(mech) || "PLAIN".equals(mech)) {
                    props.put(Sasl.POLICY_NOANONYMOUS, "false");
                }
                SaslClientFactory clientFactory = getSaslClientFactory(mech, props);
                if (clientFactory == null) {
                    continue;
                }
                SvnAuthentication auth = null;
                if ("ANONYMOUS".equals(mech)) {
                    auth = new SvnPasswordAuthentication("", new char[0], false, location, false);
                } else if ("EXTERNAL".equals(mech)) {
                    String name = repos.getExternalUserName();
                    if (name == null) {
                        name = "";
                    }
                    auth = new SvnPasswordAuthentication(name, new char[0], false, location, false);
                } else {
                    String realmName = getFullRealmName(location, realm);
                    authentication = getNextAuthentication(realmName, location);
                    if (authentication == null) {
                        throw new RadixSvnException("Unable to authenticate", RadixSvnException.Type.REJECT_AUTH, 0);
                    }
                    auth = authentication;
                }
                client = clientFactory.createSaslClient(new String[]{"ANONYMOUS".equals(mech) ? "PLAIN" : mech}, realm, "svn", location.getHost(), props, new SaslCallbackHandler(realm, auth));
                if (client != null) {
                    break;
                }
                authentication = null;
            } catch (SaslException e) {
                mechs.remove(mechsArray[i]);
                authentication = null;
            }
        }
        return client;
    }

    private static String getFullRealmName(URI location, String realm) {
        if (location == null || realm == null) {
            return realm;
        }
        return "<" + location.getScheme() + "://" + location.getHost() + ":" + location.getPort() + "> " + realm;
    }

    private static String toBase64(byte[] src) {
        return Base64.byteArrayToBase64(src);
    }

    private static byte[] fromBase64(String src) {
        if (src == null) {
            return new byte[0];
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i < src.length(); i++) {
            char ch = src.charAt(i);
            if (!Character.isWhitespace(ch) && ch != '\n' && ch != '\r') {
                bos.write((byte) ch & 0xFF);
            }
        }
        byte[] cbytes = new byte[src.length()];
        try {
            src = new String(bos.toByteArray(), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            //
        }
        int clength = Base64.base64ToByteArray(new StringBuffer(src), cbytes);
        byte[] result = new byte[clength];
        // strip trailing -1s.
        for (int i = clength - 1; i >= 0; i--) {
            if (i == -1) {
                clength--;
            }
        }
        System.arraycopy(cbytes, 0, result, 0, clength);
        return result;
    }

    private static String getMechanismName(SaslClient client, boolean isAnonymous) {
        if (client == null) {
            return null;
        }
        String name = client.getMechanismName();
        if ("PLAIN".equals(name) && isAnonymous) {
            name = "ANONYMOUS";
        }
        return name;
    }

    private static SaslClientFactory getSaslClientFactory(String howTo, Map props) {
        if (howTo == null) {
            return null;
        }
        if ("ANONYMOUS".equals(howTo)) {
            howTo = "PLAIN";
        }
        for (Enumeration factories = Sasl.getSaslClientFactories(); factories.hasMoreElements();) {
            SaslClientFactory factory = (SaslClientFactory) factories.nextElement();
            String[] howTos = factory.getMechanismNames(props);
            for (int i = 0; howTos != null && i < howTos.length; i++) {
                if (howTo.endsWith(howTos[i])) {
                    return factory;
                }
            }
        }
        return null;
    }

    private static class SaslCallbackHandler implements CallbackHandler {

        private final String realm;
        private final SvnAuthentication authentication;

        public SaslCallbackHandler(String realm, SvnAuthentication auth) {
            this.realm = realm;
            this.authentication = auth;
        }

        @Override
        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    final String userName = authentication.getUserName();
                    ((NameCallback) callback).setName(userName != null ? userName : "");
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(authentication.getPassword());
                } else if (callback instanceof RealmCallback) {
                    ((RealmCallback) callback).setText(realm);
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }
    }
}
