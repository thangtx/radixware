/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.dialogs.components.localizing;

import javax.swing.JButton;
import javax.swing.JLabel;

public interface ILineLocalizedEditor extends ILocalizedEditor{

    public JLabel getCheckedInfoLable();

    public void setCheckedInfoLable(JLabel checkedInfoLable);
    
    public JLabel getAgreedInfoLable();

    public void setAgreedInfoLable(JLabel checkedInfoLable);
    
    public JButton getOpenRichEditor();

    public void setOpenRichEditor(JButton openRichEditor);
    
    
}
