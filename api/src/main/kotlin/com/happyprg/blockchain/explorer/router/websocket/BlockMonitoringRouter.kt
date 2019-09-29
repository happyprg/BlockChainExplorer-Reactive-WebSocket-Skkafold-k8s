/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.explorer.router.websocket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.happyprg.blockchain.explorer.block.BlockHandler
import com.happyprg.blockchain.explorer.config.ChainConfig
import com.happyprg.blockchain.explorer.util.toResult
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import java.time.Duration.ofMillis
import java.util.stream.Stream

@Component
class BlockMonitoringRouter(val chainConfig: com.happyprg.blockchain.explorer.config.ChainConfig, val blockHandler: com.happyprg.blockchain.explorer.block.BlockHandler) : WebSocketHandler {

    lateinit var blockResultMap: Map<String, String>
    val intervalFlux: Flux<Tuple2<Long, Map<String, String>>>

    @Scheduled(fixedRateString = "\${scheduler.fixed-rate}", initialDelayString = "\${scheduler.initial-delay}")
    fun fetchLastBlock() = blockHandler.getLastBlock(chainConfig.sNodeHost!!).log("fetch [S] NodeLastBlock")
        .zipWith(
            blockHandler.getLastBlock(chainConfig.cNodeHost!!).log("fetch *C* NodeLastBlock")
        ) { t, u ->
            blockResultMap = mapOf("sNode" to t.toResult(), "cNode" to u.toResult())
        }.block(ofMillis(900L))!!


    @Override
    override fun handle(webSocketSession: WebSocketSession): Mono<Void> =
        webSocketSession.send(intervalFlux.map {
            webSocketSession.textMessage(
                jacksonObjectMapper().writeValueAsString(
                    mapOf(
                        "blockResultMap" to blockResultMap
                    )
                )
            )
        }).and(
            webSocketSession.receive().map {
                println(it.payloadAsText)
                it.payloadAsText
            }
        )

    init {
        this.blockResultMap = emptyMap()
        this.intervalFlux =
            Flux.zip(Flux.interval(ofMillis(2000L)), Flux.fromStream { Stream.generate { blockResultMap } })
    }
}