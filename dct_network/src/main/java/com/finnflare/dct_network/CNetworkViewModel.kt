package com.finnflare.dct_network

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_database.insertable_classes.*
import com.finnflare.dct_network.classes.Header
import com.finnflare.dct_network.classes.actual_docs.BarcodeItems
import com.finnflare.dct_network.classes.actual_docs.CActualDocsRequest
import com.finnflare.dct_network.classes.actual_docs.RFIDItems
import com.finnflare.dct_network.classes.auth.CAuthRequest
import com.finnflare.dct_network.classes.docs.CDocsRequest
import com.finnflare.dct_network.classes.shops.CShopsRequest
import com.finnflare.dct_network.classes.stocks.CStocksRequest
import com.finnflare.dct_network.classes.stores.CStoresRequest
import com.finnflare.dct_network.classes.users.CUsersRequest
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ObsoleteCoroutinesApi
class CNetworkViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    private val netDispatcher: CoroutineDispatcher = newSingleThreadContext("NetCoroutine")
    private val token = "dc93c239cfcb11d8d0789183204e35462ca9b69c"

    private val database by inject<CDatabaseViewModel>()

    val authSuccessful = MutableLiveData<Boolean>()

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

                response.body()?.response?.data?.let {
                    val users = mutableListOf<User>()

                    it.forEach {user ->
                        users.add(User(user.id, user.name))
                    }

                    database.insertUsers(users)
                }
            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun checkAuth(login: String, password: String) {
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
                    authSuccessful.postValue(false)
                    return@launch
                }

                if (!response.body()!!.response.error)
                    authSuccessful.postValue(true)

                database.updateUserLastLogin(login, login, password)

                return@launch

            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }

            authSuccessful.postValue(false)
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

                response.body()?.response?.data?.let {
                    val shops = mutableListOf<Shop>()

                    it.forEach { shop ->
                        shops.add(Shop(shop.id, shop.name, shop.httpRef))
                    }

                    database.insertShops(shops)
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

                response.body()?.response?.data?.let {
                    val stores = mutableListOf<Store>()

                    it.forEach { store ->
                        stores.add(Store(
                            store.id,
                            store.name,
                            store.shopId,
                            store.storeType.toString()
                        ))
                    }

                    database.insertStores(stores)
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

                response.body()?.response?.data?.let {
                    val docs = mutableListOf<Doc>()

                    it.forEach { doc ->
                        docs.add(Doc(
                            doc.auditor,
                            doc.basis,
                            doc.comment,
                            doc.docDate,
                            doc.docNumber,
                            doc.docSum,
                            doc.id,
                            doc.priceType,
                            doc.qty,
                            doc.qtyFact,
                            doc.storeId
                        ))
                    }

                    database.insertDocs(docs)
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

                response.body()?.response?.let {
                    val goods = mutableListOf<Good>()
                    it.goods.Goods.forEach {good ->
                        goods.add(
                            Good(
                            good.color,
                            good.guid,
                            good.model,
                            good.name,
                            good.size
                        )
                        )
                    }

                    val leftovers = mutableListOf<Leftover>()
                    it.leftovers.leftovers.forEach {lo ->
                        leftovers.add(
                            Leftover(
                            lo.docGuid,
                            lo.docNumber,lo.gtin,
                            lo.guid,
                            lo.qtyin,
                            lo.rfid.toString(),
                            lo.sn.toString(),
                            lo.state,
                            lo.storeGuid
                        )
                        )
                    }

                    val markingCodes = mutableListOf<MarkingCode>()
                    it.markingCodes.markingCodes.forEach {mc ->
                        markingCodes.add(
                            MarkingCode(
                            mc.gtin,
                            mc.guid,
                            mc.rfid.toString(),
                            mc.sn.toString(),
                            mc.state
                        ))
                    }

                    database.insertStocks(goods, leftovers, markingCodes)
                }
            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun sendActualDocState(docId: String) {
        CoroutineScope(netDispatcher).launch {
            try {
                val storeId = database.getDocInfo(docId).mStoreId

                val request = CActualDocsRequest(
                    header = Header(
                        method = "tsd.set.docs.actual",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.actual_docs.Request(
                        docs = listOf(getDocToSend(storeId, docId))
                    )
                )

                val response = CNetworkService.Api.uploadActualDocs(request)

                if (!response.isSuccessful)
                    return@launch

            } catch (e: UnknownHostException) {
            } catch (e: SocketTimeoutException) {
            } catch (e: Exception) {
            }
        }
    }

    fun sendActualDocsState() {
        CoroutineScope(netDispatcher).launch {
            try {
                val docs = mutableListOf<com.finnflare.dct_network.classes.actual_docs.Doc>()

                database.getDocsList().forEach {
                    docs.add(getDocToSend(it.mStoreId, it.mId))
                }

                val request = CActualDocsRequest(
                    header = Header(
                        method = "tsd.set.docs.actual",
                        token = token
                    ),
                    request = com.finnflare.dct_network.classes.actual_docs.Request(
                        docs = docs
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

    private fun getDocToSend(storeId: String, docId: String):
            com.finnflare.dct_network.classes.actual_docs.Doc {

        val rfid = mutableListOf<RFIDItems>()
        database.getRFIDScanResults(storeId, docId).forEach {
            rfid.add(
                RFIDItems(
                    guid = it.guid,
                    gtin = it.gtin,
                    sn = it.sn,
                    rfid = it.rfid,
                    state = it.state,
                    qtyout = it.qtyout
                )
            )
        }

        val barcode = mutableListOf<BarcodeItems>()
        database.getBarcodeScanResults(storeId, docId).forEach {
            barcode.add(
                BarcodeItems(
                    guid = it.guid,
                    gtin = it.gtin,
                    sn = it.sn,
                    state = it.state,
                    qtyout = it.qtyout
                )
            )
        }

        return com.finnflare.dct_network.classes.actual_docs.Doc(
            id = docId,
            rfidItemsList = rfid,
            barcodeItemsList = barcode,
            storeId = storeId
        )
    }
}