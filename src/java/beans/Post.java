
package beans;

import java.util.Date;

public class Post {

    private int id;
    private int userId;
    private String title;
    private Date createdTime;
    private String contents;

    public Post(int id, int userId, String title, Date createdTime, String contents) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.createdTime = createdTime;
        this.contents = contents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return userId;
    }

    public void setUser_id(int user_id) {
        this.userId = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
