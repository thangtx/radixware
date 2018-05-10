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

package org.radixware.kernel.designer.ads.method.profile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoStrobMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;
import org.radixware.kernel.designer.ads.method.throwslist.AdsMethodThrowsListEditor;
import org.radixware.kernel.designer.common.dialogs.RadixWareDesignerIcon;
import org.radixware.kernel.designer.common.dialogs.components.ApiUsageVariantEditor;
import org.radixware.kernel.designer.common.dialogs.components.DescriptionPanel;
import org.radixware.kernel.designer.common.dialogs.components.IdSourcePanel;
import org.radixware.kernel.designer.common.dialogs.components.LabeledNameEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionModel;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionStorage;
import org.radixware.kernel.designer.common.dialogs.components.state.StateDisplayer;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


public final class ChangeProfilePanel extends javax.swing.JPanel implements ChangeListener {

    public static final String RETURN_VALUE_DESCRIPTION_KEY = "return-value-description";
    public static final String METHOD_PARAMETR_DESCRIPTION_KEY = "method-parametr-";
    public static final String THROW_ITEM_DESCRIPTION_KEY = "throw-item-";

    private class AdsMethodProfileModalDisplayer extends ModalDisplayer implements ChangeListener {

        private boolean isEdited = false;

        public AdsMethodProfileModalDisplayer(AdsMethodDef method) {
            super(ChangeProfilePanel.this);
            ChangeProfilePanel.this.setIsDialogComponent(true);

            ChangeProfilePanel.this.edit(method, isReadonly(method));
            boolean valid = ChangeProfilePanel.this.isComplete();
            getDialogDescriptor().setValid(valid);

            ChangeProfilePanel.this.addChangeListener(this);
            setTitle(NbBundle.getMessage(ChangeProfilePanel.class, "PP-Title"));
            setIcon(method.getIcon());
        }

        private boolean isReadonly(AdsMethodDef method) {
            EMethodNature nature = method.getNature();
            return method.getProfile().isReadOnly()
                    || nature == EMethodNature.ALGO_STROB
                    || nature == EMethodNature.ALGO_START
                    || nature == EMethodNature.SYSTEM
                    || method instanceof AdsTransparentMethodDef
                    || method instanceof AdsCommandHandlerMethodDef
                    || method instanceof AdsRPCMethodDef;
        }

