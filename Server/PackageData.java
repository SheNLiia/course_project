package Server;

import Commands.Elem;
import Commands.Response;

import java.io.Serializable;

public class PackageData implements Serializable {

    private Elem elem;
    private String login;
    private String password;
    private String result;
    private Response response;
    private String device;
    private int session;
    private boolean admin;
    private String query;
    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getDevice() {
        return device;
    }

    public Elem getElem() {
        return elem;
    }

    public void setElem(Elem elem) {
        this.elem = elem;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
