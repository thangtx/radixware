/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.dialogs.chooseobject;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.dialogs.chooseobject.ChooseObjectsPanel;
import org.radixware.kernel.common.dialogs.chooseobject.ISelectableObject;
import org.radixware.kernel.common.dialogs.chooseobject.SelectableObjectWrapper;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

/**
 *
 * @author dlastochkin
 */
public class ChooseDefinitionExModalDisplayer implements IChooseDefinitionModalDisplayer {

    private final ChooseObjectsPanel panel;
    private final String title;

    public ChooseDefinitionExModalDisplayer(ChooseDefinitionCfg cfg) {        
        this.title = "Select " + cfg.getTypesTitle();

        List<ISelectableObject> objects = new ArrayList<>();
        for (Definition def : cfg.collectAllowedDefinitions()) {
            objects.add(SelectableObjectWrapper.getRadixObjectWrap(def));
        }
        
        panel = new ChooseObjectsPanel(objects, cfg.getTypeTitle());
    }

    @Override
    public boolean showModal() {
        return new ModalDisplayer(panel, title).showModal();
    }

    @Override
    public List<Definition> getResult() {
        List<Definition> result = new ArrayList<>();
        for (ISelectableObject object : panel.getSelectedObjects()) {
            result.add((Definition) object.getUserObject());
        }
        
        return result;
    }

}
