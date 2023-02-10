package shop.mtcoding.blog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class HtmlParseTest {

    @Test
    public void jsoup_test() throws Exception{
        Document doc = Jsoup.connect("https://search.naver.com/search.naver?where=view&sm=tab_jum&query=%EB%B6%80%EC%82%B0+%EB%A7%9B%EC%A7%91").get();
        System.out.println(doc.title());
        // #main_pack > section > div > div._list > panel-list > div:nth-child(1) > more-contents > div > ul > li:nth-child(1) > div.total_wrap.api_ani_send > div > a
        Elements elements = doc.select("#main_pack > section > div > div._list > panel-list > div:nth-child(1) > more-contents > div > ul > li:nth-child(1) > div.total_wrap.api_ani_send > div > a");
        elements.stream().forEach((e)-> {
            System.out.println(e.text());
        });
    }

    @Test
    public void jsoup_test2() {
        String html = "<p>1</p><p><img src=\"data:image/png;base64,iVBORw0KG\"><img src=\"data:image/png;base64,iVBORw0KG\"></p>";
        Document doc = Jsoup.parse(html);
        System.out.println(doc);
        Elements els = doc.select("img");
        // System.out.println(els);
        if(els.size() ==0) {
            // 임시 사진 제공해주기
            // 디비 thumbnail -> /images/profile.jfif
        } else {
            Element el = els.get(0);
            String img = el.attr("src");
            System.out.println(img);
            // 디비 thumbnail -> img
        }
    }

    @Test
    public void parse_test1(){
        String html = "<p>1</p><p><img src=\"data:image/png;base64,iVBORw0KG\"></p>";
        String tag = parseEL(html, "img");
        System.out.println(tag);
        String attr = parseAttr(tag, "src");
        System.out.println(attr);
    }

    private String parseEL(String html, String tag){
        String s1 = html.substring(html.indexOf(tag)-1);
        return s1.substring(0, s1.indexOf(">")+1);
    }
    private String parseAttr(String el, String attr){
        String s1 = el.substring(el.indexOf(attr));

        int begin = s1.indexOf("\"");
        int end = s1.indexOf("\"", 2);

        return s1.substring(begin+1, end);
    }

    // @Test
    // public void asd_test() throws Exception {
    //     // given
    //     Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
    //     System.out.println((doc.content()));
    
    //     // when
    
    
    //     // then
    
    // }


}