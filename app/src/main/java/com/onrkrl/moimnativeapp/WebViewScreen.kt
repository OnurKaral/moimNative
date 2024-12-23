import android.annotation.SuppressLint
import android.webkit.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.onrkrl.moimnativeapp.WebViewParams

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(baseURL: String, params: WebViewParams) {
    AndroidView(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                // ✅ WebView Settings
                settings.apply {
                    javaScriptEnabled = true // Enable JavaScript
                    domStorageEnabled = true // Enable DOM Storage API
                    allowFileAccess = false // Disable file access for better security
                    cacheMode = WebSettings.LOAD_NO_CACHE // Avoid caching for fresh data
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // Allow mixed content (HTTP + HTTPS)
                    userAgentString =
                        "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Mobile Safari/537.36"

                    // ✅ Hardware Acceleration for WebGL
                    loadsImagesAutomatically = true
                }

                // ✅ Ensure WebGL uses hardware acceleration
                setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

                // ✅ Enable WebView debugging for development
                WebView.setWebContentsDebuggingEnabled(true)

                // ✅ WebViewClient for page handling
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        println("✅ Page Loaded: $url")

                        // ✅ Pass Parameters via JavaScript Bridge
                        val script = """
                            (function() {
                                window.token = '${escapeJavaScriptString(params.token)}';
                                window.msisdn = '${escapeJavaScriptString(params.msisdn)}';
                            })();
                        """.trimIndent()

                        view?.evaluateJavascript(script) { result ->
                            println("✅ JS Injection Result: $result")
                        }
                    }

                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        println("🔗 Navigating to: ${request?.url}")
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }

                // ✅ WebChromeClient for JavaScript Console Logs
                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        consoleMessage?.let {
                            println("📝 JS Log: ${it.message()} [${it.sourceId()}:${it.lineNumber()}]")
                        }
                        return super.onConsoleMessage(consoleMessage)
                    }

                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        println("⏳ Loading Progress: $newProgress%")
                    }
                }

                // ✅ Load URL
                loadUrl(baseURL)
            }
        },

        // ✅ Update Logic
        update = { webView ->
            if (webView.url != baseURL) {
                println("🔄 Reloading URL: $baseURL")
                webView.loadUrl(baseURL)
            } else {
                println("🔄 Refreshing Current Page")
                webView.reload()
            }
        }
    )
}

/**
 * Escape JavaScript String to prevent injection attacks
 */
fun escapeJavaScriptString(input: String?): String {
    return input?.replace("'", "\\'")?.replace("\n", "\\n") ?: ""
}
