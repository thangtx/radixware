/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.aio;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.WritableByteChannel;
import java.util.*;
import javax.net.ssl.*;
import org.apache.commons.logging.LogFactory;
import org.radixware.kernel.common.enums.EClientAuthentication;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.enums.ELdapX500AttrType;
import org.radixware.kernel.common.enums.EPortSecurityProtocol;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.ssl.CertificateUtils;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.*;
import org.radixware.kernel.common.utils.net.SslUtils;

public abstract class ChannelPort implements EventHandler {

    protected static final boolean DEBUG_CLOSE = SystemPropUtils.getBooleanSystemProp("rdx.channelport.debug.close", false);
    protected Exception stackOnCloseHolder;
    private static final int GRACEFUL_CLOSE_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.channelport.graceful.close.timeout.millis", 10000);
    private static final int FRAME_READ_TIMEOUT_MILLIS = SystemPropUtils.getIntSystemProp("rdx.frame.read.timeout.millis", 30000);
    // Special frames
    public static final String FRAME_HTTP_RQ = "%[R]H";
    public static final String FRAME_HTTP_RS = "%[S]H";
    //    
    protected boolean isClosed = false;
    protected final EventDispatcher dispatcher;
    protected final LocalTracer tracer;
    protected final String recvFrameFormat;
    protected final String sendFrameFormat;
    protected ByteBufferWrapper sendBuffer;
    protected ByteBufferWrapper recvBuffer;
    private long closeRequestedMillis = -1;
    private Object data;
    private String shortDescription = "port";
    private boolean autoReadContinuation = true;
    private SSLEngine sslEngine;
    private ByteBufferWrapper encryptedSendBuffer;
    private ByteBufferWrapper encryptedRecvBuffer;
    private SslState sslState;
    private String certCn = null;
    private final int frameReadTimeoutMillis;
    private boolean ignoreSslExOnEOF = true;
    private boolean disconnectOnFrameReadTimeout = !SystemPropUtils.getBooleanSystemProp("rdx.channelport.no.default.disconnect.by.timeout", false);
    private boolean readingFrame = false;
    private boolean generateReceiveExceptions = false;

    public ChannelPort(final EventDispatcher dispatcher, final LocalTracer tracer, final String recvFrameFormat, final String sendFrameFormat) {
        this.tracer = tracer;
        this.dispatcher = dispatcher;
        this.recvFrameFormat = recvFrameFormat;
        this.sendFrameFormat = sendFrameFormat;
        sendBuffer = new ByteBufferWrapper(20 * 1024);
        recvBuffer = new ByteBufferWrapper(20 * 1024);

        int readTimeoutMillis = -1;
        if (recvFrameFormat != null) {
            readTimeoutMillis = new Framer(null, null, recvFrameFormat, Framer.CLEAR_DATA_FRAME_FORMAT).getFullFrameReceiveTimeoutMillis();
        }
        if (readTimeoutMillis == -1) {
            readTimeoutMillis = FRAME_READ_TIMEOUT_MILLIS;
        }
        this.frameReadTimeoutMillis = readTimeoutMillis;
    }

    /**
     * Set whether it is allowed to pass exceptions with
     * ChannelPort.ReceiveEvent. Prior Radix 1.2.30.x ChannelPort didn't support
     * passing exceptions in ChannelPort.ReceiveEvent. To maintain backward
     * compatibility, by default ChannelPort still never generates ReceiveEvents
     * with exceptions, unless it's explicitly allowed.
     *
     * <p>
     * Call to this method also performs <br/>
     * {@code disconnectOnInvalidData = !generateReceiveExceptions}
     * </p>
     *
     */
    public void setGenerateReceiveExceptions(boolean generateReceiveExceptions) {
        this.generateReceiveExceptions = generateReceiveExceptions;
        setDisconnectOnFrameReadTimeout(!generateReceiveExceptions);
    }

    public boolean isGenerateReceiveExceptions() {
        return generateReceiveExceptions;
    }

    public void setDisconnectOnFrameReadTimeout(boolean disconnectOnFrameReadTimeout) {
        this.disconnectOnFrameReadTimeout = disconnectOnFrameReadTimeout;
    }

    public boolean isDisconnectOnFrameReadTimeout() {
        return disconnectOnFrameReadTimeout;
    }

