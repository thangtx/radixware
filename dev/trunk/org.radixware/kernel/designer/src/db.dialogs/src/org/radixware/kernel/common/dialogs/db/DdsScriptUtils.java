package org.radixware.kernel.common.dialogs.db;

import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class DdsScriptUtils {
    public final static String INCOMPATIBLE_COLUMN_MESSAGE = "The script is incompatible if it contains:\n"
            + "\n"
            + "     - Delete or rename a column or table\n"
            + "     - Change column type\n"
            + "     - Delete or change PL/SQL method\n"
            + "     - Rebuilding index of a large table\n"
            + "     - Incompatible change in operational data\n";
    public final static String COMATIBILE_QESTION = 
            INCOMPATIBLE_COLUMN_MESSAGE
            + "\n"
            + "Attention! The execution of an incompatible script requires "
            + "a complete shutdown of the system, which is extremely undesirable!\n"
            + "Are you sure?";
     public final static String COMATIBILE_MESSAGE = "<html>Attention! There are incompatible scripts found. "
             + "The execution of an incompatible script requires "
            + "a complete shutdown of the system, which is extremely undesirable!</html>";
     
    public static boolean showCompatibleWarning(){
        return DialogUtils.messageConfirmation(COMATIBILE_QESTION);
    }
}
