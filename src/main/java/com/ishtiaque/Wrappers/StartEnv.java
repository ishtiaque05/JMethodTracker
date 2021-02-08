package com.ishtiaque.Wrappers;

public class StartEnv {
    private String repopath;
    private String startcommit;
    private String outfile;
    private String repo;

    public StartEnv(String repopath, String startcommit, String  outfile, String repo) {
        this.repopath = repopath;
        this.startcommit = startcommit;
        this.outfile = outfile;
        this.repo = repo;
    }

    public String getRepopath() { return repopath; }
    public String getStartcommit() { return  startcommit; }
    public String getOutfile() { return outfile; }
    public String getRepo() { return  repo; }

}
