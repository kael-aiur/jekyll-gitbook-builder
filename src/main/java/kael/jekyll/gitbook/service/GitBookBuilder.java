package kael.jekyll.gitbook.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kael on 2016/12/13.
 */
public class GitBookBuilder extends AbstractBuilder {

    private final String command;
    private final String source;
    private final String target;
    private final String repository;
    private final String branch;
    private final GitClient git;

    public GitBookBuilder(String command, String source, String target, String repository, String branch,
                          GitClient git) {
        this.command = command;
        this.source = source;
        this.target = target;
        this.repository = repository;
        this.branch = branch;
        this.git = git;
    }

    @Override
    protected ProcessBuilder getCommand() {
        ProcessBuilder command = new ProcessBuilder(this.command,"build",source,target);
        command.command();
        return command;

    }
    @Override
    protected void beforeBuild(ProcessBuilder command) {
        git.pull(repository,branch);
    }
}
