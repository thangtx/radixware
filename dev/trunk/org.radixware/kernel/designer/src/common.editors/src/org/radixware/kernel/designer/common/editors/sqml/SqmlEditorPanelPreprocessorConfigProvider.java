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

package org.radixware.kernel.designer.common.editors.sqml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.openide.util.Lookup;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.DbOptionValue;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.IfParamTag;
import org.radixware.kernel.common.sqml.translate.ISqmlPreprocessorConfig;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.sqmlnb.DatabaseParameters;
import org.radixware.kernel.designer.common.dialogs.sqmlnb.ISqmlPreprocessorConfigProvider;


public class SqmlEditorPanelPreprocessorConfigProvider implements ISqmlPreprocessorConfigProvider {

    private ISqmlPreprocessorConfig config = null;

    public SqmlEditorPanelPreprocessorConfigProvider() {
    }

    @Override
    public ISqmlPreprocessorConfig getConfig() {
        return config;
    }

    @Override
    public void beforeStart(final Lookup context) {
        if (context == null) {
            return;
        }
        final DatabaseParameters parameters = DatabaseParameters.get(null, null);
        if (parameters == null) {
            config = null;
        } else {
            config = new ISqmlPreprocessorConfig() {
                
                private Sqml lastSqml;
                private Map<Id, ISqmlPreprocessorConfig.PreprocessorParameter> paramValues = new HashMap<>();
                
                @Override
                public Object getProperty(String propertyName) {
                    final List<DbOptionValue> options = new ArrayList<>();
                    if (parameters.getExplicitOptions() != null) {
                        options.addAll(parameters.getExplicitOptions());
                    }
                    try {
                        if (parameters.isUseParamsFromDb() && parameters.getDbConfiguration() != null && parameters.getDbConfiguration().getOptions() != null) {
                            options.addAll(parameters.getDbConfiguration().getOptions());
                        }
                    } catch (Exception ex) {
                        //ignore
                    }
                    for (DbOptionValue opt : options) {
                        if (opt != null && opt.getOptionName().equals(propertyName) && opt.getMode() == EOptionMode.ENABLED) {
                            return Boolean.TRUE;
                        }
                    }
                    return null;
                }

                @Override
                public ISqmlPreprocessorConfig.PreprocessorParameter getParameter(final Id paramId) {
                    if (paramId == null) {
                        return null;
                    }
                    if (paramValues.containsKey(paramId)) {
                        return paramValues.get(paramId);
                    }
                    String paramName = paramId.toString();
                    EValType valType = null;
                    if (lastSqml != null) {
                        for (Scml.Item item : lastSqml.getItems()) {
                            if (item instanceof IfParamTag) {
                                IfParamTag itag = (IfParamTag) item;
                                if (itag.getParameterId().equals(paramId)) {
                                    paramName = itag.getParameter().getName();
                                    break;
                                }
                            }
                        }
                    }
                    final String paramNameFinal = paramName;
                    final String value = JOptionPane.showInputDialog("Parameter '" + paramName+ ":");
                    final PreprocessorParameter parameter = new PreprocessorParameter() {

                        @Override
                        public Object getValue() {
                            return value;
                        }

                        @Override
                        public Id getId() {
                            return paramId;
                        }

                        @Override
                        public String getName() {
                            return paramNameFinal;
                        }
                    };
                    paramValues.put(paramId, parameter);
                    return parameter;
                }
                
                @Override
                public String getDbTypeName() {
                    try {
                        if (parameters.isUseExplicitType()) {
                            return parameters.getExplicitType();
                        } else if (parameters.getDbConfiguration() != null) {
                            return String.valueOf(parameters.getDbConfiguration().getTargetDbType());
                        }
                    } catch (Exception ex) {
                        //ignore
                    }
                    return null;
                }

                @Override
                public String getDbVersion() {
                    try {
                        if (parameters.isUseExplicitVersion()) {
                            return parameters.getExplicitVersion();
                        } else if (parameters.getDbConfiguration() != null) {
                            return String.valueOf(parameters.getDbConfiguration().getTargetDbVersion());
                        }
                    } catch (Exception ex) {
                        //ignore
                    }
                    return null;
                }

                @Override
                public Sqml copy(Sqml sqml) {
                    return lastSqml = sqml.getClipboardSupport().copy();
                }

                @Override
                public boolean alwaysCreateCopy() {
                    return true;
                }
            };
        }
    }
}
