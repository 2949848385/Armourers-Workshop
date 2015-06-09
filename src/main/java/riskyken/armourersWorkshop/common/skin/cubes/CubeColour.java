package riskyken.armourersWorkshop.common.skin.cubes;

import io.netty.buffer.ByteBuf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import riskyken.armourersWorkshop.api.common.skin.cubes.ICubeColour;
import net.minecraft.nbt.NBTTagCompound;

public class CubeColour implements ICubeColour {

    private static final String TAG_RED = "r";
    private static final String TAG_GREEN = "g";
    private static final String TAG_BLUE = "b";
    
    private byte[] r;
    private byte[] g;
    private byte[] b;
    
    public CubeColour() {
        initArray();
    }
    
    public CubeColour(int colour) {
        r = new byte[6];
        g = new byte[6];
        b = new byte[6];
        for (int i = 0; i < 6; i++) {
            r[i] = (byte) (colour >> 16 & 0xff);
            g[i] = (byte) (colour >> 8 & 0xff);
            b[i] = (byte) (colour & 0xff);
        }
    }
    
    public CubeColour(ByteBuf buf) {
        initArray();
        readFromBuf(buf);
    }
    
    public CubeColour(DataInputStream stream, int version) throws IOException {
        initArray();
        readFromStream(stream, version);
    }
    
    private void initArray() {
        r = new byte[6];
        g = new byte[6];
        b = new byte[6];
        for (int i = 0; i < 6; i++) {
            r[i] = (byte)255;
            g[i] = (byte)255;
            b[i] = (byte)255;
        }
    }
    
    @Override
    public byte getRed(int side) {
        return r[side];
    }

    @Override
    public byte getGreen(int side) {
        return g[side];
    }

    @Override
    public byte getBlue(int side) {
        return b[side];
    }

    @Override
    public byte[] getRed() {
        return r;
    }

    @Override
    public byte[] getGreen() {
        return g;
    }

    @Override
    public byte[] getBlue() {
        return b;
    }
    
    @Override
    public void setColour(int colour, int side) {
        r[side] = (byte) (colour >> 16 & 0xff);
        g[side] = (byte) (colour >> 8 & 0xff);
        b[side] = (byte) (colour & 0xff);
    }
    
    @Deprecated
    @Override
    public void setColour(int colour) {
        for (int i = 0; i < 6; i++) {
            r[i] = (byte) (colour >> 16 & 0xff);
            g[i] = (byte) (colour >> 8 & 0xff);
            b[i] = (byte) (colour & 0xff);
        }
    }

    @Override
    public void setRed(byte red, int side) {
        r[side] = red;
    }

    @Override
    public void setGreen(byte green, int side) {
        g[side] = green;
    }

    @Override
    public void setBlue(byte blue, int side) {
        b[side] = blue;
    }

    @Override
    public void setRed(byte[] red) {
        r = red;
    }

    @Override
    public void setGreen(byte[] green) {
        g = green;
    }

    @Override
    public void setBlue(byte[] blue) {
        b = blue;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        for (int i = 0; i < 6; i++) {
            r[i] = compound.getByte(TAG_RED + i);
            g[i] = compound.getByte(TAG_GREEN + i);
            b[i] = compound.getByte(TAG_BLUE + i);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        for (int i = 0; i < 6; i++) {
            compound.setByte(TAG_RED + i, r[i]);
            compound.setByte(TAG_GREEN + i, g[i]);
            compound.setByte(TAG_BLUE + i, b[i]);
        }
    }
    
    @Override
    public void readFromBuf(ByteBuf buf) {
        for (int i = 0; i < 6; i++) {
            r[i] = buf.readByte();
            g[i] = buf.readByte();
            b[i] = buf.readByte();
        }

    }
    
    @Override
    public void writeToBuf(ByteBuf buf) {
        for (int i = 0; i < 6; i++) {
            buf.writeByte(r[i]);
            buf.writeByte(g[i]);
            buf.writeByte(b[i]);
        }
    }
    
    @Override
    public void readFromStream(DataInputStream stream, int version) throws IOException {
        for (int i = 0; i < 6; i++) {
            r[i] = stream.readByte();
            g[i] = stream.readByte();
            b[i] = stream.readByte();
        }
    }
    
    @Override
    public void writeToStream(DataOutputStream stream) throws IOException {
        for (int i = 0; i < 6; i++) {
            stream.writeByte(r[i]);
            stream.writeByte(g[i]);
            stream.writeByte(b[i]);
        }
    }

    @Override
    public String toString() {
        return "CubeColour [r=" + Arrays.toString(r) + ", g="
                + Arrays.toString(g) + ", b=" + Arrays.toString(b) + "]";
    }
}
