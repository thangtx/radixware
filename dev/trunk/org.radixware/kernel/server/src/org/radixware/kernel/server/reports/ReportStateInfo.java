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
package org.radixware.kernel.server.reports;

/**
 *
 * @author akrylov
 */
public class ReportStateInfo {

    private int fileCount;
    private int totalPageCount;
    private ReportStateInfo src;
    private int[] pageCounts = new int[3];
    private static final int PAGE_COUNT_ARR_INC = 10;

    public ReportStateInfo(ReportStateInfo src) {
        this.src = src;
    }

    public void load(ReportStateInfo stateInfo) {
        this.fileCount = stateInfo.fileCount;
        this.totalPageCount = stateInfo.totalPageCount;
        this.src = stateInfo.src;
        this.pageCounts = new int[stateInfo.pageCounts.length];
        System.arraycopy(stateInfo.pageCounts, 0, this.pageCounts, 0, this.pageCounts.length);
    }

    void incPageCount(int fileNumber) {
        if (src == null) {
            int fileIndex = fileNumber - 1;
            if (fileIndex >= pageCounts.length) {
                int[] dummy = new int[pageCounts.length + PAGE_COUNT_ARR_INC];
                System.arraycopy(pageCounts, 0, dummy, 0, pageCounts.length);
                pageCounts = dummy;
            }
            pageCounts[fileIndex]++;
            totalPageCount++;
        }
    }

    public ReportStateInfo getSource() {
        return src;
    }

    void incFileCount() {
        if (src == null) {
            fileCount++;
        }
    }

    public int getPageCount(int fileNumber) {
        if (src != null) {
            return src.getPageCount(fileNumber);
        }
        int fileIndex = fileNumber - 1;
        return pageCounts[fileIndex];
    }

    public int getTotalPageCount() {
        if (src == null) {
            return totalPageCount;
        }
        return src.getTotalPageCount();
    }

    public int getFileCount() {
        if (src == null) {
            return fileCount;
        }
        return src.getFileCount();
    }

    public Long getFileCountIfComputed() {
        if (src == null) {
            return null;
        } else {
            return Long.valueOf(src.getFileCount());
        }
    }
}
