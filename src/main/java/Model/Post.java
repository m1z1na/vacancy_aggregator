package Model;

import java.time.LocalDateTime;

public class Post {

    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;


    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (!title.equals(post.title)) return false;
        if (!link.equals(post.link)) return false;
        return description.equals(post.description);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
