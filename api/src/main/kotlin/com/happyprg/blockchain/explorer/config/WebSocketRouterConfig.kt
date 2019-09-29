/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.explorer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebSocketRouterConfig(
    val blockMonitoringRouter: com.happyprg.blockchain.explorer.router.websocket.BlockMonitoringRouter,
    val onDemandBlockMonitoringRouter: com.happyprg.blockchain.explorer.router.websocket.OnDemandBlockMonitoringRouter
) {
    @Bean
    fun websocketHandlerAdapter() = WebSocketHandlerAdapter()

    @Bean
    fun handlerMapping(): HandlerMapping {
        val handlerMapping = SimpleUrlHandlerMapping()
        handlerMapping.urlMap = mapOf(
            "/ws/block/explorer" to blockMonitoringRouter,
            "/ws/block/onDemandMonitoring" to onDemandBlockMonitoringRouter
        )
        handlerMapping.order = 1
        return handlerMapping
    }
}