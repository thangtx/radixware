package org.radixware.ws.servlet;

import java.io.Closeable;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * This class supports creation of random buffers with time-to-leave
 * options.</p>
 */
class SpeedTestServletShadow implements Closeable {

    public static final int MEGABYTE = 1 << 20;
    private static final int TIMER_TASK_DELAY = 60000;

    interface ExternalLogger {

        void log(final String data);
    }

    private final Timer timer = new Timer(true);
    private final ExternalLogger logger;
    private volatile int maxPacketSize;
    private volatile int maxPacketTTL;

    public SpeedTestServletShadow(final int maxPacketSize, final int maxPacketTTL, final ExternalLogger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("External logger can't be null");
        }else{
            this.logger = logger;
            
            this.maxPacketSize = maxPacketSize;
            this.logger.log("Max packet size for this servlet is [" + this.maxPacketSize + "] MBytes");
            
            this.maxPacketTTL = maxPacketTTL;
            this.logger.log("Max packet time-to-live for this servlet is [" + this.maxPacketTTL + "] minutes");
        }
    }

    PackageContent newPacket(final int packetSize) {
        if (packetSize <= 0 || packetSize > maxPacketSize * MEGABYTE) {
            throw new IllegalArgumentException("Packet size [" + packetSize + "] out of range 1.." + (maxPacketSize * MEGABYTE));
        } else {
            final PackageContent content = new PackageContent(packetSize, maxPacketTTL);
            final TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    if (content.ttlExhausted(System.currentTimeMillis())) {
                        content.invalidateContent();
                        this.cancel();
                    }
                }
            };

            timer.schedule(tt, TIMER_TASK_DELAY, TIMER_TASK_DELAY);
            return content;
        }
    }

    @Override
    public void close() throws IOException {
        timer.purge();
        timer.cancel();
    }
}
