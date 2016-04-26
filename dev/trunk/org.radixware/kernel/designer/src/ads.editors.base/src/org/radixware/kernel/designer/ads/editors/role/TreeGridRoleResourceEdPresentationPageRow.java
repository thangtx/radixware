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
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.ads.common.Restrictions;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.enums.EDrcResourceType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.editors.treeTable.TreeGridRow;
import org.radixware.kernel.designer.common.editors.treeTable.TreeTable;



public class TreeGridRoleResourceEdPresentationPageRow extends TreeGridRoleResourceCommonRow{
    
    
    
    
    AdsEditorPageDef page;
    
    
    
    List<TreeGridRoleResourceEdPresentationPageRow> list = null;


public String getTitle()
    {
        return page == null ? null : page.getTitle(EIsoLanguage.ENGLISH);
    }


    @Override
    public void loadValues()
    {
        Id id = page == null ? incorrectId : page.getId();
        if (row!=null)
        {


            Boolean arr[] = new Boolean[5];
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
                    if (!res.isDenied(ERestriction.ANY_PAGES))
                    {
                        arr[4] = arr[2] = true;
                    }
                    else
                    {
                        arr[4] = arr[2] = res.isPageEnabled(id);
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
                else if (!ar.isDenied(ERestriction.ANY_PAGES))
                {
                    isRight = arr[1] = true;

                }
                else
                {
                List<Id> lst = ar.getEnabledPageIds();
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
                else if (!r.isDenied(ERestriction.ANY_PAGES))
                {
                    arr[4] = arr[2] = arr[3] = true;
                }
                else
                {
                List<Id> lst = r.getEnabledPageIds();
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
            if (page!=null)
                return page.getName();
            return "#" + incorrectId.toString();
        }

   
   

   public void setRestriction(Restrictions res)
   {
       this.res = res;
       if (list!=null)
           for (TreeGridRoleResourceEdPresentationPageRow item : list)
           {
               item.setRestriction(res);
           }
   }
   public TreeGridRoleResourceEdPresentationPageRow(
           Restrictions res,
           TreeTable tbl, 
           String hash, AdsRoleDef role,
           AdsEditorPageDef page,           
           AdsEditorPresentationDef presentation
           )
    {
       this.page = page;
       initAll(res, tbl, hash, role, presentation);
    }
   public TreeGridRoleResourceEdPresentationPageRow(
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
        if (page!=null)
        {
             
            
            OrderedPage orderedPage = presentation.getEditorPages().getOrder().findOrderByPageId(page.getId());
            if (orderedPage!=null){
                EditorPages.PageOrder pageOrder = orderedPage.getSubpages();
                isMayChild = !pageOrder.isEmpty();
            }
            
 
            
            title = getName();
            icon = page.getIcon().getIcon();
            
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
            
            
            OrderedPage orderedPage = presentation.getEditorPages().getOrder().findOrderByPageId(page.getId());
            EditorPages.PageOrder pageOrder = orderedPage.getSubpages();
            
            for (OrderedPage childOrderdPage : pageOrder){
                AdsEditorPageDef childPage = childOrderdPage.findPage();
                if (childPage!=null)
                    list.add(new TreeGridRoleResourceEdPresentationPageRow(res, tbl, hash, role, childPage, presentation));
                
                
            }                       

        }
        return list;
    }


        @Override
    public boolean isMayModify(int  column) {
            if (column == 0)return true;
            if ( column == 2 || column == 4)
            {
                if (this.page==null)
                    return true;
                Restrictions r = (res!=null)  ? res : role.getOnlyCurrentResourceRestrictions(resHash);
                if  (r == null)
                    return false;
                if (r.isDenied(ERestriction.ACCESS) || !r.isDenied(ERestriction.ANY_PAGES) || r.isDenied(ERestriction.VIEW))
                    return false;
                Restrictions or = role.getOverwriteResourceRestrictions(resHash, presentation);
                List<Id> lst = or.getEnabledPageIds();
                return (lst!=null && !lst.contains(this.page.getId()));
                //return true;
            }
            return  false;
        }




        @Override
    public void CellWasModified(int column, Object val) {
            if (column!=2 && column!=4) return;
            Boolean flag = (Boolean)val;
            Id id = page == null ? incorrectId : page.getId();
            
            if (res!=null)
            {
                res.setPageEnabled(id, flag);
                loadValues();
                tbl.repaint();
                return;
            }
            Restrictions r = role.getOnlyCurrentResourceRestrictions(resHash);
            if (r == null) return;
            r.setPageEnabled(id, flag);
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
         if(page!=null)
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
            else if (!res_.isDenied(ERestriction.ANY_PAGES))
                {
                    isBold = true;
                }
            else{
            List<Id> lst = res_.getEnabledPageIds();
            if (lst  == null)
                lst = new ArrayList<>();
            isBold = lst.contains(page.getId());
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
        if (this.page==null)
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
            if (!or.isDenied(ERestriction.ANY_PAGES))
            {
                return Color.BLUE;
            }
            List<Id> lst = or.getEnabledPageIds();
            if (lst  == null)
                lst = new ArrayList<>();
            if (page!=null && lst.contains(page.getId()))
                return Color.BLUE;
            return Color.GRAY;
        }
        return  Color.BLACK;
             
        }

 
    }
