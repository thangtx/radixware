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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author akrylov
 */
public class SvnEntry {

    public enum Kind {

        FILE,
        LINK,
        DIRECTORY,
        UNKNOWN,
        NONE;

        public static Kind fromString(String kindStr) {
            if ("file".equals(kindStr)) {
                return FILE;
            } else if ("dir".equals(kindStr)) {
                return DIRECTORY;
            } else if ("none".equals(kindStr) || kindStr == null) {
                return NONE;
            }
            return UNKNOWN;
        }
    }

    public enum Status {

        NORMAL,
        NONE,
        OBSTRUCTED,
        CONFILICTED,
        ADDED,
        REPLACED,
        DELETED,
        INCOMPLETE,
        MISSING,
        IGNORED,
        UNVERSIONED
    }
    public static final String DIRENT_KIND = "kind";
    public static final String DIRENT_SIZE = "size";
    public static final String DIRENT_HAS_PROPS = "has-props";
    public static final String DIRENT_CREATED_REV = "created-rev";
    public static final String DIRENT_TIME = "time";
    public static final String DIRENT_LAST_AUTHOR = "last-author";

    private final String path;
    private final String name;
    private final String repositoryPath;
    private final String author;
    private final long revision;
    private final Date lastModified;
    private final String message;
    private long size;

    private final Kind kind;

    public SvnEntry(final String path, final String name, final String repositoryPath, final String author, final long revision, final Date lastModified, long size, final Kind kind, final String message) {
        this.path = path;
        this.repositoryPath = repositoryPath;
        this.author = author;
        this.revision = revision;
        this.lastModified = lastModified;
        this.kind = kind;
        this.name = name;
        this.message = message;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return repositoryPath;
    }

    public String getAuthor() {
        return author;
    }

    public long getRevision() {
        return revision;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public Kind getKind() {
        return kind;
    }

    public long getSize() {
        return size;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyy HH:MM:ss.SSS");
        sb.append(kind).append(" ").append(path).append("\n").append(revision).append(" ").append(author).append(" ").append(format.format(lastModified));
        return sb.toString();
    }
}
