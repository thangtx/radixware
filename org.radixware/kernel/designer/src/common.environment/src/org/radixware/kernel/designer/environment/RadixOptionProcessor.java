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

package org.radixware.kernel.designer.environment;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionProcessor;
import org.radixware.kernel.common.builder.release.ReleaseSettings;
import org.radixware.kernel.designer.ads.build.actions.AbstractBuildAction;


public class RadixOptionProcessor extends OptionProcessor {

    private Option developmentMode = Option.optionalArgument(Option.NO_SHORT_NAME, "development");
    private Option suicideMode = Option.optionalArgument(Option.NO_SHORT_NAME, "suicide");
    private Option dbDependentScriptMode = Option.optionalArgument(Option.NO_SHORT_NAME, "bddependentscripts");

    @Override
    protected Set getOptions() {
        Set<Option> options = new HashSet<>();
        options.add(developmentMode);
        options.add(suicideMode);
        options.add(dbDependentScriptMode);
        return options;
    }

    @Override
    protected void process(Env env, Map maps) throws CommandException {
        if (maps.containsKey(developmentMode)) {
            DynamicFs.enable();
        }
        if (maps.containsKey(suicideMode)) {
            AbstractBuildAction.enableSuicideMode();
        }
        if (maps.containsKey(dbDependentScriptMode)) {
            ReleaseSettings.enableDbDependentScriptsMode();
        }
    }
}
