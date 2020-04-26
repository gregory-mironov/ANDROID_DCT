package com.finnflare.dct_network

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.finnflare.dct_network.classes.Header
import com.finnflare.dct_network.classes.actual_docs.CActualDocsRequest
import com.finnflare.dct_network.classes.auth.CAuthRequest
import com.finnflare.dct_network.classes.docs.CDocsRequest
import com.finnflare.dct_network.classes.shops.CShopsRequest
import com.finnflare.dct_network.classes.stocks.CStocksRequest
import com.finnflare.dct_network.classes.users.CUsersRequest
import org.koin.core.KoinComponent

class CNetworkViewModel(application: Application): AndroidViewModel(application), KoinComponent {

    fun getUsersList() {
        val request = CUsersRequest(
            header = Header(
                method = "",
                token = ""
            ),
            request = com.finnflare.dct_network.classes.users.Request()
        )
    }

    fun verifyLogin(login: String, password: String) {
        val request = CAuthRequest(
            header = Header(
                method = "",
                token = ""
            ),
            request = com.finnflare.dct_network.classes.auth.Request(
                login = login,
                password = password
            )
        )
    }

    fun getShopsList() {
        val request = CShopsRequest(
            header = Header(
                method = "",
                token = ""
            ),
            request = com.finnflare.dct_network.classes.shops.Request()
        )
    }

    fun getDocsList(date: String, shopId: String) {
        val request = CDocsRequest(
            header = Header(
                method = "",
                token = ""
            ),
            request = com.finnflare.dct_network.classes.docs.Request(
                docDate = date,
                shopId = shopId
            )
        )
    }

    fun getStocksList(docId: String) {
        val request = CStocksRequest(
            header = Header(
                method = "",
                token = ""
            ),
            request = com.finnflare.dct_network.classes.stocks.Request(
                docID = docId
            )
        )
    }

    fun sendActualDocsState() {
        val request = CActualDocsRequest(
            header = Header(
                method = "",
                token = ""
            ),
            request = com.finnflare.dct_network.classes.actual_docs.Request(
                docs = listOf()
            )
        )
    }
}