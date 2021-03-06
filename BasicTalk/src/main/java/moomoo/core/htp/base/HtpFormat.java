package moomoo.core.htp.base;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @class public class HTP
 * @brief Hyeonseong Text Protocol Format
 * @author hyeonseong Lim
 */
public class HtpFormat {
    // HEADER
    private String type;
    private String fromIp;
    private int fromPort;
    private String toIp;
    private int toPort;
    private int transaction;
    private int length;

    private ConcurrentHashMap<String, String> body = new ConcurrentHashMap<>();

    public HtpFormat() {
        // nothing
    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getFromIp() {return fromIp;}
    public void setFromIp(String fromIp) {this.fromIp = fromIp;}

    public int getFromPort() {return fromPort;}
    public void setFromPort(int fromPort) {this.fromPort = fromPort;}

    public String getToIp() {return toIp;}
    public void setToIp(String toIp) {this.toIp = toIp;}

    public int getToPort() {return toPort;}
    public void setToPort(int toPort) {this.toPort = toPort;}


    public int getTransaction() {return transaction;}
    public void setTransaction(int transaction) {this.transaction = transaction;}

    public int getLength() {return length;}
    public void setLength(int length) {this.length = length;}

    public ConcurrentHashMap<String, String> getBody() {return body;}

    public void addBody(String key, String value) {this.body.put(key, value);}

    public String getHeaderString() {
        StringBuilder stringHeader = new StringBuilder();

        stringHeader.append(HtpKey.PROTOCOL + " " + type + "\n");
        stringHeader.append(HtpKey.FROM + " " + fromIp + ":" + fromPort + "\n");
        stringHeader.append(HtpKey.TO + " " + toIp + ":" + toPort + "\n");
        stringHeader.append(HtpKey.TRANSACTION + " " + type + " " + transaction + "\n");
        stringHeader.append(HtpKey.LENGTH + " " + getBodyString().length() + "\n");

        return stringHeader.toString();
    }

    public String getBodyString() {
        StringBuilder stringBody = new StringBuilder();
        body.forEach((key, value) -> stringBody.append(key+"="+value+"\n"));

        return stringBody.toString();
    }


    @Override
    public String toString() {
        return getHeaderString() + "\n" + getBodyString();
    }
}