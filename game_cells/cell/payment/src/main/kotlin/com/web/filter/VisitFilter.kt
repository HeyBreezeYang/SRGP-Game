package com.web.filter

import org.slf4j.LoggerFactory
import java.util.*
import javax.servlet.*
import javax.servlet.annotation.WebFilter

/**
 * Created by DJL on 2018/3/5.
 * @Description
 */
@WebFilter(urlPatterns = arrayOf("/pay"))
class VisitFilter:Filter{
    private val log = LoggerFactory.getLogger(VisitFilter::class.java)
    private val TIME_LOCK = WeakHashMap<String, Long>()
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val ip = request?.remoteHost
        val t = TIME_LOCK[ip]
        log.info("ip :" + ip)
        val now = System.currentTimeMillis()
        if (t != null && now - t <= 1200) {
            return
        }
        TIME_LOCK[ip]= now
        chain?.doFilter(request, response)
    }

    override fun init(filterConfig: FilterConfig?) {
        log.info("my filter create!~")
    }

    override fun destroy() {
        TODO("my filter destroy")
    }
}