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

package org.radixware.kernel.designer.common.dialogs.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IInheritableTitledDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.ServerPresentationSupport;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsPropertyPresentationAttributes;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.components.BorderedCollapsablePanel.TopComponent;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfo;
import org.radixware.kernel.designer.common.dialogs.components.localizing.HandleInfoAdapter;
import org.radixware.kernel.designer.common.dialogs.components.localizing.ILocalizingStringContext;
import org.radixware.kernel.designer.common.dialogs.components.localizing.LocalizingEditorPanel;


public final class InheritableTitlePanel extends LocalizingEditorPanel {

    private static abstract class TitledDefinitionAdapter extends HandleInfo implements IInheritableTitledDefinition {

        final Definition definition;

        public TitledDefinitionAdapter(Definition definition) {
            this.definition = definition;
        }

        @Override
        public Definition getAdsDefinition() {
            return findOwnerTitleDefinition();
        }

        @Override
        protected void onRemoveString() {
            setTitleId(null);
        }

        @Override
        protected void onSetString(IMultilingualStringDef multilingualString) {
            if (multilingualString != null) {
                setTitleId(multilingualString.getId());
            }
        }

        @Override
        protected void onLanguagesPatternChange(EIsoLanguage language, String newStringValue) {
            setTitle(language, newStringValue);
        }

        boolean isValid() {
            return true;
        }

        boolean canInherit() {
            return !definition.isReadOnly();
        }

        boolean isReadonly() {
            return definition.isReadOnly();
        }

        final Definition getDefinition() {
            return definition;
        }

        boolean canBeNull() {
            return !(definition instanceof AdsEditorPageDef);
        }

        @Override
        public IMultilingualStringDef getAdsMultilingualStringDef() {
            Id titleId = getTitleId();
            if (titleId != null) {
                return findOwnerTitleDefinition().findLocalizedString(titleId);
            }
            if (findOwnerTitleDefinition() != null) {
                return super.getAdsMultilingualStringDef();
            }
            return null;
        }
    }

    private static class AdapterFactory {

        private static class DefaultAdapter extends TitledDefinitionAdapter {

            final IInheritableTitledDefinition titledDefinition;

            public DefaultAdapter(AdsDefinition titledDefinition) {
                super(titledDefinition);

                this.titledDefinition = (IInheritableTitledDefinition) titledDefinition;
            }

            @Override
            public void setTitleId(Id id) {
                titledDefinition.setTitleId(id);
            }

            @Override
            public Id getTitleId() {
                return titledDefinition.getTitleId();
            }

            @Override
            public boolean setTitleInherited(boolean inherit) {
                return titledDefinition.setTitleInherited(inherit);
            }

            @Override
            public boolean isTitleInherited() {
                return titledDefinition.isTitleInherited();
            }

            @Override
            public Definition findOwnerTitleDefinition() {
                return titledDefinition.findOwnerTitleDefinition();
            }

            @Override
            public String getTitle(EIsoLanguage language) {
                return titledDefinition.getTitle(language);
            }

            @Override
            public boolean setTitle(EIsoLanguage language, String title) {
                return titledDefinition.setTitle(language, title);
            }
        }

        private static class PresentablePropertyAdapter extends TitledDefinitionAdapter {

            final IAdsPresentableProperty presentableProperty;
            final boolean isHint;

            public PresentablePropertyAdapter(AdsDefinition presentableProperty, boolean isHint) {
                super(presentableProperty);

                this.presentableProperty = (IAdsPresentableProperty) presentableProperty;
                this.isHint = isHint;
            }

            @Override
            public boolean isTitleInherited() {
                return isHint
                        ? presentableProperty.getPresentationSupport().getPresentation().isHintInherited()
                        : presentableProperty.getPresentationSupport().getPresentation().isTitleInherited();
            }

            @Override
            public boolean setTitleInherited(boolean inherit) {
                return isHint
                        ? presentableProperty.getPresentationSupport().getPresentation().setHintInherited(inherit)
                        : presentableProperty.getPresentationSupport().getPresentation().setTitleInherited(inherit);
            }

            @Override
            public Id getTitleId() {
                return isHint
                        ? presentableProperty.getPresentationSupport().getPresentation().getHintId()
                        : presentableProperty.getPresentationSupport().getPresentation().getTitleId();
            }

            @Override
            public AdsDefinition findOwnerTitleDefinition() {
                return isHint
                        ? presentableProperty.getPresentationSupport().getPresentation().findHintOwner()
                        : presentableProperty.getPresentationSupport().getPresentation().findTitleOwner();
            }

