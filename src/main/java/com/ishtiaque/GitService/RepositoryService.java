package com.ishtiaque.GitService;

import com.ishtiaque.JTracker.Tracker;
import com.ishtiaque.Parser.JavaParser;
import com.ishtiaque.Wrappers.StartEnv;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class RepositoryService {
    private Git git;
    private Repository repository;
    private StartEnv startenv;
    private Tracker tracker;
    public RepositoryService(Git git, Repository repository, StartEnv env, Tracker tracker) {
        this.git = git;
        this.repository = repository;
        this.startenv = env;
        this.tracker = tracker;
    }
    public void startTracking(ObjectId startId) throws IOException, GitAPIException {
        // use the following instead to list commits on a specific branch
        //ObjectId branchId = repository.resolve("HEAD");
        Iterable<RevCommit> commits = git.log().add(startId).call();

//        Iterable<RevCommit> commits = git.log().all().call();
        int count = 0;
        for (RevCommit commit : commits) {
            System.out.println(commit.getName());
            if (startenv.getStartcommit().equals(commit.getName())) {
                tracker.setCurrentCommit(commit.getName());
                tracker.setPrevCommit("");
            } else {
                tracker.setPrevCommit(tracker.getCurrentCommit());
                tracker.setCurrentCommit(commit.getName());
            }
            iterateAllFiles(commit.getName());
            count++;
        }
        System.out.println(count);
    }

    public Repository getRepository() { return repository; }
    public Git getGit() { return git; }

    public void iterateAllFiles(String refCommit) throws IOException {
        ObjectId head = repository.resolve(refCommit);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head);
            RevTree tree = commit.getTree();
            System.out.println("Having tree: " + tree);

            // now use a TreeWalk to iterate over all files in the Tree recursively
            // you can set Filters to narrow down the results if needed
            try (TreeWalk treeWalk = new TreeWalk(repository)) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                ArrayList<String> methodList = new ArrayList<String>();;
                while (treeWalk.next()) {
                    System.out.println("found: " + treeWalk.getPathString());
                    String filepath = treeWalk.getPathString();
                    if(filepath.endsWith(".java")) {
                        ObjectId blobId = treeWalk.getObjectId(0);
//                        startenv.getRepopath() + filepath
                        JavaParser.parseFile(getFileContentByObjectId(blobId), filepath, methodList);
                    }
                }
                this.tracker.addData(refCommit, methodList);
//                TODO compare data here
            }
        }
    }

    public String getFileContentByObjectId(ObjectId objectId) throws IOException {
        ObjectLoader loader = this.repository.open(objectId);
        OutputStream output = new OutputStream()
            {
                private StringBuilder string = new StringBuilder();
                @Override
                public void write(int b) {
                    this.string.append((char) b);
                }
                public String toString(){
                    return this.string.toString();
                }
            };
            loader.copyTo(output);
            String fileContent = output.toString();
        return fileContent;
    }

}
