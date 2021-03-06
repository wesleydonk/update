package com.wesleydonk.update.internal.controller

import com.wesleydonk.update.DataStore
import com.wesleydonk.update.Fetcher
import com.wesleydonk.update.Parser
import com.wesleydonk.update.internal.managers.SystemDownloadManager
import com.wesleydonk.update.internal.models.fakeVersion
import com.wesleydonk.update.internal.models.fakeVersionResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ControllerTest {

    private val fetcher = mockk<Fetcher>()
    private val parser = mockk<Parser>()
    private val dataStore = mockk<DataStore>()
    private val downloadManager = mockk<SystemDownloadManager>()

    private lateinit var controller: Controller

    @Before
    fun setUp() {
        controller = DefaultController(
            fetcher,
            dataStore,
            downloadManager,
            parser,
        )
        coEvery { dataStore.get() } returns null
        coEvery { dataStore.deleteAll() } returns Unit
        coEvery { dataStore.insert(any()) } returns Unit
    }

    @Test
    fun `No version is stored when no update is available`() {
        coEvery { fetcher.getLatestVersion() } returns null

        runBlocking {
            controller.execute()
        }

        coVerifyOrder {
            dataStore.deleteAll()
            fetcher.getLatestVersion()
        }

        coVerify(exactly = 0) {
            parser.parse(any())
            dataStore.insert(any())
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `No version is stored when an error is thrown while fetching`() {
        val exception = IllegalStateException()

        coEvery { fetcher.getLatestVersion() } throws exception

        runBlocking {
            controller.execute()
        }

        coVerifyOrder {
            dataStore.deleteAll()
            fetcher.getLatestVersion()
        }

        coVerify(exactly = 0) {
            parser.parse(any())
            dataStore.insert(any())
        }
    }

    @Test
    fun `Version is stored when parsing succesfully`() {
        val result = fakeVersionResult()
        val version = fakeVersion()

        coEvery { fetcher.getLatestVersion() } returns result
        every { parser.parse(result) } returns version

        runBlocking {
            controller.execute()
        }

        coVerifyOrder {
            dataStore.deleteAll()
            fetcher.getLatestVersion()
            parser.parse(result)
            dataStore.insert(version)
        }
    }
}
