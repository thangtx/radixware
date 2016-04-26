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
package org.radixware.kernel.server.units.persocomm;

import java.net.InetSocketAddress;
import org.radixware.kernel.common.enums.EUnitType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.ValueFormatter;
import org.radixware.kernel.server.exceptions.DPCRecvException;
import org.radixware.kernel.server.exceptions.DPCSendException;
import org.radixware.kernel.server.instance.Instance;
import org.radixware.schemas.personalcommunications.MessageDocument;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterUnit extends PCUnit {

    private static final int MAX_MESS_LEN = 140;

    public TwitterUnit(final Instance instance, final Long id, final String title) {
        super(instance, id, title);
    }

    @Override
    public String optionsToString() {
        return "{\n\t"
                + PCMessages.SEND_PERIOD + " " + String.valueOf(options.sendPeriod) + "; \n\t"
                + PCMessages.TWITTER_CONSUMER_TOKEN + " " + String.valueOf(options.twitterConsumerToken) + "; \n\t"
                + PCMessages.TWITTER_CONSUMER_SECRET + " " + String.valueOf(options.twitterConsumerSecret) + "; \n\t"
                + PCMessages.TWITTER_ACCESS_TOKEN + " " + String.valueOf(options.twitterAccessToken) + "; \n\t"
                + PCMessages.TWITTER_ACCESS_SECRET + " " + String.valueOf(options.twitterAccessSecret) + "; \n\t"
                + "}";
    }

    @Override
    protected void recvMessages() throws DPCRecvException {
    }

    @Override
    protected void send(final MessageDocument m, final Long id) throws DPCSendException {
        try {
            final Twitter twitter;
            final String proxy = (String) Utils.nvl(getInstance().getHttpProxy(), getInstance().getHttpsProxy());
            if (proxy != null) {
                final InetSocketAddress addr = ValueFormatter.parseInetSocketAddress(proxy);
                final ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setHttpProxyHost(addr.getHostName());
                builder.setHttpProxyPort(addr.getPort());
                twitter = new TwitterFactory(builder.build()).getInstance();
            } else {
                twitter = new TwitterFactory().getInstance();
            }
            twitter.setOAuthConsumer(options.twitterConsumerToken, options.twitterConsumerSecret);
            final AccessToken accessToken = new AccessToken(options.twitterAccessToken, options.twitterAccessSecret);
            twitter.setOAuthAccessToken(accessToken);

            final String body = m.getMessage().getBody();
            final int len = body.length();

            int start = 0, end;
            while (start < len) {
                end = Math.min(start + MAX_MESS_LEN, len);
                String mess = body.substring(start, end);

                if (end != len) {
                    int p = end;
                    while (--p >= start) {
                        if (body.charAt(p) == ' ') {
                            break;
                        }
                    }

                    if (p >= start) {
                        end = p + 1;
                        mess = body.substring(start, p);
                    }
                }

                if (!mess.isEmpty()) {
                    twitter.sendDirectMessage(m.getMessage().getAddressTo(), mess);
                }

                start = end;
            }
        } catch (Exception e) {
            throw new DPCSendException(e.getMessage(), e);
        }

        try {
            sendCallback(id, null);
        } catch (Exception e) {
            throw new DPCSendException(e.getMessage(), e);
        }
    }

    @Override
    protected void checkOptions() throws Exception {
    }

    @Override
    public String getUnitTypeTitle() {
        return PCMessages.TWITTER_UNIT_TYPE_TITLE;
    }

    @Override
    public Long getUnitType() {
        return EUnitType.DPC_TWITTER.getValue();
    }
}