    public Exception getStackOnCloseHolder() {
        if (DEBUG_CLOSE) {
            return stackOnCloseHolder;
        }
        return null;
    }

    public void startRead() {
        if (isConnected()) {
            ensureReadWaitIfAllowed();
        } else {
            close();
        }
    }

    public void setAutoReadContinuation(boolean value) {
        autoReadContinuation = value;
    }

    public boolean isAutoReadContinuation() {
        return autoReadContinuation;
    }

    private boolean isSsl() {
        return sslEngine != null;
    }

    private boolean hasRemainingDataToSend() {
        if (isClosed) {
            return false;
        }
        if (isSsl()) {
            return sendBuffer.bytesCount() + encryptedSendBuffer.bytesCount() > 0;
        } else {
            return sendBuffer.bytesCount() > 0;
        }
    }

    private void requestCloseSsl(ECloseMode mode) throws IOException {
        if (mode == ECloseMode.GRACEFUL && !hasRemainingDataToSend()) {
            closeSslStream();
        }
    }

    private void closeSslStream() throws IOException {
        sslEngine.closeOutbound();
        sslState.setOutboundClosed(true);
        processSsl();
    }

    private boolean closeRequested() {
        return closeRequestedMillis > 0;
    }

    protected void reset() {
        sslEngine = null;
        isClosed = false;
        readingFrame = false;
        closeRequestedMillis = -1;
    }

    public void close(ECloseMode mode) {
        if (isClosed) {
            return;
        }

        if (mode == null) {
            throw new NullPointerException("mode is null");
        }

        if (!closeRequested()) {
            closeRequestedMillis = System.currentTimeMillis();

            if (isSsl()) {
                try {
                    requestCloseSsl(mode);
                } catch (IOException ex) {
                    mode = ECloseMode.FORCED;
                }
            }

            dispatcher.notify(new DisconnectEvent(this));

            if (!isClosed && getSelectableInChannel() != null) {
                dispatcher.waitEvent(new GracefulCloseTimeoutEvent(getSelectableInChannel()), this, System.currentTimeMillis() + GRACEFUL_CLOSE_TIMEOUT_MILLIS);
            }
        }

        if (mode == ECloseMode.GRACEFUL && hasRemainingDataToSend()) { //wait for remaining data to be sent
            return;
        }
        
        final SelectableChannel inChannel = getSelectableInChannel();
        if (inChannel != null) {
            dispatcher.unsubscribe(inChannel);
        }
        final SelectableChannel outChannel = getSelectableOutChannel();
        if (outChannel != null) {
            dispatcher.unsubscribe(outChannel);
        }

        try {
            closeUnderlyingChannel();
        } catch (RuntimeException ex) {
            LogFactory.getLog(ChannelPort.class).debug("Error on underlying channel close", ex);
        }

        readingFrame = false;

        sendBuffer.clear();
        recvBuffer.clear();

        isClosed = true;
    }

    public void close() {
        close(ECloseMode.GRACEFUL);
    }

    protected abstract void closeUnderlyingChannel();

    public void send(final byte[] packet) throws IOException {
        send(packet, null);
    }

    abstract WritableByteChannel getOutChannel();

    abstract ReadableByteChannel getInChannel();

    abstract SelectableChannel getSelectableOutChannel();

    public abstract SelectableChannel getSelectableInChannel();

    public void send(final byte[] packet, final Map<String, String> frameAttrs) throws IOException {
        if (isClosed || closeRequested()) {
            throw new IOException(isClosed ? "closed" : "closing");
        }

        final Frame frame = new Frame();
        frame.packet = packet;
        frame.attrs = frameAttrs;
        putFrame(frame);

        if (isSsl()) {
            try {
                processSsl();
            } catch (IOException e) {
                close(ECloseMode.FORCED);
                throw e;
            }
        } else {
            try {
                final int written = getOutChannel().write(sendBuffer.getForRead());
                logDataSentIfNecessary(written);
            } catch (IOException e) {
                close(ECloseMode.FORCED);
                throw e;
            }
            if (sendBuffer.hasData()) {
                ensureWriteWait();
            }
        }
    }

    private void logDataSentIfNecessary(final int cnt) {
        if (cnt > 0 && isDebugLoggingEnabled()) {
            logDataSent(sendBuffer.getJustExtractedBytes(cnt));
        }
    }

