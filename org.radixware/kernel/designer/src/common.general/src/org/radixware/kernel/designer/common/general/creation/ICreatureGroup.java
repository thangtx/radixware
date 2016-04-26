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

package org.radixware.kernel.designer.common.general.creation;

import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.resources.icons.RadixIcon;


public interface ICreatureGroup {

    public interface ICreature<T extends RadixObject> {

        public interface WizardInfo {

            public boolean hasWizard();

            public Object createFirstStep();
        }

        public WizardInfo getWizardInfo();

        public abstract String getDisplayName();

        public String getDescription();

        /**
         * Creates new instance of object associated with this creation
         * possibility
         */
        public T createInstance();

        public RadixIcon getIcon();

        /**
         * Shows is the creation possibility is currently enabled or not
         */
        public boolean isEnabled();

        /**
         * This method should provide actions required after creation of object
         * but before its addition to container
         */
        public boolean afterCreate(T object);

        /**
         * This method should provide actions required after object is added to
         * container
         */
        public void afterAppend(T object);

        /**
         * The final step of object creation Creates object instance,performs
         * pre- and post-additioanl setup Returns true if object successfully
         * added to container, false otherwise
         *
         * @throws {@linkplain DefinitionError} if any exceptions a thrown by
         * setup and addition
         */
        public T commit();
    }

    /**
     * Returns list of creatures in current group
     */
    public abstract List<ICreature> getCreatures();

    /**
     * Returns group display name
     */
    public abstract String getDisplayName();
}