            @Override
            public void setTitleId(Id id) {
                if (isHint) {
                    presentableProperty.getPresentationSupport().getPresentation().setHintId(id);
                } else {

                    //RADIX-3716
                    if (presentableProperty.getPresentationSupport().getPresentation().getTitleId() == null && id != null) {
                        final AdsDefinition definition = ((AdsDefinition) presentableProperty);
                        final List<EIsoLanguage> lngLst =
                                definition.getModule().getSegment().getLayer().getLanguages();

                        final AdsMultilingualStringDef localizedString = definition.findLocalizedString(id);
                        if (localizedString != null) {
                            for (EIsoLanguage language : lngLst) {
                                if (localizedString.getValue(language) == null) {
                                    localizedString.setValue(language, definition.getName());
                                }
                            }
                        }
                    }
                    presentableProperty.getPresentationSupport().getPresentation().setTitleId(id);
                }
            }

            private boolean isMayInheritTitle() {
                final ServerPresentationSupport presentationSupport = presentableProperty.getPresentationSupport();

                if (presentationSupport != null) {
                    return isHint
                            ? presentationSupport.getPresentation().isMayInheritHint()
                            : presentationSupport.getPresentation().isMayInheritTitle();
                }
                return false;
            }

            @Override
            boolean isValid() {
                return !isTitleInherited() || isMayInheritTitle();
            }

            @Override
            boolean canInherit() {

                boolean isReadOnlyPropHint = false;
                final ServerPresentationSupport presentationSupport = presentableProperty.getPresentationSupport();
                if (presentationSupport != null) {
                    isReadOnlyPropHint = !presentationSupport.getPresentation().isPresentable() && isHint;
                }

                if (isValid()) {
                    return !definition.isReadOnly() && isMayInheritTitle() && !isReadOnlyPropHint;
                }
                return !definition.isReadOnly() && !isReadOnlyPropHint;
            }

            @Override
            public String getTitle(EIsoLanguage language) {
                return isHint
                        ? presentableProperty.getPresentationSupport().getPresentation().getHint(language)
                        : presentableProperty.getPresentationSupport().getPresentation().getTitle(language);
            }

            @Override
            public boolean setTitle(EIsoLanguage language, String title) {
                return isHint
                        ? presentableProperty.getPresentationSupport().getPresentation().setHint(language, title)
                        : presentableProperty.getPresentationSupport().getPresentation().setTitle(language, title);
            }
        }

        private static class PropertyRestrictionAdapter extends TitledDefinitionAdapter {

            private AdsEditorPresentationDef.PropertyAttributesSet attributes;

            public PropertyRestrictionAdapter(AdsEditorPresentationDef.PropertyAttributesSet attributes) {
                super(attributes.getEditorPresentation());
                this.attributes = attributes;
            }

            @Override
            public Id getTitleId() {
                return attributes.getTitleId();
            }

            @Override
            public boolean isTitleInherited() {
                return !attributes.hasLocal() || attributes.getLocal().getTitleId() == null;
            }

            @Override
            public boolean setTitleInherited(boolean inherit) {

                if (inherit) {
                    setTitleId(null);
                } else {
                    final AdsPropertyPresentationAttributes restriction = attributes.getOrCreateLocal();
                    final AdsMultilingualStringDef newTitle = AdsMultilingualStringDef.Factory.newInstance();

                    restriction.setTitleId(newTitle.getId());

                    final AdsDefinition ownDef = RadixObjectsUtils.findContainer(restriction, AdsDefinition.class);
                    ownDef.findLocalizingBundle().getStrings().getLocal().add(newTitle);
                }

                return true;
            }

            @Override
            public AdsDefinition findOwnerTitleDefinition() {
                return attributes.findTitleOwnerDefinition();
            }

            @Override
            public void setTitleId(Id id) {
                attributes.getOrCreateLocal().setTitleId(id);
            }

            @Override
            public boolean setTitle(EIsoLanguage language, String title) {
                final AdsMultilingualStringDef titleStr = attributes.getOrCreateLocal().findTitle();

                if (titleStr != null) {
                    titleStr.setValue(language, title);
                    return true;
                }
                return false;
            }

            @Override
            public String getTitle(EIsoLanguage language) {
                final AdsMultilingualStringDef titleStr = attributes.getOrCreateLocal().findTitle();
                return titleStr != null ? titleStr.getValue(language) : null;
            }

            @Override
            boolean canBeNull() {
                return false;
            }
        }

