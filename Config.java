package Server;

public class Config {
    private static Config INSTANCE;

    public static Config getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new Config();
        return INSTANCE;
    }

    private String login;

    private int session;

    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }
}
