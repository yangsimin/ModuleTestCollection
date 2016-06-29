package com.keymantek.serialport.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口本地代码的调用
 * open：打开串口，返回一个faildescription
 * close：关闭串口
 * @author 骆锦江
 *
 */
public class SerialPort {
	private static final String TAG = "SerialPort";

	/*
	 * Do not remove or rename the field mFd: it is used by native method
	 * close();
	 */
	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public SerialPort(File device, int baudrate,int nBits, char nEvent, int nStop, int nType)
			throws SecurityException, IOException {

		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		mFd = open(device.getAbsolutePath(), baudrate, nBits,nEvent,nStop, nType);
		if (mFd == null) {
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}

	// Getters and setters
	public InputStream getInputStream() {
		return mFileInputStream;
	}

	public OutputStream getOutputStream() {
		return mFileOutputStream;
	}

	public void closeSerialport(int type){
		close(type);
	}
	// JNI打开串口操作
	private native static FileDescriptor open(String path, int baudrate,int nBits, char nEvent, int nStop, int type);
	//JNI关闭串口操作
	private native static void close(int nType);
	

	//加载本地.so类库
	static {
		System.loadLibrary("serial_port");
	}
}
