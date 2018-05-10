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
package org.radixware.kernel.server.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.radixware.kernel.common.enums.EPriority;

public interface IPriorityResourceManager {

    /**
     *
     * @param timeoutMillis pass -1 to wait forever, 0 for no wait (the same as
     * {@linkplain  requestTicket(long)})
     * @return ticket or null in case timeout expires
     */
    public Ticket requestTicket(int priority, long timeoutMillis) throws InterruptedException;

    /**
     * @return Ticket of null if there is no ticket available at the moment
     */
    public Ticket requestTicketNow(int priority);

    /**
     *
     * @param ticket
     * @return true if ticket has been released, false if ticket has already
     * been released
     */
    public boolean releaseTicket(final Ticket ticket);

    public int getCapturedTicketsCount(final int priority);

    public void setOptions(Options options);

    public static class Ticket {

        private volatile Callable<Boolean> holderAliveChecker;
        private final int priority;
        private final AtomicBoolean released = new AtomicBoolean(false);
        private final long captureTimeMillis = System.currentTimeMillis();

        public Ticket(final int priority) {
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }

        public void setHolderAliveChecker(Callable<Boolean> holderAliveChecker) {
            this.holderAliveChecker = holderAliveChecker;
        }

        public boolean isHolderAlive() {
            Callable<Boolean> checkerSnapshot = holderAliveChecker;
            if (checkerSnapshot != null) {
                try {
                    return checkerSnapshot.call();
                } catch (Exception ex) {
                    //assume it is alive
                }
            }
            return true;
        }

        public long getCaptureTimeMillis() {
            return captureTimeMillis;
        }

        /**
         * @return true if this call was the first.
         */
        protected boolean release() {
            return !released.getAndSet(true);
        }

        public boolean isReleased() {
            return released.get();
        }

        public static int normalizePriority(final int priority) {
            if (priority < 1 || priority > 9) {
                return EPriority.NORMAL.getValue().intValue();
            }
            if (priority <= EPriority.NORMAL.getValue()) {
                return EPriority.NORMAL.getValue().intValue();
            }
            if (priority <= EPriority.ABOVE_NORMAL.getValue()) {
                return EPriority.ABOVE_NORMAL.getValue().intValue();
            }
            if (priority <= EPriority.HIGH.getValue()) {
                return EPriority.HIGH.getValue().intValue();
            }
            if (priority <= EPriority.VERY_HIGH.getValue()) {
                return EPriority.VERY_HIGH.getValue().intValue();
            }
            return EPriority.CRITICAL.getValue().intValue();
        }

    }

    public static class Options {

        private final int normalCount;
        private final int aboveNormalCount;
        private final int highCount;
        private final int veryHighCount;
        private final int criticalCount;

        public Options(int normalCount, int aboveNormalCount, int highCount, int veryHighCount, int criticalCount) {
            this.normalCount = normalCount;
            this.aboveNormalCount = aboveNormalCount;
            this.highCount = highCount;
            this.veryHighCount = veryHighCount;
            this.criticalCount = criticalCount;
        }

        public int getNormalCount() {
            return normalCount;
        }

        public int getAboveNormalCount() {
            return aboveNormalCount;
        }

        public int getHighCount() {
            return highCount;
        }

        public int getVeryHighCount() {
            return veryHighCount;
        }

        public int getCriticalCount() {
            return criticalCount;
        }

        public long getDeltaForPriority(final long priority) {
            if (priority <= EPriority.NORMAL.getValue()) {
                return getNormalCount();
            }
            if (priority <= EPriority.ABOVE_NORMAL.getValue()) {
                return getAboveNormalCount();
            }
            if (priority <= EPriority.HIGH.getValue()) {
                return getHighCount();
            }
            if (priority <= EPriority.VERY_HIGH.getValue()) {
                return getVeryHighCount();
            }
            return getCriticalCount();
        }

        @Override
        public String toString() {
            return "{" + "normalCount=" + normalCount + ", aboveNormalCount=" + aboveNormalCount + ", highCount=" + highCount + ", veryHighCount=" + veryHighCount + ", criticalCount=" + criticalCount + '}';
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 19 * hash + this.normalCount;
            hash = 19 * hash + this.aboveNormalCount;
            hash = 19 * hash + this.highCount;
            hash = 19 * hash + this.veryHighCount;
            hash = 19 * hash + this.criticalCount;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Options other = (Options) obj;
            if (this.normalCount != other.normalCount) {
                return false;
            }
            if (this.aboveNormalCount != other.aboveNormalCount) {
                return false;
            }
            if (this.highCount != other.highCount) {
                return false;
            }
            if (this.veryHighCount != other.veryHighCount) {
                return false;
            }
            if (this.criticalCount != other.criticalCount) {
                return false;
            }
            return true;
        }
    }
}
