package kael.jekyll.gitbook.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kael on 2016/12/13.
 */
public class DefaultGitClient implements GitClient {

    protected final String gitBash;
    protected final String localRepository;
    public DefaultGitClient(String gitBash, String localRepository) {
        this.gitBash = gitBash;
        this.localRepository = localRepository;
    }

    @Override
    public void pull(String repository, String branch) {
        ProcessBuilder pb = new ProcessBuilder(gitBash,"pull",repository,branch);
        pb.directory(new File(localRepository));
        try {
            System.out.println("start pull:GIT_DIR=" + localRepository +" "+ gitBash + " pull " + " " + repository + " " + branch);
            Process process = pb.start();
            print(process);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void print(Process process) throws IOException {
        InputStream is = process.getInputStream();
        int i = is.read();
        do{
            System.out.print((char)i);
            i = is.read();
            if(i == -1){
                break;
            }
        }while (true);
    }
}
