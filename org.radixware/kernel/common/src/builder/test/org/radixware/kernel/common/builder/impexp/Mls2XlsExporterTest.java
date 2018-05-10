/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.builder.impexp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.enums.EMultilingualStringKind;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;

/**
 *
 * @author akrylov
 */
public class Mls2XlsExporterTest {

    public Mls2XlsExporterTest() {
    }

    @Test
    public void testExport() throws IOException {
        final Branch branch = Branch.Factory.loadFromDir(new File("E:\\radix"));
        Date currentDate = new Date();
        //Date weekAgo = new Date(currentDate.getTime() - 7 * 24 * 60 * 60 * 1000);
        Date monthAgo = new Date(currentDate.getTime() + 30 * 24 * 60 * 60 * 1000);
        Map<Layer, List<Module>> layers = new HashMap<>();
        Layer l = branch.getLayers().findByURI("org.radixware");
        if (l != null){
            layers.put(l, null);
        }
        Mls2XlsExporter exporter = new Mls2XlsExporter(branch, layers, monthAgo, currentDate, "dsafonov", new ArrayList<>(EnumSet.allOf(EMultilingualStringKind.class)));
        exporter.doExport(new File("C:/Users/avoloshchuk/Music/New folder/strings.xls"));
    }
    
//    @Test
    public void testBigXls() throws FileNotFoundException, IOException {
        long t = System.currentTimeMillis();
        try {
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("sheet");
            for (int i = 0; i < 65535; i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < 10; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue("Long cell text...................................................................... " 
                            + i + 
                            " .................................................. more ..................................... " + j);
                }
            }
            FileOutputStream fileOut = new FileOutputStream("poi_workbook.xls");
            wb.write(fileOut);
            fileOut.close();
        } finally {
            System.out.println(System.currentTimeMillis() - t);
        }
    }
}
