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
package org.radixware.kernel.server.aio;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.trace.LocalTracer;

public class SocketChannelPort extends ChannelPort implements EventHandler {

    protected SocketChannel channel;

    /**
     * @param dispatcher
     * @param tracer
     * @param channel
     * @param recvFrameFormat - ������ ���������� �������� �������. null - ���
     * ����������
     * @param sendFrameFormat - ������ ���������� ��������� �������. null - ���
     * ����������
     * @param sslContext - �������� ssl-����������. null - plaintext ����������
     * @param isClientMode
     * @param �lientAuth
     */
    public SocketChannelPort(final EventDispatcher dispatcher, final LocalTracer tracer, final SocketChannel channel, final String recvFrameFormat, final String sendFrameFormat) {
        super(dispatcher, tracer, recvFrameFormat, sendFrameFormat);
        this.channel = channel;
        updateDescription();
    }

    protected final void updateDescription() {
        String descr;
        try {
            descr = channel == null ? "<null>" : ("sock{lcl=" + String.valueOf(channel.getLocalAddress() + ",rmt=" + String.valueOf(channel.getRemoteAddress())) + "}");
        } catch (IOException ex) {
            descr = "<unknown>";
        }
        setShortDescription(descr);
    }

    @Override
    protected void closeUnderlyingChannel() {
        if (channel != null) {
            dispatcher.unsubscribe(channel);
            try {
                channel.socket().shutdownInput();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                channel.socket().shutdownOutput();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                channel.socket().close();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            try {
                channel.close();
            } catch (IOException ex) {
                //do nothing
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            channel = null;
            if (DEBUG_CLOSE) {
                stackOnCloseHolder = new Exception("Channel became null");
            }
        }
    }

    public SocketChannel getChannel() {
        return channel;
    }

    @Override
    public boolean isConnected() {
        return !isClosed && channel != null && channel.isConnected() && channel.isOpen();
    }

    @Override
    WritableByteChannel getOutChannel() {
        return getChannel();
    }

    @Override
    ReadableByteChannel getInChannel() {
        return getChannel();
    }

    @Override
    SelectableChannel getSelectableOutChannel() {
        return getChannel();
    }

    @Override
    SelectableChannel getSelectableInChannel() {
        return getChannel();
    }
}