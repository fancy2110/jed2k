package org.jed2k;

import org.jed2k.protocol.NetworkIdentifier;

/**
 * Created by ap197_000 on 04.07.2016.
 * information about peer
 */
public class Peer implements Comparable<Peer> {

    public enum SourceFlag {
        SF_SERVER(0x1),
        SF_INCOMING(0x2),
        SF_DHT(0x4),
        SF_RESUME_DATA(0x8);

        public int value;

        SourceFlag(int s) {
            this.value = s;
        }
    }

    long    lastConnected   = 0;
    long    nextConnection  = 0;
    long    failCount       = 0;
    boolean connectable     = false;
    int source      = 0;
    NetworkIdentifier   endpoint;
    private PeerConnection  connection = null;

    public Peer(NetworkIdentifier ep) {
        endpoint = ep;
    }

    public Peer(NetworkIdentifier ep, boolean conn) {
        endpoint = ep;
        connectable = conn;
    }

    @Override
    public int compareTo(Peer o) {
        return endpoint.compareTo(o.endpoint);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Peer) {
            Peer other = (Peer)o;
            return endpoint.equals(other.endpoint);
        }

        return false;
    }

    public boolean isConnectable() {
        return connectable;
    }

    PeerConnection getConnection() { return connection; }

    void setConnection(final PeerConnection c) {
        connection = c;
    }

    boolean hasConnection() {
        return connection != null;
    }

    @Override
    public String toString() {
        return "peer: conn " + (connection!=null?connection.toString():"null") + (connectable?"connectable":"notconn") + " fail count {" + failCount + "}";
    }
}
