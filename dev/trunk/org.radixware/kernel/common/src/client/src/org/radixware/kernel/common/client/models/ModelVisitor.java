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

package org.radixware.kernel.common.client.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class ModelVisitor {

    private final List<Model> selectedModels = new ArrayList<Model>();
    private boolean cancelled;

    protected final void selectModel(final Model model) {
        selectedModels.add(model);
    }

    public final List<Model> getSelectedModels() {
        return Collections.unmodifiableList(selectedModels);
    }

    public boolean wasCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public abstract void visit(final Model model);
}
