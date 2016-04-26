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
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.designer.common.dialogs.utils.AdsClassMembersUtils;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;


final class MethodPresenter extends ClassMemberPresenter<AdsMethodDef, RadixPlatformClass.Method> {

    public MethodPresenter(AdsMethodDef source, RadixPlatformClass.Method platformMethod, AdsClassDef context, boolean published) {
        super(source, platformMethod, context, published, new AdsClassMembersUtils.TransparentMethodInfo(platformMethod, context));
    }

    public MethodPresenter(RadixPlatformClass.Method platformMethod, AdsClassDef context, boolean published) {
        this(null, platformMethod, context, published);
    }

    public MethodPresenter(AdsMethodDef adsMethod) {
        super(adsMethod, null, adsMethod.getOwnerClass(), true, null);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    void apply(Link link, AdsClassDef owner) {

        if (owner == null || getState() == null) {
            return;
        }

        switch (getState()) {
            case REPLACE:
                if (link != null) {
                    PublicationUtils.setupTransparentMethodDef((AdsTransparentMethodDef) getSource(),
                        (RadixPlatformClass.Method) link.getLinkPresenter().getPlatformSource(), owner, isSaveNames());
                }
                break;
            case PUBLISH:
                final AdsTransparentMethodDef method = getPlatformSource().isConstructor()
                    ? AdsTransparentMethodDef.Factory.newConstructorInstance()
                    : AdsTransparentMethodDef.Factory.newInstance();

                PublicationUtils.setupTransparentMethodDef(method, getPlatformSource(), owner, false);
                owner.getMethods().getLocal().add(method);
                break;
            case DELETE:
                getSource().delete();
                break;
        }
    }

    @Override
    public boolean isCorrect() {
        return getSource() == null || getPlatformSource() != null;
    }

    @Override
    public String getName() {
        if (memberInfo != null) {
            return super.getName();
        }
        if (getSource() != null) {
            return getSource().getProfile().getName();
        }
        return "<Not Defined>";
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
    public boolean isApplicable(ClassMemberPresenter<AdsMethodDef, Method> target) {
        final Method platformSource = getPlatformSource();
        if (platformSource != null && target.getPlatformSource() != null) {
            return target.isCorrect() && platformSource.isConstructor() == target.getPlatformSource().isConstructor();
        }
        return super.isApplicable(target);
    }

    @Override
    public boolean isChangeable() {
        //RADIX-6841
//        final Method platformSource = getPlatformSource();
//        if (platformSource != null) {
//            return !getPlatformSource().isConstructor();
//        }
        return true;
    }
}
