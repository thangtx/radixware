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
package org.radixware.kernel.server.trace;

import java.util.Objects;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;

public final class TraceContext {

    public final String type;
    public final String id;
    private final String enterStack;

    public TraceContext(final String type, final String id, final String enterStack) {
        this.type = type;
        this.id = id;
        this.enterStack = enterStack;
    }

    public TraceContext(final String type_, final String id_) {
        this(type_, id_, null);
    }

    @Override
    public String toString() {
        return "(Type=\"" + type + "\", Id=\"" + id + "\")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TraceContext other = (TraceContext) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public String getEnterStack() {
        return enterStack;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.type);
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
