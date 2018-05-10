package org.radixware.kernel.common.builder.impexp;

import java.util.ArrayList;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;

public class ImportConflicts extends ArrayList<ImportConflict> {

    private final IMultilingualStringDef string;
    private final int row;
    private final String qualifiedName;
    private String context;
    private boolean isLoded = false;
    private ImportInfo importInfo;

    public ImportConflicts(IMultilingualStringDef string, int row, String qualifiedName, ImportInfo meta) {
        this.string = string;
        this.row = 0;
        this.qualifiedName = qualifiedName;
        this.importInfo = meta;
    }

    public IMultilingualStringDef getString() {
        return string;
    }

    public int getRowNum() {
        return row;
    }

    public ImportInfo getImportInfo() {
        return importInfo;
    }

    @Override
    public String toString() {
        if (context == null){
            context = createContext();
        }
        return context;
    }
    
    private String createContext() {
        StringBuilder sb = new StringBuilder();
        sb.append("Localizing string #");
        sb.append(string.getId());
        sb.append("\nLocation: ");
        if (qualifiedName != null && !qualifiedName.isEmpty()) {
            sb.append(qualifiedName);
        } else {
            ILocalizingBundleDef ownerBundle = string.getOwnerBundle();
            if (ownerBundle != null) {

                final RadixObject[] strContext = new RadixObject[1];
                Definition def = ownerBundle.findBundleOwner();

                def.visit(new IVisitor() {
                    @Override
                    public void accept(RadixObject radixObject) {
                        if (radixObject instanceof ILocalizedDef) {
                            ILocalizedDef ld = (ILocalizedDef) radixObject;
                            final ArrayList<ILocalizedDef.MultilingualStringInfo> infos = new ArrayList<>();
                            ld.collectUsedMlStringIds(infos);
                            for (ILocalizedDef.MultilingualStringInfo info : infos) {
                                if ((info != null) && (info.getId() != null) && (info.getId().equals(string.getId()))) {
                                    strContext[0] = radixObject;

                                }
                            }
                        }
                    }
                }, new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof ILocalizedDef;
                        }
                    });
                
                if (strContext == null) {
                    sb.append(def.getQualifiedName());
                } else {
                    sb.append(strContext[0].getQualifiedName());
                }
                
                
            }
        }
        return sb.toString();
    }

}
