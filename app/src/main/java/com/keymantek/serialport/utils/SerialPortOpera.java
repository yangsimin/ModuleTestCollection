package com.keymantek.serialport.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;


/**
 * ���ڲ�����
 * ʵ���˶Դ��ڵĴ򿪣��رգ��������ݺͷ��������ĸ�����
 *
 * @author �����
 */
public class SerialPortOpera
{
    private SerialPort mSerialPort = null;
    protected OutputStream mOutputStream;
    protected InputStream mInputStream;

    /**
     * ��ʼ�����ڲ���
     *
     * @param uart_port_path ���ڵ�·��
     * @param baudrate       ������
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
     * �رմ��ڲ���
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
     * �������ݵĽ���
     *
     * @return ������
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
     * �������ݷ���
     *
     * @param text Ҫ���͵�����
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
