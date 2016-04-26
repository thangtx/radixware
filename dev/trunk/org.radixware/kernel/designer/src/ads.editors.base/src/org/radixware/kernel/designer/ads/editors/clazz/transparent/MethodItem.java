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

package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.ClassMethodItem;


final class MethodItem extends ClassMethodItem implements IPresentable {

    private final ClassMemberPresenter presenter;

    public MethodItem(Method method, Definition context, ClassMemberPresenter presenter, RadixPlatformClass platformClass) {
        super(method, context, platformClass);

        this.presenter = presenter;
    }

    public MethodItem(Method method, AdsClassDef context, ClassMemberPresenter presenter) {
        super(method, context);

        this.presenter = presenter;
    }

    @Override
    public ClassMemberPresenter getPresenter() {
        return presenter;
    }
}
