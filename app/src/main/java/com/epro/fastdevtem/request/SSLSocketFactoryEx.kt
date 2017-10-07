package com.epro.fastdevtem.request

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class SSLSocketFactoryEx : SSLSocketFactory {

    private var m_ctx: SSLContext? = null

    private var m_ciphers: Array<String>? = null
    var defaultProtocols: Array<String>? = null
        private set

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    constructor() {
        //        initSSLSocketFactoryEx(null,null,null);
        initSSLSocketFactoryEx(null, arrayOf(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
        }), SecureRandom())
    }

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    constructor(km: Array<KeyManager>, tm: Array<TrustManager>, random: SecureRandom) {
        initSSLSocketFactoryEx(km, tm, random)
    }

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    constructor(ctx: SSLContext) {
        initSSLSocketFactoryEx(ctx)
    }

    override fun getDefaultCipherSuites(): Array<String>? {
        return m_ciphers
    }

    override fun getSupportedCipherSuites(): Array<String>? {
        return m_ciphers
    }

    fun getSupportedProtocols(): Array<String>? {
        return defaultProtocols
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        val factory = m_ctx!!.socketFactory
        val ss = factory.createSocket(s, host, port, autoClose) as SSLSocket

        ss.enabledProtocols = defaultProtocols
        ss.enabledCipherSuites = m_ciphers

        return ss
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket {
        val factory = m_ctx!!.socketFactory
        val ss = factory.createSocket(address, port, localAddress, localPort) as SSLSocket

        ss.enabledProtocols = defaultProtocols
        ss.enabledCipherSuites = m_ciphers

        return ss
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket {
        val factory = m_ctx!!.socketFactory
        val ss = factory.createSocket(host, port, localHost, localPort) as SSLSocket

        ss.enabledProtocols = defaultProtocols
        ss.enabledCipherSuites = m_ciphers

        return ss
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        val factory = m_ctx!!.socketFactory
        val ss = factory.createSocket(host, port) as SSLSocket

        ss.enabledProtocols = defaultProtocols
        ss.enabledCipherSuites = m_ciphers

        return ss
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        val factory = m_ctx!!.socketFactory
        val ss = factory.createSocket(host, port) as SSLSocket

        ss.enabledProtocols = defaultProtocols
        ss.enabledCipherSuites = m_ciphers

        return ss
    }

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    private fun initSSLSocketFactoryEx(km: Array<KeyManager>?, tm: Array<TrustManager>, random: SecureRandom) {
        m_ctx = SSLContext.getInstance("TLS")
        m_ctx!!.init(km, tm, random)

        defaultProtocols = GetProtocolList()
        m_ciphers = GetCipherList()
    }

    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    private fun initSSLSocketFactoryEx(ctx: SSLContext) {
        m_ctx = ctx

        defaultProtocols = GetProtocolList()
        m_ciphers = GetCipherList()
    }

    protected fun GetProtocolList(): Array<String> {
        val preferredProtocols = arrayOf("TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3")
        var availableProtocols: Array<String>? = null

        var socket: SSLSocket? = null

        try {
            val factory = m_ctx!!.socketFactory
            socket = factory.createSocket() as SSLSocket

            availableProtocols = socket.supportedProtocols
            Arrays.sort(availableProtocols!!)
        } catch (e: Exception) {
            return arrayOf("TLSv1")
        } finally {
            if (socket != null) {
                try {
                    socket.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

            }
        }

        val aa = ArrayList<String>()
        for (i in preferredProtocols.indices) {
            val idx = Arrays.binarySearch(availableProtocols!!, preferredProtocols[i])
            if (idx >= 0)
                aa.add(preferredProtocols[i])
        }

        return aa.toTypedArray()
    }

    protected fun GetCipherList(): Array<String> {
        val preferredCiphers = arrayOf(

                // *_CHACHA20_POLY1305 are 3x to 4x faster than existing cipher suites.
                //   http://googleonlinesecurity.blogspot.com/2014/04/speeding-up-and-strengthening-https.html
                // Use them if available. Normative names can be found at (TLS spec depends on IPSec spec):
                //   http://tools.ietf.org/html/draft-nir-ipsecme-chacha20-poly1305-01
                //   http://tools.ietf.org/html/draft-mavrogiannopoulos-chacha-tls-02
                "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305", "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305", "TLS_ECDHE_ECDSA_WITH_CHACHA20_SHA", "TLS_ECDHE_RSA_WITH_CHACHA20_SHA",

                "TLS_DHE_RSA_WITH_CHACHA20_POLY1305", "TLS_RSA_WITH_CHACHA20_POLY1305", "TLS_DHE_RSA_WITH_CHACHA20_SHA", "TLS_RSA_WITH_CHACHA20_SHA",

                // Done with bleeding edge, back to TLS v1.2 and below
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",

                "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256",

                // TLS v1.0 (with some SSLv3 interop)
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA384", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA", "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",

                "TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA", "TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA", "SSL_DH_RSA_WITH_3DES_EDE_CBC_SHA", "SSL_DH_DSS_WITH_3DES_EDE_CBC_SHA",

                // RSA key transport sucks, but they are needed as a fallback.
                // For example, microsoft.com fails under all versions of TLS
                // if they are not included. If only TLS 1.0 is available at
                // the client, then google.com will fail too. TLS v1.3 is
                // trying to deprecate them, so it will be interesteng to see
                // what happens.
                "TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA")

        var availableCiphers: Array<String>? = null

        try {
            val factory = m_ctx!!.socketFactory
            availableCiphers = factory.supportedCipherSuites
            Arrays.sort(availableCiphers!!)
        } catch (e: Exception) {
            return arrayOf("TLS_DHE_DSS_WITH_AES_128_CBC_SHA", "TLS_DHE_DSS_WITH_AES_256_CBC_SHA", "TLS_DHE_RSA_WITH_AES_128_CBC_SHA", "TLS_DHE_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_256_CBC_SHA", "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_EMPTY_RENEGOTIATION_INFO_SCSV")
        }

        val aa = ArrayList<String>()
        for (i in preferredCiphers.indices) {
            val idx = Arrays.binarySearch(availableCiphers, preferredCiphers[i])
            if (idx >= 0)
                aa.add(preferredCiphers[i])
        }

        aa.add("TLS_EMPTY_RENEGOTIATION_INFO_SCSV")

        return aa.toTypedArray()
    }
}