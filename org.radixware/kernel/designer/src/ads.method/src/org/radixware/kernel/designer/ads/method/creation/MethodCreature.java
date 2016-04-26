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

package org.radixware.kernel.designer.ads.method.creation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoStrobMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.agents.IObjectAgent;
import org.radixware.kernel.common.utils.agents.WrapAgent;
import org.radixware.kernel.designer.ads.editors.base.EnvSelectorPanel;
import org.radixware.kernel.designer.ads.method.profile.ChangeProfilePanel;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionStorage;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.wizards.WizardSteps.Step;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.Creature;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreatureSetupStep;
import org.radixware.schemas.adsdef.MethodDefinition;


public class MethodCreature extends Creature<AdsMethodDef> {

    public enum EMethodType {

        USER_DEF(0),
        CONSTRUCTOR(1),
        TRANSPARENT(2),
        ALGO_METHOD(3),
        ALGO_STROB(4),
        RPC(5);
        public final int id;

        private EMethodType(int id) {
            this.id = id;
        }

        public static EMethodType getById(int id) {
            for (EMethodType methodType : values()) {
                if (methodType.id == id) {
                    return methodType;
                }
            }
            return null;
        }
    }
    private final Map<EMethodType, AdsMethodDef> instances = new HashMap<>();
    private final IObjectAgent<AdsMethodGroup> agent;

    public MethodCreature(final IObjectAgent<AdsMethodGroup> agent, EMethodType methodType) {
        super(new WrapAgent<RadixObjects, AdsMethodGroup>(agent) {
            @Override
            public RadixObjects getObject() {
                return getObjectSource().getOwnerClass().getMethods().getLocal();
            }
        });

        this.agent = agent;
        this.methodType = methodType;
    }

    final AdsMethodGroup getGroup() {
        return agent.getObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Creature.WizardInfo getWizardInfo() {
        return new Creature.WizardInfo() {
            @Override
            public CreatureSetupStep createFirstStep() {
                if (methodType == EMethodType.ALGO_METHOD || methodType == EMethodType.ALGO_STROB) {
                    return new MethodSetupStep2();
                }
                if (getGroup().getOwnerClass() instanceof AdsInterfaceClassDef || getGroup().getOwnerClass().isAnonymous()) {
                    return new MethodSetupStep2();
                }
                return new MethodSetupStep1();
            }

            @Override
            public boolean hasWizard() {
                return true;
            }
        };
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.Method.METHOD;
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(MethodCreature.class, "CreatureDescription");
    }

    @Override
    public String getDisplayName() {
        switch (methodType) {
            case ALGO_STROB:
                return NbBundle.getMessage(MethodCreature.class, "AlgoCreatureDisplay");
            default:
                return NbBundle.getMessage(MethodCreature.class, "CreatureDisplay");
        }
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && (getGroup() != null ? !getGroup().isReadOnly() : false);
    }

    @Override
    public AdsMethodDef createInstance() {
        AdsMethodDef instance = getInstance();

        if (instance instanceof AdsTransparentMethodDef) {
            MethodDefinition xDef = MethodDefinition.Factory.newInstance();
            instance.appendTo(xDef, ESaveMode.NORMAL);
            return AdsMethodDef.Factory.loadFrom(xDef);
        } else {
            return instance.getClipboardSupport().duplicate();
        }
    }

    private AdsMethodDef getInstance() {
        AdsMethodDef instance = instances.get(methodType);
        if (instance == null) {
            if (methodType == EMethodType.RPC) {
                instance = AdsRPCMethodDef.Factory.newTemoporaryInstance(agent.getObject().getOwnerClass(), getRpcMethod());
            } else if (methodType == EMethodType.USER_DEF) {
                instance = AdsUserMethodDef.Factory.newTemporaryInstance(getContainer());
            } else if (methodType == EMethodType.CONSTRUCTOR) {
                instance = AdsUserMethodDef.Factory.newConstructorTemporaryInstance(getContainer());
            } else if (methodType == EMethodType.ALGO_METHOD) {
                instance = AdsAlgoMethodDef.Factory.newTemporaryInstance(getContainer());
            } else if (methodType == EMethodType.ALGO_STROB) {
                instance = AdsAlgoStrobMethodDef.Factory.newTemporaryInstance(getContainer());
            }
            instances.put(methodType, instance);
        }
        return instance;
    }

    @Override
    public boolean afterCreate(AdsMethodDef object) {
        if (object != null) {
            if (object instanceof AdsRPCMethodDef) {
            } else if (object instanceof AdsAlgoStrobMethodDef) {
                object.setName(methodName);
            } else {
                if (!object.isConstructor()) {
                    object.setName(methodName);
                    object.getProfile().getReturnValue().setType(methodReturnValue);
//                    object.getProfile().getReturnValue().setDescription(methodReturnValueDescription);
                }

                AdsMethodThrowsList tList = object.getProfile().getThrowsList();
                for (AdsTypeDeclaration ex : throwList) {
                    tList.add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(ex));
                }
            }

            return true;
        }
        return false;
    }

    private void setMethodDescription(AdsMethodDef method) {
        AdsMethodDef instance = getInstance();

        descriptionStorage.setDescription(instance.getId(), method);
        descriptionStorage.setDescription(ChangeProfilePanel.RETURN_VALUE_DESCRIPTION_KEY, method.getProfile().getReturnValue());

        for (MethodParameter methodParameter : method.getProfile().getParametersList()) {
            descriptionStorage.setDescription(ChangeProfilePanel.METHOD_PARAMETR_DESCRIPTION_KEY
                    + methodParameter.getName(), methodParameter);
        }

        for (AdsMethodThrowsList.ThrowsListItem throwsListItem : method.getProfile().getThrowsList()) {
            descriptionStorage.setDescription(ChangeProfilePanel.THROW_ITEM_DESCRIPTION_KEY
                    + throwsListItem.getException().getQualifiedName(), throwsListItem);
        }
    }

