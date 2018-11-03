package com.ty.digitalfarms.net.exception;

/**
 * @author 金建强(ptma@163.com)
 * @version 2016-11-09 20:54
 */
public class HikVisionError {

    /**
     * 获取错误信息
     * 
     * @param errorCode
     * @return
     */
    public static String errorMsg(int errorCode) {
        switch (errorCode) {
            case 0:
                return "[" + errorCode + "] 没有错误!";
            case 1:
                return "[" + errorCode + "] 用户名密码错误!";
            case 2:
                return "[" + errorCode
                        + " ] 权限不足。该注册用户没有权限执行当前对设备的操作，可以与远程用户参数配置做对比。";
            case 3:
                return "[" + errorCode + " ] SDK未初始化。";
            case 4:
                return "[" + errorCode + " ] 通道号错误。设备没有对应的通道号。";
            case 5:
                return "[" + errorCode + " ] 设备总的连接数超过最大。";
            case 6:
                return "[" + errorCode + " ] 版本不匹配。SDK和设备的版本不匹配。";
            case 7:
                return "[" + errorCode
                        + " ] 连接设备失败。设备不在线或网络原因引起的连接超时等。";
            case 8:
                return "[" + errorCode + " ] 向设备发送失败。";
            case 9:
                return "[" + errorCode + " ] 从设备接收数据失败。";
            case 10:
                return "[" + errorCode + " ] 从设备接收数据超时。";
            case 11:
                return "[" + errorCode
                        + " ] 传送的数据有误。发送给设备或者从设备接收到的数据错误，如远程参数配置时输入设备不支持的值。";
            case 12:
                return "[" + errorCode + " ] 调用次序错误。";
            case 13:
                return "[" + errorCode + " ] 无此权限。";
            case 14:
                return "[" + errorCode + " ] 设备命令执行超时。";
            case 15:
                return "[" + errorCode + " ] 串口号错误。指定的设备串口号不存在。";
            case 16:
                return "[" + errorCode + " ] 报警端口错误。指定的设备报警输出端口不存在。";
            case 17:
                return "[" + errorCode
                        + " ] 参数错误。SDK接口中给入的输入或输出参数为空，或者参数格式或值不符合要求。";
            case 18:
                return "[" + errorCode + " ] 设备通道处于错误状态。";
            case 19:
                return "[" + errorCode + " ] 设备无硬盘。当设备无硬盘时，对设备的录像文件、硬盘配置等操作失败。";
            case 20:
                return "[" + errorCode
                        + " ] 硬盘号错误。当对设备进行硬盘管理操作时，指定的硬盘号不存在时返回该错误。";
            case 21:
                return "[" + errorCode + " ] 设备硬盘满。";
            case 22:
                return "[" + errorCode + " ] 设备硬盘出错。";
            case 23:
                return "[" + errorCode + " ] 设备不支持。";
            case 24:
                return "[" + errorCode + " ] 设备忙。";
            case 25:
                return "[" + errorCode + " ] 设备修改不成功。";
            case 26:
                return "[" + errorCode + " ] 密码输入格式不正确。";
            case 27:
                return "[" + errorCode + " ] 硬盘正在格式化，不能启动操作。";
            case 28:
                return "[" + errorCode + " ] 设备资源不足。";
            case 29:
                return "[" + errorCode + " ] 设备操作失败。";
            case 30:
                return "[" + errorCode
                        + " ] 语音对讲、语音广播操作中采集本地音频或打开音频输出失败。";
            case 31:
                return "[" + errorCode + " ] 设备语音对讲被占用。";
            case 32:
                return "[" + errorCode + " ] 时间输入不正确。";
            case 33:
                return "[" + errorCode + " ] 回放时设备没有指定的文件。";
            case 34:
                return "[" + errorCode
                        + " ] 创建文件出错。本地录像、保存图片、获取配置文件和远程下载录像时创建文件失败。";
            case 35:
                return "[" + errorCode + " ] 打开文件出错。设置配置文件、设备升级、上传审讯文件时打开文件失败。";
            case 36:
                return "[" + errorCode + " ] 上次的操作还没有完成。";
            case 37:
                return "[" + errorCode + " ] 获取当前播放的时间出错。";
            case 38:
                return "[" + errorCode + " ] 播放出错。";
            case 39:
                return "[" + errorCode + " ] 文件格式不正确。";
            case 40:
                return "[" + errorCode + " ] 路径错误。";
            case 41:
                return "[" + errorCode + " ] SDK资源分配错误。";
            case 42:
                return "[" + errorCode
                        + " ] 声卡模式错误。当前打开声音播放模式与实际设置的模式不符出错。";
            case 43:
                return "[" + errorCode + " ] 缓冲区太小。接收设备数据的缓冲区或存放图片缓冲区不足。";
            case 44:
                return "[" + errorCode + " ] 创建SOCKET出错。";
            case 45:
                return "[" + errorCode + " ] 设置SOCKET出错。";
            case 46:
                return "[" + errorCode + " ] 个数达到最大。分配的注册连接数、预览连接数超过SDK支持的最大数。";
            case 47:
                return "[" + errorCode + " ] 用户不存在。注册的用户ID已注销或不可用。";
            case 48:
                return "[" + errorCode + " ] 写FLASH出错。设备升级时写FLASH失败。";
            case 49:
                return "[" + errorCode + " ] 设备升级失败。网络或升级文件语言不匹配等原因升级失败。";
            case 50:
                return "[" + errorCode + " ] 解码卡已经初始化过。";
            case 51:
                return "[" + errorCode + " ] 调用播放库中某个函数失败。";
            case 52:
                return "[" + errorCode + " ] 登录设备的用户数达到最大。";
            case 53:
                return "[" + errorCode + " ] 获得本地PC的IP地址或物理地址失败。";
            case 54:
                return "[" + errorCode + " ] 设备该通道没有启动编码。";
            case 55:
                return "[" + errorCode + " ] IP地址不匹配。";
            case 56:
                return "[" + errorCode + " ] MAC地址不匹配。";
            case 57:
                return "[" + errorCode + " ] 升级文件语言不匹配。";
            case 58:
                return "[" + errorCode + " ] 播放器路数达到最大。";
            case 59:
                return "[" + errorCode + " ] 备份设备中没有足够空间进行备份。";
            case 60:
                return "[" + errorCode + " ] 没有找到指定的备份设备。";
            case 61:
                return "[" + errorCode + " ] 图像素位数不符，限24色。";
            case 62:
                return "[" + errorCode + " ] 图片高*宽超限，限128*256。";
            case 63:
                return "[" + errorCode + " ] 图片大小超限，限100K。";
            case 64:
                return "[" + errorCode + " ] 载入当前目录下Player Sdk出错。";
            case 65:
                return "[" + errorCode + " ] 找不到Player Sdk中某个函数入口。";
            case 66:
                return "[" + errorCode + " ] 载入当前目录下DSsdk出错。";
            case 67:
                return "[" + errorCode + " ] 找不到DsSdk中某个函数入口。";
            case 68:
                return "[" + errorCode + " ] 调用硬解码库DsSdk中某个函数失败。";
            case 69:
                return "[" + errorCode + " ] 声卡被独占。";
            case 70:
                return "[" + errorCode + " ] 加入多播组失败。";
            case 71:
                return "[" + errorCode + " ] 建立日志文件目录失败。";
            case 72:
                return "[" + errorCode + " ] 绑定套接字失败。";
            case 73:
                return "[" + errorCode
                        + " ] socket连接中断，此错误通常是由于连接中断或目的地不可达。";
            case 74:
                return "[" + errorCode + " ] 注销时用户ID正在进行某操作。";
            case 75:
                return "[" + errorCode + " ] 监听失败。";
            case 76:
                return "[" + errorCode + " ] 程序异常。";
            case 77:
                return "[" + errorCode
                        + " ] 写文件失败。本地录像、远程下载录像、下载图片等操作时写文件失败。";
            case 78:
                return "[" + errorCode + " ] 禁止格式化只读硬盘。";
            case 79:
                return "[" + errorCode + " ] 远程用户配置结构中存在相同的用户名。";
            case 80:
                return "[" + errorCode + " ] 导入参数时设备型号不匹配。";
            case 81:
                return "[" + errorCode + " ] 导入参数时语言不匹配。";
            case 82:
                return "[" + errorCode + " ] 导入参数时软件版本不匹配。";
            case 83:
                return "[" + errorCode + " ] 预览时外接IP通道不在线。";
            case 84:
                return "[" + errorCode + " ] 加载标准协议通讯库StreamTransClient失败。";
            case 85:
                return "[" + errorCode + " ] 加载转封装库失败。";
            case 86:
                return "[" + errorCode + " ] 超出最大的IP接入通道数。";
            case 87:
                return "[" + errorCode + " ] 添加录像标签或者其他操作超出最多支持的个数。";
            case 88:
                return "[" + errorCode
                        + " ] 图像增强仪，参数模式错误（用于硬件设置时，客户端进行软件设置时错误值）。";
            case 89:
                return "[" + errorCode + " ] 码分器不在线。";
            case 90:
                return "[" + errorCode + " ] 设备正在备份。";
            case 91:
                return "[" + errorCode + " ] 通道不支持该操作。";
            case 92:
                return "[" + errorCode + " ] 高度线位置太集中或长度线不够倾斜。";
            case 93:
                return "[" + errorCode + " ] 取消标定冲突，如果设置了规则及全局的实际大小尺寸过滤。";
            case 94:
                return "[" + errorCode + " ] 标定点超出范围。";
            case 95:
                return "[" + errorCode + " ] 尺寸过滤器不符合要求。";
            case 96:
                return "[" + errorCode + " ] 设备没有注册到ddns上。";
            case 97:
                return "[" + errorCode + " ] DDNS 服务器内部错误。";
            case 99:
                return "[" + errorCode + " ] 解码通道绑定显示输出次数受限。";
            case 150:
                return "[" + errorCode + " ] 别名重复（HiDDNS的配置）。";
            case 200:
                return "[" + errorCode + " ] 名称已存在。";
            case 201:
                return "[" + errorCode + " ] 阵列达到上限。";
            case 202:
                return "[" + errorCode + " ] 虚拟磁盘达到上限。";
            case 203:
                return "[" + errorCode + " ] 虚拟磁盘槽位已满。";
            case 204:
                return "[" + errorCode + " ] 重建阵列所需物理磁盘状态错误。";
            case 205:
                return "[" + errorCode + " ] 重建阵列所需物理磁盘为指定热备。";
            case 206:
                return "[" + errorCode + " ] 重建阵列所需物理磁盘非空闲。";
            case 207:
                return "[" + errorCode + " ] 不能从当前的阵列类型迁移到新的阵列类型。";
            case 208:
                return "[" + errorCode + " ] 迁移操作已暂停。";
            case 209:
                return "[" + errorCode + " ] 正在执行的迁移操作已取消。";
            case 210:
                return "[" + errorCode + " ] 阵列上存在虚拟磁盘，无法删除阵列。";
            case 211:
                return "[" + errorCode + " ] 对象物理磁盘为虚拟磁盘组成部分且工作正常。";
            case 212:
                return "[" + errorCode + " ] 指定的物理磁盘被分配为虚拟磁盘。";
            case 213:
                return "[" + errorCode + " ] 物理磁盘数量与指定的RAID等级不匹配。";
            case 214:
                return "[" + errorCode + " ] 阵列正常，无法重建。";
            case 215:
                return "[" + errorCode + " ] 存在正在执行的后台任务。";
            case 216:
                return "[" + errorCode + " ] 无法用ATAPI盘创建虚拟磁盘。";
            case 217:
                return "[" + errorCode + " ] 阵列无需迁移。";
            case 218:
                return "[" + errorCode + " ] 物理磁盘不属于同意类型。";
            case 219:
                return "[" + errorCode + " ] 无虚拟磁盘，无法进行此项操作。";
            case 220:
                return "[" + errorCode + " ] 磁盘空间过小，无法被指定为热备盘。";
            case 221:
                return "[" + errorCode + " ] 磁盘已被分配为某阵列热备盘。";
            case 222:
                return "[" + errorCode + " ] 阵列缺少盘。";
            case 223:
                return "[" + errorCode + " ] 名称为空。";
            case 224:
                return "[" + errorCode + " ] 输入参数有误。";
            case 225:
                return "[" + errorCode + " ] 物理磁盘不可用。";
            case 226:
                return "[" + errorCode + " ] 阵列不可用。";
            case 227:
                return "[" + errorCode + " ] 物理磁盘数不正确。";
            case 228:
                return "[" + errorCode + " ] 虚拟磁盘太小。";
            case 229:
                return "[" + errorCode + " ] 不存在。";
            case 230:
                return "[" + errorCode + " ] 不支持该操作。";
            case 231:
                return "[" + errorCode + " ] 阵列状态不是正常状态。";
            case 232:
                return "[" + errorCode + " ] 虚拟磁盘设备节点不存在。";
            case 233:
                return "[" + errorCode + " ] 槽位达到上限。";
            case 234:
                return "[" + errorCode + " ] 阵列上不存在虚拟磁盘。";
            case 235:
                return "[" + errorCode + " ] 虚拟磁盘槽位无效。";
            case 236:
                return "[" + errorCode + " ] 所需物理磁盘空间不足。";
            case 237:
                return "[" + errorCode + " ] 只有处于正常状态的阵列才能进行迁移。";
            case 238:
                return "[" + errorCode + " ] 阵列空间不足。";
            case 239:
                return "[" + errorCode + " ] 正在执行安全拔盘或重新扫描。";
            case 240:
                return "[" + errorCode + " ] 不支持创建大于16T的阵列。";
            case 300:
                return "[" + errorCode + " ] 配置ID不合理。";
            case 301:
                return "[" + errorCode + " ] 多边形不符合要求。";
            case 302:
                return "[" + errorCode + " ] 规则参数不合理。";
            case 303:
                return "[" + errorCode + " ] 配置信息冲突。";
            case 304:
                return "[" + errorCode + " ] 当前没有标定信息。";
            case 305:
                return "[" + errorCode + " ] 摄像机参数不合理。";
            case 306:
                return "[" + errorCode + " ] 长度不够倾斜，不利于标定。";
            case 307:
                return "[" + errorCode + " ] 标定出错，以为所有点共线或者位置太集中。";
            case 308:
                return "[" + errorCode + " ] 摄像机标定参数值计算失败。";
            case 309:
                return "[" + errorCode + " ] 输入的样本标定线超出了样本外接矩形框。";
            case 310:
                return "[" + errorCode + " ] 没有设置进入区域。";
            case 311:
                return "[" + errorCode
                        + " ] 交通事件规则中没有包括车道（特值拥堵和逆行）。";
            case 312:
                return "[" + errorCode + " ] 当前没有设置车道。";
            case 313:
                return "[" + errorCode + " ] 事件规则中包含2种不同方向。";
            case 314:
                return "[" + errorCode + " ] 车道和数据规则冲突。";
            case 315:
                return "[" + errorCode + " ] 不支持的事件类型。";
            case 316:
                return "[" + errorCode + " ] 车道没有方向。";
            case 317:
                return "[" + errorCode + " ] 尺寸过滤框不合理。";
            case 318:
                return "[" + errorCode + " ] 特征点定位时输入的图像没有人脸。";
            case 319:
                return "[" + errorCode + " ] 特征点定位时输入的图像太小。";
            case 320:
                return "[" + errorCode + " ] 单张图像人脸检测时输入的图像没有人脸。";
            case 321:
                return "[" + errorCode + " ] 建模时人脸太小。";
            case 322:
                return "[" + errorCode + " ] 建模时人脸图像质量太差。";
            case 323:
                return "[" + errorCode + " ] 高级参数设置错误。";
            case 324:
                return "[" + errorCode + " ] 标定样本数目错误，或数据值错误，或样本点超出地平线。";
            case 325:
                return "[" + errorCode + " ] 所配置规则不允许取消标定。";
            case 800:
                return "[" + errorCode + " ] 网络流量超过设备能力上限。";
            case 801:
                return "[" + errorCode
                        + " ] 录像文件在录像，无法被锁定。";
            case 802:
                return "[" + errorCode + " ] 由于硬盘太小无法格式化。";
            case 901:
                return "[" + errorCode + " ] 开窗通道号错误。";
            case 902:
                return "[" + errorCode + " ] 窗口层数错误，单个屏幕上最多覆盖的窗口层数。";
            case 903:
                return "[" + errorCode + " ] 窗口的块数错误，单个窗口可覆盖的屏幕个数。";
            case 904:
                return "[" + errorCode + " ] 输出分辨率错误。";
            case 905:
                return "[" + errorCode + " ] 布局号错误。";
            case 906:
                return "[" + errorCode + " ] 输入分辨率不支持。";
            case 907:
                return "[" + errorCode + " ] 子设备不在线。";
            case 908:
                return "[" + errorCode + " ] 没有空闲解码通道。";
            case 909:
                return "[" + errorCode + " ] 开窗能力上限。";
            case 910:
                return "[" + errorCode + " ] 调用顺序有误。";
            case 911:
                return "[" + errorCode + " ] 正在执行预案。";
            case 912:
                return "[" + errorCode + " ] 解码板正在使用。";
            case 401:
                return "[" + errorCode + " ] 无权限：服务器返回401时，转成这个错误码。";
            case 402:
                return "[" + errorCode + " ] 分配资源失败。";
            case 403:
                return "[" + errorCode + " ] 参数错误。";
            case 404:
                return "[" + errorCode
                        + " ] 指定的URL地址不存在：服务器返回404时，转成这个错误码。";
            case 406:
                return "[" + errorCode + " ] 用户中途强行退出。";
            case 407:
                return "[" + errorCode + " ] 获取RTSP端口错误。";
            case 410:
                return "[" + errorCode + " ] RTSP DECRIBE交互错误。";
            case 411:
                return "[" + errorCode + " ] RTSP DECRIBE发送超时。";
            case 412:
                return "[" + errorCode + " ] RTSP DECRIBE发送失败。";
            case 413:
                return "[" + errorCode + " ] RTSP DECRIBE接收超时。";
            case 414:
                return "[" + errorCode + " ] RTSP DECRIBE接收数据错误。";
            case 415:
                return "[" + errorCode + " ] RTSP DECRIBE接收失败。";
            case 416:
                return "[" + errorCode
                        + " ] RTSP DECRIBE服务器返回401,501等错误。";
            case 420:
                return "[" + errorCode + " ] RTSP SETUP交互错误。";
            case 421:
                return "[" + errorCode + " ] RTSP SETUP发送超时。";
            case 422:
                return "[" + errorCode + " ] RTSP SETUP发送错误。";
            case 423:
                return "[" + errorCode + " ] RTSP SETUP接收超时。";
            case 424:
                return "[" + errorCode + " ] RTSP SETUP接收数据错误。";
            case 425:
                return "[" + errorCode + " ] RTSP SETUP接收失败。";
            case 426:
                return "[" + errorCode + " ] 设备超过最大连接数。";
            case 430:
                return "[" + errorCode + " ] RTSP PLAY交互错误。";
            case 431:
                return "[" + errorCode + " ] RTSP PLAY发送超时。";
            case 432:
                return "[" + errorCode + " ] RTSP PLAY发送错误。";
            case 433:
                return "[" + errorCode + " ] RTSP PLAT接收超时。";
            case 434:
                return "[" + errorCode + " ] RTSP PLAY接收数据错误。";
            case 435:
                return "[" + errorCode + " ] RTSP PLAY接收失败。";
            case 436:
                return "[" + errorCode + " ] RTSP PLAY设备返回错误状态。";
            case 440:
                return "[" + errorCode + " ] RTSP TEARDOWN交互错误。";
            case 441:
                return "[" + errorCode + " ] RTSP TEARDOWN发送超时。";
            case 442:
                return "[" + errorCode + " ] RTSP TEARDOWN发送错误。";
            case 443:
                return "[" + errorCode + " ] RTSP TEARDOWN接收超时。";
            case 444:
                return "[" + errorCode + " ] RTSP TEARDOWN接收数据错误。";
            case 445:
                return "[" + errorCode + " ] RTSP TEARDOWN接收失败。";
            case 446:
                return "[" + errorCode + " ] RTSP TEARDOWN设备返回错误状态。";
            case 500:
                return "[" + errorCode + " ] 没有错误。";
            case 501:
                return "[" + errorCode + " ] 输入参数非法。";
            case 502:
                return "[" + errorCode + " ] 调用顺序不对。";
            case 503:
                return "[" + errorCode + " ] 多媒体时钟设置失败。";
            case 504:
                return "[" + errorCode + " ] 视频解码失败。";
            case 505:
                return "[" + errorCode + " ] 音频解码失败。";
            case 506:
                return "[" + errorCode + " ] 分配内存失败。";
            case 507:
                return "[" + errorCode + " ] 文件操作失败。";
            case 508:
                return "[" + errorCode + " ] 创建线程事件等失败。";
            case 509:
                return "[" + errorCode + " ] 创建directDraw失败。";
            case 510:
                return "[" + errorCode + " ] 创建后端缓存失败。";
            case 511:
                return "[" + errorCode + " ] 缓冲区满，输入流失败。";
            case 512:
                return "[" + errorCode + " ] 创建音频设备失败。";
            case 513:
                return "[" + errorCode + " ] 设置音量失败。";
            case 514:
                return "[" + errorCode + " ] 只能在播放文件时才能使用此接口。";
            case 515:
                return "[" + errorCode + " ] 只能在播放流时才能使用此接口。";
            case 516:
                return "[" + errorCode + " ] 系统不支持，解码器只能工作在Pentium";
            case 517:
                return "[" + errorCode + " ] 没有文件头。";
            case 518:
                return "[" + errorCode + " ] 解码器和编码器版本不对应。";
            case 519:
                return "[" + errorCode + " ] 初始化解码器失败。";
            case 520:
                return "[" + errorCode + " ] 文件太短或码流无法识别。";
            case 521:
                return "[" + errorCode + " ] 初始化多媒体时钟失败。";
            case 522:
                return "[" + errorCode + " ] 位拷贝失败。";
            case 523:
                return "[" + errorCode + " ] 显示overlay失败。";
            case 524:
                return "[" + errorCode + " ] 打开混合流文件失败。";
            case 525:
                return "[" + errorCode + " ] 打开视频流文件失败。";
            case 526:
                return "[" + errorCode + " ] JPEG压缩错误。";
            case 527:
                return "[" + errorCode + " ] 不支持该文件版本。";
            case 528:
                return "[" + errorCode + " ] 提取文件数据失败。";
            case 678:
                return "[" + errorCode
                        + " ] 预设的最小间隔错误。";
            case 679:
                return "[" + errorCode + " ] 预设分数错误。";
            case 680:
                return "[" + errorCode + " ] 预设的带宽值无效。";
            case 687:
                return "[" + errorCode + " ] 数据包太大。";
            case 688:
                return "[" + errorCode + " ] 数据包长度错误。";
            case 689:
                return "[" + errorCode + " ] 数据包版本错误。";
            case 690:
                return "[" + errorCode + " ] 未知数据包。";
            case 695:
                return "[" + errorCode + " ] 内存不足。";
            case 696:
                return "[" + errorCode + " ] Lib库没有初始化。";
            case 697:
                return "[" + errorCode + " ] 没有找到会话。";
            case 698:
                return "[" + errorCode + " ] 参数无效。";
            case 699:
                return "[" + errorCode + " ] Qos 错误。";
            case 700:
                return "[" + errorCode + " ] 没有错误。";
            default:
                return "[" + errorCode + " ] 未知错误。";
        }
    }

}

