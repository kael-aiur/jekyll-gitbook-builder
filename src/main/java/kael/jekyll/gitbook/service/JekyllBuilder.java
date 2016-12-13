package kael.jekyll.gitbook.service;

import java.io.File;

/**
 * Created by kael on 2016/12/13.
 */
public class JekyllBuilder extends AbstractBuilder {

    private final String command;
    private final String source;
    private final String target;
    private final String profile;
    private final String repository;
    private final String branch;
    private final GitClient git;

    public JekyllBuilder(String command, String source, String target, String profile, String repository,
                         String branch, GitClient git) {
        this.command = command;
        this.source = source;
        this.target = target;
        this.profile = profile;
        this.repository = repository;
        this.branch = branch;
        this.git = git;
    }

    @Override
    protected ProcessBuilder getCommand() {
        ProcessBuilder command = new ProcessBuilder(this.command,"build", "-s",source,"-d",target);
        command.environment().put("JEKYLL_ENV",profile);
        command.directory(new File(this.source));
        return command;
    }

    @Override
    protected void beforeBuild(ProcessBuilder command) {
        git.pull(repository,branch);
    }
}
