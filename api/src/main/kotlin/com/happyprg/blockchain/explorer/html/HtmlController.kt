/*
 * Copyright (c) 2019 http://happyprg.com all rights reserved. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.happyprg.blockchain.explorer.html

import com.happyprg.blockchain.explorer.config.ChainConfig
import com.happyprg.blockchain.explorer.config.MonitoringConfig
import com.happyprg.blockchain.explorer.router.websocket.OnDemandBlockMonitoringRouter
import org.apache.commons.lang3.StringUtils.isNotBlank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.Arrays.toString

@Controller
class HtmlController(
    val chainConfig: com.happyprg.blockchain.explorer.config.ChainConfig, val monitoringConfig: com.happyprg.blockchain.explorer.config.MonitoringConfig,
    val onDemandBlockMonitoringRouter: com.happyprg.blockchain.explorer.router.websocket.OnDemandBlockMonitoringRouter
) {

    @Autowired
    lateinit var environment: Environment

    @GetMapping("/")
    fun index(
        @RequestParam(required = false) s: String? = null,
        @RequestParam(required = false) c: String? = null,
        @RequestParam(required = false) e: String? = null,
        @RequestParam(required = false) b: String? = null,
        @RequestParam(required = false) t: String? = null,
        model: Model
    ): String {

        with(model) {
            when {
                isNotBlank(s) && s!!.startsWith("http") -> {
                    onDemandBlockMonitoringRouter.sNodeHost = s
                    addAttribute("sNodeHost", onDemandBlockMonitoringRouter.sNodeHost)
                    addAttribute("monitorHost", monitoringConfig.onDemandWebSocketHost)
                }
                else -> {
                    addAttribute("monitorHost", monitoringConfig.staticWebSocketHost)
                    addAttribute("sNodeHost", chainConfig.sNodeHost)
                }
            }

            when {
                isNotBlank(c) && c!!.startsWith("http") -> {
                    onDemandBlockMonitoringRouter.cNodeHost = c
                    addAttribute("cNodeHost", onDemandBlockMonitoringRouter.cNodeHost)
                }
                else -> addAttribute("cNodeHost", chainConfig.cNodeHost)
            }

            addAttribute("showingEventCnt", e ?: monitoringConfig.showingEventCnt)
            addAttribute("showingBlockCnt", b ?: monitoringConfig.showingBlockCnt)
            addAttribute("showingTxCnt", t ?: monitoringConfig.showingTxCnt)

            addAttribute("activeProfiles", toString(environment.activeProfiles))
        }
        return "index"
    }

}
