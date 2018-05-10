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

package org.radixware.kernel.designer.tree.ads.nodes.defs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.nodes.Children;
import org.openide.util.actions.SystemAction;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.AccessChangedEvent;
import org.radixware.kernel.common.defs.ads.UdsExportable;
import org.radixware.kernel.common.defs.ads.build.BuildOptions;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.uds.UdsDefinition;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction.CompileCookie;
import org.radixware.kernel.designer.ads.build.actions.CompileDefinitionAction;
import org.radixware.kernel.designer.common.tree.RadixObjectNode;
import org.radixware.kernel.designer.tree.ads.nodes.actions.TuneProfileAction.TuneProfileCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewClientSourceAction.ViewClientSourceCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewCommonSourceAction.ViewCommonSourceCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewExplorerSourceAction.ViewExplorerSourceCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewServerSourceAction.ViewServerSourceCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.*;
import org.radixware.kernel.designer.tree.ads.nodes.actions.ViewWebSourceAction.ViewWebSourceCookie;
import org.radixware.kernel.designer.tree.ads.nodes.actions.preview.PreviewAction;

/**
 * Base class for tree-look representation of ads definition
 *
 */
public class AdsObjectNode<T extends RadixObject> extends RadixObjectNode {
    
    private final ConfigureUsingRolesAction.Cookie usingRolesCookie;
    private final ConfigureDomainsAction.ConfigureDomainsCookie configureDomainsCookie;
    //private final boolean canChangeAccess;
    private final AdsDefinition.AccessListener accessListener;
    private ExportToUdsExchangeFormatAction.Cookie exportCookie;
    private PreviewAction.PreviewCookie cookie;
    private TuneProfileCookie tuneProfileCookie = null;
    private final Map<Object, Cookie> cookies = new HashMap<>();
    
