package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

    public List<GetLCategoryRes> getLCategory() {
        String getLCategoryQuery = "select * from large_category";
        return this.jdbcTemplate.query(getLCategoryQuery,
                (rs, rowNum) -> new GetLCategoryRes(
                        rs.getInt("large_category_id"),
                        rs.getString("name")
                ));

    }

    public List<GetMCategoryRes> getMCategory(int LCategory_id) {
        String getMCategoryQuery = "select lc.large_category_id, lc.name, middle_category_id, mc.name\n" +
                "from middle_category mc\n" +
                "    inner join large_category lc on mc.large_category_id = lc.large_category_id\n" +
                "where mc.large_category_id=?;";
        int getMCategoryParam = LCategory_id;
        return this.jdbcTemplate.query(getMCategoryQuery,
                (rs, rowNum) -> new GetMCategoryRes(
                        rs.getInt("large_category_id"),
                        rs.getString("lc.name"),
                        rs.getInt("middle_category_id"),
                        rs.getString("mc.name")
                ), getMCategoryParam);

    }


    public List<GetSCategoryRes> getSCategory(int MCategory_id) {
        String getSCategoryQuery = "select lc.large_category_id, lc.name, mc.middle_category_id, mc.name, small_category_id, sc.name\n" +
                "from small_category sc\n" +
                "    inner join middle_category mc on sc.middle_category_id = mc.middle_category_id\n" +
                "    inner join large_category lc on mc.large_category_id = lc.large_category_id\n" +
                "where sc.middle_category_id=?;";
        int getSCategoryParam = MCategory_id;
        return this.jdbcTemplate.query(getSCategoryQuery,
                (rs, rowNum) -> new GetSCategoryRes(
                        rs.getInt("large_category_id"),
                        rs.getString("lc.name"),
                        rs.getInt("middle_category_id"),
                        rs.getString("mc.name"),
                        rs.getInt("small_category_id"),
                        rs.getString("sc.name")
                ), getSCategoryParam);

    }
}
