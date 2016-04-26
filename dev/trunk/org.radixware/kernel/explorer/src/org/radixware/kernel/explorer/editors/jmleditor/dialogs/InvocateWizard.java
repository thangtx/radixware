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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizardPage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.env.ExplorerSettings;


public class InvocateWizard extends BaseWizard implements IChooseDefFromList {

    private JmlEditor editor;
    private Definition curItem;
    private Definition curDef;
   // private Collection<DefInfo> definitionList;
    private Class<? extends Definition> objTempl;
    private Set<EDefType> templLists;

    public InvocateWizard(final JmlEditor parent, final String title, final Class<? extends Definition> objTempl, final Set<EDefType> templLists) {
        super(parent, (ExplorerSettings)parent.getEnvironment().getConfigStore(), "InvocateWizard");
        editor = parent;
        this.objTempl = objTempl;
        this.templLists = templLists;
        InvocatePage invocatePage = new InvocatePage(this);
        ClassPage classPage = new ClassPage(this);
        addPage(classPage);
        addPage(invocatePage);
        this.button(WizardButton.NextButton).setEnabled(false);
        this.button(WizardButton.BackButton).released.connect(this, "btnBackWasClicked()");
        setWindowTitle(title);
    }

    @SuppressWarnings("unused")
    private void btnBackWasClicked() {
        curItem = curDef;
    }

    @Override
    public void accept() {
        super.accept();
    }

    public AdsDefinition getSelectedDef() {
        return (AdsDefinition) curItem;
    }

    @Override
    public void onItemClick(final QModelIndex modelIndex) {
        setCurItem(modelIndex);
    }

    @Override
    public void onItemDoubleClick(final QModelIndex modelIndex) {
        if (setCurItem(modelIndex)) {
            accept();
        } else {
            this.button(WizardButton.NextButton).clicked.emit(Boolean.TRUE);
        }
    }

    @Override
    public boolean setCurItem(final QModelIndex modelIndex) {
        if (modelIndex != null) {
            this.button(WizardButton.NextButton).setEnabled(true);
            this.button(WizardButton.FinishButton).setEnabled(true);
            if (modelIndex.model() instanceof ListModel) {
                final DefInfo di = ((ListModel) modelIndex.model()).getDefList().get(modelIndex.row());
                if (di != null) {
                    curItem = di.getDefinition();//Lookup.findTopLevelDefinition(editor.getUserFunc(), di.getId());
                    if ((curItem instanceof AdsEnumDef) || (curItem instanceof AdsClassDef) || (curItem instanceof AdsDomainDef)) {
                        curDef = curItem;
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                curItem = (Definition) ((ListModelForRadixObj) modelIndex.model()).getDefList().get(modelIndex.row());
                return true;
            }
        }

        this.button(WizardButton.NextButton).setEnabled(false);
        this.button(WizardButton.FinishButton).setEnabled(false);
        return false;
    }

    List<RadixObject> getInvocateItems(final Definition definition) {
        final List<RadixObject> allowedDefinitions = new ArrayList<>();

        if (definition instanceof AdsClassDef ) {            
            //if(definition instanceof AdsClassDef){
                final AdsClassDef adsClassDef=(AdsClassDef)definition;
                if(objTempl == AdsPropertyDef.class || objTempl==null){
                    allowedDefinitions.addAll(adsClassDef.getProperties().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE));
                }
                if(objTempl == AdsMethodDef.class || objTempl==null){
                    allowedDefinitions.addAll(adsClassDef.getMethods().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE));
                }
                
            /*}else{
                List<AdsDefinition> defs=new ArrayList<>();            
                defs.add((AdsDefinition)definition);
                defs.addAll(((AdsDefinition) definition).getHierarchy().findOverwriteBase().all());

                for(AdsDefinition def:defs){
                    def.visitChildren(new IVisitor() {

                        @Override
                        public void accept(RadixObject radixObject) {
                            if ((radixObject instanceof AdsDefinition) && !allowedDefinitions.contains(radixObject)) {
                                allowedDefinitions.add(((AdsDefinition) radixObject));
                            }
                        }
                    }, new VisitorProvider() {

                        @Override
                        public boolean isTarget(RadixObject object) {
                            if (objTempl == null) {
                                if ((object instanceof AdsEnumItemDef) || (object instanceof AdsMethodDef)
                                        || (object instanceof AdsPropertyDef)) {
                                    return true;
                                }
                            } else {
                                if (objTempl.isInstance(object)) {
                                    return true;
                                }
                            }
                            return false;
                        }
                    });
                }
            }*/
        }
        return allowedDefinitions;
    }

    @Override
    public boolean setSelectedDefinition(final AdsDefinition def) {
        return false;
    }

    private class ClassPage extends QWizardPage {

        private ChooseDefinitionPanel chooseDefPanel;

        public ClassPage(final InvocateWizard parent) {
            super(parent);
            this.setObjectName("ClassPage");
            final QVBoxLayout layout = new QVBoxLayout();
            layout.setMargin(0);
            chooseDefPanel = new ChooseDefinitionPanel(parent, null/*definitionList*/, editor.getUserFunc(), templLists, false,false);
            chooseDefPanel.setObjectName("ChooseDefinitionPanel");
            if(objTempl ==AdsDomainDef.class)
                this.setFinalPage(true);
            layout.addWidget(chooseDefPanel);
            this.setLayout(layout);
            InvocateWizard.this.button(WizardButton.NextButton).setEnabled(false);
        }

        public ChooseDefinitionPanel getChooseDefPanel() {
            return chooseDefPanel;
        }

        @Override
        public boolean isComplete() {
            return chooseDefPanel.isComplete();
        }

        @Override
        public void cleanupPage() {
            chooseDefPanel.closeTread();
        }
    }

    private class InvocatePage extends QWizardPage {

        private ChooseObjectPanel chooseObjectPanel;
        private boolean enabled = false;

        public InvocatePage(final InvocateWizard parent) {
            super(parent);
            this.setObjectName("InvocatePage");
            final QVBoxLayout layout = new QVBoxLayout();
            layout.setMargin(0);
            chooseObjectPanel = new ChooseObjectPanel(parent, null);
            chooseObjectPanel.setObjectName("ChooseObjectPanel");
            layout.addWidget(chooseObjectPanel);
            this.setLayout(layout);
        }

        @Override
        public void initializePage() {
            final List<RadixObject> allowedDefinitions = getInvocateItems(curItem);
            chooseObjectPanel.update(allowedDefinitions);
            if (!allowedDefinitions.isEmpty()) {
                enabled = true;
            } else {
                enabled = false;
            }
        }

        @Override
        public boolean isComplete() {
            return enabled;
        }
    }
}
