/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.explorer.block

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class BlockHandler(val blockService: com.happyprg.blockchain.explorer.block.BlockService) {
    fun getLastBlock(nodeHost: String): Mono<Result<String, FuelError>> = blockService.getLastBlock(nodeHost)
//        .log("called LastBlock")
}
