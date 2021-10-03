package com.wesleydonk.update

import org.junit.Test

class DefaultParserTest {

    private val parser: Parser = DefaultParser()

    @Test(expected = DownloadUrlMissingException::class)
    fun `Given exception when no download url is available`() {
        val input = CheckVersionResult(
            id = "update",
            parameters = emptyMap(),
        )
        parser.parse(input)
    }

    @Test
    fun `Given version when download url is available`() {
        val url = "simple url"
        val id = "update"
        val input = CheckVersionResult(
            id = id,
            parameters = mapOf(
                "download_url" to url
            ),
        )
        val result = parser.parse(input)

        assert(result.id == id)
        assert(result.downloadUrl == url)
        assert(result.downloadId == null)
    }
}