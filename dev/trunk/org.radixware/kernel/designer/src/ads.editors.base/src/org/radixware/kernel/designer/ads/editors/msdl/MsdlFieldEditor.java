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
package org.radixware.kernel.designer.ads.editors.msdl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.msdl.TemplateSchemeSearcher;
import org.radixware.kernel.common.design.msdleditor.field.FieldPanel;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.IEnumPanelRetriever;
import org.radixware.kernel.common.msdl.IFieldTemplateTextFieldRetriever;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.msdl.fields.IntFieldModel;
import org.radixware.kernel.common.msdl.fields.StrFieldModel;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseRadixObject;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseRadixObjectCfg;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.nodes.NodesManager;

public class MsdlFieldEditor<T extends MsdlField> extends RadixObjectEditor<T> implements IEnumPanelRetriever, IFieldTemplateTextFieldRetriever {

    private FieldPanel fieldPanel = null;
    private ExtendableTextField templateTextField = null;

    private class TemplateSchemeProvider extends VisitorProvider {

        private RadixObject context = null;

        public TemplateSchemeProvider(RadixObject context) {
            super();
            this.context = context;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            int dept = 0;

            for (RadixObject obj = radixObject; obj != null; obj = obj.getContainer()) {
                if (obj instanceof RootMsdlScheme) {
                    break;
                }
                dept++;
            }
            if (dept == 3) {
                boolean result = false;
                boolean currIsNeedType = isNeededType(context);
                boolean candidateIsNeedType = isNeededType(radixObject);
                boolean typesAreEqual = typesEqual(context, radixObject);
                boolean notSelfParentOrChild = isNotParentSelfOrChild(radixObject);
                result = currIsNeedType && candidateIsNeedType && typesAreEqual && notSelfParentOrChild;
                return result;
            } else {
                return false;
            }
        }

        private boolean isNeededType(RadixObject object) {
            boolean ret = false;
            if (object instanceof MsdlField) {
                MsdlField msdlField = (MsdlField) object;
                if (msdlField.getType() == EFieldType.STRUCTURE || msdlField.getType() == EFieldType.CHOICE) {
                    ret = true;
                }
            } else if (object instanceof AbstractFieldModel) {
                AbstractFieldModel afm = (AbstractFieldModel) object;
                if (afm.getType() == EFieldType.STRUCTURE || afm.getType() == EFieldType.CHOICE) {
                    ret = true;
                }
            }
            return ret;
        }

        private boolean checkParent(RadixObject toCheck, RadixObject compareTo) {
            RadixObject parent = toCheck.getContainer();
            while (parent != null) {
                if (parent == compareTo) {
                    return false;
                }
                parent = parent.getContainer();
            }
            return true;
        }

        private boolean isNotParentSelfOrChild(RadixObject candidate) {
            if (candidate == context) {
                return false;
            }

            boolean parentCheck = checkParent(context, candidate);
            if (parentCheck) {
                return checkParent(candidate, context);
            }

            return true;
        }

        private boolean typesEqual(RadixObject one, RadixObject two) {
            return getMsdlType(one) == getMsdlType(two);
        }

        private EFieldType getMsdlType(RadixObject potentialMsdl) {
            EFieldType ret = null;
            if (potentialMsdl instanceof MsdlField) {
                MsdlField msdlField = (MsdlField) potentialMsdl;
                ret = msdlField.getType();
            } else if (potentialMsdl instanceof AbstractFieldModel) {
                AbstractFieldModel afm = (AbstractFieldModel) potentialMsdl;
                ret = afm.getType();
            }
            return ret;
        }
    }

