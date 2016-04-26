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

package org.radixware.kernel.designer.tree.ads.nodes.defs.clazz.presentation;

import java.awt.Color;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.designer.common.annotations.registrators.HtmlNameSupportFactoryRegistration;
import org.radixware.kernel.designer.common.general.displaying.HtmlNameSupport;
import org.radixware.kernel.designer.common.general.displaying.IHtmlNameSupportFactory;


public class EditorPagesOrderedPageHtmlNameSupport extends HtmlNameSupport {

    @HtmlNameSupportFactoryRegistration
    public static final class Factory implements IHtmlNameSupportFactory {

        @Override
        public HtmlNameSupport newInstance(RadixObject object) {
            return new EditorPagesOrderedPageHtmlNameSupport(object);
        }
    }

    public EditorPagesOrderedPageHtmlNameSupport(RadixObject object) {
        super(object);
    }

    private OrderedPage getOrderedPage() {
        return (OrderedPage) getRadixObject();
    }

    @Override
    public Color getColor() {
        AdsEditorPageDef page = getOrderedPage().findPage();
        if (page == null) {
            return Color.RED;
        } else {
            return page.getOwnerDef() == getOrderedPage().getOwnerDefinition() ? super.getColor() : Color.GRAY;
        }

    }
}
