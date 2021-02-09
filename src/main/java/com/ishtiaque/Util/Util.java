package com.ishtiaque.Util;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class Util {
    public static Repository createRepository(String repositoryPath) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder.setGitDir(new File(repositoryPath))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
    }

    public static String buildMethodIdentifier(String filepath, String methodname, int startline) {
        return filepath + "#" + methodname + "#" + startline;
    }
}
