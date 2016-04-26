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

package org.radixware.kernel.server.widgets;

public final class Label extends javax.swing.JLabel {
	public Label (){
		super();
	}
	public Label (final String text){
		super(text);
	}

	private static final long serialVersionUID = 7948164433442436804L;

	@Override
	public void setText(String text){
		int mnemonicPos = -1;
		if (text != null && text.length() != 0){
			mnemonicPos = text.indexOf('&');
			if (mnemonicPos>-1 && mnemonicPos < text.length()-1){
				text = text.substring(0, mnemonicPos) + text.substring(mnemonicPos+1);
			}else
				mnemonicPos = -1;
		}
		super.setText(text);
		setDisplayedMnemonicIndex(mnemonicPos);
	}
}
