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

package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import java.util.Collection;
import java.util.Collections;
import javax.swing.BorderFactory;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.designer.common.dialogs.components.selector.IItemProvider;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionEvent;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionListener;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.PublicationUtils;


public final class PuplishClassMemberDisplayer extends ModalDisplayer implements SelectionListener<ClassMemberItem> {

    private final ClassMemberSelector selector;

    private PuplishClassMemberDisplayer(ClassMemberSelector selector) {
        super(selector.getComponent());

        this.selector = selector;
        selector.addSelectionListener(this);

        getComponent().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    public PuplishClassMemberDisplayer() {
        this(new ClassMemberSelector(false));
    }

    @Override
    public void selectionChanged(SelectionEvent<ClassMemberItem> event) {
        getDialogDescriptor().setValid(event.newValue != null);
    }

    public void open(final AdsClassDef ownerClass, final EClassMemberType memberType) {
        setTitle(NbBundle.getMessage(PublishingClassMemberPanel.class,
            "PublishingClassMemberPanel-Displayer") + " " + String.valueOf(memberType));

        final String classname = ownerClass.getTransparence().getPublishedName();
        final PlatformLib lib = ((AdsSegment) ownerClass.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(ownerClass.getUsageEnvironment());
        final RadixPlatformClass platformClass = lib.findPlatformClass(classname);

        selector.open(new IItemProvider<ClassMemberItem>() {

            @Override
            public Collection<ClassMemberItem> getAllItems() {
                return getItems(memberType);
            }

            @Override
            public Collection<ClassMemberItem> getItems(Object key) {
                if (key instanceof EClassMemberType) {
                    switch ((EClassMemberType) key) {
                        case FIELD:
                            Collection<RadixPlatformClass.Field> fieldList = PublicationUtils.getFields(platformClass, new PublicationUtils.UnpublishedFieldFilter(ownerClass));
                            return ClassFieldItem.createCollection(fieldList, ownerClass, platformClass);
                        case METHOD:
                            Collection<RadixPlatformClass.Method> methodsList = PublicationUtils.getMethods(platformClass, new PublicationUtils.UnpublishedMethodFilter(ownerClass));
                            return ClassMethodItem.createCollection(methodsList, ownerClass, platformClass);
                    }
                }
                return Collections.<ClassMemberItem>emptyList();
            }
        }, (ClassMemberItem) null);
        getDialogDescriptor().setValid(selector.isSelected());
    }

    public ClassMemberItem getItem() {
        return selector.getSelectedItem();
    }

}
