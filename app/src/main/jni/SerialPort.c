#include <termios.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

#include "com_keymantek_serialport_utils_SerialPort.h"

#include "android/log.h"
static const char *TAG="serial_port";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
#define IOCTRL_PMU_EXTGPS_SW_ON   0x1000F
#define IOCTRL_PMU_EXTGPS_SW_OFF  0x10010
#define IOCTRL_GPIO_ON_OFF  0x10022
int g_hSri=-1;

struct Data
{
	unsigned int name;   //gpio号
	unsigned int value;  //gpio值
};

/**
 * 设置串口参数
 * databits 数据位
 * stopbits 停止位
 * parity 校验位
 */
int set_Parity(int fd, int databits, int stopbits, int parity) {

	struct termios options;
	LOGI("fd:%d nBits;%d nEvent;%c nStop;%d \r\n" ,fd,databits,parity,stopbits);
	if (tcgetattr(fd, &options) != 0) {
		perror("SetupSerial 1");
		return 0;
	}
	options.c_cflag &= ~CSIZE;
	switch (databits) {
	case 7:
		options.c_cflag |= CS7;
		break;
	case 8:
		options.c_cflag |= CS8;
		break;
	}
	switch (parity) {
	case 'n':
	case 'N':
		options.c_cflag &= ~PARENB;
		options.c_iflag &= ~INPCK;
		break;
	case 'o':
	case 'O':
		options.c_cflag |= (PARODD | PARENB);
		options.c_iflag |= INPCK;
		break;
	case 'e':
	case 'E':
		options.c_cflag |= PARENB;
		options.c_cflag &= ~PARODD;
		options.c_iflag |= INPCK;
		break;
	case 'S':
	case 's':
		options.c_cflag &= ~PARENB;
		options.c_cflag &= ~CSTOPB;
		break;
	}
	switch (stopbits) {
	case 1:
		options.c_cflag &= ~CSTOPB;
		break;
	case 2:
		options.c_cflag |= CSTOPB;
		break;
	}
	if (parity != 'n')
		options.c_iflag |= INPCK;
	options.c_cc[VTIME] = 150; // 15 seconds
	options.c_cc[VMIN] = 0;

	tcflush(fd, TCIFLUSH);
	if (tcsetattr(fd, TCSANOW, &options) != 0) {
		LOGI("set_uart_opt tcsetattr error");
		return 0;
	}
	LOGI("set_uart_opt OK!\r\n");
	return 1;
}


int sri_io_exit()
{
	LOGI("sri_exit ++");

	if(g_hSri == -1) //open fail
	{
	}
	else
	{
        close(g_hSri); //close device
	}
	return 1;
}




int sri_IOCTLSRI(unsigned int name,unsigned int value)
{

	    if(g_hSri == -1) //open fail
		{
			return g_hSri;
		}
		else
		{
			struct Data dta;
			dta.name=name;
			dta.value=value;
			ioctl(g_hSri, IOCTRL_GPIO_ON_OFF, &dta);
			//ioctl(g_hSri,controlcode);
			return IOCTRL_GPIO_ON_OFF;
		}
}

static speed_t getBaudrate(jint baudrate)
{
        switch(baudrate) {
        case 0: return B0;
        case 50: return B50;
        case 75: return B75;
        case 110: return B110;
        case 134: return B134;
        case 150: return B150;
        case 200: return B200;
        case 300: return B300;
        case 600: return B600;
        case 1200: return B1200;
        case 1800: return B1800;
        case 2400: return B2400;
        case 4800: return B4800;
        case 9600: return B9600;
        case 19200: return B19200;
        case 38400: return B38400;
        case 57600: return B57600;
        case 115200: return B115200;
        case 230400: return B230400;
        case 460800: return B460800;
        case 500000: return B500000;
        case 576000: return B576000;
        case 921600: return B921600;
        case 1000000: return B1000000;
        case 1152000: return B1152000;
        case 1500000: return B1500000;
        case 2000000: return B2000000;
        case 2500000: return B2500000;
        case 3000000: return B3000000;
        case 3500000: return B3500000;
        case 4000000: return B4000000;
        default: return -1;
        }
}



/*
 * Class:     android_serialport_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;II)Ljava/io/FileDescriptor;
 */
