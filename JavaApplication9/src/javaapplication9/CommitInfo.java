package javaapplication9;

public class CommitInfo {
    private String author;
    private int commits = 1;
    private int insertions = 0;
    private int deletions = 0;
    private int files = 0;
    
    public void addCommit(CommitInfo commitInfo){
        commits++;
        insertions += commitInfo.insertions;
        deletions += commitInfo.deletions;
        files += commitInfo.files;
    }

    public String getAuthor() {
        return author;
    }

    public int getFiles() {
        return files;
    }

    public void setFiles(int files) {
        this.files = files;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

    public int getInsertions() {
        return insertions;
    }

    public void setInsertions(int insertions) {
        this.insertions = insertions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }
}
