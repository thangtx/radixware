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
package org.radixware.kernel.designer.environment.merge;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.netbeans.api.diff.Diff;
import org.netbeans.api.diff.DiffView;
import org.netbeans.api.diff.StreamSource;
import org.openide.DialogDescriptor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public final class DiffManager {

    static public void diff(final List<String> s1, final List<String> s2, final List<String> tabTitles, final List<String> title1, final List<String> title2, final String mainTitle) {
        diff(s1, s2, tabTitles, title1, title2, mainTitle, new Object[]{DialogDescriptor.CLOSED_OPTION});
    }

    static public boolean diff(final List<String> s1, final List<String> s2, final List<String> tabTitles, final List<String> title1, final List<String> title2, final String mainTitle, Object options[]) {
        try {
            JPanel p = new JPanel();
            ModalDisplayer md = new ModalDisplayer(p, mainTitle, options);
            p.setLayout(new BorderLayout());
            JTabbedPane tabSet = new JTabbedPane();
            p.add(tabSet, BorderLayout.CENTER);
            for (int i = 0; i < tabTitles.size(); i++) {
                JPanel p2 = new JPanel();
                tabSet.add(tabTitles.get(i), p2);
                final StreamSource local = StreamSource.createSource("name1", title1.get(i), "text/xml", new StringReader(s1.get(i)));
                final StreamSource remote = StreamSource.createSource("name2", title2.get(i), "text/xml", new StringReader(s2.get(i)));
                DiffView view = Diff.getDefault().createDiff(local, remote);
                p2.setLayout(new BorderLayout());
                p2.add(view.getComponent(), BorderLayout.CENTER);
            }
            return md.showModal();
        } catch (IOException ex) {
            MergeUtils.messageError(ex);
            return false;
        }

    }

    static public void diff(final String s1, final String s2, final String title1, final String title2, final String mainTitle) {
        diff(s1, s2, title1, title2, mainTitle, new Object[]{DialogDescriptor.CLOSED_OPTION});

    }

    static public boolean diff(final String s1, final String s2, final String title1, final String title2, final String mainTitle, Object options[]) {

        final StreamSource local = StreamSource.createSource("name1", title1, "text/xml", new StringReader(s1));
        final StreamSource remote = StreamSource.createSource("name2", title2, "text/xml", new StringReader(s2));

        try {
            DiffView view = Diff.getDefault().createDiff(local, remote);
            JPanel p = new JPanel();

            ModalDisplayer md = new ModalDisplayer(p, mainTitle, options);
            p.setLayout(new BorderLayout());
            p.add(view.getComponent(), BorderLayout.CENTER);
            return md.showModal();
        } catch (IOException ex) {
            MergeUtils.messageError(ex);
            return false;
        }
    }
}
