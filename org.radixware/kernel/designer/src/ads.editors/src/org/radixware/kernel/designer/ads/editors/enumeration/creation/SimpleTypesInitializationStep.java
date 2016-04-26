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

package org.radixware.kernel.designer.ads.editors.enumeration.creation;


import java.awt.BorderLayout;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.kernel.designer.common.dialogs.components.SpecifyNameAndValTypePanel;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;


public class SimpleTypesInitializationStep extends CreatureSetupStep<AdsEnumCreature, SimpleTypesInitializationStep.BasePanel> {
    
    public class BasePanel extends JPanel {
        
        private Map<EValType, String> valType2StringMap;
        private SpecifyNameAndValTypePanel specifyNameAndValTypePanel;
        
        public BasePanel() {
            super();
            
            setLayout(new BorderLayout());
            
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            
            specifyNameAndValTypePanel = new SpecifyNameAndValTypePanel();
            
            mainPanel.add(specifyNameAndValTypePanel);
            
            add(mainPanel, BorderLayout.NORTH);
            valType2StringMap = new EnumMap<EValType, String>(EValType.class);
            valType2StringMap.put(EValType.INT, "newIntEnum");
            valType2StringMap.put(EValType.STR, "newStrEnum");
            valType2StringMap.put(EValType.CHAR, "newCharEnum");
        }
        
        public void open(final AdsEnumCreature creature) {
            
            final EValType valType = creature.getValType();
            setFilter(ValTypes.ENUM_TYPES);
            setValType(valType);
            setIdEnum(creature.isIdEnum());
            
            specifyNameAndValTypePanel.open(creature.getModule());
            setCurrentName(valType2StringMap.get(valType));
            
            specifyNameAndValTypePanel.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    final SpecifyNameAndValTypePanel specifyNameAndValTypePanel = (SpecifyNameAndValTypePanel) e.getSource();
                    final EValType valType = specifyNameAndValTypePanel.getValType();
                    final String name = specifyNameAndValTypePanel.getCurrentName();
                    creature.setName(name);
                    creature.setValType(valType);
                    fireChange();
                }
            });
        }
        
        public void setCurrentName(String name) {
            specifyNameAndValTypePanel.setCurrentName(name);
        }
        
        public void setValType(EValType eValType) {
            specifyNameAndValTypePanel.setValType(eValType);
        }
        
        public void setFilter(Set<EValType> filter) {
            specifyNameAndValTypePanel.setFilter(filter);
        }
        
        public String getCurrentName() {
            return specifyNameAndValTypePanel.getCurrentName();
        }
        
        public EValType getValType() {
            return specifyNameAndValTypePanel.getValType();
        }
        
        public boolean isComplete() {
            return specifyNameAndValTypePanel.isComplete();
        }
        
        public boolean isIdEnum() {
            return specifyNameAndValTypePanel.isIdEnum();
        }
        
        public void setIdEnum(boolean isId) {
            specifyNameAndValTypePanel.setIdEnum(isId);
        }
    }
    
    public SimpleTypesInitializationStep() {
        super();
    }
    
    @Override
    protected BasePanel createVisualPanel() {
        return new BasePanel();
    }
    
    @Override
    public void open(final AdsEnumCreature creature) {
        getVisualPanel().open(creature);
    }
    
    @Override
    public void apply(AdsEnumCreature creature) {
        creature.setName(getVisualPanel().getCurrentName());
        creature.setIdEnum(getVisualPanel().isIdEnum());
    }
    
    @Override
    public String getDisplayName() {
        return NbBundle.getMessage(SimpleTypesInitializationStep.class, "Enum-SimpleStepTitle");
    }
    
    @Override
    public boolean isComplete() {
        return getVisualPanel().isComplete();
    }
    
    @Override
    public boolean isFinishiable() {
        return true;
    }
}
