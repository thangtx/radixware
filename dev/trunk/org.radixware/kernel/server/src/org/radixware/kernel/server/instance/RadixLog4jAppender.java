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

package org.radixware.kernel.server.instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;


public class RadixLog4jAppender implements Appender {

    @Override
    public void addFilter(Filter newFilter) {
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public void clearFilters() {
    }

    @Override
    public void close() {
    }

    @Override
    public void doAppend(LoggingEvent event) {
        if (event == null || event.getMessage() == null) {
            return;
        }
        final String loggerName = event.getLoggerName() == null ? "log4jlogger" : event.getLoggerName();
        Log commonsLog = LogFactory.getLog(loggerName);
        final Throwable t = event.getThrowableInformation() == null ? null : event.getThrowableInformation().getThrowable();
        final Object message = event.getMessage();
        if (event.getLevel().isGreaterOrEqual(Level.OFF)) {
            //do nothing
        } else if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            commonsLog.error(message, t);
        } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
            commonsLog.warn(message, t);
        } else if (event.getLevel().isGreaterOrEqual(Level.INFO)) {
            commonsLog.info(message, t);
        } else if (event.getLevel().isGreaterOrEqual(Level.ALL)) {
            commonsLog.debug(message, t);
        }
    }

    @Override
    public String getName() {
        return "RadixWare Server Log4j Appender";
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        //todo
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void setLayout(Layout layout) {
    }

    @Override
    public Layout getLayout() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
