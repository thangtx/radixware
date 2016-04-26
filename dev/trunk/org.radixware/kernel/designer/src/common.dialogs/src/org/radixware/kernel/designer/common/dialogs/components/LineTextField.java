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

package org.radixware.kernel.designer.common.dialogs.components;

import javax.swing.JTextField;
import javax.swing.text.Document;


public class LineTextField extends JTextField {

    public LineTextField() {
        super(new LineDocument(), null, 0);
    }

    @Override
    public void setDocument(Document doc) {
        super.setDocument(doc);
        if (doc != null) {
            doc.putProperty("filterNewlines", Boolean.FALSE);
        }
    }
}
