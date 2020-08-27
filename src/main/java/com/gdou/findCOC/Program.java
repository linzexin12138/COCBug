package com.gdou.findCOC;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Program {

    private static CloseableHttpClient httpClient;

    static {
        try {
            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL).loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(10000).setSocketTimeout(10000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLContext(sslContext).setSSLHostnameVerifier((x, y) -> true).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String url = "https://api.clashofstats.com/search/players?q=%E9%BD%90%E5%A4%A9&nameEquality=true&page=";
        List<String> urlList = new ArrayList<>();
        String prefix = "https://api.clashofstats.com/players/";
        List<String> idList = new ArrayList<>();


        for (int i = 0; i <= 12; i++){
            JSONObject json = JSONObject.fromObject(doSearch(url + i));
            JSONArray array = JSONArray.fromObject(json.get("items"));
            for (int j = 0; j < array.size(); j++){
                JSONObject item = array.getJSONObject(j);
                String id = item.get("tag").toString().substring(1);
                urlList.add(prefix + id);
            }
        }

        int count = 0;
        for (String haha : urlList){
            JSONObject json2 = JSONObject.fromObject(doSearch(haha));
            System.out.println("index:" + ++count + "  level:" + json2.get("townHallLevel"));
            if (json2.get("townHallLevel").toString().equals("7")){
                idList.add(haha);
            }
        }
        System.out.println(idList);
    }

    private static void parseHtml(String html) {
        //使用jsoup解析页面
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div.v-chip__content > span");
        System.out.println(elements);
    }

        public static String doSearch(String url) throws IOException {

        //创建HttpGet请求
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求
            response = httpClient.execute(httpGet);

            //判断响应状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //如果为200表示请求成功，获取返回数据
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                //打印数据长度
                return content;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放连接
            if (response == null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpClient.close();
            }
        }
        return "失败";
    }

}
