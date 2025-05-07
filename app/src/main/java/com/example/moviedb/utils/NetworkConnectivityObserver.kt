package com.example.moviedb.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

class NetworkConnectivityObserver(
    context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(ConnectivityObserver.Status.Available)
            }

            override fun onLost(network: Network) {
                trySend(ConnectivityObserver.Status.Lost)
            }

            override fun onUnavailable() {
                trySend(ConnectivityObserver.Status.Unavailable)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                trySend(ConnectivityObserver.Status.Losing)
            }
        }
        //“Tell me when a network becomes available or unavailable — but only if it provides internet access.”
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        //“Start watching the internet connectivity based on this request. Use this callback to tell me what happens.”
        connectivityManager.registerNetworkCallback(request, callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
