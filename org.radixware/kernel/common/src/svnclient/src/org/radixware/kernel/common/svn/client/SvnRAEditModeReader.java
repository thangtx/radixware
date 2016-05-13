/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.svn.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.svn.RadixSvnException;


/**
 *
 * @author akrylov
 */
class SvnRAEditModeReader {

    private static final Map<String, RAMessage.MessageItemTemplate[]> COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP = new HashMap<>();

    static {
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("target-rev", new RAMessage.MessageItemTemplate[]{RAMessage.NUMBER});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("open-root", new RAMessage.MessageItemTemplate[]{RAMessage.LIST, RAMessage.STRING});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("delete-entry", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.LIST, RAMessage.STRING});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("add-dir", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING, RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("open-dir", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING, RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("change-dir-prop", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("close-dir", new RAMessage.MessageItemTemplate[]{RAMessage.STRING});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("add-file", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING, RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("open-file", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING, RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("apply-textdelta", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("textdelta-chunk", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.WORD});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("textdelta-end", new RAMessage.MessageItemTemplate[]{RAMessage.STRING});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("change-file-prop", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("close-file", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("close-edit", new RAMessage.MessageItemTemplate[]{RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("abort-edit", new RAMessage.MessageItemTemplate[]{RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("finish-replay", new RAMessage.MessageItemTemplate[]{RAMessage.LIST});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("absent-dir", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("absent-file", new RAMessage.MessageItemTemplate[]{RAMessage.STRING, RAMessage.STRING});
        COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.put("failure", new RAMessage.MessageItemTemplate[]{RAMessage.NUMBER});
    }

    private SvnRAConnection connection;
    private SvnRAEditor editor;
    private SvnDeltaReader deltaReader;
    private String filePath;

    private boolean done;
    private boolean aborted;
    private boolean forReplay;
    private Map tokens;

    public SvnRAEditModeReader(SvnRAConnection connection, SvnRAEditor editor, boolean forReplay) {
        connection = connection;
        editor = editor;
        deltaReader = new SvnDeltaReader();
        done = false;
        aborted = false;
        forReplay = forReplay;
        tokens = new HashMap();
    }

    public boolean isAborted() {
        return aborted;
    }

    private void storeToken(String token, boolean isFile) {
        tokens.put(token, Boolean.valueOf(isFile));
    }

    private void lookupToken(String token, boolean isFile) throws RadixSvnException {
        final Boolean tokenType = (Boolean) tokens.get(token);
        if (tokenType == null || tokenType != Boolean.valueOf(isFile)) {
            throw new RadixSvnException("Invalid file or dir token during edit");
        }
    }

    private void removeToken(String token) {
        tokens.remove(token);
    }

    private void processCommand(String commandName, List<RAMessage.MessageItem> params) throws RadixSvnException {
        if ("target-rev".equals(commandName)) {
            editor.targetRevision(params.get(0).getLong());
        } else if ("open-root".equals(commandName)) {
            editor.openRoot(params.get(0).getLong());
            String token = params.get(1).getString();
            storeToken(token, false);
        } else if ("delete-entry".equals(commandName)) {
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            editor.deleteEntry(path, params.get(0).getLong());
        } else if ("add-dir".equals(commandName)) {
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            String copyFromPath = params.get(3).getString();            
            editor.addDir(path, copyFromPath, params.get(4).getLong());
            storeToken(params.get(2).getString(), false);
        } else if ("open-dir".equals(commandName)) {
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            editor.openDir(path, params.get(3).getLong());
            storeToken(params.get(2).getString(), false);
        } else if ("change-dir-prop".equals(commandName)) {
            lookupToken(params.get(0).getString(), false);
            byte[] bytes = params.get(2).getByteArray();
            String propertyName = params.get(1).getString();
            editor.changeDirProperty(propertyName, new SvnProperties.Value(propertyName, bytes));
        } else if ("close-dir".equals(commandName)) {
            String token = params.get(0).getString();
            lookupToken(token, false);
            editor.closeDir();
            removeToken(token);
        } else if ("add-file".equals(commandName)) {
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            String copyFromPath = params.get(3).getString();
                      storeToken(params.get(2).getString(), true);
            editor.addFile(path, copyFromPath, params.get(4).getLong());
            filePath = path;
        } else if ("open-file".equals(commandName)) {
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            storeToken(params.get(2).getString(), true);
            editor.openFile(params.get(0).getString(), params.get(3).getLong());
            filePath = path;
        } else if ("change-file-prop".equals(commandName)) {
            lookupToken(params.get(0).getString(), true);
            byte[] bytes = params.get(2).getByteArray();
            String propertyName = params.get(1).getString();
            editor.changeFileProperty(filePath, propertyName, new SvnProperties.Value(propertyName, bytes));
        } else if ("close-file".equals(commandName)) {
            String token = params.get(0).getString();
            lookupToken(token, true);
            editor.closeFile(filePath, params.get(1).getString());
            removeToken(token);
        } else if ("apply-textdelta".equals(commandName)) {
            lookupToken(params.get(0).getString(), true);
            editor.applyTextDelta(filePath, params.get(1).getString());
        } else if ("textdelta-chunk".equals(commandName)) {
            lookupToken(params.get(0).getString(), true);
            byte[] chunk = params.get(1).getByteArray();
            deltaReader.nextWindow(chunk, 0, chunk.length, filePath, editor);
        } else if ("textdelta-end".equals(commandName)) {
            // reset delta reader,
            // this should send empty window when diffstream contained only header.
            lookupToken(params.get(0).getString(), true);
            deltaReader.reset(filePath, editor);
            editor.textDeltaEnd(filePath);
        } else if ("close-edit".equals(commandName)) {
            editor.closeEdit();
            done = true;
            aborted = false;
            connection.write(RAMessage.MessageItem.newWord("success"), RAMessage.MessageItem.emptyList());
        } else if ("abort-edit".equals(commandName)) {
            editor.abortEdit();
            done = true;
            aborted = true;
            connection.write(RAMessage.MessageItem.newWord("success"), RAMessage.MessageItem.emptyList());
        } else if ("failure".equals(commandName)) {
            editor.abortEdit();
            done = true;
            aborted = true;
            throw new RadixSvnException("Unabled to continue wuth reading");
        } else if ("absent-dir".equals(commandName)) {
            lookupToken(params.get(1).getString(), false);
            editor.absentDir(params.get(0).getString());
        } else if ("absent-file".equals(commandName)) {
            lookupToken(params.get(1).getString(), false);
            editor.absentFile(params.get(0).getString());
        } else if ("finish-replay".equals(commandName)) {
            if (!forReplay) {
                throw new RadixSvnException("Command 'finish-replay' invalid outside of replays");
            }
            done = true;
            aborted = false;
        }
    }

    public void driveEditor() throws RadixSvnException {
        while (!done) {
            List<RAMessage.MessageItem> items = connection.read(false, RAMessage.WORD, RAMessage.NUMBER);
            final String commandName = items.get(0).getString();
            final RAMessage.MessageItemTemplate[] template = COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.get(commandName);
            if (template == null) {
                throw new RadixSvnException("Unknown editor command '" + commandName + "'");
            }
            if (template != null && items.get(1) instanceof Collection) {
                List<RAMessage.MessageItem> parameters = connection.read(true, template);
                try {
                    processCommand(commandName, parameters);
                } catch (RadixSvnException e) {
                    throw e;
                }
            }
        }

        while (!done) {
            List<RAMessage.MessageItem> items = connection.read(false, RAMessage.WORD, RAMessage.NUMBER);
            final String command = items.get(0).getString();
            done = "abort-edit".equals(command) || "success".equals(command);
        }
    }
}
