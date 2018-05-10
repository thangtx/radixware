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

package org.radixware.wps;

import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.wps.HttpQuery;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.IMainView;
import org.radixware.wps.rwt.Label;
import org.radixware.wps.rwt.PushButton;
import org.radixware.wps.rwt.RootPanel;


public class DlgStackTestRootPanel extends RootPanel {

    private final WpsEnvironment env;

    public DlgStackTestRootPanel(WpsEnvironment env) {
        this.env = env;
    }

    @Override
    protected IDialogDisplayer getDialogDisplayer() {
        return env.getDialogDisplayer();
    }

    @Override
    public IMainView getExplorerView() {
        return null;
    }

    @Override
    public void closeExplorerView() {
    }

    @Override
    protected Runnable componentRendered(HttpQuery query) {
        return new Runnable() {
            @Override
            public void run() {
                createTestUI();
            }
        };
    }

    private class TestDialog extends Dialog {

        private int dept = 0;

        public TestDialog(int dept) {
            super(String.valueOf(dept));
            this.dept = dept;
            Container c = new Container();
            Label label = new Label();
            label.setText("<html><div>"
                    + "<div> aaa </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>"
                    + "<div> bbb </div>" + "<div> bbb </div>"
                    + "</<div></html>");

            final PushButton b = new PushButton("Click Me");
            b.addClickHandler(new IButton.ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    new TestDialog(TestDialog.this.dept + 1).execDialog(b.getParent());
                }
            });
            add(b);
            add(c);
            c.getAnchors().setRight(new Anchors.Anchor(1, 0));
            c.getAnchors().setBottom(new Anchors.Anchor(1, 0));
            c.getHtml().setCss("overflow", "auto");
            c.add(label);

        }
    }

    private void createTestUI() {
        new TestDialog(0).execDialog();

    }
}
