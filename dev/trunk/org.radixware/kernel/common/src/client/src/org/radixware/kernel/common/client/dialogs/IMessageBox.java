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

package org.radixware.kernel.common.client.dialogs;

import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;


public interface IMessageBox {

    public static class StandardButton {

//        NO_BUTTON("", DialogResult.ACCEPTED, null),
//        OK("Ok", DialogResult.ACCEPTED, ClientIcon.Dialog.BUTTON_OK),
//        SAVE("Save", DialogResult.ACCEPTED, ClientIcon.CommonOperations.SAVE),
//        SAVE_ALL("Save All", DialogResult.ACCEPTED, null),
//        OPEN("Open", DialogResult.ACCEPTED, ClientIcon.CommonOperations.OPEN),
//        YES("Yes", DialogResult.ACCEPTED, ClientIcon.Dialog.BUTTON_OK),
//        YES_TO_ALL("Yes to All", DialogResult.ACCEPTED, null),
//        NO("No", DialogResult.REJECTED, ClientIcon.Dialog.BUTTON_CANCEL),
//        NO_TO_ALL("No to All", DialogResult.REJECTED, null),
//        ABORT("Abort", DialogResult.REJECTED, ClientIcon.Dialog.BUTTON_CANCEL),
//        RETRY("Retry", DialogResult.ACCEPTED, null),
//        IGNORE("Ignore", DialogResult.ACCEPTED, null),
//        CLOSE("Close", DialogResult.REJECTED, ClientIcon.Dialog.EXIT),
//        CANCEL("Cancel", DialogResult.REJECTED, ClientIcon.Dialog.BUTTON_CANCEL),
//        DISCARD("Discard", DialogResult.REJECTED, ClientIcon.Dialog.BUTTON_CANCEL),
//        HELP("Help", DialogResult.APPLY, null),
//        SKIP("Skip", DialogResult.ACCEPTED, null),
//        APPLY("Apply", DialogResult.APPLY, null),
//        RESET("Reset", DialogResult.REJECTED, ClientIcon.Dialog.BUTTON_CANCEL),
//        RESTORE_DEFAULTS("Restore Defaults", DialogResult.REJECTED, null),
//        ALL("All", DialogResult.ACCEPTED, null);
//        private final String titleKey;
//        private final DialogResult dialogResult;
//        private final ClientIcon icon;
//        private StandardButton(String titleKey, DialogResult dialogResult, ClientIcon icon) {
//            this.titleKey = titleKey;
//            this.dialogResult = dialogResult;
//            this.icon = icon;
//        }
        private EDialogButtonType button;

        private StandardButton(EDialogButtonType button) {
            this.button = button;
        }

        public String getTitle() {
            return getTitle(button);
        }

        public EDialogButtonType getButtonType() {
            return button;
        }

        public String getTitle(IClientEnvironment env) {
            return getTitle(button, env);
        }

        public static String getTitle(EDialogButtonType button) {
            return button.getValue();
        }

        public static String getTitle(EDialogButtonType button, IClientEnvironment env) {
            switch (button) {
                case OK:
                    return env.getMessageProvider().translate("ExplorerDialog", "&OK");
                case SAVE:
                    return env.getMessageProvider().translate("ExplorerDialog", "&Save");
                case SAVE_ALL:
                    return env.getMessageProvider().translate("ExplorerDialog", "Save All");
                case OPEN:
                    return env.getMessageProvider().translate("ExplorerDialog", "Open");
                case YES:
                    return env.getMessageProvider().translate("ExplorerDialog", "&Yes");
                case YES_TO_ALL:
                    return env.getMessageProvider().translate("ExplorerDialog", "Yes to &All");
                case NO:
                    return env.getMessageProvider().translate("ExplorerDialog", "&No");
                case NO_TO_ALL:
                    return env.getMessageProvider().translate("ExplorerDialog", "N&o to All");
                case ABORT:
                    return env.getMessageProvider().translate("ExplorerDialog", "Abort");
                case RETRY:
                    return env.getMessageProvider().translate("ExplorerDialog", "Retry");
                case IGNORE:
                    return env.getMessageProvider().translate("ExplorerDialog", "Ignore");
                case CLOSE:
                    return env.getMessageProvider().translate("ExplorerDialog", "&Close");
                case CANCEL:
                    return env.getMessageProvider().translate("ExplorerDialog", "&Cancel");
                case DISCARD:
                    return env.getMessageProvider().translate("ExplorerDialog", "Discard");
                case HELP:
                    return env.getMessageProvider().translate("ExplorerDialog", "Help");
                case SKIP:
                    return env.getMessageProvider().translate("ExplorerDialog", "Skip");
                case APPLY:
                    return env.getMessageProvider().translate("ExplorerDialog", "Apply");
                case RESET:
                    return env.getMessageProvider().translate("ExplorerDialog", "Reset");
                case RESTORE_DEFAULTS:
                    return env.getMessageProvider().translate("ExplorerDialog", "Restore Default");
                case ALL:
                    return env.getMessageProvider().translate("ExplorerDialog", "All");
                default:
                    return button.getValue();
            }
        }

        public DialogResult getDialogResult() {
            return getDialogResult(button);
        }

        public static DialogResult getDialogResult(EDialogButtonType button) {
            switch (button) {
                case APPLY:
                case HELP:
                    return DialogResult.APPLY;
                case NO:
                case NO_TO_ALL:
                case ABORT:
                case CLOSE:
                case CANCEL:
                case DISCARD:
                case RESET:
                case RESTORE_DEFAULTS:
                    return DialogResult.REJECTED;
                default:
                    return DialogResult.ACCEPTED;
            }
        }

        public ClientIcon getIcon() {
            return getIcon(button);
        }

        public static ClientIcon getIcon(EDialogButtonType button) {
            switch (button) {
                case CLOSE:
                    return ClientIcon.Dialog.EXIT;
                case SAVE:
                    return ClientIcon.CommonOperations.SAVE;
                case OPEN:
                    return ClientIcon.CommonOperations.OPEN;
                case OK:
                case YES:
                    return ClientIcon.Dialog.BUTTON_OK;
                case NO:
                case ABORT:
                case CANCEL:
                case DISCARD:
                case RESET:
                    return ClientIcon.Dialog.BUTTON_CANCEL;
                default:
                    return null;
            }
        }

        public static void setupDialogButton(final IPushButton button, final IClientEnvironment environment, final EDialogButtonType type) {
            button.setObjectName(type.getValue());
            button.setTitle(IMessageBox.StandardButton.getTitle(type, environment));
            final ClientIcon buttonIcon = IMessageBox.StandardButton.getIcon(type);
            if (buttonIcon != null) {
                button.setIcon(environment.getApplication().getImageManager().getIcon(buttonIcon));
            }
            if (type == EDialogButtonType.OK) {
                button.setDefault(true);
            }
        }

        public static StandardButton getForButtonType(EDialogButtonType button) {
            return all[button.ordinal()];
        }
        private static final StandardButton[] all = new StandardButton[EDialogButtonType.values().length];

        static {
            for (int i = 0; i < all.length; i++) {
                all[i] = new StandardButton(EDialogButtonType.values()[i]);
            }
        }
    }

    public EDialogButtonType show(IClientEnvironment env, String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons);

    public void setOptionText(String text);

    public String getOptionText();

    public boolean isOptionActivated();

    public EDialogButtonType execMessageBox();
    
    public void addButton(final EDialogButtonType buttonType, final String title, final Icon icon);
    
    public void removeButton(final EDialogButtonType buttonType);
}
