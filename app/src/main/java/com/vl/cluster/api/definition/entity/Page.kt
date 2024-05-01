package com.vl.cluster.api.definition.entity

class Page<K, V>(val items: List<V>, val nextKey: K?) {
    override fun hashCode() = nextKey.hashCode()
    override fun toString() = nextKey.toString()
    override fun equals(other: Any?) = other?.takeIf { it is Page<*, *> }
        ?.let { (it as Page<*, *>).nextKey == nextKey } == true
}