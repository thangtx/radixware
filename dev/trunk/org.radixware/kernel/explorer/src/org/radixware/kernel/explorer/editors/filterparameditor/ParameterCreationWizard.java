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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWizard;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameterFactory;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IParameterCreationWizard;
import org.radixware.kernel.common.client.widgets.IPeriodicalTask;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.TimerEventHandler;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.KernelIcon;
import org.radixware.kernel.explorer.types.RdxIcon;


public class ParameterCreationWizard extends QWizard implements IParameterCreationWizard {

    @Override
    public String getObjectName() {
        return objectName();
    }

    @Override
    public void acceptDialog() {
        super.accept();
    }

    @Override
    public boolean isDisposed() {
        return nativeId()==0;
    }       

    @Override
    public DialogResult execDialog() {
        DialogCode nativeResult = DialogCode.resolve(exec());
        switch (nativeResult) {
            case Accepted:
                return DialogResult.ACCEPTED;
            default:
                return DialogResult.REJECTED;
        }
    }

    @Override
    public DialogResult execDialog(IWidget parent) {
        if (parent instanceof QWidget) {
            this.setParent((QWidget) parent);
        }
        DialogCode nativeResult = DialogCode.resolve(exec());
        switch (nativeResult) {
            case Accepted:
                return DialogResult.ACCEPTED;
            default:
                return DialogResult.REJECTED;
        }
    }
    private final EventSupport eventSupport = new EventSupport(this);

    @Override
    public EventSupport getEventSupport() {
        return eventSupport;
    }

    @Override
    public DialogResult getDialogResult() {
        DialogCode nativeResult = DialogCode.resolve(result());
        switch (nativeResult) {
            case Accepted:
                return DialogResult.ACCEPTED;
            default:
                return DialogResult.REJECTED;
        }
    }

    @Override
    public String getWidowTitle() {
        return super.windowTitle();
    }

    @Override
    public Icon getWindowIcon() {
        QIcon icon = super.windowIcon();
        if (icon instanceof Icon) {
            return (Icon) icon;
        } else {
            return new RdxIcon(icon);
        }
    }

    @Override
    public void rejectDialog() {
        reject();
    }

    @Override
    public void setWindowIcon(Icon icon) {
        if (icon instanceof QIcon) {
            super.setWindowIcon((QIcon) icon);
        }
    }

    static enum WizardPages {

        NATURE, BASE_PROPERTY, VALUE_TYPE, ATTRIBUTES
    };
    private final BasePropertyWP bazePropertyPage;
    private final ValueTypeWP valueTypePage;
    private final ParamAttributesWP parameterAttributesPage;
    private ISqmlModifiableParameter parameter;

    public ParameterCreationWizard(IClientEnvironment environment, final ISqmlTableDef context, final ISqmlParameterFactory parameterFactory, final List<String> restrictedNames, final QWidget parent) {
        super(parent);
        setWizardStyle(WizardStyle.ClassicStyle);
        setWindowTitle(Application.translate("SqmlEditor", "Condition Parameter Creation Wizard"));
        setPage(WizardPages.NATURE.ordinal(), new ParamNatureWP());
        bazePropertyPage = new BasePropertyWP(environment, context, parameterFactory);
        setPage(WizardPages.BASE_PROPERTY.ordinal(), bazePropertyPage);
        valueTypePage = new ValueTypeWP(environment,parameterFactory);
        setPage(WizardPages.VALUE_TYPE.ordinal(), valueTypePage);
        parameterAttributesPage = new ParamAttributesWP(environment, context, restrictedNames);
        setPage(WizardPages.ATTRIBUTES.ordinal(), parameterAttributesPage);

        button(QWizard.WizardButton.NextButton).setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_NEXT));
        button(QWizard.WizardButton.BackButton).setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_PREV));
        button(QWizard.WizardButton.CancelButton).setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_CANCEL));
        button(QWizard.WizardButton.FinishButton).setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_OK));

        setButtonText(QWizard.WizardButton.NextButton, Application.translate("SqmlEditor", "&Next"));
        setButtonText(QWizard.WizardButton.BackButton, Application.translate("SqmlEditor", "&Back"));
        final QIcon icon = ExplorerIcon.getQIcon(KernelIcon.getInstance(AdsDefinitionIcon.SQL_CLASS_CUSTOM_PARAMETER));
        setWindowIcon(icon);
    }

    @Override
    public boolean validateCurrentPage() {
        if (super.validateCurrentPage()) {
            if (currentId() == WizardPages.BASE_PROPERTY.ordinal()) {
                parameter = bazePropertyPage.getResultParameter();
            } else if (currentId() == WizardPages.VALUE_TYPE.ordinal()) {
                parameter = valueTypePage.getResultParameter();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void initializePage(int pageId) {
        if (pageId == WizardPages.ATTRIBUTES.ordinal()) {
            parameterAttributesPage.setParameter(parameter);
        }
        super.initializePage(pageId);
    }

    public ISqmlModifiableParameter getParameter() {
        return parameter;
    }

    @Override
    public IPeriodicalTask startTimer(TimerEventHandler handler) {
        throw new UnsupportedOperationException("startTimer is not supported here.");
    }

    @Override
    public void killTimer(IPeriodicalTask task) {
        throw new UnsupportedOperationException("killTimer is not supported here.");
    }        
}
