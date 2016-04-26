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

package org.radixware.kernel.common.client.editors.xmleditor.model.validators;


final class Symbols extends ValueToken<String> {

    private final String content;

    public Symbols(final String syms, final boolean isMandatory) {
        super(isMandatory);
        content = syms;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ParsingResult<String> parse(Scaner scaner) {
        for (int i = 0; i < content.length(); i++) {
            if (scaner.isSymbol(content.charAt(i))) {
                scaner.moveCurrentPosition(1);
            } else {
                if (isMandatory()) {
                    return tokenMissing(scaner);
                } else {
                    return ParsingResult.NO_TOKEN;
                }
            }
        }
        return new ParsingResult<>(content);

    }
}
