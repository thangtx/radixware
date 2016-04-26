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

import javax.swing.Icon;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Properties;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Field;
import org.radixware.kernel.designer.common.dialogs.utils.AdsClassMembersUtils;


final class FieldPresenter extends ClassMemberPresenter<AdsPropertyDef, RadixPlatformClass.Field> {

    public FieldPresenter(AdsPropertyDef source, Field platformSource, Definition context, boolean published) {
        super(source, platformSource, context, published, new AdsClassMembersUtils.TransparentFieldInfo(platformSource, context));
    }

    public FieldPresenter(Field field, AdsClassDef transparentClass, boolean published) {
        this(null, field, transparentClass, published);
    }

    public FieldPresenter(AdsPropertyDef prop) {
        super(prop, null, null, true, null);

    }

    @Override
    public String getName() {
        if (memberInfo != null) {
            return super.getName();
        }

        if (getSource() != null) {
            return getSource().getName();
        }
        return "<Not Defined>";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    void apply(Link link, AdsClassDef owner) {
        Properties properties = owner.getProperties();
        if (getState() == EPresenterState.PUBLISH) {
                AdsTransparentPropertyDef property = AdsTransparentPropertyDef.Factory.newInstance();

                PublicationUtils.setupTransparentFieldDef(property, getPlatformSource());
                properties.getLocal().add(property);
        } else if (getState() == EPresenterState.DELETE && isPublished()) {
            getSource().delete();
        }
    }

    @Override
    public Icon getIcon() {
        if (memberInfo != null) {
            return super.getIcon();
        }
        if (getSource() != null) {
            return getSource().getIcon().getIcon(16, 16);
        }
        return null;
    }

    @Override
    public boolean isApplicable(ClassMemberPresenter<AdsPropertyDef, RadixPlatformClass.Field> target) {
        return false;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }
}
