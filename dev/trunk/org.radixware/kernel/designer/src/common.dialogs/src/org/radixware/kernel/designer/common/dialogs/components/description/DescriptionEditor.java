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

package org.radixware.kernel.designer.common.dialogs.components.description;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;

import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.KernelEnumComboBoxModel;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


@NbBundle.Messages({
    "DescriptionEditor-Title=Description",
    "DescriptionEditor-ConvertMessage=Description may be localized, click to localize"
})
public class DescriptionEditor extends JPanel {

    private static final class DescriptionDialog extends ModalDisplayer {

        public DescriptionDialog(DescriptionEditor descriptionPanel, String title) {
            super(descriptionPanel, title);
        }

        @Override
        public DescriptionEditor getComponent() {
            return (DescriptionEditor) super.getComponent();
        }
    }

    private static final class ConvertDialog extends ModalDisplayer {

        private static final class ConvertPanel extends JPanel {

            private KernelEnumComboBoxModel<EIsoLanguage> model;

            public ConvertPanel(List<EIsoLanguage> languages) {
                setLayout(new GridBagLayout());

                JLabel lblSelectLang = new JLabel(NbBundle.getMessage(DescriptionPanel.class, "LocalizedDescriptionPanel.lblSelectLang"));
                lblSelectLang.setHorizontalAlignment(JLabel.RIGHT);

                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = 0;
                constraints.gridy = 0;
                constraints.gridheight = 2;
                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.anchor = GridBagConstraints.PAGE_START;
                constraints.insets = new Insets(0, 4, 4, 8);
                add(lblSelectLang, constraints);

                if (languages != null && !languages.isEmpty()) {
                    JComboBox<KernelEnumComboBoxModel.Item<EIsoLanguage>> cmbLanguages = new JComboBox<>();
                    model = new KernelEnumComboBoxModel<>(EIsoLanguage.class, languages, languages.get(0));
                    cmbLanguages.setModel(model);

                    constraints = new GridBagConstraints();
                    constraints.gridx = 1;
                    constraints.gridy = 0;
                    constraints.gridheight = 1;
                    constraints.fill = GridBagConstraints.HORIZONTAL;
                    constraints.anchor = GridBagConstraints.PAGE_START;
                    add(cmbLanguages, constraints);
                }

                setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

                setMaximumSize(new Dimension(300, 40));
                setMinimumSize(new Dimension(250, 30));
            }
        }

        public ConvertDialog(List<EIsoLanguage> languages) {
            super(new DescriptionEditor.ConvertDialog.ConvertPanel(languages), "Description localization");
            getDialogDescriptor().setValid(languages != null && !languages.isEmpty());

            getDialog().setMaximumSize(new Dimension(300, 50));
        }

        public EIsoLanguage getLanguage() {
            return ((DescriptionEditor.ConvertDialog.ConvertPanel) getComponent()).model.getSelectedItemSource();
        }
    }

