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

package org.radixware.kernel.designer.ads.common.upgrade;

import org.radixware.kernel.common.scml.CommentsAnalizer;


class SqlProcessor {

    private final CommentsAnalizer commentsAnalizer = CommentsAnalizer.Factory.newSqlCommentsAnalizer();

    private enum EState {

        BEFORE_SELECT,
        IN_SELECT,
        AFTER_FROM
    }
    private EState state = EState.BEFORE_SELECT;
    private int level = 0;

    public void process(String sql) {
        if (state == EState.AFTER_FROM) {
            return;
        }

        final char[] chars = sql.toCharArray();
        final int len = chars.length;
        for (int i = 0; i < len; i++) {
            char c = chars[i];

            commentsAnalizer.process(c);
            
            if (commentsAnalizer.isInComment()) {
                continue;
            }

            if (commentsAnalizer.isInString()) {
                continue;
            }

            switch (c) {
                case '(':
                    level++;
                    break;
                case ')':
                    level--;
                    break;
                case 's':
                case 'S':
                    if (i <= len - 6 && "select".equals(sql.substring(i, i + 6).toLowerCase())) {
                        state = EState.IN_SELECT;
                        level++;
                        i += 5;
                    }
                    break;
                case 'f':
                case 'F':
                    if (i <= len - 4 && "from".equals(sql.substring(i, i + 4).toLowerCase())) {
                        level--;
                        i += 3;
                        if (level == 0) {
                            state = EState.AFTER_FROM;
                            return;
                        }
                    }
                    break;
            }

            if (state == EState.BEFORE_SELECT && Character.isLetterOrDigit(c)) {
                state = EState.AFTER_FROM;
                return;
            }
        }
    }

    // after select and before from
    public boolean isInTopSelect() {
        return (state == EState.IN_SELECT && level == 1);
    }
}
