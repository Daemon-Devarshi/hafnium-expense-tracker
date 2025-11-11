package com.manakinai.hafnium

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform