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
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridRow;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTable;



public class TreeGridRoleResourceEdPresentationExplorerItemRow extends TreeGridRoleResourceCommonRow{
    
    AdsExplorerItemDef explorerItem;
    
    List<TreeGridRoleResourceEdPresentationExplorerItemRow> list = null;
    

    public String getExplorerItemTitle(){
        return TreeGridRoleResourceRow.getExplorerItemTitle(null, explorerItem);
    }
    
    
    @Override
    public void loadValues()
    {
        Boolean arr[] = new Boolean[5];

        Id id = explorerItem==null ? incorrectId : explorerItem.getId();
        if (row!=null)
        {


            
            boolean isRight = false;

            if (res!=null)
            {
                if (res.isDenied(ERestriction.ACCESS))
                {
                    arr[2] =
                    arr[4] = false;
                }
                else
                {
                    if (!res.isDenied(ERestriction.ANY_CHILD))
                    {
                        arr[4] = arr[2] = true;
                    }
                    else
                    {
                        arr[4] = arr[2] = res.isChildEnabled(id);
                    }
                }
            }
            else
            {
                Restrictions ar = role.getOverwriteAndAncestordResourceRestrictions(resHash, presentation);
                if (ar.isDenied(ERestriction.ACCESS))
                {
                    arr[1] = false;
                }
                else if (!ar.isDenied(ERestriction.ANY_CHILD))
                {
                    isRight = arr[1] = true;

                }
                else
                {
                List<Id> lst = ar.getEnabledChildIds();
                if (lst==null)
                    lst = new ArrayList<>();
                isRight = arr[1] = lst.contains(id);
                }

                Restrictions r = role.getOnlyCurrentResourceRestrictions(resHash);
                if (r == null)
                {
                    arr[4] = arr[2] = false;
                    arr[3] = isRight;
                }
                else if (r.isDenied(ERestriction.ACCESS))
                {
                    arr[4] = arr[2] = arr[3] = false;
                }
                else if (!r.isDenied(ERestriction.ANY_CHILD))
                {
                    arr[4] = arr[2] = arr[3] = true;
                }
                else
                {
                List<Id> lst = r.getEnabledChildIds();
                if (lst==null)
                    lst = new ArrayList<>();
                arr[4] = arr[2] = arr[3] = lst.contains(id);
                }
            }
            row.setValues(arr);
        }
    }

        @Override
        public String getName() {
            if (explorerItem!=null)
                return explorerItem.getName();                
            return "#" + incorrectId.toString();
        }

   
   public void setRestriction(Restrictions res)
   {
       this.res = res;
       if (list!=null)
           for (TreeGridRoleResourceEdPresentationExplorerItemRow item : list)
           {
               item.setRestriction(res);
           }
   }
   public TreeGridRoleResourceEdPresentationExplorerItemRow(
           Restrictions res,
           TreeTable tbl, 
           String hash, AdsRoleDef role,
           AdsExplorerItemDef explorerItem,
           AdsEditorPresentationDef presentation
           )
    {
       this.explorerItem = explorerItem;
       initAll(res, tbl, hash, role, presentation);
    }
   public TreeGridRoleResourceEdPresentationExplorerItemRow(
           Restrictions res,
           TreeTable tbl,
           String hash, AdsRoleDef role,           
           AdsEditorPresentationDef presentation,
           Id incorrectId
           )
    {
       this.incorrectId = incorrectId;
       initAll(res, tbl, hash, role, presentation);
    }



