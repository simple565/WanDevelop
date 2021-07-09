package com.maureen.wandevelop.network

/**
 * Function:
 * @author lianml
 * Create 2021-07-09
 */
class ApiException(var code: Int, override var message: String) : RuntimeException()