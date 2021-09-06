package com.wesleydonk.update.internal.controller

import com.wesleydonk.update.DownloadUrlMissingException
import com.wesleydonk.update.Fetcher
import com.wesleydonk.update.Parser
import com.wesleydonk.update.Storage
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.models.fakeVersion
import com.wesleydonk.update.internal.models.fakeVersionResult
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ControllerTest {

    private val fetcher = mockk<Fetcher>()
    private val parser = mockk<Parser>()
    private val storage = mockk<Storage>()
    private val downloadManager = mockk<SystemDownloadManager>()

    private lateinit var controller: Controller

    @Before
    fun setUp() {
        controller = DefaultController(
            fetcher,
            parser,
            storage,
            downloadManager
        )
        coEvery { storage.get() } returns null
        coEvery { storage.deleteAll() } returns Unit
    }

    @Test
    fun `No version is stored when no update is available`() {
        coEvery { fetcher.latestVersionResult() } returns null

        runBlocking {
            controller.execute()
        }

        coVerifyOrder {
            storage.deleteAll()
            fetcher.latestVersionResult()
        }

        coVerify(exactly = 0) {
            parser.parse(any())
            storage.insert(any())
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `No version is stored when an error is thrown while fetching`() {
        val exception = IllegalStateException()

        coEvery { fetcher.latestVersionResult() } throws exception

        runBlocking {
            controller.execute()
        }

        coVerifyOrder {
            storage.deleteAll()
            fetcher.latestVersionResult()
        }

        coVerify(exactly = 0) {
            parser.parse(any())
            storage.insert(any())
        }
    }

    @Test(expected = DownloadUrlMissingException::class)
    fun `No version is stored when an error is thrown while parsing`() {
        val result = fakeVersionResult()

        coEvery { fetcher.latestVersionResult() } returns result
        every { parser.parse(result) } throws DownloadUrlMissingException()

        runBlocking {
            controller.execute()
        }

        coVerifyOrder {
            storage.deleteAll()
            fetcher.latestVersionResult()
            parser.parse(any())
        }

        coVerify(exactly = 0) {
            storage.insert(any())
        }
    }

    @Test
    fun `Version is stored when an error is thrown while parsing`() {
        val result = fakeVersionResult()
        val version = fakeVersion()

        coEvery { fetcher.latestVersionResult() } returns result
        every { parser.parse(result) } returns version

        runBlocking {
            controller.execute()
        }

        coVerifyOrder {
            storage.deleteAll()
            fetcher.latestVersionResult()
            parser.parse(result)
            storage.insert(version)
        }
    }
}