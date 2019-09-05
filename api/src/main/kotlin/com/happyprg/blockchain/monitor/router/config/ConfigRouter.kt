/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.monitor.router.config

import com.happyprg.blockchain.monitor.config.ChainConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.router

@Configuration
class ConfigRouter(val chainConfig: ChainConfig) {

    @Bean
    fun configurationRoute() = router {
        (accept(APPLICATION_JSON) and "/configuration").nest {
            GET("/all") {
                ok().body(fromObject("""chainConfig - $chainConfig"""))
            }
        }
    }
}
