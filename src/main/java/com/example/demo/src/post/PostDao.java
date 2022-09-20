package com.example.demo.src.post;

import com.example.demo.src.post.model.PostPhotoReq;
import com.example.demo.src.post.model.PostPostReq;
import com.example.demo.src.post.model.PostTagReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createPost(PostPostReq postPostReq, int user_id) {
        String creatPostQuery = "Insert into post (user_id, title, price, content, count, is_exchangable, safepay, delivery_fee, pcondition) values (?,?,?,?,?,?,?,?,?)";
        int userIdParam = user_id;
        Object[] createPostParams = new Object[]{userIdParam, postPostReq.getTitle(), postPostReq.getPrice(), postPostReq.getContent(), postPostReq.getCount(), postPostReq.getIs_exchangable(), postPostReq.getSafepay(), postPostReq.getDelivery_fee(), postPostReq.getPcondition()};
        this.jdbcTemplate.update(creatPostQuery, createPostParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int addPhoto(PostPhotoReq postPhotoReq) {
        String addPhotoQuery = "Insert into post_photos (post_id, image_path) values (?,?)";
        Object[] addPhotoParams = new Object[]{postPhotoReq.getPost_id(), postPhotoReq.getImage_path()};
        this.jdbcTemplate.update(addPhotoQuery, addPhotoParams);

        String postPhotosQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(postPhotosQuery, int.class);

    }


    public int addTag(PostTagReq postTagReq) {
        String addTagQuery = "Insert into post_tags (post_id, name) values (?,?)";
        Object[] addTagParams = new Object[]{postTagReq.getPost_id(), postTagReq.getName()};
        this.jdbcTemplate.update(addTagQuery, addTagParams);

        String postTagsQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(postTagsQuery, int.class);

    }


}
