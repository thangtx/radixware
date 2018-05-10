package org.radixware.kernel.common.builder.release;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnPathUtils;
import org.radixware.kernel.common.svn.utils.SVN;

public class ScriptFileInfo {


    private final File file;
    private final ReleaseSettings settings;
    private final SVNRepositoryAdapter repository;
    private final long revision;
    
    ConflictCommitInfo conflictCommitInfo;

    public ScriptFileInfo(File file, ReleaseSettings settings, SVNRepositoryAdapter repository, long revision) {
        this.settings = settings;
        this.repository = repository;
        this.revision = revision;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public SVNRepositoryAdapter getRepository() {
        return repository;
    }

    public ReleaseSettings getSettings() {
        return settings;
    }

    public void getSvnInfo(List<String> authors, List<Long> revisions, long startRevision, long endRevision, int maxCount) throws URISyntaxException, RadixSvnException {
        File currentFile = getFile();
        if (currentFile != null) {
            if (SVN.isNormalSvnStatus(settings.getSvnClientAdapter(), currentFile)) {
                final String path = SvnPathUtils.getFilePath(settings.getSvnClientAdapter(), repository, currentFile);
                if (SVN.isExists(repository, path, revision)) {
                    SVN.getSvnLog(repository, path, null, authors, revisions, null, startRevision, endRevision, maxCount);
                }
            }
        }
        
    }
    
    public IFlowLogger getLogger() {
        return settings.getLogger();
    }

    public long getRevision() {
        return revision;
    }

    public ConflictCommitInfo getCommitInfo() {
        return conflictCommitInfo;
    }

    public void setConflictCommitInfo(ConflictCommitInfo conflictCommitInfo) {
        this.conflictCommitInfo = conflictCommitInfo;
    }

    public static class ConflictCommitInfo{
        private final String lastAuthor;
        private final long revision;

        public ConflictCommitInfo(String lastAuthor, long revision) {
            this.lastAuthor = lastAuthor;
            this.revision = revision;
        }

        public String getLastAuthor() {
            return lastAuthor;
        }

        public long getRevision() {
            return revision;
        }

    }
}
