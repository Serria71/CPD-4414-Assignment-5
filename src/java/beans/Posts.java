/*
 * Copyright 2016 Len Payne <len.payne@lambtoncollege.ca>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package beans;

import javax.faces.bean.ApplicationScoped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Len Payne <len.payne@lambtoncollege.ca>
 */
@ManagedBean
@ApplicationScoped
public class Posts {

    private List<Post> posts;
    private Post currentPost;

    public Posts() {
        currentPost = new Post(-1, -1, "", null, "");
        getPostsFromDB();
    }

    private void getPostsFromDB() {
        try (Connection conn = DBUtils.getConnection()) {
            posts = new ArrayList<>();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM posts");
            while (rs.next()) {
                Post p = new Post(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_time"),
                        rs.getString("contents")
                );
                posts.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Posts.class.getName()).log(Level.SEVERE, null, ex);
            // This Fails Silently -- Sets Post List as Empty
            posts = new ArrayList<>();
        }
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Post getCurrentPost() {
        return currentPost;
    }

    public Post getPostById(int id) {
        for (Post p : posts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public Post getPostByTitle(String title) {
        for (Post p : posts) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }
        return null;
    }

    public String viewPost(Post post) {
        currentPost = post;
        return "viewPost";
    }

    public String addPost() {
        currentPost = new Post(-1, -1, "", null, "");
        return "editPost";
    }

    public String editPost() {
        return "editPost";
    }

    public String cancelPost() {
        int id = currentPost.getId();
        getPostsFromDB();
        currentPost = getPostById(id);
        return "viewPost";
    }

    public String savePost(User user) {
        try (Connection conn = DBUtils.getConnection()) {
            // If there's a current post, update rather than insert
            if (currentPost.getId() >= 0) {
                String sql = "UPDATE posts SET title = ?, contents = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, currentPost.getTitle());
                pstmt.setString(2, currentPost.getContents());
                pstmt.setInt(3, currentPost.getId());
                pstmt.executeUpdate();
            } else {
                String sql = "INSERT INTO posts (user_id, title, created_time, contents) VALUES (?,?,NOW(),?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, user.getId());
                pstmt.setString(2, currentPost.getTitle());
                pstmt.setString(3, currentPost.getContents());
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Posts.class.getName()).log(Level.SEVERE, null, ex);
        }
        getPostsFromDB();
        // Update the currentPost so that its details appear after navigation
        currentPost = getPostByTitle(currentPost.getTitle());
        return "viewPost";
    }
    
    public String deletePost(){
        try (Connection conn = DBUtils.getConnection()) {
            String sql = "DELETE FROM posts WHERE title = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentPost.getTitle());
            pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(Posts.class.getName()).log(Level.SEVERE, null, ex);
        }
        getPostsFromDB();
        return "index";
    }
}
