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
import org.radixware.kernel.common.client.models.FormModel;


public interface IFormView extends IView, IDialog, IFormBasedDialog {

    public interface IFormListener extends IViewListener<IFormView> {
    }

    public class FormEventSupport implements IFormListener {

        private final IFormView form;
        private final List<IFormListener> listeners = new LinkedList<IFormListener>();

        public FormEventSupport(IFormView form) {
            this.form = form;
        }

        public void opened() {
            opened(form);
        }

        public void closed() {
            closed(form);
        }

        @Override
        public void closed(IFormView form) {
            synchronized (listeners) {
                for (IFormListener l : listeners) {
                    l.closed(form);
                }
            }
        }

        @Override
        public void opened(IFormView form) {
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

    public void open(FormModel model);

    public boolean submit();

    public boolean isUpdatesEnabled();

    public void setUpdatesEnabled(boolean enable);

    public void hide();

    public void show();

    public void done(FormModel.FormResult result);

    public boolean close();

    public FormModel.FormResult formResult();

    public void addFormListener(IFormListener l);

    public void removeFormListener(IFormListener l);
}
