/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.explorer.block

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import com.happyprg.blockchain.explorer.config.ChainConfig
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils.replace
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Service
class BlockService(val chainConfig: com.happyprg.blockchain.explorer.config.ChainConfig) {

    fun getBlockByHash(blockHash: String): Mono<Result<String, FuelError>> =
        Fuel.post("${chainConfig.sNodeHost}/api/v3")
            .jsonBody(
                """{
                                          "jsonrpc": "2.0",
                                          "method": "icx_getBlockByHash",
                                          "id": 21000,
                                          "params": {
                                            "hash": "0x${replace(
                    replace(
                        blockHash, "0x",
                        ""
                    ), "bx", ""
                )}"
                                                    }
                                        }"""
            ).responseString().third.toMono()

    fun getBlockByHeight(blockHeight: Int): Mono<Result<String, FuelError>> =
        Fuel.post("${chainConfig.sNodeHost}/api/v3")
            .jsonBody(
                """{
                                        "jsonrpc": "2.0",
                                        "method": "icx_getBlockByHeight",
                                        "id": 21000,
                                        "params": {
                                            "height": "0x${blockHeight.toString(16)}"
                                        }
                                    }"""
            ).responseString().third.toMono()

    fun getLastBlock(nodeHost: String): Mono<Result<String, FuelError>> = Fuel.post("$nodeHost/api/v3")
        .jsonBody(
            """{
                                "jsonrpc": "2.0",
                                "method": "icx_getLastBlock",
                                "id": 1
                            }"""
        ).responseString().third.toMono()

    fun getBlockAndTransactionResults(blockHeight: Int, fetchSize: Int): Mono<Result<String, FuelError>> =
        Fuel.post("${chainConfig.sNodeHost}/api/v3")
            .jsonBody(
                """{
                                          "jsonrpc":"2.0",
                                          "method":"icx_getBlocksAndTransactionResults",
                                          "params":{
                                            "blockHeight":"0x${blockHeight.toString(16)}",
                                            "length": "0x${fetchSize.toString(16)}"
                                          },
                                          "id":1
                                        }
                                        """
            ).responseString().third.toMono()
}