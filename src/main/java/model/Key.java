package model;

import java.io.InputStream;
import java.util.Properties;

public class Key {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String emailAddress;
    private String emailPassword;

    // コンストラクタでプロパティをロード
    public Key() {
        Properties prop = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new RuntimeException("config.properties が見つかりません");
            }
            prop.load(in);

            this.clientId = prop.getProperty("CLIENT_ID");
            this.clientSecret = prop.getProperty("CLIENT_SECRET");
            this.redirectUri = prop.getProperty("REDIRECT_URI");
            this.emailAddress = prop.getProperty("EMAIL_ADDRESS");
            this.emailPassword = prop.getProperty("EMAIL_PASSWORD");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("config.properties の読み込みに失敗しました", e);
        }
    }

    // Getter
    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
    
    public String getEmailAddress() {
    	return emailAddress;
    }
    
    public String getEemailPassword() {
    	return emailPassword;
    }

    @Override
    public String toString() {
        return "Config{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", redirectUri='" + redirectUri + '\'' +
                '}';
    }
}

