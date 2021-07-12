package com.wesleydonk.update.internal.strategy

import com.wesleydonk.update.Strategy
import com.wesleydonk.update.TryConfig
import com.wesleydonk.update.internal.controller.DefaultController

@Deprecated("Not sure what to do with this")
class RequestOnlyStrategy(
    config: TryConfig
) : Strategy {

    private val controller = DefaultController.ofConfig(config)

    override suspend fun execute() {
        controller.execute()
    }
}