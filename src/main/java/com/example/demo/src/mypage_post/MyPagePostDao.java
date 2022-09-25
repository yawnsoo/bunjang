package com.example.demo.src.mypage_post;


import com.example.demo.src.mypage_post.model.GetMyPostsRes;
import com.example.demo.src.post.model.GetPostRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Repository
public class MyPagePostDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetMyPostsRes> getMyPosts(int userId) {
        String getMyPostsQuery = "select p.post_id, price, title, content, safepay, image_path\n" +
                "from post p\n" +
                "    left join post_photos pp on p.post_id = pp.post_id\n" +
                "where user_id = ?\n" +
                "group by p.post_id";
        int getMyPostsParam = userId;
        return this.jdbcTemplate.query(getMyPostsQuery,
                (rs, rowNum) -> new GetMyPostsRes(
                        rs.getInt("post_id"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("safepay"),
                        rs.getString("image_path")
                ), getMyPostsParam);
    }


    public List<GetMyPostsRes> getMyPostsWStatus(int userId, Integer status) {
        String getMyPostsQuery = "select p.post_id, price, title, content, safepay, image_path\n" +
                "from post p\n" +
                "    left join post_photos pp on p.post_id = pp.post_id\n" +
                "where user_id = ? and p.status = ?\n" +
                "group by p.post_id";
        Object[] getMyPostsParam = new Object[]{userId, status};
        return this.jdbcTemplate.query(getMyPostsQuery,
                (rs, rowNum) -> new GetMyPostsRes(
                        rs.getInt("post_id"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("safepay"),
                        rs.getString("image_path")
                ), getMyPostsParam);
    }

    public List<GetMyPostsRes> getMyPostsWSafepay(int userId, Integer safepay) {
        String getMyPostsQuery = "select p.post_id, price, title, content, safepay, image_path\n" +
                "from post p\n" +
                "    left join post_photos pp on p.post_id = pp.post_id\n" +
                "where user_id = ? and p.safepay = ?\n" +
                "group by p.post_id";
        Object[] getMyPostsParam = new Object[]{userId, safepay};
        return this.jdbcTemplate.query(getMyPostsQuery,
                (rs, rowNum) -> new GetMyPostsRes(
                        rs.getInt("post_id"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getInt("safepay"),
                        rs.getString("image_path")
                ), getMyPostsParam);
    }


}
