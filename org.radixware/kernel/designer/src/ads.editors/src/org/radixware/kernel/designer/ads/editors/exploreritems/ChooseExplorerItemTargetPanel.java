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

/*
 * ChooseExplorerItemTargetPanel.java
 *
 * Created on Jun 1, 2009, 7:33:04 AM
 */
package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsChildRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsEntityExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphLinkExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionSequence;
import org.radixware.kernel.designer.common.dialogs.components.DefinitionLinkEditPanel;
import org.radixware.kernel.designer.common.dialogs.components.state.StateManager;


public class ChooseExplorerItemTargetPanel extends DefinitionLinkEditPanel {

    /**
     * Creates new form ChooseExplorerItemTargetPanel
     */
    public ChooseExplorerItemTargetPanel() {
        this.setClearable(false);
    }

    private class ChooseEntityObjectClassCfg extends ChooseDefinitionCfg {

        public ChooseEntityObjectClassCfg() {
            super(context, AdsVisitorProviders.newClassVisitorProvider(EnumSet.of(EClassType.ENTITY, EClassType.APPLICATION)));
            this.setTypesTitle("Choose Referenced Class");
        }
    }

    private class ChooseSelectorPresentationCfg extends ChooseDefinitionCfg {

        public ChooseSelectorPresentationCfg(AdsEntityObjectClassDef clazz) {
            super(clazz.getPresentations().getSelectorPresentations().get(EScope.ALL));
            this.setTypesTitle("Choose Selector Presentation");
        }
    }

    private class ChooseOutgoingReferenceCfg extends ChooseDefinitionCfg {

        public ChooseOutgoingReferenceCfg() {
            super(contextTable.collectOutgoingReferences(new IFilter<DdsReferenceDef>() {
                @Override
                public boolean isTarget(DdsReferenceDef object) {
                    return object.getType() != DdsReferenceDef.EType.MASTER_DETAIL;
                }
            }));
            this.setTypesTitle("Choose Outgoing Reference");
        }
    }

    private class ChooseIncomingReferenceCfg extends ChooseDefinitionCfg {

        public ChooseIncomingReferenceCfg() {
            super(contextTable.collectIncomingReferences(new IFilter<DdsReferenceDef>() {
                @Override
                public boolean isTarget(DdsReferenceDef object) {
                    return object.getType() != DdsReferenceDef.EType.MASTER_DETAIL;

                }
            }));
            this.setTypesTitle("Choose Incoming Reference");
        }
    }

    private class ChooseTableClassCfg extends ChooseDefinitionCfg {

