package io.github.rmy20.tool.crypto.constant;

import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.Objects;

/**
 * 安全 api 提供者
 *
 * @author sheng
 */
@Getter
public enum ProviderEnum {
    /**
     * bouncy castle
     */
    BC(BouncyCastleProvider.PROVIDER_NAME) {
        @Override
        public Provider initProvider() {
            Provider provider = Security.getProvider(getName());
            if (Objects.isNull(provider)) {
                provider = new BouncyCastleProvider();
                Security.addProvider(provider);
            }
            return provider;
        }
    },
    ;

    /**
     * 初始化{@link Provider 提供者}
     */
    abstract Provider initProvider();

    ProviderEnum(String name) {
        this.name = name;
        this.provider = initProvider();
    }

    /**
     * 名称
     */
    private final String name;

    /**
     * 提供者
     */
    private final Provider provider;
}
