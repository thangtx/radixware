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
package org.radixware.kernel.common.defs.utils.changelog;

import org.radixware.kernel.common.enums.EChangelogItemKind;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author npopov
 * Revision in item list sort by time desc
 */
public final class ChangeLog {

    private static final String NO_REVISIONS = "<no revisions>";

    private final String comments;
    private final String localNotes;
    private final List<ChangeLogItem> items;

    public static final class Factory {

        private static final ChangeLog EMPTY = new ChangeLog(null, null, null);

        public static ChangeLog newInstance() {
            return EMPTY;
        }

        public static ChangeLog newInstance(final String comments, final String localNotes, final List<ChangeLogItem> items) {
            return new ChangeLog(comments, localNotes, items);
        }

        public static ChangeLog loadFromXml(org.radixware.schemas.commondef.ChangeLog xLog) {
            if (xLog == null || xLog.isNil()) {
                return null;
            }

            List<ChangeLogItem> items = null;
            if (!xLog.getChangeLogItemList().isEmpty()) {
                items = new ArrayList<>();
                for (org.radixware.schemas.commondef.ChangeLogItem xItem : xLog.getChangeLogItemList()) {
                    items.add(new ChangeLogItem(xItem));
                }
            }
            return new ChangeLog(xLog.getComments(), xLog.getLocalNotes(), items);
        }
    }

    public static class Utils {

