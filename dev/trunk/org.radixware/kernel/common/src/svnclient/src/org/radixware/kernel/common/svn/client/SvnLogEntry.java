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
package org.radixware.kernel.common.svn.client;

import java.util.Date;
import java.util.Set;

/**
 *
 * @author akrylov
 */
public class SvnLogEntry {

    private final String message;
    private final String author;
    private final Date date;
    private final long revision;
    private final Set<String> changedPaths;

    public SvnLogEntry(final String message, final String author, final Date date, final long revision, final Set<String> changedPaths) {
        this.message = message;
        this.author = author;
        this.date = date;
        this.revision = revision;
        this.changedPaths = changedPaths;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public long getRevision() {
        return revision;
    }

    public Set<String> getChangedPaths() {
        return changedPaths;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(revision).append(" | ").append(date).append(" | ").append(author);
        if (message != null) {
            sb.append("\n").append(message);
        }
        if (changedPaths != null && !changedPaths.isEmpty()) {
            sb.append("Changed Pathes:");
            for (String s : changedPaths) {
                sb.append("\n").append(s);
            }
        }
        return sb.toString();
    }
}
