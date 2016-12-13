package kael.jekyll.gitbook.service;

import kael.jekyll.gitbook.util.Properties;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kael on 2016/12/13.
 */
public class JekyllAndGitBookBuilder implements WebSiteBuilder{

    public static final String GIT_BASH = "git.bash";

    public static final String JEKYLL_BASH = "jekyll.bash";
    public static final String JEKYLL_PROFILE = "jekyll.profile";
    public static final String JEKYLL_REPO = "jekyll.repository";
    public static final String JEKYLL_BRANCH = "jekyll.branch";
    public static final String JEKYLL_GIT_LOCAL_REPO = "jekyll.git.repo";
    public static final String JEKYLL_SOURCE = "jekyll.source";
    public static final String JEKYLL_TARGET = "jekyll.target";
    public static final String JEKYLL_PROJECT_NAME = "jekyll.project.name";


    public static final String GITBOOK_BASH = "gitbook.base";
    public static final String GITBOOK_REPO = "gitbook.repository";
    public static final String GITBOOK_BRANCH = "gitbook.branch";
    public static final String GITBOOK_GIT_LOCAL_REPO = "gitbook.git.repo";
    public static final String GITBOOK_SOURCE = "gitbook.source";
    public static final String GITBOOK_TARGET = "gitbook.target";
    public static final String GITBOOK_PROJECT_NAME = "gitbook.project.name";

    private final ExecutorService threadPool;
    private final Properties properties;
    private final StaticBuilder jekyllBuilder;
    private final StaticBuilder gitBookBuilder;

    public JekyllAndGitBookBuilder(Properties properties) {
        this.threadPool = Executors.newFixedThreadPool(5);
        this.properties = properties;
        GitClient jekyllClient = new DefaultGitClient(properties.get(GIT_BASH),properties.get(JEKYLL_GIT_LOCAL_REPO));
        GitClient gitBookClient = new DefaultGitClient(properties.get(GIT_BASH),properties.get(GITBOOK_GIT_LOCAL_REPO));
        jekyllBuilder = new JekyllBuilder(
                properties.get(JEKYLL_BASH),
                properties.get(JEKYLL_SOURCE),
                properties.get(JEKYLL_TARGET),
                properties.get(JEKYLL_PROFILE),
                properties.get(JEKYLL_REPO),
                properties.get(JEKYLL_BRANCH),
                jekyllClient);
        gitBookBuilder = new GitBookBuilder(
                properties.get(GITBOOK_BASH),
                properties.get(GITBOOK_SOURCE),
                properties.get(GITBOOK_TARGET),
                properties.get(GITBOOK_REPO),
                properties.get(GITBOOK_BRANCH),
                gitBookClient);
    }

    @Override
    public void build(Map<String, Object> params) {
        threadPool.execute(new builder(params));
    }

    private class builder implements Runnable{

        private final Map<String, Object> params;

        public builder(Map<String, Object> params) {
            this.params = params;
        }

        @Override
        public void run() {
            String name = Objects.toString(((Map<String, Object>) params.get("project")).get("name"));
            name = name.replaceAll("\"","");
            if(properties.get(JEKYLL_PROJECT_NAME).equals(name)){
                System.out.println("build jekyll");
                jekyllBuilder.build();
            }
            if(properties.get(GITBOOK_PROJECT_NAME).equals(name)){
                System.out.println("build gitbook");
                gitBookBuilder.build();
            }
        }
    }
}
