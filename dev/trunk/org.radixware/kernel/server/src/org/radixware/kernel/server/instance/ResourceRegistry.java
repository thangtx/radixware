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
package org.radixware.kernel.server.instance;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.LocalTracer;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.net.SapAddress;
import org.radixware.kernel.server.jdbc.RadixConnection;

/**
 *
 * @author dsafonov
 */
public class ResourceRegistry {

    public static final String SUFFIX_ALL = "*";
    public static final String SEPARATOR = "/";
    private final List<IResourceRegistryItem> items = new ArrayList<>();
    private final LocalTracer tracer;

    public ResourceRegistry(LocalTracer tracer) {
        this.tracer = tracer;
    }

    public List<IResourceRegistryItem> getItems() {
        synchronized (items) {
            return new ArrayList<>(items);
        }
    }

    public List<IResourceRegistryItem> findItemsByKeyRegex(final String keyRegex) {
        final List<IResourceRegistryItem> result = new ArrayList<>();
        if (keyRegex == null || !keyRegex.isEmpty()) {
            final Pattern pattern = Pattern.compile(keyRegex);
            for (IResourceRegistryItem item : getItems()) {
                if (pattern.matcher(item.getKey() == null ? "<null>" : item.getKey()).matches()) {
                    result.add(item);
                }
            }
        }
        return result;
    }
    
    public IResourceRegistryItem findByTarget(final Object target) {
        for (IResourceRegistryItem item : getItems()) {
            if (item.getTarget() == target) {
                return item;
            }
        }
        return null;
    }
    
    public boolean unregister(final IResourceRegistryItem item) {
        if (item == null) {
            return false;
        }
        synchronized (items) {
            return items.remove(item);
        }
    }

    public void register(final IResourceRegistryItem item) {
        synchronized (items) {
            items.add(item);
        }
    }

    public void maintenance() {
        maintenance(null, null, null, true);
    }
    
    private boolean matchPattern(final String key, final String pattern) {
        if (key == null || pattern == null || pattern.isEmpty()) {
            return false;
        }
        return pattern.endsWith(SUFFIX_ALL) ? key.startsWith(pattern.substring(0, pattern.length() - 1)) : key.equals(pattern);
    }
    

    public void maintenance(String closeKeyPattern, String forceCloseReason, final List<IResourceRegistryItem> forciblyClosed, final boolean doClose) {
        final List<IResourceRegistryItem> closedItems = new ArrayList<>();

        for (IResourceRegistryItem item : getItems()) {
            try {
                final boolean forceClose = matchPattern(item.getKey(), closeKeyPattern);
                if (!item.isClosed() && (!item.isHolderAlive() || forceClose)) {
                    try {
                        if (doClose) {
                            item.close();
                            final String actualForceCloseReason = forceClose ? forceCloseReason : "dead holder";
                            tracer.put(EEventSeverity.WARNING, "Resource '" + getResourceDesc(item) + "' has been automatically closed" + (forceClose ? " (reason: " + actualForceCloseReason + ")" : ""), null, null, false);
                        }
                        if (forciblyClosed != null && forceClose) {
                            forciblyClosed.add(item);
                        }
                    } catch (IOException ex) {
                        try {
                            if (tracer != null) {
                                tracer.put(EEventSeverity.ERROR, "Exception on autoclosing" + (forceClose ? " (forced)" : "") + " resource '" + getResourceDesc(item) + "': " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
                            }
                        } catch (Throwable t) {
                            t.printStackTrace(System.err);
                        }
                    }
                }
                if (item.isClosed()) {
                    closedItems.add(item);
                }
            } catch (Exception ex) {
                try {
                    if (tracer != null) {
                        tracer.put(EEventSeverity.ERROR, "Exception on resource '" + getResourceDesc(item) + "' maintenance: " + ExceptionTextFormatter.throwableToString(ex), null, null, false);
                    }
                } catch (Throwable t) {
                    t.printStackTrace(System.err);
                }
            }
        }

        synchronized (items) {
            items.removeAll(closedItems);
        }
    }

    public void closeAllForcibly() {
        maintenance(SUFFIX_ALL, "close all", null, true);
    }

    public List<IResourceRegistryItem> closeAllWithKeyPrefix(final String keyPrefix, final String reason) {
        return closeByKey(keyPrefix + SUFFIX_ALL, reason, true);
    }

    public List<IResourceRegistryItem> closeByKey(final String keyPattern, final String reason, final boolean doClose) {
        final List<IResourceRegistryItem> forciblyClosed = new ArrayList<>();
        maintenance(keyPattern, reason, forciblyClosed, doClose);
        return forciblyClosed;
    }

    private String getResourceDesc(final IResourceRegistryItem item) {
        final String description = item.getDescription();
        return item.getKey() + (description == null ? "" : " : " + description);
    }

    public static String buildServerChannelKey(final String keyPrefix, final SapAddress sapAddress) {
        return keyPrefix + "/srv/" + sapAddress;
    }

    public static String buildServerSocketKey(final String keyPrefix, final String address) {
        return keyPrefix + "/srv/" + address;
    }

    public static String buildConnectedSocketChannelKey(final String keyPrefix, final SocketChannel channel) throws IOException {
        return keyPrefix + "/con/lcl[" + channel.getLocalAddress() + "];rmt[" + channel.getRemoteAddress() + "]";
    }
    
    public static String buildDbConnectionKey(final String keyPrefix, final Connection dbConnection) {
        String dbPart = null;
        if (dbConnection instanceof RadixConnection) {
            dbPart = ((RadixConnection) dbConnection).getShortDesc();
        } else {
            dbPart = dbConnection.getClass().getSimpleName() + "@"  + System.identityHashCode(dbConnection);
        }
        return keyPrefix + SEPARATOR + "dbcon" + SEPARATOR + dbPart;
    }

    public static String resourcesAsStr(final List<IResourceRegistryItem> items) {
        if (items != null && !items.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(items.size());
            sb.append(":\n");
            final List<IResourceRegistryItem> sortedItems = new ArrayList<>(items);
            Collections.sort(sortedItems, new Comparator<IResourceRegistryItem>() {

                @Override
                public int compare(IResourceRegistryItem o1, IResourceRegistryItem o2) {
                    final String key1 = o1 == null || o1.getKey() == null ? "" : o1.getKey();
                    final String key2 = o2 == null || o2.getKey() == null ? "" : o2.getKey();
                    return key1.compareTo(key2);
                }

            });
            for (IResourceRegistryItem item : sortedItems) {
                final long seconds = (System.nanoTime() - item.getCreationNanoTime()) / 1000000000l;
                sb.append(item.getKey()).append(" (").append(seconds).append(" s)\n");
            }
            return sb.toString();
        }
        return "0";
    }

}
