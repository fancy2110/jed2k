package org.dkf.jed2k.protocol.kad;

import org.dkf.jed2k.protocol.PacketHeader;
import org.dkf.jed2k.protocol.PacketKey;
import org.dkf.jed2k.protocol.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class PacketCombiner extends org.dkf.jed2k.protocol.PacketCombiner {

    public enum KadUdp {
        KADEMLIA_BOOTSTRAP_REQ      (0x00),
        KADEMLIA_BOOTSTRAP_RES      (0x08),

        KADEMLIA_HELLO_REQ          (0x10),
        KADEMLIA_HELLO_RES          (0x18),

        KADEMLIA_FIREWALLED_REQ     (0x50),
        KADEMLIA_FIREWALLED_RES     (0x58),

        KADEMLIA_CALLBACK_REQ       (0x52),

        KADEMLIA_REQ                (0x20),
        KADEMLIA_RES                (0x28),

        KADEMLIA_PUBLISH_REQ        (0x40),
        KADEMLIA_PUBLISH_RES        (0x48),

        KADEMLIA_SEARCH_REQ         (0x30),
        KADEMLIA_SEARCH_RES         (0x38),

        KADEMLIA_SEARCH_NOTES_REQ   (0x32),
        KADEMLIA_SEARCH_NOTES_RES   (0x3A),

        KADEMLIA_FINDBUDDY_REQ      (0x51),
        KADEMLIA_FINDBUDDY_RES      (0x5A),

        KADEMLIA_PUBLISH_NOTES_REQ  (0x42),
        KADEMLIA_PUBLISH_NOTES_RES  (0x4A),

        KADEMLIA2_BOOTSTRAP_REQ     (0x01),
        KADEMLIA2_BOOTSTRAP_RES     (0x09),

        KADEMLIA2_REQ               (0x21),
        KADEMLIA2_RES               (0x29),

        KADEMLIA2_HELLO_REQ         (0x11),
        KADEMLIA2_HELLO_RES         (0x19),

        KADEMLIA2_HELLO_RES_ACK     (0x22),

        KADEMLIA_FIREWALLED2_REQ    (0x53),

        KADEMLIA2_FIREWALLUDP       (0x62),
        KADEMLIA_FIREWALLED_ACK_RES (0x59),	// (null)

        KADEMLIA2_SEARCH_KEY_REQ    (0x33),
        KADEMLIA2_SEARCH_SOURCE_REQ (0x34),
        KADEMLIA2_SEARCH_NOTES_REQ  (0x35),

        KADEMLIA2_SEARCH_RES        (0x3B),

        KADEMLIA2_PUBLISH_KEY_REQ   (0x43),
        KADEMLIA2_PUBLISH_SOURCE_REQ(0x44),
        KADEMLIA2_PUBLISH_NOTES_REQ (0x45),

        KADEMLIA2_PUBLISH_RES       (0x4B),

        KADEMLIA2_PUBLISH_RES_ACK   (0x4C),

        KADEMLIA2_PING              (0x60),
        KADEMLIA2_PONG              (0x61),

        FIND_VALUE                  (0x02),
        STORE                       (0x04),
        FIND_NODE                   (0x0B);

        public final byte value;
        private KadUdp(int v) {
            value = (byte)v;
        }
    }

    public static final byte OP_KADEMLIAHEADER = (byte)0xE4;
    public static final byte KADEMLIA_VERSION_048a = (byte)0x05;
    public static final byte KADEMLIA_VERSION_049 = (byte)0x08;
    public static final byte KADEMLIA_VERSION =  KADEMLIA_VERSION_048a;

    private KadPacketHeader header = new KadPacketHeader();

    private static final Map<PacketKey, Class<? extends Serializable>> supportedPacketsKad;
    private static final Map<Class<? extends Serializable>, PacketKey> struct2KeyKad;

    private static void addHandler(byte protocol, byte type, Class<? extends Serializable> clazz) {
        PacketKey pk = new PacketKey(protocol, type);
        assert(!supportedPacketsKad.containsKey(pk));
        assert(clazz != null);
        supportedPacketsKad.put(pk, clazz);
        struct2KeyKad.put(clazz, pk);
    }

    private static void addKadHandler(byte type, Class<? extends  Serializable> clazz) {
        addHandler(OP_KADEMLIAHEADER, type, clazz);
    }

    static {
        supportedPacketsKad = new TreeMap<PacketKey, Class<? extends Serializable>>();
        struct2KeyKad = new HashMap<Class<? extends Serializable>, PacketKey>();


        addKadHandler(KadUdp.KADEMLIA2_PING.value, Kad2Ping.class);
        addKadHandler(KadUdp.KADEMLIA2_PONG.value, Kad2Pong.class);

        addKadHandler(KadUdp.KADEMLIA2_REQ.value, Kad2Req.class);
        addKadHandler(KadUdp.KADEMLIA2_RES.value, Kad2Res.class);

        //addKadHandler(KadUdp.KADEMLIA_REQ.value, Kad2Req.class);
        //addKadHandler(KadUdp.KADEMLIA_RES.value, Kad2Res.class);

        addKadHandler(KadUdp.KADEMLIA2_HELLO_REQ.value, Kad2HelloReq.class);
        addKadHandler(KadUdp.KADEMLIA2_HELLO_RES.value, Kad2HelloRes.class);

        addKadHandler(KadUdp.KADEMLIA2_SEARCH_SOURCE_REQ.value, Kad2SearchSourcesReq.class);
        addKadHandler(KadUdp.KADEMLIA2_SEARCH_KEY_REQ.value, Kad2SearchKeysReq.class);
        addKadHandler(KadUdp.KADEMLIA2_SEARCH_NOTES_REQ.value, Kad2SearchNotesReq.class);
        addKadHandler(KadUdp.KADEMLIA2_SEARCH_RES.value, Kad2SearchRes.class);

        addKadHandler(KadUdp.KADEMLIA2_BOOTSTRAP_REQ.value, Kad2BootstrapReq.class);
        addKadHandler(KadUdp.KADEMLIA2_BOOTSTRAP_RES.value, Kad2BootstrapRes.class);

        addKadHandler(KadUdp.KADEMLIA_FIREWALLED_REQ.value, KadFirewalledReq.class);
        addKadHandler(KadUdp.KADEMLIA_FIREWALLED2_REQ.value, Kad2FirewalledReq.class);
        addKadHandler(KadUdp.KADEMLIA2_FIREWALLUDP.value, Kad2FirewalledUdp.class);
        addKadHandler(KadUdp.KADEMLIA_FIREWALLED_RES.value, Kad2FirewalledRes.class);

        addKadHandler(KadUdp.KADEMLIA2_PUBLISH_KEY_REQ.value, Kad2PublishKeysReq.class);
        addKadHandler(KadUdp.KADEMLIA2_PUBLISH_SOURCE_REQ.value, Kad2PublishSourcesReq.class);
        addKadHandler(KadUdp.KADEMLIA2_PUBLISH_RES.value, Kad2PublishRes.class);
    }

    @Override
    protected Class<? extends Serializable> keyToPacket(PacketKey key) {
        return supportedPacketsKad.get(key);
    }

    @Override
    protected PacketKey classToKey(Class<? extends Serializable> clazz) {
        return struct2KeyKad.get(clazz);
    }

    @Override
    public int serviceSize(PacketHeader ph) {
        return ph.sizePacket();
    }

    @Override
    protected PacketHeader getHeader() {
        return header;
    }
}

