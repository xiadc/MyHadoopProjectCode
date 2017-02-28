package com.xdc;

import java.io.EOFException;
import org.apache.hadoop.fs.FSDataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义DataInputStream类，方便用来读取反向的字节数据
 * @author root
 *
 */
public class myDataInputStream  {
	 FSDataInputStream hdfsin;
	 public myDataInputStream(InputStream in) throws IOException {  
		 hdfsin=(FSDataInputStream)in;
		  }  
		  public final int read(byte b[]) throws IOException {  
		    return hdfsin.read(b, 0, b.length);  
		  }  
		  
		  public final int read(byte b[], int off, int len) throws IOException {  
		    return hdfsin.read(b, off, len);  
		  }  
		  
		  public final void readFully(byte b[]) throws IOException {  
		    readFully(b, 0, b.length);  
		  }  
		  
		  public final void readFully(byte b[], int off, int len) throws IOException {  
		    if (len < 0)  
		      throw new IndexOutOfBoundsException();  
		    int n = 0;  
		    while (n < len) {  
		      int count = hdfsin.read(b, off + n, len - n);  
		      if (count < 0)  
		        throw new EOFException();  
		      n += count;  
		    }  
		  }  
		  
		  public final int skipBytes(int n) throws IOException {  
		    int total = 0;  
		    int cur = 0;  
		    while ((total < n) && ((cur = (int) hdfsin.skip(n - total)) > 0)) {  
		      total += cur;  
		    }  
		    return total;  
		  }  
		  
		  public final byte readByte() throws IOException {  
		    int ch = hdfsin.read();  
		    if (ch < 0)  
		      throw new EOFException();  
		    return (byte) (ch);  
		  }  
		  
		  public final int readUnsignedByte() throws IOException {  
		    int ch = hdfsin.read();  
		    if (ch < 0)  
		      throw new EOFException();  
		    return ch;  
		  }  
		  
		  public final short readShort() throws IOException {  
		    int ch2 = hdfsin.read();  
		    int ch1 = hdfsin.read();  
		    if ((ch1 | ch2) < 0)  
		      throw new EOFException();  
		    return (short) ((ch1 << 8) + (ch2 << 0));  
		  }  
		  
		  public final int readUnsignedShort() throws IOException {  
		    int ch2 = hdfsin.read();  
		    int ch1 = hdfsin.read();  
		    if ((ch1 | ch2) < 0)  
		      throw new EOFException();  
		    return (ch1 << 8) + (ch2 << 0);  
		  }  
		  
		  public final char readChar() throws IOException {  
		    int ch2 = hdfsin.read();  
		    int ch1 = hdfsin.read();  
		    if ((ch1 | ch2) < 0)  
		      throw new EOFException();  
		    return (char) ((ch1 << 8) + (ch2 << 0));  
		  }  
		  
		  public final int readInt() throws IOException {  
		    int ch4 = hdfsin.read();  
		    int ch3 = hdfsin.read();  
		    int ch2 = hdfsin.read();  
		    int ch1 = hdfsin.read();  
		    if ((ch1 | ch2 | ch3 | ch4) < 0)  
		      throw new EOFException();  
		    return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));  
		  }  
		  
		  private byte readBuffer[] = new byte[8];  
		  
		  public final long readLong() throws IOException {  
		    readFully(readBuffer, 0, 8);  
		    return (((long) readBuffer[7] << 56) + ((long) (readBuffer[6] & 255) << 48)  
		        + ((long) (readBuffer[5] & 255) << 40) + ((long) (readBuffer[4] & 255) << 32)  
		        + ((long) (readBuffer[3] & 255) << 24) + ((readBuffer[2] & 255) << 16)  
		        + ((readBuffer[1] & 255) << 8) + ((readBuffer[0] & 255) << 0));  
		  }  
		  
		  public  float readFloat() throws IOException {  
		    return Float.intBitsToFloat(readInt());  
		  }  
		  
		  public final double readDouble() throws IOException {  
		    return Double.longBitsToDouble(readLong());  
		  }
		  public final void close() throws IOException{
			  hdfsin.close();
		  }
}