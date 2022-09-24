package com.example.demo.src.search;

import com.example.demo.src.search.model.GetPostListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //검색_정확도순
    public List<GetPostListRes> searchAccuracy (String words)
    {
        String search_words = "%"+words+"%";
        String searchAccuracyQuery = "select post_id,title,price,(select image_path from post_photos where post_id = p.post_id LIMIT 1) as images\n" +
                "from post as p where p.status = 1 and p.title LIKE ?";

        return this.jdbcTemplate.query(searchAccuracyQuery, (rs, rowNum) -> new GetPostListRes(
                rs.getInt("post_id"),
                rs.getString("images"),
                rs.getString("title"),
                rs.getInt("price")
        ),search_words);


    }

    //검색_낮은가격순 (판매글 제목에 검색어가 없어도 태그에 검색어 내용이 들어있거나 , 아니면 판매글 제목에 검색어가 들어가있는 판매글조회해서
    // 낮은가격순대로 나열
    public List<GetPostListRes> searchLowPrice (String words)
    {
        String search_words = "%"+words+"%";
        String searchAccuracyQuery = "select post_id,title,price,(select image_path from post_photos where post_id = p.post_id LIMIT 1) as images\n" +
                "from post as p where p.status = 1 and (p.title LIKE ? or post_id in(select post_id from post_tags where name LIKE ?)) ORDER BY price";

        return this.jdbcTemplate.query(searchAccuracyQuery, (rs, rowNum) -> new GetPostListRes(
                rs.getInt("post_id"),
                rs.getString("images"),
                rs.getString("title"),
                rs.getInt("price")
        ),search_words,search_words);


    }

    //검색_높은가격순 (판매글 제목에 검색어가 없어도 태그에 검색어 내용이 들어있거나 , 아니면 판매글 제목에 검색어가 들어가있는 판매글조회해서
    // 높은가격순대로 나열
    public List<GetPostListRes> searchHighPrice (String words)
    {
        String search_words = "%"+words+"%";
        String searchAccuracyQuery = "select post_id,title,price,(select image_path from post_photos where post_id = p.post_id LIMIT 1) as images\n" +
                "from post as p where p.status = 1 and (p.title LIKE ? or post_id in(select post_id from post_tags where name LIKE ?)) ORDER BY price DESC ";

        return this.jdbcTemplate.query(searchAccuracyQuery, (rs, rowNum) -> new GetPostListRes(
                rs.getInt("post_id"),
                rs.getString("images"),
                rs.getString("title"),
                rs.getInt("price")
        ),search_words,search_words);


    }
}
