package com.finnflare.dct_network

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.finnflare.dct_network.classes.Header
import com.finnflare.dct_network.classes.actual_docs.CActualDocsRequest
import com.finnflare.dct_network.classes.auth.CAuthRequest
import com.finnflare.dct_network.classes.docs.CDocsRequest
import com.finnflare.dct_network.classes.shops.CShopsRequest
import com.finnflare.dct_network.classes.stocks.CStocksRequest
import com.finnflare.dct_network.classes.stores.CStoresRequest
import com.finnflare.dct_network.classes.users.CUsersRequest
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ObsoleteCoroutinesApi
class CNetworkViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    private val netDispatcher: CoroutineDispatcher = newSingleThreadContext("NetCoroutine")

    private val token = "dc93c239cfcb11d8d0789183204e35462ca9b69c"

    fun getUsersList() {
        CoroutineScope(netDispatcher).launch {
            try {
                val request = CUsersRequest(
                    header = Header(
                        method = "tsd.get.users",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.users.Request()
                )

                val response = CNetworkService.Api.getUsers(request)

                if (!response.isSuccessful) {
                    return@launch
                }
            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun verifyLogin(login: String, password: String) {
        CoroutineScope(netDispatcher).launch {
            try {
                val request = CAuthRequest(
                    header = Header(
                        method = "tsd.Login",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.auth.Request(
                        login = login,
                        password = password
                    )
                )

                val response = CNetworkService.Api.auth(request)

                if (!response.isSuccessful) {
                    return@launch
                }

            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun getShopsList() {
        CoroutineScope(netDispatcher).launch {
            try {
                val request = CShopsRequest(
                    header = Header(
                        method = "tsd.get.shops",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.shops.Request()
                )

                val response = CNetworkService.Api.getShopsList(request)

                if (!response.isSuccessful) {
                    return@launch
                }
            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun getStoresList() {
        CoroutineScope(netDispatcher).launch {
            try {
                val request = CStoresRequest(
                    header = Header(
                        method = "tsd.get.stores",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.stores.Request()
                )

                val response = CNetworkService.Api.getStoresList(request)

                if (!response.isSuccessful) {
                    return@launch
                }
            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun getDocsList(date: String, shopId: String) {
        CoroutineScope(netDispatcher).launch {
            try {
                val request = CDocsRequest(
                    header = Header(
                        method = "tsd.get.docs",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.docs.Request(
                        docDate = date,
                        shopId = shopId
                    )
                )

                val response = CNetworkService.Api.getDocsList(request)

                if (!response.isSuccessful) {
                    return@launch
                }

            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun getStocksList(docId: String) {
        CoroutineScope(netDispatcher).launch {
            try {
                val request = CStocksRequest(
                    header = Header(
                        method = "tsd.get.stocks",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.stocks.Request(
                        docID = docId
                    )
                )

                val response = CNetworkService.Api.getStocksList(request)

                if (!response.isSuccessful) {
                    return@launch
                }

            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun sendActualDocsState() {
        CoroutineScope(netDispatcher).launch {
            try {
                val request = CActualDocsRequest(
                    header = Header(
                        method = "tsd.set.docs.actual",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.actual_docs.Request(
                        docs = listOf()
                    )
                )

                val response = CNetworkService.Api.uploadActualDocs(request)

                if (!response.isSuccessful) {
                    return@launch
                }

            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }
}