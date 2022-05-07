package com.sid.assignment.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "catfacts")
data class CatFact(
    val catFactText: String?,
    val catImg: Bitmap?
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