        static TitledDefinitionAdapter wrap(AdsDefinition definition, boolean isHint) {
            if (definition instanceof IAdsPresentableProperty) {
                return new PresentablePropertyAdapter(definition, isHint);
            }

            if (definition instanceof IInheritableTitledDefinition) {
                return new DefaultAdapter(definition);
            }

            Logger.getLogger(InheritableTitlePanel.class.getName()).log(
                    Level.WARNING, "Definition must be instance of 'IInheritableTitledDefinition'");
            return null;
        }

        static TitledDefinitionAdapter wrap(AdsEditorPresentationDef.PropertyAttributesSet attributes) {
            return new PropertyRestrictionAdapter(attributes);
        }
    }
    private final JCheckBox chkInherit;
    private Color defaultForeground = Color.BLACK;
    private boolean isUpdate = false;
    private TitledDefinitionAdapter adapter;

    public InheritableTitlePanel() {
        this("Inherit title");
    }

    public InheritableTitlePanel(String inheritChbText) {
        super("Define");

        chkInherit = new JCheckBox(inheritChbText);
        chkInherit.setFocusable(false);
        defaultForeground = chkInherit.getForeground();
        chkInherit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isUpdate) {

                    if (!chkInherit.isSelected()) {
                        ILocalizingStringContext localizingStringContext = getLocalizingStringContext();
                        if (localizingStringContext instanceof HandleInfoAdapter) {
                            final IMultilingualStringDef suorceString = ((HandleInfoAdapter) localizingStringContext).getHandleInfo().getAdsMultilingualStringDef();
                            adapter.setTitleInherited(false);

                            // RADIX-6525
                            if (suorceString != null) {
                                for (EIsoLanguage lang : suorceString.getLanguages()) {
                                    adapter.setTitle(lang, suorceString.getValue(lang));
                                }
                                final IMultilingualStringDef str = adapter.getAdsMultilingualStringDef();
                                if (str != null) {
                                    str.setSpellCheckEnabled(suorceString.isSpellCheckEnabled());
                                }
                            }
                        }
                    } else {
                        adapter.setTitleInherited(true);
                    }

                    fireChange();
                    update();
                }
            }
        });
    }

    @Override
    public void setReadonly(boolean readonly) {
        chkInherit.setEnabled(!readonly);

        if (isOpen()) {
            super.setReadonly(readonly || adapter.isTitleInherited());
        } else {
            super.setReadonly(readonly);
        }
    }

    public void open(AdsEditorPresentationDef.PropertyAttributesSet attributes) {
        open(AdapterFactory.wrap(attributes));
    }

    public void open(AdsDefinition definition) {
        open(definition, false);
    }

    public void open(AdsDefinition definition, boolean isHint) {
        open(AdapterFactory.wrap(definition, isHint));
    }

    private void open(TitledDefinitionAdapter adapter) {
        this.adapter = adapter;
        update();
    }

    public void update() {
        isUpdate = true;
        if (adapter instanceof AdapterFactory.PresentablePropertyAdapter && ((AdapterFactory.PresentablePropertyAdapter) adapter).isHint) {
            getOptions().add(Options.MODE_KEY, EEditorMode.MULTILINE);
        }

        super.open(adapter);
        checkState();

        final JPanel editorComponent = getEditor().getComponent();
        if (editorComponent instanceof BorderedCollapsablePanel) {
            final BorderedCollapsablePanel cp = (BorderedCollapsablePanel) editorComponent;

            ((TopComponent) cp.getTopComponent()).addComponent(chkInherit, 0);
            ((TopComponent) cp.getTopComponent()).addComponent(Box.createHorizontalStrut(8), 1);
        }

        revalidate();
        isUpdate = false;
    }

    private void checkState() {
        if (adapter.isValid()) {
            chkInherit.setForeground(defaultForeground);
        } else {
            chkInherit.setForeground(Color.RED);
        }
        setReadonly(adapter.isReadonly());

        if (!adapter.canInherit()) {
            chkInherit.setEnabled(false);
        }

        final boolean titleInherited = adapter.isTitleInherited();
        chkInherit.setSelected(titleInherited);
        super.setReadonly(titleInherited || adapter.isReadonly());

        // disable checkBox "Define"
        if (!adapter.canBeNull()) { 
            final JPanel editorComponent = getEditor().getComponent();
            if (editorComponent instanceof CollapsablePanel) {
                ((CollapsablePanel) editorComponent).freeze(true);
            }
        }
    }

    private boolean isOpen() {
        return adapter != null;
    }
}
