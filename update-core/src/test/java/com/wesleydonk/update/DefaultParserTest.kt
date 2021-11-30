package com.wesleydonk.update

import org.junit.Test

class DefaultParserTest {

    private val parser: Parser = DefaultParser()

    @Test
    fun `Given version when download url is available`() {
        val url = "simple url"
        val id = "update"
        val input = CheckVersionResult(id, url)
        val result = parser.parse(input)

        assert(result.id == id)
        assert(result.downloadUrl == url)
        assert(result.downloadId == null)
    }
}