    private void logDataReceivedIfNecessary(final int cnt) {
        if (cnt > 0 && isDebugLoggingEnabled()) {
            logDataReceived(recvBuffer.getJustAppendedBytes(cnt));
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public void onEvent(final Event event) {
        if (!handleCommonEvent(event)) {
            if (sslEngine != null) {
                onEventSsl(event);
            } else {
                onEventNonSsl(event);
            }
        }

    }

    private boolean handleCommonEvent(final Event event) {
        if (event.getClass() == FrameReadTimeoutEvent.class) {
            readingFrame = false;
            if (isConnected()) {
                final byte[] storedBytes = recvBuffer.extractBytes(recvBuffer.bytesCount());
                final String errMessage = "Timeout while reading full frame (" + getShortDescription() + "), " + (disconnectOnFrameReadTimeout ? "will disconnect" : "will clear buffer");
                tracer.put(generateReceiveExceptions ? EEventSeverity.DEBUG : EEventSeverity.ERROR, errMessage, null, null, false);
                tracer.put(generateReceiveExceptions ? EEventSeverity.DEBUG : EEventSeverity.ERROR, "Data in buffer on timeout (" + getShortDescription() + "): " + encodeBytesToHex(storedBytes), null, null, true);
                if (disconnectOnFrameReadTimeout) {
                    close(ECloseMode.FORCED);
                } else if (generateReceiveExceptions) {
                    dispatcher.notify(new ReceiveEvent(this, new FrameReadTimeoutException(errMessage, storedBytes)));
                }
            }
            return true;
        } else if (event instanceof GracefulCloseTimeoutEvent) {
            close(ECloseMode.FORCED);
            return true;
        }
        return false;
    }

    private void onEventNonSsl(final Event event) {
        if (event.getClass() == EventDispatcher.WriteEvent.class) {
            if (!sendBuffer.hasData()) {
                return;
            }
            try {
                if (event.isExpired) {
                    throw new IOException("timeout on writing all scheduled data");
                }
                final int written = ((WritableByteChannel) event.getSource()).write(sendBuffer.getForRead());
                logDataSentIfNecessary(written);
                if (sendBuffer.hasData()) {
                    ensureWriteWait();
                } else if (closeRequested()) {
                    close(ECloseMode.FORCED);
                }
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Communication error (" + getShortDescription() + "): " + ExceptionTextFormatter.throwableToString(e), null, null, false);
                close(ECloseMode.FORCED);
            }
        } else if (event.getClass() == EventDispatcher.ReadEvent.class) {
            int res;
            try {
                res = ((ReadableByteChannel) event.getSource()).read(recvBuffer.getForWrite(16 * 1024));
                if (res < 0) {
                    tracer.put(EEventSeverity.DEBUG, "EOF from " + getShortDescription(), null, null, false);
                    close(ECloseMode.FORCED);
                    return;
                }
                if (isDebugLoggingEnabled()) {
                    logDataReceived(recvBuffer.getJustAppendedBytes(res));
                }
                processReceivedData();
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Communication error (" + getShortDescription() + "): " + ExceptionTextFormatter.throwableToString(e), null, null, false);
                close(ECloseMode.FORCED);
                return;
            }
            if (isAutoReadContinuation()) {
                startRead();
            }
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }

    private void processReceivedData() throws IOException {
        try {
            while (!closeRequested()) {
                if (!readingFrame && getSelectableInChannel() != null && recvBuffer.hasData()) {
                    readingFrame = true;
                    dispatcher.waitEvent(new FrameReadTimeoutEvent(getSelectableInChannel()), this, System.currentTimeMillis() + frameReadTimeoutMillis);
                }

                recvBuffer.getForRead().mark();
                final Frame frame = getFrame();
                if (frame == null) {
                    break;
                }

                readingFrame = false;
                if (getSelectableInChannel() != null) {
                    dispatcher.unsubscribe(new FrameReadTimeoutEvent(getSelectableInChannel()));
                }

                logFrameRecieved(frame);
                if (!dispatcher.notify(new ReceiveEvent(this, frame))) {
                    tracer.put(EEventSeverity.WARNING, "No handler for received data found (" + getShortDescription() + ")", null, null, false);
                }
            }
        } catch (EOFException e) {
            recvBuffer.getForRead().reset();
        } catch (InvalidDataException e) {//unexpected data received, clear the buffer
            recvBuffer.getForRead().reset();
            final byte[] storedBytes = recvBuffer.extractBytes(recvBuffer.bytesCount());
            final String errMess = "Unexpected data received (" + getShortDescription() + "), will clear recv buffer";
            tracer.put(generateReceiveExceptions ? EEventSeverity.DEBUG : EEventSeverity.ERROR, errMess + ": " + ExceptionTextFormatter.throwableToString(e), null, null, false);
            tracer.put(generateReceiveExceptions ? EEventSeverity.DEBUG : EEventSeverity.ERROR, "Bytes in buffer (" + getShortDescription() + ") on unexpected data: " + encodeBytesToHex(storedBytes), null, null, true);
            if (generateReceiveExceptions) {
                dispatcher.notify(new ReceiveEvent(this, new UnexpectedDataReceivedException(errMess + ": " + e.getMessage(), e, storedBytes)));
            }
        }
    }

    private boolean isDebugLoggingEnabled() {
        return tracer.getMinSeverity(EEventSource.SERVER_CHANNEL_PORT) <= EEventSeverity.DEBUG.getValue();
    }

    protected void logDataReceived(final byte[] bytes) {
        if (isDebugLoggingEnabled()) {
            final String dataContent = encodeBytesToHex(bytes);
            tracer.put(EEventSeverity.DEBUG, "Data received (" + getShortDescription() + ") [" + bytes.length + " b]", null, null, false);
            tracer.put(EEventSeverity.DEBUG, "Received bytes: " + dataContent, null, null, true);
        }
    }

    protected void logDataSent(final byte[] bytes) {
        if (isDebugLoggingEnabled()) {
            final String dataContent = encodeBytesToHex(bytes);
            tracer.put(EEventSeverity.DEBUG, "Data sent (" + getShortDescription() + ") [" + bytes.length + " b]", null, null, false);
            tracer.put(EEventSeverity.DEBUG, "Sent bytes: " + dataContent, null, null, true);
        }
    }

    protected void logFrameRecieved(final Frame frame) {
        if (isDebugLoggingEnabled()) {
            String frameContent;
            try {
                frameContent = encodeFrameContentToHex(frame);
            } catch (Exception ex) {
                frameContent = "Unable to encode frame content:\n" + ExceptionTextFormatter.throwableToString(ex);
            }
            String frameAttrs;
            try {
                frameAttrs = encodeFrameAttrs(frame);
            } catch (Exception ex) {
                frameAttrs = "Unable to encode frame attrs:\n" + ExceptionTextFormatter.throwableToString(ex);
            }
            final StringBuilder frameStringBuilder = new StringBuilder();
            if (frameAttrs != null && !frameAttrs.isEmpty()) {
                frameStringBuilder.append("\nFrame attributes:\n");

                frameStringBuilder.append(frameAttrs);
                frameStringBuilder.append("\nFrame body:\n");
            }
            if (frameContent != null) {
                frameStringBuilder.append(frameContent);
            }
            if (frameStringBuilder.length() == 0) {
                frameStringBuilder.append("<empty>");
            }
            final String frameAsString = frameStringBuilder.toString();
            tracer.put(EEventSeverity.DEBUG, "Frame received (" + getShortDescription() + "): " + frameAsString, null, null, true);
        }
    }

    private String encodeFrameContentToHex(final Frame frame) {
        return encodeBytesToHex(frame.packet);
    }

    private String encodeBytesToHex(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        return Hex.encode(bytes);
    }

    private String encodeFrameAttrs(final Frame frame) {
        if (frame == null || frame.attrs == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : frame.attrs.entrySet()) {
            if (entry.getKey() != null && !entry.getKey().isEmpty()) {
                sb.append(entry.getKey());
                sb.append(": ");
            }
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    public abstract boolean isConnected();

    protected Frame getFrame() throws IOException, InvalidDataException {
        final Frame frame = new Frame();
        if (recvFrameFormat == null) {
            if (!recvBuffer.hasData()) {
                return null;
            }
            frame.packet = recvBuffer.extractBytes(recvBuffer.bytesCount());
            frame.rowData = frame.packet;
        } else {
            frame.attrs = new HashMap<>();
            frame.packet = new Framer(new ByteBufferInputStream(recvBuffer.getForRead()), null, recvFrameFormat, (sendFrameFormat == null || sendFrameFormat.isEmpty()) ? Framer.CLEAR_DATA_FRAME_FORMAT : sendFrameFormat).recvFrame(0, frame.attrs);
            int rowBytesCount = recvBuffer.getForRead().position() - recvBuffer.getForRead().reset().position();
            frame.rowData = recvBuffer.extractBytes(rowBytesCount);
        }
        if (recvFrameFormat != null && recvFrameFormat.equals(Framer.CLEAR_DATA_FRAME_FORMAT) && (frame.packet == null || frame.packet.length == 0)) {
            return null;
        }
        return frame;
    }

    protected void putFrame(final Frame frame) {
        if (sendFrameFormat == null) {
            sendBuffer.getForWrite(frame.packet.length).put(frame.packet);
            return;
        }

        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream(4 * 1024);
            (new Framer(null, out, recvFrameFormat, sendFrameFormat)).sendFrame(frame.packet, frame.attrs);
            final byte[] pack = out.toByteArray();
            sendBuffer.getForWrite(pack.length).put(pack);
        } catch (IOException e) {
            throw new RadixError(e.getMessage(), e);
        }
    }

    public void setShortDescription(String shorDescription) {
        this.shortDescription = shorDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void stop() {
        //template method
    }

    public void initSsl(final SSLContext sslContext, boolean isClientMode, final EClientAuthentication clientAuth, final Collection<String> cipherSuites, EPortSecurityProtocol securityProtocol) throws IOException {
        initSsl(new SslParams(sslContext, isClientMode, clientAuth, cipherSuites, true, securityProtocol));
    }

    public void initSsl(final SslParams sslParams) throws IOException {
        sslEngine = sslParams.getSslContext().createSSLEngine();
        sslEngine.setUseClientMode(sslParams.isClientMode());
        if (!sslParams.isClientMode()) {
            switch (sslParams.getclientAuth()) {
                case Required:
                    sslEngine.setNeedClientAuth(true);
                    break;
                case Enabled:
                    sslEngine.setWantClientAuth(true);
                    break;
                default:
                    sslEngine.setNeedClientAuth(false);
            }
        }
        
        SslUtils.ensureTlsVersion(sslParams.getSecurityProtocol(), sslEngine, sslParams.isClientMode());

        final String[] enabledCipherSuites = SslUtils.calculateCipherSuites(sslParams.getSecurityProtocol(), sslParams.getCipherSuites(), Arrays.asList(sslEngine.getSupportedCipherSuites()));

        if (enabledCipherSuites == null || enabledCipherSuites.length == 0) {
            throw new IOException("None of the configured cipher suites are supported {" + getShortDescription() + "}");
        }

        sslEngine.setEnabledCipherSuites(enabledCipherSuites);

        ignoreSslExOnEOF = sslParams.isIgnoreSSlExOnEOF();

        SSLSession sslSession = sslEngine.getSession();

        recvBuffer = new ByteBufferWrapper(sslSession.getApplicationBufferSize());

        encryptedRecvBuffer = new ByteBufferWrapper(sslSession.getPacketBufferSize());
        encryptedSendBuffer = new ByteBufferWrapper(sslSession.getPacketBufferSize());

        sslState = new SslState();
        sslState.setWriteBlocked(false);
        sslState.setReadBlocked(true);
    }

    public String getCertCn() {
        try {
            if (sslEngine != null && certCn == null) {
                final String dn = sslEngine.getSession().getPeerPrincipal().getName();
                certCn = CertificateUtils.parseDistinguishedName(dn).get(ELdapX500AttrType.COMMON_NAME.getValue());
            }
        } catch (SSLPeerUnverifiedException e) {
            tracer.put(EEventSeverity.WARNING, "Unable to identify user by given certificate (" + getShortDescription() + "): " + ExceptionTextFormatter.throwableToString(e), null, null, false);
        }
        return certCn;
    }

    private void processSsl() throws IOException {

        while (sslState.needTasks() || sslState.shouldRead() || sslState.shouldWrite()) {
            if (sslState.needTasks()) {
                doTasks();
            }
            if (sslState.shouldRead()) {
                pullSsl();
            }
            if (sslState.shouldWrite()) {
                pushSsl();
            }
        }

        if ((sslEngine.isInboundDone() || sslState.isInboundTerminated()) && (sslEngine.isOutboundDone() && !encryptedSendBuffer.hasData())) {
            close(ECloseMode.FORCED);
        }
    }

    private void pushSsl() throws IOException {
        if (hasRemainingDataToSend() || sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
            if (flushEncryptedSendBuffer()) {
                SSLEngineResult result = null;
                try {
                    result = sslEngine.wrap(sendBuffer.getForRead(), encryptedSendBuffer.getForWrite(sslEngine.getSession().getPacketBufferSize()));
                } catch (RuntimeException ex) {
                    throw new IOException("SSL engine error", ex);
                }

                if (result.bytesConsumed() > 0) {
                    logDataSentIfNecessary(result.bytesConsumed());//not actually sent, but log anyway
                }
                switch (result.getStatus()) {
                    case BUFFER_OVERFLOW:
                        //should never happen, buffer is autoextending
                        throw new IOException("encrypted send buffer overflow");
                    case BUFFER_UNDERFLOW:
                        throw new IOException("buffer_underflow after wrap");
                }
                switch (result.getHandshakeStatus()) {
                    case NEED_UNWRAP:
                        sslState.setWriteBlocked(!encryptedSendBuffer.hasData());
                        sslState.setReadBlocked(false);
                        break;
                    case FINISHED:
                        sslState.setWriteBlocked(false);
                        sslState.setReadBlocked(false);
                        break;
                }
                if (closeRequested() && !hasRemainingDataToSend() && !sslState.isOutboundClosed()) {
                    closeSslStream();
                }
            } else {
                sslState.setWriteBlocked(true);
            }
        }
    }

    private void pullSsl() throws IOException {
        int bytesRead;
        if (getInChannel() != null && getInChannel().isOpen()) {
            bytesRead = getInChannel().read(encryptedRecvBuffer.getForWrite(sslEngine.getSession().getApplicationBufferSize()));
        } else {
            bytesRead = -1;
        }
        if (bytesRead == -1) {
            if (sslState.isNoChanceToUnwrap()) {
                try {
                    sslEngine.closeInbound();
                } catch (SSLException ex) {
                    if (!ignoreSslExOnEOF) {
                        throw ex;
                    }
                }
            }

            sslState.setInboundTerminated(true);
        }
        SSLEngineResult result = null;
        try {
            result = sslEngine.unwrap(encryptedRecvBuffer.getForRead(), recvBuffer.getForWrite(sslEngine.getSession().getApplicationBufferSize()));
        } catch (RuntimeException ex) {
            throw new IOException("SSL engine error", ex);
        }

        if (result.bytesProduced() > 0) {
            logDataReceivedIfNecessary(result.bytesProduced());
            processReceivedData();
        }

        switch (result.getStatus()) {
            case BUFFER_UNDERFLOW:
                if (bytesRead == 0) {
                    ensureReadWaitIfAllowed();
                    sslState.setReadBlocked(true);
                } else if (sslState.isInboundTerminated()) {
                    sslState.setNoChanceToUnwrap(true);
                }
                break;
        }

        switch (result.getHandshakeStatus()) {
            case NEED_WRAP:
                if (result.bytesConsumed() == 0 && bytesRead == 0) {
                    ensureReadWaitIfAllowed();
                    sslState.setWriteBlocked(false);
                    sslState.setReadBlocked(true);
                }
                break;
            case FINISHED:
                sslState.setWriteBlocked(false);
                sslState.setReadBlocked(false);
                break;
        }
    }

    private void doTasks() {
        if (sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK) {

            Runnable task;
            while ((task = sslEngine.getDelegatedTask()) != null) {
                task.run();
            }

            if (sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                sslState.setWriteBlocked(false);
            }

            if (sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                sslState.setReadBlocked(false);
            }
        }
    }

    /**
     * Tries to write the data from <code>encryptedSendBuffer</code> to the
     * socket. If not all data is sent, the write interest is activated.
     *
     * @return <code>true</code> if all data was sent, <code>false</code>
     * otherwise.
     */
    private boolean flushEncryptedSendBuffer() throws IOException {
        try {
            if (getOutChannel() == null) {
                throw new IOException("out channel is null");
            }
            getOutChannel().write(encryptedSendBuffer.getForRead());
        } catch (IOException e) {
            close(ECloseMode.FORCED);
            throw e;
        }
        if (encryptedSendBuffer.bytesCount() > 0) {
            ensureWriteWait();
            return false;
        }
        return true;
    }

    private void ensureWriteWait() {
        dispatcher.unsubscribe(new EventDispatcher.WriteEvent(getSelectableOutChannel()));
        dispatcher.waitEvent(new EventDispatcher.WriteEvent(getSelectableOutChannel()), this, -1);
    }

    private void ensureReadWaitIfAllowed() {
        if (isAutoReadContinuation() && isConnected()) {
            dispatcher.unsubscribe(new EventDispatcher.ReadEvent(getSelectableInChannel(), null));
            dispatcher.waitEvent(new EventDispatcher.ReadEvent(getSelectableInChannel(), null), this, -1);
        }
    }

    private void onEventSsl(final Event event) {
        if (event.getClass() == EventDispatcher.WriteEvent.class) {
            if (event.isExpired) {
                close(ECloseMode.FORCED);
                return;
            }
            sslState.setWriteBlocked(false);
            try {
                processSsl();
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Communication error (" + getShortDescription() + "): " + ExceptionTextFormatter.throwableToString(e), null, null, false);
                close(ECloseMode.FORCED);
            }
        } else if (event.getClass() == EventDispatcher.ReadEvent.class) {
            sslState.setReadBlocked(false);
            try {
                processSsl();
                ensureReadWaitIfAllowed();
            } catch (IOException e) {
                tracer.put(EEventSeverity.ERROR, "Communcation error (" + getShortDescription() + "): " + ExceptionTextFormatter.exceptionStackToString(e), null, null, false);
                close(ECloseMode.FORCED);
            }
        } else {
            throw new RadixError("Invalid event " + event);
        }
    }

    static public class ReceiveEvent extends Event {

        final public ChannelPort source;
        final public Frame frame;
        final public Exception exception;

        public ReceiveEvent(final ChannelPort source) {
            this.source = source;
            this.frame = null;
            this.exception = null;
        }

        public ReceiveEvent(final ChannelPort source, final Frame frame) {
            this.source = source;
            this.frame = frame;
            this.exception = null;
        }

        public ReceiveEvent(final ChannelPort source, final Exception exception) {
            this.source = source;
            this.frame = null;
            this.exception = exception;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(final Object o) {
            return o.getClass() == getClass() && source == ((ReceiveEvent) o).source;
        }
    }

    static public class DisconnectEvent extends Event {

        ChannelPort source;

        public DisconnectEvent(final ChannelPort source) {
            this.source = source;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(final Object o) {
            return o.getClass() == getClass() && source == ((DisconnectEvent) o).source;
        }
    }

    static public class ConnectEvent extends Event {

        final public ChannelPort source;

        public ConnectEvent(final ChannelPort source) {
            this.source = source;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(final Object o) {
            return o.getClass() == getClass() && source == ((ConnectEvent) o).source;
        }
    }

    public static class Frame {

        public byte rowData[] = null;
        public byte packet[] = null;
        public Map<String, String> attrs = null;
    }

    public static class UnexpectedDataReceivedException extends IOException {

        private final byte[] dataInBuffer;

        public UnexpectedDataReceivedException(final String message, final Exception cause, byte[] dataInBuffer) {
            super(message, cause);
            this.dataInBuffer = dataInBuffer;
        }

        public byte[] getDataInBuffer() {
            return dataInBuffer;
        }
    }

    public static class FrameReadTimeoutException extends IOException {

        private final byte[] dataInBuffer;

        public FrameReadTimeoutException(final String message, final byte[] dataInBuffer) {
            super(message);
            this.dataInBuffer = dataInBuffer;
        }

        public byte[] getDataInBuffer() {
            return dataInBuffer;
        }
    }

    static public class FrameReadTimeoutEvent extends Event {

        final public SelectableChannel source;

        public FrameReadTimeoutEvent(final SelectableChannel source) {
            this.source = source;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(final Object o) {
            return o.getClass() == getClass() && source == ((FrameReadTimeoutEvent) o).source;
        }
    }

    static public class GracefulCloseTimeoutEvent extends Event {

        final public SelectableChannel source;

        public GracefulCloseTimeoutEvent(final SelectableChannel source) {
            this.source = source;
        }

        @Override
        public Object getSource() {
            return source;
        }

        @Override
        public boolean matchs(final Object o) {
            return o.getClass() == getClass() && source == ((GracefulCloseTimeoutEvent) o).source;
        }
    }

    public static enum ECloseMode {

        FORCED,
        GRACEFUL;
    }

    private class SslState {

        private boolean wantRead;
        private boolean readBlocked;
        private boolean writeBlocked;
        private boolean outboundClosed;
        private boolean inboundTerminated;
        private boolean noChanceToUnwrap;

        public boolean isReadBlocked() {
            return readBlocked;
        }

        public void setReadBlocked(boolean readBlocked) {
            this.readBlocked = readBlocked;
        }

        public boolean isWriteBlocked() {
            return writeBlocked;
        }

        public void setWriteBlocked(boolean writeBlocked) {
            this.writeBlocked = writeBlocked;
        }

        public boolean shouldRead() {
            return !isReadBlocked() && !sslEngine.isInboundDone();
        }

        public boolean shouldWrite() {
            return (hasRemainingDataToSend() || sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_WRAP) && !isWriteBlocked();
        }

        public boolean needTasks() {
            return sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK;
        }

        public boolean isOutboundClosed() {
            return outboundClosed;
        }

        public void setOutboundClosed(boolean outboundClosed) {
            this.outboundClosed = outboundClosed;
        }

        public boolean isInboundTerminated() {
            return inboundTerminated;
        }

        public void setInboundTerminated(boolean inboundTerminated) {
            this.inboundTerminated = inboundTerminated;
        }

        public boolean isNoChanceToUnwrap() {
            return noChanceToUnwrap;
        }

        public void setNoChanceToUnwrap(boolean noChanceToUnwrap) {
            this.noChanceToUnwrap = noChanceToUnwrap;
        }

        @Override
        public String toString() {
            return "SslState{" + "wantRead=" + wantRead + ", readBlocked=" + readBlocked + ", writeBlocked=" + writeBlocked + ", outboundClosed=" + outboundClosed + ", inboundTerminated=" + inboundTerminated + ", noChanceToUnwrap=" + noChanceToUnwrap + '}';
        }
    }

    public static class SslParams {

        private SSLContext sslContext;
        private boolean clientMode;
        private EClientAuthentication clientAuth;
        private Collection<String> cipherSuites;
        private boolean ignoreSSlExOnEOF;
        private final EPortSecurityProtocol securityProtocol;

        public SslParams(SSLContext sslContext, boolean isClientMode, EClientAuthentication clientAuth, Collection<String> cipherSuites, boolean ignoreSSlExOnEOF, EPortSecurityProtocol securityProtocol) {
            this.sslContext = sslContext;
            this.clientMode = isClientMode;
            this.clientAuth = clientAuth;
            this.cipherSuites = cipherSuites;
            this.ignoreSSlExOnEOF = ignoreSSlExOnEOF;
            this.securityProtocol = Utils.nvlOf(securityProtocol, EPortSecurityProtocol.SSL);
        }

        public SSLContext getSslContext() {
            return sslContext;
        }

        public void setSslContext(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        public boolean isClientMode() {
            return clientMode;
        }

        public void setClientMode(boolean clientMode) {
            this.clientMode = clientMode;
        }

        public EClientAuthentication getclientAuth() {
            return clientAuth;
        }

        public void setClientAuth(EClientAuthentication clientAuth) {
            this.clientAuth = clientAuth;
        }

        public Collection<String> getCipherSuites() {
            return cipherSuites;
        }

        public void setCipherSuites(Collection<String> cipherSuites) {
            this.cipherSuites = cipherSuites;
        }

        public boolean isIgnoreSSlExOnEOF() {
            return ignoreSSlExOnEOF;
        }

        public void setIgnoreSSlExOnEOF(boolean ignoreSSlExOnEOF) {
            this.ignoreSSlExOnEOF = ignoreSSlExOnEOF;
        }

        public EPortSecurityProtocol getSecurityProtocol() {
            return securityProtocol;
        }
    }
}
