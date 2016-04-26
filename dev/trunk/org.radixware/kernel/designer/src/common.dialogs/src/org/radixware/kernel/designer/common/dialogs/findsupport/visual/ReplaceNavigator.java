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

import javax.swing.text.BadLocationException;
import org.radixware.kernel.common.utils.PropertyStore;
import org.radixware.kernel.designer.common.dialogs.findsupport.AbstractFinder;
import org.radixware.kernel.designer.common.dialogs.findsupport.FindResult;
import org.radixware.kernel.designer.common.dialogs.findsupport.IFindCursor;
import org.radixware.kernel.designer.common.dialogs.findsupport.IFinder;


public class ReplaceNavigator extends SearchNavigator {

    private FindResult replaced;

    public ReplaceNavigator() {
        super();
    }

    public ReplaceNavigator(IFinder finder, IDocumentController component) {
        super(finder, component);
    }

    public final boolean replace(FindResult result, CharSequence replace) {
        if (result != null && result.isFound()) {
            if (isDocumentControllerSet()) {
                try {
                    getDocumentController().remove(result.first, result.last);
                    getDocumentController().insert(result.first, replace);
                } catch (BadLocationException ex) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public boolean replace() {
        FindResult result = getResult();
        if (result != replaced && replace(getFinder(), result)) {
            replaced = result;
            return true;
        }
        return false;
    }

    public int replaceAll() {

        if (!isDocumentControllerSet()) {
            return 0;
        }
        IFinder mainFinder = getFinder();
        PropertyStore options = new PropertyStore(mainFinder.getOptions());

        String pattern = options.get(AbstractFinder.Options.SEARCH_PATTER, "");
        String replace = options.get(AbstractFinder.Options.REPLACE_STRING, "");

        if (pattern.equals(replace)) {
            return 0;
        }

        boolean blockSearch = options.get(AbstractFinder.Options.SEARCH_SELECTION, Boolean.FALSE);
        boolean cycleSearch = options.get(AbstractFinder.Options.WRAP_AROUND, Boolean.TRUE);

        options.set(AbstractFinder.Options.WRAP_AROUND, Boolean.FALSE);
        if (!blockSearch && !cycleSearch) {
            options.set(AbstractFinder.Options.CURRENT_POSITION, mainFinder.getCursor().getPosition());
        } else {
            options.remove(AbstractFinder.Options.CURRENT_POSITION);
        }

        ReplaceHelper helper = new ReplaceHelper(mainFinder.createFinder(options));
        getDocumentController().runAtomic(helper);
        return helper.getFindCount();
    }

    private final class ReplaceHelper implements Runnable {

        private final IFinder finder;
        private int findCount = 0;

        public ReplaceHelper(IFinder finder) {
            this.finder = finder;
        }

        @Override
        public void run() {
            FindResult result;

            // primitive loop protection
            int barier = finder.getCursor().getSequence().length();
            while ((result = finder.find()).isFound() && findCount < barier) {
                if (replace(finder, result)) {
                    ++findCount;
                }
            }
        }

        public int getFindCount() {
            return findCount;
        }
    }

    protected final boolean replace(IFinder finder, FindResult result) {
        if (finder != null && result != null && result.isFound()) {
            PropertyStore options = finder.getOptions();

            if (options != null) {
                CharSequence replace = options.get(AbstractFinder.Options.REPLACE_STRING, "").toString();

                if (!replace(result, replace)) {
                    return false;
                }

                correction(options, finder.getCursor());
                updateSequence(finder);

                return true;
            }
        }
        return false;
    }

    private void correction(PropertyStore options, IFindCursor cursor) {
        if (!options.get(AbstractFinder.Options.BACK_SEARCH, Boolean.FALSE)) {

            CharSequence pattern = options.get(AbstractFinder.Options.SEARCH_PATTER, ""),
                replace = options.get(AbstractFinder.Options.REPLACE_STRING, "");

            int sh = replace.length() - pattern.length();

            cursor.move(sh);

            Integer stop = options.get(AbstractFinder.Options.STOP_POSITION, Integer.class);
            if (stop != null) {
                options.set(AbstractFinder.Options.STOP_POSITION, stop + sh);
            }
        }
    }

    private void updateSequence(IFinder finder) {
        CharSequence sequence = getDocumentController().getSequence();

        if (finder.getCursor().getSequence() != sequence) {
            PropertyStore options = finder.getOptions();
            options.set(AbstractFinder.Options.SEQUENCE, sequence);
        }
    }
}
