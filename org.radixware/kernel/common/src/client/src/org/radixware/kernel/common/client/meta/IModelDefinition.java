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

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.Restrictions;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.types.Id;

/**
 * Interface for definitions, that may be used to create {@link Model explorer model}
 */
public interface IModelDefinition {

    /**
     * Get identifier of owner definition (offten {@link RadClassPresentationDef class presentation}).
     * @return identifier of owner definition.
     */
    public Id getOwnerClassId();

    /**
     * Get unique identifier of definition.
     * @return definition identifier.
     */
    public Id getId();

    /**
     * Returns property definition with specified identifier.<p>
     * If no such property exists {@link org.radixware.kernel.explorer.errors.NoDefinitionWithSuchIdError NoDefinitionWithSuchIdError}
     * exception  will be thrown.
     * @param id identifier of property.
     * @return property definition.
     */
    public RadPropertyDef getPropertyDefById(Id id);

    /**
     * Returns true if this definition holds property with specified identifier; otherwise returns false.
     * @param id identifier of property.
     * @return return true if this definition holds property.
     */
    public boolean isPropertyDefExistsById(Id id);

    /**
     * Returns command definition with specified identifier.<p>
     * If no such command exists {@link org.radixware.kernel.explorer.errors.NoDefinitionWithSuchIdError NoDefinitionWithSuchIdError}
     * exception  will be thrown.
     * @param id identifier of command.
     * @return command definition.
     */
    public RadCommandDef getCommandDefById(Id id);

    /**
     * Returns true if this definition holds property with specified identifier; otherwise returns false.
     * @param id identifier of property.
     * @return true if this definition holds property with specified identifier;
     */
    public boolean isCommandDefExistsById(Id id);

    /**
     * Get list of commands (including contextless commands) applyable for this definition
     * (including contextless commands) applyable for this definition
     *
     * @return list of commands
     */
    public List<RadCommandDef> getEnabledCommands();

    /**
     * Returns restrictions of this definition.
     * @return definition restrictions.
     */
    public Restrictions getRestrictions();

    /**
     * Returns definition`s icon.
     * If this definition does not have an icon this method returns null.
     * @return definitions icon.
     */
    public Icon getIcon();

    /**
     * Returns definition`s localized title.
     * If this definition does not have a title this method returns "<No Title>" string.
     * @return definitions title.
     */
    public String getTitle();

    /**
     * Creates standard explorer visual component for this definition.
     * @return IView instance.
     */
    public IView createStandardView(IClientEnvironment environment);

    /**
     * Create explorer model based on this definition.<p>
     * If some errors occures during model creations {@link org.radixware.kernel.explorer.errors.ModelCreationError ModelCreationError}
     *  will be thrown.
     * @return explorer model.
     */
    public Model createModel(IContext.Abstract context);
}
