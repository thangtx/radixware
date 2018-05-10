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
package org.radixware.kernel.common.defs.dds.radixdoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Utility for generating HTML with JavaScript invocation
 * of database diagrams visualization library (see "joint-db-diagrams.js").
 * This JS-library based on JointJS (see "joint.js") and extends its.
 * @author ashamsutdinov
 */
public class DdsDiagramRadixdocSupport {
    
    protected static String template = loadTemplate();
 
    /**
     * Loads HTML-template (see resource "/template.html") and
     * prepares it to next processing.
     * @return HTML-template
     */
    protected static String loadTemplate() {
        try {
            InputStream is = DdsDiagramRadixdocSupport.class.getResourceAsStream("template.html");

            InputStreamReader isr = new InputStreamReader(is);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(isr);

            String read = br.readLine();

            while(read != null) {
                sb.append(read);
                sb.append(System.lineSeparator());
                read = br.readLine();
                
            }
            
            return sb.toString();

        } catch (IOException ex) {
            Logger.getLogger(DdsDiagramRadixdocSupport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Generates prepared HTML with working scripts and JSON data of DDS.
     * DDS meta information format specification:
     * <pre>
     *  {
     *     "tables": 
     *         [
     *             { 
	 *                  "name": "TableName",
     *                  "id": "TableId",
     *                  "position": { 
     *                      "x": "xPos", 
     *                      "y": "yPos" 
     *                  },
     *                  "link": "TableLink",
	 *                  "columns": [ "ColumnsNames" ],
     *                  "columns_link": "ColumnsLink",
	 *                  "indicies": [ "IndiciesNames" ],
     *                  "indicies_link": "IndiciesLink",
	 *                  "triggers": [ "TriggersName" ],
     *                  "triggers_link": "TriggersLink"
     *              }
     *          ],
     *      "views": 
     *          [
     *              { 
     *                  "name": "ViewName",
     *                  "id": "ViewId",
     *                  "position": { 
     *                      "x": "xPos", 
     *                      "y": "yPos" 
     *                  },
     *                  "link": "ViewLink",
	 *                  "columns": [ "ColumnsNames" ],
     *                  "columns_link": "ColumnsLink",
	 *                  "indicies": [ "IndiciesNames" ],
     *                  "indicies_link": "IndiciesLink",
	 *                  "triggers": [ "TriggersName" ],
     *                  "triggers_link": "TriggersLink"
     *              }
     *          ],
     *      "shortcuts": 
     *          [
     *              { 
     *                  "name": "ShortcutName",
     *                  "id": "ShortcutId", (!)
     *                  "position": { 
     *                      "x": "xPos", 
     *                      "y": "yPos" 
     *                  },
     *                  "link": "ShortcutLink",
     *                  "fields": ["FieldName"]
     *              }
     *          ],
     *      "sequences": 
     *          [
     *              { 
     *                  "name": "SequenceName",
     *                  "id": "SequenceId",
     *                  "position": { 
     *                      "x": "xPos", 
     *                      "y": "yPos" 
     *                  },
     *                  "link": "SequenceLink"
     *              }
     *          ],
     *      "labels":
     *          [
     *              {
     *                  "text": "LabelText",
     *                  "position": { 
     *                      "x": "xPos", 
     *                      "y": "yPos" 
     *                  }
     *              }
     *          ]
     *      "references": 
     *          [
     *              { 
     *                  "type" : "ForeignKeyOrMasterDetail",
     *                  "link": "ReferenceLink",
     *                  "source": "ChildTableId",
     *                  "target": "ParentTableId", 
     *                  "childColumn": "ChildColumnName", 
     *                  "parentColumn": "ParentColumnName"
     *              }
     *          ]
     *  } 
     * </pre>
     * Each of table and view fields (columns, indicies and triggers) can be linkable 
     * (reference to RadixDoc).
     * Link's format: FieldInfo@LinkToDoc
     * Example:
     * <pre>
     *     "tables": 
     *         [
     *             { 
     *                  ...
	 *                  "columns": [ "id: NUMBER(9,0)@C://radixdoc/..." ],
     *                  ...
     *              }
     *          ],
     * ...
     * </pre>
     * Mandatory fields:  
     * - For tables, views, sequences and shorcuts: name, position;
     * - For labels: position;
     * - For references: type, source/target;
     * type ::= "ForeignKey" | "MasterDetail";
     * source/target ::= id;                  
     *  
     * Delimiter is '@' - don't include this symbol to field information.
     * @param jsonData input DDS meta information in JSON-format (see specification)
     * @return handled HTML with generated JavaScript
     */
    public static String generateHtml(final JSONObject jsonData) throws JSONException  {    
        // magic number 4 - it's indent factor (cause by default tab = 4 spaces)
        // don't alter for best look of generated JavaScript code.
        loadTemplate();
        return String.format(template, jsonData.toString(4));
    }
 
}