    public static boolean showModal(DescriptionEditor panel) {
        final DescriptionEditor.DescriptionDialog dialog = new DescriptionEditor.DescriptionDialog(panel, "Edit description");
        return dialog.showModal();
    }
    private IDescriptionEditor editor;
    private IDescriptionHandleInfo handleInfo;
    private final ChangeListener listener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            changeSupport.fireChange();
        }
    };
    private final ChangeSupport changeSupport = new ChangeSupport(this);
    private final String title;
    private boolean readonly = false;
    private boolean isInherit = false;

    public DescriptionEditor() {
        this(Bundle.DescriptionEditor_Title());
    }

    public DescriptionEditor(String title) {
        super.setLayout(new BorderLayout());

        this.title = title;
        // for nb visual editor
        installEditor();
    }

    @Override
    @Deprecated
    public void setLayout(LayoutManager mgr) {
    }

    private void reopen() {
        editor.removeChangeListener(listener);

        installEditor();
        revalidate();

        editor.open(handleInfo);
        
    }

    public <T extends Object & IDescribable & ILocalizedDescribable> void open(T definition) {
       // if (definition instanceof AdsDefinition) {
            open(new MixedDescriptionHandleInfo<>(definition, false));
//        } else {
//            open(new StringDescriptionHandleInfo(definition, false));
//        }
    }

    public void open(IDescribable definition) {
        open(new StringDescriptionHandleInfo(definition, false));
    }

    public void open(ILocalizedDescribable definition) {
        open(new LocalizedDescriptionHandleInfo(definition, false));
    }

    public void open(IDescriptionHandleInfo handleInfo) {
        this.handleInfo = handleInfo;

        installEditor();
        editor.open(handleInfo);
        if (isInherit){
            setReadonly(readonly);
        }
    }

    public boolean tryOpen(RadixObject object) {
        if (object instanceof AdsDefinition) {
            open((AdsDefinition) object);
        } else {
            boolean localized = false;
            if (object instanceof ILocalizedDescribable) {
                final ILocalizedDescribable localizedDescribable = (ILocalizedDescribable) object;
                if (localizedDescribable.getDescriptionId() != null) {
                    localized = true;
                } else if (object instanceof IDescribable) {
                    final IDescribable describable = (IDescribable) object;
                    if (describable.getDescription() == null || describable.getDescription().isEmpty()) {
                        localized = true;
                    }
                }
            }

            if (localized) {
                open((ILocalizedDescribable) object);
                return true;
            } else if (object instanceof IDescribable) {
                open((IDescribable) object);
                return true;
            }
        }

        return false;
    }

    /**
     * Open description editor for <tt>descriptionModel</tt> in proxy mode.
     */
    public void open(DescriptionModel descriptionModel) {
        open(new ModelProxyDescriptionHandleInfo(descriptionModel));
    }

    public void update() {
        if (editor != null) {
            editor.update();
        }
    }

    public void setReadonly(boolean readonly) {
        if (editor != null) {
            this.readonly = readonly;
            if (isInherit){
                ILocalizedDescribable.Inheritable owner = getInherit();
                editor.setReadonly(readonly || owner.isDescriptionInherited());
            }
        }
    }

    private IDescriptionEditor createEditorImpl(String title) {
        if (handleInfo == null) {
            return DescriptionEditorFactory.createInstance(EDescriptionType.STRING, title);
        }
        return DescriptionEditorFactory.createInstance(handleInfo.getDescriptionType(), title);
    }

    private void installEditor() {
        removeAll();

        editor = createEditorImpl(title);
//        ((JComponent)editor).setPreferredSize(new Dimension(100,0));
        editor.addChangeListener(listener);
        add((JComponent) editor, BorderLayout.CENTER);
        
        if (handleInfo != null && handleInfo.isLocalizable()) {
            final Definition definition = handleInfo.getAdsDefinition();
            if (definition == null || !definition.isReadOnly()) {
                addConverter();
            }
        }
        if (isInheritable()) {
            initComponents();
        }
        revalidate();
    }

    private void addConverter() {
        final JLabel message = new JLabel(Bundle.DescriptionEditor_ConvertMessage());
        message.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        message.setForeground(Color.RED);
        message.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        final Box messageBox = new Box(BoxLayout.X_AXIS);
        messageBox.add(message);

        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final List<EIsoLanguage> languages = handleInfo.getAdsDefinition().getDescriptionLocation().getLayer().getLanguages();
                final DescriptionEditor.ConvertDialog convertDialog = new DescriptionEditor.ConvertDialog(languages);
                if (convertDialog.showModal()) {
                    localize(convertDialog.getLanguage());
                }
            }
        };

        message.addMouseListener(mouseAdapter);
        message.addMouseMotionListener(mouseAdapter);

        add(messageBox, BorderLayout.PAGE_START);
    }

    void localize(EIsoLanguage language) {
        handleInfo.localize(language);
        reopen();

        changeSupport.fireChange();
    }

    private boolean isLocalized() {
        return handleInfo.getDescriptionType() == EDescriptionType.LOCALIZED;
    }

    public boolean commit() {
        return handleInfo != null ? handleInfo.commit() : false;
    }

    public String getStringDescription() {
        return handleInfo.getStringDescription();
    }

    public Map<EIsoLanguage, String> getLocalizedDescription() {
        if (!isLocalized()) {
            return null;
        }
        return handleInfo.getValueMap();
    }

    public Object getDescription() {
        return editor.getDescription();
    }

    public void addChangeListener(ChangeListener listener) {
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeSupport.removeChangeListener(listener);
    }

    public EDescriptionType getEditorMode() {
        return handleInfo.getDescriptionType();
    }

    public boolean isProxy() {
        return handleInfo.isProxy();
    }

    public IDescriptionHandleInfo getHandleInfo() {
        return handleInfo;
    }
    
    private ILocalizedDescribable.Inheritable getInherit(){
         if (editor != null && handleInfo != null){
             IDescribable describable = handleInfo.getDescribable();
             if ((describable instanceof ILocalizedDescribable.Inheritable)){
                return (ILocalizedDescribable.Inheritable) handleInfo.getDescribable();
             }
         }
         return null;
    }
    
    private void initComponents(){
        if (editor != null){
            final ILocalizedDescribable.Inheritable owner = getInherit();
            if (owner != null){
                editor.setInherit(owner.isDescriptionInherited());

                ItemListener inheritListener = new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
                        Map<EIsoLanguage, String> values = null;
                        if (!isSelected){
                            values = handleInfo.getValueMap();
                        }
                        owner.setDescriptionInherited(isSelected);
                        if (values != null){
                            if (owner.getDescriptionId() == null){
                                for (EIsoLanguage language : values.keySet()){
                                    handleInfo.setValue(language, values.get(language));
                                }
                            }
                        }
                        editor.update();
                        commit();
                        editor.setReadonly(readonly || isSelected);
                    }
                };
                
                editor.initComponentsForInheriting(inheritListener);
            }
        } 
    }
    
    private boolean isInheritable(){
        ILocalizedDescribable.Inheritable owner = getInherit();
        isInherit = owner != null && owner.isDescriptionInheritable();
        return isInherit;
    }
    
    
    @Override
    public String toString() {
        return handleInfo.toString();
    }
}
