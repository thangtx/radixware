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

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.schemas.adsdef.ClassSource;
import org.radixware.schemas.xscml.JmlType;


public abstract class AdsSources extends RadixObject implements IJmlSource {

    private final class Source extends Jml implements IEnvDependent {

        private ERuntimeEnvironmentType env;

        public Source(AdsSources container, ERuntimeEnvironmentType env, String name) {
            super(container, env == null ? name : (env.getValue() + "_" + name));
            this.env = env;
        }

        public Source(AdsSources container, ERuntimeEnvironmentType env, String name, JmlType xDef) {
            this(container, env, name);
            if (xDef != null) {
                this.loadFrom(xDef);
            }
        }

        @Override
        public ERuntimeEnvironmentType getUsageEnvironment() {
            return env == null ? super.getUsageEnvironment() : env;
        }
    }
    private List<Source> sources;

    public AdsSources(RadixObject container) {
        super("Sources");
        setContainer(container);
        sources = new LinkedList<Source>();
        sources.add(new Source(this, null, defaultName()));
    }

    void reset() {
        sources.clear();
        sources.add(new Source(this, null, defaultName()));
    }

    private AdsDefinition getOwnerDef() {
        Definition def = getOwnerDefinition();
        while (def != null) {
            if (def instanceof AdsDefinition) {
                return (AdsDefinition) def;
            }
            def = def.getOwnerDefinition();
        }
        return null;
    }

    public boolean canSeparateSources() {
        AdsDefinition owner = getOwnerDef();
        if (owner == null) {
            return false;
        } else {
            if (owner instanceof IAdsClassMember) {
                AdsClassDef ownerClass = ((IAdsClassMember) owner).getOwnerClass();
                if (ownerClass != null && !ownerClass.isDual()) {
                    return false;
                }
            }
            return owner.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT;
        }
    }

    public boolean isSourcesSeparated() {
        if (canSeparateSources()) {
            if (sources == null) {
                return false;
            } else {
                if (sources.size() > 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public void onOwnerEnvironmentChange() {
        AdsDefinition owner = getOwnerDef();

        if (isSourcesSeparated()) {
            if (owner == null || owner.getUsageEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                setSourcesSeparated(false);
            }
        }
    }

    protected abstract String defaultName();

    public void setSourcesSeparated(boolean separate) {
        if (separate) {
            if (canSeparateSources()) {
                if (isSourcesSeparated()) {
                    return;
                }
                Source explorerSrc = null;
                Source webSrc;
                if (sources != null && !sources.isEmpty()) {
                    explorerSrc = sources.get(0);
                }
                if (explorerSrc == null) {
                    explorerSrc = new Source(this, ERuntimeEnvironmentType.EXPLORER, defaultName());
                } else {
                    explorerSrc.env = ERuntimeEnvironmentType.EXPLORER;
                }
                webSrc = new Source(this, ERuntimeEnvironmentType.WEB, defaultName());
                if (sources == null) {
                    sources = new LinkedList<Source>();
                } else {
                    sources.clear();
                }
                sources.add(explorerSrc);
                sources.add(webSrc);
                setEditState(EEditState.MODIFIED);
            }
        } else {
            if (isSourcesSeparated()) {
                Source src = new Source(this, null, defaultName());
                for (Source s : sources) {
                    src.getItems().add(Scml.Text.Factory.newInstance("// --------------------- " + s.getUsageEnvironment().getValue() + " --------------------- \n"));
                    for (Scml.Item item : s.getItems()) {
                        s.getItems().remove(item);
                        src.getItems().add(item);
                    }
                }

                sources.clear();
                sources.add(src);
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public Set<ERuntimeEnvironmentType> getDefinedSourceTypes() {
        if (sources == null) {
            return EnumSet.noneOf(ERuntimeEnvironmentType.class);
        } else {
            if (sources.size() == 1) {
                return EnumSet.of(getOwnerDef().getUsageEnvironment());
            } else {
                EnumSet<ERuntimeEnvironmentType> set = EnumSet.noneOf(ERuntimeEnvironmentType.class);
                for (Source src : sources) {
                    set.add(src.env);
                }
                return set;
            }
        }
    }

    public Jml getSource(ERuntimeEnvironmentType env) {
        if (sources != null) {
            Source cc = null;
            if (env == null && getOwnerDef() != null) {
                env = getOwnerDef().getUsageEnvironment();
            }
            for (Source s : sources) {

                if (s.getUsageEnvironment() == env) {
                    return s;
                }
                if (s.getUsageEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    cc = s;
                }
            }
            if (cc != null && (env == ERuntimeEnvironmentType.WEB || env == ERuntimeEnvironmentType.EXPLORER)) {
                return cc;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Jml getSource(String name) {
        if (sources == null) {
            return null;
        } else {
            for (Source src : sources) {
                if (src.getName().equals(name)) {
                    return src;
                }
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return sources == null || sources.isEmpty();
    }

    public void appendTo(ClassSource xDef, AdsDefinition.ESaveMode saveMode) {
        if (sources == null) {
            return;
        } else {
            for (Source src : sources) {
                ClassSource.Src xSrc = xDef.addNewSrc();
                src.appendTo(xSrc, saveMode);
                xSrc.setEnvironment(src.env);
            }
        }
    }

    public void loadFrom(JmlType xDef) {
        if (sources != null) {
            sources.clear();
        } else {
            sources = new LinkedList<Source>();
        }
        Source src = new Source(this, null, defaultName(), xDef);
        sources.add(src);
    }

    public void loadFrom(ClassSource xDef) {
        if (xDef != null) {
            if (sources != null) {
                sources.clear();
            } else {
                sources = new LinkedList<Source>();
            }
            List<ClassSource.Src> xSrcs = xDef.getSrcList();
            if (xSrcs != null) {
                for (ClassSource.Src xSrc : xSrcs) {
                    Source src = new Source(this, xSrc.getEnvironment(), defaultName(), xSrc);
                    sources.add(src);
                }
            }
        }
        if (sources == null) {
            sources = new LinkedList<Source>();
        }
        if (sources.isEmpty()) {
            Source src = new Source(this, null, defaultName());
            sources.add(src);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (sources != null) {
            for (Source s : sources) {
                s.visit(visitor, provider);
            }
        }
    }
}