    private JButton createClearButton() {
        if (templateTextField != null) {
            JButton clearButton = new JButton();
            clearButton.setToolTipText("Clear");
            clearButton.setIcon(RadixWareIcons.DELETE.CLEAR.getIcon(13, 13));
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    templateTextField.setTextFieldValue("");
                    RadixObject me = getRadixObject();
                    MsdlField currentField = null;
                    if (me instanceof MsdlField) {
                        currentField = (MsdlField) me;
                    } else if (me instanceof AbstractFieldModel) {
                        currentField = ((AbstractFieldModel) me).getMsdlField();
                    }
                    if (currentField != null) {
                        currentField.getFieldModel().getField().setTemplateSchemeId(null);
                        currentField.getFieldModel().getField().setTemplateFieldPath(null);
                        currentField.getFieldModel().setModified(true);
                    }
                }
            });
            return clearButton;
        }
        return null;
    }

    private JButton createChooseButton() {
        if (templateTextField != null) {
            JButton chooseButton = new JButton();
            chooseButton.setToolTipText("Choose field template");
            chooseButton.setIcon(RadixWareIcons.DIALOG.CHOOSE.getIcon(13, 13));
            chooseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    RadixObject context = getRadixObject();
                    ChooseRadixObjectCfg cfg = ChooseRadixObjectCfg.Factory.newInstance(context, new TemplateSchemeProvider(context));
                    cfg.setStepCount(2);
                    RadixObject selectedObject = ChooseRadixObject.chooseRadixObject(cfg);

                    MsdlField selectedField = null;
                    MsdlField currentField = null;
                    if (selectedObject instanceof MsdlField) {
                        selectedField = (MsdlField) selectedObject;
                    } else if (selectedObject instanceof AbstractFieldModel) {
                        selectedField = ((AbstractFieldModel) selectedObject).getMsdlField();
                    }

                    RadixObject me = getRadixObject();
                    if (me instanceof MsdlField) {
                        currentField = (MsdlField) me;
                    } else if (me instanceof AbstractFieldModel) {
                        currentField = ((AbstractFieldModel) me).getMsdlField();
                    }

                    if (currentField != null) {
                        AdsMsdlSchemeDef parentDef = getAdsMsdlSchemeDef(selectedField);

                        if (parentDef != null) {

                            String idString = parentDef.getId().toString();

                            currentField.getFieldModel().getField().setTemplateSchemeId(idString);
                            currentField.getFieldModel().getField().setTemplateFieldPath("/" + selectedField.getName());

                            setTemplateTextFieldValue(parentDef.getName(), selectedField.getName());

                            currentField.getFieldModel().setModified(true);
                        }
                    }

                }
            });
            return chooseButton;
        }
        return null;
    }

    private JButton createSelectInTreeButton() {
        JButton selectInTreeBtn = new JButton();

        selectInTreeBtn.setToolTipText("Select in tree");
        selectInTreeBtn.setIcon(RadixWareIcons.TREE.SELECT_IN_TREE.getIcon(13, 13));
        selectInTreeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractFieldModel tfm = getTemplateFieldModel();
                if (tfm != null) {
                    NodesManager.selectInProjects(tfm);
                }
            }
        });

        return selectInTreeBtn;
    }

    private AbstractFieldModel getTemplateFieldModel() {
        AbstractFieldModel afm = null;
        MsdlField currentField = (MsdlField) getRadixObject();

        if (currentField.getFieldModel().getField().isSetTemplateSchemeId()) {
            TemplateSchemeSearcher searcher = new TemplateSchemeSearcher(getAdsMsdlSchemeDef(currentField));
            Id id = Id.Factory.loadFrom(currentField.getFieldModel().getField().getTemplateSchemeId());
            afm = searcher.findField(id, currentField.getFieldModel().getField().getTemplateFieldPath(), currentField.getType());
        }

        return afm;
    }

    private void setTemplateTextFieldvalueIfExists() {
        //try to find template if it is set
        MsdlField currentField = (MsdlField) getRadixObject();
        if (currentField.getFieldModel().getField().isSetTemplateFieldPath()) {
            String fieldName = currentField.getFieldModel().getField().getTemplateFieldPath();
            String schemeName = null;
            AbstractFieldModel tfm = getTemplateFieldModel();
            if (tfm != null) {
                MsdlField templateField = tfm.getMsdlField();
                schemeName = getAdsMsdlSchemeDef(templateField).getName();
                setTemplateTextFieldValue(schemeName, fieldName);
            }
        }
    }

    private AdsMsdlSchemeDef getAdsMsdlSchemeDef(RadixObject obj) {
        RadixObject parent = obj;
//        while (parent != null && !((parent = parent.getContainer()) instanceof AdsMsdlSchemeDef)) {
//        }
        AdsMsdlSchemeDef parentDef = null;
        if (parent != null) {
            parentDef = (AdsMsdlSchemeDef) parent.getDefinition();
        }
        return parentDef;
    }

    private void setTemplateTextFieldValue(String containerName, String fieldName) {
        if (templateTextField != null && fieldName != null) {
            StringBuilder sb = new StringBuilder();

            if (containerName != null) {
                sb.append(containerName);
            }
            if (fieldName.charAt(0) != '/') {
                sb.append("/");
            }
            sb.append(fieldName);
            templateTextField.setTextFieldValue(sb.toString());
        }
    }

    protected MsdlFieldEditor(T field) {
        super(field);
        initComponents();
    }

    protected FieldPanel getFieldPanel() {
        if (fieldPanel == null) {
            fieldPanel = new FieldPanel();
            JScrollPane scroll = new JScrollPane(fieldPanel);
            add(scroll);
        }
        return fieldPanel;
    }
    private StrEnumPanel strEnumPanel = null;

    @Override
    public JPanel getStrEnumPanel() {
        if (strEnumPanel == null) {
            strEnumPanel = new StrEnumPanel();
        }
        MsdlField field = getField();
        StrFieldModel strFieldModel = null;
        if (field.getType() == EFieldType.CHOICE) {
            strFieldModel = ((ChoiceFieldModel) field.getFieldModel()).getSelector();
        } else {
            strFieldModel = (StrFieldModel) getField().getFieldModel();
        }
        strEnumPanel.open(getMsdlScheme(), strFieldModel);
        return strEnumPanel;
    }
    private IntEnumPanel intEnumPanel = null;

    @Override
    public JPanel getIntEnumPanel() {
        if (intEnumPanel == null) {
            intEnumPanel = new IntEnumPanel();
        }
        intEnumPanel.open(getMsdlScheme(), (IntFieldModel) getField().getFieldModel());
        return intEnumPanel;
    }
    private SelectParseMergeFunctionPanel selectParseMergeFunctionPanel = new SelectParseMergeFunctionPanel();

    @Override
    public ExtendableTextField getTemplateTextField() {
        if (templateTextField == null) {
            templateTextField = new ExtendableTextField(true);
            JButton clearButton = createClearButton();
            JButton chooseButton = createChooseButton();
            JButton selectInTreeButton = createSelectInTreeButton();
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
            buttonsPanel.add(chooseButton);
            buttonsPanel.add(clearButton);
            buttonsPanel.add(selectInTreeButton);
            templateTextField.add(buttonsPanel);
            setTemplateTextFieldvalueIfExists();
            //templateTextField.setPreferredSize(new Dimension(1000, 24));
        }
        return templateTextField;
    }

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<MsdlField> {

        @Override
        public IRadixObjectEditor<MsdlField> newInstance(MsdlField field) {
            return new MsdlFieldEditor(field);
        }
    }

    public MsdlField getField() {
        return getRadixObject();
    }

    public AdsMsdlSchemeDef getMsdlScheme() {
        return (AdsMsdlSchemeDef) getField().getDefinition();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    void reread() {
        SelectParseMergeFunctionPanel selectPanel = null;
        if (getMsdlScheme().getRootMsdlScheme().getPreprocessorClassGuid() != null
                && !getMsdlScheme().getRootMsdlScheme().isDbf()) {
            selectPanel = selectParseMergeFunctionPanel;
            selectPanel.open(getField(), new SupportSelectPreprocessorFunctionList(getField()));
        }
        getFieldPanel().open(getField(), this, selectPanel, this);
        fieldPanel.update();
    }

    @Override
    public boolean open(OpenInfo info) {
        reread();
        return super.open(info);
    }

    @Override
    public void update() {
        reread();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
