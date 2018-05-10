package org.radixware.kernel.designer.api.filters;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.designer.common.dialogs.components.GenericComboBox;

public class DescriptionModeModel extends GenericComboBox.Model {
    private Layer layer = null;
    private final List<DescriptionMode> descriptionModes = new ArrayList<>();
    private List<DescriptionMode> currentModes = null;

    public DescriptionModeModel() {
        currentModes = new ArrayList<>();
        currentModes.add(DescriptionMode.SHOW_ALL);
    }
    
    public void open(Layer layer){
        if (layer != null && this.layer != layer){
            this.layer = layer;
            List<DescriptionMode> oldModes = new ArrayList<>(descriptionModes);
            descriptionModes.clear();
            descriptionModes.add(DescriptionMode.SHOW_ALL);
            descriptionModes.add(DescriptionMode.LEGACY_DESCRIPTION);
            for (EIsoLanguage language : layer.getLanguages()){
                descriptionModes.add(new DescriptionMode(DescriptionMode.TRANSLATED_START + language.getName() + DescriptionMode.TRANSLATED_END, language)); 
            }
            if (currentModes.contains(DescriptionMode.SHOW_ALL)){
                currentModes.clear();
                currentModes.addAll(descriptionModes);
            } else {
                currentModes.retainAll(descriptionModes);
                for (DescriptionMode mode : descriptionModes) {
                    if (!oldModes.contains(mode)) {
                        if (currentModes != null) {
                            currentModes.add(mode);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        return "Description mode";
    }
    
    public String getToolTip() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        if (descriptionModes.contains(DescriptionMode.SHOW_ALL)){
            return DescriptionMode.SHOW_ALL.toString();
        }

        for (DescriptionMode mode : descriptionModes) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }

            sb.append(mode.toString());
        }

        return sb.toString();
    }

    public List<DescriptionMode> getDescriptionModes() {
        return descriptionModes;
    }

    public boolean contains(DescriptionMode mode){
        return currentModes == null? true : currentModes.contains(mode);
    }

    public List<DescriptionMode> getCurrentModes() {
        return currentModes;
    }

    public void addCurrentModes(DescriptionMode mode) {
        if (mode == DescriptionMode.SHOW_ALL){
            currentModes.clear();
            currentModes.addAll(descriptionModes);
        } else {
            currentModes.add(mode);
        }
    }
    
    public void removeCurrentModes(DescriptionMode mode) {
        currentModes.remove(mode);
        if (currentModes.contains(DescriptionMode.SHOW_ALL)){
            currentModes.remove(DescriptionMode.SHOW_ALL);
        }
    }
}
