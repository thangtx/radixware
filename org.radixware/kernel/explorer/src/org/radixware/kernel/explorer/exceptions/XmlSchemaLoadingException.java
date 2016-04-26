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

package org.radixware.kernel.explorer.exceptions;

import org.radixware.kernel.common.client.localization.MessageProvider;


public final class XmlSchemaLoadingException extends XmlEditorException {

    static final long serialVersionUID = 7139176294507874656L;

    public XmlSchemaLoadingException(MessageProvider mp, final String message, final Throwable exception) {
        super(mp.translate("XmlEditor", "Can't Open Xml Editor"), message, exception);
    }
}