    protected AdsObjectNode(T definition, Children children) {
        super(definition, children);
        
        if (AdsUtils.mayBeUsedByRoles(definition)) {
            this.usingRolesCookie = new ConfigureUsingRolesAction.Cookie(definition);
            addCookie(this.usingRolesCookie);
        } else {
            this.usingRolesCookie = null;
        }
        
        if (definition instanceof AdsDefinition) {
            AdsDefinition adsdef = (AdsDefinition) definition;
//            if (adsdef.canChangeAccessMode()) {
//                addCookie(new AdsRefactoringProvider(adsdef));
//                canChangeAccess = true;
//            } else {
//                canChangeAccess = false;
//            }
            if (adsdef.getDefinitionType() != EDefType.DOMAIN && BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null) {
                configureDomainsCookie = new ConfigureDomainsAction.ConfigureDomainsCookie(adsdef);
                addCookie(configureDomainsCookie);
            } else {
                configureDomainsCookie = null;
            }
            if (definition.isSaveable()) {
                final AdsDefinitionReloadAction.Cookie reloadCookie = new AdsDefinitionReloadAction.Cookie(adsdef);
                addCookie(reloadCookie);
            }
                    
            if (!(definition instanceof UdsDefinition) 
                    && !definition.isReadOnly()
                    && BuildOptions.UserModeHandlerLookup.getUserModeHandler() == null 
                    && AdsUtils.isEnableHumanReadable(definition)) {
                addCookie(cookie = new PreviewAction.PreviewCookie(adsdef));
            }
        } else {
            configureDomainsCookie = null;
            //   canChangeAccess = false;
        }
        
        if (definition.isSaveable() && definition instanceof AdsDefinition) {
            final AdsDefinitionReloadAction.Cookie reloadCookie = new AdsDefinitionReloadAction.Cookie((AdsDefinition) definition);
            addCookie(reloadCookie);
        }
        if (definition instanceof AdsDefinition && ((AdsDefinition) definition).canChangeAccessMode()) {
            accessListener = new AdsDefinition.AccessListener() {
                @Override
                public void onEvent(AccessChangedEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            updateIcon();
                        }
                    });
                    
                }
            };
            ((AdsDefinition) definition).getAccessChangeSupport().addEventListener(accessListener);
        } else {
            accessListener = null;
        }
        if (definition instanceof IProfileable && ((IProfileable) definition).isProfileable()) {
            
            tuneProfileCookie = new TuneProfileCookie((IProfileable) definition);
            addCookie(tuneProfileCookie);
        } else {
            tuneProfileCookie = null;
        }
        updateCompileCookie(null);
        if (definition instanceof UdsExportable && !AdsUtils.isUserExtension(definition)) {
            addCookie(exportCookie = new ExportToUdsExchangeFormatAction.Cookie((UdsExportable) definition));
        }
    }
    
    private void updateCompileCookie(List<Action> actions) {
        final JavaSourceSupport jSupport = (getRadixObject() instanceof IJavaSource ? ((IJavaSource) getRadixObject()).getJavaSourceSupport() : null);
        
        boolean buildAction = false;
        for (final ERuntimeEnvironmentType environment : ERuntimeEnvironmentType.values()) {
            if (isBuildable(jSupport, environment)) {
                if (!buildAction) {
                    if (findCookie(CompileCookie.class) == null) {
                        addCookie(CompileCookie.class, new CompileCookie(getRadixObject()));
                    }
                    if (actions != null) {
                        actions.add(SystemAction.get(CompileDefinitionAction.class));
                    }
                    buildAction = true;
                }
                
                if (findCookie(environment) == null) {
                    addCookie(environment, createCookie(jSupport, environment));
                }
                if (actions != null) {
                    actions.add(getAction(environment));
                }
            } else {
                removeCookie(environment);
            }
        }
        
        if (!buildAction) {
            removeCookie(CompileCookie.class);
        }
    }
    
    @Override
    public void addCustomActions(List<Action> actions) {
        super.addCustomActions(actions);
        final RadixObject radixObject = getRadixObject();
        
        if (radixObject.isSaveable()) {
            actions.add(SystemAction.get(AdsDefinitionReloadAction.class));
        }
        
        updateCompileCookie(actions);
        
        actions.add(null);
        if (cookie != null) {
            actions.add(SystemAction.get(PreviewAction.class));
        }
        actions.add(null);        
        if (usingRolesCookie != null) {
            actions.add(SystemAction.get(ConfigureUsingRolesAction.class));
        }
        if (configureDomainsCookie != null) {
            actions.add(SystemAction.get(ConfigureDomainsAction.class));
        }
        if (tuneProfileCookie != null) {
            actions.add(null);
            actions.add(SystemAction.get(TuneProfileAction.class));
        }
        if (exportCookie != null) {
            actions.add(null);
            actions.add(SystemAction.get(ExportToUdsExchangeFormatAction.class));
        }
        

    }
    
    private boolean isBuildable(JavaSourceSupport jSupport, ERuntimeEnvironmentType environment) {
        return jSupport != null && jSupport.getSupportedEnvironments().contains(environment)
                && jSupport.isSeparateFilesRequired(environment);
    }
    
    private Cookie createCookie(JavaSourceSupport jSupport, ERuntimeEnvironmentType environment) {
        Set<JavaSourceSupport.CodeType> types;
        switch (environment) {
            case COMMON:
                types = jSupport.getSeparateFileTypes(ERuntimeEnvironmentType.COMMON);
                return new ViewCommonSourceCookie((IJavaSource) getRadixObject(), types);
            case SERVER:
                types = jSupport.getSeparateFileTypes(ERuntimeEnvironmentType.SERVER);
                return new ViewServerSourceCookie((IJavaSource) getRadixObject(), types);
            case EXPLORER:
                types = jSupport.getSeparateFileTypes(ERuntimeEnvironmentType.EXPLORER);
                return new ViewExplorerSourceCookie((IJavaSource) getRadixObject(), types);
            case WEB:
                types = jSupport.getSeparateFileTypes(ERuntimeEnvironmentType.WEB);
                return new ViewWebSourceCookie((IJavaSource) getRadixObject(), types);
            case COMMON_CLIENT:
                types = jSupport.getSeparateFileTypes(ERuntimeEnvironmentType.COMMON_CLIENT);
                return new ViewClientSourceCookie((IJavaSource) getRadixObject(), types);
            default:
                throw new UnsupportedOperationException("Unsupported runtime environment type '" + environment.getName() + "'");
        }
    }
    
    private Action getAction(ERuntimeEnvironmentType environment) {
        switch (environment) {
            case COMMON:
                return SystemAction.get(ViewCommonSourceAction.class);
            case SERVER:
                return SystemAction.get(ViewServerSourceAction.class);
            case EXPLORER:
                return SystemAction.get(ViewExplorerSourceAction.class);
            case WEB:
                return SystemAction.get(ViewWebSourceAction.class);
            case COMMON_CLIENT:
                return SystemAction.get(ViewClientSourceAction.class);
            default:
                throw new UnsupportedOperationException("Unsupported runtime environment type '" + environment.getName() + "'");
        }
    }
    
    private Cookie findCookie(Object key) {
        return cookies.get(key);
    }
    
    private void addCookie(Object key, Cookie cookie) {
        cookies.put(key, cookie);
        addCookie(cookie);
    }
    
    private void removeCookie(Object key) {
        final Cookie cookie = findCookie(key);
        if (cookie != null) {
            removeCookie(cookie);
            cookies.remove(key);
        }
    }
}
