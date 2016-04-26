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

package org.radixware.kernel.designer.ads.editors.refactoring.pullup;

import java.awt.datatransfer.Transferable;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ETransferType;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.designer.ads.editors.refactoring.IRefactoringOperation;
import org.radixware.kernel.designer.ads.editors.refactoring.OperationStatus;
import org.radixware.kernel.designer.common.dialogs.usages.UsagesFinder;


abstract class PullUpOperation implements IRefactoringOperation {

    public static class Factory {

        private Factory() {
        }

        static PullUpOperation newInstance(PullUpSteps.PullUpSettings settings) {
            if (settings.getDefinition() instanceof AdsMethodDef) {
                return new PullUpMethodOperation(settings);
            } else if (settings.getDefinition() instanceof AdsPropertyDef) {
                return new PropertyPullUpOperation(settings);
            } else if (settings.getDefinition() instanceof AdsClassDef) {
                return new NestedClassPullUpOperation(settings);
            }
            return null;
        }
    }

    private static class PullUpMethodOperation extends PullUpOperation {

        public PullUpMethodOperation(PullUpSteps.PullUpSettings settings) {
            super(settings);
        }

        @Override
        void correction(AdsDefinition copy) {
            super.correction(copy);

            final boolean isDestinationInterface = settings.getDestinationClass() instanceof AdsInterfaceClassDef;
            final boolean isSourceInterface = settings.getSourceClass() instanceof AdsInterfaceClassDef;
            final boolean isSourceMemberPublished = settings.getDefinition().isPublished();

            if (isDestinationInterface) {

                final AdsUserMethodDef userMethod = (AdsUserMethodDef) copy;
                userMethod.getSource().getItems().clear();
                userMethod.setOverride(false);

                ((AdsUserMethodDef) settings.getDefinition()).setOverride(true);

                settings.getDefinition().setAccessMode(EAccess.PUBLIC);
            }

            if (isSourceInterface == isDestinationInterface) {
                final AdsMethodDef method = (AdsMethodDef) settings.getDefinition();

                if (isSourceMemberPublished) {
                    method.getProfile().getAccessFlags().setDeprecated(true);
                    method.afterOverride();
                } else {
                    method.delete();
                }
            }
        }

        @Override
        ClipboardSupport<? extends RadixObject> getClipboardSupport() {
            return settings.getDestinationClass().getMethods().getClipboardSupport();
        }
    }

    private static class PropertyPullUpOperation extends PullUpOperation {

        public PropertyPullUpOperation(PullUpSteps.PullUpSettings settings) {
            super(settings);
        }

        @Override
        void correction(AdsDefinition copy) {
            super.correction(copy);

            final boolean isDestinationInterface = settings.getDestinationClass() instanceof AdsInterfaceClassDef;
            final boolean isSourceInterface = settings.getSourceClass() instanceof AdsInterfaceClassDef;
            final boolean isSourceMemberPublished = settings.getDefinition().isPublished();

            if (isDestinationInterface) {
                final AdsPropertyDef propCopy = (AdsPropertyDef) copy;
                propCopy.setOverride(false);

                ((AdsPropertyDef) settings.getDefinition()).setOverride(true);

                final SearchResult<AdsPropertyDef.Getter> getter = propCopy.findGetter(ExtendableDefinitions.EScope.LOCAL);
                if (!getter.isEmpty()) {
                    getter.get().delete();
                }

                final SearchResult<AdsPropertyDef.Setter> setter = propCopy.findSetter(ExtendableDefinitions.EScope.LOCAL);
                if (!setter.isEmpty()) {
                    setter.get().delete();
                }

                settings.getDefinition().setAccessMode(EAccess.PUBLIC);
            }

            if (isSourceInterface == isDestinationInterface) {
                final AdsPropertyDef prop = (AdsPropertyDef) settings.getDefinition();

                if (isSourceMemberPublished) {
                    prop.getAccessFlags().setDeprecated(true);
                    prop.afterOverride();
                } else {
                    prop.delete();
                }
            }
        }

        @Override
        ClipboardSupport<? extends RadixObject> getClipboardSupport() {
            return settings.getDestinationClass().getProperties().getClipboardSupport();
        }
    }

    private static class NestedClassPullUpOperation extends PullUpOperation {

        public NestedClassPullUpOperation(PullUpSteps.PullUpSettings settings) {
            super(settings);
        }

        @Override
        void correction(AdsDefinition copy) {
            super.correction(copy);

            final boolean isDestinationInterface = settings.getDestinationClass() instanceof AdsInterfaceClassDef;

            if (isDestinationInterface) {
                final AdsClassDef clsCopy = (AdsClassDef) settings.getDefinition();
                clsCopy.getAccessFlags().setStatic(true);
                clsCopy.setAccessMode(EAccess.PUBLIC);
            }

            settings.getDefinition().delete();
        }

        @Override
        ClipboardSupport<? extends RadixObject> getClipboardSupport() {
            return settings.getDestinationClass().getNestedClasses().getClipboardSupport();
        }
    }
    final PullUpSteps.PullUpSettings settings;
    private AdsDefinition result;

    public PullUpOperation(PullUpSteps.PullUpSettings settings) {
        this.settings = settings;
    }

    @Override
    public OperationStatus perform() {
        try {

            result = copy();

            final AdsModule sourceModule = settings.getDefinition().getModule(),
                    destModule = settings.getDestinationClass().getModule();

            correction(result);
            updateLinks(result);
            dependenciesActualize(sourceModule, destModule);

            return OperationStatus.OK;
        } catch (Exception e) {
            return new OperationStatus(new OperationStatus.Event(OperationStatus.EEventType.ERROR, e.getMessage()));
        }
    }

    public AdsDefinition getResult() {
        return result;
    }

    /**
     * Corrects necessary properties of member
     *
     * @param copy copy of member for which perform refactoring, may be null
     */
    void correction(AdsDefinition copy) {
        final boolean isDestinationInterface = settings.getDestinationClass() instanceof AdsInterfaceClassDef;

        if (isDestinationInterface) {
            if (copy != null) {
                copy.setFinal(false);
                copy.setPublished(true);
                copy.setAccessMode(EAccess.PUBLIC);
            }
            settings.getDefinition().setAccessMode(EAccess.PUBLIC);
        }
    }

    private void updateLinks(AdsDefinition copy) {
        final List<RadixObject> usages = UsagesFinder.toList(settings.getSelectedUsages());

        for (final RadixObject radixObject : usages) {
            if (radixObject instanceof JmlTagInvocation) {
                final JmlTagInvocation tag = (JmlTagInvocation) radixObject;
                tag.setPath(new AdsPath(copy));
            }
        }
    }

    private void dependenciesActualize(AdsModule sourceModule, AdsModule destModule) {
        sourceModule.getDependences().actualize();
        if (sourceModule != destModule) {
            destModule.getDependences().actualize();
        }
    }

    private AdsDefinition copy() {
        final ClipboardSupport.DuplicationResolver resolver = new ClipboardSupport.DuplicationResolver() {
            @Override
            public ClipboardSupport.DuplicationResolver.Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
                return ClipboardSupport.DuplicationResolver.Resolution.CANCEL;
            }
        };

        final Transferable transferable = settings.getDefinition().getClipboardSupport().createTransferable(ETransferType.COPY);
        final List<RadixObject> paste = getClipboardSupport().paste(transferable, resolver);

        if (!paste.isEmpty()) {
            final AdsDefinition methodCopy = (AdsDefinition) paste.get(0);
            return methodCopy;
        }
        return null;
    }

    abstract ClipboardSupport<? extends RadixObject> getClipboardSupport();
}