        private static ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            }
        };

        public static String getPresentationString(final Long revNum, final long date,
                final String author, final String refDoc, final String description) {
            //Pattern: <revNum>) <date> | <author> | <refdoc> <description>
            StringBuilder sb = new StringBuilder();
            sb.append(revNum != null ? revNum : "-");
            sb.append(")");
            sb.append(" ");
            sb.append(Utils.formatDate(date));
            if (author != null) {
                sb.append(" | ").append(author);
            }
            if (refDoc != null) {
                sb.append(" | [").append(refDoc).append("]");
            }
            if (description != null) {
                if (refDoc == null) {
                    sb.append(" | ");
                } else {
                    sb.append(" ");
                }
                sb.append(description.replaceAll("\n", "  "));
            }
            return sb.toString();
        }

        public static long getNextRevisionNum(List<ChangeLogItem> items) {
            int maxRevNum = -1;
            for (ChangeLog.ChangeLogItem item : items) {
                if (item.getRevisionNumber() == null) {
                    continue;
                }
                maxRevNum = Math.max(maxRevNum, item.getRevisionNumber().intValue());
            }
            return maxRevNum != -1 ? maxRevNum + 1 : 1;
        }

        public static ChangeLog.ChangeLogItem getLastRevision(List<ChangeLogItem> items) {
            for (ChangeLog.ChangeLogItem item : items) {
                if (item.getKind() == EChangelogItemKind.MODIFY) {
                    return item;
                }
            }
            return null;
        }

        public static ChangeLog.ChangeLogItem getItemById(final long id, List<ChangeLogItem> items) {
            for (ChangeLog.ChangeLogItem item : items) {
                if (item.getId() == id) {
                    return item;
                }
            }
            return null;
        }

        public static void sortItems(List<ChangeLogItem> items) {
            Collections.sort(items, new Comparator<ChangeLog.ChangeLogItem>() {
                @Override
                public int compare(ChangeLog.ChangeLogItem o1, ChangeLog.ChangeLogItem o2) {
                    return Long.compare(o2.getDate(), o1.getDate());
                }
            });
        }

        public static String formatDate(final long date) {
            return dateFormat.get().format(new Date(date));
        }
    }
    
    protected ChangeLog(String comments, String localNotes, List<ChangeLogItem> items) {
        this.comments = comments;
        this.localNotes = localNotes;
        this.items = items != null ? items : Collections.<ChangeLogItem>emptyList();

        Utils.sortItems(this.items);
    }
    
    public String getPresentationString() {
        ChangeLogItem lastRev = getLastRevision();
        if (lastRev != null) {
            return Utils.getPresentationString(lastRev.revisionNumber, lastRev.date,
                    lastRev.author, lastRev.refDoc, lastRev.description);
        }
        return NO_REVISIONS;
    }

    public String getComments() {
        return comments;
    }

    public String getLocalNotes() {
        return localNotes;
    }

    public ChangeLogItem getLastRevision() {
        return Utils.getLastRevision(items);
    }

    public ChangeLogItem getItemById(final long id) {
        return Utils.getItemById(id, items);
    }

    public List<ChangeLogItem> getItems() {
        return new ArrayList<>(items);
    }

    public int getSize() {
        return items.size();
    }
    
    public void appendTo(org.radixware.schemas.commondef.ChangeLog xChangeLog, boolean exportLocals) {
        if (xChangeLog == null) {
            throw new NullPointerException("Xml document is null");
        }
        xChangeLog.setComments(comments);
        if (exportLocals) {
            xChangeLog.setLocalNotes(localNotes);
        }
        int index = items.size() - 1;
        for (; index >= 0; index--) {
            ChangeLogItem item = items.get(index);
            item.appendTo(xChangeLog.addNewChangeLogItem(), exportLocals);
        }
    }
    
    public boolean isEmpty() {
        return this == Factory.EMPTY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ChangeLog)) {
            return false;
        }
        ChangeLog other = (ChangeLog) obj;
        if (!Objects.equals(comments, other.comments)) {
            return false;
        }
        if (!Objects.equals(localNotes, other.localNotes)) {
            return false;
        }
        return Objects.equals(items, other.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comments, localNotes, items);
    }
    
    public final static class ChangeLogItem {

        private static long REVISION_ID_SEQ = 0;
        
        private final long id;
        private final Long revisionNumber;
        private final long date;
        private final String author;
        private final String description;
        private final String refDoc;
        private final String appVer;
        private final String localNotes;
        private final EChangelogItemKind kind;

        public static class Factory {

            public static ChangeLogItem loadFrom(org.radixware.schemas.commondef.ChangeLogItem xItem) {
                return new ChangeLogItem(xItem);
            }

            public static ChangeLogItem newInstance(final Long revNum, final long date, final String author,
                    final String description, final String refDoc, final String appVer, final String localNotes, EChangelogItemKind kind) {
                return new ChangeLogItem(revNum, date, author, description, refDoc, appVer, localNotes, kind);
            }
        }

        private ChangeLogItem(org.radixware.schemas.commondef.ChangeLogItem xItem) {
            this(xItem.getRevisionNumber(), xItem.getDate().getTime(),
                    xItem.getAuthor(), xItem.getDescription(), xItem.getRefDoc(),
                    xItem.getAppVer(), xItem.getLocalNotes(), xItem.getKind());
        }

        private ChangeLogItem(Long revisionNumber, long date, String author, String description, String refDoc, String appVer, String localNotes, EChangelogItemKind kind) {
            this.id = REVISION_ID_SEQ++;
            this.revisionNumber = revisionNumber;
            this.date = date;
            this.author = author;
            this.description = description;
            this.refDoc = refDoc;
            this.appVer = appVer;
            this.localNotes = localNotes;
            if (kind != null) {
                this.kind = kind;
            } else {
                this.kind = revisionNumber != null ? 
                        EChangelogItemKind.MODIFY : EChangelogItemKind.IMPORT;
            }
        }
        
        public long getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public Long getRevisionNumber() {
            return revisionNumber;
        }

        public String getRefDoc() {
            return refDoc;
        }

        public String getAppVer() {
            return appVer;
        }

        public long getDate() {
            return date;
        }

        public String getAuthor() {
            return author;
        }

        public String getLocalNotes() {
            return localNotes;
        }

        public EChangelogItemKind getKind() {
            return kind;
        }
        
        private void appendTo(org.radixware.schemas.commondef.ChangeLogItem xItem, boolean exportLocals) {
            xItem.setDescription(description);
            xItem.setRevisionNumber(revisionNumber);
            xItem.setDate(new Timestamp((date / 1000) * 1000)); //Don't store milliseconds
            xItem.setRefDoc(refDoc);
            xItem.setAppVer(appVer);
            xItem.setAuthor(author);
            if (exportLocals) {
                xItem.setLocalNotes(localNotes);
            }
            xItem.setKind(kind);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ChangeLogItem)) {
                return false;
            }
            ChangeLogItem other = (ChangeLogItem) obj;
            if (id != other.id) {
                return false;
            }
            if (!Objects.equals(revisionNumber, other.revisionNumber)) {
                return false;
            }
            if (date != other.date) {
                return false;
            }
            if (!Objects.equals(author, other.author)) {
                return false;
            }
            if (!Objects.equals(description, other.description)) {
                return false;
            }
            if (!Objects.equals(refDoc, other.refDoc)) {
                return false;
            }
            if (!Objects.equals(appVer, other.appVer)) {
                return false;
            }

            return Objects.equals(localNotes, other.localNotes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, revisionNumber, date, author, description, refDoc, appVer, localNotes);
        }
    }
}
