package com.example.jarvishome.data.base

interface Mapper <M, P> {
    fun map(model: M): P
}