        public ChooseTableClassCfg(final Id tableId, final boolean selectorPresentationsRequired) {
            super(context, AdsVisitorProviders.newClassVisitorProvider(EnumSet.of(EClassType.ENTITY, EClassType.APPLICATION), new IFilter<AdsClassDef>() {
                @Override
                public boolean isTarget(AdsClassDef object) {
                    if (object instanceof AdsEntityObjectClassDef) {
                        if (((AdsEntityObjectClassDef) object).getEntityId() == tableId) {
                            if (selectorPresentationsRequired) {
                                if (((AdsEntityObjectClassDef) object).getPresentations().getSelectorPresentations().get(EScope.ALL).isEmpty()) {
                                    return false;
                                } else {
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }));
            this.setTypesTitle("Choose Referenced Class");
        }
    }

    enum EMode {

        CHILD_REF, PARENT_REF, TABLE, LINK
    }

    private class ChooseParagraphCfg extends ChooseDefinitionCfg {

        public ChooseParagraphCfg() {
            super(context, AdsVisitorProviders.newParagraphVisitorProvider(true));
            this.setTypesTitle("Choose Paragraph");
        }
    }

    private class ChooseParagraphLinkTargetCfgs extends ChooseDefinitionSequence.ChooseDefinitionCfgs {

        public ChooseParagraphLinkTargetCfgs() {
            super(new ChooseParagraphCfg());
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
            return null;
        }

        @Override
        protected boolean hasNextConfig(Definition choosenDef) {
            return false;
        }

        @Override
        protected boolean isFinalTarget(Definition choosenDef) {
            return choosenDef instanceof AdsParagraphExplorerItemDef;
        }

        @Override
        public String getDisplayName() {
            return "Choose Referenced Paragraph";
        }
    }

    private class ChooseEntityExplorerItemTargetCfgs extends ChooseDefinitionSequence.ChooseDefinitionCfgs {

        ChooseEntityExplorerItemTargetCfgs() {
            super(mode == EMode.TABLE ? new ChooseEntityObjectClassCfg() : (mode == EMode.CHILD_REF ? new ChooseIncomingReferenceCfg() : new ChooseOutgoingReferenceCfg()));
        }

        @Override
        public String getDisplayName() {
            switch (mode) {
                case CHILD_REF:
                case TABLE:
                    return "Choose Referenced Selector Presentation";
                case PARENT_REF:
                    return "Choose Referenced Class";
                default:
                    return "";
            }
        }

        @Override
        protected boolean hasNextConfig(Definition choosenDef) {
            if (mode == EMode.PARENT_REF) {
                return choosenDef instanceof DdsReferenceDef;
            } else {
                return choosenDef instanceof DdsReferenceDef || choosenDef instanceof AdsEntityObjectClassDef;
            }
        }

        @Override
        protected boolean isFinalTarget(Definition choosenDef) {
            if (mode == EMode.CHILD_REF || mode == EMode.TABLE) {
                return choosenDef instanceof AdsSelectorPresentationDef;
            } else {
                return choosenDef instanceof AdsEntityObjectClassDef;
            }
        }

        @Override
        protected ChooseDefinitionCfg nextConfig(Definition choosenDef) {
            if (choosenDef instanceof DdsReferenceDef) {
                DdsReferenceDef ref = (DdsReferenceDef) choosenDef;
                return new ChooseTableClassCfg(mode == EMode.CHILD_REF ? ref.getChildTableId() : ref.getParentTableId(), true);
            } else if (choosenDef instanceof AdsEntityObjectClassDef) {
                return new ChooseSelectorPresentationCfg((AdsEntityObjectClassDef) choosenDef);
            } else {
                return null;
            }
        }
    }
    private AdsExplorerItemDef context = null;
    private AdsEditorPresentationDef contextEditorPresentation = null;
    private AdsEntityObjectClassDef contextClass = null;
    private DdsTableDef contextTable = null;
    private EMode mode;

    public void open(AdsExplorerItemDef ei) {
        ChooseDefinitionSequence.ChooseDefinitionCfgs cfgIter = null;
        Definition referencedDef = null;
        Id referencedDefId = null;
        this.context = ei;
        if (ei instanceof AdsEntityExplorerItemDef) {
            mode = EMode.TABLE;
            AdsEntityExplorerItemDef eei = (AdsEntityExplorerItemDef) ei;
            referencedDef = eei.findReferencedSelectorPresentation().get();
            referencedDefId = eei.getSelectorPresentationId();
            cfgIter = new ChooseEntityExplorerItemTargetCfgs();
        } else if (ei instanceof AdsParentRefExplorerItemDef) {
            mode = EMode.PARENT_REF;
            AdsParentRefExplorerItemDef prei = (AdsParentRefExplorerItemDef) ei;
            referencedDef = prei.findReferencedEntityClass();
            referencedDefId = prei.getClassId();
        } else if (ei instanceof AdsChildRefExplorerItemDef) {
            mode = EMode.CHILD_REF;
            AdsChildRefExplorerItemDef crei = (AdsChildRefExplorerItemDef) ei;
            referencedDef = crei.findReferencedSelectorPresentation().get();
            referencedDefId = crei.getSelectorPresentationId();
        } else if (ei instanceof AdsParagraphLinkExplorerItemDef) {
            mode = EMode.LINK;
            AdsParagraphLinkExplorerItemDef plei = (AdsParagraphLinkExplorerItemDef) ei;
            referencedDef = plei.findReferencedParagraph();
            referencedDefId = plei.getReferencedParagraphId();
            cfgIter = new ChooseParagraphLinkTargetCfgs();
        } else {
            throw new IllegalStateException();
        }
        if (mode == EMode.PARENT_REF || mode == EMode.CHILD_REF) {
            contextEditorPresentation = ei.findOwnerEditorPresentation();
            if (contextEditorPresentation != null) {
                contextClass = contextEditorPresentation.getOwnerClass();
                if (contextClass != null) {
                    contextTable = contextClass.findTable(ei);
                    if (contextTable != null) {
                        cfgIter = new ChooseEntityExplorerItemTargetCfgs();
                    }
                }
            }
        }

        super.open(cfgIter, referencedDef, referencedDefId);
    }
    private StateManager stateManager = new StateManager(this);

    public boolean isComplete() {
        switch (mode) {
            case CHILD_REF:
                AdsChildRefExplorerItemDef cr = (AdsChildRefExplorerItemDef) context;
                if (cr.findReferencedSelectorPresentation().get() != null) {
                    stateManager.ok();
                    return true;
                } else {
                    if (cr.getSelectorPresentationId() == null) {
                        stateManager.error("Referenced selector presentation is not specified");
                    } else {
                        stateManager.error("Referenced selector presentation not found");
                    }
                    return false;
                }
            case PARENT_REF:
                AdsParentRefExplorerItemDef pr = (AdsParentRefExplorerItemDef) context;
                if (pr.findReferencedEntityClass() != null) {
                    stateManager.ok();
                    return true;
                } else {
                    if (pr.getClassId() == null) {
                        stateManager.error("Referenced class is not specified");
                    } else {
                        stateManager.error("Referenced class not found");
                    }
                    return false;
                }
            case TABLE:
                AdsEntityExplorerItemDef er = (AdsEntityExplorerItemDef) context;
                if (er.findReferencedSelectorPresentation().get() != null) {
                    stateManager.ok();
                    return true;
                } else {
                    if (er.getSelectorPresentationId() == null) {
                        stateManager.error("Referenced selector presentation is not specified");
                    } else {
                        stateManager.error("Referenced selector presentation not found");
                    }
                    return false;
                }

            case LINK:
                AdsParagraphLinkExplorerItemDef pl = (AdsParagraphLinkExplorerItemDef) context;
                if (pl.findReferencedParagraph() != null) {
                    stateManager.ok();
                    return true;
                } else {
                    if (pl.getReferencedParagraphId() == null) {
                        stateManager.error("Referenced paragraph is not specified");
                    } else {
                        stateManager.error("Referenced paragraph not found");
                    }
                    return false;
                }
            default:
                return false;
        }
    }

    @Override
    protected void onChange() {
        List<Definition> definitions = getLastSelectedDefinitionSequence();
        switch (mode) {
            case CHILD_REF:
                if (definitions.size() == 3) {
                    AdsChildRefExplorerItemDef cr = (AdsChildRefExplorerItemDef) context;
                    cr.setChildReferenceId(definitions.get(0).getId());
                    cr.setClassId(definitions.get(1).getId());
                    cr.setSelectorPresentationId(definitions.get(2).getId());
                }
                break;
            case PARENT_REF:
                if (definitions.size() == 2) {
                    AdsParentRefExplorerItemDef pr = (AdsParentRefExplorerItemDef) context;
                    pr.setParentReferenceId(definitions.get(0).getId());
                    pr.setClassId(definitions.get(1).getId());
                }
                break;
            case TABLE:
                if (definitions.size() == 2) {
                    AdsEntityExplorerItemDef er = (AdsEntityExplorerItemDef) context;
                    er.setClassId(definitions.get(0).getId());
                    er.setSelectorPresentationId(definitions.get(1).getId());
                }
                break;
            case LINK:
                if (definitions.size() == 1) {
                    AdsParagraphLinkExplorerItemDef pl = (AdsParagraphLinkExplorerItemDef) context;
                    pl.setReferencedParagraphId(definitions.get(0).getId());
                }
                break;
        }
    }

    public String getLabelText() {
        switch (mode) {
            case CHILD_REF:
            case TABLE:
                return "Selector presentation";
            case PARENT_REF:
                return "Class";
            case LINK:
                return "Paragraph";
            default:
                return "";
        }
    }
}
