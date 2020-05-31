package resume.microservice.com.ResumeService.util;

import com.squareup.okhttp.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class RequestUtil {

    private static  final String USER_AGENT = "Mozilla/5.0";

    public static Response sendGet(String url) throws Exception {

        Response response = null;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //.url("http://localhost:8080/api/profile/23")
                    .url(url)
                    .method("GET", null)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Content-Type", "text/plain")
                    .build();

            response = client.newCall(request).execute();

            // System.out.println( "Get : " + response.body().string() );

            System.out.println( response.code());

        } catch (IOException e) {
            System.out.println( response.code());
            System.out.println( "Get : " + response.body().string() );
            e.printStackTrace();
        }

        return response;

    }




    public static Response sendPost(String url, String Json) throws Exception {

        Response response = null;

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json,text/plain");
            RequestBody body = RequestBody.create(mediaType, Json);
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Content-Type", "text/plain")
                    .build();

            response = client.newCall(request).execute();

            System.out.println( response.code() );


        } catch (IOException e) {
            System.out.println( response.code() );
            System.out.println( "Get : " + response.body().string() );
            e.printStackTrace();
        }
        return response;
    }


    public static String run(String url) throws IOException {

        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setConnectTimeout(1000);
        con.setReadTimeout(1000);

        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "text/html; charset=windows-1251");

        con.setRequestProperty("Content-Language", "ru");

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("windows-1251")));

        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }


}
