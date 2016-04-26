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

package org.radixware.kernel.designer.debugger.breakpoints;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.netbeans.api.debugger.Breakpoint;

import org.netbeans.spi.debugger.ui.BreakpointAnnotation;
import org.openide.text.Annotatable;

public class JmlBreakpointAnnotation extends BreakpointAnnotation {

    /**
     * Annotation type constant.
     */
    public static final String BREAKPOINT_ANNOTATION_TYPE = "Breakpoint"; // NOI18N
    /**
     * Annotation type constant.
     */
    public static final String DISABLED_BREAKPOINT_ANNOTATION_TYPE = "DisabledBreakpoint"; // NOI18N
    /**
     * Annotation type constant.
     */
    public static final String CONDITIONAL_BREAKPOINT_ANNOTATION_TYPE = "CondBreakpoint"; // NOI18N
    /**
     * Annotation type constant.
     */
    public static final String DISABLED_CONDITIONAL_BREAKPOINT_ANNOTATION_TYPE = "DisabledCondBreakpoint"; // NOI18N
    private Annotatable annotatable;
    private String type;
    private JmlBreakpoint breakpoint;

    public JmlBreakpointAnnotation(String type, JmlBreakpoint b) {
        this.type = type;
        this.annotatable = b.getLine();

        this.breakpoint = b;
        attach(annotatable);
    }

    @Override
    public String getAnnotationType() {
        return type;
    }

    @Override
    public String getShortDescription() {
        if (type == BREAKPOINT_ANNOTATION_TYPE) {
            return "Line Breakpoint";
        } else if (type == DISABLED_BREAKPOINT_ANNOTATION_TYPE) {
            return "Disable Line Breakpoint";
        } else if (type == CONDITIONAL_BREAKPOINT_ANNOTATION_TYPE) {
            return "Conditional Breakpoint";
        } else if (type == DISABLED_CONDITIONAL_BREAKPOINT_ANNOTATION_TYPE) {
            return "Disabled Conditional Breakpoint";
        }
        return null;
    }

    @Override
    public Breakpoint getBreakpoint() {
        return breakpoint;
    }

    void rereadProperties() {
        String currentType = this.type;
        this.type = breakpoint.isEnabled() ? BREAKPOINT_ANNOTATION_TYPE : DISABLED_BREAKPOINT_ANNOTATION_TYPE;
        if (!currentType.equals(this.type)) {
            firePropertyChange(PROP_ANNOTATION_TYPE, currentType, type);
        }
        if (this.annotatable != null) {
            detach();
        }
        this.annotatable = this.breakpoint.getLine();
        if (this.annotatable != null) {
            attach(annotatable);
        }
    }
}
