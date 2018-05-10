package org.radixware.kernel.designer.ads.localization.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.radixware.kernel.designer.ads.localization.MultilingualEditor;
import org.radixware.kernel.designer.ads.localization.source.MlsTablePanel;

public class EditorAction extends AbstractAction{
    private final MultilingualEditor editor;

    public EditorAction(MultilingualEditor editor, String name, Icon icon) {
        super(name, icon);
        this.editor = editor;
    }

    public EditorAction(MultilingualEditor editor, String name) {
        super(name);
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        editor.fireChange(getName());
    }
    
    public String getName(){
        return getValue(NAME).toString();
    }
}
