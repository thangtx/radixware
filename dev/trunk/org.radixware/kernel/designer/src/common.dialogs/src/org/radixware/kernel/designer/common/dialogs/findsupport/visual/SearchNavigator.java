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

package org.radixware.kernel.designer.common.dialogs.findsupport.visual;

import java.util.List;
import org.radixware.kernel.designer.common.dialogs.findsupport.AbstractFinder;
import org.radixware.kernel.designer.common.dialogs.findsupport.FindResult;
import org.radixware.kernel.designer.common.dialogs.findsupport.IFinder;


class SearchNavigator {

    private IDocumentController controller;
    private IFinder finder;
    private FindResult result;

    public SearchNavigator() {
        this(null, null);
    }

    public SearchNavigator(IFinder finder, IDocumentController controller) {
        setDocumentController(controller);
        setFinder(finder);
    }

    public final FindResult find() {
        return result = finder.find();
    }

    public final FindResult find(boolean move) {
        return result = finder.find(move);
    }

    public final FindResult findNext(boolean move) {
        return result = finder.findNext(move);
    }

    public final FindResult findBack(boolean move) {
        return result = finder.findBack(move);
    }

    public final List<FindResult> findAll() {
        return finder.findAll();
    }

    public final FindResult getResult() {
        return result;
    }

    public final void clearResult() {
        result = FindResult.EMPTY_RESULT;
    }

    public final IFinder getFinder() {
        return finder;
    }

    public final void setFinder(IFinder finder) {
        this.finder = finder != null ? finder : AbstractFinder.EMPTY_FINDER;
    }

    public final boolean isFinderSet() {
        return finder != null && finder.isValid();
    }

    public final IDocumentController getDocumentController() {
        return controller;
    }

    public final void setDocumentController(IDocumentController controller) {
        this.controller = controller != null ? controller : DocumentController.EMPTY_DOCUMENT_CONTROLLER;
    }

    public final boolean isDocumentControllerSet() {
        return controller != null && !controller.isEmpty();
    }

    public void select() {
        select(getResult());
    }

    public final boolean isFind() {
        return result != null && result.isFound();
    }

    public final void select(FindResult result) {
        if (isDocumentControllerSet()) {
            if (result != null && result.isFound()) {
                controller.select(result.first, result.last);
            } else {
                controller.unselect();
            }
        }
    }
}
