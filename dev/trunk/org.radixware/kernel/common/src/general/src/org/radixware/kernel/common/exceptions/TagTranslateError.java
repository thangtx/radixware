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

package org.radixware.kernel.common.exceptions;

import org.radixware.kernel.common.scml.Scml;

/**
 * Tag translate error Вызывается в случае, если {@link Scml SCML} содержит
 * недопустимый для текущего {@link Scml.Environment окружения} {@Scml.Tag тэг},
 * или не смогла его оттранслировать.
 *
 */
public class TagTranslateError extends DefinitionError {

    private final Scml.Tag tag;

    public TagTranslateError(Scml.Tag tag) {
        super("Unable to translate SCML tag");
        this.tag = tag;
    }

    public TagTranslateError(Scml.Tag tag, String cause) {
        super(cause);
        this.tag = tag;
    }

    public TagTranslateError(Scml.Tag tag, Throwable cause) {
        super("Unable to translate SCML tag", cause);
        this.tag = tag;
    }

    public Scml.Tag getTag() {
        return tag;
    }
}