    @Override
    public void afterAppend(final AdsMethodDef object) {
        getGroup().addMember(object);

        if (descriptionStorage != null) {
            setMethodDescription(object);
        }

        if (object instanceof AdsRPCMethodDef) {
            final AdsRPCMethodDef method = (AdsRPCMethodDef) object;
            try {
                method.updateComponents();
            } catch (RadixError e) {
                DialogUtils.messageError(e.getMessage());
            }

            final AdsModule radixMetaModule = method.findRadixMetaModule();
            if (radixMetaModule != null) {
                object.getModule().getDependences().add(radixMetaModule);
            }
        }

        if (EnvSelectorPanel.isMeaningFullFor(object)) {
            AdsMethodDef instance = getInstance();
            object.setUsageEnvironment(instance.getUsageEnvironment());
        }
    }
    //SETTINGS
    private EMethodType methodType = EMethodType.USER_DEF;
    private String methodName;
    private List<MethodParameter> methodParameters;
    private AdsTypeDeclaration methodReturnValue;
//    private String methodReturnValueDescription;
//    private String methodDescription;
    private DescriptionStorage descriptionStorage;
    private AdsMethodDef rpcMethod;
    private List<AdsTypeDeclaration> throwList;

    public EMethodType getMethodType() {
        return this.methodType;
    }

    public void setMethodType(EMethodType methodType) {
        this.methodType = methodType;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setRpcMethod(AdsMethodDef method) {
        this.rpcMethod = method;
    }

    public AdsMethodDef getRpcMethod() {
        return rpcMethod;
    }

    public void setMethodParameters(List<MethodParameter> methodParameters) {
        this.methodParameters = methodParameters;
    }

    public void setMethodReturnValue(AdsTypeDeclaration methodReturnValue) {
        this.methodReturnValue = methodReturnValue;
    }

//    public void setMethodReturnValueDescription(String desc) {
//        this.methodReturnValueDescription = desc;
//    }
//    public void setMethodDescription(String desc) {
//        this.methodDescription = desc;
//    }
    public void setMethodThrowList(List<AdsTypeDeclaration> list) {
        this.throwList = list;
    }

    public void setDescriptionStorage(DescriptionStorage descriptionStorage) {
        this.descriptionStorage = descriptionStorage;
    }

    class MethodSetupStep1 extends CreatureSetupStep<MethodCreature, MethodSetupStep1Visual> {

        public MethodSetupStep1() {
            super();
        }

        @Override
        public synchronized MethodSetupStep1Visual createVisualPanel() {
            return new MethodSetupStep1Visual();
        }

        @Override
        public void open(final MethodCreature creature) {
            final MethodSetupStep1Visual panel = getVisualPanel();
            panel.open(creature.getGroup().getOwnerClass());
            if (panel.getSelectedType() != null) {
                creature.setMethodType(panel.getSelectedType());
            }
            panel.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    fireChange();
                    creature.setMethodType(panel.getSelectedType());
                }
            });
        }

        @Override
        public String getDisplayName() {
            return NbBundle.getMessage(MethodSetupStep1.class, "SetupStep1");
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }

        @Override
        public boolean isFinishiable() {
            return false;
        }

        @Override
        public boolean hasNextStep() {
            return true;
        }

        @Override
        public Step createNextStep() {
            return new MethodSetupStep2();
        }
    }

    class MethodSetupStep2 extends CreatureSetupStep<MethodCreature, MethodSetupStep2Visual> {

        MethodSetupStep2() {
            super();
        }

        @Override
        public MethodSetupStep2Visual createVisualPanel() {
            return new MethodSetupStep2Visual();
        }

        @Override
        public void open(MethodCreature creature) {
            final MethodSetupStep2Visual panel = getVisualPanel();
            ChangeListener listener = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    MethodSetupStep2.this.fireChange();
                }
            };
            panel.addChangeListener(listener);
            if (creature.getMethodType() == EMethodType.RPC) {
                panel.openRpc((AdsRPCMethodDef) creature.getInstance());
            } else {
                panel.open(creature.getInstance(), creature.getGroup().getOwnerClass());
            }
        }

        @Override
        public void apply(MethodCreature creature) {
            if (creature.getMethodType() != EMethodType.TRANSPARENT) {
                if (creature.getMethodType() == EMethodType.RPC) {
                    creature.setRpcMethod(getVisualPanel().getRPCMethod());
                } else {
                    creature.setMethodName(getVisualPanel().getMethodName());
                    creature.setMethodReturnValue(getVisualPanel().getMethodReturnType());
                    creature.setMethodParameters(getVisualPanel().getMethodParameters());
//                    creature.setMethodReturnValueDescription(getVisualPanel().getMethodReturnValueDescription());
//                    creature.setMethodDescription(getVisualPanel().getMethodDescription());
                    if (creature.getMethodType() != EMethodType.ALGO_STROB) {
                        creature.setMethodThrowList(getVisualPanel().getMethodThrowList());
                        creature.setDescriptionStorage(getVisualPanel().getMethodDescriptionStorage());
                    }
                }
            }
        }

        @Override
        public String getDisplayName() {
            return NbBundle.getMessage(MethodSetupStep2.class, "SetupStep2");
        }

        @Override
        public boolean isComplete() {
            return getVisualPanel().isComplete();
        }
    }
}
