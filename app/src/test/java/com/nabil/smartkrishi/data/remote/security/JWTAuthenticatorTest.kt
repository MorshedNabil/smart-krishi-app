package com.nabil.smartkrishi.data.remote.security

import com.nabil.smartkrishi.data.security.JWTAuthenticator
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class JWTAuthenticatorTest {

    private var testToken: String? = "test_jwt_access_token_12345"
    private val authenticator = JWTAuthenticator(tokenProvider = { testToken })

    @Test
    fun isLoginEndpoint_identifiesLoginPaths() {
        assertTrue(authenticator.isLoginEndpoint("/auth/accounts/login/"))
        assertTrue(authenticator.isLoginEndpoint("/api/v1/login"))
        assertFalse(authenticator.isLoginEndpoint("/auth/accounts/me/"))
    }

    @Test
    fun isSignupEndpoint_identifiesSignupAndOtpPaths() {
        assertTrue(authenticator.isSignupEndpoint("/auth/accounts/register/"))
        assertTrue(authenticator.isSignupEndpoint("/api/v1/register"))
        assertTrue(authenticator.isSignupEndpoint("/auth/accounts/send-otp/"))
        assertFalse(authenticator.isSignupEndpoint("/auth/accounts/me/"))
    }

    @Test
    fun isRefreshTokenEndpoint_identifiesRefreshPaths() {
        assertTrue(authenticator.isRefreshTokenEndpoint("/auth/accounts/token/refresh/"))
        assertTrue(authenticator.isRefreshTokenEndpoint("/api/token/refresh"))
        assertFalse(authenticator.isRefreshTokenEndpoint("/auth/accounts/me/"))
    }

    @Test
    fun shouldSkipToken_returnsTrueForPublicAuthEndpoints() {
        assertTrue(authenticator.shouldSkipToken("/auth/accounts/login/"))
        assertTrue(authenticator.shouldSkipToken("/auth/accounts/register/"))
        assertTrue(authenticator.shouldSkipToken("/auth/accounts/send-otp/"))
        assertTrue(authenticator.shouldSkipToken("/auth/accounts/token/refresh/"))
        assertFalse(authenticator.shouldSkipToken("/auth/accounts/me/"))
    }

    @Test
    fun intercept_loginEndpoint_doesNotAttachToken() {
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/auth/accounts/login/")
            .build()

        val fakeChain = FakeChain(request)
        authenticator.intercept(fakeChain)

        val processedRequest = fakeChain.lastProcessedRequest
        assertNull("Authorization header should be omitted for login endpoint", processedRequest?.header("Authorization"))
    }

    @Test
    fun intercept_registerEndpoint_doesNotAttachToken() {
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/auth/accounts/register/")
            .build()

        val fakeChain = FakeChain(request)
        authenticator.intercept(fakeChain)

        val processedRequest = fakeChain.lastProcessedRequest
        assertNull("Authorization header should be omitted for register endpoint", processedRequest?.header("Authorization"))
    }

    @Test
    fun intercept_sendOtpEndpoint_doesNotAttachToken() {
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/auth/accounts/send-otp/")
            .build()

        val fakeChain = FakeChain(request)
        authenticator.intercept(fakeChain)

        val processedRequest = fakeChain.lastProcessedRequest
        assertNull("Authorization header should be omitted for send-otp endpoint", processedRequest?.header("Authorization"))
    }

    @Test
    fun intercept_refreshTokenEndpoint_doesNotAttachToken() {
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/auth/accounts/token/refresh/")
            .build()

        val fakeChain = FakeChain(request)
        authenticator.intercept(fakeChain)

        val processedRequest = fakeChain.lastProcessedRequest
        assertNull("Authorization header should be omitted for refresh token endpoint", processedRequest?.header("Authorization"))
    }

    @Test
    fun intercept_protectedEndpoint_attachesTokenWhenPresent() {
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/auth/accounts/me/")
            .build()

        val fakeChain = FakeChain(request)
        authenticator.intercept(fakeChain)

        val processedRequest = fakeChain.lastProcessedRequest
        assertEquals("Bearer test_jwt_access_token_12345", processedRequest?.header("Authorization"))
    }

    @Test
    fun intercept_protectedEndpoint_doesNotAttachTokenWhenTokenIsNull() {
        testToken = null
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/auth/accounts/me/")
            .build()

        val fakeChain = FakeChain(request)
        authenticator.intercept(fakeChain)

        val processedRequest = fakeChain.lastProcessedRequest
        assertNull("Authorization header should be null when token is null", processedRequest?.header("Authorization"))
    }
}

private class FakeChain(private val request: Request) : Interceptor.Chain {
    var lastProcessedRequest: Request? = null

    override fun request(): Request = request

    override fun proceed(request: Request): Response {
        lastProcessedRequest = request
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(null)
            .build()
    }

    override fun connection(): okhttp3.Connection? = null
    override fun call(): okhttp3.Call = throw NotImplementedError()
    override fun connectTimeoutMillis(): Int = 10000
    override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
    override fun readTimeoutMillis(): Int = 10000
    override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
    override fun writeTimeoutMillis(): Int = 10000
    override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
}
