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

/**
 *
 * @author akrylov
 */
public class SvnCommitSummary {

    public long revision;
    public String author;
    public Date date;
    public String errorMessage;

    public SvnCommitSummary(long revision, String author, Date date, String errorMessage) {
        this.revision = revision;
        this.author = author;
        this.date = date;
        this.errorMessage = errorMessage;
    }

    SvnCommitSummary() {

    }

    @Override
    public String toString() {
        return "" + revision + " " + author + " " + date + " " + errorMessage;
    }

}
