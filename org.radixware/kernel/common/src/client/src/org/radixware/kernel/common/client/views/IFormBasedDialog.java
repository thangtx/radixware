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

package org.radixware.kernel.common.client.views;

import java.util.LinkedList;
import java.util.List;


public interface IFormBasedDialog {

    public interface IFormListener {

        public void opened(IFormBasedDialog form);

        public void closed(IFormBasedDialog form);
    }

    public class FormEventSupport implements IFormListener {

        private final IFormBasedDialog form;
        private final List<IFormListener> listeners = new LinkedList<IFormListener>();

        public FormEventSupport(IFormBasedDialog form) {
            this.form = form;
        }

        public void opened() {
            opened(form);
        }

        public void closed() {
            closed(form);
        }

        @Override
        public void closed(IFormBasedDialog form) {
            synchronized (listeners) {
                for (IFormListener l : listeners) {
                    l.closed(form);
                }
            }
        }

        @Override
        public void opened(IFormBasedDialog form) {
            synchronized (listeners) {
                for (IFormListener l : listeners) {
                    l.opened(form);
                }
            }
        }

        public void addFormListener(IFormListener l) {
            synchronized (listeners) {
                if (!listeners.contains(l)) {
                    listeners.add(l);
                }
            }
        }

        public void removeFormListener(IFormListener l) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }

        public void disconnect() {
            synchronized (listeners) {
                listeners.clear();
            }
        }
    }
}
