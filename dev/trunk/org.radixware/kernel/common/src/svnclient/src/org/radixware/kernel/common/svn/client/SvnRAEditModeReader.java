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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.client.RAMessage.MessageItemType;


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
    
    private final SvnRAConnection connection;
    private final SvnRAEditor editor;
    private final SvnDeltaReader deltaReader;
    private String filePath;

    private boolean done;
    private boolean aborted;
    private final boolean forReplay;
    private final Map tokens;

    public SvnRAEditModeReader(SvnRAConnection connection, SvnRAEditor editor, boolean forReplay) {
        this.connection = connection;
        this.editor = editor;
        this.deltaReader = new SvnDeltaReader();
        this.done = false;
        this.aborted = false;
        this.forReplay = forReplay;
        this.tokens = new HashMap();
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
    
    private static long getDefaultLong() {
        //new Long(SVNRepository.INVALID_REVISION);
        return -1;
    }

    private static String getDefaultString() {
        return null;
    }
    
    private static byte[] getDefaultBytes() {
        return null;
    }
    
 
    
    private static long getNumberFromTheList(final RAMessage.MessageItem param) throws RadixSvnException{
        RAMessage.checkItemType(param, MessageItemType.LIST);
        final List<RAMessage.MessageItem> list = param.getList();
        if (list.isEmpty()){
            return getDefaultLong();
        }
        final RAMessage.MessageItem subItem = list.get(0);
        RAMessage.checkItemType(subItem, MessageItemType.NUMBER);
        return subItem.getLong();        
    }
    
    private static String getStringFromTheList(final RAMessage.MessageItem param) throws RadixSvnException{
        RAMessage.checkItemType(param, MessageItemType.LIST);
        final List<RAMessage.MessageItem> list = param.getList();
        if (list.isEmpty()){
            return getDefaultString();
        }
        final RAMessage.MessageItem subItem = list.get(0);
        RAMessage.checkItemType(subItem, MessageItemType.STRING);
        return subItem.getString();
    }
    
    
    private static byte[] getBytesFromTheList(final RAMessage.MessageItem param) throws RadixSvnException{
        RAMessage.checkItemType(param, MessageItemType.LIST);
        final List<RAMessage.MessageItem> list = param.getList();
        if (list.isEmpty()){
            return getDefaultBytes();
        }
        final RAMessage.MessageItem subItem = list.get(0);
        RAMessage.checkItemType(subItem, MessageItemType.BYTES, MessageItemType.STRING);
        return subItem.getByteArray();
    }    
    
    private static class StrAndLong{
        String _string;
        long _long;
    }
    private static StrAndLong getStrAndNumFromTheList(final RAMessage.MessageItem param) throws RadixSvnException{
        RAMessage.checkItemType(param, MessageItemType.LIST);
        final List<RAMessage.MessageItem> list = param.getList();
        final StrAndLong rez = new StrAndLong();
        if (list.isEmpty()){
            rez._string = getDefaultString();
            rez._long = getDefaultLong();
            return rez;
        }
        if (list.size()!=2){
            throw new RadixSvnException("Incorrect message item : " +  param.toString());
        }
        RAMessage.checkItemType(list.get(0), MessageItemType.STRING);
        RAMessage.checkItemType(list.get(1), MessageItemType.NUMBER);
        rez._string = list.get(0).getString();
        rez._long = list.get(1).getLong();
        return rez;
    }
    
    // byte[] 
    

    private void processCommand(String commandName, List<RAMessage.MessageItem> params) throws RadixSvnException {
        if ("target-rev".equals(commandName)) {                                 //"r"
            editor.targetRevision(params.get(0).getLong());
        } else if ("open-root".equals(commandName)) {                           //"(?r)s"
            editor.openRoot(getNumberFromTheList(params.get(0)));//params.get(0).getLong()
            String token = params.get(1).getString();
            storeToken(token, false);
        } else if ("delete-entry".equals(commandName)) {                        //"s(?r)s"
            lookupToken(params.get(2).getString(), false);
            final String path = params.get(0).getString();
            editor.deleteEntry(path, getNumberFromTheList(params.get(1)));//params.get(1).getLong()
        } else if ("add-dir".equals(commandName)) {                             //"sss(?sr)"
            lookupToken(params.get(1).getString(), false);
            final String path = params.get(0).getString();
            final StrAndLong s2l = getStrAndNumFromTheList(params.get(3));
            final String copyFromPath = s2l._string;//params.get(3).getString();
            editor.addDir(path, copyFromPath, s2l._long);//params.get(4).getLong();   
            storeToken(params.get(2).getString(), false);
        } else if ("open-dir".equals(commandName)) {                            //"sss(?r)"
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            editor.openDir(path, getNumberFromTheList(params.get(3)));//params.get(3).getLong()
            storeToken(params.get(2).getString(), false);
        } else if ("change-dir-prop".equals(commandName)) {                     //ss(?b)
            lookupToken(params.get(0).getString(), false);
            byte[] bytes = getBytesFromTheList(params.get(2));//params.get(2).getByteArray()
            String propertyName = params.get(1).getString();
            editor.changeDirProperty(propertyName, new SvnProperties.Value(null, bytes));
        } else if ("close-dir".equals(commandName)) {                           //"s"
            String token = params.get(0).getString();
            lookupToken(token, false);
            editor.closeDir();
            removeToken(token);
        } else if ("add-file".equals(commandName)) {                            //sss(?sr)
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            final StrAndLong s2l = getStrAndNumFromTheList(params.get(3));
            String copyFromPath = s2l._string;//params.get(3).getString();
                      storeToken(params.get(2).getString(), true);
            editor.addFile(path, copyFromPath, s2l._long);//params.get(4).getLong()
            filePath = path;
        } else if ("open-file".equals(commandName)) {                           //"sss(?r)"
            lookupToken(params.get(1).getString(), false);
            String path = params.get(0).getString();
            storeToken(params.get(2).getString(), true);
            editor.openFile(params.get(0).getString(), getNumberFromTheList(params.get(3)));//params.get(3).getLong()
            filePath = path;
        } else if ("change-file-prop".equals(commandName)) {                    //ss(?b)
            lookupToken(params.get(0).getString(), true);
            byte[] bytes = getBytesFromTheList(params.get(2));//params.get(2).getByteArray();
            final String propertyName = params.get(1).getString();
            editor.changeFileProperty(filePath, propertyName, new SvnProperties.Value(null, bytes));
        } else if ("close-file".equals(commandName)) {                          //"s(?b)"
            String token = params.get(0).getString();
            lookupToken(token, true);
            editor.closeFile(filePath, getStringFromTheList(params.get(1)));// params.get(1).getString()
            removeToken(token);
        } else if ("apply-textdelta".equals(commandName)) {                     //"s(?s)"
            lookupToken(params.get(0).getString(), true);
            editor.applyTextDelta(filePath, getStringFromTheList(params.get(1)));//params.get(1).getString()
        } else if ("textdelta-chunk".equals(commandName)) {                     //"sb"
            lookupToken(params.get(0).getString(), true);
            byte[] chunk = params.get(1).getByteArray();
            deltaReader.nextWindow(chunk, 0, chunk.length, filePath, editor);
        } else if ("textdelta-end".equals(commandName)) {                       //"s"
            // reset delta reader,
            // this should send empty window when diffstream contained only header.
            lookupToken(params.get(0).getString(), true);
            deltaReader.reset(filePath, editor);
            editor.textDeltaEnd(filePath);
        } else if ("close-edit".equals(commandName)) {                          //"()"
            editor.closeEdit();
            done = true;
            aborted = false;
            connection.write(RAMessage.MessageItem.newWord("success"), RAMessage.MessageItem.emptyList());
        } else if ("abort-edit".equals(commandName)) {                          //"()"
            editor.abortEdit();
            done = true;
            aborted = true;
            connection.write(RAMessage.MessageItem.newWord("success"), RAMessage.MessageItem.emptyList());
        } else if ("failure".equals(commandName)) {                             //"l"
            editor.abortEdit();
            done = true;
            aborted = true;
            throw new RadixSvnException("Unabled to continue wuth reading");
        } else if ("absent-dir".equals(commandName)) {                          //"ss"
            lookupToken(params.get(1).getString(), false);
            editor.absentDir(params.get(0).getString());
        } else if ("absent-file".equals(commandName)) {                         //"ss"
            lookupToken(params.get(1).getString(), false);
            editor.absentFile(params.get(0).getString());
        } else if ("finish-replay".equals(commandName)) {                       //"()"
            if (!forReplay) {
                throw new RadixSvnException("Command 'finish-replay' invalid outside of replays");
            }
            done = true;
            aborted = false;
        }
        else {
            throw new RadixSvnException("Unsupported command \'" + commandName + "\'");
        }
    }

    public void driveEditor() throws RadixSvnException {
        while (!done) {
            List<RAMessage.MessageItem> items = connection.read(false, RAMessage.WORD, RAMessage.LIST);
            
            final String commandName = items.get(0).getString();
            final RAMessage.MessageItemTemplate[] template = COMMAND_NAME_TO_MESSAGE_TEMPLATE_MAP.get(commandName);
            if (template == null) {
                throw new RadixSvnException("Unknown editor command '" + commandName + "'");
            }
            if (items.get(1).type == MessageItemType.LIST) {
                
                final List<RAMessage.MessageItem> parameters = items.get(1).getList();
                try {
                    processCommand(commandName, parameters);
                } catch (RadixSvnException e) {
                    throw e;
                }
            }
        }

        while (!done) {
            List<RAMessage.MessageItem> items = connection.read(false, RAMessage.WORD, RAMessage.LIST);
            final String command = items.get(0).getString();
            done = "abort-edit".equals(command) || "success".equals(command);
        }
    }
}
