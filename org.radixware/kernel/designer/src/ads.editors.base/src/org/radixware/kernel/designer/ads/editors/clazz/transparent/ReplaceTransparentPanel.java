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

/*
 * ReplaceFakeTransparentMethodPanel.java
 *
 * Created on 08.07.2009, 17:21:32
 */
package org.radixware.kernel.designer.ads.editors.clazz.transparent;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.ClassMemberItem;
import org.radixware.kernel.designer.ads.editors.clazz.members.transparent.ClassMemberSelector;
import org.radixware.kernel.designer.common.dialogs.components.selector.EFilterPosition;
import org.radixware.kernel.designer.common.dialogs.components.selector.IItemProvider;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemFilter;
import org.radixware.kernel.designer.common.dialogs.components.selector.SelectionListener;


final class ReplaceTransparentPanel extends JPanel {

    private final ClassMemberSelector selector;
    private final SuitableFilter suitableFilter;

    public ReplaceTransparentPanel() {
        initComponents();

        selector = new ClassMemberSelector(false);
        suitableFilter = new SuitableFilter();
        selector.getSelectorLayout().addFilterComponent(EFilterPosition.BOTTOM, suitableFilter);

        selectorPanel.add(selector.getComponent(), BorderLayout.CENTER);
        saveCheck.setSelected(true);
    }

    @Override
    public boolean requestFocusInWindow() {
        return selector.requestFocusInWindow();
    }

    public void open(final Collection<ClassMemberPresenter> allNotPublished, final ClassMemberPresenter source) {
        suitableFilter.setModel(source);

        final IItemProvider<ClassMemberItem> itemProvider = new IItemProvider<ClassMemberItem>() {

            @Override
            public Collection<ClassMemberItem> getAllItems() {
                final Collection<ClassMemberItem> collection = new ArrayList<>();
                for (ClassMemberPresenter presenter : allNotPublished) {
                    if (presenter instanceof MethodPresenter && source.isApplicable(presenter)) {
                        collection.add(new MethodItem((Method) presenter.getPlatformSource(), (AdsClassDef) presenter.getContext(), presenter));
                    }
                }
                return collection;
            }

            @Override
            public Collection<ClassMemberItem> getItems(Object key) {
                return null;
            }
        };

        selector.open(itemProvider, (ClassMemberItem) null);
    }

    ClassMemberPresenter getSelectedPresentation() {
        if (selector.isSelected()) {
            ClassMemberPresenter presenter = ((IPresentable) selector.getSelectedItem()).getPresenter();
            presenter.setSaveNames(saveCheck.isSelected());
            suitableFilter.getModel().setSaveNames(saveCheck.isSelected());
            return presenter;
        }
        return null;
    }

    public void removeSelectionListener(SelectionListener<ClassMemberItem> listener) {
        selector.removeSelectionListener(listener);
    }

    public void addSelectionListener(SelectionListener<ClassMemberItem> listener) {
        selector.addSelectionListener(listener);
    }

    ClassMemberSelector getSelector() {
        return selector;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectorPanel = new javax.swing.JPanel();
        saveCheck = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 4, 4, 4));
        setMinimumSize(new java.awt.Dimension(200, 200));
        setLayout(new java.awt.BorderLayout());

        selectorPanel.setLayout(new java.awt.BorderLayout());
        add(selectorPanel, java.awt.BorderLayout.CENTER);

        saveCheck.setText(org.openide.util.NbBundle.getMessage(ReplaceTransparentPanel.class, "SaveCustomNames")); // NOI18N
        saveCheck.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        add(saveCheck, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox saveCheck;
    private javax.swing.JPanel selectorPanel;
    // End of variables declaration//GEN-END:variables

    private static final class SuitableFilter extends ItemFilter<ClassMemberItem> {

        private final JCheckBox checkBox = new JCheckBox();
        private ClassMemberPresenter model;

        public SuitableFilter() {
            checkBox.setText(NbBundle.getMessage(SuitableFilter.class, "ShowOnlySuitableSignaturesTip"));
            checkBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    fireChange();
                }
            });
        }

        @Override
        public boolean accept(ClassMemberItem value) {
            if (checkBox.isSelected()) {
                if (model instanceof MethodPresenter) {
                    return acceptMethod((MethodPresenter) model, (MethodPresenter) ((IPresentable) value).getPresenter());
                } else if (model instanceof FieldPresenter) {
                    return acceptField((FieldPresenter) model, (FieldPresenter) ((IPresentable) value).getPresenter());
                }
            }
            return true;
        }

        @Override
        public Component getComponent() {
            return checkBox;
        }

        public void setModel(ClassMemberPresenter model) {
            this.model = model;
        }

        public ClassMemberPresenter getModel() {
            return model;
        }
        
        private boolean acceptMethod(MethodPresenter owner, MethodPresenter source) {
            if (owner.getState() != EPresenterState.DELETE && (owner.isPublished() || !owner.isCorrect())) {

                final AdsTypeDeclaration[] normProfile = owner.getSource().getProfile().getNormalizedProfile();
                final AdsTypeDeclaration[] mpProfile = source.getPlatformSource().getParameterTypes();
                final AdsTypeDeclaration[] fullMpProfile = new AdsTypeDeclaration[mpProfile.length];
                for (int a = 0, size = mpProfile.length - 1; a <= size; a++) {
                    fullMpProfile[a] = mpProfile[a];
                }

                if (normProfile.length - 1 == fullMpProfile.length) {
                    for (int i = 0; i < fullMpProfile.length; i++) {
                        final AdsTypeDeclaration mpType = fullMpProfile[i];
                        final AdsTypeDeclaration normType = normProfile[i];

                        if (!equalseTo(owner.getSource(), mpType, normType)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        private boolean acceptField(FieldPresenter owner, FieldPresenter source) {

            if (owner.getState() != EPresenterState.DELETE && (owner.isPublished() || !owner.isCorrect())) {
                final AdsTypeDeclaration typeOwner = owner.getSource().getValue().getType();
                final AdsTypeDeclaration typeSource = source.getPlatformSource().getValueType();

                return equalseTo(owner.getSource(), typeOwner, typeSource);
            }

            return true;
        }

        private boolean equalseTo(AdsDefinition context, AdsTypeDeclaration type1, AdsTypeDeclaration type2) {
            if (type1 == null) {
                return type2 == null;
            }
            if (type2 != null) {
                return type1.equalsTo(context, type2);
            }
            return false;
        }
    }

}
