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
package org.radixware.kernel.server.units.persocomm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.kernel.server.units.persocomm.interfaces.IDatabaseConnectionAccess;
import org.radixware.kernel.server.units.persocomm.interfaces.IExtendedRadixTrace;
import org.radixware.kernel.server.utils.OptionsGroup;
import org.radixware.schemas.personalcommunications.MessageDocument;
import org.radixware.schemas.personalcommunications.MessageStatistics;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public class NewTwitterUnit extends NewPCUnit {

    private static final int MAX_MESS_LEN = 140;

    public NewTwitterUnit(final Instance instModel, final Long id, final String title) {
        super(instModel, id, title);
    }

    public NewTwitterUnit(final Instance instModel, final Long id, final String title, final IDatabaseConnectionAccess dbca, final IExtendedRadixTrace trace) {
        super(instModel, id, title, dbca, trace);
    }

    @Override
    public CommunicationAdapter getCommunicationAdapter(CommunicationMode mode) throws DPCRecvException, DPCSendException {
        switch (mode) {
            case TRANSMIT:
                return this.new WriteTwitterCommunicationAdapter(options.twitterConsumerToken, options.twitterConsumerSecret, options.twitterAccessToken, options.twitterAccessSecret);
            default:
                throw new UnsupportedOperationException("Communication mode [" + mode + "] is not supported in the [" + this.getClass().getSimpleName() + "] adapter!");
        }
    }

    @Override
    public OptionsGroup optionsGroup(final Options options) {
        return new OptionsGroup()
                .add(PCMessages.SEND_PERIOD, options.sendPeriod)
                .add(PCMessages.TWITTER_CONSUMER_TOKEN, options.twitterConsumerToken)
                .add(PCMessages.TWITTER_CONSUMER_SECRET, options.twitterConsumerSecret)
                .add(PCMessages.TWITTER_ACCESS_TOKEN, options.twitterAccessToken)
                .add(PCMessages.TWITTER_ACCESS_SECRET, options.twitterAccessSecret);
    }

    @Override public boolean supportsTransmitting() {return true;}
    @Override public boolean supportsReceiving() {return false;}
    
    @Override
    public String getUnitTypeTitle() {
        return PCMessages.TWITTER_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_TWITTER.getValue();
    }

    @Override
    protected void checkOptions(final Options options) throws Exception {
        if (options.twitterConsumerToken == null || options.twitterConsumerToken.isEmpty()) {
            throw new IllegalArgumentException("twitterConsumerToken can't be null or empty");
        } else if (options.twitterConsumerSecret == null || options.twitterConsumerSecret.isEmpty()) {
            throw new IllegalArgumentException("twitterConsumerToken can't be null or empty");
        } else if (options.twitterAccessToken == null || options.twitterAccessToken.isEmpty()) {
            throw new IllegalArgumentException("twitterAccessToken can't be null or empty");
        } else if (options.twitterAccessSecret == null || options.twitterAccessSecret.isEmpty()) {
            throw new IllegalArgumentException("twitterAccessSecret can't be null or empty");
        } else {
            new AccessToken(options.twitterAccessToken, options.twitterAccessSecret);
        }
    }

    protected String getProxy() {
        return (String) Utils.nvl(getInstance().getHttpProxy(), getInstance().getHttpsProxy());
    }

    protected class WriteTwitterCommunicationAdapter implements CommunicationAdapter<PreparedTwit> {

        private final Twitter twitter;
        private MessageStatistics stat;

        public WriteTwitterCommunicationAdapter(final String twitterConsumerToken, final String twitterConsumerSecret, final String twitterAccessToken, final String twitterAccessSecret) {
            final String proxy = getProxy();

            if (proxy != null) {
                final InetSocketAddress addr = ValueFormatter.parseInetSocketAddress(proxy);

                twitter = new TwitterFactory(new ConfigurationBuilder().setHttpProxyHost(addr.getHostName()).setHttpProxyPort(addr.getPort()).build()).getInstance();
            } else {
                twitter = new TwitterFactory().getInstance();
            }
            twitter.setOAuthConsumer(twitterConsumerToken, twitterConsumerSecret);
            twitter.setOAuthAccessToken(new AccessToken(twitterAccessToken, twitterAccessSecret));
        }

        @Override
        public PreparedTwit prepareMessage(final Long messageId, final MessageDocument md) throws DPCSendException {
            final List<String> parts = new ArrayList<String>();
            final String body = md.getMessage().getBody();
            final int len = body.length();
            int start = 0, end, p;

            while (start < len) {
                p = end = Math.min(start + MAX_MESS_LEN, len);

                if (end != len) {
                    while (--p >= start) {
                        if (body.charAt(p) == ' ') {
                            break;
                        }
                    }

                    if (p >= start) {
                        end = p + 1;
                    }
                }
                parts.add(body.substring(start, p));

                start = end;
            }

            return new PreparedTwit(md.getMessage().getAddressTo(), parts.toArray(new String[parts.size()]));
        }

        @Override
        public boolean sendMessage(final Long messageId, final PreparedTwit msg) throws DPCSendException {
            for (String item : msg.getParts()) {
                try {
                    twitter.sendDirectMessage(msg.getAddress(), item);
                } catch (TwitterException ex) {
                    throw new DPCSendException(ex.getMessage(), ex);
                }
            }
            sendCallback(messageId, null);
            return true;
        }

        @Override
        public void setStatistics(MessageStatistics stat) throws DPCSendException {
            this.stat = stat;
        }

        @Override
        public MessageStatistics getStatistics() throws DPCSendException {
            return stat;
        }

        @Override
        public PreparedTwit receiveMessage() throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public MessageDocument unprepareMessage(PreparedTwit msg) throws DPCRecvException {
            throw new IllegalStateException("This method can't be called for adapter to transmit");
        }

        @Override
        public void close() throws IOException {
            twitter.shutdown();
        }
    }

    protected static class PreparedTwit {

        private final String address;
        private final String[] parts;

        public PreparedTwit(final String address, final String[] parts) {
            this.address = address;
            this.parts = parts;
        }

        public String getAddress() {
            return address;
        }

        public String[] getParts() {
            return parts;
        }
    }
}
