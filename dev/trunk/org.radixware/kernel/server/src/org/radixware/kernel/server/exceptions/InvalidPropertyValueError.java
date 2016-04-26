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

package org.radixware.kernel.server.exceptions;

import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;

public class InvalidPropertyValueError extends RadixError {

    private static final long serialVersionUID = 8408002711335047367L;
    private final Id classId;
    private final Id propId;
    private final String reasonMess;

    public InvalidPropertyValueError(final String classId, final String propId) {
        this(Id.Factory.loadFrom(classId), Id.Factory.loadFrom(propId));
    }

    public InvalidPropertyValueError(final Id classId, final Id propId) {
        this(classId, propId, null);
    }

    public InvalidPropertyValueError(final String classId, final String propId, final String reasonMess) {
        this(Id.Factory.loadFrom(classId), Id.Factory.loadFrom(propId), reasonMess);
    }

    public InvalidPropertyValueError(final Id classId, final Id propId, final String reasonMess) {
        super("Property #" + String.valueOf(classId) + "." + String.valueOf(propId) + " value is invalid" + (reasonMess != null ? ": " + reasonMess : ""));
        this.propId = propId;
        this.classId = classId;
        this.reasonMess = reasonMess;
    }

    public String getReasonMess() {
        return reasonMess;
    }

    /**
     * @return the classId
     */
    public Id getClassId() {
        return classId;
    }

    /**
     * @return the propId
     */
    public Id getPropId() {
        return propId;
    }
}
