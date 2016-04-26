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

package org.radixware.kernel.common.msdl;

import org.radixware.kernel.common.resources.icons.RadixIcon;


public final class MsdlIcon extends RadixIcon {

    public static final MsdlIcon MSDL_SCHEME = new MsdlIcon("ads/msdl/msdl_scheme.svg");
    public static final MsdlIcon MSDL_SCHEME_FIELD = new MsdlIcon("ads/msdl/field.svg");
    public static final MsdlIcon MSDL_SCHEME_FIELDS = new MsdlIcon("ads/msdl/fields.svg");

    private MsdlIcon(String uri) {
        super(uri);
    }
}
