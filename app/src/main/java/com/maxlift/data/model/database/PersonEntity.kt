package com.maxlift.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxlift.domain.model.Person

@Entity(tableName = "person")
data class PersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String
) {
  companion object {
      fun fromPersonDomain(person: Person): PersonEntity {
          return PersonEntity(id = person.id, name = person.name)
      }
  }
}

fun PersonEntity.toPersonDomain(): Person {
    return Person(id, name)
}