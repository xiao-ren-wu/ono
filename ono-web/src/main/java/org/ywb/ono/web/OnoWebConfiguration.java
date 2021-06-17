package org.ywb.ono.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author yuwenbo
 * @date 2021/6/11 上午11:33
 * @since 1.0.0
 */
@Slf4j
@ComponentScan("org.ywb.ono.web")
public class OnoWebConfiguration {
    public OnoWebConfiguration() {
        log.info("\ninit OnoWebConfiguration");
    }
}
