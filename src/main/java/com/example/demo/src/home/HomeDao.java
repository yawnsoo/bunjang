package com.example.demo.src.home;

import com.example.demo.src.home.model.GetBrandsRes;
import com.example.demo.src.home.model.GetHomePostsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HomeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetHomePostsRes> getHomePosts(int userId) {
        String getHomePostsQuery = "select p.post_id, image_path, price, title, safepay, region,\n" +
                "       case when 24 >= timestampdiff(HOUR, p.created_at, current_time)\n" +
                "                then concat(timestampdiff(HOUR, p.created_at, current_time),'시간 전')\n" +
                "                else concat(timestampdiff(DAY, p.created_at, current_time),'일 전') end created_at,\n" +
                "       count(distinct j2.post_id) jjim\n" +
                "from post p\n" +
                "         left join post_photos pp on p.post_id = pp.post_id\n" +
                "         inner join (select middle_category_id\n" +
                "                     from jjim j\n" +
                "                              inner join post on post.post_id = j.post_id and j.user_id = ?\n" +
                "         group by middle_category_id) c on p.middle_category_id = c.middle_category_id\n" +
                "         left join jjim j2 on p.post_id = j2.post_id\n" +
                "where (p.status = 1 or p.status = 3)\n" +
                "group by p.post_id";
        int getHomePostsParam = userId;
        return this.jdbcTemplate.query(getHomePostsQuery,
                (rs, rowNum) -> new GetHomePostsRes(
                        rs.getInt("post_id"),
                        rs.getString("image_path"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getInt("safepay"),
                        rs.getString("region"),
                        rs.getString("created_at"),
                        rs.getInt("jjim")
                ), getHomePostsParam);
    }


    public List<GetBrandsRes> getBrands(int userId) {
        String getBrandsQuery = "select b.brand_id, name_kr, name_eng, bf.status\n" +
                "from brand b \n" +
                "    left outer join (select * from brand_follow where user_id = ?) bf on b.brand_id = bf.brand_id";
        int getBrandsParam = userId;
        return this.jdbcTemplate.query(getBrandsQuery,
                (rs, rowNum) -> new GetBrandsRes(
                        rs.getInt("brand_id"),
                        rs.getString("name_kr"),
                        rs.getString("name_eng"),
                        rs.getInt("status")
                ),getBrandsParam);
    }

    public List<GetBrandsRes> getBrand(int userId) {
        String getBrandQuery = "select b.brand_id, name_kr, name_eng, bf.status\n" +
                "from brand b \n" +
                "    left outer join (select * from brand_follow where user_id = ?) bf on b.brand_id = bf.brand_id\n" +
                "where bf.status = 1";
        int getBrandParam = userId;
        return this.jdbcTemplate.query(getBrandQuery,
                (rs, rowNum) -> new GetBrandsRes(
                        rs.getInt("brand_id"),
                        rs.getString("name_kr"),
                        rs.getString("name_eng"),
                        rs.getInt("status")
                ),getBrandParam);
    }

    public List<GetHomePostsRes> getPosts(String brandKor, String brandEng) {
        String getPostsQuery = "select p.post_id, image_path, price, title, safepay, region,\n" +
                "       case when 24 >= timestampdiff(HOUR, p.created_at, current_time)\n" +
                "                then concat(timestampdiff(HOUR, p.created_at, current_time),'시간 전')\n" +
                "                else concat(timestampdiff(DAY, p.created_at, current_time),'일 전') end created_at,\n" +
                "       count(distinct j2.post_id) jjim\n" +
                "\n" +
                "from post p\n" +
                "         left join post_photos pp on p.post_id = pp.post_id\n" +
                "         left outer join jjim j2 on p.post_id = j2.post_id\n" +
                "         left join post_tags pt on p.post_id = pt.post_id\n" +
                "\n" +
                "where (p.status = 1 or p.status = 3) \n" +
                "and (title LIKE CONCAT('%',?,'%') or title LIKE CONCAT('%',?,'%'))\n" +
                "\n" +
                "group by p.post_id";
        Object[] getPostsParam = new Object[]{brandKor, brandEng};
        return this.jdbcTemplate.query(getPostsQuery,
                (rs, rowNum) -> new GetHomePostsRes(
                        rs.getInt("post_id"),
                        rs.getString("image_path"),
                        rs.getInt("price"),
                        rs.getString("title"),
                        rs.getInt("safepay"),
                        rs.getString("region"),
                        rs.getString("created_at"),
                        rs.getInt("jjim")
                ), getPostsParam);
    }

//    public int checkFollowInfo(int userId, int brandId) {
//        String checkFollowInfoQuery = "select exists(select user_id from brand_follow where user_id =? and brand_id =?)";
//        Object[] checkFollowInfoParam = new Object[]{userId, brandId};
//        return this.jdbcTemplate.queryForObject(checkFollowInfoQuery, int.class, checkFollowInfoParam);
//    }


}
