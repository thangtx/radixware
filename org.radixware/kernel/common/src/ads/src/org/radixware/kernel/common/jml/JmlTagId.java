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

package org.radixware.kernel.common.jml;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType;


public class JmlTagId extends Jml.Tag {

    public static final class Mode {

        public static final int DEFAULT = 0;
        public static final int SLOT_DESCRIPTION = 1;
    }
    protected AdsPath path;
    private int mode;
    private boolean isSoftRef = false;

    JmlTagId(JmlType.Item.IdReference idRef) {
        this.path = new AdsPath(idRef.getPath());
        if (idRef.isSetMode()) {
            this.mode = idRef.getMode();
        } else {
            this.mode = Mode.DEFAULT;
        }
        if (idRef.isSetSoft()) {
            this.isSoftRef = idRef.getSoft();
        }
    }

    public JmlTagId(Id[] ids) {
        this.path = new AdsPath(ids);
    }

    public boolean isSoftReference() {
        return isSoftRef;
    }

    public void setSoftReference(boolean isSoftRef) {
        if (isSoftRef != this.isSoftRef) {
            this.isSoftRef = isSoftRef;
            setEditState(EEditState.MODIFIED);
        }
    }

    public JmlTagId(AdsDefinition referencedDef) {
        if (referencedDef == null) {
            throw new NullPointerException();
        }
        this.path = new AdsPath(referencedDef);
    }

    public JmlTagId(DdsTableDef referencedDef) {
        if (referencedDef == null) {
            throw new NullPointerException();
        }
        this.path = new AdsPath(referencedDef);
    }

    public JmlTagId(DdsAccessPartitionFamilyDef referencedDef) {
        if (referencedDef == null) {
            throw new NullPointerException();
        }
        this.path = new AdsPath(referencedDef);
    }

    public JmlTagId(Module referencedDef) {
        if (referencedDef == null) {
            throw new NullPointerException();
        }
        this.path = new AdsPath(referencedDef);
    }

    public AdsPath getPath() {
        return path;
    }

    public void setPath(AdsPath path) {
        this.path = path;
        setEditState(EEditState.MODIFIED);
    }

    public Definition resolve(Definition referenceContext) {
        return resolveImpl(referenceContext).get();
    }

    private SearchResult<Definition> resolveImpl(Definition referenceContext) {
        if (referenceContext == null || path == null || path.isEmpty()) {
            return SearchResult.empty();
        } else {
            if (isSoftRef) {
                return this.path.resolveGlobal(referenceContext);
            } else {
                return this.path.resolve(referenceContext);
            }
        }
    }

