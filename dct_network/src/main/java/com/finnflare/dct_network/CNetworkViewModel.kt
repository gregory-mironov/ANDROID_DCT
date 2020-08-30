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
import com.finnflare.dct_network.classes.goods.CGoodsRequest
import com.finnflare.dct_network.classes.goods.Request
import com.finnflare.dct_network.classes.leftovers.CLeftoversRequest
import com.finnflare.dct_network.classes.marking_codes.CMarkingCodeRequest
import com.finnflare.dct_network.classes.shops.CShopsRequest
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ObsoleteCoroutinesApi
class CNetworkViewModel(application: Application) : AndroidViewModel(application), KoinComponent {
    private val netDispatcher: CoroutineDispatcher = newSingleThreadContext("NetCoroutine")
    private val token = "dc93c239cfcb11d8d0789183204e35462ca9b69c"

    private val database by inject<CDatabaseViewModel>()

    val authSuccessful = MutableLiveData<Boolean>()

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

    suspend fun getLocationsList() {
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
                return
            }

            response.body()?.response?.data?.let { data ->
                database.insertShops(mutableListOf<Shop>().apply {
                    addAll(data.map { Shop(it.id, it.name, it.httpRef) })
                })
            }

        } catch (e: UnknownHostException) {
        } catch (e: SocketTimeoutException) {
        } catch (e: Exception) {
        }
    }

    suspend fun getShopDocs(date: String, shopId: String) {
        try {
            val docs = getDocsList(date, shopId)
            val mc = getMCList(shopId)
            val (goods, states) = getGoodsList(shopId)

            database.insertNewShopInfo(docs, mc, goods, states)

        } catch (e: UnknownHostException) {
        } catch (e: SocketTimeoutException) {
        } catch (e: Exception) {
        }
    }

    private suspend fun getDocsList(date: String, shopId: String): List<Doc> {
        val docsRequest = CDocsRequest(
            header = Header(
                method = "tsd.get.docs",
                token = token
            ),
            request = com.finnflare.dct_network.classes.docs.Request(
                docDate = date,
                shopId = shopId
            )
        )

        val docsResponse = CNetworkService.Api.getDocsList(docsRequest)

        if (!docsResponse.isSuccessful) {
            throw Error("")
        }

        if (docsResponse.body()?.response?.error != false)
            throw Error("")

        return docsResponse.body()?.response?.data!!.map { doc ->
            Doc(
                doc.auditor, doc.basis,
                doc.comment, doc.docDate,
                doc.docNumber, doc.docSum ?: 0.0,
                doc.id, doc.priceType, doc.qty,
                doc.qtyFact ?: 0, doc.storeId
            )
        }

    }

    private suspend fun getMCList(shopId: String, limit: Int = 2000): List<MarkingCode> {
        val result = mutableListOf<MarkingCode>()

        var page = 1
        while (true) {

            val mcRequest = CMarkingCodeRequest(
                header = Header(
                    method = "tsd.get.marking_codes",
                    token = token
                ),
                request = com.finnflare.dct_network.classes.marking_codes.Request(
                    limit = limit,
                    page = page,
                    shopId = shopId
                )
            )

            val mcResponse = CNetworkService.Api.getMarkingCodesList(mcRequest)

            if (!mcResponse.isSuccessful)
                break

            if (mcResponse.body()?.response?.error != false)
                break

            result.addAll(mcResponse.body()?.response?.markingCodes!!.markingCodes.map {
                MarkingCode(
                    it.gtin,
                    it.guid,
                    it.rfid ?: "",
                    it.sn ?: "",
                    it.state
                )
            })

            if (mcResponse.body()?.response?.markingCodes!!.markingCodes.size < limit)
                break

            page += 1
        }

        return result
    }

    private suspend fun getGoodsList(
        shopId: String,
        limit: Int = 2000
    ): Pair<List<Good>, List<State>> {
        val goods = mutableListOf<Good>()
        val states = mutableListOf<State>()

        var page = 1
        while (true) {
            val mcRequest = CGoodsRequest(
                header = Header(
                    method = "tsd.get.goods",
                    token = token
                ),
                request = Request(
                    limit = limit,
                    page = page,
                    shopId = shopId
                )
            )

            val goodsResponse = CNetworkService.Api.getGoodsList(mcRequest)

            if (!goodsResponse.isSuccessful)
                throw Error("")

            if (goodsResponse.body()?.response?.error != false)
                throw Error("")

            goods.addAll(goodsResponse.body()?.response!!.goods!!.goods.map {
                Good(
                    it.color.toString(),
                    it.guid,
                    it.model.toString(),
                    it.name.toString(),
                    it.size.toString()
                )
            })

            states.addAll(
                goodsResponse.body()?.response!!.states.states.map {
                    State(it.state, it.stateName)
                })

            if (goodsResponse.body()?.response!!.goods!!.goods.size < limit)
                break

            page += 1
        }

        return Pair(goods, states)
    }

    suspend fun getLeftoversList(docId: String) {
        try {
            val request = CLeftoversRequest(
                header = Header(
                    method = "tsd.get.leftovers",
                    token = token
                ),
                request = com.finnflare.dct_network.classes.leftovers.Request(
                    docId = docId
                )
            )

            val response = CNetworkService.Api.getLeftoversList(request)

            if (!response.isSuccessful) {
                throw Error("")
            }

            if (response.body()?.response?.error != false)
                throw Error("")

            database.insertNewPlanLeftovers(docId, mutableListOf<Leftover>().apply {
                addAll(
                    response.body()?.response?.leftovers!!.leftovers.map { lo ->
                        Leftover(
                            lo.docGuid, lo.docNumber,
                            lo.gtin, lo.guid, lo.qtyin,
                            lo.rfid ?: "", lo.sn ?: "",
                            lo.state, lo.storeGuid
                        )
                    }
                )
            })
        } catch (e: UnknownHostException) {
        } catch (e: SocketTimeoutException) {
        } catch (e: Exception) {
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
                val docs =
                    mutableListOf<com.finnflare.dct_network.classes.actual_docs.Doc>().apply {
                        database.getDocsList().map {
                            getDocToSend(it.mStoreId, it.mId)
                        }
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

        val rfid = mutableListOf<RFIDItems>().apply {
            addAll(database.getRFIDScanResults(docId).map {
                RFIDItems(
                    guid = it.guid,
                    gtin = it.gtin,
                    sn = it.sn,
                    rfid = it.rfid,
                    state = it.state,
                    qtyout = it.qtyout
                )
            })
        }

        val barcode = mutableListOf<BarcodeItems>().apply {
            addAll(database.getBarcodeScanResults(docId).map {
                BarcodeItems(
                    guid = it.guid,
                    gtin = it.gtin,
                    sn = it.sn,
                    state = it.state,
                    qtyout = it.qtyout
                )
            })
        }

        return com.finnflare.dct_network.classes.actual_docs.Doc(
            id = docId,
            rfidItemsList = rfid,
            barcodeItemsList = barcode,
            storeId = storeId
        )
    }
}