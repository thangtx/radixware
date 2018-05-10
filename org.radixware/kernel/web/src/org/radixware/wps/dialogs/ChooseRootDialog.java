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

package org.radixware.wps.dialogs;

import java.util.List;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.wps.rwt.Dialog;
import org.radixware.wps.rwt.ListBox;
import org.radixware.wps.rwt.ListBox.ListBoxItem;
import org.radixware.wps.rwt.VerticalBox;


public class ChooseRootDialog extends Dialog {

    private static class ListItem extends ListBox.ListBoxItem {
        public ListItem(final String rootTitle, final Icon icon) {
            setTitle(rootTitle);            
            setIconWidth(14);
            setIconHeight(14);
            setIcon(icon);
        }
    }
    
    private final ListBox list = new ListBox();
    private int selectedIndex = -1;

    public ChooseRootDialog(IDialogDisplayer displayer, List<ExplorerRoot> explorerRoots, final int currentIndex) {
        super(displayer, displayer.getEnvironment().getMessageProvider().translate("SelectRootDialog", "Select Explorer Root"), false);
        final VerticalBox vbox = new VerticalBox();
        add(vbox);        
        vbox.add(list);

        for (ExplorerRoot explorerRoot : explorerRoots) {            
            final RadParagraphDef paragraphDef = explorerRoot.getParagraphDef();
            final Icon icon;
            if (paragraphDef.getIcon()==null){
                icon = 
                    displayer.getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.Definitions.TREES);
            }else{
                icon = paragraphDef.getIcon();
            }                        
            final ListItem item = new ListItem(explorerRoot.getTitle(), icon);
            item.setObjectName("rx_explorer_root_list_item_"+explorerRoot.getId().toString());
            list.add(item);
        }
        
        if (currentIndex>=0 && currentIndex<list.count()){
            list.setCurrentRow(currentIndex);
            selectedIndex = currentIndex;
        }else if (list.count()>0){
            list.setCurrentRow(0);
            selectedIndex = 0;
        }        
        
        list.setSizePolicy(SizePolicy.EXPAND, SizePolicy.EXPAND);
        list.addCurrentItemListener(new ListBox.CurrentItemListener() {
            @Override
            public void currentItemChanged(ListBoxItem currentItem) {
                selectedIndex = list.getCurrentRow();
            }
        });
        list.addDoubleClickListener(new ListBox.DoubleClickListener() {

            @Override
            public void itemDoubleClick(ListBoxItem item) {
                selectedIndex = list.getCurrentRow();
                ChooseRootDialog.this.close(DialogResult.ACCEPTED);
            }
        });
        list.setFocused(true);
        setWidth(300);
        setHeight(300);
        addCloseAction(EDialogButtonType.OK);
        addCloseAction(EDialogButtonType.CANCEL);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
