package com.maxlift.domain.usecase.person

import com.maxlift.domain.model.Person
import com.maxlift.domain.repository.IMyRepository

class EditPersonUseCase(
    private val myRepository: IMyRepository
) {
    suspend operator fun invoke(person: Person) {
        myRepository.updatePerson(person)
    }
}