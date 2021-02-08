package com.ishtiaque.GitService;

import com.ishtiaque.Parser.JavaParser;
import com.ishtiaque.Wrappers.StartEnv;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.IOException;

public class RepositoryService {
    private Git git;
    private Repository repository;
    private StartEnv startenv;
    public RepositoryService(Git git, Repository repository, StartEnv env) {
        this.git = git;
        this.repository = repository;
        this.startenv = env;
    }
    public void startTracking(ObjectId startId) throws IOException, GitAPIException {
        // use the following instead to list commits on a specific branch
        //ObjectId branchId = repository.resolve("HEAD");
        Iterable<RevCommit> commits = git.log().add(startId).call();

//        Iterable<RevCommit> commits = git.log().all().call();
        int count = 0;
        for (RevCommit commit : commits) {
            System.out.println(commit.getName());
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
                while (treeWalk.next()) {
                    System.out.println("found: " + treeWalk.getPathString());
                    String filepath = treeWalk.getPathString();
                    if(filepath.endsWith(".java")) {
                        JavaParser.parseFile(startenv.getRepopath() + filepath);
                    }
                }
            }
        }
    }

}
