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

package org.radixware.kernel.starter.log;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for creating Apache loggers, used by {@link  StarterLog}. This factory
 * should not perform any caching. It will be done by {@link LogFactory}.
 *
 */
public abstract class DelegateLogFactory {

    public Logger createJavaLogger(String name) {
        return new DelegateLogger(createApacheLog(name));
    }

    /**
     * @return new apache logger for specified name.
     */
    public abstract Log createApacheLog(String name);

    private static class DelegateLogger extends Logger {

        private final Log delegate;
        private Level explicitLevel = null;

        public DelegateLogger(Log delegate) {
            super("delegate.logger", null);
            this.delegate = delegate;
        }

        @Override
        public void setLevel(Level newLevel) throws SecurityException {
            explicitLevel = newLevel;
        }

        @Override
        public void log(Level level, String msg) {
            log(level, msg, (Throwable) null);
        }

        @Override
        public void log(Level level, String msg, Throwable thrown) {
            if (isLoggable(level)) {
                if (level == null || level.intValue() <= Level.FINEST.intValue()) {
                    delegate.trace(msg, thrown);
                    return;
                }
                if (level.intValue() <= Level.FINE.intValue()) {
                    delegate.debug(msg, thrown);
                    return;
                }
                if (level.intValue() <= Level.INFO.intValue()) {
                    delegate.info(msg, thrown);
                    return;
                }
                if (level.intValue() == StarterLog.EVENT_LEVEL.intValue()) {
                    RadixLogUtils.event(delegate, msg, thrown);
                    return;
                }
                if (level.intValue() <= Level.WARNING.intValue()) {
                    delegate.warn(msg, thrown);
                    return;
                }
                if (level.intValue() <= Level.SEVERE.intValue()) {
                    delegate.error(msg, thrown);
                    return;
                }
                if (level.intValue() < Level.OFF.intValue()) {
                    delegate.fatal(msg, thrown);
                    return;
                }
            }
        }

        @Override
        public boolean isLoggable(Level level) {
            if (explicitLevel != null) {
                if (level == null || level.intValue() < explicitLevel.intValue()) {
                    return false;
                }
                return true;
            }

            if (level == null || level.intValue() <= Level.FINEST.intValue()) {
                return delegate.isTraceEnabled();
            }   
            if (level.intValue() <= Level.FINE.intValue()) {
                return delegate.isDebugEnabled();
            }
            if (level.intValue() <= Level.INFO.intValue()) {
                return delegate.isInfoEnabled();
            }
            if (level.intValue() <= Level.WARNING.intValue()) {
                return delegate.isWarnEnabled();
            }
            if (level.intValue() <= Level.SEVERE.intValue()) {
                return delegate.isErrorEnabled();
            }
            if (level.intValue() < Level.OFF.intValue()) {
                return delegate.isFatalEnabled();
            }
            return false;
        }
    }
}
