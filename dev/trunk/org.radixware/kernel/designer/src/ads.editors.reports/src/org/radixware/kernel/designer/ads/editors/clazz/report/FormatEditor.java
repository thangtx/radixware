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

package org.radixware.kernel.designer.ads.editors.clazz.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormat;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class FormatEditor extends ExtendableTextField{
    
     private final JButton editFormatButton;
     private AdsReportFormat format;
     private EValType type;

    
    FormatEditor(final Runnable runnable, final boolean  isReadOnly){
        super(true);

        editFormatButton=addButton();
        editFormatButton.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon(13, 13));
        editFormatButton.setToolTipText("Edit Format");
        
        final ActionListener chooseButton = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
             
                AdsReportFormat newFormat=new AdsReportFormat(FormatEditor.this.format);
                FormatDialog dialog=new FormatDialog(isReadOnly) ;
                dialog.open(newFormat, type);
                dialog.invokeModalDialog();
                if (dialog.isOK() ) {
                    FormatEditor.this.format=newFormat;
                    if (runnable != null) {
                        runnable.run();
                    }                    
                    setValue(newFormat.getStrValue(FormatEditor.this.type));

                }  
            }
        };
        editFormatButton.addActionListener(chooseButton);
    }

    void open(final  AdsReportFormat format,final EValType type){        
        this.format=format;
        this.type=type;
        if(format!=null){
            setValue(format.getStrValue(FormatEditor.this.type));
        }
    }
    
    AdsReportFormat getFormat(){
        return format;
    }
    
}
