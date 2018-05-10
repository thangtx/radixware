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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsGroupModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsRPCCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsRPCMethodWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ValTypes;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.MethodDefinition;


public class AdsRPCMethodDef extends AdsMethodDef {

    private static final Id RADIX_META_ID = Id.Factory.loadFrom("mdlJCSIEZPAWRE5FGCPBMQR2R74DE");

    public static final class Factory {

        public static AdsRPCMethodDef newInstance(AdsMethodDef ss) {
            return new AdsRPCMethodDef(ss);
        }

        public static AdsRPCMethodDef newTemoporaryInstance(AdsClassDef container, AdsMethodDef ss) {
            AdsRPCMethodDef method = new AdsRPCMethodDef(ss);
            method.setContainer(container.getMethods().getLocal());
            return method;
        }
    }

    public Id getCommandId() {
        return calcCommandId();
    }
    private Id serverSideMethodId;
    private final MethodCommandProvider commandProvider = new MethodCommandProvider(this) {

        @Override
        protected Id getCommandId() {
            return calcCommandId();
        }
    };

    private Id calcCommandId() {
        return Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.COMMAND);
    }

    public AdsRPCMethodDef(AbstractMethodDefinition xDef) {
        super(xDef);
        this.serverSideMethodId = xDef.getRpcCallId();
    }

    private static String calcName(AdsMethodDef ss) {
        if (ss == null) {
            return "remoteCall_<unknown>";
        }
        return "remoteCall_" + ss.getName();
    }

    public AdsRPCMethodDef(AdsMethodDef ss) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_CLASS_METHOD), calcName(ss), false);

    }

    public static boolean isValidServerSideMethod(AdsMethodDef serverSideMethod) {
        if (serverSideMethod == null) {
            return false;
        }
        for (MethodParameter p : serverSideMethod.getProfile().getParametersList()) {
            if (p.getType() == null) {
                return false;
            } else if (!ValTypes.EAS_TYPES.contains(p.getType().getTypeId())) {
                AdsTypeDeclaration decl = p.getType();
                if (decl.getTypeId() != EValType.USER_CLASS) {
                    return false;
                }
                AdsType type = decl.resolve(serverSideMethod).get();
                if (type instanceof AdsClassType && ((AdsClassType) type).getSource() instanceof AdsEntityObjectClassDef) {
                    continue;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isSupportedServerType(AdsMethodDef context, AdsTypeDeclaration decl) {
        if (decl == AdsTypeDeclaration.VOID) {
            return true;
        } else if (decl == null || decl == AdsTypeDeclaration.UNDEFINED) {
            return false;
        } else if (decl.getArrayDimensionCount() > 0) {
            return false;
        } else if (!ValTypes.EAS_TYPES.contains(decl.getTypeId())) {
            if (decl.getTypeId() != EValType.USER_CLASS) {
                return false;
            } else {
                AdsType type = decl.resolve(context).get();
                if (type instanceof AdsClassType && ((AdsClassType) type).getSource() instanceof AdsEntityObjectClassDef) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    public static AdsTypeDeclaration convertServerType2ClientType(AdsMethodDef context, AdsTypeDeclaration decl, boolean isReturnValue) {
        if (!isSupportedServerType(context, decl)) {
            return null;
        } else {
            if (isReturnValue && decl.getTypeId() == EValType.USER_CLASS) {
                return AdsTypeDeclaration.Factory.newInstance(EValType.PARENT_REF);
            } else {
                if (decl == AdsTypeDeclaration.VOID) {
                    return decl;
                }
                return AdsTypeDeclaration.Factory.newCopy(decl);
            }
        }
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.RPC;
    }

    public AdsCommandDef findCommand() {
        return commandProvider.findCommand();
    }

    public void setServerSideMethod(AdsMethodDef method) {
        Id old = this.serverSideMethodId;
        this.serverSideMethodId = method.getId();
        try {
            setName(calcName(method));
            updateProfile();
            setEditState(EEditState.MODIFIED);
        } catch (RadixError e) {
            this.serverSideMethodId = old;
        }
    }

    public boolean updateProfile() {
        if (getContainer() == null) {
            throw new RadixError("Current method is not in branch. Unable to update components");
        }
        final AdsMethodDef ss = findServerSideMethod();
        if (ss != null) {
            this.getProfile().getParametersList().clear();
            for (MethodParameter ssp : ss.getProfile().getParametersList()) {
                AdsTypeDeclaration decl = convertServerType2ClientType(ss, ssp.getType(), false);
                if (decl != null) {
                    this.getProfile().getParametersList().add(MethodParameter.Factory.newInstance(ssp.getName(), decl));
                }
            }
            AdsTypeDeclaration decl = convertServerType2ClientType(ss, ss.getProfile().getReturnValue().getType(), true);
            if (decl == null) {
                decl = AdsTypeDeclaration.VOID;
            }
            this.getProfile().getReturnValue().setType(decl);
            this.getProfile().getThrowsList().clear();
            this.getProfile().getThrowsList().add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.exceptions.ServiceClientException")));
            this.getProfile().getThrowsList().add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(AdsTypeDeclaration.Factory.newPlatformClass("java.lang.InterruptedException")));
            fireNameChange();
            return true;
        } else {
            throw new RadixError("Server side method can not be found");
        }
    }

    public static IFilter<AdsMethodDef> createSuitableMethodsFilter() {
        return new IFilter<AdsMethodDef>() {

            @Override
            public boolean isTarget(AdsMethodDef radixObject) {
                
                if (radixObject.isConstructor()) {
                    return false;
                }
                
                List<AdsTypeDeclaration> decls = new LinkedList<>();
                AdsTypeDeclaration decl = radixObject.getProfile().getReturnValue().getType();
                if (decl != null) {
                    decls.add(decl);
                }
                for (MethodParameter p : radixObject.getProfile().getParametersList()) {
                    decl = p.getType();
                    if (decl != null) {
                        decls.add(decl);
                    }
                }
                for (AdsTypeDeclaration d : decls) {
                    if (!isSupportedServerType(radixObject, decl)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsRPCMethodWriter(this, AdsRPCMethodDef.this, purpose);
            }
        };
    }

    public AdsMethodDef findServerSideMethod() {
        AdsClassDef ss = findServerSideClass();
        if (ss == null) {
            return null;
        } else {
            return ss.getMethods().findById(serverSideMethodId, EScope.LOCAL_AND_OVERWRITE).get();
        }
    }

    public Id getServerSideMethodId() {
        return serverSideMethodId;
    }

    public AdsClassDef findServerSideClass() {
        AdsClassDef clazz = getOwnerClass();

        if (clazz instanceof AdsModelClassDef) {
            AdsClassDef ss = ((AdsModelClassDef) clazz).findServerSideClasDef();
            if (ss == null) {
                return null;
            }
            if (clazz instanceof AdsGroupModelClassDef) {
                if (ss instanceof AdsEntityObjectClassDef) {
                    AdsEntityClassDef entity = ((AdsEntityObjectClassDef) ss).findRootBasis();
                    if (entity == null) {
                        return null;
                    }
                    AdsEntityGroupClassDef group = entity.findEntityGroup();
                    return group;
                } else {
                    return null;
                }
            } else {
                return ss;
            }
        } else {
            return null;
        }
    }

    public void updateCommand() {
        AdsClassDef ssClazz = findServerSideClass();
        if (ssClazz == null) {
            throw new RadixError("Server side class can not be found");
        }
        AdsMethodDef ss = findServerSideMethod();
        if (ss == null) {
            throw new RadixError("Referenced method can not be found in class " + ssClazz.getQualifiedName());
        }
        AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(this);
        final String ns = "http://schemas.radixware.org/utils.xsd";
        IXmlDefinition xdef = searcher.findByNs(ns).get();
        if (xdef == null) {
            AdsModule module = getModule();
            if (module == null) {
                throw new RadixError("Current method is not in branch. Unable to update components");
            }
            Branch branch = getBranch();
            if (branch == null) {
                throw new RadixError("Current method is not in branch. Unable to update components");
            }
            Layer radix = branch.getLayers().findByURI("org.radixware");
            if (radix == null) {
                throw new RadixError("Layer \"org.radixware\", containing command input/output type definition can not be found");
            }
            RadixObject result = radix.getAds().find(new AdsVisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof AdsXmlSchemeDef && ns.equals(((AdsXmlSchemeDef) radixObject).getTargetNamespace());
                }
            });
            if (result instanceof AdsXmlSchemeDef) {
                xdef = (AdsXmlSchemeDef) result;
            } else {
                throw new RadixError("Command input/output type definition can not be found");
            }
            Module resultModule = result.getModule();
            if (resultModule == null) {
                throw new RadixError("Command input/output type definition can not be found");
            }
            module.getDependences().add(resultModule);
        }

        AdsCommandDef cmd = findCommand();
        if (cmd != null) {
            if (!(cmd instanceof AdsScopeCommandDef) || (((AdsScopeCommandDef) cmd).getScope() != ECommandScope.RPC)) {
                cmd.delete();
                cmd = null;
            }
        }
        if (cmd == null) {
            AdsRPCCommandDef obj_cmd = AdsRPCCommandDef.Factory.newInstance(this);
            ((IAdsPresentableClass) ssClazz).getPresentations().getCommands().getLocal().add(obj_cmd);
            cmd = obj_cmd;
        }
        cmd.setDescription("This is synthetic command generated for remote procedure call implementation purpose.\nPlease, do not modify this command");
        cmd.setName(calcName(ss));
        cmd.getData().setIsLocal(false);
        cmd.getData().setInType(AdsTypeDeclaration.Factory.newXml(xdef, "RPCRequestDocument"));
        cmd.getData().setOutType(AdsTypeDeclaration.Factory.newXml(xdef, "RPCResponseDocument"));
        cmd.getPresentation().setIsConfirmationRequired(false);
        cmd.getPresentation().setIsVisible(false);
    }

    public void updateComponents() {
        updateCommand();
        updateProfile();
        fireNameChange();
    }

    @Override
    public void appendTo(MethodDefinition xMethod, ESaveMode saveMode) {
        super.appendTo(xMethod, saveMode);
        xMethod.setRpcCallId(serverSideMethodId);
    }

    @Override
    public String getTypeTitle() {
        return "Remote Procedure Call Method";
    }

    @Override
    public String getTypesTitle() {
        return "Remote Procedure Call Methods";
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);

        final AdsModule radixMetaModule = findRadixMetaModule();
        if (radixMetaModule != null) {
            list.add(radixMetaModule);
        }
        
        final AdsCommandDef command = findCommand();
        if (command != null) {
            list.add(command);
        }
        
        final AdsMethodDef serverSideMethod = findServerSideMethod();
        if (serverSideMethod != null) {
            list.add(serverSideMethod);
        }
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.NONE;
    }

    public AdsModule findRadixMetaModule() {
        final AdsModule[] result = { null };

        getBranch().find(new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof AdsSegment) {
                    final AdsSegment segment = (AdsSegment) radixObject;
                    final AdsModule radixMeta = segment.getModules().findById(RADIX_META_ID);

                    if (radixMeta != null) {
                        result[0] = radixMeta;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean isContainer(RadixObject radixObject) {
                return radixObject instanceof Layer || radixObject instanceof Branch;
            }
        });

        return result[0];
    }
}