jobject Java_com_keymantek_serialport_utils_SerialPort_open
  (JNIEnv *env, jclass thiz, jstring path, jint baudrate, jint nBits, jchar nEvent, jint nStop, jint nType)
{
        int fd;
        speed_t speed;
        jobject mFileDescriptor;

        /* Check arguments */
        {
                speed = getBaudrate(baudrate);
                if (speed == -1) {
                        /* TODO: throw an exception */
                        LOGE("Invalid baudrate");
                        return NULL;
                }
        }
       {
        	if(g_hSri == -1)
        		{
        		g_hSri = open("/dev/sri", O_RDWR);
        			if(g_hSri==0)
        			{
        				LOGD("open sri failed!\n");
        			}
        		}
               //	g_hSri = open("/dev/sri",O_RDWR);//Open device ,get the handle
               switch(nType){
                    case 0://激光
                        sri_IOCTLSRI(96,1);
                        sri_IOCTLSRI(99,0);
                        sri_IOCTLSRI(100,1);
                        sri_IOCTLSRI(128,0);
                        sri_IOCTLSRI(78,1);
                        break;
                    case 1://普通
                        sri_IOCTLSRI(96,1);
                        sri_IOCTLSRI(99,1);
                        sri_IOCTLSRI(100,0);
                        sri_IOCTLSRI(128,0);
                        sri_IOCTLSRI(78,1);
                        break;
                    case 2://安全模块
                        sri_IOCTLSRI(96,1);
                        sri_IOCTLSRI(97,1);
                        sri_IOCTLSRI(42,0);
                        sri_IOCTLSRI(44,1);
                        sri_IOCTLSRI(127,1);
                        sleep(0.01);
                        sri_IOCTLSRI(127,0);
                        sleep(0.01);
                        sri_IOCTLSRI(127,1);
                        break;
               }
        		//sri_IOCTLSRI(96,1);
        		//sri_IOCTLSRI(99,0);
        		//sri_IOCTLSRI(100,1);
        		//sri_IOCTLSRI(128,0);
        		//sri_IOCTLSRI(78,1);
        }
        /* Opening device */
        {
                jboolean iscopy;
                const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
                LOGD("Opening serial port %s with flags 0x%x", path_utf, O_RDWR | 0);
                fd = open(path_utf, O_RDWR | 0);
                LOGD("open() fd = %d", fd);
                (*env)->ReleaseStringUTFChars(env, path, path_utf);
                if (fd == -1)
                {
                        /* Throw an exception */
                        LOGE("Cannot open port");
                        /* TODO: throw an exception */
                        return NULL;
                }
        }

        /* Configure device */
        {
				struct termios cfg;
				LOGI(TAG, "serial_port_open,Configuring serial port");
				if (tcgetattr(fd, &cfg)) {
					LOGI(TAG, "serial_port_open,tcgetattr() failed");
					LOGE(TAG, "serial_port_open", "tcgetattr() failed");
					close(fd);
					/* TODO: throw an exception */
					return NULL;
				}

				cfmakeraw(&cfg);
				cfsetispeed(&cfg, speed);
				cfsetospeed(&cfg, speed);

				if (tcsetattr(fd, TCSANOW, &cfg)) {
					LOGI(TAG, "serial_port_open", "tcsetattr() failed");
					LOGE(TAG, "serial_port_open", "tcsetattr() failed");
					close(fd);
					/* TODO: throw an exception */
					return NULL;
				}
				set_Parity(fd, nBits, nStop, nEvent);
        }

        /* Create a corresponding file descriptor */
        {
                jclass cFileDescriptor = (*env)->FindClass(env, "java/io/FileDescriptor");
                jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor, "<init>", "()V");
                jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor, "descriptor", "I");
                mFileDescriptor = (*env)->NewObject(env, cFileDescriptor, iFileDescriptor);
                (*env)->SetIntField(env, mFileDescriptor, descriptorID, (jint)fd);
        }

        return mFileDescriptor;
}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    close
 * Signature: ()V
 */
void Java_com_keymantek_serialport_utils_SerialPort_close
  (JNIEnv *env, jobject thiz, jint type)
{
    switch(type){
        //激光红外
        case 0:
            sri_IOCTLSRI(78,0);
            sri_IOCTLSRI(128,1);
            sri_IOCTLSRI(100,0);
            sri_IOCTLSRI(99,0);
            sri_IOCTLSRI(96,0);
            break;
        //普通红外
        case 1:
            sri_IOCTLSRI(78,0);
            sri_IOCTLSRI(128,1);
            sri_IOCTLSRI(100,0);
            sri_IOCTLSRI(99,0);
            sri_IOCTLSRI(96,0);
            break;
        //安全模块
        case 2:
            sri_IOCTLSRI(127,0);
            sri_IOCTLSRI(44,0);
            sri_IOCTLSRI(42,1);
            sri_IOCTLSRI(97,0);
            sri_IOCTLSRI(96,0);
        break;
    }
}
