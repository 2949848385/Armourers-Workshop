package riskyken.armourersWorkshop.common.library.global.auth;

import com.google.gson.JsonObject;

import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.common.library.global.permission.PermissionSystem.Action;
import riskyken.armourersWorkshop.common.library.global.permission.PermissionSystem.PermissionGroup;

public class PlushieSession {
    
    private PermissionGroup permissionGroup;
    private boolean isAuth;
    
    private int server_id;
    private String mc_id;
    private String mc_name;
    private String accessToken;
    private long accessTokenReceivedTime;
    private int accessTokenExpiryTime;
    
    public PlushieSession() {
        this.permissionGroup = ArmourersWorkshop.getProxy().getPermissionSystem().groupNoLogin;
    }
    
    public boolean authenticate(JsonObject jsonObject) {
        if (jsonObject != null) {
            if (jsonObject.has("valid")) {
                if (jsonObject.get("valid").getAsBoolean()) {
                    server_id = jsonObject.get("server_id").getAsInt();
                    mc_id = jsonObject.get("mc_id").getAsString();
                    mc_name = jsonObject.get("mc_name").getAsString();
                    accessToken = jsonObject.get("accessToken").getAsString();
                    accessTokenReceivedTime = System.currentTimeMillis();
                    accessTokenExpiryTime = jsonObject.get("expiryTime").getAsInt();
                    isAuth = true;
                    return true;
 
                }
            }
        }
        isAuth = false;
        return false;
    }
    
    public int getServer_id() {
        return server_id;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public boolean isAuthenticated() {
        if (isAuth) {
            if (accessTokenReceivedTime + accessTokenExpiryTime > System.currentTimeMillis()) {
                return true;
            }
        }
        return false;
    }
    
    public void setAccessToken(String accessToken, int accessTokenExpiryTime) {
        this.accessToken = accessToken;
        accessTokenReceivedTime = System.currentTimeMillis();
    }
    
    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }
    
    public boolean hasPermission(Action action) {
        return permissionGroup.havePermission(action);
    }
}
