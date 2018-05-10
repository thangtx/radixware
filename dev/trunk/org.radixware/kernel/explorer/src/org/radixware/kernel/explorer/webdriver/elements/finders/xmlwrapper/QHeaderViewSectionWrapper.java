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

package org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.explorer.webdriver.elements.HeaderViewSectionInfo;
import org.radixware.kernel.explorer.webdriver.elements.UIElementPropertySet;
import org.radixware.kernel.explorer.webdriver.elements.UIElementReference;
import org.radixware.kernel.explorer.webdriver.elements.WebDrvUIElementsManager;
import org.w3c.dom.Document;

final class QHeaderViewSectionWrapper extends UIElementWrapper{
    
    private final HeaderViewSectionInfo sectionInfo;
    private final UIElementPropertySet props;
    
    public QHeaderViewSectionWrapper(final HeaderViewSectionInfo section, final BaseNodeWrapper parent, final Document document){
        super("Section", parent, document);
        sectionInfo = section;
        props = new UIElementPropertySet(sectionInfo);
    }

    @Override
    public List<UIElementWrapper> getChildElements() {        
        return Collections.emptyList();
    }

    @Override
    public UIElementPropertySet getPropertySet() {
        return props;
    }

    @Override
    public UIElementReference getElementReference(final WebDrvUIElementsManager manager) {
        return sectionInfo.getSectionReference(manager);
    }        

}
