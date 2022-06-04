package io.github.sgpublic.biliturbo.module

class ApiModule {
    companion object {
        val TS: Long = System.currentTimeMillis() / 1000
        val TS_STR: String = (System.currentTimeMillis() / 1000).toString()
        val TS_FULL: Long = System.currentTimeMillis()
        val TS_FULL_STR: String = System.currentTimeMillis().toString()
    }
}