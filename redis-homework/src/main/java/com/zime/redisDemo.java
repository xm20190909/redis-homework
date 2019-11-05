package com.zime;

import redis.clients.jedis.Jedis;
import com.alibaba.fastjson.JSON;

public class JedisDemo {
    public static void main(String[] args){
        Jedis jedis = null;
        try{
            jedis = new Jedis("127.0.0.1",6379);
            jedis.get("hello");
            System.out.println(jedis.get("name"));

            Article article=new Article();
            article.setTitle("hi");
            article.setAuthor("xx");
            article.setContent("helloWorld");
            article.setTime("2019");
//            addArticle(article,jedis);
//            DelArticle("haha120191023",jedis);
            UpTitle(article,"hi22019",jedis);
        }
        catch(Exception e){
//            Logger.Error(e.getMessage(),e);
        }
        finally{
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public static void addArticle(Article article,Jedis jedis){
        long articleId = jedis.incr("post");
        String articlePost = JSON.toJSONString(article);
        String posts = article.getTitle()+articleId+article.getTime();
        jedis.set(posts,articlePost);
    }

    public static void DelArticle(String articleName,Jedis jedis){
        jedis.del(articleName);
    }

    public static void UpTitle(Article article,String articlName,Jedis jedis){
        article.setTitle("aaa");
        String article1 = JSON.toJSONString(article);
        jedis.set(articlName,article1);
    }

    public static void getArticle (String articleName,Jedis jedis){
//        Article article =
    }
}
