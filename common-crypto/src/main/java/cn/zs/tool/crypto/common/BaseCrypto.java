package cn.zs.tool.crypto.common;

import cn.zs.tool.crypto.constant.AlgorithmEnum;

import java.security.Provider;

/**
 * 加密算法基类
 *
 * @author sheng
 */
public interface BaseCrypto {
    /**
     * 获取当前加密算法所代表的枚举
     */
    AlgorithmEnum algorithm();

    /**
     * 获取当前加密算法所使用的{@link Provider}提供者
     */
    default Provider provider() {
        return algorithm().provider();
    }
}
