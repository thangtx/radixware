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

package org.radixware.kernel.common.enums;

/**
 * RadixObject naming policy.
 */
public enum ENamingPolicy {

    /**
     * Object name defined manually.
     */
    FREE,
    /**
     * Object name defined manually and must be correct identifier.
     */
    IDENTIFIER,
    /**
     * Object name defined manually, be correct identifier and must unique in object container.
     */
    UNIQUE_IDENTIFIER,
    /**
     * Object name will never changed.
     */
    CONST,
    /**
     * Object name is caclulated.
     */
    CALC,
}
