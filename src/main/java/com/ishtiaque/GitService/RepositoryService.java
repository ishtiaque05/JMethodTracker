package com.ishtiaque.GitService;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;

public class RepositoryService {

    public static void startTracking(Git git, ObjectId startId) throws IOException, GitAPIException {
        // use the following instead to list commits on a specific branch
        //ObjectId branchId = repository.resolve("HEAD");
        Iterable<RevCommit> commits = git.log().add(startId).call();

//        Iterable<RevCommit> commits = git.log().all().call();
        int count = 0;
        for (RevCommit commit : commits) {
            System.out.println(commit.getName());
            count++;
        }
        System.out.println(count);
    }

}
