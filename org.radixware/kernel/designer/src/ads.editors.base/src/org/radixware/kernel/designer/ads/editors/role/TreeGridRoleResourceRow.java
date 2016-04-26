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

package org.radixware.kernel.designer.ads.editors.role;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComboBox;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.ExplorerItemsOrder;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef.RoleResourcesCash;
import org.radixware.kernel.common.enums.EDrcPredefinedRoleId;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridModel;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridRow;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTable;


public class TreeGridRoleResourceRow extends TreeGridRow {

    private Icon icon;
    String title;
    TreeGridRoleResourceRow parent;
    AdsRoleDef role;
    String resHash;
    Id parentId;
    TreeTable tbl;
    AdsExplorerItemDef explorerItem;
    boolean isMayChild;
    List<TreeGridRoleResourceRow> list = null;
    AdsRoleEditorPanel.CurrParagraphRightChecker checker;
    JComboBox comboBox;
    Id incorrectId = null;
    //Map<Id, AdsParagraphExplorerItemDef> allExplorerItemsMap=null;
    RoleResourcesCash allOverwriteOptions;

    public String getExplorerItemTitle() {

        return getExplorerItemTitle(allOverwriteOptions, explorerItem);
    }

    static public String getExplorerItemTitle(RoleResourcesCash allOverwriteOptions, AdsExplorerItemDef explorerItem) {
        if (explorerItem == null) {
            return null;
        }
        String title_ = null;
        if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) explorerItem;
            if (def.isTitleInherited()) {
                AdsParagraphExplorerItemDef par = allOverwriteOptions != null ? allOverwriteOptions.findParagraph(def.getReferencedParagraphId()) : def.findReferencedParagraph();

                if (par != null) {
                    title_ = par.getLocalizedStringValue(EIsoLanguage.ENGLISH, par.getTitleId());
                }
            } else {
                title_ = def.getLocalizedStringValue(EIsoLanguage.ENGLISH, def.getTitleId());
            }

        } else if (explorerItem instanceof AdsSelectorExplorerItemDef) {
            AdsSelectorExplorerItemDef def = (AdsSelectorExplorerItemDef) explorerItem;
            if (def.isTitleInherited()) {
                AdsSelectorPresentationDef refref = def.findReferencedSelectorPresentation().get();
                if (refref != null) {
                    title_ = refref.getLocalizedStringValue(EIsoLanguage.ENGLISH, refref.getTitleId());
                }
            } else {
                title_ = def.getLocalizedStringValue(EIsoLanguage.ENGLISH, def.getTitleId());
            }
        }
        if (title_ == null || title_.isEmpty()) {
            title_ = explorerItem.getName();
        }
        return title_;
    }

    public void setInheritOrAllow(boolean isInherit) {
        if (isInherit) {
            role.RemoveResourceRestrictions(resHash);
        } else {
            role.CreateOrReplaceResourceRestrictions(
                    new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM,
                    parentId,
                    parentId == explorerItem.getId() ? null : explorerItem.getId(),
                    Restrictions.Factory.newInstance(0))//ERestriction.ACCESS.getValue().longValue()
                    );
        }

        loadValues();
        List<? extends TreeGridRow> lst = getChilds();
        if (lst != null) {
            for (TreeGridRow curr : lst) {
                TreeGridRoleResourceRow item = (TreeGridRoleResourceRow) curr;
                item.setInheritOrAllow(isInherit);
            }
        }

    }

    @Override
    public void loadValues() {
        if (row != null) {
            if (!role.getOverwriteAndAncestordResourceRestrictions(resHash, null).isDenied(ERestriction.ACCESS)) {
                row.getValues()[1] = AdsRoleEditorPanel.Allowed;
            } else {
                row.getValues()[1] = AdsRoleEditorPanel.Forbidden;
            }




            Restrictions r = role.getOnlyCurrentResourceRestrictions(resHash);
            if (r == null) {
                row.getValues()[2] = AdsRoleEditorPanel.Inherit;

            } else if (!r.isDenied(ERestriction.ACCESS)) {
                row.getValues()[2] = AdsRoleEditorPanel.Allowed;
                row.getValues()[4] = Boolean.TRUE;
            } else {
                row.getValues()[2] = AdsRoleEditorPanel.Forbidden;
            }


            if (!role.getResourceRestrictions(resHash, null).isDenied(ERestriction.ACCESS)) {
                row.getValues()[3] = AdsRoleEditorPanel.Allowed;
                row.getValues()[4] = Boolean.TRUE;
            } else {
                row.getValues()[3] = AdsRoleEditorPanel.Forbidden;
                row.getValues()[4] = Boolean.FALSE;
            }
            if (role.getId().equals(Id.Factory.loadFrom(EDrcPredefinedRoleId.SUPER_ADMIN.getValue()))) {
                row.getValues()[4] = Boolean.TRUE;
            }

            row.setValues(row.getValues());
        }
    }

    @Override
    public final String getName() {
        if (explorerItem != null) {
            return explorerItem.getName();
        }
        if (incorrectId != null) {
            return "#" + incorrectId.toString();
        }
        return null;
    }
    private TreeGridModel.TreeGridNode row = null;

    @Override
    public TreeGridModel.TreeGridNode getRowEx() {
        return row;
    }

    @Override
    public void setRowEx(TreeGridModel.TreeGridNode row) {
        this.row = row;
    }

    public TreeGridRoleResourceRow(RoleResourcesCash allOverwriteOptions, JComboBox comboBox, AdsRoleEditorPanel.CurrParagraphRightChecker checker, TreeTable tbl, Id parentId, AdsRoleDef role, AdsExplorerItemDef explorerItem, TreeGridRoleResourceRow parent) {
        this.allOverwriteOptions = allOverwriteOptions;
        this.explorerItem = explorerItem;
        initItem(comboBox, checker, tbl, parentId, role, parent);

    }

    public TreeGridRoleResourceRow(RoleResourcesCash allOverwriteOptions, JComboBox comboBox, AdsRoleEditorPanel.CurrParagraphRightChecker checker, TreeTable tbl, Id parentId, AdsRoleDef role, Id incorrectId, TreeGridRoleResourceRow parent) {
        this.allOverwriteOptions = allOverwriteOptions;
        this.incorrectId = incorrectId;
        initItem(comboBox, checker, tbl, parentId, role, parent);

    }

    private void initItem(JComboBox comboBox, AdsRoleEditorPanel.CurrParagraphRightChecker checker, TreeTable tbl, Id parentId, AdsRoleDef role, TreeGridRoleResourceRow parent) {
        this.tbl = tbl;
        this.role = role;
        this.parent = parent;
        this.parentId = parentId;

        this.checker = checker;
        this.comboBox = comboBox;

        this.resHash =
                AdsRoleDef.generateResHashKey(
                EDrcResourceType.EXPLORER_ROOT_ITEM,
                parentId,
                explorerItem == null ? incorrectId
                : parentId == explorerItem.getId() ? null : explorerItem.getId());



        if (explorerItem instanceof AdsParagraphExplorerItemDef) {
            AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) explorerItem;
            isMayChild = par.getExplorerItems().getChildren().get(EScope.ALL).size() > 0;
        } else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) explorerItem;
            AdsParagraphExplorerItemDef par2 = allOverwriteOptions != null ? allOverwriteOptions.findParagraph(def.getReferencedParagraphId()) : def.findReferencedParagraph();
            if (par2 != null) {
                isMayChild = par2.getExplorerItems().getChildren().get(EScope.ALL).size() > 0;
            } else {
                isMayChild = false;
            }
        } else {
            isMayChild = false;
        }
        title = getName();

        if (explorerItem != null) {
            icon = explorerItem.getIcon().getIcon();
        }
    }

    @Override
    public Icon getIcon(int row) {
        return icon;
    }

    @Override
    public boolean isHaveChilds() {
        return isMayChild;
    }

    @Override
    public List<? extends TreeGridRow> getChilds() {
        if (!isMayChild) {
            return null;
        }
        if (list == null) {
            list = new ArrayList(0);
            if (explorerItem instanceof AdsParagraphExplorerItemDef) {
                AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef) explorerItem;
                List<AdsExplorerItemDef> items = par.getExplorerItems().getChildren().get(EScope.ALL);
                for (AdsExplorerItemDef item : items) {
                    list.add(new TreeGridRoleResourceRow(allOverwriteOptions, comboBox, checker, tbl, parentId, role, item, this));
                }
            } else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
                AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) explorerItem;
                AdsParagraphExplorerItemDef par2 = allOverwriteOptions != null ? allOverwriteOptions.findParagraph(def.getReferencedParagraphId()) : def.findReferencedParagraph();
                if (par2 != null) {
                    List<AdsExplorerItemDef> items = par2.getExplorerItems().getChildren().get(EScope.ALL);
                    for (AdsExplorerItemDef item : items) {
                        list.add(new TreeGridRoleResourceRow(allOverwriteOptions, comboBox, checker, tbl, parentId, role, item, this));
                    }
                }
            }
        }


        AdsParagraphExplorerItemDef par = null;
        if (explorerItem instanceof AdsParagraphExplorerItemDef) {
            par = (AdsParagraphExplorerItemDef) explorerItem;
            isMayChild = par.getExplorerItems().getChildren().get(EScope.ALL).size() > 0;
        } else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef) {
            AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef) explorerItem;
            par = allOverwriteOptions != null ? allOverwriteOptions.findParagraph(def.getReferencedParagraphId()) : def.findReferencedParagraph();
        }
        if (par != null) {
            AdsParagraphExplorerItemDef root = par.findOwnerExplorerRoot();
            if (root != null) {
                ExplorerItemsOrder order = root.getExplorerItems().getItemsOrder();

                final List<ExplorerItemsOrder.OrderedItem> items = order.listOrderItems(par.getId());
                if (!items.isEmpty()) {

                    int lastUpperIndex = 0;
                    for (ExplorerItemsOrder.OrderedItem item : items) {
                        final Id id = item.getExplorerItemId();
                        for (int i = lastUpperIndex; i < list.size(); i++) {
                            if (list.get(i).explorerItem != null && id.equals(list.get(i).explorerItem.getId())) {
                                TreeGridRoleResourceRow tmp = list.get(i);
                                list.set(i, list.get(lastUpperIndex));
                                list.set(lastUpperIndex, tmp);
                                lastUpperIndex++;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return list;
    }

    @Override
    public String getTitle(int column) {
        if (column == 0) {
            return title;
        }
        return null;
    }

    @Override
    public boolean isMayModify(int column) {

        if (comboBox.getItemCount() == 3) {
            comboBox.removeItemAt(2);
        }
        Restrictions or = role.getOverwriteResourceRestrictions(resHash, null);
        if (or.isDenied(ERestriction.ACCESS)) {
            comboBox.addItem(AdsRoleEditorPanel.Forbidden);
        }
        //Inherit;Allowed;Forbidden;
        if (column == 0) {
            return true;
        }
        return !role.isReadOnly()
                && !role.getId().equals(Id.Factory.loadFrom(EDrcPredefinedRoleId.SUPER_ADMIN.getValue()))
                && column != 1 && column != 3;
    }

    @Override
    public void CellWasModified(int column, Object val) {
        if (column == 2) {
            if (val == AdsRoleEditorPanel.Inherit) {
                //role.RemoveResourceRestrictions(resHash);
                //this.getRowEx().getValues()[3] = this.getRowEx().getValues()[1];
                clearChildsResources(true);
            } else if (val == AdsRoleEditorPanel.Forbidden) {
                clearChildsResources(false);
                role.CreateOrReplaceResourceRestrictions(
                        new AdsRoleDef.Resource(
                        EDrcResourceType.EXPLORER_ROOT_ITEM,
                        parentId,
                        explorerItem == null ? incorrectId
                        : (parentId == explorerItem.getId() ? null : explorerItem.getId()),
                        Restrictions.Factory.newInstance(ERestriction.ACCESS.getValue().longValue())));
                this.getRowEx().getValues()[2] = val;
                this.getRowEx().getValues()[3] = val;
            } else// if (val == AdsRoleEditorPanel.Allowed)
            {

                TreeGridRoleResourceRow currCover = this;
                while (currCover != null) {
                    currCover.getRowEx().getValues()[2] = val;
                    currCover.getRowEx().getValues()[3] = val;

                    role.CreateOrReplaceResourceRestrictions(
                            new AdsRoleDef.Resource(
                            EDrcResourceType.EXPLORER_ROOT_ITEM,
                            parentId,
                            explorerItem == null ? incorrectId
                            : (parentId == currCover.explorerItem.getId() ? null : currCover.explorerItem.getId()),
                            Restrictions.Factory.newInstance(0)));
                    currCover = currCover.parent;
                }
                /*
                 * role.CreateOrReplaceResourceRestrictions( new
                 * AdsRoleDef.Resource( EDrcResourceType.EXPLORER_ROOT_ITEM,
                 * parentId, parentId == explorerItem.getId() ? null :
                 * explorerItem.getId(), Restrictions.Factory.newInstance(0) )
                 * ); this.getRowEx().getValues()[3] = val;
                 */
            }
            this.getRowEx().setValues(this.getRowEx().getValues());

        } else if (column == 4) {
            if (val.equals(Boolean.TRUE)) {
                TreeGridRoleResourceRow currCover = this;
                while (currCover != null) {
                    currCover.getRowEx().getValues()[4] = val;

                    role.CreateOrReplaceResourceRestrictions(
                            new AdsRoleDef.Resource(
                            EDrcResourceType.EXPLORER_ROOT_ITEM,
                            parentId,
                            explorerItem == null ? incorrectId
                            : parentId == currCover.explorerItem.getId() ? null : currCover.explorerItem.getId(),
                            Restrictions.Factory.newInstance(0)));
                    currCover = currCover.parent;
                }
                //this.getRowEx().getValues()[3] = val;
            } else {
                //role.RemoveResourceRestrictions(resHash);
                clearChildsResources(true);
                //this.getRowEx().getValues()[3] = this.getRowEx().getValues()[1];

            }

        }
        //if (parentId == explorerItem.getId())
        checker.check();
        tbl.repaint();
    }
    //this.c

    private void clearChildsResources(boolean isInherit) {
        if (getRowEx() == null) {
            return;
        }
        getRowEx().getValues()[2] = isInherit ? AdsRoleEditorPanel.Inherit : AdsRoleEditorPanel.Forbidden;
        getRowEx().getValues()[3] = isInherit ? getRowEx().getValues()[1]
                : AdsRoleEditorPanel.Forbidden;
        getRowEx().getValues()[4] = Boolean.FALSE;
        //List<TreeGridRoleResourceRow> lst = list;

        if (isInherit) {
            role.RemoveResourceRestrictions(resHash);
        } else {
            role.CreateOrReplaceResourceRestrictions(
                    new AdsRoleDef.Resource(
                    EDrcResourceType.EXPLORER_ROOT_ITEM, parentId, explorerItem.getId(), Restrictions.Factory.newInstance(0xffffffff)));
        }
        if (list == null) {
            return;
        }

        for (TreeGridRoleResourceRow currRow : list) {
            currRow.clearChildsResources(isInherit);
        }
    }

    @Override
    public Font getFont(int row) {

        Font f;
        if (!role.getResourceRestrictions(resHash, null).isDenied(ERestriction.ACCESS)) {
            f = new Font("Courier New", Font.BOLD, 12);
        } //Tahoma
        //
        else {
            f = new Font("Courier New", Font.PLAIN, 12);
        }
        //Areal
        return f;
    }

    @Override
    public Color getForeground(int row) {

        if (incorrectId != null) {
            return Color.RED;
        }

        Restrictions r = role.getOnlyCurrentResourceRestrictions(resHash);

        if (r == null) {
            Restrictions or = role.getOverwriteResourceRestrictions(resHash, null);
            if (!or.isDenied(ERestriction.ACCESS)) {
                return Color.BLUE;
            }
            return Color.GRAY;
        }
        return Color.BLACK;

    }
    /*
     * @Override public Color getBackground(int row) { return Color.PINK;
     * //return Color.lightGray; }
     */
}