    @Override
    public void appendTo(JmlType.Item item) {
        JmlType.Item.IdReference ref = item.addNewIdReference();
        ref.setPath(path.asList());
        if (mode != Mode.DEFAULT) {
            ref.setMode(mode);
        }
        if (isSoftRef) {
            ref.setSoft(true);
        }
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag id={0}]", path.toString());
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        Definition def = resolve(getOwnerJml().getOwnerDefinition());
        if (def != null) {
            list.add(def);
            if (def instanceof AdsEnumItemDef) {
                AdsEnumDef owner = ((AdsEnumItemDef) def).getOwnerEnum();
                if (owner != null) {
                    list.add(owner);
                }
            }
        }
    }

    @Override
    public void collectDirectDependences(List<Definition> list) {
        if (!isSoftRef) {
            collectDependences(list);
        }
    }

    @Override
    public String getDisplayName() {
        if (getOwnerJml() == null) {
            return "Unknown";
        }
        Definition context = getOwnerJml().getOwnerDefinition();
        Definition def = resolve(context);
        if (def != null) {
            if (mode == Mode.SLOT_DESCRIPTION && def instanceof AdsMethodDef) {
                return "slot[" + def.getQualifiedName(context) + "]";
            } else {
                return "idof[" + def.getQualifiedName(context) + "]";
            }
        } else {
            return "unknownid[" + AdsPath.toString(getPath()) + "]";
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new CodeWriter(this, purpose) {
                    @Override
                    public boolean writeCode(CodePrinter printer) {

                        final Definition def = resolve(getOwnerJml().getOwnerDefinition());
                        if (def != null) {
                            if (mode == Mode.SLOT_DESCRIPTION && def instanceof AdsMethodDef) {
                                final AdsMethodDef method = (AdsMethodDef) def;
                                final String slotDescription = method.getProfile().getProfileForQtSlotDescription();
                                if (slotDescription == null) {
                                    return false;
                                }
                                final RadixObjectLocator locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);
                                RadixObjectLocator.RadixObjectData marker = null;
                                if (locator != null) {
                                    marker = locator.start(JmlTagId.this);
                                }
                                printer.printStringLiteral(slotDescription);
                                if (marker != null) {
                                    marker.commit();
                                }
                                return true;
                            } else {
                                WriterUtils.writeIdUsage(printer, def.getId(), JmlTagId.this);
                                return true;
                            }

                        } else {
                            if (mode != Mode.SLOT_DESCRIPTION) {
                                WriterUtils.writeIdUsage(printer, path.getTargetId(), JmlTagId.this);
                                return true;
                            }
                            printer.printError();
                            return false;
                        }

                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                        //do nothing
                    }
                };
            }
        };
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        if (this.mode != mode) {
            this.mode = mode;
            setEditState(EEditState.MODIFIED);
        }
    }

    protected final Definition basicCheckImpl(IProblemHandler problemHandler) {
        final Definition context = getOwnerJml().getOwnerDefinition();
        Definition def = resolveImpl(context).get(new SearchResult.CheckForDuplicatesAdvisor<Definition>(this, problemHandler));
        if (def == null) {
            if (path != null) {
                Definition something = path.resolveSomething(getOwnerJml().getOwnerDefinition());
                if (something != null) {
                    error(problemHandler, MessageFormat.format("Referenced subdefinition of {0} is not found {1}", something.getQualifiedName(), AdsPath.toString(path)));
                } else {
                    error(problemHandler, "Referenced definition not found " + AdsPath.toString(path));
                }
            } else {
                error(problemHandler, "Referenced definition not found " + AdsPath.toString(path));
            }
        } else {
            if (def instanceof AdsDefinition) {
                AdsUtils.checkAccessibility(this, (AdsDefinition) def, false, problemHandler);
            }
        }
        if (performEnvCheck()) {
            doEnvCheck(def, problemHandler);
        }
        return def;
    }

    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        basicCheckImpl(problemHandler);
    }

    @Override
    public String getToolTip() {
        return getToolTip(EIsoLanguage.ENGLISH);
    }

    @Override
    public String getToolTip(EIsoLanguage language) {
        Definition def = null;
        final Jml jml = getOwnerJml();

        if (jml != null) {
            def = resolve(jml.getOwnerDefinition());
        } else {
            return "";
        }
        if (def == null) {
            StringBuilder b = new StringBuilder();
            b.append("<html>");
            b.append("<b><font color=\"#FF0000\">Unresoved Reference</font></b>");
            if (path != null) {
                Definition something = path.resolveSomething(jml.getOwnerDefinition());
                if (something != null) {
                    b.append("<br>Last resolved owner: ");
                    b.append(something.getQualifiedName());
                }
                b.append("<br>Definition Path: ");
                b.append(path.toString().replace("<", "&lt;").replace(">", "&gt;"));
            }
            b.append("</html>");
            return b.toString();
        } else {
            return def.getToolTip(language,jml.getOwnerDefinition());
        }
    }

    protected boolean performEnvCheck() {
        return false;
    }

    protected void doEnvCheck(Definition def, IProblemHandler problemHandler) {
        Jml jml = getOwnerJml();

        if (def instanceof AdsDefinition && jml != null && jml.getUsageEnvironment() != null) {
            AdsDefinition adsdef = (AdsDefinition) def;
            ERuntimeEnvironmentType defenv = adsdef.getUsageEnvironment();
            Set<ERuntimeEnvironmentType> mismatch = EnumSet.noneOf(ERuntimeEnvironmentType.class);
            if (defenv != ERuntimeEnvironmentType.COMMON) {
                boolean noCheck = false;
                if (jml.getUsageEnvironment().isClientEnv() && (adsdef instanceof AdsPropertyDef || adsdef instanceof AdsFilterDef.Parameter) && defenv == ERuntimeEnvironmentType.SERVER) {

                    noCheck = true;
                }
                if (!noCheck) {
                    switch (jml.getUsageEnvironment()) {
                        case COMMON_CLIENT:
                            if (defenv != ERuntimeEnvironmentType.COMMON_CLIENT) {
                                mismatch.add(ERuntimeEnvironmentType.COMMON);
                                mismatch.add(ERuntimeEnvironmentType.COMMON_CLIENT);
                            }
                            break;
                        case EXPLORER:
                            if (defenv != ERuntimeEnvironmentType.COMMON_CLIENT && defenv != ERuntimeEnvironmentType.EXPLORER) {
                                mismatch.add(ERuntimeEnvironmentType.COMMON);
                                mismatch.add(ERuntimeEnvironmentType.COMMON_CLIENT);
                                mismatch.add(ERuntimeEnvironmentType.EXPLORER);
                            }
                            break;
                        case WEB:
                            if (defenv != ERuntimeEnvironmentType.COMMON_CLIENT && defenv != ERuntimeEnvironmentType.WEB) {
                                mismatch.add(ERuntimeEnvironmentType.COMMON);
                                mismatch.add(ERuntimeEnvironmentType.COMMON_CLIENT);
                                mismatch.add(ERuntimeEnvironmentType.WEB);
                            }
                            break;
                        case SERVER:
                            if (defenv != ERuntimeEnvironmentType.SERVER) {
                                mismatch.add(ERuntimeEnvironmentType.COMMON);
                                mismatch.add(ERuntimeEnvironmentType.SERVER);
                            }
                            break;
                    }
                    if (!mismatch.isEmpty()) {
                        StringBuilder envs = new StringBuilder();

                        for (ERuntimeEnvironmentType e : mismatch) {
                            if (envs.length() > 0) {
                                envs.append(", ");
                            }
                            envs.append(e.getName());
                        }
                        error(problemHandler, "Inadmissible environment of referenced definition " + adsdef.getQualifiedName() + ": " + defenv.getName() + ". Possible usages are: " + envs.toString());
                    }
                }
            }
        }
    }
}
