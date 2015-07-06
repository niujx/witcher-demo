package com.crock.proxy;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * Created by yanshi on 15-6-24.
 */
public class Proxy implements Comparable, Serializable, Cloneable {

    private int id;
    private String host;
    private int port;
    private int nice;
    private long ms;
    private long lastUseTime;
    private double useCount;
    private double errorCount;
    private boolean isTemp;


    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getMs() {
        return ms;
    }

    public void setMs(long ms) {
        this.ms = ms;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(long lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public int getNice() {
        return nice;
    }

    public boolean isTemp() {
        return isTemp;
    }


    public void setNice(int nice) {
        this.nice = nice;
    }

    public void addCount() {
        ++useCount;
    }

    public void addErrorCount() {
        ++errorCount;
    }

    public double getUseCount() {
        return useCount;
    }

    public double getErrorCount() {
        return errorCount;
    }

    public double getHealth() {
        return 1 - errorCount / useCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proxy proxy = (Proxy) o;
        return Objects.equal(port, proxy.port) &&
                Objects.equal(host, proxy.host);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(host, port);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("host", host)
                .add("port", port)
                .add("nice", nice)
                .add("ms", ms)
                .add("lastUseTime", lastUseTime)
                .add("isTemp", isTemp)
                .add("useCount", useCount)
                .add("errorCount", errorCount)
                .toString();
    }

    @Override
    public int compareTo(Object o) {
        return Proxy.class.cast(o).getNice() - nice;
    }

    @Override
    protected Proxy clone() {
        try {
            Proxy p = (Proxy) super.clone();
            p.host = new String(this.host);
            p.isTemp = true;
            return p;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
