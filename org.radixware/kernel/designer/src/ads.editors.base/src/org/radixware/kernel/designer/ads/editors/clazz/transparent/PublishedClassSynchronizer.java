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

import java.util.Collection;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.EClassMemberType;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.PuplishClassMemberDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;


public final class PublishedClassSynchronizer {

    private PublishedClassSynchronizer() {
    }

    public static synchronized void synchronize(AdsClassDef transparentClass) {
        if (AdsTransparence.isTransparent(transparentClass)) {
            final SynchronizeDisplayer displayer = new SynchronizeDisplayer();
            displayer.open(transparentClass);
            displayer.showModal();
        }
    }

    static ClassMemberPresenter chooseMemberForReplace(Collection<ClassMemberPresenter> allNotPublished, ClassMemberPresenter source) {
        final ReplaceDisplayer displayer = new ReplaceDisplayer();
        displayer.open(allNotPublished, source);

        return displayer.showModal() ? displayer.chooseMemberForReplace() : null;
    }

    public static boolean replace(AdsTransparentMethodDef method) {

        if (method == null) {
            return false;
        }

        final PuplishClassMemberDisplayer displayer = new PuplishClassMemberDisplayer();
        displayer.open(method.getOwnerClass(), EClassMemberType.METHOD);

        if (displayer.showModal()) {

            final Method platformMethod = (Method) displayer.getItem().getSource();

            if (platformMethod != null) {
                method.getProfile().getParametersList().clear();
                method.getProfile().getThrowsList().clear();
                final AdsAccessFlags flags = method.getProfile().getAccessFlags();
                flags.setAbstract(false);
                flags.setStatic(false);
                flags.setFinal(false);
                flags.setDeprecated(false);
                PublicationUtils.setupTransparentMethodDef((AdsTransparentMethodDef) method, platformMethod, method.getOwnerClass(), false);
                return true;
            }
        }
        return false;
    }

}
