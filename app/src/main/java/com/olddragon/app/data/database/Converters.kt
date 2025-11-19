package com.olddragon.app.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * TypeConverters para Room Database
 * Converte tipos complexos em formatos que o Room pode armazenar
 */
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromLinkedHashMapStringByte(value: LinkedHashMap<String, Byte>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLinkedHashMapStringByte(value: String): LinkedHashMap<String, Byte> {
        val type = object : TypeToken<LinkedHashMap<String, Byte>>() {}.type
        return gson.fromJson(value, type) ?: LinkedHashMap()
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromLongList(value: List<Long>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLongList(value: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}
