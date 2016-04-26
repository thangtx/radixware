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


package org.radixware.kernel.designer.ads.editors.refactoring;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;


public class RefactoringResult {

    public static abstract class Change {

        public abstract void open();

        public abstract String getDescription();

        public abstract Image getIcon();
    }
    
    private List<Change> changes = new ArrayList<>();
    private final String description;

    public RefactoringResult(String description) {
        this.description = description;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public boolean addChange(Change e) {
        return changes.add(e);
    }

    public String getDescription() {
        return description;
    }
}