        @Override
        public Object[] getOptions() {
            if (method.isReadOnly()) {
                javax.swing.JButton closeBtn = new javax.swing.JButton("Close");
                closeBtn.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AdsMethodProfileModalDisplayer.this.close(false);
                    }
                });
                return new Object[]{closeBtn};
            }
            return super.getOptions();
        }

        @Override
        protected void apply() {
            if (method != null) { // && (isEdited )|| paramsPanel.isParameterNameChanged()) && !paramsPanel.isEscapeButtonPressed()
                Integer apiValue = apiUsageEditor.getCurrentValue();
                if (apiValue > -1) {
                    method.setApiUsageVariant(apiValue);
                }

                descriptionModel.applyFor(method);

                if (returnPanel != null) {
                    returnPanel.apply();
                }
                if (constructorName != null) {
                    method.setName(constructorName.getNameEditor().getCurrentName());
                }
                paramsPanel.apply();
                throwsPanel.apply();
                if (!method.getProfile().getAccessFlags().isPrivate()
                        && !method.getProfile().getAccessFlags().isStatic()
                        //                    && !method.isOverride()
                        && !method.isOverwrite()
                        && isEdited) {
                    ChangeProfilePanel.this.checkAndChangeOvers();
                }
                if (method.isIdInheritanceAllowed() && idSrcPanel != null) {
                    idSrcPanel.apply();
                }
            }
            isEdited = false;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource().equals(getComponent())) {
                isEdited = true;
                getDialogDescriptor().setValid(((ChangeProfilePanel) getComponent()).isComplete());
            }
        }
    }

    private ReturnTypePanel returnPanel;
    private MethodParametersPanel paramsPanel;
    private EnvSelectorPanel envSelectorPanel;
    private LabeledNameEditPanel constructorName;
    private AdsMethodThrowsListEditor throwsPanel;
    private AdsMethodDef method;
    private JButton fixButton = new JButton(NbBundle.getMessage(ChangeProfilePanel.class, "FixOverrideButton"));
    private boolean isDialogComponent;
    private final DescriptionEditor descriptionPanel = new DescriptionPanel();
    private ApiUsageVariantEditor apiUsageEditor = new ApiUsageVariantEditor();
    private JLabel apiUsageLabel = new JLabel(NbBundle.getMessage(ChangeProfilePanel.class, "ApiUsage"));
    private IdSourcePanel idSrcPanel;
    private JButton btInheritIdOption = new JButton();
    private DescriptionModel descriptionModel;
    private final JPanel parametersPanel = new JPanel();
    private final JSplitPane innerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    private final JSplitPane outerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    public ChangeProfilePanel() {
        ActionListener fixListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeProfilePanel.this.fixOverridenProfile();
            }
        };
        fixButton.addActionListener(fixListener);

        descriptionPanel.addChangeListener(this);
        apiUsageEditor.addChangeListener(this);

    }

    public void editProfile(AdsMethodDef method) {
        AdsMethodProfileModalDisplayer displayer = new AdsMethodProfileModalDisplayer(method);
        displayer.showModal();
    }

    public void setIsDialogComponent(boolean isDialogComponent) {
        this.isDialogComponent = isDialogComponent;
    }

    public boolean isDialogComponent() {
        return isDialogComponent;
    }

    private void updateIdInheritOption() {
        if (btInheritIdOption != null) {
            final AdsEnumItemDef item;
            if (idSrcPanel == null) {
                item = method.findIdSourceItem();
            } else {
                item = idSrcPanel.getSelectedItem();
            }
            if (item != null) {
                btInheritIdOption.setIcon(RadixWareDesignerIcon.CHECK.SET.getIcon(13, 13));
                btInheritIdOption.setToolTipText("<html>Method ID is inherited from " + item.getQualifiedName() + "<br>Click to configure</html>");
            } else {
                btInheritIdOption.setIcon(RadixWareDesignerIcon.ARROW.GO_TO_OBJECT.getIcon(13, 13));
                btInheritIdOption.setToolTipText("Click for setup method ID inheritance");
            }
        }
    }

    private void setupUI(boolean editMode, boolean addParamsAndThrows, AdsClassDef owner, final boolean readonly) {
        final boolean isOverride = !method.getHierarchy().findOverridden().isEmpty();
        final boolean isOverwrite = !method.getHierarchy().findOverwritten().isEmpty();

        //final boolean canChangeProfile = !isOverride && !isOverwrite && !readonly;
        final boolean canChangeProfile = !isOverwrite && !readonly;
        
        if (!addParamsAndThrows) {
            setupForStrob();
        } else {

            GridBagLayout gbl = new GridBagLayout();
            setLayout(gbl);
            GridBagConstraints c = new GridBagConstraints();

            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 0.0;
            c.gridy = 0;
            c.insets = new Insets(10, 10, 10, 10);
            if (method.isConstructor()) {
                constructorName = new LabeledNameEditPanel();
                constructorName.getNameEditor().setEditable(false);
                constructorName.getNameEditor().setCurrentName(owner.getName());
                gbl.setConstraints(constructorName, c);
                add(constructorName);
            } else {
                int featureCount = method.isIdInheritanceAllowed() ? 1 : 0;
                returnPanel = new ReturnTypePanel(featureCount);
                if (featureCount > 0) {
                    btInheritIdOption = returnPanel.getNameFeatures()[0];
                    updateIdInheritOption();
                    btInheritIdOption.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (method != null) {
                                if (idSrcPanel == null) {
                                    idSrcPanel = new IdSourcePanel(false);
                                }
                                ModalDisplayer displayer = new ModalDisplayer(idSrcPanel);
                                idSrcPanel.open(method);
                                idSrcPanel.setReadOnly(readonly || isOverride);
                                final boolean[] idSrcPanelModified = new boolean[]{false};
                                final ChangeListener listener = new ChangeListener() {

                                    @Override
                                    public void stateChanged(ChangeEvent e) {
                                        idSrcPanelModified[0] = true;
                                    }
                                };
                                idSrcPanel.addChangeListener(listener);
                                if (displayer.showModal() && idSrcPanelModified[0]) {
                                    changeSupport.fireChange();
                                }
                                updateIdInheritOption();
                                idSrcPanel.removeChangeListener(listener);

                            }
                        }
                    });
                }

                returnPanel.addChangeListener(this);
                returnPanel.open(method, owner, readonly);
                gbl.setConstraints(returnPanel, c);
                add(returnPanel);
            }

            // install EnvSelectorPanel
            if (!editMode && EnvSelectorPanel.isMeaningFullFor(method)) {

                envSelectorPanel = new EnvSelectorPanel();
                envSelectorPanel.open(method);

                c.insets = new Insets(0, 10, 10, 10);
                c.gridy++;
                c.gridwidth = 2;
                add(envSelectorPanel, c);
            }
            c.insets = new Insets(0, 10, 10, 10);
            c.gridwidth = 2;
            if (method.isOverride()) {
                c.gridy++;
                c.weightx = 0.0;
                c.fill = GridBagConstraints.NONE;
                c.anchor = GridBagConstraints.WEST;

                gbl.setConstraints(fixButton, c);
                add(fixButton);
            }
            parametersPanel.setLayout(new BorderLayout());
            JLabel paramLabel = new JLabel(NbBundle.getMessage(ChangeProfilePanel.class, "PP-Label-Params"));
            parametersPanel.add(paramLabel, BorderLayout.PAGE_START);
            paramLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            paramsPanel = new MethodParametersPanel();
            paramsPanel.open(method);
            paramsPanel.addChangeListener(this);
            parametersPanel.add(paramsPanel, BorderLayout.CENTER);
            throwsPanel = new AdsMethodThrowsListEditor();
            throwsPanel.addChangeListener(this);
            throwsPanel.open(method, owner, !canChangeProfile);
            innerSplitPane.setTopComponent(throwsPanel);

            descriptionModel = DescriptionModel.Factory.newInstance(method);
            descriptionPanel.open(descriptionModel);
            descriptionPanel.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    descriptionPanel.commit();
                }
            });

            descriptionPanel.setReadonly(method.isReadOnly());
            descriptionPanel.setMinimumSize(new Dimension(30, 70));
            
            innerSplitPane.setBottomComponent(descriptionPanel);
            outerSplitPane.setTopComponent(parametersPanel);
            outerSplitPane.setBottomComponent(innerSplitPane);
            innerSplitPane.setOneTouchExpandable(true);
            outerSplitPane.setOneTouchExpandable(true);
            innerSplitPane.setBorder(null);
            outerSplitPane.setBorder(null);
            outerSplitPane.setResizeWeight(0.33);
            innerSplitPane.setResizeWeight(0.5);
            c.gridy++;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.fill = GridBagConstraints.BOTH;
            add(outerSplitPane, c);

            if (isDialogComponent) {
                c.gridwidth = 1;
                c.gridx = 0;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridy++;// = method.isOverride() ? 7 : 6;
                c.weightx = 0.0;
                c.weighty = 0.0;
                gbl.setConstraints(apiUsageLabel, c);
                add(apiUsageLabel);

                c.gridx = 1;
                c.fill = GridBagConstraints.HORIZONTAL;
                //c.gridy = method.isOverride() ? 8 : 7;
                c.weightx = 0.0;
                c.weighty = 0.0;

                apiUsageEditor.open(method);
                gbl.setConstraints(apiUsageEditor, c);
                add(apiUsageEditor);

                c.weighty = 0.0;
                c.weightx = 1.0;
                c.gridx = 0;
                c.gridwidth = 2;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridy++;// = method.isOverride() ? 8 : 7;
                StateDisplayer sd = new StateDisplayer();
                gbl.setConstraints(sd, c);
                add(sd);
            }
        }
    }

    private void setupForStrob() {
        setLayout(new BorderLayout());
        constructorName = new LabeledNameEditPanel();
        constructorName.getNameEditor().setEditable(true);
        constructorName.getNameEditor().setCurrentName(method.getName());
        constructorName.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
        add(constructorName, BorderLayout.NORTH);

        if (isDialogComponent) {
            StateDisplayer sd = new StateDisplayer();
            sd.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
            add(sd, BorderLayout.SOUTH);
        }
    }
    private boolean readonly = false;

    final boolean edit(AdsMethodDef m, boolean readonly) {
        method = m;
        this.readonly = readonly;
        if (method != null) {
            setupUI(true, true, method.getOwnerClass(), readonly);
            return true;
        }
        return false;
    }

    public boolean open(AdsMethodDef m, AdsClassDef owner) {
        method = m;
        this.readonly = false;
        if (method != null) {
            setupUI(false, !(m instanceof AdsAlgoStrobMethodDef), owner, false);
            return true;
        }
        return false;
    }

    private boolean isCorrectProfile() {
        if (method.isOverride()) {
            AdsMethodDef overriden = method.getHierarchy().findOverridden().get();
            if (overriden != null) {
                List<AdsTypeDeclaration> currentParameters = new ArrayList<>();//paramsPanel.getCurrentTypes();
                for (MethodParameter p : getParameters()){
                    currentParameters.add(p.getType());
                }
                AdsTypeDeclaration returnType = returnPanel != null ? returnPanel.getReturnType() : AdsTypeDeclaration.VOID;
                
                AdsTypeDeclaration[] params = new AdsTypeDeclaration[currentParameters.size() + 1];
                System.arraycopy(currentParameters.toArray(), 0, params, 0, currentParameters.size());
                params[params.length - 1] = returnType;

                AdsTypeDeclaration[] overParams = overriden.getProfile().getNormalizedProfile();
                List<AdsTypeDeclaration> throwslist = throwsPanel.getCurrentThrowsList();
                List<AdsMethodThrowsList.ThrowsListItem> overThrowsList = overriden.getProfile().getThrowsList().list();

                AdsTypeDeclaration[] fixedOvrParams = ProfileUtilities.convertToContext(overParams, method.getOwnerClass(), overriden.getOwnerClass());
                ProfileUtilities.ProfileCompareResults res = ProfileUtilities.compareProfiles(method, params, fixedOvrParams);
                boolean throwsCheck = true;
                if (throwslist == null && overThrowsList != null) {
                    throwsCheck = false;
                } else if (throwslist != null && overThrowsList == null) {
                    throwsCheck = false;
                } else if (throwslist == null && overThrowsList == null) {
                    throwsCheck = true;
                } else {
                    int i = 0;
                    int size = throwslist.size() - 1;
                    boolean stop = false;
                    while (!stop && i <= size) {
                        AdsType exeptionType = throwslist.get(i).resolve(method).get();
                        if (exeptionType instanceof AdsClassType) {
                            AdsClassDef throwListItemClass = ((AdsClassType) exeptionType).getSource();
                            if (throwListItemClass instanceof AdsExceptionClassDef) {
                                if (!ProfileUtilities.isCorrectExeptionInThrowList((AdsExceptionClassDef) throwListItemClass, method, overThrowsList)) {
                                    throwsCheck = false;
                                    stop = true;
                                }  else {
                                    i++;
                                }
                            }
                        }
                    }
                }

                return res.ok() && throwsCheck;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void fixOverridenProfile() {
        boolean isCorrect = isCorrectProfile();
        if (!isCorrect) {
            if (DialogUtils.messageConfirmation(NbBundle.getMessage(ChangeProfilePanel.class, "FixOverride-NotCorrectTip"))) {
                AdsMethodDef overriden = method.getHierarchy().findOverridden().get();

                ProfileUtilities.syncMethodProfileToOverriden(method);

                if (!overriden.isConstructor()) {
                    if (!overriden.getProfile().getReturnValue().getType().equalsTo(method, method.getProfile().getReturnValue().getType())) {
                        method.getProfile().getReturnValue().setType(overriden.getProfile().getReturnValue().getType());
                        returnPanel.open(method, method.getOwnerClass(), readonly);
                    }
                }

                paramsPanel.update();
                returnPanel.open(method, method.getOwnerClass(), readonly);
                throwsPanel.open(method, method.getOwnerClass(), readonly);
            }
        } else {
            DialogUtils.messageInformation(NbBundle.getMessage(ChangeProfilePanel.class, "FixOverride-NoErrorsTip"));
        }
    }

    public String getCurrentlyDisplayedName() {
        if (returnPanel != null) {
            return returnPanel.getCurrentName();
        } else if (constructorName != null) {
            return constructorName.getNameEditor().getCurrentName();
        } else if (method != null) {
            return method.getName();
        } else {
            return "";
        }
    }

    public AdsTypeDeclaration getCurrentReturnType() {
        return returnPanel != null ? returnPanel.getReturnType() : null;
    }

    public List<MethodParameter> getParameters() {
        return paramsPanel != null ? paramsPanel.getParameters() : null;
    }

    public DescriptionStorage getMethodDescriptionStorage() {
        final DescriptionStorage descriptionStorage = new DescriptionStorage();

        descriptionStorage.putDescription(method.getId(), descriptionModel);

        if (!method.isConstructor()) {
            descriptionStorage.putDescription(RETURN_VALUE_DESCRIPTION_KEY, returnPanel.getDescriptionModel());
        }

        descriptionStorage.putAll(paramsPanel.getDescriptionMap());

        descriptionStorage.putAll(throwsPanel.getDescriptionMap());

        return descriptionStorage;

    }

    public List<AdsTypeDeclaration> getThrowList() {
        return throwsPanel.getCurrentThrowsList();
    }

    @Override
    public void requestFocus() {
        if (constructorName != null) {
            constructorName.requestFocusInWindow();
        } else {
            returnPanel.requestFocus();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        if (method != null && method.isOverride()) {
            return new Dimension(400, 515);
        }
        return new Dimension(400, 475);
    }
    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener l) {
        changeSupport.addChangeListener(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeSupport.removeChangeListener(l);
    }

    public boolean isComplete() {
        if (paramsPanel != null) {
            if (constructorName != null) {
                return constructorName.getNameEditor().isComplete() && paramsPanel.isComplete();
            } else {
                return returnPanel.isComplete() && paramsPanel.isComplete();
            }
        } else {
            if (constructorName != null) {
                return constructorName.getNameEditor().isComplete();
            } else {
                return true;
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        changeSupport.fireChange();
    }

    private void checkAndChangeOvers() {

        OverVisitor visitor = new OverVisitor();
        method.getModule().getSegment().getLayer().getBranch().visitAll(visitor);
        if (visitor.desc.size() > 0) {
            String question = NbBundle.getMessage(ChangeProfilePanel.class, "ChangeConfirmationMessage");
            String message = "\n" + question;
            message += "\n\n";
            for (AdsMethodDef m : visitor.desc) {
                message += m.getQualifiedName() + "\n";
            }
            message += "\n";
            if (DialogUtils.messageConfirmation(message)) {
                for (AdsMethodDef m : visitor.desc) {
                    m.getProfile().syncTo(method);
                    m.setName(method.getName());
                }
            }
        }

    }

    private class OverVisitor implements IVisitor {

        Collection<AdsMethodDef> desc;

        OverVisitor() {
            this.desc = new HashSet<>();
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof AdsMethodDef) {
                if (((Definition) radixObject).getId().equals(method.getId())) {
                    if (DefinitionsUtils.isOverridesOrOverwrites((Definition) radixObject, method)) {
                        desc.add((AdsMethodDef) radixObject);
                    }
                }
            }
        }
    }

}
