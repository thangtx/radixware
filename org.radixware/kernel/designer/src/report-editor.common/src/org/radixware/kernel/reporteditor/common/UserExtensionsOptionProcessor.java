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
package org.radixware.kernel.reporteditor.common;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionProcessor;
import org.openide.LifecycleManager;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = org.netbeans.spi.sendopts.OptionProcessor.class)
public class UserExtensionsOptionProcessor extends OptionProcessor {

    private Option defaultOption = Option.optionalArgument(Option.NO_SHORT_NAME, "user_ext_designer");

    @Override
    protected Set<Option> getOptions() {
        Set<Option> options = new HashSet<>();
        options.add(defaultOption);
        return options;
    }

    @Override
    protected void process(Env env, Map<Option, String[]> maps) throws CommandException {
        if (maps.containsKey(defaultOption)) {
            System.out.println("Application started");
        } else {
            LifecycleManager.getDefault().exit();
        }

    }
}