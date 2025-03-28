package com.maxlift.data.datasource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maxlift.data.model.database.PersonEntity

@Dao
interface PersonDao {
    @Insert
    fun save(personEntity: PersonEntity)

    @Update
    fun update(personEntity: PersonEntity)

    @Delete
    fun delete(personEntity: PersonEntity)

    @Query("SELECT * FROM person ORDER by name ASC")
    fun getAll(): List<PersonEntity>

    @Query("SELECT * FROM person WHERE id = :id")
    fun getPersonById(id: Int): PersonEntity
}