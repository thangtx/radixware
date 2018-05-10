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

package org.radixware.kernel.designer.ads.editors.pages;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyUsageSupport.PropertyRef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserCommonComponent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectsListModel;


class AvailableAdsEditorPageDefPropertiesList extends RadixObjectChooserCommonComponent {

    private class PagePropertiesRenderer extends AbstractItemRenderer {

        private final Color UNUSED_FG = new Color(30, 160, 30);

        public PagePropertiesRenderer(javax.swing.JList list) {
            super(list);
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean hasFocus) {
            Component c = super.getListCellRendererComponent(list, object, index, isSelected, hasFocus);
            if (object instanceof Id) {
                AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) object).findProperty();
                if (prop != null) {
                    if (!isUsedInEditorPage(prop)
                            && !isSelected) {
                        getNameLabel().setForeground(UNUSED_FG);
                    }
                }
            }
            return c;
        }

        private boolean isUsedInEditorPage(AdsDefinition property) {
            AdsClassDef ownerClass = editorPage.getOwnerClass();
            if (ownerClass instanceof IAdsPresentableClass) {
                if (((IAdsPresentableClass) ownerClass).getPresentations() instanceof EntityObjectPresentations) {
                    EntityObjectPresentations presents = (EntityObjectPresentations) ((IAdsPresentableClass) ownerClass).getPresentations();

                    List<AdsEditorPresentationDef> allEditorPresentations = presents.getEditorPresentations().get(EScope.ALL);
                    for (AdsEditorPresentationDef ep : allEditorPresentations) {
                        List<AdsEditorPageDef> pages = ep.getEditorPages().get(EScope.ALL);
                        for (AdsEditorPageDef pg : pages) {
                            final List<PropertyRef> usedProps = pg.getUsedProperties().get();
                            for (PropertyRef ref : usedProps) {
                                AdsDefinition refProperty = ref.findProperty();
                                if (refProperty != null
                                        && refProperty.equals(property)) {
                                    return true;
                                }
                            }
                        }
                    }
                } else if (((IAdsPresentableClass) ownerClass).getPresentations() instanceof AbstractFormPresentations) {
                    AbstractFormPresentations presents = (AbstractFormPresentations) ((IAdsPresentableClass) ownerClass).getPresentations();
                    List<AdsEditorPageDef> pages = presents.getEditorPages().get(EScope.ALL);
                    for (AdsEditorPageDef pg : pages) {
                        final List<PropertyRef> usedProps = pg.getUsedProperties().get();
                        for (PropertyRef ref : usedProps) {
                            AdsDefinition refProperty = ref.findProperty();
                            if (refProperty != null
                                    && refProperty.equals(property)) {
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }

        @Override
        public String getObjectName(Object object) {
            if (object != null) {
                if (object instanceof Id) {
                    AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) object).findProperty();
                    if (prop != null) {
                        return prop.getName();
                    } else {
                        return "<Unknowkn: " + ((Id) object).toString() + ">";
                    }
                }
            }
            return "<Not Defined>";
        }

        @Override
        public String getObjectLocation(Object object) {
            return "";
        }

        @Override
        public RadixIcon getObjectIcon(Object object) {
            if (object != null) {
                if (object instanceof Id) {
                    AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) object).findProperty();
                    if (prop != null) {
                        return prop.getIcon();
                    }
                }
            }
            return RadixObjectIcon.UNKNOWN;
        }

        @Override
        public RadixIcon getObjectLocationIcon(Object object) {
            return null;
        }
    }
    private AdsEditorPageDef editorPage;

    public AvailableAdsEditorPageDefPropertiesList(AdsEditorPageDef adsEditorPageDef) {
        super();
        list.setCellRenderer(new PagePropertiesRenderer(list));
        this.editorPage = adsEditorPageDef;
        updateContent();
    }

    @Override
    public JComponent getLabelComponent() {
        return new javax.swing.JLabel("Available properties");
    }

    @Override
    public void removeAll(Object[] objects) {
        RadixObjectsListModel model = (RadixObjectsListModel) list.getModel();
        for (Object obj : objects) {
            if (obj == null) {
                continue;
            }
            assert (obj instanceof Id) : (obj == null ? "NULL" : obj.getClass().getName());
            model.removeElement((Id) obj);
        }
    }

    @Override
    public void addAllItems(Object[] objects) {
        RadixObjectsListModel model = (RadixObjectsListModel) list.getModel();
        for (Object obj : objects) {
            if (obj instanceof Id){
                AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) obj).findProperty();
                if (prop != null) {
                    if (prop instanceof IAdsPresentableProperty) {
                        IAdsPresentableProperty presProp = (IAdsPresentableProperty) prop;
                        if (presProp.getPresentationSupport() != null
                                && presProp.getPresentationSupport().getPresentation() != null
                                && presProp.getPresentationSupport().getPresentation().isPresentable()) {
                            model.addElement((Id) obj);
                        }
                    } else {
                        model.addElement((Id) obj);
                    }
                }
            }
        }
    }

    @Override
    public final void updateContent() {
        final PropertyUsageSupport propList = editorPage.getUsedProperties();
        final List<Id> allPropIds = propList.availablePropIds(new IFilter<RadixObject>() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsPropertyDef) {
                    return !((AdsPropertyDef) radixObject).getAccessFlags().isStatic();
                }
                return true;
            }

        });
        final List<Id> usedPropIds = propList.getIds();

        allPropIds.removeAll(usedPropIds);

        List<Id> toRemove = new ArrayList<>();

        for (Id id : allPropIds) {
            AdsDefinition prop = propList.getReference(id).findProperty();
            if (prop instanceof AdsFieldPropertyDef
                    || prop instanceof AdsFieldRefPropertyDef) {
                toRemove.add(id);
            } else {
                if (prop instanceof IModelPublishableProperty) {
                    final AdsTypeDeclaration typeDecl = ((IModelPublishableProperty) prop).getTypedObject().getType();
                    if (typeDecl != null
                            && typeDecl.getTypeId() != null
                            && typeDecl.getTypeId().equals(EValType.USER_CLASS)) {
                        toRemove.add(id);
                    }
                }
                if (!toRemove.contains(id)) {
                    if (prop instanceof IAdsPresentableProperty) {
                        IAdsPresentableProperty presProp = (IAdsPresentableProperty) prop;
                        if (presProp.getPresentationSupport() != null
                                && presProp.getPresentationSupport().getPresentation() != null
                                && !presProp.getPresentationSupport().getPresentation().isPresentable()) {
                            toRemove.add(id);
                        }
                    }
                }
            }
        }
        allPropIds.removeAll(toRemove);
//        final List<AdsDefinition> availableDefs = new ArrayList<AdsDefinition>(allPropIds.size());
//        for (Id id : allPropIds) {
//            final AdsDefinition def = propList.getReference(id).findProperty();
//            if (def != null) {
//                availableDefs.add(def);
//            }
//        }


        RadixObjectsListModel model = new RadixObjectsListModel(allPropIds, editorPage.getUsedProperties());
        list.setModel(model);
    }

    @Override
    public RadixObject getSelectedRadixObject() {
        Object obj = list.getSelectedValue();
        if (obj instanceof Id) {
            AdsDefinition prop = editorPage.getUsedProperties().getReference((Id) obj).findProperty();
            if (prop != null) {
                return prop;
            }
        }
        return null;
    }
}
