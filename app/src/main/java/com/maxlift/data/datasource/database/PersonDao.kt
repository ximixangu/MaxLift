package com.maxlift.data.datasource.database

import androidx.room.Dao
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

    @Query("DELETE FROM person WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT id, name FROM person ORDER by name COLLATE NOCASE ASC")
    fun getAll(): List<PersonEntity>

    @Query("SELECT id, name FROM person WHERE id = :id")
    fun getPersonById(id: Int): PersonEntity?
}