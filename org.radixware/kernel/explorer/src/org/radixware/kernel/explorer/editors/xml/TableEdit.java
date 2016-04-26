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

package org.radixware.kernel.explorer.editors.xml;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.gui.QTextEdit;


public class TableEdit extends QTextEdit {

	public TableEdit(String value){
		this.setText(value);
		Alignment a = new Alignment();
		a.set(Qt.AlignmentFlag.AlignLeft);
		this.setAlignment(a);
		this.setFrameShape(Shape.NoFrame);
		this.setReadOnly(true);
	}
	
}
