package app.model;




public class JwtResponse {
    private String accessToken;
    private String refreshToken;


    public JwtResponse(String accessToken,String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken(){
        return refreshToken;
    }



}
