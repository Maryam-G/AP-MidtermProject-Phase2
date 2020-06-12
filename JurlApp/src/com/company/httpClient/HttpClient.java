package com.company.httpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {

    private HttpURLConnection connection;
    private Map<String, List<String>> responseHeaders;
    private String responseBody;

    public HttpClient(String urlString, String method, HashMap<String, String> header,HashMap<String, String> body){
        try {
            if(!urlString.startsWith("http://")){
                urlString = "http://" + urlString;
            }
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            // request setup

            // set method :
            connection.setRequestMethod(method);

            // set headers of request :
            for(Map.Entry<String, String> entry : header.entrySet()){
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // set body of request for "POST" :
            if(method.equals("POST") || method.equals("PUT") || method.equals("DELETE")){
//                connection.setDoOutput(true);
//                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
//                dataOutputStream.writeChars(bodyString);
//                dataOutputStream.flush();
//                dataOutputStream.close();

                String boundary = System.currentTimeMillis() + "";
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
                formData(body, boundary, bufferedOutputStream);
            }

            // check the response code :
            int status = connection.getResponseCode();

            // response body :
            BufferedReader reader;
            //todo : pak kardan e ezafiat
//            if(status / 100 != 2){
//                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
//            }else{
//                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            }
            if(connection.getErrorStream() != null){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }else{
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String line;
            StringBuffer responseContent = new StringBuffer();
            while((line = reader.readLine()) != null){
                responseContent.append("\n" + line);
            }
            reader.close();

            //response headers :
            responseHeaders = connection.getHeaderFields();

            //response body :
            responseBody = responseContent.toString();

            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public static void formData(HashMap<String, String> body, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (String key : body.keySet()) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (key.contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(body.get(key))).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    // TODO : in code ro avaz karadm...
                    File file = new File(body.get(key));
                    BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    int size = (int) file.length();
                    byte[] bytes = new byte[size];
                    bufferedOutputStream.write(tempBufferedInputStream.read(bytes, 0, bytes.length));
                    bufferedOutputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((body.get(key) + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

}
