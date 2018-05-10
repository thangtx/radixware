package org.radixware.kernel.common.builder.release;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.repository.dds.DdsScript;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.utils.Utils;

public class ScriptInfo extends ScriptFileInfo{
    final DdsScript script;
    final DdsUpdateInfo info;
    
    private boolean backwardCompatible;
    private String content = null;

    public ScriptInfo(DdsScript script, DdsUpdateInfo info, ReleaseSettings settings, final SVNRepositoryAdapter repository, long revision) {
        super(null, settings, repository, revision);
        this.script = script;
        this.info = info;
        this.backwardCompatible = info.isBackwardCompatible();
    }
    
    public Long getVersion() {
        try {
            return Long.parseLong(script.getName());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public String getFileBaseName() {
        return script.getFileBaseName();
    }
    
    @Override
    public File getFile(){
        return script.getFile();
    }
    
    public boolean isBackwardCompatible() {
        return backwardCompatible;
    }

    public void setBackwardCompatible(boolean isBackwardCompatible) {
        if (this.backwardCompatible != isBackwardCompatible) {
            backwardCompatible = isBackwardCompatible;
        }
    }
    
    public String getContent(){
        if (content == null) {
            return getFileContent();
        }
        return content;
    }
    
    private String getFileContent(){
        try {
            return script.getContent();
        } catch (IOException ex) {
            String message = "Unable to read script \'" + script.getQualifiedName() + "\'";
            getLogger().error(message);
            Logger.getLogger(ScriptInfo.class.getName()).log(Level.WARNING, message, ex);
        }
        return "";
    }
    

    public void setContent(final String content){
        this.content = content;
    }
    
    public boolean isContentModified(){
        if (content != null){
            String fileContent = getFileContent();
            return !Utils.equals(content, fileContent);
        }
        return false;
    }
    
    public boolean isScriptInfoModified(){
        return info.isBackwardCompatible() != backwardCompatible;
    }
    
    
    public boolean save() throws IOException {
        boolean modifyScriptFile = false;
        if (isContentModified()) {
            script.setContent(content);
            modifyScriptFile = true;
        }
        
        if (isScriptInfoModified()) {
            info.setBackwardCompatible(backwardCompatible);
//            DdsScripts scripts = info.getOwnerScripts();
//            if (scripts != null){
//                try {
//                    scripts.save();
//                } catch (IOException ex) {
//                    String message = "Unable to save \'" + scripts.getQualifiedName() + "\'";
//                    settings.getLogger().error(message);
//                    Logger.getLogger(ScriptInfo.class.getName()).log(Level.WARNING, message, ex);
//                }
//            }
        }
        
        return modifyScriptFile;
    }

    public DdsScript getScript() {
        return script;
    }
    
    public String getName(){
        return script.getName();
    }
    
    
    public String getLastModifer() throws URISyntaxException, RadixSvnException {
        List<String> authors = new ArrayList<>();
        List<Long> revisions = new ArrayList<>();
        getSvnInfo(authors, revisions, getRevision(), 1, 1);

        return authors.isEmpty() ? "" : authors.get(0);
    }
    
}
