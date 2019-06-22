package javaapplication9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JavaApplication9 {

    private static final String COMMIT_STR = "commit ";
    private static final String AUTHOR_STR = "Author: ";
    private static final String INSERTIONS_STR = " insertions(+)";
    private static final String DELETIONS_STR = " deletions(-)";
    private static final String FILES_CHANGED_STR = " files changed";

    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\Martins\\Documents\\Total Confident\\tudok";

        ArrayList<CommitInfo> authors = new ArrayList<>();
        ArrayList<String> commits = readCommitsFromGitLog(path);
        System.out.println("Read git log");
        for (int i = 0; i < commits.size(); i++) {
            String commit = commits.get(i);
            ArrayList<String> commitTxt = loadCommitInfoText(path, commit);
            CommitInfo commitInfo = buildCommitInfo(commitTxt);
            addCommitOnAuthor(authors, commitInfo);

            if (i % 10 == 0) {
                System.out.println(String.format("%.2f", ((float) i * 100) / ((float) commits.size())) + "% commits loaded (" + i + "/" + commits.size() + ")");
            }
        }

        System.out.println("\n\n print csv \n\n");
        System.out.println("Author ; Commits ; Insertions ; Deletions ; Files Changed");
        for (CommitInfo author : authors) {
            System.out.println(author.getAuthor() + ";" + author.getCommits() + ";" + author.getInsertions() + ";" + author.getDeletions() + ";" + author.getFiles());
        }
    }

    private static void addCommitOnAuthor(ArrayList<CommitInfo> authors, CommitInfo commitInfo) {
        boolean added = false;
        for (int i = 0; i < authors.size(); i++) {
            CommitInfo author = authors.get(i);
            if (author.getAuthor().equals(commitInfo.getAuthor())) {
                added = true;
                author.addCommit(commitInfo);
            }
        }
        if (!added) {
            authors.add(commitInfo);
        }
    }

    private static ArrayList<String> readCommitsFromGitLog(String path) throws IOException {
        ArrayList<String> commits = new ArrayList();
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"" + path + "\" && git log");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            if (line.startsWith(COMMIT_STR)) {
                commits.add(line.substring(COMMIT_STR.length()));
            }
        }
        return commits;
    }

    public static CommitInfo buildCommitInfo(ArrayList<String> commitTxt) {
        CommitInfo a = new CommitInfo();
        for (String string : commitTxt) {
            if (string.startsWith(AUTHOR_STR)) {
                a.setAuthor(string.substring(AUTHOR_STR.length()));
                //System.out.println(a.getAuthor());
                //System.out.println(string);
            }
        }
        String[] lastLine = commitTxt.get(commitTxt.size() - 1).split(",");
        //System.out.println(commitTxt.get(commitTxt.size() - 1));
        for (String parameter : lastLine) {
            parameter = parameter.trim();
            if (parameter.endsWith(INSERTIONS_STR)) {
                parameter = parameter.replace(INSERTIONS_STR, "");
                a.setInsertions(Integer.valueOf(parameter));
                //System.out.println("ins " + a.getInsertions());
            } else if (parameter.endsWith(DELETIONS_STR)) {
                parameter = parameter.replace(DELETIONS_STR, "");
                a.setDeletions(Integer.valueOf(parameter));
                //System.out.println("del " + a.getDeletions());
            } else if (parameter.endsWith(FILES_CHANGED_STR)) {
                parameter = parameter.replace(FILES_CHANGED_STR, "");
                a.setFiles(Integer.valueOf(parameter));
                //System.out.println("files " + a.getFiles());
            }
        }
        return a;
    }

    public static ArrayList<String> loadCommitInfoText(String path, String commitId) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"" + path + "\" && git show " + commitId + " --stat");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        ArrayList<String> txt = new ArrayList();
        while (true) {
            line = r.readLine();
            if (line == null) {
                break;
            }
            txt.add(line);
        }
        return txt;
    }
}
