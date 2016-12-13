package kael.jekyll.gitbook.service;

/**
 * Created by kael on 2016/12/13.
 */
public interface GitClient {
    void pull(String repository, String branch);
}
