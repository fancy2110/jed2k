package org.jed2k.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import static org.jed2k.protocol.Unsigned.uint8;
import static org.jed2k.protocol.Unsigned.uint16;
import static org.jed2k.protocol.Unsigned.uint32;

import org.jed2k.exception.ErrorCode;
import org.jed2k.exception.JED2KException;

public class ByteContainer<CS extends UNumber> implements Serializable {
    private static Logger log = Logger.getLogger(ByteBuffer.class.getName());

    public final CS size;
    public byte[] value;

    public ByteContainer(CS size) {
        this.size = size;
    }

    public ByteContainer(CS size, byte[] value) {
        this.size = size;
        this.value = value;
        this.size.assign(value.length);
    }

    @Override
    public ByteBuffer get(ByteBuffer src) throws JED2KException {
        size.get(src);
        value = new byte[size.intValue()];
        return src.get(value);
    }

    @Override
    public ByteBuffer put(ByteBuffer dst) throws JED2KException {
        if (value == null) {
            size.assign(0);
            return size.put(dst);
        } else {
            size.assign(value.length);
            return size.put(dst).put(value);
        }
    }

    public String asString() throws JED2KException {
        try {
            if (value != null)  return new String(value, "UTF-8");
            return null;
        } catch(UnsupportedEncodingException e) {
            throw new JED2KException(e, ErrorCode.UNSUPPORTED_ENCODING);
        }
    }

    public static<CS extends UNumber> ByteContainer<UInt8> fromString8(String value) throws JED2KException {
        try {
            byte[] content = value.getBytes("UTF-8");
            return new ByteContainer<UInt8>(uint8(), content);
        } catch(UnsupportedEncodingException e) {
            throw new JED2KException(e, ErrorCode.UNSUPPORTED_ENCODING);
        }
    }

    public static<CS extends UNumber> ByteContainer<UInt16> fromString16(String value) throws JED2KException {
        try {
            byte[] content = value.getBytes("UTF-8");
            return new ByteContainer<UInt16>(uint16(), content);
        } catch(UnsupportedEncodingException e) {
            throw new JED2KException(e, ErrorCode.UNSUPPORTED_ENCODING);
        }
    }

    public static<CS extends UNumber> ByteContainer<UInt32> fromString32(String value) throws JED2KException {
        try {
            byte[] content = value.getBytes("UTF-8");
            return new ByteContainer<UInt32>(uint32(), content);
        } catch(UnsupportedEncodingException e) {
            throw new JED2KException(e, ErrorCode.UNSUPPORTED_ENCODING);
        }
    }

    @Override
    public String toString() {
        try{
            if (value != null)
                return new String(value, "UTF-8");
        }
        catch(UnsupportedEncodingException ex){
            log.warning(ex.getMessage());
        }

        return new String();
    }


    @Override
    public int bytesCount() {
        return size.bytesCount() + value.length;
    }

}