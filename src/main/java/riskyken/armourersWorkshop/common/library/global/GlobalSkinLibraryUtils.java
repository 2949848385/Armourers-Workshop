package riskyken.armourersWorkshop.common.library.global;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;

import com.google.gson.JsonObject;

import riskyken.armourersWorkshop.utils.ModLogger;

public final class GlobalSkinLibraryUtils {
    
    private static final String USER_INFO_URL = "http://plushie.moe/armourers_workshop/user-info.php";
    private static final Executor JSON_DOWNLOAD_EXECUTOR = Executors.newFixedThreadPool(1);
    private static final HashMap<Integer, PlushieUser> USERS = new HashMap<Integer, PlushieUser>();
    private static final HashSet<Integer> DOWNLOADED_USERS = new HashSet<Integer>();
    
    private GlobalSkinLibraryUtils() {}
    
    public static PlushieUser getUserInfo(int userId) {
        synchronized (USERS) {
            if (USERS.containsKey(userId)) {
                return USERS.get(userId);
            }
        }
        if (!DOWNLOADED_USERS.contains(userId)) {
            DOWNLOADED_USERS.add(userId);
            JSON_DOWNLOAD_EXECUTOR.execute(new DownloadUserCallable(userId));
        }
        return null;
    }
    
    public static class DownloadUserCallable implements Runnable {

        private final int userId;
        
        public DownloadUserCallable(int userId) {
            this.userId = userId;
        }

        @Override
        public void run() {
            
            JsonObject json = DownloadUtils.downloadJsonObject(USER_INFO_URL + "?userId=" + userId);
            PlushieUser plushieUser = PlushieUser.readPlushieUser(json);
            if (plushieUser != null) {
                synchronized (USERS) {
                    USERS.put(userId, plushieUser);
                }
            } else {
                ModLogger.log(Level.ERROR, "Failed downloading info for user id: " + userId);
            }
        }
    }
    
    public static String authenticatePlayer(String token) {
        ModLogger.log("Authenticate Test Started");
        //token = "badtokentest";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"accessToken\": \"" + token  + "\"");
        sb.append("}");
        ModLogger.log(sb.toString());
        String jsonResult = null;
        
        try {
            jsonResult = performPostRequest(new URL("https://authserver.mojang.com/validate"), sb.toString(), "application/json");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        ModLogger.log(jsonResult);
        ModLogger.log("Authenticate Test Finished");
        return jsonResult;
    }
    
    public static String performPostRequest(URL url, String post, String contentType) throws IOException {
        Validate.notNull(url);
        Validate.notNull(post);
        Validate.notNull(contentType);
        HttpURLConnection connection = createUrlConnection(url);
        byte[] postAsBytes = post.getBytes(Charsets.UTF_8);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", "" + postAsBytes.length);
        connection.setDoOutput(true);
        
        OutputStream outputStream = null;
        try {
            outputStream = connection.getOutputStream();
            IOUtils.write(postAsBytes, outputStream);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            String result = IOUtils.toString(inputStream, Charsets.UTF_8);
            return result;
        } catch (IOException e) {
            IOUtils.closeQuietly(inputStream);
            inputStream = connection.getErrorStream();

            if (inputStream != null) {
                String result = IOUtils.toString(inputStream, Charsets.UTF_8);
                return result;
            } else {
                throw e;
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    private static HttpURLConnection createUrlConnection(URL url) throws IOException {
        Validate.notNull(url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        return connection;
    }
}
