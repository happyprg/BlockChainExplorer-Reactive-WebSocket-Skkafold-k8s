/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.monitor.router.websocket

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.result.Result
import com.happyprg.blockchain.monitor.block.BlockHandler
import com.happyprg.blockchain.monitor.util.toResult
import com.jayway.jsonpath.JsonPath
import org.apache.commons.lang3.StringUtils.EMPTY
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
class OnDemandBlockMonitoringRouter(val blockHandler: BlockHandler) : WebSocketHandler {

    lateinit var blockResultMap: Map<String, String>
    val intervalFlux: Flux<Tuple2<Long, Map<String, String>>>
    var sNodeHost: String = EMPTY
    var cNodeHost: String = EMPTY

    @Scheduled(fixedRateString = "\${scheduler.fixed-rate}", initialDelayString = "\${scheduler.initial-delay}")
    fun fetchLastBlock() {
        if (sNodeHost.isNotBlank() && cNodeHost.isNotBlank()) {
            blockHandler.getLastBlock(sNodeHost).log("fetch [S] NodeLastBlock")
                .zipWith(
                    blockHandler.getLastBlock(cNodeHost).log("fetch *C* NodeLastBlock")
                ) { t, u ->
                    blockResultMap = mapOf("sNode" to t.toResult(), "cNode" to u.toResult())
                }.block(ofMillis(900L))!!
            return
        }
        when {
            sNodeHost.isNotBlank() -> blockHandler.getLastBlock(sNodeHost).map {
                var toResult = "{}"
                if (it is Result.Success) {
                    toResult = it.toResult()
                }
                blockResultMap = mapOf("sNode" to toResult, "cNode" to "{}")
            }.log("Refresh succeed [S] LastBlock").block(ofMillis(900L))!!
        }
    }

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
        }).and(webSocketSession.receive().map {
            parseClientMsgSilently(it.payloadAsText).refreshNodeHosts()
        })

    init {
        this.blockResultMap = emptyMap()
        this.intervalFlux =
            Flux.zip(Flux.interval(ofMillis(2000L)), Flux.fromStream { Stream.generate { blockResultMap } })
    }


    fun Triple<String, String, String>.refreshNodeHosts(): Triple<String, String, String> {
        when {
            second.startsWith("http") -> this@OnDemandBlockMonitoringRouter.sNodeHost = second
        }
        when {
            third.startsWith("http") -> this@OnDemandBlockMonitoringRouter.cNodeHost = third
        }
        return this
    }


    fun parseClientMsgSilently(clientPayload: String): Triple<String, String, String> {
        println("clientPayload - $clientPayload")
        var first = EMPTY
        var second = EMPTY
        var third = EMPTY
        try {
            first = JsonPath.parse(clientPayload).read<String>("$.comment")
            second = JsonPath.parse(clientPayload).read<String>("$.sNodeHost")
            third = JsonPath.parse(clientPayload).read<String>("$.cNodeHost")
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)

        } finally {
            return Triple(first, second, third)
        }
    }

}