package org.radixware.kernel.common.builder.impexp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;


public class Mls2XlsImportTest {
    
    @Test
    public void testExport() throws IOException {
        final Branch branch = Branch.Factory.loadFromDir(new File("E:\\radix"));
        Xls2MlsImporter importer = new Xls2MlsImporter(branch);
        importer.doImport(new File("C:/Users/avoloshchuk/Music/New folder/1.xls"), true, true);
        System.out.println("----------------Problems--------------------------");
        System.out.println(importer.getImportInfo().getProblems().toString());
        System.out.println("----------------Conflicts-------------------------");
        System.out.println(importer.getImportInfo().getConflicts().toString());
    }
}
