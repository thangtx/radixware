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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.enums.ECommandAccessibility;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.radixdoc.CommandRadixdoc;
import org.radixware.kernel.common.defs.ads.radixdoc.ScopeCommandRadixdoc;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition.Presentations.Commands.Command;
import org.radixware.schemas.adsdef.CommandDefinition;
import org.radixware.schemas.radixdoc.Page;


public abstract class AdsScopeCommandDef extends AdsCommandDef implements IAdsClassMember, IOverridable, IRadixdocProvider {

    public static class Factory {

        public static final AdsScopeCommandDef loadFrom(Command xCommand) {
            ECommandScope scope = xCommand.getScope();
            switch (scope) {
                case GROUP:
                    return new AdsGroupCommandDef(xCommand);
                case OBJECT:
                    return new AdsObjectCommandDef(xCommand);
                case PROPERTY:
                    return new AdsPropertyCommandDef(xCommand);
                case RPC:
                    return AdsRPCCommandDef.Factory.loadFrom(xCommand);
                default:
                    return null;
            }
        }

        public static AdsScopeCommandDef newInstance(ECommandScope scope) {
            switch (scope) {
                case GROUP:
                    return new AdsGroupCommandDef();
                case OBJECT:
                    return new AdsObjectCommandDef();
                case PROPERTY:
                    return new AdsPropertyCommandDef();
                default:
                    return null;
            }
        }
    }

    public class CommandPresentation extends AdsCommandDef.CommandPresentation {

        private ECommandAccessibility accessebility;

        protected CommandPresentation(Command xCommand) {
            super(xCommand);
            if (xCommand != null) {
                this.accessebility = xCommand.getAccessibility();
            }
            if (this.accessebility == null) {
                this.accessebility = ECommandAccessibility.ALWAYS;
            }
        }

        public ECommandAccessibility getAccessebility() {
            return accessebility;
        }

        public void setAccessebility(ECommandAccessibility accessebility) {
            this.accessebility = accessebility;
            setEditState(EEditState.MODIFIED);
        }

        public void appendTo(Command xDef) {
            super.appendTo(xDef);
            xDef.setAccessibility(accessebility);
        }

        @Override
        public void appendTo(CommandDefinition xDef) {
            assert xDef instanceof Command;
            super.appendTo(xDef);
            ((Command) xDef).setAccessibility(accessebility);
        }
    }

    protected AdsScopeCommandDef(Command xCommand) {
        super(xCommand);
    }

    protected AdsScopeCommandDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.COMMAND), name);
    }

    protected AdsScopeCommandDef(Id id, String name) {
        super(id, name);
    }

    @Override
    protected CommandPresentation createPresentation(CommandDefinition xDef) {
        if (xDef == null) {
            return new AdsScopeCommandDef.CommandPresentation(null);
        } else {
            return new AdsScopeCommandDef.CommandPresentation((Command) xDef);
        }
    }

    public abstract ECommandScope getScope();

    @Override
    public void appendTo(CommandDefinition xCommand, ESaveMode saveMode) {
        assert xCommand instanceof Command;
        super.appendTo(xCommand, saveMode);
        ((Command) xCommand).setScope(getScope());
    }

    @Override
    public AdsClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsClassDef) {
                return (AdsClassDef) owner;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Hierarchy<AdsScopeCommandDef> getHierarchy() {
        if (getScope() != ECommandScope.GROUP) {
            return new MemberHierarchy<AdsScopeCommandDef>(this) {
                @Override
                protected AdsScopeCommandDef findMember(ClassPresentations clazz, Id id) {
                    if (clazz instanceof EntityObjectPresentations) {
                        return ((EntityObjectPresentations) clazz).getCommands().getLocal().findById(id);
                    } else {
                        return null;
                    }
                }
            };
        }
        return super.getHierarchy(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.COMMAND;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.WEB, ERuntimeEnvironmentType.COMMON_CLIENT);
    }

    @Override
    public ClipboardSupport<? extends AdsScopeCommandDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsScopeCommandDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                Command xDef = Command.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsScopeCommandDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof Command) {
                    return AdsScopeCommandDef.Factory.loadFrom((Command) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            public boolean isEncodedFormatSupported() {
                return true;
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", Command.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
                return null;
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (collection instanceof ExtendablePresentations.ExtendablePresentationsLocal && collection.getContainer() instanceof Commands) {
            AdsClassDef ownerClass = ((Commands) collection.getContainer()).getOwnerClass();
            switch (ownerClass.getClassDefType()) {
                case ENTITY:
                case APPLICATION:
                case FORM_HANDLER:
                    return this.getScope() == ECommandScope.OBJECT || this.getScope() == ECommandScope.PROPERTY;
                case ENTITY_GROUP:
                    return this.getScope() == ECommandScope.GROUP;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.SCOPE_COMMAND;
    }

    @Override
    public void afterOverride() {
        //do nothing
    }

    @Override
    public CommandPresentation getPresentation() {
        return (AdsScopeCommandDef.CommandPresentation) super.getPresentation();
    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsScopeCommandDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ScopeCommandRadixdoc(getSource(), page, options);
            }
        };
    }
}
