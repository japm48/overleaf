package uk.ac.ic.wlgitbridge.writelatex.model;

import uk.ac.ic.wlgitbridge.bridge.CandidateSnapshot;
import uk.ac.ic.wlgitbridge.bridge.CandidateSnapshotCallback;
import uk.ac.ic.wlgitbridge.bridge.RawDirectoryContents;
import uk.ac.ic.wlgitbridge.bridge.WritableRepositoryContents;
import uk.ac.ic.wlgitbridge.writelatex.SnapshotPostException;
import uk.ac.ic.wlgitbridge.writelatex.WLDirectoryNodeSnapshot;
import uk.ac.ic.wlgitbridge.writelatex.api.request.exception.FailedConnectionException;
import uk.ac.ic.wlgitbridge.writelatex.api.request.getdoc.exception.InvalidProjectException;
import uk.ac.ic.wlgitbridge.writelatex.filestore.store.WLFileStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Winston on 06/11/14.
 */
public class WLDataModel implements CandidateSnapshotCallback {

    private final Map<String, WLProject> projects;
    private final WLFileStore fileStore;

    public WLDataModel(String rootGitDirectoryPath) {
        projects = new HashMap<String, WLProject>();
        fileStore = new WLFileStore(rootGitDirectoryPath);
    }

    public List<WritableRepositoryContents> updateProjectWithName(String name) throws FailedConnectionException, InvalidProjectException {
        return fileStore.updateForProject(getProjectWithName(name));
    }

    public WLProject getProjectWithName(String name) {
        WLProject project;
        if (projects.containsKey(name)) {
            project = projects.get(name);
        } else {
            project = new WLProject(name);
            projects.put(name, project);
        }
        return project;
    }

    public CandidateSnapshot createCandidateSnapshotFromProjectWithContents(String projectName, RawDirectoryContents directoryContents) throws SnapshotPostException, IOException, FailedConnectionException {
        return new WLDirectoryNodeSnapshot(getProjectWithName(projectName),
                                           fileStore.createNextDirectoryNodeInProjectFromContents(getProjectWithName(projectName),
                                                                                                  directoryContents),
                                           this);
    }

    @Override
    public void approveSnapshot(int versionID, CandidateSnapshot candidateSnapshot) {

    }

}
