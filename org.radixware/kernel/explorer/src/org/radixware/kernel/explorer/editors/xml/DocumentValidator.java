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

import java.util.ArrayList;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

public final class DocumentValidator {
    
	public DocumentValidator(){

	}
	
	static public ArrayList<XmlError> validate (XmlObject xml){
		if (xml!=null){
			XmlOptions validateOptions = new XmlOptions();
			ArrayList<XmlError> errorList = new ArrayList<XmlError>();
			validateOptions.setErrorListener(errorList);
			if (!xml.validate(validateOptions)){
     			return errorList;
    		}
		}
		return null;
	}
}
