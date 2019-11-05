package com.zime;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class HashDemo {
    public static void main(String[] args) {
        Jedis jedis = null;
        try {
            jedis = new Jedis("127.0.0.1", 6379);

            Article article=new Article();
            article.setTitle("hei");
            article.setAuthor("xxx");
            article.setContent("helloWorld");
            article.setTime("2019");

//            addArticle(article,jedis);
            delArticle(3,jedis);
//            UpArticle(1,"title","aabbcc",jedis);
            selArticle(1,2,jedis);
        } catch (Exception e) {
//            Logger.Error(e.getMessage(),e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static void addArticle(Article article,Jedis jedis){
        Map<String,String> articleProperties = new HashMap<String, String>();
        articleProperties.put("title",article.getTitle());
        articleProperties.put("content",article.getContent());
        articleProperties.put("author",article.getAuthor());
        articleProperties.put("time",article.getTime());

        jedis.hmset("Article:" + jedis.incr("postID"),articleProperties);
        /**存入标题.**/
        jedis.rpush("list:article","Article:" + jedis.incr("postID"));
    }

    public Article getArticle(int postID,Jedis jedis) {
        Map<String, String> properties = jedis.hgetAll("Article:" + postID);
        Article article = new Article();
        article.setTitle(properties.get("title"));
        article.setContent(properties.get("content"));
        article.setAuthor(properties.get("author"));
        article.setTime(properties.get("time"));
        return article;
    }

    public static void delArticle(int postID,Jedis jedis){
        jedis.hdel("Article:" + postID,"title");
        jedis.hdel("Article:" + postID,"content");
        jedis.hdel("Article:" + postID,"author");
        jedis.hdel("Article:" + postID,"time");
        /**删除标题.**/
        jedis.lrem("list:article",1,"Article:" + postID);
    }

    public static void delArticleV(int postID,String key,Jedis jedis){
        jedis.hdel("Article:" + jedis.get("postID"),key);
    }

    public static void UpArticle(int postID,String key,String val,Jedis jedis){
        Map<String, String> properties = jedis.hgetAll("Article:" + postID);
        properties.put(key,val);
        jedis.hmset("Article:" + postID,properties);
    }

    public static void selArticle(int page,int size,Jedis jedis){
        /**分页.**/
        jedis.lrange("list:article",(page-1)*size,page*size-1);
        System.out.println(jedis.lrange("list:article",(page-1)*size,page*size-1));
    }
}
