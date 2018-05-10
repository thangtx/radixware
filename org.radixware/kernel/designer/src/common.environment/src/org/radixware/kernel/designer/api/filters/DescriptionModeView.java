package org.radixware.kernel.designer.api.filters;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.components.GenericComboBox;

public class DescriptionModeView extends GenericComboBox.PopupView {
    private final JPanel panel = new JPanel();
    private final DescriptionModeModel model;
    private boolean isOpened = false;
    private boolean isChanges = false;
    private boolean inUpdate = false;
    private JCheckBox cbShowAll = null;
    
    private Map<JCheckBox, DescriptionMode> views;
    private final ItemListener listener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (inUpdate){
                return;
            }
            Object source = e.getSource();
            if (source instanceof JCheckBox && model != null){
                JCheckBox currentCheckBox = (JCheckBox) source;
                DescriptionMode mode = views.get(currentCheckBox);
                if (mode != null){
                    if (currentCheckBox.isSelected()) {
                        if (!model.contains(mode)){
                            model.addCurrentModes(mode);
                            if (mode == DescriptionMode.SHOW_ALL) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (JCheckBox cb : views.keySet()){
                                            if (cb != cbShowAll){
                                                cb.setSelected(true);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        model.removeCurrentModes(mode);
                        if (mode != DescriptionMode.SHOW_ALL && cbShowAll != null) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    cbShowAll.setSelected(false);
                                }
                            });
                        }
                    }

                    isChanges = true;
                }
            }
        }
    };

    public DescriptionModeView() {
        model = new DescriptionModeModel();
        views = new HashMap<>();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }

    @Override
    public void update() {
        if (!isOpened){
            return;
        }
        inUpdate = true;
        
        for (JCheckBox cb : views.keySet()){
            cb.setSelected(model.contains(views.get(cb)));
        }
        isChanges = false;
        inUpdate = false;
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public GenericComboBox.Model getModel() {
        return model;
    }
    
    public void open(Layer layer){
        isOpened = false;
        model.open(layer);
        views.clear();
        panel.removeAll();
        cbShowAll = null;
        for (DescriptionMode mode : model.getDescriptionModes()){
            JCheckBox cb = new JCheckBox(mode.toString());
            cb.addItemListener(listener);
            if (mode == DescriptionMode.SHOW_ALL){
                cbShowAll = cb;
            }
            views.put(cb, mode);
            panel.add(cb);
        }
        isOpened = true;
        update();
        isChanges = true;
    }
    
    

    @Override
    public boolean isChanged() {
        return isChanges;
    }

}
