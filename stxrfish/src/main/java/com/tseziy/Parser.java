package com.tseziy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {
    String home = System.getProperty("user.home");
    String saveDir = home + "/.stXrfish";
    String saveDirWindows = home + "\\.stXrfish";
    String fileName = "data.json";

    public Parser() {}

    @SuppressWarnings("deprecation")
    public void parseHTTP() {
        String jsonURL = "https://proxylist.geonode.com/api/proxy-list?protocols=http&limit=500&page=1&sort_by=lastChecked&sort_type=desc";

        try {
            URL proxies = new URL(jsonURL);
            URLConnection connection = proxies.openConnection();
            InputStream is = connection.getInputStream();
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File output = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(output);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            is.close();
            fos.close();

            String jsonString = new String(Files.readAllBytes(Paths.get(saveDir + "/" + fileName)), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonData = json.getJSONArray("data");

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject proxy = jsonData.getJSONObject(i);

                String ip = proxy.getString("ip");
                String port = proxy.getString("port");

                if(tryToConnectHTTP(ip, port)) {
                    System.out.println(ip + ":" + port + " good proxy.");
                    connectHTTP(ip, port);
                    if (output.exists()) {
                        boolean deleted = output.delete();
                        if (deleted) {
                            System.out.println("temporary data successfully cleared.");
                        }
                    }
                    break;
                } else {
                    System.out.println(ip + ":" + port + " bad proxy.");
                }
            }

            if (output.exists()) {
                boolean deleted = output.delete();
                if (deleted) {
                    System.out.println("temporary data successfully cleared.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void parseHTTPWindows() {
        String jsonURL = "https://proxylist.geonode.com/api/proxy-list?protocols=http&limit=500&page=1&sort_by=lastChecked&sort_type=desc";

        try {
            URL proxies = new URL(jsonURL);
            URLConnection connection = proxies.openConnection();
            InputStream is = connection.getInputStream();
            File dir = new File(saveDirWindows);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File output = new File(dir, fileName);
            FileOutputStream fos = new FileOutputStream(output);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            is.close();
            fos.close();

            String jsonString = new String(Files.readAllBytes(Paths.get(saveDirWindows, fileName)), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(jsonString);
            JSONArray jsonData = json.getJSONArray("data");

            for (int i = 0; i < jsonData.length(); i++) {
                JSONObject proxy = jsonData.getJSONObject(i);

                String ip = proxy.getString("ip");
                String port = proxy.getString("port");

                if(tryToConnectHTTP(ip, port)) {
                    System.out.println(ip + ":" + port + " good proxy.");
                    connectHTTP(ip, port);
                    if (output.exists()) {
                        boolean deleted = output.delete();
                        if (deleted) {
                            System.out.println("temporary data successfully cleared.");
                        }
                    }
                    break;
                } else {
                    System.out.println(ip + ":" + port + " bad proxy.");
                }
            }

            if (output.exists()) {
                boolean deleted = output.delete();
                if (deleted) {
                    System.out.println("temporary data successfully cleared.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private boolean tryToConnectHTTP(String ip, String port) {
        try {
            URL testURL = new URL("http://example.com");
            
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, Integer.parseInt(port)));
            HttpURLConnection connection = (HttpURLConnection) testURL.openConnection(proxy);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("HEAD");
                
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void connectHTTP(String ip, String port) {
        String path = "/etc/environment";
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            List<String> newLines = new ArrayList<>();

            boolean foundHttp = false;
            boolean foundHttps = false;
            boolean foundFtp = false;

            for (String line : lines) {
                if (line.startsWith("http_proxy=")) {
                    newLines.add("http_proxy=\"http://" + ip + ":" + port + "\"");
                    foundHttp = true;
                } else if (line.startsWith("https_proxy=")) {
                    newLines.add("https_proxy=\"https://" + ip + ":" + port + "\"");
                    foundHttps = true;
                } else if (line.startsWith("ftp_proxy=")) {
                    newLines.add("ftp_proxy=\"ftp://" + ip + ":" + port + "\"");
                    foundFtp = true;
                } else {
                    newLines.add(line);
                }
            }

            if (!foundHttp) {
                newLines.add("http_proxy=\"http://" + ip + ":" + port + "\"");
            }
            if (!foundHttps) {
                newLines.add("https_proxy=\"https://" + ip + ":" + port + "\"");
            }
            if (!foundFtp) {
                newLines.add("ftp_proxy=\"ftp://" + ip + ":" + port + "\"");
            }

            Files.write(Paths.get(path), newLines);
            System.out.println(ip + ":" + port + " successfully connected to proxy.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void connectHTTPWindows(String ip, String port) {
        try {
            String command = "netsh winhttp set proxy " + ip + ":" + port;
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println(ip + ":" + port + " successfully connected to proxy.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectHTTP() {
        String path = "/etc/environment";
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            List<String> newLines = new ArrayList<>();

            for (String line : lines) {
                if (line.startsWith("http_proxy=") || line.startsWith("https_proxy=") || line.startsWith("ftp_proxy=")) {
                    continue;
                }
                newLines.add(line);
            }

            Files.write(Paths.get(path), newLines);
            System.out.println("succesfully disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void disconnectHTTPWindows() {
        try {
            String command = "netsh winhttp reset proxy";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("succesfully disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
