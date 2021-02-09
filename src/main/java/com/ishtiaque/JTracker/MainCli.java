package com.ishtiaque.JTracker;

import com.ishtiaque.GitService.RepositoryService;
import com.ishtiaque.Parser.JavaParser;
import com.ishtiaque.Util.Util;
import com.ishtiaque.Wrappers.StartEnv;
import org.apache.commons.cli.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class MainCli {
    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption(newOption(
                "repopath",
                true,
                "(required) path to the repository (on the local file system)",
                true)
        );
        options.addOption(newOption(
                "startcommit",
                true,
                "hash of the commit to begin with backwards history traversal. Default: HEAD",
                false)
        );
        options.addOption(newOption(
                "outfile",
                true,
                "path to the output file. Default: current working directory",
                false)
        );

        try {
            CommandLine line = parser.parse(options, args);
            String repopath = line.getOptionValue("repopath");
            // Unix vs. Windows. Probably there is a better way to do this.
            String pathDelimiter = repopath.contains("\\\\") ? "\\\\" : "/";
            // Repo paths need to reference the .git directory. We add it to the path if it's not provided.
            String gitPathEnding = pathDelimiter + ".git";
            String repositoryPath = repopath;
            if (!repopath.endsWith(gitPathEnding)) { repositoryPath = repopath + gitPathEnding; }

            String[] split = repositoryPath.replace(gitPathEnding, "").split(pathDelimiter);
            String repositoryName = split[split.length - 1];

            String startCommitName = line.getOptionValue("startcommit");
            if (startCommitName == null) {
                startCommitName = "HEAD";
            }
            // If no output file path was provided the output file will be saved in the current directory.
            String outputFilePath = line.getOptionValue("outfile");
            if (outputFilePath == null) {
                outputFilePath = System.getProperty("user.dir") + "/" + repositoryName + "-trackingInfo.json";
            }
//            FOR DEBUGGING
            StartEnv startenv = new StartEnv(repopath + "/",  startCommitName, outputFilePath, repositoryName);
            Repository repository = Util.createRepository(repositoryPath);
            Git git = new Git(repository);
            JavaParser.setupParser(); // setup parser
            ObjectId startID = repository.resolve(startCommitName);
            Tracker tracker = new Tracker(startCommitName, null);
            RepositoryService repoService = new RepositoryService(git, repository, startenv, tracker);
            repoService.startTracking(startID);

        } catch (ParseException e){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar <codeshovel-jar-file>", options);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static Option newOption(String name, boolean hasArg, String desc, boolean required) {
        Option option = new Option(name, hasArg, desc);
        option.setRequired(required);
        return option;
    }
}