   private void initAll (
           Restrictions res,
           TreeTable tbl,
           String hash, AdsRoleDef role,
           AdsEditorPresentationDef presentation
           )
    {

        this.res = res;
        this.tbl = tbl;
        this.role = role;
        this.hash = hash;        
        this.presentation = presentation;
        this.resHash = hash;        
        if (explorerItem!=null)
        {
            if (explorerItem instanceof AdsParagraphExplorerItemDef)
            {
                AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef)explorerItem;
                isMayChild = par.getExplorerItems().getChildren().get(EScope.ALL).size()>0;
            }
            else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef)
            {
                AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef)explorerItem;
                AdsParagraphExplorerItemDef par2 = def.findReferencedParagraph();
                if (par2!=null)
                    isMayChild = par2.getExplorerItems().getChildren().get(EScope.ALL).size()>0;
                else
                    isMayChild = false;
            }
            else
                isMayChild = false;
            title = getName();
            icon = explorerItem.getIcon().getIcon();
        }
        else{
            icon = null;
            if (incorrectId!=null)
                title = "#" + incorrectId.toString();
        }

   }


         

         
        @Override
    public List<? extends TreeGridRow> getChilds() {
        if (!isMayChild)
             return null;
        if (list==null)
        {
            list = new ArrayList<>(0);
            if (explorerItem instanceof AdsParagraphExplorerItemDef)
            {
                AdsParagraphExplorerItemDef par = (AdsParagraphExplorerItemDef)explorerItem;
                List<AdsExplorerItemDef>items = par.getExplorerItems().getChildren().get(EScope.ALL);                
                for (AdsExplorerItemDef item : items)
                {
                    list.add(new TreeGridRoleResourceEdPresentationExplorerItemRow(res, tbl, hash, role, item, presentation));
                }
            }
            else if (explorerItem instanceof AdsParagraphLinkExplorerItemDef)
            {
                AdsParagraphLinkExplorerItemDef def = (AdsParagraphLinkExplorerItemDef)explorerItem;
                AdsParagraphExplorerItemDef par2 = def.findReferencedParagraph();
                if (par2!=null)
                {
                    List<AdsExplorerItemDef>items = par2.getExplorerItems().getChildren().get(EScope.ALL);                    
                    
                    par2.getExplorerItems().getPlacement();
                    //getPlacement()
                    for (AdsExplorerItemDef item : items)
                    {
                        list.add(new TreeGridRoleResourceEdPresentationExplorerItemRow(res, tbl, hash, role, item, presentation));
                    }
                }
            }
        }
        return list;
    }

         
        @Override
    public boolean isMayModify(int  column) {
            if (column == 0)return true;
            if ( column == 2 || column == 4)
            {
                if (this.explorerItem==null)
                    return true;
                Restrictions r = (res!=null)  ? res : role.getOnlyCurrentResourceRestrictions(resHash);
                if  (r == null)
                    return false;
                if (r.isDenied(ERestriction.ACCESS) || !r.isDenied(ERestriction.ANY_CHILD))
                    return false;
                Restrictions or = role.getOverwriteResourceRestrictions(resHash, presentation);
                List<Id> lst = or.getEnabledChildIds();
                return (lst!=null && !lst.contains(this.explorerItem.getId()));
                //return true;
            }
            return  false;
        }




        @Override
    public void CellWasModified(int column, Object val) {
            if (column!=2 && column!=4) return;
            Boolean flag = (Boolean)val;
            Id id = explorerItem == null ? incorrectId : explorerItem.getId();
            
            if (res!=null)
            {
                res.setChildEnabled(id, flag);
                loadValues();
                tbl.repaint();
                return;
            }
            Restrictions r = role.getOnlyCurrentResourceRestrictions(resHash);
            if (r == null) return;
            r.setChildEnabled(id, flag);
            //AdsRoleDef.ge
            //role.getResources().
            role.CreateOrReplaceResourceRestrictions(new AdsRoleDef.Resource(
                    EDrcResourceType.EDITOR_PRESENTATION,
                    presentation.getOwnerClass().getId(),
                    presentation.getId(), r));
            //Restrictions r2 = role.getOnlyCurrentResourceRestrictions(resHash);
            loadValues();
             tbl.repaint();
        }

    @Override
    public Font getFont(int row) {
        boolean isBold = false;
        Font f;
         if(explorerItem!=null)
         {
            if (res!=null)
            {
                return  new Font("Courier New", Font.PLAIN, 12);
            }
            Restrictions res_ = role.getResourceRestrictions(resHash, presentation);
            if (res_.isDenied(ERestriction.ACCESS))
                {
                    isBold = false;
                }
            else if (!res_.isDenied(ERestriction.ANY_CHILD))
                {
                    isBold = true;
                }
            else{
            List<Id> lst = res_.getEnabledChildIds();
            if (lst  == null)
                lst = new ArrayList<>();
            isBold = lst.contains(explorerItem.getId());
            }
         }

        if (isBold)
            f = new Font("Courier New", Font.BOLD, 12);
        //Tahoma
        //
        else 
            f = new Font("Courier New", Font.PLAIN, 12);
        //Areal
        return f;
        }
    @Override
        public Color getForeground(int row) {
        if (this.explorerItem==null)
        {
            return  Color.RED;
        }
        if (res!=null)
        {
            return  Color.BLACK;
        }
        Restrictions r = role.getOnlyCurrentResourceRestrictions(resHash);

        if (r == null)
        {
            Restrictions or = role.getOverwriteResourceRestrictions(resHash, presentation);
            if (or.isDenied(ERestriction.ACCESS))
            {
                return Color.GRAY;
            }
            if (!or.isDenied(ERestriction.ANY_CHILD))
            {
                return Color.BLUE;
            }
            List<Id> lst = or.getEnabledChildIds();
            if (lst  == null)
                lst = new ArrayList<>();
            if (explorerItem!=null && lst.contains(explorerItem.getId()))
                return Color.BLUE;
            return Color.GRAY;
        }
        return  Color.BLACK;
             
        }
 
/*
    @Override
    public Color getBackground(int row) {
        return Color.PINK;
        //return Color.lightGray;
    }
*/
    }
