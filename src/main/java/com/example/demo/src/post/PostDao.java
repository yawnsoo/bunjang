package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createPost(PostPostReq postPostReq, int user_id) {
        String creatPostQuery = "Insert into post (user_id, title, region, large_category_id, middle_category_id, small_category_id, price, content, count, is_exchangable, safepay, delivery_fee, pcondition) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int userIdParam = user_id;
        Object[] createPostParams = new Object[]{userIdParam, postPostReq.getTitle(), postPostReq.getRegion(),postPostReq.getCategory_large(), postPostReq.getCategory_middle(), postPostReq.getCategory_small(), postPostReq.getPrice(), postPostReq.getContent(), postPostReq.getCount(), postPostReq.getIs_exchangable(), postPostReq.getSafepay(), postPostReq.getDelivery_fee(), postPostReq.getPcondition()};
        this.jdbcTemplate.update(creatPostQuery, createPostParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<String> addPhoto(List<String> img, int post_id) {
        List PhotoList = new ArrayList<String>();
        for (String s : img) {
            String addPhotoQuery = "Insert into post_photos (post_id, image_path) values (?,?)";
            Object[] addPhotoParams = new Object[]{post_id, s};
            this.jdbcTemplate.update(addPhotoQuery, addPhotoParams);
            PhotoList.add(s);
        }
        return PhotoList;
    }

//    public int addTag(PostTagReq postTagReq, int post_id) {
    public List<String> addTag(PostPostReq postPostReq, int post_id) {
        List<String> tag = postPostReq.getTags();
        List TagList = new ArrayList<String>();
        for (String s : tag) {
            String addTagQuery = "Insert into post_tags (post_id, name) values (?,?)";
            Object[] addTagParams = new Object[]{post_id, s};
            this.jdbcTemplate.update(addTagQuery, addTagParams);
            TagList.add(s);
        }
        return TagList;
    }

    public GetPostDetailRes getPostDetailRes(int post_id) {
        String getPostDetailQuery = "select post_id, post_id, user_id, title, price, content, count,\n" +
                "       large_category_id, middle_category_id, small_category_id,\n" +
                "       is_exchangable, safepay, delivery_fee, pcondition, region,\n" +
                "       case when 24 >= timestampdiff(HOUR, created_at, current_time)\n" +
                "        then concat(timestampdiff(HOUR, created_at, current_time),'시간 전')\n" +
                "        else concat(timestampdiff(DAY, created_at, current_time),'일 전') end created_at\n" +
                "from post where post_id=?;";
        int getPostDetailParam = post_id;
        return this.jdbcTemplate.queryForObject(getPostDetailQuery,
                (rs, rowNum) -> new GetPostDetailRes(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("region"),
                        rs.getString("created_at"),
                        rs.getInt("large_category_id"),
                        rs.getInt("middle_category_id"),
                        rs.getInt("small_category_id"),
                        rs.getInt("price"),
                        rs.getString("content"),
                        rs.getInt("count"),
                        rs.getInt("is_exchangable"),
                        rs.getInt("safepay"),
                        rs.getInt("delivery_fee"),
                        rs.getInt("pcondition")

                ), getPostDetailParam);
    }

    public List<GetPostRes> getPosts() {
        String getPostsQuery = "select p.post_id, price, title, safepay, image_path\n" +
                "from post p\n" +
                "    left join post_photos pp on p.post_id = pp.post_id\n" +
                "group by p.post_id";
        return this.jdbcTemplate.query(getPostsQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("post_id"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getInt("safepay"),
                        rs.getString("image_path")
                ));
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


    public List<GetPostRes> getPostsByLC(Integer LCId) {
        String getPostsByLCQuery = "select p.post_id, price, title, safepay, pp.image_path, p.large_category_id\n" +
                "from post p\n" +
                "    left join post_photos pp on p.post_id = pp.post_id\n" +
                "    left join large_category lc on lc.large_category_id = p.large_category_id\n" +
                "where p.large_category_id = ?\n" +
                "group by p.post_id";
        Integer getPostsByLCParam = LCId;
        return this.jdbcTemplate.query(getPostsByLCQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("post_id"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getInt("safepay"),
                        rs.getString("image_path")
                ), getPostsByLCParam);
    }

    public List<GetPostRes> getPostsByMC(Integer MCId) {
        String getPostsByMCQuery = "select p.post_id, price, title, safepay, pp.image_path, p.middle_category_id\n" +
                "from post p\n" +
                "    left join post_photos pp on p.post_id = pp.post_id\n" +
                "    left join middle_category mc on mc.middle_category_id = p.middle_category_id\n" +
                "where p.middle_category_id=?\n" +
                "group by p.post_id";
        Integer getPostsByMCParam = MCId;
        return this.jdbcTemplate.query(getPostsByMCQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("post_id"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getInt("safepay"),
                        rs.getString("image_path")
                ), getPostsByMCParam);
    }

    public List<GetPostRes> getPostsBySC(Integer SCId) {
        String getPostsBySCQuery = "select p.post_id, price, title, safepay, pp.image_path, p.small_category_id\n" +
                "from post p\n" +
                "    left join post_photos pp on p.post_id = pp.post_id\n" +
                "    left join small_category sc on sc.small_category_id = p.small_category_id\n" +
                "where p.small_category_id=?\n" +
                "group by p.post_id";
        Integer getPostsBySCParam = SCId;
        return this.jdbcTemplate.query(getPostsBySCQuery,
                (rs, rowNum) -> new GetPostRes(
                        rs.getInt("post_id"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getInt("safepay"),
                        rs.getString("image_path")
                ), getPostsBySCParam);
    }
}
