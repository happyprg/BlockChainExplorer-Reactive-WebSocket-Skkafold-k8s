/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.explorer.util

import com.github.kittinunf.fuel.core.FuelError
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono.just

inline fun <reified T : Any> T.toServerResponse() = ServerResponse.ok()
    .contentType(APPLICATION_JSON_UTF8)
    .body(just(this))

inline fun <reified T : com.github.kittinunf.result.Result<String, FuelError>> T.toFuelResponse() =
    when (this) {
        is com.github.kittinunf.result.Result.Success<*, *> -> {
            get().toServerResponse()
        }
        is com.github.kittinunf.result.Result.Failure<*, *> -> {
            getException().localizedMessage.toServerResponse()
        }
        else -> toServerResponse()
    }

inline fun <reified T : com.github.kittinunf.result.Result<String, FuelError>> T.toResult(): String =
    when (this) {
        is com.github.kittinunf.result.Result.Success<*, *> -> {
            get()
        }
        is com.github.kittinunf.result.Result.Failure<*, *> -> {
            getException().localizedMessage
        }
        else -> ""
    }
