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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.IContextEnvironment;
import org.radixware.kernel.common.environment.IEnvironmentAccessor;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.Id;

/**
 * <p>Base class for explorer definitions</p>
 *
 */
public abstract class Definition {

    private final Id id;

    /**
     * Constructs an explorer definition whith an id identifier.
     *
     * @param id definition`s unique identifier.
     */
    public Definition(final Id id) {
        this.id = id;
    }

    /**
     * Get unique identifier of definition.
     *
     * @return definition identifier.
     */
    public final Id getId() {
        return id;
    }

    /**
     * Get icon for definition.
     *
     * @param iconId icon identifier.
     * @return definition`s icon.
     */
    protected Icon getIcon(final Id iconId) {
        if (iconId != null) {
            try {
                return getDefManager().getImage(iconId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get icon #%s for definition %s");
                getApplication().getTracer().error(String.format(mess, iconId.toString(), toString()), err);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get information about this definition (identifier, name, title e.t.c.).
     *
     * @return information about this definition.
     */
    @Override
    public String toString() {
        return "#" + id.toString();
    }

    /**
     * Get localized information about this definition: (definition type,
     * identifier, name, title, definition holder e.t.c.).
     *
     * @return string description for this definition.
     */
    public String getDescription() {
        return toString();
    }

    protected DefManager getDefManager() {
        return getApplication().getDefManager();
    }
    private volatile IClientApplication env;

    protected IClientEnvironment getEnvironment() {
        if (Thread.currentThread() instanceof IContextEnvironment) {
            return ((IContextEnvironment) Thread.currentThread()).getClientEnvironment();
        } else {
            return null;
        }

    }

    protected IClientApplication getApplication() {
        if (env == null) {
            final Thread t = Thread.currentThread();

            if (t instanceof IEnvironmentAccessor) {
                env = (IClientApplication) ((IEnvironmentAccessor) t).getEnvironment();
            }
            if (env == null) {
                ClassLoader l = t.getContextClassLoader();
                if (l instanceof IEnvironmentAccessor) {
                    env = (IClientApplication) ((IEnvironmentAccessor) l).getEnvironment();
                }
                if (env == null) {
                    for (l = getClass().getClassLoader(); l != null; l = l.getParent()) {
                        if (l instanceof IEnvironmentAccessor) {
                            env = (IClientApplication) ((IEnvironmentAccessor) l).getEnvironment();
                            if (env != null) {
                                break;
                            }
                        }
                    }
                    if (env == null) {
                        throw new RadixError("Unable to find client environment for definition");
                    }
                }
            }
        }
        return env;
    }
}
