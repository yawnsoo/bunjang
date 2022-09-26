package com.example.demo.src.jjim;

import com.example.demo.src.jjim.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JjimDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetJjimPostsRes> getJjimPosts(int userId) {
        String getJjimPostsQuery = "select p.post_id, pp.image_path, safepay, title, price, market_name,\n" +
                "       case when 24 >= timestampdiff(HOUR, p.created_at, current_time)\n" +
                "                then concat(timestampdiff(HOUR, p.created_at, current_time),'시간 전')\n" +
                "            else concat(timestampdiff(DAY, p.created_at, current_time),'일 전') end created_at\n" +
                "\n" +
                "from post p\n" +
                "         left join post_photos pp on p.post_id = pp.post_id\n" +
                "         left outer join jjim j2 on p.post_id = j2.post_id\n" +
                "         left join user u on p.user_id = u.user_id\n" +
                "\n" +
                "where (p.status = 1 or p.status = 3) and j2.user_id = ?\n" +
                "group by p.post_id";
        int getJjimPostsParam = userId;
        return this.jdbcTemplate.query(getJjimPostsQuery,
                (rs, rowNum) -> new GetJjimPostsRes(
                        rs.getInt("post_id"),
                        rs.getString("image_path"),
                        rs.getInt("safepay"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("market_name"),
                        rs.getString("created_at")
                ),getJjimPostsParam);

    }


    public List<GetJjimCollectionRes> getJjimCollections(int userId) {
        String getJjimCollectionsQuery = "select jc.jjim_collection_id, image_path, name, count(distinct jjim_id) countProduct\n" +
                "from jjim_collection jc\n" +
                "    left join (select jjim_collection_id, jci.jjim_id, user_id, post_id\n" +
                "    from jjim_collection_inside jci left join jjim j on jci.jjim_id = j.jjim_id) jcij on jcij.jjim_collection_id = jc.jjim_collection_id\n" +
                "    left join post_photos pp on jcij.post_id = pp.post_id\n" +
                "where jc.user_id = ?\n" +
                "group by jc.jjim_collection_id";
        int getJjimCollectionsParam = userId;
        return this.jdbcTemplate.query(getJjimCollectionsQuery,
                (rs, rowNum) -> new GetJjimCollectionRes(
                        rs.getInt("jjim_collection_id"),
                        rs.getString("image_path"),
                        rs.getString("name"),
                        rs.getInt("countProduct")
                ), getJjimCollectionsParam);
    }

    public int modifyJjimPostCollection(PatchPostCollectionReq patchPostCollectionReq) {
        String modifyJjimPostCollectionQuery = "update jjim_collection_inside\n" +
                "set jjim_collection_id =?\n" +
                "where jjim_id = ?;";
        Object[] modifyJjimPostCollectionParam = new Object[]{patchPostCollectionReq.getTo_jjim_collection_id(),patchPostCollectionReq.getJjim_id()};

        return this.jdbcTemplate.update(modifyJjimPostCollectionQuery,modifyJjimPostCollectionParam);
    }

    public String getJjimCollectionName(int jjim_collection_id) {
        String getJjimCollectionNameQuery = "select name from jjim_collection where jjim_collection_id = ?";
        int getJjimCollectionNameParam = jjim_collection_id;

        return this.jdbcTemplate.queryForObject(getJjimCollectionNameQuery, String.class, getJjimCollectionNameParam);

    }

    public List<GetJjimCollectionPostRes> getJjimCollectionPosts(int userId, int jcId) {
        String getJjimCollectionPostsQuery = "select p.post_id, pp.image_path, safepay, title, price, market_name\n" +
                "\n" +
                "from post p\n" +
                "         left join post_photos pp on p.post_id = pp.post_id\n" +
                "         left join (select * from jjim j where user_id = ?) j2 on p.post_id = j2.post_id\n" +
                "         left join jjim_collection_inside jci on j2.jjim_id = jci.jjim_id\n" +
                "         left join user u on p.user_id = u.user_id\n" +
                "\n" +
                "\n" +
                "where (p.status = 1 or p.status = 3) and jjim_collection_id = ?\n" +
                "group by p.post_id";
        Object[] getJjimCollectionPostsParam = new Object[]{userId, jcId};
        return this.jdbcTemplate.query(getJjimCollectionPostsQuery,
                (rs, rowNum) -> new GetJjimCollectionPostRes(
                        rs.getInt("post_id"),
                        rs.getString("image_path"),
                        rs.getInt("safepay"),
                        rs.getString("title"),
                        rs.getInt("price"),
                        rs.getString("market_name")
                ), getJjimCollectionPostsParam);
    }

    public int deleteJjimPost(PatchJjimPostReq patchJjimPostReq){
        String deleteJjimPostQuery = "delete from jjim where post_id = ?";
        Object[] deleteJjimPostParam = new Object[]{patchJjimPostReq.getPost_id()};

        return this.jdbcTemplate.update(deleteJjimPostQuery,deleteJjimPostParam);
    }

    public int createJjimCollection(int userId, PostJjimCollectionReq postJjimCollectionReq) {
        String createJjimCollectionQuery = "insert into jjim_collection (user_id, name) VALUES (?,?)";
        Object[] createJjimCollectionParams = new Object[]{userId, postJjimCollectionReq.getName()};

        this.jdbcTemplate.update(createJjimCollectionQuery, createJjimCollectionParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

}
