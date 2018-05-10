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

package org.radixware.kernel.common.sqlscript.parser;

import org.radixware.kernel.common.sqlscript.parser.SQLConstants.StatementType;


public class SQLParsePromptStatement extends SQLParseStatement {
    private final String prompt;

    public SQLParsePromptStatement(final SQLPosition position, final String prompt) {
        super(position, StatementType.ST_PROMPT);
        if (prompt == null) {
            throw new IllegalArgumentException("Prompt can't be null");
        }
        else {
            this.prompt = prompt;
        }
    }

    public String getPrompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return "SQLParsePromptStatement{" + "prompt=" + prompt + '}';
    }
}
