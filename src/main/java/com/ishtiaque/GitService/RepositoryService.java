package com.ishtiaque.GitService;

import com.ishtiaque.JTracker.Tracker;
import com.ishtiaque.Parser.JavaParser;
import com.ishtiaque.Wrappers.StartEnv;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
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
                tracker.setPrevCommit(null);
            } else {
                tracker.setPrevCommit(tracker.getCurrentCommit());
                tracker.setCurrentCommit(commit.getName());
            }
            try {
                checkoutCMD(commit.getName());
                File folder = new File(startenv.getRepopath());
                iterateAllFiles(folder);
                this.tracker.addData(commit.getName());
                this.tracker.resetCurrentMethodVars();
            } catch (Exception e) {
                e.printStackTrace();
            }

            count++;
        }
        System.out.println(count);
//        Save the JSON here!!
    }

    public Repository getRepository() { return repository; }
    public Git getGit() { return git; }

    public void iterateAllFiles(File folder) throws IOException {
        for(File f: folder.listFiles()) {
            if(f.isDirectory() && f.getName() != ".git") {
                iterateAllFiles(f);
            }
            if (f.isFile() && f.getName().endsWith(".java")) {
                String relativeFilePath = f.getAbsolutePath().replace(this.startenv.getRepopath(), "");
                JavaParser.parseFile(f, relativeFilePath, this.tracker);
            }
        }

//        ObjectId head = repository.resolve(refCommit);
//        try (RevWalk walk = new RevWalk(repository)) {
//            RevCommit commit = walk.parseCommit(head);
//            RevTree tree = commit.getTree();
//            System.out.println("Having tree: " + tree);
//
//            // now use a TreeWalk to iterate over all files in the Tree recursively
//            // you can set Filters to narrow down the results if needed
//            try (TreeWalk treeWalk = new TreeWalk(repository)) {
//                treeWalk.addTree(tree);
//                treeWalk.setRecursive(true);
//                while (treeWalk.next()) {
//                    System.out.println("found: " + treeWalk.getPathString());
//                    String filepath = treeWalk.getPathString();
//                    if(filepath.endsWith(".java")) {
//                        ObjectId blobId = treeWalk.getObjectId(0);
////                        startenv.getRepopath() + filepath
//                        JavaParser.parseFile(getFileContentByObjectId(blobId), filepath, this.tracker);
//                    }
//                }
//                this.tracker.addData(refCommit);
////                TODO compare data here
//            }
//        }
//        this.tracker.resetCurrentMethodVars();
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

    public void checkoutCMD(String commit) throws Exception {
        try {
            git.reset().setMode( ResetCommand.ResetType.HARD ).call();
            git.clean().setCleanDirectories(true).setForce(true);
            git.checkout().setName(commit).setForced(true).call();
            git.clean().setCleanDirectories(true).setForce(true);
            git.reset().setMode( ResetCommand.ResetType.HARD ).call();
        } catch (InvalidRefNameException e) {
            e.printStackTrace();
            throw new Exception("Error");
        } catch (CheckoutConflictException e) {
            e.printStackTrace();
            throw new Exception("Error");
        } catch (RefAlreadyExistsException e) {
            e.printStackTrace();
            throw new Exception("Error");
        } catch (RefNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Error");
        } catch (GitAPIException e) {
            e.printStackTrace();
            throw new Exception("Error");
        }
    }

}
