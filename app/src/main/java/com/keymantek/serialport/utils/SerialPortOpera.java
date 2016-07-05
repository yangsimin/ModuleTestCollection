package com.keymantek.serialport.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;


/**
 * 串口操作类
 * 实现了对串口的打开，关闭，接收数据和发送数据四个功能
 *
 * @author 骆锦江
 */
public class SerialPortOpera
{
    private SerialPort mSerialPort = null;
    protected OutputStream mOutputStream;
    protected InputStream mInputStream;

    /**
     * 初始化串口操作
     *
     * @param uart_port_path 串口的路径
     * @param baudrate       波特率
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */
    public void openSerialPort(String uart_port_path, int baudrate, int nBits, char nEvent, int nStop, int nType) throws SecurityException, IOException, InvalidParameterException
    {
        if (mSerialPort == null)
        {
                    /* Check parameters */
            if ((uart_port_path.length() == 0) || (baudrate == -1))
            {
                throw new InvalidParameterException();
            }
                    /* Open the serial port */
            mSerialPort = new SerialPort(new File(uart_port_path), baudrate, nBits, nEvent, nStop, nType);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
        }
    }

    /**
     * 关闭串口操作
     */
    public void closeSerialPort(int type)
    {
             /*if (mReadThread != null)
 			mReadThread.interrupt();*/
        if (mSerialPort != null)
        {
            mSerialPort.closeSerialport(type);
            mSerialPort = null;
        }
        mSerialPort = null;
    }

    /**
     * 串口数据的接收
     *
     * @return 输入流
     */
    public InputStream SerialPortReceiver()
    {
        //String data=new String(buffer, 0, size);
        if (mInputStream == null)
        {
            if (mSerialPort == null)
            {
                return null;
            }
            return mSerialPort.getInputStream();
        }
        return mInputStream;
    }

    /**
     * 串口数据发送
     *
     * @param text 要发送的数据
     */
    public void SerialPortWrite(byte[] text)
    {
        //int i;
       /* CharSequence t = data;
        char[] text = new char[t.length()];*/
        /*for (i=0; i<t.length(); i++) {
                text[i] = t.charAt(i);
        }*/

        try
        {
            mOutputStream.write(text, 0, text.length);
            mOutputStream.flush();

            //mOutputStream.write('\n');
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
