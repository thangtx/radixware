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

package org.radixware.kernel.explorer.editors.profiling;

import com.trolltech.qt.gui.QTreeWidgetItem;
import java.util.List;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;



public class ItemTextCalculator {

   public void calcChildItemsText(final TreeItem parent, final Double periodDuration, final boolean isSumMode,final RadEnumPresentationDef timeSectionEnum){
       parent.setItemText();
       for(int i=0;i<parent.childCount();i++){
             final TreeItem item=(TreeItem)parent.child(i);
             calcItemText( item,  periodDuration,  isSumMode,timeSectionEnum);
         }
    }

    public void calcItemText(final TreeItem item, final Double periodDuration, final boolean isSumMode,final RadEnumPresentationDef timeSectionEnum){
        final Double load=(item.getDuration()*100)/periodDuration;
        item.setLoad(load);
        final Double pureLoad=(item.getPureDuration()*100)/periodDuration;
        item.setPureLoad(pureLoad);
        //Double avgLoad=(item.getAvgDuration()*100)/periodDuration;
        //item.setAvgLoad(avgLoad);
        //Double avgPureLoad=(item.getAvgPureDuration()*100)/periodDuration;
        //item.setAvgPureLoad(avgPureLoad);
        item.setItemText();
        if(timeSectionEnum!=null){
            item.calcDomains(timeSectionEnum);
        }
        calcChildItemsText(item, periodDuration, isSumMode,timeSectionEnum);
    }

    public String getItemTitle(String s,final RadEnumPresentationDef timeSectionEnum){
        final int index=s.indexOf(':');
        String strIndex="";
        if(index!=-1){
                strIndex="["+s.substring(index+1)+"]";
                s=s.substring(0,index);
        }
        String title=s;
        if(timeSectionEnum!=null){
            final RadEnumPresentationDef.Item item=timeSectionEnum.findItemByValue(s);
            if(item!=null){
                title=item.getTitle();
            }
        }
        return title+strIndex;
    }

    public  TreeItem searchInFlatList(final TreeItem searchItem,final List<QTreeWidgetItem> flatList){
        for(int i=0;i<flatList.size();i++){
            final TreeItem item=(TreeItem)flatList.get(i);
            if(searchItem.equals(item)){
                return item;
            }
        }
        return null;
    }

}
