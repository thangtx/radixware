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
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizardPage;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;

public class LibUserFuncWizard extends BaseWizard implements IChooseDefFromList {

    private final JmlEditor editor;
    private AdsLibUserFuncWrapper curLibFunc;
    private Map<String, String> allowedLibNames2PipelineId;
    private MessageProvider messageProvider;
    //private LibFuncProfilePage  libFuncProfilePage;

    public LibUserFuncWizard(final JmlEditor parent) {
        super(parent, (ExplorerSettings) parent.getEnvironment().getConfigStore(), "InvocateWizard");
        editor = parent;
        messageProvider = editor.getEnvironment().getMessageProvider();
        if (editor.getActionProvider() != null) {
            this.allowedLibNames2PipelineId = editor.getActionProvider().getAllowedLibNames2PipelineId();
        }

        final ChooseLibFuncPage chooseLibFuncPage = new ChooseLibFuncPage(this);
        addPage(chooseLibFuncPage);
        //addPage(libFuncProfilePage);
        this.button(WizardButton.NextButton).setEnabled(false);
        //this.button(WizardButton.BackButton).released.connect(this, "btnBackWasClicked()");
        setWindowTitle(messageProvider.translate("JmlEditor", "Select Function from Library"));
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
                final AdsUserFuncDef.Lookup.DefInfo di = ((ListModel) modelIndex.model()).getDefList().get(modelIndex.row());
                if (di != null) {
                    //curLibFunc = editor.getActionProvider().getUserFunc(di.getClassId(), di.getMethodId(), di.getId());//AdsUserFuncDef.Lookup.lookup(editor.getUserFunc().getBranch(), di.getClassId(), di.getMethodId(), di.getId(), null, null, null, null, null);
                    final Id id = di.getPath()[0];
                    curLibFunc = (AdsLibUserFuncWrapper) AdsUserFuncDef.Lookup.findTopLevelDefinition(editor.getUserFunc(), id);
                    if (curLibFunc != null) {
                        return true;
                    }
                    //if ((curLibFunc instanceof AdsEnumDef) || (curLibFunc instanceof AdsClassDef) || (curLibFunc instanceof AdsDomainDef)) {
                    //curDef = curItem;
                    //  return false;
                    //} else {
                    //    return true;
                    //}
                }
            } //else {
            //   curLibFunc = (Definition) ((ListModelForRadixObj) modelIndex.model()).getDefList().get(modelIndex.row());
            //   return true;
            //}
        }

        this.button(WizardButton.NextButton).setEnabled(false);
        this.button(WizardButton.FinishButton).setEnabled(false);
        return false;
    }

    public AdsLibUserFuncWrapper getSelectedDef() {
        if (curLibFunc.getName() == null || curLibFunc.getName().isEmpty()) {
            curLibFunc.setName("testname");
        }
        return curLibFunc;
    }

    public List<String> getParams() {
        return null;//libFuncProfilePage.getParams();
    }

    @Override
    public boolean setSelectedDefinition(final AdsDefinition def) {
        return false;
    }

    private class ChooseLibFuncPage extends QWizardPage {

        private final ChooseDefinitionPanel chooseDefPanel;
        private QCheckBox onlyLocalFunctionsCheckBox;
        private AdsUserFuncDef.Lookup.IDefInfoFilter localAndCommonFunctions;
        private AdsUserFuncDef.Lookup.IDefInfoFilter onlyLocalFunctions;

        public ChooseLibFuncPage(final LibUserFuncWizard parent) {
            super(parent);
            this.setObjectName("ChooseLibFuncPage");
            final QVBoxLayout layout = new QVBoxLayout();
            layout.setMargin(0);
            final Set<EDefType> templList = EnumSet.noneOf(EDefType.class);
            templList.add(EDefType.USER_FUNC);

            if (allowedLibNames2PipelineId != null) {
                localAndCommonFunctions = new AdsUserFuncDef.Lookup.IDefInfoFilter() {
                    @Override
                    public boolean isTarget(AdsUserFuncDef.Lookup.DefInfo def) {
                        return allowedLibNames2PipelineId.containsKey(def.getModuleName());
                    }
                };
                onlyLocalFunctions = new AdsUserFuncDef.Lookup.IDefInfoFilter() {
                    @Override
                    public boolean isTarget(AdsUserFuncDef.Lookup.DefInfo def) {
                        if (allowedLibNames2PipelineId.containsKey(def.getModuleName())) {
                            return allowedLibNames2PipelineId.get(def.getModuleName()) != null;
                        }
                        return false;
                    }
                };
            }
            chooseDefPanel = new ChooseDefinitionPanel(parent, null, editor.getUserFunc(), templList, false, false, null, localAndCommonFunctions);
            chooseDefPanel.setObjectName("ChooseDefinitionPanel");
            //if(objTempl ==AdsDomainDef.class)
            //    this.setFinalPage(true);
            layout.addWidget(chooseDefPanel);
            if (allowedLibNames2PipelineId != null) {
                QHBoxLayout buttonsLayout = new QHBoxLayout();
                buttonsLayout.setContentsMargins(8, 0, 8, 0);
                onlyLocalFunctionsCheckBox = new QCheckBox(messageProvider.translate("JmlEditor", "Only local functions"), parent);
                onlyLocalFunctionsCheckBox.toggled.connect(this, "onOnlyLocalFunctionsCheckBoxToggled(Boolean)");
                chooseDefPanel.onDataReadySignal.connect(this, "onDataReadySignal()");
                onlyLocalFunctionsCheckBox.setEnabled(false);
                buttonsLayout.addWidget(onlyLocalFunctionsCheckBox);
                layout.addLayout(buttonsLayout);
            }

            this.setLayout(layout);
            LibUserFuncWizard.this.button(WizardButton.NextButton).setEnabled(false);
        }

        @SuppressWarnings("unused")
        private void onOnlyLocalFunctionsCheckBoxToggled(Boolean isEnabled) {
            if (isEnabled) {
                chooseDefPanel.setDefFilter(onlyLocalFunctions);
            } else {
                chooseDefPanel.setDefFilter(localAndCommonFunctions);
            }
        }

        @SuppressWarnings("unused")
        private void onDataReadySignal() {
            onlyLocalFunctionsCheckBox.setEnabled(true);
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
}
